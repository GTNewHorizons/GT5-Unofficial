package gtPlusPlus.core.item.chemistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.common.StaticFields59;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class RocketFuels extends ItemPackage {

	public static HashSet<String> mValidRocketFuelNames = new HashSet<String>();
	public static HashMap<Integer, Fluid> mValidRocketFuels = new HashMap<Integer, Fluid>();

	public static Fluid Oil_Heavy;
	public static Fluid Diesel;
	public static Fluid Kerosene;
	public static Fluid RP1;	
	public static Fluid Nitrogen_Tetroxide;	
	public static Fluid Hydrazine;	
	public static Fluid Monomethylhydrazine;	
	public static Fluid Unsymmetrical_Dimethylhydrazine;	
	public static Fluid Nitrous_Oxide;		
	public static Fluid Hydrated_Ammonium_Nitrate_Slurry;	
	public static Fluid Liquid_Oxygen;
	public static Fluid Liquid_Hydrogen;
	public static Fluid Formaldehyde;


	//Rocket Fuel Mixs
	public static Fluid Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide;
	public static Fluid RP1_Plus_Liquid_Oxygen;
	public static Fluid Dense_Hydrazine_Mix;
	public static Fluid Monomethylhydrazine_Plus_Nitric_Acid;

	public static Item Ammonium_Nitrate_Dust;
	public static Item Formaldehyde_Catalyst;
	public static ItemStack Formaldehyde_Catalyst_Stack;
	
	public RocketFuels() {
		super(true);
	}

	public static void createKerosene(){

		FluidStack fuelA = FluidUtils.getFluidStack("diesel", 3000);
		FluidStack fuelB = FluidUtils.getFluidStack("fuel", 3000);

		if (fuelA != null){
			//GT_Values.RA.addDistilleryRecipe(23, fuelA, FluidUtils.getFluidStack(Kerosene, 50), 200, 64, false);
			GT_Values.RA.addDistilleryRecipe(CI.getNumberedCircuit(23), fuelA, FluidUtils.getFluidStack(Kerosene, 1800), 200, 64, false);
		}
		if (fuelA == null && fuelB != null){
			//GT_Values.RA.addDistilleryRecipe(23, fuelB, FluidUtils.getFluidStack(Kerosene, 50), 200, 64, false);
			GT_Values.RA.addDistilleryRecipe(CI.getNumberedCircuit(23), fuelB, FluidUtils.getFluidStack(Kerosene, 1800), 200, 64, false);
		}
	}

	public static void createRP1(){
		FluidStack fuelA = FluidUtils.getFluidStack(Kerosene, 1000);
		if (fuelA != null){
			GT_Values.RA.addDistilleryRecipe(CI.getNumberedCircuit(23), fuelA, FluidUtils.getFluidStack(RP1, 750), 20 * 40, 120, false);
		}
	}

	public static void createNitrogenTetroxide(){	
		/*CORE.RA.addDehydratorRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 4)
				}, 
				FluidUtils.getFluidStack("nitricacid", 2000), 
				FluidUtils.getFluidStack(Nitrogen_Tetroxide, 450),
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 1),	
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 1)						
				},
				new int[]{100, 50}, 
				20*16, 
				500);*/
		
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 16),
						ItemUtils.getSimpleStack(GenericChem.mOrangeCatalyst, 0),
				},
				new FluidStack[] {
						FluidUtils.getFluidStack("nitricacid", 4000)
				},
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 1)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(Nitrogen_Tetroxide, 3000),
				},
				new int[]{100, 100, 50, 50}, 
				20*30, 
				MaterialUtils.getVoltageForTier(3),
				2);

		
	}

	public static void createHydrazine(){		

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(21)
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("fluid.hydrogenperoxide", 2000),
						FluidUtils.getFluidStack("ammonia", 2000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Hydrazine, 4000),

				},
				20 * 30, 
				MaterialUtils.getVoltageForTier(2), 
				1);

	}


	public static void createMonomethylhydrazine(){

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(21),
						ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 2)
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("hydrogen", 2000),
						FluidUtils.getFluidStack(Hydrazine, 2000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Monomethylhydrazine, 4000),

				},
				20 * 48, 
				240, 
				2);

	}

	private static void createLOX() {		
		GT_Values.RA.addVacuumFreezerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1), ItemUtils.getItemStackOfAmountFromOreDict("cellLiquidOxygen", 1), 20*16);
		CORE.RA.addAdvancedFreezerRecipe(new ItemStack[] {}, new FluidStack[] {FluidUtils.getFluidStack("oxygen", 3000)}, new FluidStack[] {FluidUtils.getFluidStack(Liquid_Oxygen, 3000)}, new ItemStack[] {}, new int[] {}, 20*16, 240, 0);
	}

	private static void createLOH() {		
		GT_Values.RA.addVacuumFreezerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), ItemUtils.getItemStackOfAmountFromOreDict("cellLiquidhydrogen", 1), 20*16);
		CORE.RA.addAdvancedFreezerRecipe(new ItemStack[] {}, new FluidStack[] {FluidUtils.getFluidStack("hydrogen", 300)}, new FluidStack[] {FluidUtils.getFluidStack(Liquid_Hydrogen, 300)}, new ItemStack[] {}, new int[] {}, 20*4, 540, 0);
	}

	private static void createHydratedAmmoniumNitrateSlurry() {		

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedBioCircuit(21),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("ammonia", 4000),
						FluidUtils.getFluidStack("nitricacid", 4000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Hydrated_Ammonium_Nitrate_Slurry, 8000),

				},
				20 * 60, 
				120, 
				1);

	}

	private static void createAmmoniumNitrateDust() {
		CORE.RA.addDehydratorRecipe(
				new ItemStack[] {CI.getNumberedCircuit(8)}, 
				FluidUtils.getFluidStack(Hydrated_Ammonium_Nitrate_Slurry, 8*144), 
				FluidUtils.getWater(2000), 
				new ItemStack[] {ItemUtils.getSimpleStack(Ammonium_Nitrate_Dust, 8)}, 
				new int[] {10000}, 
				90*20,
				480);

	}

	private static void createFormaldehyde() {		

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedBioCircuit(21),
						ItemUtils.getSimpleStack(Formaldehyde_Catalyst, 0),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("oxygen", 16000),
						FluidUtils.getFluidStack("methanol", 32000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Formaldehyde, 8000),

				},
				20 * 90, 
				120, 
				1);

	}

	private static void createFormaldehydeCatalyst() {
		GT_Values.RA.addMixerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 16),
				ItemUtils.getItemStackOfAmountFromOreDict("dustVanadium", 1),
				CI.getNumberedCircuit(18),
				null,
				null,
				null,
				ItemUtils.getSimpleStack(Formaldehyde_Catalyst, 4), 
				160, 
				30);

	}

	private static void createUnsymmetricalDimethylhydrazine() {		

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedBioCircuit(21),
						ItemUtils.getSimpleStack(Formaldehyde_Catalyst, 0),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack("fluid.hydrazine", 2000),
						FluidUtils.getFluidStack(Formaldehyde, 2000),
						FluidUtils.getFluidStack("hydrogen", 4000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine, 2000),
						FluidUtils.getWater(2000)

				},
				20 * 60, 
				120, 
				2);

	}

	private static void addRocketFuelsToMap() {		
		HashMap<Integer, Recipe_GT> mRocketFuels = new LinkedHashMap<Integer, Recipe_GT>();
		mRocketFuels.put(0, new Recipe_GT(
				true,
				new ItemStack[] {},
				new ItemStack[] {},
				null,
				new int[] {},
				new FluidStack[] {FluidUtils.getFluidStack(RP1_Plus_Liquid_Oxygen, 1000)},
				new FluidStack[] {},
				0,
				0,
				256)); //Fuel Value

		mRocketFuels.put(1, new Recipe_GT(
				true,
				new ItemStack[] {},
				new ItemStack[] {},
				null,
				new int[] {},
				new FluidStack[] {FluidUtils.getFluidStack(Dense_Hydrazine_Mix, 1000)},
				new FluidStack[] {},
				0,
				0,
				512)); //Fuel Value

		mRocketFuels.put(2, new Recipe_GT(
				true,
				new ItemStack[] {},
				new ItemStack[] {},
				null,
				new int[] {},
				new FluidStack[] {FluidUtils.getFluidStack(Monomethylhydrazine_Plus_Nitric_Acid, 1000)},
				new FluidStack[] {},
				0,
				0,
				768)); //Fuel Value

		mRocketFuels.put(3, new Recipe_GT(
				true,
				new ItemStack[] {},
				new ItemStack[] {},
				null,
				new int[] {},
				new FluidStack[] {FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 1000)},
				new FluidStack[] {},
				0,
				0,
				1024)); //Fuel Value


		//Add in default Diesel for the Buggy
		mValidRocketFuels.put(-1, Diesel);	

		mValidRocketFuelNames.add(FluidRegistry.getFluidName(Diesel));
		for (int mID : mRocketFuels.keySet()) {
			Recipe_GT aFuelRecipe = mRocketFuels.get(mID);
			if (aFuelRecipe != null) {
				mValidRocketFuelNames.add(FluidRegistry.getFluidName(aFuelRecipe.mFluidInputs[0].getFluid()));
				mValidRocketFuels.put(mID, aFuelRecipe.mFluidInputs[0].getFluid());
				Recipe_GT.Gregtech_Recipe_Map.sRocketFuels.add(aFuelRecipe);		
			}
		}

	}


	private static void createRocketFuels() {

		// RP1_Plus_Liquid_Oxygen
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(1),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Liquid_Oxygen, 2000),
						FluidUtils.getFluidStack(RP1, 500),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(RP1_Plus_Liquid_Oxygen, 1500),

				},
				20 * 15, 
				240, 
				3);		


		// Dense_Hydrazine_Mix
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(2),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Hydrazine, 4000),
						FluidUtils.getFluidStack("methanol", 6000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Dense_Hydrazine_Mix, 10000),

				},
				20 * 30, 
				240, 
				3);	


		// Monomethylhydrazine_Plus_Nitric_Acid
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(3),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Monomethylhydrazine, 1000),
						FluidUtils.getFluidStack("nitricacid", 1000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Monomethylhydrazine_Plus_Nitric_Acid, 3000),

				},
				20 * 45, 
				480, 
				3);


		// Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedCircuit(4),
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine, 2000),
						FluidUtils.getFluidStack(Nitrogen_Tetroxide, 2000),
				}, 
				new ItemStack[] {

				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 4000),

				},
				20 * 60, 
				480, 
				3);	

	}

	@Override
	public String errorMessage() {
		return "Bad Rocket Fuel Science!";
	}

	@Override
	public boolean generateRecipes() {
		createKerosene();	
		createRP1();
		createNitrogenTetroxide();
		createHydrazine();
		createMonomethylhydrazine();

		if (!CORE.GTNH) {
			createLOX();
		}
		createLOH();


		createHydratedAmmoniumNitrateSlurry();		
		createAmmoniumNitrateDust();		
		createFormaldehyde();
		createFormaldehydeCatalyst();		
		createUnsymmetricalDimethylhydrazine();

		createRocketFuels();
		addRocketFuelsToMap();

		return true;
	}

	@Override
	public void items() {
		Formaldehyde_Catalyst = ItemUtils.generateSpecialUseDusts("FormaldehydeCatalyst", "Formaldehyde Catalyst", "Fe16V1", Utils.rgbtoHexValue(25, 5, 25))[0];
		Formaldehyde_Catalyst_Stack = ItemUtils.getSimpleStack(Formaldehyde_Catalyst);
	}

	@Override
	public void blocks() {
	}

	@Override
	public void fluids() {

		//Register default fluids
		Diesel = MaterialUtils.getMaterial("Fuel", "Diesel").getFluid(1).getFluid();

		// 5.08 Compat
		if (!FluidUtils.doesFluidExist("liquid_heavy_oil")){
			Oil_Heavy = FluidUtils.generateFluidNoPrefix("liquid_heavy_oil", "Heavy Oil", 200, new short[]{10, 10, 10, 100});
		}
		else {
			Oil_Heavy = MaterialUtils.getMaterial("OilHeavy", "Oil").getFluid(1).getFluid();
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellOilHeavy", 1) == null){				
				new BaseItemComponent("OilHeavy", "Heavy Oil", new short[] {10, 10, 10});
			}
		}		


		//Create Kerosene
		Kerosene = FluidUtils.generateFluidNonMolten("Kerosene", "Kerosene", 500, new short[]{150, 40, 150, 100}, null, null);
		CoalTar.Coal_Oil = Kerosene;

		//RP! Focket Fuel
		RP1 = FluidUtils.generateFluidNonMolten("RP1Fuel", "RP-1", 500, new short[]{210, 50, 50, 100}, null, null);

		//Create Nitrogen Tetroxide
		Nitrogen_Tetroxide = FluidUtils.generateFluidNonMolten("NitrogenTetroxide", "Nitrogen Tetroxide", -11, new short[]{170, 170, 0, 100}, null, null);

		//Create Hydrazine
		Hydrazine = FluidUtils.generateFluidNonMolten("Hydrazine", "Hydrazine", 2, new short[]{250, 250, 250, 100}, null, null);

		//Create Monomethylhydrazine
		Monomethylhydrazine = FluidUtils.generateFluidNonMolten("Monomethylhydrazine", "Monomethylhydrazine", -52, new short[]{125, 125, 125, 100}, null, null);

		//Create Anthracene
		Nitrous_Oxide = FluidUtils.generateFluidNonMolten("NitrousOxide", "Nitrous Oxide", -91, new short[]{255, 255, 255, 100}, null, null);

		//Nos
		if (!FluidUtils.doesFluidExist("NitrousOxide")){
			Nitrous_Oxide = FluidUtils.generateFluidNoPrefix("NitrousOxide", "Nitrous Oxide", -91, new short[]{255, 255, 255, 100});
		}
		else {
			Nitrous_Oxide = FluidUtils.getWildcardFluidStack("NitrousOxide", 1).getFluid();
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellNitrousOxide", 1) == null){				
				new BaseItemComponent("NitrousOxide", "Nitrous Oxide", new short[] {10, 10, 175});
			}
		}

		//Unsymmetrical_Dimethylhydrazine		
		if (FluidUtils.getFluidStack("1,1dimethylhydrazine", 1) == null){
			Unsymmetrical_Dimethylhydrazine = FluidUtils.generateFluidNonMolten("UnsymmetricalDimethylhydrazine", "Unsymmetrical Dimethylhydrazine", -57, new short[]{70, 210, 20, 100}, null, null);
		}
		else {
			Unsymmetrical_Dimethylhydrazine = FluidUtils.getFluidStack("1,1dimethylhydrazine", 1000).getFluid();
		}

		//Create Hydrated_Ammonium_Nitrate_Slurry
		Hydrated_Ammonium_Nitrate_Slurry = FluidUtils.generateFluidNonMolten("AmmoniumNitrateSlurry", "Hydrated Ammonium Nitrate Slurry", 450, new short[]{150, 75, 150, 100}, null, null);

		//Lithium Hydroperoxide - LiOH + H2O2 → LiOOH + 2 H2O
		Ammonium_Nitrate_Dust = ItemUtils.generateSpecialUseDusts("AmmoniumNitrate", "Ammonium Nitrate", "N2H4O3", Utils.rgbtoHexValue(150, 75, 150))[0];

		//Create Liquid_Oxygen
		if (FluidUtils.getFluidStack("LiquidOxygen", 1) == null && FluidUtils.getFluidStack("liquidoxygen", 1) == null){
			Liquid_Oxygen = FluidUtils.generateFluidNonMolten("LiquidOxygen", "Liquid Oxygen", -240, new short[]{75, 75, 220, 100}, null, null);
		}
		else {
			if (FluidUtils.getFluidStack("LiquidOxygen", 1) != null ) {
				Liquid_Oxygen = FluidUtils.getFluidStack("LiquidOxygen", 1).getFluid();				
			}
			else {
				Liquid_Oxygen = FluidUtils.getFluidStack("liquidoxygen", 1).getFluid();
			}
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellLiquidOxygen", 1) == null){				
				new BaseItemComponent("LiquidOxygen", "Liquid Oxygen", new short[] {10, 10, 175});
			}
		}

		//Create Liquid_Hydrogen
		if (FluidUtils.getFluidStack("LiquidHydrogen", 1) == null && FluidUtils.getFluidStack("liquidhydrogen", 1) == null){
			Liquid_Hydrogen = FluidUtils.generateFluidNonMolten("LiquidHydrogen", "Liquid Hydrogen", -240, new short[]{75, 75, 220, 100}, null, null);
		}
		else {
			if (FluidUtils.getFluidStack("LiquidHydrogen", 1) != null ) {
				Liquid_Hydrogen = FluidUtils.getFluidStack("LiquidHydrogen", 1).getFluid();				
			}
			else {
				Liquid_Hydrogen = FluidUtils.getFluidStack("liquidhydrogen", 1).getFluid();
			}
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellLiquidHydrogen", 1) == null){				
				new BaseItemComponent("LiquidHydrogen", "Liquid Hydrogen", new short[] {10, 10, 175});
			}
		}

		Formaldehyde = FluidUtils.generateFluidNonMolten("Formaldehyde", "Formaldehyde", 185, new short[]{150, 75, 150, 100}, null, null);

		Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide = FluidUtils.generateFluidNonMolten("RocketFuelMixA", "H8N4C2O4 Rocket Fuel", -185, new short[]{50, 220, 50, 100}, null, null);
		RP1_Plus_Liquid_Oxygen = FluidUtils.generateFluidNonMolten("RocketFuelMixB", "Rp-1 Rocket Fuel", -250, new short[]{250, 50, 50, 100}, null, null);
		Monomethylhydrazine_Plus_Nitric_Acid = FluidUtils.generateFluidNonMolten("RocketFuelMixC", "CN3H7O3 Rocket Fuel", -300, new short[]{125, 75, 180, 100}, null, null);
		Dense_Hydrazine_Mix = FluidUtils.generateFluidNonMolten("RocketFuelMixD", "Dense Hydrazine Fuel Mixture", -250, new short[]{175, 80, 120, 100}, null, null);


	}

	@Override
	public boolean onLoadComplete(FMLLoadCompleteEvent event) {

		Logger.INFO("Trying to remove GT recipes for '1,1dimethylhydrazine' && 'rocket_fuel' if they exist.");
		/*if (FluidRegistry.isFluidRegistered("1,1dimethylhydrazine")) {
		
			// Try Remove recipes for GT/EIO Rocket Fuel Cells
			if (FluidRegistry.isFluidRegistered("rocket_fuel")) {
		
				// Old Recipe				
				if (MaterialUtils.doesMaterialExist("NitrogenDioxide")) {
					GT_Recipe aOldRecipe = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellNitrogenDioxide", 1),
									ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 3),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("air", 500)
							}, 
							388);
					boolean aDidRemove = removeRecipe(aOldRecipe, GT_Recipe_Map.sChemicalRecipes);	
					Logger.INFO("Removed Old Recipe for Rocket Fuel: "+aDidRemove);
				}
		
				// Simple Recipes
				if (MaterialUtils.doesMaterialExist("Ammonia") && MaterialUtils.doesMaterialExist("Methanol")) {
		
					GT_Recipe aSimpleRecipe1 = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 3),
									ItemUtils.getItemStackOfAmountFromOreDict("cellMethanol", 4),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("chlorine", 1000)
							}, 
							480);
					GT_Recipe aSimpleRecipe2 = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellChlorine", 1),
									ItemUtils.getItemStackOfAmountFromOreDict("cellMethanol", 4),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("ammonia", 3000)
							}, 
							480);
					GT_Recipe aSimpleRecipe3 = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 3),
									ItemUtils.getItemStackOfAmountFromOreDict("cellChlorine", 1),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("methanol", 4000)
							}, 
							480);
		
					boolean aDidRemove1 = removeRecipe(aSimpleRecipe1, GT_Recipe_Map.sChemicalRecipes);	
					boolean aDidRemove2 = removeRecipe(aSimpleRecipe2, GT_Recipe_Map.sChemicalRecipes);	
					boolean aDidRemove3 = removeRecipe(aSimpleRecipe3, GT_Recipe_Map.sChemicalRecipes);	
					Logger.INFO("Removed Simple Recipe 1 for Rocket Fuel: "+aDidRemove1);				
					Logger.INFO("Removed Simple Recipe 2 for Rocket Fuel: "+aDidRemove2);				
					Logger.INFO("Removed Simple Recipe 3 for Rocket Fuel: "+aDidRemove3);					
				}
		
		
				// Complex Recipes I
				if (MaterialUtils.doesMaterialExist("Dimethylhydrazine") && MaterialUtils.doesMaterialExist("DinitrogenTetroxide")) {
		
					GT_Recipe aAdvRecipe1 = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylhydrazine", 1),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("dinitrogentetroxide", 1000)
							}, 
							16);
					GT_Recipe aAdvRecipe2 = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellDinitrogenTetroxide", 1),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("dimethylhydrazine", 1000)
							}, 
							16);
		
					GT_Recipe aAdvRecipe3 = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylhydrazine", 2),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("oxygen", 1000)
							}, 
							16);
					GT_Recipe aAdvRecipe4 = getHalfBakedRecipe(
							new ItemStack[] {
									ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
							}, 
							new FluidStack[] {
									FluidUtils.getFluidStack("dimethylhydrazine", 2000)
							}, 
							16);
		
					boolean aDidRemove1 = removeRecipe(aAdvRecipe1, GT_Recipe_Map.sMixerRecipes);	
					boolean aDidRemove2 = removeRecipe(aAdvRecipe2, GT_Recipe_Map.sMixerRecipes);	
					boolean aDidRemove3 = removeRecipe(aAdvRecipe3, GT_Recipe_Map.sMixerRecipes);	
					boolean aDidRemove4 = removeRecipe(aAdvRecipe4, GT_Recipe_Map.sMixerRecipes);	
					Logger.INFO("Removed Complex Recipe 1 for Rocket Fuel: "+aDidRemove1);				
					Logger.INFO("Removed Complex Recipe 2 for Rocket Fuel: "+aDidRemove2);				
					Logger.INFO("Removed Complex Recipe 3 for Rocket Fuel: "+aDidRemove3);				
					Logger.INFO("Removed Complex Recipe 4 for Rocket Fuel: "+aDidRemove4);						
				}
			}
		
		
			// Complex Recipes II
			if (MaterialUtils.doesMaterialExist("Dimethylhydrazine")) {
		
				GT_Recipe aAdvRecipe1 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylhydrazine", 2),
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("oxygen", 1000)
						}, 
						16);
				GT_Recipe aAdvRecipe2 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("dimethylhydrazine", 2000)
						}, 
						16);
		
				boolean aDidRemove1 = removeRecipe(aAdvRecipe1, GT_Recipe_Map.sMixerRecipes);	
				boolean aDidRemove2 = removeRecipe(aAdvRecipe2, GT_Recipe_Map.sMixerRecipes);	
				Logger.INFO("Removed Complex Recipe 5 for Rocket Fuel: "+aDidRemove1);				
				Logger.INFO("Removed Complex Recipe 6 for Rocket Fuel: "+aDidRemove2);					
			}			
		
			if (MaterialUtils.doesMaterialExist("Chloramine") && MaterialUtils.doesMaterialExist("Dimethylamine")) {
				GT_Recipe aSimpleRecipe1 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellChloramine", 2),
								GT_Utility.getIntegratedCircuit(1)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("dimethylamine", 5000)
						}, 
						480);
				GT_Recipe aSimpleRecipe2 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylamine", 5),
								GT_Utility.getIntegratedCircuit(1)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("chloramine", 2000)
						}, 
						16);
				
				
				
				GT_Recipe aAdvRecipe1 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellChloramine", 2),
								CI.emptyCells(4)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("dimethylamine", 5000)
						}, 
						480);
				GT_Recipe aAdvRecipe2 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylamine", 5),
								CI.emptyCells(1)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("chloramine", 2000)
						}, 
						16);
				GT_Recipe aAdvRecipe3 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellChloramine", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylamine", 5),
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("chloramine", 2000)
						}, 
						480);				
		
				boolean aDidRemove1 = removeRecipe(aSimpleRecipe1, GT_Recipe_Map.sChemicalRecipes);	
				boolean aDidRemove2 = removeRecipe(aSimpleRecipe2, GT_Recipe_Map.sChemicalRecipes);	
				boolean aDidRemove3 = removeRecipe(aAdvRecipe1, GT_Recipe_Map.sChemicalRecipes);	
				boolean aDidRemove4 = removeRecipe(aAdvRecipe2, GT_Recipe_Map.sChemicalRecipes);	
				boolean aDidRemove5 = removeRecipe(aAdvRecipe3, GT_Recipe_Map.sChemicalRecipes);	
				Logger.INFO("Removed Complex Recipe 1 for 1,1dimethylhydrazine: "+aDidRemove1);				
				Logger.INFO("Removed Complex Recipe 2 for 1,1dimethylhydrazine: "+aDidRemove2);				
				Logger.INFO("Removed Complex Recipe 3 for 1,1dimethylhydrazine: "+aDidRemove3);				
				Logger.INFO("Removed Complex Recipe 4 for 1,1dimethylhydrazine: "+aDidRemove4);				
				Logger.INFO("Removed Complex Recipe 5 for 1,1dimethylhydrazine: "+aDidRemove5);	
				
			}
			
			if (MaterialUtils.doesMaterialExist("Chloramine") && MaterialUtils.doesMaterialExist("Dimethylamine")) {
				GT_Recipe aSimpleRecipe1 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellChloramine", 1),
								GT_Utility.getIntegratedCircuit(1)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("dimethylamine", 1000)
						}, 
						480);
				GT_Recipe aSimpleRecipe2 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylamine", 1),
								GT_Utility.getIntegratedCircuit(1)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("chloramine", 1000)
						}, 
						16);
				
				
				
				GT_Recipe aAdvRecipe1 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellChloramine", 1),
								GT_Utility.getIntegratedCircuit(11)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("dimethylamine", 1000)
						}, 
						480);
				GT_Recipe aAdvRecipe2 = getHalfBakedRecipe(
						new ItemStack[] {
								ItemUtils.getItemStackOfAmountFromOreDict("cellDimethylamine", 1),
								GT_Utility.getIntegratedCircuit(11)
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("chloramine", 1000)
						}, 
						16);				
		
				boolean aDidRemove1 = removeRecipe(aSimpleRecipe1, GT_Recipe_Map.sChemicalRecipes);	
				boolean aDidRemove2 = removeRecipe(aSimpleRecipe2, GT_Recipe_Map.sChemicalRecipes);	
				boolean aDidRemove3 = removeRecipe(aAdvRecipe1, GT_Recipe_Map.sChemicalRecipes);	
				boolean aDidRemove4 = removeRecipe(aAdvRecipe2, GT_Recipe_Map.sChemicalRecipes);	
				Logger.INFO("Removed Complex Recipe 5 for 1,1dimethylhydrazine: "+aDidRemove1);				
				Logger.INFO("Removed Complex Recipe 6 for 1,1dimethylhydrazine: "+aDidRemove2);				
				Logger.INFO("Removed Complex Recipe 7 for 1,1dimethylhydrazine: "+aDidRemove3);				
				Logger.INFO("Removed Complex Recipe 8 for 1,1dimethylhydrazine: "+aDidRemove4);	
				
			}			
		
			if (MaterialUtils.doesMaterialExist("HypochlorousAcid") && MaterialUtils.doesMaterialExist("Ammonia") && MaterialUtils.doesMaterialExist("Methanol")) {
		
				GT_Recipe aAdvRecipe1 = getHalfBakedRecipe(
						new ItemStack[] {
								GT_Utility.getIntegratedCircuit(24),
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("hypochlorousacid", 3000),
								FluidUtils.getFluidStack("ammonia", 8000),
								FluidUtils.getFluidStack("methanol", 12000)
						}, 
						480);	
				
				GT_Recipe aAdvRecipe2 = getHalfBakedRecipe(
						new ItemStack[] {
								GT_Utility.getIntegratedCircuit(24),
						}, 
						new FluidStack[] {
								FluidUtils.getFluidStack("hypochlorousacid", 1000),
								FluidUtils.getFluidStack("ammonia", 1000),
								FluidUtils.getFluidStack("methanol", 2000)
						}, 
						480);
		
				boolean aDidRemove1 = removeRecipe(aAdvRecipe1, StaticFields59.getLargeChemicalReactorRecipeMap());
				boolean aDidRemove2 = removeRecipe(aAdvRecipe2, StaticFields59.getLargeChemicalReactorRecipeMap());
				Logger.INFO("Removed Complex Recipe 9 for 1,1dimethylhydrazine: "+aDidRemove1);			
				Logger.INFO("Removed Complex Recipe 10 for 1,1dimethylhydrazine: "+aDidRemove2);			
				
				
			}
		}*/
		
		// Try Butcher recipes manually
		
		//1,1Dimethylhydrazine
		if (FluidRegistry.isFluidRegistered("1,1dimethylhydrazine")) {
			Logger.INFO("Making sure all Chemical Reactor recipes for 1,1dimethylhydrazine have been removed.");
			AutoMap<GT_Recipe> aToRemoveSingle = new AutoMap<GT_Recipe>();
			ItemStack aUnsymCell = ItemUtils.getItemStackOfAmountFromOreDict("cell1,1Dimethylhydrazine", 1);
			FluidStack aUnsymFluid = FluidUtils.getFluidStack("1,1dimethylhydrazine", 1);
			recipe : for (GT_Recipe aRecipeSingleBlock : GT_Recipe_Map.sChemicalRecipes.mRecipeList) {
				if (aRecipeSingleBlock != null && aRecipeSingleBlock.mEnabled) {
					if (aRecipeSingleBlock.mOutputs != null && aRecipeSingleBlock.mOutputs.length > 0) {
						for (ItemStack aOutputItem : aRecipeSingleBlock.mOutputs) {
							ItemStack aTemp = aOutputItem.copy();
							aTemp.stackSize = 1;
							if (GT_Utility.areStacksEqual(aTemp, aUnsymCell)) {
								aToRemoveSingle.add(aRecipeSingleBlock);
								continue recipe;
							}
						}
					}
					if (aRecipeSingleBlock.mFluidOutputs != null && aRecipeSingleBlock.mFluidOutputs.length > 0) {
						for (FluidStack aOutput : aRecipeSingleBlock.mFluidOutputs) {
							if (GT_Utility.areFluidsEqual(aOutput, aUnsymFluid)) {
								aToRemoveSingle.add(aRecipeSingleBlock);
								continue recipe;
							}
						}
					}
				}
			}
			// Handle Multi Also
			AutoMap<GT_Recipe> aToRemoveMulti = new AutoMap<GT_Recipe>();
			recipe : for (GT_Recipe aRecipeSingleBlock : StaticFields59.getLargeChemicalReactorRecipeMap().mRecipeList) {
				if (aRecipeSingleBlock != null && aRecipeSingleBlock.mEnabled) {
					if (aRecipeSingleBlock.mOutputs != null && aRecipeSingleBlock.mOutputs.length > 0) {
						for (ItemStack aOutputItem : aRecipeSingleBlock.mOutputs) {
							ItemStack aTemp = aOutputItem.copy();
							aTemp.stackSize = 1;
							if (GT_Utility.areStacksEqual(aTemp, aUnsymCell)) {
								aToRemoveMulti.add(aRecipeSingleBlock);
								continue recipe;
							}
						}
					}
					if (aRecipeSingleBlock.mFluidOutputs != null && aRecipeSingleBlock.mFluidOutputs.length > 0) {
						for (FluidStack aOutput : aRecipeSingleBlock.mFluidOutputs) {
							if (GT_Utility.areFluidsEqual(aOutput, aUnsymFluid)) {
								aToRemoveMulti.add(aRecipeSingleBlock);
								continue recipe;
							}
						}
					}
				}
			}
			if (!aToRemoveSingle.isEmpty()) {
				Logger.INFO("Found "+aToRemoveSingle.size()+" single block recipes, removing by force.");
				for (GT_Recipe remove : aToRemoveSingle) {
					GT_Recipe_Map.sChemicalRecipes.mRecipeList.remove(remove);
				}
			}
			if (!aToRemoveMulti.isEmpty()) {
				Logger.INFO("Found "+aToRemoveSingle.size()+" multiblock recipes, removing by force.");
				for (GT_Recipe remove : aToRemoveMulti) {
					StaticFields59.getLargeChemicalReactorRecipeMap().mRecipeList.remove(remove);
				}
			}
		}
		

		if (FluidRegistry.isFluidRegistered("rocket_fuel")) {
			Logger.INFO("Making sure all Mixer recipes for rocket_fuel have been removed.");
			AutoMap<GT_Recipe> aToRemoveSingle = new AutoMap<GT_Recipe>();
			FluidStack aRocketFluid = FluidUtils.getFluidStack("rocket_fuel", 1);
			recipe : for (GT_Recipe aRecipeSingleBlock : GT_Recipe_Map.sMixerRecipes.mRecipeList) {
				if (aRecipeSingleBlock != null && aRecipeSingleBlock.mEnabled) {					
					if (aRecipeSingleBlock.mFluidOutputs != null && aRecipeSingleBlock.mFluidOutputs.length > 0) {
						for (FluidStack aOutput : aRecipeSingleBlock.mFluidOutputs) {
							if (GT_Utility.areFluidsEqual(aOutput, aRocketFluid)) {
								aToRemoveSingle.add(aRecipeSingleBlock);
								continue recipe;
							}
						}
					}
				}
			}
			if (!aToRemoveSingle.isEmpty()) {
				Logger.INFO("Found "+aToRemoveSingle.size()+" recipes, removing by force.");
				for (GT_Recipe remove : aToRemoveSingle) {
					GT_Recipe_Map.sMixerRecipes.mRecipeList.remove(remove);
				}
				aToRemoveSingle.clear();
			}
			
			Logger.INFO("Making sure all Chemical Reactor recipes for rocket_fuel have been removed.");
			recipe : for (GT_Recipe aRecipeSingleBlock : GT_Recipe_Map.sChemicalRecipes.mRecipeList) {
				if (aRecipeSingleBlock != null && aRecipeSingleBlock.mEnabled) {					
					if (aRecipeSingleBlock.mFluidOutputs != null && aRecipeSingleBlock.mFluidOutputs.length > 0) {
						for (FluidStack aOutput : aRecipeSingleBlock.mFluidOutputs) {
							if (GT_Utility.areFluidsEqual(aOutput, aRocketFluid)) {
								aToRemoveSingle.add(aRecipeSingleBlock);
								continue recipe;
							}
						}
					}
				}
			}
			// Handle Multi Also
			AutoMap<GT_Recipe> aToRemoveMulti = new AutoMap<GT_Recipe>();
			recipe : for (GT_Recipe aRecipeSingleBlock : StaticFields59.getLargeChemicalReactorRecipeMap().mRecipeList) {
				if (aRecipeSingleBlock != null && aRecipeSingleBlock.mEnabled) {
					if (aRecipeSingleBlock.mFluidOutputs != null && aRecipeSingleBlock.mFluidOutputs.length > 0) {
						for (FluidStack aOutput : aRecipeSingleBlock.mFluidOutputs) {
							if (GT_Utility.areFluidsEqual(aOutput, aRocketFluid)) {
								aToRemoveMulti.add(aRecipeSingleBlock);
								continue recipe;
							}
						}
					}
				}
			}
			if (!aToRemoveSingle.isEmpty()) {
				Logger.INFO("Found "+aToRemoveSingle.size()+" single block recipes, removing by force.");
				for (GT_Recipe remove : aToRemoveSingle) {
					GT_Recipe_Map.sChemicalRecipes.mRecipeList.remove(remove);
				}
			}
			if (!aToRemoveMulti.isEmpty()) {
				Logger.INFO("Found "+aToRemoveSingle.size()+" multiblock recipes, removing by force.");
				for (GT_Recipe remove : aToRemoveMulti) {
					StaticFields59.getLargeChemicalReactorRecipeMap().mRecipeList.remove(remove);
				}
			}
			
		}
		
		
		
		Logger.INFO("Finished clean-up of GT Rocket Fuel Recipes.");
		return true;
	}

	private static Recipe_GT getHalfBakedRecipe(ItemStack[] aInputs, FluidStack[] aFluidInputs, int aEU) {
		return new Recipe_GT(false,	aInputs, null, null, null, aFluidInputs, null, 0, aEU, 0);

	}

	public static boolean removeRecipe(GT_Recipe aRecipe, GT_Recipe_Map aMap) {	
		if (aMap != GT_Recipe_Map.sChemicalRecipes) {
			GT_Recipe aFoundRecipe = aMap.findRecipe(null, false, true, aRecipe.mEUt, aRecipe.mFluidInputs, aRecipe.mInputs);
			boolean aSingle = false;
			if (aFoundRecipe != null) {
				Logger.INFO("Found Single Block Recipe, removing.");
				aSingle = aMap.mRecipeList.remove(aFoundRecipe);
				Logger.INFO("Success? "+aSingle);
			}
			return aSingle;
		}
		else {
			GT_Recipe aFoundRecipe = aMap.findRecipe(null, false, true, aRecipe.mEUt, aRecipe.mFluidInputs, aRecipe.mInputs);
			GT_Recipe aFoundRecipe2 = StaticFields59.getLargeChemicalReactorRecipeMap().findRecipe(null, false, true, aRecipe.mEUt, aRecipe.mFluidInputs, aRecipe.mInputs);
			boolean aSingle = false;
			boolean aMulti = false;
			if (aFoundRecipe != null) {
				Logger.INFO("Found Single Block Recipe, removing.");
				aSingle = GT_Recipe_Map.sChemicalRecipes.mRecipeList.remove(aFoundRecipe);
				Logger.INFO("Success? "+aSingle);
			}
			if (aFoundRecipe2 != null) {
				Logger.INFO("Found Multiblock Recipe, removing.");
				aMulti = StaticFields59.getLargeChemicalReactorRecipeMap().mRecipeList.remove(aFoundRecipe2);
				Logger.INFO("Success? "+aSingle);
			}
			return aSingle && aMulti;
		}
	}



}
