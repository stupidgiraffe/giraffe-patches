// SPDX-License-Identifier: GPL-3.0-or-later
// Verified DEX targets: 2.4 build 001 (240010) and 2.5 build 001 (250010).
@file:Suppress("unused")
package dev.giraffe.reseam.iconpackstudio

import app.reseam.patch.*

// Semantic fingerprint survives the obfuscated class rename Lrq1; -> Lbx1;.
private val licenseStatusClass = klass("Icon Pack Studio LicenseStatus") {
    strings("LicenseStatus(hasProAccess=")
}

// Parameter zero initializes hasProAccess in both inspected versions.
private val licenseStatusConstructor = licenseStatusClass.method("<init>") {
    params(Type.Boolean, Type.Boolean)
    returns(Type.Void)
}

// The billing aggregate remains the same in 2.5.
private val purchaseFlagsClass = klass("ginlemon.flower.ipsLicense.billing.a")
private val purchaseActive = purchaseFlagsClass.method("b") {
    params()
    returns(Type.Boolean)
}

val enableIconPackStudioPro = patch("Enable Icon Pack Studio Pro") {
    description("Enable local Pro access in Icon Pack Studio 2.4 and 2.5 build 001.")
    compatibleWith("ginlemon.iconpackstudio"("2.4 build 001", "2.5 build 001"))
    execute {
        licenseStatusConstructor.before {
            param(0).assign(bool(true))
        }
        purchaseActive.alwaysReturn(true)
    }
}
