package gregtech.api.util;

import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.GTShapeStore;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialData;
import gregtech.api.material.MaterialParts;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

/// A set of oredict helpers to help with managing items that should (or should not) unify by oredict tags.
///
/// Handles other mods' versions of items that are generated through the MaterialLib system.
///
/// Unified items are excluded from recipe generation and `ForeignPartTable` governs migrated saved stacks
/// to the unification target item. To prevent items from unifying, see [#addToBlacklist].
///
/// Keyed by [OrePrefixes]. See [OrePrefixes] class javadoc for why prefixes and MaterialLib
/// [com.ruling_0.materiallib.api.Shape]s are not interchangeable.
public class OreDictUtils {

    private static volatile Map<String, MaterialLibPair> materialLibPairs;
    private static final Map<String, ItemStack> materialLibTargets = new ConcurrentHashMap<>();
    private static volatile boolean materialLibTargetsLive;

    private static final Set<ItemStack> unificationBlacklist = new ObjectOpenCustomHashSet<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final Set<Item> unificationWildcardBlacklist = new ReferenceOpenHashSet<>();

    /// Marks `stack` as never being a unified copy, for items that share an ore-dictionary name with a different item
    /// but are their own thing (the Industrial Diamond under `gemDiamond`). A recipe input holding a blacklisted item
    /// with a name-only association, such as a specific circuit or SMD component, accepts only that item.
    /// [GTModHandler#collisionProbe(String)] passes over blacklisted items.
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

    /// Registers `stack` under the ore-dictionary name of `prefix` and `material` and associates it with the pair,
    /// including for prefixes the ore-dictionary handler does not associate (buckets, bottles, molten cells).
    public static void registerAndAssociate(OrePrefixes prefix, Material material, ItemStack stack) {
        if (prefix == null || material == null || GTUtility.isStackInvalid(stack)) return;
        stack = GTUtility.copyAmount(1, stack);
        registerOre(prefix.oreDictName(material), stack);
        MaterialData.addAssociation(prefix, material, stack);
    }

