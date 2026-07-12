#!/usr/bin/env python3
"""Converts legacy per-slot `materialicons` textures into the per-shape MaterialLib texture layout.

Legacy GT keys item textures by a 192-slot `OrePrefixTextureID` (aliased to the ordinal of
`MaterialIconRegistry.IconType`, of which 158 are declared) under
`textures/items/materialicons/<SET>/<slotSuffix>.png`; several `OrePrefixes` share one slot (e.g.
`plateAlloy` and `plateSteamcraft` both use the `plate` slot). MaterialLib instead keys textures by shape
name under `textures/items/materials/<SET>/<shapeName>.png`, one file per shape, so a shared slot's file is
duplicated under every shape name that uses it.

A "SET" here is any directory under `materialicons/` that directly contains `.png` files; this covers both
the ~30 flat sets (`NONE/`, `RUBY/`, ...) and the `CUSTOM/<material>/` per-material sets, using the
directory's path relative to `materialicons/` (slashes included) as the set name -- matching the `iconSet`
string already recorded per material in `dumps/gt-materials.json`.

gtpp-only materials declare `TextureSet.of("gregtech", <set>)` like every other material (they reused
gregtech's texture domain directly once its materials/<set>/ art already covered every set they need,
including the five gtpp-exclusive `CUSTOM/<name>` sets converted here under `materials/CUSTOM/<name>/`), so
no separate domain copy step is needed for them.

Run from anywhere; paths are resolved relative to this script's location. Requires only the Python 3
standard library.

Usage: python scripts/mu/convert_textures.py
"""

import json
import shutil
from pathlib import Path

from gen_materials import BLOCK_CUTOVER_EXCLUDED
from gen_shapes import load_included_prefixes

SCRIPT_DIR = Path(__file__).resolve().parent
REPO_ROOT = SCRIPT_DIR.parent.parent
DUMP_PATH = SCRIPT_DIR / "dumps" / "oreprefixes.json"
LEGACY_BLOCKS_DUMP_PATH = SCRIPT_DIR / "dumps" / "legacy-blocks.json"

ASSETS_ITEMS = REPO_ROOT / "src/main/resources/assets/gregtech/textures/items"
SOURCE_ROOT = ASSETS_ITEMS / "materialicons"
DEST_ROOT = ASSETS_ITEMS / "materials"

# The `block` shape (Materials2BlockShapes) is a MaterialLib block, not an item: ShapeIcons resolves its icon
# through the block texture atlas, so its converted files live under `textures/blocks/materials/<SET>/`, a
# separate root from every other (item) shape converted above -- see `convert_blocks`.
ASSETS_BLOCKS = REPO_ROOT / "src/main/resources/assets/gregtech/textures/blocks"
BLOCK_SOURCE_ROOT = ASSETS_BLOCKS / "materialicons"
BLOCK_DEST_ROOT = ASSETS_BLOCKS / "materials"

# oreprefixes.json's textureIndex for "block" (see dumps/oreprefixes.json), resolved to its textureSlots suffix
# ("block1") once in main(); block is excluded from load_included_prefixes (block-kind), so its slot is looked
# up by OrePrefixes name directly, the same way load_cell_slot_suffixes does for containers.
BLOCK_PREFIX_NAME = "block"

# The `block` shape's legacy art is per-MATERIAL, not per-texture-set (`textures/blocks/iconsets/BLOCK_<X>.png`,
# one file per legacy `Materials` constant, positionally bound off `BlockMetal#mBlockIcons` -- see
# `legacy-blocks.json`'s `iconName` field and `MaterialDataDump#dumpLegacyBlocks`). GTStorageShapeBlock#
# iconPathFor resolves this at runtime from a material's legacy name, so the destination is keyed by material
# name rather than by set: `textures/blocks/materials/blocks/<legacy name lowercased>.png`, a `blocks/`
# subfolder standing in for `TextureSet`'s usual per-set folder, since this art has no texture set of its own.
BLOCK_MATERIALS_ICONSETS_DIR = ASSETS_BLOCKS / "iconsets"
BLOCK_MATERIALS_DEST_ROOT = ASSETS_BLOCKS / "materials" / "blocks"

