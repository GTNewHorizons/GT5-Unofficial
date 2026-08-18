package gregtech.mixin.mixins.early.minecraft;

import static gregtech.api.fluid.CauldronFluidHandler.CLOBBER_PARTIAL_FILL_MASK;
import static gregtech.api.fluid.CauldronFluidHandler.ORIGINAL_METADATA_MASK;
import static gregtech.api.fluid.CauldronFluidHandler.PARTIAL_FILL_MASK;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import gregtech.api.fluid.CauldronFluidHandler;

@Mixin(value = World.class)
public class World_CauldronPartialFill {

    @Redirect(
        method = "setBlockMetadataWithNotify",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setBlockMetadata(IIII)Z", remap = false),
        remap = false,
        require = 1)
    public boolean gt5u$overloadCauldronSetMetadata(Chunk chunk, int x, int y, int z, int metadata) {
        final Block block = chunk.getBlock(x, y, z);
        final int oldMetadata = chunk.getBlockMetadata(x, y, z);

        if (block == Blocks.cauldron) {
            if ((metadata & CLOBBER_PARTIAL_FILL_MASK) > 0) {
                metadata &= ORIGINAL_METADATA_MASK;
            } else if ((oldMetadata & PARTIAL_FILL_MASK) > 0 && metadata == (metadata & ORIGINAL_METADATA_MASK)) {
                return chunk.setBlockMetadata(x, y, z, (oldMetadata & PARTIAL_FILL_MASK) | metadata);
            }
        }

        return chunk.setBlockMetadata(x, y, z, metadata);
    }

    @Redirect(
        method = "getBlockMetadata",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getBlockMetadata(III)I", remap = false),
        remap = false,
        require = 1)
    public int gt5u$overloadCauldronGetMetadata(Chunk chunk, int x, int y, int z) {
        final Block block = chunk.getBlock(x, y, z);
        final int metadata = chunk.getBlockMetadata(x, y, z);

        if (block == Blocks.cauldron) {
            final int partialFill = metadata & PARTIAL_FILL_MASK;
            if (partialFill > 0) {
                CauldronFluidHandler
                    .setLastPartialFill((chunk.xPosition << 4) + x, y, (chunk.zPosition << 4) + z, partialFill >> 2);
            }
            return metadata & ORIGINAL_METADATA_MASK;
        }

        return metadata;
    }
}
