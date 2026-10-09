"""Offline regression checks for published compatibility invariants."""
from pathlib import Path
import re
import tomllib
import unittest

ROOT = Path(__file__).resolve().parents[1]
PATCH = ROOT / "apps/smartlauncher/patch/src/main/kotlin/app/hoodles/reseam/smartlauncher/SmartLauncherProPatch.kt"


class TestRepoInvariants(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.source = PATCH.read_text(encoding="utf-8")
        cls.manifest = tomllib.loads((ROOT / "manifest.toml").read_text(encoding="utf-8"))

    def test_bundle_identity(self):
        self.assertEqual(self.manifest["bundle"]["name"], "giraffe-patches")
        self.assertEqual(self.manifest["bundle"]["format_version"], 1)

    def test_only_advertises_verified_app_version(self):
        self.assertIn('"ginlemon.flowerfree"("6.6 build 021")', self.source)
        self.assertNotRegex(self.source, r'compatibleWith\(.+build 016')

    def test_ambiguous_match_is_class_scoped(self):
        self.assertRegex(self.source, r'purchaseItemsClass\s*=\s*klass')
        self.assertIn('strings("ginlemon.action.hasPremiumAccessChanged")', self.source)
        initializer = re.search(
            r'purchaseItemsCtor\s*=\s*method\([\s\S]*?\n}', self.source
        )
        self.assertIsNotNone(initializer)
        self.assertIn("inClass(purchaseItemsClass)", initializer.group())
        self.assertIn('name("<clinit>")', initializer.group())
        self.assertNotIn("first()", initializer.group())
        self.assertIn("flags(AccessFlags.STATIC or AccessFlags.CONSTRUCTOR)", initializer.group())
        self.assertNotIn("Lmn8;", initializer.group())

    def test_patch_dependency(self):
        self.assertIn("val disableSmartLauncherSignatureCheck = patch", self.source)
        self.assertIn('val enableSmartLauncherPro = patch("Enable Pro")', self.source)
        self.assertIn("dependsOn(disableSmartLauncherSignatureCheck)", self.source)
        self.assertIn("FieldRef(cls.descriptor, field.name, field.fieldType)", self.source)

    def test_metadata_contains_real_build(self):
        readme = (ROOT / "README.md").read_text(encoding="utf-8")
        self.assertIn("660210", readme)
        self.assertIn("6.6 build 021", readme)
        self.assertIn("Device-tested", readme)

    def test_no_proprietary_binary_in_tree(self):
        disallowed = {".apk", ".apkm", ".apks", ".xapk", ".key", ".p12", ".jks", ".reseam"}
        found = [str(p.relative_to(ROOT)) for p in ROOT.rglob("*") if p.is_file() and p.suffix.lower() in disallowed]
        self.assertEqual(found, [])


if __name__ == "__main__":
    unittest.main()
