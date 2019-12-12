package gtPlusPlus.core.util.minecraft;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class LangUtils {


	public static boolean rewriteEntryForLanguageRegistry(String aKey, String aNewValue){
		return rewriteEntryForLanguageRegistry("en_US", aKey, aNewValue);
	}
	
	@SuppressWarnings("unchecked")
	public static boolean rewriteEntryForLanguageRegistry(String aLang, String aKey, String aNewValue){
		LanguageRegistry aInstance = LanguageRegistry.instance();
		Field aModLanguageData = ReflectionUtils.getField(LanguageRegistry.class, "modLanguageData");
		if (aModLanguageData != null){
			Map<String,Properties> aProps = new HashMap<String, Properties>();
			Object aInstanceProps;
			try {
				aInstanceProps = aModLanguageData.get(aInstance);
				if (aInstanceProps != null){
					aProps = (Map<String, Properties>) aInstanceProps;
					Properties aLangProps = aProps.get(aLang);
					if (aLangProps != null){
						if (aLangProps.containsKey(aKey)) {
							aLangProps.remove(aKey);
							aLangProps.put(aKey, aNewValue);
						}
						else {
							aLangProps.put(aKey, aNewValue);
						}
						aProps.remove(aLang);
						aProps.put(aLang, aLangProps);
						ReflectionUtils.setField(aInstance, aModLanguageData, aProps);
					}
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				
			}
		}
		return false;
	}

	public static String trans(String aNr, String aEnglish) {
		return GT_LanguageManager.addStringLocalization("Interaction_DESCRIPTION_Index_" + aNr, aEnglish, false);
	}
	
}
