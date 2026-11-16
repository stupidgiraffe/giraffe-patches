# Smart Launcher 6.6 build 026 — ReSeam engine 0.41.0

## Runtime failure found; V2 candidate available (November 16, 2026)

**The original build 026 bundle was installed and failed on the user's Android 16 device.** ReSeam showed “Enable Pro” applied, but Smart Launcher opened “Restore your premium license” / `LeavingPremiumActivity` repeatedly. A successful CLI patch stage is NOT proof that Pro works at runtime. Do not use the original bundle or press **Disable all the premium features**; that may reset premium layout settings.

A revised V2 candidate, built and patch-stage tested in [manual GitHub run 43884782691](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/43884782691), corrects three runtime paths: it keeps the lifetime purchase result true on reads, blocks false writes for that specific lifetime item, and suppresses the matched home-screen launch of the destructive license-loss prompt. It also seeds the original persistent purchase state. Matching only b026 was preserved. Non-target DEX files were unchanged.

V2 signed bundle SHA-256: `ec1d45248285b523f9f13cdaecde56ddd1152f80d1230c65ac5e51f2104604a3`. Signer unchanged. **V2 is not yet on-device verified.** Import, install, Pro behavior, backup creation and restore still require testing. Do not advertise it as a working release until those checks succeed.

A gzip-tested 34 MB root recovery snapshot was preserved on the test phone at `Download/SmartLauncher_prepatch_b026_20261116.tar.gz`; no app data was cleared.

---

**Current supported app:** `ginlemon.flowerfree`, version **6.6 build 026** (versionCode `660260`). Built for **ReSeam Manager 0.44.0 / engine 0.41.0** using the matching SDK and CLI. This is a version-scoped `giraffe-smartlauncher` bundle signed with the project's existing permanent Ed25519 identity.

## Verification — 2026-11-16

- Original user-supplied APK SHA-256: `304eade55ce297c3f874c3fa745cae5dc6dce08f35185bbe8855fa3cc3e7bdf3`.
- The official Smart Launcher direct-download APK had **the identical SHA-256**, verified in the [one-time manual GitHub Actions run](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/43843411678).
- Direct DEX inspection found the **new** purchase-items owner `Lcn9;` (`Li89;` was build 024 only), with the `lifetime` initializer, item `Lz09;` getter/setter, premium-access broadcaster, and signature-check method.
- ReSeam CLI 0.41.0 successfully applied **Enable Pro (1 patch)** and the internal signature-check dependency (1 dependency), with **0 skipped and 0 failed**, producing a patched APK.
- The signed bundle's archive, engine version, app compatibility, manifest public key and patch catalog all passed verification.
- The original and patched `classes2.dex` and `classes3.dex` files were byte-for-byte identical. The patch rewrote `classes.dex` only. This reduces but does not eliminate backup-related risk.
- Signed `.reseam` SHA-256: `b811064d297dd9b383fcd1e8163e2e2c3a3a9a3fdefe049609d3f686b3a27a9d`.
- Public signing key: `9b5228381bdce62d03e9df9654f02c9ceb9c06fd67722aa201a6c01cc989d89b`.
- **Not yet tested on device:** successful ReSeam Manager import, install, launch, Pro behavior, backup creation and restore. Do not claim full runtime compatibility until confirmed.

## Build and import

Run only the manual `.github/workflows/smartlauncher-manual.yml` on the reviewed source, using engine `0.41.0` and permanent signing secret `RESEAM_SIGNING_KEY_BASE64`. The workflow fails closed unless the downloaded original APK matches the uploaded `660260` binary, real patching succeeds, and unaffected DEX files match. There are **no scheduled or push-triggered builds**.

From [successful workflow run 43843411678](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/43843411678), download the `giraffe-smartlauncher-b026-reseam-0.41.0` artifact, extract `giraffe-smartlauncher.reseam`, and import that `.reseam` file into ReSeam Manager 0.44.0. This patch bundle does not contain a proprietary APK.

**Backup caveat:** A previous on-device Smart Launcher backup failure was resolved by choosing a persistent folder through Smart Launcher Settings → Backup → Advanced settings → Select backup folder, granting Android document access. The new patch's backup **creation and restore** are not yet device-tested. Preserve current launcher data and exported backups before installing.

**History:** build 024 used `Li89;`, with valid prior ReSeam 0.31–0.33 builds. Build 021 used `Lmn8;`. Those old obfuscated identifiers do not apply to build 026. Historical tested references remain in [Compatibility & provenance](../../docs/COMPATIBILITY.md).
