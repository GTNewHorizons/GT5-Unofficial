package gregtech.loaders.postload;

import static gregtech.api.enums.OrePrefixes.___placeholder___;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntFunction;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IBlockTransformationHandler;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.IItemStackTransformationHandler;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.LegacyMaterialIDIndex;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.material.MaterialParts;
import gregtech.api.util.GTLog;
import gregtech.common.blocks.BlockMetal;
import gregtech.common.blocks.FrameShapeBlock;
import gregtech.common.blocks.PipeShapeBlock;
import gregtech.common.items.MetaGeneratedItem99;
import gregtech.loaders.postload.GtppItemCutoverTable.Entry;
import vexatos.tgregworks.reference.Mods;

public class PosteaTransformers implements Runnable {

    // Legacy bartworks werkstoff id offset (see LegacyWerkstoffIndex)
    private static final int OFFSET_ID_3 = 11_300;
    private static final int[] WERKSTOFFS_REMOVED_IN_2_9 = new int[] { OFFSET_ID_3, OFFSET_ID_3 + 2, OFFSET_ID_3 + 6,
        OFFSET_ID_3 + 7, OFFSET_ID_3 + 8, OFFSET_ID_3 + 11, OFFSET_ID_3 + 12 };

    /// Every legacy block/item registry name a transformer has been registered for, collected by the
    /// `addBlockTransformer`/`addBlockReplacement`/`addItemTransformer`/`addItemReplacement` wrappers so that
    /// [#claimMissingMappings] can claim all of them. Registration goes through those wrappers rather than the
    /// Postea managers directly precisely so that a name can never be transformed without also being claimed.
    private static final Set<String> transformedIds = new LinkedHashSet<>();

    @Override
    public void run() {
        validateCutoverTables();
        registerFrameboxTransformers();
        registerProgrammedCircuitTransformers();
        registerPotassiumHydroxideTransformer();
        registerPTMEGTransformers();
        registerBorosilicateGlassTransformers();
        registerIC2BlocksTransformer();
        registerMaterialLibCutoverTransformers();
        claimMissingMappings();
    }

    /// Claims the FML missing mapping of every legacy registry name a transformer was registered for. FML treats a
    /// saved id whose registration is gone as unhandled and discards it, deleting the block or stack before Postea's
    /// chunk and stack passes ever see it; claiming the mapping is the whole of what is needed to stop that, because
    /// Postea captures the world's name -> id map in `GameData.injectSnapshot` and so still resolves the saved
    /// numeric id for its own passes.
    ///
    /// [BlockReplacementManager#ignoreMissingMapping] and [ItemStackReplacementManager#ignoreMissingMapping] feed one
    /// shared ignore set, so a single call covers both the block and the item leg of a name.
    private static void claimMissingMappings() {
        for (String legacyId : transformedIds) {
            BlockReplacementManager.ignoreMissingMapping(legacyId);
        }
        GTLog.out.println("PosteaTransformers: claimed missing mappings for " + transformedIds.size() + " legacy ids");
    }

    private static void addBlockTransformer(String legacyId, IBlockTransformationHandler handler) {
        transformedIds.add(legacyId);
        BlockReplacementManager.addTransformationHandler(legacyId, handler);
    }

    private static void addBlockReplacement(String legacyId, Block block, int newMeta) {
        transformedIds.add(legacyId);
        BlockReplacementManager.addSimpleReplacement(legacyId, block, newMeta);
    }

    private static void addBlockReplacement(String legacyId, int legacyMeta, Block block, int newMeta) {
        transformedIds.add(legacyId);
        BlockReplacementManager.addSimpleReplacement(legacyId, legacyMeta, block, newMeta);
    }

    private static void addItemTransformer(String legacyId, IItemStackTransformationHandler handler) {
        transformedIds.add(legacyId);
        ItemStackReplacementManager.addTransformationHandler(legacyId, handler);
    }

    private static void addItemReplacement(String legacyId, ItemStack stack) {
        transformedIds.add(legacyId);
        ItemStackReplacementManager.addSimpleReplacement(legacyId, stack);
    }

    private static void addItemReplacement(String legacyId, ItemStack stack, boolean skipBlockRemap) {
        transformedIds.add(legacyId);
        ItemStackReplacementManager.addSimpleReplacement(legacyId, stack, skipBlockRemap);
    }

    private static void addItemReplacement(String legacyId, Item item, boolean skipBlockRemap) {
        transformedIds.add(legacyId);
        ItemStackReplacementManager.addSimpleReplacement(legacyId, item, skipBlockRemap);
    }

    private static void addItemReplacement(String legacyId, int legacyMeta, Item item, int newMeta) {
        transformedIds.add(legacyId);
        ItemStackReplacementManager.addSimpleReplacement(legacyId, legacyMeta, item, newMeta);
    }

