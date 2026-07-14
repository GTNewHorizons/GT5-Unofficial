#!/usr/bin/env python3
"""RETIRED as a regeneration tool after the user-directed materials-refactor pass (between material-
unification stages 11 and 12): `src/main/java/gregtech/api/enums/materials2/Materials2Materials.java` is now
the hand-maintained materials surface and this script's output was folded into it exactly once, then the
`Materials2Data*`/`Materials2GtppData*` codegen chain it used to emit was deleted. Do not rerun this against a
fresh dump and overwrite that file -- it would blow away any hand edit made since. Kept in the repo as the
historical record of how the port was built and as a reference for `scripts/mu/check_parity.py`'s
independently-duplicated ground-truth logic (fluid/cell/block/ore shape gating, the werkstoff/gtpp folds).

Original docstring, describing what the one-time run did:

Generates the stage-03 MaterialLib material declarations from the `gt-materials.json` and
`oreprefixes.json` material-unification dumps, plus the stage-10 werkstoff fold from `werkstoff.json`
(bartworks/goodgenerator/gtnhlanth pools): every non-proxy werkstoff merges into the gregtech dump entry of
the same name (each has one -- bartworks' BridgeMaterialsLoader created a bridge `Materials` mirror per
werkstoff at runtime, which the gregtech dump captured), adding the werkstoff shape set and a composite
`WERKSTOFF` property while the gregtech dump keeps winning every shared scalar. Every fold is written to
`merge-report.txt` for review. `werkstoff.json` is a PINNED pre-stage-10 capture -- never refresh it from
a post-fold boot (see `MaterialDataDump#dumpWerkstoffGeneratedPrefixes`). The materials-refactor pass
additionally replaced per-material family membership (`select_families`) with a noise-minimizing, family-
semantics-respecting selection over the material's true final item-shape set, in place of the original
per-`OrePrefix`-bit union (see `select_families`'s docstring for why that union over-joined families).

Run from anywhere; paths are resolved relative to this script's location. Requires only the Python 3
standard library.

Usage (historical): python scripts/mu/gen_materials.py
"""

import json
import re
from pathlib import Path

SCRIPT_DIR = Path(__file__).resolve().parent
REPO_ROOT = SCRIPT_DIR.parent.parent
MATERIALS_DUMP_PATH = SCRIPT_DIR / "dumps" / "gt-materials.json"
PREFIXES_DUMP_PATH = SCRIPT_DIR / "dumps" / "oreprefixes.json"
LEGACY_VARIANTS_DUMP_PATH = SCRIPT_DIR / "dumps" / "legacy-variants.json"
FLUID_TEXTURES_DUMP_PATH = SCRIPT_DIR / "dumps" / "fluid-textures.json"
WERKSTOFF_DUMP_PATH = SCRIPT_DIR / "dumps" / "werkstoff.json"
MERGE_REPORT_PATH = SCRIPT_DIR / "merge-report.txt"

MATERIALS2_DIR = REPO_ROOT / "src/main/java/gregtech/api/enums/materials2"
MATERIALS_OUTPUT = MATERIALS2_DIR / "Materials2Materials.java"
LANG_OUTPUT = REPO_ROOT / "src/main/resources/assets/gregtech/lang/en_US.lang"

LEGACY_BRIDGE_OUTPUT = REPO_ROOT / "src/main/java/gregtech/loaders/materials/MaterialsLegacyBridge.java"

# Materials2Materials.java alone is the one exception: it was the one-time output of this script's materials-
# refactor-pass run, then hand-maintained from that point on (see the module docstring) -- edits belong in the
# file directly, not in this script.
MATERIALS_FILE_HEADER = (
    "// One-time output of scripts/mu/gen_materials.py (RETIRED, see its module docstring); hand-maintained "
    "from here -- edit this file directly.")

# Mirrors gregtech.api.enums.OrePrefixes generation-bit constants; see scripts/mu/gen_shapes.py.
DUST = 1
METAL = 2
GEM = 4
ORE = 8
CELL = 16
PLASMA = 32
TOOL = 64
GEAR = 128
EMPTY = 256

FAMILY_BITS = [
    ("familyDusts", DUST),
    ("familyMetals", METAL),
    ("familyGems", GEM),
    ("familyOres", ORE),
    ("familyCells", CELL),
    ("familyPlasmas", PLASMA),
    ("familyToolParts", TOOL),
    ("familyGears", GEAR),
    ("familyEmpties", EMPTY),
]

REF_PROPERTY_FIELDS = [
    ("smeltInto", "SMELT_INTO"),
    ("macerateInto", "MACERATE_INTO"),
    ("arcSmeltInto", "ARC_SMELT_INTO"),
    ("directSmelting", "DIRECT_SMELTING"),
    ("handleMaterial", "HANDLE_MATERIAL"),
]

FLUID_STATES = ["solid", "fluid", "gas", "plasma", "molten"]

# `gregtech.api.enums.SubTag` names that do not match their `GTMaterialFlag` counterpart 1:1; see the
# javadoc on GTMaterialFlag.
SUBTAG_FLAG_OVERRIDES = {
    "AnaerobeGas": "ANAEROBE_GAS",
    "NobleGas": "NOBLE_GAS",
}

# (hasCorrespondingFluid, hasCorrespondingGas) as MaterialsInit declared them, where the dump disagrees: the
# dump captured bartworks' BridgeMaterialsLoader setting both flags on cell-bearing werkstoff bridge
# materials AFTER LoaderGTBlockFluid's generation loop had already run with them false. Baking the dumped
# value makes that loop register a liquid and a gas fluid under one name, orphaning one of them in Forge's
# fluid registry (a load-complete "fluid registry is corrupted" FATAL). Verified against MaterialsInit source
# before stage 04 deleted it: AquaRegia was the only ported MaterialsInit-declared material affected.
CORRESPONDING_FLAG_OVERRIDES = {
    "AquaRegia": (False, False),
}

BLOCK_TEXTURE_INDEX_MIN = 65
BLOCK_TEXTURE_INDEX_MAX = 95


# region prefix filtering (mirrors scripts/mu/gen_shapes.py so the shape delta lines up with Materials2Shapes)


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
    # Bugfix (group A, mirrors gen_shapes.py): intersect with legacy-variants.json ground truth so a
    # capability-bit-only prefix (no real constructor slot, e.g. crystal/milled/shard) is not treated as an
    # included shape here either -- keeps this file's shape set identical to Materials2Shapes.
    return prefix["name"] in legacy_variant_prefixes


def shape_field_name(prefix_name):
    return "shape" + prefix_name[0].upper() + prefix_name[1:]


# endregion

# region name sanitizing


def ml_name(name):
    """The string registered with MaterialLib: `Names.validate` rejects ':' and whitespace, nothing else, so
    only those are stripped. A handful of dynamically-generated (non-`Materials`-field) materials carry them
    (e.g. "Computation Base", "Unformed Hexanite"); every other name passes through unchanged."""
    return "".join(ch for ch in name if ch != ":" and not ch.isspace())


def field_name(name):
    """A valid Java identifier for the material's static field. Most dump names already are one; a few dozen
    chemical-formula names carry parentheses, commas, hyphens, or a leading digit, which are stripped/guarded
    the same way the legacy `Materials` class does for its equivalent fields (e.g. `Cobalt(II)Nitrate` ->
    `CobaltIINitrate`)."""
    cleaned = "".join(ch for ch in name if ch.isalnum())
    if not cleaned or cleaned[0].isdigit():
        cleaned = "_" + cleaned
    return cleaned


def java_string_literal(value):
    escaped = value.replace("\\", "\\\\").replace('"', '\\"')
    return '"' + escaped + '"'


def java_float_literal(value):
    if value == float("inf"):
        return "Float.POSITIVE_INFINITY"
    if value == float("-inf"):
        return "Float.NEGATIVE_INFINITY"
    return repr(float(value)) + "F"


def java_double_literal(value):
    if value == float("inf"):
        return "Double.POSITIVE_INFINITY"
    if value == float("-inf"):
        return "Double.NEGATIVE_INFINITY"
    return repr(float(value)) + "D"


def pack_argb(rgba):
    r, g, b, a = rgba
    if a == 0:
        a = 255
    return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF)


def pack_argb_exact(rgba):
    """Like `pack_argb`, but without the alpha=0-becomes-255 substitution `StandardProperties.TINT` needs (an
    alpha-0 packed color is fully transparent, which MaterialLib's own rendering treats as "no tint" -- but
    alpha 0x00 is `MaterialBuilder`'s own default and the overwhelmingly common legacy value, so the legacy
    bridge needs the untouched byte to reproduce `Materials#mRGBa`/`mMoltenRGBa` exactly."""
    r, g, b, a = rgba
    return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF)


def java_int_literal(value):
    if value < 0:
        return str(value)
    if value > 0x7FFFFFFF:
        return "0x%08X" % (value & 0xFFFFFFFF)
    return str(value)


# endregion


def load_materials():
    with open(MATERIALS_DUMP_PATH, encoding="utf-8") as f:
        return json.load(f)


def load_prefixes():
    with open(PREFIXES_DUMP_PATH, encoding="utf-8") as f:
        return json.load(f)["prefixes"]


def load_legacy_variants():
    with open(LEGACY_VARIANTS_DUMP_PATH, encoding="utf-8") as f:
        return json.load(f)


def load_fluid_textures():
    """Empty (every `FluidRef.texture` null) until the first dump run produces `fluid-textures.json` -- this
    script must be able to bootstrap a build that can run that dump in the first place."""
    if not FLUID_TEXTURES_DUMP_PATH.exists():
        return {}
    with open(FLUID_TEXTURES_DUMP_PATH, encoding="utf-8") as f:
        return json.load(f)


def load_legacy_variant_prefixes(variants):
    return {v["prefix"] for v in variants}


# region werkstoff fold (stage 10)

# Werkstoff prefixes that keep their legacy bartworks blocks (GregTechAPI.sBlockSheetmetalBW /
# sBlockFramesBW): mirrors the gregtech-side decision that frameGt/sheetmetal never cut over to MaterialLib
# shapes (stage 07 scoped `block` only). They still appear in GTMaterialProperties#WERKSTOFF_PREFIXES so the
# legacy reconstruction keeps serving them.
WERKSTOFF_LEGACY_ONLY_PREFIXES = {"sheetmetal", "frameGt"}

# Werkstoff prefixes with a hand-written (non-Materials2Shapes) MaterialLib shape.
WERKSTOFF_SPECIAL_PREFIXES = {"cell", "cellMolten", "block", "ore", "blockCasing", "blockCasingAdvanced"}

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
]

# GTWerkstoffFlag members the U1 collapse moved onto canonical properties (see werkstoff_property_lines).
MIGRATED_WERKSTOFF_FLAGS = {
    "TOXIC", "RADIOACTIVE", "BLAST_FURNACE", "NO_AUTO_BLAST_FURNACE_RECIPES", "NO_AUTO_VACUUM_FREEZER_RECIPES"
}

# Werkstoffe whose legacy contents never became canonical COMPOSITION: for Salt/Spodumene/RockSalt the
# gregtech-side COMPOSITION won the conflict (the werkstoff lists were self-referencing stubs),
# SodiumNitrate's already matched, and Calcium/Tellurium carried only the Materials-extension stub
# WerkstoffReconstruction synthesizes itself (its GT_ELEMENT_CONTENT_NAMES).
WERKSTOFF_CONTENTS_NOT_MIGRATED = {"Salt", "Spodumene", "RockSalt", "SodiumNitrate", "Calcium", "Tellurium"}


def load_werkstoffs():
    with open(WERKSTOFF_DUMP_PATH, encoding="utf-8") as f:
        return json.load(f)


def group_werkstoffs(werkstoffs):
    """Groups the dump by target gregtech material name. Non-proxy entries key by `varName` (== the bridge
    mirror's `mName`); the `id > 31765` proxies (werkstoffe that exist only to give a GT material bartworks
    casings/items) are not ported themselves -- only their `generatedPrefixes` fold onto their
    `bridgeMaterial`'s declaration. Returns `(by_name, display_to_var)` where each `by_name` value is
    `{"entries": [...], "prefixes": [...]}` (entries in id order, possibly empty for a proxy-only target;
    prefixes union-ordered first-entry-first)."""
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
        if entry["isProxy"] and name is None:
            raise SystemExit(f"gen_materials.py: proxy werkstoff {entry['id']} has no bridgeMaterial")
        info = by_name.setdefault(name, {"entries": [], "prefixes": []})
        if not entry["isProxy"]:
            info["entries"].append(entry)
        for prefix in entry["generatedPrefixes"]:
            if prefix not in info["prefixes"]:
                info["prefixes"].append(prefix)
    return by_name, display_to_var


def werkstoff_ref_name(ref_name, kind, display_to_var):
    """Maps a werkstoff-dump reference (contents/oreByProducts entry) to a gregtech material name: werkstoff
    references use the display name (`getDefaultName`), material references the legacy `mName` (== the ml
    name for every name the werkstoff data can reach)."""
    if kind == "werkstoff":
        var = display_to_var.get(ref_name)
        if var is None:
            raise SystemExit(f"gen_materials.py: dangling werkstoff reference {ref_name!r}")
        return var
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


def validate_werkstoff_prefixes(werkstoff_by_name, included_names):
    """Reference-closure-style fail-loud check: every dumped werkstoff prefix must map to a MaterialLib shape
    or be a deliberate legacy retention, so a new bartworks prefix cannot silently drop items."""
    known = included_names | WERKSTOFF_SPECIAL_PREFIXES | WERKSTOFF_LEGACY_ONLY_PREFIXES
    for name, info in sorted(werkstoff_by_name.items()):
        unknown = [p for p in info["prefixes"] if p not in known]
        if unknown:
            raise SystemExit(f"gen_materials.py: werkstoff {name!r} generates unmapped prefixes {unknown}")


