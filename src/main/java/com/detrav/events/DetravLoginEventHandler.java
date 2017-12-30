package com.detrav.events;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by wital_000 on 18.04.2016.
 */
public class DetravLoginEventHandler {

    static boolean inited = false;

    public static void register() {
        if (!inited) {
            inited = true;
            DetravLoginEventHandler handler = new DetravLoginEventHandler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }
}
