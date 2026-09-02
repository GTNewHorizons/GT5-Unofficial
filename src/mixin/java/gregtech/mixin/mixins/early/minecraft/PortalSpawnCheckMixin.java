package gregtech.mixin.mixins.early.minecraft;

import java.util.Random;

import net.minecraft.block.BlockPortal;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.api.util.GTSpawnEventHandler;

@Mixin(BlockPortal.class)
public class PortalSpawnCheckMixin {

    @Inject(method = "updateTick", at = @At("HEAD"), cancellable = true)
    private void checkTileEntityBlocker(World worldIn, int x, int y, int z, Random random, CallbackInfo ci) {
        if (GTSpawnEventHandler.INSTANCE.isAreaProtected(worldIn, x, y, z)) {
            ci.cancel();
        }
    }
}
