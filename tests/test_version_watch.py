import importlib.util
from pathlib import Path
import unittest

root = Path(__file__).resolve().parents[1]
spec = importlib.util.spec_from_file_location("version_watch", root / "scripts/check_smart_launcher_version.py")
module = importlib.util.module_from_spec(spec)
spec.loader.exec_module(module)


class TestVersionWatch(unittest.TestCase):
    def test_newer_upload_only_generates_advisory(self):
        report = module.check('<h2>Smart Launcher 6 ‧ Home Screen 6.6 build 022</h2>')
        self.assertTrue(report["newer_version_observed"])
        self.assertEqual(report["observed_latest_build"], 22)
        self.assertIn("compatibility is not inferred", report["message"])

    def test_same_version_is_not_new(self):
        report = module.check('<h2>Smart Launcher 6 ‧ Home Screen 6.6 build 021</h2>')
        self.assertFalse(report["newer_version_observed"])

    def test_page_change_is_unavailable(self):
        report = module.check("<h2>captcha</h2>")
        self.assertEqual(report["status"], "unavailable")
        self.assertIsNone(report["observed_latest_build"])


if __name__ == "__main__":
    unittest.main()
