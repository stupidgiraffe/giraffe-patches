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

private val SMART_LAUNCHER = "ginlemon.flowerfree"("6.6 build 026")

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

// Verified from the uploaded original Smart Launcher 6.6 build 026 (660260) APK.
// APK SHA256: 304eade55ce297c3f874c3fa745cae5dc6dce08f35185bbe8855fa3cc3e7bdf3
// DEX inspection: lifetime purchase owner Lcn9; (build 024 had a different owner).
// Lcn9;-><clinit>()V contains the lifetime string, an Lz09; constructor
// invocation and SPUT_OBJECT into a static Lcn9; purchase-item field.
// Lz09;.b():Z is its unique getter; Lz09;.a(Context,Z):V is its setter.
// Lcn9;.a(Lcn9;,Context,Z):V broadcasts ginlemon.action.hasPremiumAccessChanged.
// Lcn9;.a:Lcn9; is the singleton. The APK also contains the signature warning
// and a public static App getter; these targets must pass the CLI patch test.
// This patch remains scoped to exactly version 660260. Backup code must remain
// untouched, and runtime backup/restore is a separate device acceptance check.
private val purchaseItemsClass = classTarget("Smart Launcher purchase-items owner") {
    bytecode.findClass("Lcn9;")
        ?: error("Smart Launcher 6.6 build 026: verified purchase-items owner Lcn9; not found")
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

// Runtime safeguard for build 026: the app re-checks entitlements after <clinit>.
// Re-assert only the lifetime purchase state when its Boolean property is read or updated,
// instead of changing the shared getter/setter for every PurchasableItem.
// The home-screen invalid-license dialog is a distinct activation path in this APK.
// Suppress only its startActivity call; never invoke the dialog's destructive reset action.
private val homeScreenClass = classTarget("Smart Launcher home screen") {
    bytecode.findClass("Lginlemon/flower/HomeScreen;")
        ?: error("Smart Launcher build 026 HomeScreen not found")
}

private val homeScreenResume = method("Home screen onResume") {
    inClass(homeScreenClass)
    name("onResume")
    params()
    returns(Type.Void)
}

private val startPremiumLossDialog = homeScreenResume
    .point("LeavingPremiumActivity class reference") {
        opcode(Opcode.CONST_CLASS)
        type("Lginlemon/flower/premium/LeavingPremiumActivity;")
    }
    .next {
        invokeVirtual {
            owner("android.content.Context")
            name("startActivity")
            params("Landroid/content/Intent;")
            returns(Type.Void)
        }
    }

val enableSmartLauncherPro = patch(SmartLauncherPatchStrings.enablePro) {
    description(SmartLauncherPatchStrings.enableProDescription)
    compatibleWith(SMART_LAUNCHER)
    dependsOn(disableSmartLauncherSignatureCheck)
    execute {
        // The original one-shot initializer was insufficient for real installed build 026:
        // a later entitlement update could revoke the stored lifetime flag.
        purchasableItemGet.after {
            whenEqual(thisObject, staticField(lifetimeItemField)) { returnTrue() }
        }
        purchasableItemSet.before {
            whenEqual(thisObject, staticField(lifetimeItemField)) {
                param(1).assign(bool(true))
            }
        }
        // The separate resume-time revocation dialog can reset user premium settings.
        startPremiumLossDialog.skipWhen { bool(true) }
        purchaseItemsCtor.after {
            val lifetime = staticField(lifetimeItemField)
            // Always seed the backed state and notify listeners. The runtime read hook
            // returns true even before storage has been seeded, so no guard is valid here.
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
