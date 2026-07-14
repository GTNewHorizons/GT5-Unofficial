#!/usr/bin/env python3
"""Checks `dumps/ml-materials.json` (the resolved MaterialLib registry) against `dumps/gt-materials.json`
(the legacy `Materials` dump) for every material `gen_materials.py` was supposed to port: existence, tint,
shape set, and every mapped `GTMaterialProperties` value. Per-material shape sets are verified against
`dumps/legacy-variants.json` (construction-time ground truth), not the `generatedPrefixes` capability-bit
dump, which can drift from it. Werkstoff-backed materials (stage 10) additionally verify the merged shape
union (`werkstoff.json` `generatedPrefixes`) and the surviving decomposed `WERKSTOFF_*` properties.

The U1 collapse landed the werkstoff/gtpp scalars on canonical properties, so a canonical value may now
legitimately differ from one of the pinned legacy dumps (the other source, or the computed atomic formula,
won the key). Every such difference must appear in `dumps/u1-collapse-table.json`'s `implemented_deltas`
allowlist -- an unallowlisted deviation, a deviation whose recorded legacy/canonical values no longer match,
and an allowlist entry no longer exercised are all errors, so the committed table is the exact deviation
inventory. The verdict line reports the allowlisted-deviation count next to the verified-material count.

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

# Mirrors gen_materials.py's GTPP_ANIMATED_BLOCK_EXCLUDED (legacy animated storage blocks stay canonical --
# see its docstring).
GTPP_ANIMATED_BLOCK_EXCLUDED = {"AstralTitanium", "CelestialTungsten", "ChromaticGlass", "Hypogen"}

# Mirrors gen_materials.py's GTPP_PRECOLORED_ITEM_MATERIALS (legacy alpha>1 untinted-item rule -- see its
# docstring): these declare a white tint instead of the packed dumped rgba.
GTPP_PRECOLORED_ITEM_MATERIALS = {"AstralTitanium", "CelestialTungsten", "ChromaticGlass"}


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
COLLAPSE_TABLE_PATH = DUMPS_DIR / "u1-collapse-table.json"

# GTWerkstoffFlag members the U1/U2 collapses moved onto canonical properties or dropped as dead data.
# LOCALIZED_FORMULA additionally landed on FORMULA_LOCALIZED. The U2 pass ported ELECTROLYSIS/CENTRIFUGE/GAS/
# ENFORCE_UNIFICATION/CHEMICAL_SYNTHESIS/MIXING/SIFTING onto HAS_ELECTROLYZER_RECIPE/HAS_CENTRIFUGE_RECIPE/
# HAS_GAS/ENFORCE_ORE_DICT_UNIFICATION/HAS_CHEMICAL_RECIPE/HAS_MIXER_RECIPE/HAS_SIFTER_RECIPE, and dropped
# SUBLIMATION (no reader) and METAL_CRAFTING_SOLIDIFICATION/METAL_SOLIDIFICATION (the legacy-pipeline solidifier
# autogen they gated is unconditional on a registered molten fluid plus the target prefix existing) -- exhausting
# GTWerkstoffFlag, which no longer exists.
MIGRATED_WERKSTOFF_FLAGS = {
    "TOXIC", "RADIOACTIVE", "BLAST_FURNACE", "NO_AUTO_BLAST_FURNACE_RECIPES", "NO_AUTO_VACUUM_FREEZER_RECIPES",
    "LOCALIZED_FORMULA", "ELECTROLYSIS", "CENTRIFUGE", "GAS", "ENFORCE_UNIFICATION", "CHEMICAL_SYNTHESIS",
    "MIXING", "SIFTING", "SUBLIMATION", "METAL_CRAFTING_SOLIDIFICATION", "METAL_SOLIDIFICATION"
}

M = 3628800


def _jsonable(value):
    if isinstance(value, tuple):
        return [_jsonable(v) for v in value]
    if isinstance(value, (list, set)):
        return [_jsonable(v) for v in value]
    return value


def _values_equal(a, b):
    if isinstance(a, float) or isinstance(b, float):
        return floats_equal(a, b)
    if isinstance(a, list) and isinstance(b, list):
        return len(a) == len(b) and all(_values_equal(x, y) for x, y in zip(a, b))
    return a == b


class Deviations:
    """Validates every collapse deviation against the committed `implemented_deltas` allowlist: unknown
    deviations, drifted values, and unexercised allowlist entries all error."""

    def __init__(self, allow):
        self.allow = allow
        self.used = set()
        self.errors = []

    def dev(self, clazz, material, field, legacy, canonical):
        key = (clazz, material, field)
        entry = self.allow.get(key)
        legacy = _jsonable(legacy)
        canonical = _jsonable(canonical)
        if entry is None:
            self.errors.append(
                f"{material}: unallowlisted {clazz} deviation on {field}: legacy {legacy!r} -> {canonical!r}")
            return
        if not _values_equal(entry.get("legacy"), legacy) or not _values_equal(entry.get("canonical"), canonical):
            self.errors.append(
                f"{material}: {clazz} deviation on {field} drifted from the allowlist: recorded "
                f"{entry.get('legacy')!r} -> {entry.get('canonical')!r}, observed {legacy!r} -> {canonical!r}")
            return
        self.used.add(key)

    def unused(self):
        return sorted(set(self.allow) - self.used)


def load_allowlist():
    with open(COLLAPSE_TABLE_PATH, encoding="utf-8") as f:
        table = json.load(f)
    allow = {}
    for clazz, entries in table["implemented_deltas"].items():
        for e in entries:
            allow[(clazz, e["material"], e["field"])] = e
    return allow


class MlAtomics:
    """Reimplements `gregtech.api.material.MaterialAtomics` (the legacy `Materials` density-weighted
    formula) over ml-materials.json entries, so the collapsed proton/neutron/mass values can be verified
    against the pinned werkstoff/gtpp dumps."""

    def __init__(self, ml_by_key, elements):
        self.ml_by_key = ml_by_key
        self.elements = elements
        self.cache = {}

    def get(self, name, kind):
        key = (name, kind)
        if key in self.cache:
            return self.cache[key]
        self.cache[key] = None
        ml = self.ml_by_key.get(name)
        if ml is None:
            raise SystemExit(f"check_parity.py: composition references unknown material {name!r}")
        element = ml.get("element")
        if element is not None:
            v = self.elements[element][kind]
        elif not ml["composition"]:
            v = self.elements["Tc"][kind]
        else:
            total = 0
            weighted = 0
            for stack in ml["composition"]:
                total += stack["amount"]
                sub = self.get(stack["material"], kind)
                if sub is None:
                    raise SystemExit(f"check_parity.py: composition cycle at {name!r}")
                weighted += stack["amount"] * sub
            multiplier = ml.get("densityMultiplier") or 1
            divider = ml.get("densityDivider") or 1
            density = (M * multiplier) // divider
            v = (density * weighted) // (total * M)
        self.cache[key] = v
        return v


def load_element_table():
    """Parses `gregtech.api.enums.Element`'s enum table (protons, neutrons, additional mass)."""
    import re as _re
    table = {}
    pat = _re.compile(
        r'^\s{4}(\w+)\((-?\d+), (-?\d+), (-?\d+), "[^"]*", (?:true|false)\)[,;]\s*$', _re.M)
    source = (REPO_ROOT / "src/main/java/gregtech/api/enums/Element.java").read_text(encoding="utf-8")
    for m in pat.finditer(source):
        p, n, add = int(m.group(2)), int(m.group(3)), int(m.group(4))
        table[m.group(1)] = {"protons": p, "neutrons": n, "mass": p + n + add}
    return table

