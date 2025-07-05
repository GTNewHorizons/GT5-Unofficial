package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import gregtech.client.GTPowerfailRenderer;

@Mixin(Minecraft.class)
public class MinecraftLoadWorldMixin {

    @Inject(method = "loadWorld(Lnet/minecraft/client/multiplayer/WorldClient;Ljava/lang/String;)V", at = @At("HEAD"))
    public void gt5u$worldLoadHook(WorldClient worldClientIn, String loadingMessage, CallbackInfo ci) {
        GTPowerfailRenderer.POWERFAILS = null;
    }
}
