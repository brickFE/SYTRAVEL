#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

echo "[upgrade-precheck] project: $ROOT_DIR"

if ! command -v java >/dev/null 2>&1; then
  echo "[upgrade-precheck] ERROR: java not found in PATH"
  exit 2
fi

if ! command -v mvn >/dev/null 2>&1; then
  echo "[upgrade-precheck] ERROR: mvn not found in PATH"
  exit 2
fi

JAVA_VERSION_RAW="$(java -version 2>&1 | head -n 1)"
MVN_VERSION_RAW="$(mvn -v 2>/dev/null | head -n 1)"

echo "[upgrade-precheck] java: $JAVA_VERSION_RAW"
echo "[upgrade-precheck] maven: $MVN_VERSION_RAW"

POM_JAVA_VERSION="$(sed -n 's:.*<java.version>\(.*\)</java.version>.*:\1:p' pom.xml | head -n 1)"
BOOT_PARENT_VERSION="$(
  awk '
    /<parent>/ {in_parent=1}
    in_parent && /<artifactId>spring-boot-starter-parent<\/artifactId>/ {target=1}
    in_parent && target && /<version>/ {
      gsub(/^.*<version>|<\/version>.*$/, "", $0);
      print;
      exit
    }
    /<\/parent>/ {in_parent=0}
  ' pom.xml
)"

echo "[upgrade-precheck] pom.java.version: ${POM_JAVA_VERSION:-unknown}"
echo "[upgrade-precheck] spring-boot-parent: ${BOOT_PARENT_VERSION:-unknown}"

if [[ "${POM_JAVA_VERSION:-}" != "1.8" ]]; then
  echo "[upgrade-precheck] WARN: expected baseline java.version=1.8 before first upgrade hop"
fi

if [[ "${BOOT_PARENT_VERSION:-}" != "1.5.9.RELEASE" ]]; then
  echo "[upgrade-precheck] WARN: expected current parent spring-boot-starter-parent=1.5.9.RELEASE"
fi

echo "[upgrade-precheck] scanning javax.* imports (for Boot 3 migration impact sizing)..."
JAVA_FILES_WITH_JAVAX="$(rg -n "import javax\\." src/main/java src/test/java | wc -l | tr -d ' ')"
echo "[upgrade-precheck] javax import occurrences: $JAVA_FILES_WITH_JAVAX"

echo "[upgrade-precheck] done"