# Mirrors gen_materials.py's stage-10 werkstoff fold (WERKSTOFF_LEGACY_ONLY_PREFIXES,
# werkstoff_special_shape_lines, group_werkstoffs, werkstoff_flag_names).
WERKSTOFF_LEGACY_ONLY_PREFIXES = {"sheetmetal", "frameGt"}

# Mirrors gen_materials.py's BARTWORKS_CASING_MATERIALS: the dumped casing prefix over-reports membership
# (legacy hasItemType() granted it to any metalworking-capable werkstoff), so casing shapes are curated to
# the set legacy bartworks actually shipped a casing for.
BARTWORKS_CASING_MATERIALS = {
    "Rhodium-PlatedPalladium",
    "Ruridit",
    "HighDurabilityCompoundSteel",
    "AdemicSteel",
    "Carbon",
    "Silver",
    "Iridium",
    "Naquadah",
    "NaquadahAlloy",
    "Osmiridium",
    "BlackSteel",
    "Wood",
    "AtomicSeparationCatalyst",
    "ExtremelyUnstableNaquadah",
    "AdamantiumAlloy",
    "MAR-M200Steel",
    "MAR-Ce-M200Steel",
    "Tairitsu",
    "PreciousMetalsAlloy",
    "EnrichedNaquadahAlloy",
    "MetastableOganesson",
    "Shirabon",
    "Mu-metal",
}
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


def expected_werkstoff_shapes(material_name, werkstoff_prefixes, included_names):
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
    if "blockCasing" in werkstoff_prefixes and material_name in BARTWORKS_CASING_MATERIALS:
        names.add("blockCasing")
    if "blockCasingAdvanced" in werkstoff_prefixes and material_name in BARTWORKS_CASING_MATERIALS:
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


