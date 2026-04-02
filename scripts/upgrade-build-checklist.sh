#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 2 ]]; then
  echo "Usage: $0 <precheck-report-file> <output-markdown-file>"
  exit 2
fi

REPORT_FILE="$1"
OUT_FILE="$2"

if [[ ! -f "$REPORT_FILE" ]]; then
  echo "[upgrade-build-checklist] ERROR: report file not found: $REPORT_FILE"
  exit 2
fi

get_value() {
  local key="$1"
  grep -E "^${key}=" "$REPORT_FILE" | head -n 1 | cut -d= -f2-
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

STRICT_MODE="$(get_value upgrade_precheck_strict_mode)"
STRICT_FAILED="$(get_value upgrade_precheck_strict_failed)"
JAVA_MAJOR="$(get_value java_major)"
POM_JAVA_VERSION="$(get_value pom_java_version)"
BOOT_PARENT="$(get_value spring_boot_parent)"
JAVAX_COUNT="$(get_value javax_import_occurrences)"

mkdir -p "$(dirname "$OUT_FILE")"

{
  echo "# Upgrade Execution Checklist (Auto-generated)"
  echo
  echo "- strict mode: \`${STRICT_MODE:-unknown}\`"
  echo "- strict failed: \`${STRICT_FAILED:-unknown}\`"
  echo "- runtime java major: \`${JAVA_MAJOR:-unknown}\`"
  echo "- pom java.version: \`${POM_JAVA_VERSION:-unknown}\`"
  echo "- spring boot parent: \`${BOOT_PARENT:-unknown}\`"
  echo "- javax import occurrences: \`${JAVAX_COUNT:-unknown}\`"
  echo
  echo "## Phase 1 - Baseline Lock"
  echo "- [ ] Ensure CI strict precheck is green on PR branch"
  echo "- [ ] Confirm Java 8 baseline before first migration hop"
  echo "- [ ] Confirm parent stays at 1.5.9.RELEASE before Boot 2.7 branch starts"
  echo
  echo "## Phase 2 - javax Hotspot Refactor Order"
  for i in 1 2 3 4 5; do
    hotspot="$(get_value "javax_hotspot_${i}")"
    if [[ -n "$hotspot" ]]; then
      hotspot_path="${hotspot%%:*}"
      hotspot_count="${hotspot##*:}"
      risk_level="$(risk_level_for_hotspot "$hotspot_path")"
      echo "- [ ] hotspot ${i}: \`${hotspot_path}\` (imports: ${hotspot_count}, risk: ${risk_level}, owner: _TBD_)"
    fi
  done
  echo
  echo "## Phase 3 - Boot 2.7 + JDK17 Execution Gate"
  echo "- [ ] Create branch: \`upgrade/boot-2.7-jdk17\`"
  echo "- [ ] Run smoke gate: \`./scripts/test-gate.sh smoke\`"
  echo "- [ ] Run validation gate: \`./scripts/test-gate.sh validation\`"
  echo "- [ ] Run full gate: \`./scripts/test-gate.sh full\`"
} > "$OUT_FILE"

echo "[upgrade-build-checklist] wrote: $OUT_FILE"