def werkstoff_flag_names(entries):
    flags = []
    for entry in entries:
        for field, flag in WERKSTOFF_FLAG_FIELDS:
            if entry[field] and flag not in flags:
                flags.append(flag)
        if not entry["autoBlastFurnaceRecipes"] and "NO_AUTO_BLAST_FURNACE_RECIPES" not in flags:
            flags.append("NO_AUTO_BLAST_FURNACE_RECIPES")
        if not entry["autoVacuumFreezerRecipes"] and "NO_AUTO_VACUUM_FREEZER_RECIPES" not in flags:
            flags.append("NO_AUTO_VACUUM_FREEZER_RECIPES")
    return flags


def werkstoff_property_lines(info, ml_names, display_to_var, canonical_melting_point):
    """The werkstoff-side property lines for a werkstoff fold: the surviving decomposed
    `GTMaterialProperties.WERKSTOFF_*` keys (see each key's javadoc there for its exact elision rule) plus
    the canonical properties the U1 collapse landed the former werkstoff scalars/flags on.
    `neutrons`/`enchantmentLevel`/`additionalOredict` are never emitted (always `0`/constant `3`/empty across
    every werkstoff-backed material). Scalars come from the first (lowest-id) entry; flags/prefixes/subTags
    union across a same-name fold; contents/formula come from the first entry carrying one; oreByProducts are
    not emitted at all (the canonical `ORE_BYPRODUCTS` from the gregtech dump is the single source, matching
    the werkstoff list everywhere but the 22 divergences the collapse table flags). The canonical emits below
    reproduce the no-conflict base case only: where a canonical value already existed and diverged, the
    committed `Materials2Materials` carries the per-material winner from the collapse decision table. The
    tool-stat overrides (`durability`/`speed`/`quality`/`durabilityModifier`) emit nothing -- each equaled
    the canonical `DURABILITY`/`TOOL_SPEED`/`TOOL_QUALITY` value, and `WerkstoffReconstruction`'s closed
    override sets carry the which-werkstoffe-override knowledge instead. `canonical_melting_point` is the
    gregtech/legacy-side `MELTING_POINT` already set on this material's declaration (`None` when absent)."""
    entries = info["entries"]
    if not entries:
        return []
    first = entries[0]

    lines = []

    def emit(prop, value_text):
        lines.append(f".setProperty(GTMaterialProperties.{prop}, {value_text})")

    ids = ", ".join(str(e["id"]) for e in entries)
    emit("WERKSTOFF_IDS", f"List.of({ids})")
    emit("WERKSTOFF_TYPE", java_string_literal(first["type"]))
    emit("WERKSTOFF_POOL", java_string_literal(first["pool"]))

    if canonical_melting_point is None:
        emit("MELTING_POINT", str(first["meltingPoint"]))
    if first["boilingPoint"] != 0:
        emit("BOILING_POINT", str(first["boilingPoint"]))
    # Proton/neutron/mass counts are never emitted: the facades compute them from ELEMENT/COMPOSITION
    # via gregtech.api.material.MaterialAtomics (the legacy Materials density-weighted formula).
    if first["meltingVoltage"] != 120:
        emit("MELTING_VOLTAGE", str(first["meltingVoltage"]))
    if first["ebfGasTimeMultiplier"] != -1.0:
        emit("EBF_GAS_TIME_MULTIPLIER", java_double_literal(first["ebfGasTimeMultiplier"]))
    if first["ebfGasAmountMultiplier"] != 1.0:
        emit("EBF_GAS_AMOUNT_MULTIPLIER", java_double_literal(first["ebfGasAmountMultiplier"]))
    if first["mixCircuit"] != -1:
        emit("MIX_CIRCUIT", str(first["mixCircuit"]))

    # The hazard and auto-recipe-gating members landed on canonical booleans (BLAST_FURNACE needs no emit:
    # every carrier's declaration already sets BLAST_REQUIRED); the rest stay GTWerkstoffFlag members
    # pending the bartworks recipe-gen decision.
    flags = werkstoff_flag_names(entries)
    if "TOXIC" in flags:
        emit("TOXIC", "true")
    if "RADIOACTIVE" in flags:
        emit("IS_RADIOACTIVE", "true")
    if "NO_AUTO_BLAST_FURNACE_RECIPES" in flags:
        emit("AUTO_BLAST_FURNACE_RECIPES", "false")
    if "NO_AUTO_VACUUM_FREEZER_RECIPES" in flags:
        emit("AUTO_VACUUM_FREEZER_RECIPES", "false")
    kept_flags = [f for f in flags if f not in MIGRATED_WERKSTOFF_FLAGS]
    if kept_flags:
        emit("WERKSTOFF_FLAGS", "EnumSet.of(" + ", ".join("GTWerkstoffFlag." + f for f in kept_flags) + ")")
    if info["prefixes"]:
        emit("WERKSTOFF_PREFIXES", "List.of(" + ", ".join(java_string_literal(p) for p in info["prefixes"]) + ")")

    def ref_stack(name, kind, amount):
        mapped = ml_names[werkstoff_ref_name(name, kind, display_to_var)]
        return f"new MaterialRefStack(new MaterialRef({java_string_literal(mapped)}), {amount}L)"

    # Contents land on canonical COMPOSITION (the registry-selector bit dies -- `WerkstoffReconstruction`
    # re-derives each reference's registry from its name). The materials in
    # WERKSTOFF_CONTENTS_NOT_MIGRATED emit nothing: either the gregtech-side COMPOSITION won the conflict
    # or the legacy contents were a Materials-extension stub the reconstruction synthesizes itself.
    contents_entry = next((e for e in entries if e["contents"]), None)
    if contents_entry and first["varName"] not in WERKSTOFF_CONTENTS_NOT_MIGRATED:
        content_stacks = ", ".join(
            ref_stack(c["name"], c["kind"], c["amount"]) for c in contents_entry["contents"])
        emit("COMPOSITION", f"List.of({content_stacks})")

    sub_tags = []
    for entry in entries:
        for tag in entry["subTags"]:
            if tag not in sub_tags:
                sub_tags.append(tag)
    if sub_tags:
        emit("SUB_TAGS", "List.of(" + ", ".join(java_string_literal(t) for t in sub_tags) + ")")

    # The formula lands on canonical FORMULA (the werkstoff value beat any same-name gtpp value), with
    # FORMULA_LOCALIZED carrying the former LOCALIZED_FORMULA flag.
    formula = next((e["formula"] for e in entries if e["formula"]), "")
    if formula:
        emit("FORMULA", java_string_literal(formula))
        if any(e["formulaLocalized"] for e in entries):
            emit("FORMULA_LOCALIZED", "true")

    return lines


# endregion

# region gtpp fold (stage 11)

GTPP_DUMP_PATH = SCRIPT_DIR / "dumps" / "gtpp-materials.json"
GTPP_MERGE_REPORT_PATH = SCRIPT_DIR / "gtpp-merge-report.txt"

# `frameGt` is block-kind and GT itself never cuts `frameGt` over to a MaterialLib shape (stage 07 scoped
# `block` only, the same reason `WERKSTOFF_LEGACY_ONLY_PREFIXES` excludes it) -- gtPlusPlus's own
# `BlockBaseModular` frame blocks stay legacy pending stage 11 commit 4's block/ore cutover.
GTPP_LEGACY_ONLY_PREFIXES = {"frameGt"}

# `cell`/`cellPlasma` are not part of `GTPP_SIMPLE_PREFIXES`'s uniform 1:1 shape mapping: unlike every other
# part, which shape shape they generate onto depends on which fluid slot the material's fluid claimed (see
# `gtpp_fluid_and_cell_shape_lines`), so they are handled by that dedicated function instead of
# `gtpp_shape_lines`.
GTPP_DEFERRED_PREFIXES = {"cell", "cellPlasma"}

# Appear only on Hypogen (wireGt01..16, gearGtSmall already has a real shape so is not here)/Inconel792
# (pipeMedium)/Staballoy (pipeHuge), whose dumped `registryName` is `gregtech:gt.blockmachines`/
# `gregtech:gt.blockframes` -- gtPlusPlus reused GregTech's own `MetaGeneratedItem`/frame block for those
# slots rather than registering its own, so there is no miscutils-owned item to cut over and no gregtech shape
# to generate either (those materials are name-collision merges; the underlying legacy items are already
# whatever gregtech's own cutover decided).
GTPP_UNSUPPORTED_PREFIXES = {
    "pipeHuge", "pipeMedium", "wireGt01", "wireGt02", "wireGt04", "wireGt08", "wireGt12", "wireGt16"
}

# gtpp part prefixes with a 1:1 stage-02 Materials2Shapes field, using the same `shape<Prefix>` naming
# convention as `shape_field_name`.
GTPP_SIMPLE_PREFIXES = [
    "bolt", "crushed", "crushedCentrifuged", "crushedPurified", "dust", "dustImpure", "dustPure", "dustSmall",
    "dustTiny", "foil", "gearGt", "gearGtSmall", "ingot", "ingotHot", "nugget", "plate", "plateDense",
    "plateDouble", "plateSuperdense", "rawOre", "ring", "rotor", "screw", "spring", "springSmall", "stick",
    "stickLong", "wireFine"
]

GTPP_MARKER_NAMES = ("Brine", "Magic", "SaltWater", "SodiumChloride", "SoulSand", "Water")

# gtpp-only materials that are pure elements: the gtpp dump carries no element reference, but the computed
# atomic quantities (gregtech.api.material.MaterialAtomics) need canonical ELEMENT to read the Element table
# rather than fall back to Tc. Uranium232/Uranium233/Plutonium238 gained their Element.java isotope entries
# (U_232/U_233/Pu_238) in the U2 pass; Thorium232 is folded through the werkstoff/gt merge path instead (see
# GT_ELEMENT_BACKFILL below).
GTPP_ELEMENT_BACKFILL = {
    "Bromine": "Br", "Curium": "Cm", "Fermium": "Fm", "Germanium": "Ge", "Lithium7": "Li",
    "Neptunium": "Np", "Plutonium238": "Pu_238", "Polonium": "Po", "Protactinium": "Pa", "Radium": "Ra",
    "Rhenium": "Re", "Selenium": "Se", "Technetium": "Tc", "Thallium": "Tl", "Uranium232": "U_232",
    "Uranium233": "U_233",
}


def load_gtpp_materials():
    with open(GTPP_DUMP_PATH, encoding="utf-8") as f:
        return json.load(f)


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
    """Stage-03-style marker skip (see `compute_ported`): a part-less, composition-less, fluid-less gtpp
    entry is skipped unless another gtpp material's composition references it, or it collides by name with an
    already-ported gregtech/werkstoff material (a merge target is always ported -- its gregtech declaration
    already exists regardless of what gtpp itself carries)."""
    referenced = compute_gtpp_referenced(gtpp_materials)
    ported = []
    skipped = []
    for material in gtpp_materials:
        name = material["unlocalizedName"]
        if gtpp_is_marker(material) and name not in referenced and name not in base_ported_names:
            skipped.append(name)
        else:
            ported.append(material)
    return ported, skipped


def gtpp_shape_lines(entry, owner_prefix=""):
    """`generateShape(...)` lines (without the `.generateShape(` wrapper handled by the caller -- returns bare
    `Owner.field` references) for every dumped gtpp part this stage's codegen can act on, plus the set of
    prefixes it deliberately left for a later commit (`GTPP_DEFERRED_PREFIXES`/`GTPP_LEGACY_ONLY_PREFIXES`/
    `GTPP_UNSUPPORTED_PREFIXES`, see their docstrings). Raises if a dumped prefix is not accounted for by any
    of those sets -- a reference-closure-style fail-loud check mirroring `validate_werkstoff_prefixes`, so a
    future gtpp dump refresh with a new part kind cannot silently drop items. The `block` case additionally
    honors `BLOCK_CUTOVER_EXCLUDED`: a gtpp material sharing a name with one of those (e.g. Copper, whose
    gtpp-side `BlockBaseModular` construction resolves onto the shared legacy `gt.blockmetal2` slot) must not
    gain `shapeBlock` membership through the gtpp fold when the gregtech side of the same declaration was
    deliberately kept off it; likewise `GTPP_ANIMATED_BLOCK_EXCLUDED`, for gtpp materials whose legacy block
    has its own animated icon with no MaterialLib equivalent."""
    refs = []
    deferred = []
    for part in entry["generatedParts"]:
        prefix = part["prefix"]
        if prefix in GTPP_SIMPLE_PREFIXES:
            refs.append(f"Materials2Shapes.{shape_field_name(prefix)}")
        elif prefix == "block":
            if entry["unlocalizedName"] not in BLOCK_CUTOVER_EXCLUDED \
                    and entry["unlocalizedName"] not in GTPP_ANIMATED_BLOCK_EXCLUDED:
                refs.append("Materials2BlockShapes.shapeBlock")
        elif prefix == "milled":
            refs.append("Materials2GtppShapes.shapeMilled")
        elif prefix in GTPP_DEFERRED_PREFIXES:
            deferred.append(prefix)
        elif prefix in GTPP_LEGACY_ONLY_PREFIXES or prefix in GTPP_UNSUPPORTED_PREFIXES:
            pass
        else:
            raise SystemExit(
                f"gen_materials.py: gtpp material {entry['unlocalizedName']!r} generates unmapped part "
                f"prefix {prefix!r}")
    seen = set()
    deduped = []
    for ref in refs:
        if ref not in seen:
            seen.add(ref)
            deduped.append(ref)
    return deduped, deferred


def celsius_to_kelvin(celsius):
    """Mirrors `gtPlusPlus.core.util.math.MathUtils#celsiusToKelvin`: `round(celsius + 273.15)`. Despite the
    dump's `*PointC` field names, the value is already Kelvin for every material gtPlusPlus copied straight
    from a gregtech `Materials` constant (the overwhelming majority of the ~138 same-name folds), since those
    definitions pass the gregtech constant's Kelvin melting point as the "Celsius" constructor argument
    verbatim -- converting anyway reproduces exactly what the legacy class itself computed either way."""
    return round(celsius + 273.15)


# Materials whose dumped fluid/cell did NOT come from `Material#performFluidAndCellRegistration` (their
# legacy declaration passed `generateFluid=false`), so `gtpp_generates_fluid`'s observable-effect derivation
# would misattribute it:
# - ZirconiumTetrafluoride: `ModItems#runMaterialGenerator` registers the no-prefix `zirconiumtetrafluoride`
#   fluid (plus its cell) and hands it over via `Material#setFluid`; letting reconstruction run the
#   constructor path instead would register a spurious extra `molten.zirconiumtetrafluoride`.
GTPP_EXTERNAL_FLUID_NAMES = {"ZirconiumTetrafluoride"}


