# Compatibility and tested provenance

**Current device-tested target:** Smart Launcher **6.6 build 021**, Android package `ginlemon.flowerfree`, version code `660210`.

| Item | Status | Evidence |
| --- | --- | --- |
| Smart Launcher 6.6 build 021 on Android 16 ARM64 | **Device-tested** | User patched, installed and used the app successfully on 2026-10-10 |
| ARM64-v8a APKM and universal APKM | **Same application DEX** | SHA-256 of `classes.dex` and `classes2.dex` matched across both supplied packages; only one variant was installation-tested |
| Smart Launcher 6.6 build 020 | **Not supported here** | Previous exploratory bundle was replaced |
| Smart Launcher builds 016–017 | **Not tested here** | Upstream [Morphe](https://github.com/hoo-dles/morphe-patches) may support earlier versions |
| Anything after build 021 | **Unknown** | Do not assume obfuscated bytecode remains compatible |

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
