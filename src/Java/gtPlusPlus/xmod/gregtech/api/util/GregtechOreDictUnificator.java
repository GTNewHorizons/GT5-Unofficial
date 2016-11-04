package gtPlusPlus.xmod.gregtech.api.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.objects.GT_HashSet;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.*;
import gregtech.common.GT_Proxy.OreDictEventContainer;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.api.objects.GregtechItemData;
import gtPlusPlus.xmod.gregtech.api.objects.GregtechMaterialStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the Core of my OreDict Unification Code
 * <p/>
 * If you just want to use this to unificate your Items, then use the Function
 * in the GregTech_API File
 * <p/>
 * P.S. It is intended to be named "Unificator" and not "Unifier", because that
 * sounds more awesome.
 */
public class GregtechOreDictUnificator {
	private static final HashMap<String, ItemStack>					sName2StackMap		= new HashMap<String, ItemStack>();
	private static final HashMap<GT_ItemStack, GregtechItemData>	sItemStack2DataMap	= new HashMap<GT_ItemStack, GregtechItemData>();
	private static final GT_HashSet<GT_ItemStack>					sNoUnificationList	= new GT_HashSet<GT_ItemStack>();
	public static volatile int										VERSION				= 508;
	private static int												isRegisteringOre	= 0, isAddingOre = 0;
	private static boolean											mRunThroughTheList	= true;

	static {
		GregTech_API.sItemStackMappings.add(GregtechOreDictUnificator.sItemStack2DataMap);
	}

	public static void add(final GregtechOrePrefixes aPrefix, final GT_Materials aMaterial, final ItemStack aStack) {
		GregtechOreDictUnificator.set(aPrefix, aMaterial, aStack, false, false);
	}

	public static void addAssociation(final GregtechOrePrefixes aPrefix, final GT_Materials aMaterial,
			final ItemStack aStack, final boolean aBlackListed) {
		if (aPrefix == null || aMaterial == null || GT_Utility.isStackInvalid(aStack)) {
			return;
		}
		if (Items.feather.getDamage(aStack) == GT_Values.W) {
			for (byte i = 0; i < 16; i++) {
				GregtechOreDictUnificator.setItemData(GT_Utility.copyAmountAndMetaData(1, i, aStack),
						new GregtechItemData(aPrefix, aMaterial, aBlackListed));
			}
		}
		GregtechOreDictUnificator.setItemData(aStack, new GregtechItemData(aPrefix, aMaterial, aBlackListed));
	}

	public static void addItemData(final ItemStack aStack, final GregtechItemData aData) {
		if (GT_Utility.isStackValid(aStack) && GregtechOreDictUnificator.getItemData(aStack) == null && aData != null) {
			GregtechOreDictUnificator.setItemData(aStack, aData);
		}
	}

	/**
	 * The Blacklist just prevents the Item from being unificated into something
	 * else. Useful if you have things like the Industrial Diamond, which is
	 * better than regular Diamond, but also usable in absolutely all Diamond
	 * Recipes.
	 */
	public static void addToBlacklist(final ItemStack aStack) {
		if (GT_Utility.isStackValid(aStack)
				&& !GT_Utility.isStackInList(aStack, GregtechOreDictUnificator.sNoUnificationList)) {
			GregtechOreDictUnificator.sNoUnificationList.add(aStack);
		}
	}

