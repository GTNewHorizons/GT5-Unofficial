package gregtech.api.util;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.M;
import static gregtech.api.enums.GT_Values.W;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.GT_ItemStack2;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the Core of my OreDict Unification Code
 * <p/>
 * If you just want to use this to unificate your Items, then use the Function in the GregTech_API File
 * <p/>
 * P.S. It is intended to be named "Unificator" and not "Unifier", because that sounds more awesome.
 */
public class GT_OreDictUnificator {

    private static final Map<String, ItemStack> sName2StackMap = new HashMap<>();
    private static final Map<GT_ItemStack2, ItemData> sItemStack2DataMap = new HashMap<>();
    private static final Map<GT_ItemStack2, List<ItemStack>> sUnificationTable = new HashMap<>();
    private static final Set<GT_ItemStack2> sNoUnificationList = new HashSet<>();
    public static volatile int VERSION = 509;
    private static int isRegisteringOre = 0, isAddingOre = 0;
    private static boolean mRunThroughTheList = true;

    static {
        GregTech_API.sItemStackMappings.add(sItemStack2DataMap);
        GregTech_API.sItemStackMappings.add(sUnificationTable);
    }

    /**
     * The Blacklist just prevents the Item from being unificated into something else. Useful if you have things like
     * the Industrial Diamond, which is better than regular Diamond, but also usable in absolutely all Diamond Recipes.
     */
    public static void addToBlacklist(ItemStack aStack) {
        if (GT_Utility.isStackValid(aStack) && !GT_Utility.isStackInList(aStack, sNoUnificationList))
            sNoUnificationList.add(new GT_ItemStack2(aStack));
    }

    public static boolean isBlacklisted(ItemStack aStack) {
        return GT_Utility.isStackInList(aStack, sNoUnificationList);
    }

    public static void add(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack) {
        set(aPrefix, aMaterial, aStack, false, false);
    }

    public static void set(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack) {
        set(aPrefix, aMaterial, aStack, true, false);
    }

    public static void set(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack, boolean aOverwrite,
            boolean aAlreadyRegistered) {
        if (aMaterial == null || aPrefix == null
                || GT_Utility.isStackInvalid(aStack)
                || Items.feather.getDamage(aStack) == W)
            return;
        isAddingOre++;
        aStack = GT_Utility.copyAmount(1, aStack);
        if (!aAlreadyRegistered) registerOre(aPrefix.get(aMaterial), aStack);
        addAssociation(aPrefix, aMaterial, aStack, isBlacklisted(aStack));
        if (aOverwrite || GT_Utility.isStackInvalid(
                sName2StackMap.get(
                        aPrefix.get(aMaterial)
                               .toString())))
            sName2StackMap.put(
                    aPrefix.get(aMaterial)
                           .toString(),
                    aStack);
        isAddingOre--;
    }

    public static ItemStack getFirstOre(Object aName, long aAmount) {
        if (GT_Utility.isStringInvalid(aName)) return null;
        ItemStack tStack = sName2StackMap.get(aName.toString());
        if (GT_Utility.isStackValid(tStack)) return GT_Utility.copyAmount(aAmount, tStack);
        return GT_Utility.copyAmount(aAmount, getOresImmutable(aName).toArray());
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
        // if (Materials.mDefaultComponents.contains(aPrefix) && !aPrefix.mDynamicItems.contains((Materials)aMaterial))
        // aPrefix.mDynamicItems.add((Materials) aMaterial);
        if (OrePrefixes.mPreventableComponents.contains(aPrefix) && aPrefix.mDisabledItems.contains(aMaterial))
            return aReplacement;
        return get(aPrefix.get(aMaterial), aReplacement, aAmount, false, true);
    }

