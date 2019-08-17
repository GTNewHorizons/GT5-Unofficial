package gtPlusPlus.xmod.gregtech.registration.gregtech;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntityRocketFuelGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_LargeRocketEngine;

public class GregtechRocketFuelGenerator {

	public static void run() {
		if (LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Rocket Engines.");
			if (CORE.ConfigSwitches.enableMachine_RocketEngines) {
				run1();
			}
		}

	}

	private static void run1() {
		GregtechItemList.Controller_RocketEngine.set(
				new GregtechMetaTileEntity_LargeRocketEngine(30410, "gtpp.multimachine.rocketengine", "Rocketdyne F-1A Engine").getStackForm(1L));
				
		 GT_ModHandler.addCraftingRecipe(
				 GregtechItemList.Controller_RocketEngine.get(1L),
				 CI.bitsd,
				 new Object[]{
						 "PCP", "EME", "GWG",
						 'M', ItemList.Machine_Multi_DieselEngine,
						 'P', ItemList.Electric_Piston_IV,
						 'E', ItemList.Field_Generator_EV,
						 'C', OrePrefixes.circuit.get(Materials.Master),
						 'W', OrePrefixes.cableGt08.get(Materials.Platinum),
						 'G', ALLOY.MARAGING350.getGear(1)});	       
		
		GT_ModHandler.addCraftingRecipe(
				GregtechItemList.Casing_RocketEngine.get(1L),
				CI.bitsd,
				new Object[]{
						"PhP", "RFR", "PWP", 
						'R', OrePrefixes.pipeLarge.get(Materials.TungstenSteel),
						'F', ItemList.Casing_RobustTungstenSteel,
						'P', ALLOY.NITINOL_60.getGear(1)});

		GregtechItemList.Rocket_Engine_EV.set(new GregtechMetaTileEntityRocketFuelGenerator(793,
				"advancedgenerator.rocketFuel.tier.01", "Basic Rocket Engine", 4).getStackForm(1L));
		GregtechItemList.Rocket_Engine_IV.set(new GregtechMetaTileEntityRocketFuelGenerator(794,
				"advancedgenerator.rocketFuel.tier.02", "Advanced Rocket Engine", 5).getStackForm(1L));
		GregtechItemList.Rocket_Engine_LuV.set(new GregtechMetaTileEntityRocketFuelGenerator(795,
				"advancedgenerator.rocketFuel.tier.03", "Turbo Rocket Engine", 6).getStackForm(1L));
		if (!CORE.GTNH) {
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_EV.get(1L, new Object[0]),
					GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
					| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
					new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('P'),
							ItemList.Electric_Piston_EV, Character.valueOf('E'), ItemList.Electric_Motor_EV,
							Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Ultimate), Character.valueOf('W'),
							OrePrefixes.cableGt02.get(Materials.Aluminium), Character.valueOf('G'),
							ALLOY.TANTALLOY_61.getGear(1)});

			GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_IV.get(1L, new Object[0]),
					GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
					| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
					new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('P'),
							ItemList.Electric_Piston_IV, Character.valueOf('E'), ItemList.Electric_Motor_IV,
							Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Superconductor),
							Character.valueOf('W'), OrePrefixes.cableGt02.get(Materials.Platinum), Character.valueOf('G'),
							ALLOY.STELLITE.getGear(1)});
		}
		if (CORE.GTNH) {
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_EV.get(1L, new Object[0]),
					GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
					| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
					new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_EV, Character.valueOf('P'),
							ItemList.Electric_Piston_EV, Character.valueOf('E'), ItemList.Electric_Motor_EV,
							Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Elite), Character.valueOf('W'),
							OrePrefixes.cableGt02.get(Materials.Aluminium), Character.valueOf('G'),
							ALLOY.TANTALLOY_61.getGear(1)});

			GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_IV.get(1L, new Object[0]),
					GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
					| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
					new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_IV, Character.valueOf('P'),
							ItemList.Electric_Piston_IV, Character.valueOf('E'), ItemList.Electric_Motor_IV,
							Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Master),
							Character.valueOf('W'), OrePrefixes.cableGt02.get(Materials.Platinum), Character.valueOf('G'),
							ALLOY.STELLITE.getGear(1)});
		}
		final ItemStack INGREDIENT_1 = CI.electricPiston_LuV;
		final ItemStack INGREDIENT_2 = CI.electricMotor_LuV;
		if (!CORE.GTNH) {
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_LuV.get(1L, new Object[0]),
					GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
					| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
					new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_LuV, Character.valueOf('P'),
							INGREDIENT_1, Character.valueOf('E'), INGREDIENT_2, Character.valueOf('C'),
							OrePrefixes.circuit.get(Materials.Infinite), Character.valueOf('W'),
							OrePrefixes.cableGt02.get(Materials.Tungsten), Character.valueOf('G'),
							ALLOY.ZERON_100.getGear(1)});
		}
		if (CORE.GTNH) {
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Rocket_Engine_LuV.get(1L, new Object[0]),
					GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
					| GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
					new Object[]{"PCP", "EME", "GWG", Character.valueOf('M'), ItemList.Hull_LuV, Character.valueOf('P'),
							INGREDIENT_1, Character.valueOf('E'), INGREDIENT_2, Character.valueOf('C'),
							OrePrefixes.circuit.get(Materials.Ultimate), Character.valueOf('W'),
							OrePrefixes.cableGt02.get(Materials.Tungsten), Character.valueOf('G'),
							ALLOY.ZERON_100.getGear(1)});
		}
	}
}