def gtpp_generates_fluid(entry):
    """Whether the legacy `Material` constructor's `generateFluid` flag was true, i.e. whether
    `Material#performFluidAndCellRegistration` ran for this material at dump time -- see
    `GTMaterialProperties#GTPP_GENERATES_FLUID`'s javadoc. Not itself dumped, but recoverable from its
    observable effect (that method is the only in-constructor source of a fluid, a plasma, or a
    `cell`/`cellPlasma` part), minus the externally-assigned exceptions in `GTPP_EXTERNAL_FLUID_NAMES`."""
    if entry["unlocalizedName"] in GTPP_EXTERNAL_FLUID_NAMES:
        return False
    return gtpp_has_fluid(entry) or any(
        p["prefix"] in ("cell", "cellPlasma") for p in entry["generatedParts"])


def gtpp_generates_cells(entry):
    """Whether the legacy `vGenerateCells` flag was `true` -- recoverable exactly from a `cell`/`cellPlasma`
    generated part, since `Material#checkForCellAndGenerate`/`#generateFluid` never register one otherwise."""
    return any(p["prefix"] in ("cell", "cellPlasma") for p in entry["generatedParts"])


def gtpp_scalar_property_lines(entry, gt_entry, ml_names):
    """The gtpp-side property lines for a gtpp material: the surviving decomposed
    `GTMaterialProperties.GTPP_*` keys (see each key's javadoc there for its exact elision rule) plus the
    canonical properties the U1 collapse landed the former gtpp scalars on. `hasOre` is never emitted (shape
    membership -- see `gtpp_ore_shape_lines` -- already carries that signal) and `fluidName` is never emitted
    (reconstruction derives it from `GTMaterialProperties#LEGACY_FLUIDS`'s molten/fluid/gas slots, gated on
    `GTPP_GENERATES_FLUID`, rather than pinning a redundant name). `gt_entry` is the gregtech/werkstoff dump
    entry this gtpp material is same-name merging onto (`None` for a gtpp-owned "new" declaration, whose own
    canonical scalars are set by nothing but this call). The canonical emits reproduce the no-conflict base
    case only: a merge whose gregtech-side canonical value already exists keeps it (melting point,
    durability, blast requirement), with the per-material exceptions the collapse decision table resolved
    (e.g. Force adopting gtpp's blast requirement) committed directly in `Materials2Materials`."""
    lines = []

    def emit(prop, value_text):
        lines.append(f".setProperty(GTMaterialProperties.{prop}, {value_text})")

    if entry["tier"] != 0:
        emit("TIER", str(entry["tier"]))
    if entry["voltageMultiplier"] != 16:
        emit("VOLTAGE_MULTIPLIER", f"{entry['voltageMultiplier']}L")

    # For a "new" declaration (gt_entry is None) the caller already mirrors meltingPointC/usesBlastFurnace
    # verbatim into the canonical MELTING_POINT/BLAST_REQUIRED (build_gtpp_new_block); a merge keeps the
    # gregtech-side canonical melting point (the +273-double-conversion divergences collapsed to it).

    # GregTech's own dump never carries a boiling point (see GTMaterialProperties#BOILING_POINT), so gtpp's
    # own value is always the sole source.
    emit("BOILING_POINT", str(celsius_to_kelvin(entry["boilingPointC"])))

    canon_durability = gt_entry["toolDurability"] if gt_entry and gt_entry["toolDurability"] != 0 else None
    if canon_durability is None:
        emit("DURABILITY", str(entry["durability"]))

    if entry["isRadioactive"]:
        emit("IS_RADIOACTIVE", "true")
    if entry["radiationLevel"] != 0:
        emit("RADIATION_LEVEL", str(entry["radiationLevel"]))
    # The formula lands on canonical FORMULA, stored in display form
    # (StringUtils#sanitizeStringKeepBrackets(Question) applied the way the legacy gtpp tooltip renderer
    # did). This emit is the no-conflict base case: the committed Materials2Materials carries it only
    # where no gregtech/werkstoff-side value won the key (a Materials2Formulas entry and a werkstoff
    # formula both beat gtpp's).
    if entry["unlocalizedName"] in GTPP_ELEMENT_BACKFILL:
        emit("ELEMENT", java_string_literal(GTPP_ELEMENT_BACKFILL[entry["unlocalizedName"]]))
    if entry["chemicalFormula"]:
        emit("FORMULA", java_string_literal(gtpp_display_formula(entry["chemicalFormula"])))

    emit("GTPP_STATE", java_string_literal(entry["state"]))

    generates_fluid = gtpp_generates_fluid(entry)
    plasma_name = entry["fluids"]["plasma"] if generates_fluid else None
    if plasma_name is not None:
        emit("GTPP_PLASMA_NAME", java_string_literal(plasma_name))

    if generates_fluid:
        emit("GTPP_GENERATES_FLUID", "true")
    if gtpp_generates_cells(entry):
        emit("GTPP_GENERATES_CELLS", "true")

    def composition_literal():
        return material_ref_stack_list_literal(
            [{
                "material": c["name"], "amount": c["amount"]
            } for c in entry["composition"]], ml_names)

    # A single canonical COMPOSITION carries the make-up: gtpp's own list lands there whenever the
    # gregtech side carried none (a populated gregtech-side list wins the shared key --
    # fold_gtpp_materials already rejected genuine two-sided disagreements as false merges), and a
    # material whose components did not all port emits nothing rather than pinning an empty list.
    if entry["composition"] and (gt_entry is None or not gt_entry["composition"]):
        literal = composition_literal()
        if literal != "List.of()":
            emit("COMPOSITION", literal)

    return lines


def gtpp_fluid_ref(name, temperature_k):
    return None if name is None else {"name": name, "temperature": temperature_k}


def gtpp_own_fluid_refs(entry):
    """gtpp's own per-`FLUID_STATES`-slot ref for `entry`, independent of any gregtech-side data: a gtpp
    material only ever has one non-plasma fluid, and its dumped name already reveals which slot it belongs
    in -- a `molten.`-prefixed name is the molten slot (every `SOLID`-state material,
    `Material#generateFluid`'s `addGTFluidMolten` branch, plus every `LIQUID`/`PURE_LIQUID`-state material,
    which also routes through `addGTFluidMolten`); a `GAS`/`PURE_GAS`-state material's bare name is the gas
    slot; any other bare name (an already-registered Forge fluid an early lookup found, e.g. `Ammonia`) is
    the fluid slot. Plasma is always registered at a fixed 10000 K (`FluidUtils#addGTPlasma`'s hardcoded
    temperature), independent of the material's own melting point. Empty (no slot populated) unless
    [#gtpp_generates_fluid] is true for `entry`."""
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
    """The ref governing each `FLUID_STATES` slot after gtpp's fold onto `gt_entry` (`None` for a "new"
    gtpp-only material): gregtech's own ref wins wherever it has one (a genuine same-name merge -- gtpp never
    overrides an already-fluid-having gregtech slot with reconstruction-era data, the stage-10 trap this
    mirrors), gtpp's own [#gtpp_own_fluid_refs] fills any slot gregtech left empty. Distinct from
    [#gtpp_claimed_states], which of these slots gtpp itself must register (this includes every governing
    ref regardless of which side registered it, since cell/cellPlasma shape membership needs to know whether
    the slot ends up populated at all, not who populated it)."""
    combined = {}
    own = gtpp_own_fluid_refs(entry)
    for state in FLUID_STATES:
        gt_ref = gt_entry["fluids"].get(state) if gt_entry else None
        combined[state] = gt_ref if gt_ref else own[state]
    return combined


def gtpp_claimed_states(entry, gt_entry):
    """The `FLUID_STATES` slots gtpp itself must claim: it has a ref there and `gt_entry` (the same-name
    gregtech/werkstoff declaration, `None` for a "new" material) does not. A same-name merge only needs
    gtpp's contribution for the slots gregtech's own dump never captured -- a handful of materials are
    partial (e.g. `Iodine` merges its liquid/gas fluid from gregtech but only gtpp ever generated its plasma;
    `Hafnium`/`Zirconium` get every slot from gtpp, gregtech captured no fluid for them at all)."""
    own = gtpp_own_fluid_refs(entry)
    claimed = set()
    for state in FLUID_STATES:
        gt_ref = gt_entry["fluids"].get(state) if gt_entry else None
        if own[state] and not gt_ref:
            claimed.add(state)
    return claimed


def gtpp_fluid_and_cell_shape_lines(entry, gt_entry, used_fluid_names, fluid_textures):
    """`generateShape(...)` lines plus the `LEGACY_FLUIDS` property line for a gtpp material, mirroring
    gregtech's own `fluid_shape_lines`/`container_shape_lines` but keyed off gtpp's single fluid+plasma pair
    (see [#gtpp_own_fluid_refs]) instead of gregtech's five-slot `FluidNames` split.

    The `LEGACY_FLUIDS` property is only emitted when [#gtpp_claimed_states] is non-empty (gtpp has at least
    one slot to contribute), and always carries the full [#gtpp_combined_fluid_refs] -- reproducing
    gregtech's own already-set slots verbatim -- since `setProperty` overwrites rather than merges, unlike
    `generateShape`.

    `cell`/`cellPlasma` shape membership, by contrast, is driven by [#gtpp_combined_fluid_refs] directly
    (not gated on which side claimed the underlying fluid): gtpp's `cell` item always holds whichever single
    fluid the material ends up with, whoever registered it, so it maps to `shapeCellMolten` for the molten
    slot or `shapeCell` for fluid/gas; `cellPlasma` always maps to `shapeCellPlasmaLight` (unlike gregtech's
    own plasma cells, gtpp's `FluidUtils#addGTPlasma` never varied cell volume by whether the material also
    had a molten fluid -- always the 1000 mB IC2 cell, so the 144 mB `shapeCellPlasma` candidate never
    applies to a gtpp-owned plasma cell). Re-declaring a shape gregtech's own declaration already generated
    is harmless (`generateShape` is idempotent -- gregtech merges already rely on this for every other
    shared part, e.g. a merge material's `dust` line duplicating what its family membership already implies)
    -- e.g. `Rhodium`/`Californium`/`Ruthenium` have a gregtech-registered molten fluid with no gregtech
    `cellMolten` container at all (no legacy `cellMolten` item ever existed for them), so gtpp's `cell` part
    is the only thing that ever claims `shapeCellMolten` for them."""
    own = gtpp_own_fluid_refs(entry)
    claimed = gtpp_claimed_states(entry, gt_entry)
    lines = []
    if claimed:
        combined = gtpp_combined_fluid_refs(entry, gt_entry)
        fluid_args = ", ".join(fluid_ref_literal(combined[state], fluid_textures) for state in FLUID_STATES)
        lines.append(f"            .setProperty(GTMaterialProperties.LEGACY_FLUIDS, new FluidNames({fluid_args}))")
        for state in FLUID_STATES:
            if state not in claimed:
                continue
            ref = combined[state]
            if ref["name"] not in used_fluid_names:
                used_fluid_names.add(ref["name"])
                lines.append(f"            .generateShape(Materials2FluidShapes.{FLUID_SHAPE_FIELDS[state]})")

    combined = gtpp_combined_fluid_refs(entry, gt_entry)
    prim_slot = next((s for s in ("molten", "fluid", "gas") if own[s]), None)
    part_prefixes = {p["prefix"] for p in entry["generatedParts"]}
    if "cell" in part_prefixes and prim_slot and combined[prim_slot]:
        field = "shapeCellMolten" if prim_slot == "molten" else "shapeCell"
        lines.append(f"            .generateShape(Materials2CellShapes.{field})")
    if "cellPlasma" in part_prefixes and combined["plasma"]:
        lines.append("            .generateShape(Materials2CellShapes.shapeCellPlasmaLight)")

    return lines


def gtpp_ore_shape_lines(entry):
    """`generateShape(Materials2OreShapes.shapeOre)` when the dumped material generated a legacy `BlockBaseOre`
    (`entry["hasOre"]` -- not itself a `generatedParts` prefix, so it is handled here rather than in
    `gtpp_shape_lines`). Emitted unconditionally, same as the fluid/cell shape lines: for a same-name merge
    this is the only way to grant ore membership when gregtech's own dump never captured it for that material
    (`generateShape` is idempotent, so re-declaring it for a merge material gregtech's own dump already
    granted it costs nothing). Every gtpp ore variant is `StoneType.Stone` ("stone") --
    the legacy `GTPPOreAdapter` hardcoded `StoneType.Stone` unconditionally, gtpp never generated ore on any
    other stone type."""
    if entry.get("hasOre"):
        return ["            .generateShape(Materials2OreShapes.shapeOre)"]
    return []


def gtpp_display_formula(formula):
    """The legacy gtpp tooltip renderer's cleanup: strip the sanitizeStringKeepBrackets set, keeping `?`
    when the formula contains one (the KeepBracketsQuestion variant)."""
    drop = " -_!@#" if "?" in formula else " -_?!@#"
    return "".join(c for c in formula if c not in drop)


def gtpp_composition_set(entry):
    return {(c["name"], c["amount"]) for c in entry["composition"] if c.get("name")}


def gt_composition_set(gt_entry):
    return {(c["material"], c["amount"]) for c in gt_entry["composition"] if c and c.get("material")}


def gtpp_composition_mismatch(gtpp_entry, gt_entry):
    """The stage-11 trap check: a same-name fold onto a gregtech/werkstoff declaration is only safe when both
    sides agree on what the material is made of. Both composition lists are compared as-dumped (name, amount)
    sets; either side being empty is not itself a conflict (an undumped composition is not evidence of a
    different substance), only a genuine disagreement between two non-empty lists is."""
    gtpp_comp = gtpp_composition_set(gtpp_entry)
    gt_comp = gt_composition_set(gt_entry)
    if gtpp_comp and gt_comp and gtpp_comp != gt_comp:
        return gtpp_comp, gt_comp
    return None


