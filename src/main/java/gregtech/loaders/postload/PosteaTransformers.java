package gregtech.loaders.postload;

import static gregtech.api.enums.OrePrefixes.___placeholder___;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.BlockAccessCompat;
import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.blocks.BlockFrameBox;

public class PosteaTransformers implements Runnable {

    // Offset from WerkstoffMaterialPool
    private static final int OFFSET_ID_3 = 11_300;
    private static final int[] WERKSTOFFS_REMOVED_IN_2_9 = new int[] { OFFSET_ID_3, OFFSET_ID_3 + 2, OFFSET_ID_3 + 6,
        OFFSET_ID_3 + 7, OFFSET_ID_3 + 8, OFFSET_ID_3 + 11, OFFSET_ID_3 + 12 };

    @Override
    public void run() {
        registerFrameboxTransformers();
        registerPotassiumHydroxideTransformer();
        registerPTMEGTransformers();
        registerBorosilicateGlassTransformers();
        registerIC2BlocksTransformer();
        registerIC2ItemTransformers();
        registerBartworksLabPartTransformer();
        registerExtruderShapeTransformers();
        registerBartworksMaterialBlockTransformers();
    }

    private static NBTTagCompound passthrough(NBTTagCompound tag) {
        return tag;
    }

    private void registerBartworksMaterialBlockTransformers() {
        registerBartworksMaterialBlockTransformer("bw.werkstoffblockTE", WerkstoffLoader.BWBlocks);
        registerBartworksMaterialBlockTransformer("bw.werkstoffblockcasingTE", WerkstoffLoader.BWBlockCasings);
        registerBartworksMaterialBlockTransformer(
            "bw.werkstoffblockscasingadvancedTE",
            WerkstoffLoader.BWBlockCasingsAdvanced);
    }

    private void registerBartworksMaterialBlockTransformer(String tileEntityId, Block block) {
        AtomicInteger originalBlockId = new AtomicInteger(-1);
        BlockReplacementManager.registerIDResolver(Block.blockRegistry.getNameForObject(block), originalBlockId::set);
        TileEntityReplacementManager.tileEntityTransformer(tileEntityId, (tag, world, chunk) -> {
            if (BlockAccessCompat.getBlockIDAtTE(tag, chunk) != originalBlockId.get()) return null;
            short materialId = tag.getShort("m");
            if (isWerkstoffRemoved(materialId)) return null;
            return new BlockInfo(block, materialId > 0 ? materialId : BlockAccessCompat.getBlockMetaAtTE(tag, chunk));
        });
    }

    private void registerIC2ItemTransformers() {
        // These are used to convert ic2 items to their new counterparts.
        // I.e. Uranium235, Uranium238, Plutonium, ect.

        ItemStackReplacementManager.addSimpleReplacement(
            "IC2:itemUran238",
            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Uranium, 1L),
            true);

