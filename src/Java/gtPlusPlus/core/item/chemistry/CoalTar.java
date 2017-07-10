package gtPlusPlus.core.item.chemistry;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;
import net.minecraft.item.ItemStack;

public class CoalTar {

	public static void run(){

		//Create Coal Gas
		FluidUtils.generateFluidNonMolten("CoalGas", "Coal Gas", 500, new short[]{48, 48, 48, 100}, null, null);
				
		
		
		
		//Ethanol
		// v - Dehydrate cells to remove water
		//Create Ethylene
		FluidUtils.generateFluidNonMolten("Ethylene", "Ethylene", -103, new short[]{255, 255, 255, 100}, null, null);
		//Create Benzene - (Toluene + Hydrogen | 95% Benzene / 5% methane)
		FluidUtils.generateFluidNonMolten("Benzene", "Benzene", 81, new short[]{150, 75, 0, 100}, null, null);
		//Create Ethylbenzene - Ethylbenzene is produced in on a large scale by combining benzene and ethylene in an acid-catalyzed chemical reaction
		//Use Chemical Reactor
		FluidUtils.generateFluidNonMolten("Ethylbenzene", "Ethylbenzene", 136, new short[]{255, 255, 255, 100}, null, null);



		//Create Anthracene
		FluidUtils.generateFluidNonMolten("Anthracene", "Anthracene", 340, new short[]{255, 255, 255, 100}, null, null);



		//Create Coal Tar
		FluidUtils.generateFluidNonMolten("CoalTar", "Coal Tar", 450, new short[]{32, 32, 32, 100}, null, null);
		// v - Distill (60% Tar oil/15% Naphtha/20% Ethylbenzene/5% Anthracene)
		//Create Coal Tar Oil
		FluidUtils.generateFluidNonMolten("CoalTarOil", "Coal Tar Oil", 240, new short[]{240, 240, 150, 100}, null, null);
		// v - Wash With Sulfuric Acid
		//Create Sulfuric Coal Tar Oil
		FluidUtils.generateFluidNonMolten("SulfuricCoalTarOil", "Sulfuric Coal Tar Oil", 240, new short[]{250, 170, 12, 100}, null, null);
		// v - Distill (No loss, just time consuming)
		//Create Naphthalene
		FluidUtils.generateFluidNonMolten("Naphthalene", "Naphthalene", 115, new short[]{210, 185, 135, 100}, null, null);
		// v - Oxidize with mercury and nitric acid
		//Create Phthalic Acid
		FluidUtils.generateFluidNonMolten("PhthalicAcid", "Phthalic Acid", 207, new short[]{210, 220, 210, 100}, null, null);
		// v - Dehydrate at 180C+
		//Create Phthalic Anhydride
		ItemUtils.generateSpecialUseDusts("PhthalicAnhydride", "Phthalic Anhydride", Utils.rgbtoHexValue(175, 175, 175));



		//Create 2-Ethylanthraquinone
		//2-Ethylanthraquinone is prepared from the reaction of phthalic anhydride and ethylbenzene
		FluidUtils.generateFluidNonMolten("2Ethylanthraquinone", "2-Ethylanthraquinone", 415, new short[]{227, 255, 159, 100}, null, null);
		//Create 2-Ethylanthrahydroquinone
		//Palladium plate + Hydrogen(250) + 2-Ethylanthraquinone(500) = 600 Ethylanthrahydroquinone
		FluidUtils.generateFluidNonMolten("2Ethylanthrahydroquinone", "2-Ethylanthrahydroquinone", 415, new short[]{207, 225, 129, 100}, null, null);
		//Create Hydrogen Peroxide
		//Compressed Air(1500) + Ethylanthrahydroquinone(500) + Anthracene(5) = 450 Ethylanthraquinone && 200 Peroxide
		FluidUtils.generateFluidNonMolten("HydrogenPeroxide", "Hydrogen Peroxide", 150, new short[]{210, 255, 255, 100}, null, null);



		//Lithium Hydroperoxide - LiOH + H2O2 → LiOOH + 2 H2O
		ItemUtils.generateSpecialUseDusts("LithiumHydroperoxide", "Lithium Hydroperoxide", Utils.rgbtoHexValue(125, 125, 125));
		// v - Dehydrate
		//Lithium Peroxide - 2 LiOOH → Li2O2 + H2O2 + 2 H2O
		FluidUtils.generateFluidNonMolten("LithiumPeroxide", "Lithium Peroxide", 446, new short[]{135, 135, 135, 100}, null, null);

		createRecipes();
		

	}

	private static void createRecipes() {
		recipeCreateEthylene();
		recipeCreatebenzene();
		recipeCreateEthylbenzene();
		
		recipeCoalToCoalTar();
	}

	public static void recipeCreateEthylene(){
		CORE.RA.addDehydratorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
				FluidUtils.getFluidStack("fluid.bioethanol", 2000),
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1)
				},
				120*20,
				80);
		
		CORE.RA.addDehydratorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2),
				FluidUtils.getFluidStack("bioethanol", 2000),
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellEthylene", 1)
				},
				120*20,
				80);
	}

	public static void recipeCreatebenzene(){

	}

	public static void recipeCreateEthylbenzene(){
		//GT_Values.RA.addChemicalRecipe(arg0, arg1, arg2, arg3)
	}


	public static void recipeCoalToCoalTar(){
		//Pyrolyse
				//Lignite Coal
				AddGregtechRecipe.PyrolyseOven(
						GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16L), //Input 1
						GT_Values.NF, //Fluid Input
						8,
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
						FluidUtils.getFluidStack("fluid.coaltar", 800), //Fluid Output
						150*20,
						120);
				//Coal
				AddGregtechRecipe.PyrolyseOven(
						GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L), //Input 1
						GT_Values.NF, //Fluid Input
						8,
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
						FluidUtils.getFluidStack("fluid.coaltar", 2200), //Fluid Output
						120*20,
						240);
				//Coal Coke
				AddGregtechRecipe.PyrolyseOven(
						ItemUtils.getItemStack("Railcraft:fuel.coke", 16), //Input 1
						GT_Values.NF, //Fluid Input
						8,
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 3), //Item Output
						FluidUtils.getFluidStack("fluid.coaltar", 3400), //Fluid Output
						100*20,
						360);

				//Coke Oven
				//Create Coal Tar From Coal
				CORE.RA.addCokeOvenRecipe(
						GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16L), //Input 1
						GT_Values.NI, //Input 2
						GT_Values.NF, //Fluid Input
						FluidUtils.getFluidStack("fluid.coaltar", 800), //Fluid Output
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
						150*10,  //Time in ticks
						120); //EU

				//Create Coal Tar From Coal
				CORE.RA.addCokeOvenRecipe(
						GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L), //Input 1
						GT_Values.NI, //Input 2
						GT_Values.NF, //Fluid Input
						FluidUtils.getFluidStack("fluid.coaltar", 2200), //Fluid Output
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
						120*10,  //Time in ticks
						240); //EU

				//Create Coal Tar From Coal
				CORE.RA.addCokeOvenRecipe(
						ItemUtils.getItemStack("Railcraft:fuel.coke", 16), //Input 1
						GT_Values.NI, //Input 2
						GT_Values.NF, //Fluid Input
						FluidUtils.getFluidStack("fluid.coaltar", 3400), //Fluid Output
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 3), //Item Output
						120*10,  //Time in ticks
						360); //EU
	}


}
