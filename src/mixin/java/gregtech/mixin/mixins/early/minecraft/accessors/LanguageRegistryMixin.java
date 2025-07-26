package gregtech.mixin.mixins.early.minecraft.accessors;

import java.util.Map;
import java.util.Properties;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.mixin.interfaces.accessors.LanguageRegistryAccessor;

@Mixin(value = LanguageRegistry.class)
public class LanguageRegistryMixin implements LanguageRegistryAccessor {

    @Shadow(remap = false)
    private Map<String, Properties> modLanguageData;

    @Override
    public Map<String, Properties> gt5u$getModLanguageData() {
        return modLanguageData;
    }
}
