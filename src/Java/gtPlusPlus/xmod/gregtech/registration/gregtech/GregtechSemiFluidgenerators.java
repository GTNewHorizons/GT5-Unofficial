package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GT_MetaTileEntity_SemiFluidGenerator;

public class GregtechSemiFluidgenerators {

	public static void run(){
		GregtechItemList.Generator_SemiFluid_LV.set(new GT_MetaTileEntity_SemiFluidGenerator(837, "basicgenerator.semifluid.tier.01",
				"Basic Semi-Fluid Generator", 1).getStackForm(1L));
		GregtechItemList.Generator_SemiFluid_MV.set(new GT_MetaTileEntity_SemiFluidGenerator(838, "basicgenerator.semifluid.tier.02",
				"Advanced Semi-Fluid Generator", 2).getStackForm(1L));
		GregtechItemList.Generator_SemiFluid_HV.set(new GT_MetaTileEntity_SemiFluidGenerator(839, "basicgenerator.semifluid.tier.03",
				"Turbo Semi-Fluid Generator", 3).getStackForm(1L));
	}
	
}
