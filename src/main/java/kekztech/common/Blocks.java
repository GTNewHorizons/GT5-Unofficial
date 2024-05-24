package kekztech.common;

import net.minecraft.block.Block;

import kekztech.common.blocks.Block_GDCUnit;
import kekztech.common.blocks.Block_IchorJar;
import kekztech.common.blocks.Block_LapotronicEnergyUnit;
import kekztech.common.blocks.Block_LargeHexPlate;
import kekztech.common.blocks.Block_TFFTStorageField;
import kekztech.common.blocks.Block_ThaumiumReinforcedJar;
import kekztech.common.blocks.Block_YSZUnit;
import kekztech.core.KekzCore;

public class Blocks {

    public static Block yszUnit;
    public static Block gdcUnit;
    public static Block tfftStorageField;
    public static Block jarThaumiumReinforced;
    public static Block jarIchor;
    public static Block lscLapotronicEnergyUnit;

    public static Block largeHexPlate;

    public static void preInit() {
        KekzCore.LOGGER.info("Registering blocks...");

        registerBlocks_SOFC();
        registerBlocks_TFFT();
        registerBlocks_Jars();
        registerBlocks_LSC();
        registerBlocks_Cosmetics();

        KekzCore.LOGGER.info("Finished registering blocks");
    }

    private static void registerBlocks_SOFC() {
        yszUnit = Block_YSZUnit.registerBlock();
        gdcUnit = Block_GDCUnit.registerBlock();
    }

    private static void registerBlocks_TFFT() {
        tfftStorageField = Block_TFFTStorageField.registerBlock();
    }

    private static void registerBlocks_Jars() {
        jarThaumiumReinforced = Block_ThaumiumReinforcedJar.registerBlock();
        jarIchor = Block_IchorJar.registerBlock();
    }

    private static void registerBlocks_LSC() {
        lscLapotronicEnergyUnit = Block_LapotronicEnergyUnit.registerBlock();
    }

    private static void registerBlocks_Cosmetics() {
        largeHexPlate = Block_LargeHexPlate.registerBlock();
    }
}
