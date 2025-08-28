package gregtech.common.powergoggles.handlers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.powergoggles.PowerGogglesClient;
import gregtech.common.powergoggles.PowerGogglesUtil;
import gregtech.common.powergoggles.gui.PowerGogglesGuiHudConfig;

public class PowerGogglesEventHandler {

    private static final PowerGogglesEventHandler INSTANCE = new PowerGogglesEventHandler();

    private final int TICKS_BETWEEN_UPDATES = 100;
    private final Map<UUID, PowerGogglesClient> CLIENTS = new HashMap<>();
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
        if (updateTicker != 1) {
            return;
        }
        updateClient(event);
    }

    private void updateClient(TickEvent.PlayerTickEvent event) {

        if (event.side.isClient()) {
            PowerGogglesHudHandler.drawTick();
            return;
        }

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        UUID uuid = player.getUniqueID();

        if (!CLIENTS.containsKey(uuid)) {
            return;
        }

        PowerGogglesClient client = CLIENTS.get(uuid);
        client.updatePlayer(player);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        if (PowerGogglesKeybindHandler.openConfigGui.isPressed()) {
            openConfig();
        } else if (PowerGogglesKeybindHandler.toggleChart.isPressed()) {
            toggleChart();
        }
    }

    // Annoyingly, this method is called before the player's baubles are initialized
    // when they join this world for the first time(At least in singleplayer)
    @SubscribeEvent
    public void serverOnPlayerConnect(FMLNetworkEvent.ServerConnectionFromClientEvent event) {
        NetHandlerPlayServer handler = (NetHandlerPlayServer) event.handler;
        EntityPlayerMP player = handler.playerEntity;
        UUID uuid = player.getUniqueID();

        if (CLIENTS.containsKey(uuid)) {
            return;
        }
        processNewClient(player);
    }

    private void processNewClient(EntityPlayerMP player) {
        ItemStack goggles = PowerGogglesUtil.getPlayerGoggles(player);
        if (goggles == null) {
            return;
        }
        updatePlayerLink(goggles, player);
    }

    private void openConfig() {
        Minecraft screenInfo = Minecraft.getMinecraft();
        Minecraft.getMinecraft()
            .displayGuiScreen(new PowerGogglesGuiHudConfig(screenInfo.displayWidth, screenInfo.displayHeight));
    }

    private void toggleChart() {
        PowerGogglesConfigHandler.showPowerChart = !PowerGogglesConfigHandler.showPowerChart;
        PowerGogglesConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)
            .get("Show Power Chart")
            .set(PowerGogglesConfigHandler.showPowerChart);
        PowerGogglesConfigHandler.config.save();
    }

    public void updatePlayerLink(ItemStack itemstack, EntityPlayerMP player) {
        PowerGogglesClient client = CLIENTS.computeIfAbsent(player.getUniqueID(), uuid -> new PowerGogglesClient());
        client.updateLscLink(itemstack);
    }

    public Map<UUID, PowerGogglesClient> getClients() {
        return CLIENTS;
    }
}
