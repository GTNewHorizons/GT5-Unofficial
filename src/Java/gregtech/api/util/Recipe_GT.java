package gregtech.api.util;

import static gregtech.api.enums.GT_Values.*;

import java.util.*;

import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.ItemData;
import codechicken.nei.PositionedStack;
import gtPlusPlus.api.interfaces.IComparableRecipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.nei.GT_NEI_MultiBlockHandler;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Custom GT Recipe Class
 * @author Alkalus
 *
 */
public class Recipe_GT extends GT_Recipe  implements IComparableRecipe{

	public Recipe_GT(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecialItems, final int[] aChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
		//Logger.SPECIFIC_WARNING(this.getClass().getName()+" | [GregtechRecipe]", "Created new recipe instance for "+ItemUtils.getArrayStackNames(aInputs), 167);
	}

	public Recipe_GT(final ItemStack aInput1, final ItemStack aOutput1, final int aFuelValue, final int aType) {
		this(aInput1, aOutput1, null, null, null, aFuelValue, aType);
	}

	// aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
	public Recipe_GT(final ItemStack aInput1, final ItemStack aOutput1, final ItemStack aOutput2, final ItemStack aOutput3, final ItemStack aOutput4, final int aSpecialValue, final int aType) {
		this(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4}, null, null, null, null, 0, 0, Math.max(1, aSpecialValue));

