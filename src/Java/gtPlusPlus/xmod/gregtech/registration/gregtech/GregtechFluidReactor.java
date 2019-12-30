package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.GregtechMetaTileEntity_ChemicalReactor;

public class GregtechFluidReactor {

	public static void run() {
			
			GregtechItemList.FluidReactor_LV
			.set(new GregtechMetaTileEntity_ChemicalReactor(31021, "chemicalplant.01.tier.01", "Chemical Plant I", 1)
					.getStackForm(1L));	
			GregtechItemList.FluidReactor_HV
			.set(new GregtechMetaTileEntity_ChemicalReactor(31022, "chemicalplant.01.tier.02", "Chemical Plant II", 3)
					.getStackForm(1L));
			GregtechItemList.FluidReactor_IV
			.set(new GregtechMetaTileEntity_ChemicalReactor(31023, "chemicalplant.01.tier.03", "Chemical Plant III", 5)
					.getStackForm(1L));
			GregtechItemList.FluidReactor_ZPM
			.set(new GregtechMetaTileEntity_ChemicalReactor(31024, "chemicalplant.01.tier.04", "Chemical Plant IV", 7)
					.getStackForm(1L));
		
	}
}
