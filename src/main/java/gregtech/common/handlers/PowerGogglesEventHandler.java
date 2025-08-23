package gregtech.common.handlers;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;

import appeng.api.util.DimensionalCoord;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacketLinkPowerGoggles;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import gregtech.common.items.ItemPowerGoggles;
import gregtech.common.items.gui.PowerGogglesGuiHudConfig;
import gregtech.common.misc.WirelessNetworkManager;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesEventHandler {

    private static Map<UUID, Integer> tickMap = new HashMap<>();
    public static Minecraft mc;
    public static Map<UUID, DimensionalCoord> lscLinkMap = new HashMap<>();
    public static boolean forceUpdate = false;
    public static boolean forceRefresh = false;
    public static boolean firstClientTick = true;

    @SubscribeEvent
    public void playerTickEnd(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (event.side == Side.CLIENT) {
            doClientStuff(event);
        } else {
            doServerStuff(event);
        }
    }

    private void doClientStuff(TickEvent.PlayerTickEvent event) {
        if (firstClientTick) {
            ItemStack bauble = ItemPowerGoggles.getEquippedPowerGoggles(event.player);
            if (bauble != null) setLink(bauble);
            firstClientTick = false;
        }
        if (forceUpdate || PowerGogglesHudHandler.updateClient) PowerGogglesHudHandler.drawTick();
        forceUpdate = false;
    }

    private void doServerStuff(TickEvent.PlayerTickEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        UUID uuid = player.getUniqueID();

        int playerTicks = forceUpdate ? 1
            : (tickMap.getOrDefault(uuid, 0) + 1) % PowerGogglesHudHandler.ticksBetweenMeasurements;
        tickMap.put(uuid, playerTicks);

        if (playerTicks != 1) return;
        if (isValidLink(player, lscLinkMap.get(uuid))) {
            MTELapotronicSuperCapacitor lsc = getLsc(player);
            NW.sendToPlayer(
                new GTPacketUpdatePowerGoggles(BigInteger.valueOf(lsc.getEUVar()), lsc.maxEUStore(), forceRefresh),
                player);
        } else {
            if (lscLinkMap.get(uuid) != null) {
                lscLinkMap.put(uuid, null);
                forceRefresh = true;
            }
            NW.sendToPlayer(
                new GTPacketUpdatePowerGoggles(WirelessNetworkManager.getUserEU((player).getUniqueID()), forceRefresh),
                player);
        }
        forceUpdate = false;
        forceRefresh = false;
    }

    private MTELapotronicSuperCapacitor getLsc(EntityPlayerMP player) {
        DimensionalCoord coords = lscLinkMap.get(player.getUniqueID());
        WorldServer lscDim = player.mcServer.worldServerForDimension(coords.getDimension());
        TileEntity tileEntity = lscDim.getTileEntity(coords.x, coords.y, coords.z);
        return ((MTELapotronicSuperCapacitor) ((IGregTechTileEntity) tileEntity).getMetaTileEntity());
    }

    @SubscribeEvent
    public void clientOnPlayerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {

    }

    @SubscribeEvent
    public void clientOnPlayerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        PowerGogglesHudHandler.clear();
        firstClientTick = true;
    }

    @SubscribeEvent
    public void serverOnPlayerConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {

    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        if (PowerGogglesKeybindHandler.openConfigGui.isPressed()) {
            Minecraft screenInfo = Minecraft.getMinecraft();
            Minecraft.getMinecraft()
                .displayGuiScreen(new PowerGogglesGuiHudConfig(screenInfo.displayWidth, screenInfo.displayHeight));

        } else if (PowerGogglesKeybindHandler.toggleChart.isPressed()) {
            PowerGogglesConfigHandler.showPowerChart = !PowerGogglesConfigHandler.showPowerChart;
            PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                .get("Show Power Chart")
                .set(PowerGogglesConfigHandler.showPowerChart);
            PowerGogglesConfigHandler.config.save();
        }
    }

    private void setLink(ItemStack item) {
        if (!item.hasTagCompound() || item.getTagCompound()
            .hasNoTags()) NW.sendToServer(new GTPacketLinkPowerGoggles());
        else {
            NBTTagCompound tag = item.getTagCompound();
            DimensionalCoord coords = new DimensionalCoord(
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getInteger("dim"));
            NW.sendToServer(new GTPacketLinkPowerGoggles(coords));
        }
    }

    public static boolean isValidLink(EntityPlayerMP player, DimensionalCoord coords) {
        if (coords == null) return false;
        WorldServer lscDim = player.mcServer.worldServerForDimension(coords.getDimension());
        TileEntity tileEntity = lscDim.getTileEntity(coords.x, coords.y, coords.z);
        if (!(tileEntity instanceof IGregTechTileEntity gte)) return false;
        return gte.getMetaTileEntity() instanceof MTELapotronicSuperCapacitor;
    }

    public static void setLscLink(EntityPlayerMP player, DimensionalCoord coords) {
        lscLinkMap.put(player.getUniqueID(), coords);
    }

    public static DimensionalCoord getLscLink(UUID uuid) {
        return lscLinkMap.get(uuid);
    }

    @SubscribeEvent
    public void serverOnPlayerDisconnect(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) {
        EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
        setLscLink(player, null);
    }
}
