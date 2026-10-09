# Build from source

This repository uses Reseam's [official bundle workspace plugin](https://reseam.app/docs/engine/bundles/), pinned to **0.20.0** to match the working reference's engine line. It is not a fork of Reseam Manager.

## Prerequisites

- JDK **17** (Reseam's documented baseline)
- Android SDK, `ANDROID_HOME` pointing at its root (with a platform `android.jar`)
- Gradle **8.14.3** or a verified compatible version (a wrapper is intentionally not included until its upstream binary can be reviewed)
- Reseam CLI **0.20.0**, installed as `reseam` or pointed to by `RESEAM_BIN`
- Private Ed25519 signing **seed** generated with the matching CLI. Never commit, upload, or echo it.

```bash
reseam bundle keygen --out "$HOME/.reseam/bundle-signing.key"
export RESEAM_BUNDLE_KEY="$HOME/.reseam/bundle-signing.key"
gradle :apps:smartlauncher:patch:classes
gradle bundle
```

Expected output: `build/reseam/giraffe-patches.reseam`. A first successful *compilation* validates SDK compatibility only; **do not promote it as the tested bundle** until a real build-021 APKM patch/install test succeeds.

To inspect a bundle (trust only the key you generated):

```bash
reseam bundle list build/reseam/giraffe-patches.reseam --trust YOUR_PUBLIC_KEY_HEX
```

Use the [Reseam patch CLI](https://reseam.app/docs/cli/patch/) `--dry-run` option for a non-writing check against your personally obtained Smart Launcher APKM. Then complete an on-device patch/install test before shipping.

## Matching invariants

- The owner class is resolved by `ginlemon.action.hasPremiumAccessChanged`.
- The `<clinit>` initializer search is **scoped to that class**, rather than the globally ambiguous string `lifetime`.
- The lifetime field is taken from the matched initializer after the known instruction sequence.
- The signature dependency targets only the matching `System.exit(int)` call.
- Both patches explicitly advertise only `6.6 build 021`.
- Do not introduce any patched APK, APKM, APKS, private key, or app signing credential into the repository.

## Porting more apps

Use a separate folder under `apps/<app>/patch/`. Each app gets its own tested versions and reproducible evidence. Prefer queries based on structural ownership over obfuscated class names. Attribution is mandatory for third-party patch logic.
