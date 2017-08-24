package gregtech.api.util;

import static gregtech.api.enums.GT_Values.*;

import java.util.*;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.core.util.Utils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class CustomRecipeMap/* extends GT_Recipe_Map*/{
	/**
	 * Contains all Recipe Maps
	 */
	public static final Collection<CustomRecipeMap> sMappings = new ArrayList<>();

	//public static final CustomRecipeMap sOreWasherRecipes = new GT_Recipe_Map_OreWasher(new HashSet<GT_Recipe>(0), "ic.recipe.orewasher", "Ore Washer", "ic2.blockOreWashingPlant", RES_PATH_GUI + "basicmachines/OreWasher", 1, 3, 1, 1, 1, E, 1, E, true, false);
	//Fission Fuel Plant Recipes
	public static final CustomRecipeMap sFissionFuelProcessing = new CustomRecipeMap(new HashSet<GT_Recipe>(50), "gt.recipe.fissionfuel", "Fission Fuel Processing", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 4, 1, E, 1, E, true, true);

	/**
	 * HashMap of Recipes based on their Items
	 */
	public final Map<GT_ItemStack, Collection<GT_Recipe>> mRecipeItemMap = new HashMap<>();
	/**
	 * HashMap of Recipes based on their Fluids
	 */
	public final Map<Fluid, Collection<GT_Recipe>> mRecipeFluidMap = new HashMap<>();
	/**
	 * The List of all Recipes
	 */
	public final Collection<GT_Recipe> mRecipeList;
	/**
	 * String used as an unlocalised Name.
	 */
	public final String mUnlocalizedName;
	/**
	 * String used in NEI for the Recipe Lists. If null it will use the unlocalised Name instead
	 */
	public final String mNEIName;
	/**
	 * GUI used for NEI Display. Usually the GUI of the Machine itself
	 */
	public final String mNEIGUIPath;
	public final String mNEISpecialValuePre, mNEISpecialValuePost;
	public final int mUsualInputCount, mUsualOutputCount, mNEISpecialValueMultiplier, mMinimalInputItems, mMinimalInputFluids, mAmperage;
	public final boolean mNEIAllowed, mShowVoltageAmperageInNEI;

	/**
	 * Initialises a new type of Recipe Handler.
	 *
	 * @param aRecipeList                a List you specify as Recipe List. Usually just an ArrayList with a pre-initialised Size.
	 * @param aUnlocalizedName           the unlocalised Name of this Recipe Handler, used mainly for NEI.
	 * @param aLocalName                 the displayed Name inside the NEI Recipe GUI.
	 * @param aNEIGUIPath                the displayed GUI Texture, usually just a Machine GUI. Auto-Attaches ".png" if forgotten.
	 * @param aUsualInputCount           the usual amount of Input Slots this Recipe Class has.
	 * @param aUsualOutputCount          the usual amount of Output Slots this Recipe Class has.
	 * @param aNEISpecialValuePre        the String in front of the Special Value in NEI.
	 * @param aNEISpecialValueMultiplier the Value the Special Value is getting Multiplied with before displaying
	 * @param aNEISpecialValuePost       the String after the Special Value. Usually for a Unit or something.
	 * @param aNEIAllowed                if NEI is allowed to display this Recipe Handler in general.
	 */
	public CustomRecipeMap(final Collection<GT_Recipe> aRecipeList, final String aUnlocalizedName, final String aLocalName, final String aNEIName, final String aNEIGUIPath, final int aUsualInputCount, final int aUsualOutputCount, final int aMinimalInputItems, final int aMinimalInputFluids, final int aAmperage, final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier, final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI, final boolean aNEIAllowed) {
		//super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		sMappings.add(this);
		this.mNEIAllowed = aNEIAllowed;
		this.mShowVoltageAmperageInNEI = aShowVoltageAmperageInNEI;
		this.mRecipeList = aRecipeList;
		this.mNEIName = aNEIName == null ? aUnlocalizedName : aNEIName;
		this.mNEIGUIPath = aNEIGUIPath.endsWith(".png") ? aNEIGUIPath : aNEIGUIPath + ".png";
		this.mNEISpecialValuePre = aNEISpecialValuePre;
		this.mNEISpecialValueMultiplier = aNEISpecialValueMultiplier;
		this.mNEISpecialValuePost = aNEISpecialValuePost;
		this.mAmperage = aAmperage;
		this.mUsualInputCount = aUsualInputCount;
		this.mUsualOutputCount = aUsualOutputCount;
		this.mMinimalInputItems = aMinimalInputItems;
		this.mMinimalInputFluids = aMinimalInputFluids;
		GregTech_API.sFluidMappings.add(this.mRecipeFluidMap);
		GregTech_API.sItemStackMappings.add(this.mRecipeItemMap);
		GT_LanguageManager.addStringLocalization(this.mUnlocalizedName = aUnlocalizedName, aLocalName);
	}

	public GT_Recipe addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		return this.addRecipe(new GT_Recipe(aOptimize, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
	}

	public GT_Recipe addRecipe(final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		return this.addRecipe(new GT_Recipe(false, null, null, null, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue), false, false, false);
	}

	public GT_Recipe addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		return this.addRecipe(new GT_Recipe(aOptimize, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
	}

	public GT_Recipe addRecipe(final GT_Recipe aRecipe) {
		return this.addRecipe(aRecipe, true, false, false);
	}

	protected GT_Recipe addRecipe(final GT_Recipe aRecipe, final boolean aCheckForCollisions, final boolean aFakeRecipe, final boolean aHidden) {
		aRecipe.mHidden = aHidden;
		aRecipe.mFakeRecipe = aFakeRecipe;
		if ((aRecipe.mFluidInputs.length < this.mMinimalInputFluids) && (aRecipe.mInputs.length < this.mMinimalInputItems)) {
			return null;
		}
		if (aCheckForCollisions && (this.findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)) {
			return null;
		}
		return this.add(aRecipe);
	}

	/**
	 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
	 */
	public GT_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		return this.addFakeRecipe(aCheckForCollisions, new GT_Recipe(false, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
	}

	/**
	 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
	 */
	public GT_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		return this.addFakeRecipe(aCheckForCollisions, new GT_Recipe(false, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
	}

	/**
	 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
	 */
	public GT_Recipe addFakeRecipe(final boolean aCheckForCollisions, final GT_Recipe aRecipe) {
		return this.addRecipe(aRecipe, aCheckForCollisions, true, false);
	}

	public GT_Recipe add(final GT_Recipe aRecipe) {
		this.mRecipeList.add(aRecipe);
		for (final FluidStack aFluid : aRecipe.mFluidInputs) {
			if (aFluid != null) {
				Collection<GT_Recipe> tList = this.mRecipeFluidMap.get(aFluid.getFluid());
				if (tList == null) {
					this.mRecipeFluidMap.put(aFluid.getFluid(), tList = new HashSet<>(1));
				}
				tList.add(aRecipe);
			}
		}
		return this.addToItemMap(aRecipe);
	}

	public void reInit() {
		final Map<GT_ItemStack, Collection<GT_Recipe>> tMap = this.mRecipeItemMap;
		if (tMap != null) {
			tMap.clear();
		}
		for (final GT_Recipe tRecipe : this.mRecipeList) {
			GT_OreDictUnificator.setStackArray(true, tRecipe.mInputs);
			GT_OreDictUnificator.setStackArray(true, tRecipe.mOutputs);
			if (tMap != null) {
				this.addToItemMap(tRecipe);
			}
		}
	}

	/**
	 * @return if this Item is a valid Input for any for the Recipes
	 */
	public boolean containsInput(final ItemStack aStack) {
		return (aStack != null) && (this.mRecipeItemMap.containsKey(new GT_ItemStack(aStack)) || this.mRecipeItemMap.containsKey(new GT_ItemStack(GT_Utility.copyMetaData(W, aStack))));
	}

	/**
	 * @return if this Fluid is a valid Input for any for the Recipes
	 */
	public boolean containsInput(final FluidStack aFluid) {
		return (aFluid != null) && this.containsInput(aFluid.getFluid());
	}

	/**
	 * @return if this Fluid is a valid Input for any for the Recipes
	 */
	public boolean containsInput(final Fluid aFluid) {
		return (aFluid != null) && this.mRecipeFluidMap.containsKey(aFluid);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
	}

	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
		return this.findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, null, aInputs);
	}

	/**
	 * finds a Recipe matching the aFluid and ItemStack Inputs.
	 *
	 * @param aTileEntity    an Object representing the current coordinates of the executing Block/Entity/Whatever. This may be null, especially during Startup.
	 * @param aRecipe        in case this is != null it will try to use this Recipe first when looking things up.
	 * @param aNotUnificated if this is T the Recipe searcher will unificate the ItemStack Inputs
	 * @param aVoltage       Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
	 * @param aFluids        the Fluid Inputs
	 * @param aSpecialSlot   the content of the Special Slot, the regular Manager doesn't do anything with this, but some custom ones do.
	 * @param aInputs        the Item Inputs
	 * @return the Recipe it has found or null for no matching Recipe
	 */
	public GT_Recipe findRecipe(final IHasWorldObjectAndCoords aTileEntity, final GT_Recipe aRecipe, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack aSpecialSlot, ItemStack... aInputs) {
		// No Recipes? Well, nothing to be found then.
		if (this.mRecipeList.isEmpty()) {
			Utils.LOG_INFO("BAD RECIPE");
			return null;
		}

		// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
		// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
		if (GregTech_API.sPostloadFinished) {
			if (this.mMinimalInputFluids > 0) {
				if (aFluids == null) {
					Utils.LOG_INFO("BAD RECIPE [1]");
					return null;
				}
				int tAmount = 0;
				for (final FluidStack aFluid : aFluids) {
					if (aFluid != null) {
						tAmount++;
					}
				}
				if (tAmount < this.mMinimalInputFluids) {
					Utils.LOG_INFO("BAD RECIPE [2]");
					return null;
				}
			}
			if (this.mMinimalInputItems > 0) {
				if (aInputs == null) {
					Utils.LOG_INFO("BAD RECIPE [3]");
					return null;
				}
				int tAmount = 0;
				for (final ItemStack aInput : aInputs) {
					if (aInput != null) {
						tAmount++;
					}
				}
				if (tAmount < this.mMinimalInputItems) {
					Utils.LOG_INFO("BAD RECIPE [4]");
					return null;
				}
			}
		}

		// Unification happens here in case the Input isn't already unificated.
		if (aNotUnificated) {
			aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);
		}

		// Check the Recipe which has been used last time in order to not have to search for it again, if possible.
		if (aRecipe != null) {
			if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
				return aRecipe.mEnabled && ((aVoltage * this.mAmperage) >= aRecipe.mEUt) ? aRecipe : null;
			}
		}

		// Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
		if ((this.mUsualInputCount > 0) && (aInputs != null)) {
			for (final ItemStack tStack : aInputs) {
				if (tStack != null) {
					Collection<GT_Recipe>
					tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(tStack));
					if (tRecipes != null) {
						for (final GT_Recipe tRecipe : tRecipes) {
							if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
								return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
							}
						}
					}
					tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
					if (tRecipes != null) {
						for (final GT_Recipe tRecipe : tRecipes) {
							if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
								return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
							}
						}
					}
				}
			}
		}

		// If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
		if ((this.mMinimalInputItems == 0) && (aFluids != null)) {
			for (final FluidStack aFluid : aFluids) {
				if (aFluid != null) {
					final Collection<GT_Recipe>
					tRecipes = this.mRecipeFluidMap.get(aFluid.getFluid());
					if (tRecipes != null) {
						for (final GT_Recipe tRecipe : tRecipes) {
							if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
								return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
							}
						}
					}
				}
			}
		}

		// And nothing has been found.
		Utils.LOG_INFO("BAD RECIPE [5]");
		return null;
	}

	protected GT_Recipe addToItemMap(final GT_Recipe aRecipe) {
		for (final ItemStack aStack : aRecipe.mInputs) {
			if (aStack != null) {
				final GT_ItemStack tStack = new GT_ItemStack(aStack);
				Collection<GT_Recipe> tList = this.mRecipeItemMap.get(tStack);
				if (tList == null) {
					this.mRecipeItemMap.put(tStack, tList = new HashSet<>(1));
				}
				tList.add(aRecipe);
			}
		}
		return aRecipe;
	}
}