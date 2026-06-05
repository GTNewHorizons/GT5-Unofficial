package gregtech.api.util;

import static gregtech.api.enums.GTValues.E;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import gregtech.api.GregTechAPI;

/**
 * @deprecated Use standard translation with {@link StatCollector}.
 */
@Deprecated
public class GTLanguageManager {

    public static final String ANY_SUB_BLOCK = "Any Sub Block of this";

    /**
     * Buffer to reduce memory allocation when injecting data to LanguageRegistry.
     */
    private static final HashMap<String, String> TEMPMAP = new HashMap<>();
    /**
     * Buffer used when something is trying to add new lang entry while config file is not set up yet.
     */
    public static final Map<String, String> BUFFERMAP = new HashMap<>();
    /**
     * Map containing all the translation data coming into this class.
     */
    private static final Map<String, String> LANGMAP = new HashMap<>();
    /**
     * Config file handler bound to GregTech.lang or GregTech_(locale_name).lang. Even though it says English file, it's
     * not necessarily English, but on system it's always treated as English (as in, "default" language.)
     */
    public static Configuration sEnglishFile;
    /**
     * If the game is running with en_US language. This does not get updated when user changes language in game; GT lang
     * system cannot handle that anyway.
     */
    public static boolean isEN_US;
    /**
     * If placeholder like %material should be used for writing lang entries to file.
     */
    @Deprecated
    public static boolean i18nPlaceholder = true;
    /**
     * If there's any lang entry that is not found on lang file and waiting to be written.
     */
    private static boolean hasUnsavedEntry = false;

    // TODO: convert to enum
    public static String FACE_ANY = "gt.lang.face.any", FACE_BOTTOM = "gt.lang.face.bottom",
        FACE_TOP = "gt.lang.face.top", FACE_LEFT = "gt.lang.face.left", FACE_FRONT = "gt.lang.face.front",
        FACE_RIGHT = "gt.lang.face.right", FACE_BACK = "gt.lang.face.back", FACE_NONE = "gt.lang.face.none";

    public static String[] FACES = { FACE_BOTTOM, FACE_TOP, FACE_LEFT, FACE_FRONT, FACE_RIGHT, FACE_BACK, FACE_NONE };

    /**
     * Map referencing private field of StringTranslate, used by StatCollector. Used to inject lang entries there.
     */
    private static final Map<String, String> stringTranslateLanguageList;
    private static final Map<String, String> stringTranslateLanguageListFallBack;
    public static String LanguageCode = "en_US";

