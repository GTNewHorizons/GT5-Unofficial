package gregtech.api.util.client;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles dynamic key generation for material auto name fusion.
 *
 * The main use case is so that we only need to translate material and the form separately, and they are glued together
 * during runtime. However since the mc lang system expects a static lang key for item, we need to generate the names
 * at runtime.
 */
public class DynamicLangManager {
    private static List<ItemStack> dynamicStacks = new ArrayList<>();

    public static void addStack(ItemStack stack) {
        dynamicStacks.add(stack);
    }

    public static void reload(Map<String, String> mcLangMap) {
        for (ItemStack stack : dynamicStacks) {
            mcLangMap.put(stack.getUnlocalizedName() + ".name", stack.getDisplayName());
        }
    }
}
