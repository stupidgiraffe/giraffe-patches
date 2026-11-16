# Compatibility and tested provenance

## Smart Launcher build 026 runtime regression (November 16, 2026)

**FAIL — original b026 bundle with ReSeam 0.41.0.** The user installed the exact patched build 026 APK (its SHA-256 matched ReSeam's output) and saw “Restore your premium license” / `ginlemon.flower.premium.LeavingPremiumActivity` repeatedly. ReSeam reporting 1 applied patch + 1 dependency was not sufficient. The initial one-time purchase-state write did not survive the app's later entitlement checks.

**V2 candidate, device acceptance pending.** A new exact-version patch changes the lifetime getter, setter and initialization write and skips the specific `HomeScreen.onResume` call launching `LeavingPremiumActivity`. Manual CI run [43884782691](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/43884782691) passed compilation, signature and exact-APK patch stage. V2 bundle SHA-256 `ec1d45248285b523f9f13cdaecde56ddd1152f80d1230c65ac5e51f2104604a3`. **No on-device import, installation, or Pro-function success has been verified for V2.** Preserve user data; the device has a gzip-tested root recovery archive (34 MB) at `Download/SmartLauncher_prepatch_b026_20261116.tar.gz`.


## Smart Launcher 6.6 build 026 — ReSeam engine 0.41.0 (2026-11-16)

- Uploaded APK: `ginlemon.flowerfree`, version `6.6 build 026`, code `660260`, SHA-256 `304eade55ce297c3f874c3fa745cae5dc6dce08f35185bbe8855fa3cc3e7bdf3`. The developer's direct-download APK had the **identical SHA-256**.
- Exact DEX fingerprint changes: `Lcn9;` is the purchase-items class (replaces `Li89;` in build 024); `Lcn9;-><clinit>()V` contains the `lifetime` item, uses `Lz09;.b():Z` and `Lz09;.a(Context,Z):V` for purchase state, and `Lcn9;.a(Lcn9;,Context,Z):V` broadcasts premium-access changes.
- ReSeam 0.41 requires `StringRef`-based patch metadata; this project added the patch module's generated string table and preserved the published patch ID.
- [Manual GitHub Actions run 43843411678](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/43843411678): **success**, engine 0.41.0, permanently signed, catalog verified, and real exact-APK patching succeeded (**1 applied, 1 dependency, 0 skipped, 0 failed**).
- `classes2.dex` and `classes3.dex` remained byte-for-byte unchanged between original and patched APK.
- Bundle SHA-256 `b811064d297dd9b383fcd1e8163e2e2c3a3a9a3fdefe049609d3f686b3a27a9d`; permanent Ed25519 public key `9b5228381bdce62d03e9df9654f02c9ceb9c06fd67722aa201a6c01cc989d89b`.
- **Evidence level: CLI patch-stage verified, NOT device-verified.** The new bundle has not yet been imported/installed/used on the phone, and Pro activation, backup creation, and restore functionality have not been runtime-tested. The previous native backup problem was due to missing folder access, not a verified patch defect. Do not wipe or reset launcher data.

Older compatibility records below are historical and must not be read as current b026 support.


## Smart Launcher build 024, ReSeam 0.31.0 (2026-10-26)

Uploaded APK was inspected directly, not identified from filename: package `ginlemon.flowerfree`, version code `660240`, version name `6.6 build 024`, SHA-256 `de80631a4b305c9ae46ea86195085a36cdf21371cc092801ef36b549b2586b5b`. The premium purchase-items owner was reverified as `Li89;`, with its `lifetime` item in `Li89;.c:Lmm8;` and its premium-state broadcaster. The old `Lmn8;` descriptor in build 024 is **unrelated** and must not be reused. A version-pinned isolated ReSeam 0.31.0 Kotlin workspace lives in `projects/smartlauncher`, built only by the manual Smart Launcher workflow. **Compilation and signed manifest inspection are not a full patch/install runtime test**. Preserve working build 021 until accepted.



**Historical device-tested target:** Smart Launcher **6.6 build 021**, Android package `ginlemon.flowerfree`, version code `660210`.

**Historical source toolchain:** Reseam SDK and CLI **0.26.2**. The previous successful phone test used a **0.20.0** bundle; a newly built 0.26.2 bundle still needs an on-device retest.

| Item | Status | Evidence |
| --- | --- | --- |
| Smart Launcher 6.6 build 021 on Android 16 ARM64 | **Device-tested** | User patched, installed and used the app successfully on 2026-10-10 |
| ARM64-v8a APKM and universal APKM | **Same application DEX** | SHA-256 of `classes.dex` and `classes2.dex` matched across both supplied packages; only one variant was installation-tested |
| Smart Launcher 6.6 build 020 | **Not supported here** | Previous exploratory bundle was replaced |
| Smart Launcher builds 016–017 | **Not tested here** | Upstream [Morphe](https://github.com/hoo-dles/morphe-patches) may support earlier versions |
| Builds newer than 021 | **See version-specific entries above** | Each build must be independently re-fingerprinted and patch-tested |

## 0.26.4 device regression found on October 19, 2026

The permanently signed 0.26.2 SDK preview (`0b80f0a205830db95e9056b2b789ac3e121342ef785e98c5155ca766ae503bb9`) was imported into Reseam Manager 0.22.3, engine 0.26.4, on the Android 16 tester phone. A fresh **Patch again** failed with `11 classs matched 'Smart Launcher purchase-items owner'`. The old `klass { strings("ginlemon.action.hasPremiumAccessChanged") }` query matches any class containing the event string, including numerous receivers and activities. No install was attempted and the working installation was preserved.

**Verified patch-stage fix:** PR [#5](https://github.com/stupidgiraffe/giraffe-patches/pull/5) resolves the verified `Lmn8;` owner from the version-pinned APK. CI compiled the updated patch, and the permanently signed bundle (SHA-256 `6fe4d630d52336172b9bfceb5786934a1d4eceb5a93a52f5e0b0d7700c7b9903`) was imported on the Android 16 tester. **Patch again succeeded:** Manager wrote a new `base.apk` on 2026-10-19 06:14:42 UTC and recorded `Enable Pro` in `patched.json`. This is a **patch-stage test only**; the new APK was NOT installed or launched. The old installation and a local 21 MB rollback copy were preserved.

**Additional limitation:** on the next Manager app startup, the locally imported Giraffe bundles disappeared from `bundles.json` and Manager showed an outdated/removed-source warning. Automatic updates and stable publication require proving the network source works and completing device install/launch verification.

## Permanently signed CI candidate (device test pending)

- Engine: **0.26.2**
- Workflow: [signed preview run #39062693018](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/39062693018)
- Bundle SHA-256: `0b80f0a205830db95e9056b2b789ac3e121342ef785e98c5155ca766ae503bb9`
- Permanent Ed25519 public key: `9b5228381bdce62d03e9df9654f02c9ceb9c06fd67722aa201a6c01cc989d89b`
- Signature and manifest: independently verified; CI completed with permanent-key fingerprint validation.
- Local phone copy: `Download/giraffe-patches-permanent-key-test.reseam` (checksum matched).
- **Status: build-validated, NOT yet patch/install/device-tested**. Do not release as stable on this evidence alone.

## Working on-device reference (not a reproducible release yet)

A signed exploratory bundle **did patch the user's build-021 APKM successfully**. Its immutable reference values:

- Bundle file: `smart-launcher-6.6-b021-verified-matcher.reseam`
- SHA-256: `9e98a65589c6d032523bae6da3f5269b949a50ab9f050b4f471a232170deef14`
- Ed25519 public signer: `a7f67faa9c134e05b3e5c1d49ce6199928aea202dc529b7f965cd1b9d59a9436`
- Engine recorded in bundle manifest: `0.20.0`
- Legacy bundle identifier: `smart-launcher-hoodles-b021-fixed`

The original **private** signing key is not stored in this repo. These reference hashes are for provenance, **not a public release link**. Recompiling source under the new `giraffe-patches` bundle identifier changes metadata and requires a fresh test and a permanent release key.

### Diagnosis that made build 021 work

The first Reseam port stopped at `2 methods matched 'Smart Launcher purchase-items initializer'` with obfuscated methods `Lhn8;-><clinit>()V` and `Lmn8;-><clinit>()V`. The fix scopes the initializer to the class broadcasting `ginlemon.action.hasPremiumAccessChanged`. For the tested APKM that resolves to `Lmn8`; `Lhn8` is the unrelated string match. No hardcoded obfuscated descriptor is used in source.

The signature-check dependency applied before that failure. The working archive passed Ed25519 signature and payload SHA-256 verification; **that does not prove a new build from this repository is identical**.

## Reporting new compatibility

Open a [compatibility issue](https://github.com/stupidgiraffe/giraffe-patches/issues/new/choose) with app version, version code, Reseam version, package format, architecture, Android version, and the **last failed patch line**. Never upload user data, account tokens, or proprietary APKs publicly.

**Sources for public releases:** [APKMirror Smart Launcher uploads](https://www.apkmirror.com/uploads/?appcategory=smart-launcher) (last checked 2026-10-10) and [Morphe changelog](https://github.com/hoo-dles/morphe-patches/blob/main/CHANGELOG.md).
