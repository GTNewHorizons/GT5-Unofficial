package gtPlusPlus.core.util.minecraft;

import java.util.Map;
import java.util.Properties;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.mixin.interfaces.accessors.LanguageRegistryAccessor;

public class LangUtils {

    public static void rewriteEntryForLanguageRegistry(String aLang, String aKey, String aNewValue) {
        final Map<String, Properties> aProps = ((LanguageRegistryAccessor) LanguageRegistry.instance())
            .gt5u$getModLanguageData();
        Properties aLangProps = aProps.get(aLang);
        if (aLangProps != null) {
            aLangProps.put(aKey, aNewValue);
        }
    }
}
