package gregtech.api.util;

import static gregtech.api.enums.GTValues.E;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.GTShapeStore;
import gregtech.api.material.MaterialData;
import gregtech.api.material.MaterialParts;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

/// GregTech's view of the ore dictionary: which item stands for an ore-dictionary name, and which items are other
/// mods' copies of it. What an item is made of lives in [MaterialData].
///
/// MaterialLib decides the item for every name of a (prefix, material) pair it serves: the pair's stack, MaterialLib's
/// own item or the foreign item a shape override declares, whatever else registers under the name and in whatever
/// order. A pair MaterialLib's own item serves answers from GregTech's preInit on; one a foreign item stands in for
/// answers from GregTech's init, once that item is registered (see [#useMaterialLibTargets]). Names with no such pair
/// (containers, name-only circuits) are decided by [#set(OrePrefixes, Material, ItemStack)]; other names have none.
///
/// Another mod's item registered under a name that stands for a different item is a unified copy
/// ([#isUnifiedCopy]): GregTech generates no recipes for it, and `ForeignPartTable` migrates saved stacks of it.
///
/// Keyed by [OrePrefixes] throughout: an association starts from an ore-dictionary name. See [OrePrefixes]'s
/// class javadoc for why the prefix and shape spaces are not interchangeable.
public class GTOreDictUnificator {

