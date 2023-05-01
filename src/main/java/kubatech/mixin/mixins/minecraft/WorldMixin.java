package kubatech.mixin.mixins.minecraft;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import kubatech.loaders.BlockLoader;

@SuppressWarnings("unused")
@Mixin(value = World.class)
public class WorldMixin {

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getBlock", at = @At("RETURN"), require = 1)
    private void getBlockDetector(int x, int y, int z, CallbackInfoReturnable<Block> callbackInfoReturnable) {
        if (callbackInfoReturnable.getReturnValue() == BlockLoader.kubaBlock)
            BlockLoader.kubaBlock.setLastBlockAccess((World) (Object) this, x, y, z);
    }
}