    /// Registers `stack` under the ore-dictionary name of `prefix` and `materialName` with a name-only association
    /// (see [ItemData#ItemData(OrePrefixes, String)]).
    public static void registerAndAssociate(OrePrefixes prefix, String materialName, ItemStack stack) {
        if (prefix == null || materialName == null || GTUtility.isStackInvalid(stack)) return;
        stack = GTUtility.copyAmount(1, stack);
        registerOre(prefix.oreDictName(materialName), stack);
        MaterialData.addAssociation(prefix, materialName, stack);
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
    /// A foreign item standing in for a pair is associated with it and added to its prefix here, since the
    /// ore-dictionary event that would do so may never be processed for it (`stickObsidian` is an ignored name).
    public static void useMaterialLibTargets() {
        materialLibTargetsLive = true;
        for (MaterialLibPair pair : materialLibPairs().values()) {
            if (!MaterialParts.isMinted(pair.prefix(), pair.material())) {
                ItemStack stack = MaterialParts.stack(pair.prefix(), pair.material(), 1);
                MaterialData.addAssociation(pair.prefix(), pair.material(), stack);
                pair.prefix()
                    .add(stack);
            }
        }
    }

    /// The item every stack registered under `oreName` unifies to: the MaterialLib stack of the pair the name stands
    /// for, or the empty cell for `cellEmpty`, which MaterialLib serves outside any pair. Null when MaterialLib serves
    /// nothing under the name, when GregTech's shapes are not registered yet, or when a foreign item stands in for the
    /// pair and [#useMaterialLibTargets] has not run yet.
    @Nullable
    public static ItemStack unificationTarget(String oreName) {
        ItemStack target = materialLibTargets.get(oreName);
        if (target != null) return target;
        if (GTShapeStore.all()
            .isEmpty()) return null;
        MaterialLibPair pair = materialLibPairs().get(oreName);
        if (pair == null) {
            if (!oreName.equals(OrePrefixes.cell.oreDictName(Materials.Empty)) || !ItemList.Cell_Empty.hasBeenSet()) {
                return null;
            }
            target = ItemList.Cell_Empty.get(1);
        } else {
            if (!materialLibTargetsLive && !MaterialParts.isMinted(pair.prefix(), pair.material())) return null;
            target = MaterialParts.stack(pair.prefix(), pair.material(), 1);
        }
        if (target != null) materialLibTargets.put(oreName, target);
        return target;
    }

    /// Whether `stack` is another mod's copy of a part that unifies onto a different item. Recipes generated for such
    /// a copy would only repeat those of the item it unifies onto.
    public static boolean isUnifiedCopy(ItemStack stack) {
        return !GTUtility.isStackInvalid(stack) && !staysDistinct(stack) && unifiedTargetOf(stack) != null;
    }

    /// Whether `stack` stays in play under a name that stands for a different item (see [#staysDistinct]), while
    /// recipe inputs holding that other item accept it.
    public static boolean isInterchangeableWithTarget(ItemStack stack) {
        return !GTUtility.isStackInvalid(stack) && staysDistinct(stack) && unifiedTargetOf(stack) != null;
    }

    /// Whether `stack` is never a unified copy: blacklisted, or a block. Blocks are placed in the world, and other
    /// mods' storage and decorative blocks stay obtainable in their own right.
    private static boolean staysDistinct(ItemStack stack) {
        return isBlacklisted(stack) || GTUtility.getBlockFromStack(stack) != Blocks.air;
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

    /// The stack's item data when it names a unification target: a prefix+material association or a
    /// name-only one (see [ItemData#hasUnificationName]), never a bare composition.
    @Nullable
    private static ItemData getUnifiable(ItemStack stack) {
        ItemData rData = MaterialData.get(stack);
        return rData != null && rData.hasUnificationName() ? rData : null;
    }

    /// The ore-dictionary name of `stack`'s association, or null when it has none. Recipe inputs with the same
    /// association name are interchangeable.
    @Nullable
    public static String associationName(@Nullable ItemStack stack) {
        ItemData data = MaterialData.get(stack);
        return data != null && data.hasUnificationName() ? data.toString() : null;
    }

    /// The association name by which a recipe input holding `stack` accepts other items, or null when it accepts
    /// only `stack`: when `stack` has no association, or is blacklisted with a name-only association.
    @Nullable
    public static String inputAssociationName(@Nullable ItemStack stack) {
        ItemData data = MaterialData.get(stack);
        if (data == null || !data.hasUnificationName() || data.isNameOnly() && isBlacklisted(stack)) return null;
        return data.toString();
    }

    /// `stack` followed by every other obtainable item that shares its association: what a recipe input holding
    /// `stack` also accepts. Unified copies are left out.
    public static List<ItemStack> getInterchangeableStacks(ItemStack stack) {
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(stack);
        String name = inputAssociationName(stack);
        if (name == null) return stacks;
        for (ItemStack ore : OreDictionary.getOres(name)) {
            if (ore == null || GTUtility.areStacksEqual(ore, stack, true) || isUnifiedCopy(ore)) continue;
            if (name.equals(associationName(ore))) stacks.add(GTUtility.copyAmountUnsafe(stack.stackSize, ore));
        }
        return stacks;
    }

    public static boolean registerOre(OrePrefixes prefix, Material material, ItemStack stack) {
        return registerOre(prefix.oreDictName(material), stack);
    }

    public static boolean registerOre(Object name, ItemStack stack) {
        if (name == null || GTUtility.isStackInvalid(stack)) return false;

        String tName = name.toString();

        if (GTUtility.isStringInvalid(tName)) return false;

        for (ItemStack itemStack : OreDictionary.getOres(tName))
            if (GTUtility.areStacksEqual(itemStack, stack, true)) return false;

        OreDictionary.registerOre(tName, stack);
        return true;
    }

}