def mlv_from_dump(ml, atomics):
    """The collapsed-scalar view of an ml-materials.json entry `collect_collapsed_deviations` consumes --
    the offline delta generator builds the same view from the committed `Materials2Materials` source."""
    if "boilingPoint" not in ml or "formula" not in ml:
        raise SystemExit(
            "check_parity.py: dumps/ml-materials.json predates the U1 collapse schema; regenerate it "
            "(runServer with -Dgt.dumpMaterialData=true) and copy it here first")
    return {
        "meltingPoint": ml["meltingPoint"],
        "boilingPoint": ml["boilingPoint"],
        "meltingVoltage": ml["meltingVoltage"],
        "blastRequired": ml["blastRequired"],
        "toxic": ml["toxic"],
        "isRadioactive": ml["isRadioactive"],
        "radiationLevel": ml["radiationLevel"],
        "tier": ml["tier"],
        "voltageMultiplier": ml["voltageMultiplier"],
        "mixCircuit": ml["mixCircuit"],
        "ebfGasTimeMultiplier": ml["ebfGasTimeMultiplier"],
        "ebfGasAmountMultiplier": ml["ebfGasAmountMultiplier"],
        "subTags": ml["subTags"],
        "toolDurability": ml["toolDurability"],
        "toolSpeed": ml["toolSpeed"],
        "toolQuality": ml["toolQuality"],
        "autoBlast": ml["autoBlast"],
        "autoVacuum": ml["autoVacuum"],
        "hasElectrolyzerRecipe": ml["hasElectrolyzerRecipe"],
        "hasCentrifugeRecipe": ml["hasCentrifugeRecipe"],
        "hasGas": ml["hasGas"],
        "enforceOreDictUnification": ml["enforceOreDictUnification"],
        "hasChemicalRecipe": ml["hasChemicalRecipe"],
        "hasMixerRecipe": ml["hasMixerRecipe"],
        "hasSifterRecipe": ml["hasSifterRecipe"],
        "hasMetalCraftingSolidifierRecipe": ml["hasMetalCraftingSolidifierRecipe"],
        "hasMetalSolidifierRecipe": ml["hasMetalSolidifierRecipe"],
        "formula": ml["formula"],
        "formulaLocalized": ml["formulaLocalized"],
        "composition": [(c["material"], c["amount"]) for c in ml["composition"]],
        "oreByProducts": list(ml["oreByProducts"]),
        "protons": atomics.get(ml["name"], "protons"),
        "neutrons": atomics.get(ml["name"], "neutrons"),
        "mass": atomics.get(ml["name"], "mass"),
    }


