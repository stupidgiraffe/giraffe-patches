#!/usr/bin/env bash
# Install the matching Reseam CLI from the official upstream v0.20.0 release.
# If no usable Linux binary is present, build the same pinned tag from source.
set -euo pipefail
VERSION="v0.20.0"
DEST="${RUNNER_TEMP:-/tmp}/reseam-cli"
mkdir -p "$DEST"
if curl --fail --location --silent --show-error --retry 2 \
  "https://git.reseam.app/reseam/reseam/releases/download/${VERSION}/reseam-linux-x64" \
  --output "$DEST/reseam"; then
  chmod 755 "$DEST/reseam"
  if ! "$DEST/reseam" --help >/dev/null 2>&1; then
    rm -f "$DEST/reseam"
  fi
fi
if [[ ! -x "$DEST/reseam" ]]; then
  rm -rf "$DEST/source"
  git clone --depth 1 --branch "$VERSION" https://git.reseam.app/reseam/reseam.git "$DEST/source"
  cargo build --release -p reseam-cli --manifest-path "$DEST/source/Cargo.toml"
  cp "$DEST/source/target/release/reseam" "$DEST/reseam"
fi
"$DEST/reseam" --help >/dev/null
echo "RESEAM_BIN=$DEST/reseam" >> "${GITHUB_ENV:-/dev/null}"
echo "Reseam CLI pinned to $VERSION: $DEST/reseam"
