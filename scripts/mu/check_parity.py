#!/usr/bin/env python3
"""Checks `dumps/ml-materials.json` (the resolved MaterialLib registry) against `dumps/gt-materials.json`
(the legacy `Materials` dump) for every material `gen_materials.py` was supposed to port: existence, tint,
shape set, and every mapped `GTMaterialProperties` value. Per-material shape sets are verified against
`dumps/legacy-variants.json` (construction-time ground truth), not the `generatedPrefixes` capability-bit
dump, which can drift from it. Werkstoff-backed materials (stage 10) additionally verify the merged shape
union (`werkstoff.json` `generatedPrefixes`) and the composite `WERKSTOFF` property round-trip.

Regenerate the dumps first (`runServer` with `-Dgt.dumpMaterialData=true`, then copy
`run/server/material-dump/*.json` over `dumps/*.json`) before running this. EXCEPTIONS -- pinned pre-stage-10
captures, never overwrite from a post-stage-10 boot:
- `werkstoff.json`: bartworks' reroute loop shrinks `generatedPrefixes` toward whatever MaterialLib already
  serves (see `MaterialDataDump#dumpWerkstoffGeneratedPrefixes`), and the reconstruction flip changed merged
  werkstoffe's contested scalars to the gregtech-won values.
- `fluid-textures.json`: werkstoff fluid texture capture happened at legacy `GTFluid` construction, which the
  flip removed (reconstructed werkstoffe resolve MaterialLib's already-registered fluids instead).
- `gt-materials.json`: post-flip dumps are semantically identical but reordered (bridge mirrors are created in
  `werkstoffHashSet` iteration order, which the flip changed to ascending-id).
- `gtpp-materials.json`: pinned at its stage-11 commit-1 capture (see `MaterialDataDump#dumpGtppMaterials`'s
  javadoc) -- `Material#setTextureSet`'s composition heuristic is registration-order-sensitive for 15
  materials, so a re-dump could capture a different `textureSet` than the codegen already committed for.

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

WERKSTOFF_PATH = DUMPS_DIR / "werkstoff.json"

# Mirrors gen_materials.py's stage-10 werkstoff fold (WERKSTOFF_LEGACY_ONLY_PREFIXES,
# werkstoff_special_shape_lines, group_werkstoffs, werkstoff_flag_names).
WERKSTOFF_LEGACY_ONLY_PREFIXES = {"sheetmetal", "frameGt"}
WERKSTOFF_FLAG_FIELDS = [
    ("sublimation", "SUBLIMATION"),
    ("toxic", "TOXIC"),
    ("radioactive", "RADIOACTIVE"),
    ("blastFurnace", "BLAST_FURNACE"),
    ("elektrolysis", "ELECTROLYSIS"),
    ("centrifuge", "CENTRIFUGE"),
    ("gas", "GAS"),
    ("enforceUnification", "ENFORCE_UNIFICATION"),
    ("chemicalRecipes", "CHEMICAL_SYNTHESIS"),
    ("metalCraftingSolidifierRecipes", "METAL_CRAFTING_SOLIDIFICATION"),
    ("metalSolidifierRecipes", "METAL_SOLIDIFICATION"),
    ("mixerRecipes", "MIXING"),
    ("sifterRecipes", "SIFTING"),
    ("formulaLocalized", "LOCALIZED_FORMULA"),
]


def load_werkstoffs():
    with open(WERKSTOFF_PATH, encoding="utf-8") as f:
        return json.load(f)


REPO_ROOT = SCRIPT_DIR.parent.parent
LEGACY_BRIDGE_SOURCES = [
    "src/main/java/gregtech/loaders/materials/MaterialsLegacyBridge.java",
    "src/main/java/gregtech/loaders/materials/LegacyMarkerMaterials.java",
]


def load_legacy_constant_names():
    """Mirrors gen_materials.py's load_legacy_constant_names (byproduct reference-kind recovery)."""
    import re as _re
    names = set()
    for rel in LEGACY_BRIDGE_SOURCES:
        path = REPO_ROOT / rel
        if not path.exists():
            continue
        text = path.read_text(encoding="utf-8")
        names.update(_re.findall(r'LegacyMaterials' + chr(92) + r'.stub' + chr(92) + r'("([^"]+)"' + chr(92) + r')', text))
        names.update(_re.findall(r'setName' + chr(92) + r'("([^"]+)"' + chr(92) + r')', text))
    return names


def werkstoff_byproduct_kind(entry, byproduct_name, display_to_var, legacy_constants):
    if byproduct_name == entry["name"]:
        return "werkstoff"
    if byproduct_name in legacy_constants:
        return "material"
    return "werkstoff"