PRIORITY_SET = "NONE"

# The ore/small-ore block shapes (Materials2OreShapes), excluded from gen_shapes.py's item-shape filter like
# `block`; looked up here directly by OrePrefixes name. Their material-overlay art lives in the blocks-tree
# materialicons source (textures/blocks/materialicons/<SET>/ore(Small).png), not the items tree -- legacy ore
# rendering is block-atlas-backed even though it reuses the same per-material icon-set slot numbering as items.
ORE_PREFIX_NAMES = ["ore", "oreSmall"]

# Cell shapes are fluid-in-container shapes (Materials2CellShapes), excluded from gen_shapes.py's item-shape
# filter, so their slot is looked up here directly by OrePrefixes name instead of via load_included_prefixes.
# shapeCellPlasmaLight is a second MaterialLib shape sharing shapeCellPlasma's oredict prefix (see
# Materials2CellShapes) and so has no OrePrefixes entry of its own; it aliases cellPlasma's slot by hand.
CELL_PREFIX_NAMES = [
    "cell", "cellPlasma", "cellMolten", "cellHydroCracked1", "cellHydroCracked2", "cellHydroCracked3",
    "cellSteamCracked1", "cellSteamCracked2", "cellSteamCracked3"
]
CELL_SHAPE_ALIASES = {"cellPlasmaLight": "cellPlasma"}

# Untinted container base textures for Materials2CellShapes' `emptyIcon` overrides: MaterialLib renders a
# container's untinted base under its tinted fill, the inverse of legacy's tinted-cell.png-under-untinted-
# cell_OVERLAY.png layering, so the shared overlay art becomes the base. `cell_OVERLAY.png` is byte-identical
# (MD5) across 61 of 66 material icon sets and `cellPlasma_OVERLAY.png` across 13 of 16 sets that have plasma-
# cell art; one arbitrary majority-hash set is used as the single shared source for each. The minority sets
# that render a cosmetically different overlay (cell: protohalkonitebase, sgcrystalfluid, spacetime, MAGNETIC;
# cellPlasma: MAGNETIC, spacetime) are accepted as a minor cosmetic deviation, same as any other material
# sharing one base texture.
CELL_BASE_SOURCE = SOURCE_ROOT / "DULL" / "cell_OVERLAY.png"
CELL_BASE_DEST = DEST_ROOT / "cell_base.png"
CELL_PLASMA_BASE_SOURCE = SOURCE_ROOT / "NONE" / "cellPlasma_OVERLAY.png"
CELL_PLASMA_BASE_DEST = DEST_ROOT / "cell_plasma_base.png"


def load_slot_suffixes():
    """Maps each included shape's name to its source slot basename (the `textureIndex`'s
    `textureSlots` suffix, leading '/' stripped), skipping shapes with no texture index and slots that are
    `MaterialIconRegistry.IconType` VOID padding entries (no real texture backs a void slot)."""
    with open(DUMP_PATH, encoding="utf-8") as f:
        dump = json.load(f)
    texture_slots = dump["textureSlots"]

    shape_slot = {}
    shapes_without_index = []
    for prefix in load_included_prefixes():
        name = prefix["name"]
        texture_index = prefix.get("textureIndex", -1)
        if texture_index is None or texture_index < 0:
            shapes_without_index.append(name)
            continue
        suffix = texture_slots.get(str(texture_index))
        if suffix is None or suffix == "/void":
            shapes_without_index.append(name)
            continue
        shape_slot[name] = suffix.lstrip("/")
    return shape_slot, shapes_without_index


def load_cell_slot_suffixes():
    """As `load_slot_suffixes`, for the fluid-in-container cell shapes (see `CELL_PREFIX_NAMES`), looked up by
    `OrePrefixes` name directly since `gen_shapes.py`'s item-shape filter excludes containers."""
    with open(DUMP_PATH, encoding="utf-8") as f:
        dump = json.load(f)
    texture_slots = dump["textureSlots"]
    prefixes_by_name = {p["name"]: p for p in dump["prefixes"]}

    shape_slot = {}
    for name in CELL_PREFIX_NAMES:
        suffix = texture_slots[str(prefixes_by_name[name]["textureIndex"])]
        shape_slot[name] = suffix.lstrip("/")
    for alias, source in CELL_SHAPE_ALIASES.items():
        shape_slot[alias] = shape_slot[source]
    return shape_slot


