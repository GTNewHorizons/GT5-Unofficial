#!/usr/bin/env python3
"""Diffs a fresh `ml-materials.json` dedicated-server dump against the pinned
`scripts/mu/dumps/ml-materials.json`, keyed by material name.

Declaration changes that only move where a shape or property is declared -- between a family and its
members, or between `build()` and a later `editMaterial` -- must leave the resolved registry identical.
This differ is the gate for that: it compares what MaterialLib actually resolved, not what the source
says.

By default only `shapes` is compared, because a family restructure is expected to change `families`
while leaving every material's resolved shape set untouched. `--all-fields` compares every field and
is the gate for property folding, where a reordered last-write-wins collapse would otherwise pass
silently.

List-valued fields are compared as sorted multisets: dump order follows registry iteration order and
carries no meaning.

Usage: python scripts/mu/diff_ml.py [--all-fields] [--field NAME] [fresh_path]
(fresh_path defaults to run/server/material-dump/ml-materials.json)
"""

import json
import sys
from collections import Counter
from pathlib import Path

SCRIPT_DIR = Path(__file__).resolve().parent
REPO_ROOT = SCRIPT_DIR.parent.parent
BASELINE_PATH = SCRIPT_DIR / "dumps" / "ml-materials.json"
DEFAULT_FRESH_PATH = REPO_ROOT / "run" / "server" / "material-dump" / "ml-materials.json"


def load_by_name(path):
    with open(path, encoding="utf-8") as f:
        return {entry["name"]: entry for entry in json.load(f)}


def normalize(value):
    """Sorted-multiset form of a list field, recursively, so registry iteration order never registers
    as a difference. Dict elements are keyed by their sorted item pairs."""
    if isinstance(value, list):
        return sorted((repr(normalize(v)) for v in value))
    if isinstance(value, dict):
        return {k: normalize(v) for k, v in sorted(value.items())}
    return value


def main():
    args = [a for a in sys.argv[1:]]
    all_fields = "--all-fields" in args
    if all_fields:
        args.remove("--all-fields")

    fields = None
    while "--field" in args:
        i = args.index("--field")
        fields = (fields or set()) | {args[i + 1]}
        del args[i : i + 2]

    fresh_path = Path(args[0]) if args else DEFAULT_FRESH_PATH
    base = load_by_name(BASELINE_PATH)
    fresh = load_by_name(fresh_path)

    print(f"baseline: {BASELINE_PATH}")
    print(f"fresh:    {fresh_path}")
    print(f"materials  baseline={len(base)}  fresh={len(fresh)}")

    removed = sorted(set(base) - set(fresh))
    added = sorted(set(fresh) - set(base))
    if removed:
        print(f"\nmaterials MISSING from fresh: {len(removed)}")
        for name in removed[:40]:
            print(f"  {name}")
    if added:
        print(f"\nmaterials NEW in fresh: {len(added)}")
        for name in added[:40]:
            print(f"  {name}")

    if fields is not None:
        compared = fields
    elif all_fields:
        compared = None
    else:
        compared = {"shapes"}

    differing = []
    for name in sorted(set(base) & set(fresh)):
        b, f = base[name], fresh[name]
        keys = set(b) | set(f) if compared is None else compared
        deltas = [
            (k, b.get(k), f.get(k)) for k in sorted(keys) if normalize(b.get(k)) != normalize(f.get(k))
        ]
        if deltas:
            differing.append((name, deltas))

    scope = "every field" if compared is None else ", ".join(sorted(compared))
    print(f"\ncompared: {scope}")
    print(f"materials with a delta: {len(differing)}")

    # Printed before the truncated per-material detail so a delta outside the first 60 materials still
    # shows up somewhere.
    gained, lost, scalar = Counter(), Counter(), Counter()
    for _, deltas in differing:
        for key, old, new in deltas:
            if isinstance(old, list) or isinstance(new, list):
                o, n = set(old or ()), set(new or ())
                for v in n - o:
                    gained[f"{key}:{v}"] += 1
                for v in o - n:
                    lost[f"{key}:{v}"] += 1
            else:
                scalar[key] += 1
    if gained:
        print("  gained:", dict(gained.most_common()))
    if lost:
        print("  lost:  ", dict(lost.most_common()))
    if scalar:
        print("  scalar fields changed:", dict(scalar.most_common()))

    for name, deltas in differing[:60]:
        print(f"\n  {name}")
        for key, old, new in deltas:
            print(f"    {key}:")
            print(f"      baseline: {old}")
            print(f"      fresh:    {new}")
    if len(differing) > 60:
        print(f"\n  ... and {len(differing) - 60} more")

    clean = not differing and not removed and not added
    print("\nRESULT:", "clean" if clean else "DELTAS PRESENT")
    return 0 if clean else 1


if __name__ == "__main__":
    sys.exit(main())