        ItemStackReplacementManager.addSimpleReplacement(
            "IC2:itemUran235",
            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Uranium235, 1L),
            true);

        ItemStackReplacementManager.addSimpleReplacement(
            "IC2:itemUran235small",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Uranium235, 1L),
            true);

        ItemStackReplacementManager.addSimpleReplacement(
            "IC2:itemPlutonium",
            GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Plutonium, 1L),
            true);

        ItemStackReplacementManager.addSimpleReplacement(
            "IC2:itemPlutoniumsmall",
            GTOreDictUnificator.get(OrePrefixes.nugget, Materials.Plutonium, 1L),
            true);

        ItemStackReplacementManager.addSimpleReplacement("IC2:itemFuelPlantBall", ItemList.IC2_Plantball.get(1));
        ItemStackReplacementManager.addSimpleReplacement("IC2:itemBiochaff", ItemList.IC2_BioChaff.get(1));
        ItemStackReplacementManager.addSimpleReplacement("IC2:itemDynamite", ItemList.Dynamite.get(1));

        // Cells
        ItemStackReplacementManager.addSimpleReplacement("IC2:itemCellEmpty", 6, Materials.Biomass.getCells(1));
        ItemStackReplacementManager.addSimpleReplacement("IC2:itemCellEmpty", 7, Materials.Biogas.getCells(1));
        ItemStackReplacementManager.addSimpleReplacement("IC2:itemCellEmpty", 12, Materials.DistilledWater.getCells(1));
    }

    private void registerIC2BlocksTransformer() {
        // These are used to convert ic2 blocks to their new counterparts.
        // I.e. Reinforced glass, iron fences, etc.

        BlockReplacementManager.addSimpleReplacement("IC2:blockAlloyGlass", GregTechAPI.sBlockGlass1, 10);
        BlockReplacementManager.addSimpleReplacement("IC2:blockRubber", ItemList.PadBouncy.getBlock(), 0);
        BlockReplacementManager
            .addSimpleReplacement("IC2:blockAlloy", ItemList.Block_ReinforcedConcrete.getBlock(), 13);
        BlockReplacementManager.addSimpleReplacement("IC2:blockFenceIron", Casings.IronFence.getBlock(), 0);
        BlockReplacementManager.addSimpleReplacement("IC2:blockMachine", 0, ItemList.Casing_LV.getBlock(), 1);
        BlockReplacementManager.addSimpleReplacement("IC2:blockMetal", 0, GregTechAPI.sBlockMetal2, 7);
        BlockReplacementManager.addSimpleReplacement("IC2:blockMetal", 1, GregTechAPI.sBlockMetal7, 7);
        BlockReplacementManager.addSimpleReplacement("IC2:blockMetal", 2, GregTechAPI.sBlockMetal2, 0);
        BlockReplacementManager.addSimpleReplacement("IC2:blockMetal", 3, GregTechAPI.sBlockMetal7, 14);
        BlockReplacementManager.addSimpleReplacement("IC2:blockMetal", 4, GregTechAPI.sBlockMetal4, 2);
        BlockReplacementManager.addSimpleReplacement("IC2:blockMetal", 5, GregTechAPI.sBlockMetal6, 13);
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
            Item frameItem = Item.getItemFromBlock(GregTechAPI.sBlockFrames);
            int itemId = Item.getIdFromItem(frameItem);
            // Change this item into the correct frame item (make sure to keep amount)
            tag.setInteger("id", itemId);
            tag.setInteger("Damage", indexInMaterialList);
            return true;
        });
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

    // sounds stupid, but this needs to persist, so renamed items don't break janky bartworks & recipe map code
    private static void registerBartworksLabPartTransformer() {
        ItemStackReplacementManager.addTransformationHandler("bartworks:BioLabParts", (name, nbt) -> {
            // ensure it has the extra tag
            if (nbt.hasKey("tag")) {
                var tag = nbt.getCompoundTag("tag");
                // skip special NEI recipe item for BioLab Clonal Cellular Synthesis
                if (tag.hasKey("NEI")) return false;
                tag.func_150296_c()
                    .removeIf(key -> !key.equals("Name"));
            }
            return true;
        });
    }

    // Extruder shape unification (#8771)
    private static void registerExtruderShapeTransformers() {
        ItemStackReplacementManager
            .addSimpleReplacement("dreamcraft:ExtruderShapeBoat", ItemList.Shape_Extruder_Boat.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("miscutils:MU-metaitem.01", 32040, ItemList.Shape_Extruder_Shaft.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("miscutils:item.BasicAgrichemItem", 22, ItemList.Shape_Extruder_Pellet.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32350, ItemList.Shape_Extruder_Plate.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32351, ItemList.Shape_Extruder_Rod.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32352, ItemList.Shape_Extruder_Bolt.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32353, ItemList.Shape_Extruder_Ring.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32354, ItemList.Shape_Extruder_Cell.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32355, ItemList.Shape_Extruder_Ingot.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32357, ItemList.Shape_Extruder_Casing.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32358, ItemList.Shape_Extruder_Pipe_Tiny.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32359, ItemList.Shape_Extruder_Pipe_Small.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32360, ItemList.Shape_Extruder_Pipe_Medium.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32361, ItemList.Shape_Extruder_Pipe_Large.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32362, ItemList.Shape_Extruder_Pipe_Huge.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32363, ItemList.Shape_Extruder_Block.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32364, ItemList.Shape_Extruder_Sword.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32365, ItemList.Shape_Extruder_Pickaxe.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32366, ItemList.Shape_Extruder_Shovel.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32367, ItemList.Shape_Extruder_Axe.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32368, ItemList.Shape_Extruder_Hoe.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32369, ItemList.Shape_Extruder_Hammer.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32370, ItemList.Shape_Extruder_File.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32371, ItemList.Shape_Extruder_Saw.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32372, ItemList.Shape_Extruder_Gear.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32373, ItemList.Shape_Extruder_Bottle.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32374, ItemList.Shape_Extruder_Rotor.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32375, ItemList.Shape_Extruder_Small_Gear.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32376, ItemList.Shape_Extruder_Turbine_Blade.get(1));
        ItemStackReplacementManager
            .addSimpleReplacement("gregtech:gt.metaitem.01", 32377, ItemList.Shape_Extruder_Drill.get(1));
    }
}
