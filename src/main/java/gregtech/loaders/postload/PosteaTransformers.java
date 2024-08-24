package gregtech.loaders.postload;

import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import vexatos.tgregworks.reference.Mods;

public class PosteaTransformers implements Runnable {

    @Override
    public void run() {
        registerFrameboxTransformers();
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
            if (indexInMaterialList < 0 || indexInMaterialList >= GregTech_API.sGeneratedMaterials.length) {
                // Do not modify this TE, so return null
                return null;
            }

            // Now we know for sure that the tileentity is a frame box
            // If this frame has a cover on it, we need to keep it, but we still need to make sure the block
            // is the new frame block. We can make sure to keep the TE using a pass-through transformer.
            // This works because between the old and new frame systems, the TileEntity used for covered frames
            // is still the same
            if (tag.hasKey(GT_Values.NBT.COVERS)) {
                return new BlockInfo(GregTech_API.sBlockFrames, indexInMaterialList, PosteaTransformers::passthrough);
            }

            // If this frame has no covers, simply return a block and delete the TileEntity
            return new BlockInfo(GregTech_API.sBlockFrames, indexInMaterialList);
        });

        ItemStackReplacementManager.addItemReplacement("gregtech:gt.blockmachines", (tag) -> {
            // Get item meta id and see if this is a frame box, this works pretty much identically to the TE transformer
            int id = tag.getInteger("Damage");
            int indexInMaterialList = id - 4096;
            // Not a frame box
            if (indexInMaterialList < 0 || indexInMaterialList >= GregTech_API.sGeneratedMaterials.length) {
                return tag;
            }
            // Not a frame box if the material for this id does not have a frame box associated with it.
            // Apparently the DEFC ID overlaps with the material ID for a Bastnasite frame box for example
            if ((GregTech_API.sGeneratedMaterials[indexInMaterialList].mTypes & 0x2) == 0) {
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
}
