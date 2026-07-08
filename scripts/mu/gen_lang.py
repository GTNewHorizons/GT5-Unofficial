#!/usr/bin/env python3
"""Generates the stage-05 shape display-name overrides from the `gt-materials.json` dump's
`prefixLocalNameOverrides` field, emitting `shape.gregtech.<prefix>.gregtech.<mlName>=<legacyName>` lang
entries for every (prefix, material) pair whose legacy display name does not match MaterialLib's plain `%s`
substitution default.

Run from anywhere; paths are resolved relative to this script's location. Requires only the Python 3
standard library and gen_materials.py (its sibling in this directory).

Usage: python scripts/mu/gen_lang.py
"""

from gen_materials import LANG_OUTPUT, assign_names, compute_ported, load_materials

SHAPE_LANG_BEGIN = "# --- BEGIN GENERATED SHAPE NAME OVERRIDES (scripts/mu/gen_lang.py) ---"
SHAPE_LANG_END = "# --- END GENERATED SHAPE NAME OVERRIDES ---"


def collect_overrides(ported, ml_names):
    entries = []
    for material in ported:
        mn = ml_names[material["name"]]
        for override in material.get("prefixLocalNameOverrides", []):
            entries.append((override["prefix"], mn, override["legacyName"]))
    entries.sort()
    return entries


def write_shape_lang_file(entries):
    block_lines = [SHAPE_LANG_BEGIN]
    for prefix, ml_name, legacy_name in entries:
        block_lines.append(f"shape.gregtech.{prefix}.gregtech.{ml_name}={legacy_name}")
    block_lines.append(SHAPE_LANG_END)
    block = "\n".join(block_lines)

    text = LANG_OUTPUT.read_text(encoding="utf-8")
    if SHAPE_LANG_BEGIN in text and SHAPE_LANG_END in text:
        before = text[:text.index(SHAPE_LANG_BEGIN)]
        after = text[text.index(SHAPE_LANG_END) + len(SHAPE_LANG_END):]
        text = before.rstrip("\n") + "\n\n" + block + after
    else:
        text = text.rstrip("\n") + "\n\n" + block + "\n"
    LANG_OUTPUT.write_text(text, encoding="utf-8")


def main():
    materials = load_materials()
    ported, _ = compute_ported(materials)
    ml_names, _ = assign_names(ported)
    entries = collect_overrides(ported, ml_names)
    write_shape_lang_file(entries)
    print(f"gen_lang.py: {len(entries)} shape name overrides written to {LANG_OUTPUT}")


if __name__ == "__main__":
    main()
