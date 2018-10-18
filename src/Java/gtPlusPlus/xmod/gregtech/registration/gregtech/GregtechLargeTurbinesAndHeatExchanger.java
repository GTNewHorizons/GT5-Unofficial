package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Turbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_SHSteam;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.turbines.GT_MTE_LargeTurbine_Steam;

public class GregtechLargeTurbinesAndHeatExchanger {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Larger Turbines & Extra Large Heat Exchanger.");
				run1();			
		}

	}

	private static void run1() {		
		 GregtechItemList.Large_Steam_Turbine.set(new GT_MTE_LargeTurbine_Steam(865, "multimachine.largerturbine", "XL Turbo Steam Turbine").getStackForm(1L));
		 GregtechItemList.Large_HPSteam_Turbine.set(new GT_MTE_LargeTurbine_SHSteam(866, "multimachine.largerhpturbine", "XL Turbo HP Steam Turbine").getStackForm(1L));
		 GregtechItemList.Hatch_Turbine_Rotor.set(new GT_MetaTileEntity_Hatch_Turbine(30010, "hatch.turbine", "Rotor Assembly", 8).getStackForm(1L));  
		 
	}
}