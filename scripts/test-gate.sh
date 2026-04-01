#!/usr/bin/env bash
set -euo pipefail

MODE="${1:-smoke}"
TMP_LOG="$(mktemp)"
trap 'rm -f "${TMP_LOG}"' EXIT

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
  run_mvn mvn -q -Dtest=PasswordSupportTest,SYLoginServiceTest,SYUserServiceTest,PermissionGuardTest test
}

run_validation() {
  run_mvn mvn -q -Dtest=SYLoginRestValidationTest,SYUserRestValidationTest,SYProjectRestValidationTest,SYTeamRestValidationTest,SYProductRestValidationTest,SYRoleRestValidationTest,SYClassesRestValidationTest test
}

run_full() {
  run_mvn mvn test
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
