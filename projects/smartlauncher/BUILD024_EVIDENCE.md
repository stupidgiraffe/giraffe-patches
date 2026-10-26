# Smart Launcher build 024 fingerprint evidence

Verification timestamp: 2026-10-26 JST.

Input: user-supplied `smart-launcher-6-6-6-build-024.apk`; SHA-256:
`de80631a4b305c9ae46ea86195085a36cdf21371cc092801ef36b549b2586b5b`.

Read from binary AndroidManifest.xml (not filename):
- package: `ginlemon.flowerfree`
- versionName: `6.6 build 024`
- versionCode: `660240`
- minSdk: 24; targetSdk: 36

Read from DEX method/class/field tables and code units:
- `classes.dex` `Li89;` defines `<clinit>()V`, with one `const-string "lifetime"` marker inside it, followed by `invoke-direct Lmm8;.<init>` and `sput-object Li89;.c:Lmm8;`.
- `Li89;` has one `Li89;.a(Li89;,Context,boolean)V` static method containing `ginlemon.action.hasPremiumAccessChanged`.
- `Lmm8;` defines `b():boolean` and `a(Context,boolean):void` alongside its constructor.
- `Li89;.a:Li89;` is a static self-type field (singleton).
- A public/static no-argument method returning `ginlemon.flower.App` remains present.
- One method contains the exact signature-warning text and an `invoke-static java.lang.System.exit(int)` call.
- `Lmn8;` also occurs in `classes2.dex` but is no longer the purchase-owner class; broad descriptor checks would be dangerously misleading.

Source selection: exact version-scoped `Li89;` matcher for build 024 only. Existing build 021 has `Lmn8;` purchase owner and remains separate.

Proof grade: binary structural inspection, not full ReSeam patch-stage or installed runtime validation. Do not report these last two as tested until executed.
