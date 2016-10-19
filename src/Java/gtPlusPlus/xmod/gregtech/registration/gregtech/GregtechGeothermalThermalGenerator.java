package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.RECIPE_CONSTANTS;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntityGeothermalGenerator;
import net.minecraft.item.ItemStack;

public class GregtechGeothermalThermalGenerator {

	public static void run()
	{
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Industrial Geothermal Engines.");
			run1();
		}

	}

	private static void run1(){
		GregtechItemList.Geothermal_Engine_EV.set(new GregtechMetaTileEntityGeothermalGenerator(830, "advancedgenerator.geothermalFuel.tier.01", "Basic Geothermal Engine", 4).getStackForm(1L));
		GregtechItemList.Geothermal_Engine_IV.set(new GregtechMetaTileEntityGeothermalGenerator(831, "advancedgenerator.geothermalFuel.tier.02", "Turbo Geothermal Engine", 5).getStackForm(1L));
		GregtechItemList.Geothermal_Engine_LuV.set(new GregtechMetaTileEntityGeothermalGenerator(832, "advancedgenerator.geothermalFuel.tier.03", "Vulcan Geothermal Engine", 6).getStackForm(1L));

		GT_ModHandler.addCraftingRecipe(
				GregtechItemList.Geothermal_Engine_EV.get(1L, new Object[0]),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED, 
				new Object[]{"CEC", "GMG", "PWP",
					Character.valueOf('M'), ItemList.Hull_EV,
					Character.valueOf('P'), ItemList.Electric_Piston_EV,
					Character.valueOf('E'), ItemList.Electric_Motor_EV,
					Character.valueOf('C'), GregtechOrePrefixes.circuit.get(Materials.Ultimate),
					Character.valueOf('W'), OrePrefixes.cableGt04.get(Materials.Aluminium),
					Character.valueOf('G'), ALLOY.TANTALLOY_61.getGear(1)});
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Geothermal_Engine_IV.get(1L, new Object[0]),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"CEC", "GMG", "PWP",
			Character.valueOf('M'), ItemList.Hull_IV,
			Character.valueOf('P'), ItemList.Electric_Piston_IV,
			Character.valueOf('E'), ItemList.Electric_Motor_IV,
			Character.valueOf('C'), GregtechOrePrefixes.circuit.get(GT_Materials.Symbiotic),
			Character.valueOf('W'), OrePrefixes.cableGt04.get(Materials.Platinum),
			Character.valueOf('G'), ALLOY.STELLITE.getGear(1)});
		
		final ItemStack INGREDIENT_1 = RECIPE_CONSTANTS.electricPiston_LuV;
		final ItemStack INGREDIENT_2 = RECIPE_CONSTANTS.electricMotor_LuV;
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Geothermal_Engine_LuV.get(1L, new Object[0]),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"CEC", "GMG", "PWP", 
			Character.valueOf('M'), ItemList.Hull_LuV,
			Character.valueOf('P'), INGREDIENT_1, 
			Character.valueOf('E'), INGREDIENT_2, 
			Character.valueOf('C'), GregtechOrePrefixes.circuit.get(GT_Materials.Neutronic), 
			Character.valueOf('W'), OrePrefixes.cableGt04.get(Materials.Tungsten),
			Character.valueOf('G'), ALLOY.ZERON_100.getGear(1)});
	}

}
