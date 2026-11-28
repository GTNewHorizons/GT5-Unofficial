package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;

import gregtech.common.hydrodam.DamWaterHeights;

/// Makes entities in dam water only count as submerged below the water level. Vanilla reads the liquid height from the
/// block's metadata, which for dam water is a volume id.
@Mixin(World.class)
public class WorldMixin_DamWater {

    @WrapOperation(
        method = "handleMaterialAcceleration",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBlockMetadata(III)I"),
        require = 1)
    private int gt5u$captureDamWaterFill(World world, int x, int y, int z, Operation<Integer> original,
        @Share("damWaterFill") LocalFloatRef damWaterFill) {
        damWaterFill.set(DamWaterHeights.getFill(world, x, y, z));

        return original.call(world, x, y, z);
    }

    @WrapOperation(
        method = "handleMaterialAcceleration",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockLiquid;getLiquidHeightPercent(I)F"),
        require = 1)
    private float gt5u$damWaterHeight(int meta, Operation<Float> original,
        @Share("damWaterFill") LocalFloatRef damWaterFill) {
        float fill = damWaterFill.get();

        // getLiquidHeightPercent returns the empty fraction of the block.
        return Float.isNaN(fill) ? original.call(meta) : 1 - fill;
    }
}
