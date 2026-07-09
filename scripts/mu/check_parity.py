#!/usr/bin/env python3
"""Checks `dumps/ml-materials.json` (the resolved MaterialLib registry) against `dumps/gt-materials.json`
(the legacy `Materials` dump) for every material `gen_materials.py` was supposed to port: existence, tint,
shape set, and every mapped `GTMaterialProperties` value. Per-material shape sets are verified against
`dumps/legacy-variants.json` (construction-time ground truth), not the `generatedPrefixes` capability-bit
dump, which can drift from it.

Regenerate the dumps first (`runServer` with `-Dgt.dumpMaterialData=true`, then copy
`run/server/material-dump/*.json` over `dumps/*.json`) before running this.

Usage: python scripts/mu/check_parity.py
Exit status is nonzero if any mismatch is found.
"""

import json
import sys
from pathlib import Path

SCRIPT_DIR = Path(__file__).resolve().parent
DUMPS_DIR = SCRIPT_DIR / "dumps"
GT_PATH = DUMPS_DIR / "gt-materials.json"
ML_PATH = DUMPS_DIR / "ml-materials.json"
PREFIXES_PATH = DUMPS_DIR / "oreprefixes.json"
LEGACY_VARIANTS_PATH = DUMPS_DIR / "legacy-variants.json"
FLUID_TEXTURES_PATH = DUMPS_DIR / "fluid-textures.json"
LEGACY_BLOCKS_PATH = DUMPS_DIR / "legacy-blocks.json"

FLOAT_TOLERANCE = 1e-6

FLUID_STATES = ["solid", "fluid", "gas", "plasma", "molten"]

# Mirrors gen_materials.py's stage-06 fluid/cell shape codegen (FLUID_CUTOVER_EXCLUDED, FLUID_SHAPE_FIELDS,
# CRACKED_FLUID_SHAPE_FIELDS, CELL_SHAPE_FIELDS) so this script's expected shape set includes them too --
# otherwise every fluid- or cell-generating material would show a spurious "extra" shapes mismatch.
FLUID_CUTOVER_EXCLUDED = {"Water", "Ice", "Lava", "ConstructionFoam", "UUMatter"}
FLUID_SHAPE_NAMES = {
    "solid": "fluidSolid",
    "fluid": "fluidLiquid",
    "gas": "fluidGas",
    "plasma": "fluidPlasma",
    "molten": "fluidMolten",
}
CRACKED_FLUID_SHAPE_NAMES = {
    "hydroCracked": ["fluidHydroCracked1", "fluidHydroCracked2", "fluidHydroCracked3"],
    "steamCracked": ["fluidSteamCracked1", "fluidSteamCracked2", "fluidSteamCracked3"],
}
CRACKED_CELL_SHAPE_NAMES = {
    "cellHydroCracked1": "hydroCracked",
    "cellHydroCracked2": "hydroCracked",
    "cellHydroCracked3": "hydroCracked",
    "cellSteamCracked1": "steamCracked",
    "cellSteamCracked2": "steamCracked",
    "cellSteamCracked3": "steamCracked",
}

# Mirrors gen_materials.py's BLOCK_CUTOVER_EXCLUDED (structural block-identity references -- see its docstring).
BLOCK_CUTOVER_EXCLUDED = {
    "Steel", "Osmiridium", "Neutronium", "NaquadahAlloy", "WhiteDwarfMatter", "BlackDwarfMatter",
    "MagnetohydrodynamicallyConstrainedStarMatter", "SpaceTime", "TranscendentMetal", "Universium", "Copper",
    "Zinc"
}


def load_legacy_block_materials():
    if not LEGACY_BLOCKS_PATH.exists():
        return set()
    with open(LEGACY_BLOCKS_PATH, encoding="utf-8") as f:
        entries = json.load(f)
    return {e["material"] for e in entries}


