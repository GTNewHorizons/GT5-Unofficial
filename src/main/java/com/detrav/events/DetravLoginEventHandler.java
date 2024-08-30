package com.detrav.events;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;

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
            FMLCommonHandler.instance()
                .bus()
                .register(handler);
        }
    }
}
