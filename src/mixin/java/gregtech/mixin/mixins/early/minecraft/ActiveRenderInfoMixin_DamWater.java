package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;

import gregtech.common.hydrodam.DamWaterHeights;

/// Makes the camera only count as underwater (fog, overlay) below a dam water block's water level. Vanilla reads the
/// liquid height from the block's metadata, which for dam water is a volume id.
@Mixin(ActiveRenderInfo.class)
public class ActiveRenderInfoMixin_DamWater {

    /// Vanilla subtracts this from the empty fraction, because vanilla water is drawn 1/9 below the top of its block.
    private static final float VANILLA_SURFACE_OFFSET = 0.11111111F;

    @WrapOperation(
        method = "getBlockAtEntityViewpoint",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMetadata(III)I"),
        require = 1)
    private static int gt5u$captureDamWaterFill(World world, int x, int y, int z, Operation<Integer> original,
        @Share("damWaterFill") LocalFloatRef damWaterFill) {
        damWaterFill.set(DamWaterHeights.getFill(world, x, y, z));

        return original.call(world, x, y, z);
    }

    @WrapOperation(
        method = "getBlockAtEntityViewpoint",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockLiquid;getLiquidHeightPercent(I)F"),
        require = 1)
    private static float gt5u$damWaterHeight(int meta, Operation<Float> original,
        @Share("damWaterFill") LocalFloatRef damWaterFill) {
        float fill = damWaterFill.get();

        return Float.isNaN(fill) ? original.call(meta) : 1 - fill + VANILLA_SURFACE_OFFSET;
    }
}