def fold_gtpp_materials(gtpp_ported, gt_by_name, ported_name_set):
    """Splits ported gtpp materials into merges (same-name fold onto an already-ported gregtech/werkstoff
    declaration -- see `gtpp_composition_mismatch`), new declarations (no name collision -- gtpp owns the
    whole thing, textured from the miscutils domain), and false merges (a name collision whose compositions
    disagree -- the stage-11 trap: excluded from both buckets and reported, never guessed at)."""
    merges = []
    new = []
    false_merges = []
    for entry in gtpp_ported:
        name = entry["unlocalizedName"]
        if name in ported_name_set:
            mismatch = gtpp_composition_mismatch(entry, gt_by_name[name])
            if mismatch:
                false_merges.append((entry, mismatch))
            else:
                merges.append(entry)
        else:
            new.append(entry)
    return merges, new, false_merges


def build_gtpp_merge_block(entry, ml_names, gt_by_name, used_fluid_names, fluid_textures):
    name_literal = java_string_literal(ml_names[entry["unlocalizedName"]])
    shape_refs, _deferred = gtpp_shape_lines(entry)
    lines = [f"        MaterialLibAPI.editMaterial(\"gregtech\", {name_literal})"]
    for ref in shape_refs:
        lines.append(f"            .generateShape({ref})")
    gt_entry = gt_by_name.get(entry["unlocalizedName"])
    lines.extend(gtpp_fluid_and_cell_shape_lines(entry, gt_entry, used_fluid_names, fluid_textures))
    lines.extend(gtpp_ore_shape_lines(entry))
    scalar_lines = gtpp_scalar_property_lines(entry, gt_entry, ml_names)
    scalar_lines[-1] += ";"
    lines.extend(f"            {line}" for line in scalar_lines)
    return lines


def build_gtpp_new_block(entry, field, ml_names, included_names, family_shape_members, used_fluid_names,
                          fluid_textures):
    name = entry["unlocalizedName"]
    name_literal = java_string_literal(ml_names[name])
    texture_set_literal = java_string_literal(entry["textureSet"])
    shape_refs, _deferred = gtpp_shape_lines(entry)

    # Same audit build_material_block applies to gregtech-side materials: family membership is chosen from the
    # material's true item-shape set (select_families), not a per-OrePrefix-bit union (FAMILY_BITS/
    # joined_families) that over-joins whenever a shared prefix like `dust` carries more than one family's bit.
    part_prefixes = [p["prefix"] for p in entry["generatedParts"]]
    dumped_items = set(p for p in part_prefixes if p in included_names)
    families, missing, excess = select_families(dumped_items, family_shape_members)

    fluid_cell_lines = gtpp_fluid_and_cell_shape_lines(entry, None, used_fluid_names, fluid_textures)
    families = families + cell_plasma_families(fluid_cell_lines)

    lines = []
    lines.append(
        f"        Materials2Materials.{field} = MaterialLibAPI"
        f".newMaterial(\"gregtech\", {name_literal}, TextureSet.of(\"miscutils\", {texture_set_literal}))")
    if entry["unlocalizedName"] in GTPP_PRECOLORED_ITEM_MATERIALS:
        lines.append("            .setTint(0xFFFFFFFF)")
    else:
        lines.append(f"            .setTint({java_int_literal(pack_argb(entry['rgba']))})")
    if entry["unlocalizedName"] in FLUID_UNTINTED_MATERIALS:
        lines.append("            .setFluidTint(0xFFFFFFFF)")
    if entry["unlocalizedName"] in GTPP_PRECOLORED_BLOCK_MATERIALS:
        lines.append("            .setProperty(StandardProperties.BLOCK_TINT, 0xFFFFFFFF)")
    if entry["unlocalizedName"] in GTPP_CELL_TINT_MATERIALS:
        lines.append(
            "            .setProperty(StandardProperties.CELL_TINT, "
            f"{java_int_literal(pack_argb(entry['rgba']))})")
    lines.append("            .addToFamily(Materials2Families.familyAll)")
    for family in families:
        lines.append(f"            .addToFamily(Materials2Families.{family})")
    for ref in shape_refs:
        lines.append(f"            .generateShape({ref})")
    for prefix_name in missing:
        lines.append(f"            .generateShape(Materials2Shapes.{shape_field_name(prefix_name)})")
    lines.extend(fluid_cell_lines)
    lines.extend(gtpp_ore_shape_lines(entry))

    local_name = entry.get("localName")
    if local_name:
        lines.append(f"            .setProperty(GTMaterialProperties.LOCAL_NAME, {java_string_literal(local_name)})")
    lines.append(f"            .setProperty(GTMaterialProperties.ARGB, {java_int_literal(pack_argb_exact(entry['rgba']))})")
    lines.append(f"            .setProperty(GTMaterialProperties.MELTING_POINT, {celsius_to_kelvin(entry['meltingPointC'])})")
    if entry["usesBlastFurnace"]:
        lines.append("            .setProperty(GTMaterialProperties.BLAST_REQUIRED, true)")
    for line in gtpp_scalar_property_lines(entry, None, ml_names):
        lines.append(f"            {line}")
    lines.append("            .build();")

    if excess:
        lines.append(f"        MaterialLibAPI.editMaterial(\"gregtech\", {name_literal})")
        for prefix_name in excess:
            lines.append(f"            .removeShape(Materials2Shapes.{shape_field_name(prefix_name)})")
        lines[-1] += ";"

    return lines


def gtpp_contested_values(gt_entry, gtpp_entry):
    contested = []

    def contest(label, gt_value, gtpp_value):
        if gt_value != gtpp_value:
            contested.append(f"{label} gt={gt_value!r} gtpp={gtpp_value!r}")

    contest("localName", gt_entry["localName"], gtpp_entry["localName"])
    contest("rgb", gt_entry["rgba"][:3], gtpp_entry["rgba"][:3])
    contest("meltingPointK", gt_entry["meltingPoint"], celsius_to_kelvin(gtpp_entry["meltingPointC"]))
    contest("blastRequired", bool(gt_entry["blastRequired"]), bool(gtpp_entry["usesBlastFurnace"]))
    return contested


def write_gtpp_merge_report(merges, new_entries, false_merges, skipped, gt_by_name):
    lines = []
    lines.append(
        "gtpp merge report -- generated by scripts/mu/gen_materials.py; regenerate, do not edit.")
    lines.append("")
    lines.append(
        f"{len(merges) + len(new_entries)} gtpp materials ported: {len(merges)} merged onto an existing "
        f"gregtech/werkstoff declaration, {len(new_entries)} new gregtech-owned declarations (textured from "
        f"the miscutils domain). {len(skipped)} skipped as unreferenced markers "
        f"({', '.join(sorted(skipped))}). {len(false_merges)} false merges excluded (see below).")
    lines.append("")
    if false_merges:
        lines.append("== FALSE MERGES (composition mismatch -- excluded, not ported; needs coordinator review)")
        for entry, (gtpp_comp, gt_comp) in false_merges:
            lines.append(f"   {entry['unlocalizedName']}: gtpp composition {sorted(gtpp_comp)}")
            lines.append(f"   {entry['unlocalizedName']}: gt composition    {sorted(gt_comp)}")
        lines.append("")
    lines.append("== Merges (gregtech dump wins contested scalars)")
    for entry in sorted(merges, key=lambda e: e["unlocalizedName"]):
        name = entry["unlocalizedName"]
        contested = gtpp_contested_values(gt_by_name[name], entry)
        parts = sorted({p["prefix"] for p in entry["generatedParts"]})
        line = f"   {name}: parts {parts}"
        if contested:
            line += f" | contested (gregtech dump won): {'; '.join(contested)}"
        lines.append(line)
    lines.append("")
    lines.append("== New declarations")
    for entry in sorted(new_entries, key=lambda e: e["unlocalizedName"]):
        parts = sorted({p["prefix"] for p in entry["generatedParts"]})
        lines.append(f"   {entry['unlocalizedName']}: textureSet={entry['textureSet']}, parts {parts}")
    lines.append("")
    GTPP_MERGE_REPORT_PATH.write_text("\n".join(lines) + "\n", encoding="utf-8")


# endregion


def load_legacy_variants_by_material(variants):
    """Ground-truth per-material shape membership: which prefixes `MetaGeneratedItemX32` actually built a
    legacy item for, keyed by legacy material name. Used instead of a material's dumped `generatedPrefixes`
    (capability bits alone), which can drift from construction-time truth -- see `MaterialDataDump`'s
    `dumpGeneratedPrefixes` javadoc for the MHDCSM/plateDouble example this fixes."""
    by_material = {}
    for v in variants:
        by_material.setdefault(v["material"], set()).add(v["prefix"])
    return by_material


def has_fluids(material):
    fluids = material["fluids"]
    return any(fluids.get(state) for state in FLUID_STATES)


def compute_referenced_names(materials):
    """Every material name any other dump entry points at via a MaterialRef-shaped field, so a marker
    material with no shapes/fluids/composition of its own is still ported when something needs to resolve it.
    Self-references (a material listing itself, e.g. in `oreByProducts`) are excluded -- they do not require
    the material to exist for anyone *else's* sake."""
    referenced = set()
    for material in materials:
        name = material["name"]
        for dump_key, _ in REF_PROPERTY_FIELDS:
            value = material.get(dump_key)
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
    """The gregtech dump entries that become MaterialLib declarations. Beyond stage 03's marker skip, a name
    backing a werkstoff (`werkstoff_names` -- its previously-skipped bridge mirror is revived) or referenced
    by werkstoff data (`werkstoff_referenced`) is always ported."""
    referenced = compute_referenced_names(materials) | set(werkstoff_referenced)
    forced = set(werkstoff_names)
    skipped = []
    ported = []
    for material in materials:
        is_marker = not material["generatedPrefixes"] and not has_fluids(material) and not material[
            "composition"]
        if is_marker and material["name"] not in referenced and material["name"] not in forced:
            skipped.append(material["name"])
        else:
            ported.append(material)
    return ported, skipped


def assign_names(ported):
    ml_names = {}
    field_names = {}
    ml_seen = {}
    field_seen = {}
    for material in ported:
        raw = material["name"]
        mn = ml_name(raw)
        fn = field_name(raw)
        if mn in ml_seen:
            raise SystemExit(
                f"gen_materials.py: MaterialLib name collision: {raw!r} and {ml_seen[mn]!r} both sanitize to "
                f"{mn!r}")
        if fn in field_seen:
            raise SystemExit(
                f"gen_materials.py: Java field name collision: {raw!r} and {field_seen[fn]!r} both sanitize "
                f"to {fn!r}")
        ml_seen[mn] = raw
        field_seen[fn] = raw
        ml_names[raw] = mn
        field_names[raw] = fn
    return ml_names, field_names


# The FAMILY_BITS families that actually grant item shapes (Cells/Plasmas/Empties carry an empty shape roster
# in Materials2Families.java -- membership for those is decided separately, see `cell_plasma_families`).
SHAPE_FAMILIES = ["familyDusts", "familyMetals", "familyGems", "familyOres", "familyToolParts", "familyGears"]

# familyDusts' shape roster (dust/dustSmall/dustTiny) is a strict subset of familyMetals'/familyGems' own
# rosters (both list all three dust shapes too), so joining familyDusts alongside either is always redundant --
# exactly the pattern the materials-refactor pass forbids ("little sense to have a material in both
# familyDusts and familyMetals or familyGems"). `select_families`'s own tie-break already drops a fully
# redundant family, but this filter makes the rule a hard invariant rather than an emergent property of
# enumeration order.
DUST_EXCLUSIVE_CONFLICTS = {"familyMetals", "familyGems"}


def compute_dumped_items(material, included_names, legacy_variants_by_material, extra_item_prefixes=()):
    """A material's true final item-shape prefix set (construction-time ground truth, see
    `load_legacy_variants_by_material`), restricted to the item shapes family membership reasons about --
    fluid/cell/block/ore shapes are never family-driven (`fluid_shape_lines`/`container_shape_lines`/
    `block_shape_lines`/`ore_shape_lines` are all unconditional of family membership)."""
    actual_variants = legacy_variants_by_material.get(material["name"], set())
    dumped_items = set(p for p in actual_variants if p in included_names)
    dumped_items |= set(p for p in extra_item_prefixes if p in included_names)
    return dumped_items


# Shapes that define what KIND of material something is, regardless of encoding cost: a material generating
# an ingot is a metal, one generating gem-tier shapes is a gem (the family-selection rule "semantics win
# conflicts" -- see select_families). Plates and sticks deliberately do NOT define anything: both the metal
# and gem families generate them, and non-metals like Wood and Paper carry them too.
METAL_DEFINING_SHAPES = {"ingot"}
GEM_DEFINING_SHAPES = {"gem", "gemChipped", "gemExquisite", "gemFlawed", "gemFlawless"}