		Logger.WARNING("Switch case method for adding fuels");
		if ((this.mInputs.length > 0) && (aSpecialValue > 0)) {
			switch (aType) {
				// Diesel Generator
				case 0:
					Logger.WARNING("Added fuel "+aInput1.getDisplayName()+" is ROCKET FUEL - continuing");
					Gregtech_Recipe_Map.sRocketFuels.addRecipe(this);
					break;
					// Gas Turbine
				case 1:
					Gregtech_Recipe_Map.sGeoThermalFuels.addRecipe(this);
					break;
					// Thermal Generator
				case 2:
					Gregtech_Recipe_Map.sRTGFuels.addRecipe(this);
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
		public static final Gregtech_Recipe_Map_Fuel sRocketFuels = new Gregtech_Recipe_Map_Fuel(new HashSet<Recipe_GT>(10), "gt.recipe.rocketenginefuel", "Rocket Engine Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 3000, " EU", true, true);
		public static final GT_Recipe_Map sGeoThermalFuels = new GT_Recipe_Map(new HashSet<GT_Recipe>(10), "gt.recipe.geothermalfuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);
		public static final GT_Recipe_Map sChemicalDehydratorRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.chemicaldehydrator", "Chemical Dehydrator", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sAlloyBlastSmelterRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.alloyblastsmelter", "Alloy Blast Smelter", null, RES_PATH_GUI + "basicmachines/BlastSmelter", 9, 1, 1, 0, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sSteamTurbineFuels = new GT_Recipe_Map(new HashSet<GT_Recipe>(10), "gt.recipe.geothermalfuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);

		//LFTR recipes
		public static final GT_Recipe_Map sLiquidFluorineThoriumReactorRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(50), "gt.recipe.lftr", "Liquid Fluoride Thorium Reactor", null, RES_PATH_GUI + "basicmachines/LFTR", 0, 0, 0, 2, 1, "Start: ", 1, " EU", true, true);
		//Fission Fuel Plant Recipes
		//public static final GT_Recipe_Map sFissionFuelProcessing = new GT_Recipe_Map(new HashSet<GT_Recipe>(50), "gt.recipe.fissionfuel", "Fission Fuel Processing", null, RES_PATH_GUI + "basicmachines/LFTR", 0, 0, 0, 9, 1, E, 1, E, true, true);

		//Basic Washer Map
		public static final GT_Recipe_Map sSimpleWasherRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(3), "gt.recipe.simplewasher", "Simple Dust Washer", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 1, 1, 0, 0, 1, E, 1, E, true, true);


		//RTG Fuel Map
		public static final GT_Recipe.GT_Recipe_Map_Fuel sRTGFuels = new Recipe_GT.GT_Recipe_Map_Fuel(
				new HashSet<GT_Recipe>(10), "gt.recipe.RTGgenerators", "RTG", null,
				"gregtech:textures/gui/basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 365, " Minecraft Days", true, true);

		//Thermal Boiler map
		public static final GT_Recipe.GT_Recipe_Map_Fuel sThermalFuels = new GT_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gt.recipe.thermalgeneratorfuel",
				"Thermal Generator Fuel", null, "gregtech:textures/gui/basicmachines/Default", 1, 1, 0, 0, 1,
				"Fuel Value: ", 1000, " EU", true, false);

		//Cyclotron recipe map
		public static final GT_Recipe_Map sCyclotronRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.cyclotron", "COMET - Compact Cyclotron", null, RES_PATH_GUI + "basicmachines/BlastSmelter", 1, 1, 1, 0, 1, E, 1, E, true, true);

		//Advanced Mixer
		public static final GT_Recipe_Map sAdvancedMixerRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(1000), "gt.recipe.advanced.mixer",
				"Advanced Material Combiner", null, "gregtech:textures/gui/basicmachines/MixerAdvanced", 4, 4, 1, 0, 2, "", 1, "", true, true);


		//Mini Fusion
		public static final GT_Recipe_Map sSlowFusionRecipes = new GT_Recipe_Map(new HashSet(50), "gt.recipe.slowfusionreactor",
				"Slow Fusion Reactor", null, "gregtech:textures/gui/basicmachines/Default", 0, 0, 0, 2, 1, "Start: ", 1,
				" EU", true, false);

		public static final GT_Recipe_Map sSlowFusion2Recipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(50), "gt.recipe.slowfusionreactor2",
				"Pocket Fusion", null, "gregtech:textures/gui/basicmachines/LFTR", 2, 0, 0, 0, 4, "Start: ", 1,
				" EU", true, false);


		//Component Assembler
		public static final GT_Recipe_Map sComponentAssemblerRecipes = new GT_Recipe_Map_Assembler(new HashSet<GT_Recipe>(300), "gt.recipe.componentassembler", "Component Assembler", null, RES_PATH_GUI + "basicmachines/Assembler", 6, 1, 1, 0, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sFishPondRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(3), "gt.recipe.fishpond", "Zhuhai - Fishing Port", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 0, 1, 0, 0, 1, "Requires Circuit: ", 1, ".", true, true);
		public static final Gregtech_Recipe_Map sMultiblockCentrifugeRecipes = new GT_Recipe_Map_LargeCentrifuge();
		public static final Gregtech_Recipe_Map sMultiblockElectrolyzerRecipes = new GT_Recipe_Map_LargeElectrolyzer();
		public static final Gregtech_Recipe_Map sAdvFreezerRecipes = new GT_Recipe_Map_AdvancedVacuumFreezer();
		public static final GT_Recipe_Map sAdvFreezerRecipes_GT = new GT_Recipe_Map(new HashSet<GT_Recipe>(2000), "gt.recipe.temp", "temp", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 0, 0, 0, 0, 0, "", 0, "", false, false);


		/**
		 * HashMap of Recipes based on their Items
		 */
		public final Map<GT_ItemStack, Collection<Recipe_GT>> mRecipeItemMap = new HashMap<>();
		/**
		 * HashMap of Recipes based on their Fluids
		 */
		public final Map<Fluid, Collection<Recipe_GT>> mRecipeFluidMap = new HashMap<>();
		/**
		 * The List of all Recipes
		 */
		public final Collection<Recipe_GT> mRecipeList;
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
		public Gregtech_Recipe_Map(final Collection<Recipe_GT> aRecipeList,
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

		public Recipe_GT addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new Recipe_GT(aOptimize, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		public Recipe_GT addRecipe(final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new Recipe_GT(false, null, null, null, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue), false, false, false);
		}

		public Recipe_GT addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new Recipe_GT(aOptimize, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		public Recipe_GT addRecipe(final boolean aOptimize, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addRecipe(new Recipe_GT(aOptimize, null, null, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/*public GregtechRecipe addRecipe(boolean aOptimize, FluidStack aInput1, FluidStack aOutput1, ItemStack[] bInput1, ItemStack[] bOutput1, int aDuration, int aEUt, int aSpecialValue) {
			 return addRecipe(new GregtechRecipe(aOptimize, aInput1, aOutput1, bInput1,bOutput1, aDuration, aEUt, aSpecialValue));

			}*/

		public Recipe_GT addRecipe(final Recipe_GT aRecipe) {
			Logger.WARNING("Adding Recipe Method 1");
			return this.addRecipe(aRecipe, true, false, false);
		}

		protected Recipe_GT addRecipe(final Recipe_GT aRecipe, final boolean aCheckForCollisions, final boolean aFakeRecipe, final boolean aHidden) {
			Logger.WARNING("Adding Recipe Method 2 - This Checks if hidden, fake or if duplicate recipes exists, I think.");
			aRecipe.mHidden = aHidden;
			aRecipe.mFakeRecipe = aFakeRecipe;
			Logger.WARNING("Logging some data about this method: GregtechRecipe["+aRecipe.toString()+"] | aCheckForCollisions["+aCheckForCollisions+"] | aFakeRecipe["+aFakeRecipe+"] | aHidden["+aHidden+"]");
			Logger.WARNING("Logging some data about this method: mMinimalInputFluids["+this.mMinimalInputFluids+"] | mMinimalInputItems["+this.mMinimalInputItems+"] | aRecipe.mFluidInputs.length["+aRecipe.mFluidInputs.length+"] | aRecipe.mInputs.length["+aRecipe.mInputs.length+"]");
			if ((aRecipe.mFluidInputs.length < this.mMinimalInputFluids) && (aRecipe.mInputs.length < this.mMinimalInputItems)){
				Logger.WARNING("Step 2 failed");
				return null;}

			Logger.WARNING("Logging some data about this method: aCheckForCollisions["+aCheckForCollisions+"] | findRecipe != null ["+(this.findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)+"]");
			if (aCheckForCollisions && (this.findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)){
				Logger.WARNING("Step 2 failed - 2");
				return null;
			}
			return this.add(aRecipe);
		}



		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public Recipe_GT addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addFakeRecipe(aCheckForCollisions, new Recipe_GT(false, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public Recipe_GT addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return this.addFakeRecipe(aCheckForCollisions, new Recipe_GT(false, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public Recipe_GT addFakeRecipe(final boolean aCheckForCollisions, final Recipe_GT aRecipe) {
			return this.addRecipe(aRecipe, aCheckForCollisions, true, false);
		}

		public Recipe_GT add(final Recipe_GT aRecipe) {
			Logger.WARNING("Adding Recipe Method 3");
			this.mRecipeList.add(aRecipe);
			for (final FluidStack aFluid : aRecipe.mFluidInputs) {
				if (aFluid != null) {
					Logger.WARNING("Fluid is valid - getting some kind of fluid instance to add to the recipe hashmap.");
					Collection<Recipe_GT> tList = this.mRecipeFluidMap.get(aFluid.getFluid());
					if (tList == null) {
						this.mRecipeFluidMap.put(aFluid.getFluid(), tList = new HashSet<>(1));
					}
					tList.add(aRecipe);
				}
			}
			return this.addToItemMap(aRecipe);
		}

		public void reInit() {
			final Map<GT_ItemStack, Collection<Recipe_GT>> tMap = this.mRecipeItemMap;
			if (tMap != null) {
				tMap.clear();
			}
			for (final Recipe_GT tRecipe : this.mRecipeList) {
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

		public Recipe_GT findRecipe(final IHasWorldObjectAndCoords aTileEntity, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
			return this.findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
		}

		public Recipe_GT findRecipe(final IHasWorldObjectAndCoords aTileEntity, final Recipe_GT aRecipe, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack... aInputs) {
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
		public Recipe_GT findRecipe(final IHasWorldObjectAndCoords aTileEntity, final Recipe_GT aRecipe, final boolean aNotUnificated, final long aVoltage, final FluidStack[] aFluids, final ItemStack aSpecialSlot, ItemStack... aInputs) {
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
						Collection<Recipe_GT>
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(tStack));
						if (tRecipes != null) {
							for (final Recipe_GT tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
						if (tRecipes != null) {
							for (final Recipe_GT tRecipe : tRecipes) {
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
						final Collection<Recipe_GT>
						tRecipes = this.mRecipeFluidMap.get(aFluid.getFluid());
						if (tRecipes != null) {
							for (final Recipe_GT tRecipe : tRecipes) {
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

		protected Recipe_GT addToItemMap(final Recipe_GT aRecipe) {
			Logger.WARNING("Adding Recipe Method 4");
			for (final ItemStack aStack : aRecipe.mInputs) {
				if (aStack != null) {
					Logger.WARNING("Method 4 - Manipulating "+aStack.getDisplayName());
					final GT_ItemStack tStack = new GT_ItemStack(aStack);
					Logger.WARNING("Method 4 - Made gt stack of item "+tStack.toStack().getDisplayName());
					Collection<Recipe_GT> tList = this.mRecipeItemMap.get(tStack);
					if (tList != null){
						Logger.WARNING("Method 4 - Gt Recipe Hashmap: "+tList.toString());
					}
					if (tList == null){
						Logger.WARNING("Method 4 - brrr list was NUll");
						this.mRecipeItemMap.put(tStack, tList = new HashSet<>(1));
						Logger.WARNING("Method 4 - Attemping backup method for Gt Recipe Hashmap:");

						while (tList.iterator().hasNext()){
							Logger.WARNING(tList.iterator().next().toString());
						}

					}
					tList.add(aRecipe);
					Logger.WARNING("Method 4 - Added recipe to map? I think.");
				}
			}
			return aRecipe;
		}

		public Recipe_GT findRecipe(final IGregTechTileEntity baseMetaTileEntity, final Recipe_GT aRecipe, final boolean aNotUnificated,
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
						Collection<Recipe_GT>
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(tStack));
						if (tRecipes != null) {
							for (final Recipe_GT tRecipe : tRecipes) {
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs)) {
									return tRecipe.mEnabled && ((aVoltage * this.mAmperage) >= tRecipe.mEUt) ? tRecipe : null;
								}
							}
						}
						tRecipes = this.mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
						if (tRecipes != null) {
							for (final Recipe_GT tRecipe : tRecipes) {
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
						final Collection<Recipe_GT>
						tRecipes = this.mRecipeFluidMap.get(aFluid.getFluid());
						if (tRecipes != null) {
							for (final Recipe_GT tRecipe : tRecipes) {
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
		public GT_Recipe_Map_NonGTRecipes(final Collection<Recipe_GT> aRecipeList, final String aUnlocalizedName, final String aLocalName, final String aNEIName, final String aNEIGUIPath, final int aUsualInputCount, final int aUsualOutputCount, final int aMinimalInputItems, final int aMinimalInputFluids, final int aAmperage, final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier, final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI, final boolean aNEIAllowed) {
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
		public Recipe_GT addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public Recipe_GT addRecipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public Recipe_GT addRecipe(final Recipe_GT aRecipe) {
			return null;
		}

		@Override
		public Recipe_GT addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final int[] aOutputChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public Recipe_GT addFakeRecipe(final boolean aCheckForCollisions, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecial, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			return null;
		}

		@Override
		public Recipe_GT addFakeRecipe(final boolean aCheckForCollisions, final Recipe_GT aRecipe) {
			return null;
		}

		@Override
		public Recipe_GT add(final Recipe_GT aRecipe) {
			return null;
		}

		@Override
		public void reInit() {/**/}

		@Override
		protected Recipe_GT addToItemMap(final Recipe_GT aRecipe) {
			return null;
		}
	}

	/**
	 * Just a Recipe Map with Utility specifically for Fuels.
	 */
	public static class Gregtech_Recipe_Map_Fuel extends Gregtech_Recipe_Map {
		public Gregtech_Recipe_Map_Fuel(final Collection<Recipe_GT> aRecipeList, final String aUnlocalizedName, final String aLocalName, final String aNEIName, final String aNEIGUIPath, final int aUsualInputCount, final int aUsualOutputCount, final int aMinimalInputItems, final int aMinimalInputFluids, final int aAmperage, final String aNEISpecialValuePre, final int aNEISpecialValueMultiplier, final String aNEISpecialValuePost, final boolean aShowVoltageAmperageInNEI, final boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		public Recipe_GT addFuel(final ItemStack aInput, final ItemStack aOutput, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 1");
			return this.addFuel(aInput, aOutput, null, null, 10000, aFuelValueInEU);
		}

		public Recipe_GT addFuel(final ItemStack aInput, final ItemStack aOutput, final int aChance, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 2");
			return this.addFuel(aInput, aOutput, null, null, aChance, aFuelValueInEU);
		}

		public Recipe_GT addFuel(final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 3");
			return this.addFuel(null, null, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public Recipe_GT addFuel(final ItemStack aInput, final ItemStack aOutput, final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 4");
			return this.addFuel(aInput, aOutput, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public Recipe_GT addFuel(final ItemStack aInput, final ItemStack aOutput, final FluidStack aFluidInput, final FluidStack aFluidOutput, final int aChance, final int aFuelValueInEU) {
			Logger.WARNING("Adding Fuel using method 5");
			return this.addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, new int[]{aChance}, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, 0, 0, aFuelValueInEU);
		}
	}

	public static class GT_Recipe_Map_LargeCentrifuge extends Gregtech_Recipe_Map {
		private static int INPUT_COUNT;
		private static int OUTPUT_COUNT;
		private static int FLUID_INPUT_COUNT;
		private static int FLUID_OUTPUT_COUNT;

		public GT_Recipe_Map_LargeCentrifuge() {
			super(new HashSet<Recipe_GT>(2000), "gt.recipe.largecentrifuge", "Large Centrifuge", null,
					RES_PATH_GUI + "basicmachines/FissionFuel", GT_Recipe_Map_LargeCentrifuge.INPUT_COUNT,
					GT_Recipe_Map_LargeCentrifuge.OUTPUT_COUNT, 0, 0, 1, "", 1, "", true, true);
		}

		@Override
		public Recipe_GT addRecipe(final boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs,
				final Object aSpecial, final int[] aOutputChances, FluidStack[] aFluidInputs,
				FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			final ArrayList<ItemStack> adjustedInputs = new ArrayList<ItemStack>();
			final ArrayList<ItemStack> adjustedOutputs = new ArrayList<ItemStack>();
			final ArrayList<FluidStack> adjustedFluidInputs = new ArrayList<FluidStack>();
			final ArrayList<FluidStack> adjustedFluidOutputs = new ArrayList<FluidStack>();
			if (aInputs == null) {
				aInputs = new ItemStack[0];
			}
			for (final ItemStack input : aInputs) {
				FluidStack inputFluidContent = FluidContainerRegistry.getFluidForFilledItem(input);
				if (inputFluidContent != null) {
					final FluidStack fluidStack = inputFluidContent;
					fluidStack.amount *= input.stackSize;
					if (inputFluidContent.getFluid().getName().equals("ic2steam")) {
						inputFluidContent = GT_ModHandler.getSteam((long) inputFluidContent.amount);
					}
					adjustedFluidInputs.add(inputFluidContent);
				} else {
					final ItemData itemData = GT_OreDictUnificator.getItemData(input);
					if (itemData == null || !itemData.hasValidPrefixMaterialData()
							|| itemData.mMaterial.mMaterial != Materials.Empty) {
						if (itemData != null && itemData.hasValidPrefixMaterialData()
								&& itemData.mPrefix == OrePrefixes.cell) {
							//final ItemStack dustStack = itemData.mMaterial.mMaterial.getDust(input.stackSize);
							final ItemStack dustStack = ItemUtils.getGregtechOreStack(OrePrefixes.dust, itemData.mMaterial.mMaterial, input.stackSize);
							if (dustStack != null) {
								adjustedInputs.add(dustStack);
							} else {
								adjustedInputs.add(input);
							}
						} else {
							adjustedInputs.add(input);
						}
					}
				}
			}
			if (aFluidInputs == null) {
				aFluidInputs = new FluidStack[0];
			}
			for (final FluidStack fluidInput : aFluidInputs) {
				adjustedFluidInputs.add(fluidInput);
			}
			aInputs = adjustedInputs.toArray(new ItemStack[adjustedInputs.size()]);
			aFluidInputs = adjustedFluidInputs.toArray(new FluidStack[adjustedFluidInputs.size()]);
			if (aOutputs == null) {
				aOutputs = new ItemStack[0];
			}
			for (final ItemStack output : aOutputs) {
				FluidStack outputFluidContent = FluidContainerRegistry.getFluidForFilledItem(output);
				if (outputFluidContent != null) {
					final FluidStack fluidStack2 = outputFluidContent;
					fluidStack2.amount *= output.stackSize;
					if (outputFluidContent.getFluid().getName().equals("ic2steam")) {
						outputFluidContent = GT_ModHandler.getSteam((long) outputFluidContent.amount);
					}
					adjustedFluidOutputs.add(outputFluidContent);
				} else {
					final ItemData itemData = GT_OreDictUnificator.getItemData(output);
					if (itemData == null || !itemData.hasValidPrefixMaterialData()
							|| itemData.mMaterial.mMaterial != Materials.Empty) {
						adjustedOutputs.add(output);
					}
				}
			}
			if (aFluidOutputs == null) {
				aFluidOutputs = new FluidStack[0];
			}
			for (final FluidStack fluidOutput : aFluidOutputs) {
				adjustedFluidOutputs.add(fluidOutput);
			}
			aOutputs = adjustedOutputs.toArray(new ItemStack[adjustedOutputs.size()]);
			aFluidOutputs = adjustedFluidOutputs.toArray(new FluidStack[adjustedFluidOutputs.size()]);
			Recipe_GT mNew = new GT_Recipe_LargeCentrifuge(false, aInputs, aOutputs, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, 0);
			if (RecipeUtils.doesGregtechRecipeHaveEqualCells(mNew)) {
				return this.addRecipe(mNew);
			}
			else {
				return null;
			}	
		}

		static {
			GT_Recipe_Map_LargeCentrifuge.INPUT_COUNT = 2;
			GT_Recipe_Map_LargeCentrifuge.OUTPUT_COUNT = 2;
			GT_Recipe_Map_LargeCentrifuge.FLUID_INPUT_COUNT = 4;
			GT_Recipe_Map_LargeCentrifuge.FLUID_OUTPUT_COUNT = 4;
		}

		private static class GT_Recipe_LargeCentrifuge extends Recipe_GT {
			protected GT_Recipe_LargeCentrifuge(final boolean aOptimize, final ItemStack[] aInputs,
					final ItemStack[] aOutputs, final Object aSpecialItems, final int[] aChances,
					final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration,
					final int aEUt, final int aSpecialValue) {
				super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration,
						aEUt, aSpecialValue);
			}

			public ArrayList<PositionedStack> getInputPositionedStacks() {
				final int itemLimit = Math.min(this.mInputs.length, GT_Recipe_Map_LargeCentrifuge.INPUT_COUNT);
				final int fluidLimit = Math.min(this.mFluidInputs.length,
						GT_Recipe_Map_LargeCentrifuge.FLUID_INPUT_COUNT);
				final ArrayList<PositionedStack> inputStacks = new ArrayList<PositionedStack>(itemLimit + fluidLimit);
				for (int i = 0; i < itemLimit; ++i) {
					inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
							(Object) this.mInputs[i].copy(), 48 - i * 18, 5));
				}
				for (int i = 0; i < fluidLimit; ++i) {
					if (i < 3) {
						inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
								(Object) GT_Utility.getFluidDisplayStack(this.mFluidInputs[i], true), 48 - i * 18, 23));
					} else {
						inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
								(Object) GT_Utility.getFluidDisplayStack(this.mFluidInputs[i], true), 12, 5));
					}
				}
				return inputStacks;
			}

			public ArrayList<PositionedStack> getOutputPositionedStacks() {
				final int itemLimit = Math.max(this.mOutputs.length, 0);
				final int fluidLimit = Math.max(this.mFluidOutputs.length, 0);
				final ArrayList<PositionedStack> outputStacks = new ArrayList<PositionedStack>(itemLimit + fluidLimit);				
				AutoMap<Object> mNEIMap = new AutoMap<Object>();				
				for (int i = 0; i < itemLimit; ++i) {
					mNEIMap.put((Object) this.mOutputs[i].copy());
				}				
				for (int i = 0; i < fluidLimit; ++i) {
					mNEIMap.put((Object) GT_Utility.getFluidDisplayStack(this.mFluidOutputs[i], true));
				}
				int xPos[] = new int[] {102, 120, 138};
				int yPos[] = new int[] {5, 23, 41, 59};			
				int mRow = 0;
				int mColumn = 0;
				for (int i = 0; i < Math.min(mNEIMap.size(), 16); ++i) {					
					if (mColumn >= 3) {
						mColumn = 0;
						mRow++;
					}					
					outputStacks.add(
							(PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
									(Object) mNEIMap.get(i), xPos[mColumn++], yPos[mRow]));
				}				
				return outputStacks;
			}
		}
	}

	public static class GT_Recipe_Map_LargeElectrolyzer extends Gregtech_Recipe_Map {
		private static int INPUT_COUNT;
		private static int OUTPUT_COUNT;
		private static int FLUID_INPUT_COUNT;
		private static int FLUID_OUTPUT_COUNT;

		public GT_Recipe_Map_LargeElectrolyzer() {
			super(new HashSet<Recipe_GT>(2000), "gt.recipe.largeelectrolyzer", "Large Electrolyzer", null,
					RES_PATH_GUI + "basicmachines/FissionFuel", GT_Recipe_Map_LargeElectrolyzer.INPUT_COUNT,
					GT_Recipe_Map_LargeElectrolyzer.OUTPUT_COUNT, 0, 0, 1, "", 1, "", true, true);
		}

		@Override
		public Recipe_GT addRecipe(final boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs,
				final Object aSpecial, final int[] aOutputChances, FluidStack[] aFluidInputs,
				FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			final ArrayList<ItemStack> adjustedInputs = new ArrayList<ItemStack>();
			final ArrayList<ItemStack> adjustedOutputs = new ArrayList<ItemStack>();
			final ArrayList<FluidStack> adjustedFluidInputs = new ArrayList<FluidStack>();
			final ArrayList<FluidStack> adjustedFluidOutputs = new ArrayList<FluidStack>();
			if (aInputs == null) {
				aInputs = new ItemStack[0];
			}
			for (final ItemStack input : aInputs) {
				FluidStack inputFluidContent = FluidContainerRegistry.getFluidForFilledItem(input);
				if (inputFluidContent != null) {
					final FluidStack fluidStack = inputFluidContent;
					fluidStack.amount *= input.stackSize;
					if (inputFluidContent.getFluid().getName().equals("ic2steam")) {
						inputFluidContent = GT_ModHandler.getSteam((long) inputFluidContent.amount);
					}
					adjustedFluidInputs.add(inputFluidContent);
				} else {
					final ItemData itemData = GT_OreDictUnificator.getItemData(input);
					if (itemData == null || !itemData.hasValidPrefixMaterialData()
							|| itemData.mMaterial.mMaterial != Materials.Empty) {
						if (itemData != null && itemData.hasValidPrefixMaterialData()
								&& itemData.mPrefix == OrePrefixes.cell) {
							//final ItemStack dustStack = itemData.mMaterial.mMaterial.getDust(input.stackSize);
							final ItemStack dustStack = ItemUtils.getGregtechOreStack(OrePrefixes.dust, itemData.mMaterial.mMaterial, input.stackSize);
							if (dustStack != null) {
								adjustedInputs.add(dustStack);
							} else {
								adjustedInputs.add(input);
							}
						} else {
							adjustedInputs.add(input);
						}
					}
				}
			}
			if (aFluidInputs == null) {
				aFluidInputs = new FluidStack[0];
			}
			for (final FluidStack fluidInput : aFluidInputs) {
				adjustedFluidInputs.add(fluidInput);
			}
			aInputs = adjustedInputs.toArray(new ItemStack[adjustedInputs.size()]);
			aFluidInputs = adjustedFluidInputs.toArray(new FluidStack[adjustedFluidInputs.size()]);
			if (aOutputs == null) {
				aOutputs = new ItemStack[0];
			}
			for (final ItemStack output : aOutputs) {
				FluidStack outputFluidContent = FluidContainerRegistry.getFluidForFilledItem(output);
				if (outputFluidContent != null) {
					final FluidStack fluidStack2 = outputFluidContent;
					fluidStack2.amount *= output.stackSize;
					if (outputFluidContent.getFluid().getName().equals("ic2steam")) {
						outputFluidContent = GT_ModHandler.getSteam((long) outputFluidContent.amount);
					}
					adjustedFluidOutputs.add(outputFluidContent);
				} else {
					final ItemData itemData = GT_OreDictUnificator.getItemData(output);
					if (itemData == null || !itemData.hasValidPrefixMaterialData()
							|| itemData.mMaterial.mMaterial != Materials.Empty) {
						adjustedOutputs.add(output);
					}
				}
			}
			if (aFluidOutputs == null) {
				aFluidOutputs = new FluidStack[0];
			}
			for (final FluidStack fluidOutput : aFluidOutputs) {
				adjustedFluidOutputs.add(fluidOutput);
			}
			aOutputs = adjustedOutputs.toArray(new ItemStack[adjustedOutputs.size()]);
			aFluidOutputs = adjustedFluidOutputs.toArray(new FluidStack[adjustedFluidOutputs.size()]);			
			Recipe_GT mNew = new GT_Recipe_LargeElectrolyzer(false, aInputs, aOutputs, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, 0);
			if (RecipeUtils.doesGregtechRecipeHaveEqualCells(mNew)) {
				return this.addRecipe(mNew);
			}
			else {
				return null;
			}			
		}

		static {
			GT_Recipe_Map_LargeElectrolyzer.INPUT_COUNT = 2;
			GT_Recipe_Map_LargeElectrolyzer.OUTPUT_COUNT = 2;
			GT_Recipe_Map_LargeElectrolyzer.FLUID_INPUT_COUNT = 4;
			GT_Recipe_Map_LargeElectrolyzer.FLUID_OUTPUT_COUNT = 4;
		}

		private static class GT_Recipe_LargeElectrolyzer extends Recipe_GT {
			protected GT_Recipe_LargeElectrolyzer(final boolean aOptimize, final ItemStack[] aInputs,
					final ItemStack[] aOutputs, final Object aSpecialItems, final int[] aChances,
					final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration,
					final int aEUt, final int aSpecialValue) {
				super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration,
						aEUt, aSpecialValue);
			}

			@Override
			public ArrayList<PositionedStack> getInputPositionedStacks() {
				final int itemLimit = Math.min(this.mInputs.length, GT_Recipe_Map_LargeElectrolyzer.INPUT_COUNT);
				final int fluidLimit = Math.min(this.mFluidInputs.length,
						GT_Recipe_Map_LargeElectrolyzer.FLUID_INPUT_COUNT);
				final ArrayList<PositionedStack> inputStacks = new ArrayList<PositionedStack>(itemLimit + fluidLimit);
				for (int i = 0; i < itemLimit; ++i) {
					inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
							(Object) this.mInputs[i].copy(), 48 - i * 18, 5));
				}
				for (int i = 0; i < fluidLimit; ++i) {
					if (i < 3) {
						inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
								(Object) GT_Utility.getFluidDisplayStack(this.mFluidInputs[i], true), 48 - i * 18, 23));
					} else {
						inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
								(Object) GT_Utility.getFluidDisplayStack(this.mFluidInputs[i], true), 12, 5));
					}
				}
				return inputStacks;
			}

			@Override
			public ArrayList<PositionedStack> getOutputPositionedStacks() {
				final int itemLimit = Math.max(this.mOutputs.length, 0);
				final int fluidLimit = Math.max(this.mFluidOutputs.length, 0);
				final ArrayList<PositionedStack> outputStacks = new ArrayList<PositionedStack>(itemLimit + fluidLimit);				
				AutoMap<Object> mNEIMap = new AutoMap<Object>();				
				for (int i = 0; i < itemLimit; ++i) {
					mNEIMap.put((Object) this.mOutputs[i].copy());
				}				
				for (int i = 0; i < fluidLimit; ++i) {
					mNEIMap.put((Object) GT_Utility.getFluidDisplayStack(this.mFluidOutputs[i], true));
				}
				int xPos[] = new int[] {102, 120, 138};
				int yPos[] = new int[] {5, 23, 41, 59};		
				int mRow = 0;
				int mColumn = 0;
				for (int i = 0; i < Math.min(mNEIMap.size(), 16); ++i) {					
					if (mColumn >= 3) {
						mColumn = 0;
						mRow++;
					}					
					outputStacks.add(
							(PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
									(Object) mNEIMap.get(i), xPos[mColumn++], yPos[mRow]));
				}				
				return outputStacks;
			}
		}
	}

	public static class GT_Recipe_Map_AdvancedVacuumFreezer extends Gregtech_Recipe_Map {
		private static int INPUT_COUNT;
		private static int OUTPUT_COUNT;
		private static int FLUID_INPUT_COUNT;
		private static int FLUID_OUTPUT_COUNT;

		public GT_Recipe_Map_AdvancedVacuumFreezer() {
			super(new HashSet<Recipe_GT>(2000), "gt.recipe.advfreezer", "Adv. Cryogenic Freezer", null,
					RES_PATH_GUI + "basicmachines/FissionFuel", GT_Recipe_Map_AdvancedVacuumFreezer.INPUT_COUNT,
					GT_Recipe_Map_AdvancedVacuumFreezer.OUTPUT_COUNT, 0, 0, 1, "", 1, "", true, true);
		}

		@Override
		public Recipe_GT addRecipe(final boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs,
				final Object aSpecial, final int[] aOutputChances, FluidStack[] aFluidInputs,
				FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
			final ArrayList<ItemStack> adjustedInputs = new ArrayList<ItemStack>();
			final ArrayList<ItemStack> adjustedOutputs = new ArrayList<ItemStack>();
			final ArrayList<FluidStack> adjustedFluidInputs = new ArrayList<FluidStack>();
			final ArrayList<FluidStack> adjustedFluidOutputs = new ArrayList<FluidStack>();
			if (aInputs == null) {
				aInputs = new ItemStack[0];
			}
			for (final ItemStack input : aInputs) {
				FluidStack inputFluidContent = FluidContainerRegistry.getFluidForFilledItem(input);
				if (inputFluidContent != null) {
					final FluidStack fluidStack = inputFluidContent;
					fluidStack.amount *= input.stackSize;
					if (inputFluidContent.getFluid().getName().equals("ic2steam")) {
						inputFluidContent = GT_ModHandler.getSteam((long) inputFluidContent.amount);
					}
					adjustedFluidInputs.add(inputFluidContent);
				} else {
					final ItemData itemData = GT_OreDictUnificator.getItemData(input);
					if (itemData == null || !itemData.hasValidPrefixMaterialData()
							|| itemData.mMaterial.mMaterial != Materials.Empty) {
						if (itemData != null && itemData.hasValidPrefixMaterialData()
								&& itemData.mPrefix == OrePrefixes.cell) {
							//final ItemStack dustStack = itemData.mMaterial.mMaterial.getDust(input.stackSize);
							final ItemStack dustStack = ItemUtils.getGregtechOreStack(OrePrefixes.dust, itemData.mMaterial.mMaterial, input.stackSize);
							if (dustStack != null) {
								adjustedInputs.add(dustStack);
							} else {
								adjustedInputs.add(input);
							}
						} else {
							adjustedInputs.add(input);
						}
					}
				}
			}
			if (aFluidInputs == null) {
				aFluidInputs = new FluidStack[0];
			}
			for (final FluidStack fluidInput : aFluidInputs) {
				adjustedFluidInputs.add(fluidInput);
			}
			aInputs = adjustedInputs.toArray(new ItemStack[adjustedInputs.size()]);
			aFluidInputs = adjustedFluidInputs.toArray(new FluidStack[adjustedFluidInputs.size()]);
			if (aOutputs == null) {
				aOutputs = new ItemStack[0];
			}
			for (final ItemStack output : aOutputs) {
				FluidStack outputFluidContent = FluidContainerRegistry.getFluidForFilledItem(output);
				if (outputFluidContent != null) {
					final FluidStack fluidStack2 = outputFluidContent;
					fluidStack2.amount *= output.stackSize;
					if (outputFluidContent.getFluid().getName().equals("ic2steam")) {
						outputFluidContent = GT_ModHandler.getSteam((long) outputFluidContent.amount);
					}
					adjustedFluidOutputs.add(outputFluidContent);
				} else {
					final ItemData itemData = GT_OreDictUnificator.getItemData(output);
					if (itemData == null || !itemData.hasValidPrefixMaterialData()
							|| itemData.mMaterial.mMaterial != Materials.Empty) {
						adjustedOutputs.add(output);
					}
				}
			}
			if (aFluidOutputs == null) {
				aFluidOutputs = new FluidStack[0];
			}
			for (final FluidStack fluidOutput : aFluidOutputs) {
				adjustedFluidOutputs.add(fluidOutput);
			}
			aOutputs = adjustedOutputs.toArray(new ItemStack[adjustedOutputs.size()]);
			aFluidOutputs = adjustedFluidOutputs.toArray(new FluidStack[adjustedFluidOutputs.size()]);

			Recipe_GT mNew = new GT_Recipe_AdvFreezer(false, aInputs, aOutputs, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, 0);
			if (RecipeUtils.doesGregtechRecipeHaveEqualCells(mNew)) {
				return this.addRecipe(mNew);
			}
			else {
				return null;
			}
		}

		static {
			GT_Recipe_Map_AdvancedVacuumFreezer.INPUT_COUNT = 2;
			GT_Recipe_Map_AdvancedVacuumFreezer.OUTPUT_COUNT = 2;
			GT_Recipe_Map_AdvancedVacuumFreezer.FLUID_INPUT_COUNT = 4;
			GT_Recipe_Map_AdvancedVacuumFreezer.FLUID_OUTPUT_COUNT = 4;
		}

		private static class GT_Recipe_AdvFreezer extends Recipe_GT {
			protected GT_Recipe_AdvFreezer(final boolean aOptimize, final ItemStack[] aInputs,
					final ItemStack[] aOutputs, final Object aSpecialItems, final int[] aChances,
					final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration,
					final int aEUt, final int aSpecialValue) {
				super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration,
						aEUt, aSpecialValue);
			}

			public ArrayList<PositionedStack> getInputPositionedStacks() {
				final int itemLimit = Math.min(this.mInputs.length, GT_Recipe_Map_AdvancedVacuumFreezer.INPUT_COUNT);
				final int fluidLimit = Math.min(this.mFluidInputs.length,
						GT_Recipe_Map_AdvancedVacuumFreezer.FLUID_INPUT_COUNT);
				final ArrayList<PositionedStack> inputStacks = new ArrayList<PositionedStack>(itemLimit + fluidLimit);
				for (int i = 0; i < itemLimit; ++i) {
					inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
							(Object) this.mInputs[i].copy(), 48 - i * 18, 5));
				}
				for (int i = 0; i < fluidLimit; ++i) {
					if (i < 3) {
						inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
								(Object) GT_Utility.getFluidDisplayStack(this.mFluidInputs[i], true), 48 - i * 18, 23));
					} else {
						inputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
								(Object) GT_Utility.getFluidDisplayStack(this.mFluidInputs[i], true), 12, 5));
					}
				}
				return inputStacks;
			}


			public ArrayList<PositionedStack> getOutputPositionedStacks() {
				final int itemLimit = Math.min(this.mOutputs.length, GT_Recipe_Map_AdvancedVacuumFreezer.OUTPUT_COUNT);
				final int fluidLimit = Math.min(this.mFluidOutputs.length,
						GT_Recipe_Map_AdvancedVacuumFreezer.FLUID_OUTPUT_COUNT);
				final ArrayList<PositionedStack> outputStacks = new ArrayList<PositionedStack>(itemLimit + fluidLimit);
				for (int i = 0; i < itemLimit; ++i) {
					outputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
							(Object) this.mOutputs[i].copy(), 102 + i * 18, 5));
				}
				for (int i = 0; i < fluidLimit; ++i) {
					outputStacks.add((PositionedStack) new GT_NEI_MultiBlockHandler.FixedPositionedStack(
							(Object) GT_Utility.getFluidDisplayStack(this.mFluidOutputs[i], true), 102 + i * 18, 23));
				}
				return outputStacks;
			}
		}
	}

	public ArrayList<PositionedStack> getInputPositionedStacks() {
		return null;
	}

	public ArrayList<PositionedStack> getOutputPositionedStacks() {
		return null;
	}

	public int compareTo(Recipe_GT recipe) {
		// first lowest tier recipes
		// then fastest
		// then with lowest special value
		// then dry recipes
		// then with fewer inputs
		if (this.mEUt != recipe.mEUt) {
			return this.mEUt - recipe.mEUt;
		} else if (this.mDuration != recipe.mDuration) {
			return this.mDuration - recipe.mDuration;
		} else if (this.mSpecialValue != recipe.mSpecialValue) {
			return this.mSpecialValue - recipe.mSpecialValue;
		} else if (this.mFluidInputs.length != recipe.mFluidInputs.length) {
			return this.mFluidInputs.length - recipe.mFluidInputs.length;
		} else if (this.mInputs.length != recipe.mInputs.length) {
			return this.mInputs.length - recipe.mInputs.length;
		}
		return 0;
	}
	
	public int compareTo(GT_Recipe recipe) {
		// first lowest tier recipes
		// then fastest
		// then with lowest special value
		// then dry recipes
		// then with fewer inputs
		if (this.mEUt != recipe.mEUt) {
			return this.mEUt - recipe.mEUt;
		} else if (this.mDuration != recipe.mDuration) {
			return this.mDuration - recipe.mDuration;
		} else if (this.mSpecialValue != recipe.mSpecialValue) {
			return this.mSpecialValue - recipe.mSpecialValue;
		} else if (this.mFluidInputs.length != recipe.mFluidInputs.length) {
			return this.mFluidInputs.length - recipe.mFluidInputs.length;
		} else if (this.mInputs.length != recipe.mInputs.length) {
			return this.mInputs.length - recipe.mInputs.length;
		}
		return 0;
	}

}