package gregtech.mixin.mixins.early.minecraft;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.ibm.icu.impl.LocaleUtility;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.api.util.GTInflectionManager;
import gregtech.api.util.GTLanguageManager;

@Mixin(value = LanguageRegistry.class)
public class LanguageRegistryMixin {

    @Inject(method = "mergeLanguageTable", at = @At(value = "HEAD"), remap = false)
    private void gt5u$mergeLanguageTable(Map field_135032_a, String lang, CallbackInfo ci) {
        GTLanguageManager.LOCALE = LocaleUtility.getLocaleFromName(lang);
        GTInflectionManager.loadInflectionJson(lang);
        GTLanguageManager.reloadLanguage(field_135032_a);
    }
}
