package detrav.proxies;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import detrav.DetravScannerMod;
import detrav.enums.Textures01;
import detrav.gui.DetravScannerGUI;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        int test = Textures01.mTextures.length;
    }

    @Override
    public void onPostLoad() {
        super.onPostLoad();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public void openProspectorGUI() {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        player.openGui(
            DetravScannerMod.instance,
            DetravScannerGUI.GUI_ID,
            player.worldObj,
            (int) player.posX,
            (int) player.posY,
            (int) player.posZ);
    }

    @Override
    public void onPreInit() {
        super.onPreInit();
    }

    @Override
    public void sendPlayerExeption(String s) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage("DetravScannerMod: " + s);
    }
}