def collect_collapsed_deviations(dev, error, key, gt, wk_entries, gtpp_entry, mlv, display_to_var):
    """Verifies every U1-collapsed canonical value in `mlv` against the pinned legacy dumps. A straight
    promotion (single legacy source) errors on mismatch via `error(message)`; a value with competing legacy
    sources (or a computed canonical, for the atomic counts) records each losing source through
    `dev(clazz, key, field, legacy, canonical)` for allowlist validation. `key` is the ML registry name;
    `gt`, `wk_entries`, `gtpp_entry` are the material's gt-materials/werkstoff/gtpp-materials dump data
    (each possibly absent)."""
    wk = wk_entries[0] if wk_entries else None
    wk_flags = werkstoff_flag_names(wk_entries) if wk_entries else set()

    def mism(clazz, field, legacy, canonical):
        if isinstance(legacy, float) or isinstance(canonical, float):
            if floats_equal(legacy, canonical):
                return
        elif legacy == canonical:
            return
        dev(clazz, key, field, legacy, canonical)

    ml_melt = mlv["meltingPoint"] or 0
    if gt is not None:
        mism("melting_point_gt", "meltingPoint", gt["meltingPoint"], ml_melt)
    if wk is not None:
        mism("melting_point_werkstoff", "meltingPoint", wk["meltingPoint"], ml_melt)
    if gtpp_entry is not None:
        mism("melting_point_gtpp", "meltingPoint", celsius_to_kelvin(gtpp_entry["meltingPointC"]), ml_melt)

    ml_boil = mlv["boilingPoint"] or 0
    if wk is not None:
        mism("boiling_point_werkstoff", "boilingPoint", wk["boilingPoint"], ml_boil)
    if gtpp_entry is not None:
        mism("boiling_point_gtpp", "boilingPoint", celsius_to_kelvin(gtpp_entry["boilingPointC"]), ml_boil)
    if wk is None and gtpp_entry is None and ml_boil:
        error(f"boilingPoint {ml_boil} with no legacy source")

    ml_dur = mlv["toolDurability"] or 0
    ml_quality = mlv["toolQuality"] or 0
    ml_speed = mlv["toolSpeed"] if mlv["toolSpeed"] is not None else 1.0
    if gt is not None:
        mism("tool_stats_gt", "toolDurability", gt["toolDurability"], ml_dur)
        mism("tool_stats_gt", "toolQuality", gt["toolQuality"], ml_quality)
        mism("tool_stats_gt", "toolSpeed", gt["toolSpeed"], ml_speed)
    if wk is not None:
        if wk["durability"]:
            mism("tool_stats_werkstoff", "durabilityOverride", wk["durability"], ml_dur)
        if wk["quality"]:
            mism("tool_stats_werkstoff", "qualityOverride", wk["quality"], ml_quality)
        if wk["speed"]:
            mism("tool_stats_werkstoff", "speedOverride", wk["speed"], ml_speed)
        if wk["durabilityModifier"] != 1.0:
            dev("werkstoff_durability_modifier", key, "durabilityModifier", wk["durabilityModifier"], ml_dur)
    if gtpp_entry is not None:
        mism("tool_stats_gtpp", "durability", gtpp_entry["durability"], ml_dur)

    ml_blast = bool(mlv["blastRequired"])
    if gt is not None:
        mism("blast_gt", "blastRequired", bool(gt["blastRequired"]), ml_blast)
    if gtpp_entry is not None:
        mism("blast_gtpp", "usesBlastFurnace", bool(gtpp_entry["usesBlastFurnace"]), ml_blast)
    if "BLAST_FURNACE" in wk_flags and not ml_blast:
        error("werkstoff BLAST_FURNACE flag lost (blastRequired is false)")

    ml_toxic = bool(mlv["toxic"])
    if ml_toxic != ("TOXIC" in wk_flags):
        error(f"toxic expected {'TOXIC' in wk_flags}, got {ml_toxic}")

    wk_radioactive = "RADIOACTIVE" in wk_flags
    gtpp_radioactive = bool(gtpp_entry["isRadioactive"]) if gtpp_entry is not None else False
    ml_radioactive = bool(mlv["isRadioactive"])
    if ml_radioactive != (wk_radioactive or gtpp_radioactive):
        error(f"isRadioactive expected {wk_radioactive or gtpp_radioactive}, got {ml_radioactive}")
    if wk is not None and wk_radioactive != ml_radioactive:
        mism("radioactivity_merge", "isRadioactive(werkstoff)", wk_radioactive, ml_radioactive)
    if gtpp_entry is not None and gtpp_radioactive != ml_radioactive:
        mism("radioactivity_merge", "isRadioactive(gtpp)", gtpp_radioactive, ml_radioactive)

    expected_radiation = gtpp_entry["radiationLevel"] if gtpp_entry is not None else 0
    if (mlv["radiationLevel"] or 0) != expected_radiation:
        error(f"radiationLevel expected {expected_radiation!r}, got {mlv['radiationLevel']!r}")

    # U2 GTWerkstoffFlag ports. hasElectrolyzerRecipe/hasCentrifugeRecipe have two independent legacy sources
    # (gt-materials.json's own mExtraData-derived field, from architecture predating the werkstoff port, and
    # the werkstoff isElektrolysis/isCentrifuge stat) that never both apply to the same material in practice,
    # so the combined value is enforced as their OR and a per-source mismatch is allowlist-eligible exactly like
    # isRadioactive above; the rest have only the werkstoff stat as a legacy source.
    wk_electrolyzer = bool(wk["elektrolysis"]) if wk is not None else False
    gt_electrolyzer = bool(gt["hasElectrolyzerRecipe"]) if gt is not None else False
    ml_electrolyzer = bool(mlv["hasElectrolyzerRecipe"])
    if ml_electrolyzer != (wk_electrolyzer or gt_electrolyzer):
        error(f"hasElectrolyzerRecipe expected {wk_electrolyzer or gt_electrolyzer}, got {ml_electrolyzer}")
    if wk is not None and wk_electrolyzer != ml_electrolyzer:
        mism("electrolyzer_merge", "hasElectrolyzerRecipe(werkstoff)", wk_electrolyzer, ml_electrolyzer)
    if gt is not None and gt_electrolyzer != ml_electrolyzer:
        mism("electrolyzer_merge", "hasElectrolyzerRecipe(gt)", gt_electrolyzer, ml_electrolyzer)

    wk_centrifuge = bool(wk["centrifuge"]) if wk is not None else False
    gt_centrifuge = bool(gt["hasCentrifugeRecipe"]) if gt is not None else False
    ml_centrifuge = bool(mlv["hasCentrifugeRecipe"])
    if ml_centrifuge != (wk_centrifuge or gt_centrifuge):
        error(f"hasCentrifugeRecipe expected {wk_centrifuge or gt_centrifuge}, got {ml_centrifuge}")
    if wk is not None and wk_centrifuge != ml_centrifuge:
        mism("centrifuge_merge", "hasCentrifugeRecipe(werkstoff)", wk_centrifuge, ml_centrifuge)
    if gt is not None and gt_centrifuge != ml_centrifuge:
        mism("centrifuge_merge", "hasCentrifugeRecipe(gt)", gt_centrifuge, ml_centrifuge)

    if wk is not None:
        for field, wk_field in (
                ("hasGas", "gas"), ("enforceOreDictUnification", "enforceUnification"),
                ("hasChemicalRecipe", "chemicalRecipes"), ("hasMixerRecipe", "mixerRecipes"),
                ("hasSifterRecipe", "sifterRecipes"),
                ("hasMetalCraftingSolidifierRecipe", "metalCraftingSolidifierRecipes"),
                ("hasMetalSolidifierRecipe", "metalSolidifierRecipes")):
            expected = bool(wk[wk_field])
            actual = bool(mlv[field])
            if expected != actual:
                error(f"{field} expected {expected!r}, got {actual!r}")
    else:
        for field in ("hasGas", "enforceOreDictUnification", "hasChemicalRecipe", "hasMixerRecipe",
                      "hasSifterRecipe", "hasMetalCraftingSolidifierRecipe", "hasMetalSolidifierRecipe"):
            if mlv[field]:
                error(f"{field} true with no werkstoff source")

    gt_auto_blast = bool(gt["autoBlast"]) if gt is not None else True
    gt_auto_vacuum = bool(gt["autoVacuum"]) if gt is not None else True
    expected_auto_blast = gt_auto_blast and "NO_AUTO_BLAST_FURNACE_RECIPES" not in wk_flags
    expected_auto_vacuum = gt_auto_vacuum and "NO_AUTO_VACUUM_FREEZER_RECIPES" not in wk_flags
    if (mlv["autoBlast"] is None or mlv["autoBlast"]) != expected_auto_blast:
        error(f"autoBlast expected {expected_auto_blast!r}, got {mlv['autoBlast']!r}")
    if (mlv["autoVacuum"] is None or mlv["autoVacuum"]) != expected_auto_vacuum:
        error(f"autoVacuum expected {expected_auto_vacuum!r}, got {mlv['autoVacuum']!r}")

    if wk is not None:
        if (mlv["meltingVoltage"] if mlv["meltingVoltage"] is not None else 120) != wk["meltingVoltage"]:
            error(f"meltingVoltage expected {wk['meltingVoltage']!r}, got {mlv['meltingVoltage']!r}")
        if (mlv["mixCircuit"] if mlv["mixCircuit"] is not None else -1) != wk["mixCircuit"]:
            error(f"mixCircuit expected {wk['mixCircuit']!r}, got {mlv['mixCircuit']!r}")
        ebf_time = mlv["ebfGasTimeMultiplier"] if mlv["ebfGasTimeMultiplier"] is not None else -1.0
        ebf_amount = mlv["ebfGasAmountMultiplier"] if mlv["ebfGasAmountMultiplier"] is not None else 1.0
        if not floats_equal(ebf_time, wk["ebfGasTimeMultiplier"]):
            error(f"ebfGasTimeMultiplier expected {wk['ebfGasTimeMultiplier']!r}, got {ebf_time!r}")
        if not floats_equal(ebf_amount, wk["ebfGasAmountMultiplier"]):
            error(f"ebfGasAmountMultiplier expected {wk['ebfGasAmountMultiplier']!r}, got {ebf_amount!r}")
        expected_sub_tags = []
        for entry in wk_entries:
            for tag in entry["subTags"]:
                if tag not in expected_sub_tags:
                    expected_sub_tags.append(tag)
        if (mlv["subTags"] or []) != expected_sub_tags:
            error(f"subTags expected {expected_sub_tags!r}, got {mlv['subTags']!r}")
    else:
        for field in ("meltingVoltage", "mixCircuit", "ebfGasTimeMultiplier", "ebfGasAmountMultiplier",
                      "subTags"):
            if mlv[field] is not None:
                error(f"{field} {mlv[field]!r} with no werkstoff source")

    if gtpp_entry is not None:
        if (mlv["tier"] or 0) != gtpp_entry["tier"]:
            error(f"tier expected {gtpp_entry['tier']!r}, got {mlv['tier']!r}")
        expected_voltage = gtpp_entry["voltageMultiplier"]
        if (mlv["voltageMultiplier"] if mlv["voltageMultiplier"] is not None else 16) != expected_voltage:
            error(f"voltageMultiplier expected {expected_voltage!r}, got {mlv['voltageMultiplier']!r}")
    else:
        if mlv["tier"] is not None:
            error(f"tier {mlv['tier']!r} with no gtpp source")
        if mlv["voltageMultiplier"] is not None:
            error(f"voltageMultiplier {mlv['voltageMultiplier']!r} with no gtpp source")

    if wk_entries:
        wk_formula = next((e["formula"] for e in wk_entries if e["formula"]), "")
        if (mlv["formula"] or "") != wk_formula:
            error(f"formula expected werkstoff {wk_formula!r}, got {mlv['formula']!r}")
        wk_localized = any(e["formulaLocalized"] for e in wk_entries)
        if bool(mlv["formulaLocalized"]) != wk_localized:
            error(f"formulaLocalized expected {wk_localized!r}, got {mlv['formulaLocalized']!r}")
    if gtpp_entry is not None:
        mism("formula_gtpp", "chemicalFormula", gtpp_entry["chemicalFormula"] or None, mlv["formula"])

    if wk is not None:
        mism("atomic_werkstoff", "protons", wk["protons"], mlv["protons"])
        mism("atomic_werkstoff", "mass", wk["mass"], mlv["mass"])
    if gtpp_entry is not None:
        mism("atomic_gtpp", "protons", gtpp_entry["protons"], mlv["protons"])
        mism("atomic_gtpp", "neutrons", gtpp_entry["neutrons"], mlv["neutrons"])

    ml_comp = [list(c) for c in mlv["composition"]]
    if gt is not None:
        gt_comp = [[ml_name(c["material"]), c["amount"]] for c in gt["composition"] if c]
        if gt_comp != ml_comp:
            dev("composition_gt", key, "composition", gt_comp, ml_comp)
    if wk_entries:
        contents = next((e["contents"] for e in wk_entries if e["contents"]), [])
        wk_comp = [[ml_name(werkstoff_ref_name(c["name"], c["kind"], display_to_var)), c["amount"]]
                   for c in contents]
        if wk_comp != ml_comp:
            dev("composition_werkstoff", key, "composition", wk_comp, ml_comp)
    if gtpp_entry is not None:
        gtpp_comp = sorted([ml_name(c["name"]), c["amount"]] for c in gtpp_entry["composition"] if c.get("name"))
        if gtpp_comp != sorted(ml_comp):
            dev("composition_gtpp", key, "composition", gtpp_comp, sorted(ml_comp))

    ml_by = list(mlv["oreByProducts"])
    if gt is not None:
        gt_by = sorted(ml_name(b) for b in gt["oreByProducts"])
        if gt_by != sorted(ml_by):
            error(f"oreByProducts expected {gt_by!r}, got {sorted(ml_by)!r}")
    if wk_entries:
        byproducts_entry = next((e for e in wk_entries if e["oreByProducts"]), None)
        if byproducts_entry:
            wk_by = [ml_name(display_to_var.get(b, b)) for b in byproducts_entry["oreByProducts"]]
        else:
            wk_by = []
        if wk_by != ml_by:
            dev("ore_byproducts_werkstoff", key, "oreByProducts", wk_by, ml_by)


