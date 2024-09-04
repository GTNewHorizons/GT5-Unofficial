package kekztech.common;

import net.minecraft.block.Block;

import gregtech.api.enums.Mods;
import kekztech.KekzCore;
import kekztech.common.blocks.BlockGDCUnit;
import kekztech.common.blocks.BlockIchorJar;
import kekztech.common.blocks.BlockLapotronicEnergyUnit;
import kekztech.common.blocks.BlockLargeHexPlate;
import kekztech.common.blocks.BlockTFFTStorageField;
import kekztech.common.blocks.BlockThaumiumReinforcedJar;
import kekztech.common.blocks.BlockYSZUnit;

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
        yszUnit = BlockYSZUnit.registerBlock();
        gdcUnit = BlockGDCUnit.registerBlock();
    }

    private static void registerBlocks_TFFT() {
        tfftStorageField = BlockTFFTStorageField.registerBlock();
    }

    private static void registerBlocks_Jars() {
        jarThaumiumReinforced = BlockThaumiumReinforcedJar.registerBlock();
        jarIchor = BlockIchorJar.registerBlock();
    }

    private static void registerBlocks_LSC() {
        lscLapotronicEnergyUnit = BlockLapotronicEnergyUnit.registerBlock();
    }

    private static void registerBlocks_Cosmetics() {
        largeHexPlate = BlockLargeHexPlate.registerBlock();
    }
}
