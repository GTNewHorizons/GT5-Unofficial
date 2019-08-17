package gtPlusPlus.core.item.chemistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
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
	
	public static void createKerosene(){
		
		
		
		FluidStack fuelA = FluidUtils.getFluidStack("diesel", 300);
		FluidStack fuelB = FluidUtils.getFluidStack("fuel", 300);
		
		
		
		if (fuelA != null){
			//GT_Values.RA.addDistilleryRecipe(23, fuelA, FluidUtils.getFluidStack(Kerosene, 50), 200, 64, false);
			GT_Values.RA.addDistilleryRecipe(CI.getNumberedCircuit(23), fuelA, FluidUtils.getFluidStack(Kerosene, 100), 200, 64, false);
		}
		if (fuelA == null && fuelB != null){
			//GT_Values.RA.addDistilleryRecipe(23, fuelB, FluidUtils.getFluidStack(Kerosene, 50), 200, 64, false);
			GT_Values.RA.addDistilleryRecipe(CI.getNumberedCircuit(23), fuelB, FluidUtils.getFluidStack(Kerosene, 100), 200, 64, false);
		}
	}

	public static void createRP1(){
		FluidStack fuelA = FluidUtils.getFluidStack(Kerosene, 100);
		if (fuelA != null){
			//GT_Values.RA.addDistilleryRecipe(23, fuelA, FluidUtils.getFluidStack(RP1, 25), 400, 120, false);
			GT_Values.RA.addDistilleryRecipe(CI.getNumberedCircuit(23), fuelA, FluidUtils.getFluidStack(RP1, 50), 400, 120, false);
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
		CORE.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrazine", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("cellFormaldehyde", 2),
				FluidUtils.getFluidStack("hydrogen", 4000),
				FluidUtils.getFluidStack(Unsymmetrical_Dimethylhydrazine, 1000),
				ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
				20*60);

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
		if (ItemUtils.checkForInvalidItems(aCell11dimethylhydrazine)) {
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

	@Override
	public String errorMessage() {
		// TODO Auto-generated method stub
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
	public boolean onLoadComplete(FMLLoadCompleteEvent event) {	

		
		if (MathUtils.randInt(1, 2) > 0) {
			return false;
		}
		

		Materials aMaterial_Chloramine = MaterialUtils.getMaterial("Chloramine");
		Materials aMaterial_Dimethylamine = MaterialUtils.getMaterial("Dimethylamine");
		Materials aMaterial_DilutedHydrochloricAcid = MaterialUtils.getMaterial("DilutedHydrochloricAcid");
		Materials aMaterial_NitrogenDioxide = MaterialUtils.getMaterial("NitrogenDioxide");
		Materials aMaterial_DinitrogenTetroxide = MaterialUtils.getMaterial("DinitrogenTetroxide");
		Materials aMaterial_Dimethylhydrazine = MaterialUtils.getMaterial("Dimethylhydrazine");
		
		Materials aMaterial_Oxygen = Materials.Oxygen;
		Materials aMaterial_Water = Materials.Water;
		Materials aMaterial_HypochlorousAcid = MaterialUtils.getMaterial("HypochlorousAcid");
		Materials aMaterial_Ammonia = MaterialUtils.getMaterial("Ammonia");
		Materials aMaterial_Methanol = MaterialUtils.getMaterial("Methanol");
		
		if (aMaterial_Chloramine == null || aMaterial_Dimethylamine == null || aMaterial_DilutedHydrochloricAcid == null 
				|| aMaterial_Dimethylhydrazine == null || aMaterial_NitrogenDioxide == null || aMaterial_DinitrogenTetroxide == null 
				|| aMaterial_HypochlorousAcid == null || aMaterial_Ammonia == null || aMaterial_Methanol == null) {
			return false;
		}
		

		ItemStack aCellEmpty = CI.emptyCells(1);
		ItemStack aCellWater = aMaterial_Water.getCells(1);
		ItemStack aCellOxygen = aMaterial_Oxygen.getCells(1);
		ItemStack aCellChloramine = aMaterial_Chloramine.getCells(1);
		ItemStack aCellDimethylamine = aMaterial_Dimethylamine.getCells(1);
		ItemStack aCellDilutedHydrochloricAcid = aMaterial_DilutedHydrochloricAcid.getCells(1);
		ItemStack aCellNitrogenDioxide = aMaterial_NitrogenDioxide.getCells(1);
		ItemStack aCellDinitrogenTetroxide = aMaterial_DinitrogenTetroxide.getCells(1);
		ItemStack aCellDimethylhydrazine = aMaterial_Dimethylhydrazine.getCells(1);
		
		
		
		


		GT_Recipe aChemReactor_1 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special

		GT_Recipe aChemReactor_2 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special
		
		
		
		
		GT_Recipe aChemReactor_Basic_1 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special

		GT_Recipe aChemReactor_Basic_2 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special

		GT_Recipe aChemReactor_Basic_3 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special

		GT_Recipe aChemReactor_Basic_4 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special
		
		
		
		
		
		
		
		
		
		
		
		

		GT_Recipe aChemReactor_Adv_1 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special

		GT_Recipe aChemReactor_Adv_2 = new Recipe_GT(
				true, //Optimise
				new ItemStack[] {}, //I
				new ItemStack[] {}, //O
				null, //Special
				new int[] {}, //Chance
				new FluidStack[] {}, //I
				new FluidStack[] {}, //O
				0,  //Dura
				0,  //Eu
				0); //Special

		
		

		//RecipeUtils.removeGtRecipe(aChemReactor_Basic_1, GT_Recipe.GT_Recipe_Map.sChemicalRecipes);
		//RecipeUtils.removeGtRecipe(aChemReactor_Basic_1, GT_Recipe.GT_Recipe_Map.sChemicalRecipes);
		//RecipeUtils.removeGtRecipe(aChemReactor_Basic_1, GT_Recipe.GT_Recipe_Map.sChemicalRecipes);
		//RecipeUtils.removeGtRecipe(aChemReactor_Basic_1, GT_Recipe.GT_Recipe_Map.sChemicalRecipes);	
		
		
		
		
		

      /*  GT_Values.RA.addChemicalRecipe(                   Materials.Chloramine.getCells(2),    GT_Utility.getIntegratedCircuit(1),  Materials.Dimethylamine.getGas(5000), Materials.Dimethylhydrazine.getFluid(6000),        Materials.DilutedHydrochloricAcid.getCells(1), Materials.Empty.getCells(1), 960, 480);        
        GT_Values.RA.addChemicalRecipe(                   Materials.Dimethylamine.getCells(5), GT_Utility.getIntegratedCircuit(1),  Materials.Chloramine.getFluid(2000),  Materials.Dimethylhydrazine.getFluid(6000),        Materials.DilutedHydrochloricAcid.getCells(1), Materials.Empty.getCells(4), 960, 480);        
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Chloramine.getCells(2),    Materials.Empty.getCells(4),         Materials.Dimethylamine.getGas(5000), Materials.DilutedHydrochloricAcid.getFluid(1000),  Materials.Dimethylhydrazine.getCells(6),       GT_Values.NI, 960, 480);        
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Dimethylamine.getCells(5), Materials.Empty.getCells(1),         Materials.Chloramine.getFluid(2000),  Materials.DilutedHydrochloricAcid.getFluid(1000),  Materials.Dimethylhydrazine.getCells(6),       GT_Values.NI, 960, 480);        
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(Materials.Dimethylamine.getCells(5), Materials.Chloramine.getCells(2),    Materials.Chloramine.getFluid(2000),  Materials.DilutedHydrochloricAcid.getFluid(1000),  Materials.Dimethylhydrazine.getCells(6),       Materials.DilutedHydrochloricAcid.getCells(1), 960, 480);        
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(24)}, new FluidStack[]{Materials.HypochlorousAcid.getFluid(3000), Materials.Ammonia.getGas(8000), Materials.Methanol.getFluid(12000)}, new FluidStack[]{Materials.Dimethylhydrazine.getFluid(12000), Materials.DilutedHydrochloricAcid.getFluid(2000), Materials.Water.getFluid(9000)}, null, 1040, 480);
        
        GT_Values.RA.addChemicalRecipe(GT_Utility.getIntegratedCircuit(2),    GT_Values.NI,                        Materials.NitrogenDioxide.getGas(1000), Materials.DinitrogenTetroxide.getGas(1000), GT_Values.NI, 640);        
        GT_Values.RA.addChemicalRecipe(Materials.NitrogenDioxide.getCells(1), GT_Utility.getIntegratedCircuit(2),  GT_Values.NF,                           Materials.DinitrogenTetroxide.getGas(1000), Materials.Empty.getCells(1), 640);        
        GT_Values.RA.addChemicalRecipe(Materials.NitrogenDioxide.getCells(1), GT_Utility.getIntegratedCircuit(12), GT_Values.NF,                           GT_Values.NF, Materials.DinitrogenTetroxide.getCells(1), 640);        
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Ammonia.getGas(8000), Materials.Oxygen.getGas(7000)},                                   new FluidStack[]{Materials.DinitrogenTetroxide.getGas(6000), Materials.Water.getFluid(9000)}, null, 480, 30);
    	GT_Values.RA.addMultiblockChemicalRecipe(new ItemStack[]{GT_Utility.getIntegratedCircuit(23)}, new FluidStack[]{Materials.Nitrogen.getGas(2000), Materials.Hydrogen.getGas(6000), Materials.Oxygen.getGas(7000)}, new FluidStack[]{Materials.DinitrogenTetroxide.getGas(6000), Materials.Water.getFluid(9000)}, null, 1100, 480);

        GT_Values.RA.addMixerRecipe(Materials.Dimethylhydrazine.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.DinitrogenTetroxide.getGas(1000), new FluidStack(ItemList.sRocketFuel, 2000), Materials.Empty.getCells(1), 60, 16);
        GT_Values.RA.addMixerRecipe(Materials.DinitrogenTetroxide.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Dimethylhydrazine.getFluid(1000), new FluidStack(ItemList.sRocketFuel, 2000), Materials.Empty.getCells(1), 60, 16);
        GT_Values.RA.addMixerRecipe(Materials.Dimethylhydrazine.getCells(2), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Oxygen.getGas(1000), new FluidStack(ItemList.sRocketFuel, 3000), Materials.Empty.getCells(2), 60, 16);
        GT_Values.RA.addMixerRecipe(Materials.Oxygen.getCells(1), GT_Values.NI, GT_Values.NI, GT_Values.NI, Materials.Dimethylhydrazine.getFluid(2000), new FluidStack(ItemList.sRocketFuel, 3000), Materials.Empty.getCells(1), 60, 16);

		
		*/
		
		
		
		
		
		
		
		
		
		//Get Rocket Fuel
		
		//Find recipes using default values
		
		//Remove	
		
		//Rebake map		
		
		return true;
	};

	@Override
	public void items() {
		Formaldehyde_Catalyst = ItemUtils.generateSpecialUseDusts("FormaldehydeCatalyst", "Formaldehyde Catalyst", "Fe16V1", Utils.rgbtoHexValue(25, 5, 25))[0];
	}

	@Override
	public void blocks() {
	}

	@Override
	public void fluids() {
		
		//Register default fluids
		Diesel = MaterialUtils.getMaterial("Fuel", "Diesel").getFluid(1).getFluid();
		Oil_Heavy = MaterialUtils.getMaterial("OilHeavy", "Oil").getFluid(1).getFluid();		
		
		
		//Create Kerosene
		Kerosene = FluidUtils.generateFluidNonMolten("Kerosene", "Kerosene", 500, new short[]{150, 40, 150, 100}, null, null);
		CoalTar.Coal_Oil = Kerosene;
		
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

		Formaldehyde = FluidUtils.generateFluidNonMolten("Formaldehyde", "Formaldehyde", -92, new short[]{150, 75, 150, 100}, null, null);
		
		Unsymmetrical_Dimethylhydrazine_Plus_Nitrogen_Tetroxide = FluidUtils.generateFluidNonMolten("RocketFuelMixA", "H8N4C2O4 Rocket Fuel", -185, new short[]{50, 220, 50, 100}, null, null);
		RP1_Plus_Liquid_Oxygen = FluidUtils.generateFluidNonMolten("RocketFuelMixB", "Rp-1 Fuel Mixture", -250, new short[]{250, 50, 50, 100}, null, null);
		Monomethylhydrazine_Plus_Nitric_Acid = FluidUtils.generateFluidNonMolten("RocketFuelMixC", "CN3H7O3 Rocket Fuel", -300, new short[]{125, 75, 180, 100}, null, null);
		Dense_Hydrazine_Mix = FluidUtils.generateFluidNonMolten("RocketFuelMixD", "Dense Hydrazine Fuel Mixture", -250, new short[]{175, 80, 120, 100}, null, null);

		
	}



}
