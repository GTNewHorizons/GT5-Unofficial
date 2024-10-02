package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import kubatech.loaders.BlockLoader;

@Mixin(value = World.class)
public class WorldMixin {

    @ModifyReturnValue(method = "getBlock", at = @At("RETURN"), require = 1)
    private Block gt5u$getBlockDetector(Block block, int x, int y, int z) {
        if (block == BlockLoader.kubaBlock) BlockLoader.kubaBlock.setLastBlockAccess((World) (Object) this, x, y, z);
        return block;
    }
}
