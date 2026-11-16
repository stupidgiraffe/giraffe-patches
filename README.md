> **KNOWN REGRESSION — Smart Launcher 6.6 build 026 (Nov 16, 2026): The previous ReSeam engine 0.41.0 bundle successfully patched the APK but FAILED runtime Pro activation on a device, showing a restore-license warning. Do not treat it as a working release. A V2 candidate is [pending on-device acceptance](https://github.com/stupidgiraffe/giraffe-patches/pull/10). Preserve launcher data and backups; do not tap “Disable all the premium features” in the failing installation.**

> **Latest validated Smart Launcher patch:** **6.6 build 026** (`660260`) for **ReSeam Manager 0.44.0 / engine 0.41.0**. The [manual signed build](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/43843411678) applied to the **exact original APK** (1 patch + 1 dependency; 0 failures). On-device import/install/Pro and native backup/restore still need acceptance. Source and complete evidence: [projects/smartlauncher](projects/smartlauncher/README.md).

> **Icon Pack Studio is separate:** its last verified targets are 2.4/2.5, previously built for older ReSeam engines; **do not treat that artifact as compatible with engine 0.41.0 without rebuilding**. Older Smart Launcher builds 021 and 024 remain historical.

<p align="center">
  <img src="assets/giraffe-patches-banner.svg" alt="Giraffe Patches — independent Android patches for Reseam" width="100%">
</p>

<p align="center">
  <a href="https://reseam.app/"><img src="https://img.shields.io/badge/Reseam-0.41.0-8c77ff?style=flat-square" alt="Reseam 0.41.0"></a>
  <a href="https://www.gnu.org/licenses/gpl-3.0.html"><img src="https://img.shields.io/badge/license-GPL--3.0--or--later-45bfa3?style=flat-square" alt="GPL-3.0-or-later"></a>
  <a href="https://github.com/stupidgiraffe/giraffe-patches/actions/workflows/validate.yml"><img src="https://github.com/stupidgiraffe/giraffe-patches/actions/workflows/validate.yml/badge.svg" alt="Validation workflow"></a>
  <a href="https://buymeacoffee.com/stupidgiraffe"><img src="https://img.shields.io/badge/Support-Buy%20Me%20a%20Coffee-f6c960?style=flat-square" alt="Buy Me a Coffee"></a>
</p>

<p align="center"><strong>Small, focused Android patches. Honest compatibility reports. Signed releases.</strong><br>
Independent community project · Not affiliated with Reseam, Smart Launcher, or Morphe.</p>

---

## ⚡ Smart Launcher 6 — current supported target

<table>
<tr><td><b>CLI patch-stage verified target</b></td><td><b>6.6 build 026</b> (version code <code>660260</code>)</td></tr>
<tr><td>Package</td><td><code>ginlemon.flowerfree</code></td></tr>
<tr><td>Device verification</td><td>Pending. Original APK patched successfully in CLI; device installation and native backup/restore not tested.</td></tr>
<tr><td>Verified original APK</td><td>Developer direct APK matches the owner's uploaded SHA-256 exactly.</td></tr>
<tr><td>ReSeam bundle engine</td><td><code>0.41.0</code>, for Manager 0.44.0</td></tr>
<tr><td>Build workflow</td><td>Manual only; permanent signer, fail-closed patch-stage acceptance.</td></tr>
</table>

> **Build 026 has passed CLI patch-stage validation but not device acceptance.** Obfuscated matchers are exact-version scoped; do not force this bundle onto other app releases.

### What is in the bundle?

- **Disable Smart Launcher signature check:** prevents a specific integrity-check exit, needed by the following patch.
- **Enable Pro:** applies the purchase-item lifetime state change and triggers the existing premium-access event. It is dependent on the first patch.

Both are scoped to Smart Launcher `6.6 build 026`. **No Discord, YouTube, Instagram, or unrelated patches are included.** Existing patches shown as *skipped* in Reseam belong to other installed bundles.

## 📦 Installation and automatic updates

**Permanent bundle signing public key** (Ed25519; verify in Reseam Manager):

`9b5228381bdce62d03e9df9654f02c9ceb9c06fd67722aa201a6c01cc989d89b`

The private signing seed is backed up in APIVault and encrypted in GitHub Actions; it is never committed to this repository. The permanent-key build from CI was verified against this fingerprint, but still requires a fresh device patch/install test.

**Reseam Manager update feed** (available after the first permanent-key stable release):

`https://github.com/stupidgiraffe/giraffe-patches/releases/latest/download/patches.json`

Once the first stable release is published, add that **URL as a bundle source in Reseam Manager**, rather than importing a local `.reseam` file. Manager checks the index and installs new releases signed with the same trusted key. The URL returns 404 until the first stable release; the development preview artifacts are not an update source.

1. Obtain **Smart Launcher 6.6 build 026** from the developer, or use the original supplied APK. Preserve your installed launcher data and native backups.
2. Open [Reseam Manager](https://reseam.app/) and, after the first stable release, **add the update feed URL above**. A manual `.reseam` import cannot receive updates.
3. Review and trust the bundle **public signing key**. Enable **Enable Pro** and its Smart Launcher dependency; disable unrelated universal patches during troubleshooting.
4. Patch your original APKM/APK. Install the resulting package and verify on your device.

**Release status:** the dedicated [Smart Launcher manual workflow](https://github.com/stupidgiraffe/giraffe-patches/actions/runs/43843411678) built and patch-tested ReSeam engine 0.41.0 against the exact original build 026 APK. The unrelated legacy preview and release workflows are disabled. No recurring GitHub Actions are authorized.

> Reseam bundles contain patches, **not APKs**. We never distribute proprietary Smart Launcher packages, patched app APKs, private signing keys, or user data.

## 🦒 Why Giraffe Patches?

Compatibility should be specific and verifiable. Instead of labeling a patch “latest” indefinitely, each app has an explicit target version and evidence level. Version checks are useful signals, but **only an actual patch-and-install test earns a device-tested label**.

| Status | Meaning |
|---|---|
| **Device-tested** | The exact target was patched and launched on a real device |
| **Build-validated** | Source compiled and bundle passed structure/signature checks |
| **Untested** | Exploratory support, not claimed compatible |

Read the [compatibility record](docs/COMPATIBILITY.md), [build guide](docs/BUILD.md), [release procedure](docs/RELEASING.md), and [contributing guide](CONTRIBUTING.md).

## 🧰 Developers

The current build 026 source lives in `projects/smartlauncher/apps/smartlauncher/patch/` (legacy build 021 source remains in `apps/smartlauncher/patch/`) and uses the official [Reseam patch SDK](https://reseam.app/docs/engine/bundles/). The current Smart Launcher workspace plugin and matching CLI are pinned to `0.41.0`. The historical device-tested bundle used 0.20.0. Run `gradle bundle` with the matching Reseam CLI, Android SDK, and a private signing key; see [BUILD.md](docs/BUILD.md) for exact requirements.

Smart Launcher build 021 contains multiple initializers with the `"lifetime"` string and 11 classes containing the premium-access broadcast string. To fail closed instead of matching unrelated code, the patch now resolves the **verified build-021 purchase-items owner** by exact descriptor and still constrains its initializer. Future Smart Launcher builds require separate analysis.

## 🙌 Credits & licensing

Smart Launcher is owned by its respective developers. The underlying patch logic is adapted from **[hoo-dles/morphe-patches](https://github.com/hoo-dles/morphe-patches)** (Smart Launcher Enable Pro and signature-check patches), distributed under GPL-3.0 with upstream notices. The independent Reseam port and matcher correction are documented in [NOTICE.md](NOTICE.md). Code is offered under **GPL-3.0-or-later** with applicable attribution and upstream notices; do not imply an endorsement from Reseam or Morphe.

<p align="center">
  <a href="https://buymeacoffee.com/stupidgiraffe"><img src="https://img.shields.io/badge/☕_Support_development-Buy_Me_a_Coffee-F6C85F?style=for-the-badge" alt="Support development"></a>
</p>
<p align="center"><sub>Made for the community. Contributions, bug reports, and device compatibility results welcome.</sub></p>