def convert_cell_bases():
    """Copies the two shared untinted container base textures (see `CELL_BASE_SOURCE`/`CELL_PLASMA_BASE_SOURCE`)
    into `materials/`, outside the per-set/per-shape layout since every material shares one base per slot."""
    written = 0
    for source, dest in ((CELL_BASE_SOURCE, CELL_BASE_DEST), (CELL_PLASMA_BASE_SOURCE, CELL_PLASMA_BASE_DEST)):
        if not source.is_file():
            continue
        dest.parent.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(source, dest)
        written += 1
    return written


def find_texture_sets(source_root):
    """Every directory under `source_root` that directly contains at least one `.png` file, keyed by its path
    relative to `source_root` with forward slashes (e.g. `NONE`, `RUBY`, `CUSTOM/bedrockium`)."""
    sets = {}
    for path in sorted(source_root.rglob("*")):
        if not path.is_dir():
            continue
        if not any(child.suffix == ".png" for child in path.iterdir() if child.is_file()):
            continue
        rel = path.relative_to(source_root).as_posix()
        sets[rel] = path
    ordered = {}
    if PRIORITY_SET in sets:
        ordered[PRIORITY_SET] = sets.pop(PRIORITY_SET)
    ordered.update(sets)
    return ordered


def load_block_slot_suffix():
    """The `textureSlots` suffix (leading '/' stripped) `block`'s `textureIndex` resolves to -- `block` is
    block-kind, excluded from `load_included_prefixes`, so this looks it up by `OrePrefixes` name directly, the
    same way `load_cell_slot_suffixes` does for containers."""
    with open(DUMP_PATH, encoding="utf-8") as f:
        dump = json.load(f)
    texture_slots = dump["textureSlots"]
    prefixes_by_name = {p["name"]: p for p in dump["prefixes"]}
    return texture_slots[str(prefixes_by_name[BLOCK_PREFIX_NAME]["textureIndex"])].lstrip("/")


def convert_blocks():
    """Converts the `block` shape's texture from every set under `textures/blocks/materialicons/` that has one,
    into `textures/blocks/materials/<SET>/block.png` (see `ASSETS_BLOCKS`). Unlike every other (item) shape,
    `block` has no per-shape slot table of its own to iterate -- it is the only MaterialLib block shape this
    stage adds -- so this converts its single slot directly instead of going through `convert_set`'s
    per-shape-name loop."""
    slot = load_block_slot_suffix()
    texture_sets = find_texture_sets(BLOCK_SOURCE_ROOT)
    files_written = 0
    sets_with_texture = 0
    for set_name, source_dir in texture_sets.items():
        base = source_dir / f"{slot}.png"
        if not base.is_file():
            continue
        dest_dir = BLOCK_DEST_ROOT / set_name
        dest_dir.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(base, dest_dir / "block.png")
        files_written += 1
        sets_with_texture += 1
        base_mcmeta = source_dir / f"{slot}.png.mcmeta"
        if base_mcmeta.is_file():
            shutil.copyfile(base_mcmeta, dest_dir / "block.png.mcmeta")
            files_written += 1
    return len(texture_sets), sets_with_texture, files_written


