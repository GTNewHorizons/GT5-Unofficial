package gregtech.api.util;

import static gregtech.api.enums.GT_Values.*;

import java.util.*;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This File contains the functions used for Recipes. Please do not include this File AT ALL in your Moddownload as it ruins compatibility
 * This is just the Core of my Recipe System, if you just want to GET the Recipes I add, then you can access this File.
 * Do NOT add Recipes using the Constructors inside this Class, The GregTech_API File calls the correct Functions for these Constructors.
 * <p/>
 * I know this File causes some Errors, because of missing Main Functions, but if you just need to compile Stuff, then remove said erroreous Functions.
 */
public class Recipe_GT extends GT_Recipe{

	public static volatile int VERSION = 508;

	protected Recipe_GT(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecialItems, final int[] aChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
		Utils.LOG_SPECIFIC_WARNING(this.getClass().getName()+" | [GregtechRecipe]", "Created new recipe instance for "+ItemUtils.getArrayStackNames(aInputs), 167);
	}

	public Recipe_GT(final ItemStack aInput1, final ItemStack aOutput1, final int aFuelValue, final int aType) {
		this(aInput1, aOutput1, null, null, null, aFuelValue, aType);
	}

	// aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
	public Recipe_GT(final ItemStack aInput1, final ItemStack aOutput1, final ItemStack aOutput2, final ItemStack aOutput3, final ItemStack aOutput4, final int aSpecialValue, final int aType) {
		this(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4}, null, null, null, null, 0, 0, Math.max(1, aSpecialValue));

