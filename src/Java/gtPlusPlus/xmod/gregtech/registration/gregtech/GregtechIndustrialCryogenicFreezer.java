package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialVacuumFreezer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_EBF;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_Fusion_MK4;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_Implosion;

public class GregtechIndustrialCryogenicFreezer {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			run1();
		}	
	}

	private static void run1() {
		// Gregtech 4 Multiblock Auto-Crafter
		Logger.INFO("Gregtech 5 Content | Registering Advanced GT Multiblock replacements.");		
		GregtechItemList.Machine_Adv_BlastFurnace.set(new GregtechMetaTileEntity_Adv_EBF(963, "multimachine.adv.blastfurnace", "[Factory Grade] Advanced Blast Furnace").getStackForm(1L));
		GregtechItemList.Machine_Adv_ImplosionCompressor.set(new GregtechMetaTileEntity_Adv_Implosion(964, "multimachine.adv.implosioncompressor", "[Factory Grade] Advanced Implosion Compressor").getStackForm(1L));
		GregtechItemList.Industrial_Cryogenic_Freezer.set(new GregtechMetaTileEntity_IndustrialVacuumFreezer(910, "industrialfreezer.controller.tier.single", "Advanced Cryogenic Freezer").getStackForm(1L));		
		GregtechItemList.FusionComputer_UV2.set(new GregtechMetaTileEntity_Adv_Fusion_MK4(965, "fusioncomputer.tier.09", "FusionTek MK IV").getStackForm(1L));


	}

}
