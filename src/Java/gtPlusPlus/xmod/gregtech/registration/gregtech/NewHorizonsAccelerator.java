package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntityGeothermalGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_WorldAccelerator;
import net.minecraft.item.ItemStack;

public class NewHorizonsAccelerator {

	public static void run()
	{
		if (LoadedMods.Gregtech && !CORE.GTNH){
			Utils.LOG_INFO("New Horizons Content | Registering World Accelerators.");
			if (CORE.configSwitches.enableMachine_WorldAccelerators) {
				run1();
			}
		}

	}

	private static void run1(){
		GregtechItemList.AcceleratorLV.set(new GT_MetaTileEntity_WorldAccelerator(
				11100, "basicmachine.accelerator.tier.01", "Basic World Accelerator", 1).getStackForm(1L));
		GregtechItemList.AcceleratorMV.set(new GT_MetaTileEntity_WorldAccelerator(
				11101, "basicmachine.accelerator.tier.02", "Advanced World Accelerator", 2).getStackForm(1L));
		GregtechItemList.AcceleratorHV.set(new GT_MetaTileEntity_WorldAccelerator(
				11102, "basicmachine.accelerator.tier.03", "Advanced World Accelerator II", 3).getStackForm(1L));
		GregtechItemList.AcceleratorEV.set(new GT_MetaTileEntity_WorldAccelerator(
				11103, "basicmachine.accelerator.tier.04", "Advanced World Accelerator III", 4).getStackForm(1L));
		GregtechItemList.AcceleratorIV.set(new GT_MetaTileEntity_WorldAccelerator(
				11104, "basicmachine.accelerator.tier.05", "Advanced World Accelerator IV", 5).getStackForm(1L));
		GregtechItemList.AcceleratorLuV.set(new GT_MetaTileEntity_WorldAccelerator(
				11105, "basicmachine.accelerator.tier.06", "Elite World Accelerator", 6).getStackForm(1L));
		GregtechItemList.AcceleratorZPM.set(new GT_MetaTileEntity_WorldAccelerator(
				11106, "basicmachine.accelerator.tier.07", "Elite World Accelerator II", 7).getStackForm(1L));
		GregtechItemList.AcceleratorUV.set(new GT_MetaTileEntity_WorldAccelerator(
				11107, "basicmachine.accelerator.tier.08", "Ultimate Time Anomaly", 8).getStackForm(1L));
		
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorLV.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_LV,
						'M', ItemList.Electric_Motor_LV,
						'P', ItemList.Electric_Pump_LV,
						'B', ItemList.Hull_LV,
						'C', ItemList.Conveyor_Module_LV,
						'I', ItemList.Electric_Piston_LV});

		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorMV.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_MV,
						'M', ItemList.Electric_Motor_MV,
						'P', ItemList.Electric_Pump_MV,
						'B', ItemList.Hull_MV,
						'C', ItemList.Conveyor_Module_MV,
						'I', ItemList.Electric_Piston_MV});

		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorHV.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_HV,
						'M', ItemList.Electric_Motor_HV,
						'P', ItemList.Electric_Pump_HV,
						'B', ItemList.Hull_HV,
						'C', ItemList.Conveyor_Module_HV,
						'I', ItemList.Electric_Piston_HV});

		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorEV.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_EV,
						'M', ItemList.Electric_Motor_EV,
						'P', ItemList.Electric_Pump_EV,
						'B', ItemList.Hull_EV,
						'C', ItemList.Conveyor_Module_EV,
						'I', ItemList.Electric_Piston_EV});

		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorIV.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_IV,
						'M', ItemList.Electric_Motor_IV,
						'P', ItemList.Electric_Pump_IV,
						'B', ItemList.Hull_IV,
						'C', ItemList.Conveyor_Module_IV,
						'I', ItemList.Electric_Piston_IV});

		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorLuV.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_LuV,
						'M', ItemList.Electric_Motor_LuV,
						'P', ItemList.Electric_Pump_LuV,
						'B', ItemList.Hull_LuV,
						'C', ItemList.Conveyor_Module_LuV,
						'I', ItemList.Electric_Piston_LuV});

		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorZPM.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_ZPM,
						'M', ItemList.Electric_Motor_ZPM,
						'P', ItemList.Electric_Pump_ZPM,
						'B', ItemList.Hull_ZPM,
						'C', ItemList.Conveyor_Module_ZPM,
						'I', ItemList.Electric_Piston_ZPM});

		GT_ModHandler.addCraftingRecipe(GregtechItemList.AcceleratorUV.get(1L),
				GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE | GT_ModHandler.RecipeBits.BUFFERED,
				new Object[]{"RMR", "PBC", "IMI",
						'R', ItemList.Robot_Arm_UV,
						'M', ItemList.Electric_Motor_UV,
						'P', ItemList.Electric_Pump_UV,
						'B', ItemList.Hull_UV,
						'C', ItemList.Conveyor_Module_UV,
						'I', ItemList.Electric_Piston_UV});
	}

}
