package gtPlusPlus.core.item.chemistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class RocketFuels {
	
	public static HashSet<String> mValidRocketFuelNames = new HashSet<String>();
	public static HashMap<Integer, Fluid> mValidRocketFuels = new HashMap<Integer, Fluid>();

	public static Fluid Kerosene;	
	public static Fluid RP1;	
	public static Fluid Nitrogen_Tetroxide;	
	public static Fluid Hydrazine;	
	public static Fluid Monomethylhydrazine;	
	public static Fluid Unsymmetrical_Dimethylhydrazine;	
	public static Fluid Nitrous_Oxide;		
	public static Fluid Hydrated_Ammonium_Nitrate_Slurry;	
	public static Fluid Liquid_Oxygen;
	public static Fluid Formaldehyde;


	//Rocket Fuel Mixs
	public static Fluid Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide;
	public static Fluid RP1_Plus_Liquid_Oxygen;
	public static Fluid Dense_Hydrazine_Mix;
	public static Fluid Monomethylhydrazine_Plus_Nitric_Acid;

	public static Item Ammonium_Nitrate_Dust;
	public static Item Formaldehyde_Catalyst;

	public static void run(){

		//Create Kerosene
		Kerosene = FluidUtils.generateFluidNonMolten("Kerosene", "Kerosene", 500, new short[]{150, 40, 150, 100}, null, null);

		//RP! Focket Fuel
		RP1 = FluidUtils.generateFluidNonMolten("RP1Fuel", "RP-1 Rocket Fuel", 500, new short[]{210, 50, 50, 100}, null, null);

		//Create Nitrogen Tetroxide
		Nitrogen_Tetroxide = FluidUtils.generateFluidNonMolten("NitrogenTetroxide", "Nitrogen Tetroxide", -11, new short[]{170, 170, 0, 100}, null, null);

		//Create Hydrazine
		Hydrazine = FluidUtils.generateFluidNonMolten("Hydrazine", "Hydrazine", 2, new short[]{250, 250, 250, 100}, null, null);

		//Create Monomethylhydrazine
		Monomethylhydrazine = FluidUtils.generateFluidNonMolten("Monomethylhydrazine", "Monomethylhydrazine", -52, new short[]{125, 125, 125, 100}, null, null);

		//Create Anthracene
		Nitrous_Oxide = FluidUtils.generateFluidNonMolten("NitrousOxide", "Nitrous Oxide", -91, new short[]{255, 255, 255, 100}, null, null);

		//Nos
		if (FluidUtils.getFluidStack("NitrousOxide", 1) == null){
			Nitrous_Oxide = FluidUtils.generateFluidNonMolten("NitrousOxide", "Nitrous Oxide", -91, new short[]{255, 255, 255, 100}, null, null);
		}
		else {
			Nitrous_Oxide = FluidUtils.getFluidStack("NitrousOxide", 1000).getFluid();
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

		Formaldehyde = FluidUtils.generateFluidNonMolten("Formaldehyde", "Formaldehyde", -92, new short[]{150, 75, 150, 100}, null, null);
		Formaldehyde_Catalyst = ItemUtils.generateSpecialUseDusts("FormaldehydeCatalyst", "Formaldehyde Catalyst", "Fe16V1", Utils.rgbtoHexValue(25, 5, 25))[0];


		Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide = FluidUtils.generateFluidNonMolten("RocketFuelMixA", "H8N4C2O4 Rocket Fuel", -185, new short[]{50, 220, 50, 100}, null, null);
		RP1_Plus_Liquid_Oxygen = FluidUtils.generateFluidNonMolten("RocketFuelMixB", "Rp-1 Fuel Mixture", -250, new short[]{250, 50, 50, 100}, null, null);
		Monomethylhydrazine_Plus_Nitric_Acid = FluidUtils.generateFluidNonMolten("RocketFuelMixC", "CN3H7O3 Rocket Fuel", -300, new short[]{125, 75, 180, 100}, null, null);
		Dense_Hydrazine_Mix = FluidUtils.generateFluidNonMolten("RocketFuelMixD", "Dense Hydrazine Fuel Mixture", -250, new short[]{175, 80, 120, 100}, null, null);



		createRecipes();


	}

	private static void createRecipes() {
		createKerosene();	
		createRP1();
		createNitrogenTetroxide();
		createHydrazine();
		createMonomethylhydrazine();

		if (!CORE.GTNH) {
			createLOX();
		}


		createHydratedAmmoniumNitrateSlurry();		
		createAmmoniumNitrateDust();		
		createFormaldehyde();
		createFormaldehydeCatalyst();		
		createUnsymmetricalDimethylhydrazine();

		createRocketFuels();
		addRocketFuelsToMap();

	}

	public static void createKerosene(){
		FluidStack fuelA = FluidUtils.getFluidStack("diesel", 400);
		FluidStack fuelB = FluidUtils.getFluidStack("fuel", 400);
		if (fuelA != null){
			GT_Values.RA.addDistilleryRecipe(23, fuelA, FluidUtils.getFluidStack(Kerosene, 50), 200, 64, false);
		}
		if (fuelA ==  null && fuelB != null){
			GT_Values.RA.addDistilleryRecipe(23, fuelB, FluidUtils.getFluidStack(Kerosene, 50), 200, 64, false);
		}
	}

	public static void createRP1(){
		FluidStack fuelA = FluidUtils.getFluidStack(Kerosene, 100);
		if (fuelA != null){
			GT_Values.RA.addDistilleryRecipe(23, fuelA, FluidUtils.getFluidStack(RP1, 25), 400, 120, false);
		}
	}

	public static void createNitrogenTetroxide(){	
		CORE.RA.addDehydratorRecipe(
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
				500);
	}

	public static void createHydrazine(){
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 2), 
				CI.getNumberedCircuit(23), 
				FluidUtils.getFluidStack("fluid.hydrogenperoxide", 2000),
				FluidUtils.getFluidStack(Hydrazine, 2000),
				ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2), 
				20*32);

		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 2), 
				CI.getNumberedCircuit(23), 
				FluidUtils.getFluidStack("ammonia", 2000),
				FluidUtils.getFluidStack(Hydrazine, 2000),
				ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2), 
				20*32);
	}


	public static void createMonomethylhydrazine(){
		CORE.RA.addDehydratorRecipe(
				new ItemStack[] {
						ItemUtils.getItemStackOfAmountFromOreDict("cellHydrazine", 2),
						ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 2)
				}, 
				FluidUtils.getFluidStack("hydrogen", 2000),
				FluidUtils.getFluidStack(Monomethylhydrazine, 3000),
				new ItemStack[] {
						CI.emptyCells(4)
				},
				new int[] {10000},
				20*48,
				240);

	}

	private static void createLOX() {		
		GT_Values.RA.addVacuumFreezerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1), ItemUtils.getItemStackOfAmountFromOreDict("cellLiquidOxygen", 1), 20*16);
		CORE.RA.addAdvancedFreezerRecipe(new ItemStack[] {}, new FluidStack[] {FluidUtils.getFluidStack("oxygen", 3000)}, new FluidStack[] {FluidUtils.getFluidStack(Liquid_Oxygen, 3000)}, new ItemStack[] {}, new int[] {}, 20*16, 240, 0);

	}

	private static void createHydratedAmmoniumNitrateSlurry() {
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 8),
				ItemUtils.getItemStackOfAmountFromOreDict("cellNitricAcid", 8),
				null,
				FluidUtils.getFluidStack(Hydrated_Ammonium_Nitrate_Slurry, 22*144),
				null,
				48*16);

	}

	private static void createAmmoniumNitrateDust() {
		CORE.RA.addDehydratorRecipe(
				new ItemStack[] {CI.getNumberedCircuit(8)}, 
				FluidUtils.getFluidStack(Hydrated_Ammonium_Nitrate_Slurry, 8*144), 
				FluidUtils.getWater(2000), 
				new ItemStack[] {ItemUtils.getSimpleStack(Ammonium_Nitrate_Dust, 8)}, 
				new int[] {10000}, 
				90*20,
				500);

	}

	private static void createFormaldehyde() {
		CORE.RA.addDehydratorRecipe(
				new ItemStack[] {
						ItemUtils.getSimpleStack(Formaldehyde_Catalyst, 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 16)
				}, 
				FluidUtils.getFluidStack("methanol", 32000),
				FluidUtils.getFluidStack(Formaldehyde, 8000),
				new ItemStack[] {ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 16)},
				new int[] {10000}, 
				90*20,
				120);
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
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrazine", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("cellFormaldehyde", 2),
				FluidUtils.getFluidStack("hydrogen", 4000),
				FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine, 1000),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2),
				20*60);

	}

	private static void addRocketFuelsToMap() {	
		AutoMap<Recipe_GT> mRocketFuels = new AutoMap<Recipe_GT>();
		mRocketFuels.put(new Recipe_GT(
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
		
		mRocketFuels.put(new Recipe_GT(
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
		
		mRocketFuels.put(new Recipe_GT(
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
		
		mRocketFuels.put(new Recipe_GT(
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
		
		int mID = 0;
		for (Recipe_GT r : mRocketFuels) {
			if (r != null) {
				mValidRocketFuelNames.add(r.mFluidInputs[0].getFluid().getName());
				mValidRocketFuels.put(mID++, r.mFluidInputs[0].getFluid());
				Recipe_GT.Gregtech_Recipe_Map.sRocketFuels.add(r);				
			}
		}

	}


	private static void createRocketFuels() {

		//Done
		GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedCircuit(23),
				ItemUtils.getItemStackOfAmountFromOreDict("cellLiquidOxygen", 2),
				FluidUtils.getFluidStack(RP1, 500), 
				FluidUtils.getFluidStack(RP1_Plus_Liquid_Oxygen, 100), 
				CI.emptyCells(2),
				null, 
				null, 
				null,
				null, 
				null, 
				new int[] {10000}, 
				20*32,				
				480);	

		GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedCircuit(23),
				ItemUtils.getItemStackOfAmountFromOreDict("cellRP1Fuel", 1),
				FluidUtils.getFluidStack(Liquid_Oxygen, 4000), 
				FluidUtils.getFluidStack(RP1_Plus_Liquid_Oxygen, 200), 
				CI.emptyCells(1),
				null, 
				null, 
				null,
				null, 
				null, 
				new int[] {10000}, 
				20*64,
				480);	

		GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedCircuit(23),
				ItemUtils.getItemStackOfAmountFromOreDict("cellNitrogenTetroxide", 2),
				FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine, 2000), 
				FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 1750), 
				CI.emptyCells(2),
				null, 
				null, 
				null,
				null, 
				null, 
				new int[] {10000}, 
				20*48,
				480);	

		ItemStack aCell11dimethylhydrazine = ItemUtils.getItemStackOfAmountFromOreDict("cell1,1Dimethylhydrazine", 2);
		if (aCell11dimethylhydrazine != null && aCell11dimethylhydrazine.getItem() != ModItems.AAA_Broken) {
			GT_Values.RA.addCentrifugeRecipe(
					CI.getNumberedCircuit(23),
					aCell11dimethylhydrazine,
					FluidUtils.getFluidStack(Nitrogen_Tetroxide, 2000), 
					FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 1750), 
					CI.emptyCells(2),
					null, 
					null, 
					null,
					null, 
					null, 
					new int[] {10000}, 
					20*48,
					480);	
		}
		else {
			GT_Values.RA.addCentrifugeRecipe(
					CI.getNumberedCircuit(23),
					ItemUtils.getItemStackOfAmountFromOreDict("cellUnsymmetricalDimethylhydrazine", 2),
					FluidUtils.getFluidStack(Nitrogen_Tetroxide, 2000), 
					FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide, 1750), 
					CI.emptyCells(2),
					null, 
					null, 
					null,
					null, 
					null, 
					new int[] {10000}, 
					20*48,
					480);	
		}
		

		GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedCircuit(23),
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrazine", 4),
				FluidUtils.getFluidStack("methanol", 6000), 
				FluidUtils.getFluidStack(Dense_Hydrazine_Mix, 10000), 
				CI.emptyCells(4),
				null, 
				null, 
				null,
				null, 
				null, 
				new int[] {10000}, 
				20*100,
				240);	

		GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedCircuit(23),
				ItemUtils.getItemStackOfAmountFromOreDict("cellMethanol", 6),
				FluidUtils.getFluidStack(Hydrazine, 4000), 
				FluidUtils.getFluidStack(Dense_Hydrazine_Mix, 10000), 
				CI.emptyCells(6),
				null, 
				null, 
				null,
				null, 
				null, 
				new int[] {10000}, 
				20*100,
				240);	

		GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedCircuit(23),
				ItemUtils.getItemStackOfAmountFromOreDict("cellNitricAcid", 1),
				FluidUtils.getFluidStack(Monomethylhydrazine, 1000), 
				FluidUtils.getFluidStack(Monomethylhydrazine_Plus_Nitric_Acid, 2000), 
				CI.emptyCells(1),
				null, 
				null, 
				null,
				null, 
				null, 
				new int[] {10000}, 
				20*32,
				240);

		GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedCircuit(23),
				ItemUtils.getItemStackOfAmountFromOreDict("cellMonomethylhydrazine", 1),
				FluidUtils.getFluidStack("nitricacid", 1000), 
				FluidUtils.getFluidStack(Monomethylhydrazine_Plus_Nitric_Acid, 2000), 
				CI.emptyCells(1),
				null, 
				null, 
				null,
				null, 
				null, 
				new int[] {10000}, 
				20*32,
				240);

	}



}
