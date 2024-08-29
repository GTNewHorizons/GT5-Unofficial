package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.FusionComputer_UV2;
import static gregtech.api.enums.MetaTileEntityIDs.FusionComputer_UV3;
import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Cryogenic_Freezer;
import static gregtech.api.enums.MetaTileEntityIDs.Machine_Adv_BlastFurnace;
import static gregtech.api.enums.MetaTileEntityIDs.Machine_Adv_DistillationTower;
import static gregtech.api.enums.MetaTileEntityIDs.Machine_Adv_ImplosionCompressor;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialVacuumFreezer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_DistillationTower;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_EBF;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_Fusion_MK4;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_Fusion_MK5;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.advanced.GregtechMetaTileEntity_Adv_Implosion;

public class GregtechFactoryGradeReplacementMultis {

    public static void run() {
        run1();
    }

    private static void run1() {
        Logger.INFO("Gregtech 5 Content | Registering Advanced GT Multiblock replacements.");
        GregtechItemList.Machine_Adv_BlastFurnace.set(
            new GregtechMetaTileEntity_Adv_EBF(Machine_Adv_BlastFurnace.ID, "multimachine.adv.blastfurnace", "Volcanus")
                .getStackForm(1L));
        GregtechItemList.Machine_Adv_ImplosionCompressor.set(
            new GregtechMetaTileEntity_Adv_Implosion(
                Machine_Adv_ImplosionCompressor.ID,
                "multimachine.adv.implosioncompressor",
                "Density^2").getStackForm(1L));
        GregtechItemList.Industrial_Cryogenic_Freezer.set(
            new GregtechMetaTileEntity_IndustrialVacuumFreezer(
                Industrial_Cryogenic_Freezer.ID,
                "multimachine.adv.industrialfreezer",
                "Cryogenic Freezer").getStackForm(1L));
        GregtechItemList.FusionComputer_UV2.set(
            new GregtechMetaTileEntity_Adv_Fusion_MK4(
                FusionComputer_UV2.ID,
                "fusioncomputer.tier.09",
                "FusionTech MK IV").getStackForm(1L));
        GregtechItemList.FusionComputer_UV3.set(
            new GregtechMetaTileEntity_Adv_Fusion_MK5(
                FusionComputer_UV3.ID,
                "fusioncomputer.tier.10",
                "FusionTech MK V").getStackForm(1L));

        // 31021
        GregtechItemList.Machine_Adv_DistillationTower.set(
            new GregtechMetaTileEntity_Adv_DistillationTower(
                Machine_Adv_DistillationTower.ID,
                "multimachine.adv.distillationtower",
                "Dangote Distillus").getStackForm(1L));
    }
}
