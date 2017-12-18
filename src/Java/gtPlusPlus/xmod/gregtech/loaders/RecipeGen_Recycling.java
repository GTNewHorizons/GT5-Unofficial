package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GT_Values.M;

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.lang3.reflect.FieldUtils;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.array.Pair;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeGen_Recycling implements Runnable {

	final Material toGenerate;
	public static Map<String, ItemStack> mNameMap;

	public RecipeGen_Recycling(final Material M) {
		this.toGenerate = M;
		if (mNameMap == null){
			mNameMap = this.getNameMap();			
		}
		if (mNameMap != null){
			generateRecipes(this.toGenerate);		
		}
	}

	@Override
	public void run() {
		
	}

	public static void generateRecipes(final Material material) {

		Utils.LOG_INFO("Generating Recycling recipes for " + material.getLocalizedName());

		final OrePrefixes[] mValidPrefixesAsString = { OrePrefixes.ingot, OrePrefixes.ingotHot, OrePrefixes.nugget,
				OrePrefixes.plate, OrePrefixes.plateDense, OrePrefixes.plateDouble, OrePrefixes.plateTriple,
				OrePrefixes.plateQuadruple, OrePrefixes.plateQuintuple, OrePrefixes.stick, OrePrefixes.stickLong,
				OrePrefixes.bolt, OrePrefixes.screw, OrePrefixes.ring, OrePrefixes.rotor, OrePrefixes.gearGt,
				OrePrefixes.gearGtSmall };

		Utils.LOG_INFO("Found " + mValidPrefixesAsString.length + " valid OreDict prefixes.");
		if (mValidPrefixesAsString.length >= 1) {
			for (final OrePrefixes validPrefix : mValidPrefixesAsString) {
				try {
					
					if (material.getState() != MaterialState.SOLID){
						continue;
					}
					
					final ItemStack tempStack = get(validPrefix, material, 1);
					final ItemStack mDust = getDust(material, validPrefix);
					final Pair<OrePrefixes, ItemStack> mData = getDustData(material, validPrefix);					
					int mFluidAmount = (int) GT_Utility.translateMaterialToFluidAmount(mData.getKey().mMaterialAmount, true);
							
					//Maceration
					if (tempStack != null) {
						// mValidItems[mSlotIndex++] = tempStack;
						if ((mDust != null) && GT_ModHandler.addPulverisationRecipe(tempStack, mDust)) {
							Utils.LOG_INFO("Recycle Recipe: " + material.getLocalizedName() + " - Success - Recycle "
									+ tempStack.getDisplayName() + " and obtain " + mDust.getDisplayName());
						}
						else {
							Utils.LOG_INFO("Recycle Recipe: " + material.getLocalizedName() + " - Failed");
							if (mDust == null) {
								Utils.LOG_INFO("Invalid Dust output.");
							}
						}
					}
					
					//Arc Furnace
					if (tempStack != null) {
						
					}					
					
					//Fluid Extractor
					if (tempStack != null) {
						// mValidItems[mSlotIndex++] = tempStack;
						if ((mDust != null) && GT_Values.RA.addFluidExtractionRecipe(tempStack, null, material.getFluid(mFluidAmount), 0, 30, 8)) {
							Utils.LOG_INFO("Recycle Recipe: " + material.getLocalizedName() + " - Success - Recycle "
									+ tempStack.getDisplayName() + " and obtain " + mDust.getDisplayName());
						}
						else {
							Utils.LOG_INFO("Recycle Recipe: " + material.getLocalizedName() + " - Failed");
							if (mDust == null) {
								Utils.LOG_INFO("Invalid Dust output.");
							}
						}
					}
					
				}
				catch (final Throwable t) {
					t.printStackTrace();
					// Utils.LOG_INFO("Returning Null. Throwable Info:
					// "+t.getMessage());
					// Utils.LOG_INFO("Throwable Info: "+t.toString());
					// Utils.LOG_INFO("Throwable Info:
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
			Utils.LOG_INFO("Built valid dust pair.");
			return new Pair<OrePrefixes, ItemStack>(mPrefix, mDust);
		}
		else {
			Utils.LOG_INFO("mPrefix: "+(mPrefix!=null));
			Utils.LOG_INFO("mDust: "+(mDust!=null));
		}
		Utils.LOG_INFO("Failed to build valid dust pair.");
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
			Utils.LOG_INFO("Trying to get a Dust");
			rStack = get(OrePrefixes.dust, aMaterial, aMaterialAmount / M);
		}
		if ((rStack == null) && ((((aMaterialAmount * 4) % M) == 0) || (aMaterialAmount >= (M * 8)))) {
			Utils.LOG_INFO("Trying to get a Small Dust");
			rStack = get(OrePrefixes.dustSmall, aMaterial, (aMaterialAmount * 4) / M);
		}
		if ((rStack == null) && (((aMaterialAmount * 9) >= M))) {
			Utils.LOG_INFO("Trying to get a Tiny Dust");
			rStack = get(OrePrefixes.dustTiny, aMaterial, (aMaterialAmount * 9) / M);
		}
		if (rStack == null) {
			Utils.LOG_INFO("Returning Null. Method: " + ReflectionUtils.getMethodName(0));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(1));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(2));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(3));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(4));
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
			Utils.LOG_INFO("Returning Null. Method: " + ReflectionUtils.getMethodName(0));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(1));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(2));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(3));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(4));
			return null;
		}
		if (!mNameMap.containsKey(aName.toString()) && aMentionPossibleTypos) {
			Utils.LOG_INFO("Unknown Key for Unification, Typo? " + aName);
		}
		return GT_Utility.copyAmount(aAmount,
				new Object[] { mNameMap.get(aName.toString()), getFirstOre(aName, aAmount), aReplacement });
	}

	public static ItemStack getFirstOre(final Object aName, final long aAmount) {
		if (GT_Utility.isStringInvalid(aName)) {
			Utils.LOG_INFO("Returning Null. Method: " + ReflectionUtils.getMethodName(0));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(1));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(2));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(3));
			Utils.LOG_INFO("Called from method: " + ReflectionUtils.getMethodName(4));
			return null;
		}
		final ItemStack tStack = (ItemStack) mNameMap.get(aName.toString());
		if (GT_Utility.isStackValid(tStack)) {
			Utils.LOG_INFO("Found valid stack.");
			return GT_Utility.copyAmount(aAmount, new Object[] { tStack });
		}
		return GT_Utility.copyAmount(aAmount, getOres(aName).toArray());
	}

	public static ArrayList<ItemStack> getOres(final Object aOreName) {
		final String aName = (aOreName == null) ? "" : aOreName.toString();
		final ArrayList<ItemStack> rList = new ArrayList<ItemStack>();
		if (GT_Utility.isStringValid(aName)) {
			Utils.LOG_INFO("Making a list of all OreDict entries for "+aOreName+".");
			if (rList.addAll(OreDictionary.getOres(aName))){
				Utils.LOG_INFO("Added "+rList.size()+" elements to list.");
			}
			else {
				Utils.LOG_INFO("Failed to Add Collection from oreDictionary, forcing an entry.");
				rList.add(ItemUtils.getItemStackOfAmountFromOreDict((String) aOreName, 1));
			}
		}
		return rList;
	}

	public Map<String, ItemStack> getNameMap() {
		Map<String, ItemStack> tempMap;
		try {
			tempMap = (Map<String, ItemStack>) FieldUtils.readStaticField(GT_OreDictUnificator.class, "sName2StackMap",
					true);
			if (tempMap != null) {
				Utils.LOG_INFO("Found 'sName2StackMap' in GT_OreDictUnificator.class.");
				return tempMap;
			}
		}
		catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		Utils.LOG_INFO("Invalid map stored in GT_OreDictUnificator.class, unable to find sName2StackMap field.");
		return null;
	}

}
