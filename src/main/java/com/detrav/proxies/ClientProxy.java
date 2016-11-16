package com.detrav.proxies;

import com.detrav.DetravScannerMod;
import com.detrav.enums.Textures01;
import com.detrav.events.DetravDrawBlockHighlightEventHandler;
import com.detrav.events.DetravKeyHandler;
import com.detrav.gui.DetravGuiProPick;
import gregtech.api.enums.Textures;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class ClientProxy extends CommonProxy {

    public ClientProxy()
    {
        int test = Textures01.mTextures.length;
    }

    @Override
    public void onPostLoad() {
        super.onPostLoad();
        DetravDrawBlockHighlightEventHandler.register();
        //Textures.ItemIcons.CustomIcon test = new Textures.ItemIcons.CustomIcon("iconsets/PRO_PICK_HEAD");
        //test.run();

    }
    @Override
    public void onLoad()
    {
        super.onLoad();
    }

    public void openProPickGui()
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        player.openGui(DetravScannerMod.instance, DetravGuiProPick.GUI_ID,player.worldObj,(int)player.posX,(int)player.posY,(int)player.posZ);
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("NetworkTested: " + Integer.toString(level)));
    }
    @Override
    public void onPreInit()
    {
        super.onPreInit();
        DetravKeyHandler.register();
    }

    @Override
    public void sendPlayerExeption(String s) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("DetravScannerMod: " + s);
    }
}