def check_werkstoff(errors, name, info, ml):
    """Verifies the surviving werkstoff blob fields (identity/bookkeeping plus the recipe-gen flags pending
    U2); the collapsed scalars are covered by `collect_collapsed_deviations`."""
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
    check("flags", sorted(set(werkstoff_flag_names(entries)) - MIGRATED_WERKSTOFF_FLAGS))
    check("prefixes", info["prefixes"])


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
                    legacy_block_materials, werkstoff_info, display_to_var, gtpp_info, sink, atomics):
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
        dumped_shapes |= expected_werkstoff_shapes(name, werkstoff_info["prefixes"], included_names)
    if gtpp_info:
        dumped_shapes |= gtpp_expected_shapes(gtpp_info, gt, used_fluid_names)
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

    check_int("blastTemp", "blastTemp", 0)
    check_int("gasTemp", "gasTemp", 0)
    check_int("fuelPower", "fuelPower", 0)
    check_int("fuelType", "fuelType", 0)
    check_int("oreMultiplier", "oreMultiplier", 1)
    check_int("byProductMultiplier", "byProductMultiplier", 1)
    check_int("smeltingMultiplier", "smeltingMultiplier", 1)

    expected_sub_id = gt["subId"]
    actual_sub_id = ml["subId"] if ml["subId"] is not None else -1
    if expected_sub_id != actual_sub_id:
        errors.append(f"{name}: subId expected {expected_sub_id!r}, got {actual_sub_id!r}")

    expected_heat_damage = gt["heatDamage"]
    actual_heat_damage = ml["heatDamage"] if ml["heatDamage"] is not None else 0.0
    if not floats_equal(expected_heat_damage, actual_heat_damage):
        errors.append(f"{name}: heatDamage expected {expected_heat_damage!r}, got {actual_heat_damage!r}")

    expected_molten_default = gt["moltenRgba"] == [255, 255, 255, 0]
    expected_molten = None if expected_molten_default else to_unsigned32(pack_argb(gt["moltenRgba"]))
    actual_molten = to_unsigned32(ml["moltenTint"]) if ml["moltenTint"] is not None else None
    if expected_molten != actual_molten:
        errors.append(f"{name}: moltenTint expected {expected_molten!r}, got {actual_molten!r}")

    if gt["element"] != ml["element"]:
        sink.dev("element_gt", ml_name(name), "element", gt["element"], ml["element"])

    check_ref_field(errors, name, "smeltInto", gt["smeltInto"], ml["smeltInto"])
    check_ref_field(errors, name, "macerateInto", gt["macerateInto"], ml["macerateInto"])
    check_ref_field(errors, name, "arcSmeltInto", gt["arcSmeltInto"], ml["arcSmeltInto"])
    check_ref_field(errors, name, "directSmelting", gt["directSmelting"], ml["directSmelting"])
    check_ref_field(errors, name, "handleMaterial", gt["handleMaterial"], ml["handleMaterial"])
    # materialInto was GTMaterialProperties.MATERIAL_INTO, removed as dead (never set, only read by the dump
    # tool) -- ml-materials.json no longer carries this field.

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
    #
    # A same-name gtpp fold may claim a slot gregtech's own dump never captured (gtpp_claimed_states, e.g.
    # `Iodine`'s plasma, `Hafnium`'s entire fluid) -- gtpp_combined_fluid_refs provides gregtech's own ref
    # verbatim plus gtpp's contribution for those slots; `gt["fluids"]` alone otherwise.
    expected_fluids = gtpp_combined_fluid_refs(gtpp_info, gt) if gtpp_info else gt["fluids"]
    if name not in FLUID_CUTOVER_EXCLUDED:
        for state in FLUID_STATES:
            expected_fluid = expected_fluids.get(state)
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

    check_werkstoff(errors, name, werkstoff_info, ml)
    if gtpp_info is not None:
        check_gtpp_data(errors, name, gtpp_info, ml)

    collect_collapsed_deviations(
        sink.dev,
        lambda message: errors.append(f"{name}: {message}"),
        ml_name(name),
        gt,
        werkstoff_info["entries"] if werkstoff_info else [],
        gtpp_info,
        mlv_from_dump(ml, atomics),
        display_to_var)

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


