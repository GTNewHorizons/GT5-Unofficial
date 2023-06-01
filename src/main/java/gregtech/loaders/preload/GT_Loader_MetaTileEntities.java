package gregtech.loaders.preload;

import static gregtech.api.enums.Mods.*;

import net.minecraft.util.EnumChatFormatting;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.util.*;
import gregtech.common.tileentities.automation.*;
import gregtech.common.tileentities.boilers.*;
import gregtech.common.tileentities.debug.*;
import gregtech.common.tileentities.generators.*;
import gregtech.common.tileentities.machines.*;
import gregtech.common.tileentities.machines.basic.*;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineFluid;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineItem;
import gregtech.common.tileentities.machines.multi.*;
import gregtech.common.tileentities.machines.steam.*;
import gregtech.common.tileentities.storage.*;

// Free IDs left for machines in GT as of 29th of July 2022 - Colen. Please try use them up in order.
// 358
// 359
// 366
// 367
// 368
// 369
// 370
// 376
// 377
// 378
// 379
// 386
// 387
// 388
// 389
// 390
// 396
// 397
// 398
// 399
// 410
// 419
// 426
// 427
// 428
// 429
// 430
// 436
// 437
// 438
// 439
// 446
// 447
// 448
// 449
// 450
// 456
// 457
// 458
// 459
// 466
// 467
// 468
// 469
// 470
// 476
// 477
// 478
// 479
// 486
// 487
// 488
// 489
// 496
// 497
// 498
// 499
// 506
// 507
// 508
// 509
// 518
// 519
// 526
// 530
// 537
// 538
// 539
// 546
// 547
// 548
// 549
// 550
// 556
// 557
// 558
// 559
// 566
// 567
// 576
// 577
// 578
// 579
// 586
// 587
// 588
// 589
// 590
// 596
// 597
// 598
// 599
// 607
// 608
// 609
// 610
// 616
// 617
// 618
// 619
// 626
// 627
// 628
// 629
// 630
// 636
// 637
// 639
// 646
// 647
// 648
// 649
// 650
// 656
// 657
// 658
// 659
// 666
// 667
// 668
// 669
// 670
// 676
// 677
// 678
// 682
// 683
// 684
// 686
// 687
// 688
// 689
// 702
// 703
// 704
// 705
// 706
// 707
// 708
// 709
// 714
// 715
// 716
// 717
// 718
// 719
// 721
// 722
// 723
// 724
// 725
// 726
// 727
// 728
// 729
// 730
// 731
// 732
// 733
// 734
// 735
// 736
// 737
// 738
// 739
// 741
// 742
// 743
// 744
// 745
// 746
// 747
// 748
// 749

// TODO Some GT MetaTileEntity registrations are done in load/GT_Loader_MetaTileEntities_Recipes.java due to joint
// registration+recipe methods, they should be split and brought here to register all in preload.

public class GT_Loader_MetaTileEntities implements Runnable { // TODO CHECK CIRCUIT RECIPES AND USAGES

    private static final String aTextWire1 = "wire.";
    private static final String aTextCable1 = "cable.";
    private static final String aTextWire2 = " Wire";
    private static final String aTextCable2 = " Cable";
    public static final String imagination = EnumChatFormatting.RESET + "You just need "
        + EnumChatFormatting.DARK_PURPLE
        + "I"
        + EnumChatFormatting.LIGHT_PURPLE
        + "m"
        + EnumChatFormatting.DARK_RED
        + "a"
        + EnumChatFormatting.RED
        + "g"
        + EnumChatFormatting.YELLOW
        + "i"
        + EnumChatFormatting.GREEN
        + "n"
        + EnumChatFormatting.AQUA
        + "a"
        + EnumChatFormatting.DARK_AQUA
        + "t"
        + EnumChatFormatting.BLUE
        + "i"
        + EnumChatFormatting.DARK_BLUE
        + "o"
        + EnumChatFormatting.DARK_PURPLE
        + "n"
        + EnumChatFormatting.RESET
        + " to use this.";