def select_families(target, family_shape_members):
    """Chooses the `SHAPE_FAMILIES` subset that best explains a material's true final item-shape set `target`
    (see `compute_dumped_items`), semantics first, encoding noise second:

    - A material generating a `METAL_DEFINING_SHAPES`/`GEM_DEFINING_SHAPES` shape MUST join
      `familyMetals`/`familyGems`, even when a smaller family plus explicit adds would need fewer edit lines
      -- what a material IS wins over encoding thrift.
    - `familyDusts` is exclusively for materials that are not metals/gems (`DUST_EXCLUSIVE_CONFLICTS`); its
      members generate dusts plus at most a minor amount of explicitly-added extra shapes.
    - Within those constraints, the subset minimizes total encoding noise (the `removeShape` count for shapes
      the joined families imply but the material does not generate, plus the explicit `generateShape` count
      for shapes no joined family covers), tie-broken toward explaining more of `target` through membership
      and then toward fewer families.

    This replaces the original stage-03 codegen's per-`OrePrefix`-bit union (`FAMILY_BITS`, formerly
    `joined_families`): that scheme joined every family whose bit is set on ANY of a material's real shapes,
    but several prefixes carry more than one family's bit at the OrePrefix level (`dust` alone carries
    `DUST|METAL|GEM|ORE` -- see `scripts/mu/dumps/oreprefixes.json` -- because the `dust` *prefix* is used by
    all four kinds of material, not because generating a dust makes a material a metal, a gem, AND an ore
    simultaneously). That scheme therefore joined a near-universal family set onto any dust-bearing material
    and relied on `removeShape` to claw back everything the material did not really make -- exactly the
    Magnesia-style complaint (joined to `familyGems`, then `removeShape` for nearly every gem shape) this
    function exists to fix. Returns `(chosen_families, missing, excess)`.
    """
    target = set(target)
    forced = set()
    if target & METAL_DEFINING_SHAPES:
        forced.add("familyMetals")
    if target & GEM_DEFINING_SHAPES:
        forced.add("familyGems")
    candidates = [f for f in SHAPE_FAMILIES if family_shape_members[f]]
    n = len(candidates)
    best_key = None
    best_chosen = ()
    for mask in range(1 << n):
        chosen = tuple(candidates[i] for i in range(n) if mask & (1 << i))
        if not forced <= set(chosen):
            continue
        if "familyDusts" in chosen and any(f in chosen for f in DUST_EXCLUSIVE_CONFLICTS):
            continue
        union = set()
        for family in chosen:
            union.update(family_shape_members[family])
        cost = len(union ^ target)
        coverage = len(union & target)
        key = (cost, -coverage, len(chosen), chosen)
        if best_key is None or key < best_key:
            best_key = key
            best_chosen = chosen
    union = set()
    for family in best_chosen:
        union.update(family_shape_members[family])
    missing = sorted(target - union)
    excess = sorted(union - target)
    return list(best_chosen), missing, excess


def _shape_field_token(line):
    """The bare `shape<X>` field name a `.generateShape(Materials2XShapes.shape<X>)` line ends with."""
    return line.rstrip(";").rstrip(")").rsplit(".", 1)[-1]


CELLS_SHAPE_FIELDS = {
    "shapeCell", "shapeCellMolten", "shapeCellHydroCracked1", "shapeCellHydroCracked2", "shapeCellHydroCracked3",
    "shapeCellSteamCracked1", "shapeCellSteamCracked2", "shapeCellSteamCracked3"
}
PLASMA_SHAPE_FIELDS = {"shapeFluidPlasma", "shapeCellPlasma", "shapeCellPlasmaLight"}


def cell_plasma_families(shape_lines):
    """`familyCells`/`familyPlasmas` membership, decided directly from which of a material's own already-built
    `generateShape` lines (fluid/container/werkstoff-special, plus a merge target's gtpp fold) name a cell or
    plasma shape. Both families carry an empty shape roster (`Materials2Families.java`), so unlike
    `SHAPE_FAMILIES` this is a pure genuine-capability tag with no `removeShape`/`generateShape` cost either
    way; deriving it from the material's real generated shapes (rather than the old per-OrePrefix-bit union)
    is the same audit `select_families` applies to the shape-bearing families. `familyEmpties` (the legacy
    `EMPTY` generation bit) is dropped entirely: its shape roster is and always was empty and no material in
    this port generates a shape of that kind, so no material can genuinely belong to it."""
    tokens = {_shape_field_token(line) for line in shape_lines if ".generateShape(" in line}
    families = []
    if tokens & CELLS_SHAPE_FIELDS:
        families.append("familyCells")
    if tokens & PLASMA_SHAPE_FIELDS:
        families.append("familyPlasmas")
    return families


def gtpp_merged_item_prefixes(entry, included_names):
    """The `Materials2Shapes`-pipeline part prefixes a gtpp same-name fold adds to a merge target -- included
    in the target's family-selection shape set so the family reflects the material's true final form (e.g.
    Iodine's gregtech/werkstoff side is dust+cell only, but its gtpp fold adds ingot/plate/stick/...; the
    material is a metal by its final shapes, and leaving the fold's parts out would park it in `familyDusts`
    with a dozen explicit adds, exactly the semantics the familyDusts-is-dust-only rule forbids)."""
    if entry is None:
        return set()
    return {p["prefix"] for p in entry["generatedParts"] if p["prefix"] in included_names}


def material_ref_literal(raw_name, ml_names):
    return f"new MaterialRef({java_string_literal(ml_names[raw_name])})"


def material_ref_stack_list_literal(stacks, ml_names):
    entries = []
    for stack in stacks:
        if not stack or not stack.get("material"):
            continue
        entries.append(
            f"new MaterialRefStack({material_ref_literal(stack['material'], ml_names)}, {stack['amount']}L)")
    return "List.of(" + ", ".join(entries) + ")"


def material_ref_stack_list_from_names(names, ml_names):
    entries = [f"new MaterialRefStack({material_ref_literal(name, ml_names)}, 1L)" for name in names]
    return "List.of(" + ", ".join(entries) + ")"


def fluid_ref_literal(fluid, fluid_textures):
    if fluid is None:
        return "null"
    texture = fluid_textures.get(fluid["name"])
    texture_literal = java_string_literal(texture) if texture is not None else "null"
    return f"new FluidRef({java_string_literal(fluid['name'])}, {fluid['temperature']}, {texture_literal})"


# gt-materials.json-ported materials that are pure isotopes: the pinned dump's `element` field is null
# because the legacy `Materials.Thorium232`/`Materials.Thorium234` objects never set `mElement`, but the
# computed atomic quantities (gregtech.api.material.MaterialAtomics) need canonical ELEMENT to read the new
# Element.java isotope entries (Th_232/Th_234) rather than fall back to Tc.
GT_ELEMENT_BACKFILL = {"Thorium232": "Th_232", "Thorium234": "Th_234"}


def build_property_lines(material, ml_names, fluid_textures):
    lines = []

    local_name = material.get("localName")
    if local_name:
        lines.append(f".setProperty(GTMaterialProperties.LOCAL_NAME, {java_string_literal(local_name)})")

    true_name = material["name"]
    if ml_name(true_name) != true_name:
        lines.append(f".setProperty(GTMaterialProperties.LEGACY_NAME, {java_string_literal(true_name)})")

    lines.append(f".setProperty(GTMaterialProperties.ARGB, {java_int_literal(pack_argb_exact(material['rgba']))})")
    if material["moltenRgba"] != material["rgba"]:
        lines.append(
            ".setProperty(GTMaterialProperties.MOLTEN_ARGB, "
            f"{java_int_literal(pack_argb_exact(material['moltenRgba']))})")

    if material["meltingPoint"] != 0:
        lines.append(f".setProperty(GTMaterialProperties.MELTING_POINT, {material['meltingPoint']})")
    if material["blastTemp"] != 0:
        lines.append(f".setProperty(GTMaterialProperties.BLAST_TEMP, {material['blastTemp']})")
    if material["blastRequired"]:
        lines.append(".setProperty(GTMaterialProperties.BLAST_REQUIRED, true)")
    if material["gasTemp"] != 0:
        lines.append(f".setProperty(GTMaterialProperties.GAS_TEMP, {material['gasTemp']})")
    if material["fuelPower"] != 0:
        lines.append(f".setProperty(GTMaterialProperties.FUEL_POWER, {material['fuelPower']})")
    if material["fuelType"] != 0:
        lines.append(f".setProperty(GTMaterialProperties.FUEL_TYPE, {material['fuelType']})")
    if material["heatDamage"] != 0.0:
        lines.append(
            f".setProperty(GTMaterialProperties.HEAT_DAMAGE, {java_float_literal(material['heatDamage'])})")
    if material["toolSpeed"] != 1.0:
        lines.append(
            f".setProperty(GTMaterialProperties.TOOL_SPEED, {java_float_literal(material['toolSpeed'])})")
    if material["toolDurability"] != 0:
        lines.append(f".setProperty(GTMaterialProperties.DURABILITY, {material['toolDurability']})")
    if material["toolQuality"] != 0:
        lines.append(f".setProperty(GTMaterialProperties.TOOL_QUALITY, {material['toolQuality']})")
    if material["subId"] != -1:
        lines.append(f".setProperty(GTMaterialProperties.OLD_SUB_ID, {material['subId']})")

    if material["moltenRgba"] != [255, 255, 255, 0]:
        lines.append(
            f".setProperty(GTMaterialProperties.MOLTEN_TINT, {java_int_literal(pack_argb(material['moltenRgba']))})")

    element = material["element"] or GT_ELEMENT_BACKFILL.get(material["name"])
    if element is not None:
        lines.append(f".setProperty(GTMaterialProperties.ELEMENT, {java_string_literal(element)})")

    if material["composition"]:
        lines.append(
            ".setProperty(GTMaterialProperties.COMPOSITION, "
            f"{material_ref_stack_list_literal(material['composition'], ml_names)})")

    for dump_key, property_name in REF_PROPERTY_FIELDS:
        value = material.get(dump_key)
        if value:
            lines.append(
                f".setProperty(GTMaterialProperties.{property_name}, {material_ref_literal(value, ml_names)})")

    if material["oreByProducts"]:
        lines.append(
            ".setProperty(GTMaterialProperties.ORE_BYPRODUCTS, "
            f"{material_ref_stack_list_from_names(material['oreByProducts'], ml_names)})")

    if material["subTags"]:
        flags = ", ".join(
            "GTMaterialFlag." + SUBTAG_FLAG_OVERRIDES.get(tag, tag) for tag in material["subTags"])
        lines.append(f".setProperty(GTMaterialProperties.FLAGS, EnumSet.of({flags}))")

    if material["oreMultiplier"] != 1:
        lines.append(f".setProperty(GTMaterialProperties.ORE_MULTIPLIER, {material['oreMultiplier']})")
    if material["byProductMultiplier"] != 1:
        lines.append(
            f".setProperty(GTMaterialProperties.BYPRODUCT_MULTIPLIER, {material['byProductMultiplier']})")
    if material["smeltingMultiplier"] != 1:
        lines.append(
            f".setProperty(GTMaterialProperties.SMELTING_MULTIPLIER, {material['smeltingMultiplier']})")

    if not material["unifiable"]:
        lines.append(".setProperty(GTMaterialProperties.UNIFIABLE, false)")
    if material["densityMultiplier"] != 1:
        lines.append(f".setProperty(GTMaterialProperties.DENSITY_MULTIPLIER, {material['densityMultiplier']})")
    if material["densityDivider"] != 1:
        lines.append(f".setProperty(GTMaterialProperties.DENSITY_DIVIDER, {material['densityDivider']})")
    if material["steamMultiplier"] != 1.0:
        lines.append(
            f".setProperty(GTMaterialProperties.STEAM_MULTIPLIER, {java_float_literal(material['steamMultiplier'])})")
    if material["gasMultiplier"] != 1.0:
        lines.append(
            f".setProperty(GTMaterialProperties.GAS_MULTIPLIER, {java_float_literal(material['gasMultiplier'])})")
    if material["plasmaMultiplier"] != 1.0:
        lines.append(
            ".setProperty(GTMaterialProperties.PLASMA_MULTIPLIER, "
            f"{java_float_literal(material['plasmaMultiplier'])})")

    if material["generationFlags"]:
        gen_flags = ", ".join("GTMaterialGenerationFlag." + f for f in material["generationFlags"])
        lines.append(f".setProperty(GTMaterialProperties.GENERATION_FLAGS, EnumSet.of({gen_flags}))")

    has_fluid, has_gas = CORRESPONDING_FLAG_OVERRIDES.get(
        material["name"], (material["hasCorrespondingFluid"], material["hasCorrespondingGas"]))
    if has_fluid:
        lines.append(".setProperty(GTMaterialProperties.HAS_CORRESPONDING_FLUID, true)")
    if has_gas:
        lines.append(".setProperty(GTMaterialProperties.HAS_CORRESPONDING_GAS, true)")
    if material["hasElectrolyzerRecipe"]:
        lines.append(".setProperty(GTMaterialProperties.HAS_ELECTROLYZER_RECIPE, true)")
    if material["hasCentrifugeRecipe"]:
        lines.append(".setProperty(GTMaterialProperties.HAS_CENTRIFUGE_RECIPE, true)")
    if material["canBeCracked"]:
        lines.append(".setProperty(GTMaterialProperties.CAN_BE_CRACKED, true)")
    if material["hasGlowingOre"]:
        lines.append(".setProperty(GTMaterialProperties.HAS_GLOWING_ORE, true)")
    if material["processingMaterialTierEU"] != 0:
        lines.append(
            ".setProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU, "
            f"{material['processingMaterialTierEU']})")

    if material["addedPrefixes"]:
        added = ", ".join(java_string_literal(p) for p in material["addedPrefixes"])
        lines.append(f".setProperty(GTMaterialProperties.ADDED_PREFIXES, List.of({added}))")
    if material["removedPrefixes"]:
        removed = ", ".join(java_string_literal(p) for p in material["removedPrefixes"])
        lines.append(f".setProperty(GTMaterialProperties.REMOVED_PREFIXES, List.of({removed}))")

    if material.get("color"):
        lines.append(f".setProperty(GTMaterialProperties.DYE, {java_string_literal(material['color'])})")
    if not material["autoBlast"]:
        lines.append(".setProperty(GTMaterialProperties.AUTO_BLAST_FURNACE_RECIPES, false)")
    if not material["autoVacuum"]:
        lines.append(".setProperty(GTMaterialProperties.AUTO_VACUUM_FREEZER_RECIPES, false)")
    if not material["autoRecycle"]:
        lines.append(".setProperty(GTMaterialProperties.AUTO_RECYCLE_RECIPES, false)")

    tool_enchant = material["enchants"].get("tool")
    if tool_enchant:
        lines.append(
            f".setProperty(GTMaterialProperties.TOOL_ENCHANTMENT, {java_string_literal(tool_enchant['name'])})")
        lines.append(f".setProperty(GTMaterialProperties.TOOL_ENCHANTMENT_LEVEL, {tool_enchant['level']})")
    armor_enchant = material["enchants"].get("armor")
    if armor_enchant:
        lines.append(
            f".setProperty(GTMaterialProperties.ARMOR_ENCHANTMENT, {java_string_literal(armor_enchant['name'])})")
        lines.append(f".setProperty(GTMaterialProperties.ARMOR_ENCHANTMENT_LEVEL, {armor_enchant['level']})")

    if material["aspects"]:
        aspect_entries = ", ".join(
            f"new AspectRefStack({java_string_literal(a['name'])}, {a['amount']})" for a in material["aspects"])
        lines.append(f".setProperty(GTMaterialProperties.ASPECTS, List.of({aspect_entries}))")

    fluids = material["fluids"]
    if has_fluids(material) and material["name"] not in FLUID_CUTOVER_EXCLUDED:
        fluid_args = ", ".join(fluid_ref_literal(fluids[state], fluid_textures) for state in FLUID_STATES)
        lines.append(f".setProperty(GTMaterialProperties.LEGACY_FLUIDS, new FluidNames({fluid_args}))")

    cracked = material.get("crackedFluids") or {}
    hydro_cracked = cracked.get("hydroCracked")
    if hydro_cracked:
        entries = ", ".join(fluid_ref_literal(f, fluid_textures) for f in hydro_cracked)
        lines.append(f".setProperty(GTMaterialProperties.CRACKED_HYDRO_FLUIDS, List.of({entries}))")
    steam_cracked = cracked.get("steamCracked")
    if steam_cracked:
        entries = ", ".join(fluid_ref_literal(f, fluid_textures) for f in steam_cracked)
        lines.append(f".setProperty(GTMaterialProperties.CRACKED_STEAM_FLUIDS, List.of({entries}))")

    return lines


