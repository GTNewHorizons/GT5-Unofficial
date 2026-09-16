package gregtech.loaders.postload;

import static gregtech.api.enums.OrePrefixes.___placeholder___;

import java.util.Arrays;
import java.util.function.Supplier;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.items.tools.GTToolItems;
import gregtech.common.items.tools.ToolItemBase;
import gregtech.common.items.tools.ToolWrenchElectricItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

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
        registerBartworksLabPartTransformer();
        registerStandaloneToolTransformers();
        registerRemovedToolTransformers();
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
            Item frameItem = Item.getItemFromBlock(GregTechAPI.sBlockFrames);
            int itemId = Item.getIdFromItem(frameItem);
            // Change this item into the correct frame item (make sure to keep amount)
            tag.setInteger("id", itemId);
            tag.setInteger("Damage", indexInMaterialList);
            return true;
        });
    }

    // TODO: Remove this and bio and breakthrough circuits once 2.8 is released.
    private void registerProgrammedCircuitTransformers() {
        ItemStackReplacementManager
            .addSimpleReplacement("miscutils:item.BioRecipeSelector", ItemList.Circuit_Integrated.getItem(), true);
        ItemStackReplacementManager
            .addSimpleReplacement("miscutils:item.T3RecipeSelector", ItemList.Circuit_Integrated.getItem(), true);
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

    /* ---------- REMOVED TOOLS ---------- */

    /**
     * The GT++ tool item held nothing but the Angle Grinder and the Automatic Snips, both long deprecated and now
     * deleted outright. Nothing replaces them, so any that are still lying around in old saves are dropped: without
     * this, removing the item would greet the player with Forge's missing-registry-entry screen on world load.
     */
    private static void registerRemovedToolTransformers() {
        ItemStackReplacementManager.ignoreMissingMapping("gregtech:gt.plusplus.metatool.01");
    }

    /* ---------- STANDALONE TOOL SPLIT ---------- */

    /**
     * One tool type that has moved off {@code MetaGeneratedTool01} onto its own item.
     * <p/>
     * {@code oldMeta} is the metadata the type occupied on the old item. Each type held an even id for the charged
     * item and the following odd id for the discharged one, so a saved stack is matched on its even id. Those ids stay
     * reserved in {@code IDMetaTool01} and must never be reused, or old saves would migrate into the wrong tool.
     * <p/>
     * {@code runtimeId} is filled in by Postea: the numeric item id is not stable between installs, so it has to be
     * resolved by registry name rather than assumed.
     */
    private static final class MigratedTool {

        private final int oldMeta;
        private final String newRegistryName;
        private final Supplier<ToolItemBase> newItem;
        private int runtimeId = -1;

        private MigratedTool(int oldMeta, String newRegistryName, Supplier<ToolItemBase> newItem) {
            this.oldMeta = oldMeta;
            this.newRegistryName = newRegistryName;
            this.newItem = newItem;
        }
    }

    private static final MigratedTool[] MIGRATED_TOOLS = {
        new MigratedTool(14, "gregtech:gt.tool.soft_mallet", () -> GTToolItems.SOFT_MALLET),
        new MigratedTool(16, "gregtech:gt.tool.wrench", () -> GTToolItems.WRENCH),
        new MigratedTool(120, "gregtech:gt.tool.wrench_lv", () -> GTToolItems.WRENCH_LV),
        new MigratedTool(122, "gregtech:gt.tool.wrench_mv", () -> GTToolItems.WRENCH_MV),
        new MigratedTool(124, "gregtech:gt.tool.wrench_hv", () -> GTToolItems.WRENCH_HV), };

    private static final Int2ObjectMap<MigratedTool> MIGRATED_TOOLS_BY_OLD_META = new Int2ObjectOpenHashMap<>();

    /**
     * Converts tools saved under the old scheme -- one {@code gt.metatool.01} item whose metadata was the tool type
     * and whose material was a string in NBT -- into the standalone tool items, whose metadata is the material. Tool
     * types that have not moved yet are left untouched.
     */
    private static void registerStandaloneToolTransformers() {
        for (MigratedTool tool : MIGRATED_TOOLS) {
            MIGRATED_TOOLS_BY_OLD_META.put(tool.oldMeta, tool);
            ItemStackReplacementManager.registerIDResolver(tool.newRegistryName, i -> tool.runtimeId = i);
        }

        ItemStackReplacementManager
            .addTransformationHandler("gregtech:gt.metatool.01", (name, nbt) -> convertTool(nbt));
    }

    private static boolean convertTool(NBTTagCompound nbt) {
        final int oldMeta = nbt.getShort("Damage");
        final MigratedTool tool = MIGRATED_TOOLS_BY_OLD_META.get(oldMeta - (oldMeta % 2));
        // Some other tool type, or an item that was never a tool. Leave it alone.
        if (tool == null) return false;

        final ToolItemBase newItem = tool.newItem.get();
        // The new item failed to register, or Postea could not resolve its runtime id. Rewriting the stack now would
        // point it at nothing, so leave it as it is rather than destroying it.
        if (newItem == null || tool.runtimeId < 0) return false;

        final Materials material = Materials.getRealMaterial(
            MetaToolStackMigration.readToolStats(nbt)
                .getString("PrimaryMaterial"));
        final int newMeta = ToolItemBase.getMaterialMeta(material);
        if (newMeta < 0) return false;

        final boolean electric = newItem instanceof ToolWrenchElectricItem;
        final long defaultMaxCharge = electric ? ((ToolWrenchElectricItem) newItem).getDefaultMaxCharge() : 0L;
        MetaToolStackMigration.rewriteToolStack(nbt, newMeta, electric, defaultMaxCharge);
        IDExtenderCompat.setItemStackID(nbt, tool.runtimeId);
        return true;
    }
}
