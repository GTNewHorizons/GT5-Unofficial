package gregtech.loaders.postload;

import static gregtech.api.enums.OrePrefixes.___placeholder___;

import java.util.Arrays;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.BWMetaGeneratedItems;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.material.MU;
import gregtech.api.util.GTLog;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.BlockMetal;
import gregtech.common.items.MetaGeneratedItem99;
import gregtech.loaders.postload.GtppItemCutoverTable.Entry;
import gtPlusPlus.core.material.MaterialReconstruction;
import vexatos.tgregworks.reference.Mods;

public class PosteaTransformers implements Runnable {

    // Offset from WerkstoffMaterialPool
    private static final int OFFSET_ID_3 = 11_300;
    private static final int[] WERKSTOFFS_REMOVED_IN_2_9 = new int[] { OFFSET_ID_3, OFFSET_ID_3 + 2, OFFSET_ID_3 + 6,
        OFFSET_ID_3 + 7, OFFSET_ID_3 + 8, OFFSET_ID_3 + 11, OFFSET_ID_3 + 12 };

    @Override
    public void run() {
        registerFrameboxTransformers();
        registerProgrammedCircuitTransformers();
        registerPotassiumHydroxideTransformer();
        registerPTMEGTransformers();
        registerBorosilicateGlassTransformers();
        registerIC2BlocksTransformer();
        registerMaterialLibCutoverTransformers();
    }

    /// Migrates saved [MetaGeneratedItemX32] stacks (`gt.metaitem.01/02/03`, damage < 32000) and
    /// [MetaGeneratedItem99] stacks (`gt.metaitem.99`, cell molten/cracked prefixes) whose prefix cut over to
    /// a MaterialLib shape (see [MU]) into the equivalent MaterialLib stack. Hand-listed custom parts
    /// (damage >= 32000 on `metaitem.01/02/03`) and prefixes that have not cut over pass through unchanged.
    private void registerMaterialLibCutoverTransformers() {
        registerMetaItemCutoverTransformer("gt.metaitem.01");
        registerMetaItemCutoverTransformer("gt.metaitem.02");
        registerMetaItemCutoverTransformer("gt.metaitem.03");
        registerMetaItem99CutoverTransformer();
        registerStorageBlockCutoverTransformers();
        registerWerkstoffItemCutoverTransformers();
        registerGtppItemCutoverTransformers();
        registerGtppCarryoverCellTransformers();
        registerGtppOreCutoverTransformers();
    }

