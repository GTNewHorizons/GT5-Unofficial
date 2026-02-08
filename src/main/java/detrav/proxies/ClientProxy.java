package detrav.proxies;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import detrav.DetravScannerMod;
import detrav.gui.DetravScannerGUI;
import detrav.items.tools.DetravToolElectricProspectorBase;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        // if removed, textures are somehow not loaded in the game.
        int test = DetravToolElectricProspectorBase.mProspectorTextures.length;
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