def group_werkstoffs(werkstoffs):
    by_name = {}
    display_to_var = {}
    for entry in werkstoffs:
        if entry["pool"] == "unknown":
            continue
        if not entry["isProxy"]:
            display_to_var[entry["name"]] = entry["varName"]
    for entry in sorted(werkstoffs, key=lambda e: e["id"]):
        if entry["pool"] == "unknown":
            continue
        name = entry["bridgeMaterial"] if entry["isProxy"] else entry["varName"]
        info = by_name.setdefault(name, {"entries": [], "prefixes": []})
        if not entry["isProxy"]:
            info["entries"].append(entry)
        for prefix in entry["generatedPrefixes"]:
            if prefix not in info["prefixes"]:
                info["prefixes"].append(prefix)
    return by_name, display_to_var


def werkstoff_ref_name(ref_name, kind, display_to_var):
    if kind == "werkstoff":
        return display_to_var[ref_name]
    return ref_name


def compute_werkstoff_referenced_names(werkstoff_by_name, display_to_var):
    referenced = set()
    for info in werkstoff_by_name.values():
        for entry in info["entries"]:
            for content in entry["contents"]:
                referenced.add(werkstoff_ref_name(content["name"], content["kind"], display_to_var))
            for byproduct in entry["oreByProducts"]:
                referenced.add(display_to_var.get(byproduct, byproduct))
    return referenced


def expected_werkstoff_shapes(werkstoff_prefixes, included_names):
    names = set(p for p in werkstoff_prefixes if p in included_names)
    if "cell" in werkstoff_prefixes:
        names.add("cell")
    if "cellMolten" in werkstoff_prefixes:
        names.add("cellMolten")
    if "block" in werkstoff_prefixes:
        names.add("block")
    if "ore" in werkstoff_prefixes:
        names.add("ore")
        names.add("oreSmall")
    if "blockCasing" in werkstoff_prefixes:
        names.add("blockCasing")
    if "blockCasingAdvanced" in werkstoff_prefixes:
        names.add("blockCasingAdvanced")
    return names


def werkstoff_flag_names(entries):
    flags = set()
    for entry in entries:
        for field, flag in WERKSTOFF_FLAG_FIELDS:
            if entry[field]:
                flags.add(flag)
        if not entry["autoBlastFurnaceRecipes"]:
            flags.add("NO_AUTO_BLAST_FURNACE_RECIPES")
        if not entry["autoVacuumFreezerRecipes"]:
            flags.add("NO_AUTO_VACUUM_FREEZER_RECIPES")
    return flags