# Mirrors gen_materials.py's GTPP_EXTERNAL_FLUID_NAMES (see its comment for the ZirconiumTetrafluoride
# rationale).
GTPP_EXTERNAL_FLUID_NAMES = {"ZirconiumTetrafluoride"}


def gtpp_generates_fluid(entry):
    if entry["unlocalizedName"] in GTPP_EXTERNAL_FLUID_NAMES:
        return False
    return gtpp_has_fluid(entry) or gtpp_generates_cells(entry)


def gtpp_generates_cells(entry):
    return any(p["prefix"] in ("cell", "cellPlasma") for p in entry["generatedParts"])


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


def has_fluids_dict(fluids):
    return any(fluids.get(state) for state in FLUID_STATES)


def gtpp_fluid_ref(name, temperature_k):
    return None if name is None else {"name": name, "temperature": temperature_k}


def gtpp_own_fluid_refs(entry):
    """Mirrors gen_materials.py's `gtpp_own_fluid_refs`: gtpp's own per-`FLUID_STATES`-slot ref for `entry`,
    independent of any gregtech-side data. Empty unless [#gtpp_generates_fluid] is true for `entry`."""
    refs = {state: None for state in FLUID_STATES}
    if not gtpp_generates_fluid(entry):
        return refs
    fluids = entry["fluids"]
    fluid_name = fluids.get("fluid")
    if fluid_name is not None:
        if fluid_name.startswith("molten."):
            slot = "molten"
        elif entry["state"] in ("GAS", "PURE_GAS"):
            slot = "gas"
        else:
            slot = "fluid"
        refs[slot] = gtpp_fluid_ref(fluid_name, celsius_to_kelvin(entry["meltingPointC"]))
    refs["plasma"] = gtpp_fluid_ref(fluids.get("plasma"), 10000)
    return refs


