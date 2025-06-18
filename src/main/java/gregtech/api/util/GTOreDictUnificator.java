package gregtech.api.util;

import static gregtech.api.enums.GTValues.E;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.Contract;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.items.GTGenericItem;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.mixin.ObjectWithId;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

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
    private static final Long2ObjectMap<ItemData> FAST_ITEM_DATA_MAP = new Long2ObjectOpenHashMap<>();
    private static final Map<ItemStack, List<ItemStack>> sUnificationTable = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static final LongSet sNoUnificationList = new LongOpenHashSet();
    private static int isRegisteringOre = 0, isAddingOre = 0;
    private static boolean mRunThroughTheList = true;

    static {
        GregTechAPI.sItemStackMappings.add(sItemStack2DataMap);
        GregTechAPI.sItemStackMappings.add(sUnificationTable);
    }

    public static long stackHash(ItemStack stack) {
        if (stack == null) return 0;

        return stackHash(stack.getItem(), stack.itemDamage);
    }

    /**
     * Calculates a long equivalent of an ItemStack. This contains the same info as {@link GTItemStack#ITEMSTACK_HASH_STRATEGY2}, i.e. no NBT.
     */
    public static long stackHash(Item item, int meta) {
        // Use a mixin'd id mechanism here because we only care about having a unique value.
        // An id lookup would be too slow, even if it makes more sense.
        // We can't use the System.identityHashCode here because it isn't guaranteed to be unique.
        return ((long) ((ObjectWithId) item).getId()) << 32 | (long) meta;
    }

    public static int stackHash(FluidStack stack) {
        if (stack == null) return 0;

        return stackHash(stack.getFluid());
    }

    /**
     * Calculates an int equivalent of a FluidStack. Similar idea to the item stack equivalent above.
     */
    public static int stackHash(Fluid fluid) {
        return ((ObjectWithId) fluid).getId();
    }

    /**
     * The Blacklist just prevents the Item from being unificated into something else. Useful if you have things like
     * the Industrial Diamond, which is better than regular Diamond, but also usable in absolutely all Diamond Recipes.
     */
    public static void addToBlacklist(ItemStack aStack) {
        if (!GTUtility.isStackValid(aStack)) return;

        sNoUnificationList.add(stackHash(aStack));
    }

    public static boolean isBlacklisted(ItemStack aStack) {
        return sNoUnificationList.contains(stackHash(aStack));
    }

    public static void add(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack) {
        set(aPrefix, aMaterial, aStack, false, false);
    }

    public static void set(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack) {
        set(aPrefix, aMaterial, aStack, true, false);
    }

    public static void set(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack, boolean aOverwrite,
        boolean aAlreadyRegistered) {
        if (aMaterial == null || aPrefix == null || GTUtility.isStackInvalid(aStack) || aStack.itemDamage == WILDCARD) return;
        isAddingOre++;
        aStack = GTUtility.copyAmount(1, aStack);
        if (!aAlreadyRegistered) registerOre(aPrefix.get(aMaterial), aStack);
        addAssociation(aPrefix, aMaterial, aStack, isBlacklisted(aStack));
        String oredictName = aPrefix.get(aMaterial).toString();
        if (aOverwrite || GTUtility.isStackInvalid(sName2StackMap.get(oredictName))) {
            sName2StackMap.put(oredictName, aStack);
        }
        isAddingOre--;
    }

    public static ItemStack getFirstOre(Object aName, long aAmount) {
        if (GTUtility.isStringInvalid(aName)) return null;
        ItemStack tStack = sName2StackMap.get(aName.toString());
        if (GTUtility.isStackValid(tStack)) return GTUtility.copyAmount(aAmount, tStack);
        return GTUtility.copyAmount(aAmount, getOresImmutable(aName).toArray());
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
        final ItemStack stackFromName = sName2StackMap.get(aName.toString());
        if (stackFromName != null) return GTUtility.copyAmount(aAmount, stackFromName);
        if (aMentionPossibleTypos) {
            GTLog.err.println("Unknown Key for Unification, Typo? " + aName);
        }
        final ItemStack stackFirstOre = getFirstOre(aName, aAmount);
        if (stackFirstOre != null) return GTUtility.copyAmount(aAmount, stackFirstOre);
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

    public static ItemStack get(ItemStack aStack) {
        return get(true, aStack);
    }

    public static ItemStack get(boolean aUseBlackList, ItemStack aStack) {
        return get(aUseBlackList, aStack, false);
    }

    /**
     * @param unsafe If true, it does not limit stack size by 64.
     */
    public static ItemStack get(boolean aUseBlackList, ItemStack aStack, boolean unsafe) {
        if (GTUtility.isStackInvalid(aStack)) return null;
        ItemData tPrefixMaterial = getAssociation(aStack);
        if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData()
            || (aUseBlackList && tPrefixMaterial.mBlackListed)) return GTUtility.copyOrNull(aStack);
        if (aUseBlackList && !GregTechAPI.sUnificationEntriesRegistered && isBlacklisted(aStack)) {
            tPrefixMaterial.mBlackListed = true;
            return GTUtility.copyOrNull(aStack);
        }
        if (tPrefixMaterial.mUnificationTarget == null)
            tPrefixMaterial.mUnificationTarget = sName2StackMap.get(tPrefixMaterial.toString());
        ItemStack rStack = tPrefixMaterial.mUnificationTarget;
        if (GTUtility.isStackInvalid(rStack)) return GTUtility.copyOrNull(aStack);
        ItemStack newStack;
        if (unsafe) {
            newStack = GTUtility.copyAmountUnsafe(aStack.stackSize, rStack);
        } else {
            newStack = GTUtility.copyAmount(aStack.stackSize, rStack);
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
    @Contract("_, null -> null")
    static ItemStack get_nocopy(boolean aUseBlackList, ItemStack inputStack) {
        if (GTUtility.isStackInvalid(inputStack)) return null;

        ItemData itemData = getAssociation(inputStack);

        if (itemData == null || !itemData.hasValidPrefixMaterialData() || (aUseBlackList && itemData.mBlackListed)) {
            return inputStack;
        }

        // Check if this stack should be unificated. If not, blacklist it and do nothing.
        if (aUseBlackList && !GregTechAPI.sUnificationEntriesRegistered && isBlacklisted(inputStack)) {
            itemData.mBlackListed = true;
            return inputStack;
        }

        // Try to populate the unification target if it isn't set already
        if (itemData.mUnificationTarget == null) {
            itemData.mUnificationTarget = sName2StackMap.get(itemData.toString());
        }

        ItemStack rStack = itemData.mUnificationTarget;
        if (GTUtility.isStackInvalid(rStack)) return inputStack;

        // Yes, == and not .equals().
        // This check is primarily intended to optimize for the case where both rStack and inputStack
        // do not have NBT, and so we would be comparing null == null.
        //
        // Even if inputStack and rStack may have equal NBT, we prefer to do an inexpensive
        // new ItemStack() over the potentially expensive NBTTagCompound.equals().
        if (inputStack.getTagCompound() == rStack.getTagCompound()) {
            // Warning: rStack's stack size may not be equal to inputStack's stack size.
            return rStack;
        }

        // Okay, okay, I lied, we actually do need to make a copy.
        // This is to fix a long-standing bug where we were mutating NBT directly on rStack,
        // which had unexpected and unpredictable ripple effects.
        //
        // We will do some custom copying here, to avoid ItemStack.copy(),
        // which calls the potentially expensive NBTTagCompound.copy()
        // NBT is assigned by reference here, so mutating it may have unexpected side effects.
        ItemStack newStack = new ItemStack(rStack.getItem(), inputStack.stackSize, Items.feather.getDamage(rStack));
        newStack.setTagCompound(inputStack.getTagCompound());
        return newStack;
    }

    public static List<ItemStack> getNonUnifiedStacks(Object obj) {
        if (sUnificationTable.isEmpty() && !sItemStack2DataMap.isEmpty()) {
            // use something akin to double-check lock. this synchronization overhead is causing lag whenever my
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
        ItemStack[] aStacks = {};
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

    public static void addItemData(ItemStack aStack, ItemData aData) {
        if (GTUtility.isStackValid(aStack) && getItemData(aStack) == null && aData != null) setItemData(aStack, aData);
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
                throw new IllegalArgumentException();
            }
        }
        if (GTUtility.arrayContainsNonNull(tData)) {
            GTOreDictUnificator.addItemData(output, new ItemData(tData));
        }
    }

    public static void setItemData(ItemStack aStack, ItemData aData) {
        if (GTUtility.isStackInvalid(aStack) || aData == null) return;
        ItemData tData = getItemData(aStack);
        if (tData == null || !tData.hasValidPrefixMaterialData()) {
            if (tData != null) for (Object tObject : tData.mExtraData)
                if (!aData.mExtraData.contains(tObject)) aData.mExtraData.add(tObject);
            if (aStack.stackSize > 1) {
                if (aData.mMaterial != null) aData.mMaterial.mAmount /= aStack.stackSize;
                for (MaterialStack tMaterial : aData.mByProducts) tMaterial.mAmount /= aStack.stackSize;
                aStack = GTUtility.copyAmount(1, aStack);
            }
            sItemStack2DataMap.put(aStack, aData);
            FAST_ITEM_DATA_MAP.put(stackHash(aStack), aData);
            if (aData.hasValidMaterialData()) {
                long tValidMaterialAmount = aData.mMaterial.mMaterial.contains(SubTag.NO_RECYCLING) ? 0
                    : aData.mMaterial.mAmount >= 0 ? aData.mMaterial.mAmount : M;
                for (MaterialStack tMaterial : aData.mByProducts)
                    tValidMaterialAmount += tMaterial.mMaterial.contains(SubTag.NO_RECYCLING) ? 0
                        : tMaterial.mAmount >= 0 ? tMaterial.mAmount : M;
                if (tValidMaterialAmount < M) GTModHandler.addToRecyclerBlackList(aStack);
            }
            if (mRunThroughTheList) {
                if (GregTechAPI.sLoadStarted) {
                    mRunThroughTheList = false;
                    for (Entry<ItemStack, ItemData> tEntry : sItemStack2DataMap.entrySet()) if (!tEntry.getValue()
                        .hasValidPrefixData() || tEntry.getValue().mPrefix.mAllowNormalRecycling)
                        GTRecipeRegistrator.registerMaterialRecycling(
                            GTItemStack.internalCopyStack(tEntry.getKey()),
                            tEntry.getValue());
                }
            } else {
                if (!aData.hasValidPrefixData() || aData.mPrefix.mAllowNormalRecycling)
                    GTRecipeRegistrator.registerMaterialRecycling(aStack, aData);
            }
        } else {
            for (Object tObject : aData.mExtraData)
                if (!tData.mExtraData.contains(tObject)) tData.mExtraData.add(tObject);
        }
    }

    public static void removeItemData(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) {
            return;
        }
        sItemStack2DataMap.remove(aStack);
        FAST_ITEM_DATA_MAP.remove(stackHash(aStack));
    }

    public static void addAssociation(OrePrefixes aPrefix, Materials aMaterial, ItemStack aStack,
        boolean aBlackListed) {
        if (aPrefix == null || aMaterial == null || GTUtility.isStackInvalid(aStack)) return;
        if (Items.feather.getDamage(aStack) == WILDCARD) for (byte i = 0; i < 16; i++)
            setItemData(GTUtility.copyAmountAndMetaData(1, i, aStack), new ItemData(aPrefix, aMaterial, aBlackListed));
        setItemData(aStack, new ItemData(aPrefix, aMaterial, aBlackListed));
    }

    @Nullable
    public static ItemData getItemData(ItemStack aStack) {
        if (GTUtility.isStackInvalid(aStack)) return null;
        ItemData rData = FAST_ITEM_DATA_MAP.get(stackHash(aStack));
        if (rData == null) { // Try the lookup again but with wildcard damage value
            rData = FAST_ITEM_DATA_MAP.get(stackHash(aStack.getItem(), WILDCARD));
        }
        return rData;
    }

    @Nullable
    public static ItemData getAssociation(ItemStack aStack) {
        ItemData rData = getItemData(aStack);
        return rData != null && rData.hasValidPrefixMaterialData() ? rData : null;
    }

    /**
     * Converts an array of items to their unificated (likely GT) versions.
     * @param items The items to be unificated to their GT versions.
     * @param copy  Whether the stacks should be copied. You must not mutate the returned data if this is false.
     */
    public static ItemStack[] unificate(ItemStack[] items, boolean copy) {
        ItemStack[] itemsOut = null;

        for (int i = 0; i < items.length; i++) {
            ItemStack stack = items[i];

            if (stack == null) continue;

            Item item = stack.getItem();

            if (item == null) continue;
            if (item instanceof GTGenericItem) continue;

            ItemStack unificated = copy ? get(true, stack, false) : get_nocopy(true, stack);

            if (!GTUtility.areStacksEqual(stack, unificated)) {
                if (itemsOut == null) itemsOut = Arrays.copyOf(items, items.length);

                itemsOut[i] = stack;
            }
        }

        if (copy) {
            if (itemsOut == null) itemsOut = Arrays.copyOf(items, items.length);

            for (int i = 0; i < items.length; i++) {
                ItemStack src = items[i];
                ItemStack dst = itemsOut[i];

                if (src != null && src == dst) {
                    itemsOut[i] = src.copy();
                }
            }
        }

        return itemsOut == null ? items : itemsOut;
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
        OreDictionary.registerOre(tName, GTUtility.copyAmount(1, aStack));
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
