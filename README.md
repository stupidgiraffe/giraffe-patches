> **Current manually built bundles:** Smart Launcher **6.6 build 024 (660240)** and Icon Pack Studio **2.5 build 001 (250010)** for ReSeam Manager **0.32.0** (engine 0.32.0). Smart Launcher passed CLI patch-stage verification on 2026-10-29; Icon Pack Studio passed compilation, signer, and manifest checks but still needs APK patch-stage and device checks. Previously verified 0.31.0 artifacts remain historical. See `projects/smartlauncher` and `projects/iconpackstudio` for the isolated, manual-only projects.

> **Historical note:** Smart Launcher **6.6 build 024 (660240)** for ReSeam **0.31.0** is maintained in [`projects/smartlauncher`](projects/smartlauncher/README.md), with a manual-only signed build workflow. It has been bytecode-inspected and compiled, but needs a device patch/install check. Historical build 021 remains the last user-reported working target; root `apps/smartlauncher` is the legacy 021 source.

<p align="center">
  <img src="assets/giraffe-patches-banner.svg" alt="Giraffe Patches — independent Android patches for Reseam" width="100%">
</p>

<p align="center">
  <a href="https://reseam.app/"><img src="https://img.shields.io/badge/Reseam-0.26.x-8c77ff?style=flat-square" alt="Reseam 0.26.x"></a>
  <a href="https://www.gnu.org/licenses/gpl-3.0.html"><img src="https://img.shields.io/badge/license-GPL--3.0--or--later-45bfa3?style=flat-square" alt="GPL-3.0-or-later"></a>
  <a href="https://github.com/stupidgiraffe/giraffe-patches/actions/workflows/validate.yml"><img src="https://github.com/stupidgiraffe/giraffe-patches/actions/workflows/validate.yml/badge.svg" alt="Validation workflow"></a>
  <a href="https://buymeacoffee.com/stupidgiraffe"><img src="https://img.shields.io/badge/Support-Buy%20Me%20a%20Coffee-f6c960?style=flat-square" alt="Buy Me a Coffee"></a>
</p>

<p align="center"><strong>Small, focused Android patches. Honest compatibility reports. Signed releases.</strong><br>
Independent community project · Not affiliated with Reseam, Smart Launcher, or Morphe.</p>

---

## ⚡ Smart Launcher 6 — current supported target

<table>
<tr><td><b>Latest confirmed patchable</b></td><td><b>6.6 build 021</b> (version code <code>660210</code>)</td></tr>
<tr><td>Package</td><td><code>ginlemon.flowerfree</code></td></tr>
<tr><td>Device verification</td><td>Android 16, ARM64 phone; patch applied, installed, and launched (community tester, October 2026)</td></tr>
<tr><td>APK variants</td><td>ARM64 and universal APKM packages have matching DEX bytecode. Patched installation confirmed on one device, <em>not</em> independently on every variant.</td></tr>
<tr><td>Reseam bundle engine</td><td><code>0.26.2</code> (current source builds); <code>0.20.0</code> (historical device-tested bundle)</td></tr>
<tr><td>Upstream comparison</td><td>Morphe lists 6.6 build 016; this is an <em>independent port</em> with a build-021 matcher correction.</td></tr>
</table>