    /// Migrates saved legacy `BlockBaseOre` placed/inventory stacks into the equivalent MaterialLib
    /// [gregtech.api.enums.materials2.Materials2OreShapes#shapeOre] stack, resolved through
    /// [GtppOreCutoverTable]'s (unlocalized name, registry name) rows the same way
    /// [gregtech.common.ores.GTPPOreAdapter#getBlock] resolves it live. Unlike every gtpp part/block row
    /// (one distinct registered instance per material, no meta multiplexing), so a single item + block
    /// transformer pair per row is enough, mirroring [#registerGtppItemCutoverTransformers]' `block`-row
    /// handling. A material that never gained `shapeOre` membership resolves null and is left on its legacy
    /// slot, same as every other cutover table here.
    private static void registerGtppOreCutoverTransformers() {
        for (GtppOreCutoverTable.Entry entry : GtppOreCutoverTable.ENTRIES) {
            ItemStackReplacementManager.addTransformationHandler(entry.registryName(), (originalId, tag) -> {
                ItemStack cutover = resolveGtppOreCutoverStack(entry);
                if (cutover == null) return false;
                IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
                tag.setShort("Damage", (short) cutover.getItemDamage());
                return true;
            });

            BlockReplacementManager.addTransformationHandler(entry.registryName(), info -> {
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
        com.ruling_0.materiallib.api.Material ml = MaterialReconstruction.materialLibOf(entry.unlocalizedName());
        return MU.stack(OrePrefixes.ore, ml, 1);
    }

    /// Migrates saved gtPlusPlus per-material part stacks (`miscutils:item*`/`miscutils:block*`, one distinct
    /// registered item/block per (material, prefix), always damage 0) into the equivalent MaterialLib stack,
    /// resolved through [GtppItemCutoverTable]'s pinned (prefix, material, registry name) rows -- the
    /// gtPlusPlus counterpart of [#registerWerkstoffItemCutoverTransformers], differing only in that each row
    /// is its own registered item/block rather than a damage variant of a shared meta-item, so no damage
    /// read/branch is needed. `cell`/`cellPlasma` rows resolve through [MaterialReconstruction#cellStack]
    /// instead of plain [MU#stack], since a row's material may have claimed `shapeCellMolten` rather than
    /// `shapeCell` -- see that method's javadoc. `frameGt` is out of the table and migrates separately
    /// (deferred). `block` rows additionally get a [BlockReplacementManager] handler for placed instances,
    /// since a storage block (unlike every other gtpp part) is placeable, and are skipped entirely (both the
    /// item and block handler return false/null, leaving the legacy slot canonical) for the small
    /// [MaterialReconstruction#isBlockCutOver] exclusion list -- the table itself has no way to know about that
    /// per-material exception since it is generated purely from the dump.
    private static void registerGtppItemCutoverTransformers() {
        for (Entry entry : GtppItemCutoverTable.ENTRIES) {
            ItemStackReplacementManager.addTransformationHandler(entry.registryName(), (originalId, tag) -> {
                ItemStack cutover = resolveGtppCutoverStack(entry);
                if (cutover == null) return false;
                IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
                tag.setShort("Damage", (short) cutover.getItemDamage());
                return true;
            });
            if (entry.prefix() == OrePrefixes.block) {
                BlockReplacementManager.addTransformationHandler(entry.registryName(), info -> {
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
        if (entry.prefix() == OrePrefixes.block && !MaterialReconstruction.isBlockCutOver(entry.materialName())) {
            return null;
        }
        if (entry.prefix() == OrePrefixes.cell || entry.prefix() == OrePrefixes.cellPlasma) {
            return MaterialReconstruction.cellStack(entry.materialName(), entry.prefix(), 1);
        }
        com.ruling_0.materiallib.api.Material material = MaterialLibAPI.getMaterial("gregtech", entry.materialName());
        return MU.stack(entry.prefix(), material, 1);
    }

    /// The five materials whose legacy `cell` item was `miscutils:itemCell<Name>` (the same naming convention
    /// as every other gtpp-owned cell -- see `scripts/mu/gen_gtpp_item_transformers.py`) at some point in
    /// history, but had already resolved to a gregtech-owned `materiallib:cell` stack by the time the stage-11
    /// dump ran (these five are also plain gregtech elements whose own fluid/cell cutover -- stage 06 -- claims
    /// the oredict `cell<Name>` slot before gtpp's `Material` construction ever runs, so their dump never
    /// captured the miscutils registry name at all). [GtppItemCutoverTable] is generated purely from that dump
    /// and so cannot include a row for them; they are migrated by hand instead, to the same fallback
    /// [MaterialReconstruction#cellStack] every other gtpp cell resolves through.
    private static void registerGtppCarryoverCellTransformers() {
        registerGtppCarryoverCellTransformer("Iodine");
        registerGtppCarryoverCellTransformer("ThoriumTetrafluoride");
        registerGtppCarryoverCellTransformer("Xenon");
        registerGtppCarryoverCellTransformer("Neon");
        registerGtppCarryoverCellTransformer("Krypton");
    }

    private static void registerGtppCarryoverCellTransformer(String materialName) {
        ItemStack cutover = MaterialReconstruction.cellStack(materialName, OrePrefixes.cell, 1);
        if (cutover == null) {
            throw new IllegalStateException("No MaterialLib cell stack for carryover material " + materialName);
        }
        ItemStackReplacementManager.addSimpleReplacement("miscutils:itemCell" + materialName, cutover);
    }

    /// Migrates saved bartworks werkstoff item stacks (`bartworks:gt.bwMetaGenerated<prefix>`, damage =
    /// werkstoff id) into the equivalent MaterialLib stack, resolved through the werkstoff's bridge material
    /// exactly like the live item path (`WerkstoffLoader#getCorrespondingItemStackUnsafe`). Damages of
    /// werkstoffe unknown to MaterialLib (a third-party WerkstoffAdder's) pass through unchanged. Ore/small ore
    /// migrate through [BWOreAdapter] instead (block-kind, no `bw.bwMetaGenerated<prefix>` item exists for
    /// them); storage blocks migrate through [#registerWerkstoffBlockCutoverTransformer]. The casing slots
    /// (`blockCasing`/`blockCasingAdvanced`) stay legacy-canonical for now: multiblock structure matchers
    /// reference the legacy casing blocks by identity, so their cutover is a coordinated block+structure flip.
    private static void registerWerkstoffItemCutoverTransformers() {
        int count = 0;
        for (Map.Entry<OrePrefixes, BWMetaGeneratedItems> entry : WerkstoffLoader.items.entrySet()) {
            OrePrefixes prefix = entry.getKey();
            UniqueIdentifier id = GameRegistry.findUniqueIdentifierFor(entry.getValue());
            ItemStackReplacementManager.addTransformationHandler(id.modId + ":" + id.name, (originalId, tag) -> {
                int damage = tag.getInteger("Damage");
                Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) damage);
                if (werkstoff == null) return false;
                ItemStack cutover = MU.stack(prefix, werkstoff.getBridgeMaterial(), 1);
                if (cutover == null) return false;
                IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
                tag.setShort("Damage", (short) cutover.getItemDamage());
                return true;
            });
            count++;
        }
        GTLog.out.println("PosteaTransformers: registered werkstoff item transformers for " + count + " legacy items");
    }

    /// Migrates saved placed blocks and item stacks of a cut-over material's legacy storage-block slot (see
    /// [BlockMetal], [MU]) into the equivalent MaterialLib block stack. `addSimpleReplacement`'s block+meta
    /// overload registers a matching item replacement automatically, so no separate item-side call is needed.
    /// Materials that did not cut over (see `gen_materials.py`'s `BLOCK_CUTOVER_EXCLUDED`) are skipped: their
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
    /// werkstoff id) into the equivalent MaterialLib block stack, resolved through the werkstoff's bridge
    /// material exactly like the live item path (`WerkstoffLoader#getCorrespondingItemStackUnsafe`). Third-party
    /// werkstoffe unknown to MaterialLib pass through unchanged, leaving the legacy slot canonical for them.
    /// `bw.werkstoffblockTE` already has a handler registered by [#removeWerkstoffTileEntities]; Postea tries
    /// each registered handler in turn until one returns non-null, so the two coexist without conflict.
    private static void registerWerkstoffBlockCutoverTransformer(String teId, String itemId, OrePrefixes prefix) {
        TileEntityReplacementManager.tileEntityTransformer(teId, (tag, world, chunk) -> {
            Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get(tag.getShort("m"));
            if (werkstoff == null) return null;
            ItemStack cutover = MU.stack(prefix, werkstoff.getBridgeMaterial(), 1);
            if (cutover == null) return null;
            return new BlockInfo(Block.getBlockFromItem(cutover.getItem()), cutover.getItemDamage());
        });

        ItemStackReplacementManager.addTransformationHandler(itemId, (originalId, tag) -> {
            Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) tag.getInteger("Damage"));
            if (werkstoff == null) return false;
            ItemStack cutover = MU.stack(prefix, werkstoff.getBridgeMaterial(), 1);
            if (cutover == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
            tag.setShort("Damage", (short) cutover.getItemDamage());
            return true;
        });
    }

    private static void registerStorageBlockCutoverTransformer(String originalId, Block legacyBlock) {
        BlockMetal metal = (BlockMetal) legacyBlock;
        for (int meta = 0; meta < metal.mMats.length; meta++) {
            ItemStack cutover = MU.stack(OrePrefixes.block, metal.mMats[meta], 1);
            if (cutover == null) continue;
            Block mlBlock = Block.getBlockFromItem(cutover.getItem());
            BlockReplacementManager.addSimpleReplacement(originalId, meta, mlBlock, cutover.getItemDamage());
        }
    }

    private static void registerMetaItemCutoverTransformer(String itemName) {
        MetaGeneratedItemX32 item = (MetaGeneratedItemX32) GameRegistry.findItem("gregtech", itemName);
        ItemStackReplacementManager.addTransformationHandler("gregtech:" + itemName, (originalId, tag) -> {
            int damage = tag.getInteger("Damage");
            if (damage >= 32000) return false;
            OrePrefixes prefix = item.getOrePrefix(damage);
            Materials material = item.getMaterial(damage);
            ItemStack cutover = MU.stack(prefix, material, 1);
            if (cutover == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
            tag.setShort("Damage", (short) cutover.getItemDamage());
            return true;
        });
    }

    private static void registerMetaItem99CutoverTransformer() {
        MetaGeneratedItem99 item = (MetaGeneratedItem99) GameRegistry.findItem("gregtech", "gt.metaitem.99");
        ItemStackReplacementManager.addTransformationHandler("gregtech:gt.metaitem.99", (originalId, tag) -> {
            int damage = tag.getInteger("Damage");
            OrePrefixes prefix = item.getOrePrefix(damage);
            Materials material = item.getMaterial(damage);
            ItemStack cutover = MU.stack(prefix, material, 1);
            if (cutover == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(cutover.getItem()));
            tag.setShort("Damage", (short) cutover.getItemDamage());
            return true;
        });
    }

    private static NBTTagCompound passthrough(NBTTagCompound tag) {
        return tag;
    }

    private void registerIC2BlocksTransformer() {
        // These are used to convert ic2 blocks to their new counterparts.
        // I.e. Reinforced glass, iron fences, etc.

        BlockReplacementManager.addSimpleReplacement("IC2:blockAlloyGlass", GregTechAPI.sBlockGlass1, 10);
        BlockReplacementManager.addSimpleReplacement("IC2:blockRubber", ItemList.PadBouncy.getBlock(), 0);
        BlockReplacementManager
            .addSimpleReplacement("IC2:blockAlloy", ItemList.Block_ReinforcedConcrete.getBlock(), 13);
        BlockReplacementManager.addSimpleReplacement("IC2:blockFenceIron", Casings.IronFence.getBlock(), 0);
    }

    private void registerFrameboxTransformers() {
        // These are used to convert old TileEntity frame boxes into the new system
        // that does not use TEs by default

        TileEntityReplacementManager.tileEntityTransformer("BaseMetaPipeEntity", (tag, world, chunk) -> {
            // Read the MTE ID from the NBT data and try to figure out if this is a frame box
            int id = tag.getInteger("mID");
            // Framebox IDs start at 4096
            int indexInMaterialList = id - 4096;
            // The offset from 4096 is the index in the material list, so if this is outside the valid range then this
            // is not a valid frame box, and we don't want to modify it
            if (indexInMaterialList < 0 || indexInMaterialList >= GregTechAPI.sGeneratedMaterials.length) {
                // Do not modify this TE, so return null
                return null;
            }

            // Now we know for sure that the tileentity is a frame box
            // If this frame has a cover on it, we need to keep it, but we still need to make sure the block
            // is the new frame block. We can make sure to keep the TE using a pass-through transformer.
            // This works because between the old and new frame systems, the TileEntity used for covered frames
            // is still the same
            if (tag.hasKey(GTValues.NBT.COVERS)) {
                return new BlockInfo(
                    GregTechAPI.sBlockFrames,
                    indexInMaterialList | BlockFrameBox.MTE_BIT,
                    PosteaTransformers::passthrough);
            }

            // If this frame has no covers, simply return a block and delete the TileEntity
            return new BlockInfo(GregTechAPI.sBlockFrames, indexInMaterialList);
        });

        ItemStackReplacementManager.addTransformationHandler("gregtech:gt.blockmachines", (originalId, tag) -> {
            // Get item meta id and see if this is a frame box, this works pretty much identically to the TE transformer
            int id = tag.getInteger("Damage");
            int indexInMaterialList = id - 4096;
            // Not a frame box
            if (indexInMaterialList < 0 || indexInMaterialList >= GregTechAPI.sGeneratedMaterials.length) {
                return false;
            }
            // Not a frame box if the material for this id does not have a frame box associated with it.
            // Apparently the DEFC ID overlaps with the material ID for a Bastnasite frame box for example
            if (!GregTechAPI.sGeneratedMaterials[indexInMaterialList].hasMetalItems()) {
                return false;
            }
            Item frameItem = GameRegistry.findItem(Mods.GregTech, "gt.blockframes");
            int itemId = Item.getIdFromItem(frameItem);
            // Change this item into the correct frame item (make sure to keep amount)
            tag.setInteger("id", itemId);
            tag.setInteger("Damage", indexInMaterialList);
            return true;
        });
    }

    // TODO: Remove this and bio and breakthrough circuits once 2.8 is released.
    private void registerProgrammedCircuitTransformers() {
        ItemStackReplacementManager.addSimpleReplacement(
            "miscutils:item.BioRecipeSelector",
            GameRegistry.findItem(Mods.GregTech, "gt.integrated_circuit"),
            true);
        ItemStackReplacementManager.addSimpleReplacement(
            "miscutils:item.T3RecipeSelector",
            GameRegistry.findItem(Mods.GregTech, "gt.integrated_circuit"),
            true);
    }

    private void registerPotassiumHydroxideTransformer() {
        // For players updating from dailies
        ItemStackReplacementManager
            .addSimpleReplacement("dreamcraft:PotassiumHydroxideDust", Materials.PotassiumHydroxide.getDust(1), true);
        // For players updating directly from 2.8.4 or before
        ItemStackReplacementManager.addSimpleReplacement(
            "dreamcraft:item.PotassiumHydroxideDust",
            Materials.PotassiumHydroxide.getDust(1),
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
            ItemStackReplacementManager
                .addSimpleReplacement(originalId, removedWerkstoff, Item.getItemFromBlock(Blocks.dirt), 0);
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
        BlockReplacementManager.addTransformationHandler("bartworks:BW_GlasBlocks", info -> {
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

        BlockReplacementManager.addTransformationHandler("bartworks:BW_GlasBlocks2", info -> {
            // Turn old transcendent glass into new hexanite glass
            if (info.metadata == 0) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 9;
                return true;
            }
            return false;
        });

        // Item replacements
        ItemStackReplacementManager.addTransformationHandler("bartworks:BW_GlasBlocks", (name, nbt) -> {
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

        ItemStackReplacementManager.addTransformationHandler("bartworks:BW_GlasBlocks2", (name, nbt) -> {
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