# Materials whose fluid is hand-wired in LoaderGTBlockFluid rather than produced by the generic per-material
# GTFluidFactory pipeline this stage cuts over. Water/Ice: mFluid is vanilla "water" for both, mSolid is a
# single "ice" fluid shared by both (Materials.Water.mSolid = Materials.Ice.mSolid), and mGas is Water's steam
# shared the same way -- claiming a MaterialLib fluid shape for both would double-register the identical name,
# which MaterialLib rejects (fluid names must be unique per resolve, reused or not). Lava: vanilla "lava".
# ConstructionFoam/UUMatter: an IC2 item's fluid looked up via GTUtility#getFluidForFilledItem, owned by IC2's
# own registration. All five are left as legacy exactly as before; claiming their shapes would only produce a
# harmless "already registered elsewhere" MaterialLib warning, but stage 06 targets a clean resolve.
FLUID_CUTOVER_EXCLUDED = {"Water", "Ice", "Lava", "ConstructionFoam", "UUMatter"}

# dump fluid-slot key -> Materials2FluidShapes field.
FLUID_SHAPE_FIELDS = {
    "solid": "shapeFluidSolid",
    "fluid": "shapeFluidLiquid",
    "gas": "shapeFluidGas",
    "plasma": "shapeFluidPlasma",
    "molten": "shapeFluidMolten",
}
CRACKED_FLUID_SHAPE_FIELDS = {
    "hydroCracked": ["shapeFluidHydroCracked1", "shapeFluidHydroCracked2", "shapeFluidHydroCracked3"],
    "steamCracked": ["shapeFluidSteamCracked1", "shapeFluidSteamCracked2", "shapeFluidSteamCracked3"],
}
# legacy-variants.json prefix -> (Materials2CellShapes field, dump key whose presence the shape's fluid
# requires -- MaterialLib's container contract needs a material to generate at least one of the container's
# fluid shapes too, which not every material with a real legacy item slot satisfies (e.g. a metal's plain
# `cell` slot exists from its CELL capability flag alone, with no base liquid/gas ever put in it; its molten
# and plasma go into cellMolten/cellPlasma instead). Missing the required fluid leaves the legacy item
# uncut -- see MU#isCutOver.
CELL_SHAPE_FIELDS = {
    "cellHydroCracked1": ("shapeCellHydroCracked1", "hydroCracked"),
    "cellHydroCracked2": ("shapeCellHydroCracked2", "hydroCracked"),
    "cellHydroCracked3": ("shapeCellHydroCracked3", "hydroCracked"),
    "cellSteamCracked1": ("shapeCellSteamCracked1", "steamCracked"),
    "cellSteamCracked2": ("shapeCellSteamCracked2", "steamCracked"),
    "cellSteamCracked3": ("shapeCellSteamCracked3", "steamCracked"),
}

LEGACY_BLOCKS_DUMP_PATH = SCRIPT_DIR / "dumps" / "legacy-blocks.json"

# Materials whose legacy storage block is hard-referenced by identity (`GregTechAPI.sBlockMetalN == ...`) from
# multiblock casing matchers, a machine-block-update listener, or a client-side piston-tier icon fallback,
# rather than through GTOreDictUnificator -- see stage-07's structural-reference sweep. Cutting these over would
# leave the referencing code comparing against a Block no consumer still points at (the legacy BlockMetal
# instance stays registered and playable for every material, cut over or not -- see gen_materials.py's
# `block_shape_lines` javadoc -- but only the canonical oredict association moves for cut-over materials, and
# these call sites bypass the oredict entirely). Steel/Osmiridium/Neutronium/NaquadahAlloy/WhiteDwarfMatter/
# BlackDwarfMatter/MagnetohydrodynamicallyConstrainedStarMatter: MTESteamForgeHammer, MTESteamCompressor,
# MTEExoFoundry, MTESpinmatron casing tiers. SpaceTime/TranscendentMetal/Universium/Neutronium:
# MTEElectricImplosionCompressor(Legacy) piston tiers, bartworks ItemRegistry's registerMachineBlock
# machine-block-update listener, EICPistonVisualizer's client-side icon fallback. Copper/Zinc:
# MTEIndustrialChemicalBath casing. Osmiridium alone: MTEMicrowaveEnergyTransmitter's teleport-unlock world
# scan.
BLOCK_CUTOVER_EXCLUDED = {
    "Steel", "Osmiridium", "Neutronium", "NaquadahAlloy", "WhiteDwarfMatter", "BlackDwarfMatter",
    "MagnetohydrodynamicallyConstrainedStarMatter", "SpaceTime", "TranscendentMetal", "Universium", "Copper",
    "Zinc"
}

# gtPlusPlus reconstructed materials whose legacy storage block has its own per-material animated icon
# (`gtPlusPlus.core.handler.events.AnimatedBlockTextureHandler`, forcing off-screen icon-cycle sync against
# Angelica's on-screen-only default) with no MaterialLib equivalent yet -- mirrors
# `gtPlusPlus.core.material.MaterialReconstruction#BLOCK_CUTOVER_EXCLUDED`, which keeps the legacy block
# canonical for these names. Unlike `BLOCK_CUTOVER_EXCLUDED` above (GT materials with structural identity
# references), this generator previously had no knowledge of the gtPlusPlus set at all, so it kept emitting
# `shapeBlock` for these names regardless -- MaterialLib generated its own untextured, statically-tinted
# storage block alongside the legacy one nobody ever intended a player to see. Keep the two Java-side sets in
# sync if either changes.
GTPP_ANIMATED_BLOCK_EXCLUDED = {"AstralTitanium", "CelestialTungsten", "ChromaticGlass", "Hypogen"}


def load_legacy_block_materials():
    """Every material name with a real legacy storage-block slot (union of the 13 `BlockMetal` instances'
    `Materials[]` arrays), from `legacy-blocks.json` -- see `MaterialDataDump#dumpLegacyBlocks`."""
    if not LEGACY_BLOCKS_DUMP_PATH.exists():
        return set()
    with open(LEGACY_BLOCKS_DUMP_PATH, encoding="utf-8") as f:
        entries = json.load(f)
    return {e["material"] for e in entries}


def block_shape_lines(material, legacy_block_materials):
    """`generateShape(...)` line for `Materials2BlockShapes`, driven by `legacy-blocks.json` ground truth (see
    `load_legacy_block_materials`) rather than the family/delta machinery every other item shape uses: `block`'s
    dumped `generationBits` is `0` (see `gen_shapes.py`'s `is_block_kind`), so it never appears in any
    material's `generatedPrefixes` and cannot participate in that pipeline at all. `BLOCK_CUTOVER_EXCLUDED`
    materials keep their legacy storage block canonical despite having a real legacy slot -- see its
    docstring."""
    if material["name"] not in legacy_block_materials or material["name"] in BLOCK_CUTOVER_EXCLUDED:
        return []
    return ["            .generateShape(Materials2BlockShapes.shapeBlock)"]


def fluid_shape_lines(material, used_fluid_names):
    """`generateShape(...)` lines for `Materials2FluidShapes`, driven directly by which legacy fluid slots the
    dump captured (mSolid/mFluid/mGas/mPlasma/mStandardMoltenFluid, hydro/steam-cracked arrays) -- these are
    live Forge fluid object references, not capability bits, so unlike item shapes there is no drift to guard
    against with a legacy-variants intersection.

    A material only claims (generates) a shape for a name not already claimed this run: MaterialLib requires
    every fluid name unique across the whole resolve, but a large fraction of materials legitimately reuse one
    Forge fluid across two slots -- `GTFluidFactory` names both a material's `fluid` and `gas` fluid
    `aMaterial.mName.toLowerCase()`, so whichever `addAutoGeneratedCorresponding*` ran first (fluid before gas,
    see `GTProxy`) created it and the other silently reused it, leaving `mFluid`/`mGas` pointing at the exact
    same object. Processing states in `FLUID_SHAPE_FIELDS`/`CRACKED_FLUID_SHAPE_FIELDS` order and skipping an
    already-claimed name reproduces that first-claim-wins order; the skipped state's material still resolves
    correctly via `LegacyMaterials#wireFluids`, which looks the fluid up by name rather than requiring the
    material to be this shape's own generator."""
    if material["name"] in FLUID_CUTOVER_EXCLUDED:
        return []
    lines = []
    fluids = material["fluids"]
    for state, field in FLUID_SHAPE_FIELDS.items():
        ref = fluids.get(state)
        if ref and ref["name"] not in used_fluid_names:
            used_fluid_names.add(ref["name"])
            lines.append(f"            .generateShape(Materials2FluidShapes.{field})")
    cracked = material.get("crackedFluids") or {}
    for key, shape_fields in CRACKED_FLUID_SHAPE_FIELDS.items():
        refs = cracked.get(key)
        if not refs:
            continue
        for ref, field in zip(refs, shape_fields):
            if ref and ref["name"] not in used_fluid_names:
                used_fluid_names.add(ref["name"])
                lines.append(f"            .generateShape(Materials2FluidShapes.{field})")
    return lines


def container_shape_lines(material, legacy_variants_by_material):
    """`generateShape(...)` lines for `Materials2CellShapes`, driven by legacy-variants.json (the same ground
    truth item shapes use): `cellMolten`/cracked-cell prefixes live on `MetaGeneratedItem99`, which is not a
    `MetaGeneratedItemX32` subclass and so needed its own legacy-variants capture (see that class). `cellPlasma`
    picks one of two candidate shapes per material depending on whether it also has a molten fluid (see
    `Materials2CellShapes`). A material's legacy variant only becomes a MaterialLib shape when the fluid it
    requires is also present (see `CELL_SHAPE_FIELDS`); materials that fall short keep their legacy item."""
    if material["name"] in FLUID_CUTOVER_EXCLUDED:
        return []
    variants = legacy_variants_by_material.get(material["name"], set())
    fluids = material["fluids"]
    cracked = material.get("crackedFluids") or {}
    lines = []
    if "cell" in variants and (fluids.get("fluid") or fluids.get("gas")):
        lines.append("            .generateShape(Materials2CellShapes.shapeCell)")
    if "cellMolten" in variants and fluids.get("molten"):
        lines.append("            .generateShape(Materials2CellShapes.shapeCellMolten)")
    if "cellPlasma" in variants and fluids.get("plasma"):
        field = "shapeCellPlasma" if fluids.get("molten") else "shapeCellPlasmaLight"
        lines.append(f"            .generateShape(Materials2CellShapes.{field})")
    for prefix_name, (field, cracked_key) in CELL_SHAPE_FIELDS.items():
        if prefix_name in variants and cracked.get(cracked_key):
            lines.append(f"            .generateShape(Materials2CellShapes.{field})")
    return lines


def ore_shape_lines(material):
    """`generateShape(...)` lines for `Materials2OreShapes`, driven directly by the dumped `generatedPrefixes`
    ground truth: `ore`/`oreSmall` are block-kind (see `gen_shapes.py`'s `is_block_kind`), so unlike every other
    prefix carrying the `ORE` generation bit they never appear in `Materials2Shapes`'s per-item pipeline
    (`is_included_shape` excludes block-kind prefixes outright) and so need their own hand-written membership
    line here, the same way `block_shape_lines`/`container_shape_lines` do for their own block-kind/container
    shapes."""
    prefixes = material["generatedPrefixes"]
    lines = []
    if "ore" in prefixes:
        lines.append("            .generateShape(Materials2OreShapes.shapeOre)")
    if "oreSmall" in prefixes:
        lines.append("            .generateShape(Materials2OreShapes.shapeOreSmall)")
    return lines


## Legacy `hasItemType()` (`Werkstoff.java`) grants the `blockCasing`/`blockCasingAdvanced` prefixes to any
## werkstoff whose `toGenerate` bitmask includes metalworking (`SIMPLE_METALWORKING`/`CRAFTING_METALWORKING`/
## `DOUBLE_DENSE_PLATES`) -- a much broader set than the werkstoffe that legacy bartworks actually shipped a
## bolted/rebolted casing recipe/use for. The dumped `generatedPrefixes` therefore over-reports casing
## membership; this is the curated legacy set (cross-checked against `dumps/werkstoff.json`'s casing-flagged
## werkstoffe), keyed the same way `group_werkstoffs` keys `werkstoff_by_name` (a non-proxy werkstoff's
## `varName`, or a proxy's `bridgeMaterial`).
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


