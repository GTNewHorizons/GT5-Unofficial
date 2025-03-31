package gregtech.common.handlers;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import appeng.api.util.DimensionalCoord;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import gregtech.common.misc.WirelessNetworkManager;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesEventHandler {

    private static int ticks = 0;
    public static Minecraft mc;
    public static DimensionalCoord lscLink = null;
    public static boolean forceUpdate = false;
    public static boolean forceRefresh = false;

    @SubscribeEvent
    public void playerTickEnd(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.type != TickEvent.Type.PLAYER) return;
        if (event.side == Side.CLIENT) {
            if (PowerGogglesHudHandler.updateClient) PowerGogglesHudHandler.drawTick();
        } else {
            if (ticks == 0 || forceUpdate) {
                EntityPlayerMP player = (EntityPlayerMP) event.player;
                if (lscLink != null) {
                    TileEntity tileEntity = player.worldObj.getTileEntity(lscLink.x, lscLink.y, lscLink.z);
                    MTELapotronicSuperCapacitor lsc = (MTELapotronicSuperCapacitor) ((IGregTechTileEntity) tileEntity)
                        .getMetaTileEntity();
                    NW.sendToPlayer(
                        new GTPacketUpdatePowerGoggles(BigInteger.valueOf(lsc.getEUVar()), forceRefresh),
                        player);
                } else {
                    NW.sendToPlayer(
                        new GTPacketUpdatePowerGoggles(
                            WirelessNetworkManager.getUserEU((player).getUniqueID()),
                            forceRefresh),
                        player);
                }
                if (forceUpdate) ticks = 0;
                forceUpdate = false;
                forceRefresh = false;

            }
            ticks++;
            ticks %= PowerGogglesHudHandler.ticksBetweenMeasurements;
        }
    }

    @SubscribeEvent
    public void clientOnPlayerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        forceUpdate = true;
        forceRefresh = true;
    }

    @SubscribeEvent
    public void clientOnPlayerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PowerGogglesHudHandler.clear();
    }
}
