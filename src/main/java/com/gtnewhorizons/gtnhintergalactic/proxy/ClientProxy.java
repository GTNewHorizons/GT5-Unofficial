package com.gtnewhorizons.gtnhintergalactic.proxy;

import net.minecraftforge.common.MinecraftForge;

import com.gtnewhorizons.gtnhintergalactic.block.BlockSpaceElevatorCable;
import com.gtnewhorizons.gtnhintergalactic.client.IGTextures;
import com.gtnewhorizons.gtnhintergalactic.client.TooltipUtil;
import com.gtnewhorizons.gtnhintergalactic.nei.NEI_IG_Config;
import com.gtnewhorizons.gtnhintergalactic.render.RenderSpaceElevatorCable;
import com.gtnewhorizons.gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

/**
 * Proxy used by the client to load stuff
 *
 * @author minecraft7771
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        TooltipUtil.postInit();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        BlockSpaceElevatorCable.setRenderID(RenderingRegistry.getNextAvailableRenderId());
        RenderingRegistry.registerBlockHandler(BlockSpaceElevatorCable.getRenderID(), new RenderSpaceElevatorCable());
        ClientRegistry
                .bindTileEntitySpecialRenderer(TileEntitySpaceElevatorCable.class, new RenderSpaceElevatorCable());
        new IGTextures().run();
        MinecraftForge.EVENT_BUS.register(new NEI_IG_Config());
    }
}
