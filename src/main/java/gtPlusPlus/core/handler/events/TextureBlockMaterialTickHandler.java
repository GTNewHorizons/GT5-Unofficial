package gtPlusPlus.core.handler.events;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.client.TextureBlockMaterial;

@SideOnly(Side.CLIENT)
public class TextureBlockMaterialTickHandler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.getTextureManager() == null) return;
        TextureBlockMaterial.tickAll();
    }
}