def check_werkstoff(errors, name, info, ml, display_to_var, legacy_constants):
    actual = ml.get("werkstoff")
    entries = info["entries"] if info else []
    if not entries:
        if actual is not None:
            errors.append(f"{name}: unexpected werkstoff property {actual!r}")
        return
    if actual is None:
        errors.append(f"{name}: missing werkstoff property")
        return
    first = entries[0]

    def check(field, expected):
        if actual.get(field) != expected:
            errors.append(f"{name}: werkstoff.{field} expected {expected!r}, got {actual.get(field)!r}")

    check("ids", [e["id"] for e in entries])
    check("type", first["type"])
    check("pool", first["pool"])
    check("meltingPoint", first["meltingPoint"])
    check("boilingPoint", first["boilingPoint"])
    check("protons", first["protons"])
    check("neutrons", first["neutrons"])
    check("mass", first["mass"])
    check("meltingVoltage", first["meltingVoltage"])
    check("durabilityOverride", first["durability"])
    check("qualityOverride", first["quality"])
    check("enchantmentLevel", first["enchantmentLevel"])
    check("mixCircuit", first["mixCircuit"])
    if not floats_equal(actual.get("speedOverride"), first["speed"]):
        errors.append(f"{name}: werkstoff.speedOverride expected {first['speed']!r}")
    if not floats_equal(actual.get("durabilityModifier"), first["durabilityModifier"]):
        errors.append(f"{name}: werkstoff.durabilityModifier expected {first['durabilityModifier']!r}")
    if not floats_equal(actual.get("ebfGasTimeMultiplier"), first["ebfGasTimeMultiplier"]):
        errors.append(f"{name}: werkstoff.ebfGasTimeMultiplier expected {first['ebfGasTimeMultiplier']!r}")
    if not floats_equal(actual.get("ebfGasAmountMultiplier"), first["ebfGasAmountMultiplier"]):
        errors.append(f"{name}: werkstoff.ebfGasAmountMultiplier expected {first['ebfGasAmountMultiplier']!r}")
    check("flags", sorted(werkstoff_flag_names(entries)))
    check("prefixes", info["prefixes"])
    expected_contents = next((e["contents"] for e in entries if e["contents"]), [])
    actual_contents = actual.get("contents") or []
    expected_triples = [
        (ml_name(werkstoff_ref_name(c["name"], c["kind"], display_to_var)), c["amount"],
         c["kind"] == "werkstoff")
        for c in expected_contents]
    actual_triples = [(c["material"], c["amount"], c["werkstoff"]) for c in actual_contents]
    if expected_triples != actual_triples:
        errors.append(f"{name}: werkstoff.contents expected {expected_triples!r}, got {actual_triples!r}")
    byproducts_entry = next((e for e in entries if e["oreByProducts"]), None)
    expected_byproducts = []
    if byproducts_entry:
        for b in byproducts_entry["oreByProducts"]:
            kind = werkstoff_byproduct_kind(byproducts_entry, b, display_to_var, legacy_constants)
            expected_byproducts.append(
                (ml_name(werkstoff_ref_name(b, kind, display_to_var) if kind == "werkstoff" else b), 1,
                 kind == "werkstoff"))
    actual_byproducts = [(b["material"], b["amount"], b["werkstoff"]) for b in (actual.get("oreByProducts") or [])]
    if expected_byproducts != actual_byproducts:
        errors.append(
            f"{name}: werkstoff.oreByProducts expected {expected_byproducts!r}, "
            f"got {actual_byproducts!r}")
    sub_tags = []
    additional_oredict = []
    for entry in entries:
        for tag in entry["subTags"]:
            if tag not in sub_tags:
                sub_tags.append(tag)
        for oredict in entry["additionalOredict"]:
            if oredict not in additional_oredict:
                additional_oredict.append(oredict)
    check("subTags", sub_tags)
    check("additionalOreDict", additional_oredict)
    check("formula", next((e["formula"] for e in entries if e["formula"]), ""))

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


def compute_ported(materials, werkstoff_referenced=(), werkstoff_names=()):
    referenced = compute_referenced_names(materials) | set(werkstoff_referenced)
    forced = set(werkstoff_names)
    ported = []
    for material in materials:
        is_marker = not material["generatedPrefixes"] and not has_fluids(material) and not material[
            "composition"]
        if is_marker and material["name"] not in referenced and material["name"] not in forced:
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
                    legacy_block_materials, werkstoff_info, display_to_var, legacy_constants, gtpp_info=None):
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
    # ore/oreSmall are block-kind (excluded from `included_names`) but carry a real ORE generation bit, so
    # unlike `block` their membership is the dumped `generatedPrefixes` ground truth directly -- see
    # gen_materials.py's `ore_shape_lines`.
    if "ore" in gt["generatedPrefixes"]:
        dumped_shapes.add("ore")
    if "oreSmall" in gt["generatedPrefixes"]:
        dumped_shapes.add("oreSmall")
    if werkstoff_info:
        dumped_shapes |= expected_werkstoff_shapes(werkstoff_info["prefixes"], included_names)
    if gtpp_info:
        dumped_shapes |= gtpp_expected_shapes(gtpp_info)
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

    check_werkstoff(errors, name, werkstoff_info, ml, display_to_var, legacy_constants)
    if gtpp_info:
        check_gtpp_data(errors, name, gtpp_info, ml)

    return errors


def check_fluid_texture(errors, material_name, field, fluid_name, actual_fluid, fluid_textures):
    """Cross-checks a generated `FluidRef`'s `texture` against `fluid-textures.json`'s capture for the same
    fluid name -- an out-of-sync texture usually means `gen_materials.py` was regenerated from a dump older or
    newer than the `fluid-textures.json` snapshot it was regenerated alongside."""
    expected_texture = fluid_textures.get(fluid_name)
    actual_texture = actual_fluid.get("texture")
    if expected_texture != actual_texture:
        errors.append(f"{material_name}: {field}.texture expected {expected_texture!r}, got {actual_texture!r}")


GTPP_PATH = DUMPS_DIR / "gtpp-materials.json"