    static {
        try {
            Field fieldStringTranslateLanguageList = ReflectionHelper
                .findField(net.minecraft.util.StringTranslate.class, "languageList", "field_74816_c");
            Field fieldStringTranslateInstance = ReflectionHelper
                .findField(net.minecraft.util.StringTranslate.class, "instance", "field_74817_a");
            Field fieldStatCollectorFallbackTranslator = ReflectionHelper
                .findField(net.minecraft.util.StatCollector.class, "fallbackTranslator", "field_150828_b");
            // noinspection unchecked
            stringTranslateLanguageList = (Map<String, String>) fieldStringTranslateLanguageList
                .get(fieldStringTranslateInstance.get(null));
            stringTranslateLanguageListFallBack = (Map<String, String>) fieldStringTranslateLanguageList
                .get(fieldStatCollectorFallbackTranslator.get(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * If you newly use this method, please consider using MC lang system instead.
     */
    public static synchronized void addStringLocalization(String aKey, String aEnglish) {
        String trimmedKey = aKey != null ? aKey.trim() : "";
        if (trimmedKey.isEmpty()) return; // RIP cascading class loading, don't use GT_Utility here
        if (aEnglish == null) aEnglish = "";
        if (sEnglishFile == null) {
            // Lang file is not set up yet
            BUFFERMAP.put(trimmedKey, aEnglish);
            return;
        }
        if (!BUFFERMAP.isEmpty()) {
            // Lang file is now set up, resolve all the buffers
            // This won't be visited twice
            for (Entry<String, String> tEntry : BUFFERMAP.entrySet()) {
                storeTranslation(tEntry.getKey(), tEntry.getValue());
            }
            BUFFERMAP.clear();
        }
        if (!LANGMAP.containsKey(trimmedKey)) {
            storeTranslation(trimmedKey, aEnglish);
        }
    }

    public static synchronized void addAnySubBlockLocalization(String unlocalizedName) {
        addStringLocalization(unlocalizedName + "." + WILDCARD + ".name", ANY_SUB_BLOCK);
    }

    private static synchronized void storeTranslation(String trimmedKey, String english) {
        String translation = writeToLangFile(trimmedKey, english);
        LANGMAP.put(trimmedKey, translation);
        TEMPMAP.put(trimmedKey, translation);
        LanguageRegistry.instance()
            .injectLanguage(LanguageCode, TEMPMAP);
        TEMPMAP.clear();
    }

    private static synchronized String writeToLangFile(String trimmedKey, String aEnglish) {
        addToMCLangListFallBack(trimmedKey, aEnglish);
        // If the key is already provided by the standard lang system, don't touch GregTech.lang.
        if (StatCollector.canTranslate(trimmedKey)) {
            return StatCollector.translateToLocal(trimmedKey);
        }
        Property tProperty = sEnglishFile.get("LanguageFile", trimmedKey, aEnglish);
        if (hasUnsavedEntry && GregTechAPI.sPostloadFinished) {
            sEnglishFile.save();
            hasUnsavedEntry = false;
        }
        String translation = tProperty.getString();
        if (tProperty.wasRead()) {
            if (isEN_US && !aEnglish.equals(translation)) {
                tProperty.set(aEnglish);
                markFileDirty();
                return aEnglish;
            }
        } else {
            markFileDirty();
        }
        addToMCLangList(trimmedKey, translation);
        return translation;
    }

    private static synchronized void markFileDirty() {
        if (GregTechAPI.sPostloadFinished) {
            sEnglishFile.save();
        } else {
            hasUnsavedEntry = true;
        }
    }

    public static String getTranslation(String aKey) {
        String tTrimmedKey = aKey != null ? aKey.trim() : "";
        if (tTrimmedKey.isEmpty()) return E;

        if (StatCollector.canTranslate(tTrimmedKey)) {
            return StatCollector.translateToLocal(tTrimmedKey);
        }
        String anotherKeyToTry;
        if (tTrimmedKey.endsWith(".name")) {
            anotherKeyToTry = tTrimmedKey.substring(0, tTrimmedKey.length() - 5);
        } else {
            anotherKeyToTry = tTrimmedKey + ".name";
        }
        if (StatCollector.canTranslate(anotherKeyToTry)) {
            return StatCollector.translateToLocal(anotherKeyToTry);
        }
        return tTrimmedKey;
    }

    public static String getTranslation(String aKey, String aSeperator) {
        if (aKey == null) return E;
        String rTranslation = E;
        StringBuilder rTranslationSB = new StringBuilder(rTranslation);
        for (String tString : aKey.split(aSeperator)) {
            rTranslationSB.append(getTranslation(tString));
        }
        rTranslation = String.valueOf(rTranslationSB);
        return rTranslation;
    }

    @SuppressWarnings("unused")
    public static String getTranslateableItemStackName(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return "null";
        final String tName = ItemStackNBT.getDisplayName(aStack);
        if (GTUtility.isStringValid(tName)) {
            return tName;
        }
        return aStack.getUnlocalizedName() + ".name";
    }

    /** @deprecated Face strings are now in en_US.lang under {@code gt.lang.face.*}. */
    @Deprecated
    public static void writePlaceholderStrings() {}

    private static void addToMCLangList(String aKey, String translation) {
        if (stringTranslateLanguageList != null) {
            stringTranslateLanguageList.put(aKey, translation);
        }
    }

    private static void addToMCLangListFallBack(String aKey, String english) {
        if (stringTranslateLanguageListFallBack != null) {
            stringTranslateLanguageListFallBack.put(aKey, english);
        }
    }

    public static synchronized void reloadLanguage(Map<String, String> languageMap) {
        if (!GregTechAPI.sFullLoadFinished) return;
        File languageDir = sEnglishFile.getConfigFile()
            .getParentFile();
        String userLang = Minecraft.getMinecraft()
            .getLanguageManager()
            .getCurrentLanguage()
            .getLanguageCode();
        LanguageCode = userLang;
        if (userLang.equals("en_US")) {
            reloadLanguageWithEnglish(languageDir, languageMap);
            return;
        }
        String l10nFileName = "GregTech_" + userLang + ".lang";
        File l10nFile = new File(languageDir, l10nFileName);
        if (!l10nFile.isFile()) {
            reloadLanguageWithEnglish(languageDir, languageMap);
            return;
        }
        sEnglishFile = new Configuration(l10nFile);
        isEN_US = false;
        sEnglishFile.load();
        for (String key : LANGMAP.keySet()) {
            if (languageMap.containsKey(key)) {
                LANGMAP.put(key, languageMap.get(key));
                continue;
            }
            Property tProperty = sEnglishFile.get("LanguageFile", key, stringTranslateLanguageListFallBack.get(key));
            String translation = tProperty.getString();
            languageMap.put(key, translation);
            LANGMAP.put(key, translation);
        }
    }

    public static synchronized void reloadLanguageWithEnglish(File languageDir, Map<String, String> languageMap) {
        isEN_US = true;
        sEnglishFile = new Configuration(new File(languageDir, "GregTech.lang"));
        for (String key : LANGMAP.keySet()) {
            if (languageMap.containsKey(key)) {
                LANGMAP.put(key, languageMap.get(key));
                continue;
            }
            String english = stringTranslateLanguageListFallBack.get(key);
            languageMap.put(key, english);
            LANGMAP.put(key, english);
        }
    }

    public static boolean hasGTLocalizationKey(final String key) {
        return LANGMAP.containsKey(key);
    }
}
