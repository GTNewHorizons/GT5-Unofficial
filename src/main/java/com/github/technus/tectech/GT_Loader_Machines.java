package com.github.technus.tectech;

import com.github.technus.tectech.elementalMatter.machine.*;

/**
 * Created by danie_000 on 16.11.2016.
 */
public class GT_Loader_Machines implements Runnable {
    public void run() {
        // ===================================================================================================
        // eM IN
        // ===================================================================================================

        CustomItemList.eM_in_UV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12070, "hatch.emin.tier.08", "UV Elemental Input Hatch",8).getStackForm(1L));

        CustomItemList.eM_in_UHV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12071, "hatch.emin.tier.09", "UHV Elemental Input Hatch",9).getStackForm(1L));

        CustomItemList.eM_in_UEV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12072, "hatch.emin.tier.10", "UEV Elemental Input Hatch",10).getStackForm(1L));

        CustomItemList.eM_in_UIV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12073, "hatch.emin.tier.11", "UIV Elemental Input Hatch",11).getStackForm(1L));

        CustomItemList.eM_in_UMV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12074, "hatch.emin.tier.12", "UMV Elemental Input Hatch",12).getStackForm(1L));

        CustomItemList.eM_in_UXV.set(new GT_MetaTileEntity_Hatch_InputElemental(
                12075, "hatch.emin.tier.13", "UXV Elemental Input Hatch",13).getStackForm(1L));

        // ===================================================================================================
        // eM OUT
        // ===================================================================================================

        CustomItemList.eM_out_UV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12080, "hatch.emout.tier.08", "UV Elemental Output Hatch",8).getStackForm(1L));

        CustomItemList.eM_out_UHV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12081, "hatch.emout.tier.09", "UHV Elemental Output Hatch",9).getStackForm(1L));

        CustomItemList.eM_out_UEV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12082, "hatch.emout.tier.10", "UEV Elemental Output Hatch",10).getStackForm(1L));

        CustomItemList.eM_out_UIV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12083, "hatch.emout.tier.11", "UIV Elemental Output Hatch",11).getStackForm(1L));

        CustomItemList.eM_out_UMV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12084, "hatch.emout.tier.12", "UMV Elemental Output Hatch",12).getStackForm(1L));

        CustomItemList.eM_out_UXV.set(new GT_MetaTileEntity_Hatch_OutputElemental(
                12085, "hatch.emout.tier.13", "UXV Elemental Output Hatch",13).getStackForm(1L));

        // ===================================================================================================
        // eM Waste OUT
        // ===================================================================================================

        CustomItemList.eM_muffler_UV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12090, "hatch.emmuffler.tier.08", "UV Overflow Output Hatch",8,1e10f).getStackForm(1L));

        CustomItemList.eM_muffler_UHV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12091, "hatch.emmuffler.tier.09", "UHV Overflow Output Hatch",9,5e10f).getStackForm(1L));

        CustomItemList.eM_muffler_UEV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12092, "hatch.emmuffler.tier.10", "UEV Overflow Output Hatch",10,25e10f).getStackForm(1L));

        CustomItemList.eM_muffler_UIV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12093, "hatch.emmuffler.tier.11", "UIV Overflow Output Hatch",11,125e10f).getStackForm(1L));

        CustomItemList.eM_muffler_UMV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12094, "hatch.emmuffler.tier.12", "UMV Overflow Output Hatch",12,125e11f).getStackForm(1L));

        CustomItemList.eM_muffler_UXV.set(new GT_MetaTileEntity_Hatch_MufflerElemental(
                12095, "hatch.emmuffler.tier.13", "UXV Overflow Output Hatch",13,125e12f).getStackForm(1L));

        // ===================================================================================================
        // Multi AMP Power INPUTS
        // ===================================================================================================

        CustomItemList.eM_energymulti4_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12100, "hatch.energymulti04.tier.08", "UV 4A Energy Hatch",8,4).getStackForm(1L));
        CustomItemList.eM_energymulti16_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12110, "hatch.energymulti16.tier.08", "UV 16A Energy Hatch",8,16).getStackForm(1L));
        CustomItemList.eM_energymulti64_UV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12120, "hatch.energymulti64.tier.08", "UV 64A Energy Hatch",8,64).getStackForm(1L));

        CustomItemList.eM_energymulti4_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12101, "hatch.energymulti04.tier.09", "UHV 4A Energy Hatch",9,4).getStackForm(1L));
        CustomItemList.eM_energymulti16_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12111, "hatch.energymulti16.tier.09", "UHV 16A Energy Hatch",9,16).getStackForm(1L));
        CustomItemList.eM_energymulti64_UHV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12121, "hatch.energymulti64.tier.09", "UHV 64A Energy Hatch",9,64).getStackForm(1L));

        CustomItemList.eM_energymulti4_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12102, "hatch.energymulti04.tier.10", "UEV 4A Energy Hatch",10,4).getStackForm(1L));
        CustomItemList.eM_energymulti16_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12112, "hatch.energymulti16.tier.10", "UEV 16A Energy Hatch",10,16).getStackForm(1L));
        CustomItemList.eM_energymulti64_UEV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12122, "hatch.energymulti64.tier.10", "UEV 64A Energy Hatch",10,64).getStackForm(1L));

        CustomItemList.eM_energymulti4_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12103, "hatch.energymulti04.tier.11", "UIV 4A Energy Hatch",11,4).getStackForm(1L));
        CustomItemList.eM_energymulti16_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12113, "hatch.energymulti16.tier.11", "UIV 16A Energy Hatch",11,16).getStackForm(1L));
        CustomItemList.eM_energymulti64_UIV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12123, "hatch.energymulti64.tier.11", "UIV 64A Energy Hatch",11,64).getStackForm(1L));

        CustomItemList.eM_energymulti4_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12104, "hatch.energymulti04.tier.12", "UMV 4A Energy Hatch",12,4).getStackForm(1L));
        CustomItemList.eM_energymulti16_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12114, "hatch.energymulti16.tier.12", "UMV 16A Energy Hatch",12,16).getStackForm(1L));
        CustomItemList.eM_energymulti64_UMV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12124, "hatch.energymulti64.tier.12", "UMV 64A Energy Hatch",12,64).getStackForm(1L));

        CustomItemList.eM_energymulti4_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12105, "hatch.energymulti04.tier.13", "UXV 4A Energy Hatch",13,4).getStackForm(1L));
        CustomItemList.eM_energymulti16_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12115, "hatch.energymulti16.tier.13", "UXV 16A Energy Hatch",13,16).getStackForm(1L));
        CustomItemList.eM_energymulti64_UXV.set(new GT_MetaTileEntity_Hatch_EnergyMulti(
                12125, "hatch.energymulti64.tier.13", "UXV 64A Energy Hatch",13,64).getStackForm(1L));


        // ===================================================================================================
        // Multi AMP Power OUTPUTS
        // ===================================================================================================

        CustomItemList.eM_dynamomulti4_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12130, "hatch.dynamomulti04.tier.08", "UV 4A Dynamo Hatch",8,4).getStackForm(1L));
        CustomItemList.eM_dynamomulti16_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12140, "hatch.dynamomulti16.tier.08", "UV 16A Dynamo Hatch",8,16).getStackForm(1L));
        CustomItemList.eM_dynamomulti64_UV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12150, "hatch.dynamomulti64.tier.08", "UV 64A Dynamo Hatch",8,64).getStackForm(1L));

        CustomItemList.eM_dynamomulti4_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12131, "hatch.dynamomulti04.tier.09", "UHV 4A Dynamo Hatch",9,4).getStackForm(1L));
        CustomItemList.eM_dynamomulti16_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12141, "hatch.dynamomulti16.tier.09", "UHV 16A Dynamo Hatch",9,16).getStackForm(1L));
        CustomItemList.eM_dynamomulti64_UHV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12151, "hatch.dynamomulti64.tier.09", "UHV 64A Dynamo Hatch",9,64).getStackForm(1L));

        CustomItemList.eM_dynamomulti4_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12132, "hatch.dynamomulti04.tier.10", "UEV 4A Dynamo Hatch",10,4).getStackForm(1L));
        CustomItemList.eM_dynamomulti16_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12142, "hatch.dynamomulti16.tier.10", "UEV 16A Dynamo Hatch",10,16).getStackForm(1L));
        CustomItemList.eM_dynamomulti64_UEV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12152, "hatch.dynamomulti64.tier.10", "UEV 64A Dynamo Hatch",10,64).getStackForm(1L));

        CustomItemList.eM_dynamomulti4_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12133, "hatch.dynamomulti04.tier.11", "UIV 4A Dynamo Hatch",11,4).getStackForm(1L));
        CustomItemList.eM_dynamomulti16_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12143, "hatch.dynamomulti16.tier.11", "UIV 16A Dynamo Hatch",11,16).getStackForm(1L));
        CustomItemList.eM_dynamomulti64_UIV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12153, "hatch.dynamomulti64.tier.11", "UIV 64A Dynamo Hatch",11,64).getStackForm(1L));

        CustomItemList.eM_dynamomulti4_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12134, "hatch.dynamomulti04.tier.12", "UMV 4A Dynamo Hatch",12,4).getStackForm(1L));
        CustomItemList.eM_dynamomulti16_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12144, "hatch.dynamomulti16.tier.12", "UMV 16A Dynamo Hatch",12,16).getStackForm(1L));
        CustomItemList.eM_dynamomulti64_UMV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12154, "hatch.dynamomulti64.tier.12", "UMV 64A Dynamo Hatch",12,64).getStackForm(1L));

        CustomItemList.eM_dynamomulti4_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12135, "hatch.dynamomulti04.tier.13", "UXV 4A Dynamo Hatch",13,4).getStackForm(1L));
        CustomItemList.eM_dynamomulti16_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12145, "hatch.dynamomulti16.tier.13", "UXV 16A Dynamo Hatch",13,16).getStackForm(1L));
        CustomItemList.eM_dynamomulti64_UXV.set(new GT_MetaTileEntity_Hatch_DynamoMulti(
                12155, "hatch.dynamomulti64.tier.13", "UXV 64A Dynamo Hatch",13,64).getStackForm(1L));

        // ===================================================================================================
        // MULTIBLOCKS EM
        // ===================================================================================================

        CustomItemList.Machine_Multi_Transformer.set(new GT_MetaTileEntity_EMtransformer(12160,"multimachine.em.transformer", "Active Transformer").getStackForm(1L));
        CustomItemList.Machine_Multi_MatterToEM.set(new GT_MetaTileEntity_EMquantifier(12161,"multimachine.em.mattertoem","Matter Quantifier").getStackForm(1L));
        CustomItemList.Machine_Multi_EMToMatter.set(new GT_MetaTileEntity_EMdequantifier(12162,"multimachine.em.emtomatter","Matter De-quantifier").getStackForm(1L));
        CustomItemList.Machine_Multi_EMjunction.set(new GT_MetaTileEntity_EMjunction(12163,"multimachine.em.junction","Matter junction").getStackForm(1L));
        CustomItemList.Machine_Multi_EMmachine.set(new GT_MetaTileEntity_EMmachine(12164,"multimachine.em.processing","Quantum Processing Machine").getStackForm(1L));
        CustomItemList.Machine_Multi_EMCrafter.set(new GT_MetaTileEntity_EMcrafter(12165,"multimachine.em.crafter","Matter Assembler").getStackForm(1L));
        CustomItemList.Machine_Multi_Collider.set(new GT_MetaTileEntity_EMcollider(12166,"multimachine.em.collider","Matter Collider").getStackForm(1L));
        CustomItemList.Machine_Multi_BHG.set(new GT_MetaTileEntity_EMbhg(12167,"multimachine.em.blackholegenerator","Black Hole Generator").getStackForm(1L));
        CustomItemList.Machine_Multi_Wormhole.set(new GT_MetaTileEntity_EMwormhole(12168,"multimachine.em.wormhole","Wormhole").getStackForm(1L));
        CustomItemList.Machine_Multi_Scanner.set(new GT_MetaTileEntity_EMscanner(12169,"multimachine.em.scanner","Elemental Scanner").getStackForm(1L));
        CustomItemList.Machine_Multi_Computer.set(new GT_MetaTileEntity_Computer(12170,"multimachine.em.computer","Quantum computer").getStackForm(1L));

        // ===================================================================================================
        // Hatches EM
        // ===================================================================================================
        CustomItemList.Parametrizer_Hatch.set(new GT_MetaTileEntity_Hatch_Param(12180,"hatch.param.tier.06","Parametrizer for machines",6).getStackForm(1L));
        CustomItemList.Uncertainty_Hatch.set(new GT_MetaTileEntity_Hatch_Uncertainty(12181,"hatch.emcertain.tier.06","Uncertainty resolver",6).getStackForm(1L));
        CustomItemList.UncertaintyX_Hatch.set(new GT_MetaTileEntity_Hatch_Uncertainty(12182,"hatch.emcertain.tier.10","Uncertainty resolver X",10).getStackForm(1L));

        // ===================================================================================================
        // EM pipe
        // ===================================================================================================
        CustomItemList.EMpipe.set(new GT_MetaTileEntity_EMpipe(12179,"pipe.elementalmatter","Quantum tunnel").getStackForm(1L));
    }
}
