package gregtech.api.util;

import java.util.HashMap;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Retained solely as a holder for the GregTech.lang config-file handle and a few flags that external code still reads.
 * No new translations should be registered through this class; use {@link StatCollector} and the standard
 * {@code en_US.lang} resource file instead.
 *
 * <p>
 * The GregTech.lang config file (~52k entries) is still loaded at startup and read by
 * {@link gregtech.api.util.AssemblyLineServer} and other callers. Those entries have not yet been migrated to
 * the standard Minecraft lang file.
 *
 * <p>
 * TODO: migrate all remaining entries from GregTech.lang into
 * {@code src/main/resources/assets/gregtech/lang/en_US.lang}, then remove this class and the
 * {@code sEnglishFile} load/save infrastructure in {@link gregtech.loaders.preload.GTPreLoad} and
 * {@link gregtech.GTMod}.
 *
 * @deprecated Use standard translation with {@link StatCollector}.
 */
@Deprecated
public class GTLanguageManager {

    public static final String ANY_SUB_BLOCK = "Any Sub Block of this";

    /**
     * Config file handler bound to GregTech.lang or GregTech_(locale_name).lang.
     * Loaded read-only at startup; nothing is written to it during a normal game session anymore.
     */
    public static Configuration sEnglishFile;

    /**
     * Whether the game is running with the en_US locale. Not updated on in-game language changes.
     */
    public static boolean isEN_US;

    /**
     * Whether placeholder format strings (e.g. {@code %material Ingot}) should be injected instead of
     * fully-resolved names. Read by {@link gregtech.api.items.MetaGeneratedItemX01} and
     * {@link gregtech.common.blocks.GTBlockOre}.
     */
    @Deprecated
    public static boolean i18nPlaceholder = true;

    public static String LanguageCode = "en_US";

    public static String FACE_ANY = "gt.lang.face.any", FACE_BOTTOM = "gt.lang.face.bottom",
        FACE_TOP = "gt.lang.face.top", FACE_LEFT = "gt.lang.face.left", FACE_FRONT = "gt.lang.face.front",
        FACE_RIGHT = "gt.lang.face.right", FACE_BACK = "gt.lang.face.back", FACE_NONE = "gt.lang.face.none";

    public static String[] FACES = { FACE_BOTTOM, FACE_TOP, FACE_LEFT, FACE_FRONT, FACE_RIGHT, FACE_BACK, FACE_NONE };

    /**
     * Null-safe wrapper around {@link LanguageRegistry#injectLanguage}.
     * Entries with null values are silently dropped so that any existing translation for that key is preserved.
     * Use this everywhere instead of calling {@link LanguageRegistry#injectLanguage} directly.
     */
    @Deprecated
    public static void injectLanguage(HashMap<String, String> tLang) {
        tLang.values()
            .removeIf(Objects::isNull);
        if (!tLang.isEmpty()) {
            GTLanguageManager.injectLanguage(tLang);
        }
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
}
