// SPDX-License-Identifier: GPL-3.0-or-later
// Target: ginlemon.iconpackstudio versionName "2.4 build 001" versionCode 240010.
// The class fingerprint and obfuscated billing helper are verified against APKM build 240010.
@file:Suppress("unused")
package dev.giraffe.reseam.iconpackstudio

import app.reseam.patch.*

private val licenseStatusClass = klass("Icon Pack Studio LicenseStatus") {
    strings("LicenseStatus(hasProAccess=")
}

private val licenseStatusConstructor = licenseStatusClass.method("<init>") {
    params(Type.Boolean, Type.Boolean)
    returns(Type.Void)
}

// Obfuscated exact-build helper invoked by export, welcome and settings.
private val purchaseFlagsClass = klass("ginlemon.flower.ipsLicense.billing.a")
private val purchaseActive = purchaseFlagsClass.method("b") {
    params()
    returns(Type.Boolean)
}

val enableIconPackStudioPro = patch("Enable Icon Pack Studio Pro") {
    description("Adapted premium-state check for Icon Pack Studio 2.4 build 001.")
    compatibleWith("ginlemon.iconpackstudio"("2.4 build 001"))
    execute {
        licenseStatusConstructor.before {
            param(0).assign(bool(true))
        }
        purchaseActive.alwaysReturn(true)
    }
}
