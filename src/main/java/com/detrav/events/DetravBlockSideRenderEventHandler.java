package com.detrav.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.event.RenderWorldLastEvent;

/**
 * Created by wital_000 on 18.04.2016.
 */
public class DetravBlockSideRenderEventHandler {

    public static long modeBlockBreak = 0L;

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent ev) {
        //need to draw large borders
    }


        static boolean inited = false;

    public static void register() {
        if (!inited) {
            inited = true;
            DetravBlockSideRenderEventHandler handler = new DetravBlockSideRenderEventHandler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }
}
