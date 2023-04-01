package gregtech.loaders.preload;

import static gregtech.api.enums.ModIDs.*;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import codechicken.nei.api.API;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine_GT_Recipe.SpecialEffects;
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
import gregtech.loaders.postload.GT_PCBFactoryMaterialLoader;
import gregtech.loaders.postload.GT_ProcessingArrayRecipeLoader;
import ic2.core.Ic2Items;

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

public class GT_Loader_MetaTileEntities implements Runnable { // TODO CHECK CIRCUIT RECIPES AND USAGES

    private static final String aTextWire1 = "wire.";
    private static final String aTextCable1 = "cable.";
    private static final String aTextWire2 = " Wire";
    private static final String aTextCable2 = " Cable";
    private static final String aTextPlate = "PPP";
    private static final String aTextPlateWrench = "PwP";
    private static final String aTextPlateMotor = "PMP";
    private static final String aTextCableHull = "CMC";
    private static final String aTextWireHull = "WMW";
    private static final String aTextWireChest = "WTW";
    private static final String aTextWireCoil = "WCW";
    private static final String aTextMotorWire = "EWE";
    private static final String aTextWirePump = "WPW";
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
    private static final long bits = GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE
            | GT_ModHandler.RecipeBits.BUFFERED;
    private static final long bitsd = GT_ModHandler.RecipeBits.DISMANTLEABLE | bits;

