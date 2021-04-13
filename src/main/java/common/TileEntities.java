package common;

import common.tileentities.*;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {

    // Multiblock controllers
    public static GTMTE_SOFuelCellMK1 sofc1;
    public static GTMTE_SOFuelCellMK2 sofc2;
    //public static GTMTE_ModularNuclearReactor mdr;
    public static GTMTE_FluidMultiStorage fms;
    public static GTMTE_LapotronicSuperCapacitor lsc;
    public static GTMTE_SpaceElevator se;
    // Singleblocks
    public static GTMTE_TFFTMultiHatch mhHV;
    public static GTMTE_TFFTMultiHatch mhIV;
    public static GTMTE_TFFTMultiHatch mhZPM;

    public static void preInit() {
        GameRegistry.registerTileEntity(TE_TFFTMultiHatch.class, "kekztech_tfftmultihatch_tile");
        //GameRegistry.registerTileEntity(TE_ItemProxyCable.class, "kekztech_itemproxycable_tile");
        //GameRegistry.registerTileEntity(TE_ItemProxySource.class, "kekztech_itemproxysource_tile");
        //GameRegistry.registerTileEntity(TE_ItemProxyEndpoint.class, "kekztech_itemproxyendpoint_tile");
        GameRegistry.registerTileEntity(TE_ThaumiumReinforcedJar.class, "kekztech_thaumiumreinforcedjar");
        GameRegistry.registerTileEntity(TE_IchorJar.class, "kekztech_ichorjar");
        //GameRegistry.registerTileEntity(TE_SpaceElevatorCapacitor.class, "kekztech_secapacitor");
        //GameRegistry.registerTileEntity(TE_BeamTransmitter.class, "kekztech_beamtransmitter");
    }
    
    public static void init() {
        // Multiblock controllers
        sofc1 = new GTMTE_SOFuelCellMK1(13101, "multimachine.fuelcellmk1", "Solid-Oxide Fuel Cell Mk I");
        sofc2 = new GTMTE_SOFuelCellMK2(13102, "multimachine.fuelcellmk2", "Solid-Oxide Fuel Cell Mk II");
        //mdr = new GTMTE_ModularNuclearReactor(13103, "multimachine.nuclearreactor", "Nuclear Reactor");
        fms = new GTMTE_FluidMultiStorage(13104, "multimachine.tf_fluidtank", "T.F.F.T");
        lsc = new GTMTE_LapotronicSuperCapacitor(13106, "multimachine.supercapacitor", "Lapotronic Supercapacitor");
        //se = new GTMTE_SpaceElevator(13107, "multimachine.spaceelevator", "Space Elevator");
        // Singleblocks
        mhHV = new GTMTE_TFFTMultiHatch(13108, "machine.multihatch.0", "T.F.F.T Multi I/O Hatch [HV]", 3);
        mhIV = new GTMTE_TFFTMultiHatch(13109, "machine.multihatch.1", "T.F.F.T Multi I/O Hatch [IV]", 5);
        mhZPM = new GTMTE_TFFTMultiHatch(13110, "machine.multihatch.2", "T.F.F.T Multi I/O Hatch [ZPM]", 7);
    }

}