def werkstoff_special_shape_lines(material, werkstoff_prefixes):
    """`generateShape(...)` lines for the werkstoff prefixes outside `Materials2Shapes`' per-item pipeline:
    cells (fluid-in-container, the fluid itself is already MaterialLib-owned via the bridge mirror's
    `LEGACY_FLUIDS` capture), storage blocks, ores (bartworks small ores exist for every ore werkstoff --
    `BWOreAdapter` registers big+small blocks per stone type -- so `ore` implies `oreSmall` here), and the
    bartworks casings (see `BARTWORKS_CASING_MATERIALS`). `WERKSTOFF_LEGACY_ONLY_PREFIXES` deliberately produce
    nothing."""
    name = material["name"]
    fluids = material["fluids"]
    lines = []
    if "cell" in werkstoff_prefixes:
        if not (fluids.get("fluid") or fluids.get("gas")):
            raise SystemExit(f"gen_materials.py: werkstoff {name!r} generates cell but has no dumped fluid")
        lines.append("            .generateShape(Materials2CellShapes.shapeCell)")
    if "cellMolten" in werkstoff_prefixes:
        if not fluids.get("molten"):
            raise SystemExit(
                f"gen_materials.py: werkstoff {name!r} generates cellMolten but has no dumped molten fluid")
        lines.append("            .generateShape(Materials2CellShapes.shapeCellMolten)")
    if "block" in werkstoff_prefixes:
        lines.append("            .generateShape(Materials2BlockShapes.shapeBlock)")
    if "ore" in werkstoff_prefixes:
        lines.append("            .generateShape(Materials2OreShapes.shapeOre)")
        lines.append("            .generateShape(Materials2OreShapes.shapeOreSmall)")
    if "blockCasing" in werkstoff_prefixes and name in BARTWORKS_CASING_MATERIALS:
        lines.append("            .generateShape(Materials2BlockShapes.shapeBlockCasing)")
    if "blockCasingAdvanced" in werkstoff_prefixes and name in BARTWORKS_CASING_MATERIALS:
        lines.append("            .generateShape(Materials2BlockShapes.shapeBlockCasingAdvanced)")
    return lines


def build_gt_shape_lines(material, legacy_variants_by_material, used_fluid_names, fluid_textures,
                          legacy_block_materials, werkstoff_prefixes):
    """A material's fluid/container/block/ore/werkstoff-special `generateShape` lines, deduplicated. Split out
    of the block assembly because `fluid_shape_lines`'s first-claim-wins name dedup (`used_fluid_names`) is
    order-sensitive across the whole resolve: every gt/werkstoff material must claim before any gtpp fold does
    (the original multi-file emission order), while block assembly needs the gtpp fold's lines in hand -- so
    claiming runs as its own ordered pass first."""
    shape_lines = []
    shape_lines.extend(fluid_shape_lines(material, used_fluid_names))
    shape_lines.extend(container_shape_lines(material, legacy_variants_by_material))
    shape_lines.extend(block_shape_lines(material, legacy_block_materials))
    shape_lines.extend(ore_shape_lines(material))
    shape_lines.extend(werkstoff_special_shape_lines(material, werkstoff_prefixes))
    seen = set()
    deduped = []
    for line in shape_lines:
        if line not in seen:
            seen.add(line)
            deduped.append(line)
    return deduped


## Legacy fluids registered through a dedicated, hand-written `GTFluidFactory.builder(...)` chain (see
## `LoaderGTBlockFluid.java`) rather than one of `GTProxy`'s generic `addAutoGenerated*` methods never call
## `.withColorRGBA(...)`, so they render untinted (their dedicated art already encodes the right color) even
## though the material's own `rgba` (used to color every *other* shape it generates) is not white. The generic
## auto-methods, by contrast, always tint -- including materials with a custom icon set sharing one texture
## across a whole alloy family (e.g. the `molten.darksteel`-icon family) -- so this must be curated per material,
## not inferred from "has a captured, non-generic fluid texture". Keyed by `material["name"]`.
FLUID_UNTINTED_MATERIALS = {
    "spatialFluid",
    "temporalFluid",
    "ExcitedDTCC",
    "ExcitedDTPC",
    "ExcitedDTRC",
    "ExcitedDTEC",
    "ExcitedDTSC",
    "RawStarMatter",
    "DimensionallyTranscendentResidue",
    "AstralTitanium",
    "CelestialTungsten",
    "ChromaticGlass",
    "Hypogen",
    "Rhugnor",
}

## Dragonblood is a hand-maintained exception to the white-fluid rule above, not modeled by this generator.
## Its molten/plasma art (`fluid.molten.hypogen`/`fluid.plasma.hypogen`) is literally Hypogen's shared placeholder
## texture, not dedicated Dragonblood art -- staying white would make the two fluids indistinguishable in world/
## tank/NEI even though their cell fill (`CELL_TINT`, matching this same red) already differs. Dragonblood's
## `Materials2Materials` declaration sets `.setFluidTint(0xFFDC2814)` explicitly (the same red as its own `setTint`)
## and carries no `CELL_TINT` property, since a cell fill with no `CELL_TINT` set falls back to `FLUID_TINT`
## (see `ShapeFluidInContainer#getColorFromItemStack`), which is now the identical red -- redundant, not different.
## Rhugnor keeps `FLUID_UNTINTED_MATERIALS` membership: its molten/plasma art is dedicated, pre-colored purple
## (already close to its own tint), not a shared placeholder, so tinting it would double-tint rather than
## differentiate it from anything.

## gtPlusPlus applies the same untinted-fluid principle through a hard-coded legacy rule instead of a builder
## chain: fluids of any `is_custom` texture-set material register WHITE (`FluidUtils.addGTFluid`/`addGTPlasma`
## force `{255,255,255,255}` when `aMaterial.getTextureSet().is_custom`) -- hence the six CUSTOM-set gtpp
## materials' `FLUID_UNTINTED_MATERIALS` membership above. Items follow a separate legacy rule: untinted when
## the material's dumped rgba alpha exceeds 1 (`BaseItemComponent#getColorFromItemStack`: "Animated materials
## ship baked animated textures; render them untinted"), which among gtpp-only materials selects exactly these
## three -- Hypogen's tint is already white, and Dragonblood/Rhugnor (alpha 0) kept their red/purple item tint
## over the shared pre-colored art in legacy. Emitted as a white `setTint` replacing the packed dumped rgba.
GTPP_PRECOLORED_ITEM_MATERIALS = {"AstralTitanium", "CelestialTungsten", "ChromaticGlass"}

## The CUSTOM-set gtpp material whose storage block IS cut over to MaterialLib (unlike
## `GTPP_ANIMATED_BLOCK_EXCLUDED`, whose legacy blocks stay canonical) and whose block art is pre-colored
## (legacy `materialicons/CUSTOM/<set>/block5.png`, hand-copied into the miscutils blocks tree as `block.png`):
## the pre-baked purple would double-tint under the material tint, so the MaterialLib block gets the
## plain-block `StandardProperties.BLOCK_TINT` white override instead. Dragonblood's block is also
## ML-cut-over but has no art of its own -- its texture set shares hypogen's orange `block.png`, which takes
## the material's red `setTint` like any ordinary block (no `BLOCK_TINT` override).
GTPP_PRECOLORED_BLOCK_MATERIALS = {"Rhugnor"}

## Rhugnor's cell container's fill layer is coupled to `FLUID_UNTINTED_MATERIALS` by default, which would render
## its molten/plasma cell fill white -- but legacy tinted GT-pp's `BaseItemComponent` items with the material's
## own rgba whenever the dumped alpha is 0 (Rhugnor's alpha, unlike the `GTPP_PRECOLORED_ITEM_MATERIALS` trio),
## and a filled cell is an item, not a fluid; `StandardProperties.CELL_TINT` reproduces that legacy cell-fill
## color, set to the same packed rgba as the material's own (untouched) `setTint`. Dragonblood carries no
## `CELL_TINT` -- see the `FLUID_UNTINTED_MATERIALS` comment above; its cell fill now reaches the same red through
## the `FLUID_TINT` fallback instead.
GTPP_CELL_TINT_MATERIALS = {"Rhugnor"}


def build_material_block(
        material, ml_names, field_names, included_names, family_shape_members,
        legacy_variants_by_material, fluid_textures, werkstoff_info, display_to_var,
        shape_lines, gtpp_entry, gtpp_lines):
    field = field_names[material["name"]]
    name_literal = java_string_literal(ml_names[material["name"]])
    icon_set_literal = java_string_literal(material["iconSet"])
    werkstoff_prefixes = werkstoff_info["prefixes"] if werkstoff_info else []

    # Family selection sees the material's full final item-shape set, gtpp fold included; the fold's own
    # explicit generateShape lines then cover whatever parts it contributes, so `missing` only needs the
    # gt/werkstoff-side leftovers (and `excess` can never collide with a fold-added shape).
    gtpp_parts = gtpp_merged_item_prefixes(gtpp_entry, included_names)
    dumped_items = compute_dumped_items(material, included_names, legacy_variants_by_material, werkstoff_prefixes)
    dumped_items |= gtpp_parts
    families, missing, excess = select_families(dumped_items, family_shape_members)
    families = families + cell_plasma_families(shape_lines + (gtpp_lines or []))
    missing = [p for p in missing if p not in gtpp_parts]

    lines = []
    lines.append(
        f"        Materials2Materials.{field} = MaterialLibAPI"
        f".newMaterial(\"gregtech\", {name_literal}, TextureSet.of(\"gregtech\", {icon_set_literal}))")
    lines.append(f"            .setTint({java_int_literal(pack_argb(material['rgba']))})")
    if material["name"] in FLUID_UNTINTED_MATERIALS:
        lines.append("            .setFluidTint(0xFFFFFFFF)")
    lines.append("            .addToFamily(Materials2Families.familyAll)")
    for family in families:
        lines.append(f"            .addToFamily(Materials2Families.{family})")

    for prefix_name in missing:
        lines.append(f"            .generateShape(Materials2Shapes.{shape_field_name(prefix_name)})")
    lines.extend(shape_lines)

    for property_line in build_property_lines(material, ml_names, fluid_textures):
        lines.append("            " + property_line)
    if werkstoff_info:
        canonical_melting_point = material["meltingPoint"] if material["meltingPoint"] != 0 else None
        for werkstoff_line in werkstoff_property_lines(
                werkstoff_info, ml_names, display_to_var, canonical_melting_point):
            lines.append("            " + werkstoff_line)

    lines.append("            .build();")

    if excess:
        lines.append(f"        MaterialLibAPI.editMaterial(\"gregtech\", {name_literal})")
        for prefix_name in excess:
            lines.append(f"            .removeShape(Materials2Shapes.{shape_field_name(prefix_name)})")
        lines[-1] += ";"

    return lines


def chunk(items, size):
    for i in range(0, len(items), size):
        yield items[i:i + size]


# One material's complete builder-chain statements comfortably fits several hundred materials' worth under the
# JVM's 64KB bytecode-per-method cap on its own, but 1387 of them together do not -- init() is split into
# initPartN() methods of this many materials each (a material's own block, including any gtpp-fold edit
# immediately after it, always stays within one method: chunking operates on whole per-material blocks, never
# splits one). Tuned empirically against `./gradlew build`'s "code too large" error; lower if that recurs.
MATERIALS_PER_INIT_CHUNK = 45


def write_unified_materials_file(field_lines, material_blocks):
    """Emits the single hand-maintained materials file (see this script's module docstring): one [Material]
    field per ported material (gregtech/werkstoff and gtpp-only alike), then `init()` dispatching to
    `initPartN()` methods (see `MATERIALS_PER_INIT_CHUNK`), each holding a run of complete per-material
    declarations. A material's full configuration -- its main `newMaterial` chain, the `removeShape` edit for
    any family excess, and (for a gtpp same-name fold) the gtpp `editMaterial` chain -- is one contiguous
    statement run wherever it falls, never split across files or interrupted by another material's edit the
    way the retired `Materials2DataN`/`Materials2GtppDataN` chain required."""
    chunks = list(chunk(material_blocks, MATERIALS_PER_INIT_CHUNK))

    lines = []
    lines.append("package gregtech.api.enums.materials2;")
    lines.append("")
    lines.append("import java.util.EnumSet;")
    lines.append("import java.util.List;")
    lines.append("")
    lines.append("import com.ruling_0.materiallib.api.Material;")
    lines.append("import com.ruling_0.materiallib.api.MaterialLibAPI;")
    lines.append("import com.ruling_0.materiallib.api.TextureSet;")
    lines.append("")
    lines.append("import gregtech.api.material.AspectRefStack;")
    lines.append("import gregtech.api.material.FluidNames;")
    lines.append("import gregtech.api.material.FluidRef;")
    lines.append("import gregtech.api.material.GTMaterialFlag;")
    lines.append("import gregtech.api.material.GTMaterialGenerationFlag;")
    lines.append("import gregtech.api.material.GTMaterialProperties;")
    lines.append("import gregtech.api.material.GTWerkstoffFlag;")
    lines.append("import gregtech.api.material.MaterialRef;")
    lines.append("import gregtech.api.material.MaterialRefStack;")
    lines.append("")
    lines.append(MATERIALS_FILE_HEADER)
    lines.append(
        "/// The materials maintenance surface: one [Material] field per ported material (name matches the "
        "legacy `Materials`")
    lines.append(
        "/// field where the dump's name is already a valid Java identifier, else a sanitized variant -- see")
    lines.append(
        "/// `scripts/mu/gen_materials.py`), then every material's complete final declaration")
    lines.append(
        "/// (tint, texture set, family membership, shape adds/removes, properties, fluids) as one "
        "contiguous statement run in")
    lines.append(
        "/// `initPartN()` (split only to stay under the JVM's 64KB bytecode-per-method limit -- see "
        "`MATERIALS_PER_INIT_CHUNK`).")
    lines.append("public class Materials2Materials {")
    lines.append("")
    lines.append("    // spotless:off")
    lines.extend(field_lines)
    lines.append("    // spotless:on")
    lines.append("")
    lines.append("    public static void init() {")
    for i in range(len(chunks)):
        lines.append(f"        initPart{i + 1}();")
    lines.append("    }")
    for i, batch in enumerate(chunks):
        lines.append("")
        lines.append(f"    private static void initPart{i + 1}() {{")
        lines.append("        // spotless:off")
        for block in batch:
            lines.extend(block)
        lines.append("        // spotless:on")
        lines.append("    }")
    lines.append("")
    lines.append("    private Materials2Materials() {}")
    lines.append("}")
    lines.append("")
    MATERIALS_OUTPUT.write_text("\n".join(lines), encoding="utf-8")


