# Icon Pack Studio — ReSeam 0.32.0

This is a version-scoped Reseam 0.32.0 patch for Icon Pack Studio:
- 2.4 build 001 (240010)
- 2.5 build 001 (250010)

The LicenseStatus class changed between the app builds, so the patch locates it with the stable `LicenseStatus(hasProAccess=` string. The relevant constructor retains the same two-Boolean signature. The obfuscated billing aggregate was also structurally checked in version 2.5.

Use only the on-demand `Build Icon Pack Studio Reseam 0.32.0 (manual)` workflow. It compiles with matching Reseam 0.32.0 CLI/Gradle plugin and signs the resulting native `.reseam` bundle with the existing permanent key. It asserts the exact engine, advertised app versions, signing public key, and patch registration.

Download the `giraffe-iconpackstudio-reseam-0.32.0` Actions artifact ZIP, extract the `.reseam` file, then import the file into ReSeam Manager 0.32.0.

Successful compilation and signed metadata checks do not prove device runtime behavior. No app binaries or signing secrets are stored in this repository. No automated/scheduled CI runs are needed.

ReSeam 0.32.0 manual signed build (2026-10-29): run [40734838816](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/40734838816) succeeded. Bundle SHA-256 `f6e58f8715d8c610e89ec6f589fe88fc627feda4eecbb1817685676ff4733620`, signer `9b5228381bdce62d03e9df9654f02c9ceb9c06fd67722aa201a6c01cc989d89b`; metadata registers both app versions. No live APK patch-stage or device test yet.
