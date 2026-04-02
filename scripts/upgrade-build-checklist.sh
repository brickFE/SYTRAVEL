#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 2 ]]; then
  echo "Usage: $0 <precheck-report-file> <output-markdown-file> [owners-map-file] [--fail-on-unmapped]"
  exit 2
fi

REPORT_FILE="$1"
OUT_FILE="$2"
OWNERS_MAP_FILE="config/upgrade-owners.map"
FAIL_ON_UNMAPPED=0
for arg in "${@:3}"; do
  if [[ "$arg" == "--fail-on-unmapped" ]]; then
    FAIL_ON_UNMAPPED=1
  else
    OWNERS_MAP_FILE="$arg"
  fi
done

if [[ ! -f "$REPORT_FILE" ]]; then
  echo "[upgrade-build-checklist] ERROR: report file not found: $REPORT_FILE"
  exit 2
fi

get_value() {
  local key="$1"
  (grep -E "^${key}=" "$REPORT_FILE" || true) | head -n 1 | cut -d= -f2-
}

risk_level_for_hotspot() {
  local hotspot="$1"
  case "$hotspot" in
    src/main/java/com/sy/travel/service/*) echo "HIGH" ;;
    src/main/java/com/sy/travel/rest/*) echo "MEDIUM" ;;
    src/main/java/com/sy/travel/*) echo "MEDIUM" ;;
    src/test/java/*) echo "LOW" ;;
    *) echo "MEDIUM" ;;
  esac
}

owner_for_hotspot() {
  local hotspot="$1"
  if [[ ! -f "$OWNERS_MAP_FILE" ]]; then
    echo "TBD"
    return
  fi
  while IFS= read -r line; do
    [[ -z "$line" || "$line" =~ ^# ]] && continue
    pattern="${line%%=*}"
    owner="${line#*=}"
    if [[ "$hotspot" == $pattern ]]; then
      echo "$owner"
      return
    fi
  done < "$OWNERS_MAP_FILE"
  echo "TBD"
}

STRICT_MODE="$(get_value upgrade_precheck_strict_mode)"
STRICT_FAILED="$(get_value upgrade_precheck_strict_failed)"
TARGET_PHASE="$(get_value target_phase)"
JAVA_MAJOR="$(get_value java_major)"
POM_JAVA_VERSION="$(get_value pom_java_version)"
BOOT_PARENT="$(get_value spring_boot_parent)"
RUNTIME_JAVA_OK="$(get_value runtime_java_ok)"
POM_JAVA_OK="$(get_value pom_java_ok)"
BOOT_PARENT_OK="$(get_value spring_boot_parent_ok)"
JAVAX_COUNT="$(get_value javax_import_occurrences)"
JAVAX_SERVLET_COUNT="$(get_value javax_servlet_count)"
JAVAX_VALIDATION_COUNT="$(get_value javax_validation_count)"
JAVAX_PERSISTENCE_COUNT="$(get_value javax_persistence_count)"
JAVAX_WEBSOCKET_COUNT="$(get_value javax_websocket_count)"
JAVAX_ANNOTATION_COUNT="$(get_value javax_annotation_count)"
JAVAX_OTHER_COUNT="$(get_value javax_other_count)"

mkdir -p "$(dirname "$OUT_FILE")"
UNMAPPED_COUNT=0

{
  echo "# Upgrade Execution Checklist (Auto-generated)"
  echo
  echo "- strict mode: \`${STRICT_MODE:-unknown}\`"
  echo "- strict failed: \`${STRICT_FAILED:-unknown}\`"
  echo "- target phase: \`${TARGET_PHASE:-unknown}\`"
  echo "- runtime java major: \`${JAVA_MAJOR:-unknown}\`"
  echo "- pom java.version: \`${POM_JAVA_VERSION:-unknown}\`"
  echo "- spring boot parent: \`${BOOT_PARENT:-unknown}\`"
  echo "- phase checks: runtime_java_ok=\`${RUNTIME_JAVA_OK:-unknown}\`, pom_java_ok=\`${POM_JAVA_OK:-unknown}\`, spring_boot_parent_ok=\`${BOOT_PARENT_OK:-unknown}\`"
  echo "- javax import occurrences: \`${JAVAX_COUNT:-unknown}\`"
  echo "- javax category breakdown: servlet=\`${JAVAX_SERVLET_COUNT:-0}\`, validation=\`${JAVAX_VALIDATION_COUNT:-0}\`, persistence=\`${JAVAX_PERSISTENCE_COUNT:-0}\`, websocket=\`${JAVAX_WEBSOCKET_COUNT:-0}\`, annotation=\`${JAVAX_ANNOTATION_COUNT:-0}\`, other=\`${JAVAX_OTHER_COUNT:-0}\`"
  echo
  echo "## Phase 0 - Recommended Migration Batches"
  if [[ "${JAVAX_VALIDATION_COUNT:-0}" -gt 0 || "${JAVAX_ANNOTATION_COUNT:-0}" -gt 0 ]]; then
    echo "- [ ] Batch A (High ROI): validation + annotation migration (\`javax.validation.*\` / \`javax.annotation.*\`) [owner: backend-api]"
  fi
  if [[ "${JAVAX_PERSISTENCE_COUNT:-0}" -gt 0 ]]; then
    echo "- [ ] Batch B (Data Layer): persistence migration (\`javax.persistence.*\`) [owner: backend-core]"
  fi
  if [[ "${JAVAX_SERVLET_COUNT:-0}" -gt 0 || "${JAVAX_WEBSOCKET_COUNT:-0}" -gt 0 ]]; then
    echo "- [ ] Batch C (Protocol Layer): servlet/websocket migration (\`javax.servlet.*\` / \`javax.websocket.*\`) [owner: backend-platform]"
  fi
  if [[ "${JAVAX_OTHER_COUNT:-0}" -gt 0 ]]; then
    echo "- [ ] Batch D (Misc): remaining \`javax.*\` imports [owner: backend-core]"
  fi
  echo
  echo "## Phase 1 - Current Baseline Stabilization (Boot 3 + JDK17)"
  echo "- [ ] Ensure CI strict precheck is green on PR branch"
  echo "- [ ] Confirm runtime Java and pom java.version are aligned with current target phase"
  echo "- [ ] Confirm Spring Boot parent stays on 3.x during current phase"
  echo "- [ ] Run smoke + validation gates and capture baseline report"
  echo
  echo "## Phase 2 - javax Hotspot Refactor Order"
  for i in 1 2 3 4 5; do
    hotspot="$(get_value "javax_hotspot_${i}")"
    if [[ -n "$hotspot" ]]; then
      hotspot_path="${hotspot%%:*}"
      hotspot_count="${hotspot##*:}"
      risk_level="$(risk_level_for_hotspot "$hotspot_path")"
      hotspot_owner="$(owner_for_hotspot "$hotspot_path")"
      if [[ "$hotspot_owner" == "TBD" ]]; then
        UNMAPPED_COUNT=$((UNMAPPED_COUNT + 1))
      fi
      echo "- [ ] hotspot ${i}: \`${hotspot_path}\` (imports: ${hotspot_count}, risk: ${risk_level}, owner: ${hotspot_owner})"
    fi
  done
  echo
  echo "## Phase 3 - Next-Hop Execution Gates"
  echo "- [ ] JDK17 gate: run precheck \`./scripts/upgrade-precheck.sh --phase jdk17 --strict\`"
  echo "- [ ] JDK17 gate: run smoke gate \`./scripts/test-gate.sh smoke\`"
  echo "- [ ] Boot3 planning gate: estimate javax->jakarta migration batches from hotspot list"
  echo "- [ ] Main-branch full gate: \`./scripts/test-gate.sh full\`"
  echo
  echo "## Meta"
  echo "- unmapped owners: \`${UNMAPPED_COUNT}\`"
} > "$OUT_FILE"

echo "[upgrade-build-checklist] wrote: $OUT_FILE"
if [[ "$FAIL_ON_UNMAPPED" -eq 1 && "$UNMAPPED_COUNT" -gt 0 ]]; then
  echo "[upgrade-build-checklist] ERROR: found ${UNMAPPED_COUNT} hotspot(s) without owner mapping"
  exit 1
fi
