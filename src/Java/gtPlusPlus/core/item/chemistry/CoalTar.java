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

		//Create Coal Tar From Coal
		CORE.RA.addCokeOvenRecipe(
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Lignite, 16L), //Input 1
				GT_Values.NI, //Input 2
				GT_Values.NF, //Fluid Input
				FluidUtils.getFluidStack("coaltar", 800), //Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
				150*20,  //Time in ticks
				120); //EU

		//Create Coal Tar From Coal
		CORE.RA.addCokeOvenRecipe(
				GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L), //Input 1
				GT_Values.NI, //Input 2
				GT_Values.NF, //Fluid Input
				FluidUtils.getFluidStack("coaltar", 2200), //Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2), //Item Output
				120*20,  //Time in ticks
				240); //EU

		//Create Coal Tar From Coal
		CORE.RA.addCokeOvenRecipe(
				ItemUtils.getItemStack("Railcraft:fuel.coke", 16), //Input 1
				GT_Values.NI, //Input 2
				GT_Values.NF, //Fluid Input
				FluidUtils.getFluidStack("coaltar", 3400), //Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 3), //Item Output
				120*20,  //Time in ticks
				360); //EU


		/*//Create Coal Tar From Coal
		GT_Values.RA.addBlastRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("gemCoal", 16),
				GT_Values.NI,
				GT_Values.NF,
				FluidUtils.getFluidStack("coaltar", 2200),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallDarkAsh", 2),
				GT_Values.NI,
				120*20,
				240, //EU
				2700); //Heat

		//Create Coal Tar From Coal
		GT_Values.RA.addBlastRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("gemCoke", 16),
				GT_Values.NI,
				GT_Values.NF,
				FluidUtils.getFluidStack("coaltar", 3400),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallAsh", 3),
				GT_Values.NI,
				120*20,
				360, //EU
				3100); //Heat
*/
	}

}
