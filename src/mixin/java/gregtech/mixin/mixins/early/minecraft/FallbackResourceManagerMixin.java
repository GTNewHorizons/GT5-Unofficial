package gregtech.mixin.mixins.early.minecraft;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.FallbackResourceManager;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

@Mixin(FallbackResourceManager.class)
public class FallbackResourceManagerMixin {

    @Unique
    private Set<File> visitedJars;

    @Inject(method = "getAllResources", at = @At("HEAD"))
    public void gt5u$initSet(ResourceLocation l, CallbackInfoReturnable<?> cir) {
        visitedJars = new HashSet<>();
    }

    @Inject(method = "getAllResources", at = @At("RETURN"))
    public void gt5u$clearSet(ResourceLocation l, CallbackInfoReturnable<?> cir) {
        visitedJars = null;
    }

    @WrapOperation(
        method = "getAllResources",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/IResourcePack;resourceExists(Lnet/minecraft/util/ResourceLocation;)Z"))
    public boolean gt5u$preventDuplicateDiscovery(IResourcePack iresourcepack, ResourceLocation loc,
        Operation<Boolean> exists) {

        if (iresourcepack instanceof AbstractResourcePack abstractPack) {
            if (!visitedJars.add(abstractPack.resourcePackFile)) {
                return false;
            }
        }

        return exists.call(iresourcepack, loc);
    }
}
