package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import gregtech.client.SeekingOggCodec;

@Mixin(targets = "net.minecraft.client.audio.SoundManager$2$1")
public abstract class SoundManagerInnerMixin {

    @WrapOperation(
        method = "getInputStream",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/IResourceManager;getResource(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/resources/IResource;"))
    IResource gt5u$stripSeekParams(IResourceManager instance, ResourceLocation location,
        Operation<IResource> original) {
        if (location.getResourcePath()
            .endsWith(SeekingOggCodec.EXTENSION)) {
            location = new ResourceLocation(
                location.getResourceDomain(),
                SeekingOggCodec.stripSeekMetadata(location.getResourcePath()));
        }
        return original.call(instance, location);
    }
}
