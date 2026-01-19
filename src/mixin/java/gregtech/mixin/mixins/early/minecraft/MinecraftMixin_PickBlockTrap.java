package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.common.events.PickBlockEvent;

@Mixin(Minecraft.class)
public class MinecraftMixin_PickBlockTrap {

    @Inject(method = "func_147112_ai", at = @At("HEAD"), cancellable = true)
    private void gt5u$before$pickBlock(CallbackInfo ci) {
        if (MinecraftForge.EVENT_BUS.post(new PickBlockEvent())) {
            ci.cancel();
        }
    }
}
