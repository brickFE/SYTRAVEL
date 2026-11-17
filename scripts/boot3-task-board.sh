#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

OUT_FILE="${1:-build/boot3-jakarta-task-board.md}"
OWNERS_MAP_FILE="${2:-config/upgrade-owners.map}"
mkdir -p "$(dirname "$OUT_FILE")"

owner_for_file() {
  local target="$1"
  if [[ ! -f "$OWNERS_MAP_FILE" ]]; then
    echo "TBD"
    return
  fi
  while IFS= read -r line; do
    [[ -z "$line" || "$line" =~ ^# ]] && continue
    local pattern="${line%%=*}"
    local owner="${line#*=}"
    if [[ "$target" == $pattern ]]; then
      echo "$owner"
      return
    fi
  done < "$OWNERS_MAP_FILE"
  echo "TBD"
}

emit_batch() {
  local title="$1"
  local pattern="$2"
  local owner_missing=0
  local files
  files="$(rg -n "$pattern" src/main/java src/test/java 2>/dev/null | cut -d: -f1 | sort -u || true)"
  echo "## ${title}"
  echo
  echo "| File | Owner |"
  echo "| --- | --- |"
  if [[ -z "$files" ]]; then
    echo "| _none_ | _none_ |"
  else
    while read -r f; do
      [[ -z "$f" ]] && continue
      owner="$(owner_for_file "$f")"
      if [[ "$owner" == "TBD" ]]; then
        owner_missing=$((owner_missing + 1))
      fi
      echo "| \`${f}\` | ${owner} |"
    done <<< "$files"
  fi
  echo
  echo "- unmapped_owners: ${owner_missing}"
  echo
}

{
  echo "# Boot 3 Jakarta Task Board (Auto-generated)"
  echo
  echo "- generated_at_utc: $(date -u +"%Y-%m-%dT%H:%M:%SZ")"
  echo "- owners_map: ${OWNERS_MAP_FILE}"
  echo
  emit_batch "Batch A - validation/annotation" 'import javax\.(validation|annotation)\.'
  emit_batch "Batch B - persistence" 'import javax\.persistence\.'
  emit_batch "Batch C - servlet/websocket" 'import javax\.(servlet|websocket)\.'
} > "$OUT_FILE"

echo "[boot3-task-board] wrote: $OUT_FILE"
