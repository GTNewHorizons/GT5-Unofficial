package gregtech.api.util.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * Handles dynamic key generation for material auto name fusion.
 *
 * The main use case is so that we only need to translate material and the form separately,
 * and they are glued together during runtime. However, a static lang key is convenient for
 * many purposes, and we can make it safe to transmit {@link ItemStack#getUnlocalizedName}
 * from server to client.
 */
public class DynamicLangManager {

    private static final Map<String, String> mcLangMap;

    static {
        Field fieldStringTranslateLanguageList = ReflectionHelper
            .findField(net.minecraft.util.StringTranslate.class, "languageList", "field_74816_c");
        Field fieldStringTranslateInstance = ReflectionHelper
            .findField(net.minecraft.util.StringTranslate.class, "instance", "field_74817_a");
        try {
            // noinspection unchecked
            mcLangMap = (Map<String, String>) fieldStringTranslateLanguageList
                .get(fieldStringTranslateInstance.get(null));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static final List<ItemStack> dynamicStacks = new ArrayList<>();

    public static void addStack(ItemStack stack) {
        dynamicStacks.add(stack);
    }

    public static void reload() {
        for (ItemStack stack : dynamicStacks) {
            mcLangMap.put(stack.getUnlocalizedName() + ".name", stack.getDisplayName());
        }
    }
}
