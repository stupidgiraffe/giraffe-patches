<p align="center">
  <img src="assets/giraffe-patches-banner.svg" alt="Giraffe Patches — independent Android patches for Reseam" width="100%">
</p>

<p align="center">
  <a href="https://reseam.app/"><img src="https://img.shields.io/badge/Reseam-0.20.x-8c77ff?style=flat-square" alt="Reseam 0.20.x"></a>
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
<tr><td>Reseam bundle engine</td><td><code>0.20.0</code> (working reference bundle)</td></tr>
<tr><td>Upstream comparison</td><td>Morphe lists 6.6 build 016; this is an <em>independent port</em> with a build-021 matcher correction.</td></tr>
</table>

> **Build 021 is the latest release verified by this project, not a promise that every future Smart Launcher release is compatible.** The latest publicly listed upload was also build 021 when checked on 2026-10-10 ([APKMirror release feed](https://www.apkmirror.com/uploads/?appcategory=smart-launcher)). Updates can change obfuscated code and break a patch.

### What is in the bundle?

- **Disable Smart Launcher signature check:** prevents a specific integrity-check exit, needed by the following patch.
- **Enable Pro:** applies the purchase-item lifetime state change and triggers the existing premium-access event. It is dependent on the first patch.

Both are scoped to Smart Launcher `6.6 build 021`. **No Discord, YouTube, Instagram, or unrelated patches are included.** Existing patches shown as *skipped* in Reseam belong to other installed bundles.

## 📦 Installation

1. Obtain **Smart Launcher 6.6 build 021** from your own trusted source; keep an unmodified backup.
2. Open [Reseam Manager](https://reseam.app/) and import a **signed `.reseam` bundle** from this repository's **[Releases](https://github.com/stupidgiraffe/giraffe-patches/releases)** page, once a public release is posted.
3. Review and trust the bundle **public signing key**. Enable **Enable Pro** and its Smart Launcher dependency; disable unrelated universal patches during troubleshooting.
4. Patch your original APKM/APK. Install the resulting package and verify on your device.

**Release status:** The source and CI compile successfully, and a **signed preview build** can be downloaded from the [latest successful Preview workflow](https://github.com/stupidgiraffe/giraffe-patches/actions/workflows/preview.yml) (open a run → **Artifacts** → `giraffe-patches-preview-reseam` → extract the single `.reseam` file). Its signing key is **temporary**; this newly compiled Giraffe-branded bundle has **not yet been tested on-device**. The previously working signed test bundle's provenance is recorded in [Compatibility & provenance](docs/COMPATIBILITY.md). Stable downloads will appear on [Releases](https://github.com/stupidgiraffe/giraffe-patches/releases) only after a permanent signing key and real device verification.

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

The source lives in `apps/smartlauncher/patch/` and uses the official [Reseam patch SDK](https://reseam.app/docs/engine/bundles/). The workspace plugin is pinned to `0.20.0` for the initial port. Run `gradle bundle` with the matching Reseam CLI, Android SDK, and a private signing key; see [BUILD.md](docs/BUILD.md) for exact requirements.

We keep the compatibility matcher tied to **the premium-access-change owner class**, not an ambiguous `"lifetime"` string that matches two classes in build 021. That correction is the main difference from the unsuccessful early port.

## 🙌 Credits & licensing

Smart Launcher is owned by its respective developers. The underlying patch logic is adapted from **[hoo-dles/morphe-patches](https://github.com/hoo-dles/morphe-patches)** (Smart Launcher Enable Pro and signature-check patches), distributed under GPL-3.0 with upstream notices. The independent Reseam port and matcher correction are documented in [NOTICE.md](NOTICE.md). Code is offered under **GPL-3.0-or-later** with applicable attribution and upstream notices; do not imply an endorsement from Reseam or Morphe.

<p align="center">
  <a href="https://buymeacoffee.com/stupidgiraffe"><img src="https://img.shields.io/badge/☕_Support_development-Buy_Me_a_Coffee-F6C85F?style=for-the-badge" alt="Support development"></a>
</p>
<p align="center"><sub>Made for the community. Contributions, bug reports, and device compatibility results welcome.</sub></p>
