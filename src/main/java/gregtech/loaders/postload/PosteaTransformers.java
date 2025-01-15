package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.AdvancedSolarPanel;
import static gregtech.api.enums.Mods.SuperSolarPanels;

import java.util.function.BiConsumer;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.util.MTEReplacementManager;
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
        BiConsumer<NBTTagCompound, NBTTagCompound> nbtTransformer = (oldNbt, newNbt) -> newNbt
            .setLong("mStoredEnergy", oldNbt.getInteger("storage"));

        // Advanced Solar Panels
        MTEReplacementManager.builder()
            .fromTile("Advanced Solar Panel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_LV)
            .withCustomTileNBT(nbtTransformer)
            .register();

        MTEReplacementManager.builder()
            .fromTile("Hybrid Solar Panel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_MV)
            .withCustomTileNBT(nbtTransformer)
            .register();

        MTEReplacementManager.builder()
            .fromTile("Ultimate Hybrid Solar Panel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_HV)
            .withCustomTileNBT(nbtTransformer)
            .register();

        MTEReplacementManager.builder()
            .fromTile("Quantum Solar Panel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_EV)
            .withCustomTileNBT(nbtTransformer)
            .register();

        // Handle ItemStacks separately because they are meta items but not meta tiles
        MTEReplacementManager.builder()
            .fromItem(AdvancedSolarPanel.ID + ":BlockAdvSolarPanel")
            .toMTE(metadata -> switch (metadata) {
            case 0 -> MetaTileEntityIDs.SOLAR_PANEL_LV;
            case 1 -> MetaTileEntityIDs.SOLAR_PANEL_MV;
            case 2 -> MetaTileEntityIDs.SOLAR_PANEL_HV;
            default -> MetaTileEntityIDs.SOLAR_PANEL_EV;
            })
            .register();

        // Super Solar Panels
        MTEReplacementManager.builder()
            .fromTile("SpectralSolarPanel")
            .fromItem(SuperSolarPanels.ID + ":SpectralSolarPanel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_IV)
            .withCustomTileNBT(nbtTransformer)
            .register();

        MTEReplacementManager.builder()
            .fromTile("SingularSolarPanel")
            .fromItem(SuperSolarPanels.ID + ":SingularSolarPanel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_LuV)
            .withCustomTileNBT(nbtTransformer)
            .register();

        MTEReplacementManager.builder()
            .fromTile("AdminSolarPanel")
            .fromItem(SuperSolarPanels.ID + ":AdminSolarPanel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_ZPM)
            .withCustomTileNBT(nbtTransformer)
            .register();

        MTEReplacementManager.builder()
            .fromTile("PhotonicSolarPanel")
            .fromItem(SuperSolarPanels.ID + ":PhotonicSolarPanel")
            .toMTE(MetaTileEntityIDs.SOLAR_PANEL_UV)
            .withCustomTileNBT(nbtTransformer)
            .register();
    }
}