    private static final Map<String, ItemStack> sName2StackMap = new HashMap<>();
    private static volatile Map<String, MaterialLibPair> materialLibPairs;
    private static final Map<String, ItemStack> materialLibTargets = new ConcurrentHashMap<>();
    private static volatile boolean materialLibTargetsLive;
    private static final Map<ItemStack, List<ItemStack>> sUnificationTable = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);

    private static final Set<ItemStack> unificationBlacklist = new ObjectOpenCustomHashSet<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final Set<Item> unificationWildcardBlacklist = new ReferenceOpenHashSet<>();

    /**
     * The Blacklist just prevents the Item from being unificated into something else. Useful if you have things like
     * the Industrial Diamond, which is better than regular Diamond, but also usable in absolutely all Diamond Recipes.
     */
    public static void addToBlacklist(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return;

        if (Items.feather.getDamage(stack) == WILDCARD) {
            unificationWildcardBlacklist.add(stack.getItem());
        } else {
            unificationBlacklist.add(stack);
        }
    }

    public static boolean isBlacklisted(ItemStack stack) {
        if (stack == null) return false;
        return unificationBlacklist.contains(stack) || unificationWildcardBlacklist.contains(stack.getItem());
    }

    public static void add(OrePrefixes prefix, Material material, ItemStack stack) {
        set(prefix, material, stack, false, false);
    }

    public static void set(OrePrefixes prefix, Material material, ItemStack stack) {
        set(prefix, material, stack, true, false);
    }

    /// Registers `stack` under the ore-dictionary name for `prefix` and `materialName`, makes it that name's
    /// unification target, and associates it with the composition-free ingredient for that name.
    public static void set(OrePrefixes prefix, String materialName, ItemStack stack) {
        if (materialName == null || prefix == null || GTUtility.isStackInvalid(stack)) return;
        if (Items.feather.getDamage(stack) == WILDCARD) return;

        stack = GTUtility.copyAmount(1, stack);
        String oreName = prefix.oreDictName(materialName);
        registerOre(oreName, stack);
        MaterialData.addAssociation(prefix, materialName, stack);
        sName2StackMap.put(oreName, stack);
    }

    public static void set(OrePrefixes prefix, Material material, ItemStack stack, boolean overwrite,
        boolean alreadyRegistered) {

        if (material == null || prefix == null || GTUtility.isStackInvalid(stack)) return;
        if (Items.feather.getDamage(stack) == WILDCARD) return;

        stack = GTUtility.copyAmount(1, stack);
        String oreName = prefix.oreDictName(material);
        if (!alreadyRegistered) registerOre(oreName, stack);
        MaterialData.addAssociation(prefix, material, stack);

        if (overwrite || GTUtility.isStackInvalid(sName2StackMap.get(oreName))) {
            sName2StackMap.put(oreName, stack);
        }
    }

    private record MaterialLibPair(OrePrefixes prefix, Material material) {}

    /// Every ore-dictionary name MaterialLib serves a unifiable (prefix, material) pair under. Ore blocks stay
    /// out: their shapes are stone variants with no single stack, and vanilla's ore blocks keep their names.
    private static Map<String, MaterialLibPair> materialLibPairs() {
        Map<String, MaterialLibPair> pairs = materialLibPairs;
        if (pairs == null) {
            pairs = new HashMap<>();
            for (OrePrefixes prefix : OrePrefixes.VALUES) {
                if (!prefix.isUnifiable() || prefix.getName()
                    .startsWith("ore") || MaterialParts.shape(prefix) == null) continue;
                for (Material material : MaterialLibAPI.getMaterials()) {
                    if (MaterialParts.isCutOver(prefix, material)) {
                        pairs.put(prefix.oreDictName(material), new MaterialLibPair(prefix, material));
                    }
                }
            }
            materialLibPairs = pairs;
        }
        return pairs;
    }

    /// Lets every MaterialLib pair decide its names from here on, including those a foreign item stands in for.
    /// Called at the start of GregTech's init: during preInit another mod's overriding item may not be registered
    /// yet.
    /// A foreign item standing in for a pair is associated with it here, since the ore-dictionary event that
    /// would do so may never be processed for it (`stickObsidian` is an ignored name).
    public static void useMaterialLibTargets() {
        materialLibTargetsLive = true;
        for (MaterialLibPair pair : materialLibPairs().values()) {
            if (!MaterialParts.isMinted(pair.prefix(), pair.material())) {
                MaterialData.addAssociation(
                    pair.prefix(),
                    pair.material(),
                    MaterialParts.stack(pair.prefix(), pair.material(), 1));
            }
        }
    }

    /// The stack MaterialLib serves under `oreName`, or null when it serves none, when GregTech's shapes are not
    /// registered yet, or when a foreign item stands in for the pair and [#useMaterialLibTargets] has not run yet.
    private static ItemStack materialLibTarget(String oreName) {
        ItemStack target = materialLibTargets.get(oreName);
        if (target != null) return target;
        if (GTShapeStore.all()
            .isEmpty()) return null;
        MaterialLibPair pair = materialLibPairs().get(oreName);
        if (pair == null) return null;
        if (!materialLibTargetsLive && !MaterialParts.isMinted(pair.prefix(), pair.material())) return null;
        target = MaterialParts.stack(pair.prefix(), pair.material(), 1);
        if (target != null) materialLibTargets.put(oreName, target);
        return target;
    }

    /// The item every stack registered under `name` unifies to: the pair's MaterialLib stack, or the stack GregTech
    /// set for the name. Null when neither exists, so parts of shapes GregTech does not generate are never unified.
    static ItemStack unificationTarget(String name) {
        ItemStack served = materialLibTarget(name);
        if (served != null) return served;
        ItemStack stack = sName2StackMap.get(name);
        return GTUtility.isStackValid(stack) ? stack : null;
    }

    public static ItemStack getFirstOre(Object name, long amount) {
        if (GTUtility.isStringInvalid(name)) return null;
        return GTUtility.copyAmount(amount, getFirstOre_nocopy(name.toString()));
    }

    private static ItemStack getFirstOre_nocopy(String name) {
        ItemStack served = materialLibTarget(name);
        if (served != null) return served;
        ItemStack stack = sName2StackMap.get(name);
        if (GTUtility.isStackValid(stack)) return stack;

        ItemStack firstStack = null;
        for (ItemStack ore : getOresImmutable(name)) {
            if (ore != null) {
                if (!isBlacklisted(ore)) return ore;
                if (firstStack == null) firstStack = ore;
            }
        }
        return firstStack;
    }

    /// Whether `stack` is another mod's copy of a part that unifies onto a different item. Recipes generated for such
    /// a copy would only repeat those of the item it unifies onto.
    public static boolean isUnifiedCopy(ItemStack stack) {
        return !GTUtility.isStackInvalid(stack) && !isBlacklisted(stack) && unifiedTargetOf(stack) != null;
    }

    /// The item `stack` unifies onto, or null when it unifies onto nothing but itself.
    @Nullable
    private static ItemStack unifiedTargetOf(ItemStack stack) {
        ItemData itemData = getUnifiable(stack);
        if (itemData == null) return null;
        ItemStack target = unificationTarget(itemData.toString());
        if (target == null || target.getItem() == stack.getItem() && target.getItemDamage() == stack.getItemDamage()) {
            return null;
        }
        return target;
    }

    public static List<ItemStack> getNonUnifiedStacks(Object obj) {
        if (sUnificationTable.isEmpty() && !MaterialData.stacks()
            .isEmpty()) {
            // use something akin to double check lock. this synchronization overhead is causing lag whenever my
            // 5900x tries to do NEI lookup
            synchronized (sUnificationTable) {
                if (sUnificationTable.isEmpty() && !MaterialData.stacks()
                    .isEmpty()) {
                    for (ItemStack tGTStack0 : MaterialData.stacks()) {
                        ItemStack tStack0 = GTItemStack.internalCopyStack(tGTStack0);
                        ItemStack tStack1 = unifiedTargetOf(tStack0);
                        if (tStack1 != null) {
                            List<ItemStack> list = sUnificationTable.computeIfAbsent(tStack1, k -> new ArrayList<>());
                            // greg's original code tries to dedupe the list using List#contains, which won't work
                            // on vanilla ItemStack. I removed it since it never worked and can be slow.
                            list.add(tStack0);
                        }
                    }
                }
            }
        }
        ItemStack[] stacks = GTValues.emptyItemStackArray;
        if (obj instanceof ItemStack) stacks = new ItemStack[] { (ItemStack) obj };
        else if (obj instanceof ItemStack[]) stacks = (ItemStack[]) obj;
        else if (obj instanceof List) stacks = ((List<?>) obj).toArray(new ItemStack[0]);
        List<ItemStack> rList = new ArrayList<>();
        for (ItemStack stack : stacks) {
            if (stack == null) continue;
            rList.add(stack);
            List<ItemStack> tList = sUnificationTable.get(stack);
            if (tList != null) {
                for (ItemStack tStack : tList) {
                    ItemStack tStack1 = GTUtility.copyAmountUnsafe(stack.stackSize, tStack);
                    rList.add(tStack1);
                }
            }
        }
        return rList;
    }

    /// The stack's item data when it names a unification target: a prefix+material association or a
    /// name-only one (see [ItemData#hasUnificationName]), never a bare composition.
    @Nullable
    private static ItemData getUnifiable(ItemStack stack) {
        ItemData rData = MaterialData.get(stack);
        return rData != null && rData.hasUnificationName() ? rData : null;
    }

    public static boolean isItemStackInstanceOf(ItemStack stack, Object name) {
        if (GTUtility.isStringInvalid(name) || GTUtility.isStackInvalid(stack)) return false;
        for (ItemStack tOreStack : getOresImmutable(name.toString()))
            if (GTUtility.areStacksEqual(tOreStack, stack, true)) return true;
        return false;
    }

    public static boolean registerOre(OrePrefixes prefix, Material material, ItemStack stack) {
        return registerOre(prefix.oreDictName(material), stack);
    }

    public static boolean registerOre(Object name, ItemStack stack) {
        if (name == null || GTUtility.isStackInvalid(stack)) return false;

        String tName = name.toString();

        if (GTUtility.isStringInvalid(tName)) return false;

        for (ItemStack itemStack : getOresImmutable(tName))
            if (GTUtility.areStacksEqual(itemStack, stack, true)) return false;

        OreDictionary.registerOre(tName, stack);
        return true;
    }

    /**
     * @return a Copy of the OreDictionary.getOres() List
     */
    public static ArrayList<ItemStack> getOres(OrePrefixes prefix, Material material) {
        return getOres(prefix.oreDictName(material));
    }

    /**
     * @return a Copy of the OreDictionary.getOres() List
     */
    public static ArrayList<ItemStack> getOres(Object oreName) {
        String name = oreName == null ? E : oreName.toString();
        ArrayList<ItemStack> rList = new ArrayList<>();
        if (GTUtility.isStringValid(name)) rList.addAll(OreDictionary.getOres(name));
        return rList;
    }

    /**
     * Fast version of {@link #getOres(Object)}, which doesn't call
     * {@link System#arraycopy(Object, int, Object, int, int)} in {@link ArrayList#addAll}
     */
    public static List<ItemStack> getOresImmutable(@Nullable Object oreName) {
        String name = oreName == null ? E : oreName.toString();

        return GTUtility.isStringValid(name) ? Collections.unmodifiableList(OreDictionary.getOres(name))
            : Collections.emptyList();
    }
}
