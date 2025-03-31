package gregtech.common.handlers;

import static gregtech.api.enums.GTValues.NW;

import appeng.api.util.DimensionalCoord;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import gregtech.common.misc.WirelessNetworkManager;
import net.minecraft.tileentity.TileEntity;

import java.math.BigInteger;

public class PowerGogglesEventHandler {

    private static int ticks = 0;
    public static Minecraft mc;
    public static DimensionalCoord lscLink = null;
    @SubscribeEvent
    public void playerTickStart(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        if (event.type != TickEvent.Type.PLAYER) return;
        if (event.side == Side.CLIENT) {
            if (PowerGogglesHudHandler.updateClient) PowerGogglesHudHandler.drawTick();
        } else {
            if ((ticks % PowerGogglesHudHandler.ticksBetweenMeasurements) == 0) {
                EntityPlayerMP player = (EntityPlayerMP) event.player;
                if(lscLink != null) {
                    TileEntity tileEntity = player.worldObj.getTileEntity(lscLink.x, lscLink.y, lscLink.z);
                    MTELapotronicSuperCapacitor lsc = (MTELapotronicSuperCapacitor) ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
                    NW.sendToPlayer(
                        new GTPacketUpdatePowerGoggles(BigInteger.valueOf(lsc.getEUVar())),
                        player);
                } else {
                    NW.sendToPlayer(
                        new GTPacketUpdatePowerGoggles(WirelessNetworkManager.getUserEU((player).getUniqueID())),
                        player);
                }

            }
            ticks++;
        }
    }

    @SubscribeEvent
    public void clientOnPlayerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        PowerGogglesHudHandler.drawTick();
    }

    @SubscribeEvent
    public void clientOnPlayerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PowerGogglesHudHandler.clear();
    }
}
