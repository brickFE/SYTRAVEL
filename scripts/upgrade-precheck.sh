#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

STRICT_MODE=0
REPORT_FILE=""
while [[ $# -gt 0 ]]; do
  case "$1" in
    --strict)
      STRICT_MODE=1
      shift
      ;;
    --report)
      REPORT_FILE="${2:-}"
      if [[ -z "$REPORT_FILE" ]]; then
        echo "[upgrade-precheck] ERROR: --report requires a file path"
        exit 2
      fi
      shift 2
      ;;
    *)
      echo "[upgrade-precheck] ERROR: unknown argument: $1"
      exit 2
      ;;
  esac
done

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
JAVA_VERSION_TOKEN="$(echo "$JAVA_VERSION_RAW" | awk -F'"' '{print $2}')"
JAVA_MAJOR="$(echo "$JAVA_VERSION_TOKEN" | awk -F. '{if ($1 == "1") print $2; else print $1}')"

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

STRICT_FAILED=0
if [[ "$STRICT_MODE" -eq 1 ]]; then
  if [[ "${JAVA_MAJOR:-}" != "8" ]]; then
    echo "[upgrade-precheck] STRICT-ERROR: expected runtime Java major version 8, got ${JAVA_MAJOR:-unknown}"
    STRICT_FAILED=1
  fi
  if [[ "${POM_JAVA_VERSION:-}" != "1.8" ]]; then
    echo "[upgrade-precheck] STRICT-ERROR: expected pom java.version=1.8"
    STRICT_FAILED=1
  fi
  if [[ "${BOOT_PARENT_VERSION:-}" != "1.5.9.RELEASE" ]]; then
    echo "[upgrade-precheck] STRICT-ERROR: expected spring-boot-starter-parent=1.5.9.RELEASE"
    STRICT_FAILED=1
  fi
fi

echo "[upgrade-precheck] scanning javax.* imports (for Boot 3 migration impact sizing)..."
JAVA_FILES_WITH_JAVAX="$(rg -n "import javax\\." src/main/java src/test/java | wc -l | tr -d ' ')"
echo "[upgrade-precheck] javax import occurrences: $JAVA_FILES_WITH_JAVAX"
JAVAX_HOTSPOTS="$(
  rg -n "import javax\\." src/main/java src/test/java \
    | cut -d: -f1 \
    | sort \
    | uniq -c \
    | sort -nr \
    | head -n 5 \
    | awk '{print $2":"$1}'
)"
JAVAX_CATEGORY_COUNTS="$(
  rg -n "import javax\\." src/main/java src/test/java || true
)"
read -r JAVAX_SERVLET_COUNT JAVAX_VALIDATION_COUNT JAVAX_PERSISTENCE_COUNT JAVAX_WEBSOCKET_COUNT JAVAX_ANNOTATION_COUNT JAVAX_OTHER_COUNT <<EOF
$(echo "$JAVAX_CATEGORY_COUNTS" | awk '
BEGIN {servlet=0; validation=0; persistence=0; websocket=0; annotation=0; other=0}
/import javax\.servlet\./ {servlet++; next}
/import javax\.validation\./ {validation++; next}
/import javax\.persistence\./ {persistence++; next}
/import javax\.websocket\./ {websocket++; next}
/import javax\.annotation\./ {annotation++; next}
/import javax\./ {other++; next}
END {print servlet, validation, persistence, websocket, annotation, other}
')
EOF
if [[ -n "$JAVAX_HOTSPOTS" ]]; then
  echo "[upgrade-precheck] top javax hotspots:"
  echo "$JAVAX_HOTSPOTS" | while read -r line; do
    echo "[upgrade-precheck]   $line"
  done
fi
echo "[upgrade-precheck] javax categories: servlet=${JAVAX_SERVLET_COUNT}, validation=${JAVAX_VALIDATION_COUNT}, persistence=${JAVAX_PERSISTENCE_COUNT}, websocket=${JAVAX_WEBSOCKET_COUNT}, annotation=${JAVAX_ANNOTATION_COUNT}, other=${JAVAX_OTHER_COUNT}"

if [[ -n "$REPORT_FILE" ]]; then
  mkdir -p "$(dirname "$REPORT_FILE")"
  {
    echo "upgrade_precheck_strict_mode=$STRICT_MODE"
    echo "upgrade_precheck_strict_failed=$STRICT_FAILED"
    echo "java_version_raw=$JAVA_VERSION_RAW"
    echo "java_major=$JAVA_MAJOR"
    echo "maven_version_raw=$MVN_VERSION_RAW"
    echo "pom_java_version=${POM_JAVA_VERSION:-unknown}"
    echo "spring_boot_parent=${BOOT_PARENT_VERSION:-unknown}"
    echo "javax_import_occurrences=$JAVA_FILES_WITH_JAVAX"
    echo "javax_servlet_count=$JAVAX_SERVLET_COUNT"
    echo "javax_validation_count=$JAVAX_VALIDATION_COUNT"
    echo "javax_persistence_count=$JAVAX_PERSISTENCE_COUNT"
    echo "javax_websocket_count=$JAVAX_WEBSOCKET_COUNT"
    echo "javax_annotation_count=$JAVAX_ANNOTATION_COUNT"
    echo "javax_other_count=$JAVAX_OTHER_COUNT"
    idx=1
    echo "$JAVAX_HOTSPOTS" | while read -r hotspot; do
      if [[ -n "$hotspot" ]]; then
        echo "javax_hotspot_${idx}=$hotspot"
        idx=$((idx + 1))
      fi
    done
  } > "$REPORT_FILE"
  echo "[upgrade-precheck] report written: $REPORT_FILE"
fi

if [[ "$STRICT_MODE" -eq 1 && "$STRICT_FAILED" -eq 1 ]]; then
  echo "[upgrade-precheck] strict check failed"
  exit 1
fi

echo "[upgrade-precheck] done"
