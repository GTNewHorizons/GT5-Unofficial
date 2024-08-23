package common;

import net.minecraft.block.Block;

import common.blocks.Block_GDCUnit;
import common.blocks.Block_IchorJar;
import common.blocks.Block_LapotronicEnergyUnit;
import common.blocks.Block_LargeHexPlate;
import common.blocks.Block_TFFTStorageField;
import common.blocks.Block_ThaumiumReinforcedJar;
import common.blocks.Block_YSZUnit;

import gregtech.api.enums.Mods;
import kekztech.KekzCore;

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
        if (Mods.Thaumcraft.isModLoaded()) {
            registerBlocks_Jars();
        }
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
