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

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MU;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the Core of my OreDict Unification Code
 * <p/>
 * If you just want to use this to unificate your Items, then use the Function in the GregTechAPI File
 * <p/>
 * P.S. It is intended to be named "Unificator" and not "Unifier", because that sounds more awesome.
 */
public class GTOreDictUnificator {

    private static final Map<String, ItemStack> sName2StackMap = new HashMap<>();
    private static final Map<ItemStack, ItemData> sItemStack2DataMap = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final Map<ItemStack, List<ItemStack>> sUnificationTable = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final Set<ItemStack> sNoUnificationList = new ObjectOpenCustomHashSet<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static int isRegisteringOre = 0, isAddingOre = 0;
    private static boolean mRunThroughTheList = true;

    /**
     * The Blacklist just prevents the Item from being unificated into something else. Useful if you have things like
     * the Industrial Diamond, which is better than regular Diamond, but also usable in absolutely all Diamond Recipes.
     */
    public static void addToBlacklist(ItemStack stack) {
        if (GTUtility.isStackValid(stack) && !GTUtility.isStackInStackSet(stack, sNoUnificationList))
            sNoUnificationList.add(stack);
    }

    public static boolean isBlacklisted(ItemStack stack) {
        return GTUtility.isStackInStackSet(stack, sNoUnificationList);
    }

    public static void add(OrePrefixes prefix, Material material, ItemStack stack) {
        set(prefix, material, stack, false, false);
    }

    /// Transitional: accepts the legacy material types through [MU#toMaterial] until every caller passes a
    /// [Material] directly.
    public static void add(OrePrefixes prefix, IOreMaterial material, ItemStack stack) {
        set(prefix, MU.toMaterial(material), stack, false, false);
    }

    public static void set(OrePrefixes prefix, Material material, ItemStack stack) {
        set(prefix, material, stack, true, false);
    }

    public static void set(OrePrefixes prefix, IOreMaterial material, ItemStack stack) {
        set(prefix, MU.toMaterial(material), stack, true, false);
    }

    /// Registers `stack` under the ore-dictionary name for `prefix` and `materialName`, makes it that name's
    /// unification target, and associates it with the composition-free ingredient for that name.
    public static void set(OrePrefixes prefix, String materialName, ItemStack stack) {
        if (materialName == null || prefix == null
            || GTUtility.isStackInvalid(stack)
            || Items.feather.getDamage(stack) == WILDCARD) return;
        isAddingOre++;
        stack = GTUtility.copyAmount(1, stack);
        String name = prefix.oreDictName(materialName);
        registerOre(name, stack);
        setItemData(stack, new ItemData(prefix, materialName));
        sName2StackMap.put(name, stack);
        isAddingOre--;
    }

    public static void set(OrePrefixes prefix, IOreMaterial material, ItemStack stack, boolean overwrite,
        boolean alreadyRegistered) {
        set(prefix, MU.toMaterial(material), stack, overwrite, alreadyRegistered);
    }

    public static void set(OrePrefixes prefix, Material material, ItemStack stack, boolean overwrite,
        boolean alreadyRegistered) {
        if (material == null || prefix == null
            || GTUtility.isStackInvalid(stack)
            || Items.feather.getDamage(stack) == WILDCARD) return;
        isAddingOre++;
        stack = GTUtility.copyAmount(1, stack);
        if (!alreadyRegistered) registerOre(prefix.oreDictName(material), stack);
        addAssociation(prefix, material, stack, isBlacklisted(stack));
        if (overwrite || GTUtility.isStackInvalid(sName2StackMap.get(prefix.oreDictName(material))))
            sName2StackMap.put(prefix.oreDictName(material), stack);
        isAddingOre--;
    }

