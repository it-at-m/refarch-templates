#!/usr/bin/env bash
set -euo pipefail

# Determine the directory where this script is located (independent of the calling location)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

echo "Starting download script ..."

set_proxy_from_global_npmrc() {
  local GLOBAL_NPMRC
  GLOBAL_NPMRC=$(npm config get userconfig 2>/dev/null || echo "")
  [ -n "$GLOBAL_NPMRC" ] && [ -f "$GLOBAL_NPMRC" ] || return 0

  echo "Reading proxy-configuration from global .npmrc: $GLOBAL_NPMRC"

  unset http_proxy https_proxy HTTP_PROXY HTTPS_PROXY

  local P HP
  P=$(grep -E '^[[:space:]]*proxy=' "$GLOBAL_NPMRC"        | tail -n1 | cut -d'=' -f2- || true)
  HP=$(grep -E '^[[:space:]]*https-proxy=' "$GLOBAL_NPMRC" | tail -n1 | cut -d'=' -f2- || true)

  if [ -n "$P" ]; then
    export http_proxy="$P" HTTP_PROXY="$P"
    echo " => set http_proxy to: $P"
  fi
  if [ -n "$HP" ]; then
    export https_proxy="$HP" HTTPS_PROXY="$HP"
    echo " => set https_proxy to: $HP"
  fi
  if [ -z "$P" ] && [ -z "$HP" ]; then
    echo " => No proxy-configuration found in .npmrc"
  fi
}

set_proxy_from_global_npmrc

# Da das Skript direkt im Projekt liegt, ist der Projektordner das SCRIPT_DIR
PROJECT="$SCRIPT_DIR"

echo "----------------------------------------"
echo "Project: $PROJECT"

# Reading version number of openapi-config file
CFG="$PROJECT/openapitools.json"
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

echo " => Selected version in config-file: $VER"

# Remote filename and location of jar
REMOTE_JAR="openapi-generator-cli-$VER.jar"
URL="https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/$VER/$REMOTE_JAR"

# Local desired filename and location
LOCAL_JAR="$VER.jar"
DIR="$PROJECT/node_modules/@openapitools/openapi-generator-cli/versions"
mkdir -p "$DIR"

if [ -f "$DIR/$LOCAL_JAR" ]; then
  echo " => .jar file already exists: $DIR/$LOCAL_JAR, skipping download."
  echo "----------------------------------------"
  echo "Done ..."
  exit 0
fi

echo " => Downloading .jar file:"
echo "    URL:        $URL"
echo "    Target-file: $DIR/$LOCAL_JAR"

if command -v curl >/dev/null 2>&1; then
  echo "  => Using curl"
  curl -fSL "$URL" -o "$DIR/$LOCAL_JAR"
else
  echo "  => Using wget"
  wget -O "$DIR/$LOCAL_JAR" "$URL"
fi

echo " => Download completed"
echo "----------------------------------------"
echo "Done ..."