def gtpp_combined_fluid_refs(entry, gt_entry):
    """Mirrors gen_materials.py's `gtpp_combined_fluid_refs`: the ref governing each `FLUID_STATES` slot
    after gtpp's fold onto `gt_entry` (`None` for a "new" gtpp-only material) -- gregtech's own ref wins
    wherever it has one, gtpp's own [#gtpp_own_fluid_refs] fills any slot gregtech left empty."""
    combined = {}
    own = gtpp_own_fluid_refs(entry)
    for state in FLUID_STATES:
        gt_ref = gt_entry["fluids"].get(state) if gt_entry else None
        combined[state] = gt_ref if gt_ref else own[state]
    return combined


def gtpp_claimed_states(entry, gt_entry):
    """Mirrors gen_materials.py's `gtpp_claimed_states`: the `FLUID_STATES` slots gtpp itself must claim (it
    has a ref there and `gt_entry` does not) -- a same-name merge only needs gtpp's contribution for the
    slots gregtech's own dump never captured (e.g. `Iodine` merges its liquid/gas fluid from gregtech but
    only gtpp ever generated its plasma)."""
    own = gtpp_own_fluid_refs(entry)
    claimed = set()
    for state in FLUID_STATES:
        gt_ref = gt_entry["fluids"].get(state) if gt_entry else None
        if own[state] and not gt_ref:
            claimed.add(state)
    return claimed


def gtpp_expected_fluid_and_cell_shape_names(entry, gt_entry, used_fluid_names):
    """Mirrors gen_materials.py's `gtpp_fluid_and_cell_shape_lines`'s shape membership (not the
    `LEGACY_FLUIDS` property value itself, checked separately in `check_material`/`check_gtpp_new_material`)
    -- the bare `Materials2FluidShapes`/`Materials2CellShapes` short names a gtpp material claims for the
    states [#gtpp_claimed_states] says it owns, plus cell/cellPlasma membership driven by
    [#gtpp_combined_fluid_refs] directly (independent of which side claimed the underlying fluid -- see the
    codegen function's javadoc for why re-declaring an already-present shape is harmless)."""
    names = set()
    own = gtpp_own_fluid_refs(entry)
    claimed = gtpp_claimed_states(entry, gt_entry)
    combined = gtpp_combined_fluid_refs(entry, gt_entry)
    for state in FLUID_STATES:
        if state not in claimed:
            continue
        ref = combined[state]
        if ref["name"] not in used_fluid_names:
            used_fluid_names.add(ref["name"])
            names.add(FLUID_SHAPE_NAMES[state])

    prim_slot = next((s for s in ("molten", "fluid", "gas") if own[s]), None)
    part_prefixes = {p["prefix"] for p in entry["generatedParts"]}
    if "cell" in part_prefixes and prim_slot and combined[prim_slot]:
        names.add("cellMolten" if prim_slot == "molten" else "cell")
    if "cellPlasma" in part_prefixes and combined["plasma"]:
        names.add("cellPlasmaLight")
    return names


