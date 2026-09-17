package detrav.proxies;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import detrav.DetravScannerMod;
import detrav.client.DetravOreMarkerRenderer;
import detrav.gui.DetravScannerGUI;
import detrav.items.DetravToolItems;
import detrav.items.tools.DetravToolElectricProspectorBase;
import gregtech.common.render.MetaGeneratedToolRenderer;

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
        MinecraftForge.EVENT_BUS.register(new DetravOreMarkerRenderer());
        registerScannerRenderers();
    }

    /**
     * The scanners render the same way the other standalone tools do: a material-tinted head over a handle. GregTech
     * registers its own tools itself; these belong to this mod, so they are registered here.
     */
    private static void registerScannerRenderers() {
        final MetaGeneratedToolRenderer renderer = new MetaGeneratedToolRenderer();
        for (Item scanner : new Item[] { DetravToolItems.PROSPECTOR_LV, DetravToolItems.PROSPECTOR_MV,
            DetravToolItems.PROSPECTOR_HV, DetravToolItems.PROSPECTOR_EV, DetravToolItems.PROSPECTOR_IV,
            DetravToolItems.PROSPECTOR_LUV, DetravToolItems.PROSPECTOR_ZPM, DetravToolItems.PROSPECTOR_UV,
            DetravToolItems.PROSPECTOR_UHV, DetravToolItems.ELECTRIC_PROSPECTOR_LUV,
            DetravToolItems.ELECTRIC_PROSPECTOR_ZPM, DetravToolItems.ELECTRIC_PROSPECTOR_UV,
            DetravToolItems.ELECTRIC_PROSPECTOR_UHV }) {
            MinecraftForgeClient.registerItemRenderer(scanner, renderer);
        }
    }

    @Override
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
