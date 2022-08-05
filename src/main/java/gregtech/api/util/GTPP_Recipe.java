package gregtech.api.util;

import static gregtech.api.enums.GT_Values.*;

import java.util.*;

import codechicken.nei.PositionedStack;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.*;
import gregtech.api.objects.GT_ItemStack;
import gregtech.nei.GT_NEI_DefaultHandler.FixedPositionedStack;
import gtPlusPlus.api.interfaces.IComparableRecipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.*;

/**
 * Custom GT Recipe Class
 * @author Alkalus
 *
 */
public class GTPP_Recipe extends GT_Recipe implements IComparableRecipe {

	private final String mRecipeHash;
	private final AutoMap<Integer> mHashMap = new AutoMap<Integer>();

	public GTPP_Recipe(final boolean aOptimize, final ItemStack[] aInputs, final ItemStack[] aOutputs, final Object aSpecialItems, final int[] aChances, final FluidStack[] aFluidInputs, final FluidStack[] aFluidOutputs, final int aDuration, final int aEUt, final int aSpecialValue) {
		super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
		//Logger.SPECIFIC_WARNING(this.getClass().getName()+" | [GregtechRecipe]", "Created new recipe instance for "+ItemUtils.getArrayStackNames(aInputs), 167);
		this.mRecipeHash = getRecipeHash(this);
		this.mHashMap.addAll(convertStringDataToInts(getEncodedRecipeData(this)));
	}

	public GTPP_Recipe(final ItemStack aInput1, final ItemStack aOutput1, final int aFuelValue, final int aType) {
		this(aInput1, aOutput1, null, null, null, aFuelValue, aType);
	}

	private static AutoMap<Integer> convertStringDataToInts(AutoMap<String> aData){
		AutoMap<Integer> aMap = new AutoMap<Integer>();
		for (String string : aData) {
			aMap.add(string.hashCode());
		}
		return aMap;
	}

	private static AutoMap<String> getEncodedRecipeData(GTPP_Recipe aRecipe){
		AutoMap<String> aData = new AutoMap<String>();
		aData.add(aRecipe.mRecipeHash);
		aData.add(""+aRecipe.mCanBeBuffered);
		aData.add(""+aRecipe.mHidden);
		aData.add(""+aRecipe.mEnabled);
		aData.add(""+aRecipe.mDuration);
		aData.add(""+aRecipe.mEUt);
		aData.add(""+aRecipe.mFakeRecipe);
		aData.add(""+aRecipe.mSpecialItems);
		aData.add(aRecipe.mChances.toString());
		aData.add(aRecipe.mInputs.toString());
		aData.add(aRecipe.mOutputs.toString());
		aData.add(aRecipe.mFluidInputs.toString());
		aData.add(aRecipe.mFluidOutputs.toString());
		return aData;
	}

	public static String getRecipeHash(GT_Recipe aRecipe) {
		String aEncoderString = aRecipe.toString();
		return aEncoderString;
	}

	private final void checkModified() {
		if (hasBeenModified()) {
			String[] aInfo = RecipeUtils.getRecipeInfo(this);
			for (String s : aInfo) {
				Logger.INFO(s);
			}
			CORE.crash("Someone has edited an internal GT++ recipe, which is no longer allowed. Please complain to whoever has done this, not Alkalus.");
		}
	}

	private final boolean hasBeenModified() {
		String aEncoderString = this.toString();
		boolean aBasicHashCheck = this.mRecipeHash.equals(aEncoderString);
		if (!aBasicHashCheck) {
			Logger.INFO("This Recipe Hash: "+aEncoderString);
			Logger.INFO("Expected Hash Code: "+this.mRecipeHash);
			return true;
		}
		AutoMap<Integer> aData = new AutoMap<Integer>();
		aData.addAll(convertStringDataToInts(getEncodedRecipeData(this)));
		long aHashTotal = 0;
		long aExpectedHashTotal = 0;
		for (int a : aData) {
			aHashTotal += a;
		}
		for (int a : this.mHashMap) {
			aExpectedHashTotal += a;
		}
		if (aHashTotal != aExpectedHashTotal) {
			Logger.INFO("This Recipe Hash: "+aEncoderString);
			Logger.INFO("Expected Hash Code: "+this.mRecipeHash);
			Logger.INFO("This Recipe Hash: "+aHashTotal);
			Logger.INFO("Expected Hash Code: "+aExpectedHashTotal);
			return true;
		}
		return false;
	}

	// aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
	public GTPP_Recipe(final ItemStack aInput1, final ItemStack aOutput1, final ItemStack aOutput2, final ItemStack aOutput3, final ItemStack aOutput4, final int aSpecialValue, final int aType) {
		this(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4}, null, null, null, null, 0, 0, Math.max(1, aSpecialValue));

		Logger.WARNING("Switch case method for adding fuels");
		if ((this.mInputs.length > 0) && (aSpecialValue > 0)) {
			switch (aType) {
			// Diesel Generator
			case 0:
				Logger.WARNING("Added fuel "+aInput1.getDisplayName()+" is ROCKET FUEL - continuing");
				GTPP_Recipe_Map.sRocketFuels.addRecipe(this);
				break;
				// Gas Turbine
			case 1:
				GTPP_Recipe_Map.sGeoThermalFuels.addRecipe(this);
				break;
				// Thermal Generator
			case 2:
				GTPP_Recipe_Map.sRTGFuels.addRecipe(this);
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

	public static void reInit() {
		GT_Log.out.println("GT_Mod: Re-Unificating Recipes.");
		for (final GTPP_Recipe_Map_Internal tMapEntry : GTPP_Recipe_Map_Internal.sMappingsEx) {
			//tMapEntry.reInit();
			if (tMapEntry != null && tMapEntry.mRecipeList != null && !tMapEntry.mRecipeList.isEmpty()) {
				for (GT_Recipe aRecipe : tMapEntry.mRecipeList) {
					checkRecipeOwnership(aRecipe);
				}
			}
		}
	}

	private final static boolean checkRecipeOwnership(GT_Recipe aRecipe) {
		if (aRecipe != null && aRecipe instanceof GTPP_Recipe) {
			GTPP_Recipe nRecipe = (GTPP_Recipe) aRecipe;
			GTPP_Recipe_Map_Internal.mHashedRecipes.put(nRecipe.hashCode(), nRecipe);
			return true;
		}
		return false;
	}

	public final static void checkRecipeModifications() {
		for (GTPP_Recipe aRecipe : GTPP_Recipe_Map_Internal.mHashedRecipes.values()) {
			Logger.INFO("Checking recipe: "+aRecipe.hashCode());
			aRecipe.checkModified();
		}
	}

	public static class GTPP_Recipe_Map_Internal extends GT_Recipe_Map {

		public static final Collection<GTPP_Recipe_Map_Internal> sMappingsEx = new ArrayList<>();
		private static final HashMap<Integer, GTPP_Recipe> mHashedRecipes = new HashMap<Integer, GTPP_Recipe>();

		public GTPP_Recipe_Map_Internal(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
			GT_Recipe_Map.sMappings.remove(this);
			GTPP_Recipe_Map_Internal.sMappingsEx.add(this);
		}
	}

	public static class GTPP_Recipe_Map {
		//public static final GT_Recipe_Map sChemicalBathRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gtpp.recipe.chemicalbath", "Chemical Bath", null, RES_PATH_GUI + "basicmachines/ChemicalBath", 1, 3, 1, 1, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sCokeOvenRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.cokeoven", "Coke Oven", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 1, 0, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sMatterFab2Recipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.matterfab2", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Default", 6, 6, 0, 0, 1, E, 1, E, true, true);
		//public static final Gregtech_Recipe_Map sMatterFabRecipes = new Gregtech_Recipe_Map(new HashSet<GregtechRecipe>(200), "gtpp.recipe.matterfab", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Massfabricator", 1, 3, 1, 1, 1, E, 1, E, true, true);

		public static final GT_Recipe_Map_Fuel sRocketFuels = new GT_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gtpp.recipe.rocketenginefuel", "Rocket Engine Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 3000, " EU", true, true);

		public static final GTPP_Recipe_Map_Internal sGeoThermalFuels = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10), "gtpp.recipe.geothermalfuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);
		public static final GTPP_Recipe_Map_Internal sChemicalDehydratorRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.chemicaldehydrator", "Dehydrator", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sVacuumFurnaceRecipes = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(500), "gtpp.recipe.vacfurnace", "Vacuum Furnace", null, RES_PATH_GUI + "basicmachines/FissionFuel", 6, 6, 1, 0, 1, "Heat Capacity: ", 1, " K", false, true);
		public static final GTPP_Recipe_Map_Internal sAlloyBlastSmelterRecipes = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(200), "gtpp.recipe.alloyblastsmelter", "Alloy Blast Smelter", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 1, 0, 1, E, 1, E, true, true);
		public static final GTPP_Recipe_Map_Internal sSteamTurbineFuels = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10), "gtpp.recipe.steamturbinefuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, false);

		//LFTR recipes
		public static final GTPP_Recipe_Map_Internal sLiquidFluorineThoriumReactorRecipes = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(50), "gtpp.recipe.lftr", "Liquid Fluoride Thorium Reactor", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 2, 0, "Power: ", 1, " EU/t per Dynamo", true, true);

		// Ore Milling Map
		public static final GTPP_Recipe_Map_Internal sOreMillRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.oremill", "Milling", null, RES_PATH_GUI + "basicmachines/LFTR", 3, 4, 1, 0, 1, E, 1, E, true, true);

		//Fission Fuel Plant Recipes
		public static final GTPP_Recipe_Map_Internal sFissionFuelProcessing = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(50), "gtpp.recipe.fissionfuel", "Nuclear Fuel Processing", null, RES_PATH_GUI + "basicmachines/FissionFuel", 0, 0, 0, 0, 1, E, 1, E, true, true);

		//Cold Trap
		public static final GTPP_Recipe_Map_Internal sColdTrapRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.coldtrap", "Cold Trap", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);

		//Reactor Processing Unit
		public static final GTPP_Recipe_Map_Internal sReactorProcessingUnitRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.reactorprocessingunit", "Reactor Processing Unit", null, RES_PATH_GUI + "basicmachines/Dehydrator", 2, 9, 0, 0, 1, E, 1, E, true, true);

		//Basic Washer Map
		public static final GTPP_Recipe_Map_Internal sSimpleWasherRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.simplewasher", "Simple Dust Washer", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 1, 1, 0, 0, 1, E, 1, E, true, true);

		//Molecular Transformer Map
		public static final GTPP_Recipe_Map_Internal sMolecularTransformerRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.moleculartransformer", "Molecular Transformer", null, RES_PATH_GUI + "basicmachines/Scanner", 1, 1, 0, 0, 1, E, 1, E, true, true);

		//Elemental Duplicator Map
		public static final GTPP_Recipe_Map_Internal sElementalDuplicatorRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.elementaldupe", "Elemental Duplicator", null, RES_PATH_GUI + "basicmachines/Replicator", 1, 1, 0, 1, 1, E, 1, E, true, false);


		//public static final GT_Recipe_Map sSimpleWasherRecipes_FakeFuckBW = new GT_Recipe_Map(new HashSet<GT_Recipe>(3), "gtpp.recipe.simplewasher", "Fuck you Bart", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 1, 1, 0, 0, 1, E, 1, E, true, false);

		public static final GTPP_Recipe_Map_Internal sChemicalPlantRecipes = new GTPP_Recipe_Map_ChemicalPlant(
				new HashSet<GT_Recipe>(100),
				"gtpp.recipe.fluidchemicaleactor",
				"Chemical Plant",
				null,
				CORE.MODID + ":textures/gui/FluidReactor",
				0,
				0,
				0,
				2,
				1,
				"Tier: ",
				1,
				E,
				true,
				true);


		//RTG Fuel Map
		public static final GT_Recipe.GT_Recipe_Map_Fuel sRTGFuels = new GTPP_Recipe.GT_Recipe_Map_Fuel(
				new HashSet<GT_Recipe>(10), "gtpp.recipe.RTGgenerators", "RTG", null,
				"gregtech:textures/gui/basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 365, " Minecraft Days", true, true);

		//Thermal Boiler map
		public static final GTPP_Recipe_Map_Internal sThermalFuels = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(10), "gtpp.recipe.thermalgeneratorfuel",
				"Thermal Generator Fuel", null, RES_PATH_GUI + "basicmachines/FissionFuel", 1, 1, 0, 0, 1,
				null, 1000, null, true, true);

		//Solar Tower map
		public static final GTPP_Recipe_Map_Internal sSolarTowerRecipes = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(10), "gtpp.recipe.solartower",
				"Solar Tower", null, RES_PATH_GUI + "basicmachines/FissionFuel", 1, 1, 0, 0, 1,
				null, 1000, null, true, true);

		//Cyclotron recipe map
		public static final GTPP_Recipe_Map_Internal sCyclotronRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(200), "gtpp.recipe.cyclotron", "COMET - Compact Cyclotron", null, RES_PATH_GUI + "basicmachines/BlastSmelter", 2, 16, 0, 0, 1, E, 1, E, true, true);

		//Advanced Mixer
		public static final GTPP_Recipe_Map_Internal sAdvancedMixerRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(1000), "gtpp.recipe.advanced.mixer",
				"Advanced Material Combiner", null, "gregtech:textures/gui/basicmachines/MixerAdvanced", 4, 4, 1, 0, 2, "", 1, "", true, true);


		//Mini Fusion
		public static final GTPP_Recipe_Map_Internal sSlowFusionRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(50), "gtpp.recipe.slowfusionreactor",
				"Mimir - Slow Fusion", null, "gregtech:textures/gui/basicmachines/LFTR", 0, 0, 0, 2, 1, "Start: ", 1,
				" EU", true, true);


		//Component Assembler
		public static final GT_Recipe_Map sComponentAssemblerRecipes = new GT_Recipe_Map_Assembler(new HashSet<GT_Recipe>(300), "gtpp.recipe.componentassembler", "Component Assembler", null, RES_PATH_GUI + "basicmachines/Assembler", 6, 1, 1, 0, 1, E, 1, E, true, true);

		//Special Maps for Multis
		public static final GTPP_Recipe_Map_Internal sFishPondRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(3), "gtpp.recipe.fishpond", "Zhuhai - Fishing Port", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 0, 1, 0, 0, 1, "Requires Circuit: ", 1, ".", true, true);
		public static final GTPP_Recipe_Map_Internal sSpargeTowerRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.spargetower", "Sparging", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, E, 1, E, true, false);

		//public static final GTPP_Recipe_Map sMultiblockCentrifugeRecipes = new GT_Recipe_Map_LargeCentrifuge();
		//public static final GTPP_Recipe_Map sMultiblockElectrolyzerRecipes = new GT_Recipe_Map_LargeElectrolyzer();
		//public static final GTPP_Recipe_Map sAdvFreezerRecipes = new GT_Recipe_Map_AdvancedVacuumFreezer();

		public static final GTPP_Recipe_Map_Internal sAdvFreezerRecipes_GT = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(2000), "gtpp.recipe.cryogenicfreezer", "Cryogenic Freezer", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, "", 0, "", false, true);
		public static final GTPP_Recipe_Map_Internal sMultiblockCentrifugeRecipes_GT = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(2000), "gtpp.recipe.multicentrifuge", "Multiblock Centrifuge", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, "", 0, "", false, true);
		public static final GTPP_Recipe_Map_Internal sMultiblockElectrolyzerRecipes_GT = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(2000), "gtpp.recipe.multielectro", "Multiblock Electrolyzer", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 0, 0, 1, "", 0, "", false, true);
		// internal copy of sChemicalPlantRecipes
		public static final GTPP_Recipe_Map_Internal sChemicalPlant_GT = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(2000), "gtpp.recipe.temp4", "temp4", null, RES_PATH_GUI + "basicmachines/PotionBrewer", 0, 0, 0, 0, 0, "", 0, "", false, false);
		public static final GTPP_Recipe_Map_Internal sMultiblockMixerRecipes_GT = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(2000), "gtpp.recipe.multimixer", "Multiblock Mixer", null, RES_PATH_GUI + "basicmachines/FissionFuel", 12, 9, 0, 0, 1, "", 0, "", false, true);

		//Semi-Fluid Fuel Map
		public static final GT_Recipe_Map_Fuel sSemiFluidLiquidFuels = new GT_Recipe_Map_Fuel(new HashSet<GT_Recipe>(10), "gtpp.recipe.semifluidgeneratorfuels", "Semifluid Generator Fuels", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);

		// Flotation Cell
		public static final GTPP_Recipe_Map_Internal sFlotationCellRecipes = new GTPP_Recipe_Map_Internal(new HashSet<GT_Recipe>(10000), "gtpp.recipe.flotationcell", "Flotation Cell", null, RES_PATH_GUI + "basicmachines/LFTR", 6, 4, 1, 1, 1, "Ore Key: ", 1, E, true, true);

		// Tree Growth Simulator
		public static final GTPP_Recipe_Map_Internal sTreeSimFakeRecipes = new GTPP_Recipe_Map_MultiNoCell(new HashSet<GT_Recipe>(100), "gtpp.recipe.treefarm", "Tree Growth Simulator", null, RES_PATH_GUI + "basicmachines/FissionFuel", 9, 9, 1, 0, 1, "", 1, "", false, true);

	}

	public static class GTPP_Recipe_Map_MultiNoCell extends GTPP_Recipe_Map_Internal {

		// region NEI stuff
		private static final HashMap<Integer, Pair<Integer, Integer>> mInputSlotMap = new HashMap<>();
		private static final HashMap<Integer, Pair<Integer, Integer>> mOutputSlotMap = new HashMap<>();

		static {
			int[] slotsX = new int[] {12, 30, 48};
			int[] slotsY = new int[] {5, 23, 41, 64};
			// Input slots
			int aIndex = 0;
			for (int slotY : slotsY) {
				for (int slotX : slotsX) {
					mInputSlotMap.put(aIndex++, new Pair<>(slotX, slotY));
				}
			}
			// Output slots
			slotsX = new int[] {102, 120, 138};
			aIndex = 0;
			for (int slotY : slotsY) {
				for (int slotX : slotsX) {
					mOutputSlotMap.put(aIndex++, new Pair<>(slotX, slotY));
				}
			}
		}

		// endregion NEI stuff

		public GTPP_Recipe_Map_MultiNoCell(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		@Override
		public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GTPP_Recipe_MultiNoCell(aOptimize, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		@Override
		public GT_Recipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GTPP_Recipe_MultiNoCell(false, null, null, null, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue), false, false, false);
		}

		@Override
		public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GTPP_Recipe_MultiNoCell(aOptimize, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		@Override
		public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addFakeRecipe(aCheckForCollisions, new GTPP_Recipe_MultiNoCell(false, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		@Override
		public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addFakeRecipe(aCheckForCollisions, new GTPP_Recipe_MultiNoCell(false, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		@Override
		public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue,boolean hidden) {
			return addFakeRecipe(aCheckForCollisions, new GTPP_Recipe_MultiNoCell(false, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue),hidden);
		}

		public static class GTPP_Recipe_MultiNoCell extends GT_Recipe {

			public GTPP_Recipe_MultiNoCell(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
				super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
			}

			@Override
			public ArrayList<PositionedStack> getInputPositionedStacks() {
				int aInputItemsCount = mInputs.length;
				int aInputFluidsCount = mFluidInputs.length;
				int aInputSlotsUsed = 0;
				int aSlotToCheck = 0;

				ArrayList<PositionedStack> inputStacks = new ArrayList<>(aInputItemsCount + aInputFluidsCount);

				// Special Slot
				if (mSpecialItems != null) {
					inputStacks.add(new FixedPositionedStack(mSpecialItems, 120, 52));
				}
				// Up to 9 Inputs Slots
				if (aInputItemsCount > 0) {
					if (aInputItemsCount > 9) {
						aInputItemsCount = 9;
					}
					for (int i=0;i<aInputItemsCount;i++) {
						int x = mInputSlotMap.get(aInputSlotsUsed).getKey();
						int y = mInputSlotMap.get(aInputSlotsUsed).getValue();
						ItemStack aRepStack = getRepresentativeInput(aSlotToCheck++);
						if (aRepStack != null) {
							inputStacks.add(new FixedPositionedStack(aRepStack, x, y));
							aInputSlotsUsed++;
						}
					}
				}
				// Up to 9 Fluid Inputs Slots
				aSlotToCheck = aInputSlotsUsed;
				if (aInputFluidsCount > 0) {
					for (int i=0;i<aInputFluidsCount;i++) {
						int x = mInputSlotMap.get(aSlotToCheck).getKey();
						int y = mInputSlotMap.get(aSlotToCheck).getValue();
						inputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidInputs[i], true), x, y));
						aSlotToCheck++;
						aInputSlotsUsed++;
					}
				}

				return inputStacks;
			}

			@Override
			public ArrayList<PositionedStack> getOutputPositionedStacks() {
				int aOutputItemsCount = mOutputs.length;
				int aOutputFluidsCount = mFluidOutputs.length;
				int aOutputSlotsUsed = 0;
				int aSlotToCheck = 0;

				ArrayList<PositionedStack> outputStacks = new ArrayList<>(aOutputItemsCount + aOutputFluidsCount);

				// Up to 9 Output Slots
				if (aOutputItemsCount > 0) {
					if (aOutputItemsCount > 9) {
						aOutputItemsCount = 9;
					}
					for (int i=0;i<aOutputItemsCount;i++) {
						int x = mOutputSlotMap.get(aOutputSlotsUsed).getKey();
						int y = mOutputSlotMap.get(aOutputSlotsUsed).getValue();
						ItemStack aRepStack = getOutput(aSlotToCheck);
						if (aRepStack != null) {
							outputStacks.add(new FixedPositionedStack(aRepStack, x, y, getOutputChance(aSlotToCheck)));
							aOutputSlotsUsed++;
						}
						aSlotToCheck++;
					}
				}
				// Up to 9 Fluid Outputs Slots
				aSlotToCheck = aOutputSlotsUsed;
				if (aOutputFluidsCount > 0) {
					for (int i=0;i<aOutputFluidsCount;i++) {
						int x = mOutputSlotMap.get(aSlotToCheck).getKey();
						int y = mOutputSlotMap.get(aSlotToCheck).getValue();
						outputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidOutputs[i], true), x, y));
						aSlotToCheck++;
						aOutputSlotsUsed++;
					}
				}

				return outputStacks;
			}
		}
	}

	public static class GTPP_Recipe_Map_ChemicalPlant extends GTPP_Recipe_Map_Internal {

		public GTPP_Recipe_Map_ChemicalPlant(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		@Override
		public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GTPP_Recipe_ChemicalPlant(aOptimize, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		@Override
		public GT_Recipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GTPP_Recipe_ChemicalPlant(false, null, null, null, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue), false, false, false);
		}

		@Override
		public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GTPP_Recipe_ChemicalPlant(aOptimize, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		public static class GTPP_Recipe_ChemicalPlant extends GT_Recipe {

			public GTPP_Recipe_ChemicalPlant(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
				super(aOptimize, aInputs, aOutputs, aSpecialItems, aChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue);
			}

			@Override
			public ArrayList<PositionedStack> getInputPositionedStacks() {
				int tStartIndex = 0;
				ArrayList<PositionedStack> inputStacks = new ArrayList<>(mInputs.length + mFluidInputs.length);

				// Four Input Slots
				if (getRepresentativeInput(tStartIndex) != null) {
					inputStacks.add(new FixedPositionedStack(getRepresentativeInput(tStartIndex), 3, -4));
				}
				tStartIndex++;
				if (getRepresentativeInput(tStartIndex) != null) {
					inputStacks.add(new FixedPositionedStack(getRepresentativeInput(tStartIndex), 21, -4));
				}
				tStartIndex++;
				if (getRepresentativeInput(tStartIndex) != null) {
					inputStacks.add(new FixedPositionedStack(getRepresentativeInput(tStartIndex), 39, -4));
				}
				tStartIndex++;
				if (getRepresentativeInput(tStartIndex) != null) {
					inputStacks.add(new FixedPositionedStack(getRepresentativeInput(tStartIndex), 57, -4));
				}
				tStartIndex++;

				if (mSpecialItems != null) {
					inputStacks.add(new FixedPositionedStack(mSpecialItems, 120, 52));
				}

				//New fluid display behaviour when 3 fluid inputs are detected. (Basically a mix of the code below for outputs an the code above for 9 input slots.)
				if (mFluidInputs.length >= 1) {
					if ((mFluidInputs[0] != null) && (mFluidInputs[0].getFluid() != null)) {
						inputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidInputs[0], true), 3, 31));
					}
					if ((mFluidInputs.length > 1) && (mFluidInputs[1] != null) && (mFluidInputs[1].getFluid() != null)) {
						inputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidInputs[1], true), 21, 31));
					}
					if ((mFluidInputs.length > 2) && (mFluidInputs[2] != null) && (mFluidInputs[2].getFluid() != null)) {
						inputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidInputs[2], true), 39, 31));
					}
					if ((mFluidInputs.length > 3) && (mFluidInputs[3] != null) && (mFluidInputs[3].getFluid() != null)) {
						inputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidInputs[3], true), 57, 31));
					}
				}

				return inputStacks;
			}

			@Override
			public ArrayList<PositionedStack> getOutputPositionedStacks() {
				int tStartIndex = 0;
				ArrayList<PositionedStack> outputStacks = new ArrayList<>(mOutputs.length + mFluidOutputs.length);

				//Four Output Slots
				if (getOutput(tStartIndex) != null) {
					outputStacks.add(new FixedPositionedStack(getOutput(tStartIndex), 102, 5, getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (getOutput(tStartIndex) != null) {
					outputStacks.add(new FixedPositionedStack(getOutput(tStartIndex), 120, 5, getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (getOutput(tStartIndex) != null) {
					outputStacks.add(new FixedPositionedStack(getOutput(tStartIndex), 102, 23, getOutputChance(tStartIndex)));
				}
				tStartIndex++;
				if (getOutput(tStartIndex) != null) {
					outputStacks.add(new FixedPositionedStack(getOutput(tStartIndex), 120, 23, getOutputChance(tStartIndex)));
				}
				tStartIndex++;

				if (mFluidOutputs.length > 0) {
					if ((mFluidOutputs[0] != null) && (mFluidOutputs[0].getFluid() != null)) {
						outputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidOutputs[0], true), 138, 5));
					}
					if ((mFluidOutputs.length > 1) && (mFluidOutputs[1] != null) && (mFluidOutputs[1].getFluid() != null)) {
						outputStacks.add(new FixedPositionedStack(GT_Utility.getFluidDisplayStack(mFluidOutputs[1], true), 138, 23));
					}
				}

				return outputStacks;
			}
		}
	}
}
