package gregtech.loaders.postload;

import static gregtech.api.enums.OrePrefixes.___placeholder___;

import java.util.Arrays;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.MetaGeneratedItemX32;
import gregtech.api.material.MU;
import gregtech.common.blocks.BlockFrameBox;
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

    /// Migrates saved [MetaGeneratedItemX32] stacks (`gt.metaitem.01/02/03`, damage < 32000) whose prefix
    /// cut over to a MaterialLib shape (see [MU]) into the equivalent MaterialLib stack. Hand-listed custom
    /// parts (damage >= 32000) and prefixes that did not cut over (e.g. `cell`, `cellPlasma`) pass through
    /// unchanged.
    private void registerMaterialLibCutoverTransformers() {
        registerMetaItemCutoverTransformer("gt.metaitem.01");
        registerMetaItemCutoverTransformer("gt.metaitem.02");
        registerMetaItemCutoverTransformer("gt.metaitem.03");
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