def expected_fluid_and_cell_shapes(material, legacy_variants_by_material, used_fluid_names):
    if material["name"] in FLUID_CUTOVER_EXCLUDED:
        return set()
    names = set()
    fluids = material["fluids"]
    for state, shape_name in FLUID_SHAPE_NAMES.items():
        ref = fluids.get(state)
        if ref and ref["name"] not in used_fluid_names:
            used_fluid_names.add(ref["name"])
            names.add(shape_name)
    cracked = material.get("crackedFluids") or {}
    for key, shape_names in CRACKED_FLUID_SHAPE_NAMES.items():
        refs = cracked.get(key)
        if refs:
            for ref, shape_name in zip(refs, shape_names):
                if ref and ref["name"] not in used_fluid_names:
                    used_fluid_names.add(ref["name"])
                    names.add(shape_name)

    variants = legacy_variants_by_material.get(material["name"], set())
    if "cell" in variants and (fluids.get("fluid") or fluids.get("gas")):
        names.add("cell")
    if "cellMolten" in variants and fluids.get("molten"):
        names.add("cellMolten")
    if "cellPlasma" in variants and fluids.get("plasma"):
        names.add("cellPlasma" if fluids.get("molten") else "cellPlasmaLight")
    for prefix_name, cracked_key in CRACKED_CELL_SHAPE_NAMES.items():
        if prefix_name in variants and cracked.get(cracked_key):
            names.add(prefix_name)
    return names

# Mirrors gen_materials.py's SUBTAG_FLAG_OVERRIDES / ml_name -- kept duplicated rather than imported since
# this script must also run standalone against a checked-out dump pair.
SUBTAG_FLAG_OVERRIDES = {
    "AnaerobeGas": "ANAEROBE_GAS",
    "NobleGas": "NOBLE_GAS",
}

BLOCK_TEXTURE_INDEX_MIN = 65
BLOCK_TEXTURE_INDEX_MAX = 95


def ml_name(name):
    return "".join(ch for ch in name if ch != ":" and not ch.isspace())


def is_block_kind(prefix):
    name = prefix["name"]
    if name in ("block", "frameGt"):
        return True
    if name.startswith("ore") and name != "rawOre":
        return True
    texture_index = prefix.get("textureIndex", -1)
    if texture_index is not None and BLOCK_TEXTURE_INDEX_MIN <= texture_index <= BLOCK_TEXTURE_INDEX_MAX:
        return True
    return False


def is_complex_tool_or_armor(prefix):
    name = prefix["name"]
    if name in ("tool", "armor"):
        return True
    if name.startswith("tool") and not name.startswith("toolHead"):
        return True
    if name.startswith("armor"):
        return True
    return False


def is_included_shape(prefix, legacy_variant_prefixes):
    if prefix["generationBits"] == 0:
        return False
    if not prefix["isMaterialBased"]:
        return False
    if prefix["isContainer"]:
        return False
    if is_block_kind(prefix):
        return False
    if is_complex_tool_or_armor(prefix):
        return False
    # Mirrors gen_shapes.py/gen_materials.py: a prefix only ever had a real legacy item if it also held a
    # constructor slot, which legacy-variants.json (ground truth) records and the capability-bit dump alone
    # does not.
    return prefix["name"] in legacy_variant_prefixes


def load_legacy_variants():
    with open(LEGACY_VARIANTS_PATH, encoding="utf-8") as f:
        return json.load(f)


def load_legacy_variant_prefixes(variants):
    return {v["prefix"] for v in variants}


def load_legacy_variants_by_material(variants):
    by_material = {}
    for v in variants:
        by_material.setdefault(v["material"], set()).add(v["prefix"])
    return by_material


def has_fluids(material):
    fluids = material["fluids"]
    return any(fluids.get(state) for state in FLUID_STATES)


