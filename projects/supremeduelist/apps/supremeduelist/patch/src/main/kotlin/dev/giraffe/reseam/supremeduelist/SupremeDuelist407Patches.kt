// SPDX-License-Identifier: GPL-3.0-or-later
// MiguelNinja19/miguel-morphe-patches v1.14.0-dev.14 (GPL-3.0)
// ReSeam adaptation for exact Supreme Duelist 4.0.7 (584), arm64 only.
@file:Suppress("unused")
package dev.giraffe.reseam.supremeduelist

import app.reseam.patch.*
import java.security.MessageDigest

private val SUPREME_DUELIST = "com.Neurononfire.SupremeDuelist"("4.0.7", 584)
private const val LIB_PATH = "lib/arm64-v8a/libil2cpp.so"
private const val ORIGINAL_SHA256 = "453ed08e7450bb2aacdcb96d0d4573677e4b4247857bee15e9df9817197e8f8c"
private const val LIB_BYTES = 68348408

private fun hex(value: String): ByteArray = value.split(' ').map { it.toInt(16).toByte() }.toByteArray()
private data class NativeEdit(val offset: Int, val expected: ByteArray, val replacement: ByteArray)
private val REWARDED = NativeEdit(0x193EA04, hex("fe 5f bd a9"), hex("34 ed ff 17"))
private val ADS_REMOVED = NativeEdit(0x1AA1138, hex("fe 0f 1e f8 f4 4f 01 a9"), hex("20 00 80 52 c0 03 5f d6"))
private val NATIVE_EDITS = listOf(REWARDED, ADS_REMOVED)

private fun sliceEquals(data: ByteArray, offset: Int, expected: ByteArray) =
    offset >= 0 && offset + expected.size <= data.size &&
        expected.indices.all { data[offset + it] == expected[it] }

// Digest original ELF with any of this bundle's exact known edits normalized,
// enabling independent patch ordering while refusing all unrecognized binaries.
private fun normalizedSha256(data: ByteArray): String {
    require(data.size == LIB_BYTES) { "Wrong IL2CPP binary length: " + data.size }
    val hash = MessageDigest.getInstance("SHA-256")
    var previous = 0
    for (edit in NATIVE_EDITS.sortedBy { it.offset }) {
        require(sliceEquals(data, edit.offset, edit.expected) ||
            sliceEquals(data, edit.offset, edit.replacement)) {
            "Native method signature mismatch at 0x" + edit.offset.toString(16)
        }
        hash.update(data, previous, edit.offset - previous)
        hash.update(edit.expected)
        previous = edit.offset + edit.expected.size
    }
    hash.update(data, previous, data.size - previous)
    return hash.digest().joinToString("") { "%02x".format(it.toInt() and 255) }
}
private fun applyNative(data: ByteArray, edit: NativeEdit): ByteArray {
    require(normalizedSha256(data) == ORIGINAL_SHA256) { "Expected exact v4.0.7 arm64 libil2cpp.so" }
    require(sliceEquals(data, edit.offset, edit.expected) ||
        sliceEquals(data, edit.offset, edit.replacement)) { "Target instruction changed" }
    edit.replacement.copyInto(data, edit.offset)
    return data
}
private fun FileScope.patchExactNative(edit: NativeEdit) {
    val matches = components().filter { LIB_PATH in component(it).list() }
    require(matches.size == 1) { "Expected one ARM64 libil2cpp.so split, found " + matches.size }
    val split = component(matches.single())
    val original = split.read(LIB_PATH) ?: error("Missing " + LIB_PATH)
    split.writeStored(LIB_PATH, applyNative(original, edit))
}

private val licenseClient = klass("com.pairip.licensecheck.LicenseClient")
private val checkLicense = licenseClient.method("checkLicense") {
    params(Type.Context)
    returns(Type.Void)
}
private val initializeLicenseCheck = licenseClient.method("initializeLicenseCheck") {
    params()
    returns(Type.Void)
}
val bypassPairIp = patch(SupremeDuelistStrings.bypassPairip) {
    description(SupremeDuelistStrings.bypassPairipDescription)
    compatibleWith(SUPREME_DUELIST)
    enabledByDefault(false)
    execute {
        checkLicense.alwaysReturn()
        initializeLicenseCheck.alwaysReturn()
    }
}
val removeAds = patch(SupremeDuelistStrings.removeAds) {
    description(SupremeDuelistStrings.removeAdsDescription)
    compatibleWith(SUPREME_DUELIST)
    enabledByDefault(false)
    execute { files.patchExactNative(ADS_REMOVED) }
}
val freeAdRewards = patch(SupremeDuelistStrings.freeAdRewards) {
    description(SupremeDuelistStrings.freeAdRewardsDescription)
    compatibleWith(SUPREME_DUELIST)
    enabledByDefault(false)
    execute { files.patchExactNative(REWARDED) }
}