    public static ItemStack get(OrePrefixes aPrefix, Object aMaterial, long aAmount, boolean aNoInvalidAmounts) {
        // if (Materials.mDefaultComponents.contains(aPrefix) && !aPrefix.mDynamicItems.contains((Materials)aMaterial))
        // aPrefix.mDynamicItems.add((Materials) aMaterial);
        if (OrePrefixes.mPreventableComponents.contains(aPrefix) && aPrefix.mDisabledItems.contains(aMaterial))
            return null;
        return get(aPrefix.get(aMaterial), null, aAmount, false, aNoInvalidAmounts);
    }

    public static ItemStack get(Object aName, ItemStack aReplacement, long aAmount, boolean aMentionPossibleTypos,
            boolean aNoInvalidAmounts) {
        if (aNoInvalidAmounts && aAmount < 1) return null;
        final ItemStack stackFromName = sName2StackMap.get(aName.toString());
        if (stackFromName != null) return GT_Utility.copyAmount(aAmount, stackFromName);
        if (aMentionPossibleTypos) {
            GT_Log.err.println("Unknown Key for Unification, Typo? " + aName);
            // Debug callstack of entries not in sName2StackMap
            // StackTraceElement[] cause = Thread.currentThread().getStackTrace();
            // GT_Log.err.println(Arrays.toString(cause));

        }
        final ItemStack stackFirstOre = getFirstOre(aName, aAmount);
        if (stackFirstOre != null) return GT_Utility.copyAmount(aAmount, stackFirstOre);
        return GT_Utility.copyAmount(aAmount, aReplacement);
    }

    public static ItemStack[] setStackArray(boolean aUseBlackList, ItemStack... aStacks) {
        for (int i = 0; i < aStacks.length; i++) aStacks[i] = get(aUseBlackList, GT_Utility.copyOrNull(aStacks[i]));
        return aStacks;
    }

    public static ItemStack[] getStackArray(boolean aUseBlackList, Object... aStacks) {
        ItemStack[] rStacks = new ItemStack[aStacks.length];
        for (int i = 0; i < aStacks.length; i++) rStacks[i] = get(aUseBlackList, GT_Utility.copy(aStacks[i]));
        return rStacks;
    }

    public static ItemStack setStack(ItemStack aStack) {
        return setStack(true, aStack);
    }

    public static ItemStack setStack(boolean aUseBlackList, ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return aStack;
        ItemStack tStack = get(aUseBlackList, aStack);
        if (GT_Utility.areStacksEqual(aStack, tStack)) return aStack;
        aStack.func_150996_a(tStack.getItem());
        Items.feather.setDamage(aStack, Items.feather.getDamage(tStack));
        return aStack;
    }

    public static ItemStack get(ItemStack aStack) {
        return get(true, aStack);
    }

    public static ItemStack get(boolean aUseBlackList, ItemStack aStack) {
        return get(aUseBlackList, aStack, false);
    }

