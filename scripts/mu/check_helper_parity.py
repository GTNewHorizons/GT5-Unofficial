#!/usr/bin/env python3
"""Checks `helper-parity.json` (a fresh dedicated-server dump, `run/server/material-dump/helper-parity.json`,
written by `LegacyHelperParity.verifyAgainstLegacy`) for mismatches between the live legacy `Materials` helper
methods and their proposed MaterialLib-native replacements, filtering out rows already accepted into
`EXPECTED_DIVERGENCES` so a genuine new divergence stands out from a known, already-triaged one.

Usage: python scripts/mu/check_helper_parity.py [fresh_helper_parity_path]
(defaults to run/server/material-dump/helper-parity.json)
"""

import json
import sys
from pathlib import Path

REPO_ROOT = Path(__file__).resolve().parent.parent.parent

# Emitted for every material both handle passes claim, not only disagreements, so it is reported rather than failed.
INFORMATIONAL_KEY = "handleOverrideOverlap"

# Rows accepted as known, intentional divergences, keyed by mismatch key ("localizedNameKey", "part", ...).
# Each value is a list of allowlisted entries: a bare material name for a key with no prefix/subTag scope, or a
# (material, prefix_or_subtag_name) tuple for the "generatesPrefix"/"subTag"/"part" keys. Populate an entry only
# after triaging a real mismatch this script reported -- never with guesses.
#
# Justifications for the entries below, each triaged from a reported mismatch:
#
# HAS_COLOR / handleMaterial / tooltips on the six `Any*` wildcard markers and `Steam`
#     `MaterialBuilder.constructMaterial` auto-adds HAS_COLOR whenever `mColor != null`, and `mColor` defaults to
#     the non-null `Dyes._NULL`, so every facade carries the tag while the MaterialLib backing carries only what
#     was declared. The same materials lose a computed tool handle and an element-derived formula. All are inert:
#     a marker generates no items, and `Steam` declares no GENERATION_FLAGS, so none reaches a tool recipe or an
#     item tooltip.
#
# CRYSTAL / CRYSTALLISABLE on `Salt`, `RockSalt`, `Spodumene`
#     Merged werkstoff/gregtech declarations carry these in SUB_TAGS but not FLAGS, and `MU.hasFlag` reads the
#     union of both. Verified inert rather than argued: with the sub-tag path resolved through MaterialLib flags,
#     `generatesPrefix` reports zero mismatches across the full prefix cross product and the recipe census is
#     unchanged. The gem tiers additionally require TRANSPARENT, which none of the three carries.
#
# `NULL` in nameDomainDiff
#     `LegacyNameDomainTable` has no `NULL` row because the dump that generated it skips the `_NULL` sentinel,
#     so freezing the domain flips `isLegacyNamed(_NULL)` to false. Every call site either filters `_NULL`
#     upstream or treats it as "not a real material", which is what false means.
#
EXPECTED_DIVERGENCES = {
    "unresolved": [],
    "localizedNameKey": [],
    "localizedName": [],
    "textureSet": [],
    "rgba": [],
    "validStones": [],
    "generatesPrefix": [],
    "subTag": [
        ["AnyBronze", "HAS_COLOR"],
        ["AnyCopper", "HAS_COLOR"],
        ["AnyCarbon", "HAS_COLOR"],
        ["AnyIron", "HAS_COLOR"],
        ["AnyRubber", "HAS_COLOR"],
        ["AnySyntheticRubber", "HAS_COLOR"],
        ["Steam", "HAS_COLOR"],
        ["Salt", "CRYSTAL"],
        ["Salt", "CRYSTALLISABLE"],
        ["RockSalt", "CRYSTAL"],
        ["RockSalt", "CRYSTALLISABLE"],
        ["Spodumene", "CRYSTAL"],
        ["Spodumene", "CRYSTALLISABLE"],
    ],
    "part": [],
    "id": [],
    "tooltips": ["AnyCopper", "AnyCarbon", "AnyIron", "Concrete", "Pumice"],
    "disabledHotIngots": [],
    "handleMaterial": [
        "AnyBronze",
        "AnyCopper",
        "AnyCarbon",
        "AnyIron",
        "AnyRubber",
        "AnySyntheticRubber",
        "Steam",
    ],
    "handleOverrideOverlap": [],
    "harvestLevels": [],
    "oldSubIdInDomain": [],
    "nameDomainDiff": ["NULL"],
}


def load_json(path):
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def is_expected(key, row):
    material = row.get("material")
    scope_value = row.get("prefix") or row.get("subTag")
    for entry in EXPECTED_DIVERGENCES.get(key, []):
        if isinstance(entry, (list, tuple)):
            if list(entry) == [material, scope_value]:
                return True
        elif entry == material:
            return True
    return False


def main():
    fresh_path = Path(sys.argv[1]) if len(sys.argv) > 1 else REPO_ROOT / "run/server/material-dump/helper-parity.json"

    data = load_json(fresh_path)
    summary = data.get("summary", {})
    mismatches = data.get("mismatches", {})

    print("mismatch counts by key:")
    for key in sorted(summary):
        print(f"  {key}: {summary[key]}")
    print()

    overlap = mismatches.get(INFORMATIONAL_KEY, [])
    if overlap:
        print(f"{INFORMATIONAL_KEY} ({len(overlap)} rows, informational -- materials both handle passes claim):")
        for row in overlap:
            print(f"  {row}")
        print()

    unexpected_total = 0
    for key in sorted(mismatches):
        if key == INFORMATIONAL_KEY:
            continue
        rows = mismatches[key]
        unexpected = [row for row in rows if not is_expected(key, row)]
        if not unexpected:
            continue
        unexpected_total += len(unexpected)
        print(f"{key}: {len(unexpected)} unallowlisted mismatch(es)")
        for row in unexpected:
            print(f"  {row}")
        print()

    disabled_hot_ingot_names = data.get("disabledHotIngotNames", {})
    if disabled_hot_ingot_names:
        print("disabledHotIngotNames (legacy field -> ML material name):")
        for name in sorted(disabled_hot_ingot_names):
            print(f"  {name}: {disabled_hot_ingot_names[name]}")
        print()

    if unexpected_total:
        print(f"FAIL: {unexpected_total} unallowlisted mismatch(es)")
        return 1

    print("PASS: no unallowlisted mismatches")
    return 0


if __name__ == "__main__":
    sys.exit(main())
