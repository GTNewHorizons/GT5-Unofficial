package gregtech.loaders.postload;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;

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

        // For each material that has a frame box generated, there is a unique TE id, so we need a transformer for each
        // of these
        for (int meta = 1; meta < GregTech_API.sGeneratedMaterials.length; meta++) {
            // Grab the material and see if it has a frame box
            Materials material = GregTech_API.sGeneratedMaterials[meta];
            if (((material != null) && ((material.mTypes & 0x2) != 0))) {
                String teName = "GT_Frame_" + material;
                // Stupid java rules about effectively final variables in a lambda
                final int finalMeta = meta;
                TileEntityReplacementManager.tileEntityTransformer(teName, (tag, world) -> {
                    // If this frame has a cover on it, we need to keep it, but we still need to make sure the block
                    // is the new frame block. We can make sure to keep the TE using a pass-through transformer.
                    // This works because between the old and new frame systems, the TileEntity used for covered frames
                    // is still the same
                    if (tag.hasKey(GT_Values.NBT.COVERS)) {
                        return new BlockInfo(GregTech_API.sBlockFrames, finalMeta, PosteaTransformers::passthrough);
                    }

                    // If this frame has no covers, simply return a block and delete the TileEntity
                    return new BlockInfo(GregTech_API.sBlockFrames, finalMeta);
                });
            }
        }
    }
}
