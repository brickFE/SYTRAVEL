#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

OUT_FILE="${1:-build/boot3-jakarta-migration-plan.md}"
mkdir -p "$(dirname "$OUT_FILE")"

collect_count() {
  local pattern="$1"
  (rg -n "$pattern" src/main/java src/test/java 2>/dev/null || true) | wc -l | tr -d ' '
}

SERVLET_COUNT="$(collect_count 'import javax\.servlet\.')"
VALIDATION_COUNT="$(collect_count 'import javax\.validation\.')"
PERSISTENCE_COUNT="$(collect_count 'import javax\.persistence\.')"
WEBSOCKET_COUNT="$(collect_count 'import javax\.websocket\.')"
ANNOTATION_COUNT="$(collect_count 'import javax\.annotation\.')"
TOTAL_COUNT="$(collect_count 'import javax\.')"

top_hotspots="$(
  rg -n 'import javax\.' src/main/java src/test/java 2>/dev/null \
    | cut -d: -f1 \
    | sort \
    | uniq -c \
    | sort -nr \
    | head -n 20
)"

{
  echo "# Boot 3 / Jakarta Migration Plan (Auto-generated)"
  echo
  echo "- generated_at_utc: $(date -u +"%Y-%m-%dT%H:%M:%SZ")"
  echo "- total_javax_imports: ${TOTAL_COUNT}"
  echo "- servlet: ${SERVLET_COUNT}"
  echo "- validation: ${VALIDATION_COUNT}"
  echo "- persistence: ${PERSISTENCE_COUNT}"
  echo "- websocket: ${WEBSOCKET_COUNT}"
  echo "- annotation: ${ANNOTATION_COUNT}"
  echo
  echo "## Suggested Migration Batches"
  echo
  echo "1. **Batch A (Low-risk / High-volume):** \`javax.validation.*\` and \`javax.annotation.*\` -> \`jakarta.*\`"
  echo "2. **Batch B (Data layer):** \`javax.persistence.*\` -> \`jakarta.persistence.*\`"
  echo "3. **Batch C (Protocol layer):** \`javax.servlet.*\` and \`javax.websocket.*\` -> \`jakarta.*\`"
  echo
  echo "## Top Hotspots (by javax import count)"
  echo
  echo "| File | javax imports |"
  echo "| --- | ---: |"
  if [[ -n "$top_hotspots" ]]; then
    while read -r row; do
      [[ -z "$row" ]] && continue
      count="$(echo "$row" | awk '{print $1}')"
      file="$(echo "$row" | awk '{print $2}')"
      echo "| \`${file}\` | ${count} |"
    done <<< "$top_hotspots"
  else
    echo "| _none_ | 0 |"
  fi
  echo
  echo "## Next Stage Checklist"
  echo
  echo "- [ ] Complete Batch A and run smoke gate"
  echo "- [ ] Complete Batch B and run smoke + validation"
  echo "- [ ] Complete Batch C and run full regression"
  echo "- [ ] Remove all \`javax.*\` imports and switch Boot parent to 3.x branch"
} > "$OUT_FILE"

echo "[boot3-migration-plan] wrote: $OUT_FILE"