# Mirrors gen_materials.py's gtpp-fold region (stage 11): GTPP_LEGACY_ONLY_PREFIXES/GTPP_DEFERRED_PREFIXES/
# GTPP_UNSUPPORTED_PREFIXES/GTPP_SIMPLE_PREFIXES, gtpp_is_marker/compute_gtpp_ported, gtpp_shape_lines,
# fold_gtpp_materials/gtpp_composition_mismatch, celsius_to_kelvin. Kept duplicated per this script's
# standing convention (see the module docstring) rather than imported.
GTPP_LEGACY_ONLY_PREFIXES = {"frameGt"}
GTPP_DEFERRED_PREFIXES = {"cell", "cellPlasma"}
GTPP_UNSUPPORTED_PREFIXES = {
    "pipeHuge", "pipeMedium", "wireGt01", "wireGt02", "wireGt04", "wireGt08", "wireGt12", "wireGt16"
}
GTPP_SIMPLE_PREFIXES = {
    "bolt", "crushed", "crushedCentrifuged", "crushedPurified", "dust", "dustImpure", "dustPure", "dustSmall",
    "dustTiny", "foil", "gearGt", "gearGtSmall", "ingot", "ingotHot", "nugget", "plate", "plateDense",
    "plateDouble", "plateSuperdense", "rawOre", "ring", "rotor", "screw", "spring", "springSmall", "stick",
    "stickLong", "wireFine"
}


def load_gtpp_materials():
    return load(GTPP_PATH)


def gtpp_has_fluid(entry):
    fluids = entry["fluids"]
    return bool(fluids.get("fluid")) or bool(fluids.get("plasma"))


def gtpp_is_marker(entry):
    return not entry["generatedParts"] and not entry["composition"] and not gtpp_has_fluid(entry)


def compute_gtpp_referenced(gtpp_materials):
    referenced = set()
    for material in gtpp_materials:
        for stack in material["composition"]:
            name = stack.get("name")
            if name and name != material["unlocalizedName"]:
                referenced.add(name)
    return referenced


def compute_gtpp_ported(gtpp_materials, base_ported_names):
    referenced = compute_gtpp_referenced(gtpp_materials)
    ported = []
    for material in gtpp_materials:
        name = material["unlocalizedName"]
        if gtpp_is_marker(material) and name not in referenced and name not in base_ported_names:
            continue
        ported.append(material)
    return ported


def gtpp_expected_shapes(entry):
    """The MaterialLib shape-name set a gtpp material's dumped `generatedParts` translate to -- see
    gen_materials.py's `gtpp_shape_lines`. Raises on an unmapped prefix (a future gtpp dump refresh with a new
    part kind must not silently drop items from this check either)."""
    names = set()
    for part in entry["generatedParts"]:
        prefix = part["prefix"]
        if prefix in GTPP_SIMPLE_PREFIXES:
            names.add(prefix)
        elif prefix in ("block", "milled"):
            names.add(prefix)
        elif prefix in GTPP_DEFERRED_PREFIXES or prefix in GTPP_LEGACY_ONLY_PREFIXES \
                or prefix in GTPP_UNSUPPORTED_PREFIXES:
            continue
        else:
            raise SystemExit(
                f"check_parity.py: gtpp material {entry['unlocalizedName']!r} generates unmapped part "
                f"prefix {prefix!r}")
    return names


def celsius_to_kelvin(celsius):
    return round(celsius + 273.15)


def gtpp_composition_set(entry):
    return {(c["name"], c["amount"]) for c in entry["composition"] if c.get("name")}


def gt_composition_set_for_gtpp(gt_entry):
    return {(c["material"], c["amount"]) for c in gt_entry["composition"] if c and c.get("material")}


def fold_gtpp_materials(gtpp_ported, gt_by_name, ported_name_set):
    """Mirrors gen_materials.py's `fold_gtpp_materials`: splits into merges (same-name fold, composition
    agrees), new declarations (no name collision), and false merges (composition disagrees -- a planning
    stop, not silently kept; expected empty, per `gtpp-merge-report.txt`)."""
    merges = []
    new = []
    false_merges = []
    for entry in gtpp_ported:
        name = entry["unlocalizedName"]
        if name in ported_name_set:
            gt_entry = gt_by_name.get(name)
            gtpp_comp = gtpp_composition_set(entry)
            gt_comp = gt_composition_set_for_gtpp(gt_entry) if gt_entry else set()
            if gtpp_comp and gt_comp and gtpp_comp != gt_comp:
                false_merges.append((entry, gtpp_comp, gt_comp))
            else:
                merges.append(entry)
        else:
            new.append(entry)
    return merges, new, false_merges


