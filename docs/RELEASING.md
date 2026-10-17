# Signing and releasing

A release means **both** a source build that produces a valid signed Reseam bundle **and** successful application/install on a real build-021 target. A toolchain upgrade alone is not an on-device test. The working historical binary is a baseline only.

## One-time setup

1. Install the matching [Reseam CLI](https://reseam.app/docs/engine/setup/) and generate an Ed25519 seed with `reseam bundle keygen --out bundle-signing.key`.
2. Store the seed securely offline. Convert it to **base64** for a GitHub Actions secret named `RESEAM_SIGNING_KEY_BASE64`; never publish the seed itself.
3. Set GitHub Actions repository **variable** `RESEAM_PUBLIC_KEY_HEX` to the public key derived by the CLI. This is public verification material; it must correspond to the secret seed.
4. Test the release build locally with `gradle bundle` and `reseam bundle list ... --trust <public-key>`.
5. Apply the exact resulting `.reseam` to a clean Smart Launcher **6.6 build 021** APKM, install it on Android 16, and check app launch and intended behavior.
6. Record testing in `docs/COMPATIBILITY.md`, including bundle SHA-256 and the new signer. Do not declare a version compatible based on compilation alone.

**The historical test signer's private key is not available.** Expect a one-time signer change when the official Giraffe Patches release key is generated. Treat the new key as permanent thereafter.

## GitHub workflow and Manager automatic updates

The [stable update index](https://github.com/stupidgiraffe/giraffe-patches/releases/latest/download/patches.json) is hosted as a GitHub Release asset; it starts working after the first production-signed release. Users must **add the `patches.json` URL as a source** in Reseam Manager rather than importing a standalone preview `.reseam`. Reseam Manager checks the index for releases; it only trusts the same signing key. Do not publish ephemeral preview keys into this feed.

The workflow automatically restores the previous stable release's `patches.json` before extending it. It refuses a signer mismatch or a reused release tag; each old `.reseam` stays immutable. Pushing an approved `vX.Y.Z` tag runs the workflow automatically; manual dispatch remains available. Do **not** tag an untested engine update just to satisfy Manager.

Daily engine monitoring prepares a review PR when a newer Reseam SDK/CLI is published, but deliberately does not merge, sign or release it without compatibility verification. Repository Actions must allow creating pull requests for the updater bot.

Use the manually triggered **Release** workflow on a committed, reviewed version after configuring the secret and variable. The workflow pins CLI and SDK to **0.26.2**, compiles, signs, inspects the bundle, restores release history and emits cumulative `patches.json` alongside the bundle.

A release must not be triggered until the new key/bundle has been device-tested. **Do not force publication after a compilation failure.** Keep old release URLs immutable.

The [Reseam publish command](https://reseam.app/docs/engine/publish/) creates the Manager index. The release workflow preserves history automatically, and verifies its pinned public key.

## Before tagging

- [ ] Gradle source compilation succeeds
- [ ] Reseam archive signature and `bundle list` succeeds
- [ ] Build-021 patch dry run succeeds
- [ ] Device patch/install/launch succeeds
- [ ] Compatibility record updated to identify that exact bundle
- [ ] Source code and included upstream notices reviewed
- [ ] No upstream proprietary APK, private seed, secrets, or personal data committed
- [ ] Release notes identify `6.6 build 021 (660210)` explicitly
