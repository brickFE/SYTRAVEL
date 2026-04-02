#!/usr/bin/env bash
set -euo pipefail

if [[ $# -lt 2 ]]; then
  echo "Usage: $0 <mirror-url> <output-settings-file> [mirror-id]"
  echo "Example: $0 https://your-nexus/repository/maven-public/ .mvn/settings-mirror.xml company-mirror"
  exit 2
fi

MIRROR_URL="$1"
OUT_FILE="$2"
MIRROR_ID="${3:-custom-mirror}"

mkdir -p "$(dirname "$OUT_FILE")"
cat > "$OUT_FILE" <<EOF
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <mirrors>
    <mirror>
      <id>${MIRROR_ID}</id>
      <name>${MIRROR_ID}</name>
      <url>${MIRROR_URL}</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
</settings>
EOF

echo "[create-maven-settings] wrote: $OUT_FILE"
echo "[create-maven-settings] export MAVEN_SETTINGS_FILE=$OUT_FILE"