> **Build 021 is the latest release verified by this project, not a promise that every future Smart Launcher release is compatible.** The latest publicly listed upload was also build 021 when checked on 2026-10-10 ([APKMirror release feed](https://www.apkmirror.com/uploads/?appcategory=smart-launcher)). Updates can change obfuscated code and break a patch.

### What is in the bundle?

- **Disable Smart Launcher signature check:** prevents a specific integrity-check exit, needed by the following patch.
- **Enable Pro:** applies the purchase-item lifetime state change and triggers the existing premium-access event. It is dependent on the first patch.

Both are scoped to Smart Launcher `6.6 build 021`. **No Discord, YouTube, Instagram, or unrelated patches are included.** Existing patches shown as *skipped* in Reseam belong to other installed bundles.

## 📦 Installation and automatic updates

**Permanent bundle signing public key** (Ed25519; verify in Reseam Manager):

`9b5228381bdce62d03e9df9654f02c9ceb9c06fd67722aa201a6c01cc989d89b`

The private signing seed is backed up in APIVault and encrypted in GitHub Actions; it is never committed to this repository. The permanent-key build from CI was verified against this fingerprint, but still requires a fresh device patch/install test.

**Reseam Manager update feed** (available after the first permanent-key stable release):

`https://github.com/stupidgiraffe/giraffe-patches/releases/latest/download/patches.json`

Once the first stable release is published, add that **URL as a bundle source in Reseam Manager**, rather than importing a local `.reseam` file. Manager checks the index and installs new releases signed with the same trusted key. The URL returns 404 until the first stable release; the development preview artifacts are not an update source.

1. Obtain **Smart Launcher 6.6 build 021** from your own trusted source; keep an unmodified backup.
2. Open [Reseam Manager](https://reseam.app/) and, after the first stable release, **add the update feed URL above**. A manual `.reseam` import cannot receive updates.
3. Review and trust the bundle **public signing key**. Enable **Enable Pro** and its Smart Launcher dependency; disable unrelated universal patches during troubleshooting.
4. Patch your original APKM/APK. Install the resulting package and verify on your device.

**Release status:** CI builds a bundle for the current Reseam engine, and a **signed preview build** can be downloaded from the [latest successful Preview workflow](https://github.com/stupidgiraffe/giraffe-patches/actions/workflows/preview.yml) (open a run → **Artifacts** → `giraffe-patches-preview-reseam` → extract the single `.reseam` file). The preview uses the permanent signing key if configured in GitHub Actions, otherwise a temporary CI signer; this exact new bundle has **not yet been tested on-device**. The previously working signed test bundle's provenance is recorded in [Compatibility & provenance](docs/COMPATIBILITY.md). Stable downloads and the live update feed will appear on [Releases](https://github.com/stupidgiraffe/giraffe-patches/releases) only after configuring the permanent signing seed and real device verification. GitHub Actions checks upstream Reseam daily and opens review PRs for future engine changes; it cannot safely self-release untested patches.

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

The source lives in `apps/smartlauncher/patch/` and uses the official [Reseam patch SDK](https://reseam.app/docs/engine/bundles/). The workspace plugin and matching CLI are pinned to `0.26.2`. The historical device-tested bundle used 0.20.0. Run `gradle bundle` with the matching Reseam CLI, Android SDK, and a private signing key; see [BUILD.md](docs/BUILD.md) for exact requirements.

Smart Launcher build 021 contains multiple initializers with the `"lifetime"` string and 11 classes containing the premium-access broadcast string. To fail closed instead of matching unrelated code, the patch now resolves the **verified build-021 purchase-items owner** by exact descriptor and still constrains its initializer. Future Smart Launcher builds require separate analysis.

## 🙌 Credits & licensing

Smart Launcher is owned by its respective developers. The underlying patch logic is adapted from **[hoo-dles/morphe-patches](https://github.com/hoo-dles/morphe-patches)** (Smart Launcher Enable Pro and signature-check patches), distributed under GPL-3.0 with upstream notices. The independent Reseam port and matcher correction are documented in [NOTICE.md](NOTICE.md). Code is offered under **GPL-3.0-or-later** with applicable attribution and upstream notices; do not imply an endorsement from Reseam or Morphe.

<p align="center">
  <a href="https://buymeacoffee.com/stupidgiraffe"><img src="https://img.shields.io/badge/☕_Support_development-Buy_Me_a_Coffee-F6C85F?style=for-the-badge" alt="Support development"></a>
</p>
<p align="center"><sub>Made for the community. Contributions, bug reports, and device compatibility results welcome.</sub></p>
