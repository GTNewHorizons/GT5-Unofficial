package com.detrav.events;

import com.detrav.net.DetravModePacket03;
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
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if(event.isCanceled()) return;
        if(!event.world.isRemote)
        if(event.entity instanceof EntityPlayerMP)
        {
            DetravNetwork.INSTANCE.sendToPlayer(new DetravModePacket03((EntityPlayerMP)event.entity),(EntityPlayerMP)event.entity);
            DetravLevelUpEvent.UpdateHealthAttribute((EntityPlayer) event.entity);
        }
        //if(Minecraft.getMinecraft().thePlayer!=null)
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(""+(event.entity instanceof EntityPlayerMP)+" | " + event.world.isRemote));
    }


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
