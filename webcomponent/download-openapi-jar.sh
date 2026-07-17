#!/usr/bin/env bash
set -euo pipefail

# Determine the directory where this script is located (independent of the calling location)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

echo "Starting download script ..."

echo "----------------------------------------"

# Reading version number of openapi-config file
CFG="$SCRIPT_DIR/openapitools.json"
if [ ! -f "$CFG" ]; then
  echo "Config file $CFG not found - exiting"
  exit 1
fi

VER=$(grep -oE '"version"\s*:\s*"[0-9]+\.[0-9]+\.[0-9]+"' "$CFG" \
  | head -n1 \
  | sed -E 's/.*"version"\s*:\s*"([^"]+)".*/\1/')

if [ -z "${VER:-}" ]; then
  echo " No valid version found - exiting"
  exit 1
fi

echo "Selected version in config-file: $VER"

# Remote filename and location of jar
REMOTE_JAR="openapi-generator-cli-$VER.jar"

# Local desired filename and location
LOCAL_JAR="$VER.jar"
DIR="$SCRIPT_DIR/node_modules/@openapitools/openapi-generator-cli/versions"
mkdir -p "$DIR"

# Check if jar file already exists
if [ -f "$DIR/$LOCAL_JAR" ]; then
  echo ".jar file already exists: $DIR/$LOCAL_JAR, skipping download."
  echo "----------------------------------------"
  echo "Done ..."
  exit 0
fi

# Check if mvn is available
if ! command -v mvn > /dev/null 2>&1; then
  echo "Error: Maven (mvn) is not installed or not in PATH but required for downloading."
  exit 1
fi

echo "Downloading .jar file using Maven..."

mvn dependency:copy "-Dartifact=org.openapitools:openapi-generator-cli:$VER:jar" "-DoutputDirectory=$DIR" 1>/dev/null
mv "$DIR/$REMOTE_JAR" "$DIR/$LOCAL_JAR"

echo "Download completed"
echo "File downloaded to $DIR/$LOCAL_JAR"
echo "----------------------------------------"
echo "Done ..."
