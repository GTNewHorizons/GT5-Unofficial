package goodgenerator.client;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import goodgenerator.client.render.AntimatterRenderer;
import goodgenerator.common.CommonProxy;
import gregtech.common.render.RenderInit;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);

        new AntimatterRenderer();
        RenderInit.onResourceReload(AntimatterRenderer::reload);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

}
