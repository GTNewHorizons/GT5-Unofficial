package common;

import common.tileentities.*;
import common.tileentities.GTMTE_TFFT;
import common.tileentities.GTMTE_TFFTHatch;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {

    // Multiblock controllers
    public static GTMTE_SOFuelCellMK1 sofc1;
    public static GTMTE_SOFuelCellMK2 sofc2;
    // public static GTMTE_ModularNuclearReactor mdr;
    public static GTMTE_TFFT tfft;
    public static GTMTE_LapotronicSuperCapacitor lsc;
    public static GTMTE_SpaceElevator se;

    // Singleblocks
    public static GTMTE_TFFTHatch tfftHatch;

    public static void preInit() {
        // GameRegistry.registerTileEntity(TE_ItemProxyCable.class, "kekztech_itemproxycable_tile");
        // GameRegistry.registerTileEntity(TE_ItemProxySource.class, "kekztech_itemproxysource_tile");
        // GameRegistry.registerTileEntity(TE_ItemProxyEndpoint.class, "kekztech_itemproxyendpoint_tile");
        GameRegistry.registerTileEntity(TE_ThaumiumReinforcedJar.class, "kekztech_thaumiumreinforcedjar");
        GameRegistry.registerTileEntity(TE_ThaumiumReinforcedVoidJar.class, "kekztech_thaumiumreinforcedvoidjar");
        GameRegistry.registerTileEntity(TE_IchorJar.class, "kekztech_ichorjar");
        GameRegistry.registerTileEntity(TE_IchorVoidJar.class, "kekztech_ichorvoidjar");
        // GameRegistry.registerTileEntity(TE_SpaceElevatorCapacitor.class, "kekztech_secapacitor");
        // GameRegistry.registerTileEntity(TE_BeamTransmitter.class, "kekztech_beamtransmitter");
    }

    public static void init() {
        // Multiblock controllers
        sofc1 = new GTMTE_SOFuelCellMK1(13101, "multimachine.fuelcellmk1", "Solid-Oxide Fuel Cell Mk I");
        sofc2 = new GTMTE_SOFuelCellMK2(13102, "multimachine.fuelcellmk2", "Solid-Oxide Fuel Cell Mk II");
        // mdr = new GTMTE_ModularNuclearReactor(13103, "multimachine.nuclearreactor", "Nuclear Reactor");
        tfft = new GTMTE_TFFT(13104, "multimachine.tfft", "T.F.F.T");
        lsc = new GTMTE_LapotronicSuperCapacitor(13106, "multimachine.supercapacitor", "Lapotronic Supercapacitor");
        // se = new GTMTE_SpaceElevator(13107, "multimachine.spaceelevator", "Space Elevator");

        // Singleblocks
        tfftHatch = new GTMTE_TFFTHatch(13109, "machine.tffthatch", "T.F.F.T Multi I/O Hatch");
    }
}
