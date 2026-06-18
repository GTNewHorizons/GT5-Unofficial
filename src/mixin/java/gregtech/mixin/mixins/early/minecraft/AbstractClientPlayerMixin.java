package gregtech.mixin.mixins.early.minecraft;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import gregtech.mixin.interfaces.accessors.AbstractClientPlayerAccessor;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin implements AbstractClientPlayerAccessor {

    @Unique
    private ResourceLocation gt$locationCape;

    @ModifyReturnValue(method = "func_152122_n", at = @At(value = "RETURN"))
    private boolean gt5u$hasCape(boolean original) {
        return gt$locationCape != null || original;
    }

    @ModifyReturnValue(method = "getLocationCape", at = @At(value = "RETURN"))
    private ResourceLocation gt5u$getCape(ResourceLocation original) {
        return gt$locationCape != null ? gt$locationCape : original;
    }

    @Override
    public void gt5u$setCape(ResourceLocation gtCape) {
        gt$locationCape = gtCape;
    }
}
