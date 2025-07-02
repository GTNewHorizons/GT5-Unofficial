package gregtech.mixin.mixins.early.forge;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gregtech.common.GTProxy;

@Mixin(value = ForgeHooks.class, remap = false)
public class ForgeHooksMixin {

    @Inject(method = "blockStrength", at = @At("RETURN"), cancellable = true)
    private static void gt$blockStrengthHack(Block block, EntityPlayer player, World world, int x, int y, int z,
        CallbackInfoReturnable<Float> cir) {
        float oldValue = cir.getReturnValueF();
        float newValue = GTProxy.onBlockStrength(block, player, world, x, y, z, oldValue);
        if (oldValue != newValue) {
            cir.setReturnValue(newValue);
        }
    }

}
