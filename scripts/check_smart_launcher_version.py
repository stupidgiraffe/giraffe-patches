#!/usr/bin/env python3
"""Advisory only: detects a potential newer public APKMirror upload.

Never interprets a discovered version as patch-compatible.
Fail-closed for page changes, rate limiting or unavailable upstream.
"""
import argparse
import json
import re
import sys
from urllib.request import Request, urlopen

FEED = "https://www.apkmirror.com/uploads/?appcategory=smart-launcher"
TESTED_BUILD = 21

def extract_builds(html: str) -> list[int]:
    return sorted({int(m) for m in re.findall(r"Smart Launcher 6[^<]{0,180}?6\.6\s+build\s+(\d+)", html, re.I)}, reverse=True)

def check(html: str) -> dict:
    builds = extract_builds(html)
    newest = max(builds) if builds else None
    return {
        "verified_build": TESTED_BUILD,
        "observed_latest_build": newest,
        "newer_version_observed": newest is not None and newest > TESTED_BUILD,
        "status": "parsed" if newest is not None else "unavailable",
        "url": FEED,
        "message": "A newer upload needs independent analysis; compatibility is not inferred." if newest and newest > TESTED_BUILD else "No new build verified; feed observations are not an exhaustive update guarantee."
    }

def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--html-file", help="offline fixture for tests")
    args = parser.parse_args()
    try:
        if args.html_file:
            with open(args.html_file, encoding="utf-8") as f:
                html = f.read()
        else:
            req = Request(FEED, headers={"User-Agent": "GiraffePatches-VersionWatch/0.1 (+https://github.com/stupidgiraffe/giraffe-patches)"})
            with urlopen(req, timeout=20) as res:
                html = res.read(2_000_000).decode("utf-8", errors="replace")
    except Exception as e:
        print(json.dumps({"verified_build": TESTED_BUILD, "status": "unavailable", "message": str(e)}))
        return 0
    result = check(html)
    print(json.dumps(result))
    return 0

if __name__ == "__main__":
    sys.exit(main())
