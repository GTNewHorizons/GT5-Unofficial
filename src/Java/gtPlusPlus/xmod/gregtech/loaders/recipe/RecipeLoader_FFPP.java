package gtPlusPlus.xmod.gregtech.loaders.recipe;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_FFPP {	

	public static void generate() {

		// Create Fuels
		
		final FluidStack aLithiumFluoride = FLUORIDES.LITHIUM_FLUORIDE.getFluid(100);
		final FluidStack aBerylliumFluoride = FLUORIDES.BERYLLIUM_FLUORIDE.getFluid(100);
		final FluidStack aThoriumFluoride = FLUORIDES.THORIUM_TETRAFLUORIDE.getFluid(100);
		final FluidStack aZirconiumFluoride = FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluid(100);
		final FluidStack aUraniumTetraFluoride = FLUORIDES.URANIUM_TETRAFLUORIDE.getFluid(100);
		final FluidStack aUranium235 = ELEMENT.getInstance().URANIUM235.getFluid(1000);
		final FluidStack aLiFBeF2ZrF4U235 = NUCLIDE.LiFBeF2ZrF4U235.getFluid(100);
		final FluidStack aLiFBeF2ZrF4UF4 = NUCLIDE.LiFBeF2ZrF4UF4.getFluid(100);	
		final FluidStack aLiFBeF2ThF4UF4 = NUCLIDE.LiFBeF2ThF4UF4.getFluid(100);	

		// 7LiF - BeF2 - ZrF4 - U235 - 590C
		CORE.RA.addFissionFuel(
				FluidUtils.getFluidStack(aLithiumFluoride, 550),
				FluidUtils.getFluidStack(aBerylliumFluoride, 150),
				FluidUtils.getFluidStack(aZirconiumFluoride, 60),
				FluidUtils.getFluidStack(aUranium235, 240),
				null, null, null, null, null, // Extra 5 inputs
				FluidUtils.getFluidStack(aLiFBeF2ZrF4U235, 1000),
				null,
				90 * 60 * 20, // Duration
				MaterialUtils.getVoltageForTier(5)
				);
		
		// 7LiF - BeF2 - ZrF4 - UF4 - 650C
		CORE.RA.addFissionFuel(
				FluidUtils.getFluidStack(aLithiumFluoride, 650),
				FluidUtils.getFluidStack(aBerylliumFluoride, 250),
				FluidUtils.getFluidStack(aZirconiumFluoride, 80),
				FluidUtils.getFluidStack(aUraniumTetraFluoride, 70),
				null, null, null, null, null, // Extra 5 inputs
				FluidUtils.getFluidStack(aLiFBeF2ZrF4UF4, 1000),
				null,
				120 * 60 * 20,
				MaterialUtils.getVoltageForTier(5)
				);

		// 7liF - BeF2 - ThF4 - UF4 - 566C
		CORE.RA.addFissionFuel(
				FluidUtils.getFluidStack(aLithiumFluoride, 620),
				FluidUtils.getFluidStack(aBerylliumFluoride, 280),
				FluidUtils.getFluidStack(aThoriumFluoride, 70),
				FluidUtils.getFluidStack(aUraniumTetraFluoride, 70),
				null, null, null, null, null, // Extra 5 inputs
				FluidUtils.getFluidStack(aLiFBeF2ThF4UF4, 1000),
				null,
				150 * 60 * 20, // Duration
				MaterialUtils.getVoltageForTier(5)
				);
		
		
		// Reprocess Fuels

		final FluidStack aBurntLiFBeF2ZrF4U235 = new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4U235, 17);
		final FluidStack aBurntLiFBeF2ZrF4UF4 = new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4UF4, 17);
		final FluidStack aBurntLiFBeF2ThF4UF4 = new FluidStack(NuclearChem.Burnt_LiFBeF2ThF4UF4, 17);
		final FluidStack aBurntLi2BeF4 = new FluidStack(ModItems.fluidFLiBeSaltBurnt, 34);
		final FluidStack aHelium = Materials.Helium.getGas(1000);
		final FluidStack aFluorine = Materials.Fluorine.getGas(1000);

		CORE.RA.addFissionFuel(
				FluidUtils.getFluidStack(aBurntLiFBeF2ZrF4U235, 500),
				FluidUtils.getFluidStack(aBerylliumFluoride, 280),
				FluidUtils.getFluidStack(aThoriumFluoride, 70),
				FluidUtils.getFluidStack(aUraniumTetraFluoride, 70),
				null, null, null, null, null, // Extra 5 inputs
				FluidUtils.getFluidStack(aLiFBeF2ThF4UF4, 1000),
				null,
				150 * 60 * 20, // Duration
				MaterialUtils.getVoltageForTier(5)
				);

	}
}
