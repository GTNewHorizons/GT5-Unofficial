package kekztech.common;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.Mods;
import kekztech.common.tileentities.MTEHatchTFFT;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;
import kekztech.common.tileentities.MTESOFuelCellMK1;
import kekztech.common.tileentities.MTESOFuelCellMK2;
import kekztech.common.tileentities.MTETankTFFT;
import kekztech.common.tileentities.TileEntityIchorJar;
import kekztech.common.tileentities.TileEntityIchorVoidJar;
import kekztech.common.tileentities.TileEntityThaumiumReinforcedJar;
import kekztech.common.tileentities.TileEntityThaumiumReinforcedVoidJar;

public class TileEntities {

    // Multiblock controllers
    public static MTESOFuelCellMK1 sofc1;
    public static MTESOFuelCellMK2 sofc2;
    public static MTETankTFFT tfft;
    public static MTELapotronicSuperCapacitor lsc;

    // Singleblocks
    public static MTEHatchTFFT tfftHatch;

    public static void preInit() {
        if (Mods.Thaumcraft.isModLoaded()) {
            GameRegistry.registerTileEntity(TileEntityThaumiumReinforcedJar.class, "kekztech_thaumiumreinforcedjar");
            GameRegistry
                .registerTileEntity(TileEntityThaumiumReinforcedVoidJar.class, "kekztech_thaumiumreinforcedvoidjar");
            GameRegistry.registerTileEntity(TileEntityIchorJar.class, "kekztech_ichorjar");
            GameRegistry.registerTileEntity(TileEntityIchorVoidJar.class, "kekztech_ichorvoidjar");
        }
    }

    public static void init() {
        // Multiblock controllers
        sofc1 = new MTESOFuelCellMK1(
            MetaTileEntityIDs.sofc1.ID,
            "multimachine.fuelcellmk1",
            "Solid-Oxide Fuel Cell Mk I");
        sofc2 = new MTESOFuelCellMK2(
            MetaTileEntityIDs.sofc2.ID,
            "multimachine.fuelcellmk2",
            "Solid-Oxide Fuel Cell Mk II");
        tfft = new MTETankTFFT(MetaTileEntityIDs.tfft.ID, "multimachine.tfft", "T.F.F.T");
        lsc = new MTELapotronicSuperCapacitor(
            MetaTileEntityIDs.lsc.ID,
            "multimachine.supercapacitor",
            "Lapotronic Supercapacitor");

        // Singleblocks
        tfftHatch = new MTEHatchTFFT(MetaTileEntityIDs.tfftHatch.ID, "machine.tffthatch", "T.F.F.T Multi I/O Hatch");
    }
}
