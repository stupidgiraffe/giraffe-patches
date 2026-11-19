# Supreme Duelist Stickman 4.0.7 (584): ReSeam 0.42.2 experimental rebuild

This is an independent GPL-3.0 port inspired by MiguelNinja19/miguel-morphe-patches v1.14.0-dev.14.
Exact 4.0.7 ARM64 target only: libil2cpp.so size 68,348,408, normalized SHA256
453ed08e7450bb2aacdcb96d0d4573677e4b4247857bee15e9df9817197e8f8c.

Five opt-in entries:
- PairIP Java license bypass (unchanged).
- Remove ads local entitlement true (unchanged).
- Free rewarded weapons: all 3 direct S_RewardWeapon.ShowRewardedWeaponAd call sites NOPed. After each call the original handlers set the weapon bool-array entry true and close the panel. The old AdManager->UnlockWeapon redirect is now normalized and restored rather than used. Button may still SAY watch video, but should NOT play video.
- Coins: in GameManager constructor overwrite 4 instructions to initialize coin balance to 999,999 each launch; not actually infinite and may be overwritten later by other game code.
- Unlock weapons, skins, maps and battle modes: use exact v4.0.7 instruction locations; selected weapon and skin flags are written into the actual bool arrays and original unlocked UI branches taken; map and battle mode copied booleans forced true; S_RewardWeapon.dha / dvb popup build entry returns immediately. Colors, persistence and other progression not fully verified.

**Free IAP is NOT ported.** Original upstream patch reports fake BillingResult.OK with empty purchases, which does not confer entitlement; implementing this would create a fake or harmful feature. The other five are NOT all runtime verified; native exact-byte tests alone are insufficient. User installed game/data are untouched by build; the user must explicitly test.

ReSeam latest engine v0.42.2 and installed Manager 0.45.0 verified Nov 19 2026. All native changes are exact and opt-in; original SHA checked before any write, including support for previous 0.42.1 ad patch bytes. No APK/game binaries or signing key distributed.

Source: https://github.com/MiguelNinja19/miguel-morphe-patches/tree/v1.14.0-dev.14/patches/src/main/kotlin/com/Neurononfire/SupremeDuelist
