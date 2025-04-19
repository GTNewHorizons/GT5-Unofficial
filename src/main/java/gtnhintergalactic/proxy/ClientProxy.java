package gtnhintergalactic.proxy;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import gtnhintergalactic.block.BlockSpaceElevatorCable;
import gtnhintergalactic.client.IGTextures;
import gtnhintergalactic.client.TooltipUtil;
import gtnhintergalactic.nei.NEI_IG_Config;
import gtnhintergalactic.render.RenderSpaceElevatorCable;
import gtnhintergalactic.tile.TileEntitySpaceElevatorCable;

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