def compute_referenced_names(materials):
    referenced = set()
    ref_fields = [
        "smeltInto", "macerateInto", "arcSmeltInto", "directSmelting", "handleMaterial", "materialInto"
    ]
    for material in materials:
        name = material["name"]
        for field in ref_fields:
            value = material.get(field)
            if value and value != name:
                referenced.add(value)
        for stack in material["composition"]:
            if stack and stack.get("material") and stack["material"] != name:
                referenced.add(stack["material"])
        for byproduct in material["oreByProducts"]:
            if byproduct != name:
                referenced.add(byproduct)
    return referenced


def compute_ported(materials):
    referenced = compute_referenced_names(materials)
    ported = []
    for material in materials:
        is_marker = not material["generatedPrefixes"] and not has_fluids(material) and not material[
            "composition"]
        if is_marker and material["name"] not in referenced:
            continue
        ported.append(material)
    return ported


def pack_argb(rgba):
    r, g, b, a = rgba
    if a == 0:
        a = 255
    return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF)


def to_unsigned32(value):
    return value & 0xFFFFFFFF


def floats_equal(a, b):
    if a == b:
        return True
    try:
        return abs(a - b) < FLOAT_TOLERANCE
    except (TypeError, OverflowError):
        return False


def check_ref_field(errors, name, field, gt_value, ml_value):
    expected = ml_name(gt_value) if gt_value else None
    if expected != ml_value:
        errors.append(f"{name}: {field} expected {expected!r}, got {ml_value!r}")


