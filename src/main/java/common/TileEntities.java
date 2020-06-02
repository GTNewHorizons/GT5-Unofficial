package common;

import common.tileentities.*;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {

    public static GTMTE_SOFuelCellMK1 sofc1;
    public static GTMTE_SOFuelCellMK2 sofc2;
    public static GTMTE_ModularNuclearReactor mdr;
    public static GTMTE_FluidMultiStorage fms;
    public static GTMTE_ItemServer is;
    public static GTMTE_LapotronicSuperCapacitor lsc;
    public static GTMTE_SpaceElevator se;
    
    public static void preInit() {
        GameRegistry.registerTileEntity(TE_TFFTMultiHatch.class, "kekztech_tfftmultihatch_tile");
        //GameRegistry.registerTileEntity(TE_ItemServerIOPort.class, "kekztech_itemserverioport_tile");
        GameRegistry.registerTileEntity(TE_ItemProxyCable.class, "kekztech_itemproxycable_tile");
        GameRegistry.registerTileEntity(TE_ItemProxySource.class, "kekztech_itemproxysource_tile");
        GameRegistry.registerTileEntity(TE_ItemProxyEndpoint.class, "kekztech_itemproxyendpoint_tile");
        GameRegistry.registerTileEntity(TE_ThaumiumReinforcedJar.class, "kekztech_thaumiumreinforcedjar");
        GameRegistry.registerTileEntity(TE_IchorJar.class, "kekztech_ichorjar");
    }
    
    public static void init() {
        sofc1 = new GTMTE_SOFuelCellMK1(13101, "multimachine.fuelcellmk1", "Solid-Oxide Fuel Cell Mk I");
        sofc2 = new GTMTE_SOFuelCellMK2(13102, "multimachine.fuelcellmk2", "Solid-Oxide Fuel Cell Mk II");
        mdr = new GTMTE_ModularNuclearReactor(13103, "multimachine.nuclearreactor", "Nuclear Reactor");
        fms = new GTMTE_FluidMultiStorage(13104, "multimachine.tf_fluidtank", "T.F.F.T");
        //is = new GTMTE_ItemServer(13105, "multimachine.itemserver", "Item Server");
        lsc = new GTMTE_LapotronicSuperCapacitor(13106, "multimachine.supercapacitor", "Lapotronic Supercapacitor");
        //se = new GTMTE_SpaceElevator(13107, "multimachine.spaceelevator", "Space Elevator");
    }

}
