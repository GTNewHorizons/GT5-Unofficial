package gregtech.common.handlers;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class PowerGogglesEventHandler {

    public static Minecraft mc;

    @SubscribeEvent
    public void tickEnd(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || event.type != TickEvent.Type.CLIENT || event.side != Side.CLIENT)
            return;
        if (mc == null) mc = Minecraft.getMinecraft();
        else if (mc.theWorld != null) {
            PowerGogglesHudHandler.clientTick();
        }
    }
}
