package gregtech.mixin.mixins.early.minecraft;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import gregtech.mixin.hooks.MixinsVariablesHelper;

@Mixin(value = LanguageRegistry.class)
public class LanguageRegistryMixin {

    @Inject(method = "loadLanguagesFor", at = @At(value = "HEAD"), remap = false, require = 1)
    private void gt5u$loadLanguagesForHEAD(ModContainer container, Side side, CallbackInfo callbackInfo) {
        MixinsVariablesHelper.currentlyTranslating = container.getModId();
    }

    @Inject(method = "loadLanguagesFor", at = @At(value = "RETURN"), remap = false, require = 1)
    private void gt5u$loadLanguagesForRETURN(ModContainer container, Side side, CallbackInfo callbackInfo) {
        MixinsVariablesHelper.currentlyTranslating = null;
    }
}
