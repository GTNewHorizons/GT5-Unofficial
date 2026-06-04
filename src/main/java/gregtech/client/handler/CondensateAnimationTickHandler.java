package gregtech.client.handler;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CondensateAnimationTickHandler {

    public static int currentFrame = 0;
    public static final ResourceLocation texture = new ResourceLocation(
        GregTech.resourceDomain,
        "textures/items/iconsets/bec_condensate_over.png");

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc == null || mc.getTextureManager() == null) return;
        currentFrame++;
        if (currentFrame > 63) {
            currentFrame = 0;
        }
    }
}
