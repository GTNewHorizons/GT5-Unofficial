package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.OreDictNames;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBattery;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBattery;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.GregtechMetaTileEntity_PowerSubStationController;

public class GregtechPowerSubStation {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Power Substation Node.");
			if (CORE.configSwitches.enableMultiblock_PowerSubstation) {
				run1();
			}
		}

	}

	private static void run1() {
		// Steam Condensors
		GregtechItemList.PowerSubStation.set(new GregtechMetaTileEntity_PowerSubStationController(812,
				"substation.01.input.single", "Power Station Control Node").getStackForm(1L));
		int tID = 886;
		GregtechItemList.Hatch_Input_Battery_MV.set(new GT_MetaTileEntity_Hatch_InputBattery(tID++, "hatch.input_battery.tier.00", "Charging Bus (MV)", 0).getStackForm(1L));
		GregtechItemList.Hatch_Input_Battery_EV.set(new GT_MetaTileEntity_Hatch_InputBattery(tID++, "hatch.input_battery.tier.01", "Charging Bus (EV)", 1).getStackForm(1L));

		GregtechItemList.Hatch_Output_Battery_MV.set(new GT_MetaTileEntity_Hatch_OutputBattery(tID++, "hatch.output_battery.tier.00", "Discharging Bus (MV)", 0).getStackForm(1L));
		GregtechItemList.Hatch_Output_Battery_EV.set(new GT_MetaTileEntity_Hatch_OutputBattery(tID++, "hatch.output_battery.tier.01", "Discharging Bus (EV)", 1).getStackForm(1L));
        
        GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Input_Battery_MV.get(1L, new Object[0]), CI.bitsd, new Object[]{"C", "M", 'M', ItemList.Hull_MV, 'C', ItemList.Battery_Buffer_2by2_MV});
        GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Input_Battery_EV.get(1L, new Object[0]), CI.bitsd, new Object[]{"C", "M", 'M', ItemList.Hull_EV, 'C', ItemList.Battery_Buffer_4by4_EV});
        GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Output_Battery_MV.get(1L, new Object[0]), CI.bitsd, new Object[]{"M", "C", 'M', ItemList.Hull_MV, 'C', ItemList.Battery_Buffer_2by2_MV});
        GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Output_Battery_EV.get(1L, new Object[0]), CI.bitsd, new Object[]{"M", "C", 'M', ItemList.Hull_EV, 'C', ItemList.Battery_Buffer_4by4_EV});
		
	}
}