def check_gtpp_data(errors, name, entry, ml):
    """Verifies `GTMaterialProperties.GTPP` round-trips against the dumped gtpp scalar fields (existence +
    every mapped [GTppData] field), for both merges and new declarations."""
    data = ml.get("gtpp")
    if data is None:
        errors.append(f"{name}: missing gtpp property")
        return

    def check(field, expected):
        if data.get(field) != expected:
            errors.append(f"{name}: gtpp.{field} expected {expected!r}, got {data.get(field)!r}")

    check("tier", entry["tier"])
    check("voltageMultiplier", entry["voltageMultiplier"])
    check("meltingPointK", celsius_to_kelvin(entry["meltingPointC"]))
    check("boilingPointK", celsius_to_kelvin(entry["boilingPointC"]))
    check("durability", entry["durability"])
    check("usesBlastFurnace", bool(entry["usesBlastFurnace"]))
    check("isRadioactive", bool(entry["isRadioactive"]))
    check("radiationLevel", entry["radiationLevel"])
    check("hasOre", bool(entry["hasOre"]))
    check("chemicalFormula", entry["chemicalFormula"])


def check_gtpp_new_material(errors, entry, ml_by_key):
    """Full existence/tint/textureSet/shape-set/property check for a gtpp-only material (no gregtech/werkstoff
    counterpart) -- everything about its ml-materials.json entry is gtpp-sourced, so (unlike a merge) the
    shape set is expected to match `gtpp_expected_shapes` exactly, not merely be a superset of it."""
    name = entry["unlocalizedName"]
    key = ml_name(name)
    ml = ml_by_key.get(key)
    if ml is None:
        errors.append(f"{name}: gtpp material missing from ml-materials.json (expected key {key!r})")
        return

    expected_tint = to_unsigned32(pack_argb(entry["rgba"]))
    actual_tint = to_unsigned32(ml["tint"]) if ml["tint"] is not None else None
    if expected_tint != actual_tint:
        errors.append(f"{name}: gtpp tint expected 0x{expected_tint:08X}, got {ml['tint']!r}")

    if ml["textureSet"] != entry["textureSet"]:
        errors.append(f"{name}: gtpp textureSet expected {entry['textureSet']!r}, got {ml['textureSet']!r}")

    expected_shapes = gtpp_expected_shapes(entry)
    actual_shapes = set(ml["shapes"])
    if expected_shapes != actual_shapes:
        missing = expected_shapes - actual_shapes
        extra = actual_shapes - expected_shapes
        errors.append(f"{name}: gtpp shapes mismatch, missing={sorted(missing)}, extra={sorted(extra)}")

    check_gtpp_data(errors, name, entry, ml)


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
    werkstoffs = load_werkstoffs()
    werkstoff_by_name, display_to_var = group_werkstoffs(werkstoffs)
    legacy_constants = load_legacy_constant_names()
    werkstoff_referenced = compute_werkstoff_referenced_names(werkstoff_by_name, display_to_var)
    werkstoff_names = {name for name, info in werkstoff_by_name.items() if info["entries"]}
    ported = compute_ported(gt_materials, werkstoff_referenced, werkstoff_names)
    ml_by_key = {m["name"]: m for m in ml_materials}

    gt_by_name = {m["name"]: m for m in gt_materials}
    ported_name_set = set(m["name"] for m in ported)
    gtpp_materials = load_gtpp_materials()
    gtpp_ported = compute_gtpp_ported(gtpp_materials, ported_name_set)
    gtpp_merges, gtpp_new, gtpp_false_merges = fold_gtpp_materials(gtpp_ported, gt_by_name, ported_name_set)
    gtpp_merges_by_name = {e["unlocalizedName"]: e for e in gtpp_merges}

    errors = []
    if gtpp_false_merges:
        for entry, gtpp_comp, gt_comp in gtpp_false_merges:
            errors.append(
                f"{entry['unlocalizedName']}: gtpp false merge (composition mismatch), needs coordinator "
                f"review -- gtpp={sorted(gtpp_comp)} gt={sorted(gt_comp)}")

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
                legacy_block_materials, werkstoff_by_name.get(material["name"]), display_to_var,
                legacy_constants, gtpp_merges_by_name.get(material["name"])))

    for entry in gtpp_new:
        check_gtpp_new_material(errors, entry, ml_by_key)

    expected_keys = set(ml_name(m["name"]) for m in ported) | set(ml_name(e["unlocalizedName"]) for e in gtpp_new)
    extra = set(ml_by_key) - expected_keys
    if extra:
        errors.append(f"unexpected ml-materials.json entries not in the ported set: {sorted(extra)}")

    total = len(ported) + len(gtpp_new)
    if errors:
        print(f"check_parity.py: {len(errors)} mismatches across {total} ported materials")
        for error in errors:
            print(" -", error)
        sys.exit(1)

    print(f"check_parity.py: OK, {total} materials verified")


if __name__ == "__main__":
    main()
