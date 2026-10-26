// SPDX-FileCopyrightText: 2026 Hoo-dles
// SPDX-FileCopyrightText: 2026 Giraffe Patches contributors
// SPDX-License-Identifier: GPL-3.0-or-later
//
// Reseam adaptation of the Hoo-dles Smart Launcher patches.
// The original logic and specific bytecode targets are credited in NOTICE.md.
// Smart Launcher is proprietary software; this repository contains no APKs.

@file:Suppress("unused")

package app.hoodles.reseam.smartlauncher

import app.reseam.patch.*
import app.reseam.patch.dex.AccessFlags
import app.reseam.patch.dex.Opcode
import app.reseam.patch.types.FieldRef

private val SMART_LAUNCHER = "ginlemon.flowerfree"("6.6 build 024")

private val signatureCheckMethod = method("Smart Launcher signature check") {
    strings("Not genuine apk. This may not stop humans but may stop machines.")
    calls {
        owner("java.lang.System")
        name("exit")
        params(Type.Int)
        returns(Type.Void)
    }
}

private val signatureExitCall = signatureCheckMethod.point("System.exit in signature check") {
    invokeStatic {
        owner("java.lang.System")
        name("exit")
        params(Type.Int)
        returns(Type.Void)
    }
}

val disableSmartLauncherSignatureCheck = patch {
    compatibleWith(SMART_LAUNCHER)
    execute {
        signatureExitCall.skipWhen { bool(true) }
    }
}

// Verified against supplied original Smart Launcher 6.6 build 024 (660240) APK.
// APK SHA256: de80631a4b305c9ae46ea86195085a36cdf21371cc092801ef36b549b2586b5b
// The purchase-items owner moved from Lmn8; in build 021 to Li89; in build 024.
// Li89;-><clinit>()V contains the unique purchase item "lifetime" marker,
// followed by Lmm8; construction and the SPUT_OBJECT Li89;.c field write.
// Li89;.a(Li89;,Context,boolean)V broadcasts the existing premium state event.
// Lmm8;.b():boolean is the getter; Lmm8;.a(Context,boolean):void is setter.
// The unrelated Lmn8; class still exists in 024; do not use it for this build.
// Matchers remain exactly version-scoped; other builds require new APK analysis.
private val purchaseItemsClass = classTarget("Smart Launcher purchase-items owner") {
    bytecode.findClass("Li89;")
        ?: error("Smart Launcher 6.6 build 024: verified purchase-items owner Li89; not found")
}

private val purchaseItemsCtor = method("Smart Launcher purchase-items initializer") {
    inClass(purchaseItemsClass)
    name("<clinit>")
    strings("lifetime")
    flags(AccessFlags.STATIC or AccessFlags.CONSTRUCTOR)
    opcode(Opcode.INVOKE_DIRECT)
    opcode(Opcode.SPUT_OBJECT)
    opcode(Opcode.NEW_INSTANCE)
}

private val lifetimeItemField = purchaseItemsCtor
    .point("lifetime marker") { string("lifetime") }
    .next { opcode(Opcode.INVOKE_DIRECT) }
    .next { opcode(Opcode.SPUT_OBJECT) }
    .field("lifetime PurchasableItem field")

private val purchasableItemClass = classTarget("PurchasableItem class") {
    bytecode.findClass(lifetimeItemField.type)
        ?: error("Smart Launcher: PurchasableItem class ${lifetimeItemField.type} not found")
}

private val purchasableItemGet = method("PurchasableItem getter") {
    inClass(purchasableItemClass)
    params()
    returns(Type.Boolean)
}

private val purchasableItemSet = method("PurchasableItem setter") {
    inClass(purchasableItemClass)
    params(Type.Context, Type.Boolean)
    returns(Type.Void)
}

private val getApp = method("Smart Launcher App getter") {
    flags(AccessFlags.PUBLIC or AccessFlags.STATIC)
    params()
    returns("Lginlemon/flower/App;")
}

private val purchaseItemsSingleton = fieldTarget("PurchaseItems singleton") {
    val cls = purchaseItemsClass.classDef
    val field = cls.staticFields
        .firstOrNull { it.fieldType == cls.descriptor }
        ?: error("Smart Launcher: PurchaseItems singleton field not found")
    FieldRef(cls.descriptor, field.name, field.fieldType)
}

private val premiumAccessChanged = method("premium access changed broadcaster") {
    inClass(purchaseItemsClass)
    flags(AccessFlags.STATIC)
    strings("ginlemon.action.hasPremiumAccessChanged")
}

val enableSmartLauncherPro = patch("Enable Pro") {
    description(
        "Marks the lifetime PurchasableItem active and broadcasts the app's " +
            "existing premium-access-changed event."
    )
    compatibleWith(SMART_LAUNCHER)
    dependsOn(disableSmartLauncherSignatureCheck)
    execute {
        purchaseItemsCtor.after {
            val lifetime = staticField(lifetimeItemField)
            val enabled = lifetime.call(purchasableItemGet)
            whenFalse(enabled) {
                val app = call(getApp)
                lifetime.call(purchasableItemSet, app, bool(true))
                call(
                    premiumAccessChanged,
                    staticField(purchaseItemsSingleton),
                    app,
                    bool(true),
                )
            }
        }
    }
}
