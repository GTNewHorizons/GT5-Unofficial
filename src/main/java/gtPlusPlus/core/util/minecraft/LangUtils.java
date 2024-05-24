package gtPlusPlus.core.util.minecraft;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class LangUtils {

    @SuppressWarnings("unchecked")
    public static void rewriteEntryForLanguageRegistry(String aLang, String aKey, String aNewValue) {
        LanguageRegistry aInstance = LanguageRegistry.instance();
        Field aModLanguageData = ReflectionUtils.getField(LanguageRegistry.class, "modLanguageData");
        if (aModLanguageData != null) {
            Map<String, Properties> aProps;
            Object aInstanceProps;
            try {
                aInstanceProps = aModLanguageData.get(aInstance);
                if (aInstanceProps != null) {
                    aProps = (Map<String, Properties>) aInstanceProps;
                    Properties aLangProps = aProps.get(aLang);
                    if (aLangProps != null) {
                        if (aLangProps.containsKey(aKey)) {
                            aLangProps.remove(aKey);
                            aLangProps.put(aKey, aNewValue);
                        } else {
                            aLangProps.put(aKey, aNewValue);
                        }
                        aProps.remove(aLang);
                        aProps.put(aLang, aLangProps);
                        ReflectionUtils.setField(aInstance, aModLanguageData, aProps);
                    }
                }
            } catch (IllegalArgumentException | IllegalAccessException ignored) {

            }
        }
    }
}
