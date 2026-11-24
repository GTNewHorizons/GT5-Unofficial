package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.block.BlockLiquid;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gregtech.api.GregTechAPI;

/// Stops vanilla liquids from treating dam water as part of their flow. Vanilla reads the flow level from the block's
/// metadata, which for dam water is a volume id, so dam water would otherwise act as a random flow level or source.
@Mixin(BlockLiquid.class)
public class BlockLiquidMixin_DamWater {

    @Inject(method = "func_149804_e", at = @At("HEAD"), cancellable = true, require = 1)
    private void gt5u$ignoreDamWaterFlowDecay(World world, int x, int y, int z, CallbackInfoReturnable<Integer> cir) {
        if (world.getBlock(x, y, z) == GregTechAPI.sBlockDamWater) cir.setReturnValue(-1);
    }

    @Inject(method = "getEffectiveFlowDecay", at = @At("HEAD"), cancellable = true, require = 1)
    private void gt5u$ignoreDamWaterEffectiveFlowDecay(IBlockAccess world, int x, int y, int z,
        CallbackInfoReturnable<Integer> cir) {
        if (world.getBlock(x, y, z) == GregTechAPI.sBlockDamWater) cir.setReturnValue(-1);
    }
}
