#!/usr/bin/env python3
"""Field-by-field, order-normalized diff between two `gt-materials.json` dumps.

Stage 04 uses this to compare a fresh dump (regenerated with the MaterialLib-backed
`MaterialsLegacyBridge` active) against the archived canonical dump captured before the bridge
existed. Both dumps share the exact same schema (`MaterialDataDump.dumpGtMaterial`), so this is a
generic structural diff rather than a semantic remapping like `check_parity.py` (which compares two
different schemas, `gt-materials.json` and `ml-materials.json`).

List-typed fields are compared order-normalized (as sorted multisets) because dump order reflects
registry/iteration order, not anything semantically meaningful; scalar and fixed-position fields
(`rgba`, `moltenRgba`, `fluids`, `enchants`) are compared positionally.

Usage: python scripts/mu/diff_dumps.py [canonical.json] [actual.json]
Defaults to working/material-unification/dumps/gt-materials.json vs scripts/mu/dumps/gt-materials.json.
"""

import json
import sys
from pathlib import Path

SCRIPT_DIR = Path(__file__).resolve().parent
REPO_ROOT = SCRIPT_DIR.parent.parent
DEFAULT_CANONICAL = REPO_ROOT.parent / "working/material-unification/dumps/gt-materials.json"
DEFAULT_ACTUAL = SCRIPT_DIR / "dumps/gt-materials.json"

FLOAT_TOLERANCE = 1e-6

# List-shaped fields whose element order is not meaningful; compared as sorted multisets.
UNORDERED_LIST_FIELDS = {
    "subTags",
    "generatedPrefixes",
    "addedPrefixes",
    "removedPrefixes",
    "oreByProducts",
}
# List-of-dict fields; compared as sorted multisets of a stable sort key.
UNORDERED_DICT_LIST_FIELDS = {
    "composition": lambda e: (e["material"], e["amount"]) if e else None,
    "aspects": lambda e: (e["name"], e["amount"]),
}


def load(path):
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def sort_key(value):
    return json.dumps(value, sort_keys=True)


def diff_scalar(path, expected, actual, errors):
    if isinstance(expected, float) or isinstance(actual, float):
        if expected is None or actual is None or abs(expected - actual) > FLOAT_TOLERANCE:
            errors.append(f"{path}: expected {expected!r}, got {actual!r}")
        return
    if expected != actual:
        errors.append(f"{path}: expected {expected!r}, got {actual!r}")


def diff_unordered_list(path, expected, actual, errors):
    e_sorted = sorted(expected or [])
    a_sorted = sorted(actual or [])
    if e_sorted != a_sorted:
        missing = sorted(set(e_sorted) - set(a_sorted))
        extra = sorted(set(a_sorted) - set(e_sorted))
        errors.append(f"{path}: missing {missing}, extra {extra}")


def diff_unordered_dict_list(path, expected, actual, key_fn, errors):
    e_sorted = sorted((key_fn(e) for e in (expected or [])), key=sort_key)
    a_sorted = sorted((key_fn(e) for e in (actual or [])), key=sort_key)
    if e_sorted != a_sorted:
        errors.append(f"{path}: expected {e_sorted}, got {a_sorted}")


def diff_material(name, expected, actual):
    errors = []
    keys = sorted(set(expected.keys()) | set(actual.keys()))
    for key in keys:
        path = f"{name}.{key}"
        e_val = expected.get(key)
        a_val = actual.get(key)
        if key in UNORDERED_LIST_FIELDS:
            diff_unordered_list(path, e_val, a_val, errors)
        elif key in UNORDERED_DICT_LIST_FIELDS:
            diff_unordered_dict_list(path, e_val, a_val, UNORDERED_DICT_LIST_FIELDS[key], errors)
        elif isinstance(e_val, dict) or isinstance(a_val, dict):
            for subkey in sorted(set((e_val or {}).keys()) | set((a_val or {}).keys())):
                diff_scalar(f"{path}.{subkey}", (e_val or {}).get(subkey), (a_val or {}).get(subkey), errors)
        else:
            diff_scalar(path, e_val, a_val, errors)
    return errors


def main():
    args = sys.argv[1:]
    canonical_path = Path(args[0]) if len(args) > 0 else DEFAULT_CANONICAL
    actual_path = Path(args[1]) if len(args) > 1 else DEFAULT_ACTUAL

    canonical = load(canonical_path)
    actual = load(actual_path)

    canonical_by_name = {m["name"]: m for m in canonical}
    actual_by_name = {m["name"]: m for m in actual}

    missing = sorted(set(canonical_by_name) - set(actual_by_name))
    extra = sorted(set(actual_by_name) - set(canonical_by_name))

    total_with_diffs = 0
    total_compared = 0
    for name in sorted(set(canonical_by_name) & set(actual_by_name)):
        total_compared += 1
        errors = diff_material(name, canonical_by_name[name], actual_by_name[name])
        if errors:
            total_with_diffs += 1
            print(f"=== {name} ===")
            for e in errors:
                print(f"  {e}")

    print()
    print(f"diff_dumps.py: {len(canonical_by_name)} canonical, {len(actual_by_name)} actual materials")
    print(f"  compared {total_compared} materials present in both: {total_with_diffs} with differences")
    if missing:
        print(f"  missing from actual ({len(missing)}): {missing}")
    if extra:
        print(f"  extra in actual ({len(extra)}): {extra}")

    if total_with_diffs or missing or extra:
        sys.exit(1)
    print("  OK: dumps are equivalent")


if __name__ == "__main__":
    main()
