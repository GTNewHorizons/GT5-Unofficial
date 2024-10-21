package gregtech.mixin.mixins.early.minecraft.pollution;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;

import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import gregtech.common.pollution.ColorOverrideType;
import gregtech.common.pollution.Pollution;

@Mixin(RenderBlocks.class)
public class MixinRenderBlocks_PollutionWithOptifine {

    @Dynamic("Target is an optifine method")
    @ModifyExpressionValue(
        method = "renderStandardBlock",
        at = @At(
            value = "INVOKE",
            target = "LCustomColorizer;getColorMultiplier(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)I",
            remap = false))
    private int gt5u$pollutionStandardBlock(int color, Block block, int blockX, int blockY, int blockZ) {
        ColorOverrideType type = Pollution.standardBlocks.matchesID(block);
        if (type == null) return color;
        return type.getColor(color, blockX, blockZ);
    }

    @Dynamic("Target is an optifine method")
    @ModifyExpressionValue(
        method = "renderBlockLiquid",
        at = @At(
            value = "INVOKE",
            target = "LCustomColorizer;getFluidColor(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)I",
            remap = false))
    private int gt5u$pollutionBlockLiquid(int color, Block block, int blockX, int blockY, int blockZ) {
        ColorOverrideType type = Pollution.liquidBlocks.matchesID(block);
        if (type == null || block.getMaterial() != Material.water) {
            return color;
        }
        return type.getColor(color, blockX, blockZ);
    }

    @Dynamic("Target is an optifine method")
    @ModifyExpressionValue(
        method = "renderBlockDoublePlant",
        at = @At(
            value = "INVOKE",
            target = "LCustomColorizer;getColorMultiplier(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)I",
            remap = false))
    private int gt5u$pollutionBlockDoublePlant(int color, BlockDoublePlant block, int blockX, int blockY, int blockZ) {
        ColorOverrideType type = Pollution.doublePlants.matchesID(block);
        if (type == null) return color;
        return type.getColor(color, blockX, blockZ);
    }

    @Dynamic("Target is an optifine method")
    @ModifyExpressionValue(
        method = "renderCrossedSquares",
        at = @At(
            value = "INVOKE",
            target = "LCustomColorizer;getColorMultiplier(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)I",
            remap = false))
    private int gt5u$pollutionCrossedSquares(int color, Block block, int blockX, int blockY, int blockZ) {
        ColorOverrideType type = Pollution.crossedSquares.matchesID(block);
        if (type == null) return color;
        return type.getColor(color, blockX, blockZ);
    }

    @Dynamic("Target is an optifine method")
    @ModifyExpressionValue(
        method = "renderBlockVine",
        at = @At(
            value = "INVOKE",
            target = "LCustomColorizer;getColorMultiplier(Lnet/minecraft/block/Block;Lnet/minecraft/world/IBlockAccess;III)I",
            remap = false))
    private int gt5u$pollutionBlockVine(int color, Block block, int blockX, int blockY, int blockZ) {
        ColorOverrideType type = Pollution.blockVine.matchesID(block);
        if (type == null) return color;
        return type.getColor(color, blockX, blockZ);
    }
}