# region legacy bridge


def write_legacy_bridge_file(ported_names, field_names, materials_by_name):
    """`MaterialsLegacyBridge.java` is hand-maintained (loop-driven over the resolved MaterialLib registry --
    see its class javadoc) since the one-time `MaterialsInit` port this function used to regenerate it from is
    long gone; the generator no longer writes it."""
    return None, None


# endregion


def werkstoff_contested_values(gt, entry):
    """The scalar values the gregtech dump won over the pool declaration -- listed per fold in the merge
    report so a merge of two genuinely different substances (rather than the bridge mirror of the same one)
    is reviewable rather than silent."""
    contested = []

    def contest(label, gt_value, w_value):
        if gt_value != w_value:
            contested.append(f"{label} gt={gt_value!r} werkstoff={w_value!r}")

    contest("localName", gt["localName"], entry["name"])
    contest("rgb", gt["rgba"][:3], entry["rgb"])
    contest("textureSet", gt["iconSet"], entry["texSet"])
    contest("meltingPoint", gt["meltingPoint"], entry["meltingPoint"])
    contest("blastRequired", bool(gt["blastRequired"]), bool(entry["blastFurnace"]))
    if entry["durability"]:
        contest("toolDurability", gt["toolDurability"], entry["durability"])
    if entry["speed"]:
        contest("toolSpeed", gt["toolSpeed"], entry["speed"])
    if entry["quality"]:
        contest("toolQuality", gt["toolQuality"], entry["quality"])
    return contested


def write_merge_report(werkstoff_by_name, gt_by_name, base_ported_names, ported_names):
    lines = []
    lines.append("Werkstoff merge report -- generated by scripts/mu/gen_materials.py; regenerate, do not edit.")
    lines.append("")
    entry_count = sum(len(info["entries"]) for info in werkstoff_by_name.values())
    named = [n for n, info in werkstoff_by_name.items() if info["entries"]]
    proxy_only = [n for n, info in werkstoff_by_name.items() if not info["entries"]]
    merged = sorted(n for n in named if n in base_ported_names)
    revived = sorted(n for n in named if n not in base_ported_names)
    newly_referenced = sorted(ported_names - base_ported_names - set(named))
    lines.append(
        f"{entry_count} pool entries folded onto {len(named)} gregtech declarations "
        f"({len(merged)} merged with stage-03 ported materials, {len(revived)} revived previously-skipped "
        f"bridge mirrors); proxy shape folds onto {len(proxy_only)} gregtech materials "
        f"({', '.join(sorted(proxy_only))}); {len(newly_referenced)} markers newly ported because werkstoff "
        f"data references them ({', '.join(newly_referenced)}).")
    lines.append("")
    for name in sorted(named):
        info = werkstoff_by_name[name]
        entries = info["entries"]
        gt = gt_by_name[name]
        pools = sorted({e["pool"] for e in entries})
        ids = ", ".join(str(e["id"]) for e in entries)
        status = "merged-with-ported" if name in base_ported_names else "revived-mirror"
        lines.append(f"== {name} (pools: {', '.join(pools)}; ids: {ids}; {status})")
        lines.append(f"   shapes: {', '.join(info['prefixes'])}")
        contested = werkstoff_contested_values(gt, entries[0])
        if contested:
            lines.append(f"   contested (gregtech dump won): {'; '.join(contested)}")
        if len(entries) > 1:
            details = "; ".join(
                f"id {e['id']} type={e['type']} prefixes={','.join(e['generatedPrefixes'])}" for e in entries)
            lines.append(f"   intra-pool fold of {len(entries)} same-name werkstoffe: {details}")
    lines.append("")
    MERGE_REPORT_PATH.write_text("\n".join(lines) + "\n", encoding="utf-8")
    return len(merged), len(revived), newly_referenced


LANG_BEGIN = "# --- BEGIN GENERATED MATERIAL NAMES (scripts/mu/gen_materials.py) ---"
LANG_END = "# --- END GENERATED MATERIAL NAMES ---"


def write_lang_file(ported, ml_names, extra_entries=()):
    entries = sorted(
        (ml_names[material["name"]], material["localName"]) for material in ported if material["localName"])
    entries = sorted(set(entries) | set(extra_entries))
    block_lines = [LANG_BEGIN]
    for name, local_name in entries:
        block_lines.append(f"material.gregtech.{name}={local_name}")
    block_lines.append(LANG_END)
    block = "\n".join(block_lines)

    if LANG_OUTPUT.exists():
        text = LANG_OUTPUT.read_text(encoding="utf-8")
        if LANG_BEGIN in text and LANG_END in text:
            before = text[:text.index(LANG_BEGIN)]
            after = text[text.index(LANG_END) + len(LANG_END):]
            text = before.rstrip("\n") + "\n\n" + block + after
        else:
            text = text.rstrip("\n") + "\n\n" + block + "\n"
    else:
        text = block + "\n"
    LANG_OUTPUT.write_text(text, encoding="utf-8")


def main():
    materials = load_materials()
    prefixes = load_prefixes()
    legacy_variants = load_legacy_variants()
    legacy_variant_prefixes = load_legacy_variant_prefixes(legacy_variants)
    legacy_variants_by_material = load_legacy_variants_by_material(legacy_variants)
    fluid_textures = load_fluid_textures()
    legacy_block_materials = load_legacy_block_materials()
    werkstoffs = load_werkstoffs()

    included_prefixes = [p for p in prefixes if is_included_shape(p, legacy_variant_prefixes)]
    included_names = set(p["name"] for p in included_prefixes)

    family_shape_members = {family: [] for family, _ in FAMILY_BITS}
    for prefix in included_prefixes:
        bits = prefix["generationBits"]
        for family, bit in FAMILY_BITS:
            if bits & bit:
                family_shape_members[family].append(prefix["name"])

    werkstoff_by_name, display_to_var = group_werkstoffs(werkstoffs)
    validate_werkstoff_prefixes(werkstoff_by_name, included_names)
    werkstoff_referenced = compute_werkstoff_referenced_names(werkstoff_by_name, display_to_var)
    werkstoff_names = {name for name, info in werkstoff_by_name.items() if info["entries"]}

    base_ported, _ = compute_ported(materials)
    base_ported_names = set(m["name"] for m in base_ported)
    ported, skipped = compute_ported(materials, werkstoff_referenced, werkstoff_names)
    ported_name_set = set(m["name"] for m in ported)
    missing_targets = sorted(set(werkstoff_by_name) - ported_name_set)
    if missing_targets:
        raise SystemExit(
            f"gen_materials.py: werkstoff fold targets missing from the gregtech dump/ported set: "
            f"{missing_targets}")

    ml_names, field_names = assign_names(ported)
    MATERIALS2_DIR.mkdir(parents=True, exist_ok=True)

    # gt/werkstoff main declarations claim shared fluid names first (used_fluid_names), exactly as before the
    # single-file merge -- the gtpp fold below must keep claiming in the same relative order (all merges, then
    # all new materials) so a material's fluid/cell shape assignment does not change (see gtpp_shape_lines'
    # "first claim wins" contract). Block assembly happens after the gtpp folds are built, since a merge
    # target's family selection needs its fold's parts (see build_material_block).
    used_fluid_names = set()
    gt_shape_lines_by_name = {}
    for material in ported:
        werkstoff_info = werkstoff_by_name.get(material["name"])
        gt_shape_lines_by_name[material["name"]] = build_gt_shape_lines(
            material, legacy_variants_by_material, used_fluid_names, fluid_textures, legacy_block_materials,
            werkstoff_info["prefixes"] if werkstoff_info else [])

    ported_names = set(ml_names.keys())
    materials_by_name = {m["name"]: m for m in ported}
    bridge_count, retained_fields = write_legacy_bridge_file(ported_names, field_names, materials_by_name)

    gt_by_name = {m["name"]: m for m in materials}
    merged_count, revived_count, newly_referenced = write_merge_report(
        werkstoff_by_name, gt_by_name, base_ported_names, ported_name_set)

    gtpp_materials = load_gtpp_materials()
    gtpp_ported, gtpp_skipped = compute_gtpp_ported(gtpp_materials, ported_name_set)
    gtpp_merges, gtpp_new, gtpp_false_merges = fold_gtpp_materials(gtpp_ported, gt_by_name, ported_name_set)

    gtpp_ml_seen = dict(ml_names)
    gtpp_field_seen = dict(field_names)
    gtpp_ml_names = dict(ml_names)
    gtpp_field_names = {}
    for entry in gtpp_new:
        raw = entry["unlocalizedName"]
        mn = ml_name(raw)
        fn = field_name(raw)
        if mn in gtpp_ml_seen:
            raise SystemExit(
                f"gen_materials.py: gtpp MaterialLib name collision: {raw!r} sanitizes to {mn!r}, already "
                f"used by {gtpp_ml_seen[mn]!r}")
        if fn in gtpp_field_seen:
            raise SystemExit(f"gen_materials.py: gtpp Java field name collision: {fn!r}")
        gtpp_ml_seen[mn] = raw
        gtpp_field_seen[fn] = raw
        gtpp_ml_names[raw] = mn
        gtpp_field_names[raw] = fn


    gtpp_merge_entry_by_name = {e["unlocalizedName"]: e for e in gtpp_merges}
    gtpp_merge_lines_by_name = {}
    for entry in gtpp_merges:
        gtpp_merge_lines_by_name[entry["unlocalizedName"]] = build_gtpp_merge_block(
            entry, gtpp_ml_names, gt_by_name, used_fluid_names, fluid_textures)
    gtpp_new_blocks = []
    for entry in gtpp_new:
        field = gtpp_field_names[entry["unlocalizedName"]]
        gtpp_new_blocks.append(
            build_gtpp_new_block(
                entry, field, gtpp_ml_names, included_names, family_shape_members, used_fluid_names,
                fluid_textures))
    write_gtpp_merge_report(gtpp_merges, gtpp_new, gtpp_false_merges, gtpp_skipped, gt_by_name)

    # File layout order: every gt/werkstoff material immediately followed by its own gtpp same-name fold (if
    # any), so a material's complete configuration is one contiguous run; gtpp-only materials follow after.
    field_lines = [f"    public static Material {field_names[m['name']]};" for m in ported]
    field_lines += [f"    public static Material {gtpp_field_names[e['unlocalizedName']]};" for e in gtpp_new]
    material_blocks = []
    for material in ported:
        name = material["name"]
        block = build_material_block(
            material, ml_names, field_names, included_names, family_shape_members, legacy_variants_by_material,
            fluid_textures, werkstoff_by_name.get(name), display_to_var,
            gt_shape_lines_by_name[name], gtpp_merge_entry_by_name.get(name),
            gtpp_merge_lines_by_name.get(name))
        merge_lines = gtpp_merge_lines_by_name.get(name)
        if merge_lines:
            block.extend(merge_lines)
        material_blocks.append(block)
    material_blocks.extend(gtpp_new_blocks)
    write_unified_materials_file(field_lines, material_blocks)

    gtpp_lang_extra = [
        (gtpp_ml_names[e["unlocalizedName"]], e["localName"]) for e in gtpp_new if e.get("localName")]
    write_lang_file(ported, ml_names, gtpp_lang_extra)

    chunk_count = len(list(chunk(material_blocks, MATERIALS_PER_INIT_CHUNK)))
    print(f"gen_materials.py: {len(materials)} dump entries, {len(ported)} ported, {len(skipped)} skipped")
    print(
        f"  werkstoff fold: {sum(len(i['entries']) for i in werkstoff_by_name.values())} pool entries onto "
        f"{len(werkstoff_names)} declarations ({merged_count} merged, {revived_count} revived mirrors), "
        f"{len(newly_referenced)} newly referenced markers ({', '.join(newly_referenced)}); see "
        f"{MERGE_REPORT_PATH.relative_to(REPO_ROOT)}")
    print(
        f"  wrote {MATERIALS_OUTPUT.relative_to(REPO_ROOT)}: {len(material_blocks)} materials, "
        f"{chunk_count} initPartN() chunks")
    print(f"  wrote {LANG_OUTPUT.relative_to(REPO_ROOT)}")
    captured = sum(1 for name in used_fluid_names if fluid_textures.get(name))
    print(
        f"  fluid textures: {captured}/{len(used_fluid_names)} generated fluid shapes captured a texture "
        f"({len(used_fluid_names) - captured} fall back to texture-set lookup)")
    uncaptured = sorted(name for name in used_fluid_names if not fluid_textures.get(name))
    if uncaptured:
        print(f"  fluids with no captured texture: {', '.join(uncaptured)}")
    if bridge_count is not None:
        print(
            f"  wrote {LEGACY_BRIDGE_OUTPUT.relative_to(REPO_ROOT)}: {bridge_count} bridged fields, "
            f"{len(retained_fields)} retained as markers (see LegacyMarkerMaterials.loadMarkers)")
    block_cutover = sorted(
        m["name"] for m in ported if m["name"] in legacy_block_materials and m["name"] not in BLOCK_CUTOVER_EXCLUDED)
    block_excluded = sorted(legacy_block_materials & BLOCK_CUTOVER_EXCLUDED)
    print(
        f"  block shape: {len(legacy_block_materials)} legacy storage-block materials, "
        f"{len(block_cutover)} cut over, {len(block_excluded)} excluded (structural references): "
        f"{', '.join(block_excluded)}")
    print(
        f"  gtpp fold: {len(gtpp_materials)} dump entries, {len(gtpp_ported)} ported "
        f"({len(gtpp_merges)} merged, {len(gtpp_new)} new), {len(gtpp_skipped)} skipped markers "
        f"({', '.join(sorted(gtpp_skipped))}), {len(gtpp_false_merges)} false merges excluded "
        f"({', '.join(sorted(e['unlocalizedName'] for e, _ in gtpp_false_merges))}); see "
        f"{GTPP_MERGE_REPORT_PATH.relative_to(REPO_ROOT)}")
    print(f"  total ported materials: {len(ported)} gregtech/werkstoff + {len(gtpp_new)} gtpp-only = "
        f"{len(ported) + len(gtpp_new)}")


if __name__ == "__main__":
    main()