def convert_per_material_blocks():
    """Converts every cut-over material's legacy per-material storage-block art (see
    `BLOCK_MATERIALS_ICONSETS_DIR`/`BLOCK_MATERIALS_DEST_ROOT`) from `legacy-blocks.json`'s `iconName` field,
    which names the source file positionally rather than by the material's own name (see that field's
    docstring in `MaterialDataDump#dumpLegacyBlocks`) -- so, unlike `convert_set`, the destination name here is
    the material's legacy name, not the source file's. `BLOCK_CUTOVER_EXCLUDED` materials are skipped since
    they never resolve through the MaterialLib block shape (see `gen_materials.py`)."""
    with open(LEGACY_BLOCKS_DUMP_PATH, encoding="utf-8") as f:
        entries = json.load(f)

    files_written = 0
    materials_converted = []
    materials_missing_art = []
    for entry in entries:
        material = entry["material"]
        if material in BLOCK_CUTOVER_EXCLUDED:
            continue
        icon_name = entry.get("iconName")
        source = BLOCK_MATERIALS_ICONSETS_DIR / f"{icon_name.rsplit('/', 1)[-1]}.png" if icon_name else None
        if source is None or not source.is_file():
            materials_missing_art.append(material)
            continue

        BLOCK_MATERIALS_DEST_ROOT.mkdir(parents=True, exist_ok=True)
        dest = BLOCK_MATERIALS_DEST_ROOT / f"{material.lower()}.png"
        shutil.copyfile(source, dest)
        files_written += 1
        materials_converted.append(material)

        source_mcmeta = source.with_name(source.name + ".mcmeta")
        if source_mcmeta.is_file():
            shutil.copyfile(source_mcmeta, dest.with_name(dest.name + ".mcmeta"))
            files_written += 1

    return materials_converted, materials_missing_art, files_written


def load_ore_slot_suffixes():
    """As `load_block_slot_suffix`, for the ore/small-ore block shapes (see `ORE_PREFIX_NAMES`) -- both are
    block-kind, excluded from `load_included_prefixes`, so looked up by `OrePrefixes` name directly, the same
    way `load_cell_slot_suffixes` does for containers."""
    with open(DUMP_PATH, encoding="utf-8") as f:
        dump = json.load(f)
    texture_slots = dump["textureSlots"]
    prefixes_by_name = {p["name"]: p for p in dump["prefixes"]}

    shape_slot = {}
    for name in ORE_PREFIX_NAMES:
        suffix = texture_slots[str(prefixes_by_name[name]["textureIndex"])]
        shape_slot[name] = suffix.lstrip("/")
    return shape_slot


def convert_ore_blocks():
    """Converts the `ore`/`oreSmall` shapes' material-overlay textures from every set under
    `textures/blocks/materialicons/` that has one, into `textures/blocks/materials/<SET>/ore.png` /
    `oreSmall.png` (see `ASSETS_BLOCKS`) -- the same blocks-tree destination `convert_blocks` uses for the
    `block` shape, since [gregtech.api.enums.materials2.Materials2OreShapes]'s variant blocks register their
    icons through the block icon atlas, not the item one. This art is shared across every stone-type variant
    (legacy `GTBlockOre#getTextures` draws the same per-material-set `ore`/`oreSmall` slot regardless of stone
    type; the stone-specific background comes from `variantBase`, a fixed set of vanilla/GT texture paths hand-
    declared in `Materials2OreShapes`, not converted art), so this reuses `convert_set`'s per-shape-name/overlay
    loop wholesale rather than the material-keyed logic `convert_per_material_blocks` needed for storage blocks."""
    shape_slot = load_ore_slot_suffixes()
    texture_sets = find_texture_sets(BLOCK_SOURCE_ROOT)
    total_files = 0
    shapes_with_any_texture = set()
    for set_name, source_dir in texture_sets.items():
        files_written, shapes_with_texture = convert_set(set_name, source_dir, shape_slot, dest_root=BLOCK_DEST_ROOT)
        total_files += files_written
        shapes_with_any_texture |= shapes_with_texture
    return len(texture_sets), total_files, shapes_with_any_texture


