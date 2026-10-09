# Contributing to Giraffe Patches

Thanks for improving Android patch compatibility. Keep each contribution small and verifiable.

1. File an issue identifying the target package and **exact app version/build** before a large port.
2. Use the official Reseam SDK patterns; do not hardcode obfuscated class names unless there is a compelling version-scoped reason.
3. Add a compatibility entry stating whether the change is **device-tested**, **build-validated**, or **untested**.
4. Include proof: exact matcher failure, reproduction instructions, Gradle logs, and Reseam dry-run output. Redact package/account paths and secrets.
5. Preserve attribution and licenses of any upstream code you adapt.
6. Never submit proprietary APKs, modified app binaries, private signing material, or personal data to this public repository.

We welcome new app requests, compatibility reports, better matcher constraints, and documentation fixes. Pull requests should identify test app version(s) and what was actually verified.

See [BUILD.md](docs/BUILD.md) and [COMPATIBILITY.md](docs/COMPATIBILITY.md).
