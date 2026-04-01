#!/usr/bin/env bash
set -euo pipefail

MODE="${1:-smoke}"

run_unit() {
  mvn -q -Dtest=PasswordSupportTest,SYLoginServiceTest,SYUserServiceTest,PermissionGuardTest test
}

run_validation() {
  mvn -q -Dtest=SYUserRestValidationTest,SYProjectRestValidationTest,SYTeamRestValidationTest,SYProductRestValidationTest,SYRoleRestValidationTest,SYClassesRestValidationTest test
}

run_full() {
  mvn test
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