    public static ItemStack get(boolean aUseBlackList, ItemStack aStack, boolean unsafe) {
        if (GT_Utility.isStackInvalid(aStack)) return null;
        ItemData tPrefixMaterial = getAssociation(aStack);
        if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData()
                || (aUseBlackList && tPrefixMaterial.mBlackListed))
            return GT_Utility.copyOrNull(aStack);
        if (aUseBlackList && !GregTech_API.sUnificationEntriesRegistered && isBlacklisted(aStack)) {
            tPrefixMaterial.mBlackListed = true;
            return GT_Utility.copyOrNull(aStack);
        }
        if (tPrefixMaterial.mUnificationTarget == null)
            tPrefixMaterial.mUnificationTarget = sName2StackMap.get(tPrefixMaterial.toString());
        ItemStack rStack = tPrefixMaterial.mUnificationTarget;
        if (GT_Utility.isStackInvalid(rStack)) return GT_Utility.copyOrNull(aStack);
        ItemStack newStack;
        if (unsafe) {
            newStack = GT_Utility.copyAmountUnsafe(aStack.stackSize, rStack);
        } else {
            newStack = GT_Utility.copyAmount(aStack.stackSize, rStack);
        }
        // NBT is assigned by reference here, so mutating it may have unexpected side effects.
        newStack.setTagCompound(aStack.getTagCompound());
        return newStack;
    }

    /**
     * Doesn't copy the returned stack or set quantity. Be careful and do not mutate it; intended only to optimize
     * comparisons
     */
    public static ItemStack get_nocopy(ItemStack aStack) {
        return get_nocopy(true, aStack);
    }

    /**
     * Doesn't copy the returned stack or set quantity. Be careful and do not mutate it; intended only to optimize
     * comparisons
     */
    static ItemStack get_nocopy(boolean aUseBlackList, ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return null;
        ItemData tPrefixMaterial = getAssociation(aStack);
        if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData()
                || (aUseBlackList && tPrefixMaterial.mBlackListed))
            return aStack;
        if (aUseBlackList && !GregTech_API.sUnificationEntriesRegistered && isBlacklisted(aStack)) {
            tPrefixMaterial.mBlackListed = true;
            return aStack;
        }
        if (tPrefixMaterial.mUnificationTarget == null)
            tPrefixMaterial.mUnificationTarget = sName2StackMap.get(tPrefixMaterial.toString());
        ItemStack rStack = tPrefixMaterial.mUnificationTarget;
        if (GT_Utility.isStackInvalid(rStack)) return aStack;

        // Yes, == and not .equals().
        // This check is primarily intended to optimize for the case where both rStack and aStack
        // do not have NBT, and so we would be comparing null == null.
        //
        // Even if aStack and rStack may have equal NBT, we prefer to do an inexpensive
        // new ItemStack() over the potentially expensive NBTTagCompound.equals().
        if (aStack.getTagCompound() == rStack.getTagCompound()) {
            // Warning: rStack's stack size may not be equal to aStack's stack size.
            return rStack;
        }

        // Okay, okay, I lied, we actually do need to make a copy.
        // This is to fix a long-standing bug where we were mutating NBT directly on rStack,
        // which had unexpected and unpredictable ripple effects.
        //
        // We will do some custom copying here, to avoid ItemStack.copy(),
        // which calls the potentially expensive NBTTagCompound.copy()
        // NBT is assigned by reference here, so mutating it may have unexpected side effects.
        ItemStack newStack = new ItemStack(rStack.getItem(), aStack.stackSize, Items.feather.getDamage(rStack));
        newStack.setTagCompound(aStack.getTagCompound());
        return newStack;
    }

    /**
     * Compares the first argument against an already-unificated second argument as if aUseBlackList was both true and
     * false.
     */
    public static boolean isInputStackEqual(ItemStack aStack, ItemStack unified_tStack) {
        boolean alreadyCompared = false;
        if (GT_Utility.isStackInvalid(aStack)) return false;
        ItemData tPrefixMaterial = getAssociation(aStack);
        ItemStack rStack = null;
        if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData())
            return GT_Utility.areStacksEqual(aStack, unified_tStack, true);
        else if (tPrefixMaterial.mBlackListed) {
            if (GT_Utility.areStacksEqual(aStack, unified_tStack, true)) return true;
            else alreadyCompared = true;
        }
        if (!alreadyCompared && !GregTech_API.sUnificationEntriesRegistered && isBlacklisted(aStack)) {
            tPrefixMaterial.mBlackListed = true;
            if (GT_Utility.areStacksEqual(aStack, unified_tStack, true)) return true;
            else alreadyCompared = true;
        }
        if (tPrefixMaterial.mUnificationTarget == null)
            tPrefixMaterial.mUnificationTarget = sName2StackMap.get(tPrefixMaterial.toString());
        rStack = tPrefixMaterial.mUnificationTarget;
        if (GT_Utility.isStackInvalid(rStack))
            return !alreadyCompared && GT_Utility.areStacksEqual(aStack, unified_tStack, true);
        return GT_Utility.areStacksEqual(rStack, unified_tStack, true);
    }

    public static List<ItemStack> getNonUnifiedStacks(Object obj) {
        if (sUnificationTable.isEmpty() && !sItemStack2DataMap.isEmpty()) {
            // use something akin to double check lock. this synchronization overhead is causing lag whenever my
            // 5900x tries to do NEI lookup
            synchronized (sUnificationTable) {
                if (sUnificationTable.isEmpty() && !sItemStack2DataMap.isEmpty()) {
                    for (GT_ItemStack tGTStack0 : sItemStack2DataMap.keySet()) {
                        ItemStack tStack0 = tGTStack0.toStack();
                        ItemStack tStack1 = get_nocopy(false, tStack0);
                        if (!GT_Utility.areStacksEqual(tStack0, tStack1)) {
                            GT_ItemStack2 tGTStack1 = new GT_ItemStack2(tStack1);
                            List<ItemStack> list = sUnificationTable.computeIfAbsent(tGTStack1, k -> new ArrayList<>());
                            // greg's original code tries to dedupe the list using List#contains, which won't work
                            // on vanilla ItemStack. I removed it since it never worked and can be slow.
                            list.add(tStack0);
                        }
                    }
                }
            }
        }
        ItemStack[] aStacks = {};
        if (obj instanceof ItemStack) aStacks = new ItemStack[] { (ItemStack) obj };
        else if (obj instanceof ItemStack[]) aStacks = (ItemStack[]) obj;
        else if (obj instanceof List) aStacks = (ItemStack[]) ((List) obj).toArray(new ItemStack[0]);
        List<ItemStack> rList = new ArrayList<>();
        for (ItemStack aStack : aStacks) {
            if (aStack == null) continue;
            rList.add(aStack);
            List<ItemStack> tList = sUnificationTable.get(new GT_ItemStack2(aStack));
            if (tList != null) {
                for (ItemStack tStack : tList) {
                    ItemStack tStack1 = GT_Utility.copyAmount(aStack.stackSize, tStack);
                    rList.add(tStack1);
                }
            }
        }
        return rList;
    }

    public static void addItemData(ItemStack aStack, ItemData aData) {
        if (GT_Utility.isStackValid(aStack) && getItemData(aStack) == null && aData != null) setItemData(aStack, aData);
    }

    public static void setItemData(ItemStack aStack, ItemData aData) {
        if (GT_Utility.isStackInvalid(aStack) || aData == null) return;
        ItemData tData = getItemData(aStack);
        if (tData == null || !tData.hasValidPrefixMaterialData()) {
            if (tData != null) for (Object tObject : tData.mExtraData)
                if (!aData.mExtraData.contains(tObject)) aData.mExtraData.add(tObject);
            if (aStack.stackSize > 1) {
                if (aData.mMaterial != null) aData.mMaterial.mAmount /= aStack.stackSize;
                for (MaterialStack tMaterial : aData.mByProducts) tMaterial.mAmount /= aStack.stackSize;
                aStack = GT_Utility.copyAmount(1, aStack);
            }
            sItemStack2DataMap.put(new GT_ItemStack2(aStack), aData);
            if (aData.hasValidMaterialData()) {
                long tValidMaterialAmount = aData.mMaterial.mMaterial.contains(SubTag.NO_RECYCLING) ? 0
                        : aData.mMaterial.mAmount >= 0 ? aData.mMaterial.mAmount : M;
                for (MaterialStack tMaterial : aData.mByProducts)
                    tValidMaterialAmount += tMaterial.mMaterial.contains(SubTag.NO_RECYCLING) ? 0
                            : tMaterial.mAmount >= 0 ? tMaterial.mAmount : M;
                if (tValidMaterialAmount < M) GT_ModHandler.addToRecyclerBlackList(aStack);
            }
            if (mRunThroughTheList) {
                if (GregTech_API.sLoadStarted) {
                    mRunThroughTheList = false;
                    for (Entry<GT_ItemStack2, ItemData> tEntry : sItemStack2DataMap.entrySet()) if (!tEntry.getValue()
                                                                                                           .hasValidPrefixData()
                            || tEntry.getValue().mPrefix.mAllowNormalRecycling)
                        GT_RecipeRegistrator.registerMaterialRecycling(
                                tEntry.getKey()
                                      .toStack(),
                                tEntry.getValue());
                }
            } else {
                if (!aData.hasValidPrefixData() || aData.mPrefix.mAllowNormalRecycling)
                    GT_RecipeRegistrator.registerMaterialRecycling(aStack, aData);
            }
        } else {
            for (Object tObject : aData.mExtraData)
                if (!tData.mExtraData.contains(tObject)) tData.mExtraData.add(tObject);
        }
    }

    public static void addAssociation(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack,
            boolean aBlackListed) {
        if (aPrefix == null || aMaterial == null || GT_Utility.isStackInvalid(aStack)) return;
        if (Items.feather.getDamage(aStack) == W) for (byte i = 0; i < 16; i++)
            setItemData(GT_Utility.copyAmountAndMetaData(1, i, aStack), new ItemData(aPrefix, aMaterial, aBlackListed));
        setItemData(aStack, new ItemData(aPrefix, aMaterial, aBlackListed));
    }

    @Nullable
    public static ItemData getItemData(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return null;
        ItemData rData = sItemStack2DataMap.get(new GT_ItemStack2(aStack));
        if (rData == null) rData = sItemStack2DataMap.get(new GT_ItemStack2(aStack, true));
        return rData;
    }

    @Nullable
    public static ItemData getAssociation(ItemStack aStack) {
        ItemData rData = getItemData(aStack);
        return rData != null && rData.hasValidPrefixMaterialData() ? rData : null;
    }

    public static boolean isItemStackInstanceOf(ItemStack aStack, Object aName) {
        if (GT_Utility.isStringInvalid(aName) || GT_Utility.isStackInvalid(aStack)) return false;
        for (ItemStack tOreStack : getOresImmutable(aName.toString()))
            if (GT_Utility.areStacksEqual(tOreStack, aStack, true)) return true;
        return false;
    }

    public static boolean isItemStackDye(ItemStack aStack) {
        if (GT_Utility.isStackInvalid(aStack)) return false;

        for (Dyes tDye : Dyes.VALUES) if (isItemStackInstanceOf(aStack, tDye.toString())) return true;

        return false;
    }

    public static boolean registerOre(OrePrefixes aPrefix, Object aMaterial, ItemStack aStack) {
        return registerOre(aPrefix.get(aMaterial), aStack);
    }

    public static boolean registerOre(Object aName, ItemStack aStack) {
        if (aName == null || GT_Utility.isStackInvalid(aStack)) return false;

        String tName = aName.toString();

        if (GT_Utility.isStringInvalid(tName)) return false;

        for (ItemStack itemStack : getOresImmutable(tName))
            if (GT_Utility.areStacksEqual(itemStack, aStack, true)) return false;

        isRegisteringOre++;
        OreDictionary.registerOre(tName, GT_Utility.copyAmount(1, aStack));
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

    public static ItemStack getGem(MaterialStack aMaterial) {
        return aMaterial == null ? null : getGem(aMaterial.mMaterial, aMaterial.mAmount);
    }

    public static ItemStack getGem(Materials aMaterial, OrePrefixes aPrefix) {
        return aMaterial == null ? null : getGem(aMaterial, aPrefix.mMaterialAmount);
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
        return aMaterial == null ? null : getDust(aMaterial, aPrefix.mMaterialAmount);
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
        return aMaterial == null ? null : getIngot(aMaterial, aPrefix.mMaterialAmount);
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
        if (GT_Utility.isStringValid(aName)) rList.addAll(OreDictionary.getOres(aName));
        return rList;
    }

    /**
     * Fast version of {@link #getOres(Object)}, which doesn't call
     * {@link System#arraycopy(Object, int, Object, int, int)} in {@link ArrayList#addAll}
     */
    public static List<ItemStack> getOresImmutable(@Nullable Object aOreName) {
        String aName = aOreName == null ? E : aOreName.toString();

        return GT_Utility.isStringValid(aName) ? Collections.unmodifiableList(OreDictionary.getOres(aName))
                : Collections.emptyList();
    }
}
