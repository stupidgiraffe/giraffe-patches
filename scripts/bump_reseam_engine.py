#!/usr/bin/env python3
"""Prepare an upstream Reseam engine-version bump; never change app compatibility."""
from pathlib import Path
import re
import sys

ROOT = Path(__file__).resolve().parents[1]
VERSION_RE = re.compile(r"v?(\d+)\.(\d+)\.(\d+)\Z")
PINNED_FILES = (
    "settings.gradle.kts",
    "scripts/install_reseam_cli.sh",
    ".github/workflows/preview.yml",
    ".github/workflows/release.yml",
    ".github/workflows/validate.yml",
    "docs/BUILD.md",
    "docs/RELEASING.md",
    "docs/COMPATIBILITY.md",
    "README.md",
)

def parse_version(value: str) -> tuple[int, int, int]:
    match = VERSION_RE.fullmatch(value)
    if not match:
        raise ValueError(f"Invalid Reseam version: {value!r}")
    return tuple(int(n) for n in match.groups())

def current_version() -> str:
    source = (ROOT / "settings.gradle.kts").read_text(encoding="utf-8")
    match = re.search(r'id\("app\.reseam\.workspace"\) version "(\d+\.\d+\.\d+)"', source)
    if not match:
        raise RuntimeError("Missing pinned Reseam Gradle plugin")
    return match.group(1)

def bump(new_version: str) -> bool:
    new_version = new_version.removeprefix("v")
    current = current_version()
    if parse_version(new_version) <= parse_version(current):
        return False
    changes: dict[Path, str] = {}
    for relative_path in PINNED_FILES:
        path = ROOT / relative_path
        old = path.read_text(encoding="utf-8")
        if current not in old:
            raise RuntimeError(f"Expected pinned version {current} in {relative_path}")
        updated = old.replace(current, new_version)
        if relative_path == "README.md":
            # Keep the supported engine-line badge truthful.
            updated = updated.replace(
                f"Reseam-{'.'.join(current.split('.')[:2])}.x",
                f"Reseam-{'.'.join(new_version.split('.')[:2])}.x",
            )
        changes[path] = updated
    for path, updated in changes.items():
        path.write_text(updated, encoding="utf-8")
    return True

if __name__ == "__main__":
    if len(sys.argv) != 2:
        raise SystemExit("Usage: python scripts/bump_reseam_engine.py vX.Y.Z")
    changed = bump(sys.argv[1])
    print(f"Reseam SDK {current_version()}; changed={str(changed).lower()}")
