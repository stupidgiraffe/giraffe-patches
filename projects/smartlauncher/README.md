# Smart Launcher 6.6 build 024 — ReSeam 0.31.0

This isolated project compiles a **Smart Launcher 6.6 build 024 (660240)** patch against ReSeam 0.31.0. It uses a distinct bundle ID `giraffe-smartlauncher` (separate from the legacy `giraffe-patches` build-021 bundle), while retaining the permanent Ed25519 signing identity and manual-only workflow. This avoids possible Manager cache/identity collisions when both versions are imported.

Compatibility was established from the *actual uploaded APK*, not its filename:
- Package `ginlemon.flowerfree`
- Version name `6.6 build 024`, version code `660240`
- APK SHA-256 `de80631a4b305c9ae46ea86195085a36cdf21371cc092801ef36b549b2586b5b`
- APK contents: `classes.dex`, `classes2.dex`, `classes3.dex`; ZIP integrity passed.
- Purchase-items owner moved from `Lmn8;` (build 021) to **`Li89;`** (build 024). In 024, `Lmn8;` still exists but is unrelated, so retaining the old hardcoded class would be wrong.
- `Li89;-><clinit>()V` holds the `lifetime` item assigned to field `Li89;.c:Lmm8;`; `Lmm8;` has the matching no-arg boolean getter and Context/boolean setter.
- `Li89;.a(Li89;,Context,boolean)V` contains the premium access changed broadcast string, and the Smart Launcher signature warning method still calls `System.exit(int)`. The unmodified patch flow therefore has verified corresponding targets.
- The global public/static zero-arg `Lginlemon/flower/App;` return signature was found in the APK.

Patch-stage acceptance (2026-10-26): ReSeam CLI 0.31.0 loaded the permanently signed `giraffe-smartlauncher` bundle and applied `Enable Pro` plus its dependency to the developer's direct APK, which had SHA-256 `de80631a4b305c9ae46ea86195085a36cdf21371cc092801ef36b549b2586b5b`—identical to the owner's uploaded APK. The engine reported 1 applied, 1 dependency, 0 skipped and 0 failed, and produced a patched APK. Successful on-device import/install remains unproven. Evidence: [manual run 40309922742](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/40309922742). This build uses a separate bundle name to avoid possible Manager conflicts with legacy `giraffe-patches`; the conflict is a hypothesis, not a proven root cause.

The patch registers **Enable Pro** with the internal **Disable Smart Launcher signature check** dependency. Build/version checking and signer verification do **not** establish that a patched build installs/launches correctly on a particular phone. User acceptance is required before marking build 024 device-tested.

To build, manually dispatch `.github/workflows/smartlauncher-manual.yml`, download artifact `giraffe-smartlauncher-b024-reseam-0.31.0`, extract the `.reseam`, and import it directly into ReSeam Manager. No APKs or signing secrets are committed.

**Historical build 021:** The verified older source remains in `apps/smartlauncher/patch/` and in Git history, and the October 26 signed ReSeam 0.31.0 artifact is in successful run [40301721159](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/40301721159). Do not claim this build 024 source supports 021; different obfuscated owner classes require different version-scoped builds.
