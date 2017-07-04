package gtPlusPlus.core.item.chemistry;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Proxy;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;

public class CoalTar {

	public static void run(){

		//Ethanol
		// v - Dehydrate cells to remove water
		//Create Ethylene
		FluidUtils.generateFluidNonMolten("Ethylene", "Ethylene", -103, new short[]{255, 255, 255, 100}, null, null);
				
		//Create Benzene - (Toluene + Hydrogen | 95% Benzene / 5% methane)
		FluidUtils.generateFluidNonMolten("Benzene", "Benzene", 81, new short[]{150, 75, 0, 100}, null, null);
		
		//Create Ethylbenzene - Ethylbenzene is produced in on a large scale by combining benzene and ethylene in an acid-catalyzed chemical reaction
		//Use Chemical Reactor
		FluidUtils.generateFluidNonMolten("Ethylbenzene", "Ethylbenzene", 136, new short[]{255, 255, 255, 100}, null, null);
		
		
		
		
		
		
		
		
		
		
		
		
		//Create Coal Tar
		FluidUtils.generateFluidNonMolten("CoalTar", "Coal Tar", 450, new short[]{32, 32, 32, 100}, null, null);
		
		// v - Distill (70% Tar oil/10% Naphtha/20% Ethylbenzene)
		
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
		FluidUtils.generateFluidNonMolten("2Ethylanthraquinone", "2-Ethylanthraquinone", 415, new short[]{227, 255, 159, 100}, null, null);
		
		
		
		
		
		
		
		
		
		
		//Pyrolyse
		//Lignite Coal
		GT_Values.RA.addPyrolyseRecipe(
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16L), //Input 1
				GT_Values.NF, //Fluid Input
				8,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
				FluidUtils.getFluidStack("fluid.coaltar", 800), //Fluid Output
				150*20,
				120);
		//Coal
		GT_Values.RA.addPyrolyseRecipe(
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L), //Input 1
				GT_Values.NF, //Fluid Input
				8,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
				FluidUtils.getFluidStack("fluid.coaltar", 2200), //Fluid Output
				120*20,
				240);
		//Coal Coke
		GT_Values.RA.addPyrolyseRecipe(
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
