#!/usr/bin/env bash
set -euo pipefail

MODE="${1:-smoke}"
TMP_LOG="$(mktemp)"
trap 'rm -f "${TMP_LOG}"' EXIT
MAVEN_SETTINGS_FILE="${MAVEN_SETTINGS_FILE:-}"

UNIT_TESTS="PasswordSupportTest,SYLoginServiceTest,SYUserServiceTest,PermissionGuardTest,SYWebsocketServiceTest,GlobalExceptionHandlerTest"
VALIDATION_TESTS="SYLoginRestValidationTest,SYUserRestValidationTest,SYProjectRestValidationTest,SYTeamRestValidationTest,SYProductRestValidationTest,SYRoleRestValidationTest,SYClassesRestValidationTest,SYLoggerRestValidationTest"

MAVEN_CMD=("mvn")
if [[ -n "$MAVEN_SETTINGS_FILE" ]]; then
  if [[ ! -f "$MAVEN_SETTINGS_FILE" ]]; then
    echo "[test-gate] ERROR: MAVEN_SETTINGS_FILE does not exist: $MAVEN_SETTINGS_FILE" >&2
    exit 2
  fi
  MAVEN_CMD+=("-s" "$MAVEN_SETTINGS_FILE")
  echo "[test-gate] using maven settings: $MAVEN_SETTINGS_FILE"
fi

run_mvn() {
  local cmd="$*"
  set +e
  bash -lc "${cmd}" 2>&1 | tee "${TMP_LOG}"
  local code=${PIPESTATUS[0]}
  set -e
  if [[ ${code} -ne 0 ]]; then
    if grep -q "status code: 403" "${TMP_LOG}"; then
      echo "" >&2
      echo "[test-gate] Maven repository access failed (HTTP 403)." >&2
      echo "[test-gate] Please switch to a network/mirror that can access required artifacts." >&2
    fi
    return "${code}"
  fi
}

run_unit() {
  echo "[test-gate] unit tests: ${UNIT_TESTS}"
  run_mvn "${MAVEN_CMD[@]}" -q -Dtest="${UNIT_TESTS}" test
}

run_validation() {
  echo "[test-gate] validation tests: ${VALIDATION_TESTS}"
  run_mvn "${MAVEN_CMD[@]}" -q -Dtest="${VALIDATION_TESTS}" test
}

run_full() {
  run_mvn "${MAVEN_CMD[@]}" test
}

case "${MODE}" in
  smoke)
    run_unit
    run_validation
    ;;
  unit)
    run_unit
    ;;
  validation)
    run_validation
    ;;
  full)
    run_full
    ;;
  *)
    echo "Unknown mode: ${MODE}" >&2
    echo "Usage: $0 [smoke|unit|validation|full]" >&2
    exit 2
    ;;
esac
