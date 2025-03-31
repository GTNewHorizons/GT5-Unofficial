package gregtech.common.handlers;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import appeng.api.util.DimensionalCoord;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacketLinkGoggles;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import gregtech.common.misc.WirelessNetworkManager;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesEventHandler {

    private static int ticks = 0;
    public static Minecraft mc;
    public static DimensionalCoord lscLink = null;
    public static boolean forceUpdate = false;
    public static boolean forceRefresh = false;
    public static boolean firstClientTick = true;

    @SubscribeEvent
    public void playerTickEnd(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.type != TickEvent.Type.PLAYER) return;
        if (event.side == Side.CLIENT) {
            if (firstClientTick) {
                InventoryBaubles baubles = PlayerHandler.getPlayerBaubles(event.player);
                for (ItemStack bauble : baubles.stackList) {
                    if (bauble == null) continue;
                    if (bauble.getUnlocalizedName()
                        .equals("gt.PowerNerd_Goggles")) {
                        setLink(bauble);
                    }
                }
                firstClientTick = false;
            }
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
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

    }

    @SubscribeEvent
    public void clientOnPlayerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PowerGogglesHudHandler.clear();
        firstClientTick = true;
    }

    @SubscribeEvent
    public void serverOnPlayerConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {

    }

    private void setLink(ItemStack item) {
        if (item.getTagCompound()
            .hasNoTags()) NW.sendToServer(new GTPacketLinkGoggles());
        else {
            NBTTagCompound tag = item.getTagCompound();
            DimensionalCoord coords = new DimensionalCoord(
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getInteger("dim"));
            NW.sendToServer(new GTPacketLinkGoggles(coords));
        }
    }

    @SubscribeEvent
    public void serverOnPlayerDisconnect(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) {
        lscLink = null;
    }
}
