# Icon Pack Studio 2.4 and 2.5 build 001

Manual-build, version-scoped Reseam 0.29.0 patch. Supports:
- Icon Pack Studio 2.4 build 001 (240010)
- Icon Pack Studio 2.5 build 001 (250010)

The LicenseStatus class changed from `Lrq1;` to `Lbx1;`, but its distinctive string and constructor argument ordering are preserved. The purchase-state aggregate `ginlemon.flower.ipsLicense.billing.a.b():boolean` remains present. All three offline structural checks passed against the uploaded 2.5 APKM; 2.4 was structurally inspected previously, but its binary was not included in the latest upload.

This project modifies local license state and the purchase-active query. It does not ship APKs or private signing keys. It does not promise server-side entitlements or successful runtime behavior without patch/app testing.

Only the manually triggered **Build Icon Pack Studio Reseam (manual)** GitHub Actions workflow builds this isolated project. It uses the existing permanent bundle signing key, verifies that the public key matches the pinned value, and uploads `giraffe-iconpackstudio-reseam` as an Actions artifact. Download that ZIP, extract its **.reseam** bundle, then import the bundle into Reseam. The source ZIP is not a valid installable bundle.

Engine compatibility is bound to the Reseam release line, so the bundle must be rebuilt on future incompatible engine versions. No scheduled automation is required.
