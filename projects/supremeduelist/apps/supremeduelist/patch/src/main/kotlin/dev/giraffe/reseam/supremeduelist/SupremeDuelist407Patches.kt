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
private fun hex(s: String): ByteArray = s.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
private data class NativeEdit(val offset: Int, val expected: ByteArray, val replacement: ByteArray, val previous: List<ByteArray> = emptyList())
private val REWARD_EDITS = listOf(
    NativeEdit(0x193EA04, hex("fe5fbda9"), hex("fe5fbda9"), listOf(hex("34edff17"))),
    NativeEdit(0x1B31C90, hex("5d33f897"), hex("1f2003d5")),
    NativeEdit(0x1B347D4, hex("8c28f897"), hex("1f2003d5")),
    NativeEdit(0x1B353E0, hex("8925f897"), hex("1f2003d5")),
)
private val ADS_EDITS = listOf(
    NativeEdit(0x1AA1138, hex("fe0f1ef8f44f01a9"), hex("20008052c0035fd6")),
)
private val COIN_EDITS = listOf(
    NativeEdit(0x19B7A60, hex("a8825fb81f01146b4a000054742a00b9"), hex("f4478852f401a072742a00b91f2003d5")),
)
private val UNLOCK_EDITS = listOf(
    NativeEdit(0x1A7E1D8, hex("08814039a8000034"), hex("2900805209810039")),
    NativeEdit(0x1A7FA78, hex("08814039080f0035686640f9"), hex("290080520981003977000014")),
    NativeEdit(0x1A80424, hex("08814039a8000034"), hex("2900805209810039")),
    NativeEdit(0x1A825E0, hex("5f692938"), hex("4b692938")),
    NativeEdit(0x1A82724, hex("cf696838"), hex("2f008052")),
    NativeEdit(0x1A82734, hex("ef031f2a"), hex("2f008052")),
    NativeEdit(0x1A85054, hex("08814039080f0035686640f9"), hex("290080520981003977000014")),
    NativeEdit(0x1A85ED8, hex("8d696938"), hex("2d008052")),
    NativeEdit(0x1A85EE8, hex("ed031f2a"), hex("2d008052")),
    NativeEdit(0x1B30EA8, hex("fe0f1bf8"), hex("c0035fd6")),
    NativeEdit(0x1B33800, hex("fe0f1bf8"), hex("c0035fd6")),
)
private val ALL_EDITS = (REWARD_EDITS + ADS_EDITS + COIN_EDITS + UNLOCK_EDITS).sortedBy { it.offset }
private fun matches(data: ByteArray, pos: Int, v: ByteArray): Boolean =
    pos >= 0 && pos + v.size <= data.size && v.indices.all { data[pos + it] == v[it] }
private fun normalizedSha256(data: ByteArray): String {
    require(data.size == LIB_BYTES) { "Wrong IL2CPP size: ${data.size}" }
    val h = MessageDigest.getInstance("SHA-256")
    var prior = 0
    for (e in ALL_EDITS) {
        require(prior <= e.offset) { "Overlapping native edits" }
        require(matches(data, e.offset, e.expected) || matches(data, e.offset, e.replacement) ||
            e.previous.any { matches(data, e.offset, it) }) {
            "Unexpected native instructions at 0x${e.offset.toString(16)}"
        }
        h.update(data, prior, e.offset - prior)
        h.update(e.expected)
        prior = e.offset + e.expected.size
    }
    h.update(data, prior, data.size - prior)
    return h.digest().joinToString("") { "%02x".format(it.toInt() and 255) }
}
private fun FileScope.patchExactNative(edits: List<NativeEdit>) {
    val matches = components().filter { LIB_PATH in component(it).list() }
    require(matches.size == 1) { "Expected one ARM64 libil2cpp.so split, found ${matches.size}" }
    val split = component(matches.single())
    val data = split.read(LIB_PATH) ?: error("Missing $LIB_PATH")
    require(normalizedSha256(data) == ORIGINAL_SHA256) { "Wrong build: requires exact v4.0.7 (584) ARM64" }
    for (e in edits) e.replacement.copyInto(data, e.offset)
    split.writeStored(LIB_PATH, data)
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
    execute { files.patchExactNative(ADS_EDITS) }
}
val freeAdRewards = patch(SupremeDuelistStrings.freeAdRewards) {
    description(SupremeDuelistStrings.freeAdRewardsDescription)
    compatibleWith(SUPREME_DUELIST)
    enabledByDefault(false)
    execute { files.patchExactNative(REWARD_EDITS) }
}

val unlimitedCoins = patch(SupremeDuelistStrings.unlimitedCoins) {
    description(SupremeDuelistStrings.unlimitedCoinsDescription)
    compatibleWith(SUPREME_DUELIST)
    enabledByDefault(false)
    execute { files.patchExactNative(COIN_EDITS) }
}
val unlockAll = patch(SupremeDuelistStrings.unlockAll) {
    description(SupremeDuelistStrings.unlockAllDescription)
    compatibleWith(SUPREME_DUELIST)
    enabledByDefault(false)
    execute { files.patchExactNative(UNLOCK_EDITS) }
}
