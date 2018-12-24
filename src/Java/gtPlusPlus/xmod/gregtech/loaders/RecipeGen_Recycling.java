package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GT_Values.M;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeGen_Recycling implements Runnable {

	public static AutoMap<Runnable> mQueuedRecyclingGenerators = new AutoMap<Runnable>();
	
	public static void executeGenerators() {
		if (mQueuedRecyclingGenerators.size() > 0) {
			for (Runnable R : mQueuedRecyclingGenerators.values()) {
				R.run();
			}
		}
	}
	
	final Material toGenerate;
	public static Map<String, ItemStack> mNameMap;

	public RecipeGen_Recycling(final Material M) {
		this.toGenerate = M;
		if (mNameMap == null){
			mNameMap = this.getNameMap();			
		}
		mQueuedRecyclingGenerators.put(this);
	}

	@Override
	public void run() {
		if (mNameMap != null){
			generateRecipes(this.toGenerate);		
		}
	}

	public static void generateRecipes(final Material material) {

		Logger.WARNING("Generating Recycling recipes for " + material.getLocalizedName());

		final OrePrefixes[] mValidPrefixesAsString = { OrePrefixes.ingot, OrePrefixes.ingotHot, OrePrefixes.nugget,
				OrePrefixes.plate, OrePrefixes.plateDense, OrePrefixes.plateDouble, OrePrefixes.plateTriple,
				OrePrefixes.plateQuadruple, OrePrefixes.plateQuintuple, OrePrefixes.stick, OrePrefixes.stickLong,
				OrePrefixes.bolt, OrePrefixes.screw, OrePrefixes.ring, OrePrefixes.rotor, OrePrefixes.gearGt,
				OrePrefixes.gearGtSmall, OrePrefixes.gear, OrePrefixes.block, OrePrefixes.cableGt01, OrePrefixes.cableGt02,
				OrePrefixes.cableGt04, OrePrefixes.cableGt08, OrePrefixes.cableGt12, OrePrefixes.wireFine, OrePrefixes.wireGt01,
				OrePrefixes.wireGt02, OrePrefixes.wireGt04, OrePrefixes.wireGt08, OrePrefixes.wireGt12, OrePrefixes.wireGt16,
				OrePrefixes.foil, OrePrefixes.frameGt, OrePrefixes.pipeHuge, OrePrefixes.pipeLarge, OrePrefixes.pipeMedium, OrePrefixes.pipeSmall, OrePrefixes.pipeTiny,
				};

		int mSlotIndex = 0;		
		Pair<OrePrefixes, ItemStack>[] mValidPairs = new Pair[mValidPrefixesAsString.length];
		
		for (int r=0;r<mValidPairs.length;r++){
			ItemStack temp = getItemStackOfAmountFromOreDictNoBroken(mValidPrefixesAsString[r].name()+Utils.sanitizeString(material.getLocalizedName()), 1);
			if (temp != null){
				mValidPairs[mSlotIndex++] = new Pair<OrePrefixes, ItemStack>(mValidPrefixesAsString[r], temp.copy());
			}
		}
		
		if (mValidPairs.length > 0){
			int validCounter = 0;
			Pair<OrePrefixes, ItemStack>[] temp = mValidPairs;
			for (Pair<OrePrefixes, ItemStack> temp2 : mValidPairs){
				if (temp2 != null){
					Logger.WARNING("Valid: "+temp2.getValue().getDisplayName());
					validCounter++;
				}
			}
			Pair<OrePrefixes, ItemStack> temp3[] = new Pair[validCounter];
			int temp4 = 0;
			for (Pair<OrePrefixes, ItemStack> r : mValidPairs){
				if (r != null){
					temp3[temp4++] = r;					
				}
			}
			if (temp3.length > 0){
				mValidPairs = temp3.clone();
			}
		}
		
		if (mValidPrefixesAsString.length >= 1) {
			for (final Pair<OrePrefixes, ItemStack> validPrefix : mValidPairs) {
				try {
					
					if (material == null || validPrefix == null || material.getState() != MaterialState.SOLID || validPrefix.getKey() == OrePrefixes.ingotHot) {
						continue;
					}
					
					final ItemStack tempStack = validPrefix.getValue();
					final ItemStack mDust = getDust(material, validPrefix.getKey());
					final Pair<OrePrefixes, ItemStack> mData = getDustData(material, validPrefix.getKey());					
					int mFluidAmount = (int) GT_Utility.translateMaterialToFluidAmount(validPrefix.getKey().mMaterialAmount, true);
							
					//Maceration
					if (ItemUtils.checkForInvalidItems(tempStack)) {
						// mValidItems[mSlotIndex++] = tempStack;
						if ((mDust != null) && GT_ModHandler.addPulverisationRecipe(tempStack, mDust)) {
							Logger.WARNING("Recycle Recipe: " + material.getLocalizedName() + " - Success - Recycle "
									+ tempStack.getDisplayName() + " and obtain " + mDust.getDisplayName());
						}
						else {
							Logger.WARNING("Recycle Recipe: " + material.getLocalizedName() + " - Failed");
							if (mDust == null) {
								Logger.WARNING("Invalid Dust output.");
							}
						}
					}
					
					//Arc Furnace
					if (ItemUtils.checkForInvalidItems(tempStack)) {
						
					}					
					
					//Fluid Extractor
					if (ItemUtils.checkForInvalidItems(tempStack)) {
						// mValidItems[mSlotIndex++] = tempStack;
						if ((mDust != null) && GT_Values.RA.addFluidExtractionRecipe(tempStack, null, material.getFluid(mFluidAmount), 0, 30, 8)) {
							Logger.WARNING("Fluid Recycle Recipe: " + material.getLocalizedName() + " - Success - Recycle "
									+ tempStack.getDisplayName() + " and obtain " + mFluidAmount+"mb of "+material.getFluid(1).getLocalizedName()+".");
						}
						else {
							Logger.WARNING("Fluid Recycle Recipe: " + material.getLocalizedName() + " - Failed");
							if (mDust == null) {
								Logger.WARNING("Invalid Dust output.");
							}
						}
					}
					
				}
				catch (final Throwable t) {
					t.printStackTrace();
					// Utils.LOG_WARNING("Returning Null. Throwable Info:
					// "+t.getMessage());
					// Utils.LOG_WARNING("Throwable Info: "+t.toString());
					// Utils.LOG_WARNING("Throwable Info:
					// "+t.getCause().toString());
				}

			}
		}
	}

	public static Pair<OrePrefixes, ItemStack> getDustData(final Material aMaterial, final OrePrefixes aPrefix) {
		return getDustData(aMaterial, aPrefix.mMaterialAmount);
	}
	
	public static Pair<OrePrefixes, ItemStack> getDustData(final Material aMaterial, final long aMaterialAmount) {
		ItemStack mDust = null;
		OrePrefixes mPrefix = null;
		
		if (aMaterial == null || aMaterialAmount <= 0) {
			return null;
		}
		if ((((aMaterialAmount % M) == 0) || (aMaterialAmount >= (M * 16)))) {
			mDust = get(OrePrefixes.dust, aMaterial, aMaterialAmount / M);
			mPrefix = OrePrefixes.dust;
		}
		if ((mDust == null) && ((((aMaterialAmount * 4) % M) == 0) || (aMaterialAmount >= (M * 8)))) {
			mDust = get(OrePrefixes.dustSmall, aMaterial, (aMaterialAmount * 4) / M);
			mPrefix = OrePrefixes.dustSmall;
		}
		if ((mDust == null) && (((aMaterialAmount * 9) >= M))) {
			mDust = get(OrePrefixes.dustTiny, aMaterial, (aMaterialAmount * 9) / M);
			mPrefix = OrePrefixes.dustTiny;
		}
		
		if (mPrefix != null && mDust != null){
			Logger.WARNING("Built valid dust pair.");
			return new Pair<OrePrefixes, ItemStack>(mPrefix, mDust);
		}
		else {
			Logger.WARNING("mPrefix: "+(mPrefix!=null));
			Logger.WARNING("mDust: "+(mDust!=null));
		}
		Logger.WARNING("Failed to build valid dust pair.");
		return null;		
	}
	
	public static ItemStack getDust(final Material aMaterial, final OrePrefixes aPrefix) {
		return aMaterial == null ? null : getDust(aMaterial, aPrefix.mMaterialAmount);
	}
	
	public static ItemStack getDust(final Material aMaterial, final long aMaterialAmount) {
		if (aMaterialAmount <= 0) {
			return null;
		}
		ItemStack rStack = null;
		if ((((aMaterialAmount % M) == 0) || (aMaterialAmount >= (M * 16)))) {
			Logger.WARNING("Trying to get a Dust");
			rStack = get(OrePrefixes.dust, aMaterial, aMaterialAmount / M);
		}
		if ((rStack == null) && ((((aMaterialAmount * 4) % M) == 0) || (aMaterialAmount >= (M * 8)))) {
			Logger.WARNING("Trying to get a Small Dust");
			rStack = get(OrePrefixes.dustSmall, aMaterial, (aMaterialAmount * 4) / M);
		}
		if ((rStack == null) && (((aMaterialAmount * 9) >= M))) {
			Logger.WARNING("Trying to get a Tiny Dust");
			rStack = get(OrePrefixes.dustTiny, aMaterial, (aMaterialAmount * 9) / M);
		}
		return rStack;
	}

	public static ItemStack get(final Object aName, final long aAmount) {
		return get(aName, null, aAmount, true, true);
	}

	public static ItemStack get(final Object aName, final ItemStack aReplacement, final long aAmount) {
		return get(aName, aReplacement, aAmount, true, true);
	}

	public static ItemStack get(final OrePrefixes aPrefix, final Material aMaterial, final long aAmount) {
		return get(aPrefix, aMaterial, null, aAmount);
	}

	public static ItemStack get(final OrePrefixes aPrefix, final Material aMaterial, final ItemStack aReplacement,
			final long aAmount) {
		return get(aPrefix.name()+Utils.sanitizeString(aMaterial.getLocalizedName()), aReplacement, aAmount, false, true);
	}

	public static ItemStack get(final Object aName, final ItemStack aReplacement, final long aAmount,
			final boolean aMentionPossibleTypos, final boolean aNoInvalidAmounts) {
		if (aNoInvalidAmounts && (aAmount < 1L)) {
			Logger.WARNING("Returning Null. Method: " + ReflectionUtils.getMethodName(0));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(1));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(2));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(3));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(4));
			return null;
		}
		if (!mNameMap.containsKey(aName.toString()) && aMentionPossibleTypos) {
			Logger.WARNING("Unknown Key for Unification, Typo? " + aName);
		}
		return GT_Utility.copyAmount(aAmount,
				new Object[] { mNameMap.get(aName.toString()), getFirstOre(aName, aAmount), aReplacement });
	}

	public static ItemStack getFirstOre(final Object aName, final long aAmount) {
		if (GT_Utility.isStringInvalid(aName)) {
			Logger.WARNING("Returning Null. Method: " + ReflectionUtils.getMethodName(0));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(1));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(2));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(3));
			Logger.WARNING("Called from method: " + ReflectionUtils.getMethodName(4));
			return null;
		}
		final ItemStack tStack = mNameMap.get(aName.toString());
		if (GT_Utility.isStackValid(tStack)) {
			Logger.WARNING("Found valid stack.");
			return GT_Utility.copyAmount(aAmount, new Object[] { tStack });
		}
		return GT_Utility.copyAmount(aAmount, getOres(aName).toArray());
	}

	public static ArrayList<ItemStack> getOres(final Object aOreName) {
		final String aName = (aOreName == null) ? "" : aOreName.toString();
		final ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
		if (GT_Utility.isStringValid(aName)) {
			Logger.WARNING("Making a list of all OreDict entries for "+aOreName+".");
			if (rList.addAll(OreDictionary.getOres(aName))){
				Logger.WARNING("Added "+rList.size()+" elements to list.");
			}
			else {
				Logger.WARNING("Failed to Add Collection from oreDictionary, forcing an entry.");
				rList.add(ItemUtils.getItemStackOfAmountFromOreDict((String) aOreName, 1));
			}
		}
		return rList;
	}

	@SuppressWarnings("unchecked")
	public Map<String, ItemStack> getNameMap() {
		Map<String, ItemStack> tempMap;
		try {
			tempMap = (Map<String, ItemStack>) FieldUtils.readStaticField(GT_OreDictUnificator.class, "sName2StackMap",
					true);
			if (tempMap != null) {
				Logger.WARNING("Found 'sName2StackMap' in GT_OreDictUnificator.class.");
				return tempMap;
			}
		}
		catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		Logger.WARNING("Invalid map stored in GT_OreDictUnificator.class, unable to find sName2StackMap field.");
		return null;
	}
	
	public static ItemStack getItemStackOfAmountFromOreDictNoBroken(String oredictName, final int amount) {

		try {

			if (oredictName.contains("-") || oredictName.contains("_")) {
				oredictName = Utils.sanitizeString(oredictName, new char[] {'-', '_'});			
			}
			else {
				oredictName = Utils.sanitizeString(oredictName);			
			}

			// Adds a check to grab dusts using GT methodology if possible.
			ItemStack returnValue = null;
			if (oredictName.toLowerCase().contains("dust")) {
				final String MaterialName = oredictName.toLowerCase().replace("dust", "");
				final Materials m = Materials.get(MaterialName);
				if (m != null && m != Materials._NULL) {
					returnValue = ItemUtils.getGregtechDust(m, amount);
					if (ItemUtils.checkForInvalidItems(returnValue)) {
						return returnValue;
					}
				}
			}
			if (returnValue == null) {
				returnValue = getItemStackOfAmountFromOreDict(oredictName, amount);				
				if (ItemUtils.checkForInvalidItems(returnValue)) {
					return returnValue.copy();					
				}
			}			
			return null;
		} catch (final Throwable t) {
			return null;
		}
	}
	
	public static ItemStack getItemStackOfAmountFromOreDict(String oredictName, final int amount) {
		String mTemp = oredictName;

		// Banned Materials and replacements for GT5.8 compat.

		if (oredictName.toLowerCase().contains("ingotclay")) {
			return ItemUtils.getSimpleStack(Items.clay_ball, amount);
		}

		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			if (oredictName.toLowerCase().contains("rutile")) {
				mTemp = oredictName.replace("Rutile", "Titanium");
			}
			if (oredictName.toLowerCase().contains("vanadiumsteel")) {
				mTemp = oredictName.replace("VanadiumSteel", "StainlessSteel");
			}
		}
		final ArrayList<ItemStack> oreDictList = OreDictionary.getOres(mTemp);
		if (!oreDictList.isEmpty()) {
			final ItemStack returnValue = oreDictList.get(0).copy();
			returnValue.stackSize = amount;
			return returnValue;
		}
		return null;
		//return getItemStackOfAmountFromOreDictNoBroken(mTemp, amount);
	}

}