    /// Resolves every generated cutover row once, here, so a row whose material or target shape stopped
    /// resolving is one log line naming the row rather than a silent per-stack no-op at chunk load -- where the
    /// legacy item is already gone, so FML deletes the stack instead of migrating it. Every row is expected to
    /// resolve; a row that deliberately never cuts over belongs in its table's javadoc as such, so that what
    /// shows up here reads as a regression.
    private static void validateCutoverTables() {
        List<String> unresolved = new ArrayList<>();

        for (Entry entry : GtppItemCutoverTable.ENTRIES) {
            if (resolveGtppCutoverStack(entry) == null) unresolved.add(entry.registryName());
        }
        for (GtppOreCutoverTable.Entry entry : GtppOreCutoverTable.ENTRIES) {
            if (resolveGtppOreCutoverStack(entry) == null) unresolved.add(entry.registryName());
        }
        for (Map.Entry<Integer, LegacyPipeCutoverTable.Entry> row : LegacyPipeCutoverTable.entries()
            .entrySet()) {
            LegacyPipeCutoverTable.Entry entry = row.getValue();
            if (MaterialLibAPI.getBlock(entry.shape()) == null || !entry.material()
                .hasShape(entry.shape())) {
                unresolved.add(
                    "pipe MTE id " + row.getKey()
                        + " ("
                        + entry.material()
                            .getName()
                        + " "
                        + entry.shape()
                            .getName()
                        + ")");
            }
        }

        if (!unresolved.isEmpty()) {
            GTMod.GT_FML_LOGGER.error(
                "PosteaTransformers: {} cutover rows no longer resolve; saved stacks of these will be deleted rather than migrated: {}",
                unresolved.size(),
                unresolved.subList(0, Math.min(unresolved.size(), MAX_REPORTED_UNRESOLVED_ROWS)));
        }
    }

    private static final int MAX_REPORTED_UNRESOLVED_ROWS = 50;

    /// Migrates saved [MetaGeneratedItemX32] stacks (`gt.metaitem.01/02/03`, damage < 32000) and
    /// [MetaGeneratedItem99] stacks (`gt.metaitem.99`, cell molten/cracked prefixes) whose prefix cut over to
    /// a MaterialLib shape (see [MaterialParts]) into the equivalent MaterialLib stack. Hand-listed custom parts
    /// (damage >= 32000 on `metaitem.01/02/03`) and prefixes that have not cut over pass through unchanged.
    private void registerMaterialLibCutoverTransformers() {
        registerMetaItemCutoverTransformer("gt.metaitem.01");
        registerMetaItemCutoverTransformer("gt.metaitem.02");
        registerMetaItemCutoverTransformer("gt.metaitem.03");
        registerMetaItem99CutoverTransformer();
        registerStorageBlockCutoverTransformers();
        registerFrameAndSheetmetalCutoverTransformers();
        registerWerkstoffItemCutoverTransformers();
        registerGtppItemCutoverTransformers();
        registerGtppCarryoverCellTransformers();
        registerGtppOreCutoverTransformers();
        registerGtppFrameCutoverTransformers();
        registerPipeCutoverTransformers();
        registerEmptyCellCutoverTransformer();
    }

    /// Migrates saved IC2 empty cells (`IC2:itemCellEmpty`) onto the gregtech-owned empty cell every cell shape
    /// drains to ([ItemList#Cell_Empty]). Keyed on meta 0 alone: metas 1-14 of that id are IC2's own filled cells,
    /// which keep their meaning.
    private static void registerEmptyCellCutoverTransformer() {
        ItemStack cutover = ItemList.Cell_Empty.get(1);
        if (cutover == null) {
            throw new IllegalStateException("No MaterialLib stack for the empty cell cutover");
        }
        addItemReplacement("IC2:itemCellEmpty", 0, cutover.getItem(), cutover.getItemDamage());
    }

    /// Migrates saved legacy pipe-family instances (per-material wire/cable/fluid-pipe/item-pipe MTE ids,
    /// see [LegacyPipeCutoverTable]) onto the material-agnostic MaterialLib shape blocks: block = the
    /// shape's [PipeShapeBlock], meta = the MaterialLib material index. Every legacy pipe was TE-backed and
    /// every shape block hosts an eager TE, so placed instances keep their tile entity with `mID` rewritten
    /// to the shape's single MTE id; the rest of the tag (connections, covers, paint, stored fluids/items)
    /// rides along untouched. Inventory stacks (`gt.blockmachines`, damage = MTE id) become the shape
    /// block's ItemBlock at the material index, mirroring [#registerFrameboxTransformers]' item idiom.
    /// Ids outside the table -- gaps inside the freed ranges and every live MTE id -- pass through to the
    /// other `BaseMetaPipeEntity`/`gt.blockmachines` handlers unchanged.
    private static void registerPipeCutoverTransformers() {
        Map<Integer, LegacyPipeCutoverTable.Entry> table = LegacyPipeCutoverTable.entries();

        TileEntityReplacementManager.tileEntityTransformer("BaseMetaPipeEntity", (tag, world, chunk) -> {
            LegacyPipeCutoverTable.Entry entry = table.get(tag.getInteger("mID"));
            if (entry == null) return null;
            PipeShapeBlock shapeBlock = (PipeShapeBlock) MaterialLibAPI.getBlock(entry.shape());
            return new BlockInfo(
                shapeBlock,
                entry.material()
                    .getIndex(),
                pipeTag -> {
                    pipeTag.setInteger("mID", shapeBlock.getMteId());
                    return pipeTag;
                });
        });

        addItemTransformer("gregtech:gt.blockmachines", (originalId, tag) -> {
            LegacyPipeCutoverTable.Entry entry = table.get(tag.getInteger("Damage"));
            if (entry == null) return false;
            Item shapeItem = Item.getItemFromBlock(MaterialLibAPI.getBlock(entry.shape()));
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(shapeItem));
            tag.setShort(
                "Damage",
                (short) entry.material()
                    .getIndex());
            return true;
        });

