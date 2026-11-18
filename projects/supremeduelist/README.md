# Supreme Duelist 4.0.7 — ReSeam experimental port

Exact ARM64 APKM target: com.Neurononfire.SupremeDuelist 4.0.7 (584).
ReSeam engine 0.42.1 for Manager 0.45.0 (Manager's own SDK 0.42.0, same 0.42 release line).

Native source IL2CPP SHA256: 453ed08e7450bb2aacdcb96d0d4573677e4b4247857bee15e9df9817197e8f8c
Includes 3 opt-in patches: PairIP Java bypass, Remove Ads native get_AdsRemoved, experimental skip rewarded weapon ads by tail-call ShowRewardedWeaponAd(int)->UnlockWeapon(int).

Miguel's unlimited coins and unlock all patches are NOT included because their old offsets are invalid and their broad ARM64 signatures are ambiguous in v4.0.7. His free IAP patch is also not included: simulated billing success alone does not grant entitlements. Do not label the v4.0.7 target as runtime verified.

Source: MiguelNinja19/miguel-morphe-patches tag v1.14.0-dev.14, GPL-3.0. Preserve license and attribution.

Historical 0.41.0 build passed in run https://github.com/stupidgiraffe/giraffe-patches/actions/runs/44267937204, but was incompatible with Manager 0.45.0. The new 0.42.1 build requires separate compilation and validation. Permanent Ed25519 signing public key 9b5228381bdce62d03e9df9654f02c9ceb9c06fd67722aa201a6c01cc989d89b.
Historical 0.41.0 bundle SHA256 d209e551ce1bfd16879d4b6465b90426794dd4fa6d09bc88d7ccbfead83ecb1e. ReSeam runtime patch stage on v4.0.7 and game play were NOT tested. No app/game data changed and no scheduled CI enabled.

Status: experimental native and Java build deliverable; native coin/unlock/gameplay still pending. Original ReSeam workflows were restored on this branch after the manual build.
