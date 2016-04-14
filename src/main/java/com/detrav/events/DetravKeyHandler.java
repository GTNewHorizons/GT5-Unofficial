package com.detrav.events;

import com.detrav.net.DetravModeSwitchPacket02;
import com.detrav.net.DetravNetwork;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

/**
 * Created by wital_000 on 14.04.2016.
 */
public class DetravKeyHandler {
    public static KeyBinding modeSwitchKey;

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(modeSwitchKey.isPressed())
        {
            DetravNetwork.INSTANCE.sendToServer(new DetravModeSwitchPacket02(Minecraft.getMinecraft().thePlayer));
            //Minecraft.getMinecraft().thePlayer.getEntityData().
        }
    }

    static boolean inited = false;
    public static void register()
    {
        if(!inited) {
            inited = true;
            modeSwitchKey = new KeyBinding("key.detrav.modeSwitch", Keyboard.KEY_GRAVE,"key.categories.misc");
            ClientRegistry.registerKeyBinding(modeSwitchKey);
            DetravKeyHandler handler = new DetravKeyHandler();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }

    }
}
