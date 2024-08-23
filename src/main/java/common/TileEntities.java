package common;

import common.tileentities.GTMTE_LapotronicSuperCapacitor;
import common.tileentities.GTMTE_SOFuelCellMK1;
import common.tileentities.GTMTE_SOFuelCellMK2;
import common.tileentities.GTMTE_TFFT;
import common.tileentities.GTMTE_TFFTHatch;
import common.tileentities.TE_IchorJar;
import common.tileentities.TE_IchorVoidJar;
import common.tileentities.TE_ThaumiumReinforcedJar;
import common.tileentities.TE_ThaumiumReinforcedVoidJar;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.MetaTileEntityIDs;
import gregtech.api.enums.Mods;

public class TileEntities {

    // Multiblock controllers
    public static GTMTE_SOFuelCellMK1 sofc1;
    public static GTMTE_SOFuelCellMK2 sofc2;
    public static GTMTE_TFFT tfft;
    public static GTMTE_LapotronicSuperCapacitor lsc;

    // Singleblocks
    public static GTMTE_TFFTHatch tfftHatch;

    public static void preInit() {
        if (Mods.Thaumcraft.isModLoaded()) {
            GameRegistry.registerTileEntity(TE_ThaumiumReinforcedJar.class, "kekztech_thaumiumreinforcedjar");
            GameRegistry.registerTileEntity(TE_ThaumiumReinforcedVoidJar.class, "kekztech_thaumiumreinforcedvoidjar");
            GameRegistry.registerTileEntity(TE_IchorJar.class, "kekztech_ichorjar");
            GameRegistry.registerTileEntity(TE_IchorVoidJar.class, "kekztech_ichorvoidjar");
        }
    }

    public static void init() {
        // Multiblock controllers
        sofc1 = new GTMTE_SOFuelCellMK1(
            MetaTileEntityIDs.sofc1.ID,
            "multimachine.fuelcellmk1",
            "Solid-Oxide Fuel Cell Mk I");
        sofc2 = new GTMTE_SOFuelCellMK2(
            MetaTileEntityIDs.sofc2.ID,
            "multimachine.fuelcellmk2",
            "Solid-Oxide Fuel Cell Mk II");
        tfft = new GTMTE_TFFT(MetaTileEntityIDs.tfft.ID, "multimachine.tfft", "T.F.F.T");
        lsc = new GTMTE_LapotronicSuperCapacitor(
            MetaTileEntityIDs.lsc.ID,
            "multimachine.supercapacitor",
            "Lapotronic Supercapacitor");

        // Singleblocks
        tfftHatch = new GTMTE_TFFTHatch(MetaTileEntityIDs.tfftHatch.ID, "machine.tffthatch", "T.F.F.T Multi I/O Hatch");
    }
}