    public static ItemStack getFirstOre(Object name, long amount) {
        if (GTUtility.isStringInvalid(name)) return null;
        ItemStack tStack = sName2StackMap.get(name.toString());
        if (GTUtility.isStackValid(tStack)) return GTUtility.copyAmount(amount, tStack);
        return GTUtility.copyAmount(amount, getOresImmutable(name).toArray());
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

    /// Transitional: accepts the legacy material types through [MU#toMaterial] until every caller passes a
    /// [Material] directly.
    public static ItemStack get(OrePrefixes prefix, IOreMaterial material, long amount) {
        return get(prefix, MU.toMaterial(material), null, amount);
    }

    public static ItemStack get(OrePrefixes prefix, Material material, ItemStack replacement, long amount) {
        if (OrePrefixes.mPreventableComponents.contains(prefix)
            && prefix.mDisabledItems.contains(MU.materialOf(material))) return replacement;
        return get(prefix.oreDictName(material), replacement, amount, false, true);
    }

    public static ItemStack get(OrePrefixes prefix, IOreMaterial material, ItemStack replacement, long amount) {
        return get(prefix, MU.toMaterial(material), replacement, amount);
    }

    public static ItemStack get(OrePrefixes prefix, Material material, long amount, boolean noInvalidAmounts) {
        if (OrePrefixes.mPreventableComponents.contains(prefix)
            && prefix.mDisabledItems.contains(MU.materialOf(material))) return null;
        return get(prefix.oreDictName(material), null, amount, false, noInvalidAmounts);
    }

    public static ItemStack get(OrePrefixes prefix, IOreMaterial material, long amount, boolean noInvalidAmounts) {
        return get(prefix, MU.toMaterial(material), amount, noInvalidAmounts);
    }

    public static ItemStack get(Object name, ItemStack replacement, long amount, boolean mentionPossibleTypos,
        boolean noInvalidAmounts) {
        if (noInvalidAmounts && amount < 1) return null;
        final ItemStack stackFromName = sName2StackMap.get(name.toString());
        if (stackFromName != null) return GTUtility.copyAmount(amount, stackFromName);
        if (mentionPossibleTypos) {
            GT_ORE_DICT_LOGGER.error("Unknown Key for Unification, Typo? {}", name);
        }
        final ItemStack stackFirstOre = getFirstOre(name, amount);
        if (stackFirstOre != null) return GTUtility.copyAmount(amount, stackFirstOre);
        return GTUtility.copyAmount(amount, replacement);
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
        ItemData itemData = getAssociation(stack);
        if (itemData == null || !itemData.hasValidPrefixMaterialData() || (useBlackList && itemData.mBlackListed)) {
            return GTUtility.copyOrNull(stack);
        }
        if (useBlackList && !GregTechAPI.sUnificationEntriesRegistered && isBlacklisted(stack)) {
            itemData.mBlackListed = true;
            return GTUtility.copyOrNull(stack);
        }
        if (itemData.mUnificationTarget == null) {
            itemData.mUnificationTarget = sName2StackMap.get(itemData.toString());
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
        ItemData itemData = getAssociation(stack);
        if (itemData == null || !itemData.hasValidPrefixMaterialData() || (useBlackList && itemData.mBlackListed)) {
            return stack;
        }
        if (useBlackList && !GregTechAPI.sUnificationEntriesRegistered && isBlacklisted(stack)) {
            itemData.mBlackListed = true;
            return stack;
        }
        if (itemData.mUnificationTarget == null) {
            itemData.mUnificationTarget = sName2StackMap.get(itemData.toString());
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
        return isInputStackEqual(stack, getAssociation(stack), unified_tStack);
    }

    /**
     * Compares the first argument against an already-unificated second argument as if aUseBlackList was both true and
     * false.
     */
    public static boolean isInputStackEqual(ItemStack stack, ItemData stackPrefixData, ItemStack unified_tStack) {
        boolean alreadyCompared = false;
        if (GTUtility.isStackInvalid(stack)) return false;
        ItemStack rStack = null;
        if (stackPrefixData == null || !stackPrefixData.hasValidPrefixMaterialData())
            return GTUtility.areStacksEqual(stack, unified_tStack, true);
        else if (stackPrefixData.mBlackListed) {
            if (GTUtility.areStacksEqual(stack, unified_tStack, true)) return true;
            else alreadyCompared = true;
        }
        if (!alreadyCompared && !GregTechAPI.sUnificationEntriesRegistered && isBlacklisted(stack)) {
            stackPrefixData.mBlackListed = true;
            if (GTUtility.areStacksEqual(stack, unified_tStack, true)) return true;
            else alreadyCompared = true;
        }
        if (stackPrefixData.mUnificationTarget == null)
            stackPrefixData.mUnificationTarget = sName2StackMap.get(stackPrefixData.toString());
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

    public static void addItemData(ItemStack stack, ItemData data) {
        if (GTUtility.isStackValid(stack) && getItemData(stack) == null && data != null) setItemData(stack, data);
    }

    public static void addItemDataFromInputs(ItemStack output, Object... inputs) {
        int length = inputs.length;
        ItemData[] tData = new ItemData[length];
        for (int i = 0; i < length; i++) {
            if (inputs[i] instanceof ItemStack) {
                tData[i] = GTOreDictUnificator.getItemData((ItemStack) inputs[i]);
            } else if (inputs[i] instanceof ItemData) {
                tData[i] = (ItemData) inputs[i];
            } else {
                throw new IllegalArgumentException("Illegal item data: " + inputs[i]);
            }
        }
        if (GTUtility.arrayContainsNonNull(tData)) {
            GTOreDictUnificator.addItemData(output, new ItemData(tData));
        }
    }

    public static void setItemData(ItemStack stack, ItemData data) {
        if (GTUtility.isStackInvalid(stack) || data == null) return;
        ItemData tData = getItemData(stack);
        if (tData == null || !tData.hasValidPrefixMaterialData()) {
            if (tData != null) for (Object tObject : tData.mExtraData)
                if (!data.mExtraData.contains(tObject)) data.mExtraData.add(tObject);
            if (stack.stackSize > 1) {
                if (data.mMaterial != null) data.mMaterial.mAmount /= stack.stackSize;
                for (MaterialStack tMaterial : data.mByProducts) tMaterial.mAmount /= stack.stackSize;
                stack = GTUtility.copyAmount(1, stack);
            }
            sItemStack2DataMap.put(stack, data);
            if (data.hasValidMaterialData()) {
                long tValidMaterialAmount = MU.hasFlag(data.mMaterial.mMaterial, GTMaterialFlag.NO_RECYCLING) ? 0
                    : data.mMaterial.mAmount >= 0 ? data.mMaterial.mAmount : M;
                for (MaterialStack tMaterial : data.mByProducts)
                    tValidMaterialAmount += MU.hasFlag(tMaterial.mMaterial, GTMaterialFlag.NO_RECYCLING) ? 0
                        : tMaterial.mAmount >= 0 ? tMaterial.mAmount : M;
                if (tValidMaterialAmount < M) GTModHandler.addToRecyclerBlackList(stack);
            }
            if (mRunThroughTheList) {
                if (GregTechAPI.sLoadStarted) {
                    mRunThroughTheList = false;
                    for (Entry<ItemStack, ItemData> tEntry : sItemStack2DataMap.entrySet()) if (!tEntry.getValue()
                        .hasValidPrefixData() || tEntry.getValue().mPrefix.isRecyclable())
                        GTRecipeRegistrator.registerMaterialRecycling(
                            GTItemStack.internalCopyStack(tEntry.getKey()),
                            tEntry.getValue());
                }
            } else {
                if (!data.hasValidPrefixData() || data.mPrefix.isRecyclable())
                    GTRecipeRegistrator.registerMaterialRecycling(stack, data);
            }
        } else {
            for (Object tObject : data.mExtraData)
                if (!tData.mExtraData.contains(tObject)) tData.mExtraData.add(tObject);
        }
    }

    public static void removeItemData(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) {
            return;
        }
        sItemStack2DataMap.remove(stack);
    }

    public static void addAssociation(OrePrefixes prefix, Material material, ItemStack stack, boolean blackListed) {
        if (prefix == null || material == null || GTUtility.isStackInvalid(stack)) return;
        if (Items.feather.getDamage(stack) == WILDCARD) for (byte i = 0; i < 16; i++)
            setItemData(GTUtility.copyAmountAndMetaData(1, i, stack), new ItemData(prefix, material, blackListed));
        setItemData(stack, new ItemData(prefix, material, blackListed));
    }

    /// Transitional: accepts the legacy material types through [MU#toMaterial] until every caller passes a
    /// [Material] directly.
    public static void addAssociation(OrePrefixes prefix, IOreMaterial material, ItemStack stack, boolean blackListed) {
        addAssociation(prefix, MU.toMaterial(material), stack, blackListed);
    }

    public static void addAssociation(OrePrefixes prefix, String materialName, ItemStack stack, boolean blackListed) {
        if (prefix == null || materialName == null || GTUtility.isStackInvalid(stack)) return;
        if (Items.feather.getDamage(stack) == WILDCARD) for (byte i = 0; i < 16; i++)
            setItemData(GTUtility.copyAmountAndMetaData(1, i, stack), new ItemData(prefix, materialName, blackListed));
        setItemData(stack, new ItemData(prefix, materialName, blackListed));
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

    /// Transitional: accepts the legacy material types until every caller passes a [Material] directly. The
    /// legacy name is used directly rather than through [MU#toMaterial] so an unbacked legacy material still
    /// registers under its own name.
    public static boolean registerOre(OrePrefixes prefix, IOreMaterial material, ItemStack stack) {
        return registerOre(prefix.oreDictName(material), stack);
    }

    public static boolean registerOre(Object name, ItemStack stack) {
        if (name == null || GTUtility.isStackInvalid(stack)) return false;

        String tName = name.toString();

        if (GTUtility.isStringInvalid(tName)) return false;

        for (ItemStack itemStack : getOresImmutable(tName))
            if (GTUtility.areStacksEqual(itemStack, stack, true)) return false;

        isRegisteringOre++;
        OreDictionary.registerOre(tName, GTUtility.copyAmount(1, stack));
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

    public static ItemStack getGem(MaterialStack material) {
        return material == null ? null : getGem(material.mMaterial, material.mAmount);
    }

    public static ItemStack getGem(Material material, OrePrefixes prefix) {
        return material == null ? null : getGem(material, prefix.getMaterialAmount());
    }

    /// Transitional: accepts the legacy material types through [MU#toMaterial] until every caller passes a
    /// [Material] directly. The same applies to the legacy-typed overloads of `getDust`/`getIngot`/
    /// `getIngotOrDust`/`getDustOrIngot` below.
    public static ItemStack getGem(IOreMaterial material, OrePrefixes prefix) {
        return material == null ? null : getGem(MU.toMaterial(material), prefix.getMaterialAmount());
    }

    public static ItemStack getGem(IOreMaterial material, long materialAmount) {
        return getGem(MU.toMaterial(material), materialAmount);
    }

    public static ItemStack getGem(Material material, long materialAmount) {
        ItemStack rStack = null;
        if (((materialAmount >= M))) rStack = get(OrePrefixes.gem, material, materialAmount / M);
        if (rStack == null) {
            if ((((materialAmount * 2) % M == 0) || materialAmount >= M * 16))
                rStack = get(OrePrefixes.gemFlawed, material, (materialAmount * 2) / M);
            if ((((materialAmount * 4) >= M))) rStack = get(OrePrefixes.gemChipped, material, (materialAmount * 4) / M);
        }
        return rStack;
    }

    public static ItemStack getDust(MaterialStack material) {
        return material == null ? null : getDust(material.mMaterial, material.mAmount);
    }

    public static ItemStack getDust(Material material, OrePrefixes prefix) {
        return material == null ? null : getDust(material, prefix.getMaterialAmount());
    }

    public static ItemStack getDust(IOreMaterial material, OrePrefixes prefix) {
        return material == null ? null : getDust(MU.toMaterial(material), prefix.getMaterialAmount());
    }

    public static ItemStack getDust(IOreMaterial material, long materialAmount) {
        return getDust(MU.toMaterial(material), materialAmount);
    }

    public static ItemStack getDust(Material material, long materialAmount) {
        if (materialAmount <= 0) return null;
        ItemStack rStack = null;
        if (((materialAmount % M == 0) || materialAmount >= M * 16))
            rStack = get(OrePrefixes.dust, material, materialAmount / M);
        if (rStack == null && (((materialAmount * 4) % M == 0) || materialAmount >= M * 8))
            rStack = get(OrePrefixes.dustSmall, material, (materialAmount * 4) / M);
        if (rStack == null && (((materialAmount * 9) >= M)))
            rStack = get(OrePrefixes.dustTiny, material, (materialAmount * 9) / M);
        return rStack;
    }

    public static ItemStack getIngot(MaterialStack material) {
        return material == null ? null : getIngot(material.mMaterial, material.mAmount);
    }

    public static ItemStack getIngot(Material material, OrePrefixes prefix) {
        return material == null ? null : getIngot(material, prefix.getMaterialAmount());
    }

    public static ItemStack getIngot(IOreMaterial material, OrePrefixes prefix) {
        return material == null ? null : getIngot(MU.toMaterial(material), prefix.getMaterialAmount());
    }

    public static ItemStack getIngot(IOreMaterial material, long materialAmount) {
        return getIngot(MU.toMaterial(material), materialAmount);
    }

    public static ItemStack getIngot(Material material, long materialAmount) {
        if (materialAmount <= 0) return null;
        ItemStack rStack = null;
        if (((materialAmount % (M * 9) == 0 && materialAmount / (M * 9) > 1) || materialAmount >= M * 72))
            rStack = get(OrePrefixes.block, material, materialAmount / (M * 9));
        if (rStack == null && ((materialAmount % M == 0) || materialAmount >= M * 8))
            rStack = get(OrePrefixes.ingot, material, materialAmount / M);
        if (rStack == null && (((materialAmount * 9) >= M)))
            rStack = get(OrePrefixes.nugget, material, (materialAmount * 9) / M);
        return rStack;
    }

    public static ItemStack getIngotOrDust(Material material, long materialAmount) {
        if (materialAmount <= 0) return null;
        ItemStack rStack = getIngot(material, materialAmount);
        if (rStack == null) rStack = getDust(material, materialAmount);
        return rStack;
    }

    public static ItemStack getIngotOrDust(IOreMaterial material, long materialAmount) {
        return getIngotOrDust(MU.toMaterial(material), materialAmount);
    }

    public static ItemStack getIngotOrDust(MaterialStack material) {
        ItemStack rStack = getIngot(material);
        if (rStack == null) rStack = getDust(material);
        return rStack;
    }

    public static ItemStack getDustOrIngot(Material material, long materialAmount) {
        if (materialAmount <= 0) return null;
        ItemStack rStack = getDust(material, materialAmount);
        if (rStack == null) rStack = getIngot(material, materialAmount);
        return rStack;
    }

    public static ItemStack getDustOrIngot(IOreMaterial material, long materialAmount) {
        return getDustOrIngot(MU.toMaterial(material), materialAmount);
    }

    public static ItemStack getDustOrIngot(MaterialStack material) {
        ItemStack rStack = getDust(material);
        if (rStack == null) rStack = getIngot(material);
        return rStack;
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
    public static ArrayList<ItemStack> getOres(OrePrefixes prefix, IOreMaterial material) {
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
