package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.SuperSolarPanels;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.MetaTileEntityIDs;
import vexatos.tgregworks.reference.Mods;

public class PosteaTransformers implements Runnable {

    @Override
    public void run() {
        registerFrameboxTransformers();
        registerSolarPanelTransformers();
    }

    private static NBTTagCompound passthrough(NBTTagCompound tag) {
        return tag;
    }

    private void registerFrameboxTransformers() {
        // These are used to convert old TileEntity frame boxes into the new system
        // that does not use TEs by default

        TileEntityReplacementManager.tileEntityTransformer("BaseMetaPipeEntity", (tag, world) -> {
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
                return new BlockInfo(GregTechAPI.sBlockFrames, indexInMaterialList, PosteaTransformers::passthrough);
            }

            // If this frame has no covers, simply return a block and delete the TileEntity
            return new BlockInfo(GregTechAPI.sBlockFrames, indexInMaterialList);
        });

        ItemStackReplacementManager.addItemReplacement("gregtech:gt.blockmachines", (tag) -> {
            // Get item meta id and see if this is a frame box, this works pretty much identically to the TE transformer
            int id = tag.getInteger("Damage");
            int indexInMaterialList = id - 4096;
            // Not a frame box
            if (indexInMaterialList < 0 || indexInMaterialList >= GregTechAPI.sGeneratedMaterials.length) {
                return tag;
            }
            // Not a frame box if the material for this id does not have a frame box associated with it.
            // Apparently the DEFC ID overlaps with the material ID for a Bastnasite frame box for example
            if ((GregTechAPI.sGeneratedMaterials[indexInMaterialList].mTypes & 0x2) == 0) {
                return tag;
            }
            Item frameItem = GameRegistry.findItem(Mods.GregTech, "gt.blockframes");
            int itemId = Item.getIdFromItem(frameItem);
            // Change this item into the correct frame item (make sure to keep amount)
            tag.setInteger("id", itemId);
            tag.setInteger("Damage", indexInMaterialList);
            return tag;
        });
    }

    private void registerSolarPanelTransformers() {
        Function<NBTTagCompound, NBTTagCompound> nbtTransformer = nbt -> {
            NBTTagCompound newNbt = new NBTTagCompound();
            newNbt.setLong("mStoredEnergy", nbt.getInteger("storage"));
            return newNbt;
        };

        // Advanced Solar Panels
        TileEntityReplacementManager.tileEntityTransformer(
            "Advanced Solar Panel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_LV, nbtTransformer));
        TileEntityReplacementManager.tileEntityTransformer(
            "Hybrid Solar Panel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_MV, nbtTransformer));
        TileEntityReplacementManager.tileEntityTransformer(
            "Ultimate Hybrid Solar Panel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_HV, nbtTransformer));
        TileEntityReplacementManager.tileEntityTransformer(
            "Quantum Solar Panel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_EV, nbtTransformer));

        ItemStackReplacementManager.addItemReplacement(
            AdvancedSolarPanel.ID + ":BlockAdvSolarPanel",
            getMTEItemTransformer(tag -> switch (tag.getInteger("Damage")) {
            case 0 -> MetaTileEntityIDs.SOLAR_PANEL_LV;
            case 1 -> MetaTileEntityIDs.SOLAR_PANEL_MV;
            case 2 -> MetaTileEntityIDs.SOLAR_PANEL_HV;
            default -> MetaTileEntityIDs.SOLAR_PANEL_EV;
            }));

        // Super Solar Panels
        TileEntityReplacementManager.tileEntityTransformer(
            "SpectralSolarPanel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_IV, nbtTransformer));
        TileEntityReplacementManager.tileEntityTransformer(
            "SingularSolarPanel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_LuV, nbtTransformer));
        TileEntityReplacementManager.tileEntityTransformer(
            "AdminSolarPanel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_ZPM, nbtTransformer));
        TileEntityReplacementManager.tileEntityTransformer(
            "PhotonicSolarPanel",
            getMTETileTransformer(MetaTileEntityIDs.SOLAR_PANEL_UV, nbtTransformer));

        ItemStackReplacementManager.addItemReplacement(
            SuperSolarPanels.ID + ":SpectralSolarPanel",
            getMTEItemTransformer(MetaTileEntityIDs.SOLAR_PANEL_IV));
        ItemStackReplacementManager.addItemReplacement(
            SuperSolarPanels.ID + ":SingularSolarPanel",
            getMTEItemTransformer(MetaTileEntityIDs.SOLAR_PANEL_LuV));
        ItemStackReplacementManager.addItemReplacement(
            SuperSolarPanels.ID + ":AdminSolarPanel",
            getMTEItemTransformer(MetaTileEntityIDs.SOLAR_PANEL_ZPM));
        ItemStackReplacementManager.addItemReplacement(
            SuperSolarPanels.ID + ":PhotonicSolarPanel",
            getMTEItemTransformer(MetaTileEntityIDs.SOLAR_PANEL_UV));
    }

    private BiFunction<NBTTagCompound, World, BlockInfo> getMTETileTransformer(MetaTileEntityIDs mte) {
        return getMTETileTransformer(mte, Function.identity());
    }

    private BiFunction<NBTTagCompound, World, BlockInfo> getMTETileTransformer(MetaTileEntityIDs mte,
        Function<NBTTagCompound, NBTTagCompound> nbtTransformer) {
        return (tag, world) -> new BlockInfo(GregTechAPI.sBlockMachines, mte.ID, oldTileTag -> {
            oldTileTag = nbtTransformer.apply(oldTileTag);
            oldTileTag.setInteger("mID", mte.ID);
            oldTileTag.setString("mOwnerName", "Postea"); // set a name so that things don't npe
            oldTileTag.setShort("mFacing", (short) ForgeDirection.NORTH.ordinal());
            return oldTileTag;
        });
    }

    private Function<NBTTagCompound, NBTTagCompound> getMTEItemTransformer(MetaTileEntityIDs mte) {
        return tag -> {
            Item mteItem = GameRegistry.findItem(Mods.GregTech, "gt.blockmachines");
            int mteItemId = Item.getIdFromItem(mteItem);
            tag.setInteger("id", mteItemId);
            tag.setInteger("Damage", mte.ID);
            return tag;
        };
    }

    private Function<NBTTagCompound, NBTTagCompound> getMTEItemTransformer(
        Function<NBTTagCompound, MetaTileEntityIDs> mteFinder) {
        return tag -> {
            MetaTileEntityIDs mte = mteFinder.apply(tag);
            Item mteItem = GameRegistry.findItem(Mods.GregTech, "gt.blockmachines");
            int mteItemId = Item.getIdFromItem(mteItem);
            tag.setInteger("id", mteItemId);
            tag.setInteger("Damage", mte.ID);
            return tag;
        };
    }
}
