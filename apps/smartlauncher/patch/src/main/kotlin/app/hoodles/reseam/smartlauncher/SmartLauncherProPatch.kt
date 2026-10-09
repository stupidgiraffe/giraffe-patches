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

private val SMART_LAUNCHER = "ginlemon.flowerfree"("6.6 build 021")

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

// Build 021 contains TWO static initializers that mention "lifetime".
// Lhn8 is an unrelated enum initializer; Lmn8 owns the purchasable items.
// The obfuscated descriptors are NOT safe to bake into the shipped matcher.
// Instead locate the class that broadcasts the premium-access-changed event,
// then search for its constructor. This prevents the original ambiguous match.
private val purchaseItemsClass = klass("Smart Launcher purchase-items owner") {
    strings("ginlemon.action.hasPremiumAccessChanged")
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
