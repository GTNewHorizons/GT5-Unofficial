#!/usr/bin/env python3
"""Ad-hoc lookup helper for stage-12 caller migration: given material names on stdin (or argv), prints the
ML-generated shape set from `dumps/ml-materials.json`, so a call-site rewrite can be checked against the
material's actual generated shapes before trusting `MaterialLibAPI.getStack`/`getFluidStack` not to throw or
silently resolve to a different item than the legacy `GTOreDictUnificator.get`/`Materials.X.getY()` call did
(a material can generate a shape in MaterialLib while the legacy oredict slot for that same shape was already
claimed by a foreign mod's item -- shape presence alone does not guarantee identical behavior, see the
Materials.Infinity/Avaritia:Resource case in stage 12's ArcFurnaceRecipes fix).

Usage: python scripts/mu/check_shapes.py Steel Infinity DenseSteam ...
"""

import json
import sys
from pathlib import Path

SCRIPT_DIR = Path(__file__).resolve().parent
ML_PATH = SCRIPT_DIR / "dumps" / "ml-materials.json"


def main():
    names = sys.argv[1:] if len(sys.argv) > 1 else [line.strip() for line in sys.stdin if line.strip()]
    ml = json.load(open(ML_PATH, encoding="utf-8"))
    by_name = {m["name"]: m for m in ml}
    for name in names:
        m = by_name.get(name)
        if m is None:
            print(f"{name}: NOT IN ml-materials.json (legacy-only or unmapped -- do not migrate)")
            continue
        print(f"{name}: {sorted(m['shapes'])}")


if __name__ == "__main__":
    main()
