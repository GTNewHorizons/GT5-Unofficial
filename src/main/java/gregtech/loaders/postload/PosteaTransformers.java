package gregtech.loaders.postload;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;

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
            int meta = 0;

            // If this frame has a cover on it, we need to keep it, but we still need to make sure the block
            // is the new frame block. We can make sure to keep the TE using a pass-through transformer.
            // This works because between the old and new frame systems, the TileEntity used for covered frames
            // is still the same
            if (tag.hasKey(GT_Values.NBT.COVERS)) {
                return new BlockInfo(GregTech_API.sBlockFrames, meta, PosteaTransformers::passthrough);
            }

            // If this frame has no covers, simply return a block and delete the TileEntity
            return new BlockInfo(GregTech_API.sBlockFrames, meta);
        });
    }
}
