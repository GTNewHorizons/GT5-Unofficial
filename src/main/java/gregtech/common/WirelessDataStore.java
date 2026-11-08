package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessDataSticks;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;

import net.minecraft.server.MinecraftServer;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeOutputHatch;

@EventBusSubscriber
public class WirelessDataStore {

    public static final long IO_TICK_RATE = 200;
    public static final long DOWNLOAD_TICK_OFFSET = 1;
    /// Remove expired uploads the tick before data is re-uploaded
    public static final long CLEAN_TICK_OFFSET = IO_TICK_RATE - 1;
    /// Uploads can last for two wireless network ticks before they're considered expired.
    public static final long UPLOAD_EXPIRATION_THRESHOLD = IO_TICK_RATE * 2;

    private final LinkedHashMap<IALRecipeOutputHatch, DataStickUpload> uploads = new LinkedHashMap<>();
    private ALRecipeDataPacket cachedRecipes = null;

    private static class DataStickUpload {

        public long lastInjection;
        public HashSet<RecipeAssemblyLine> comparisonCache;
    }

    public void uploadData(IALRecipeOutputHatch injector) {
        long now = MinecraftServer.getServer()
            .getTickCounter();

        var upload = uploads.computeIfAbsent(injector, $ -> new DataStickUpload());

        upload.lastInjection = now;

        HashSet<RecipeAssemblyLine> injected = new HashSet<>();

        var packet = injector.getAssemblyLineRecipes();

        if (packet != null) {
            injected.addAll(packet.contents);
        }

        if (!Objects.equals(upload.comparisonCache, injected)) {
            upload.comparisonCache = injected;
            this.cachedRecipes = null;
        }
    }

    public ALRecipeDataPacket downloadData() {
        if (cachedRecipes == null) {
            LinkedHashSet<RecipeAssemblyLine> union = new LinkedHashSet<>();

            for (var upload : this.uploads.values()) {
                if (upload.comparisonCache != null) {
                    union.addAll(upload.comparisonCache);
                }
            }

            this.cachedRecipes = new ALRecipeDataPacket(ImmutableList.copyOf(union));
        }

        return cachedRecipes;
    }

    public static WirelessDataStore getWirelessDataSticks(UUID uuid) {
        UUID team = SpaceProjectManager.getLeader(uuid);
        if (GlobalWirelessDataSticks.get(team) == null) {
            GlobalWirelessDataSticks.put(team, new WirelessDataStore());
        }
        return GlobalWirelessDataSticks.get(team);
    }

    public static void onServerClosed() {
        GlobalWirelessDataSticks.clear();
    }

    @SubscribeEvent
    public static void removeExpiredRecipes(ServerTickEvent event) {
        if (event.phase != Phase.END) return;

        long now = MinecraftServer.getServer()
            .getTickCounter();

        if (now % IO_TICK_RATE != CLEAN_TICK_OFFSET) return;

        long expiredThreshold = now - UPLOAD_EXPIRATION_THRESHOLD;

        for (var store : GlobalWirelessDataSticks.values()) {
            boolean updated = store.uploads.entrySet()
                .removeIf(e -> e.getValue().lastInjection <= expiredThreshold);

            if (updated) {
                store.cachedRecipes = null;
            }
        }
    }
}