		Utils.LOG_INFO("Switch case method for adding fuels");
		if ((this.mInputs.length > 0) && (aSpecialValue > 0)) {
			switch (aType) {
			// Diesel Generator
			case 0:
				Utils.LOG_INFO("Added fuel "+aInput1.getDisplayName()+" is ROCKET FUEL - continuing");
				Gregtech_Recipe_Map.sRocketFuels.addRecipe(this);
				break;
				// Gas Turbine
			case 1:
				Gregtech_Recipe_Map.sGeoThermalFuels.addRecipe(this);
				break;
				// Thermal Generator
			case 2:
				//Gregtech_Recipe_Map.sHotFuels.addRecipe(this);
				break;
				// Plasma Generator
			case 4:
				//Gregtech_Recipe_Map.sPlasmaFuels.addRecipe(this);
				break;
				// Magic Generator
			case 5:
				//Gregtech_Recipe_Map.sMagicFuels.addRecipe(this);
				break;
				// Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
			default:
				//Gregtech_Recipe_Map.sDenseLiquidFuels.addRecipe(this);
				break;
			}
		}
	}

	//Custom Recipe Handlers
	public Recipe_GT(final ItemStack aInput, final FluidStack aFluid, final ItemStack[] aOutput, final int aDuration, final int aEUt) {
		this(true, new ItemStack[]{aInput}, aOutput.clone(), null, null, new FluidStack[]{aFluid}, null, aDuration, aEUt, 0);
		if ((this.mInputs.length > 0) && (this.mOutputs[0] != null)) {
			Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(this);
		}
	}

	public Recipe_GT(final FluidStack aInput1, final FluidStack aInput2, final FluidStack aOutput1, final int aDuration, final int aEUt, final int aSpecialValue) {
		this(true, null, null, null, null, new FluidStack[]{aInput1, aInput2}, new FluidStack[]{aOutput1}, Math.max(aDuration, 1), aEUt, Math.max(Math.min(aSpecialValue, 160000000), 0));
		if (this.mInputs.length > 1) {
			Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.addRecipe(this);
		}
	}

	public Recipe_GT(
			final FluidStack aInput1, final FluidStack aInput2, final FluidStack aInput3,
			final FluidStack aInput4, final FluidStack aInput5, final FluidStack aInput6,
			final FluidStack aInput7, final FluidStack aInput8, final FluidStack aInput9,
			final FluidStack aOutput1, final FluidStack aOutput2,
			final int aDuration, final int aEUt) {
		this(true, null, null, null, null, new FluidStack[]{aInput1, aInput2, aInput3, aInput4, aInput5, aInput6, aInput7, aInput8, aInput9}, new FluidStack[]{aOutput1, aOutput2}, Math.max(aDuration, 1), aEUt, 0);
		if (this.mInputs.length > 1) {
			CustomRecipeMap.sFissionFuelProcessing.addRecipe(this);
		}
	}

	/*public GregtechRecipe(ItemStack aInput, FluidStack aFluid, ItemStack[] aOutput, int aDuration, int aEUt) {
        this(true, new ItemStack[]{aInput}, aOutput.clone(), null, null, new FluidStack[]{aFluid}, null, aDuration, aEUt, 0);
        if (mInputs.length > 0 && mOutputs[0] != null) {
            Gregtech_Recipe_Map.sChemicalDehydratorRecipes.addRecipe(this);
        }
    }*/


	public static void reInit() {
		GT_Log.out.println("GT_Mod: Re-Unificating Recipes.");
		for (final Gregtech_Recipe_Map tMapEntry : Gregtech_Recipe_Map.sMappings) {
			tMapEntry.reInit();
		}
	}

	@Override
	public ItemStack getRepresentativeInput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mInputs.length)) {
			return null;
		}
		return GT_Utility.copy(this.mInputs[aIndex]);
	}

	@Override
	public ItemStack getOutput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mOutputs.length)) {
			return null;
		}
		return GT_Utility.copy(this.mOutputs[aIndex]);
	}

	@Override
	public int getOutputChance(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mChances.length)) {
			return 10000;
		}
		return this.mChances[aIndex];
	}

	@Override
	public FluidStack getRepresentativeFluidInput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mFluidInputs.length) || (this.mFluidInputs[aIndex] == null)) {
			return null;
		}
		return this.mFluidInputs[aIndex].copy();
	}

	@Override
	public FluidStack getFluidOutput(final int aIndex) {
		if ((aIndex < 0) || (aIndex >= this.mFluidOutputs.length) || (this.mFluidOutputs[aIndex] == null)) {
			return null;
		}
		return this.mFluidOutputs[aIndex].copy();
	}

	@Override
	public boolean isRecipeInputEqual(final boolean aDecreaseStacksizeBySuccess, final FluidStack[] aFluidInputs, final ItemStack... aInputs) {
		return this.isRecipeInputEqual(aDecreaseStacksizeBySuccess, false, aFluidInputs, aInputs);
	}

	@Override
	public boolean isRecipeInputEqual(final boolean aDecreaseStacksizeBySuccess, final boolean aDontCheckStackSizes, final FluidStack[] aFluidInputs, final ItemStack... aInputs) {
		if ((this.mFluidInputs.length > 0) && (aFluidInputs == null)) {
			return false;
		}
		for (final FluidStack tFluid : this.mFluidInputs) {
			if (tFluid != null) {
				boolean temp = true;
				for (final FluidStack aFluid : aFluidInputs) {
					if ((aFluid != null) && aFluid.isFluidEqual(tFluid) && (aDontCheckStackSizes || (aFluid.amount >= tFluid.amount))) {
						temp = false;
						break;
					}
				}
				if (temp) {
					return false;
				}
			}
		}

		if ((this.mInputs.length > 0) && (aInputs == null)) {
			return false;
		}

		for (final ItemStack tStack : this.mInputs) {
			if (tStack != null) {
				boolean temp = true;
				for (final ItemStack aStack : aInputs) {
					if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) && (aDontCheckStackSizes || (aStack.stackSize >= tStack.stackSize))) {
						temp = false;
						break;
					}
				}
				if (temp) {
					return false;
				}
			}
		}

		if (aDecreaseStacksizeBySuccess) {
			if (aFluidInputs != null) {
				for (final FluidStack tFluid : this.mFluidInputs) {
					if (tFluid != null) {
						for (final FluidStack aFluid : aFluidInputs) {
							if ((aFluid != null) && aFluid.isFluidEqual(tFluid) && (aDontCheckStackSizes || (aFluid.amount >= tFluid.amount))) {
								aFluid.amount -= tFluid.amount;
								break;
							}
						}
					}
				}
			}

			if (aInputs != null) {
				for (final ItemStack tStack : this.mInputs) {
					if (tStack != null) {
						for (final ItemStack aStack : aInputs) {
							if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) && (aDontCheckStackSizes || (aStack.stackSize >= tStack.stackSize))) {
								aStack.stackSize -= tStack.stackSize;
								break;
							}
						}
					}
				}
			}
		}

		return true;
	}

	public static class Gregtech_Recipe_Map {
		/**
		 * Contains all Recipe Maps
		 */
		public static final Collection<Gregtech_Recipe_Map> sMappings = new ArrayList<>();
		//public static final GT_Recipe_Map sChemicalBathRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.chemicalbath", "Chemical Bath", null, RES_PATH_GUI + "basicmachines/ChemicalBath", 1, 3, 1, 1, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sCokeOvenRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.cokeoven", "Coke Oven", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 2, 1, 0, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sMatterFab2Recipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.matterfab2", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, E, 1, E, true, true);
		//public static final Gregtech_Recipe_Map sMatterFabRecipes = new Gregtech_Recipe_Map(new HashSet<GregtechRecipe>(200), "gt.recipe.matterfab", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Massfabricator", 1, 3, 1, 1, 1, E, 1, E, true, true);
		public static final Gregtech_Recipe_Map_Fuel sRocketFuels = new Gregtech_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gt.recipe.rocketenginefuel", "Rocket Engine Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 3000, " EU", true, true);
		public static final Gregtech_Recipe_Map_Fuel sGeoThermalFuels = new Gregtech_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gt.recipe.geothermalfuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);
		public static final GT_Recipe_Map sChemicalDehydratorRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.chemicaldehydrator", "Chemical Dehydrator", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sAlloyBlastSmelterRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.alloyblastsmelter", "Alloy Blast Smelter", null, RES_PATH_GUI + "basicmachines/BlastSmelter", 9, 1, 1, 0, 1, E, 1, E, true, true);

		//LFTR recipes
		public static final GT_Recipe_Map sLiquidFluorineThoriumReactorRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(50), "gt.recipe.lftr", "Liquid Fluoride Thorium Reactor", null, RES_PATH_GUI + "basicmachines/LFTR", 0, 0, 0, 2, 1, "Start: ", 1, " EU", true, true);
		//Fission Fuel Plant Recipes
		//public static final GT_Recipe_Map sFissionFuelProcessing = new GT_Recipe_Map(new HashSet<GT_Recipe>(50), "gt.recipe.fissionfuel", "Fission Fuel Processing", null, RES_PATH_GUI + "basicmachines/LFTR", 0, 0, 0, 9, 1, E, 1, E, true, true);

		//Basic Washer Map
		public static final GT_Recipe_Map sSimpleWasherRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(3), "gt.recipe.simplewasher", "Simple Dust Washer", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 1, 1, 0, 0, 1, E, 1, E, true, true);
        
		
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
		public Gregtech_Recipe_Map(final Collection<GT_Recipe> aRecipeList,
				final String aUnlocalizedName, final String aLocalName, final String aNEIName,
				final String aNEIGUIPath, final int aUsualInputCount,
				final int aUsualOutputCount, final int aMinimalInputItems,
				final int aMinimalInputFluids, final int aAmperage,
				final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier,
				final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI,
				final boolean aNEIAllowed) {
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

		public GT_Recipe addRecipe(final boolean aOptimize, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new GT_Recipe(aOptimize, null, null, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/*public GregtechRecipe addRecipe(boolean aOptimize, FluidStack aInput1, FluidStack aOutput1, ItemStack[] bInput1, ItemStack[] bOutput1, int aDuration, int aEUt, int aSpecialValue) {
			 return addRecipe(new GregtechRecipe(aOptimize, aInput1, aOutput1, bInput1,bOutput1, aDuration, aEUt, aSpecialValue));

			}*/

		public GT_Recipe addRecipe(final GT_Recipe aRecipe) {
			Utils.LOG_INFO("Adding Recipe Method 1");
			return this.addRecipe(aRecipe, true, false, false);
		}

		protected GT_Recipe addRecipe(final GT_Recipe aRecipe, final boolean aCheckForCollisions, final boolean aFakeRecipe, final boolean aHidden) {
			Utils.LOG_INFO("Adding Recipe Method 2 - This Checks if hidden, fake or if duplicate recipes exists, I think.");
			aRecipe.mHidden = aHidden;
			aRecipe.mFakeRecipe = aFakeRecipe;
			Utils.LOG_INFO("Logging some data about this method: GregtechRecipe["+aRecipe.toString()+"] | aCheckForCollisions["+aCheckForCollisions+"] | aFakeRecipe["+aFakeRecipe+"] | aHidden["+aHidden+"]");
			Utils.LOG_INFO("Logging some data about this method: mMinimalInputFluids["+this.mMinimalInputFluids+"] | mMinimalInputItems["+this.mMinimalInputItems+"] | aRecipe.mFluidInputs.length["+aRecipe.mFluidInputs.length+"] | aRecipe.mInputs.length["+aRecipe.mInputs.length+"]");
			if ((aRecipe.mFluidInputs.length < this.mMinimalInputFluids) && (aRecipe.mInputs.length < this.mMinimalInputItems)){
				Utils.LOG_INFO("Step 2 failed");
				return null;}

			Utils.LOG_INFO("Logging some data about this method: aCheckForCollisions["+aCheckForCollisions+"] | findRecipe != null ["+(this.findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)+"]");
			if (aCheckForCollisions && (this.findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)){
				Utils.LOG_INFO("Step 2 failed - 2");
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
			Utils.LOG_INFO("Adding Recipe Method 3");
			this.mRecipeList.add(aRecipe);
			for (final FluidStack aFluid : aRecipe.mFluidInputs) {
				if (aFluid != null) {
					Utils.LOG_INFO("Fluid is valid - getting some kind of fluid instance to add to the recipe hashmap.");
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
				return null;
			}

			// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
			// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
			if (GregTech_API.sPostloadFinished) {
				if (this.mMinimalInputFluids > 0) {
					if (aFluids == null) {
						return null;
					}
					int tAmount = 0;
					for (final FluidStack aFluid : aFluids) {
						if (aFluid != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputFluids) {
						return null;
					}
				}
				if (this.mMinimalInputItems > 0) {
					if (aInputs == null) {
						return null;
					}
					int tAmount = 0;
					for (final ItemStack aInput : aInputs) {
						if (aInput != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputItems) {
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
			return null;
		}

		protected GT_Recipe addToItemMap(final GT_Recipe aRecipe) {
			Utils.LOG_INFO("Adding Recipe Method 4");
			for (final ItemStack aStack : aRecipe.mInputs) {
				if (aStack != null) {
					Utils.LOG_INFO("Method 4 - Manipulating "+aStack.getDisplayName());
					final GT_ItemStack tStack = new GT_ItemStack(aStack);
					Utils.LOG_INFO("Method 4 - Made gt stack of item "+tStack.toStack().getDisplayName());
					Collection<GT_Recipe> tList = this.mRecipeItemMap.get(tStack);
					if (tList != null){
						Utils.LOG_INFO("Method 4 - Gt Recipe Hashmap: "+tList.toString());
					}
					if (tList == null){
						Utils.LOG_INFO("Method 4 - brrr list was NUll");
						this.mRecipeItemMap.put(tStack, tList = new HashSet<>(1));
						Utils.LOG_INFO("Method 4 - Attemping backup method for Gt Recipe Hashmap:");

						while (tList.iterator().hasNext()){
							Utils.LOG_INFO(tList.iterator().next().toString());
						}

					}
					tList.add(aRecipe);
					Utils.LOG_INFO("Method 4 - Added recipe to map? I think.");
				}
			}
			return aRecipe;
		}

		public GT_Recipe findRecipe(final IGregTechTileEntity baseMetaTileEntity, final GT_Recipe aRecipe, final boolean aNotUnificated,
				final long aVoltage, final FluidStack[] aFluids, final FluidStack[] fluidStacks) {

			ItemStack aInputs[] = null;
			// No Recipes? Well, nothing to be found then.
			if (this.mRecipeList.isEmpty()) {
				return null;
			}

			// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
			// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
			if (GregTech_API.sPostloadFinished) {
				if (this.mMinimalInputFluids > 0) {
					if (aFluids == null) {
						return null;
					}
					int tAmount = 0;
					for (final FluidStack aFluid : aFluids) {
						if (aFluid != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputFluids) {
						return null;
					}
				}
				if (this.mMinimalInputItems > 0) {
					if (aInputs == null) {
						return null;
					}
					int tAmount = 0;
					for (final ItemStack aInput : aInputs) {
						if (aInput != null) {
							tAmount++;
						}
					}
					if (tAmount < this.mMinimalInputItems) {
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
			return null;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Here are a few Classes I use for Special Cases in some Machines without having to write a separate Machine Class.
	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * Abstract Class for general Recipe Handling of non GT Recipes
	 */
	public static abstract class GT_Recipe_Map_NonGTRecipes extends Gregtech_Recipe_Map {
		public GT_Recipe_Map_NonGTRecipes(final Collection<GT_Recipe> aRecipeList, final String aUnlocalizedName, final String aLocalName, final String aNEIName, final String aNEIGUIPath, final int aUsualInputCount, final int aUsualOutputCount, final int aMinimalInputItems, final int aMinimalInputFluids, final int aAmperage, final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier, final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI, final boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		@Override
		public boolean containsInput(final ItemStack aStack) {
			return false;
		}

		@Override
		public boolean containsInput(final FluidStack aFluid) {
			return false;
		}

		@Override
		public boolean containsInput(final Fluid aFluid) {
			return false;
		}

		@Override
		public GT_Recipe addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public Recipe_GT addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public GT_Recipe addRecipe(final GT_Recipe aRecipe) {
			return null;
		}

		@Override
		public GT_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public GT_Recipe addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public GT_Recipe addFakeRecipe(final boolean aCheckForCollisions, final GT_Recipe aRecipe) {
			return null;
		}

		@Override
		public GT_Recipe add(final GT_Recipe aRecipe) {
			return null;
		}

		@Override
		public void reInit() {/**/}

		@Override
		protected GT_Recipe addToItemMap(final GT_Recipe aRecipe) {
			return null;
		}
	}

	/**
	 * Just a Recipe Map with Utility specifically for Fuels.
	 */
	public static class Gregtech_Recipe_Map_Fuel extends Gregtech_Recipe_Map {
		public Gregtech_Recipe_Map_Fuel(final Collection<GT_Recipe> aRecipeList, final String aUnlocalizedName, final String aLocalName, final String aNEIName, final String aNEIGUIPath, final int aUsualInputCount, final int aUsualOutputCount, final int aMinimalInputItems, final int aMinimalInputFluids, final int aAmperage, final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier, final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI, final boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		public GT_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 1");
			return this.addFuel(aInput, aOutput, null, null, 10000, aFuelValueInEU);
		}

		public GT_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final int aChance, final int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 2");
			return this.addFuel(aInput, aOutput, null, null, aChance, aFuelValueInEU);
		}

		public GT_Recipe addFuel(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 3");
			return this.addFuel(null, null, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public GT_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 4");
			return this.addFuel(aInput, aOutput, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public GT_Recipe addFuel(final ItemStack aInput, final ItemStack aOutput, final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aChance, final int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 5");
			return this.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, new int[]{aChance}, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, 0, 0, aFuelValueInEU);
		}
	}

}