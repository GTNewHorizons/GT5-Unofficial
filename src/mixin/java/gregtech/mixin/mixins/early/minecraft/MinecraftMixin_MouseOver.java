package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.Minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.GTMod;

@Mixin(Minecraft.class)
public class MinecraftMixin_MouseOver {

    @Inject(
        method = "runTick",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.BEFORE,
            target = "Lnet/minecraft/client/renderer/EntityRenderer;getMouseOver(F)V"))
    private void gt5u$before$getMouseOver(CallbackInfo ci) {
        GTMod.clientProxy()
            .setComputingPickBlock(true);
    }

    @Inject(
        method = "runTick",
        at = @At(
            value = "INVOKE",
            shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/renderer/EntityRenderer;getMouseOver(F)V"))
    private void gt5u$after$getMouseOver(CallbackInfo ci) {
        GTMod.clientProxy()
            .setComputingPickBlock(false);
    }
}
