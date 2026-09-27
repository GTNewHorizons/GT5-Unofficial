package gregtech.api.util;

import static gregtech.GTLoggers.GT_ORE_DICT_LOGGER;
import static gregtech.api.enums.GTValues.E;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.GTShapeStore;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

/// NEVER INCLUDE THIS FILE IN YOUR MOD!!!
///
/// This is the Core of my OreDict Unification Code
///
/// If you just want to use this to unificate your Items, then use the Function in the GregTechAPI File
///
/// P.S. It is intended to be named "Unificator" and not "Unifier", because that sounds more awesome.
///
/// Two questions: which stack is canonical for an ore-dictionary name, and what material any stack is made of
/// ([ItemData]).
///
/// MaterialLib answers the first for every name of a (prefix, material) pair it serves: the pair's stack,
/// MaterialLib's own item or the foreign item a shape override declares, is the unification target, whatever
/// else registers under the name and in whatever order. A pair MaterialLib's own item serves answers from
/// GregTech's preInit on; one a foreign item stands in for answers from GregTech's init, once that item is
/// registered (see [#useMaterialLibTargets]). Only names with no such pair (containers, name-only
/// circuits, ore blocks, other mods' parts of shapes GregTech does not generate) are decided here, by
/// [#set(OrePrefixes, Material, ItemStack)] or else by the first registered stack that is not blacklisted.
///
/// Keyed by [OrePrefixes] throughout: an association starts from an ore-dictionary name. See [OrePrefixes]'s
/// class javadoc for why the prefix and shape spaces are not interchangeable.
public class GTOreDictUnificator {

