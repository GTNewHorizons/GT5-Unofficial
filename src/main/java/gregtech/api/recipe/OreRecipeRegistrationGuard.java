package gregtech.api.recipe;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

/**
 * Dedupes material-autogen ore recipe registration during {@code activateOreDictHandler}.
 * When canonical inputs are enabled, key is {@code prefixName|mat:materialName|registratorId} so multiple
 * ore-dict names for the same material do not register identical recipes again. Otherwise
 * {@code prefixName|oreDictName|registratorId}.
 */
public final class OreRecipeRegistrationGuard {

    public static final String DISABLE_PROPERTY = "gt.recipe.ore.guard.disable";

    private static Set<String> seenOreDictKeys;
    private static final Set<String> seenReverseRecipeKeys = new HashSet<>();

    private OreRecipeRegistrationGuard() {}

    public static void begin() {
        OreRecipeDedupeFlags.logConfigurationOnce();
        seenOreDictKeys = new HashSet<>();
        seenReverseRecipeKeys.clear();
    }

    public static void end() {
        seenOreDictKeys = null;
    }

    static void clearReverseRecipeKeysForTesting() {
        seenReverseRecipeKeys.clear();
    }

    /**
     * @return {@code true} when this registrator should run for {@code (prefix, material, oreDictName)}
     */
    public static boolean tryProcess(OrePrefixes prefix, Materials material, String oreDictName, String registratorId) {
        if (seenOreDictKeys == null || !OreRecipeDedupeFlags.guardProcessEnabled()) {
            return true;
        }
        if (prefix == null || oreDictName == null
            || oreDictName.isEmpty()
            || registratorId == null
            || registratorId.isEmpty()) {
            return true;
        }
        String key;
        if (OreRecipeDedupeFlags.canonicalInputsEnabled() && material != null
            && material != Materials._NULL
            && material.mName != null) {
            key = prefix.getName() + "|mat:" + material.mName + "|" + registratorId;
        } else {
            key = prefix.getName() + "|" + oreDictName + "|" + registratorId;
        }
        return seenOreDictKeys.add(key);
    }

    /**
     * Dedupes repeated ore-dict aliases for the same concrete source stack while preserving distinct block/meta
     * variants.
     */
    public static boolean tryProcessStack(OrePrefixes prefix, Materials material, ItemStack stack,
        String registratorId) {
        if (!OreRecipeDedupeFlags.guardProcessEnabled() || seenOreDictKeys == null) {
            return true;
        }
        if (prefix == null || stack == null
            || stack.getItem() == null
            || registratorId == null
            || registratorId.isEmpty()) {
            return true;
        }
        String materialName = material == null || material.mName == null ? "_NULL" : material.mName;
        String key = prefix.getName() + "|mat:"
            + materialName
            + "|stack:"
            + itemIdentity(stack.getItem())
            + ':'
            + stack.getItemDamage()
            + "|tag:"
            + stackTag(stack)
            + '|'
            + registratorId;
        return seenOreDictKeys.add(key);
    }

    private static String itemIdentity(Item item) {
        GameRegistry.UniqueIdentifier identifier;
        try {
            identifier = GameRegistry.findUniqueIdentifierFor(item);
        } catch (RuntimeException ignored) {
            identifier = null;
        }
        if (identifier == null) {
            // Registered Minecraft items are singletons. This fallback is only for unregistered test/runtime items.
            return item.getClass()
                .getName() + '@'
                + System.identityHashCode(item);
        }
        return identifier.modId + ':' + identifier.name;
    }

    /** @deprecated pass {@link Materials} so canonical-input dedupe can key by material */
    @Deprecated
    public static boolean tryProcess(OrePrefixes prefix, String oreDictName, String registratorId) {
        return tryProcess(prefix, null, oreDictName, registratorId);
    }

    /**
     * Dedupes reverse recycling recipes registered once per concrete source stack.
     */
    public static boolean tryRegisterReverseRecipe(String recipeKind, Materials material, OrePrefixes prefix,
        ItemStack stack) {
        if (seenOreDictKeys == null || !OreRecipeDedupeFlags.guardReverseEnabled()) {
            return true;
        }
        if (recipeKind == null || recipeKind.isEmpty()
            || material == null
            || material.mName == null
            || prefix == null
            || stack == null
            || stack.getItem() == null) {
            return true;
        }
        return seenReverseRecipeKeys.add(
            "reverse|" + recipeKind
                + '|'
                + prefix.getName()
                + "|mat:"
                + material.mName
                + "|stack:"
                + itemIdentity(stack.getItem())
                + ':'
                + stack.getItemDamage()
                + "|tag:"
                + stackTag(stack));
    }

    private static String stackTag(ItemStack stack) {
        return stack.getTagCompound() == null ? ""
            : stack.getTagCompound()
                .toString();
    }
}
