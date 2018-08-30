package gtPlusPlus.core.item.chemistry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class CoalTar {

	private static Fluid Coal_Gas;
	private static Fluid Ethylene;
	private static Fluid Benzene;
	private static Fluid Ethylbenzene;
	private static Fluid Anthracene;
	private static Fluid Toluene;
	private static Fluid Coal_Tar;
	private static Fluid Coal_Tar_Oil;
	private static Fluid Sulfuric_Coal_Tar_Oil;
	private static Fluid Naphthalene;
	private static Fluid Phthalic_Acid;
	private static Fluid Ethylanthraquinone2;
	private static Fluid Ethylanthrahydroquinone2;
	private static Fluid Hydrogen_Peroxide;
	private static Fluid Lithium_Peroxide;

	public static void run(){

		//Special Compatibility for Coke
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemCoalCoke, 1), "fuelCoke");
		//Create Coal Gas
		Coal_Gas = FluidUtils.generateFluidNonMolten("CoalGas", "Coal Gas", 500, new short[]{48, 48, 48, 100}, null, null);
		//Ethanol
		// v - Dehydrate cells to remove water
		//Create Ethylene
		Ethylene = FluidUtils.generateFluidNonMolten("Ethylene", "Ethylene", -103, new short[]{255, 255, 255, 100}, null, null);
		//Create Benzene - (Toluene + Hydrogen | 95% Benzene / 5% methane)
		Benzene = FluidUtils.generateFluidNonMolten("Benzene", "Benzene", 81, new short[]{150, 75, 0, 100}, null, null);
		//Create Ethylbenzene - Ethylbenzene is produced in on a large scale by combining benzene and ethylene in an acid-catalyzed chemical reaction
		//Use Chemical Reactor
		Ethylbenzene = FluidUtils.generateFluidNonMolten("Ethylbenzene", "Ethylbenzene", 136, new short[]{255, 255, 255, 100}, null, null);
		//Create Anthracene
		Anthracene = FluidUtils.generateFluidNonMolten("Anthracene", "Anthracene", 340, new short[]{255, 255, 255, 100}, null, null);
		//Toluene		
		if (FluidUtils.getFluidStack("liquid_toluene", 1) == null){
			Toluene = FluidUtils.generateFluidNonMolten("Toluene", "Toluene", -95, new short[]{140, 70, 20, 100}, null, null);
		}
		else {
			Toluene = FluidUtils.getFluidStack("liquid_toluene", 1000).getFluid();
			Item itemCellToluene = new BaseItemComponent("Toluene", "Toluene", new short[]{140, 70, 20, 100});
			MaterialGenerator.addFluidCannerRecipe(ItemUtils.getEmptyCell(), ItemUtils.getSimpleStack(itemCellToluene), FluidUtils.getFluidStack("liquid_toluene", 1000), null);
		}

		//Create Coal Tar
		Coal_Tar = FluidUtils.generateFluidNonMolten("CoalTar", "Coal Tar", 450, new short[]{32, 32, 32, 100}, null, null);
		// v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene)
		//Create Coal Tar Oil
		Coal_Tar_Oil = FluidUtils.generateFluidNonMolten("CoalTarOil", "Coal Tar Oil", 240, new short[]{240, 240, 150, 100}, null, null);
		// v - Wash With Sulfuric Acid
		//Create Sulfuric Coal Tar Oil
		Sulfuric_Coal_Tar_Oil = FluidUtils.generateFluidNonMolten("SulfuricCoalTarOil", "Sulfuric Coal Tar Oil", 240, new short[]{250, 170, 12, 100}, null, null);
		// v - Distill (No loss, just time consuming)
		//Create Naphthalene
		Naphthalene = FluidUtils.generateFluidNonMolten("Naphthalene", "Naphthalene", 115, new short[]{210, 185, 135, 100}, null, null);
		// v - Oxidize with mercury and nitric acid
		//Create Phthalic Acid
		Phthalic_Acid = FluidUtils.generateFluidNonMolten("PhthalicAcid", "Phthalic Acid", 207, new short[]{210, 220, 210, 100}, null, null);
		// v - Dehydrate at 180C+
		//Create Phthalic Anhydride
		ItemUtils.generateSpecialUseDusts("PhthalicAnhydride", "Phthalic Anhydride", "C6H4(CO)2O", Utils.rgbtoHexValue(175, 175, 175));



		//Create 2-Ethylanthraquinone
		//2-Ethylanthraquinone is prepared from the reaction of phthalic anhydride and ethylbenzene
		Ethylanthraquinone2 = FluidUtils.generateFluidNonMolten("2Ethylanthraquinone", "2-Ethylanthraquinone", 415, new short[]{227, 255, 159, 100}, null, null);
		//Create 2-Ethylanthrahydroquinone
		//Palladium plate + Hydrogen(250) + 2-Ethylanthraquinone(500) = 600 Ethylanthrahydroquinone
		Ethylanthrahydroquinone2 = FluidUtils.generateFluidNonMolten("2Ethylanthrahydroquinone", "2-Ethylanthrahydroquinone", 415, new short[]{207, 225, 129, 100}, null, null);
		//Create Hydrogen Peroxide
		//Compressed Air(1500) + Ethylanthrahydroquinone(500) + Anthracene(5) = 450 Ethylanthraquinone && 200 Peroxide
		Hydrogen_Peroxide = FluidUtils.generateFluidNonMolten("HydrogenPeroxide", "Hydrogen Peroxide", 150, new short[]{210, 255, 255, 100}, null, null);



		//Lithium Hydroperoxide - LiOH + H2O2 → LiOOH + 2 H2O
		ItemUtils.generateSpecialUseDusts("LithiumHydroperoxide", "Lithium Hydroperoxide", "HLiO2", Utils.rgbtoHexValue(125, 125, 125));
		// v - Dehydrate
		//Lithium Peroxide - 2 LiOOH → Li2O2 + H2O2 + 2 H2O
		Lithium_Peroxide = FluidUtils.generateFluidNonMolten("LithiumPeroxide", "Lithium Peroxide", 446, new short[]{135, 135, 135, 100}, null, null);

		//Burn the coal gas!
		GT_Values.RA.addFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalGas", 1), null, 64, 1);
		GT_Values.RA.addFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricCoalTarOil", 1), null, 32, 3);
		GT_Values.RA.addFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTarOil", 1), null, 64, 3);
		GT_Values.RA.addFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTar", 1), null, 192, 3);
		createRecipes();


	}

	private static void createRecipes() {
		recipeCreateEthylene();
		recipeCreateBenzene();
		recipeCreateEthylbenzene();

		recipeCoalToCoalTar();
		recipeCoalTarToCoalTarOil();
		recipeCoalTarOilToSulfuricOilToNaphthalene();
		recipeNaphthaleneToPhthalicAcid();
		recipePhthalicAcidToPhthalicAnhydride();
		recipe2Ethylanthraquinone();
		recipe2Ethylanthrahydroquinone();
		recipeHydrogenPeroxide();
		recipeLithiumHydroperoxide();
		recipeLithiumPeroxide();
		
		recipeEthylBenzineFuelsIntoHeavyFuel();
	}



	private static void recipeEthylBenzineFuelsIntoHeavyFuel() {
			GT_Values.RA.addChemicalRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("cellFuel", 9), 
					ItemUtils.getItemStackOfAmountFromOreDict("cellEthylbenzene", 2), 
					null,
					FluidUtils.getFluidStack("nitrofuel", 7500),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 11), 
					100,
					CORE.GTNH ? 1000 : 500);
			GT_Values.RA.addChemicalRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("cellBioDiesel", 9), 
					ItemUtils.getItemStackOfAmountFromOreDict("cellEthylbenzene", 2), 
					null,
					FluidUtils.getFluidStack("nitrofuel", 3000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 11), 
					300,
					CORE.GTNH ? 1000 : 500);
	}

	public static void recipeCreateEthylene(){

		FluidStack bioEth1 = FluidUtils.getFluidStack("fluid.bioethanol", 2000);
		FluidStack bioEth2 = FluidUtils.getFluidStack("bioethanol", 2000);

		if (bioEth1 != null){
			CORE.RA.addDehydratorRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
					bioEth1,
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1)
					},
					120*20,
					80);
		}

		if (bioEth2 != null){
			CORE.RA.addDehydratorRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
					bioEth2,
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1)
					},
					120*20,
					80);
		}
	}

	public static void recipeCreateBenzene(){
		//Create Benzene - (Toluene + Hydrogen | 95% Benzene / 5% methane)		
		CORE.RA.addDehydratorRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("cellToluene", 10),
						ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 10)
				}, 
				null, 
				null,
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("cellMethane", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellBenzene", 19)
				},
				new int[]{10000, 10000}, 
				20*100, 
				90);
	}

	public static void recipeCreateEthylbenzene(){
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 2), 
				ItemUtils.getItemStackOfAmountFromOreDict("cellBenzene", 2), 
				null,
				FluidUtils.getFluidStack("fluid.ethylbenzene", 4000),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 4), 
				300);
	}


	public static void recipeCoalToCoalTar(){
		//Lignite
		AddGregtechRecipe.addCokeAndPyrolyseRecipes(
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16L),
				8,
				GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2),
				FluidUtils.getFluidStack("fluid.coaltar", 800), 
				90,
				60);

		//Coal
		AddGregtechRecipe.addCokeAndPyrolyseRecipes(
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12L),
				8,
				GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2),
				FluidUtils.getFluidStack("fluid.coaltar", 2200), 
				60,
				120);

		//Coke
		AddGregtechRecipe.addCokeAndPyrolyseRecipes(
				ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 8),
				8,
				GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 3),
				FluidUtils.getFluidStack("fluid.coaltar", 3400), 
				30,
				240);

	}

	private static void recipeCoalTarToCoalTarOil() {
		// v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene)
		//Create Coal Tar Oil
		//FluidUtils.generateFluidNonMolten("CoalTarOil", "Coal Tar Oil", 240, new short[]{240, 240, 150, 100}, null, null);
		GT_Values.RA.addDistilleryRecipe(
				CI.getNumberedCircuit(1), //Circuit
				FluidUtils.getFluidStack("fluid.coaltar", 1000), //aInput
				FluidUtils.getFluidStack("fluid.coaltaroil", 600), //aOutput
				600, //aDuration
				64,//aEUt
				false //Hidden?
				);	
		GT_Values.RA.addDistilleryRecipe(
				CI.getNumberedCircuit(2), //Circuit
				FluidUtils.getFluidStack("fluid.coaltar", 1000), //aInput
				FluidUtils.getFluidStack("liquid_naphtha", 150), //aOutput
				300, //aDuration
				30,//aEUt
				false //Hidden?
				);
		GT_Values.RA.addDistilleryRecipe(
				CI.getNumberedCircuit(3), //Circuit
				FluidUtils.getFluidStack("fluid.coaltar", 1000), //aInput
				FluidUtils.getFluidStack("fluid.ethylbenzene", 200), //aOutput
				450, //aDuration
				86,//aEUt
				false //Hidden?
				);
		GT_Values.RA.addDistilleryRecipe(
				CI.getNumberedCircuit(4), //Circuit
				FluidUtils.getFluidStack("fluid.coaltar", 1000), //aInput
				FluidUtils.getFluidStack("fluid.anthracene", 50), //aOutput
				900, //aDuration
				30,//aEUt
				false //Hidden?
				);
		GT_Values.RA.addDistillationTowerRecipe(
				FluidUtils.getFluidStack("fluid.coaltar", 1000),
				new FluidStack[]{
						FluidUtils.getFluidStack("fluid.coaltaroil", 600), //aOutput
						FluidUtils.getFluidStack("liquid_naphtha", 150), //aOutput
						FluidUtils.getFluidStack("fluid.ethylbenzene", 200), //aOutput
						FluidUtils.getFluidStack("fluid.anthracene", 50), //aOutput
				},
				null,
				900,
				60);

	}

	private static void recipeCoalTarOilToSulfuricOilToNaphthalene() {
		//SulfuricCoalTarOil
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellCoalTarOil", 8),
				ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricAcid", 8),
				null,
				null,
				ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricCoalTarOil", 16),
				20*16);
		GT_Values.RA.addDistilleryRecipe(
				CI.getNumberedCircuit(6), //Circuit
				FluidUtils.getFluidStack("fluid.sulfuriccoaltaroil", 1000), //aInput
				FluidUtils.getFluidStack("fluid.naphthalene", 1000), //aOutput
				1200, //aDuration
				30,//aEUt
				false //Hidden?
				);	

	}

	private static void recipeNaphthaleneToPhthalicAcid() {
		//SulfuricCoalTarOil
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellNaphthalene", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithium", 5),
				null,
				FluidUtils.getFluidStack("fluid.phthalicacid", 2500),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
				20*16);

	}

	private static void recipePhthalicAcidToPhthalicAnhydride() {
		CORE.RA.addDehydratorRecipe(
				ItemUtils.getGregtechCircuit(6),
				FluidUtils.getFluidStack("fluid.phthalicacid", 144),
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustPhthalicAnhydride", 1)
				},
				60*20,
				120);

	}

	private static void recipe2Ethylanthraquinone() {
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustPhthalicAnhydride", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEthylbenzene", 2),
				null,
				FluidUtils.getFluidStack("fluid.2ethylanthraquinone", 2000+(144*4)),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
				20*16);

	}

	private static void recipe2Ethylanthrahydroquinone() {
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("platePalladium", 0),
				ItemUtils.getItemStackOfAmountFromOreDict("cell2Ethylanthraquinone", 1),
				FluidUtils.getFluidStack("hydrogen", 500),
				FluidUtils.getFluidStack("fluid.2ethylanthrahydroquinone", 1200),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1),
				20*40);

	}

	private static void recipeHydrogenPeroxide() {		
		GT_Values.RA.addElectrolyzerRecipe(
				GT_ModHandler.getAirCell(15),
				ItemUtils.getItemStackOfAmountFromOreDict("cell2Ethylanthrahydroquinone", 5),
				FluidUtils.getFluidStack("fluid.anthracene", 50),
				FluidUtils.getFluidStack("fluid.2ethylanthrahydroquinone", 4450),
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 18), 
				null, 
				null,
				null,
				null,
				new int[]{10000, 10000}, 
				20*90, 
				240);		
	}


	private static void recipeLithiumHydroperoxide() {
		GT_Values.RA.addElectrolyzerRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 7),
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 1),
				FluidUtils.getFluidStack("fluid.cellhydrogenperoxide", 50),
				null,
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroperoxide", 14),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1), 
				null, 
				null,
				null,
				null,
				new int[]{10000, 10000}, 
				20*60, 
				240);		
	}


	private static void recipeLithiumPeroxide() {
		CORE.RA.addDehydratorRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroperoxide", 2),
						ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 3)
				}, 
				null, 
				null, 
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumPeroxide", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenPeroxide", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 2)
				},
				new int[]{10000, 10000, 10000}, 
				20*100, 
				240);
	}

}
