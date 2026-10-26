# Smart Launcher Reseam 0.31.0 — manual build

Isolated, reproducible Reseam 0.31.0 bundle build using the **same patch source** as the working Smart Launcher 6.6 build 021 (660210) patch. The bundle retains the existing `giraffe-patches` identity and permanent Ed25519 signing key. It includes `Enable Pro` with the internal signature-check dependency.

**Supported app version: build 021 only.** The developer released Smart Launcher 6.6 build 024 around October 23, 2026, but its bytecode and patch compatibility are not yet verified. Do not add build 024 to compatibleWith until the official/verified APK is inspected and patch-stage behavior is tested. The build-021 purchase-items owner `Lmn8;` is obfuscated and is not safe to assume for 024.

Build: trigger the new manual-only workflow `.github/workflows/smartlauncher-manual.yml`; download the artifact, extract `giraffe-patches.reseam`, and import it directly into Manager. This standalone subproject intentionally does not use the root repository's old 0.26.2 toolchain.

Compilation, manifest, signature and patch registration checks are *not* an on-device patch/install test. The user reports existing Smart Launcher patches working; this particular 0.31.0 archive has to be tested separately on a target APK.
