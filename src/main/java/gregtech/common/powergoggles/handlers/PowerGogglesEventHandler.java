package gregtech.common.powergoggles.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
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
import gregtech.common.powergoggles.PowerGogglesClient;
import gregtech.common.powergoggles.gui.PowerGogglesGuiHudConfig;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesEventHandler {

    private static final PowerGogglesEventHandler INSTANCE = new PowerGogglesEventHandler();
    private final int TICKS_BETWEEN_UPDATES = 100;
    // private Map<UUID, Integer> tickMap = new HashMap<>();
    // private Minecraft mc;
    // private Map<UUID, DimensionalCoord> lscLinkMap = new HashMap<>();
    // private boolean forceUpdate = false;
    // private boolean forceRefresh = false;
    // private boolean firstClientTick = true;

    private final Map<UUID, PowerGogglesClient> clientMap = new HashMap<>();
    private int updateTicker = 0;

    private PowerGogglesEventHandler() {}

    public static PowerGogglesEventHandler getInstance() {
        return INSTANCE;
    }

    @SubscribeEvent
    public void processServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        updateTicker = (updateTicker + 1) % TICKS_BETWEEN_UPDATES;
    }

    @SubscribeEvent
    public void processPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (event.side.isClient()) {
            return;
        }
        if (updateTicker != 1) {
            return;
        }
        updateClient(event);
    }

    private void updateClient(TickEvent.PlayerTickEvent event) {
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        UUID uuid = player.getUniqueID();

        if (!clientMap.containsKey(uuid)) {
            return;
        }

        PowerGogglesClient client = clientMap.get(uuid);
        client.updatePlayer(player);
    }

    @SubscribeEvent
    private void clientOnPlayerConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {

    }

    // @SubscribeEvent
    // private void clientOnPlayerDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
    // PowerGogglesHudHandler.clear();
    // firstClientTick = true;
    // }

    @SubscribeEvent
    private void serverOnPlayerConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {

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

    private boolean isValidLink(EntityPlayerMP player, DimensionalCoord coords) {
        if (coords == null) return false;
        WorldServer lscDim = player.mcServer.worldServerForDimension(coords.getDimension());
        TileEntity tileEntity = lscDim.getTileEntity(coords.x, coords.y, coords.z);
        if (!(tileEntity instanceof IGregTechTileEntity gte)) return false;
        return gte.getMetaTileEntity() instanceof MTELapotronicSuperCapacitor;
    }

    // private void setLscLink(EntityPlayerMP player, DimensionalCoord coords) {
    // lscLinkMap.put(player.getUniqueID(), coords);
    // }
    //
    // private DimensionalCoord getLscLink(UUID uuid) {
    // return lscLinkMap.get(uuid);
    // }
    //
    // @SubscribeEvent
    // private void serverOnPlayerDisconnect(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) {
    // EntityPlayerMP player = ((NetHandlerPlayServer) event.handler).playerEntity;
    // setLscLink(player, null);
    // }

    public void updatePlayerLink(ItemStack itemstack, EntityPlayerMP player) {
        PowerGogglesClient client = clientMap.computeIfAbsent(player.getUniqueID(), uuid -> new PowerGogglesClient());
        client.updateLscLink(itemstack);
    }
}
