package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntitySolarGenerator;

public class GregtechSolarGenerators {
	public static void run() {
		if (LoadedMods.Gregtech && configSwitches.enableMachine_SolarGenerators) {
			Utils.LOG_INFO("Gregtech5u Content | Registering Solar Generator Blocks.");
			if (CORE.configSwitches.enableMachine_SolarGenerators) {
				run1();
			}
		}

	}

	private static void run1() {

		GregtechItemList.GT_Solar_ULV.set(new GregtechMetaTileEntitySolarGenerator(800, "sunabsorber.tier.00",
				"Ultra Low Voltage Solar Generator", 0).getStackForm(1L));
		GregtechItemList.GT_Solar_LV.set(
				new GregtechMetaTileEntitySolarGenerator(801, "sunabsorber.tier.01", "Low Voltage Solar Generator", 1)
						.getStackForm(1L));
		GregtechItemList.GT_Solar_MV.set(new GregtechMetaTileEntitySolarGenerator(802, "sunabsorber.tier.02",
				"Medium Voltage Solar Generator", 2).getStackForm(1L));
		GregtechItemList.GT_Solar_HV.set(
				new GregtechMetaTileEntitySolarGenerator(803, "sunabsorber.tier.03", "High Voltage Solar Generator", 3)
						.getStackForm(1L));
		GregtechItemList.GT_Solar_EV.set(new GregtechMetaTileEntitySolarGenerator(804, "sunabsorber.tier.04",
				"Extreme Voltage Solar Generator", 4).getStackForm(1L));
		GregtechItemList.GT_Solar_IV.set(new GregtechMetaTileEntitySolarGenerator(805, "sunabsorber.tier.05",
				"Insane Voltage Solar Generator", 5).getStackForm(1L));
		GregtechItemList.GT_Solar_LuV.set(new GregtechMetaTileEntitySolarGenerator(806, "sunabsorber.tier.06",
				"Ludicrous Voltage Solar Generator", 6).getStackForm(1L));
		GregtechItemList.GT_Solar_ZPM.set(
				new GregtechMetaTileEntitySolarGenerator(807, "sunabsorber.tier.07", "ZPM Voltage Solar Generator", 7)
						.getStackForm(1L));
		GregtechItemList.GT_Solar_UV.set(new GregtechMetaTileEntitySolarGenerator(808, "sunabsorber.tier.08",
				"Ultimate Voltage Solar Generator", 8).getStackForm(1L));
		GregtechItemList.GT_Solar_MAX.set(
				new GregtechMetaTileEntitySolarGenerator(809, "sunabsorber.tier.09", "MAX Voltage Solar Generator", 9)
						.getStackForm(1L));

	}
}
