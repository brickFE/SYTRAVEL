#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

BATCH="${1:-}"
BASE_REF="${2:-HEAD~1}"
TARGET_REF="${3:-HEAD}"

if [[ -z "$BATCH" ]]; then
  echo "Usage: $0 <A|B|C> [base_ref] [target_ref]" >&2
  echo "Example: $0 A origin/main HEAD" >&2
  exit 2
fi

case "$BATCH" in
  A)
    PATTERN='^src/main/java/com/sy/travel/(dto|rest|handler)/'
    LABEL='Batch A (dto/rest/handler)'
    GATE='smoke'
    ;;
  B)
    PATTERN='^src/main/java/com/sy/travel/entity/'
    LABEL='Batch B (entity)'
    GATE='smoke + validation'
    ;;
  C)
    PATTERN='^src/main/java/com/sy/travel/service/SYWebsocketService\.java$'
    LABEL='Batch C (websocket protocol layer)'
    GATE='full'
    ;;
  *)
    echo "Invalid batch: $BATCH (expected A|B|C)" >&2
    exit 2
    ;;
esac

valid_base_ref=1
valid_target_ref=1

if ! git rev-parse --verify --quiet "$BASE_REF^{commit}" >/dev/null; then
  valid_base_ref=0
fi

if ! git rev-parse --verify --quiet "$TARGET_REF^{commit}" >/dev/null; then
  valid_target_ref=0
fi

if [[ "$valid_base_ref" -eq 0 || "$valid_target_ref" -eq 0 ]]; then
  echo "[freeze-guard] WARN: Unable to resolve revision(s)." >&2
  [[ "$valid_base_ref" -eq 0 ]] && echo "[freeze-guard]  - invalid base_ref: $BASE_REF" >&2
  [[ "$valid_target_ref" -eq 0 ]] && echo "[freeze-guard]  - invalid target_ref: $TARGET_REF" >&2
  echo "[freeze-guard] Fallback: evaluating only local working tree Java changes." >&2
  changed_files="$(git status --porcelain -- src/main/java | awk '{print $2}' || true)"
else
  changed_files="$(git diff --name-only "$BASE_REF" "$TARGET_REF" -- src/main/java || true)"
fi

if [[ -z "$changed_files" ]]; then
  echo "[freeze-guard] No Java source changes detected in range $BASE_REF..$TARGET_REF"
  echo "[freeze-guard] Batch: $LABEL"
  echo "[freeze-guard] Suggested acceptance gate: ./scripts/test-gate.sh $GATE"
  exit 0
fi

violations="$(echo "$changed_files" | rg -v "$PATTERN" || true)"

if [[ -n "$violations" ]]; then
  echo "[freeze-guard] FAIL: Found cross-batch changes for $LABEL in range $BASE_REF..$TARGET_REF" >&2
  echo "$violations" >&2
  echo "[freeze-guard] Rule: Task 1 batch freeze forbids cross-batch insertion." >&2
  exit 1
fi

echo "[freeze-guard] PASS: All Java source changes belong to $LABEL"
echo "[freeze-guard] Files checked:"
echo "$changed_files" | sed 's/^/ - /'
echo "[freeze-guard] Suggested acceptance gate: ./scripts/test-gate.sh $GATE"