        GTLog.out
            .println("PosteaTransformers: registered pipe-family transformers for " + table.size() + " legacy MTE ids");
    }

    /// Migrates saved legacy `BlockBaseOre` placed/inventory stacks into the equivalent MaterialLib
    /// [OreShapes#ore] stack, resolved through
    /// [GtppOreCutoverTable]'s (unlocalized name, registry name) rows the same way
    /// [gregtech.common.ores.GTOreAdapter#getBlock] resolves it live. Unlike every gtpp part/block row
    /// (one distinct registered instance per material, no meta multiplexing), so a single item + block
    /// transformer pair per row is enough, mirroring [#registerGtppItemCutoverTransformers]' `block`-row
    /// handling. A material that never gained `ore` membership resolves null and is left on its legacy
    /// slot, same as every other cutover table here.
    private static void registerGtppOreCutoverTransformers() {
        for (GtppOreCutoverTable.Entry entry : GtppOreCutoverTable.ENTRIES) {
            addItemTransformer(entry.registryName(), (originalId, tag) -> {
                ItemStack cutover = resolveGtppOreCutoverStack(entry);
                if (cutover == null) return false;
                IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
                tag.setShort("Damage", (short) cutover.getItemDamage());
                return true;
            });

            addBlockTransformer(entry.registryName(), info -> {
                ItemStack cutover = resolveGtppOreCutoverStack(entry);
                if (cutover == null) return false;
                info.blockID = Block.getIdFromBlock(Block.getBlockFromItem(cutover.getItem()));
                info.metadata = cutover.getItemDamage();
                return true;
            });
        }
        GTLog.out.println(
            "PosteaTransformers: registered gtpp ore transformers for " + GtppOreCutoverTable.ENTRIES.length
                + " legacy blocks");
    }

    private static ItemStack resolveGtppOreCutoverStack(GtppOreCutoverTable.Entry entry) {
        Material ml = MaterialLibAPI.getMaterial("gregtech", entry.unlocalizedName());
        if (ml == null || !ml.hasShape(OreShapes.ore)) return null;
        return MaterialLibAPI.getStack(ml, OreShapes.ore, 1);
    }

    /// Migrates saved placed blocks and inventory stacks of a gtPlusPlus per-material frame block
    /// (`miscutils:blockFrameGt<Name>`, one distinct registered block per material at meta 0) into the
    /// equivalent MaterialLib `frameGt` shape stack. Unlike every metadata-keyed legacy part, the material is
    /// fixed by which registry name is being migrated rather than by a damage value, so each of
    /// [GtppFrameCutoverTable]'s materials gets its own registration instead of sharing
    /// one metadata-keyed handler. `addSimpleReplacement`'s block+meta overload registers a matching item
    /// replacement automatically, so no separate item-side call is needed. A material whose frame shape did
    /// not generate is left on its legacy slot.
    private static void registerGtppFrameCutoverTransformers() {
        Material[] materials = GtppFrameCutoverTable.materials();
        int count = 0;
        for (Material material : materials) {
            if (!material.hasShape(TEBlockShapes.frameGt)) continue;
            ItemStack cutover = MaterialLibAPI.getStack(material, TEBlockShapes.frameGt, 1);
            String legacyId = "miscutils:blockFrameGt" + material.getName();
            Block mlBlock = Block.getBlockFromItem(cutover.getItem());
            addBlockReplacement(legacyId, 0, mlBlock, cutover.getItemDamage());
            count++;
        }
        GTLog.out.println("PosteaTransformers: registered gtpp frame transformers for " + count + " legacy blocks");
    }

    /// Migrates saved gtPlusPlus per-material part stacks (`miscutils:item*`/`miscutils:block*`, one distinct
    /// registered item/block per (material, prefix), always damage 0) into the equivalent MaterialLib stack,
    /// resolved through [GtppItemCutoverTable]'s pinned (prefix, material, registry name) rows -- the
    /// gtPlusPlus counterpart of [#registerWerkstoffItemCutoverTransformers], differing only in that each row
    /// is its own registered item/block rather than a damage variant of a shared meta-item, so no damage
    /// read/branch is needed. `cell` rows resolve through [MaterialParts#cell] instead of plain
    /// [MaterialParts#stack], since a
    /// row's material may have claimed `cellMolten` rather than `cell` -- see that method's javadoc.
    /// `frameGt` is out of the table and migrates separately. `block` rows additionally get a
    /// [BlockReplacementManager] handler for placed instances, since a storage block (unlike every other gtpp
    /// part) is placeable; a row whose material carries no `block` shape resolves null from [MaterialParts#stack] same
    /// as any other prefix, leaving the legacy slot canonical.
    private static void registerGtppItemCutoverTransformers() {
        for (Entry entry : GtppItemCutoverTable.ENTRIES) {
            addItemTransformer(entry.registryName(), (originalId, tag) -> {
                ItemStack cutover = resolveGtppCutoverStack(entry);
                if (cutover == null) return false;
                IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
                tag.setShort("Damage", (short) cutover.getItemDamage());
                return true;
            });
            if (entry.prefix() == OrePrefixes.block) {
                addBlockTransformer(entry.registryName(), info -> {
                    ItemStack cutover = resolveGtppCutoverStack(entry);
                    if (cutover == null) return false;
                    info.blockID = Block.getIdFromBlock(Block.getBlockFromItem(cutover.getItem()));
                    info.metadata = cutover.getItemDamage();
                    return true;
                });
            }
        }
        GTLog.out.println(
            "PosteaTransformers: registered gtpp item transformers for " + GtppItemCutoverTable.ENTRIES.length
                + " legacy items");
    }

    private static ItemStack resolveGtppCutoverStack(Entry entry) {
        Material material = MaterialLibAPI.getMaterial("gregtech", entry.materialName());
        if (entry.prefix() == OrePrefixes.cell) {
            return MaterialParts.cell(material, 1);
        }
        return MaterialParts.stack(entry.prefix(), material, 1);
    }

    /// The materials whose legacy `cell` item was `miscutils:itemCell<Name>`, the same naming convention as
    /// every other gtPlusPlus-owned cell, but for which [GtppItemCutoverTable] holds no row: that table is
    /// generated purely from a pinned registry dump, and the dump never captured that id for them. Most are
    /// plain gregtech elements whose own fluid/cell cutover claims the oredict `cell<Name>` slot before gtpp's
    /// `Material` construction ever runs, so the dump recorded a gregtech-owned `materiallib:cell` stack in
    /// their place; `ZirconiumTetrafluoride`'s gtpp cell carried no `itemCell` prefix, so only its unprefixed
    /// id reached the dump. They are migrated by hand instead, to the same fallback [MaterialParts#cell] every
    /// other gtpp cell resolves through.
    private static void registerGtppCarryoverCellTransformers() {
        registerGtppCarryoverCellTransformer("Iodine");
        registerGtppCarryoverCellTransformer("ThoriumTetrafluoride");
        registerGtppCarryoverCellTransformer("ZirconiumTetrafluoride");
        registerGtppCarryoverCellTransformer("Xenon");
        registerGtppCarryoverCellTransformer("Neon");
        registerGtppCarryoverCellTransformer("Krypton");
    }

    private static void registerGtppCarryoverCellTransformer(String materialName) {
        Material material = MaterialLibAPI.getMaterial("gregtech", materialName);
        ItemStack cutover = MaterialParts.cell(material, 1);
        if (cutover == null) {
            throw new IllegalStateException("No MaterialLib cell stack for carryover material " + materialName);
        }
        addItemReplacement("miscutils:itemCell" + materialName, cutover);
    }

    /// Migrates saved bartworks werkstoff item stacks (`bartworks:gt.bwMetaGenerated<prefix>`, damage =
    /// werkstoff id) into the equivalent MaterialLib stack, resolved through [LegacyWerkstoffIndex] exactly
    /// like the live item path ([MaterialParts#stack]). Damages of
    /// werkstoffe unknown to MaterialLib (a third-party mod's own werkstoff ids) pass through unchanged. Ore/small ore
    /// migrate through [BWOreAdapter] instead (block-kind, no `bw.bwMetaGenerated<prefix>` item exists for
    /// them); storage blocks migrate through [#registerWerkstoffBlockCutoverTransformer]. The casing slots
    /// (`blockCasing`/`blockCasingAdvanced`) stay legacy-canonical: multiblock structure matchers reference
    /// the legacy casing blocks by identity.
    /// The part prefixes the legacy bartworks meta items covered, one item each, registered as
    /// `bartworks:gt.bwMetaGenerated<prefix>`. Declared rather than read off the live items because the items
    /// themselves are gone: a saved stack still names them, so the migration has to keep answering for every
    /// prefix they ever occupied.
    private static final OrePrefixes[] LEGACY_WERKSTOFF_ITEM_PREFIXES = { OrePrefixes.dust, OrePrefixes.dustTiny,
        OrePrefixes.dustSmall, OrePrefixes.ingot, OrePrefixes.ingotHot, OrePrefixes.nugget, OrePrefixes.gem,
        OrePrefixes.gemChipped, OrePrefixes.gemExquisite, OrePrefixes.gemFlawed, OrePrefixes.gemFlawless,
        OrePrefixes.lens, OrePrefixes.crushed, OrePrefixes.crushedPurified, OrePrefixes.crushedCentrifuged,
        OrePrefixes.dustPure, OrePrefixes.dustImpure, OrePrefixes.rawOre, OrePrefixes.cell, OrePrefixes.cellPlasma,
        OrePrefixes.plate, OrePrefixes.foil, OrePrefixes.stick, OrePrefixes.stickLong, OrePrefixes.toolHeadWrench,
        OrePrefixes.toolHeadHammer, OrePrefixes.toolHeadSaw, OrePrefixes.turbineBlade, OrePrefixes.gearGt,
        OrePrefixes.gearGtSmall, OrePrefixes.bolt, OrePrefixes.screw, OrePrefixes.ring, OrePrefixes.spring,
        OrePrefixes.springSmall, OrePrefixes.rotor, OrePrefixes.wireFine, OrePrefixes.plateDouble,
        OrePrefixes.plateDense, OrePrefixes.plateSuperdense, OrePrefixes.plateTriple, OrePrefixes.plateQuadruple,
        OrePrefixes.plateQuintuple, OrePrefixes.cellMolten };

    private static void registerWerkstoffItemCutoverTransformers() {
        for (OrePrefixes prefix : LEGACY_WERKSTOFF_ITEM_PREFIXES) {
            addItemTransformer("bartworks:gt.bwMetaGenerated" + prefix.getName(), (originalId, tag) -> {
                int damage = tag.getInteger("Damage");
                Material material = LegacyWerkstoffIndex.get(damage);
                if (material == null) return false;
                ItemStack cutover = MaterialParts.stack(prefix, material, 1);
                if (cutover == null) return false;
                IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
                tag.setShort("Damage", (short) cutover.getItemDamage());
                return true;
            });
        }
        GTLog.out.println(
            "PosteaTransformers: registered werkstoff item transformers for " + LEGACY_WERKSTOFF_ITEM_PREFIXES.length
                + " legacy items");
    }

    /// Migrates saved placed blocks and item stacks of a cut-over material's legacy storage-block slot (see
    /// [BlockMetal], [MaterialParts]) into the equivalent MaterialLib block stack. `addSimpleReplacement`'s block+meta
    /// overload registers a matching item replacement automatically, so no separate item-side call is needed.
    /// Materials that did not cut over are skipped: their
    /// slot stays legacy, and the legacy block instance itself is never removed (see [BlockMetal]'s javadoc), so
    /// nothing needs migrating for them.
    private static void registerStorageBlockCutoverTransformers() {
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal1", GregTechAPI.sBlockMetal1);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal2", GregTechAPI.sBlockMetal2);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal3", GregTechAPI.sBlockMetal3);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal4", GregTechAPI.sBlockMetal4);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal5", GregTechAPI.sBlockMetal5);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal6", GregTechAPI.sBlockMetal6);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal7", GregTechAPI.sBlockMetal7);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal8", GregTechAPI.sBlockMetal8);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal9", GregTechAPI.sBlockMetal9);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockmetal10", GregTechAPI.sBlockMetal10);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockgem1", GregTechAPI.sBlockGem1);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockgem2", GregTechAPI.sBlockGem2);
        registerStorageBlockCutoverTransformer("gregtech:gt.blockgem3", GregTechAPI.sBlockGem3);
        registerWerkstoffBlockCutoverTransformer(
            "bw.werkstoffblockTE",
            "bartworks:bw.werkstoffblocks.01",
            OrePrefixes.block);
        registerWerkstoffBlockCutoverTransformer(
            "bw.werkstoffblockcasingTE",
            "bartworks:bw.werkstoffblockscasing.01",
            OrePrefixes.blockCasing);
        registerWerkstoffBlockCutoverTransformer(
            "bw.werkstoffblockscasingadvancedTE",
            "bartworks:bw.werkstoffblockscasingadvanced.01",
            OrePrefixes.blockCasingAdvanced);
    }

    /// Migrates saved placed (TE-based) and inventory bartworks werkstoff storage-block stacks (`m`/`Damage` =
    /// werkstoff id) into the equivalent MaterialLib block stack, resolved through [LegacyWerkstoffIndex]
    /// exactly like the live item path ([MaterialParts#stack]). Third-party
    /// werkstoffe unknown to MaterialLib pass through unchanged, leaving the legacy slot canonical for them.
    /// `bw.werkstoffblockTE` already has a handler registered by [#removeWerkstoffTileEntities]; Postea tries
    /// each registered handler in turn until one returns non-null, so the two coexist without conflict.
    private static void registerWerkstoffBlockCutoverTransformer(String teId, String itemId, OrePrefixes prefix) {
        TileEntityReplacementManager.tileEntityTransformer(teId, (tag, world, chunk) -> {
            Material material = LegacyWerkstoffIndex.get(tag.getShort("m"));
            if (material == null) return null;
            ItemStack cutover = MaterialParts.stack(prefix, material, 1);
            if (cutover == null) return null;
            return new BlockInfo(Block.getBlockFromItem(cutover.getItem()), cutover.getItemDamage());
        });

        addItemTransformer(itemId, (originalId, tag) -> {
            Material material = LegacyWerkstoffIndex.get(tag.getInteger("Damage"));
            if (material == null) return false;
            ItemStack cutover = MaterialParts.stack(prefix, material, 1);
            if (cutover == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
            tag.setShort("Damage", (short) cutover.getItemDamage());
            return true;
        });
    }

    private static void registerStorageBlockCutoverTransformer(String originalId, Block legacyBlock) {
        BlockMetal metal = (BlockMetal) legacyBlock;
        for (int meta = 0; meta < metal.mMats.length; meta++) {
            Material material = metal.mMats[meta];
            if (material == null || !material.hasShape(BlockShapes.block)) continue;
            ItemStack cutover = MaterialLibAPI.getStack(material, BlockShapes.block, 1);
            Block mlBlock = Block.getBlockFromItem(cutover.getItem());
            addBlockReplacement(originalId, meta, mlBlock, cutover.getItemDamage());
        }
    }

    /// Migrates saved placed blocks and inventory stacks of the legacy frameGt/sheetmetal blocks
    /// (`gregtech:bw.frames`, `gregtech:bw.sheetmetal`, `gregtech:gt.sheetmetal`, `gregtech:gt.blockframes`,
    /// meta = material id) into the equivalent MaterialLib shape stack. The bw-named ids carry the gregtech
    /// domain despite their name because gregtech registered them. None of these blocks carry a TileEntity,
    /// unlike [#registerWerkstoffBlockCutoverTransformer]'s storage blocks, so only a block leg and an item leg
    /// are needed.
    private static void registerFrameAndSheetmetalCutoverTransformers() {
        registerPartCutoverTransformer(
            "gregtech:bw.frames",
            OrePrefixes.frameGt,
            PosteaTransformers::werkstoffMaterialAt);
        registerPartCutoverTransformer(
            "gregtech:bw.sheetmetal",
            OrePrefixes.sheetmetal,
            PosteaTransformers::werkstoffMaterialAt);
        registerPartCutoverTransformer("gregtech:gt.sheetmetal", OrePrefixes.sheetmetal, LegacyMaterialIDIndex::get);
        registerPartCutoverTransformer("gregtech:gt.blockframes", OrePrefixes.frameGt, LegacyMaterialIDIndex::get);
    }

    /// The MaterialLib material a legacy bartworks werkstoff id resolves to, or null when the id is unknown
    /// (a third-party mod's own werkstoff id) -- the [#registerPartCutoverTransformer] resolver for
    /// werkstoff-keyed legacy parts.
    private static Material werkstoffMaterialAt(int meta) {
        return LegacyWerkstoffIndex.get(meta);
    }

    /// Migrates saved placed blocks and inventory stacks of a legacy non-TE part block whose meta is a
    /// material id into the equivalent MaterialLib [prefix] stack, resolving the id through
    /// `materialResolver`. Materials the resolver cannot place (unknown legacy id) or that have not cut over
    /// to `prefix` are left on their legacy slot.
    private static void registerPartCutoverTransformer(String legacyId, OrePrefixes prefix,
        IntFunction<Material> materialResolver) {
        addBlockTransformer(legacyId, info -> {
            Material material = materialResolver.apply(info.metadata);
            if (material == null) return false;
            ItemStack cutover = MaterialParts.stack(prefix, material, 1);
            if (cutover == null) return false;
            info.blockID = Block.getIdFromBlock(Block.getBlockFromItem(cutover.getItem()));
            info.metadata = cutover.getItemDamage();
            return true;
        });

        addItemTransformer(legacyId, (originalId, tag) -> {
            Material material = materialResolver.apply(tag.getInteger("Damage"));
            if (material == null) return false;
            ItemStack cutover = MaterialParts.stack(prefix, material, 1);
            if (cutover == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
            tag.setShort("Damage", (short) cutover.getItemDamage());
            return true;
        });
    }

    private static void registerMetaItemCutoverTransformer(String itemName) {
        MetaGeneratedItemX32 item = (MetaGeneratedItemX32) GameRegistry.findItem("gregtech", itemName);
        addItemTransformer("gregtech:" + itemName, (originalId, tag) -> {
            int damage = tag.getInteger("Damage");
            if (damage >= 32000) return false;
            OrePrefixes prefix = item.getOrePrefix(damage);
            Material material = item.getMaterial(damage);
            ItemStack cutover = MaterialParts.stack(prefix, material, 1);
            if (cutover == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
            tag.setShort("Damage", (short) cutover.getItemDamage());
            return true;
        });
    }

    private static void registerMetaItem99CutoverTransformer() {
        MetaGeneratedItem99 item = (MetaGeneratedItem99) GameRegistry.findItem("gregtech", "gt.metaitem.99");
        addItemTransformer("gregtech:gt.metaitem.99", (originalId, tag) -> {
            int damage = tag.getInteger("Damage");
            OrePrefixes prefix = item.getOrePrefix(damage);
            Material material = item.getMaterial(damage);
            ItemStack cutover = MaterialParts.stack(prefix, material, 1);
            if (cutover == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
            tag.setShort("Damage", (short) cutover.getItemDamage());
            return true;
        });
    }

    private void registerIC2BlocksTransformer() {
        // These are used to convert ic2 blocks to their new counterparts.
        // I.e. Reinforced glass, iron fences, etc.

        addBlockReplacement("IC2:blockAlloyGlass", GregTechAPI.sBlockGlass1, 10);
        addBlockReplacement("IC2:blockRubber", ItemList.PadBouncy.getBlock(), 0);
        addBlockReplacement("IC2:blockAlloy", ItemList.Block_ReinforcedConcrete.getBlock(), 13);
        addBlockReplacement("IC2:blockFenceIron", Casings.IronFence.getBlock(), 0);
    }

    private void registerFrameboxTransformers() {
        // These are used to convert old TileEntity frame boxes into the new system
        // that does not use TEs by default

        TileEntityReplacementManager.tileEntityTransformer("BaseMetaPipeEntity", (tag, world, chunk) -> {
            // Read the MTE ID from the NBT data and try to figure out if this is a frame box
            int id = tag.getInteger("mID");
            // Legacy per-material framebox IDs start at 4096; the offset is the index in the material list
            int indexInMaterialList = id - 4096;
            Material material = LegacyMaterialIDIndex.get(indexInMaterialList);
            if (material == null) {
                // Do not modify this TE, so return null
                return null;
            }
            FrameShapeBlock frameBlock = (FrameShapeBlock) MaterialLibAPI.getBlock(TEBlockShapes.frameGt);

            // If this frame has a cover on it, we need to keep its TileEntity, rewritten to the single
            // material-agnostic frame MTE the shape block binds
            if (tag.hasKey(GTValues.NBT.COVERS)) {
                return new BlockInfo(frameBlock, material.getIndex(), coveredFrameTag -> {
                    coveredFrameTag.setInteger("mID", frameBlock.getMteId());
                    return coveredFrameTag;
                });
            }

            // If this frame has no covers, simply return a block and delete the TileEntity
            return new BlockInfo(frameBlock, material.getIndex());
        });

        addItemTransformer("gregtech:gt.blockmachines", (originalId, tag) -> {
            // Get item meta id and see if this is a frame box, this works pretty much identically to the TE transformer
            int id = tag.getInteger("Damage");
            int indexInMaterialList = id - 4096;
            // Not a frame box if the material for this id does not have a frame box associated with it.
            // Apparently the DEFC ID overlaps with the material ID for a Bastnasite frame box for example
            if (!LegacyMetalItemsTable.contains(indexInMaterialList)) {
                return false;
            }
            Material material = LegacyMaterialIDIndex.get(indexInMaterialList);
            if (material == null) {
                return false;
            }
            Item frameItem = Item.getItemFromBlock(MaterialLibAPI.getBlock(TEBlockShapes.frameGt));
            int itemId = Item.getIdFromItem(frameItem);
            // Change this item into the correct frame item (make sure to keep amount)
            tag.setInteger("id", itemId);
            tag.setInteger("Damage", material.getIndex());
            return true;
        });
    }

    // TODO: Remove this and bio and breakthrough circuits once 2.8 is released.
    private void registerProgrammedCircuitTransformers() {
        addItemReplacement(
            "miscutils:item.BioRecipeSelector",
            GameRegistry.findItem(Mods.GregTech, "gt.integrated_circuit"),
            true);
        addItemReplacement(
            "miscutils:item.T3RecipeSelector",
            GameRegistry.findItem(Mods.GregTech, "gt.integrated_circuit"),
            true);
    }

    private void registerPotassiumHydroxideTransformer() {
        // For players updating from dailies
        addItemReplacement(
            "dreamcraft:PotassiumHydroxideDust",
            MaterialLibAPI.getStack(Materials.PotassiumHydroxideGT5U, Shapes.dust, 1),
            true);
        // For players updating directly from 2.8.4 or before
        addItemReplacement(
            "dreamcraft:item.PotassiumHydroxideDust",
            MaterialLibAPI.getStack(Materials.PotassiumHydroxideGT5U, Shapes.dust, 1),
            true); // FML Warning suppression in coremod
    }

    private void registerPTMEGTransformers() {
        removeWerkstoffMetaGeneratedItems();
        removeWerkstoffItems("bartworks:bw.werkstoffblocks.01");
        removeWerkstoffTileEntities();
    }

    private static void removeWerkstoffMetaGeneratedItems() {
        String placeholderName = ___placeholder___.getName();
        Arrays.stream(OrePrefixes.VALUES)
            .map(OrePrefixes::getName)
            .filter(prefix -> !placeholderName.equals(prefix))
            .map(prefix -> String.format("bartworks:gt.bwMetaGenerated%s", prefix))
            .forEach(PosteaTransformers::removeWerkstoffItems);
    }

    private static void removeWerkstoffItems(String originalId) {
        for (int removedWerkstoff : WERKSTOFFS_REMOVED_IN_2_9) {
            addItemReplacement(originalId, removedWerkstoff, Item.getItemFromBlock(Blocks.dirt), 0);
        }
    }

    private static void removeWerkstoffTileEntities() {
        TileEntityReplacementManager.tileEntityTransformer(
            "bw.werkstoffblockTE",
            (nbt, world, chunk) -> isWerkstoffRemoved(nbt.getShort("m")) ? new BlockInfo(Blocks.dirt, 0) : null);
    }

    private static boolean isWerkstoffRemoved(short meta) {
        for (int j : WERKSTOFFS_REMOVED_IN_2_9) {
            if (j == meta) {
                return true;
            }
        }
        return false;
    }

    private static int tieredGlassBlockId = -1;
    private static int extraGlassBlockId = -1;
    private static int tieredGlassItemId = -1;
    private static int extraGlassItemId = -1;

    private static void registerBorosilicateGlassTransformers() {
        ItemStackReplacementManager.registerIDResolver("bartworks:BW_TieredGlass", i -> tieredGlassItemId = i);
        ItemStackReplacementManager.registerIDResolver("bartworks:BW_ExtraGlass", i -> extraGlassItemId = i);
        BlockReplacementManager.registerIDResolver("bartworks:BW_TieredGlass", i -> tieredGlassBlockId = i);
        BlockReplacementManager.registerIDResolver("bartworks:BW_ExtraGlass", i -> extraGlassBlockId = i);

        // Block replacements
        addBlockTransformer("bartworks:BW_GlasBlocks", info -> {
            // Normal through osmium glass metadata unchanged
            if (info.metadata >= 0 && info.metadata <= 5) {
                info.blockID = tieredGlassBlockId;
            }

            // Old colored glass transformed into non-colored glass
            if (info.metadata >= 6 && info.metadata <= 11) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 0;
            }

            // Thorium-Yttrium Glass moved to its new place
            if (info.metadata == 12) {
                info.blockID = extraGlassBlockId;
                info.metadata = 0;
            }

            // Old neutronium glass moved to new neutronium glass
            if (info.metadata == 13) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 6;
            }

            // Old cosmic glass turned into new infinity glass
            if (info.metadata == 14) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 7;
            }

            // Old infinity glass turned into new transcendent glass
            if (info.metadata == 15) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 8;
            }

            return true;
        });

        addBlockTransformer("bartworks:BW_GlasBlocks2", info -> {
            // Turn old transcendent glass into new hexanite glass
            if (info.metadata == 0) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 9;
                return true;
            }
            return false;
        });

        // Item replacements
        addItemTransformer("bartworks:BW_GlasBlocks", (name, nbt) -> {
            int metadata = nbt.getShort("Damage");

            // Normal through osmium glass metadata unchanged
            if (metadata >= 0 && metadata <= 5) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
            }

            // Old colored glass transformed into non-colored glass
            if (metadata >= 6 && metadata <= 11) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 0);
            }

            // Thorium-Yttrium Glass moved to its new place
            if (metadata == 12) {
                IDExtenderCompat.setItemStackID(nbt, extraGlassItemId);
                nbt.setShort("Damage", (short) 0);
            }

            // Old neutronium glass moved to new neutronium glass
            if (metadata == 13) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 6);
            }

            // Old cosmic glass turned into new infinity glass
            if (metadata == 14) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 7);
            }

            // Old infinity glass turned into new transcendent glass
            if (metadata == 15) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 8);
            }

            return true;
        });

        addItemTransformer("bartworks:BW_GlasBlocks2", (name, nbt) -> {
            // Turn old transcendent glass into new hexanite glass
            if (nbt.getShort("Damage") == 0) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 9);
                return true;
            }
            return false;
        });
    }
}