    private static void run1() {

        ItemList.Machine_Bricked_BlastFurnace.set(
            new GT_MetaTileEntity_BrickedBlastFurnace(140, "multimachine.brickedblastfurnace", "Bricked Blast Furnace")
                .getStackForm(1L));
        ItemList.Hull_Bronze.set(
            new GT_MetaTileEntity_BasicHull_Bronze(1, "hull.bronze", "Bronze Hull", 0, "For your first Steam Machines")
                .getStackForm(1L));
        ItemList.Hull_Bronze_Bricks.set(
            new GT_MetaTileEntity_BasicHull_BronzeBricks(
                2,
                "hull.bronze_bricked",
                "Bricked Bronze Hull",
                0,
                "For your first Steam Machines").getStackForm(1L));
        ItemList.Hull_HP.set(
            new GT_MetaTileEntity_BasicHull_Steel(3, "hull.steel", "Steel Hull", 0, "For improved Steam Machines")
                .getStackForm(1L));
        ItemList.Hull_HP_Bricks.set(
            new GT_MetaTileEntity_BasicHull_SteelBricks(
                4,
                "hull.steel_bricked",
                "Bricked Wrought Iron Hull",
                0,
                "For improved Steam Machines").getStackForm(1L));

        ItemList.Hull_ULV.set(
            new GT_MetaTileEntity_BasicHull(10, "hull.tier.00", "ULV Machine Hull", 0, imagination).getStackForm(1L));
        ItemList.Hull_LV.set(
            new GT_MetaTileEntity_BasicHull(11, "hull.tier.01", "LV Machine Hull", 1, imagination).getStackForm(1L));
        ItemList.Hull_MV.set(
            new GT_MetaTileEntity_BasicHull(12, "hull.tier.02", "MV Machine Hull", 2, imagination).getStackForm(1L));
        ItemList.Hull_HV.set(
            new GT_MetaTileEntity_BasicHull(13, "hull.tier.03", "HV Machine Hull", 3, imagination).getStackForm(1L));
        ItemList.Hull_EV.set(
            new GT_MetaTileEntity_BasicHull(14, "hull.tier.04", "EV Machine Hull", 4, imagination).getStackForm(1L));
        ItemList.Hull_IV.set(
            new GT_MetaTileEntity_BasicHull(15, "hull.tier.05", "IV Machine Hull", 5, imagination).getStackForm(1L));
        ItemList.Hull_LuV.set(
            new GT_MetaTileEntity_BasicHull(16, "hull.tier.06", "LuV Machine Hull", 6, imagination).getStackForm(1L));
        ItemList.Hull_ZPM.set(
            new GT_MetaTileEntity_BasicHull(17, "hull.tier.07", "ZPM Machine Hull", 7, imagination).getStackForm(1L));
        ItemList.Hull_UV.set(
            new GT_MetaTileEntity_BasicHull(18, "hull.tier.08", "UV Machine Hull", 8, imagination).getStackForm(1L));
        ItemList.Hull_MAX.set(
            new GT_MetaTileEntity_BasicHull(19, "hull.tier.09", "UHV Machine Hull", 9, imagination).getStackForm(1L));

        ItemList.Transformer_LV_ULV.set(
            new GT_MetaTileEntity_Transformer(
                20,
                "transformer.tier.00",
                "Ultra Low Voltage Transformer",
                0,
                "LV -> ULV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_MV_LV.set(
            new GT_MetaTileEntity_Transformer(
                21,
                "transformer.tier.01",
                "Low Voltage Transformer",
                1,
                "MV -> LV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_HV_MV.set(
            new GT_MetaTileEntity_Transformer(
                22,
                "transformer.tier.02",
                "Medium Voltage Transformer",
                2,
                "HV -> MV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_EV_HV.set(
            new GT_MetaTileEntity_Transformer(
                23,
                "transformer.tier.03",
                "High Voltage Transformer",
                3,
                "EV -> HV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_IV_EV.set(
            new GT_MetaTileEntity_Transformer(
                24,
                "transformer.tier.04",
                "Extreme Transformer",
                4,
                "IV -> EV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_LuV_IV.set(
            new GT_MetaTileEntity_Transformer(
                25,
                "transformer.tier.05",
                "Insane Transformer",
                5,
                "LuV -> IV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_ZPM_LuV.set(
            new GT_MetaTileEntity_Transformer(
                26,
                "transformer.tier.06",
                "Ludicrous Transformer",
                6,
                "ZPM -> LuV (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_UV_ZPM.set(
            new GT_MetaTileEntity_Transformer(
                27,
                "transformer.tier.07",
                "ZPM Voltage Transformer",
                7,
                "UV -> ZPM (Use Soft Mallet to invert)").getStackForm(1L));
        ItemList.Transformer_MAX_UV.set(
            new GT_MetaTileEntity_Transformer(
                28,
                "transformer.tier.08",
                "Ultimate Transformer",
                8,
                "UHV -> UV (Use Soft Mallet to invert)").getStackForm(1L));

        ItemList.Hatch_Dynamo_ULV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(30, "hatch.dynamo.tier.00", "ULV Dynamo Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Dynamo_LV
            .set(new GT_MetaTileEntity_Hatch_Dynamo(31, "hatch.dynamo.tier.01", "LV Dynamo Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Dynamo_MV
            .set(new GT_MetaTileEntity_Hatch_Dynamo(32, "hatch.dynamo.tier.02", "MV Dynamo Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Dynamo_HV
            .set(new GT_MetaTileEntity_Hatch_Dynamo(33, "hatch.dynamo.tier.03", "HV Dynamo Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Dynamo_EV
            .set(new GT_MetaTileEntity_Hatch_Dynamo(34, "hatch.dynamo.tier.04", "EV Dynamo Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Dynamo_IV
            .set(new GT_MetaTileEntity_Hatch_Dynamo(35, "hatch.dynamo.tier.05", "IV Dynamo Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Dynamo_LuV.set(
            new GT_MetaTileEntity_Hatch_Dynamo(36, "hatch.dynamo.tier.06", "LuV Dynamo Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Dynamo_ZPM.set(
            new GT_MetaTileEntity_Hatch_Dynamo(37, "hatch.dynamo.tier.07", "ZPM Dynamo Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Dynamo_UV
            .set(new GT_MetaTileEntity_Hatch_Dynamo(38, "hatch.dynamo.tier.08", "UV Dynamo Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Dynamo_MAX.set(
            new GT_MetaTileEntity_Hatch_Dynamo(39, "hatch.dynamo.tier.09", "UHV Dynamo Hatch", 9).getStackForm(1L));

        // 1234
        ItemList.Hatch_Energy_ULV.set(
            new GT_MetaTileEntity_Hatch_Energy(40, "hatch.energy.tier.00", "ULV Energy Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Energy_LV
            .set(new GT_MetaTileEntity_Hatch_Energy(41, "hatch.energy.tier.01", "LV Energy Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Energy_MV
            .set(new GT_MetaTileEntity_Hatch_Energy(42, "hatch.energy.tier.02", "MV Energy Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Energy_HV
            .set(new GT_MetaTileEntity_Hatch_Energy(43, "hatch.energy.tier.03", "HV Energy Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Energy_EV
            .set(new GT_MetaTileEntity_Hatch_Energy(44, "hatch.energy.tier.04", "EV Energy Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Energy_IV
            .set(new GT_MetaTileEntity_Hatch_Energy(45, "hatch.energy.tier.05", "IV Energy Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Energy_LuV.set(
            new GT_MetaTileEntity_Hatch_Energy(46, "hatch.energy.tier.06", "LuV Energy Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Energy_ZPM.set(
            new GT_MetaTileEntity_Hatch_Energy(47, "hatch.energy.tier.07", "ZPM Energy Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Energy_UV
            .set(new GT_MetaTileEntity_Hatch_Energy(48, "hatch.energy.tier.08", "UV Energy Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Energy_MAX.set(
            new GT_MetaTileEntity_Hatch_Energy(49, "hatch.energy.tier.09", "UHV Energy Hatch", 9).getStackForm(1L));

        ItemList.Hatch_Input_ULV
            .set(new GT_MetaTileEntity_Hatch_Input(50, "hatch.input.tier.00", "Input Hatch (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Input_LV
            .set(new GT_MetaTileEntity_Hatch_Input(51, "hatch.input.tier.01", "Input Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Input_MV
            .set(new GT_MetaTileEntity_Hatch_Input(52, "hatch.input.tier.02", "Input Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Input_HV
            .set(new GT_MetaTileEntity_Hatch_Input(53, "hatch.input.tier.03", "Input Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Input_EV
            .set(new GT_MetaTileEntity_Hatch_Input(54, "hatch.input.tier.04", "Input Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Input_IV
            .set(new GT_MetaTileEntity_Hatch_Input(55, "hatch.input.tier.05", "Input Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Input_LuV
            .set(new GT_MetaTileEntity_Hatch_Input(56, "hatch.input.tier.06", "Input Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Input_ZPM
            .set(new GT_MetaTileEntity_Hatch_Input(57, "hatch.input.tier.07", "Input Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Input_UV
            .set(new GT_MetaTileEntity_Hatch_Input(58, "hatch.input.tier.08", "Input Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Input_MAX
            .set(new GT_MetaTileEntity_Hatch_Input(59, "hatch.input.tier.09", "Input Hatch (UHV)", 9).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_EV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(200, 4, "hatch.multi.input.tier.01", "Quadruple Input Hatch (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_IV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(710, 4, "hatch.multi.input.tier.02", "Quadruple Input Hatch (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_LuV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                711,
                4,
                "hatch.multi.input.tier.03",
                "Quadruple Input Hatch (LuV)",
                6).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_ZPM.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                712,
                4,
                "hatch.multi.input.tier.04",
                "Quadruple Input Hatch (ZPM)",
                7).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(713, 4, "hatch.multi.input.tier.05", "Quadruple Input Hatch (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UHV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                714,
                4,
                "hatch.multi.input.tier.06",
                "Quadruple Input Hatch (UHV)",
                9).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UEV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                715,
                4,
                "hatch.multi.input.tier.07",
                "Quadruple Input Hatch (UEV)",
                10).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_UIV.set(
            new GT_MetaTileEntity_Hatch_MultiInput(
                716,
                4,
                "hatch.multi.input.tier.08",
                "Quadruple Input Hatch (UIV)",
                11).getStackForm(1L));

        ItemList.Hatch_Output_ULV.set(
            new GT_MetaTileEntity_Hatch_Output(60, "hatch.output.tier.00", "Output Hatch (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Output_LV.set(
            new GT_MetaTileEntity_Hatch_Output(61, "hatch.output.tier.01", "Output Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Output_MV.set(
            new GT_MetaTileEntity_Hatch_Output(62, "hatch.output.tier.02", "Output Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Output_HV.set(
            new GT_MetaTileEntity_Hatch_Output(63, "hatch.output.tier.03", "Output Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Output_EV.set(
            new GT_MetaTileEntity_Hatch_Output(64, "hatch.output.tier.04", "Output Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Output_IV.set(
            new GT_MetaTileEntity_Hatch_Output(65, "hatch.output.tier.05", "Output Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Output_LuV.set(
            new GT_MetaTileEntity_Hatch_Output(66, "hatch.output.tier.06", "Output Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Output_ZPM.set(
            new GT_MetaTileEntity_Hatch_Output(67, "hatch.output.tier.07", "Output Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Output_UV.set(
            new GT_MetaTileEntity_Hatch_Output(68, "hatch.output.tier.08", "Output Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Output_MAX.set(
            new GT_MetaTileEntity_Hatch_Output(69, "hatch.output.tier.09", "Output Hatch (UHV)", 9).getStackForm(1L));

        ItemList.Quantum_Tank_LV
            .set(new GT_MetaTileEntity_QuantumTank(120, "quantum.tank.tier.06", "Quantum Tank I", 6).getStackForm(1L));
        ItemList.Quantum_Tank_MV
            .set(new GT_MetaTileEntity_QuantumTank(121, "quantum.tank.tier.07", "Quantum Tank II", 7).getStackForm(1L));
        ItemList.Quantum_Tank_HV.set(
            new GT_MetaTileEntity_QuantumTank(122, "quantum.tank.tier.08", "Quantum Tank III", 8).getStackForm(1L));
        ItemList.Quantum_Tank_EV
            .set(new GT_MetaTileEntity_QuantumTank(123, "quantum.tank.tier.09", "Quantum Tank IV", 9).getStackForm(1L));
        ItemList.Quantum_Tank_IV
            .set(new GT_MetaTileEntity_QuantumTank(124, "quantum.tank.tier.10", "Quantum Tank V", 10).getStackForm(1L));

        ItemList.Quantum_Chest_LV.set(
            new GT_MetaTileEntity_QuantumChest(125, "quantum.chest.tier.06", "Quantum Chest I", 6).getStackForm(1L));
        ItemList.Quantum_Chest_MV.set(
            new GT_MetaTileEntity_QuantumChest(126, "quantum.chest.tier.07", "Quantum Chest II", 7).getStackForm(1L));
        ItemList.Quantum_Chest_HV.set(
            new GT_MetaTileEntity_QuantumChest(127, "quantum.chest.tier.08", "Quantum Chest III", 8).getStackForm(1L));
        ItemList.Quantum_Chest_EV.set(
            new GT_MetaTileEntity_QuantumChest(128, "quantum.chest.tier.09", "Quantum Chest IV", 9).getStackForm(1L));
        ItemList.Quantum_Chest_IV.set(
            new GT_MetaTileEntity_QuantumChest(129, "quantum.chest.tier.10", "Quantum Chest V", 10).getStackForm(1L));

        ItemList.Super_Tank_LV
            .set(new GT_MetaTileEntity_SuperTank(130, "super.tank.tier.01", "Super Tank I", 1).getStackForm(1L));
        ItemList.Super_Tank_MV
            .set(new GT_MetaTileEntity_SuperTank(131, "super.tank.tier.02", "Super Tank II", 2).getStackForm(1L));
        ItemList.Super_Tank_HV
            .set(new GT_MetaTileEntity_SuperTank(132, "super.tank.tier.03", "Super Tank III", 3).getStackForm(1L));
        ItemList.Super_Tank_EV
            .set(new GT_MetaTileEntity_SuperTank(133, "super.tank.tier.04", "Super Tank IV", 4).getStackForm(1L));
        ItemList.Super_Tank_IV
            .set(new GT_MetaTileEntity_SuperTank(134, "super.tank.tier.05", "Super Tank V", 5).getStackForm(1L));

        ItemList.Super_Chest_LV
            .set(new GT_MetaTileEntity_SuperChest(135, "super.chest.tier.01", "Super Chest I", 1).getStackForm(1L));
        ItemList.Super_Chest_MV
            .set(new GT_MetaTileEntity_SuperChest(136, "super.chest.tier.02", "Super Chest II", 2).getStackForm(1L));
        ItemList.Super_Chest_HV
            .set(new GT_MetaTileEntity_SuperChest(137, "super.chest.tier.03", "Super Chest III", 3).getStackForm(1L));
        ItemList.Super_Chest_EV
            .set(new GT_MetaTileEntity_SuperChest(138, "super.chest.tier.04", "Super Chest IV", 4).getStackForm(1L));
        ItemList.Super_Chest_IV
            .set(new GT_MetaTileEntity_SuperChest(139, "super.chest.tier.05", "Super Chest V", 5).getStackForm(1L));

        ItemList.Long_Distance_Pipeline_Fluid.set(
            new GT_MetaTileEntity_LongDistancePipelineFluid(
                2700,
                "long.distance.pipeline.fluid",
                "Long Distance Fluid Pipeline",
                1).getStackForm(1L));
        ItemList.Long_Distance_Pipeline_Item.set(
            new GT_MetaTileEntity_LongDistancePipelineItem(
                2701,
                "long.distance.pipeline.item",
                "Long Distance Item Pipeline",
                1).getStackForm(1L));

        ItemList.AdvDebugStructureWriter.set(
            new GT_MetaTileEntity_AdvDebugStructureWriter(
                349,
                "advdebugstructurewriter",
                "Advanced Debug Structure Writer",
                5).getStackForm(1L));

        if (GregTech_API.mAE2) {
            ItemList.Hatch_Output_Bus_ME.set(
                new GT_MetaTileEntity_Hatch_OutputBus_ME(2710, "hatch.output_bus.me", "Output Bus (ME)")
                    .getStackForm(1L));
            ItemList.Hatch_Input_Bus_ME.set(
                new GT_MetaTileEntity_Hatch_InputBus_ME(2711, "hatch.input_bus.me", "Stocking Input Bus (ME)")
                    .getStackForm(1L));
            ItemList.Hatch_Output_ME.set(
                new GT_MetaTileEntity_Hatch_Output_ME(2713, "hatch.output.me", "Output Hatch (ME)").getStackForm(1L));
        }

        ItemList.Hatch_Input_Bus_ULV.set(
            new GT_MetaTileEntity_Hatch_InputBus(70, "hatch.input_bus.tier.00", "Input Bus (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Input_Bus_LV.set(
            new GT_MetaTileEntity_Hatch_InputBus(71, "hatch.input_bus.tier.01", "Input Bus (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Input_Bus_MV.set(
            new GT_MetaTileEntity_Hatch_InputBus(72, "hatch.input_bus.tier.02", "Input Bus (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Input_Bus_HV.set(
            new GT_MetaTileEntity_Hatch_InputBus(73, "hatch.input_bus.tier.03", "Input Bus (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Input_Bus_EV.set(
            new GT_MetaTileEntity_Hatch_InputBus(74, "hatch.input_bus.tier.04", "Input Bus (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Input_Bus_IV.set(
            new GT_MetaTileEntity_Hatch_InputBus(75, "hatch.input_bus.tier.05", "Input Bus (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Input_Bus_LuV.set(
            new GT_MetaTileEntity_Hatch_InputBus(76, "hatch.input_bus.tier.06", "Input Bus (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Input_Bus_ZPM.set(
            new GT_MetaTileEntity_Hatch_InputBus(77, "hatch.input_bus.tier.07", "Input Bus (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Input_Bus_UV.set(
            new GT_MetaTileEntity_Hatch_InputBus(78, "hatch.input_bus.tier.08", "Input Bus (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Input_Bus_MAX.set(
            new GT_MetaTileEntity_Hatch_InputBus(79, "hatch.input_bus.tier.09", "Input Bus (UHV)", 9).getStackForm(1L));

        ItemList.Hatch_Output_Bus_ULV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(80, "hatch.output_bus.tier.00", "Output Bus (ULV)", 0)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_LV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(81, "hatch.output_bus.tier.01", "Output Bus (LV)", 1)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_MV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(82, "hatch.output_bus.tier.02", "Output Bus (MV)", 2)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_HV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(83, "hatch.output_bus.tier.03", "Output Bus (HV)", 3)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_EV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(84, "hatch.output_bus.tier.04", "Output Bus (EV)", 4)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_IV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(85, "hatch.output_bus.tier.05", "Output Bus (IV)", 5)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_LuV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(86, "hatch.output_bus.tier.06", "Output Bus (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_ZPM.set(
            new GT_MetaTileEntity_Hatch_OutputBus(87, "hatch.output_bus.tier.07", "Output Bus (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_UV.set(
            new GT_MetaTileEntity_Hatch_OutputBus(88, "hatch.output_bus.tier.08", "Output Bus (UV)", 8)
                .getStackForm(1L));
        ItemList.Hatch_Output_Bus_MAX.set(
            new GT_MetaTileEntity_Hatch_OutputBus(89, "hatch.output_bus.tier.09", "Output Bus (UHV)", 9)
                .getStackForm(1L));

        ItemList.Hatch_Maintenance.set(
            new GT_MetaTileEntity_Hatch_Maintenance(90, "hatch.maintenance", "Maintenance Hatch", 1).getStackForm(1L));

        ItemList.Hatch_AutoMaintenance.set(
            new GT_MetaTileEntity_Hatch_Maintenance(111, "hatch.maintenance.auto", "Auto Maintenance Hatch", 6, true)
                .getStackForm(1L));
        ItemList.Hatch_DataAccess_EV.set(
            new GT_MetaTileEntity_Hatch_DataAccess(145, "hatch.dataaccess", "Data Access Hatch", 4).getStackForm(1L));
        ItemList.Hatch_DataAccess_LuV.set(
            new GT_MetaTileEntity_Hatch_DataAccess(146, "hatch.dataaccess.adv", "Advanced Data Access Hatch", 6)
                .getStackForm(1L));
        ItemList.Hatch_DataAccess_UV.set(
            new GT_MetaTileEntity_Hatch_DataAccess(147, "hatch.dataaccess.auto", "Automatable Data Access Hatch", 8)
                .getStackForm(1L));

        ItemList.Hatch_Muffler_LV.set(
            new GT_MetaTileEntity_Hatch_Muffler(91, "hatch.muffler.tier.01", "Muffler Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Muffler_MV.set(
            new GT_MetaTileEntity_Hatch_Muffler(92, "hatch.muffler.tier.02", "Muffler Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Muffler_HV.set(
            new GT_MetaTileEntity_Hatch_Muffler(93, "hatch.muffler.tier.03", "Muffler Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Muffler_EV.set(
            new GT_MetaTileEntity_Hatch_Muffler(94, "hatch.muffler.tier.04", "Muffler Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Muffler_IV.set(
            new GT_MetaTileEntity_Hatch_Muffler(95, "hatch.muffler.tier.05", "Muffler Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Muffler_LuV.set(
            new GT_MetaTileEntity_Hatch_Muffler(96, "hatch.muffler.tier.06", "Muffler Hatch (LuV)", 6)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_ZPM.set(
            new GT_MetaTileEntity_Hatch_Muffler(97, "hatch.muffler.tier.07", "Muffler Hatch (ZPM)", 7)
                .getStackForm(1L));
        ItemList.Hatch_Muffler_UV.set(
            new GT_MetaTileEntity_Hatch_Muffler(98, "hatch.muffler.tier.08", "Muffler Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Muffler_MAX.set(
            new GT_MetaTileEntity_Hatch_Muffler(99, "hatch.muffler.tier.09", "Muffler Hatch (UHV)", 9)
                .getStackForm(1L));

        ItemList.Machine_Bronze_Boiler
            .set(new GT_MetaTileEntity_Boiler_Bronze(100, "boiler.bronze", "Small Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler
            .set(new GT_MetaTileEntity_Boiler_Steel(101, "boiler.steel", "High Pressure Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler_Lava
            .set(new GT_MetaTileEntity_Boiler_Lava(102, "boiler.lava", "High Pressure Lava Boiler").getStackForm(1L));
        ItemList.Machine_Bronze_Boiler_Solar
            .set(new GT_MetaTileEntity_Boiler_Solar(105, "boiler.solar", "Simple Solar Boiler").getStackForm(1L));
        ItemList.Machine_HP_Solar.set(
            new GT_MetaTileEntity_Boiler_Solar_Steel(114, "boiler.steel.solar", "High Pressure Solar Boiler")
                .getStackForm(1L));
        ItemList.Machine_Bronze_BlastFurnace.set(
            new GT_MetaTileEntity_BronzeBlastFurnace(108, "bronzemachine.blastfurnace", "Bronze Plated Blast Furnace")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Furnace
            .set(new GT_MetaTileEntity_Furnace_Bronze(103, "bronzemachine.furnace", "Steam Furnace").getStackForm(1L));
        ItemList.Machine_HP_Furnace.set(
            new GT_MetaTileEntity_Furnace_Steel(104, "hpmachine.furnace", "High Pressure Furnace").getStackForm(1L));
        ItemList.Machine_Bronze_Macerator.set(
            new GT_MetaTileEntity_Macerator_Bronze(106, "bronzemachine.macerator", "Steam Macerator").getStackForm(1L));
        ItemList.Machine_HP_Macerator.set(
            new GT_MetaTileEntity_Macerator_Steel(107, "hpmachine.macerator", "High Pressure Macerator")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Extractor.set(
            new GT_MetaTileEntity_Extractor_Bronze(109, "bronzemachine.extractor", "Steam Extractor").getStackForm(1L));
        ItemList.Machine_HP_Extractor.set(
            new GT_MetaTileEntity_Extractor_Steel(110, "hpmachine.extractor", "High Pressure Extractor")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Hammer.set(
            new GT_MetaTileEntity_ForgeHammer_Bronze(112, "bronzemachine.hammer", "Steam Forge Hammer")
                .getStackForm(1L));
        ItemList.Machine_HP_Hammer.set(
            new GT_MetaTileEntity_ForgeHammer_Steel(113, "hpmachine.hammer", "High Pressure Forge Hammer")
                .getStackForm(1L));
        ItemList.Machine_Bronze_Compressor.set(
            new GT_MetaTileEntity_Compressor_Bronze(115, "bronzemachine.compressor", "Steam Compressor")
                .getStackForm(1L));
        ItemList.Machine_HP_Compressor.set(
            new GT_MetaTileEntity_Compressor_Steel(116, "hpmachine.compressor", "High Pressure Compressor")
                .getStackForm(1L));
        ItemList.Machine_Bronze_AlloySmelter.set(
            new GT_MetaTileEntity_AlloySmelter_Bronze(118, "bronzemachine.alloysmelter", "Steam Alloy Smelter")
                .getStackForm(1L));
        ItemList.Machine_HP_AlloySmelter.set(
            new GT_MetaTileEntity_AlloySmelter_Steel(119, "hpmachine.alloysmelter", "High Pressure Alloy Smelter")
                .getStackForm(1L));

        ItemList.Locker_ULV
            .set(new GT_MetaTileEntity_Locker(150, "locker.tier.00", "Ultra Low Voltage Locker", 0).getStackForm(1L));
        ItemList.Locker_LV
            .set(new GT_MetaTileEntity_Locker(151, "locker.tier.01", "Low Voltage Locker", 1).getStackForm(1L));
        ItemList.Locker_MV
            .set(new GT_MetaTileEntity_Locker(152, "locker.tier.02", "Medium Voltage Locker", 2).getStackForm(1L));
        ItemList.Locker_HV
            .set(new GT_MetaTileEntity_Locker(153, "locker.tier.03", "High Voltage Locker", 3).getStackForm(1L));
        ItemList.Locker_EV
            .set(new GT_MetaTileEntity_Locker(154, "locker.tier.04", "Extreme Voltage Locker", 4).getStackForm(1L));
        ItemList.Locker_IV
            .set(new GT_MetaTileEntity_Locker(155, "locker.tier.05", "Insane Voltage Locker", 5).getStackForm(1L));
        ItemList.Locker_LuV
            .set(new GT_MetaTileEntity_Locker(156, "locker.tier.06", "Ludicrous Voltage Locker", 6).getStackForm(1L));
        ItemList.Locker_ZPM
            .set(new GT_MetaTileEntity_Locker(157, "locker.tier.07", "ZPM Voltage Locker", 7).getStackForm(1L));
        ItemList.Locker_UV
            .set(new GT_MetaTileEntity_Locker(158, "locker.tier.08", "Ultimate Voltage Locker", 8).getStackForm(1L));
        ItemList.Locker_MAX.set(
            new GT_MetaTileEntity_Locker(159, "locker.tier.09", "Highly Ultimate Voltage Locker", 9).getStackForm(1L));

        ItemList.Battery_Buffer_1by1_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                160,
                "batterybuffer.01.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                161,
                "batterybuffer.01.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                162,
                "batterybuffer.01.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                163,
                "batterybuffer.01.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                164,
                "batterybuffer.01.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                165,
                "batterybuffer.01.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                166,
                "batterybuffer.01.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                167,
                "batterybuffer.01.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                168,
                "batterybuffer.01.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                1).getStackForm(1L));
        ItemList.Battery_Buffer_1by1_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                169,
                "batterybuffer.01.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                1).getStackForm(1L));

        ItemList.Battery_Buffer_2by2_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                170,
                "batterybuffer.04.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                171,
                "batterybuffer.04.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                172,
                "batterybuffer.04.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                173,
                "batterybuffer.04.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                174,
                "batterybuffer.04.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                175,
                "batterybuffer.04.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                176,
                "batterybuffer.04.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                177,
                "batterybuffer.04.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                178,
                "batterybuffer.04.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                4).getStackForm(1L));
        ItemList.Battery_Buffer_2by2_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                179,
                "batterybuffer.04.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                4).getStackForm(1L));

        ItemList.Battery_Buffer_3by3_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                180,
                "batterybuffer.09.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                181,
                "batterybuffer.09.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                182,
                "batterybuffer.09.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                183,
                "batterybuffer.09.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                184,
                "batterybuffer.09.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                185,
                "batterybuffer.09.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                186,
                "batterybuffer.09.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                187,
                "batterybuffer.09.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                188,
                "batterybuffer.09.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                9).getStackForm(1L));
        ItemList.Battery_Buffer_3by3_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                189,
                "batterybuffer.09.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                9).getStackForm(1L));

        ItemList.Battery_Buffer_4by4_ULV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                190,
                "batterybuffer.16.tier.00",
                "Ultra Low Voltage Battery Buffer",
                0,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                191,
                "batterybuffer.16.tier.01",
                "Low Voltage Battery Buffer",
                1,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_MV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                192,
                "batterybuffer.16.tier.02",
                "Medium Voltage Battery Buffer",
                2,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_HV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                193,
                "batterybuffer.16.tier.03",
                "High Voltage Battery Buffer",
                3,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_EV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                194,
                "batterybuffer.16.tier.04",
                "Extreme Voltage Battery Buffer",
                4,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_IV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                195,
                "batterybuffer.16.tier.05",
                "Insane Voltage Battery Buffer",
                5,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_LuV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                196,
                "batterybuffer.16.tier.06",
                "Ludicrous Voltage Battery Buffer",
                6,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_ZPM.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                197,
                "batterybuffer.16.tier.07",
                "ZPM Voltage Battery Buffer",
                7,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_UV.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                198,
                "batterybuffer.16.tier.08",
                "Ultimate Voltage Battery Buffer",
                8,
                "",
                16).getStackForm(1L));
        ItemList.Battery_Buffer_4by4_MAX.set(
            new GT_MetaTileEntity_BasicBatteryBuffer(
                199,
                "batterybuffer.16.tier.09",
                "Highly Ultimate Voltage Battery Buffer",
                9,
                "",
                16).getStackForm(1L));

        ItemList.Battery_Charger_4by4_ULV.set(
            new GT_MetaTileEntity_Charger(
                690,
                "batterycharger.16.tier.00",
                "Ultra Low Voltage Battery Charger",
                0,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LV.set(
            new GT_MetaTileEntity_Charger(
                691,
                "batterycharger.16.tier.01",
                "Low Voltage Battery Charger",
                1,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_MV.set(
            new GT_MetaTileEntity_Charger(
                692,
                "batterycharger.16.tier.02",
                "Medium Voltage Battery Charger",
                2,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_HV.set(
            new GT_MetaTileEntity_Charger(
                693,
                "batterycharger.16.tier.03",
                "High Voltage Battery Charger",
                3,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_EV.set(
            new GT_MetaTileEntity_Charger(
                694,
                "batterycharger.16.tier.04",
                "Extreme Voltage Battery Charger",
                4,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_IV.set(
            new GT_MetaTileEntity_Charger(
                695,
                "batterycharger.16.tier.05",
                "Insane Voltage Battery Charger",
                5,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_LuV.set(
            new GT_MetaTileEntity_Charger(
                696,
                "batterycharger.16.tier.06",
                "Ludicrous Voltage Battery Charger",
                6,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_ZPM.set(
            new GT_MetaTileEntity_Charger(
                697,
                "batterycharger.16.tier.07",
                "ZPM Voltage Battery Charger",
                7,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_UV.set(
            new GT_MetaTileEntity_Charger(
                698,
                "batterycharger.16.tier.08",
                "Ultimate Voltage Battery Charger",
                8,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));
        ItemList.Battery_Charger_4by4_MAX.set(
            new GT_MetaTileEntity_Charger(
                699,
                "batterycharger.16.tier.09",
                "Highly Ultimate Voltage Battery Charger",
                9,
                "Each battery gives 8A in/4A out (min 4A/2A)",
                4).getStackForm(1L));

        {

            // Wireless Energy Hatches

            ItemList.Wireless_Hatch_Energy_ULV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    206,
                    "hatch.wireless.receiver.tier.00",
                    "ULV Wireless Energy Hatch",
                    0).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_LV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    207,
                    "hatch.wireless.receiver.tier.01",
                    "LV Wireless Energy Hatch",
                    1).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_MV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    208,
                    "hatch.wireless.receiver.tier.02",
                    "MV Wireless Energy Hatch",
                    2).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_HV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    209,
                    "hatch.wireless.receiver.tier.03",
                    "HV Wireless Energy Hatch",
                    3).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_EV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    216,
                    "hatch.wireless.receiver.tier.04",
                    "EV Wireless Energy Hatch",
                    4).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_IV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    217,
                    "hatch.wireless.receiver.tier.05",
                    "IV Wireless Energy Hatch",
                    5).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_LuV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    218,
                    "hatch.wireless.receiver.tier.06",
                    "LuV Wireless Energy Hatch",
                    6).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_ZPM.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    219,
                    "hatch.wireless.receiver.tier.07",
                    "ZPM Wireless Energy Hatch",
                    7).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_UV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    227,
                    "hatch.wireless.receiver.tier.08",
                    "UV Wireless Energy Hatch",
                    8).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_UHV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    229,
                    "hatch.wireless.receiver.tier.09",
                    "UHV Wireless Energy Hatch",
                    9).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_UEV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    266,
                    "hatch.wireless.receiver.tier.10",
                    "UEV Wireless Energy Hatch",
                    10).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_UIV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    267,
                    "hatch.wireless.receiver.tier.11",
                    "UIV Wireless Energy Hatch",
                    11).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_UMV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    268,
                    "hatch.wireless.receiver.tier.12",
                    "UMV Wireless Energy Hatch",
                    12).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_UXV.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    269,
                    "hatch.wireless.receiver.tier.13",
                    "UXV Wireless Energy Hatch",
                    13).getStackForm(1L));
            ItemList.Wireless_Hatch_Energy_MAX.set(
                new GT_MetaTileEntity_Wireless_Hatch(
                    286,
                    "hatch.wireless.receiver.tier.14",
                    "MAX Wireless Energy Hatch",
                    14).getStackForm(1L));

            // Wireless Dynamo Hatches

            ItemList.Wireless_Dynamo_Energy_ULV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    287,
                    "hatch.wireless.transmitter.tier.00",
                    "ULV Wireless Energy Dynamo",
                    0).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_LV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    288,
                    "hatch.wireless.transmitter.tier.01",
                    "LV Wireless Energy Dynamo",
                    1).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_MV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    289,
                    "hatch.wireless.transmitter.tier.02",
                    "MV Wireless Energy Dynamo",
                    2).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_HV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    296,
                    "hatch.wireless.transmitter.tier.03",
                    "HV Wireless Energy Dynamo",
                    3).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_EV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    297,
                    "hatch.wireless.transmitter.tier.04",
                    "EV Wireless Energy Dynamo",
                    4).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_IV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    298,
                    "hatch.wireless.transmitter.tier.05",
                    "IV Wireless Energy Dynamo",
                    5).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_LuV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    299,
                    "hatch.wireless.transmitter.tier.06",
                    "LuV Wireless Energy Dynamo",
                    6).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_ZPM.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    310,
                    "hatch.wireless.transmitter.tier.07",
                    "ZPM Wireless Energy Dynamo",
                    7).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_UV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    316,
                    "hatch.wireless.transmitter.tier.08",
                    "UV Wireless Energy Dynamo",
                    8).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_UHV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    317,
                    "hatch.wireless.transmitter.tier.09",
                    "UHV Wireless Energy Dynamo",
                    9).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_UEV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    318,
                    "hatch.wireless.transmitter.tier.10",
                    "UEV Wireless Energy Dynamo",
                    10).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_UIV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    319,
                    "hatch.wireless.transmitter.tier.11",
                    "UIV Wireless Energy Dynamo",
                    11).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_UMV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    346,
                    "hatch.wireless.transmitter.tier.12",
                    "UMV Wireless Energy Dynamo",
                    12).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_UXV.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    347,
                    "hatch.wireless.transmitter.tier.13",
                    "UXV Wireless Energy Dynamo",
                    13).getStackForm(1L));
            ItemList.Wireless_Dynamo_Energy_MAX.set(
                new GT_MetaTileEntity_Wireless_Dynamo(
                    348,
                    "hatch.wireless.transmitter.tier.14",
                    "MAX Wireless Energy Dynamo",
                    14).getStackForm(1L));
        }
    }

    private static void run2() {
        ItemList.Machine_LV_Scanner.set(
            new GT_MetaTileEntity_Scanner(341, "basicmachine.scanner.tier.01", "Basic Scanner", 1).getStackForm(1L));
        ItemList.Machine_MV_Scanner.set(
            new GT_MetaTileEntity_Scanner(342, "basicmachine.scanner.tier.02", "Advanced Scanner", 2).getStackForm(1L));
        ItemList.Machine_HV_Scanner.set(
            new GT_MetaTileEntity_Scanner(343, "basicmachine.scanner.tier.03", "Advanced Scanner II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Scanner.set(
            new GT_MetaTileEntity_Scanner(344, "basicmachine.scanner.tier.04", "Advanced Scanner III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Scanner.set(
            new GT_MetaTileEntity_Scanner(345, "basicmachine.scanner.tier.05", "Advanced Scanner IV", 5)
                .getStackForm(1L));
        ItemList.Machine_LV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(401, "basicmachine.boxinator.tier.01", "Basic Packager", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(402, "basicmachine.boxinator.tier.02", "Advanced Packager", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(403, "basicmachine.boxinator.tier.03", "Advanced Packager II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(404, "basicmachine.boxinator.tier.04", "Advanced Packager III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(405, "basicmachine.boxinator.tier.05", "Boxinator", 5).getStackForm(1L));
        ItemList.Machine_LuV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(406, "basicmachine.boxinator.tier.06", "Boxinator", 6).getStackForm(1L));
        ItemList.Machine_ZPM_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(407, "basicmachine.boxinator.tier.07", "Boxinator", 7).getStackForm(1L));
        ItemList.Machine_UV_Boxinator.set(
            new GT_MetaTileEntity_Boxinator(408, "basicmachine.boxinator.tier.08", "Boxinator", 8).getStackForm(1L));

        ItemList.Machine_LV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(441, "basicmachine.rockbreaker.tier.01", "Basic Rock Breaker", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(442, "basicmachine.rockbreaker.tier.02", "Advanced Rock Breaker", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(443, "basicmachine.rockbreaker.tier.03", "Advanced Rock Breaker II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(444, "basicmachine.rockbreaker.tier.04", "Advanced Rock Breaker III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_RockBreaker.set(
            new GT_MetaTileEntity_RockBreaker(
                445,
                "basicmachine.rockbreaker.tier.05",
                "Cryogenic Magma Solidifier R-8200",
                5).getStackForm(1L));

        if (Forestry.isModLoaded()) {
            ItemList.Machine_IndustrialApiary.set(
                new GT_MetaTileEntity_IndustrialApiary(9399, "basicmachine.industrialapiary", "Industrial Apiary", 8)
                    .getStackForm(1L));
        }

        ItemList.Machine_LV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(461, "basicmachine.massfab.tier.01", "Basic Mass Fabricator", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(462, "basicmachine.massfab.tier.02", "Advanced Mass Fabricator", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(463, "basicmachine.massfab.tier.03", "Advanced Mass Fabricator II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(464, "basicmachine.massfab.tier.04", "Advanced Mass Fabricator III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Massfab.set(
            new GT_MetaTileEntity_Massfabricator(465, "basicmachine.massfab.tier.05", "Advanced Mass Fabricator IV", 5)
                .getStackForm(1L));

        ItemList.Machine_LV_Replicator.set(
            new GT_MetaTileEntity_Replicator(481, "basicmachine.replicator.tier.01", "Basic Replicator", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Replicator.set(
            new GT_MetaTileEntity_Replicator(482, "basicmachine.replicator.tier.02", "Advanced Replicator", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Replicator.set(
            new GT_MetaTileEntity_Replicator(483, "basicmachine.replicator.tier.03", "Advanced Replicator II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Replicator.set(
            new GT_MetaTileEntity_Replicator(484, "basicmachine.replicator.tier.04", "Advanced Replicator III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Replicator.set(
            new GT_MetaTileEntity_Replicator(485, "basicmachine.replicator.tier.05", "Advanced Replicator IV", 5)
                .getStackForm(1L));

        ItemList.Machine_LV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(491, "basicmachine.brewery.tier.01", "Basic Brewery", 1)
                .getStackForm(1L));
        ItemList.Machine_MV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(492, "basicmachine.brewery.tier.02", "Advanced Brewery", 2)
                .getStackForm(1L));
        ItemList.Machine_HV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(493, "basicmachine.brewery.tier.03", "Advanced Brewery II", 3)
                .getStackForm(1L));
        ItemList.Machine_EV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(494, "basicmachine.brewery.tier.04", "Advanced Brewery III", 4)
                .getStackForm(1L));
        ItemList.Machine_IV_Brewery.set(
            new GT_MetaTileEntity_PotionBrewer(495, "basicmachine.brewery.tier.05", "Advanced Brewery IV", 5)
                .getStackForm(1L));

        ItemList.Machine_LV_Miner
            .set(new GT_MetaTileEntity_Miner(679, "basicmachine.miner.tier.01", "Basic Miner", 1).getStackForm(1L));
        ItemList.Machine_MV_Miner
            .set(new GT_MetaTileEntity_Miner(680, "basicmachine.miner.tier.02", "Good Miner", 2).getStackForm(1L));
        ItemList.Machine_HV_Miner
            .set(new GT_MetaTileEntity_Miner(681, "basicmachine.miner.tier.03", "Advanced Miner", 3).getStackForm(1L));

    }

    private static void run3() {
        ItemList.Machine_Multi_BlastFurnace.set(
            new GT_MetaTileEntity_ElectricBlastFurnace(1000, "multimachine.blastfurnace", "Electric Blast Furnace")
                .getStackForm(1L));
        ItemList.Machine_Multi_ImplosionCompressor.set(
            new GT_MetaTileEntity_ImplosionCompressor(1001, "multimachine.implosioncompressor", "Implosion Compressor")
                .getStackForm(1L));
        ItemList.Machine_Multi_VacuumFreezer.set(
            new GT_MetaTileEntity_VacuumFreezer(1002, "multimachine.vacuumfreezer", "Vacuum Freezer").getStackForm(1L));
        ItemList.Machine_Multi_Furnace.set(
            new GT_MetaTileEntity_MultiFurnace(1003, "multimachine.multifurnace", "Multi Smelter").getStackForm(1L));
        ItemList.Machine_Multi_PlasmaForge.set(
            new GT_MetaTileEntity_PlasmaForge(
                1004,
                "multimachine.plasmaforge",
                "Dimensionally Transcendent Plasma Forge").getStackForm(1L));

        ItemList.Machine_Multi_LargeBoiler_Bronze.set(
            new GT_MetaTileEntity_LargeBoiler_Bronze(1020, "multimachine.boiler.bronze", "Large Bronze Boiler")
                .getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Steel.set(
            new GT_MetaTileEntity_LargeBoiler_Steel(1021, "multimachine.boiler.steel", "Large Steel Boiler")
                .getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Titanium.set(
            new GT_MetaTileEntity_LargeBoiler_Titanium(1022, "multimachine.boiler.titanium", "Large Titanium Boiler")
                .getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_TungstenSteel.set(
            new GT_MetaTileEntity_LargeBoiler_TungstenSteel(
                1023,
                "multimachine.boiler.tungstensteel",
                "Large Tungstensteel Boiler").getStackForm(1L));

        ItemList.Generator_Diesel_LV.set(
            new GT_MetaTileEntity_DieselGenerator(
                1110,
                "basicgenerator.diesel.tier.01",
                "Basic Combustion Generator",
                1).getStackForm(1L));
        ItemList.Generator_Diesel_MV.set(
            new GT_MetaTileEntity_DieselGenerator(
                1111,
                "basicgenerator.diesel.tier.02",
                "Advanced Combustion Generator",
                2).getStackForm(1L));
        ItemList.Generator_Diesel_HV.set(
            new GT_MetaTileEntity_DieselGenerator(
                1112,
                "basicgenerator.diesel.tier.03",
                "Turbo Combustion Generator",
                3).getStackForm(1L));

        ItemList.Generator_Gas_Turbine_LV.set(
            new GT_MetaTileEntity_GasTurbine(1115, "basicgenerator.gasturbine.tier.01", "Basic Gas Turbine", 1, 95)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_MV.set(
            new GT_MetaTileEntity_GasTurbine(1116, "basicgenerator.gasturbine.tier.02", "Advanced Gas Turbine", 2, 90)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_HV.set(
            new GT_MetaTileEntity_GasTurbine(1117, "basicgenerator.gasturbine.tier.03", "Turbo Gas Turbine", 3, 85)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_EV.set(
            new GT_MetaTileEntity_GasTurbine(1118, "basicgenerator.gasturbine.tier.04", "Turbo Gas Turbine II", 4, 60)
                .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_IV.set(
            new GT_MetaTileEntity_GasTurbine(1119, "basicgenerator.gasturbine.tier.05", "Turbo Gas Turbine III", 5, 50)
                .getStackForm(1L));

        ItemList.Generator_Steam_Turbine_LV.set(
            new GT_MetaTileEntity_SteamTurbine(1120, "basicgenerator.steamturbine.tier.01", "Basic Steam Turbine", 1)
                .getStackForm(1L));
        ItemList.Generator_Steam_Turbine_MV.set(
            new GT_MetaTileEntity_SteamTurbine(1121, "basicgenerator.steamturbine.tier.02", "Advanced Steam Turbine", 2)
                .getStackForm(1L));
        ItemList.Generator_Steam_Turbine_HV.set(
            new GT_MetaTileEntity_SteamTurbine(1122, "basicgenerator.steamturbine.tier.03", "Turbo Steam Turbine", 3)
                .getStackForm(1L));

        ItemList.Generator_Naquadah_Mark_I.set(
            new GT_MetaTileEntity_NaquadahReactor(
                1190,
                "basicgenerator.naquadah.tier.04",
                new String[] { "Requires Enriched Naquadah Bolts" },
                "Naquadah Reactor Mark I",
                4).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_II.set(
            new GT_MetaTileEntity_NaquadahReactor(
                1191,
                "basicgenerator.naquadah.tier.05",
                new String[] { "Requires Enriched Naquadah Rods" },
                "Naquadah Reactor Mark II",
                5).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_III.set(
            new GT_MetaTileEntity_NaquadahReactor(
                1192,
                "basicgenerator.naquadah.tier.06",
                new String[] { "Requires Enriched Naquadah Long Rods" },
                "Naquadah Reactor Mark III",
                6).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_IV.set(
            new GT_MetaTileEntity_NaquadahReactor(
                1188,
                "basicgenerator.naquadah.tier.07",
                new String[] { "Requires Naquadria Bolts" },
                "Naquadah Reactor Mark IV",
                7).getStackForm(1L));
        ItemList.Generator_Naquadah_Mark_V.set(
            new GT_MetaTileEntity_NaquadahReactor(
                1189,
                "basicgenerator.naquadah.tier.08",
                new String[] { "Requires Naquadria Rods" },
                "Naquadah Reactor Mark V",
                8).getStackForm(1L));

        ItemList.MagicEnergyConverter_LV.set(
            new GT_MetaTileEntity_MagicEnergyConverter(
                1123,
                "basicgenerator.magicenergyconverter.tier.01",
                "Novice Magic Energy Converter",
                1).getStackForm(1L));
        ItemList.MagicEnergyConverter_MV.set(
            new GT_MetaTileEntity_MagicEnergyConverter(
                1124,
                "basicgenerator.magicenergyconverter.tier.02",
                "Adept Magic Energy Converter",
                2).getStackForm(1L));
        ItemList.MagicEnergyConverter_HV.set(
            new GT_MetaTileEntity_MagicEnergyConverter(
                1125,
                "basicgenerator.magicenergyconverter.tier.03",
                "Master Magic Energy Converter",
                3).getStackForm(1L));

        ItemList.MagicEnergyAbsorber_LV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                1127,
                "basicgenerator.magicenergyabsorber.tier.01",
                "Novice Magic Energy Absorber",
                1).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_MV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                1128,
                "basicgenerator.magicenergyabsorber.tier.02",
                "Adept Magic Energy Absorber",
                2).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_HV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                1129,
                "basicgenerator.magicenergyabsorber.tier.03",
                "Master Magic Energy Absorber",
                3).getStackForm(1L));
        ItemList.MagicEnergyAbsorber_EV.set(
            new GT_MetaTileEntity_MagicalEnergyAbsorber(
                1130,
                "basicgenerator.magicenergyabsorber.tier.04",
                "Grandmaster Magic Energy Absorber",
                4).getStackForm(1L));

        ItemList.FusionComputer_LuV.set(
            new GT_MetaTileEntity_FusionComputer1(1193, "fusioncomputer.tier.06", "Fusion Control Computer Mark I")
                .getStackForm(1L));
        ItemList.FusionComputer_ZPMV.set(
            new GT_MetaTileEntity_FusionComputer2(1194, "fusioncomputer.tier.07", "Fusion Control Computer Mark II")
                .getStackForm(1L));
        ItemList.FusionComputer_UV.set(
            new GT_MetaTileEntity_FusionComputer3(1195, "fusioncomputer.tier.08", "Fusion Control Computer Mark III")
                .getStackForm(1L));

        ItemList.Generator_Plasma_IV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                1196,
                "basicgenerator.plasmagenerator.tier.05",
                "Plasma Generator Mark I",
                4).getStackForm(1L));
        ItemList.Generator_Plasma_LuV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                1197,
                "basicgenerator.plasmagenerator.tier.06",
                "Plasma Generator Mark II",
                5).getStackForm(1L));
        ItemList.Generator_Plasma_ZPMV.set(
            new GT_MetaTileEntity_PlasmaGenerator(
                1198,
                "basicgenerator.plasmagenerator.tier.07",
                "Plasma Generator Mark III",
                6).getStackForm(1L));

        ItemList.Processing_Array.set(
            new GT_MetaTileEntity_ProcessingArray(1199, "multimachine.processingarray", "Processing Array")
                .getStackForm(1L));
        ItemList.Distillation_Tower.set(
            new GT_MetaTileEntity_DistillationTower(1126, "multimachine.distillationtower", "Distillation Tower")
                .getStackForm(1L));
        ItemList.Ore_Processor.set(
            new GT_MetaTileEntity_IntegratedOreFactory(1132, "multimachine.oreprocessor", "Integrated Ore Factory")
                .getStackForm(1L));

        ItemList.LargeSteamTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_Steam(1131, "multimachine.largeturbine", "Large Steam Turbine")
                .getStackForm(1L));
        ItemList.LargeGasTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_Gas(1151, "multimachine.largegasturbine", "Large Gas Turbine")
                .getStackForm(1L));
        ItemList.LargeHPSteamTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_HPSteam(1152, "multimachine.largehpturbine", "Large HP Steam Turbine")
                .getStackForm(1L));
        ItemList.LargeAdvancedGasTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_GasAdvanced(
                1005,
                "multimachine.largeadvancedgasturbine",
                "Large Advanced Gas Turbine").getStackForm(1L));
        ItemList.Machine_Multi_TranscendentPlasmaMixer.set(
            new GT_MetaTileEntity_TranscendentPlasmaMixer(
                1006,
                "multimachine.transcendentplasmamixer",
                "Transcendent Plasma Mixer").getStackForm(1));

        ItemList.LargePlasmaTurbine.set(
            new GT_MetaTileEntity_LargeTurbine_Plasma(1153, "multimachine.largeplasmaturbine", "Large Plasma Generator")
                .getStackForm(1L));

        ItemList.Pump_LV
            .set(new GT_MetaTileEntity_Pump(1140, "basicmachine.pump.tier.01", "Basic Pump", 1).getStackForm(1L));
        ItemList.Pump_MV
            .set(new GT_MetaTileEntity_Pump(1141, "basicmachine.pump.tier.02", "Advanced Pump", 2).getStackForm(1L));
        ItemList.Pump_HV
            .set(new GT_MetaTileEntity_Pump(1142, "basicmachine.pump.tier.03", "Advanced Pump II", 3).getStackForm(1L));
        ItemList.Pump_EV.set(
            new GT_MetaTileEntity_Pump(1143, "basicmachine.pump.tier.04", "Advanced Pump III", 4).getStackForm(1L));
        ItemList.Pump_IV
            .set(new GT_MetaTileEntity_Pump(1144, "basicmachine.pump.tier.05", "Advanced Pump IV", 5).getStackForm(1L));

        ItemList.Teleporter
            .set(new GT_MetaTileEntity_Teleporter(1145, "basicmachine.teleporter", "Teleporter", 9).getStackForm(1L));

        ItemList.MobRep_LV.set(
            new GT_MetaTileEntity_MonsterRepellent(1146, "basicmachine.mobrep.tier.01", "Basic Monster Repellator", 1)
                .getStackForm(1L));
        ItemList.MobRep_MV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                1147,
                "basicmachine.mobrep.tier.02",
                "Advanced Monster Repellator",
                2).getStackForm(1L));
        ItemList.MobRep_HV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                1148,
                "basicmachine.mobrep.tier.03",
                "Advanced Monster Repellator II",
                3).getStackForm(1L));
        ItemList.MobRep_EV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                1149,
                "basicmachine.mobrep.tier.04",
                "Advanced Monster Repellator III",
                4).getStackForm(1L));
        ItemList.MobRep_IV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                1150,
                "basicmachine.mobrep.tier.05",
                "Advanced Monster Repellator IV",
                5).getStackForm(1L));
        ItemList.MobRep_LuV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                1135,
                "basicmachine.mobrep.tier.06",
                "Advanced Monster Repellator V",
                6).getStackForm(1L));
        ItemList.MobRep_ZPM.set(
            new GT_MetaTileEntity_MonsterRepellent(
                1136,
                "basicmachine.mobrep.tier.07",
                "Advanced Monster Repellator VI",
                7).getStackForm(1L));
        ItemList.MobRep_UV.set(
            new GT_MetaTileEntity_MonsterRepellent(
                1137,
                "basicmachine.mobrep.tier.08",
                "Advanced Monster Repellator VII",
                8).getStackForm(1L));

        ItemList.Machine_Multi_HeatExchanger.set(
            new GT_MetaTileEntity_HeatExchanger(1154, "multimachine.heatexchanger", "Large Heat Exchanger")
                .getStackForm(1L));
        ItemList.Charcoal_Pile.set(
            new GT_MetaTileEntity_Charcoal_Pit(1155, "multimachine.charcoalpile", "Charcoal Pile Igniter")
                .getStackForm(1));
        ItemList.Seismic_Prospector_LV.set(
            new GT_MetaTileEntity_SeismicProspector(
                1156,
                "basicmachine.seismicprospector.01",
                "Seismic Prospector LV",
                1).getStackForm(1));
        ItemList.Seismic_Prospector_MV.set(
            new GT_MetaTileEntity_SeismicProspector(
                2100,
                "basicmachine.seismicprospector.02",
                "Seismic Prospector MV",
                2).getStackForm(1));
        ItemList.Seismic_Prospector_HV.set(
            new GT_MetaTileEntity_SeismicProspector(
                2101,
                "basicmachine.seismicprospector.03",
                "Seismic Prospector HV",
                3).getStackForm(1));

        ItemList.Seismic_Prospector_Adv_LV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                2102,
                "basicmachine.seismicprospector.07",
                "Advanced Seismic Prospector LV",
                1,
                5 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_MV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                2103,
                "basicmachine.seismicprospector.06",
                "Advanced Seismic Prospector MV",
                2,
                7 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_HV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                2104,
                "basicmachine.seismicprospector.05",
                "Advanced Seismic Prospector HV",
                3,
                9 * 16 / 2,
                2).getStackForm(1));
        ItemList.Seismic_Prospector_Adv_EV.set(
            new GT_MetaTileEntity_AdvSeismicProspector(
                1173,
                "basicmachine.seismicprospector.04",
                "Advanced Seismic Prospector EV",
                4,
                11 * 16 / 2,
                2).getStackForm(1));

        // Converter recipes in case you had old one lying around
        ItemList.OilDrill1.set(
            new GT_MetaTileEntity_OilDrill1(1157, "multimachine.oildrill1", "Oil/Gas/Fluid Drilling Rig")
                .getStackForm(1));
        ItemList.OilDrill2.set(
            new GT_MetaTileEntity_OilDrill2(141, "multimachine.oildrill2", "Oil/Gas/Fluid Drilling Rig II")
                .getStackForm(1));
        ItemList.OilDrill3.set(
            new GT_MetaTileEntity_OilDrill3(142, "multimachine.oildrill3", "Oil/Gas/Fluid Drilling Rig III")
                .getStackForm(1));
        ItemList.OilDrill4.set(
            new GT_MetaTileEntity_OilDrill4(149, "multimachine.oildrill4", "Oil/Gas/Fluid Drilling Rig IV")
                .getStackForm(1));
        ItemList.OilDrillInfinite.set(
            new GT_MetaTileEntity_OilDrillInfinite(
                148,
                "multimachine.oildrillinfinite",
                "Infinite Oil/Gas/Fluid Drilling Rig").getStackForm(1));

        ItemList.ConcreteBackfiller1.set(
            new GT_MetaTileEntity_ConcreteBackfiller1(143, "multimachine.concretebackfiller1", "Concrete Backfiller")
                .getStackForm(1));
        ItemList.ConcreteBackfiller2.set(
            new GT_MetaTileEntity_ConcreteBackfiller2(
                144,
                "multimachine.concretebackfiller3",
                "Advanced Concrete Backfiller").getStackForm(1));
        ItemList.OreDrill1.set(
            new GT_MetaTileEntity_OreDrillingPlant1(1158, "multimachine.oredrill1", "Ore Drilling Plant")
                .getStackForm(1));
        ItemList.OreDrill2.set(
            new GT_MetaTileEntity_OreDrillingPlant2(1177, "multimachine.oredrill2", "Ore Drilling Plant II")
                .getStackForm(1));
        ItemList.OreDrill3.set(
            new GT_MetaTileEntity_OreDrillingPlant3(1178, "multimachine.oredrill3", "Ore Drilling Plant III")
                .getStackForm(1));
        ItemList.OreDrill4.set(
            new GT_MetaTileEntity_OreDrillingPlant4(1179, "multimachine.oredrill4", "Ore Drilling Plant IV")
                .getStackForm(1));

        ItemList.PyrolyseOven
            .set(new GT_MetaTileEntity_PyrolyseOven(1159, "multimachine.pyro", "Pyrolyse Oven").getStackForm(1));
        ItemList.OilCracker
            .set(new GT_MetaTileEntity_OilCracker(1160, "multimachine.cracker", "Oil Cracking Unit").getStackForm(1));
        ItemList.MicroTransmitter_HV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                1161,
                "basicmachine.microtransmitter.03",
                "HV Microwave Energy Transmitter",
                3).getStackForm(1L));
        ItemList.MicroTransmitter_EV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                1162,
                "basicmachine.microtransmitter.04",
                "EV Microwave Energy Transmitter",
                4).getStackForm(1L));
        ItemList.MicroTransmitter_IV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                1163,
                "basicmachine.microtransmitter.05",
                "IV Microwave Energy Transmitter",
                5).getStackForm(1L));
        ItemList.MicroTransmitter_LUV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                1164,
                "basicmachine.microtransmitter.06",
                "LuV Microwave Energy Transmitter",
                6).getStackForm(1L));
        ItemList.MicroTransmitter_ZPM.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                1165,
                "basicmachine.microtransmitter.07",
                "ZPM Microwave Energy Transmitter",
                7).getStackForm(1L));
        ItemList.MicroTransmitter_UV.set(
            new GT_MetaTileEntity_MicrowaveEnergyTransmitter(
                1166,
                "basicmachine.microtransmitter.08",
                "UV Microwave Energy Transmitter",
                8).getStackForm(1L));
        ItemList.Machine_Multi_Assemblyline.set(
            new GT_MetaTileEntity_AssemblyLine(1170, "multimachine.assemblyline", "Assembling Line").getStackForm(1L));
        ItemList.Machine_Multi_DieselEngine.set(
            new GT_MetaTileEntity_DieselEngine(1171, "multimachine.dieselengine", "Combustion Engine")
                .getStackForm(1L));
        ItemList.Machine_Multi_ExtremeDieselEngine.set(
            new GT_MetaTileEntity_ExtremeDieselEngine(
                2105,
                "multimachine.extremedieselengine",
                "Extreme Combustion Engine").getStackForm(1L));
        ItemList.Machine_Multi_Cleanroom.set(
            new GT_MetaTileEntity_Cleanroom(1172, "multimachine.cleanroom", "Cleanroom Controller").getStackForm(1));

        ItemList.Machine_HV_LightningRod.set(
            new GT_MetaTileEntity_LightningRod(1174, "basicgenerator.lightningrod.03", "Lightning Rod", 3)
                .getStackForm(1));
        ItemList.Machine_EV_LightningRod.set(
            new GT_MetaTileEntity_LightningRod(1175, "basicgenerator.lightningrod.04", "Lightning Rod II", 4)
                .getStackForm(1));
        ItemList.Machine_IV_LightningRod.set(
            new GT_MetaTileEntity_LightningRod(1176, "basicgenerator.lightningrod.05", "Lightning Rod III", 5)
                .getStackForm(1));
        ItemList.Machine_Multi_LargeChemicalReactor.set(
            new GT_MetaTileEntity_LargeChemicalReactor(1169, "multimachine.chemicalreactor", "Large Chemical Reactor")
                .getStackForm(1));
        ItemList.PCBFactory
            .set(new GT_MetaTileEntity_PCBFactory(356, "multimachine.pcbfactory", "PCB Factory").getStackForm(1));
        ItemList.NanoForge
            .set(new GT_MetaTileEntity_NanoForge(357, "multimachine.nanoforge", "Nano Forge").getStackForm(1));
    }

    private static void run4() {
        for (int i = 0; i < GregTech_API.sGeneratedMaterials.length; i++) {
            if (((GregTech_API.sGeneratedMaterials[i] != null)
                && ((GregTech_API.sGeneratedMaterials[i].mTypes & 0x2) != 0))
                || (GregTech_API.sGeneratedMaterials[i] == Materials.Wood)) {
                new GT_MetaPipeEntity_Frame(
                    4096 + i,
                    "GT_Frame_" + GregTech_API.sGeneratedMaterials[i],
                    (GT_LanguageManager.i18nPlaceholder ? "%material"
                        : GregTech_API.sGeneratedMaterials[i] != null
                            ? GregTech_API.sGeneratedMaterials[i].mDefaultLocalName
                            : "")
                        + " Frame Box",
                    GregTech_API.sGeneratedMaterials[i]);
            }
        }
        boolean bEC = !GT_Mod.gregtechproxy.mHardcoreCables;

        makeWires(Materials.RedAlloy, 2000, 0L, 1L, 1L, gregtech.api.enums.GT_Values.V[0], true, false);

        makeWires(Materials.Cobalt, 1200, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.Lead, 1220, 2L, 4L, 2L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.Tin, 1240, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);

        makeWires(Materials.Zinc, 1260, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);
        makeWires(Materials.SolderingAlloy, 1280, 1L, 2L, 1L, gregtech.api.enums.GT_Values.V[1], true, false);

        makeWires(
            Materials.Iron,
            1300,
            bEC ? 3L : 4L,
            bEC ? 6L : 8L,
            2L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.Nickel,
            1320,
            bEC ? 3L : 5L,
            bEC ? 6L : 10L,
            3L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.Cupronickel,
            1340,
            bEC ? 3L : 4L,
            bEC ? 6L : 8L,
            2L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.Copper,
            1360,
            bEC ? 2L : 3L,
            bEC ? 4L : 6L,
            1L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);
        makeWires(
            Materials.AnnealedCopper,
            1380,
            bEC ? 1L : 2L,
            bEC ? 2L : 4L,
            1L,
            gregtech.api.enums.GT_Values.V[2],
            true,
            false);

        makeWires(
            Materials.Kanthal,
            1400,
            bEC ? 3L : 8L,
            bEC ? 6L : 16L,
            4L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.Gold,
            1420,
            bEC ? 2L : 6L,
            bEC ? 4L : 12L,
            3L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.Electrum,
            1440,
            bEC ? 2L : 5L,
            bEC ? 4L : 10L,
            2L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.Silver,
            1460,
            bEC ? 1L : 4L,
            bEC ? 2L : 8L,
            1L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);
        makeWires(
            Materials.BlueAlloy,
            1480,
            bEC ? 1L : 4L,
            bEC ? 2L : 8L,
            2L,
            gregtech.api.enums.GT_Values.V[3],
            true,
            false);

        makeWires(
            Materials.Nichrome,
            1500,
            bEC ? 4L : 32L,
            bEC ? 8L : 64L,
            3L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.Steel,
            1520,
            bEC ? 2L : 16L,
            bEC ? 4L : 32L,
            2L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.BlackSteel,
            1540,
            bEC ? 2L : 14L,
            bEC ? 4L : 28L,
            3L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.Titanium,
            1560,
            bEC ? 2L : 12L,
            bEC ? 4L : 24L,
            4L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);
        makeWires(
            Materials.Aluminium,
            1580,
            bEC ? 1L : 8L,
            bEC ? 2L : 16L,
            1L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);

        makeWires(
            Materials.Graphene,
            1600,
            bEC ? 1L : 16L,
            bEC ? 2L : 32L,
            1L,
            gregtech.api.enums.GT_Values.V[5],
            false,
            true);
        makeWires(
            Materials.Osmium,
            1620,
            bEC ? 2L : 32L,
            bEC ? 4L : 64L,
            4L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);
        makeWires(
            Materials.Platinum,
            1640,
            bEC ? 1L : 16L,
            bEC ? 2L : 32L,
            2L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);
        makeWires(
            Materials.TungstenSteel,
            1660,
            bEC ? 2L : 14L,
            bEC ? 4L : 28L,
            3L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);
        makeWires(
            Materials.Tungsten,
            1680,
            bEC ? 2L : 12L,
            bEC ? 4L : 24L,
            2L,
            gregtech.api.enums.GT_Values.V[5],
            true,
            false);

        makeWires(
            Materials.HSSG,
            1700,
            bEC ? 2L : 128L,
            bEC ? 4L : 256L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);
        makeWires(
            Materials.NiobiumTitanium,
            1720,
            bEC ? 2L : 128L,
            bEC ? 4L : 256L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);
        makeWires(
            Materials.VanadiumGallium,
            1740,
            bEC ? 2L : 128L,
            bEC ? 4L : 256L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);
        makeWires(
            Materials.YttriumBariumCuprate,
            1760,
            bEC ? 4L : 256L,
            bEC ? 8L : 512L,
            4L,
            gregtech.api.enums.GT_Values.V[6],
            true,
            false);

        makeWires(
            Materials.Naquadah,
            1780,
            bEC ? 2L : 64L,
            bEC ? 4L : 128L,
            2L,
            gregtech.api.enums.GT_Values.V[7],
            true,
            false);

        makeWires(
            Materials.NaquadahAlloy,
            1800,
            bEC ? 4L : 64L,
            bEC ? 8L : 128L,
            2L,
            gregtech.api.enums.GT_Values.V[8],
            true,
            false);
        makeWires(
            Materials.Duranium,
            1820,
            bEC ? 8L : 64L,
            bEC ? 16L : 128L,
            1L,
            gregtech.api.enums.GT_Values.V[8],
            true,
            false);
        makeWires(
            Materials.TPV,
            1840,
            bEC ? 1L : 14L,
            bEC ? 2L : 28L,
            6L,
            gregtech.api.enums.GT_Values.V[4],
            true,
            false);

        // Superconductor base.
        makeWires(
            Materials.Pentacadmiummagnesiumhexaoxid,
            2200,
            1L,
            2L,
            1L,
            gregtech.api.enums.GT_Values.V[2],
            false,
            false);
        makeWires(
            Materials.Titaniumonabariumdecacoppereikosaoxid,
            2220,
            1L,
            8L,
            2L,
            gregtech.api.enums.GT_Values.V[3],
            false,
            false);
        makeWires(Materials.Uraniumtriplatinid, 2240, 1L, 16L, 3L, gregtech.api.enums.GT_Values.V[4], false, false);
        makeWires(Materials.Vanadiumtriindinid, 2260, 1L, 64L, 4L, gregtech.api.enums.GT_Values.V[5], false, false);
        makeWires(
            Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            2280,
            2L,
            256L,
            6L,
            gregtech.api.enums.GT_Values.V[6],
            false,
            false);
        makeWires(
            Materials.Tetranaquadahdiindiumhexaplatiumosminid,
            2300,
            2L,
            1024L,
            8L,
            gregtech.api.enums.GT_Values.V[7],
            false,
            false);
        makeWires(
            Materials.Longasssuperconductornameforuvwire,
            2500,
            2L,
            4096L,
            12L,
            gregtech.api.enums.GT_Values.V[8],
            false,
            false);
        makeWires(
            Materials.Longasssuperconductornameforuhvwire,
            2520,
            2L,
            16384L,
            16L,
            gregtech.api.enums.GT_Values.V[9],
            false,
            false);
        makeWires(
            Materials.SuperconductorUEVBase,
            2032,
            2L,
            65536L,
            24L,
            gregtech.api.enums.GT_Values.V[10],
            false,
            false);
        makeWires(
            Materials.SuperconductorUIVBase,
            2052,
            2L,
            262144L,
            32L,
            gregtech.api.enums.GT_Values.V[11],
            false,
            false);
        makeWires(Materials.SuperconductorUMVBase, 2072, 2L, 1048576L, 32L, GT_Values.V[12], false, false);

        // Actual superconductors.
        makeWires(Materials.SuperconductorMV, 2320, 0L, 0L, 4L, gregtech.api.enums.GT_Values.V[2], false, true);
        makeWires(Materials.SuperconductorHV, 2340, 0L, 0L, 6L, gregtech.api.enums.GT_Values.V[3], false, true);
        makeWires(Materials.SuperconductorEV, 2360, 0L, 0L, 8L, gregtech.api.enums.GT_Values.V[4], false, true);
        makeWires(Materials.SuperconductorIV, 2380, 0L, 0L, 12L, gregtech.api.enums.GT_Values.V[5], false, true);
        makeWires(Materials.SuperconductorLuV, 2400, 0L, 0L, 16L, gregtech.api.enums.GT_Values.V[6], false, true);
        makeWires(Materials.SuperconductorZPM, 2420, 0L, 0L, 24L, gregtech.api.enums.GT_Values.V[7], false, true);
        makeWires(Materials.SuperconductorUV, 2440, 0L, 0L, 32L, gregtech.api.enums.GT_Values.V[8], false, true);
        makeWires(Materials.SuperconductorUHV, 2020, 0L, 0L, 48L, gregtech.api.enums.GT_Values.V[9], false, true);
        makeWires(Materials.SuperconductorUEV, 2026, 0L, 0L, 64L, gregtech.api.enums.GT_Values.V[10], false, true);
        makeWires(Materials.SuperconductorUIV, 2081, 0L, 0L, 64L, gregtech.api.enums.GT_Values.V[11], false, true);
        makeWires(Materials.SuperconductorUMV, 2089, 0L, 0L, 64L, gregtech.api.enums.GT_Values.V[12], false, true);

        makeWires(Materials.Ichorium, 2600, 2L, 2L, 12L, GT_Values.V[9], false, true);
        makeWires(MaterialsUEVplus.SpaceTime, 2606, 0L, 0L, 1_000_000L, GT_Values.V[14], false, true);

        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(Materials.Wood),
            new GT_MetaPipeEntity_Fluid(
                5101,
                "GT_Pipe_Wood_Small",
                "Small Wooden Fluid Pipe",
                0.375F,
                Materials.Wood,
                10,
                350,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(Materials.Wood),
            new GT_MetaPipeEntity_Fluid(5102, "GT_Pipe_Wood", "Wooden Fluid Pipe", 0.5F, Materials.Wood, 30, 350, false)
                .getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(Materials.Wood),
            new GT_MetaPipeEntity_Fluid(
                5103,
                "GT_Pipe_Wood_Large",
                "Large Wooden Fluid Pipe",
                0.75F,
                Materials.Wood,
                60,
                350,
                false).getStackForm(1L));

        generateFluidPipes(Materials.Copper, Materials.Copper.mName, 5110, 20, 1000, true);
        generateFluidMultiPipes(Materials.Copper, Materials.Copper.mName, 5115, 20, 1000, true);
        generateFluidPipes(Materials.Bronze, Materials.Bronze.mName, 5120, 120, 2000, true);
        generateFluidMultiPipes(Materials.Bronze, Materials.Bronze.mName, 5125, 120, 2000, true);
        generateFluidPipes(Materials.Steel, Materials.Steel.mName, 5130, 240, 2500, true);
        generateFluidMultiPipes(Materials.Steel, Materials.Steel.mName, 5135, 240, 2500, true);
        generateFluidPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5140, 360, 3000, true);
        generateFluidMultiPipes(Materials.StainlessSteel, Materials.StainlessSteel.mName, 5145, 360, 3000, true);
        generateFluidPipes(Materials.Titanium, Materials.Titanium.mName, 5150, 480, 5000, true);
        generateFluidMultiPipes(Materials.Titanium, Materials.Titanium.mName, 5155, 480, 5000, true);
        generateFluidPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5160, 600, 7500, true);
        generateFluidMultiPipes(Materials.TungstenSteel, Materials.TungstenSteel.mName, 5270, 600, 7500, true);
        generateFluidPipes(
            Materials.Polybenzimidazole,
            Materials.Polybenzimidazole.mName,
            "PBI",
            5280,
            600,
            1000,
            true);
        generateFluidMultiPipes(
            Materials.Polybenzimidazole,
            Materials.Polybenzimidazole.mName,
            "PBI",
            5290,
            600,
            1000,
            true);
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(Materials.Ultimate),
            new GT_MetaPipeEntity_Fluid(
                5165,
                "GT_Pipe_HighPressure_Small",
                "Small High Pressure Fluid Pipe",
                0.375F,
                Materials.Redstone,
                4800,
                1500,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(Materials.Ultimate),
            new GT_MetaPipeEntity_Fluid(
                5166,
                "GT_Pipe_HighPressure",
                "High Pressure Fluid Pipe",
                0.5F,
                Materials.Redstone,
                7200,
                1500,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(Materials.Ultimate),
            new GT_MetaPipeEntity_Fluid(
                5167,
                "GT_Pipe_HighPressure_Large",
                "Large High Pressure Fluid Pipe",
                0.75F,
                Materials.Redstone,
                9600,
                1500,
                true).getStackForm(1L));
        generateFluidPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5170, 360, 350, true);
        generateFluidMultiPipes(Materials.Plastic, Materials.Plastic.mName, "Plastic", 5175, 360, 350, true);
        generateFluidPipes(
            Materials.Polytetrafluoroethylene,
            Materials.Polytetrafluoroethylene.mName,
            "PTFE",
            5680,
            480,
            600,
            true);
        generateFluidMultiPipes(
            Materials.Polytetrafluoroethylene,
            Materials.Polytetrafluoroethylene.mName,
            "PTFE",
            5685,
            480,
            600,
            true);
        generateFluidPipes(
            MaterialsUEVplus.SpaceTime,
            MaterialsUEVplus.SpaceTime.mName,
            5300,
            250000,
            2147483647,
            true);
        generateFluidMultiPipes(
            MaterialsUEVplus.SpaceTime,
            MaterialsUEVplus.SpaceTime.mName,
            5305,
            250000,
            2147483647,
            true);
        generateFluidPipes(
            MaterialsUEVplus.TranscendentMetal,
            MaterialsUEVplus.TranscendentMetal.mName,
            5310,
            220000,
            2147483647,
            true);
        generateFluidMultiPipes(
            MaterialsUEVplus.TranscendentMetal,
            MaterialsUEVplus.TranscendentMetal.mName,
            5315,
            220000,
            2147483647,
            true);

        generateItemPipes(Materials.Brass, Materials.Brass.mName, 5602, 1);
        generateItemPipes(Materials.Electrum, Materials.Electrum.mName, 5612, 2);
        generateItemPipes(Materials.Platinum, Materials.Platinum.mName, 5622, 4);
        generateItemPipes(Materials.Osmium, Materials.Osmium.mName, 5632, 8);
        generateItemPipes(Materials.PolyvinylChloride, Materials.PolyvinylChloride.mName, "PVC", 5690, 4);
        generateItemPipes(Materials.Nickel, Materials.Nickel.mName, 5700, 1);
        generateItemPipes(Materials.Cobalt, Materials.Cobalt.mName, 5710, 2);
        generateItemPipes(Materials.Aluminium, Materials.Aluminium.mName, 5720, 2);

        ItemList.Automation_ChestBuffer_ULV.set(
            new GT_MetaTileEntity_ChestBuffer(
                9230,
                "automation.chestbuffer.tier.00",
                "Ultra Low Voltage Chest Buffer",
                0).getStackForm(1L));
        ItemList.Automation_ChestBuffer_LV.set(
            new GT_MetaTileEntity_ChestBuffer(9231, "automation.chestbuffer.tier.01", "Low Voltage Chest Buffer", 1)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_MV.set(
            new GT_MetaTileEntity_ChestBuffer(9232, "automation.chestbuffer.tier.02", "Medium Voltage Chest Buffer", 2)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_HV.set(
            new GT_MetaTileEntity_ChestBuffer(9233, "automation.chestbuffer.tier.03", "High Voltage Chest Buffer", 3)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_EV.set(
            new GT_MetaTileEntity_ChestBuffer(9234, "automation.chestbuffer.tier.04", "Extreme Voltage Chest Buffer", 4)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_IV.set(
            new GT_MetaTileEntity_ChestBuffer(9235, "automation.chestbuffer.tier.05", "Insane Voltage Chest Buffer", 5)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_LuV.set(
            new GT_MetaTileEntity_ChestBuffer(
                9236,
                "automation.chestbuffer.tier.06",
                "Ludicrous Voltage Chest Buffer",
                6).getStackForm(1L));
        ItemList.Automation_ChestBuffer_ZPM.set(
            new GT_MetaTileEntity_ChestBuffer(9237, "automation.chestbuffer.tier.07", "ZPM Voltage Chest Buffer", 7)
                .getStackForm(1L));
        ItemList.Automation_ChestBuffer_UV.set(
            new GT_MetaTileEntity_ChestBuffer(
                9238,
                "automation.chestbuffer.tier.08",
                "Ultimate Voltage Chest Buffer",
                8).getStackForm(1L));
        ItemList.Automation_ChestBuffer_MAX.set(
            new GT_MetaTileEntity_ChestBuffer(
                9239,
                "automation.chestbuffer.tier.09",
                "Highly Ultimate Voltage Chest Buffer",
                9).getStackForm(1L));

        ItemList.Automation_Filter_ULV.set(
            new GT_MetaTileEntity_Filter(9240, "automation.filter.tier.00", "Ultra Low Voltage Item Filter", 0)
                .getStackForm(1L));
        ItemList.Automation_Filter_LV.set(
            new GT_MetaTileEntity_Filter(9241, "automation.filter.tier.01", "Low Voltage Item Filter", 1)
                .getStackForm(1L));
        ItemList.Automation_Filter_MV.set(
            new GT_MetaTileEntity_Filter(9242, "automation.filter.tier.02", "Medium Voltage Item Filter", 2)
                .getStackForm(1L));
        ItemList.Automation_Filter_HV.set(
            new GT_MetaTileEntity_Filter(9243, "automation.filter.tier.03", "High Voltage Item Filter", 3)
                .getStackForm(1L));
        ItemList.Automation_Filter_EV.set(
            new GT_MetaTileEntity_Filter(9244, "automation.filter.tier.04", "Extreme Voltage Item Filter", 4)
                .getStackForm(1L));
        ItemList.Automation_Filter_IV.set(
            new GT_MetaTileEntity_Filter(9245, "automation.filter.tier.05", "Insane Voltage Item Filter", 5)
                .getStackForm(1L));
        ItemList.Automation_Filter_LuV.set(
            new GT_MetaTileEntity_Filter(9246, "automation.filter.tier.06", "Ludicrous Voltage Item Filter", 6)
                .getStackForm(1L));
        ItemList.Automation_Filter_ZPM.set(
            new GT_MetaTileEntity_Filter(9247, "automation.filter.tier.07", "ZPM Voltage Item Filter", 7)
                .getStackForm(1L));
        ItemList.Automation_Filter_UV.set(
            new GT_MetaTileEntity_Filter(9248, "automation.filter.tier.08", "Ultimate Voltage Item Filter", 8)
                .getStackForm(1L));
        ItemList.Automation_Filter_MAX.set(
            new GT_MetaTileEntity_Filter(9249, "automation.filter.tier.09", "Highly Ultimate Voltage Item Filter", 9)
                .getStackForm(1L));

        ItemList.Automation_TypeFilter_ULV.set(
            new GT_MetaTileEntity_TypeFilter(9250, "automation.typefilter.tier.00", "Ultra Low Voltage Type Filter", 0)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_LV.set(
            new GT_MetaTileEntity_TypeFilter(9251, "automation.typefilter.tier.01", "Low Voltage Type Filter", 1)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_MV.set(
            new GT_MetaTileEntity_TypeFilter(9252, "automation.typefilter.tier.02", "Medium Voltage Type Filter", 2)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_HV.set(
            new GT_MetaTileEntity_TypeFilter(9253, "automation.typefilter.tier.03", "High Voltage Type Filter", 3)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_EV.set(
            new GT_MetaTileEntity_TypeFilter(9254, "automation.typefilter.tier.04", "Extreme Voltage Type Filter", 4)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_IV.set(
            new GT_MetaTileEntity_TypeFilter(9255, "automation.typefilter.tier.05", "Insane Voltage Type Filter", 5)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_LuV.set(
            new GT_MetaTileEntity_TypeFilter(9256, "automation.typefilter.tier.06", "Ludicrous Voltage Type Filter", 6)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_ZPM.set(
            new GT_MetaTileEntity_TypeFilter(9257, "automation.typefilter.tier.07", "ZPM Voltage Type Filter", 7)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_UV.set(
            new GT_MetaTileEntity_TypeFilter(9258, "automation.typefilter.tier.08", "Ultimate Voltage Type Filter", 8)
                .getStackForm(1L));
        ItemList.Automation_TypeFilter_MAX.set(
            new GT_MetaTileEntity_TypeFilter(
                9259,
                "automation.typefilter.tier.09",
                "Highly Ultimate Voltage Type Filter",
                9).getStackForm(1L));

        ItemList.Automation_Regulator_ULV.set(
            new GT_MetaTileEntity_Regulator(9270, "automation.regulator.tier.00", "Ultra Low Voltage Regulator", 0)
                .getStackForm(1L));
        ItemList.Automation_Regulator_LV.set(
            new GT_MetaTileEntity_Regulator(9271, "automation.regulator.tier.01", "Low Voltage Regulator", 1)
                .getStackForm(1L));
        ItemList.Automation_Regulator_MV.set(
            new GT_MetaTileEntity_Regulator(9272, "automation.regulator.tier.02", "Medium Voltage Regulator", 2)
                .getStackForm(1L));
        ItemList.Automation_Regulator_HV.set(
            new GT_MetaTileEntity_Regulator(9273, "automation.regulator.tier.03", "High Voltage Regulator", 3)
                .getStackForm(1L));
        ItemList.Automation_Regulator_EV.set(
            new GT_MetaTileEntity_Regulator(9274, "automation.regulator.tier.04", "Extreme Voltage Regulator", 4)
                .getStackForm(1L));
        ItemList.Automation_Regulator_IV.set(
            new GT_MetaTileEntity_Regulator(9275, "automation.regulator.tier.05", "Insane Voltage Regulator", 5)
                .getStackForm(1L));
        ItemList.Automation_Regulator_LuV.set(
            new GT_MetaTileEntity_Regulator(9276, "automation.regulator.tier.06", "Ludicrous Voltage Regulator", 6)
                .getStackForm(1L));
        ItemList.Automation_Regulator_ZPM.set(
            new GT_MetaTileEntity_Regulator(9277, "automation.regulator.tier.07", "ZPM Voltage Regulator", 7)
                .getStackForm(1L));
        ItemList.Automation_Regulator_UV.set(
            new GT_MetaTileEntity_Regulator(9278, "automation.regulator.tier.08", "Ultimate Voltage Regulator", 8)
                .getStackForm(1L));
        ItemList.Automation_Regulator_MAX.set(
            new GT_MetaTileEntity_Regulator(
                9279,
                "automation.regulator.tier.09",
                "Highly Ultimate Voltage Regulator",
                9).getStackForm(1L));

        ItemList.Automation_SuperBuffer_ULV.set(
            new GT_MetaTileEntity_SuperBuffer(
                9300,
                "automation.superbuffer.tier.00",
                "Ultra Low Voltage Super Buffer",
                0).getStackForm(1L));
        ItemList.Automation_SuperBuffer_LV.set(
            new GT_MetaTileEntity_SuperBuffer(9301, "automation.superbuffer.tier.01", "Low Voltage Super Buffer", 1)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_MV.set(
            new GT_MetaTileEntity_SuperBuffer(9302, "automation.superbuffer.tier.02", "Medium Voltage Super Buffer", 2)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_HV.set(
            new GT_MetaTileEntity_SuperBuffer(9303, "automation.superbuffer.tier.03", "High Voltage Super Buffer", 3)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_EV.set(
            new GT_MetaTileEntity_SuperBuffer(9304, "automation.superbuffer.tier.04", "Extreme Voltage Super Buffer", 4)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_IV.set(
            new GT_MetaTileEntity_SuperBuffer(9305, "automation.superbuffer.tier.05", "Insane Voltage Super Buffer", 5)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_LuV.set(
            new GT_MetaTileEntity_SuperBuffer(
                9306,
                "automation.superbuffer.tier.06",
                "Ludicrous Voltage Super Buffer",
                6).getStackForm(1L));
        ItemList.Automation_SuperBuffer_ZPM.set(
            new GT_MetaTileEntity_SuperBuffer(9307, "automation.superbuffer.tier.07", "ZPM Voltage Super Buffer", 7)
                .getStackForm(1L));
        ItemList.Automation_SuperBuffer_UV.set(
            new GT_MetaTileEntity_SuperBuffer(
                9308,
                "automation.superbuffer.tier.08",
                "Ultimate Voltage Super Buffer",
                8).getStackForm(1L));
        ItemList.Automation_SuperBuffer_MAX.set(
            new GT_MetaTileEntity_SuperBuffer(
                9309,
                "automation.superbuffer.tier.09",
                "Highly Ultimate Voltage Super Buffer",
                9).getStackForm(1L));

        ItemList.Automation_ItemDistributor_ULV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9320,
                "automation.itemdistributor.tier.00",
                "Ultra Low Voltage Item Distributor",
                0).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9321,
                "automation.itemdistributor.tier.01",
                "Low Voltage Item Distributor",
                1).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9322,
                "automation.itemdistributor.tier.02",
                "Medium Voltage Item Distributor",
                2).getStackForm(1L));
        ItemList.Automation_ItemDistributor_HV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9323,
                "automation.itemdistributor.tier.03",
                "High Voltage Item Distributor",
                3).getStackForm(1L));
        ItemList.Automation_ItemDistributor_EV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9324,
                "automation.itemdistributor.tier.04",
                "Extreme Voltage Item Distributor",
                4).getStackForm(1L));
        ItemList.Automation_ItemDistributor_IV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9325,
                "automation.itemdistributor.tier.05",
                "Insane Voltage Item Distributor",
                5).getStackForm(1L));
        ItemList.Automation_ItemDistributor_LuV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9326,
                "automation.itemdistributor.tier.06",
                "Ludicrous Voltage Item Distributor",
                6).getStackForm(1L));
        ItemList.Automation_ItemDistributor_ZPM.set(
            new GT_MetaTileEntity_ItemDistributor(
                9327,
                "automation.itemdistributor.tier.07",
                "ZPM Voltage Item Distributor",
                7).getStackForm(1L));
        ItemList.Automation_ItemDistributor_UV.set(
            new GT_MetaTileEntity_ItemDistributor(
                9328,
                "automation.itemdistributor.tier.08",
                "Ultimate Voltage Item Distributor",
                8).getStackForm(1L));
        ItemList.Automation_ItemDistributor_MAX.set(
            new GT_MetaTileEntity_ItemDistributor(
                9329,
                "automation.itemdistributor.tier.09",
                "MAX Voltage Item Distributor",
                9).getStackForm(1L));

        ItemList.Automation_RecipeFilter_ULV.set(
            new GT_MetaTileEntity_RecipeFilter(
                9330,
                "automation.recipefilter.tier.00",
                "Ultra Low Voltage Recipe Filter",
                0).getStackForm(1L));
        ItemList.Automation_RecipeFilter_LV.set(
            new GT_MetaTileEntity_RecipeFilter(9331, "automation.recipefilter.tier.01", "Low Voltage Recipe Filter", 1)
                .getStackForm(1L));
        ItemList.Automation_RecipeFilter_MV.set(
            new GT_MetaTileEntity_RecipeFilter(
                9332,
                "automation.recipefilter.tier.02",
                "Medium Voltage Recipe Filter",
                2).getStackForm(1L));
        ItemList.Automation_RecipeFilter_HV.set(
            new GT_MetaTileEntity_RecipeFilter(9333, "automation.recipefilter.tier.03", "High Voltage Recipe Filter", 3)
                .getStackForm(1L));
        ItemList.Automation_RecipeFilter_EV.set(
            new GT_MetaTileEntity_RecipeFilter(
                9334,
                "automation.recipefilter.tier.04",
                "Extreme Voltage Recipe Filter",
                4).getStackForm(1L));
        ItemList.Automation_RecipeFilter_IV.set(
            new GT_MetaTileEntity_RecipeFilter(
                9335,
                "automation.recipefilter.tier.05",
                "Insane Voltage Recipe Filter",
                5).getStackForm(1L));
        ItemList.Automation_RecipeFilter_LuV.set(
            new GT_MetaTileEntity_RecipeFilter(
                9336,
                "automation.recipefilter.tier.06",
                "Ludicrous Voltage Recipe Filter",
                6).getStackForm(1L));
        ItemList.Automation_RecipeFilter_ZPM.set(
            new GT_MetaTileEntity_RecipeFilter(9337, "automation.recipefilter.tier.07", "ZPM Voltage Recipe Filter", 7)
                .getStackForm(1L));
        ItemList.Automation_RecipeFilter_UV.set(
            new GT_MetaTileEntity_RecipeFilter(
                9338,
                "automation.recipefilter.tier.08",
                "Ultimate Voltage Recipe Filter",
                8).getStackForm(1L));
        ItemList.Automation_RecipeFilter_MAX.set(
            new GT_MetaTileEntity_RecipeFilter(
                9339,
                "automation.recipefilter.tier.09",
                "Highly Ultimate Voltage Recipe Filter",
                9).getStackForm(1L));

    }

    @SuppressWarnings("PointlessArithmeticExpression")
    private static void makeWires(Materials aMaterial, int aStartID, long aLossInsulated, long aLoss, long aAmperage,
        long aVoltage, boolean aInsulatable, boolean aAutoInsulated) {
        String name = GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName;
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt01,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 0,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".01",
                "1x " + name + aTextWire2,
                0.125F,
                aMaterial,
                aLoss,
                1L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt02,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 1,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".02",
                "2x " + name + aTextWire2,
                0.25F,
                aMaterial,
                aLoss,
                2L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt04,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 2,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".04",
                "4x " + name + aTextWire2,
                0.375F,
                aMaterial,
                aLoss,
                4L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt08,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 3,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".08",
                "8x " + name + aTextWire2,
                0.5F,
                aMaterial,
                aLoss,
                8L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt12,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 4,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".12",
                "12x " + name + aTextWire2,
                0.625F,
                aMaterial,
                aLoss,
                12L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.wireGt16,
            aMaterial,
            new GT_MetaPipeEntity_Cable(
                aStartID + 5,
                aTextWire1 + aMaterial.mName.toLowerCase() + ".16",
                "16x " + name + aTextWire2,
                0.75F,
                aMaterial,
                aLoss,
                16L * aAmperage,
                aVoltage,
                false,
                !aAutoInsulated).getStackForm(1L));
        if (aInsulatable) {
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt01,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 6,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".01",
                    "1x " + name + aTextCable2,
                    0.25F,
                    aMaterial,
                    aLossInsulated,
                    1L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt02,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 7,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".02",
                    "2x " + name + aTextCable2,
                    0.375F,
                    aMaterial,
                    aLossInsulated,
                    2L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt04,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 8,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".04",
                    "4x " + name + aTextCable2,
                    0.5F,
                    aMaterial,
                    aLossInsulated,
                    4L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt08,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 9,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".08",
                    "8x " + name + aTextCable2,
                    0.625F,
                    aMaterial,
                    aLossInsulated,
                    8L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt12,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 10,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".12",
                    "12x " + name + aTextCable2,
                    0.75F,
                    aMaterial,
                    aLossInsulated,
                    12L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
            GT_OreDictUnificator.registerOre(
                OrePrefixes.cableGt16,
                aMaterial,
                new GT_MetaPipeEntity_Cable(
                    aStartID + 11,
                    aTextCable1 + aMaterial.mName.toLowerCase() + ".16",
                    "16x " + name + aTextCable2,
                    0.875F,
                    aMaterial,
                    aLossInsulated,
                    16L * aAmperage,
                    aVoltage,
                    true,
                    false).getStackForm(1L));
        }
    }

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Registering MetaTileEntities.");
        run1();
        run2();
        run3();
        run4();
    }

    private static void generateItemPipes(Materials aMaterial, String name, int startID, int baseInvSlots) {
        generateItemPipes(
            aMaterial,
            name,
            GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName,
            startID,
            baseInvSlots);
    }

    private static void generateItemPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseInvSlots) {
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID,
                "GT_Pipe_" + name,
                displayName + " Item Pipe",
                0.50F,
                aMaterial,
                baseInvSlots,
                32768 / baseInvSlots,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 1,
                "GT_Pipe_" + name + "_Large",
                "Large " + displayName + " Item Pipe",
                0.75F,
                aMaterial,
                baseInvSlots * 2,
                16384 / baseInvSlots,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 2,
                "GT_Pipe_" + name + "_Huge",
                "Huge " + displayName + " Item Pipe",
                1.00F,
                aMaterial,
                baseInvSlots * 4,
                8192 / baseInvSlots,
                false).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveMedium.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 3,
                "GT_Pipe_Restrictive_" + name,
                "Restrictive " + displayName + " Item Pipe",
                0.50F,
                aMaterial,
                baseInvSlots,
                3276800 / baseInvSlots,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveLarge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 4,
                "GT_Pipe_Restrictive_" + name + "_Large",
                "Large Restrictive " + displayName + " Item Pipe",
                0.75F,
                aMaterial,
                baseInvSlots * 2,
                1638400 / baseInvSlots,
                true).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeRestrictiveHuge.get(aMaterial),
            new GT_MetaPipeEntity_Item(
                startID + 5,
                "GT_Pipe_Restrictive_" + name + "_Huge",
                "Huge Restrictive " + displayName + " Item Pipe",
                0.875F,
                aMaterial,
                baseInvSlots * 4,
                819200 / baseInvSlots,
                true).getStackForm(1L));
    }

    @SuppressWarnings("SameParameterValue")
    private static void generateFluidPipes(Materials aMaterial, String name, int startID, int baseCapacity,
        int heatCapacity, boolean gasProof) {
        generateFluidPipes(
            aMaterial,
            name,
            GT_LanguageManager.i18nPlaceholder ? "%material" : aMaterial.mDefaultLocalName,
            startID,
            baseCapacity,
            heatCapacity,
            gasProof);
    }

    private static void generateFluidPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseCapacity, int heatCapacity, boolean gasProof) {
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeTiny.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID,
                "GT_Pipe_" + name + "_Tiny",
                "Tiny " + displayName + " Fluid Pipe",
                0.25F,
                aMaterial,
                baseCapacity / 6,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeSmall.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 1,
                "GT_Pipe_" + name + "_Small",
                "Small " + displayName + " Fluid Pipe",
                0.375F,
                aMaterial,
                baseCapacity / 3,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeMedium.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 2,
                "GT_Pipe_" + name,
                displayName + " Fluid Pipe",
                0.5F,
                aMaterial,
                baseCapacity,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeLarge.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 3,
                "GT_Pipe_" + name + "_Large",
                "Large " + displayName + " Fluid Pipe",
                0.75F,
                aMaterial,
                baseCapacity * 2,
                heatCapacity,
                gasProof).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeHuge.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 4,
                "GT_Pipe_" + name + "_Huge",
                "Huge " + displayName + " Fluid Pipe",
                0.875F,
                aMaterial,
                baseCapacity * 4,
                heatCapacity,
                gasProof).getStackForm(1L));
    }

    @SuppressWarnings("SameParameterValue")
    private static void generateFluidMultiPipes(Materials aMaterial, String name, int startID, int baseCapacity,
        int heatCapacity, boolean gasProof) {
        generateFluidMultiPipes(aMaterial, name, "%material", startID, baseCapacity, heatCapacity, gasProof);
    }

    private static void generateFluidMultiPipes(Materials aMaterial, String name, String displayName, int startID,
        int baseCapacity, int heatCapacity, boolean gasProof) {
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeQuadruple.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID,
                "GT_Pipe_" + name + "_Quadruple",
                "Quadruple " + displayName + " Fluid Pipe",
                1.0F,
                aMaterial,
                baseCapacity,
                heatCapacity,
                gasProof,
                4).getStackForm(1L));
        GT_OreDictUnificator.registerOre(
            OrePrefixes.pipeNonuple.get(aMaterial),
            new GT_MetaPipeEntity_Fluid(
                startID + 1,
                "GT_Pipe_" + name + "_Nonuple",
                "Nonuple " + displayName + " Fluid Pipe",
                1.0F,
                aMaterial,
                baseCapacity / 3,
                heatCapacity,
                gasProof,
                9).getStackForm(1L));
    }
}
