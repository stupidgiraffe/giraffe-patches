# Icon Pack Studio 2.4 build 001

Manual-build, version-scoped Reseam 0.29.0 patch. Source adapted by Giraffe Patches based on original Morphe/ReVanced research. Targets version code 240010 only.

The patch modifies a local LicenseStatus constructor argument and a purchase-active query; it does not replace billing systems or distribute any proprietary APKs.

This subproject has an isolated Reseam workspace. To build with the repository's permanent signing key, manually invoke GitHub Actions workflow **Build Icon Pack Studio Reseam (manual)**. It has no push, PR, tag, schedule or recurring triggers.

After a successful job, download the `giraffe-iconpackstudio-reseam` artifact ZIP; extract the single `.reseam` bundle and import that (not this source ZIP) into Reseam. The exact resulting bundle must be patch-tested against the original APKM to verify behavioral compatibility. Build success by itself is not a runtime license-state test.
