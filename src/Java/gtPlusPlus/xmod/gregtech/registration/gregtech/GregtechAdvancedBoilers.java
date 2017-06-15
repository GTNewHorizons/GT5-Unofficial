package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.RECIPES_MachineComponents;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.*;
import net.minecraft.item.ItemStack;

public class GregtechAdvancedBoilers {

	public static void run() {
		if (LoadedMods.Gregtech){
			Utils.LOG_INFO("Gregtech5u Content | Registering Advanced Boilers.");
			run1();
		}
	}

	private static void run1(){
		//Boilers
		GregtechItemList.Boiler_Advanced_LV.set(new GT_MetaTileEntity_Boiler_LV(753, "Advanced Boiler [LV]", 1).getStackForm(1L));
		GregtechItemList.Boiler_Advanced_MV.set(new GT_MetaTileEntity_Boiler_MV(754, "Advanced Boiler [MV]", 2).getStackForm(1L));
		GregtechItemList.Boiler_Advanced_HV.set(new GT_MetaTileEntity_Boiler_HV(755, "Advanced Boiler [HV]", 3).getStackForm(1L));
		
		
		ItemStack chassisT1 = ItemUtils.getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 0, 1);
		ItemStack chassisT2 = ItemUtils.getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 1, 1);
		ItemStack chassisT3 = ItemUtils.getItemStackWithMeta(true, "miscutils:itemBoilerChassis", "Boiler_Chassis_T1", 2, 1);


		//Make the Coil in each following recipe a hammer and a Screwdriver.

		
		//Chassis Recipes
		GT_ModHandler.addCraftingRecipe(
		chassisT1,
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"WCW", "GMG", "WPW",
			Character.valueOf('M'), ItemList.Hull_ULV,
			Character.valueOf('P'), OrePrefixes.pipeLarge.get(Materials.Bronze),
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Primitive),
			Character.valueOf('W'), OrePrefixes.plate.get(Materials.Lead),
			Character.valueOf('G'), OrePrefixes.pipeSmall.get(Materials.Copper)});

		GT_ModHandler.addCraftingRecipe(
		chassisT2,
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"WCW", "GMG", "WPW",
			Character.valueOf('M'), ItemList.Hull_LV,
			Character.valueOf('P'), OrePrefixes.pipeLarge.get(Materials.Steel),
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic),
			Character.valueOf('W'), OrePrefixes.plate.get(Materials.Steel),
			Character.valueOf('G'), OrePrefixes.pipeSmall.get(Materials.Bronze)});

		GT_ModHandler.addCraftingRecipe(
		chassisT3,
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"WCW", "GMG", "WPW",
			Character.valueOf('M'), ItemList.Hull_MV,
			Character.valueOf('P'), OrePrefixes.pipeLarge.get(Materials.StainlessSteel),
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good),
			Character.valueOf('W'), OrePrefixes.plate.get(Materials.Aluminium),
			Character.valueOf('G'), OrePrefixes.pipeSmall.get(Materials.Steel)});

		
		ItemStack pipeTier1 = ItemUtils.getItemStackOfAmountFromOreDict(RECIPES_MachineComponents.pipeTier7, 1);
		ItemStack pipeTier2 = ItemUtils.getItemStackOfAmountFromOreDict(RECIPES_MachineComponents.pipeTier8, 1);
		ItemStack pipeTier3 = ItemUtils.getItemStackOfAmountFromOreDict(RECIPES_MachineComponents.pipeTier9, 1);
		
		//Boiler Recipes
		GT_ModHandler.addCraftingRecipe(
		GregtechItemList.Boiler_Advanced_LV.get(1L, new Object[0]),
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"dCw", "WMW", "GPG",
			Character.valueOf('M'), ItemList.Hull_LV,
			Character.valueOf('P'), pipeTier1,
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Basic),
			Character.valueOf('W'), chassisT1,
			Character.valueOf('G'), OrePrefixes.gear.get(Materials.Steel)});

		GT_ModHandler.addCraftingRecipe(
		GregtechItemList.Boiler_Advanced_MV.get(1L, new Object[0]),
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"dCw", "WMW", "GPG",
			Character.valueOf('M'), ItemList.Hull_MV,
			Character.valueOf('P'), pipeTier2,
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Good),
			Character.valueOf('W'), chassisT2,
			Character.valueOf('G'), ALLOY.SILICON_CARBIDE.getGear(1)});

		GT_ModHandler.addCraftingRecipe(
		GregtechItemList.Boiler_Advanced_HV.get(1L, new Object[0]),
		GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
		new Object[]{"dCw", "WMW", "GPG",
			Character.valueOf('M'), ItemList.Hull_HV,
			Character.valueOf('P'), pipeTier3,
			Character.valueOf('C'), OrePrefixes.circuit.get(Materials.Advanced),
			Character.valueOf('W'), chassisT3,
			Character.valueOf('G'), ALLOY.SILICON_CARBIDE.getGear(1)});

		
	}
	
}