    private static final Map<String, ItemStack> sName2StackMap = new HashMap<>();
    private static volatile Map<String, MaterialLibPair> materialLibPairs;
    private static final Map<String, ItemStack> materialLibTargets = new ConcurrentHashMap<>();
    private static volatile boolean materialLibTargetsLive;
    private static final Map<ItemStack, ItemData> sItemStack2DataMap = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final Map<ItemStack, List<ItemStack>> sUnificationTable = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);

    private static final Set<ItemStack> unificationBlacklist = new ObjectOpenCustomHashSet<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final Set<Item> unificationWildcardBlacklist = new ReferenceOpenHashSet<>();

    private static int isRegisteringOre = 0, isAddingOre = 0;
    private static boolean batchRegisteredRecyclingRecipes = false;

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

        isAddingOre++;
        stack = GTUtility.copyAmount(1, stack);
        String oreName = prefix.oreDictName(materialName);
        registerOre(oreName, stack);
        addAssociation(prefix, materialName, stack);
        sName2StackMap.put(oreName, stack);
        updateUnificationTarget(oreName, stack);
        isAddingOre--;
    }

    public static void set(OrePrefixes prefix, Material material, ItemStack stack, boolean overwrite,
        boolean alreadyRegistered) {

        if (material == null || prefix == null || GTUtility.isStackInvalid(stack)) return;
        if (Items.feather.getDamage(stack) == WILDCARD) return;

        isAddingOre++;
        stack = GTUtility.copyAmount(1, stack);
        String oreName = prefix.oreDictName(material);
        if (!alreadyRegistered) registerOre(oreName, stack);
        addAssociation(prefix, material, stack);

        if (overwrite || GTUtility.isStackInvalid(sName2StackMap.get(oreName))) {
            sName2StackMap.put(oreName, stack);
            updateUnificationTarget(oreName, stack);
        }
        isAddingOre--;
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

    /// Lets every MaterialLib pair decide its names from here on, including those a foreign item stands in for,
    /// and drops every target resolved before. Called at the start of GregTech's init: during preInit another
    /// mod's overriding item may not be registered yet.
    /// A foreign item standing in for a pair is associated with it here, since the ore-dictionary event that
    /// would do so may never be processed for it (`stickObsidian` is an ignored name).
    public static void useMaterialLibTargets() {
        materialLibTargetsLive = true;
        resetUnificationEntries();
        for (MaterialLibPair pair : materialLibPairs().values()) {
            if (!MaterialParts.isMinted(pair.prefix(), pair.material())) {
                addAssociation(pair.prefix(), pair.material(), MaterialParts.stack(pair.prefix(), pair.material(), 1));
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
    private static ItemStack unificationTarget(String name) {
        ItemStack served = materialLibTarget(name);
        if (served != null) return served;
        ItemStack stack = sName2StackMap.get(name);
        return GTUtility.isStackValid(stack) ? stack : null;
    }

    public static ItemStack getFirstOre(Object name, long amount) {
        if (GTUtility.isStringInvalid(name)) return null;
        return GTUtility.copyAmount(amount, getFirstOre_nocopy(name.toString()));
    }

    public static ItemStack getFirstOre_nocopy(String name) {
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

    public static ItemStack get(Object name, long amount) {
        return get(name, null, amount, true, true);
    }

    public static ItemStack get(Object name, ItemStack replacement, long amount) {
        return get(name, replacement, amount, true, true);
    }

    public static ItemStack get(OrePrefixes prefix, Material material, long amount) {
        return get(prefix, material, null, amount);
    }

    public static ItemStack get(OrePrefixes prefix, Material material, ItemStack replacement, long amount) {
        if (material == null) return replacement;
        if (OrePrefixes.mPreventableComponents.contains(prefix) && prefix.mDisabledItems.contains(material))
            return replacement;
        return get(prefix.oreDictName(material), replacement, amount, false, true);
    }

    public static ItemStack get(OrePrefixes prefix, Material material, long amount, boolean noInvalidAmounts) {
        if (material == null) return null;
        if (OrePrefixes.mPreventableComponents.contains(prefix) && prefix.mDisabledItems.contains(material))
            return null;
        return get(prefix.oreDictName(material), null, amount, false, noInvalidAmounts);
    }

    public static ItemStack get(Object aName, ItemStack aReplacement, long aAmount, boolean aMentionPossibleTypos,
        boolean aNoInvalidAmounts) {

        if (aNoInvalidAmounts && aAmount < 1) return null;

        final ItemStack stackFirstOre = getFirstOre(aName, aAmount);
        if (stackFirstOre != null) return GTUtility.copyAmount(aAmount, stackFirstOre);

        if (aMentionPossibleTypos) GT_ORE_DICT_LOGGER.error("Unknown Key for Unification, Typo? {}", aName);
        return GTUtility.copyAmount(aAmount, aReplacement);
    }

    /**
     * Wrapper for setStackArray that assumes safe copying
     */
    public static ItemStack[] setStackArray(boolean useBlackList, ItemStack... stacks) {
        return setStackArray(useBlackList, false, stacks);
    }

    public static ItemStack[] setStackArray(boolean useBlackList, boolean unsafe, ItemStack... stacks) {
        for (int i = 0; i < stacks.length; i++) stacks[i] = get(useBlackList, GTUtility.copyOrNull(stacks[i]), unsafe);
        return stacks;
    }

    public static ItemStack[] getStackArray(boolean useBlackList, Object... stacks) {
        ItemStack[] rStacks = new ItemStack[stacks.length];
        for (int i = 0; i < stacks.length; i++) {
            rStacks[i] = get(useBlackList, GTUtility.copy(stacks[i]), true);
        }
        return rStacks;
    }

    public static ItemStack setStack(ItemStack stack) {
        return setStack(true, stack);
    }

    public static ItemStack setStack(boolean useBlackList, ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return stack;
        ItemStack tStack = get(useBlackList, stack);
        if (GTUtility.areStacksEqual(stack, tStack)) return stack;
        stack.func_150996_a(tStack.getItem());
        Items.feather.setDamage(stack, Items.feather.getDamage(tStack));
        return stack;
    }

    public static ItemStack get(ItemStack stack) {
        return get(true, stack);
    }

    public static ItemStack get(boolean useBlackList, ItemStack stack) {
        return get(useBlackList, stack, false);
    }

    /**
     * @param unsafe If true, it does not limit stack size to 64.
     */
    public static ItemStack get(boolean useBlackList, ItemStack stack, boolean unsafe) {
        if (GTUtility.isStackInvalid(stack)) return null;
        ItemData itemData = getUnifiable(stack);
        if (itemData == null || !itemData.hasUnificationName() || (useBlackList && isBlacklisted(stack))) {
            return GTUtility.copyOrNull(stack);
        }
        if (itemData.mUnificationTarget == null) {
            itemData.mUnificationTarget = unificationTarget(itemData.toString());
        }
        final ItemStack rStack = itemData.mUnificationTarget;
        if (GTUtility.isStackInvalid(rStack)) {
            return GTUtility.copyOrNull(stack);
        }
        final ItemStack newStack;
        if (unsafe) {
            newStack = GTUtility.copyAmountUnsafe(stack.stackSize, rStack);
        } else {
            newStack = GTUtility.copyAmount(stack.stackSize, rStack);
        }
        // NBT is assigned by reference here, so mutating it may have unexpected side effects.
        if (newStack != null) {
            newStack.setTagCompound(stack.getTagCompound());
        }
        return newStack;
    }

    /**
     * Doesn't always copy the returned stack or set quantity. Be careful and do not mutate it
     */
    public static ItemStack get_nocopy(ItemStack stack) {
        return get_nocopy(true, stack);
    }

    /**
     * Doesn't always copy the returned stack or set quantity. Be careful and do not mutate it
     */
    public static ItemStack get_nocopy(boolean useBlackList, ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return null;
        ItemData itemData = getUnifiable(stack);
        if (itemData == null || !itemData.hasUnificationName() || (useBlackList && isBlacklisted(stack))) {
            return stack;
        }
        if (itemData.mUnificationTarget == null) {
            itemData.mUnificationTarget = unificationTarget(itemData.toString());
        }
        ItemStack rStack = itemData.mUnificationTarget;
        if (GTUtility.isStackInvalid(rStack)) {
            return stack;
        }

        // Yes, == and not .equals().
        // This check is primarily intended to optimize for the case where both rStack and stack
        // do not have NBT, and so we would be comparing null == null.
        //
        // Even if stack and rStack may have equal NBT, we prefer to do an inexpensive
        // new ItemStack() over the potentially expensive NBTTagCompound.equals().
        if (stack.getTagCompound() == rStack.getTagCompound()) {
            // Warning: rStack's stack size may not be equal to stack's stack size.
            return rStack;
        }

        // Okay, okay, I lied, we actually do need to make a copy.
        // This is to fix a long-standing bug where we were mutating NBT directly on rStack,
        // which had unexpected and unpredictable ripple effects.
        //
        // We will do some custom copying here, to avoid ItemStack.copy(),
        // which calls the potentially expensive NBTTagCompound.copy()
        // NBT is assigned by reference here, so mutating it may have unexpected side effects.
        ItemStack newStack = new ItemStack(rStack.getItem(), stack.stackSize, Items.feather.getDamage(rStack));
        newStack.setTagCompound(stack.getTagCompound());
        return newStack;
    }

    /**
     * Compares the first argument against an already-unificated second argument as if aUseBlackList was both true and
     * false.
     */
    public static boolean isInputStackEqual(ItemStack stack, ItemStack unified_tStack) {
        if (GTUtility.isStackInvalid(stack)) return false;
        return isInputStackEqual(stack, getItemData(stack), unified_tStack);
    }

    /**
     * Compares the first argument against an already-unificated second argument as if aUseBlackList was both true and
     * false.
     */
    public static boolean isInputStackEqual(ItemStack stack, ItemData stackPrefixData, ItemStack unified_tStack) {
        boolean alreadyCompared = false;
        if (GTUtility.isStackInvalid(stack)) return false;
        ItemStack rStack = null;
        if (stackPrefixData == null || !stackPrefixData.hasUnificationName())
            return GTUtility.areStacksEqual(stack, unified_tStack, true);
        else if (isBlacklisted(stack)) {
            if (GTUtility.areStacksEqual(stack, unified_tStack, true)) return true;
            else alreadyCompared = true;
        }
        if (stackPrefixData.mUnificationTarget == null) {
            stackPrefixData.mUnificationTarget = unificationTarget(stackPrefixData.toString());
        }
        rStack = stackPrefixData.mUnificationTarget;
        if (GTUtility.isStackInvalid(rStack))
            return !alreadyCompared && GTUtility.areStacksEqual(stack, unified_tStack, true);
        return GTUtility.areStacksEqual(rStack, unified_tStack, true);
    }

    public static List<ItemStack> getNonUnifiedStacks(Object obj) {
        if (sUnificationTable.isEmpty() && !sItemStack2DataMap.isEmpty()) {
            // use something akin to double check lock. this synchronization overhead is causing lag whenever my
            // 5900x tries to do NEI lookup
            synchronized (sUnificationTable) {
                if (sUnificationTable.isEmpty() && !sItemStack2DataMap.isEmpty()) {
                    for (ItemStack tGTStack0 : sItemStack2DataMap.keySet()) {
                        ItemStack tStack0 = GTItemStack.internalCopyStack(tGTStack0);
                        ItemStack tStack1 = get_nocopy(false, tStack0);
                        if (!GTUtility.areStacksEqual(tStack0, tStack1)) {
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

    /**
     * Adds material composition data for a stack. Does not override a composition when it already exists.
     */
    public static void addItemData(ItemStack stack, ItemData data) {
        if (GTUtility.isStackInvalid(stack) || data == null) return;

        ItemData prevData = getItemData(stack);
        if (prevData == null || !prevData.hasExplicitComposition) {
            setItemData(stack, data);
        }
    }

    /**
     * Adds material composition data for a stack. Does not override a composition when it already exists.
     */
    public static void addItemDataFromInputs(ItemStack output, Object... inputs) {
        int length = inputs.length;
        ItemData[] dataInputs = new ItemData[length];
        for (int i = 0; i < length; i++) {
            if (inputs[i] instanceof ItemStack stack) {
                dataInputs[i] = getItemData(stack);
            } else if (inputs[i] instanceof ItemData data) {
                dataInputs[i] = data;
            } else {
                throw new IllegalArgumentException("Illegal item data: " + inputs[i]);
            }
        }
        if (GTUtility.arrayContainsNonNull(dataInputs)) {
            addItemData(output, new ItemData(dataInputs));
        }
    }

    /**
     * Sets material composition data for a stack, for example from stack recipe ingredients.
     */
    public static void setItemData(ItemStack stack, ItemData data) {
        if (GTUtility.isStackInvalid(stack) || data == null) return;

        if (stack.stackSize > 1) {
            if (data.mMaterial != null) data.mMaterial.mAmount /= stack.stackSize;
            for (MaterialStack material : data.mByProducts) {
                material.mAmount /= stack.stackSize;
            }
            stack = GTUtility.copyAmount(1, stack);
        }

        // Preserve association when it already exists
        ItemData prevData = getItemData(stack);
        if (prevData != null && prevData.hasValidPrefixMaterialData()) {
            data = new ItemData(prevData, data);
        }

        sItemStack2DataMap.put(stack, data);
        processRecycling(stack, data, prevData);
    }

    private static void processRecycling(ItemStack stack, ItemData data, ItemData prevData) {
        if (data.hasValidMaterialData()) {
            long recyclableMaterialAmount = 0;

            if (!MaterialUtils.hasFlag(data.mMaterial.mMaterial, GTMaterialFlag.NO_RECYCLING)) {
                recyclableMaterialAmount += data.mMaterial.mAmount >= 0 ? data.mMaterial.mAmount : M;
            }

            for (MaterialStack material : data.mByProducts) {
                if (!MaterialUtils.hasFlag(material.mMaterial, GTMaterialFlag.NO_RECYCLING)) {
                    recyclableMaterialAmount += material.mAmount >= 0 ? material.mAmount : M;
                }
            }

            if (recyclableMaterialAmount < M) {
                GTModHandler.addToRecyclerBlackList(stack);
            }
        }

        if (!batchRegisteredRecyclingRecipes) {
            if (!GregTechAPI.sLoadStarted) return;
            batchRegisteredRecyclingRecipes = true;

            for (Entry<ItemStack, ItemData> entry : sItemStack2DataMap.entrySet()) {
                ItemStack entryStack = entry.getKey();
                ItemData entryData = entry.getValue();

                if (shouldRegisterMaterialRecycling(entryData)) {
                    GTRecipeRegistrator.registerMaterialRecycling(entryStack, entryData);
                }
            }
        } else if (shouldRegisterMaterialRecycling(data) && !shouldRegisterMaterialRecycling(prevData)) {
            GTRecipeRegistrator.registerMaterialRecycling(stack, data);
        }
    }

    private static boolean shouldRegisterMaterialRecycling(ItemData data) {
        if (data == null) return false;
        return data.hasExplicitComposition || !data.hasValidPrefixData() || data.mPrefix.isRecyclable();
    }

    public static void removeItemData(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) {
            return;
        }
        sItemStack2DataMap.remove(stack);
    }

    /// Adds an ore-dictionary association for a stack: the prefix and material an ore-dictionary name stands
    /// for, e.g. `plateIron` as `OrePrefixes.plate` + `Materials.Iron`, which lookup and unification key on.
    ///
    /// An existing association is never rewritten. An existing explicit composition is merged into the
    /// association; when the two name different materials, the association's wins.
    public static void addAssociation(OrePrefixes prefix, Material material, ItemStack stack) {
        if (prefix == null || material == null || GTUtility.isStackInvalid(stack)) return;
        addAssociation(() -> new ItemData(prefix, material), stack);
    }

    /// Adds a name-only association (see [ItemData#ItemData(OrePrefixes, String)]) for the ore-dictionary
    /// entries whose material side is not a material at all.
    public static void addAssociation(OrePrefixes prefix, String materialName, ItemStack stack) {
        if (prefix == null || materialName == null || GTUtility.isStackInvalid(stack)) return;
        addAssociation(() -> new ItemData(prefix, materialName), stack);
    }

    private static void addAssociation(Supplier<ItemData> association, ItemStack stack) {
        if (Items.feather.getDamage(stack) == WILDCARD) {
            for (byte i = 0; i < 16; i++) {
                addAssociationForStack(association.get(), GTUtility.copyAmountAndMetaData(1, i, stack));
            }
        }

        addAssociationForStack(association.get(), stack);
    }

    private static void addAssociationForStack(ItemData association, ItemStack stack) {
        ItemData prevData = getItemData(stack);

        // Keep the existing OreDict association
        if (prevData != null && prevData.hasUnificationName()) {
            return;
        }

        // Merge association with existing composition
        if (prevData != null && prevData.hasExplicitComposition) {
            association = new ItemData(association, prevData);
        }

        setItemData(stack, association);
    }

    @Nullable
    public static ItemData getItemData(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return null;
        ItemData rData = sItemStack2DataMap.get(stack);
        if (rData == null) { // Try the lookup again but with wildcard damage value
            rData = sItemStack2DataMap.get(GTItemStack.internalCopyStack(stack, true));
        }
        return rData;
    }

    @Nullable
    public static ItemData getAssociation(ItemStack stack) {
        ItemData rData = getItemData(stack);
        return rData != null && rData.hasValidPrefixMaterialData() ? rData : null;
    }

    /// The stack's item data when it names a unification target: a prefix+material association or a
    /// name-only one (see [ItemData#hasUnificationName]), never a bare composition.
    @Nullable
    private static ItemData getUnifiable(ItemStack stack) {
        ItemData rData = getItemData(stack);
        return rData != null && rData.hasUnificationName() ? rData : null;
    }

    public static boolean isItemStackInstanceOf(ItemStack stack, Object name) {
        if (GTUtility.isStringInvalid(name) || GTUtility.isStackInvalid(stack)) return false;
        for (ItemStack tOreStack : getOresImmutable(name.toString()))
            if (GTUtility.areStacksEqual(tOreStack, stack, true)) return true;
        return false;
    }

    public static boolean isItemStackDye(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return false;

        for (Dyes tDye : Dyes.VALUES) if (isItemStackInstanceOf(stack, tDye.toString())) return true;

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

        isRegisteringOre++;
        OreDictionary.registerOre(tName, stack);
        isRegisteringOre--;
        return true;
    }

    public static boolean isRegisteringOres() {
        return isRegisteringOre > 0;
    }

    public static boolean isAddingOres() {
        return isAddingOre > 0;
    }

    public static void resetUnificationEntries() {
        for (ItemData tPrefixMaterial : sItemStack2DataMap.values()) tPrefixMaterial.mUnificationTarget = null;
    }

    private static void updateUnificationTarget(String oreName, ItemStack target) {
        ItemStack served = materialLibTarget(oreName);
        if (served != null) target = served;
        for (ItemStack stack : getOresImmutable(oreName)) {
            updateUnificationTarget(stack, target);
        }
    }

    private static void updateUnificationTarget(ItemStack stack, ItemStack target) {
        ItemData data = getUnifiable(stack);
        if (data != null) {
            data.mUnificationTarget = target;
        }

        if (Items.feather.getDamage(stack) == WILDCARD) {
            ItemStack lookupStack = new ItemStack(stack.getItem(), 1, 0);
            for (byte meta = 0; meta < 16; meta++) {
                lookupStack.setItemDamage(meta);
                data = getUnifiable(lookupStack);
                if (data != null) {
                    data.mUnificationTarget = target;
                }
            }
        }
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

    public static Map<String, ItemStack> getName2StackMap() {
        return sName2StackMap;
    }
}