def convert_set(set_name, source_dir, shape_slot, skip_overlay_shapes=frozenset(), dest_root=None):
    """Converts `shape_slot`'s shapes for one texture set. `skip_overlay_shapes` names shapes that never render
    an `_OVERLAY` file even when the source slot has one: `ShapeFluidInContainer` (see `Materials2CellShapes`)
    never calls `ShapeIcons#getOverlay`, since its own base/fill layering already covers both render passes, so
    converting an `_OVERLAY` file for a cell shape would only be dead weight. `dest_root` defaults to the items
    tree (`DEST_ROOT`); `convert_ore_blocks` passes the blocks tree instead."""
    dest_dir = (dest_root if dest_root is not None else DEST_ROOT) / set_name
    files_written = 0
    shapes_with_texture = set()
    for shape_name, slot in shape_slot.items():
        base = source_dir / f"{slot}.png"
        if not base.is_file():
            continue
        dest_dir.mkdir(parents=True, exist_ok=True)
        shutil.copyfile(base, dest_dir / f"{shape_name}.png")
        files_written += 1
        shapes_with_texture.add(shape_name)

        base_mcmeta = source_dir / f"{slot}.png.mcmeta"
        if base_mcmeta.is_file():
            shutil.copyfile(base_mcmeta, dest_dir / f"{shape_name}.png.mcmeta")
            files_written += 1

        if shape_name in skip_overlay_shapes:
            continue

        overlay = source_dir / f"{slot}_OVERLAY.png"
        if overlay.is_file():
            shutil.copyfile(overlay, dest_dir / f"{shape_name}_OVERLAY.png")
            files_written += 1

            overlay_mcmeta = source_dir / f"{slot}_OVERLAY.png.mcmeta"
            if overlay_mcmeta.is_file():
                shutil.copyfile(overlay_mcmeta, dest_dir / f"{shape_name}_OVERLAY.png.mcmeta")
                files_written += 1

    return files_written, shapes_with_texture


def main():
    shape_slot, shapes_without_index = load_slot_suffixes()
    cell_slot = load_cell_slot_suffixes()
    shape_slot.update(cell_slot)
    skip_overlay_shapes = frozenset(cell_slot)
    texture_sets = find_texture_sets(SOURCE_ROOT)

    total_files = 0
    shapes_with_any_texture = set()
    per_set_files = {}
    for set_name, source_dir in texture_sets.items():
        files_written, shapes_with_texture = convert_set(set_name, source_dir, shape_slot, skip_overlay_shapes)
        per_set_files[set_name] = files_written
        total_files += files_written
        shapes_with_any_texture |= shapes_with_texture

    base_files_written = convert_cell_bases()
    total_files += base_files_written

    block_sets, block_sets_with_texture, block_files_written = convert_blocks()
    total_files += block_files_written

    per_material_converted, per_material_missing, per_material_files_written = convert_per_material_blocks()
    total_files += per_material_files_written

    ore_sets, ore_files_written, ore_shapes_with_texture = convert_ore_blocks()
    total_files += ore_files_written

    shapes_never_textured = sorted(
        (set(shape_slot) | set(shapes_without_index)) - shapes_with_any_texture
    )

    print(f"convert_textures.py: {len(texture_sets)} texture sets processed, {total_files} files written")
    for set_name in texture_sets:
        print(f"  {set_name}: {per_set_files[set_name]} files")
    print(f"cell container base textures: {base_files_written} files (cell_base.png, cell_plasma_base.png)")
    print(
        f"block shape (blocks tree): {block_sets_with_texture}/{block_sets} texture sets have art, "
        f"{block_files_written} files written")
    print(
        f"block shape per-material art: {len(per_material_converted)}/"
        f"{len(per_material_converted) + len(per_material_missing)} cut-over materials converted, "
        f"{per_material_files_written} files written")
    if per_material_missing:
        print(f"  materials with no legacy per-material art: {len(per_material_missing)}")
        for name in sorted(per_material_missing):
            print(f"    {name}")
    print(
        f"ore/oreSmall shape (blocks tree): {len(ore_shapes_with_texture)}/{len(ORE_PREFIX_NAMES)} shapes have "
        f"art in at least one of {ore_sets} sets, {ore_files_written} files written")
    print(f"shapes with no texture index or slot (no source in any set): {len(shapes_without_index)}")
    for name in sorted(shapes_without_index):
        print(f"  {name}")
    print(f"shapes with no texture in any set (including the above): {len(shapes_never_textured)}")
    for name in shapes_never_textured:
        print(f"  {name}")


if __name__ == "__main__":
    main()