	public static ItemStack get(final boolean aUseBlackList, final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return null;
		}
		final GregtechItemData tPrefixMaterial = GregtechOreDictUnificator.getAssociation(aStack);
		ItemStack rStack = null;
		if (tPrefixMaterial == null || !tPrefixMaterial.hasValidPrefixMaterialData()
				|| aUseBlackList && tPrefixMaterial.mBlackListed) {
			return GT_Utility.copy(aStack);
		}
		if (aUseBlackList && !GregTech_API.sUnificationEntriesRegistered
				&& GregtechOreDictUnificator.isBlacklisted(aStack)) {
			tPrefixMaterial.mBlackListed = true;
			return GT_Utility.copy(aStack);
		}
		if (tPrefixMaterial.mUnificationTarget == null) {
			tPrefixMaterial.mUnificationTarget = GregtechOreDictUnificator.sName2StackMap
					.get(tPrefixMaterial.toString());
		}
		rStack = tPrefixMaterial.mUnificationTarget;
		if (GT_Utility.isStackInvalid(rStack)) {
			return GT_Utility.copy(aStack);
		}
		assert rStack != null;
		rStack.setTagCompound(aStack.getTagCompound());
		return GT_Utility.copyAmount(aStack.stackSize, rStack);
	}

	public static ItemStack get(final GregtechOrePrefixes aPrefix, final Object aMaterial, final ItemStack aReplacement,
			final long aAmount) {
		return GregtechOreDictUnificator.get(aPrefix.get(aMaterial), aReplacement, aAmount, false, true);
	}

	public static ItemStack get(final GregtechOrePrefixes aPrefix, final Object aMaterial, final long aAmount) {
		return GregtechOreDictUnificator.get(aPrefix, aMaterial, null, aAmount);
	}

	public static ItemStack get(final ItemStack aStack) {
		return GregtechOreDictUnificator.get(true, aStack);
	}

	public static ItemStack get(final Object aName, final ItemStack aReplacement, final long aAmount) {
		return GregtechOreDictUnificator.get(aName, aReplacement, aAmount, true, true);
	}

	public static ItemStack get(final Object aName, final ItemStack aReplacement, final long aAmount,
			final boolean aMentionPossibleTypos, final boolean aNoInvalidAmounts) {
		if (aNoInvalidAmounts && aAmount < 1) {
			return null;
		}
		if (!GregtechOreDictUnificator.sName2StackMap.containsKey(aName.toString()) && aMentionPossibleTypos) {
			GT_Log.err.println("Unknown Key for Unification, Typo? " + aName);
		}
		return GT_Utility.copyAmount(aAmount, GregtechOreDictUnificator.sName2StackMap.get(aName.toString()),
				GregtechOreDictUnificator.getFirstOre(aName, aAmount), aReplacement);
	}

	public static ItemStack get(final Object aName, final long aAmount) {
		return GregtechOreDictUnificator.get(aName, null, aAmount, true, true);
	}

	public static GregtechItemData getAssociation(final ItemStack aStack) {
		final GregtechItemData rData = GregtechOreDictUnificator.getItemData(aStack);
		return rData != null && rData.hasValidPrefixMaterialData() ? rData : null;
	}

	public static ItemStack getDust(final GregtechMaterialStack aMaterial) {
		return aMaterial == null ? null : GregtechOreDictUnificator.getDust(aMaterial.mMaterial, aMaterial.mAmount);
	}

	public static ItemStack getDust(final GT_Materials aMaterial, final long aMaterialAmount) {
		if (aMaterialAmount <= 0) {
			return null;
		}
		ItemStack rStack = null;
		if (aMaterialAmount % GT_Values.M == 0 || aMaterialAmount >= GT_Values.M * 16) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.dust, aMaterial, aMaterialAmount / GT_Values.M);
		}
		if (rStack == null && (aMaterialAmount * 4 % GT_Values.M == 0 || aMaterialAmount >= GT_Values.M * 8)) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.dustSmall, aMaterial,
					aMaterialAmount * 4 / GT_Values.M);
		}
		if (rStack == null && aMaterialAmount * 9 >= GT_Values.M) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.dustTiny, aMaterial,
					aMaterialAmount * 9 / GT_Values.M);
		}
		return rStack;
	}

	public static ItemStack getDust(final GT_Materials aMaterial, final OrePrefixes aPrefix) {
		return aMaterial == null ? null : GregtechOreDictUnificator.getDust(aMaterial, aPrefix.mMaterialAmount);
	}

	public static ItemStack getDustOrIngot(final GregtechMaterialStack aMaterial) {
		ItemStack rStack = GregtechOreDictUnificator.getDust(aMaterial);
		if (rStack == null) {
			rStack = GregtechOreDictUnificator.getIngot(aMaterial);
		}
		return rStack;
	}

	public static ItemStack getDustOrIngot(final GT_Materials aMaterial, final long aMaterialAmount) {
		if (aMaterialAmount <= 0) {
			return null;
		}
		ItemStack rStack = GregtechOreDictUnificator.getDust(aMaterial, aMaterialAmount);
		if (rStack == null) {
			rStack = GregtechOreDictUnificator.getIngot(aMaterial, aMaterialAmount);
		}
		return rStack;
	}

	public static ItemStack getFirstOre(final Object aName, final long aAmount) {
		if (GT_Utility.isStringInvalid(aName)) {
			return null;
		}
		final ItemStack tStack = GregtechOreDictUnificator.sName2StackMap.get(aName.toString());
		if (GT_Utility.isStackValid(tStack)) {
			return GT_Utility.copyAmount(aAmount, tStack);
		}
		return GT_Utility.copyAmount(aAmount, GregtechOreDictUnificator.getOres(aName).toArray());
	}

	public static ItemStack getGem(final GregtechMaterialStack aMaterial) {
		return aMaterial == null ? null : GregtechOreDictUnificator.getGem(aMaterial.mMaterial, aMaterial.mAmount);
	}

	public static ItemStack getGem(final GT_Materials aMaterial, final long aMaterialAmount) {
		ItemStack rStack = null;
		if (aMaterialAmount >= GT_Values.M || aMaterialAmount >= GT_Values.M * 32) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.gem, aMaterial, aMaterialAmount / GT_Values.M);
		}
		if (rStack == null && (aMaterialAmount * 2 % GT_Values.M == 0 || aMaterialAmount >= GT_Values.M * 16)) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.gemFlawed, aMaterial,
					aMaterialAmount * 2 / GT_Values.M);
		}
		if (rStack == null && aMaterialAmount * 4 >= GT_Values.M) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.gemChipped, aMaterial,
					aMaterialAmount * 4 / GT_Values.M);
		}
		return rStack;
	}

	public static ItemStack getGem(final GT_Materials aMaterial, final OrePrefixes aPrefix) {
		return aMaterial == null ? null : GregtechOreDictUnificator.getGem(aMaterial, aPrefix.mMaterialAmount);
	}

	public static ItemStack getIngot(final GregtechMaterialStack aMaterial) {
		return aMaterial == null ? null : GregtechOreDictUnificator.getIngot(aMaterial.mMaterial, aMaterial.mAmount);
	}

	public static ItemStack getIngot(final GT_Materials aMaterial, final long aMaterialAmount) {
		if (aMaterialAmount <= 0) {
			return null;
		}
		ItemStack rStack = null;
		if (aMaterialAmount % (GT_Values.M * 9) == 0 && aMaterialAmount / (GT_Values.M * 9) > 1
				|| aMaterialAmount >= GT_Values.M * 72) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.block, aMaterial,
					aMaterialAmount / (GT_Values.M * 9));
		}
		if (rStack == null && (aMaterialAmount % GT_Values.M == 0 || aMaterialAmount >= GT_Values.M * 8)) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.ingot, aMaterial, aMaterialAmount / GT_Values.M);
		}
		if (rStack == null && aMaterialAmount * 9 >= GT_Values.M) {
			rStack = GregtechOreDictUnificator.get(GregtechOrePrefixes.nugget, aMaterial,
					aMaterialAmount * 9 / GT_Values.M);
		}
		return rStack;
	}

	public static ItemStack getIngot(final GT_Materials aMaterial, final OrePrefixes aPrefix) {
		return aMaterial == null ? null : GregtechOreDictUnificator.getIngot(aMaterial, aPrefix.mMaterialAmount);
	}

	public static ItemStack getIngotOrDust(final GregtechMaterialStack aMaterial) {
		ItemStack rStack = GregtechOreDictUnificator.getIngot(aMaterial);
		if (aMaterial != null && aMaterial.mMaterial != null) {
			rStack = GregtechOreDictUnificator.getDust(aMaterial);
		}
		if (rStack == null) {
			rStack = GregtechOreDictUnificator.getDust(aMaterial);
		}
		return rStack;
	}

	public static ItemStack getIngotOrDust(final GT_Materials aMaterial, final long aMaterialAmount) {
		if (aMaterialAmount <= 0) {
			return null;
		}
		ItemStack rStack = GregtechOreDictUnificator.getIngot(aMaterial, aMaterialAmount);
		if (rStack == null) {
			rStack = GregtechOreDictUnificator.getDust(aMaterial, aMaterialAmount);
		}
		return rStack;
	}

	public static GregtechItemData getItemData(final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return null;
		}
		GregtechItemData rData = GregtechOreDictUnificator.sItemStack2DataMap.get(new GT_ItemStack(aStack));
		if (rData == null) {
			rData = GregtechOreDictUnificator.sItemStack2DataMap
					.get(new GT_ItemStack(GT_Utility.copyMetaData(GT_Values.W, aStack)));
		}
		return rData;
	}

	/**
	 * @return a Copy of the OreDictionary.getOres() List
	 */
	public static ArrayList<ItemStack> getOres(final Object aOreName) {
		final String aName = aOreName == null ? GT_Values.E : aOreName.toString();
		final ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
		if (GT_Utility.isStringValid(aName)) {
			rList.addAll(OreDictionary.getOres(aName));
		}
		return rList;
	}

	/**
	 * @return a Copy of the OreDictionary.getOres() List
	 */
	public static ArrayList<ItemStack> getOres(final OrePrefixes aPrefix, final Object aMaterial) {
		return GregtechOreDictUnificator.getOres(aPrefix.get(aMaterial));
	}

	public static ItemStack[] getStackArray(final boolean aUseBlackList, final Object... aStacks) {
		final ItemStack[] rStacks = new ItemStack[aStacks.length];
		for (int i = 0; i < aStacks.length; i++) {
			rStacks[i] = GregtechOreDictUnificator.get(aUseBlackList, GT_Utility.copy(aStacks[i]));
		}
		return rStacks;
	}

	public static boolean isAddingOres() {
		return GregtechOreDictUnificator.isAddingOre > 0;
	}

	public static boolean isBlacklisted(final ItemStack aStack) {
		return GT_Utility.isStackInList(aStack, GregtechOreDictUnificator.sNoUnificationList);
	}

	public static boolean isItemStackDye(final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return false;
		}
		for (final Dyes tDye : Dyes.VALUES) {
			if (GregtechOreDictUnificator.isItemStackInstanceOf(aStack, tDye.toString())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isItemStackInstanceOf(final ItemStack aStack, final Object aName) {
		if (GT_Utility.isStringInvalid(aName) || GT_Utility.isStackInvalid(aStack)) {
			return false;
		}
		for (final ItemStack tOreStack : GregtechOreDictUnificator.getOres(aName.toString())) {
			if (GT_Utility.areStacksEqual(tOreStack, aStack, true)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isRegisteringOres() {
		return GregtechOreDictUnificator.isRegisteringOre > 0;
	}

	public static boolean registerOre(final GregtechOrePrefixes aPrefix, final Object aMaterial,
			final ItemStack aStack) {
		return GregtechOreDictUnificator.registerOre(aPrefix.get(aMaterial), aStack);
	}

	public static boolean registerOre(final Object aName, final ItemStack aStack) {
		if (aName == null || GT_Utility.isStackInvalid(aStack)) {
			return false;
		}
		final String tName = aName.toString();
		if (GT_Utility.isStringInvalid(tName)) {
			return false;
		}
		final ArrayList<ItemStack> tList = GregtechOreDictUnificator.getOres(tName);
		for (int i = 0; i < tList.size(); i++) {
			if (GT_Utility.areStacksEqual(tList.get(i), aStack, true)) {
				return false;
			}
		}
		GregtechOreDictUnificator.isRegisteringOre++;
		OreDictionary.registerOre(tName, GT_Utility.copyAmount(1, aStack));
		GregtechOreDictUnificator.isRegisteringOre--;
		return true;
	}

	public static void registerRecipes(final OreDictEventContainer tOre) {
		// TODO Auto-generated method stub

	}

	public static void resetUnificationEntries() {
		for (final GregtechItemData tPrefixMaterial : GregtechOreDictUnificator.sItemStack2DataMap.values()) {
			tPrefixMaterial.mUnificationTarget = null;
		}
	}

	public static void set(final GregtechOrePrefixes aPrefix, final GT_Materials aMaterial, final ItemStack aStack) {
		GregtechOreDictUnificator.set(aPrefix, aMaterial, aStack, true, false);
	}

	public static void set(final GregtechOrePrefixes aPrefix, final GT_Materials aMaterial, ItemStack aStack,
			final boolean aOverwrite, final boolean aAlreadyRegistered) {
		if (aMaterial == null || aPrefix == null || GT_Utility.isStackInvalid(aStack)
				|| Items.feather.getDamage(aStack) == GT_Values.W) {
			return;
		}
		GregtechOreDictUnificator.isAddingOre++;
		aStack = GT_Utility.copyAmount(1, aStack);
		if (!aAlreadyRegistered) {
			GregtechOreDictUnificator.registerOre(aPrefix.get(aMaterial), aStack);
		}
		GregtechOreDictUnificator.addAssociation(aPrefix, aMaterial, aStack,
				GregtechOreDictUnificator.isBlacklisted(aStack));
		if (aOverwrite || GT_Utility
				.isStackInvalid(GregtechOreDictUnificator.sName2StackMap.get(aPrefix.get(aMaterial).toString()))) {
			GregtechOreDictUnificator.sName2StackMap.put(aPrefix.get(aMaterial).toString(), aStack);
		}
		GregtechOreDictUnificator.isAddingOre--;
	}

	public static void setItemData(ItemStack aStack, final GregtechItemData aData) {
		if (GT_Utility.isStackInvalid(aStack) || aData == null) {
			return;
		}
		final GregtechItemData tData = GregtechOreDictUnificator.getItemData(aStack);
		if (tData == null || !tData.hasValidPrefixMaterialData()) {
			if (tData != null) {
				for (final Object tObject : tData.mExtraData) {
					if (!aData.mExtraData.contains(tObject)) {
						aData.mExtraData.add(tObject);
					}
				}
			}
			if (aStack.stackSize > 1) {
				if (aData.mMaterial != null) {
					aData.mMaterial.mAmount /= aStack.stackSize;
				}
				for (final GregtechMaterialStack tMaterial : aData.mByProducts) {
					tMaterial.mAmount /= aStack.stackSize;
				}
				aStack = GT_Utility.copyAmount(1, aStack);
			}
			GregtechOreDictUnificator.sItemStack2DataMap.put(new GT_ItemStack(aStack), aData);
			if (aData.hasValidMaterialData()) {
				long tValidMaterialAmount = aData.mMaterial.mMaterial.contains(SubTag.NO_RECYCLING) ? 0
						: aData.mMaterial.mAmount >= 0 ? aData.mMaterial.mAmount : GT_Values.M;
				for (final GregtechMaterialStack tMaterial : aData.mByProducts) {
					tValidMaterialAmount += tMaterial.mMaterial.contains(SubTag.NO_RECYCLING) ? 0
							: tMaterial.mAmount >= 0 ? tMaterial.mAmount : GT_Values.M;
				}
				if (tValidMaterialAmount < GT_Values.M) {
					GT_ModHandler.addToRecyclerBlackList(aStack);
				}
			}
			if (GregtechOreDictUnificator.mRunThroughTheList) {
				if (GregTech_API.sLoadStarted) {
					GregtechOreDictUnificator.mRunThroughTheList = false;
					for (final Entry<GT_ItemStack, GregtechItemData> tEntry : GregtechOreDictUnificator.sItemStack2DataMap
							.entrySet()) {
						if (!tEntry.getValue().hasValidPrefixData()
								|| tEntry.getValue().mPrefix.mAllowNormalRecycling) {
							GregtechRecipeRegistrator.registerMaterialRecycling(tEntry.getKey().toStack(),
									tEntry.getValue());
						}
					}
				}
			}
			else {
				if (!aData.hasValidPrefixData() || aData.mPrefix.mAllowNormalRecycling) {
					GregtechRecipeRegistrator.registerMaterialRecycling(aStack, aData);
				}
			}
		}
		else {
			for (final Object tObject : aData.mExtraData) {
				if (!tData.mExtraData.contains(tObject)) {
					tData.mExtraData.add(tObject);
				}
			}
		}
	}

	public static ItemStack setStack(final boolean aUseBlackList, final ItemStack aStack) {
		if (GT_Utility.isStackInvalid(aStack)) {
			return aStack;
		}
		final ItemStack tStack = GregtechOreDictUnificator.get(aUseBlackList, aStack);
		if (GT_Utility.areStacksEqual(aStack, tStack)) {
			return aStack;
		}
		aStack.func_150996_a(tStack.getItem());
		Items.feather.setDamage(aStack, Items.feather.getDamage(tStack));
		return aStack;
	}

	public static ItemStack setStack(final ItemStack aStack) {
		return GregtechOreDictUnificator.setStack(true, aStack);
	}

	public static ItemStack[] setStackArray(final boolean aUseBlackList, final ItemStack... aStacks) {
		for (int i = 0; i < aStacks.length; i++) {
			aStacks[i] = GregtechOreDictUnificator.get(aUseBlackList, GT_Utility.copy(aStacks[i]));
		}
		return aStacks;
	}
}
