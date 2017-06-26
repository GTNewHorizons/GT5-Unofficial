package com.detrav.events;

import com.detrav.net.DetravNetwork;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

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
