package gtPlusPlus.core.item.chemistry;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.GT_Proxy;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;

public class CoalTar {

	public static void run(){

		//Create Coal Tar
		FluidUtils.generateFluidNonMolten("CoalTar", "Coal Tar", 450, new short[]{32, 32, 32, 100}, null, null);

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
