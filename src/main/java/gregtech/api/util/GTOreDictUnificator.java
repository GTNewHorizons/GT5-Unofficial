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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;

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

    private static final Set<ItemStack> unificationBlacklist = new ObjectOpenCustomHashSet<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final Set<Item> unificationWildcardBlacklist = new ReferenceOpenHashSet<>();

    private static int isRegisteringOre = 0, isAddingOre = 0;
    private static boolean mRunThroughTheList = true;

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

    public static void add(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack) {
        set(aPrefix, aMaterial, aStack, false, false);
    }

    public static void set(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack) {
        set(aPrefix, aMaterial, aStack, true, false);
    }

    public static void set(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack, boolean aOverwrite,
        boolean aAlreadyRegistered) {

        if (aMaterial == null || aPrefix == null || GTUtility.isStackInvalid(aStack)) return;
        if (Items.feather.getDamage(aStack) == WILDCARD) return;

        isAddingOre++;
        aStack = GTUtility.copyAmount(1, aStack);
        if (!aAlreadyRegistered) registerOre(aPrefix.get(aMaterial), aStack);
        addAssociation(aPrefix, aMaterial, aStack);

        String oreName = aPrefix.get(aMaterial)
            .toString();
        if (aOverwrite || GTUtility.isStackInvalid(sName2StackMap.get(oreName))) {
            resetUnificationTarget(oreName);
            sName2StackMap.put(oreName, aStack);
        }
        isAddingOre--;
    }

    public static ItemStack getFirstOre(Object name, long amount) {
        if (GTUtility.isStringInvalid(name)) return null;
        return GTUtility.copyAmount(amount, getFirstOre_nocopy(name.toString()));
    }

    public static ItemStack getFirstOre_nocopy(String name) {
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

    public static ItemStack get(Object aName, long aAmount) {
        return get(aName, null, aAmount, true, true);
    }

    public static ItemStack get(Object aName, ItemStack aReplacement, long aAmount) {
        return get(aName, aReplacement, aAmount, true, true);
    }

    public static ItemStack get(OrePrefixes aPrefix, Object aMaterial, long aAmount) {
        return get(aPrefix, aMaterial, null, aAmount);
    }

    public static ItemStack get(OrePrefixes aPrefix, Object aMaterial, ItemStack aReplacement, long aAmount) {
        if (OrePrefixes.mPreventableComponents.contains(aPrefix) && aPrefix.mDisabledItems.contains(aMaterial))
            return aReplacement;
        return get(aPrefix.get(aMaterial), aReplacement, aAmount, false, true);
    }

    public static ItemStack get(OrePrefixes aPrefix, Object aMaterial, long aAmount, boolean aNoInvalidAmounts) {
        if (OrePrefixes.mPreventableComponents.contains(aPrefix) && aPrefix.mDisabledItems.contains(aMaterial))
            return null;
        return get(aPrefix.get(aMaterial), null, aAmount, false, aNoInvalidAmounts);
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
    public static ItemStack[] setStackArray(boolean aUseBlackList, ItemStack... aStacks) {
        return setStackArray(aUseBlackList, false, aStacks);
    }

    public static ItemStack[] setStackArray(boolean aUseBlackList, boolean aUnsafe, ItemStack... aStacks) {
        for (int i = 0; i < aStacks.length; i++)
            aStacks[i] = get(aUseBlackList, GTUtility.copyOrNull(aStacks[i]), aUnsafe);
        return aStacks;
    }

    public static ItemStack[] getStackArray(boolean aUseBlackList, Object... aStacks) {
        ItemStack[] rStacks = new ItemStack[aStacks.length];
        for (int i = 0; i < aStacks.length; i++) {
            rStacks[i] = get(aUseBlackList, GTUtility.copy(aStacks[i]), true);
        }
        return rStacks;
    }

    public static ItemStack setStack(ItemStack aStack) {
        return setStack(true, aStack);
    }

    public static ItemStack setStack(boolean aUseBlackList, ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return aStack;
        ItemStack tStack = get(aUseBlackList, aStack);
        if (GTUtility.areStacksEqual(aStack, tStack)) return aStack;
        aStack.func_150996_a(tStack.getItem());
        Items.feather.setDamage(aStack, Items.feather.getDamage(tStack));
        return aStack;
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
        if (itemData == null || !itemData.hasValidPrefixMaterialData() || (useBlackList && isBlacklisted(stack))) {
            return GTUtility.copyOrNull(stack);
        }
        if (itemData.mUnificationTarget == null) {
            itemData.mUnificationTarget = getFirstOre_nocopy(itemData.toString());
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
    public static ItemStack get_nocopy(ItemStack aStack) {
        return get_nocopy(true, aStack);
    }

    /**
     * Doesn't always copy the returned stack or set quantity. Be careful and do not mutate it
     */
    public static ItemStack get_nocopy(boolean useBlackList, ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return null;
        ItemData itemData = getAssociation(stack);
        if (itemData == null || !itemData.hasValidPrefixMaterialData() || (useBlackList && isBlacklisted(stack))) {
            return stack;
        }
        if (itemData.mUnificationTarget == null) {
            itemData.mUnificationTarget = getFirstOre_nocopy(itemData.toString());
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
    public static boolean isInputStackEqual(ItemStack aStack, ItemStack unified_tStack) {
        if (GTUtility.isStackInvalid(aStack)) return false;
        return isInputStackEqual(aStack, getAssociation(aStack), unified_tStack);
    }

    /**
     * Compares the first argument against an already-unificated second argument as if aUseBlackList was both true and
     * false.
     */
    public static boolean isInputStackEqual(ItemStack aStack, ItemData aStackPrefixData, ItemStack unified_tStack) {
        boolean alreadyCompared = false;
        if (GTUtility.isStackInvalid(aStack)) return false;
        ItemStack rStack = null;
        if (aStackPrefixData == null || !aStackPrefixData.hasValidPrefixMaterialData())
            return GTUtility.areStacksEqual(aStack, unified_tStack, true);
        else if (isBlacklisted(aStack)) {
            if (GTUtility.areStacksEqual(aStack, unified_tStack, true)) return true;
            else alreadyCompared = true;
        }
        if (aStackPrefixData.mUnificationTarget == null) {
            aStackPrefixData.mUnificationTarget = getFirstOre_nocopy(aStackPrefixData.toString());
        }
        rStack = aStackPrefixData.mUnificationTarget;
        if (GTUtility.isStackInvalid(rStack))
            return !alreadyCompared && GTUtility.areStacksEqual(aStack, unified_tStack, true);
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
        ItemStack[] aStacks = GTValues.emptyItemStackArray;
        if (obj instanceof ItemStack) aStacks = new ItemStack[] { (ItemStack) obj };
        else if (obj instanceof ItemStack[]) aStacks = (ItemStack[]) obj;
        else if (obj instanceof List) aStacks = ((List<?>) obj).toArray(new ItemStack[0]);
        List<ItemStack> rList = new ArrayList<>();
        for (ItemStack aStack : aStacks) {
            if (aStack == null) continue;
            rList.add(aStack);
            List<ItemStack> tList = sUnificationTable.get(aStack);
            if (tList != null) {
                for (ItemStack tStack : tList) {
                    ItemStack tStack1 = GTUtility.copyAmountUnsafe(aStack.stackSize, tStack);
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

        ItemData prevData = getItemData(stack);
        if (prevData != null && prevData.hasValidPrefixMaterialData()) {
            return;
        }

        if (stack.stackSize > 1) {
            if (data.mMaterial != null) data.mMaterial.mAmount /= stack.stackSize;
            for (MaterialStack material : data.mByProducts) {
                material.mAmount /= stack.stackSize;
            }
            stack = GTUtility.copyAmount(1, stack);
        }

        sItemStack2DataMap.put(stack, data);

        if (data.hasValidMaterialData()) {
            long recyclableMaterialAmount = 0;
            if (!data.mMaterial.mMaterial.contains(SubTag.NO_RECYCLING)) {
                recyclableMaterialAmount += data.mMaterial.mAmount >= 0 ? data.mMaterial.mAmount : M;
            }

            for (MaterialStack material : data.mByProducts) {
                if (!material.mMaterial.contains(SubTag.NO_RECYCLING)) {
                    recyclableMaterialAmount += material.mAmount >= 0 ? material.mAmount : M;
                }
            }

            if (recyclableMaterialAmount < M) {
                GTModHandler.addToRecyclerBlackList(stack);
            }
        }

        if (mRunThroughTheList) {
            if (GregTechAPI.sLoadStarted) {
                mRunThroughTheList = false;

                for (Entry<ItemStack, ItemData> entry : sItemStack2DataMap.entrySet()) {
                    ItemStack entryStack = entry.getKey();
                    ItemData entryData = entry.getValue();
                    if (!entryData.hasValidPrefixData() || entryData.mPrefix.isRecyclable()) {
                        GTRecipeRegistrator.registerMaterialRecycling(entryStack, entryData);
                    }
                }
            }
        } else if (!data.hasValidPrefixData() || data.mPrefix.isRecyclable()) {
            GTRecipeRegistrator.registerMaterialRecycling(stack, data);
        }
    }

    public static void removeItemData(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) {
            return;
        }
        sItemStack2DataMap.remove(aStack);
    }

    public static void addAssociation(OrePrefixes prefix, Materials material, ItemStack stack) {
        if (prefix == null || material == null || GTUtility.isStackInvalid(stack)) return;

        if (Items.feather.getDamage(stack) == WILDCARD) {
            for (byte i = 0; i < 16; i++) {
                setItemData(GTUtility.copyAmountAndMetaData(1, i, stack), new ItemData(prefix, material));
            }
        }

        setItemData(stack, new ItemData(prefix, material));
    }

    @Nullable
    public static ItemData getItemData(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return null;
        ItemData rData = sItemStack2DataMap.get(aStack);
        if (rData == null) { // Try the lookup again but with wildcard damage value
            rData = sItemStack2DataMap.get(GTItemStack.internalCopyStack(aStack, true));
        }
        return rData;
    }

    @Nullable
    public static ItemData getAssociation(ItemStack aStack) {
        ItemData rData = getItemData(aStack);
        return rData != null && rData.hasValidPrefixMaterialData() ? rData : null;
    }

    public static boolean isItemStackInstanceOf(ItemStack aStack, Object aName) {
        if (GTUtility.isStringInvalid(aName) || GTUtility.isStackInvalid(aStack)) return false;
        for (ItemStack tOreStack : getOresImmutable(aName.toString()))
            if (GTUtility.areStacksEqual(tOreStack, aStack, true)) return true;
        return false;
    }

    public static boolean isItemStackDye(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return false;

        for (Dyes tDye : Dyes.VALUES) if (isItemStackInstanceOf(aStack, tDye.toString())) return true;

        return false;
    }

    public static boolean registerOre(OrePrefixes aPrefix, Object aMaterial, ItemStack aStack) {
        return registerOre(aPrefix.get(aMaterial), aStack);
    }

    public static boolean registerOre(Object aName, ItemStack aStack) {
        if (aName == null || GTUtility.isStackInvalid(aStack)) return false;

        String tName = aName.toString();

        if (GTUtility.isStringInvalid(tName)) return false;

        for (ItemStack itemStack : getOresImmutable(tName))
            if (GTUtility.areStacksEqual(itemStack, aStack, true)) return false;

        isRegisteringOre++;
        OreDictionary.registerOre(tName, aStack);
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

    public static void resetUnificationTarget(String oreName) {
        for (ItemData data : sItemStack2DataMap.values()) {
            if (oreName.equals(data.toString())) {
                data.mUnificationTarget = null;
            }
        }
    }

    public static ItemStack getGem(MaterialStack aMaterial) {
        return aMaterial == null ? null : getGem(aMaterial.mMaterial, aMaterial.mAmount);
    }

    public static ItemStack getGem(Materials aMaterial, OrePrefixes aPrefix) {
        return aMaterial == null ? null : getGem(aMaterial, aPrefix.getMaterialAmount());
    }

    public static ItemStack getGem(Materials aMaterial, long aMaterialAmount) {
        ItemStack rStack = null;
        if (((aMaterialAmount >= M))) rStack = get(OrePrefixes.gem, aMaterial, aMaterialAmount / M);
        if (rStack == null) {
            if ((((aMaterialAmount * 2) % M == 0) || aMaterialAmount >= M * 16))
                rStack = get(OrePrefixes.gemFlawed, aMaterial, (aMaterialAmount * 2) / M);
            if ((((aMaterialAmount * 4) >= M)))
                rStack = get(OrePrefixes.gemChipped, aMaterial, (aMaterialAmount * 4) / M);
        }
        return rStack;
    }

    public static ItemStack getDust(MaterialStack aMaterial) {
        return aMaterial == null ? null : getDust(aMaterial.mMaterial, aMaterial.mAmount);
    }

    public static ItemStack getDust(Materials aMaterial, OrePrefixes aPrefix) {
        return aMaterial == null ? null : getDust(aMaterial, aPrefix.getMaterialAmount());
    }

    public static ItemStack getDust(Materials aMaterial, long aMaterialAmount) {
        if (aMaterialAmount <= 0) return null;
        ItemStack rStack = null;
        if (((aMaterialAmount % M == 0) || aMaterialAmount >= M * 16))
            rStack = get(OrePrefixes.dust, aMaterial, aMaterialAmount / M);
        if (rStack == null && (((aMaterialAmount * 4) % M == 0) || aMaterialAmount >= M * 8))
            rStack = get(OrePrefixes.dustSmall, aMaterial, (aMaterialAmount * 4) / M);
        if (rStack == null && (((aMaterialAmount * 9) >= M)))
            rStack = get(OrePrefixes.dustTiny, aMaterial, (aMaterialAmount * 9) / M);
        return rStack;
    }

    public static ItemStack getIngot(MaterialStack aMaterial) {
        return aMaterial == null ? null : getIngot(aMaterial.mMaterial, aMaterial.mAmount);
    }

    public static ItemStack getIngot(Materials aMaterial, OrePrefixes aPrefix) {
        return aMaterial == null ? null : getIngot(aMaterial, aPrefix.getMaterialAmount());
    }

    public static ItemStack getIngot(Materials aMaterial, long aMaterialAmount) {
        if (aMaterialAmount <= 0) return null;
        ItemStack rStack = null;
        if (((aMaterialAmount % (M * 9) == 0 && aMaterialAmount / (M * 9) > 1) || aMaterialAmount >= M * 72))
            rStack = get(OrePrefixes.block, aMaterial, aMaterialAmount / (M * 9));
        if (rStack == null && ((aMaterialAmount % M == 0) || aMaterialAmount >= M * 8))
            rStack = get(OrePrefixes.ingot, aMaterial, aMaterialAmount / M);
        if (rStack == null && (((aMaterialAmount * 9) >= M)))
            rStack = get(OrePrefixes.nugget, aMaterial, (aMaterialAmount * 9) / M);
        return rStack;
    }

    public static ItemStack getIngotOrDust(Materials aMaterial, long aMaterialAmount) {
        if (aMaterialAmount <= 0) return null;
        ItemStack rStack = getIngot(aMaterial, aMaterialAmount);
        if (rStack == null) rStack = getDust(aMaterial, aMaterialAmount);
        return rStack;
    }

    public static ItemStack getIngotOrDust(MaterialStack aMaterial) {
        ItemStack rStack = getIngot(aMaterial);
        if (rStack == null) rStack = getDust(aMaterial);
        return rStack;
    }

    public static ItemStack getDustOrIngot(Materials aMaterial, long aMaterialAmount) {
        if (aMaterialAmount <= 0) return null;
        ItemStack rStack = getDust(aMaterial, aMaterialAmount);
        if (rStack == null) rStack = getIngot(aMaterial, aMaterialAmount);
        return rStack;
    }

    public static ItemStack getDustOrIngot(MaterialStack aMaterial) {
        ItemStack rStack = getDust(aMaterial);
        if (rStack == null) rStack = getIngot(aMaterial);
        return rStack;
    }

    /**
     * @return a Copy of the OreDictionary.getOres() List
     */
    public static ArrayList<ItemStack> getOres(OrePrefixes aPrefix, Object aMaterial) {
        return getOres(aPrefix.get(aMaterial));
    }

    /**
     * @return a Copy of the OreDictionary.getOres() List
     */
    public static ArrayList<ItemStack> getOres(Object aOreName) {
        String aName = aOreName == null ? E : aOreName.toString();
        ArrayList<ItemStack> rList = new ArrayList<>();
        if (GTUtility.isStringValid(aName)) rList.addAll(OreDictionary.getOres(aName));
        return rList;
    }

    /**
     * Fast version of {@link #getOres(Object)}, which doesn't call
     * {@link System#arraycopy(Object, int, Object, int, int)} in {@link ArrayList#addAll}
     */
    public static List<ItemStack> getOresImmutable(@Nullable Object aOreName) {
        String aName = aOreName == null ? E : aOreName.toString();

        return GTUtility.isStringValid(aName) ? Collections.unmodifiableList(OreDictionary.getOres(aName))
            : Collections.emptyList();
    }

    public static Map<String, ItemStack> getName2StackMap() {
        return sName2StackMap;
    }
}
