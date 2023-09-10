package client;

import common.CommonProxy;
import common.tileentities.TE_SpaceElevatorCapacitor;
import common.tileentities.TE_SpaceElevatorTether;

import client.renderer.TESR_SECapacitor;
import client.renderer.TESR_SETether;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(final FMLPreInitializationEvent e) {
        super.preInit(e);
        // Register TESR
        ClientRegistry.bindTileEntitySpecialRenderer(TE_SpaceElevatorTether.class, new TESR_SETether());
        ClientRegistry.bindTileEntitySpecialRenderer(TE_SpaceElevatorCapacitor.class, new TESR_SECapacitor());
    }

    @Override
    public void init(final FMLInitializationEvent e) {
        super.init(e);
    }
}
