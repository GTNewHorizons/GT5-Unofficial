package gtPlusPlus.xmod.gregtech.loaders.recipe;

import gregtech.api.enums.Materials;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_NuclearFuelProcessing {	

	public static void generate() {

		// Create Fuels
		
		final FluidStack aLithiumFluoride = FLUORIDES.LITHIUM_FLUORIDE.getFluidStack(100);
		final FluidStack aBerylliumFluoride = FLUORIDES.BERYLLIUM_FLUORIDE.getFluidStack(100);
		final FluidStack aThoriumFluoride = FLUORIDES.THORIUM_TETRAFLUORIDE.getFluidStack(100);
		final FluidStack aZirconiumFluoride = FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(100);
		final FluidStack aUraniumTetraFluoride = FLUORIDES.URANIUM_TETRAFLUORIDE.getFluidStack(100);
		final FluidStack aUranium235 = ELEMENT.getInstance().URANIUM235.getFluidStack(1000);
		final FluidStack aLiFBeF2ZrF4U235 = NUCLIDE.LiFBeF2ZrF4U235.getFluidStack(100);
		final FluidStack aLiFBeF2ZrF4UF4 = NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(100);	
		final FluidStack aLiFBeF2ThF4UF4 = NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(100);	

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
		final FluidStack aHelium = Materials.Helium.getGas(1000);
		final FluidStack aFluorine = Materials.Fluorine.getGas(1000);
		
		
		// Reactor Core step 0 - Process Burnt Salt
		

		
		// Tier 1 Fuel - Gives back FLIBE and breeds U233
		CORE.RA.addReactorProcessingUnitRecipe(
				CI.getNumberedAdvancedCircuit(1),
				CI.emptyCells(2),
				new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4U235, 4000),
				new ItemStack[] {
						FLUORIDES.LITHIUM_FLUORIDE.getCell(1),
						ELEMENT.getInstance().URANIUM233.getCell(1)
						},
				new int[] {10000, 10000},
				NUCLIDE.LiFBeF2.getFluidStack(2000),
				20 * 60 * 60,
				MaterialUtils.getVoltageForTier(3));
		
		// Tier 2 Fuel
		CORE.RA.addFissionFuel(
				new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4UF4, 3000),
				null, null, null, null, null, null, null, null, // Extra 5 inputs
				FLUORIDES.ZIRCONIUM_TETRAFLUORIDE.getFluidStack(1000),
				NUCLIDE.LiFBeF2UF4.getFluidStack(2000),
				60 * 60 * 20, // Duration
				MaterialUtils.getVoltageForTier(4)
				);
		
		// Tier 3 Fuel
		CORE.RA.addFissionFuel(
				new FluidStack(NuclearChem.Burnt_LiFBeF2ThF4UF4, 3000),
				null, null, null, null, null, null, null, null, // Extra 5 inputs
				FLUORIDES.THORIUM_TETRAFLUORIDE.getFluidStack(1000),
				NUCLIDE.LiFBeF2UF4.getFluidStack(2000),
				120 * 60 * 20, // Duration
				MaterialUtils.getVoltageForTier(4)
				);
		


		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		// Reactor Blanket step 0 - Splitting
		/*		CORE.RA.addReactorProcessingUnitRecipe(
						CI.getNumberedAdvancedCircuit(6),
						CI.emptyCells(1),
						new FluidStack(ModItems.fluidFLiBeSaltBurnt, 3000),
						new ItemStack[] {
								FLUORIDES.LITHIUM_FLUORIDE.getCell(1)
								},
						new int[] {10000},
						NUCLIDE.LiFBeF2.getFluidStack(2000),
						20 * 60 * 60,
						MaterialUtils.getVoltageForTier(3));*/	
		
		
		// Reactor Blanket step 1 - Fluorination
		CORE.RA.addReactorProcessingUnitRecipe(
				CI.getNumberedAdvancedCircuit(7),
				ELEMENT.getInstance().FLUORINE.getCell(10),
				NUCLIDE.LiFThF4.getFluidStack(10000),
				new ItemStack[] {
						CI.emptyCells(8),
						ELEMENT.getInstance().LITHIUM.getCell(2),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233)
						},
				new int[] {10000, 10000, 2000, 2000, 2000, 2000, 2000, 2000},
				NUCLIDE.UF6F2.getFluidStack(10000),
				20 * 60 * 5,
				MaterialUtils.getVoltageForTier(5));		
		CORE.RA.addReactorProcessingUnitRecipe(
				CI.getNumberedAdvancedCircuit(8),
				ELEMENT.getInstance().FLUORINE.getCell(10),
				NUCLIDE.LiFBeF2ThF4.getFluidStack(10000),
				new ItemStack[] {
						CI.emptyCells(6),
						ELEMENT.getInstance().LITHIUM.getCell(2),
						FLUORIDES.BERYLLIUM_FLUORIDE.getCell(2),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233),
						ItemUtils.getSimpleStack(ModItems.dustProtactinium233)
						},
				new int[] {10000, 10000, 10000, 2000, 2000, 2000, 2000, 2000, 2000},
				NUCLIDE.UF6F2.getFluidStack(10000),
				20 * 60 * 5,
				MaterialUtils.getVoltageForTier(5));
		

		// Reactor Blanket step 2 - Sorption + Cold Trap
		CORE.RA.addColdTrapRecipe(
				8,
				FLUORIDES.SODIUM_FLUORIDE.getCell(2),
				NUCLIDE.UF6F2.getFluidStack(3000),
				new ItemStack[] {
						ELEMENT.getInstance().FLUORINE.getCell(2),
						ELEMENT.getInstance().URANIUM233.getDust(1),
						ELEMENT.getInstance().URANIUM233.getDust(1),
						ELEMENT.getInstance().URANIUM233.getDust(1)
				},
				new int[] {10000, 3000, 2000, 1000},
				FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(1000),
				20 * 60 * 10,
				MaterialUtils.getVoltageForTier(3));		
		
		
		
		
		


	}
}