def check_material(gt, ml, included_names, legacy_variants_by_material, used_fluid_names, fluid_textures,
                    legacy_block_materials):
    errors = []
    name = gt["name"]

    expected_tint = to_unsigned32(pack_argb(gt["rgba"]))
    actual_tint = to_unsigned32(ml["tint"]) if ml["tint"] is not None else None
    if expected_tint != actual_tint:
        errors.append(f"{name}: tint expected 0x{expected_tint:08X}, got {ml['tint']!r}")

    if ml["textureSet"] != gt["iconSet"]:
        errors.append(f"{name}: textureSet expected {gt['iconSet']!r}, got {ml['textureSet']!r}")

    actual_variants = legacy_variants_by_material.get(name, set())
    dumped_shapes = set(p for p in actual_variants if p in included_names)
    dumped_shapes |= expected_fluid_and_cell_shapes(gt, legacy_variants_by_material, used_fluid_names)
    if name in legacy_block_materials and name not in BLOCK_CUTOVER_EXCLUDED:
        dumped_shapes.add("block")
    actual_shapes = set(ml["shapes"])
    if dumped_shapes != actual_shapes:
        missing = dumped_shapes - actual_shapes
        extra = actual_shapes - dumped_shapes
        errors.append(f"{name}: shapes mismatch, missing={sorted(missing)}, extra={sorted(extra)}")

    if ml["localName"] != gt["localName"]:
        errors.append(f"{name}: localName expected {gt['localName']!r}, got {ml['localName']!r}")

    def check_int(field, ml_field, default):
        expected = gt[field]
        actual = ml[ml_field] if ml[ml_field] is not None else default
        if expected != actual:
            errors.append(f"{name}: {field} expected {expected!r}, got {actual!r}")

    check_int("meltingPoint", "meltingPoint", 0)
    check_int("blastTemp", "blastTemp", 0)
    check_int("gasTemp", "gasTemp", 0)
    check_int("fuelPower", "fuelPower", 0)
    check_int("fuelType", "fuelType", 0)
    check_int("toolDurability", "toolDurability", 0)
    check_int("toolQuality", "toolQuality", 0)
    check_int("oreMultiplier", "oreMultiplier", 1)
    check_int("byProductMultiplier", "byProductMultiplier", 1)
    check_int("smeltingMultiplier", "smeltingMultiplier", 1)

    expected_sub_id = gt["subId"]
    actual_sub_id = ml["subId"] if ml["subId"] is not None else -1
    if expected_sub_id != actual_sub_id:
        errors.append(f"{name}: subId expected {expected_sub_id!r}, got {actual_sub_id!r}")

    expected_blast_required = bool(gt["blastRequired"])
    actual_blast_required = bool(ml["blastRequired"]) if ml["blastRequired"] is not None else False
    if expected_blast_required != actual_blast_required:
        errors.append(
            f"{name}: blastRequired expected {expected_blast_required!r}, got {actual_blast_required!r}")

    expected_heat_damage = gt["heatDamage"]
    actual_heat_damage = ml["heatDamage"] if ml["heatDamage"] is not None else 0.0
    if not floats_equal(expected_heat_damage, actual_heat_damage):
        errors.append(f"{name}: heatDamage expected {expected_heat_damage!r}, got {actual_heat_damage!r}")

    expected_tool_speed = gt["toolSpeed"]
    actual_tool_speed = ml["toolSpeed"] if ml["toolSpeed"] is not None else 1.0
    if not floats_equal(expected_tool_speed, actual_tool_speed):
        errors.append(f"{name}: toolSpeed expected {expected_tool_speed!r}, got {actual_tool_speed!r}")

    expected_molten_default = gt["moltenRgba"] == [255, 255, 255, 0]
    expected_molten = None if expected_molten_default else to_unsigned32(pack_argb(gt["moltenRgba"]))
    actual_molten = to_unsigned32(ml["moltenTint"]) if ml["moltenTint"] is not None else None
    if expected_molten != actual_molten:
        errors.append(f"{name}: moltenTint expected {expected_molten!r}, got {actual_molten!r}")

    if gt["element"] != ml["element"]:
        errors.append(f"{name}: element expected {gt['element']!r}, got {ml['element']!r}")

    expected_composition = [(ml_name(c["material"]), c["amount"]) for c in gt["composition"] if c]
    actual_composition = [(c["material"], c["amount"]) for c in ml["composition"]]
    if expected_composition != actual_composition:
        errors.append(f"{name}: composition expected {expected_composition!r}, got {actual_composition!r}")

    check_ref_field(errors, name, "smeltInto", gt["smeltInto"], ml["smeltInto"])
    check_ref_field(errors, name, "macerateInto", gt["macerateInto"], ml["macerateInto"])
    check_ref_field(errors, name, "arcSmeltInto", gt["arcSmeltInto"], ml["arcSmeltInto"])
    check_ref_field(errors, name, "directSmelting", gt["directSmelting"], ml["directSmelting"])
    check_ref_field(errors, name, "handleMaterial", gt["handleMaterial"], ml["handleMaterial"])
    check_ref_field(errors, name, "materialInto", gt["materialInto"], ml["materialInto"])

    expected_byproducts = sorted(ml_name(b) for b in gt["oreByProducts"])
    actual_byproducts = sorted(ml["oreByProducts"])
    if expected_byproducts != actual_byproducts:
        errors.append(f"{name}: oreByProducts expected {expected_byproducts!r}, got {actual_byproducts!r}")

    expected_flags = sorted(SUBTAG_FLAG_OVERRIDES.get(tag, tag) for tag in gt["subTags"])
    actual_flags = sorted(ml["flags"])
    if expected_flags != actual_flags:
        errors.append(f"{name}: flags expected {expected_flags!r}, got {actual_flags!r}")

    expected_aspects = sorted((a["name"], a["amount"]) for a in gt["aspects"])
    actual_aspects = sorted((a["name"], a["amount"]) for a in ml["aspects"])
    if expected_aspects != actual_aspects:
        errors.append(f"{name}: aspects expected {expected_aspects!r}, got {actual_aspects!r}")

    # FLUID_CUTOVER_EXCLUDED materials keep their fluid fields hand-wired in LoaderGTBlockFluid rather than
    # through GTMaterialProperties.LEGACY_FLUIDS (see gen_materials.py), so ml["fluids"] is intentionally empty
    # for them.
    if name not in FLUID_CUTOVER_EXCLUDED:
        for state in FLUID_STATES:
            expected_fluid = gt["fluids"].get(state)
            actual_fluid = ml["fluids"].get(state)
            if bool(expected_fluid) != bool(actual_fluid):
                errors.append(f"{name}: fluids.{state} expected {expected_fluid!r}, got {actual_fluid!r}")
            elif expected_fluid and actual_fluid:
                if expected_fluid["name"] != actual_fluid["name"] or expected_fluid["temperature"] != actual_fluid[
                        "temperature"]:
                    errors.append(f"{name}: fluids.{state} expected {expected_fluid!r}, got {actual_fluid!r}")
                else:
                    check_fluid_texture(errors, name, f"fluids.{state}", expected_fluid["name"], actual_fluid,
                                         fluid_textures)

        cracked = gt.get("crackedFluids") or {}
        for dump_key, ml_key in (("hydroCracked", "crackedHydroFluids"), ("steamCracked", "crackedSteamFluids")):
            expected_refs = cracked.get(dump_key) or []
            actual_refs = ml.get(ml_key) or []
            for i, expected_ref in enumerate(expected_refs):
                if not expected_ref or i >= len(actual_refs) or not actual_refs[i]:
                    continue
                check_fluid_texture(
                    errors, name, f"{ml_key}[{i}]", expected_ref["name"], actual_refs[i], fluid_textures)

    return errors