def gtpp_expected_shapes(entry, gt_entry, used_fluid_names):
    """The MaterialLib shape-name set a gtpp material's dumped `generatedParts` translate to -- see
    gen_materials.py's `gtpp_shape_lines`/`gtpp_fluid_and_cell_shape_lines`/`gtpp_ore_shape_lines`. Raises on
    an unmapped prefix (a future gtpp dump refresh with a new part kind must not silently drop items from this
    check either). `block` additionally honors `BLOCK_CUTOVER_EXCLUDED`, mirroring `gtpp_shape_lines`; `hasOre`
    grants `ore`, mirroring `gtpp_ore_shape_lines`."""
    names = set()
    for part in entry["generatedParts"]:
        prefix = part["prefix"]
        if prefix in GTPP_SIMPLE_PREFIXES:
            names.add(prefix)
        elif prefix == "block":
            if entry["unlocalizedName"] not in BLOCK_CUTOVER_EXCLUDED                     and entry["unlocalizedName"] not in GTPP_ANIMATED_BLOCK_EXCLUDED:
                names.add(prefix)
        elif prefix == "milled":
            names.add(prefix)
        elif prefix in ("cell", "cellPlasma"):
            continue
        elif prefix in GTPP_LEGACY_ONLY_PREFIXES or prefix in GTPP_UNSUPPORTED_PREFIXES:
            continue
        else:
            raise SystemExit(
                f"check_parity.py: gtpp material {entry['unlocalizedName']!r} generates unmapped part "
                f"prefix {prefix!r}")
    if entry.get("hasOre"):
        names.add("ore")
    if gtpp_generates_fluid(entry):
        names |= gtpp_expected_fluid_and_cell_shape_names(entry, gt_entry, used_fluid_names)
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
    """Verifies the surviving gtpp blob fields (the presence signal and fluid/cell reconstruction data); the
    collapsed scalars are covered by `collect_collapsed_deviations`."""
    data = ml.get("gtpp")
    if data is None:
        errors.append(f"{name}: missing gtpp property")
        return

    def check(field, expected):
        if data.get(field) != expected:
            errors.append(f"{name}: gtpp.{field} expected {expected!r}, got {data.get(field)!r}")

    check("state", entry["state"])
    generates_fluid = gtpp_generates_fluid(entry)
    check("generatesFluid", generates_fluid)
    check("generatesCells", gtpp_generates_cells(entry))
    check("fluidName", entry["fluids"]["fluid"] if generates_fluid else None)
    check("plasmaName", entry["fluids"]["plasma"] if generates_fluid else None)


def check_gtpp_new_material(errors, entry, ml_by_key, used_fluid_names, display_to_var, sink, atomics):
    """Full existence/tint/textureSet/shape-set/property check for a gtpp-only material (no gregtech/werkstoff
    counterpart) -- everything about its ml-materials.json entry is gtpp-sourced, so (unlike a merge) the
    shape set is expected to match `gtpp_expected_shapes` exactly, not merely be a superset of it."""
    name = entry["unlocalizedName"]
    key = ml_name(name)
    ml = ml_by_key.get(key)
    if ml is None:
        errors.append(f"{name}: gtpp material missing from ml-materials.json (expected key {key!r})")
        return

    if name in GTPP_PRECOLORED_ITEM_MATERIALS:
        expected_tint = 0xFFFFFFFF
    else:
        expected_tint = to_unsigned32(pack_argb(entry["rgba"]))
    actual_tint = to_unsigned32(ml["tint"]) if ml["tint"] is not None else None
    if expected_tint != actual_tint:
        errors.append(f"{name}: gtpp tint expected 0x{expected_tint:08X}, got {ml['tint']!r}")

    if ml["textureSet"] != entry["textureSet"]:
        errors.append(f"{name}: gtpp textureSet expected {entry['textureSet']!r}, got {ml['textureSet']!r}")

    expected_shapes = gtpp_expected_shapes(entry, None, used_fluid_names)
    actual_shapes = set(ml["shapes"])
    if expected_shapes != actual_shapes:
        missing = expected_shapes - actual_shapes
        extra = actual_shapes - expected_shapes
        errors.append(f"{name}: gtpp shapes mismatch, missing={sorted(missing)}, extra={sorted(extra)}")

    check_gtpp_data(errors, name, entry, ml)

    collect_collapsed_deviations(
        sink.dev,
        lambda message: errors.append(f"{name}: {message}"),
        key,
        None,
        [],
        entry,
        mlv_from_dump(ml, atomics),
        display_to_var)

    if gtpp_generates_fluid(entry):
        expected_refs = gtpp_own_fluid_refs(entry)
        for state in FLUID_STATES:
            expected_ref = expected_refs[state]
            actual_ref = ml["fluids"].get(state)
            if bool(expected_ref) != bool(actual_ref):
                errors.append(f"{name}: gtpp fluids.{state} expected {expected_ref!r}, got {actual_ref!r}")
            elif expected_ref and actual_ref and (expected_ref["name"] != actual_ref["name"]
                                                    or expected_ref["temperature"] != actual_ref["temperature"]):
                errors.append(f"{name}: gtpp fluids.{state} expected {expected_ref!r}, got {actual_ref!r}")


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

    sink = Deviations(load_allowlist())
    atomics = MlAtomics(ml_by_key, load_element_table())

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
                gtpp_merges_by_name.get(material["name"]), sink, atomics))

    for entry in gtpp_new:
        check_gtpp_new_material(errors, entry, ml_by_key, used_fluid_names, display_to_var, sink, atomics)

    expected_keys = set(ml_name(m["name"]) for m in ported) | set(ml_name(e["unlocalizedName"]) for e in gtpp_new)
    extra = set(ml_by_key) - expected_keys
    if extra:
        errors.append(f"unexpected ml-materials.json entries not in the ported set: {sorted(extra)}")

    errors.extend(sink.errors)
    for key in sink.unused():
        errors.append(f"allowlist entry never exercised: {key!r}")

    total = len(ported) + len(gtpp_new)
    if errors:
        print(f"check_parity.py: {len(errors)} mismatches across {total} ported materials")
        for error in errors:
            print(" -", error)
        sys.exit(1)

    print(f"check_parity.py: OK, {total} materials verified, {len(sink.used)} allowlisted deviations")


if __name__ == "__main__":
    main()
