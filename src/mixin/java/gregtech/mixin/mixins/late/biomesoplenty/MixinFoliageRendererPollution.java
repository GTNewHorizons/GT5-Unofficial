package gregtech.mixin.mixins.late.biomesoplenty;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import biomesoplenty.client.render.blocks.FoliageRenderer;
import gregtech.common.pollution.ColorOverrideType;
import gregtech.common.pollution.Pollution;

@Mixin(value = FoliageRenderer.class, remap = false)
public class MixinFoliageRendererPollution {

    @ModifyExpressionValue(
        method = "renderCrossedSquares",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;colorMultiplier(Lnet/minecraft/world/IBlockAccess;III)I",
            remap = true))
    private int gt5u$pollutionCrossedSquares(int color, Block block, int blockX, int blockY, int blockZ,
        RenderBlocks renderer) {
        ColorOverrideType type = Pollution.blockVine.matchesID(block);
        if (type == null) return color;
        return type.getColor(color, blockX, blockZ);
    }
}
