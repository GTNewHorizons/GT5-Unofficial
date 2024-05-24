package kekztech.common;

import cpw.mods.fml.common.registry.GameRegistry;
import kekztech.common.tileentities.GTMTE_LapotronicSuperCapacitor;
import kekztech.common.tileentities.GTMTE_SOFuelCellMK1;
import kekztech.common.tileentities.GTMTE_SOFuelCellMK2;
import kekztech.common.tileentities.GTMTE_TFFT;
import kekztech.common.tileentities.GTMTE_TFFTHatch;
import kekztech.common.tileentities.TE_IchorJar;
import kekztech.common.tileentities.TE_IchorVoidJar;
import kekztech.common.tileentities.TE_ThaumiumReinforcedJar;
import kekztech.common.tileentities.TE_ThaumiumReinforcedVoidJar;

public class TileEntities {

    // Multiblock controllers
    public static GTMTE_SOFuelCellMK1 sofc1;
    public static GTMTE_SOFuelCellMK2 sofc2;
    public static GTMTE_TFFT tfft;
    public static GTMTE_LapotronicSuperCapacitor lsc;

    // Singleblocks
    public static GTMTE_TFFTHatch tfftHatch;

    public static void preInit() {
        GameRegistry.registerTileEntity(TE_ThaumiumReinforcedJar.class, "kekztech_thaumiumreinforcedjar");
        GameRegistry.registerTileEntity(TE_ThaumiumReinforcedVoidJar.class, "kekztech_thaumiumreinforcedvoidjar");
        GameRegistry.registerTileEntity(TE_IchorJar.class, "kekztech_ichorjar");
        GameRegistry.registerTileEntity(TE_IchorVoidJar.class, "kekztech_ichorvoidjar");
    }

    public static void init() {
        // Multiblock controllers
        sofc1 = new GTMTE_SOFuelCellMK1(13101, "multimachine.fuelcellmk1", "Solid-Oxide Fuel Cell Mk I");
        sofc2 = new GTMTE_SOFuelCellMK2(13102, "multimachine.fuelcellmk2", "Solid-Oxide Fuel Cell Mk II");
        tfft = new GTMTE_TFFT(13104, "multimachine.tfft", "T.F.F.T");
        lsc = new GTMTE_LapotronicSuperCapacitor(13106, "multimachine.supercapacitor", "Lapotronic Supercapacitor");

        // Singleblocks
        tfftHatch = new GTMTE_TFFTHatch(13109, "machine.tffthatch", "T.F.F.T Multi I/O Hatch");
    }
}
