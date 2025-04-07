package gregtech.common.handlers;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;

import appeng.api.util.DimensionalCoord;
import baubles.common.container.InventoryBaubles;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacketLinkGoggles;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import gregtech.common.gui.PowerGogglesGuiHudConfig;
import gregtech.common.misc.WirelessNetworkManager;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesEventHandler {

    private static int ticks = 0;
    public static Minecraft mc;
    public static Map<UUID, MTELapotronicSuperCapacitor> playerLscMap = new HashMap<>();
    public static Map<UUID, DimensionalCoord> lscLinkMap = new HashMap<>();
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
                if (isValidLink(player, lscLinkMap.get(player.getUniqueID()))) {
                    DimensionalCoord coords = lscLinkMap.get(player.getUniqueID());
                    WorldServer lscDim = player.mcServer.worldServerForDimension(coords.getDimension());
                    TileEntity tileEntity = lscDim.getTileEntity(coords.x, coords.y, coords.z);
                    MTELapotronicSuperCapacitor lsc = ((MTELapotronicSuperCapacitor) ((IGregTechTileEntity) tileEntity)
                        .getMetaTileEntity());
                    NW.sendToPlayer(
                        new GTPacketUpdatePowerGoggles(BigInteger.valueOf(lsc.getEUVar()), forceRefresh),
                        player);
                } else {
                    if (lscLinkMap.get(player.getUniqueID()) != null) {
                        lscLinkMap.put(player.getUniqueID(), null);
                        forceRefresh = true;
                    }
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (PowerGogglesKeybindHandler.openConfigGui.isPressed()) {
            Minecraft.getMinecraft()
                .displayGuiScreen(new PowerGogglesGuiHudConfig());

        }
        if (PowerGogglesKeybindHandler.toggleChart.isPressed()) {
            PowerGogglesConfigHandler.showPowerChart = !PowerGogglesConfigHandler.showPowerChart;
            PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
                .get("Show Power Chart")
                .set(PowerGogglesConfigHandler.showPowerChart);
            PowerGogglesConfigHandler.config.save();
        }
    }

    private void setLink(ItemStack item) {
        if (!item.hasTagCompound() || item.getTagCompound()
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

    @SubscribeEvent
    public void serverOnPlayerDisconnect(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) {
        EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
        setLscLink(player, null);
    }
}