def check_fluid_texture(errors, material_name, field, fluid_name, actual_fluid, fluid_textures):
    """Cross-checks a generated `FluidRef`'s `texture` against `fluid-textures.json`'s capture for the same
    fluid name -- an out-of-sync texture usually means `gen_materials.py` was regenerated from a dump older or
    newer than the `fluid-textures.json` snapshot it was regenerated alongside."""
    expected_texture = fluid_textures.get(fluid_name)
    actual_texture = actual_fluid.get("texture")
    if expected_texture != actual_texture:
        errors.append(f"{material_name}: {field}.texture expected {expected_texture!r}, got {actual_texture!r}")


def load(path):
    if not path.exists():
        raise SystemExit(f"check_parity.py: missing dump {path}; run the server with -Dgt.dumpMaterialData=true "
                          "and copy run/server/material-dump/*.json here first")
    with open(path, encoding="utf-8") as f:
        return json.load(f)


def main():
    gt_materials = load(GT_PATH)
    ml_materials = load(ML_PATH)
    prefixes = load(PREFIXES_PATH)["prefixes"]
    legacy_variants = load_legacy_variants()
    legacy_variant_prefixes = load_legacy_variant_prefixes(legacy_variants)
    legacy_variants_by_material = load_legacy_variants_by_material(legacy_variants)
    fluid_textures = load(FLUID_TEXTURES_PATH)
    legacy_block_materials = load_legacy_block_materials()

    included_names = set(p["name"] for p in prefixes if is_included_shape(p, legacy_variant_prefixes))
    ported = compute_ported(gt_materials)
    ml_by_key = {m["name"]: m for m in ml_materials}

    errors = []
    used_fluid_names = set()
    for material in ported:
        key = ml_name(material["name"])
        ml = ml_by_key.get(key)
        if ml is None:
            errors.append(f"{material['name']}: missing from ml-materials.json (expected key {key!r})")
            continue
        errors.extend(
            check_material(
                material, ml, included_names, legacy_variants_by_material, used_fluid_names, fluid_textures,
                legacy_block_materials))

    expected_keys = set(ml_name(m["name"]) for m in ported)
    extra = set(ml_by_key) - expected_keys
    if extra:
        errors.append(f"unexpected ml-materials.json entries not in the ported set: {sorted(extra)}")

    if errors:
        print(f"check_parity.py: {len(errors)} mismatches across {len(ported)} ported materials")
        for error in errors:
            print(" -", error)
        sys.exit(1)

    print(f"check_parity.py: OK, {len(ported)} materials verified")


if __name__ == "__main__":
    main()
