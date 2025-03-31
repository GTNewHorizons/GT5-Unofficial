package gregtech.common.handlers;

import cpw.mods.fml.common.network.FMLNetworkEvent;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import gregtech.common.misc.WirelessNetworkManager;
import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayerMP;

import static gregtech.api.enums.GTValues.NW;

public class PowerGogglesEventHandler {
    private static int ticks = 0;
    public static Minecraft mc;
//    @SubscribeEvent
//    public void tickEnd(TickEvent.ClientTickEvent event) {
//        if (event.phase != TickEvent.Phase.START) return;
//        if(event.type != TickEvent.Type.CLIENT || event.side != Side.CLIENT) return;
//        if (mc == null) mc = Minecraft.getMinecraft();
//        else if (mc.theWorld != null) {
//            PowerGogglesHudHandler.drawTick();
//        }
//    }

    @SubscribeEvent
    public void serverTickEnd(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if(event.type != TickEvent.Type.PLAYER) return;
        if(event.side == Side.CLIENT) {
            if (PowerGogglesHudHandler.updateClient) PowerGogglesHudHandler.drawTick();
        }else{
            if ((ticks % PowerGogglesHudHandler.ticksBetweenMeasurements) == 0) {
                EntityPlayerMP player = (EntityPlayerMP) event.player;
                NW.sendToPlayer(new GTPacketUpdatePowerGoggles(WirelessNetworkManager.getUserEU((player).getUniqueID())), player);
            }
            ticks++;
        }
    }
    @SubscribeEvent
    public void onPlayerJoinEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        PowerGogglesHudHandler.drawTick();
    }
    @SubscribeEvent
    public void onPlayerLeaveEvent(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PowerGogglesHudHandler.clear();
    }
}
