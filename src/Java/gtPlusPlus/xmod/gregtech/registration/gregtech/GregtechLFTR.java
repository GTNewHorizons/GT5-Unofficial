package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_ReactorColdTrap;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_ReactorProcessingUnit;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMTE_NuclearReactor;

public class GregtechLFTR {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Liquid Fluorine Thorium Reactor [LFTR].");
			if (CORE.ConfigSwitches.enableMultiblock_LiquidFluorideThoriumReactor) {
				run1();
			}
		}

	}

	private static void run1() {
		// LFTR
		GregtechItemList.ThoriumReactor.set(new GregtechMTE_NuclearReactor(751, "lftr.controller.single", "Thorium Reactor [LFTR]").getStackForm(1L));
		// Reactor Processing Units
		GregtechItemList.ReactorProcessingUnit_IV.set(new GregtechMetaTileEntity_ReactorProcessingUnit(31031, "rpu.tier.01", "Reactor Processing Unit I", 5).getStackForm(1L));
		GregtechItemList.ReactorProcessingUnit_ZPM.set(new GregtechMetaTileEntity_ReactorProcessingUnit(31032, "rpu.tier.02", "Reactor Processing Unit II", 7).getStackForm(1L));
		// Cold Traps
		GregtechItemList.ColdTrap_IV.set(new GregtechMetaTileEntity_ReactorColdTrap(31033, "coldtrap.tier.01", "Cold Trap I", 5).getStackForm(1L));
		GregtechItemList.ColdTrap_ZPM.set(new GregtechMetaTileEntity_ReactorColdTrap(31034, "coldtrap.tier.02", "Cold Trap II", 7).getStackForm(1L));

	}
}