    private static void run1() {
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Pipe_Polytetrafluoroethylene.get(1L),
                bits,
                new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polytetrafluoroethylene), 'F',
                        OrePrefixes.frameGt.get(Materials.Polytetrafluoroethylene), 'I',
                        OrePrefixes.pipeMedium.get(Materials.Polytetrafluoroethylene) });

        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Pipe_Polybenzimidazole.get(1L),
                bits,
                new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Polybenzimidazole), 'F',
                        OrePrefixes.frameGt.get(Materials.Polybenzimidazole), 'I',
                        OrePrefixes.pipeMedium.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_ULV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_LV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_MV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_HV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_EV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_IV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_LuV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.Chrome) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_ZPM.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.Iridium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_UV.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.Osmium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_MAX.get(1L),
                bits,
                new Object[] { aTextPlate, aTextPlateWrench, aTextPlate, 'P',
                        OrePrefixes.plate.get(Materials.Neutronium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_BronzePlatedBricks.get(1L),
                bits,
                new Object[] { "PhP", "PBP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                        new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                        OrePrefixes.frameGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_StableTitanium.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                        OrePrefixes.frameGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_HeatProof.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Invar), 'F',
                        OrePrefixes.frameGt.get(Materials.Invar) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_FrostProof.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Aluminium), 'F',
                        OrePrefixes.frameGt.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_CleanStainlessSteel.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel),
                        'F', OrePrefixes.frameGt.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_RobustTungstenSteel.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                        OrePrefixes.frameGt.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_MiningOsmiridium.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Osmiridium), 'F',
                        OrePrefixes.frameGt.get(Materials.Osmiridium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_MiningNeutronium.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Neutronium), 'F',
                        OrePrefixes.frameGt.get(Materials.Neutronium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_MiningBlackPlutonium.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.BlackPlutonium),
                        'F', OrePrefixes.frameGt.get(Materials.BlackPlutonium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Turbine.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Magnalium), 'F',
                        OrePrefixes.frameGt.get(Materials.BlueSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Turbine1.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.StainlessSteel),
                        'F', ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Turbine2.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                        ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Turbine3.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                        ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_TurbineGasAdvanced.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.HSSS), 'F',
                        ItemList.Casing_Turbine });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Pipe_Bronze.get(1L),
                bits,
                new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                        OrePrefixes.frameGt.get(Materials.Bronze), 'I', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Pipe_Steel.get(1L),
                bits,
                new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                        OrePrefixes.frameGt.get(Materials.Steel), 'I', OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Pipe_Titanium.get(1L),
                bits,
                new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                        OrePrefixes.frameGt.get(Materials.Titanium), 'I',
                        OrePrefixes.pipeMedium.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Pipe_TungstenSteel.get(1L),
                bits,
                new Object[] { "PIP", "IFI", "PIP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                        OrePrefixes.frameGt.get(Materials.TungstenSteel), 'I',
                        OrePrefixes.pipeMedium.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Gearbox_Bronze.get(1L),
                bits,
                new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                        OrePrefixes.frameGt.get(Materials.Bronze), 'G', OrePrefixes.gearGt.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Gearbox_Steel.get(1L),
                bits,
                new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                        OrePrefixes.frameGt.get(Materials.Steel), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Gearbox_Titanium.get(1L),
                bits,
                new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                        OrePrefixes.frameGt.get(Materials.Titanium), 'G', OrePrefixes.gearGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Gearbox_TungstenSteel.get(1L),
                bits,
                new Object[] { "PhP", "GFG", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                        OrePrefixes.frameGt.get(Materials.TungstenSteel), 'G', ItemList.Robot_Arm_IV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Grate.get(1L),
                bits,
                new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', new ItemStack(Blocks.iron_bars, 1), 'F',
                        OrePrefixes.frameGt.get(Materials.Steel), 'M', ItemList.Electric_Motor_MV, 'V',
                        OrePrefixes.rotor.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Assembler.get(1L),
                bits,
                new Object[] { "PVP", "PFP", aTextPlateMotor, 'P', OrePrefixes.circuit.get(Materials.Ultimate), 'F',
                        OrePrefixes.frameGt.get(Materials.TungstenSteel), 'M', ItemList.Electric_Motor_IV, 'V',
                        OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Firebox_Bronze.get(1L),
                bits,
                new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Bronze), 'F',
                        OrePrefixes.frameGt.get(Materials.Bronze), 'S', OrePrefixes.stick.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Firebox_Steel.get(1L),
                bits,
                new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Steel), 'F',
                        OrePrefixes.frameGt.get(Materials.Steel), 'S', OrePrefixes.stick.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Firebox_Titanium.get(1L),
                bits,
                new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'F',
                        OrePrefixes.frameGt.get(Materials.Titanium), 'S', OrePrefixes.stick.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Firebox_TungstenSteel.get(1L),
                bits,
                new Object[] { "PSP", "SFS", "PSP", 'P', OrePrefixes.plate.get(Materials.TungstenSteel), 'F',
                        OrePrefixes.frameGt.get(Materials.TungstenSteel), 'S',
                        OrePrefixes.stick.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Stripes_A.get(1L),
                bits,
                new Object[] { "Y  ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Stripes_B.get(1L),
                bits,
                new Object[] { "  Y", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_RadioactiveHazard.get(1L),
                bits,
                new Object[] { " YB", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_BioHazard.get(1L),
                bits,
                new Object[] { " Y ", " MB", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_ExplosionHazard.get(1L),
                bits,
                new Object[] { " Y ", " M ", "  B", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_FireHazard.get(1L),
                bits,
                new Object[] { " Y ", " M ", " B ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_AcidHazard.get(1L),
                bits,
                new Object[] { " Y ", " M ", "B  ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_MagicHazard.get(1L),
                bits,
                new Object[] { " Y ", "BM ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_FrostHazard.get(1L),
                bits,
                new Object[] { "BY ", " M ", "   ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_NoiseHazard.get(1L),
                bits,
                new Object[] { "   ", " M ", "BY ", 'M', ItemList.Casing_SolidSteel, 'Y', Dyes.dyeYellow, 'B',
                        Dyes.dyeBlack });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Advanced_Iridium.get(1L),
                bits,
                new Object[] { "PhP", "PFP", aTextPlateWrench, 'P', OrePrefixes.plate.get(Materials.Iridium), 'F',
                        OrePrefixes.frameGt.get(Materials.Iridium) });

        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_Stripes_A });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_Stripes_B });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_RadioactiveHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_BioHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_ExplosionHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_FireHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_AcidHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_MagicHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_FrostHazard });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Casing_SolidSteel.get(1L),
                bits,
                new Object[] { ItemList.Casing_NoiseHazard });

        ItemList.Machine_Bricked_BlastFurnace.set(
                new GT_MetaTileEntity_BrickedBlastFurnace(
                        140,
                        "multimachine.brickedblastfurnace",
                        "Bricked Blast Furnace").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bricked_BlastFurnace.get(1L),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "BFB", "FwF", "BFB", 'B', ItemList.Casing_Firebricks, 'F',
                        OreDictNames.craftingIronFurnace });

        ItemList.Hull_Bronze.set(
                new GT_MetaTileEntity_BasicHull_Bronze(
                        1,
                        "hull.bronze",
                        "Bronze Hull",
                        0,
                        "For your first Steam Machines").getStackForm(1L));
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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_Bronze.get(1L),
                bits,
                new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_Bronze_Bricks.get(1L),
                bits,
                new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.Bronze), 'B',
                        new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_HP.get(1L),
                bits,
                new Object[] { aTextPlate, "PhP", aTextPlate, 'P', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_HP_Bricks.get(1L),
                bits,
                new Object[] { aTextPlate, "PhP", "BBB", 'P', OrePrefixes.plate.get(Materials.WroughtIron), 'B',
                        new ItemStack(Blocks.brick_block, 1) });

        ItemList.Hull_ULV.set(
                new GT_MetaTileEntity_BasicHull(10, "hull.tier.00", "ULV Machine Hull", 0, imagination)
                        .getStackForm(1L));
        ItemList.Hull_LV.set(
                new GT_MetaTileEntity_BasicHull(11, "hull.tier.01", "LV Machine Hull", 1, imagination)
                        .getStackForm(1L));
        ItemList.Hull_MV.set(
                new GT_MetaTileEntity_BasicHull(12, "hull.tier.02", "MV Machine Hull", 2, imagination)
                        .getStackForm(1L));
        ItemList.Hull_HV.set(
                new GT_MetaTileEntity_BasicHull(13, "hull.tier.03", "HV Machine Hull", 3, imagination)
                        .getStackForm(1L));
        ItemList.Hull_EV.set(
                new GT_MetaTileEntity_BasicHull(14, "hull.tier.04", "EV Machine Hull", 4, imagination)
                        .getStackForm(1L));
        ItemList.Hull_IV.set(
                new GT_MetaTileEntity_BasicHull(15, "hull.tier.05", "IV Machine Hull", 5, imagination)
                        .getStackForm(1L));
        ItemList.Hull_LuV.set(
                new GT_MetaTileEntity_BasicHull(16, "hull.tier.06", "LuV Machine Hull", 6, imagination)
                        .getStackForm(1L));
        ItemList.Hull_ZPM.set(
                new GT_MetaTileEntity_BasicHull(17, "hull.tier.07", "ZPM Machine Hull", 7, imagination)
                        .getStackForm(1L));
        ItemList.Hull_UV.set(
                new GT_MetaTileEntity_BasicHull(18, "hull.tier.08", "UV Machine Hull", 8, imagination)
                        .getStackForm(1L));
        ItemList.Hull_MAX.set(
                new GT_MetaTileEntity_BasicHull(19, "hull.tier.09", "UHV Machine Hull", 9, imagination)
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_ULV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_ULV, 'C', OrePrefixes.cableGt01.get(Materials.Lead),
                        'H', OrePrefixes.plate.get(Materials.WroughtIron), 'P',
                        OrePrefixes.plate.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_LV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_LV, 'C', OrePrefixes.cableGt01.get(Materials.Tin),
                        'H', OrePrefixes.plate.get(Materials.Steel), 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_MV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_MV, 'C',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper), 'H', OrePrefixes.plate.get(Materials.Aluminium),
                        'P', OrePrefixes.plate.get(Materials.WroughtIron) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_HV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_HV, 'C', OrePrefixes.cableGt01.get(Materials.Gold),
                        'H', OrePrefixes.plate.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.plate.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_EV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_EV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Aluminium), 'H', OrePrefixes.plate.get(Materials.Titanium),
                        'P', OrePrefixes.plate.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_IV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_IV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Tungsten), 'H',
                        OrePrefixes.plate.get(Materials.TungstenSteel), 'P',
                        OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_LuV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                        OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H',
                        OrePrefixes.plate.get(Materials.Chrome), 'P',
                        OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_ZPM.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_ZPM, 'C',
                        OrePrefixes.cableGt02.get(Materials.Naquadah), 'H', OrePrefixes.plate.get(Materials.Iridium),
                        'P', OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_UV.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                        OrePrefixes.cableGt04.get(Materials.NaquadahAlloy), 'H',
                        OrePrefixes.plate.get(Materials.Osmium), 'P',
                        OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hull_MAX.get(1L),
                GT_ModHandler.RecipeBits.REVERSIBLE,
                new Object[] { aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                        OrePrefixes.wireGt04.get(Materials.SuperconductorUV), 'H',
                        OrePrefixes.plate.get(Materials.Neutronium), 'P',
                        OrePrefixes.plate.get(Materials.Polybenzimidazole) });

        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_ULV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_LV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_MV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_HV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_EV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_IV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_LuV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_ZPM.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_UV.get(1L));
        GT_ModHandler.removeRecipeByOutput(ItemList.Hull_MAX.get(1L));

        if (GT_Mod.gregtechproxy.mHardMachineCasings) {
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_ULV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ULV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Lead), 'H',
                            OrePrefixes.plate.get(Materials.WroughtIron), 'P', OrePrefixes.plate.get(Materials.Wood) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_LV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Tin), 'H', OrePrefixes.plate.get(Materials.Steel), 'P',
                            OrePrefixes.plate.get(Materials.WroughtIron) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_MV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Copper), 'H',
                            OrePrefixes.plate.get(Materials.Aluminium), 'P',
                            OrePrefixes.plate.get(Materials.WroughtIron) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_HV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_HV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Gold), 'H',
                            OrePrefixes.plate.get(Materials.StainlessSteel), 'P',
                            OrePrefixes.plate.get(Materials.Plastic) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_EV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_EV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Aluminium), 'H',
                            OrePrefixes.plate.get(Materials.Titanium), 'P', OrePrefixes.plate.get(Materials.Plastic) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_IV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_IV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Tungsten), 'H',
                            OrePrefixes.plate.get(Materials.TungstenSteel), 'P',
                            OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_LuV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                            OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'H',
                            OrePrefixes.plate.get(Materials.Chrome), 'P',
                            OrePrefixes.plate.get(Materials.Polytetrafluoroethylene) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_ZPM.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_ZPM, 'C',
                            OrePrefixes.cableGt01.get(Materials.Naquadah), 'H',
                            OrePrefixes.plate.get(Materials.Iridium), 'P',
                            OrePrefixes.plate.get(Materials.Polybenzimidazole) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_UV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                            OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'H',
                            OrePrefixes.plate.get(Materials.Osmium), 'P',
                            OrePrefixes.plate.get(Materials.Polybenzimidazole) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_MAX.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "PHP", aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                            OrePrefixes.wireGt04.get(Materials.SuperconductorUV), 'H',
                            OrePrefixes.plate.get(Materials.Neutronium), 'P',
                            OrePrefixes.plate.get(Materials.Polybenzimidazole) });
        } else {
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_ULV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_ULV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Lead) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_LV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_LV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Tin) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_MV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_MV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Copper) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_HV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_HV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Gold) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_EV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_EV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Aluminium) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_IV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_IV, 'C',
                            OrePrefixes.cableGt01.get(Materials.Tungsten) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_LuV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_LuV, 'C',
                            OrePrefixes.cableGt01.get(Materials.VanadiumGallium) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_ZPM.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_ZPM, 'C',
                            OrePrefixes.cableGt01.get(Materials.Naquadah) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_UV.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_UV, 'C',
                            OrePrefixes.wireGt04.get(Materials.NaquadahAlloy) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Hull_MAX.get(1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { aTextCableHull, 'M', ItemList.Casing_MAX, 'C',
                            OrePrefixes.wireGt04.get(Materials.SuperconductorUV) });
        }
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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_LV_ULV.get(1L),
                bitsd,
                new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_ULV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Tin), 'B', OrePrefixes.cableGt01.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_MV_LV.get(1L),
                bitsd,
                new Object[] { " BB", "CM ", " BB", 'M', ItemList.Hull_LV, 'C',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B',
                        OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_HV_MV.get(1L),
                bitsd,
                new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_MV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Gold), 'B', OrePrefixes.cableGt01.get(Materials.AnyCopper),
                        'K', ItemList.Circuit_Parts_Coil });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_EV_HV.get(1L),
                bitsd,
                new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_HV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OrePrefixes.cableGt01.get(Materials.Gold),
                        'K', ItemList.Circuit_Chip_ULPIC });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_IV_EV.get(1L),
                bitsd,
                new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_EV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Tungsten), 'B',
                        OrePrefixes.cableGt01.get(Materials.Aluminium), 'K', ItemList.Circuit_Chip_LPIC });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_LuV_IV.get(1L),
                bitsd,
                new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_IV, 'C',
                        OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B',
                        OrePrefixes.cableGt01.get(Materials.Tungsten), 'K', ItemList.Circuit_Chip_PIC });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_ZPM_LuV.get(1L),
                bitsd,
                new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_LuV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Naquadah), 'B',
                        OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'K', ItemList.Circuit_Chip_HPIC });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_UV_ZPM.get(1L),
                bitsd,
                new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_ZPM, 'C',
                        OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B',
                        OrePrefixes.cableGt01.get(Materials.Naquadah), 'K', ItemList.Circuit_Chip_UHPIC });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Transformer_MAX_UV.get(1L),
                bitsd,
                new Object[] { "KBB", "CM ", "KBB", 'M', ItemList.Hull_UV, 'C',
                        OrePrefixes.wireGt01.get(Materials.Bedrockium), 'B',
                        OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'K', ItemList.Circuit_Chip_NPIC });

        ItemList.Hatch_Dynamo_ULV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(30, "hatch.dynamo.tier.00", "ULV Dynamo Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Dynamo_LV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(31, "hatch.dynamo.tier.01", "LV Dynamo Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Dynamo_MV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(32, "hatch.dynamo.tier.02", "MV Dynamo Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Dynamo_HV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(33, "hatch.dynamo.tier.03", "HV Dynamo Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Dynamo_EV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(34, "hatch.dynamo.tier.04", "EV Dynamo Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Dynamo_IV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(35, "hatch.dynamo.tier.05", "IV Dynamo Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Dynamo_LuV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(36, "hatch.dynamo.tier.06", "LuV Dynamo Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Dynamo_ZPM.set(
                new GT_MetaTileEntity_Hatch_Dynamo(37, "hatch.dynamo.tier.07", "ZPM Dynamo Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Dynamo_UV.set(
                new GT_MetaTileEntity_Hatch_Dynamo(38, "hatch.dynamo.tier.08", "UV Dynamo Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Dynamo_MAX.set(
                new GT_MetaTileEntity_Hatch_Dynamo(39, "hatch.dynamo.tier.09", "UHV Dynamo Hatch", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Dynamo_ULV.get(1L),
                bitsd,
                new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_ULV, 'S', OrePrefixes.spring.get(Materials.Lead),
                        'X', OrePrefixes.circuit.get(Materials.Primitive), 'O', ItemList.ULV_Coil, 'L',
                        OrePrefixes.cell.get(Materials.Lubricant), 'P', OrePrefixes.rotor.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Dynamo_LV.get(1L),
                bitsd,
                new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_LV, 'S', OrePrefixes.spring.get(Materials.Tin),
                        'X', OrePrefixes.circuit.get(Materials.Basic), 'O', ItemList.LV_Coil, 'L',
                        OrePrefixes.cell.get(Materials.Lubricant), 'P', ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Dynamo_MV.get(1L),
                bitsd,
                new Object[] { "XOL", "SMP", "XOL", 'M', ItemList.Hull_MV, 'S',
                        OrePrefixes.spring.get(Materials.Copper), 'X', ItemList.Circuit_Chip_ULPIC, 'O',
                        ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P',
                        ItemList.Electric_Pump_MV });
        // 1234
        ItemList.Hatch_Energy_ULV.set(
                new GT_MetaTileEntity_Hatch_Energy(40, "hatch.energy.tier.00", "ULV Energy Hatch", 0).getStackForm(1L));
        ItemList.Hatch_Energy_LV.set(
                new GT_MetaTileEntity_Hatch_Energy(41, "hatch.energy.tier.01", "LV Energy Hatch", 1).getStackForm(1L));
        ItemList.Hatch_Energy_MV.set(
                new GT_MetaTileEntity_Hatch_Energy(42, "hatch.energy.tier.02", "MV Energy Hatch", 2).getStackForm(1L));
        ItemList.Hatch_Energy_HV.set(
                new GT_MetaTileEntity_Hatch_Energy(43, "hatch.energy.tier.03", "HV Energy Hatch", 3).getStackForm(1L));
        ItemList.Hatch_Energy_EV.set(
                new GT_MetaTileEntity_Hatch_Energy(44, "hatch.energy.tier.04", "EV Energy Hatch", 4).getStackForm(1L));
        ItemList.Hatch_Energy_IV.set(
                new GT_MetaTileEntity_Hatch_Energy(45, "hatch.energy.tier.05", "IV Energy Hatch", 5).getStackForm(1L));
        ItemList.Hatch_Energy_LuV.set(
                new GT_MetaTileEntity_Hatch_Energy(46, "hatch.energy.tier.06", "LuV Energy Hatch", 6).getStackForm(1L));
        ItemList.Hatch_Energy_ZPM.set(
                new GT_MetaTileEntity_Hatch_Energy(47, "hatch.energy.tier.07", "ZPM Energy Hatch", 7).getStackForm(1L));
        ItemList.Hatch_Energy_UV.set(
                new GT_MetaTileEntity_Hatch_Energy(48, "hatch.energy.tier.08", "UV Energy Hatch", 8).getStackForm(1L));
        ItemList.Hatch_Energy_MAX.set(
                new GT_MetaTileEntity_Hatch_Energy(49, "hatch.energy.tier.09", "UHV Energy Hatch", 9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Energy_ULV.get(1L),
                bitsd,
                new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_ULV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Lead), 'X', OrePrefixes.circuit.get(Materials.Primitive),
                        'O', ItemList.ULV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P',
                        OrePrefixes.rotor.get(Materials.Lead) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Energy_LV.get(1L),
                bitsd,
                new Object[] { "COL", "XMP", "COL", 'M', ItemList.Hull_LV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Tin), 'X', OrePrefixes.circuit.get(Materials.Basic), 'O',
                        ItemList.LV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P',
                        ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Energy_MV.get(1L),
                bitsd,
                new Object[] { "XOL", "CMP", "XOL", 'M', ItemList.Hull_MV, 'C',
                        OrePrefixes.cableGt01.get(Materials.Copper), 'X', ItemList.Circuit_Chip_ULPIC, 'O',
                        ItemList.MV_Coil, 'L', OrePrefixes.cell.get(Materials.Lubricant), 'P',
                        ItemList.Electric_Pump_MV });

        ItemList.Hatch_Input_ULV.set(
                new GT_MetaTileEntity_Hatch_Input(50, "hatch.input.tier.00", "Input Hatch (ULV)", 0).getStackForm(1L));
        ItemList.Hatch_Input_LV.set(
                new GT_MetaTileEntity_Hatch_Input(51, "hatch.input.tier.01", "Input Hatch (LV)", 1).getStackForm(1L));
        ItemList.Hatch_Input_MV.set(
                new GT_MetaTileEntity_Hatch_Input(52, "hatch.input.tier.02", "Input Hatch (MV)", 2).getStackForm(1L));
        ItemList.Hatch_Input_HV.set(
                new GT_MetaTileEntity_Hatch_Input(53, "hatch.input.tier.03", "Input Hatch (HV)", 3).getStackForm(1L));
        ItemList.Hatch_Input_EV.set(
                new GT_MetaTileEntity_Hatch_Input(54, "hatch.input.tier.04", "Input Hatch (EV)", 4).getStackForm(1L));
        ItemList.Hatch_Input_IV.set(
                new GT_MetaTileEntity_Hatch_Input(55, "hatch.input.tier.05", "Input Hatch (IV)", 5).getStackForm(1L));
        ItemList.Hatch_Input_LuV.set(
                new GT_MetaTileEntity_Hatch_Input(56, "hatch.input.tier.06", "Input Hatch (LuV)", 6).getStackForm(1L));
        ItemList.Hatch_Input_ZPM.set(
                new GT_MetaTileEntity_Hatch_Input(57, "hatch.input.tier.07", "Input Hatch (ZPM)", 7).getStackForm(1L));
        ItemList.Hatch_Input_UV.set(
                new GT_MetaTileEntity_Hatch_Input(58, "hatch.input.tier.08", "Input Hatch (UV)", 8).getStackForm(1L));
        ItemList.Hatch_Input_MAX.set(
                new GT_MetaTileEntity_Hatch_Input(59, "hatch.input.tier.09", "Input Hatch (UHV)", 9).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_EV.set(
                new GT_MetaTileEntity_Hatch_MultiInput(
                        200,
                        4,
                        "hatch.multi.input.tier.01",
                        "Quadruple Input Hatch (EV)",
                        4).getStackForm(1L));
        ItemList.Hatch_Input_Multi_2x2_IV.set(
                new GT_MetaTileEntity_Hatch_MultiInput(
                        710,
                        4,
                        "hatch.multi.input.tier.02",
                        "Quadruple Input Hatch (IV)",
                        5).getStackForm(1L));
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
                new GT_MetaTileEntity_Hatch_MultiInput(
                        713,
                        4,
                        "hatch.multi.input.tier.05",
                        "Quadruple Input Hatch (UV)",
                        8).getStackForm(1L));
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
                new GT_MetaTileEntity_Hatch_Output(60, "hatch.output.tier.00", "Output Hatch (ULV)", 0)
                        .getStackForm(1L));
        ItemList.Hatch_Output_LV.set(
                new GT_MetaTileEntity_Hatch_Output(61, "hatch.output.tier.01", "Output Hatch (LV)", 1)
                        .getStackForm(1L));
        ItemList.Hatch_Output_MV.set(
                new GT_MetaTileEntity_Hatch_Output(62, "hatch.output.tier.02", "Output Hatch (MV)", 2)
                        .getStackForm(1L));
        ItemList.Hatch_Output_HV.set(
                new GT_MetaTileEntity_Hatch_Output(63, "hatch.output.tier.03", "Output Hatch (HV)", 3)
                        .getStackForm(1L));
        ItemList.Hatch_Output_EV.set(
                new GT_MetaTileEntity_Hatch_Output(64, "hatch.output.tier.04", "Output Hatch (EV)", 4)
                        .getStackForm(1L));
        ItemList.Hatch_Output_IV.set(
                new GT_MetaTileEntity_Hatch_Output(65, "hatch.output.tier.05", "Output Hatch (IV)", 5)
                        .getStackForm(1L));
        ItemList.Hatch_Output_LuV.set(
                new GT_MetaTileEntity_Hatch_Output(66, "hatch.output.tier.06", "Output Hatch (LuV)", 6)
                        .getStackForm(1L));
        ItemList.Hatch_Output_ZPM.set(
                new GT_MetaTileEntity_Hatch_Output(67, "hatch.output.tier.07", "Output Hatch (ZPM)", 7)
                        .getStackForm(1L));
        ItemList.Hatch_Output_UV.set(
                new GT_MetaTileEntity_Hatch_Output(68, "hatch.output.tier.08", "Output Hatch (UV)", 8)
                        .getStackForm(1L));
        ItemList.Hatch_Output_MAX.set(
                new GT_MetaTileEntity_Hatch_Output(69, "hatch.output.tier.09", "Output Hatch (UHV)", 9)
                        .getStackForm(1L));

        ItemList.Quantum_Tank_LV.set(
                new GT_MetaTileEntity_QuantumTank(120, "quantum.tank.tier.06", "Quantum Tank I", 6).getStackForm(1L));
        ItemList.Quantum_Tank_MV.set(
                new GT_MetaTileEntity_QuantumTank(121, "quantum.tank.tier.07", "Quantum Tank II", 7).getStackForm(1L));
        ItemList.Quantum_Tank_HV.set(
                new GT_MetaTileEntity_QuantumTank(122, "quantum.tank.tier.08", "Quantum Tank III", 8).getStackForm(1L));
        ItemList.Quantum_Tank_EV.set(
                new GT_MetaTileEntity_QuantumTank(123, "quantum.tank.tier.09", "Quantum Tank IV", 9).getStackForm(1L));
        ItemList.Quantum_Tank_IV.set(
                new GT_MetaTileEntity_QuantumTank(124, "quantum.tank.tier.10", "Quantum Tank V", 10).getStackForm(1L));

        ItemList.Quantum_Chest_LV.set(
                new GT_MetaTileEntity_QuantumChest(125, "quantum.chest.tier.06", "Quantum Chest I", 6)
                        .getStackForm(1L));
        ItemList.Quantum_Chest_MV.set(
                new GT_MetaTileEntity_QuantumChest(126, "quantum.chest.tier.07", "Quantum Chest II", 7)
                        .getStackForm(1L));
        ItemList.Quantum_Chest_HV.set(
                new GT_MetaTileEntity_QuantumChest(127, "quantum.chest.tier.08", "Quantum Chest III", 8)
                        .getStackForm(1L));
        ItemList.Quantum_Chest_EV.set(
                new GT_MetaTileEntity_QuantumChest(128, "quantum.chest.tier.09", "Quantum Chest IV", 9)
                        .getStackForm(1L));
        ItemList.Quantum_Chest_IV.set(
                new GT_MetaTileEntity_QuantumChest(129, "quantum.chest.tier.10", "Quantum Chest V", 10)
                        .getStackForm(1L));

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
        ItemList.Super_Chest_MV.set(
                new GT_MetaTileEntity_SuperChest(136, "super.chest.tier.02", "Super Chest II", 2).getStackForm(1L));
        ItemList.Super_Chest_HV.set(
                new GT_MetaTileEntity_SuperChest(137, "super.chest.tier.03", "Super Chest III", 3).getStackForm(1L));
        ItemList.Super_Chest_EV.set(
                new GT_MetaTileEntity_SuperChest(138, "super.chest.tier.04", "Super Chest IV", 4).getStackForm(1L));
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
            // ItemList.Hatch_CraftingInput_Bus_ME.set(new GT_MetaTileEntity_Hatch_CraftingInputBus_ME(2712,
            // "hatch.crafting_input_bus.me", "Crafting Input Bus (ME)").getStackForm(1L));
            ItemList.Hatch_Output_ME.set(
                    new GT_MetaTileEntity_Hatch_Output_ME(2713, "hatch.output.me", "Output Hatch (ME)")
                            .getStackForm(1L));
        }

        ItemList.Hatch_Input_Bus_ULV.set(
                new GT_MetaTileEntity_Hatch_InputBus(70, "hatch.input_bus.tier.00", "Input Bus (ULV)", 0)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_LV.set(
                new GT_MetaTileEntity_Hatch_InputBus(71, "hatch.input_bus.tier.01", "Input Bus (LV)", 1)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_MV.set(
                new GT_MetaTileEntity_Hatch_InputBus(72, "hatch.input_bus.tier.02", "Input Bus (MV)", 2)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_HV.set(
                new GT_MetaTileEntity_Hatch_InputBus(73, "hatch.input_bus.tier.03", "Input Bus (HV)", 3)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_EV.set(
                new GT_MetaTileEntity_Hatch_InputBus(74, "hatch.input_bus.tier.04", "Input Bus (EV)", 4)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_IV.set(
                new GT_MetaTileEntity_Hatch_InputBus(75, "hatch.input_bus.tier.05", "Input Bus (IV)", 5)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_LuV.set(
                new GT_MetaTileEntity_Hatch_InputBus(76, "hatch.input_bus.tier.06", "Input Bus (LuV)", 6)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_ZPM.set(
                new GT_MetaTileEntity_Hatch_InputBus(77, "hatch.input_bus.tier.07", "Input Bus (ZPM)", 7)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_UV.set(
                new GT_MetaTileEntity_Hatch_InputBus(78, "hatch.input_bus.tier.08", "Input Bus (UV)", 8)
                        .getStackForm(1L));
        ItemList.Hatch_Input_Bus_MAX.set(
                new GT_MetaTileEntity_Hatch_InputBus(79, "hatch.input_bus.tier.09", "Input Bus (UHV)", 9)
                        .getStackForm(1L));

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
                new GT_MetaTileEntity_Hatch_Maintenance(90, "hatch.maintenance", "Maintenance Hatch", 1)
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Maintenance.get(1L),
                bitsd,
                new Object[] { "dwx", "hMc", "fsr", 'M', ItemList.Hull_LV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Maintenance.get(1L),
                bitsd,
                new Object[] { "dwx", "hMC", "fsr", 'M', ItemList.Hull_LV, 'C',
                        GT_ModHandler.getModItem("Railcraft", "tool.crowbar", 1L, 0) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Maintenance.get(1L),
                bitsd,
                new Object[] { "dwx", "hMC", "fsr", 'M', ItemList.Hull_LV, 'C',
                        GT_ModHandler.getModItem("Railcraft", "tool.crowbar.reinforced", 1L, 0) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Maintenance.get(1L),
                bitsd,
                new Object[] { "dwx", "hMC", "fsr", 'M', ItemList.Hull_LV, 'C',
                        GT_ModHandler.getModItem("Railcraft", "tool.crowbar.magic", 1L, 0) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Maintenance.get(1L),
                bitsd,
                new Object[] { "dwx", "hMC", "fsr", 'M', ItemList.Hull_LV, 'C',
                        GT_ModHandler.getModItem("Railcraft", "tool.crowbar.void", 1L, 0) });

        ItemList.Hatch_AutoMaintenance.set(
                new GT_MetaTileEntity_Hatch_Maintenance(
                        111,
                        "hatch.maintenance.auto",
                        "Auto Maintenance Hatch",
                        6,
                        true).getStackForm(1L));
        ItemList.Hatch_DataAccess_EV.set(
                new GT_MetaTileEntity_Hatch_DataAccess(145, "hatch.dataaccess", "Data Access Hatch", 4)
                        .getStackForm(1L));
        ItemList.Hatch_DataAccess_LuV.set(
                new GT_MetaTileEntity_Hatch_DataAccess(146, "hatch.dataaccess.adv", "Advanced Data Access Hatch", 6)
                        .getStackForm(1L));
        ItemList.Hatch_DataAccess_UV.set(
                new GT_MetaTileEntity_Hatch_DataAccess(147, "hatch.dataaccess.auto", "Automatable Data Access Hatch", 8)
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_DataAccess_EV.get(1L),
                bitsd,
                new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_EV, 'O', ItemList.Tool_DataStick, 'C',
                        OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_DataAccess_LuV.get(1L),
                bitsd,
                new Object[] { "COC", "OMO", "COC", 'M', ItemList.Hull_LuV, 'O', ItemList.Tool_DataOrb, 'C',
                        OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_DataAccess_UV.get(1L),
                bitsd,
                new Object[] { "CRC", "OMO", "CRC", 'M', ItemList.Hull_UV, 'O', ItemList.Tool_DataOrb, 'C',
                        OrePrefixes.circuit.get(Materials.Infinite), 'R', ItemList.Robot_Arm_UV });

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_AutoMaintenance.get(1L),
                bitsd,
                new Object[] { "CHC", "AMA", "CHC", 'M', ItemList.Hull_LuV, 'H', ItemList.Hatch_Maintenance, 'A',
                        ItemList.Robot_Arm_LuV, 'C', OrePrefixes.circuit.get(Materials.Ultimate) });

        ItemList.Hatch_Muffler_LV.set(
                new GT_MetaTileEntity_Hatch_Muffler(91, "hatch.muffler.tier.01", "Muffler Hatch (LV)", 1)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_MV.set(
                new GT_MetaTileEntity_Hatch_Muffler(92, "hatch.muffler.tier.02", "Muffler Hatch (MV)", 2)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_HV.set(
                new GT_MetaTileEntity_Hatch_Muffler(93, "hatch.muffler.tier.03", "Muffler Hatch (HV)", 3)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_EV.set(
                new GT_MetaTileEntity_Hatch_Muffler(94, "hatch.muffler.tier.04", "Muffler Hatch (EV)", 4)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_IV.set(
                new GT_MetaTileEntity_Hatch_Muffler(95, "hatch.muffler.tier.05", "Muffler Hatch (IV)", 5)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_LuV.set(
                new GT_MetaTileEntity_Hatch_Muffler(96, "hatch.muffler.tier.06", "Muffler Hatch (LuV)", 6)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_ZPM.set(
                new GT_MetaTileEntity_Hatch_Muffler(97, "hatch.muffler.tier.07", "Muffler Hatch (ZPM)", 7)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_UV.set(
                new GT_MetaTileEntity_Hatch_Muffler(98, "hatch.muffler.tier.08", "Muffler Hatch (UV)", 8)
                        .getStackForm(1L));
        ItemList.Hatch_Muffler_MAX.set(
                new GT_MetaTileEntity_Hatch_Muffler(99, "hatch.muffler.tier.09", "Muffler Hatch (UHV)", 9)
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Muffler_LV.get(1L),
                bitsd,
                new Object[] { "MX ", "PR ", 'M', ItemList.Hull_LV, 'P', OrePrefixes.pipeMedium.get(Materials.Bronze),
                        'R', OrePrefixes.rotor.get(Materials.Bronze), 'X', ItemList.Electric_Motor_LV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Hatch_Muffler_MV.get(1L),
                bitsd,
                new Object[] { "MX ", "PR ", 'M', ItemList.Hull_MV, 'P', OrePrefixes.pipeMedium.get(Materials.Steel),
                        'R', OrePrefixes.rotor.get(Materials.Steel), 'X', ItemList.Electric_Motor_MV });

        ItemList.Machine_Bronze_Boiler
                .set(new GT_MetaTileEntity_Boiler_Bronze(100, "boiler.bronze", "Small Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler.set(
                new GT_MetaTileEntity_Boiler_Steel(101, "boiler.steel", "High Pressure Coal Boiler").getStackForm(1L));
        ItemList.Machine_Steel_Boiler_Lava.set(
                new GT_MetaTileEntity_Boiler_Lava(102, "boiler.lava", "High Pressure Lava Boiler").getStackForm(1L));
        ItemList.Machine_Bronze_Boiler_Solar
                .set(new GT_MetaTileEntity_Boiler_Solar(105, "boiler.solar", "Simple Solar Boiler").getStackForm(1L));
        ItemList.Machine_HP_Solar.set(
                new GT_MetaTileEntity_Boiler_Solar_Steel(114, "boiler.steel.solar", "High Pressure Solar Boiler")
                        .getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_Boiler.get(1L),
                bitsd,
                new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                        OrePrefixes.plate.get(Materials.Bronze), 'B', new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Steel_Boiler.get(1L),
                bitsd,
                new Object[] { aTextPlate, "PwP", "BFB", 'F', OreDictNames.craftingIronFurnace, 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'B', new ItemStack(Blocks.brick_block, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Steel_Boiler_Lava.get(1L),
                bitsd,
                new Object[] { aTextPlate, "PTP", aTextPlateMotor, 'M', ItemList.Hull_HP, 'P',
                        OrePrefixes.plate.get(Materials.Steel), 'T',
                        GT_ModHandler.getModItem("BuildCraft|Factory", "tankBlock", 1L, 0) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_Boiler_Solar.get(1L),
                bitsd,
                new Object[] { "GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_Bronze_Bricks, 'P',
                        OrePrefixes.pipeSmall.get(Materials.Bronze), 'S', OrePrefixes.plateDouble.get(Materials.Silver),
                        'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HP_Solar.get(1L),
                bitsd,
                new Object[] { "GGG", "SSS", aTextPlateMotor, 'M', ItemList.Hull_HP_Bricks, 'P',
                        OrePrefixes.pipeSmall.get(Materials.Steel), 'S', OrePrefixes.plateTriple.get(Materials.Silver),
                        'G', GT_ModHandler.getModItem("IC2", "blockAlloyGlass", 1L) });

        ItemList.Machine_Bronze_BlastFurnace.set(
                new GT_MetaTileEntity_BronzeBlastFurnace(
                        108,
                        "bronzemachine.blastfurnace",
                        "Bronze Plated Blast Furnace").getStackForm(1L));
        ItemList.Machine_Bronze_Furnace.set(
                new GT_MetaTileEntity_Furnace_Bronze(103, "bronzemachine.furnace", "Steam Furnace").getStackForm(1L));
        ItemList.Machine_HP_Furnace.set(
                new GT_MetaTileEntity_Furnace_Steel(104, "hpmachine.furnace", "High Pressure Furnace")
                        .getStackForm(1L));
        ItemList.Machine_Bronze_Macerator.set(
                new GT_MetaTileEntity_Macerator_Bronze(106, "bronzemachine.macerator", "Steam Macerator")
                        .getStackForm(1L));
        ItemList.Machine_HP_Macerator.set(
                new GT_MetaTileEntity_Macerator_Steel(107, "hpmachine.macerator", "High Pressure Macerator")
                        .getStackForm(1L));
        ItemList.Machine_Bronze_Extractor.set(
                new GT_MetaTileEntity_Extractor_Bronze(109, "bronzemachine.extractor", "Steam Extractor")
                        .getStackForm(1L));
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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_Furnace.get(1L),
                bitsd,
                new Object[] { "XXX", "XMX", "XFX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                        OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HP_Furnace.get(1L),
                bitsd,
                new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Furnace, 'X',
                        OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_Macerator.get(1L),
                bitsd,
                new Object[] { "DXD", "XMX", "PXP", 'M', ItemList.Hull_Bronze, 'X',
                        OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'D',
                        OrePrefixes.gem.get(Materials.Diamond) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HP_Macerator.get(1L),
                bitsd,
                new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Macerator, 'X',
                        OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_Extractor.get(1L),
                bitsd,
                new Object[] { "XXX", "PMG", "XXX", 'M', ItemList.Hull_Bronze, 'X',
                        OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'G',
                        new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HP_Extractor.get(1L),
                bitsd,
                new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Extractor, 'X',
                        OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_Hammer.get(1L),
                bitsd,
                new Object[] { "XPX", "XMX", "XAX", 'M', ItemList.Hull_Bronze, 'X',
                        OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston, 'A',
                        OreDictNames.craftingAnvil });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HP_Hammer.get(1L),
                bitsd,
                new Object[] { "PSP", "XMX", "PPP", 'M', ItemList.Machine_Bronze_Hammer, 'X',
                        OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_Compressor.get(1L),
                bitsd,
                new Object[] { "XXX", aTextPlateMotor, "XXX", 'M', ItemList.Hull_Bronze, 'X',
                        OrePrefixes.pipeSmall.get(Materials.Bronze), 'P', OreDictNames.craftingPiston });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HP_Compressor.get(1L),
                bitsd,
                new Object[] { "XSX", "PMP", "XXX", 'M', ItemList.Machine_Bronze_Compressor, 'X',
                        OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Bronze_AlloySmelter.get(1L),
                bitsd,
                new Object[] { "XXX", "FMF", "XXX", 'M', ItemList.Hull_Bronze_Bricks, 'X',
                        OrePrefixes.pipeSmall.get(Materials.Bronze), 'F', OreDictNames.craftingFurnace });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HP_AlloySmelter.get(1L),
                bitsd,
                new Object[] { "PSP", "PMP", "PXP", 'M', ItemList.Machine_Bronze_AlloySmelter, 'X',
                        OrePrefixes.pipeSmall.get(Materials.WroughtIron), 'P',
                        OrePrefixes.plate.get(Materials.WroughtIron), 'S', OrePrefixes.plate.get(Materials.Steel) });

        ItemList.Locker_ULV.set(
                new GT_MetaTileEntity_Locker(150, "locker.tier.00", "Ultra Low Voltage Locker", 0).getStackForm(1L));
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
        ItemList.Locker_LuV.set(
                new GT_MetaTileEntity_Locker(156, "locker.tier.06", "Ludicrous Voltage Locker", 6).getStackForm(1L));
        ItemList.Locker_ZPM
                .set(new GT_MetaTileEntity_Locker(157, "locker.tier.07", "ZPM Voltage Locker", 7).getStackForm(1L));
        ItemList.Locker_UV.set(
                new GT_MetaTileEntity_Locker(158, "locker.tier.08", "Ultimate Voltage Locker", 8).getStackForm(1L));
        ItemList.Locker_MAX.set(
                new GT_MetaTileEntity_Locker(159, "locker.tier.09", "Highly Ultimate Voltage Locker", 9)
                        .getStackForm(1L));

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_ULV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                        OrePrefixes.wireGt01.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_LV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                        OrePrefixes.wireGt01.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_MV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                        OrePrefixes.wireGt01.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_HV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                        OrePrefixes.wireGt01.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_EV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                        OrePrefixes.wireGt01.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_IV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                        OrePrefixes.wireGt01.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_LuV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                        OrePrefixes.wireGt01.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_ZPM.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                        OrePrefixes.wireGt01.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_UV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                        OrePrefixes.wireGt01.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_1by1_MAX.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                        OrePrefixes.wireGt01.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_ULV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                        OrePrefixes.wireGt04.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_LV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                        OrePrefixes.wireGt04.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_MV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                        OrePrefixes.wireGt04.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_HV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                        OrePrefixes.wireGt04.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_EV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                        OrePrefixes.wireGt04.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_IV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                        OrePrefixes.wireGt04.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_LuV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                        OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_ZPM.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                        OrePrefixes.wireGt04.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_UV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                        OrePrefixes.wireGt04.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_2by2_MAX.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                        OrePrefixes.wireGt04.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_ULV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                        OrePrefixes.wireGt08.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_LV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                        OrePrefixes.wireGt08.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_MV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                        OrePrefixes.wireGt08.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_HV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                        OrePrefixes.wireGt08.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_EV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                        OrePrefixes.wireGt08.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_IV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                        OrePrefixes.wireGt08.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_LuV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                        OrePrefixes.wireGt08.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_ZPM.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                        OrePrefixes.wireGt08.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_UV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                        OrePrefixes.wireGt08.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_3by3_MAX.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                        OrePrefixes.wireGt08.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_ULV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ULV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_LV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_MV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MV, 'W',
                        OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_HV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_HV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_EV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_EV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_IV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_IV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_LuV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_LuV, 'W',
                        OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_ZPM.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_ZPM, 'W',
                        OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_UV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_UV, 'W',
                        OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Buffer_4by4_MAX.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, 'M', ItemList.Hull_MAX, 'W',
                        OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_ULV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ULV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Lead), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.Battery_RE_ULV_Tantalum, 'C', OrePrefixes.circuit.get(Materials.Primitive) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_LV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Tin), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.Battery_RE_LV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_MV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MV, 'W',
                        OrePrefixes.wireGt16.get(Materials.AnyCopper), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.Battery_RE_MV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_HV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_HV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Gold), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.Battery_RE_HV_Lithium, 'C', OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_EV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_EV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Aluminium), 'T', OreDictNames.craftingChest, 'B',
                        OrePrefixes.battery.get(Materials.Master), 'C', OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_IV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_IV, 'W',
                        OrePrefixes.wireGt16.get(Materials.Tungsten), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.Energy_LapotronicOrb, 'C', OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_LuV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_LuV, 'W',
                        OrePrefixes.wireGt16.get(Materials.VanadiumGallium), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.Energy_LapotronicOrb2, 'C', OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_ZPM.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_ZPM, 'W',
                        OrePrefixes.wireGt16.get(Materials.Naquadah), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.Energy_LapotronicOrb2, 'C', OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_UV.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_UV, 'W',
                        OrePrefixes.wireGt16.get(Materials.NaquadahAlloy), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.ZPM2, 'C', OrePrefixes.circuit.get(Materials.SuperconductorUHV) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Battery_Charger_4by4_MAX.get(1L),
                bitsd,
                new Object[] { aTextWireChest, aTextWireHull, "BCB", 'M', ItemList.Hull_MAX, 'W',
                        OrePrefixes.wireGt16.get(Materials.SuperconductorUHV), 'T', OreDictNames.craftingChest, 'B',
                        ItemList.ZPM2, 'C', OrePrefixes.circuit.get(Materials.Infinite) });

        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_ULV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ULV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_LV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_MV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_MV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_HV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_HV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_EV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_EV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_IV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_IV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_LuV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_LuV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_ZPM.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_ZPM, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_UV.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_UV, 'T', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Locker_MAX.get(1L),
                bitsd,
                new Object[] { "T", "M", 'M', ItemList.Battery_Buffer_2by2_MAX, 'T', OreDictNames.craftingChest });
    }

    private static void run2() {
        ItemList.Machine_LV_AlloySmelter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        201,
                        "basicmachine.alloysmelter.tier.01",
                        "Basic Alloy Smelter",
                        1,
                        "HighTech combination Smelter",
                        GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "AlloySmelter.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ALLOY_SMELTER",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_MV_AlloySmelter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        202,
                        "basicmachine.alloysmelter.tier.02",
                        "Advanced Alloy Smelter",
                        2,
                        "HighTech combination Smelter",
                        GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "AlloySmelter.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ALLOY_SMELTER",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_HV_AlloySmelter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        203,
                        "basicmachine.alloysmelter.tier.03",
                        "Advanced Alloy Smelter II",
                        3,
                        "HighTech combination Smelter",
                        GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "AlloySmelter.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ALLOY_SMELTER",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_EV_AlloySmelter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        204,
                        "basicmachine.alloysmelter.tier.04",
                        "Advanced Alloy Smelter III",
                        4,
                        "HighTech combination Smelter",
                        GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "AlloySmelter.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ALLOY_SMELTER",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_IV_AlloySmelter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        205,
                        "basicmachine.alloysmelter.tier.05",
                        "Advanced Alloy Smelter IV",
                        5,
                        "HighTech combination Smelter",
                        GT_Recipe.GT_Recipe_Map.sAlloySmelterRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "AlloySmelter.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ALLOY_SMELTER",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));

        ItemList.Machine_LV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        211,
                        "basicmachine.assembler.tier.01",
                        "Basic Assembling Machine",
                        1,
                        "Avengers, Assemble!",
                        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "Assembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ASSEMBLER",
                        new Object[] { "ACA", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        212,
                        "basicmachine.assembler.tier.02",
                        "Advanced Assembling Machine",
                        2,
                        "Avengers, Assemble!",
                        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                        9,
                        1,
                        24000,
                        0,
                        1,
                        "Assembler2.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ASSEMBLER",
                        new Object[] { "ACA", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        213,
                        "basicmachine.assembler.tier.03",
                        "Advanced Assembling Machine II",
                        3,
                        "Avengers, Assemble!",
                        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                        9,
                        1,
                        32000,
                        0,
                        1,
                        "Assembler2.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ASSEMBLER",
                        new Object[] { "ACA", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        214,
                        "basicmachine.assembler.tier.04",
                        "Advanced Assembling Machine III",
                        4,
                        "Avengers, Assemble!",
                        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                        9,
                        1,
                        48000,
                        0,
                        1,
                        "Assembler2.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ASSEMBLER",
                        new Object[] { "ACA", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Assembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        215,
                        "basicmachine.assembler.tier.05",
                        "Advanced Assembling Machine IV",
                        5,
                        "Avengers, Assemble!",
                        GT_Recipe.GT_Recipe_Map.sAssemblerRecipes,
                        9,
                        1,
                        64000,
                        0,
                        1,
                        "Assembler2.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ASSEMBLER",
                        new Object[] { "ACA", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Bender.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        221,
                        "basicmachine.bender.tier.01",
                        "Basic Bending Machine",
                        1,
                        "Boo, he's bad! We want BENDER!!!",
                        GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Bender.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "BENDER",
                        new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Bender.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        222,
                        "basicmachine.bender.tier.02",
                        "Advanced Bending Machine",
                        2,
                        "Boo, he's bad! We want BENDER!!!",
                        GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Bender.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "BENDER",
                        new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Bender.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        223,
                        "basicmachine.bender.tier.03",
                        "Advanced Bending Machine II",
                        3,
                        "Boo, he's bad! We want BENDER!!!",
                        GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Bender.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "BENDER",
                        new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Bender.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        224,
                        "basicmachine.bender.tier.04",
                        "Advanced Bending Machine III",
                        4,
                        "Boo, he's bad! We want BENDER!!!",
                        GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Bender.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "BENDER",
                        new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Bender.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        225,
                        "basicmachine.bender.tier.05",
                        "Advanced Bending Machine IV",
                        5,
                        "Boo, he's bad! We want BENDER!!!",
                        GT_Recipe.GT_Recipe_Map.sBenderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Bender.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "BENDER",
                        new Object[] { aTextPlateWrench, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Canner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        231,
                        "basicmachine.canner.tier.01",
                        "Basic Canning Machine",
                        1,
                        "Unmobile Food Canning Machine GTA4",
                        GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                        2,
                        2,
                        0,
                        0,
                        1,
                        "Canner.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CANNER",
                        new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Canner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        232,
                        "basicmachine.canner.tier.02",
                        "Advanced Canning Machine",
                        2,
                        "Unmobile Food Canning Machine GTA4",
                        GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                        2,
                        2,
                        0,
                        0,
                        1,
                        "Canner.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CANNER",
                        new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Canner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        233,
                        "basicmachine.canner.tier.03",
                        "Advanced Canning Machine II",
                        3,
                        "Unmobile Food Canning Machine GTA4",
                        GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                        2,
                        2,
                        0,
                        0,
                        1,
                        "Canner.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CANNER",
                        new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Canner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        234,
                        "basicmachine.canner.tier.04",
                        "Advanced Canning Machine III",
                        4,
                        "Unmobile Food Canning Machine GTA4",
                        GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                        2,
                        2,
                        0,
                        0,
                        1,
                        "Canner.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CANNER",
                        new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Canner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        235,
                        "basicmachine.canner.tier.05",
                        "Advanced Canning Machine IV",
                        5,
                        "Unmobile Food Canning Machine GTA4",
                        GT_Recipe.GT_Recipe_Map.sCannerRecipes,
                        2,
                        2,
                        0,
                        0,
                        1,
                        "Canner.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CANNER",
                        new Object[] { aTextWirePump, aTextCableHull, "GGG", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        241,
                        "basicmachine.compressor.tier.01",
                        "Basic Compressor",
                        1,
                        "Compress-O-Matic C77",
                        GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Compressor.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "COMPRESSOR",
                        new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        242,
                        "basicmachine.compressor.tier.02",
                        "Advanced Compressor",
                        2,
                        "Compress-O-Matic C77",
                        GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Compressor.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "COMPRESSOR",
                        new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        243,
                        "basicmachine.compressor.tier.03",
                        "Advanced Compressor II",
                        3,
                        "Compress-O-Matic C77",
                        GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Compressor.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "COMPRESSOR",
                        new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        244,
                        "basicmachine.compressor.tier.04",
                        "Advanced Compressor III",
                        4,
                        "Compress-O-Matic C77",
                        GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Compressor.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "COMPRESSOR",
                        new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Compressor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        245,
                        "basicmachine.compressor.tier.05",
                        "Singularity Compressor",
                        5,
                        "Compress-O-Matic C77",
                        GT_Recipe.GT_Recipe_Map.sCompressorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Compressor.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "COMPRESSOR",
                        new Object[] { aTextWireCoil, aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        251,
                        "basicmachine.cutter.tier.01",
                        "Basic Cutting Machine",
                        1,
                        "Slice'N Dice",
                        GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                        1,
                        2,
                        8000,
                        0,
                        1,
                        "Cutter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CUTTER",
                        new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B',
                                OreDictNames.craftingDiamondBlade }).getStackForm(1L));
        ItemList.Machine_MV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        252,
                        "basicmachine.cutter.tier.02",
                        "Advanced Cutting Machine",
                        2,
                        "Slice'N Dice",
                        GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                        2,
                        2,
                        16000,
                        0,
                        1,
                        "Cutter2.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CUTTER",
                        new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B',
                                OreDictNames.craftingDiamondBlade }).getStackForm(1L));
        ItemList.Machine_HV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        253,
                        "basicmachine.cutter.tier.03",
                        "Advanced Cutting Machine II",
                        3,
                        "Slice'N Dice",
                        GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                        2,
                        4,
                        32000,
                        0,
                        1,
                        "Cutter4.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CUTTER",
                        new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B',
                                OreDictNames.craftingDiamondBlade }).getStackForm(1L));
        ItemList.Machine_EV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        254,
                        "basicmachine.cutter.tier.04",
                        "Advanced Cutting Machine III",
                        4,
                        "Slice'N Dice",
                        GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                        2,
                        4,
                        64000,
                        0,
                        1,
                        "Cutter4.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CUTTER",
                        new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B',
                                OreDictNames.craftingDiamondBlade }).getStackForm(1L));
        ItemList.Machine_IV_Cutter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        255,
                        "basicmachine.cutter.tier.05",
                        "Advanced Cutting Machine IV",
                        5,
                        "Slice'N Dice",
                        GT_Recipe.GT_Recipe_Map.sCutterRecipes,
                        2,
                        4,
                        96000,
                        0,
                        1,
                        "Cutter4.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CUTTER",
                        new Object[] { "WCG", "VMB", "CWE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B',
                                OreDictNames.craftingDiamondBlade }).getStackForm(1L));

        ItemList.Machine_LV_E_Furnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        261,
                        "basicmachine.e_furnace.tier.01",
                        "Basic Electric Furnace",
                        1,
                        "Not like using a Commodore 64",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_FURNACE",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Furnace").getStackForm(1L));
        ItemList.Machine_MV_E_Furnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        262,
                        "basicmachine.e_furnace.tier.02",
                        "Advanced Electric Furnace",
                        2,
                        "Not like using a Commodore 64",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_FURNACE",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Furnace").getStackForm(1L));
        ItemList.Machine_HV_E_Furnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        263,
                        "basicmachine.e_furnace.tier.03",
                        "Advanced Electric Furnace II",
                        3,
                        "Not like using a Commodore 64",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_FURNACE",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Furnace").getStackForm(1L));
        ItemList.Machine_EV_E_Furnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        264,
                        "basicmachine.e_furnace.tier.04",
                        "Advanced Electric Furnace III",
                        4,
                        "Not like using a Commodore 64",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_FURNACE",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Furnace").getStackForm(1L));
        ItemList.Machine_IV_E_Furnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        265,
                        "basicmachine.e_furnace.tier.05",
                        "Electron Exitement Processor",
                        5,
                        "Not like using a Commodore 64",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_FURNACE",
                        new Object[] { "ECE", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Furnace").getStackForm(1L));

        ItemList.Machine_LV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        271,
                        "basicmachine.extractor.tier.01",
                        "Basic Extractor",
                        1,
                        "Dejuicer-Device of Doom - D123",
                        GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Extractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRACTOR",
                        new Object[] { "GCG", "EMP", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        272,
                        "basicmachine.extractor.tier.02",
                        "Advanced Extractor",
                        2,
                        "Dejuicer-Device of Doom - D123",
                        GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Extractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRACTOR",
                        new Object[] { "GCG", "EMP", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        273,
                        "basicmachine.extractor.tier.03",
                        "Advanced Extractor II",
                        3,
                        "Dejuicer-Device of Doom - D123",
                        GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Extractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRACTOR",
                        new Object[] { "GCG", "EMP", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        274,
                        "basicmachine.extractor.tier.04",
                        "Advanced Extractor III",
                        4,
                        "Dejuicer-Device of Doom - D123",
                        GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Extractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRACTOR",
                        new Object[] { "GCG", "EMP", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Extractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        275,
                        "basicmachine.extractor.tier.05",
                        "Vacuum Extractor",
                        5,
                        "Dejuicer-Device of Doom - D123",
                        GT_Recipe.GT_Recipe_Map.sExtractorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Extractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRACTOR",
                        new Object[] { "GCG", "EMP", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Extruder.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        281,
                        "basicmachine.extruder.tier.01",
                        "Basic Extruder",
                        1,
                        "Universal Machine for Metal Working",
                        GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Extruder.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRUDER",
                        new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_MV_Extruder.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        282,
                        "basicmachine.extruder.tier.02",
                        "Advanced Extruder",
                        2,
                        "Universal Machine for Metal Working",
                        GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Extruder.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRUDER",
                        new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_HV_Extruder.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        283,
                        "basicmachine.extruder.tier.03",
                        "Advanced Extruder II",
                        3,
                        "Universal Machine for Metal Working",
                        GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Extruder.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRUDER",
                        new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_EV_Extruder.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        284,
                        "basicmachine.extruder.tier.04",
                        "Advanced Extruder III",
                        4,
                        "Universal Machine for Metal Working",
                        GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Extruder.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRUDER",
                        new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_IV_Extruder.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        285,
                        "basicmachine.extruder.tier.05",
                        "Advanced Extruder IV",
                        5,
                        "Universal Machine for Metal Working",
                        GT_Recipe.GT_Recipe_Map.sExtruderRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Extruder.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "EXTRUDER",
                        new Object[] { "CCE", "XMP", "CCE", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'X',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));

        ItemList.Machine_LV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        291,
                        "basicmachine.lathe.tier.01",
                        "Basic Lathe",
                        1,
                        "Produces Rods more efficiently",
                        GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Lathe.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LATHE",
                        new Object[] { aTextWireCoil, "EMD", "CWP", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D',
                                OrePrefixes.gem.get(Materials.Diamond) }).getStackForm(1L));
        ItemList.Machine_MV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        292,
                        "basicmachine.lathe.tier.02",
                        "Advanced Lathe",
                        2,
                        "Produces Rods more efficiently",
                        GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Lathe.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LATHE",
                        new Object[] { aTextWireCoil, "EMD", "CWP", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D',
                                OrePrefixes.gemFlawless.get(Materials.Diamond) }).getStackForm(1L));
        ItemList.Machine_HV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        293,
                        "basicmachine.lathe.tier.03",
                        "Advanced Lathe II",
                        3,
                        "Produces Rods more efficiently",
                        GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Lathe.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LATHE",
                        new Object[] { aTextWireCoil, "EMD", "CWP", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D',
                                OreDictNames.craftingIndustrialDiamond }).getStackForm(1L));
        ItemList.Machine_EV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        294,
                        "basicmachine.lathe.tier.04",
                        "Advanced Lathe III",
                        4,
                        "Produces Rods more efficiently",
                        GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Lathe.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LATHE",
                        new Object[] { aTextWireCoil, "EMD", "CWP", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D',
                                OreDictNames.craftingIndustrialDiamond }).getStackForm(1L));
        ItemList.Machine_IV_Lathe.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        295,
                        "basicmachine.lathe.tier.05",
                        "Advanced Lathe IV",
                        5,
                        "Produces Rods more efficiently",
                        GT_Recipe.GT_Recipe_Map.sLatheRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Lathe.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LATHE",
                        new Object[] { aTextWireCoil, "EMD", "CWP", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'D',
                                OreDictNames.craftingIndustrialDiamond }).getStackForm(1L));

        ItemList.Machine_LV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        301,
                        "basicmachine.macerator.tier.01",
                        "Basic Macerator",
                        1,
                        "Schreddering your Ores",
                        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Macerator1.png",
                        SoundResource.IC2_MACHINES_MACERATOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "MACERATOR",
                        new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.gem.get(Materials.Diamond) }).getStackForm(1L));
        ItemList.Machine_MV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        302,
                        "basicmachine.macerator.tier.02",
                        "Advanced Macerator",
                        2,
                        "Schreddering your Ores",
                        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Macerator1.png",
                        SoundResource.IC2_MACHINES_MACERATOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "MACERATOR",
                        new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.gemFlawless.get(Materials.Diamond) }).getStackForm(1L));
        ItemList.Machine_HV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        303,
                        "basicmachine.macerator.tier.03",
                        "Universal Macerator",
                        3,
                        "Schreddering your Ores with Byproducts",
                        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Macerator2.png",
                        SoundResource.IC2_MACHINES_MACERATOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PULVERIZER",
                        new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder })
                                        .getStackForm(1L));
        ItemList.Machine_EV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        304,
                        "basicmachine.macerator.tier.04",
                        "Universal Pulverizer",
                        4,
                        "Schreddering your Ores with Byproducts",
                        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "Macerator3.png",
                        SoundResource.IC2_MACHINES_MACERATOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PULVERIZER",
                        new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder })
                                        .getStackForm(1L));
        ItemList.Machine_IV_Macerator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        305,
                        "basicmachine.macerator.tier.05",
                        "Blend-O-Matic 9001",
                        5,
                        "Schreddering your Ores with Byproducts",
                        GT_Recipe.GT_Recipe_Map.sMaceratorRecipes,
                        1,
                        4,
                        0,
                        0,
                        1,
                        "Macerator4.png",
                        SoundResource.IC2_MACHINES_MACERATOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PULVERIZER",
                        new Object[] { "PEG", "WWM", "CCW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G', OreDictNames.craftingGrinder })
                                        .getStackForm(1L));

        ItemList.Machine_LV_Microwave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        311,
                        "basicmachine.microwave.tier.01",
                        "Basic Microwave",
                        1,
                        "Did you really read the instruction Manual?",
                        GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MICROWAVE",
                        new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L',
                                OrePrefixes.plate.get(Materials.Lead) }).getStackForm(1L));
        ItemList.Machine_MV_Microwave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        312,
                        "basicmachine.microwave.tier.02",
                        "Advanced Microwave",
                        2,
                        "Did you really read the instruction Manual?",
                        GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MICROWAVE",
                        new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L',
                                OrePrefixes.plate.get(Materials.Lead) }).getStackForm(1L));
        ItemList.Machine_HV_Microwave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        313,
                        "basicmachine.microwave.tier.03",
                        "Advanced Microwave II",
                        3,
                        "Did you really read the instruction Manual?",
                        GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MICROWAVE",
                        new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L',
                                OrePrefixes.plate.get(Materials.Lead) }).getStackForm(1L));
        ItemList.Machine_EV_Microwave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        314,
                        "basicmachine.microwave.tier.04",
                        "Advanced Microwave III",
                        4,
                        "Did you really read the instruction Manual?",
                        GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MICROWAVE",
                        new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L',
                                OrePrefixes.plate.get(Materials.Lead) }).getStackForm(1L));
        ItemList.Machine_IV_Microwave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        315,
                        "basicmachine.microwave.tier.05",
                        "Advanced Microwave IV",
                        5,
                        "Did you really read the instruction Manual?",
                        GT_Recipe.GT_Recipe_Map.sMicrowaveRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Furnace.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MICROWAVE",
                        new Object[] { "LWC", "LMR", "LEC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'L',
                                OrePrefixes.plate.get(Materials.Lead) }).getStackForm(1L));

        ItemList.Machine_LV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        321,
                        "basicmachine.printer.tier.01",
                        "Basic Printer",
                        1,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        322,
                        "basicmachine.printer.tier.02",
                        "Advanced Printer",
                        2,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        323,
                        "basicmachine.printer.tier.03",
                        "Advanced Printer II",
                        3,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        324,
                        "basicmachine.printer.tier.04",
                        "Advanced Printer III",
                        4,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        325,
                        "basicmachine.printer.tier.05",
                        "Advanced Printer IV",
                        5,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_LuV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        326,
                        "basicmachine.printer.tier.06",
                        "Advanced Printer V",
                        6,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_ZPM_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        327,
                        "basicmachine.printer.tier.07",
                        "Advanced Printer VI",
                        7,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_UV_Printer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        328,
                        "basicmachine.printer.tier.08",
                        "Advanced Printer VII",
                        8,
                        "It can copy Books and paint Stuff",
                        GT_Recipe.GT_Recipe_Map.sPrinterRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Printer.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.TOP_SMOKE,
                        "PRINTER",
                        new Object[] { aTextMotorWire, aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Recycler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        331,
                        "basicmachine.recycler.tier.01",
                        "Basic Recycler",
                        1,
                        "Compress, burn, obliterate and filter EVERYTHING",
                        GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Recycler.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "RECYCLER",
                        new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.dust.get(Materials.Glowstone) }).getStackForm(1L));
        ItemList.Machine_MV_Recycler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        332,
                        "basicmachine.recycler.tier.02",
                        "Advanced Recycler",
                        2,
                        "Compress, burn, obliterate and filter EVERYTHING",
                        GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Recycler.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "RECYCLER",
                        new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.dust.get(Materials.Glowstone) }).getStackForm(1L));
        ItemList.Machine_HV_Recycler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        333,
                        "basicmachine.recycler.tier.03",
                        "Advanced Recycler II",
                        3,
                        "Compress, burn, obliterate and filter EVERYTHING",
                        GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Recycler.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "RECYCLER",
                        new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.dust.get(Materials.Glowstone) }).getStackForm(1L));
        ItemList.Machine_EV_Recycler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        334,
                        "basicmachine.recycler.tier.04",
                        "Advanced Recycler III",
                        4,
                        "Compress, burn, obliterate and filter EVERYTHING",
                        GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Recycler.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "RECYCLER",
                        new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.dust.get(Materials.Glowstone) }).getStackForm(1L));
        ItemList.Machine_IV_Recycler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        335,
                        "basicmachine.recycler.tier.05",
                        "The Oblitterator",
                        5,
                        "Compress, burn, obliterate and filter EVERYTHING",
                        GT_Recipe.GT_Recipe_Map.sRecyclerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Recycler.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "RECYCLER",
                        new Object[] { "GCG", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.dust.get(Materials.Glowstone) }).getStackForm(1L));

        ItemList.Machine_LV_Scanner.set(
                new GT_MetaTileEntity_Scanner(341, "basicmachine.scanner.tier.01", "Basic Scanner", 1)
                        .getStackForm(1L));
        ItemList.Machine_MV_Scanner.set(
                new GT_MetaTileEntity_Scanner(342, "basicmachine.scanner.tier.02", "Advanced Scanner", 2)
                        .getStackForm(1L));
        ItemList.Machine_HV_Scanner.set(
                new GT_MetaTileEntity_Scanner(343, "basicmachine.scanner.tier.03", "Advanced Scanner II", 3)
                        .getStackForm(1L));
        ItemList.Machine_EV_Scanner.set(
                new GT_MetaTileEntity_Scanner(344, "basicmachine.scanner.tier.04", "Advanced Scanner III", 4)
                        .getStackForm(1L));
        ItemList.Machine_IV_Scanner.set(
                new GT_MetaTileEntity_Scanner(345, "basicmachine.scanner.tier.05", "Advanced Scanner IV", 5)
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_Scanner.get(1L),
                bitsd,
                new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_LV, 'T', ItemList.Emitter_LV, 'R',
                        ItemList.Sensor_LV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_Scanner.get(1L),
                bitsd,
                new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_MV, 'T', ItemList.Emitter_MV, 'R',
                        ItemList.Sensor_MV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_Scanner.get(1L),
                bitsd,
                new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_HV, 'T', ItemList.Emitter_HV, 'R',
                        ItemList.Sensor_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                        OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_Scanner.get(1L),
                bitsd,
                new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_EV, 'T', ItemList.Emitter_EV, 'R',
                        ItemList.Sensor_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_Scanner.get(1L),
                bitsd,
                new Object[] { "CTC", aTextWireHull, "CRC", 'M', ItemList.Hull_IV, 'T', ItemList.Emitter_IV, 'R',
                        ItemList.Sensor_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tungsten) });

        ItemList.Machine_LV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        351,
                        "basicmachine.wiremill.tier.01",
                        "Basic Wiremill",
                        1,
                        "Produces Wires more efficiently",
                        GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Wiremill.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "WIREMILL",
                        new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        352,
                        "basicmachine.wiremill.tier.02",
                        "Advanced Wiremill",
                        2,
                        "Produces Wires more efficiently",
                        GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Wiremill.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "WIREMILL",
                        new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        353,
                        "basicmachine.wiremill.tier.03",
                        "Advanced Wiremill II",
                        3,
                        "Produces Wires more efficiently",
                        GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Wiremill.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "WIREMILL",
                        new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        354,
                        "basicmachine.wiremill.tier.04",
                        "Advanced Wiremill III",
                        4,
                        "Produces Wires more efficiently",
                        GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Wiremill.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "WIREMILL",
                        new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Wiremill.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        355,
                        "basicmachine.wiremill.tier.05",
                        "Advanced Wiremill IV",
                        5,
                        "Produces Wires more efficiently",
                        GT_Recipe.GT_Recipe_Map.sWiremillRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Wiremill.png",
                        SoundResource.IC2_MACHINES_RECYCLER_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "WIREMILL",
                        new Object[] { aTextMotorWire, aTextCableHull, aTextMotorWire, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        361,
                        "basicmachine.centrifuge.tier.01",
                        "Basic Centrifuge",
                        1,
                        "Separating Molecules",
                        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Centrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CENTRIFUGE",
                        new Object[] { "CEC", aTextWireHull, "CEC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        362,
                        "basicmachine.centrifuge.tier.02",
                        "Advanced Centrifuge",
                        2,
                        "Separating Molecules",
                        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Centrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CENTRIFUGE",
                        new Object[] { "CEC", aTextWireHull, "CEC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        363,
                        "basicmachine.centrifuge.tier.03",
                        "Turbo Centrifuge",
                        3,
                        "Separating Molecules",
                        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Centrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CENTRIFUGE",
                        new Object[] { "CEC", aTextWireHull, "CEC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        364,
                        "basicmachine.centrifuge.tier.04",
                        "Molecular Separator",
                        4,
                        "Separating Molecules",
                        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Centrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CENTRIFUGE",
                        new Object[] { "CEC", aTextWireHull, "CEC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Centrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        365,
                        "basicmachine.centrifuge.tier.05",
                        "Molecular Cyclone",
                        5,
                        "Separating Molecules",
                        GT_Recipe.GT_Recipe_Map.sCentrifugeRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Centrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CENTRIFUGE",
                        new Object[] { "CEC", aTextWireHull, "CEC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        371,
                        "basicmachine.electrolyzer.tier.01",
                        "Basic Electrolyzer",
                        1,
                        "Electrolyzing Molecules",
                        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Electrolyzer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROLYZER",
                        new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                OrePrefixes.wireGt01.get(Materials.Gold), 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        372,
                        "basicmachine.electrolyzer.tier.02",
                        "Advanced Electrolyzer",
                        2,
                        "Electrolyzing Molecules",
                        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Electrolyzer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROLYZER",
                        new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                OrePrefixes.wireGt01.get(Materials.Silver), 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        373,
                        "basicmachine.electrolyzer.tier.03",
                        "Advanced Electrolyzer II",
                        3,
                        "Electrolyzing Molecules",
                        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Electrolyzer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROLYZER",
                        new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                OrePrefixes.wireGt01.get(Materials.Electrum), 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        374,
                        "basicmachine.electrolyzer.tier.04",
                        "Advanced Electrolyzer III",
                        4,
                        "Electrolyzing Molecules",
                        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Electrolyzer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROLYZER",
                        new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                OrePrefixes.wireGt01.get(Materials.Platinum), 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Electrolyzer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        375,
                        "basicmachine.electrolyzer.tier.05",
                        "Molecular Disintegrator E-4908",
                        5,
                        "Electrolyzing Molecules",
                        GT_Recipe.GT_Recipe_Map.sElectrolyzerRecipes,
                        2,
                        6,
                        64000,
                        0,
                        1,
                        "Electrolyzer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROLYZER",
                        new Object[] { "IGI", "IMI", "CWC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                OrePrefixes.wireGt01.get(Materials.HSSG), 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        381,
                        "basicmachine.thermalcentrifuge.tier.01",
                        "Basic Thermal Centrifuge",
                        1,
                        "Separating Ores more precisely",
                        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ThermalCentrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "THERMAL_CENTRIFUGE",
                        new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_MV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        382,
                        "basicmachine.thermalcentrifuge.tier.02",
                        "Advanced Thermal Centrifuge",
                        2,
                        "Separating Ores more precisely",
                        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ThermalCentrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "THERMAL_CENTRIFUGE",
                        new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_HV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        383,
                        "basicmachine.thermalcentrifuge.tier.03",
                        "Advanced Thermal Centrifuge II",
                        3,
                        "Separating Ores more precisely",
                        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ThermalCentrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "THERMAL_CENTRIFUGE",
                        new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_EV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        384,
                        "basicmachine.thermalcentrifuge.tier.04",
                        "Advanced Thermal Centrifuge III",
                        4,
                        "Separating Ores more precisely",
                        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ThermalCentrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "THERMAL_CENTRIFUGE",
                        new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));
        ItemList.Machine_IV_ThermalCentrifuge.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        385,
                        "basicmachine.thermalcentrifuge.tier.05",
                        "Blaze Sweatshop T-6350",
                        5,
                        "Separating Ores more precisely",
                        GT_Recipe.GT_Recipe_Map.sThermalCentrifugeRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ThermalCentrifuge.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "THERMAL_CENTRIFUGE",
                        new Object[] { "CEC", "OMO", "WEW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE }).getStackForm(1L));

        ItemList.Machine_LV_OreWasher.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        391,
                        "basicmachine.orewasher.tier.01",
                        "Basic Ore Washing Plant",
                        1,
                        "Getting more Byproducts from your Ores",
                        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                        1,
                        3,
                        16000,
                        0,
                        1,
                        "OreWasher.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ORE_WASHER",
                        new Object[] { "RGR", "CEC", aTextWireHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_MV_OreWasher.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        392,
                        "basicmachine.orewasher.tier.02",
                        "Advanced Ore Washing Plant",
                        2,
                        "Getting more Byproducts from your Ores",
                        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                        1,
                        3,
                        24000,
                        0,
                        1,
                        "OreWasher.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ORE_WASHER",
                        new Object[] { "RGR", "CEC", aTextWireHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_HV_OreWasher.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        393,
                        "basicmachine.orewasher.tier.03",
                        "Advanced Ore Washing Plant II",
                        3,
                        "Getting more Byproducts from your Ores",
                        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                        1,
                        3,
                        32000,
                        0,
                        1,
                        "OreWasher.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ORE_WASHER",
                        new Object[] { "RGR", "CEC", aTextWireHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_EV_OreWasher.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        394,
                        "basicmachine.orewasher.tier.04",
                        "Advanced Ore Washing Plant III",
                        4,
                        "Getting more Byproducts from your Ores",
                        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                        1,
                        3,
                        40000,
                        0,
                        1,
                        "OreWasher.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ORE_WASHER",
                        new Object[] { "RGR", "CEC", aTextWireHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));
        ItemList.Machine_IV_OreWasher.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        395,
                        "basicmachine.orewasher.tier.05",
                        "Repurposed Laundry-Washer I-360",
                        5,
                        "Getting more Byproducts from your Ores",
                        GT_Recipe.GT_Recipe_Map.sOreWasherRecipes,
                        1,
                        3,
                        48000,
                        0,
                        1,
                        "OreWasher.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ORE_WASHER",
                        new Object[] { "RGR", "CEC", aTextWireHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP }).getStackForm(1L));

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
                new GT_MetaTileEntity_Boxinator(405, "basicmachine.boxinator.tier.05", "Boxinator", 5)
                        .getStackForm(1L));
        ItemList.Machine_LuV_Boxinator.set(
                new GT_MetaTileEntity_Boxinator(406, "basicmachine.boxinator.tier.06", "Boxinator", 6)
                        .getStackForm(1L));
        ItemList.Machine_ZPM_Boxinator.set(
                new GT_MetaTileEntity_Boxinator(407, "basicmachine.boxinator.tier.07", "Boxinator", 7)
                        .getStackForm(1L));
        ItemList.Machine_UV_Boxinator.set(
                new GT_MetaTileEntity_Boxinator(408, "basicmachine.boxinator.tier.08", "Boxinator", 8)
                        .getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LV, 'R', ItemList.Robot_Arm_LV, 'V',
                        ItemList.Conveyor_Module_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tin), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_MV, 'R', ItemList.Robot_Arm_MV, 'V',
                        ItemList.Conveyor_Module_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_HV, 'R', ItemList.Robot_Arm_HV, 'V',
                        ItemList.Conveyor_Module_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt01.get(Materials.Gold), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_EV, 'R', ItemList.Robot_Arm_EV, 'V',
                        ItemList.Conveyor_Module_EV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                        OrePrefixes.cableGt01.get(Materials.Aluminium), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_IV, 'R', ItemList.Robot_Arm_IV, 'V',
                        ItemList.Conveyor_Module_IV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tungsten), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LuV_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_LuV, 'R', ItemList.Robot_Arm_LuV, 'V',
                        ItemList.Conveyor_Module_LuV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                        OrePrefixes.cableGt01.get(Materials.VanadiumGallium), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_ZPM_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'R', ItemList.Robot_Arm_ZPM, 'V',
                        ItemList.Conveyor_Module_ZPM, 'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W',
                        OrePrefixes.cableGt01.get(Materials.Naquadah), 'B', OreDictNames.craftingChest });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_UV_Boxinator.get(1L),
                bitsd,
                new Object[] { "BCB", "RMV", aTextWireCoil, 'M', ItemList.Hull_UV, 'R', ItemList.Robot_Arm_UV, 'V',
                        ItemList.Conveyor_Module_UV, 'C', OrePrefixes.circuit.get(Materials.SuperconductorUHV), 'W',
                        OrePrefixes.cableGt01.get(Materials.NaquadahAlloy), 'B', OreDictNames.craftingChest });

        ItemList.Machine_LV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        411,
                        "basicmachine.unboxinator.tier.01",
                        "Basic Unpackager",
                        1,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_MV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        412,
                        "basicmachine.unboxinator.tier.02",
                        "Advanced Unpackager",
                        2,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_HV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        413,
                        "basicmachine.unboxinator.tier.03",
                        "Advanced Unpackager II",
                        3,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_EV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        414,
                        "basicmachine.unboxinator.tier.04",
                        "Advanced Unpackager III",
                        4,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_IV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        415,
                        "basicmachine.unboxinator.tier.05",
                        "Unboxinator",
                        5,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_LuV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        416,
                        "basicmachine.unboxinator.tier.06",
                        "Unboxinator",
                        6,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_ZPM_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        417,
                        "basicmachine.unboxinator.tier.07",
                        "Unboxinator",
                        7,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_UV_Unboxinator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        418,
                        "basicmachine.unboxinator.tier.08",
                        "Unboxinator",
                        8,
                        "Grabs things out of Boxes",
                        GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes,
                        1,
                        2,
                        0,
                        0,
                        1,
                        "Unpackager.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "UNBOXINATOR",
                        new Object[] { "BCB", "VMR", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));

        ItemList.Machine_LV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        421,
                        "basicmachine.chemicalreactor.tier.01",
                        "Basic Chemical Reactor",
                        1,
                        "Letting Chemicals react with each other",
                        GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                        2,
                        2,
                        16000,
                        0,
                        1,
                        "ChemicalReactor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CHEMICAL_REACTOR",
                        new Object[] { "GRG", "WEW", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        422,
                        "basicmachine.chemicalreactor.tier.02",
                        "Advanced Chemical Reactor",
                        2,
                        "Letting Chemicals react with each other",
                        GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                        2,
                        2,
                        16000,
                        0,
                        1,
                        "ChemicalReactor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CHEMICAL_REACTOR",
                        new Object[] { "GRG", "WEW", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        423,
                        "basicmachine.chemicalreactor.tier.03",
                        "Advanced Chemical Reactor II",
                        3,
                        "Letting Chemicals react with each other",
                        GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                        2,
                        2,
                        16000,
                        0,
                        1,
                        "ChemicalReactor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CHEMICAL_REACTOR",
                        new Object[] { "GRG", "WEW", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.pipeMedium.get(Materials.Plastic) }).getStackForm(1L));
        ItemList.Machine_EV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        424,
                        "basicmachine.chemicalreactor.tier.04",
                        "Advanced Chemical Reactor III",
                        4,
                        "Letting Chemicals react with each other",
                        GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                        2,
                        2,
                        16000,
                        0,
                        1,
                        "ChemicalReactor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CHEMICAL_REACTOR",
                        new Object[] { "GRG", "WEW", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.pipeLarge.get(Materials.Plastic) }).getStackForm(1L));
        ItemList.Machine_IV_ChemicalReactor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        425,
                        "basicmachine.chemicalreactor.tier.05",
                        "Advanced Chemical Reactor IV",
                        5,
                        "Letting Chemicals react with each other",
                        GT_Recipe.GT_Recipe_Map.sChemicalRecipes,
                        2,
                        2,
                        16000,
                        0,
                        1,
                        "ChemicalReactor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CHEMICAL_REACTOR",
                        new Object[] { "GRG", "WEW", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                OrePrefixes.pipeHuge.get(Materials.Plastic) }).getStackForm(1L));

        ItemList.Machine_LV_FluidCanner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        431,
                        "basicmachine.fluidcanner.tier.01",
                        "Basic Fluid Canner",
                        1,
                        "Puts Fluids into and out of Containers",
                        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidCanner.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_CANNER",
                        new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_FluidCanner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        432,
                        "basicmachine.fluidcanner.tier.02",
                        "Advanced Fluid Canner",
                        2,
                        "Puts Fluids into and out of Containers",
                        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                        1,
                        1,
                        32000,
                        0,
                        1,
                        "FluidCanner.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_CANNER",
                        new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_FluidCanner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        433,
                        "basicmachine.fluidcanner.tier.03",
                        "Quick Fluid Canner",
                        3,
                        "Puts Fluids into and out of Containers",
                        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                        1,
                        1,
                        48000,
                        0,
                        1,
                        "FluidCanner.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_CANNER",
                        new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_FluidCanner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        434,
                        "basicmachine.fluidcanner.tier.04",
                        "Turbo Fluid Canner",
                        4,
                        "Puts Fluids into and out of Containers",
                        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                        1,
                        1,
                        64000,
                        0,
                        1,
                        "FluidCanner.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_CANNER",
                        new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_FluidCanner.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        435,
                        "basicmachine.fluidcanner.tier.05",
                        "Instant Fluid Canner",
                        5,
                        "Puts Fluids into and out of Containers",
                        GT_Recipe.GT_Recipe_Map.sFluidCannerRecipes,
                        1,
                        1,
                        80000,
                        0,
                        1,
                        "FluidCanner.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_CANNER",
                        new Object[] { "GCG", "GMG", "WPW", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_RockBreaker.set(
                new GT_MetaTileEntity_RockBreaker(441, "basicmachine.rockbreaker.tier.01", "Basic Rock Breaker", 1)
                        .getStackForm(1L));
        ItemList.Machine_MV_RockBreaker.set(
                new GT_MetaTileEntity_RockBreaker(442, "basicmachine.rockbreaker.tier.02", "Advanced Rock Breaker", 2)
                        .getStackForm(1L));
        ItemList.Machine_HV_RockBreaker.set(
                new GT_MetaTileEntity_RockBreaker(
                        443,
                        "basicmachine.rockbreaker.tier.03",
                        "Advanced Rock Breaker II",
                        3).getStackForm(1L));
        ItemList.Machine_EV_RockBreaker.set(
                new GT_MetaTileEntity_RockBreaker(
                        444,
                        "basicmachine.rockbreaker.tier.04",
                        "Advanced Rock Breaker III",
                        4).getStackForm(1L));
        ItemList.Machine_IV_RockBreaker.set(
                new GT_MetaTileEntity_RockBreaker(
                        445,
                        "basicmachine.rockbreaker.tier.05",
                        "Cryogenic Magma Solidifier R-8200",
                        5).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_RockBreaker.get(1L),
                bitsd,
                new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_LV, 'D', OreDictNames.craftingGrinder,
                        'E', ItemList.Electric_Motor_LV, 'P', ItemList.Electric_Piston_LV, 'C',
                        OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'G',
                        new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_RockBreaker.get(1L),
                bitsd,
                new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_MV, 'D', OreDictNames.craftingGrinder,
                        'E', ItemList.Electric_Motor_MV, 'P', ItemList.Electric_Piston_MV, 'C',
                        OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper),
                        'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_RockBreaker.get(1L),
                bitsd,
                new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_HV, 'D', OreDictNames.craftingGrinder,
                        'E', ItemList.Electric_Motor_HV, 'P', ItemList.Electric_Piston_HV, 'C',
                        OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold),
                        'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_RockBreaker.get(1L),
                bitsd,
                new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_EV, 'D', OreDictNames.craftingGrinder,
                        'E', ItemList.Electric_Motor_EV, 'P', ItemList.Electric_Piston_EV, 'C',
                        OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium),
                        'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_RockBreaker.get(1L),
                bitsd,
                new Object[] { "PED", aTextWireHull, "GGG", 'M', ItemList.Hull_IV, 'D', OreDictNames.craftingGrinder,
                        'E', ItemList.Electric_Motor_IV, 'P', ItemList.Electric_Piston_IV, 'C',
                        OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten),
                        'G', new ItemStack(Blocks.glass, 1) });

        ItemList.Machine_LV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(451, "basicmachine.disassembler.tier.01", "Basic Disassembler", 1)
                        .getStackForm(1L));
        ItemList.Machine_MV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(452, "basicmachine.disassembler.tier.02", "Advanced Disassembler", 2)
                        .getStackForm(1L));
        ItemList.Machine_HV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(
                        453,
                        "basicmachine.disassembler.tier.03",
                        "Advanced Disassembler II",
                        3).getStackForm(1L));
        ItemList.Machine_EV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(
                        454,
                        "basicmachine.disassembler.tier.04",
                        "Advanced Disassembler III",
                        4).getStackForm(1L));
        ItemList.Machine_IV_Disassembler.set(
                new GT_MetaTileEntity_Disassembler(
                        455,
                        "basicmachine.disassembler.tier.05",
                        "Advanced Disassembler IV",
                        5).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_Disassembler.get(1L),
                bitsd,
                new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_LV, 'A', ItemList.Robot_Arm_LV, 'C',
                        OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_Disassembler.get(1L),
                bitsd,
                new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_MV, 'A', ItemList.Robot_Arm_MV, 'C',
                        OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_Disassembler.get(1L),
                bitsd,
                new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_HV, 'A', ItemList.Robot_Arm_HV, 'C',
                        OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_Disassembler.get(1L),
                bitsd,
                new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_EV, 'A', ItemList.Robot_Arm_EV, 'C',
                        OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_Disassembler.get(1L),
                bitsd,
                new Object[] { "ACA", aTextWireHull, "ACA", 'M', ItemList.Hull_IV, 'A', ItemList.Robot_Arm_IV, 'C',
                        OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten) });

        if (Forestry.isModLoaded()) {
            ItemList.Machine_IndustrialApiary.set(
                    new GT_MetaTileEntity_IndustrialApiary(
                            9399,
                            "basicmachine.industrialapiary",
                            "Industrial Apiary",
                            8).getStackForm(1L));

            /* Conversion recipes */
            // TODO: Move those recipes with the other recipes
            if (Gendustry.isModLoaded()) {
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.Machine_IndustrialApiary.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "IndustrialApiary", 1, 0) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_Frame.get(1),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "UpgradeFrame", 1) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_PRODUCTION.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 0) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_PLAINS.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 17) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_LIGHT.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 11) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_FLOWERING.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 2) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_WINTER.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 20) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_DRYER.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 5) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_AUTOMATION.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 14) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_HUMIDIFIER.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 4) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_HELL.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 13) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_POLLEN.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 22) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_DESERT.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 16) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_COOLER.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 7) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_LIFESPAN.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 1) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_SEAL.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 10) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_STABILIZER.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 19) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_JUNGLE.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 18) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_TERRITORY.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 3) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_OCEAN.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 21) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_SKY.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 12) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_HEATER.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 6) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        ItemList.IndustrialApiary_Upgrade_SIEVE.get(1L),
                        new Object[] { GT_ModHandler.getModItem(Gendustry.modID, "ApiaryUpgrade", 1, 15) });
            }
        }

        ItemList.Machine_LV_Massfab.set(
                new GT_MetaTileEntity_Massfabricator(461, "basicmachine.massfab.tier.01", "Basic Mass Fabricator", 1)
                        .getStackForm(1L));
        ItemList.Machine_MV_Massfab.set(
                new GT_MetaTileEntity_Massfabricator(462, "basicmachine.massfab.tier.02", "Advanced Mass Fabricator", 2)
                        .getStackForm(1L));
        ItemList.Machine_HV_Massfab.set(
                new GT_MetaTileEntity_Massfabricator(
                        463,
                        "basicmachine.massfab.tier.03",
                        "Advanced Mass Fabricator II",
                        3).getStackForm(1L));
        ItemList.Machine_EV_Massfab.set(
                new GT_MetaTileEntity_Massfabricator(
                        464,
                        "basicmachine.massfab.tier.04",
                        "Advanced Mass Fabricator III",
                        4).getStackForm(1L));
        ItemList.Machine_IV_Massfab.set(
                new GT_MetaTileEntity_Massfabricator(
                        465,
                        "basicmachine.massfab.tier.05",
                        "Advanced Mass Fabricator IV",
                        5).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_Massfab.get(1L),
                bitsd,
                new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_LV, 'F', ItemList.Field_Generator_LV,
                        'C', OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt04.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_Massfab.get(1L),
                bitsd,
                new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_MV, 'F', ItemList.Field_Generator_MV,
                        'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_Massfab.get(1L),
                bitsd,
                new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_HV, 'F', ItemList.Field_Generator_HV,
                        'C', OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_Massfab.get(1L),
                bitsd,
                new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_EV, 'F', ItemList.Field_Generator_EV,
                        'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_Massfab.get(1L),
                bitsd,
                new Object[] { "CFC", aTextWireHull, "CFC", 'M', ItemList.Hull_IV, 'F', ItemList.Field_Generator_IV,
                        'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                        OrePrefixes.cableGt04.get(Materials.Tungsten) });

        ItemList.Machine_LV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        471,
                        "basicmachine.amplifab.tier.01",
                        "Basic Amplifabricator",
                        1,
                        "Extracting UU Amplifier",
                        GT_Recipe.GT_Recipe_Map.sAmplifiers,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Amplifabricator.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AMPLIFAB",
                        new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_MV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        472,
                        "basicmachine.amplifab.tier.02",
                        "Advanced Amplifabricator",
                        2,
                        "Extracting UU Amplifier",
                        GT_Recipe.GT_Recipe_Map.sAmplifiers,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Amplifabricator.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AMPLIFAB",
                        new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_HV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        473,
                        "basicmachine.amplifab.tier.03",
                        "Advanced Amplifabricator II",
                        3,
                        "Extracting UU Amplifier",
                        GT_Recipe.GT_Recipe_Map.sAmplifiers,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Amplifabricator.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AMPLIFAB",
                        new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_EV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        474,
                        "basicmachine.amplifab.tier.04",
                        "Advanced Amplifabricator III",
                        4,
                        "Extracting UU Amplifier",
                        GT_Recipe.GT_Recipe_Map.sAmplifiers,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Amplifabricator.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AMPLIFAB",
                        new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));
        ItemList.Machine_IV_Amplifab.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        475,
                        "basicmachine.amplifab.tier.05",
                        "Advanced Amplifabricator IV",
                        5,
                        "Extracting UU Amplifier",
                        GT_Recipe.GT_Recipe_Map.sAmplifiers,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Amplifabricator.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AMPLIFAB",
                        new Object[] { aTextWirePump, aTextPlateMotor, "CPC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4 }).getStackForm(1L));

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_Replicator.get(1L),
                bitsd,
                new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_LV, 'F',
                        ItemList.Field_Generator_LV, 'E', ItemList.Emitter_LV, 'C',
                        OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt04.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_Replicator.get(1L),
                bitsd,
                new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_MV, 'F',
                        ItemList.Field_Generator_MV, 'E', ItemList.Emitter_MV, 'C',
                        OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt04.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_Replicator.get(1L),
                bitsd,
                new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_HV, 'F',
                        ItemList.Field_Generator_HV, 'E', ItemList.Emitter_HV, 'C',
                        OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_Replicator.get(1L),
                bitsd,
                new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_EV, 'F',
                        ItemList.Field_Generator_EV, 'E', ItemList.Emitter_EV, 'C',
                        OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt04.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_Replicator.get(1L),
                bitsd,
                new Object[] { "EFE", aTextCableHull, aTextMotorWire, 'M', ItemList.Hull_IV, 'F',
                        ItemList.Field_Generator_IV, 'E', ItemList.Emitter_IV, 'C',
                        OrePrefixes.circuit.get(Materials.Master), 'W',
                        OrePrefixes.cableGt04.get(Materials.Tungsten) });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_Brewery.get(1L),
                bitsd,
                new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Pump_LV, 'B',
                        new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tin), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_Brewery.get(1L),
                bitsd,
                new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Pump_MV, 'B',
                        new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_Brewery.get(1L),
                bitsd,
                new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Pump_HV, 'B',
                        new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt01.get(Materials.Gold), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_Brewery.get(1L),
                bitsd,
                new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Pump_EV, 'B',
                        new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                        OrePrefixes.cableGt01.get(Materials.Aluminium), 'G', new ItemStack(Blocks.glass, 1) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_Brewery.get(1L),
                bitsd,
                new Object[] { "GPG", aTextWireHull, "CBC", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Pump_IV, 'B',
                        new ItemStack(Items.brewing_stand, 0), 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tungsten), 'G', new ItemStack(Blocks.glass, 1) });

        ItemList.Machine_LV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        501,
                        "basicmachine.fermenter.tier.01",
                        "Basic Fermenter",
                        1,
                        "Fermenting Fluids",
                        GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Fermenter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FERMENTER",
                        new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        502,
                        "basicmachine.fermenter.tier.02",
                        "Advanced Fermenter",
                        2,
                        "Fermenting Fluids",
                        GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Fermenter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FERMENTER",
                        new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        503,
                        "basicmachine.fermenter.tier.03",
                        "Advanced Fermenter II",
                        3,
                        "Fermenting Fluids",
                        GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Fermenter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FERMENTER",
                        new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        504,
                        "basicmachine.fermenter.tier.04",
                        "Advanced Fermenter III",
                        4,
                        "Fermenting Fluids",
                        GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Fermenter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FERMENTER",
                        new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Fermenter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        505,
                        "basicmachine.fermenter.tier.05",
                        "Advanced Fermenter IV",
                        5,
                        "Fermenting Fluids",
                        GT_Recipe.GT_Recipe_Map.sFermentingRecipes,
                        1,
                        1,
                        1000,
                        0,
                        1,
                        "Fermenter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FERMENTER",
                        new Object[] { aTextWirePump, "GMG", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_FluidExtractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        511,
                        "basicmachine.fluidextractor.tier.01",
                        "Basic Fluid Extractor",
                        1,
                        "Extracting Fluids from Items",
                        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidExtractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_EXTRACTOR",
                        new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_FluidExtractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        512,
                        "basicmachine.fluidextractor.tier.02",
                        "Advanced Fluid Extractor",
                        2,
                        "Extracting Fluids from Items",
                        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidExtractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_EXTRACTOR",
                        new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_FluidExtractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        513,
                        "basicmachine.fluidextractor.tier.03",
                        "Advanced Fluid Extractor II",
                        3,
                        "Extracting Fluids from Items",
                        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidExtractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_EXTRACTOR",
                        new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_FluidExtractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        514,
                        "basicmachine.fluidextractor.tier.04",
                        "Advanced Fluid Extractor III",
                        4,
                        "Extracting Fluids from Items",
                        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidExtractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_EXTRACTOR",
                        new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_FluidExtractor.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        515,
                        "basicmachine.fluidextractor.tier.05",
                        "Advanced Fluid Extractor IV",
                        5,
                        "Extracting Fluids from Items",
                        GT_Recipe.GT_Recipe_Map.sFluidExtractionRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidExtractor.png",
                        SoundResource.IC2_MACHINES_EXTRACTOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_EXTRACTOR",
                        new Object[] { "GEG", "TPT", "CMC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        521,
                        "basicmachine.fluidsolidifier.tier.01",
                        "Basic Fluid Solidifier",
                        1,
                        "Cools Fluids down to form Solids",
                        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidSolidifier.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_SOLIDIFIER",
                        new Object[] { "PGP", aTextWireHull, "CBC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_MV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        522,
                        "basicmachine.fluidsolidifier.tier.02",
                        "Advanced Fluid Solidifier",
                        2,
                        "Cools Fluids down to form Solids",
                        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidSolidifier.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_SOLIDIFIER",
                        new Object[] { "PGP", aTextWireHull, "CBC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_HV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        523,
                        "basicmachine.fluidsolidifier.tier.03",
                        "Advanced Fluid Solidifier II",
                        3,
                        "Cools Fluids down to form Solids",
                        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidSolidifier.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_SOLIDIFIER",
                        new Object[] { "PGP", aTextWireHull, "CBC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_EV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        524,
                        "basicmachine.fluidsolidifier.tier.04",
                        "Advanced Fluid Solidifier III",
                        4,
                        "Cools Fluids down to form Solids",
                        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidSolidifier.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_SOLIDIFIER",
                        new Object[] { "PGP", aTextWireHull, "CBC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));
        ItemList.Machine_IV_FluidSolidifier.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        525,
                        "basicmachine.fluidsolidifier.tier.05",
                        "Advanced Fluid Solidifier IV",
                        5,
                        "Cools Fluids down to form Solids",
                        GT_Recipe.GT_Recipe_Map.sFluidSolidficationRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "FluidSolidifier.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_SOLIDIFIER",
                        new Object[] { "PGP", aTextWireHull, "CBC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS, 'B', OreDictNames.craftingChest })
                                        .getStackForm(1L));

        ItemList.Machine_LV_Distillery.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        531,
                        "basicmachine.distillery.tier.01",
                        "Basic Distillery",
                        1,
                        "Extracting the most relevant Parts of Fluids",
                        GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                        1,
                        1,
                        8000,
                        0,
                        1,
                        "Distillery.png",
                        SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "DISTILLERY",
                        new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Distillery.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        532,
                        "basicmachine.distillery.tier.02",
                        "Advanced Distillery",
                        2,
                        "Extracting the most relevant Parts of Fluids",
                        GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                        1,
                        1,
                        16000,
                        0,
                        1,
                        "Distillery.png",
                        SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "DISTILLERY",
                        new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Distillery.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        533,
                        "basicmachine.distillery.tier.03",
                        "Advanced Distillery II",
                        3,
                        "Extracting the most relevant Parts of Fluids",
                        GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                        1,
                        1,
                        24000,
                        0,
                        1,
                        "Distillery.png",
                        SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "DISTILLERY",
                        new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Distillery.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        534,
                        "basicmachine.distillery.tier.04",
                        "Advanced Distillery III",
                        4,
                        "Extracting the most relevant Parts of Fluids",
                        GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                        1,
                        1,
                        32000,
                        0,
                        1,
                        "Distillery.png",
                        SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "DISTILLERY",
                        new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Distillery.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        535,
                        "basicmachine.distillery.tier.05",
                        "Advanced Distillery IV",
                        5,
                        "Extracting the most relevant Parts of Fluids",
                        GT_Recipe.GT_Recipe_Map.sDistilleryRecipes,
                        1,
                        1,
                        40000,
                        0,
                        1,
                        "Distillery.png",
                        SoundResource.GT_MACHINES_DISTILLERY_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "DISTILLERY",
                        new Object[] { "GBG", aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'B',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_ChemicalBath.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        541,
                        "basicmachine.chemicalbath.tier.01",
                        "Basic Chemical Bath",
                        1,
                        "Bathing Ores in Chemicals to separate them",
                        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                        1,
                        3,
                        8000,
                        0,
                        1,
                        "ChemicalBath.png",
                        SoundResource.NONE,
                        false,
                        true,
                        SpecialEffects.NONE,
                        "CHEMICAL_BATH",
                        new Object[] { "VGW", "PGV", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_ChemicalBath.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        542,
                        "basicmachine.chemicalbath.tier.02",
                        "Advanced Chemical Bath",
                        2,
                        "Bathing Ores in Chemicals to separate them",
                        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                        1,
                        3,
                        8000,
                        0,
                        1,
                        "ChemicalBath.png",
                        SoundResource.NONE,
                        false,
                        true,
                        SpecialEffects.NONE,
                        "CHEMICAL_BATH",
                        new Object[] { "VGW", "PGV", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_ChemicalBath.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        543,
                        "basicmachine.chemicalbath.tier.03",
                        "Advanced Chemical Bath II",
                        3,
                        "Bathing Ores in Chemicals to separate them",
                        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                        1,
                        3,
                        8000,
                        0,
                        1,
                        "ChemicalBath.png",
                        SoundResource.NONE,
                        false,
                        true,
                        SpecialEffects.NONE,
                        "CHEMICAL_BATH",
                        new Object[] { "VGW", "PGV", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_ChemicalBath.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        544,
                        "basicmachine.chemicalbath.tier.04",
                        "Advanced Chemical Bath III",
                        4,
                        "Bathing Ores in Chemicals to separate them",
                        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                        1,
                        3,
                        8000,
                        0,
                        1,
                        "ChemicalBath.png",
                        SoundResource.NONE,
                        false,
                        true,
                        SpecialEffects.NONE,
                        "CHEMICAL_BATH",
                        new Object[] { "VGW", "PGV", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_ChemicalBath.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        545,
                        "basicmachine.chemicalbath.tier.05",
                        "Advanced Chemical Bath IV",
                        5,
                        "Bathing Ores in Chemicals to separate them",
                        GT_Recipe.GT_Recipe_Map.sChemicalBathRecipes,
                        1,
                        3,
                        8000,
                        0,
                        1,
                        "ChemicalBath.png",
                        SoundResource.NONE,
                        false,
                        true,
                        SpecialEffects.NONE,
                        "CHEMICAL_BATH",
                        new Object[] { "VGW", "PGV", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        551,
                        "basicmachine.polarizer.tier.01",
                        "Basic Polarizer",
                        1,
                        "Bipolarising your Magnets",
                        GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Polarizer.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "POLARIZER",
                        new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        552,
                        "basicmachine.polarizer.tier.02",
                        "Advanced Polarizer",
                        2,
                        "Bipolarising your Magnets",
                        GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Polarizer.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "POLARIZER",
                        new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        553,
                        "basicmachine.polarizer.tier.03",
                        "Advanced Polarizer II",
                        3,
                        "Bipolarising your Magnets",
                        GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Polarizer.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "POLARIZER",
                        new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        554,
                        "basicmachine.polarizer.tier.04",
                        "Advanced Polarizer III",
                        4,
                        "Bipolarising your Magnets",
                        GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Polarizer.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "POLARIZER",
                        new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Polarizer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        555,
                        "basicmachine.polarizer.tier.05",
                        "Advanced Polarizer IV",
                        5,
                        "Bipolarising your Magnets",
                        GT_Recipe.GT_Recipe_Map.sPolarizerRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "Polarizer.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "POLARIZER",
                        new Object[] { "ZSZ", aTextWireHull, "ZSZ", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        561,
                        "basicmachine.electromagneticseparator.tier.01",
                        "Basic Electromagnetic Separator",
                        1,
                        "Separating the magnetic Ores from the rest",
                        GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ElectromagneticSeparator.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROMAGNETIC_SEPARATOR",
                        new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        562,
                        "basicmachine.electromagneticseparator.tier.02",
                        "Advanced Electromagnetic Separator",
                        2,
                        "Separating the magnetic Ores from the rest",
                        GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ElectromagneticSeparator.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROMAGNETIC_SEPARATOR",
                        new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        563,
                        "basicmachine.electromagneticseparator.tier.03",
                        "Advanced Electromagnetic Separator II",
                        3,
                        "Separating the magnetic Ores from the rest",
                        GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ElectromagneticSeparator.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROMAGNETIC_SEPARATOR",
                        new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        564,
                        "basicmachine.electromagneticseparator.tier.04",
                        "Advanced Electromagnetic Separator III",
                        4,
                        "Separating the magnetic Ores from the rest",
                        GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ElectromagneticSeparator.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROMAGNETIC_SEPARATOR",
                        new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_ElectromagneticSeparator.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        565,
                        "basicmachine.electromagneticseparator.tier.05",
                        "Advanced Electromagnetic Separator IV",
                        5,
                        "Separating the magnetic Ores from the rest",
                        GT_Recipe.GT_Recipe_Map.sElectroMagneticSeparatorRecipes,
                        1,
                        3,
                        0,
                        0,
                        1,
                        "ElectromagneticSeparator.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTROMAGNETIC_SEPARATOR",
                        new Object[] { "VWZ", "WMS", "CWZ", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'S',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC, 'Z',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        571,
                        "basicmachine.autoclave.tier.01",
                        "Basic Autoclave",
                        1,
                        "Crystallizing your Dusts",
                        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                        2,
                        2,
                        10000,
                        0,
                        1,
                        "Autoclave2.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AUTOCLAVE",
                        new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        572,
                        "basicmachine.autoclave.tier.02",
                        "Advanced Autoclave",
                        2,
                        "Crystallizing your Dusts",
                        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                        2,
                        2,
                        20000,
                        0,
                        1,
                        "Autoclave2.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AUTOCLAVE",
                        new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        573,
                        "basicmachine.autoclave.tier.03",
                        "Advanced Autoclave II",
                        3,
                        "Crystallizing your Dusts",
                        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                        2,
                        3,
                        30000,
                        0,
                        1,
                        "Autoclave3.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AUTOCLAVE",
                        new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        574,
                        "basicmachine.autoclave.tier.04",
                        "Advanced Autoclave III",
                        4,
                        "Crystallizing your Dusts",
                        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                        2,
                        4,
                        40000,
                        0,
                        1,
                        "Autoclave4.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AUTOCLAVE",
                        new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Autoclave.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        575,
                        "basicmachine.autoclave.tier.05",
                        "Advanced Autoclave IV",
                        5,
                        "Crystallizing your Dusts",
                        GT_Recipe.GT_Recipe_Map.sAutoclaveRecipes,
                        2,
                        4,
                        50000,
                        0,
                        1,
                        "Autoclave4.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "AUTOCLAVE",
                        new Object[] { "IGI", "IMI", "CPC", 'M', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'I',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        581,
                        "basicmachine.mixer.tier.01",
                        "Basic Mixer",
                        1,
                        "Will it Blend?",
                        GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "Mixer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MIXER",
                        new Object[] { "GRG", "GEG", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        582,
                        "basicmachine.mixer.tier.02",
                        "Advanced Mixer",
                        2,
                        "Will it Blend?",
                        GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                        6,
                        1,
                        32000,
                        0,
                        1,
                        "Mixer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MIXER",
                        new Object[] { "GRG", "GEG", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        583,
                        "basicmachine.mixer.tier.03",
                        "Advanced Mixer II",
                        3,
                        "Will it Blend?",
                        GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                        6,
                        4,
                        48000,
                        0,
                        1,
                        "Mixer4.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MIXER",
                        new Object[] { "GRG", "GEG", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        584,
                        "basicmachine.mixer.tier.04",
                        "Advanced Mixer III",
                        4,
                        "Will it Blend?",
                        GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                        9,
                        4,
                        64000,
                        0,
                        1,
                        "Mixer6.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MIXER",
                        new Object[] { "GRG", "GEG", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_Mixer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        585,
                        "basicmachine.mixer.tier.05",
                        "Advanced Mixer IV",
                        5,
                        "Will it Blend?",
                        GT_Recipe.GT_Recipe_Map.sMixerRecipes,
                        9,
                        4,
                        128000,
                        0,
                        1,
                        "Mixer6.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "MIXER",
                        new Object[] { "GRG", "GEG", aTextCableHull, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR, 'R',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        591,
                        "basicmachine.laserengraver.tier.01",
                        "Basic Precision Laser Engraver",
                        1,
                        "Don't look directly at the Laser",
                        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                        2,
                        1,
                        8000,
                        0,
                        1,
                        "LaserEngraver.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LASER_ENGRAVER",
                        new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        592,
                        "basicmachine.laserengraver.tier.02",
                        "Advanced Precision Laser Engraver",
                        2,
                        "Don't look directly at the Laser",
                        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                        2,
                        1,
                        16000,
                        0,
                        1,
                        "LaserEngraver.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LASER_ENGRAVER",
                        new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        593,
                        "basicmachine.laserengraver.tier.03",
                        "Advanced Precision Laser Engraver II",
                        3,
                        "Don't look directly at the Laser",
                        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                        2,
                        1,
                        24000,
                        0,
                        1,
                        "LaserEngraver.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LASER_ENGRAVER",
                        new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        594,
                        "basicmachine.laserengraver.tier.04",
                        "Advanced Precision Laser Engraver III",
                        4,
                        "Don't look directly at the Laser",
                        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                        4,
                        1,
                        32000,
                        0,
                        1,
                        "LaserEngraver2.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LASER_ENGRAVER",
                        new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_LaserEngraver.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        595,
                        "basicmachine.laserengraver.tier.05",
                        "Advanced Precision Laser Engraver IV",
                        5,
                        "Don't look directly at the Laser",
                        GT_Recipe.GT_Recipe_Map.sLaserEngraverRecipes,
                        4,
                        1,
                        40000,
                        0,
                        1,
                        "LaserEngraver2.png",
                        SoundResource.IC2_MACHINES_MAGNETIZER_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "LASER_ENGRAVER",
                        new Object[] { "PEP", aTextCableHull, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        601,
                        "basicmachine.press.tier.01",
                        "Basic Forming Press",
                        1,
                        "Imprinting Images into things",
                        GT_Recipe.GT_Recipe_Map.sPressRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Press.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PRESS",
                        new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        602,
                        "basicmachine.press.tier.02",
                        "Advanced Forming Press",
                        2,
                        "Imprinting Images into things",
                        GT_Recipe.GT_Recipe_Map.sPressRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Press.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PRESS",
                        new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        603,
                        "basicmachine.press.tier.03",
                        "Advanced Forming Press II",
                        3,
                        "Imprinting Images into things",
                        GT_Recipe.GT_Recipe_Map.sPressRecipes,
                        4,
                        1,
                        0,
                        0,
                        1,
                        "Press2.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PRESS",
                        new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        604,
                        "basicmachine.press.tier.04",
                        "Advanced Forming Press III",
                        4,
                        "Imprinting Images into things",
                        GT_Recipe.GT_Recipe_Map.sPressRecipes,
                        4,
                        1,
                        0,
                        0,
                        1,
                        "Press2.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PRESS",
                        new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Press.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        605,
                        "basicmachine.press.tier.05",
                        "Advanced Forming Press IV",
                        5,
                        "Imprinting Images into things",
                        GT_Recipe.GT_Recipe_Map.sPressRecipes,
                        6,
                        1,
                        0,
                        0,
                        1,
                        "Press3.png",
                        SoundResource.IC2_MACHINES_COMPRESSOR_OP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PRESS",
                        new Object[] { aTextWirePump, aTextCableHull, aTextWirePump, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        611,
                        "basicmachine.hammer.tier.01",
                        "Basic Forge Hammer",
                        1,
                        "Stop, Hammertime!",
                        GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                        1,
                        1,
                        8000,
                        6,
                        3,
                        "Hammer.png",
                        SoundResource.RANDOM_ANVIL_USE,
                        false,
                        false,
                        SpecialEffects.MAIN_RANDOM_SPARKS,
                        "HAMMER",
                        new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A',
                                OreDictNames.craftingAnvil }).getStackForm(1L));
        ItemList.Machine_MV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        612,
                        "basicmachine.hammer.tier.02",
                        "Advanced Forge Hammer",
                        2,
                        "Stop, Hammertime!",
                        GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                        1,
                        1,
                        16000,
                        6,
                        3,
                        "Hammer.png",
                        SoundResource.RANDOM_ANVIL_USE,
                        false,
                        false,
                        SpecialEffects.MAIN_RANDOM_SPARKS,
                        "HAMMER",
                        new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A',
                                OreDictNames.craftingAnvil }).getStackForm(1L));
        ItemList.Machine_HV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        613,
                        "basicmachine.hammer.tier.03",
                        "Advanced Forge Hammer II",
                        3,
                        "Stop, Hammertime!",
                        GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                        1,
                        1,
                        32000,
                        6,
                        3,
                        "Hammer.png",
                        SoundResource.RANDOM_ANVIL_USE,
                        false,
                        false,
                        SpecialEffects.MAIN_RANDOM_SPARKS,
                        "HAMMER",
                        new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A',
                                OreDictNames.craftingAnvil }).getStackForm(1L));
        ItemList.Machine_EV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        614,
                        "basicmachine.hammer.tier.04",
                        "Advanced Forge Hammer III",
                        4,
                        "Stop, Hammertime!",
                        GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                        1,
                        1,
                        64000,
                        6,
                        3,
                        "Hammer.png",
                        SoundResource.RANDOM_ANVIL_USE,
                        false,
                        false,
                        SpecialEffects.MAIN_RANDOM_SPARKS,
                        "HAMMER",
                        new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A',
                                OreDictNames.craftingAnvil }).getStackForm(1L));
        ItemList.Machine_IV_Hammer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        615,
                        "basicmachine.hammer.tier.05",
                        "Advanced Forge Hammer IV",
                        5,
                        "Stop, Hammertime!",
                        GT_Recipe.GT_Recipe_Map.sHammerRecipes,
                        1,
                        1,
                        96000,
                        6,
                        3,
                        "Hammer.png",
                        SoundResource.RANDOM_ANVIL_USE,
                        false,
                        false,
                        SpecialEffects.MAIN_RANDOM_SPARKS,
                        "HAMMER",
                        new Object[] { aTextWirePump, aTextCableHull, "WAW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'A',
                                OreDictNames.craftingAnvil }).getStackForm(1L));

        ItemList.Machine_LV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        621,
                        "basicmachine.fluidheater.tier.01",
                        "Basic Fluid Heater",
                        1,
                        "Heating up your Fluids",
                        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                        1,
                        0,
                        8000,
                        0,
                        1,
                        "FluidHeater.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_HEATER",
                        new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_MV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        622,
                        "basicmachine.fluidheater.tier.02",
                        "Advanced Fluid Heater",
                        2,
                        "Heating up your Fluids",
                        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                        1,
                        0,
                        8000,
                        0,
                        1,
                        "FluidHeater.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_HEATER",
                        new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_HV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        623,
                        "basicmachine.fluidheater.tier.03",
                        "Advanced Fluid Heater II",
                        3,
                        "Heating up your Fluids",
                        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                        1,
                        0,
                        8000,
                        0,
                        1,
                        "FluidHeater.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_HEATER",
                        new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_EV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        624,
                        "basicmachine.fluidheater.tier.04",
                        "Advanced Fluid Heater III",
                        4,
                        "Heating up your Fluids",
                        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                        1,
                        0,
                        8000,
                        0,
                        1,
                        "FluidHeater.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_HEATER",
                        new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));
        ItemList.Machine_IV_FluidHeater.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        625,
                        "basicmachine.fluidheater.tier.05",
                        "Advanced Fluid Heater IV",
                        5,
                        "Heating up your Fluids",
                        GT_Recipe.GT_Recipe_Map.sFluidHeaterRecipes,
                        1,
                        0,
                        8000,
                        0,
                        1,
                        "FluidHeater.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "FLUID_HEATER",
                        new Object[] { "OGO", aTextPlateMotor, aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'O',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'G',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS }).getStackForm(1L));

        ItemList.Machine_LV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        631,
                        "basicmachine.slicer.tier.01",
                        "Basic Slicing Machine",
                        1,
                        "Slice of Life",
                        GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Slicer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SLICER",
                        new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        632,
                        "basicmachine.slicer.tier.02",
                        "Advanced Slicing Machine",
                        2,
                        "Slice of Life",
                        GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Slicer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SLICER",
                        new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        633,
                        "basicmachine.slicer.tier.03",
                        "Advanced Slicing Machine II",
                        3,
                        "Slice of Life",
                        GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Slicer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SLICER",
                        new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        634,
                        "basicmachine.slicer.tier.04",
                        "Advanced Slicing Machine III",
                        4,
                        "Slice of Life",
                        GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Slicer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SLICER",
                        new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Slicer.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        635,
                        "basicmachine.slicer.tier.05",
                        "Advanced Slicing Machine IV",
                        5,
                        "Slice of Life",
                        GT_Recipe.GT_Recipe_Map.sSlicerRecipes,
                        2,
                        1,
                        0,
                        0,
                        1,
                        "Slicer.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SLICER",
                        new Object[] { aTextWireCoil, "PMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        641,
                        "basicmachine.sifter.tier.01",
                        "Basic Sifting Machine",
                        1,
                        "Stay calm and keep sifting",
                        GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                        1,
                        9,
                        1000,
                        2,
                        5,
                        "Sifter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SIFTER",
                        new Object[] { "WFW", aTextPlateMotor, "CFC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter,
                                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_MV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        642,
                        "basicmachine.sifter.tier.02",
                        "Advanced Sifting Machine",
                        2,
                        "Stay calm and keep sifting",
                        GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                        1,
                        9,
                        2000,
                        2,
                        5,
                        "Sifter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SIFTER",
                        new Object[] { "WFW", aTextPlateMotor, "CFC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter,
                                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_HV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        643,
                        "basicmachine.sifter.tier.03",
                        "Advanced Sifting Machine II",
                        3,
                        "Stay calm and keep sifting",
                        GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                        1,
                        9,
                        4000,
                        2,
                        5,
                        "Sifter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SIFTER",
                        new Object[] { "WFW", aTextPlateMotor, "CFC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter,
                                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_EV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        644,
                        "basicmachine.sifter.tier.04",
                        "Advanced Sifting Machine III",
                        4,
                        "Stay calm and keep sifting",
                        GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                        1,
                        9,
                        8000,
                        2,
                        5,
                        "Sifter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SIFTER",
                        new Object[] { "WFW", aTextPlateMotor, "CFC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter,
                                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));
        ItemList.Machine_IV_Sifter.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        645,
                        "basicmachine.sifter.tier.05",
                        "Advanced Sifting Machine IV",
                        5,
                        "Stay calm and keep sifting",
                        GT_Recipe.GT_Recipe_Map.sSifterRecipes,
                        1,
                        9,
                        16000,
                        2,
                        5,
                        "Sifter.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "SIFTER",
                        new Object[] { "WFW", aTextPlateMotor, "CFC", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON, 'F', OreDictNames.craftingFilter,
                                'C', GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE }).getStackForm(1L));

        ItemList.Machine_LV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        651,
                        "basicmachine.arcfurnace.tier.01",
                        "Basic Arc Furnace",
                        1,
                        "",
                        GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                        1,
                        4,
                        16000,
                        0,
                        1,
                        "ArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, aTextPlate, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_MV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        652,
                        "basicmachine.arcfurnace.tier.02",
                        "Advanced Arc Furnace",
                        2,
                        "",
                        GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                        1,
                        4,
                        24000,
                        0,
                        1,
                        "ArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, aTextPlate, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_HV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        653,
                        "basicmachine.arcfurnace.tier.03",
                        "Advanced Arc Furnace II",
                        3,
                        "",
                        GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                        1,
                        4,
                        32000,
                        0,
                        1,
                        "ArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, aTextPlate, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_EV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        654,
                        "basicmachine.arcfurnace.tier.04",
                        "Advanced Arc Furnace III",
                        4,
                        "",
                        GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                        1,
                        4,
                        48000,
                        0,
                        1,
                        "ArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, aTextPlate, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_IV_ArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        655,
                        "basicmachine.arcfurnace.tier.05",
                        "Advanced Arc Furnace IV",
                        5,
                        "",
                        GT_Recipe.GT_Recipe_Map.sArcFurnaceRecipes,
                        1,
                        4,
                        64000,
                        0,
                        1,
                        "ArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, aTextPlate, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));

        ItemList.Machine_LV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        661,
                        "basicmachine.plasmaarcfurnace.tier.01",
                        "Basic Plasma Arc Furnace",
                        1,
                        "",
                        GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                        1,
                        4,
                        1000,
                        0,
                        1,
                        "PlasmaArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PLASMA_ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, "TPT", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_MV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        662,
                        "basicmachine.plasmaarcfurnace.tier.02",
                        "Advanced Plasma Arc Furnace",
                        2,
                        "",
                        GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                        1,
                        4,
                        2000,
                        0,
                        1,
                        "PlasmaArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PLASMA_ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, "TPT", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_HV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        663,
                        "basicmachine.plasmaarcfurnace.tier.03",
                        "Advanced Plasma Arc Furnace II",
                        3,
                        "",
                        GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                        1,
                        4,
                        4000,
                        0,
                        1,
                        "PlasmaArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PLASMA_ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, "TPT", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_EV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        664,
                        "basicmachine.plasmaarcfurnace.tier.04",
                        "Advanced Plasma Arc Furnace III",
                        4,
                        "",
                        GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                        1,
                        4,
                        8000,
                        0,
                        1,
                        "PlasmaArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PLASMA_ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, "TPT", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));
        ItemList.Machine_IV_PlasmaArcFurnace.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        665,
                        "basicmachine.plasmaarcfurnace.tier.05",
                        "Advanced Plasma Arc Furnace IV",
                        5,
                        "",
                        GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes,
                        1,
                        4,
                        16000,
                        0,
                        1,
                        "PlasmaArcFurnace.png",
                        SoundResource.IC2_MACHINES_INDUCTION_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "PLASMA_ARC_FURNACE",
                        new Object[] { "WGW", aTextCableHull, "TPT", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'P',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4, 'T',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP, 'G',
                                OrePrefixes.cell.get(Materials.Graphite) }).getStackForm(1L));

        ItemList.Machine_LV_Oven.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        671,
                        "basicmachine.e_oven.tier.01",
                        "Basic Electric Oven",
                        1,
                        "Just a Furnace with a different Design",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Oven.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_OVEN",
                        new Object[] { "CEC", aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Oven").getStackForm(1L));
        ItemList.Machine_MV_Oven.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        672,
                        "basicmachine.e_oven.tier.02",
                        "Advanced Electric Oven",
                        2,
                        "Just a Furnace with a different Design",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Oven.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_OVEN",
                        new Object[] { "CEC", aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Oven").getStackForm(1L));
        ItemList.Machine_HV_Oven.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        673,
                        "basicmachine.e_oven.tier.03",
                        "Advanced Electric Oven II",
                        3,
                        "Just a Furnace with a different Design",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Oven.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_OVEN",
                        new Object[] { "CEC", aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Oven").getStackForm(1L));
        ItemList.Machine_EV_Oven.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        674,
                        "basicmachine.e_oven.tier.04",
                        "Advanced Electric Oven III",
                        4,
                        "Just a Furnace with a different Design",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Oven.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_OVEN",
                        new Object[] { "CEC", aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Oven").getStackForm(1L));
        ItemList.Machine_IV_Oven.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        675,
                        "basicmachine.e_oven.tier.05",
                        "Advanced Electric Oven IV",
                        5,
                        "Just a Furnace with a different Design",
                        GT_Recipe.GT_Recipe_Map.sFurnaceRecipes,
                        1,
                        1,
                        0,
                        0,
                        1,
                        "E_Oven.png",
                        SoundResource.IC2_MACHINES_ELECTROFURNACE_LOOP,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "ELECTRIC_OVEN",
                        new Object[] { "CEC", aTextCableHull, "WEW", 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING })
                                        .setProgressBarTextureName("E_Oven").getStackForm(1L));

        ItemList.Machine_LV_Miner
                .set(new GT_MetaTileEntity_Miner(679, "basicmachine.miner.tier.01", "Basic Miner", 1).getStackForm(1L));
        ItemList.Machine_MV_Miner
                .set(new GT_MetaTileEntity_Miner(680, "basicmachine.miner.tier.02", "Good Miner", 2).getStackForm(1L));
        ItemList.Machine_HV_Miner.set(
                new GT_MetaTileEntity_Miner(681, "basicmachine.miner.tier.03", "Advanced Miner", 3).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_LV_Miner.get(1L),
                bitsd,
                new Object[] { "EEE", aTextWireHull, "CSC", 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV, 'C',
                        OrePrefixes.circuit.get(Materials.Basic), 'W', OrePrefixes.cableGt01.get(Materials.Tin), 'S',
                        ItemList.Sensor_LV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_MV_Miner.get(1L),
                bitsd,
                new Object[] { "PEP", aTextWireHull, "CSC", 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV, 'P',
                        ItemList.Electric_Piston_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                        OrePrefixes.cableGt02.get(Materials.Copper), 'S', ItemList.Sensor_MV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_Miner.get(1L),
                bitsd,
                new Object[] { "RPR", aTextWireHull, "CSC", 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV, 'P',
                        ItemList.Electric_Piston_HV, 'R', ItemList.Robot_Arm_HV, 'C',
                        OrePrefixes.circuit.get(Materials.Advanced), 'W', OrePrefixes.cableGt04.get(Materials.Gold),
                        'S', ItemList.Sensor_HV });
    }

    private static void run3() {
        ItemList.Machine_Multi_BlastFurnace.set(
                new GT_MetaTileEntity_ElectricBlastFurnace(1000, "multimachine.blastfurnace", "Electric Blast Furnace")
                        .getStackForm(1L));
        ItemList.Machine_Multi_ImplosionCompressor.set(
                new GT_MetaTileEntity_ImplosionCompressor(
                        1001,
                        "multimachine.implosioncompressor",
                        "Implosion Compressor").getStackForm(1L));
        ItemList.Machine_Multi_VacuumFreezer.set(
                new GT_MetaTileEntity_VacuumFreezer(1002, "multimachine.vacuumfreezer", "Vacuum Freezer")
                        .getStackForm(1L));
        ItemList.Machine_Multi_Furnace.set(
                new GT_MetaTileEntity_MultiFurnace(1003, "multimachine.multifurnace", "Multi Smelter")
                        .getStackForm(1L));
        ItemList.Machine_Multi_PlasmaForge.set(
                new GT_MetaTileEntity_PlasmaForge(
                        1004,
                        "multimachine.plasmaforge",
                        "Dimensionally Transcendent Plasma Forge").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_BlastFurnace.get(1L),
                bitsd,
                new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                        OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_VacuumFreezer.get(1L),
                bitsd,
                new Object[] { aTextPlate, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_FrostProof, 'P',
                        ItemList.Electric_Pump_HV, 'C', OrePrefixes.circuit.get(Materials.Data), 'W',
                        OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_ImplosionCompressor.get(1L),
                bitsd,
                new Object[] { "OOO", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_SolidSteel, 'O',
                        Ic2Items.reinforcedStone, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_Furnace.get(1L),
                bitsd,
                new Object[] { "FFF", aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_HeatProof, 'F',
                        OreDictNames.craftingIronFurnace, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnnealedCopper) });

        ItemList.Machine_Multi_LargeBoiler_Bronze.set(
                new GT_MetaTileEntity_LargeBoiler_Bronze(1020, "multimachine.boiler.bronze", "Large Bronze Boiler")
                        .getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Steel.set(
                new GT_MetaTileEntity_LargeBoiler_Steel(1021, "multimachine.boiler.steel", "Large Steel Boiler")
                        .getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_Titanium.set(
                new GT_MetaTileEntity_LargeBoiler_Titanium(
                        1022,
                        "multimachine.boiler.titanium",
                        "Large Titanium Boiler").getStackForm(1L));
        ItemList.Machine_Multi_LargeBoiler_TungstenSteel.set(
                new GT_MetaTileEntity_LargeBoiler_TungstenSteel(
                        1023,
                        "multimachine.boiler.tungstensteel",
                        "Large Tungstensteel Boiler").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_LargeBoiler_Bronze.get(1L),
                bitsd,
                new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Bronze, 'C',
                        OrePrefixes.circuit.get(Materials.Good), 'W', OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_LargeBoiler_Steel.get(1L),
                bitsd,
                new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Steel, 'C',
                        OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_LargeBoiler_Titanium.get(1L),
                bitsd,
                new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_Titanium, 'C',
                        OrePrefixes.circuit.get(Materials.Data), 'W', OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_LargeBoiler_TungstenSteel.get(1L),
                bitsd,
                new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Firebox_TungstenSteel,
                        'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt01.get(Materials.Aluminium) });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Diesel_LV.get(1L),
                bitsd,
                new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LV, 'P', ItemList.Electric_Piston_LV, 'E',
                        ItemList.Electric_Motor_LV, 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tin), 'G', OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Diesel_MV.get(1L),
                bitsd,
                new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_MV, 'P', ItemList.Electric_Piston_MV, 'E',
                        ItemList.Electric_Motor_MV, 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper), 'G',
                        OrePrefixes.gearGt.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Diesel_HV.get(1L),
                bitsd,
                new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_HV, 'P', ItemList.Electric_Piston_HV, 'E',
                        ItemList.Electric_Motor_HV, 'C', OrePrefixes.circuit.get(Materials.Advanced), 'W',
                        OrePrefixes.cableGt01.get(Materials.Gold), 'G',
                        OrePrefixes.gearGt.get(Materials.StainlessSteel) });

        ItemList.Generator_Gas_Turbine_LV.set(
                new GT_MetaTileEntity_GasTurbine(1115, "basicgenerator.gasturbine.tier.01", "Basic Gas Turbine", 1, 95)
                        .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_MV.set(
                new GT_MetaTileEntity_GasTurbine(
                        1116,
                        "basicgenerator.gasturbine.tier.02",
                        "Advanced Gas Turbine",
                        2,
                        90).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_HV.set(
                new GT_MetaTileEntity_GasTurbine(1117, "basicgenerator.gasturbine.tier.03", "Turbo Gas Turbine", 3, 85)
                        .getStackForm(1L));
        ItemList.Generator_Gas_Turbine_EV.set(
                new GT_MetaTileEntity_GasTurbine(
                        1118,
                        "basicgenerator.gasturbine.tier.04",
                        "Turbo Gas Turbine II",
                        4,
                        60).getStackForm(1L));
        ItemList.Generator_Gas_Turbine_IV.set(
                new GT_MetaTileEntity_GasTurbine(
                        1119,
                        "basicgenerator.gasturbine.tier.05",
                        "Turbo Gas Turbine III",
                        5,
                        50).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Gas_Turbine_LV.get(1L),
                bitsd,
                new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV,
                        'R', OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Gas_Turbine_MV.get(1L),
                bitsd,
                new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV,
                        'R', OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Gas_Turbine_HV.get(1L),
                bitsd,
                new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV,
                        'R', OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced),
                        'W', OrePrefixes.cableGt01.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Gas_Turbine_EV.get(1L),
                bitsd,
                new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_EV, 'E', ItemList.Electric_Motor_EV,
                        'R', OrePrefixes.rotor.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.Data),
                        'W', OrePrefixes.cableGt01.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Gas_Turbine_IV.get(1L),
                bitsd,
                new Object[] { "CRC", "RMR", aTextMotorWire, 'M', ItemList.Hull_IV, 'E', ItemList.Electric_Motor_IV,
                        'R', OrePrefixes.rotor.get(Materials.TungstenSteel), 'C',
                        OrePrefixes.circuit.get(Materials.Elite), 'W', OrePrefixes.cableGt01.get(Materials.Tungsten) });

        ItemList.Generator_Steam_Turbine_LV.set(
                new GT_MetaTileEntity_SteamTurbine(
                        1120,
                        "basicgenerator.steamturbine.tier.01",
                        "Basic Steam Turbine",
                        1).getStackForm(1L));
        ItemList.Generator_Steam_Turbine_MV.set(
                new GT_MetaTileEntity_SteamTurbine(
                        1121,
                        "basicgenerator.steamturbine.tier.02",
                        "Advanced Steam Turbine",
                        2).getStackForm(1L));
        ItemList.Generator_Steam_Turbine_HV.set(
                new GT_MetaTileEntity_SteamTurbine(
                        1122,
                        "basicgenerator.steamturbine.tier.03",
                        "Turbo Steam Turbine",
                        3).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Steam_Turbine_LV.get(1L),
                bitsd,
                new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_LV, 'E', ItemList.Electric_Motor_LV,
                        'R', OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Basic), 'W',
                        OrePrefixes.cableGt01.get(Materials.Tin), 'P', OrePrefixes.pipeMedium.get(Materials.Bronze) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Steam_Turbine_MV.get(1L),
                bitsd,
                new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_MV, 'E', ItemList.Electric_Motor_MV,
                        'R', OrePrefixes.rotor.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Good), 'W',
                        OrePrefixes.cableGt01.get(Materials.AnyCopper), 'P',
                        OrePrefixes.pipeMedium.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Steam_Turbine_HV.get(1L),
                bitsd,
                new Object[] { "PCP", "RMR", aTextMotorWire, 'M', ItemList.Hull_HV, 'E', ItemList.Electric_Motor_HV,
                        'R', OrePrefixes.rotor.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced),
                        'W', OrePrefixes.cableGt01.get(Materials.Gold), 'P',
                        OrePrefixes.pipeMedium.get(Materials.StainlessSteel) });

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
        if (!Thaumcraft.isModLoaded()) {
            GT_ModHandler.addCraftingRecipe(
                    ItemList.MagicEnergyConverter_LV.get(1L),
                    bitsd,
                    new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B', new ItemStack(Blocks.beacon), 'C',
                            OrePrefixes.circuit.get(Materials.Advanced), 'T', ItemList.Field_Generator_LV, 'F',
                            OrePrefixes.plate.get(Materials.Platinum) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.MagicEnergyConverter_MV.get(1L),
                    bitsd,
                    new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B', new ItemStack(Blocks.beacon), 'C',
                            OrePrefixes.circuit.get(Materials.Data), 'T', ItemList.Field_Generator_MV, 'F',
                            OrePrefixes.plate.get(Materials.Iridium) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.MagicEnergyConverter_HV.get(1L),
                    bitsd,
                    new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B', new ItemStack(Blocks.beacon), 'C',
                            OrePrefixes.circuit.get(Materials.Elite), 'T', ItemList.Field_Generator_HV, 'F',
                            OrePrefixes.plate.get(Materials.Neutronium) });

            GT_ModHandler.addCraftingRecipe(
                    ItemList.MagicEnergyAbsorber_LV.get(1L),
                    bitsd,
                    new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_LV, 'B',
                            ItemList.MagicEnergyConverter_LV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Advanced),
                            'T', ItemList.Field_Generator_LV, 'F', OrePrefixes.plate.get(Materials.Platinum) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.MagicEnergyAbsorber_MV.get(1L),
                    bitsd,
                    new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_MV, 'B',
                            ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Data), 'T',
                            ItemList.Field_Generator_MV, 'F', OrePrefixes.plate.get(Materials.Iridium) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.MagicEnergyAbsorber_HV.get(1L),
                    bitsd,
                    new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                            ItemList.MagicEnergyConverter_MV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Elite),
                            'T', ItemList.Field_Generator_HV, 'F', OrePrefixes.plate.get(Materials.Europium) });
            GT_ModHandler.addCraftingRecipe(
                    ItemList.MagicEnergyAbsorber_EV.get(1L),
                    bitsd,
                    new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                            ItemList.MagicEnergyConverter_HV.get(1L), 'C', OrePrefixes.circuit.get(Materials.Master),
                            'T', ItemList.Field_Generator_EV, 'F', OrePrefixes.plate.get(Materials.Neutronium) });
        }
        ItemList.FusionComputer_LuV.set(
                new GT_MetaTileEntity_FusionComputer1(1193, "fusioncomputer.tier.06", "Fusion Control Computer Mark I")
                        .getStackForm(1L));
        ItemList.FusionComputer_ZPMV.set(
                new GT_MetaTileEntity_FusionComputer2(1194, "fusioncomputer.tier.07", "Fusion Control Computer Mark II")
                        .getStackForm(1L));
        ItemList.FusionComputer_UV.set(
                new GT_MetaTileEntity_FusionComputer3(
                        1195,
                        "fusioncomputer.tier.08",
                        "Fusion Control Computer Mark III").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_Fusion_Coil.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CTC", 'M', ItemList.Casing_Coil_Superconductor, 'C',
                        OrePrefixes.circuit.get(Materials.Master), 'F', ItemList.Field_Generator_MV, 'T',
                        ItemList.Neutron_Reflector });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Plasma_IV.get(1L),
                bitsd,
                new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_LuV, 'F', ItemList.Field_Generator_HV,
                        'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt04.get(Materials.Tungsten), 'U',
                        OrePrefixes.stick.get(Materials.Plutonium241) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Plasma_LuV.get(1L),
                bitsd,
                new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_ZPM, 'F', ItemList.Field_Generator_EV,
                        'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                        OrePrefixes.wireGt04.get(Materials.VanadiumGallium), 'U',
                        OrePrefixes.stick.get(Materials.Europium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Generator_Plasma_ZPMV.get(1L),
                bitsd,
                new Object[] { "UCU", "FMF", aTextWireCoil, 'M', ItemList.Hull_UV, 'F', ItemList.Field_Generator_IV,
                        'C', OrePrefixes.circuit.get(Materials.Ultimate), 'W',
                        OrePrefixes.wireGt04.get(Materials.Naquadah), 'U',
                        OrePrefixes.stick.get(Materials.Americium) });

        ItemList.Processing_Array.set(
                new GT_MetaTileEntity_ProcessingArray(1199, "multimachine.processingarray", "Processing Array")
                        .getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Processing_Array.get(1L),
                bitsd,
                new Object[] { "CTC", "FMF", "CBC", 'M', ItemList.Hull_EV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C',
                        OrePrefixes.circuit.get(Materials.Elite), 'F', ItemList.Robot_Arm_EV, 'T',
                        ItemList.Energy_LapotronicOrb });

        GT_ProcessingArrayRecipeLoader.registerDefaultGregtechMaps();
        ItemList.Distillation_Tower.set(
                new GT_MetaTileEntity_DistillationTower(1126, "multimachine.distillationtower", "Distillation Tower")
                        .getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Distillation_Tower.get(1L),
                bitsd,
                new Object[] { "CBC", "FMF", "CBC", 'M', ItemList.Hull_HV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C',
                        OrePrefixes.circuit.get(Materials.Data), 'F', ItemList.Electric_Pump_HV });

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
                new GT_MetaTileEntity_LargeTurbine_HPSteam(
                        1152,
                        "multimachine.largehpturbine",
                        "Large HP Steam Turbine").getStackForm(1L));
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
                new GT_MetaTileEntity_LargeTurbine_Plasma(
                        1153,
                        "multimachine.largeplasmaturbine",
                        "Large Plasma Generator").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.LargeSteamTurbine.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Advanced),
                        'P', OrePrefixes.gearGt.get(Materials.Steel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.LargeGasTurbine.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C',
                        OrePrefixes.circuit.get(Materials.Data), 'P',
                        OrePrefixes.gearGt.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.LargeAdvancedGasTurbine.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C',
                        OrePrefixes.circuit.get(Materials.Master), 'P', OrePrefixes.gearGt.get(Materials.HSSG) });

        ItemList.Pump_LV
                .set(new GT_MetaTileEntity_Pump(1140, "basicmachine.pump.tier.01", "Basic Pump", 1).getStackForm(1L));
        ItemList.Pump_MV.set(
                new GT_MetaTileEntity_Pump(1141, "basicmachine.pump.tier.02", "Advanced Pump", 2).getStackForm(1L));
        ItemList.Pump_HV.set(
                new GT_MetaTileEntity_Pump(1142, "basicmachine.pump.tier.03", "Advanced Pump II", 3).getStackForm(1L));
        ItemList.Pump_EV.set(
                new GT_MetaTileEntity_Pump(1143, "basicmachine.pump.tier.04", "Advanced Pump III", 4).getStackForm(1L));
        ItemList.Pump_IV.set(
                new GT_MetaTileEntity_Pump(1144, "basicmachine.pump.tier.05", "Advanced Pump IV", 5).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Pump_LV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_LV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.Bronze), 'C', OrePrefixes.circuit.get(Materials.Basic), 'P',
                        ItemList.Electric_Pump_LV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Pump_MV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_MV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.Steel), 'C', OrePrefixes.circuit.get(Materials.Good), 'P',
                        ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Pump_HV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_HV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.StainlessSteel), 'C',
                        OrePrefixes.circuit.get(Materials.Advanced), 'P', ItemList.Electric_Pump_HV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Pump_EV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_EV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.Titanium), 'C', OrePrefixes.circuit.get(Materials.Data),
                        'P', ItemList.Electric_Pump_EV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Pump_IV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextPlateMotor, "BPB", 'M', ItemList.Hull_IV, 'B',
                        OrePrefixes.pipeLarge.get(Materials.TungstenSteel), 'C',
                        OrePrefixes.circuit.get(Materials.Elite), 'P', ItemList.Electric_Pump_IV });

        ItemList.Teleporter.set(
                new GT_MetaTileEntity_Teleporter(1145, "basicmachine.teleporter", "Teleporter", 9).getStackForm(1L));

        ItemList.MobRep_LV.set(
                new GT_MetaTileEntity_MonsterRepellent(
                        1146,
                        "basicmachine.mobrep.tier.01",
                        "Basic Monster Repellator",
                        1).getStackForm(1L));
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

        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_LV.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LV, 'E', ItemList.Emitter_LV.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_MV.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_MV, 'E', ItemList.Emitter_MV.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_HV.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_HV, 'E', ItemList.Emitter_HV.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_EV.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_EV, 'E', ItemList.Emitter_EV.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_IV.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_IV, 'E', ItemList.Emitter_IV.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_LuV.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_LuV, 'E', ItemList.Emitter_LuV.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_ZPM.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_ZPM, 'E', ItemList.Emitter_ZPM.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MobRep_UV.get(1L),
                bitsd,
                new Object[] { "EEE", " M ", "CCC", 'M', ItemList.Hull_UV, 'E', ItemList.Emitter_UV.get(1L), 'C',
                        OrePrefixes.circuit.get(Materials.SuperconductorUHV) });

        ItemList.Machine_Multi_HeatExchanger.set(
                new GT_MetaTileEntity_HeatExchanger(1154, "multimachine.heatexchanger", "Large Heat Exchanger")
                        .getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_HeatExchanger.get(1L),
                bitsd,
                new Object[] { aTextWireCoil, aTextCableHull, aTextWireCoil, 'M', ItemList.Casing_Pipe_Titanium, 'C',
                        OrePrefixes.pipeMedium.get(Materials.Titanium), 'W', ItemList.Electric_Pump_EV });

        ItemList.Charcoal_Pile.set(
                new GT_MetaTileEntity_Charcoal_Pit(1155, "multimachine.charcoalpile", "Charcoal Pile Igniter")
                        .getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Charcoal_Pile.get(1L),
                bitsd,
                new Object[] { "EXE", "EME", "hCw", 'M', ItemList.Hull_HP_Bricks, 'E',
                        OrePrefixes.plate.get(Materials.AnyBronze), 'C', new ItemStack(Items.flint_and_steel, 1), 'X',
                        OrePrefixes.rotor.get(Materials.Steel), });

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
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Seismic_Prospector_Adv_LV.get(1L),
                bits,
                new Object[] { ItemList.Seismic_Prospector_LV });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Seismic_Prospector_Adv_MV.get(1L),
                bits,
                new Object[] { ItemList.Seismic_Prospector_MV });
        GT_ModHandler.addShapelessCraftingRecipe(
                ItemList.Seismic_Prospector_Adv_HV.get(1L),
                bits,
                new Object[] { ItemList.Seismic_Prospector_HV });

        GT_ModHandler.addCraftingRecipe(
                ItemList.Seismic_Prospector_Adv_LV.get(1L),
                bitsd,
                new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_LV, 'W',
                        OrePrefixes.plateDouble.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.Basic),
                        'C', ItemList.Sensor_LV, 'X', OrePrefixes.cableGt02.get(Materials.Tin) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Seismic_Prospector_Adv_MV.get(1L),
                bitsd,
                new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_MV, 'W',
                        OrePrefixes.plateDouble.get(Materials.BlackSteel), 'E', OrePrefixes.circuit.get(Materials.Good),
                        'C', ItemList.Sensor_MV, 'X', OrePrefixes.cableGt02.get(Materials.Copper) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Seismic_Prospector_Adv_HV.get(1L),
                bitsd,
                new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_HV, 'W',
                        OrePrefixes.plateDouble.get(Materials.StainlessSteel), 'E',
                        OrePrefixes.circuit.get(Materials.Advanced), 'C', ItemList.Sensor_HV, 'X',
                        OrePrefixes.cableGt04.get(Materials.Gold) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Seismic_Prospector_Adv_EV.get(1L),
                bitsd,
                new Object[] { "WWW", "EME", "CXC", 'M', ItemList.Hull_EV, 'W',
                        OrePrefixes.plateDouble.get(Materials.VanadiumSteel), 'E',
                        OrePrefixes.circuit.get(Materials.Data), 'C', ItemList.Sensor_EV, 'X',
                        OrePrefixes.cableGt04.get(Materials.Aluminium) });

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
                new GT_MetaTileEntity_ConcreteBackfiller1(
                        143,
                        "multimachine.concretebackfiller1",
                        "Concrete Backfiller").getStackForm(1));
        ItemList.ConcreteBackfiller2.set(
                new GT_MetaTileEntity_ConcreteBackfiller2(
                        144,
                        "multimachine.concretebackfiller3",
                        "Advanced Concrete Backfiller").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                ItemList.ConcreteBackfiller1.get(1L),
                bitsd,
                new Object[] { "WPW", "EME", "CQC", 'M', ItemList.Hull_MV, 'W',
                        OrePrefixes.frameGt.get(Materials.Steel), 'E', OrePrefixes.circuit.get(Materials.Good), 'C',
                        ItemList.Electric_Motor_MV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel), 'Q',
                        ItemList.Electric_Pump_MV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.ConcreteBackfiller2.get(1L),
                bitsd,
                new Object[] { "WPW", "EME", "CQC", 'M', ItemList.ConcreteBackfiller1, 'W',
                        OrePrefixes.frameGt.get(Materials.Titanium), 'E', OrePrefixes.circuit.get(Materials.Data), 'C',
                        ItemList.Electric_Motor_EV, 'P', OrePrefixes.pipeLarge.get(Materials.Steel), 'Q',
                        ItemList.Electric_Pump_EV });

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
        GT_ModHandler.addCraftingRecipe(
                ItemList.PyrolyseOven.get(1L),
                bitsd,
                new Object[] { "WEP", "EME", "WCP", 'M', ItemList.Hull_MV, 'W', ItemList.Electric_Piston_MV, 'P',
                        OrePrefixes.wireGt04.get(Materials.Cupronickel), 'E', OrePrefixes.circuit.get(Materials.Good),
                        'C', ItemList.Electric_Pump_MV });

        ItemList.OilCracker.set(
                new GT_MetaTileEntity_OilCracker(1160, "multimachine.cracker", "Oil Cracking Unit").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                ItemList.OilCracker.get(1L),
                bitsd,
                new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_HV, 'W',
                        ItemList.Casing_Coil_Cupronickel, 'E', OrePrefixes.circuit.get(Materials.Advanced), 'C',
                        ItemList.Electric_Pump_HV });

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
        GT_ModHandler.addCraftingRecipe(
                ItemList.MicroTransmitter_HV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_HV, 'B', ItemList.Battery_RE_HV_Lithium,
                        'C', ItemList.Emitter_HV, 'G', OrePrefixes.circuit.get(Materials.Advanced), 'P',
                        ItemList.Field_Generator_HV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MicroTransmitter_EV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_EV, 'B',
                        GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W), 'C', ItemList.Emitter_EV, 'G',
                        OrePrefixes.circuit.get(Materials.Data), 'P', ItemList.Field_Generator_EV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MicroTransmitter_IV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_IV, 'B', ItemList.Energy_LapotronicOrb,
                        'C', ItemList.Emitter_IV, 'G', OrePrefixes.circuit.get(Materials.Elite), 'P',
                        ItemList.Field_Generator_IV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MicroTransmitter_LUV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_LuV, 'B',
                        ItemList.Energy_LapotronicOrb2, 'C', ItemList.Emitter_LuV, 'G',
                        OrePrefixes.circuit.get(Materials.Master), 'P', ItemList.Field_Generator_LuV });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MicroTransmitter_ZPM.get(1L),
                bitsd,
                new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_ZPM, 'B',
                        GregTech_API.sOPStuff
                                .get(ConfigCategories.Recipes.gregtechrecipes, "EnableZPMandUVBatteries", false)
                                        ? ItemList.Energy_Module
                                        : ItemList.ZPM2,
                        'C', ItemList.Emitter_ZPM, 'G', OrePrefixes.circuit.get(Materials.Ultimate), 'P',
                        ItemList.Field_Generator_ZPM });
        GT_ModHandler.addCraftingRecipe(
                ItemList.MicroTransmitter_UV.get(1L),
                bitsd,
                new Object[] { "CPC", aTextCableHull, "GBG", 'M', ItemList.Hull_UV, 'B',
                        GregTech_API.sOPStuff
                                .get(ConfigCategories.Recipes.gregtechrecipes, "EnableZPMandUVBatteries", false)
                                        ? ItemList.Energy_Module
                                        : ItemList.ZPM3,
                        'C', ItemList.Emitter_UV, 'G', OrePrefixes.circuit.get(Materials.SuperconductorUHV), 'P',
                        ItemList.Field_Generator_UV });

        ItemList.Machine_Multi_Assemblyline.set(
                new GT_MetaTileEntity_AssemblyLine(1170, "multimachine.assemblyline", "Assembling Line")
                        .getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_Assemblyline.get(1L),
                bitsd,
                new Object[] { aTextWireCoil, "EME", aTextWireCoil, 'M', ItemList.Hull_IV, 'W',
                        ItemList.Casing_Assembler, 'E', OrePrefixes.circuit.get(Materials.Elite), 'C',
                        ItemList.Robot_Arm_IV });

        ItemList.Machine_Multi_DieselEngine.set(
                new GT_MetaTileEntity_DieselEngine(1171, "multimachine.dieselengine", "Combustion Engine")
                        .getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_DieselEngine.get(1L),
                bitsd,
                new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                        ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.Elite), 'W',
                        OrePrefixes.cableGt01.get(Materials.TungstenSteel), 'G',
                        OrePrefixes.gearGt.get(Materials.Titanium) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_EngineIntake.get(4L),
                bitsd,
                new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.Titanium), 'F',
                        ItemList.Casing_StableTitanium, 'P', OrePrefixes.rotor.get(Materials.Titanium) });

        ItemList.Machine_Multi_ExtremeDieselEngine.set(
                new GT_MetaTileEntity_ExtremeDieselEngine(
                        2105,
                        "multimachine.extremedieselengine",
                        "Extreme Combustion Engine").getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_ExtremeDieselEngine.get(1L),
                bitsd,
                new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                        ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.Master), 'W',
                        OrePrefixes.cableGt01.get(Materials.HSSG), 'G',
                        OrePrefixes.gearGt.get(Materials.TungstenSteel) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Casing_ExtremeEngineIntake.get(4L),
                bitsd,
                new Object[] { "PhP", "RFR", aTextPlateWrench, 'R', OrePrefixes.pipeMedium.get(Materials.TungstenSteel),
                        'F', ItemList.Casing_RobustTungstenSteel, 'P',
                        OrePrefixes.rotor.get(Materials.TungstenSteel) });

        ItemList.Machine_Multi_Cleanroom.set(
                new GT_MetaTileEntity_Cleanroom(1172, "multimachine.cleanroom", "Cleanroom Controller")
                        .getStackForm(1));
        // If Cleanroom is enabled, add a recipe, else hide from NEI.
        if (GT_Mod.gregtechproxy.mEnableCleanroom) {
            GT_ModHandler.addCraftingRecipe(
                    ItemList.Machine_Multi_Cleanroom.get(1L),
                    bitsd,
                    new Object[] { "FFF", "RHR", "MCM", 'H', ItemList.Hull_HV, 'F', ItemList.Component_Filter, 'R',
                            OrePrefixes.rotor.get(Materials.StainlessSteel), 'M', ItemList.Electric_Motor_HV, 'C',
                            OrePrefixes.circuit.get(Materials.Advanced) });
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Hull_HV.get(1L), ItemList.Component_Filter.get(2L),
                            GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 1L),
                            ItemList.Electric_Motor_HV.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 1L),
                            GT_Utility.getIntegratedCircuit(1) },
                    Materials.StainlessSteel.getMolten(864L),
                    ItemList.Machine_Multi_Cleanroom.get(1L),
                    1200,
                    120);
        } else {
            if (NotEnoughItems.isModLoaded()) {
                API.hideItem(ItemList.Machine_Multi_Cleanroom.get(1L));
            }
        }

        ItemList.Machine_LV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1180,
                        "basicmachine.circuitassembler.tier.01",
                        "Basic Circuit Assembler",
                        1,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_MV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1181,
                        "basicmachine.circuitassembler.tier.02",
                        "Advanced Circuit Assembler",
                        2,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_HV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1182,
                        "basicmachine.circuitassembler.tier.03",
                        "Advanced Circuit Assembler II",
                        3,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_EV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1183,
                        "basicmachine.circuitassembler.tier.04",
                        "Advanced Circuit Assembler III",
                        4,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_IV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1184,
                        "basicmachine.circuitassembler.tier.05",
                        "Advanced Circuit Assembler IV",
                        5,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_LuV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1185,
                        "basicmachine.circuitassembler.tier.06",
                        "Advanced Circuit Assembler V",
                        6,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_ZPM_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1186,
                        "basicmachine.circuitassembler.tier.07",
                        "Advanced Circuit Assembler VI",
                        7,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));
        ItemList.Machine_UV_CircuitAssembler.set(
                new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                        1187,
                        "basicmachine.circuitassembler.tier.08",
                        "Advanced Circuit Assembler VII",
                        8,
                        "Pick-n-Place all over the place",
                        GT_Recipe.GT_Recipe_Map.sCircuitAssemblerRecipes,
                        6,
                        1,
                        16000,
                        0,
                        1,
                        "CircuitAssembler.png",
                        SoundResource.NONE,
                        false,
                        false,
                        SpecialEffects.NONE,
                        "CIRCUITASSEMBLER",
                        new Object[] { "ACE", "VMV", aTextWireCoil, 'M',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL, 'V',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR, 'A',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM, 'C',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT, 'W',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE, 'E',
                                GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER }).getStackForm(1L));

        ItemList.Machine_HV_LightningRod.set(
                new GT_MetaTileEntity_LightningRod(1174, "basicgenerator.lightningrod.03", "Lightning Rod", 3)
                        .getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_HV_LightningRod.get(1L),
                bitsd,
                new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_LuV, 'L', ItemList.Energy_LapotronicOrb, 'T',
                        ItemList.Transformer_ZPM_LuV });
        ItemList.Machine_EV_LightningRod.set(
                new GT_MetaTileEntity_LightningRod(1175, "basicgenerator.lightningrod.04", "Lightning Rod II", 4)
                        .getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_EV_LightningRod.get(1L),
                bitsd,
                new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_ZPM, 'L', ItemList.Energy_LapotronicOrb2, 'T',
                        ItemList.Transformer_UV_ZPM });
        ItemList.Machine_IV_LightningRod.set(
                new GT_MetaTileEntity_LightningRod(1176, "basicgenerator.lightningrod.05", "Lightning Rod III", 5)
                        .getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_IV_LightningRod.get(1L),
                bitsd,
                new Object[] { "LTL", "TMT", "LTL", 'M', ItemList.Hull_UV, 'L', ItemList.ZPM2, 'T',
                        ItemList.Transformer_MAX_UV });

        ItemList.Machine_Multi_LargeChemicalReactor.set(
                new GT_MetaTileEntity_LargeChemicalReactor(
                        1169,
                        "multimachine.chemicalreactor",
                        "Large Chemical Reactor").getStackForm(1));
        GT_ModHandler.addCraftingRecipe(
                ItemList.Machine_Multi_LargeChemicalReactor.get(1L),
                bitsd,
                new Object[] { "CRC", "PMP", "CBC", 'C', OrePrefixes.circuit.get(Materials.Advanced), 'R',
                        OrePrefixes.rotor.get(Materials.StainlessSteel), 'P',
                        OrePrefixes.pipeLarge.get(Materials.Polytetrafluoroethylene), 'M', ItemList.Electric_Motor_HV,
                        'B', ItemList.Hull_HV });

        ItemList.PCBFactory
                .set(new GT_MetaTileEntity_PCBFactory(356, "multimachine.pcbfactory", "PCB Factory").getStackForm(1));
        GT_PCBFactoryMaterialLoader.load();
        ItemList.NanoForge
                .set(new GT_MetaTileEntity_NanoForge(357, "multimachine.nanoforge", "Nano Forge").getStackForm(1));
    }

    private static void run4() {
        long bits = GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED;
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
        makeWires(Materials.SpaceTime, 2606, 0L, 0L, 1_000_000L, GT_Values.V[14], false, true);

        if (!GT_Mod.gregtechproxy.mDisableIC2Cables) {
            GT_ModHandler.addCraftingRecipe(
                    GT_ModHandler.getIC2Item("copperCableItem", 2L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.AnyCopper) });
            GT_ModHandler.addCraftingRecipe(
                    GT_ModHandler.getIC2Item("goldCableItem", 4L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.Gold) });
            GT_ModHandler.addCraftingRecipe(
                    GT_ModHandler.getIC2Item("ironCableItem", 3L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.AnyIron) });
            GT_ModHandler.addCraftingRecipe(
                    GT_ModHandler.getIC2Item("tinCableItem", 3L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                    new Object[] { "xP", 'P', OrePrefixes.plate.get(Materials.Tin) });
        }

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
                new GT_MetaPipeEntity_Fluid(
                        5102,
                        "GT_Pipe_Wood",
                        "Wooden Fluid Pipe",
                        0.5F,
                        Materials.Wood,
                        30,
                        350,
                        false).getStackForm(1L));
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
        generateFluidPipes(Materials.SpaceTime, Materials.SpaceTime.mName, 5300, 250000, 2147483647, true);
        generateFluidMultiPipes(Materials.SpaceTime, Materials.SpaceTime.mName, 5305, 250000, 2147483647, true);
        generateFluidPipes(
                Materials.TranscendentMetal,
                Materials.TranscendentMetal.mName,
                5310,
                220000,
                2147483647,
                true);
        generateFluidMultiPipes(
                Materials.TranscendentMetal,
                Materials.TranscendentMetal.mName,
                5315,
                220000,
                2147483647,
                true);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.TungstenSteel, 1L),
                        ItemList.Electric_Pump_EV.get(1L), GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1L),
                300,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.TungstenSteel, 1L),
                        ItemList.Electric_Pump_IV.get(1L), GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1L),
                400,
                4096);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.TungstenSteel, 1L),
                        ItemList.Electric_Pump_IV.get(2L), GT_Utility.getIntegratedCircuit(5) },
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 1L),
                600,
                7680);

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
                new GT_MetaTileEntity_ChestBuffer(
                        9232,
                        "automation.chestbuffer.tier.02",
                        "Medium Voltage Chest Buffer",
                        2).getStackForm(1L));
        ItemList.Automation_ChestBuffer_HV.set(
                new GT_MetaTileEntity_ChestBuffer(
                        9233,
                        "automation.chestbuffer.tier.03",
                        "High Voltage Chest Buffer",
                        3).getStackForm(1L));
        ItemList.Automation_ChestBuffer_EV.set(
                new GT_MetaTileEntity_ChestBuffer(
                        9234,
                        "automation.chestbuffer.tier.04",
                        "Extreme Voltage Chest Buffer",
                        4).getStackForm(1L));
        ItemList.Automation_ChestBuffer_IV.set(
                new GT_MetaTileEntity_ChestBuffer(
                        9235,
                        "automation.chestbuffer.tier.05",
                        "Insane Voltage Chest Buffer",
                        5).getStackForm(1L));
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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_ULV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Primitive) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_LV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_MV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Good) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_HV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Advanced) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_EV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Data) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_IV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Elite) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_LuV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Master) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_ZPM.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Ultimate) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_UV.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.SuperconductorUHV) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ChestBuffer_MAX.get(1L),
                bits,
                new Object[] { "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Infinite) });

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
                new GT_MetaTileEntity_Filter(
                        9249,
                        "automation.filter.tier.09",
                        "Highly Ultimate Voltage Item Filter",
                        9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_ULV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_LV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_MV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_HV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_EV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_IV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_LuV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_ZPM.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_UV.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Filter_MAX.get(1L),
                bits,
                new Object[] { " F ", "CMV", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });

        ItemList.Automation_TypeFilter_ULV.set(
                new GT_MetaTileEntity_TypeFilter(
                        9250,
                        "automation.typefilter.tier.00",
                        "Ultra Low Voltage Type Filter",
                        0).getStackForm(1L));
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
                new GT_MetaTileEntity_TypeFilter(
                        9254,
                        "automation.typefilter.tier.04",
                        "Extreme Voltage Type Filter",
                        4).getStackForm(1L));
        ItemList.Automation_TypeFilter_IV.set(
                new GT_MetaTileEntity_TypeFilter(9255, "automation.typefilter.tier.05", "Insane Voltage Type Filter", 5)
                        .getStackForm(1L));
        ItemList.Automation_TypeFilter_LuV.set(
                new GT_MetaTileEntity_TypeFilter(
                        9256,
                        "automation.typefilter.tier.06",
                        "Ludicrous Voltage Type Filter",
                        6).getStackForm(1L));
        ItemList.Automation_TypeFilter_ZPM.set(
                new GT_MetaTileEntity_TypeFilter(9257, "automation.typefilter.tier.07", "ZPM Voltage Type Filter", 7)
                        .getStackForm(1L));
        ItemList.Automation_TypeFilter_UV.set(
                new GT_MetaTileEntity_TypeFilter(
                        9258,
                        "automation.typefilter.tier.08",
                        "Ultimate Voltage Type Filter",
                        8).getStackForm(1L));
        ItemList.Automation_TypeFilter_MAX.set(
                new GT_MetaTileEntity_TypeFilter(
                        9259,
                        "automation.typefilter.tier.09",
                        "Highly Ultimate Voltage Type Filter",
                        9).getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_ULV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_LV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_MV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_HV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_EV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_IV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_LuV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_ZPM.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_UV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_TypeFilter_MAX.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_ULV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_LV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_MV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_HV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_EV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_IV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_LuV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_ZPM.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_UV.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_Regulator_MAX.get(1L),
                bits,
                new Object[] { "XFX", "VMV", "XCX", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UHV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });

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
                new GT_MetaTileEntity_SuperBuffer(
                        9302,
                        "automation.superbuffer.tier.02",
                        "Medium Voltage Super Buffer",
                        2).getStackForm(1L));
        ItemList.Automation_SuperBuffer_HV.set(
                new GT_MetaTileEntity_SuperBuffer(
                        9303,
                        "automation.superbuffer.tier.03",
                        "High Voltage Super Buffer",
                        3).getStackForm(1L));
        ItemList.Automation_SuperBuffer_EV.set(
                new GT_MetaTileEntity_SuperBuffer(
                        9304,
                        "automation.superbuffer.tier.04",
                        "Extreme Voltage Super Buffer",
                        4).getStackForm(1L));
        ItemList.Automation_SuperBuffer_IV.set(
                new GT_MetaTileEntity_SuperBuffer(
                        9305,
                        "automation.superbuffer.tier.05",
                        "Insane Voltage Super Buffer",
                        5).getStackForm(1L));
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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_ULV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_LV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_MV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_HV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_EV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_IV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_LuV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_ZPM.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_UV.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                        ItemList.Tool_DataOrb });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_MAX.get(1L),
                bits,
                new Object[] { "DMV", 'M', ItemList.Automation_ChestBuffer_MAX, 'V', ItemList.Conveyor_Module_UHV, 'D',
                        ItemList.Tool_DataOrb });

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_ULV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_LV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_MV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_HV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_EV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_IV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_LuV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_ZPM.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_UV.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'D',
                        ItemList.Tool_DataStick });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_SuperBuffer_MAX.get(1L),
                bits,
                new Object[] { "DMV", "DDD", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'D',
                        ItemList.Tool_DataStick });

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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_ULV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ULV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_LV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LV, 'V', ItemList.Conveyor_Module_LV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_MV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MV, 'V', ItemList.Conveyor_Module_MV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_HV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_HV, 'V', ItemList.Conveyor_Module_HV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_EV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_EV, 'V', ItemList.Conveyor_Module_EV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_IV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_IV, 'V', ItemList.Conveyor_Module_IV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_LuV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_LuV, 'V', ItemList.Conveyor_Module_LuV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_ZPM.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Conveyor_Module_ZPM, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_UV.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_UV, 'V', ItemList.Conveyor_Module_UV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_ItemDistributor_MAX.get(1L),
                bits,
                new Object[] { "XCX", "VMV", " V ", 'M', ItemList.Hull_MAX, 'V', ItemList.Conveyor_Module_UHV, 'C',
                        OreDictNames.craftingChest, 'X', OrePrefixes.circuit.get(Materials.Basic) });

        ItemList.Automation_RecipeFilter_ULV.set(
                new GT_MetaTileEntity_RecipeFilter(
                        9330,
                        "automation.recipefilter.tier.00",
                        "Ultra Low Voltage Recipe Filter",
                        0).getStackForm(1L));
        ItemList.Automation_RecipeFilter_LV.set(
                new GT_MetaTileEntity_RecipeFilter(
                        9331,
                        "automation.recipefilter.tier.01",
                        "Low Voltage Recipe Filter",
                        1).getStackForm(1L));
        ItemList.Automation_RecipeFilter_MV.set(
                new GT_MetaTileEntity_RecipeFilter(
                        9332,
                        "automation.recipefilter.tier.02",
                        "Medium Voltage Recipe Filter",
                        2).getStackForm(1L));
        ItemList.Automation_RecipeFilter_HV.set(
                new GT_MetaTileEntity_RecipeFilter(
                        9333,
                        "automation.recipefilter.tier.03",
                        "High Voltage Recipe Filter",
                        3).getStackForm(1L));
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
                new GT_MetaTileEntity_RecipeFilter(
                        9337,
                        "automation.recipefilter.tier.07",
                        "ZPM Voltage Recipe Filter",
                        7).getStackForm(1L));
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

        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_ULV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ULV, 'V', ItemList.Robot_Arm_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_LV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LV, 'V', ItemList.Robot_Arm_LV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_MV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MV, 'V', ItemList.Robot_Arm_MV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_HV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_HV, 'V', ItemList.Robot_Arm_HV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_EV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_EV, 'V', ItemList.Robot_Arm_EV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_IV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_IV, 'V', ItemList.Robot_Arm_IV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_LuV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_LuV, 'V', ItemList.Robot_Arm_LuV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_ZPM.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_ZPM, 'V', ItemList.Robot_Arm_ZPM, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_UV.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_UV, 'V', ItemList.Robot_Arm_UV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
        GT_ModHandler.addCraftingRecipe(
                ItemList.Automation_RecipeFilter_MAX.get(1L),
                bits,
                new Object[] { " F ", "VMC", " X ", 'M', ItemList.Hull_MAX, 'V', ItemList.Robot_Arm_UHV, 'C',
                        OreDictNames.craftingChest, 'F', OreDictNames.craftingFilter, 'X',
                        OrePrefixes.circuit.get(Materials.Basic) });
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
