package gregtech.common.data;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;

import org.spongepowered.libraries.com.google.common.reflect.TypeToken;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.MapMaker;
import com.google.gson.Gson;
import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPowerfailStatusPacket;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTTextBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.NBTPersist;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

@EventBusSubscriber
public class GTPowerfailTracker {

    public static final String DATA_NAME = "gt.powerfails";

    /** The powerfail state. When a world is loaded, this will never be null. */
    private static SaveData INSTANCE;

    /** A fast lookup for players. Only populated on dedicated servers. */
    private static final ConcurrentMap<UUID, EntityPlayerMP> PLAYERS_BY_ID = new MapMaker().weakValues()
        .makeMap();
    /** Players with pending powerfail syncs. Used to debounce updates to once per tick. */
    private static final HashSet<UUID> PENDING_UPDATES = new HashSet<>();

    /** Something that can own a machine, usually a player but may also be a team. */
    interface MachineOwner {

        boolean isOnline();

        void sendMessage(String message);
    }

    @Desugar
    private record PlayerOwner(UUID player) implements MachineOwner {

        @Override
        public boolean isOnline() {
            return getPlayer(player) != null;
        }

        @Override
        public void sendMessage(String message) {
            EntityPlayerMP playerMP = getPlayer(player);

            if (playerMP == null) return;

            playerMP.addChatMessage(new ChatComponentText(message));
        }
    }

    @Desugar
    private record TeamOwner(UUID leader) implements MachineOwner {

        private List<EntityPlayerMP> getPlayers() {
            HashSet<UUID> inTeam = new HashSet<>();

            for (var e : SpaceProjectManager.spaceTeams.entrySet()) {
                if (e.getValue()
                    .equals(leader)) inTeam.add(e.getKey());
            }

            List<EntityPlayerMP> players = new ArrayList<>();

            ServerConfigurationManager configurationManager = MinecraftServer.getServer()
                .getConfigurationManager();

            for (EntityPlayerMP player : configurationManager.playerEntityList) {
                if (inTeam.contains(
                    player.getGameProfile()
                        .getId())) {
                    players.add(player);
                }
            }

            return players;
        }

        @Override
        public boolean isOnline() {
            return !getPlayers().isEmpty();
        }

        @Override
        public void sendMessage(String message) {
            for (EntityPlayerMP player : getPlayers()) {
                player.addChatMessage(new ChatComponentText(message));
            }
        }
    }

    private static class TeamInfo {

        public Int2ObjectOpenHashMap<DimensionInfo> byWorld = new Int2ObjectOpenHashMap<>();
    }

    private static class DimensionInfo {

        public Long2ObjectOpenHashMap<Powerfail> byCoord = new Long2ObjectOpenHashMap<>();
    }

    public static class Powerfail {

        public int dim, x, y, z;
        public int mteId;
        public int count;
        public Date lastOccurrence;

        public float getSecs() {
            return lastOccurrence == null ? 0 : (new Date().getTime() - lastOccurrence.getTime()) / 1000f;
        }

        public void update(IGregTechTileEntity igte) {
            this.x = igte.getXCoord();
            this.y = igte.getYCoord();
            this.z = igte.getZCoord();
            this.mteId = igte.getMetaTileID();
            this.count++;
            this.lastOccurrence = new Date();
        }

        @Override
        public String toString() {
            float secs = getSecs();

            String ago;

            if (secs > 60f * 60f * 24f) {
                ago = GTUtility.formatNumbers(secs / 60f / 60f / 24f) + " days";
            } else if (secs > 60f * 60f) {
                ago = GTUtility.formatNumbers(secs / 60f / 60f) + " hours";
            } else if (secs > 60f) {
                ago = GTUtility.formatNumbers(secs / 60f) + " minutes";
            } else {
                ago = GTUtility.formatNumbers(secs) + " seconds";
            }

            IMetaTileEntity imte = GTDataUtils.getIndexSafe(GregTechAPI.METATILEENTITIES, mteId);

            return new GTTextBuilder().setBase(EnumChatFormatting.GRAY)
                .addName(imte == null ? "<error>" : imte.getLocalName())
                .addText(" at ")
                .addCoord(x, y, z)
                .addText(" in ")
                .addValue(DimensionManager.getWorld(dim).provider.getDimensionName())
                .addText(" powerfailed ")
                .addNumber(count)
                .addText(count > 1 ? " times " : " time ")
                .addNumber(ago)
                .addText(" ago")
                .toString();
        }
    }

    private static MachineOwner getMachineOwner(UUID player) {
        UUID leader = SpaceProjectManager.getLeader(player);

        if (leader == null) return new PlayerOwner(player);

        return new TeamOwner(leader);
    }

    public static void createPowerfailEvent(IGregTechTileEntity igte) {
        MachineOwner owner = getMachineOwner(igte.getOwnerUuid());

        TeamInfo teamInfo = INSTANCE.powerfailInfo.computeIfAbsent(owner, ignored -> new TeamInfo());

        DimensionInfo dimensionInfo = teamInfo.byWorld
            .computeIfAbsent(igte.getWorld().provider.dimensionId, ignored -> new DimensionInfo());

        long coord = CoordinatePacker.pack(igte.getXCoord(), igte.getYCoord(), igte.getZCoord());

        Powerfail powerfail = dimensionInfo.byCoord.computeIfAbsent(coord, ignored -> new Powerfail());

        float secs = powerfail.getSecs();

        powerfail.update(igte);

        if ((powerfail.count == 1 || secs > 60) && owner.isOnline()) {
            owner.sendMessage(powerfail.toString());
        }

        PENDING_UPDATES.add(igte.getOwnerUuid());
        INSTANCE.markDirty();
    }

    public static List<Powerfail> getPowerfails(UUID player, OptionalInt inDim) {
        MachineOwner owner = getMachineOwner(player);

        // spotless:off
        return GTDataUtils.ofNullableStream(INSTANCE.powerfailInfo.get(owner))
            .flatMap(team -> inDim.isPresent() ? GTDataUtils.ofNullableStream(team.byWorld.get(inDim.getAsInt())) : team.byWorld.values().stream())
            .flatMap(dim -> dim.byCoord.values().stream())
            .collect(Collectors.toList());
        // spotless:on
    }

    public static void clearPowerfails(EntityPlayerMP player, OptionalInt inDim) {
        MachineOwner owner = getMachineOwner(
            player.getGameProfile()
                .getId());

        TeamInfo teamInfo = INSTANCE.powerfailInfo.get(owner);

        if (teamInfo == null) return;

        if (inDim.isPresent()) {
            teamInfo.byWorld.remove(inDim.getAsInt());
        } else {
            teamInfo.byWorld.clear();
        }

        sendPlayerPowerfailStatus(player);
    }

    public static void showPowerfails(EntityPlayerMP player) {
        INSTANCE.playerNoRendering.remove(
            player.getGameProfile()
                .getId());

        sendPlayerRenderingFlag(player);
        sendPlayerPowerfailStatus(player);
    }

    public static void hidePowerfails(EntityPlayerMP player) {
        INSTANCE.playerNoRendering.add(
            player.getGameProfile()
                .getId());

        sendPlayerRenderingFlag(player);
    }

    public static void sendPlayerRenderingFlag(EntityPlayerMP player) {
        boolean shouldRender = !INSTANCE.playerNoRendering.contains(
            player.getGameProfile()
                .getId());

        GTPowerfailStatusPacket packet = shouldRender ? GTPowerfailStatusPacket.show() : GTPowerfailStatusPacket.hide();

        GTValues.NW.sendToPlayer(packet, player);
    }

    public static void sendPlayerPowerfailStatus(EntityPlayerMP player) {
        if (INSTANCE.playerNoRendering.contains(
            player.getGameProfile()
                .getId()))
            return;

        GTPowerfailStatusPacket packet = GTPowerfailStatusPacket.set(
            player.dimension,
            getPowerfails(
                player.getGameProfile()
                    .getId(),
                OptionalInt.of(player.dimension)));

        GTValues.NW.sendToPlayer(packet, player);
    }

    @SubscribeEvent
    public static void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP playerMP)) return;

        PLAYERS_BY_ID.put(
            playerMP.getGameProfile()
                .getId(),
            playerMP);

        List<Powerfail> powerfails = getPowerfails(
            event.player.getGameProfile()
                .getId(),
            OptionalInt.empty());

        if (!powerfails.isEmpty()) {
            // spotless:off
            event.player.addChatMessage(new ChatComponentText(GRAY + "You have " + GOLD + powerfails.size() + GRAY + " uncleared powerfail" + (powerfails.size() > 1 ? "s" : "") + "."));
            event.player.addChatMessage(new ChatComponentText(GRAY + "Use /powerfails list or /powerfails show for more info."));
            event.player.addChatMessage(new ChatComponentText(GRAY + "Use /powerfails clear or /powerfails clear-dim to clear powerfails."));
            event.player.addChatMessage(new ChatComponentText(GRAY + "Use /powerfails show or /powerfails hide to toggle overlay rendering."));
            // spotless:on
        }

        sendPlayerRenderingFlag(playerMP);
        sendPlayerPowerfailStatus(playerMP);
    }

    @SubscribeEvent
    public static void onPlayerTeleported(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.player instanceof EntityPlayerMP playerMP)) return;

        PLAYERS_BY_ID.put(
            playerMP.getGameProfile()
                .getId(),
            playerMP);

        sendPlayerPowerfailStatus(playerMP);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.player instanceof EntityPlayerMP playerMP)) return;

        PLAYERS_BY_ID.put(
            playerMP.getGameProfile()
                .getId(),
            playerMP);

        sendPlayerPowerfailStatus(playerMP);
    }

    @SubscribeEvent
    public static void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.player instanceof EntityPlayerMP playerMP)) return;

        PLAYERS_BY_ID.remove(
            playerMP.getGameProfile()
                .getId());
    }

    @SubscribeEvent
    public static void onPostTick(TickEvent.ServerTickEvent event) {
        if (event.side != Side.SERVER) return;
        if (event.phase != TickEvent.Phase.END) return;

        for (UUID playerId : PENDING_UPDATES) {
            EntityPlayerMP player = getPlayer(playerId);

            if (player == null) continue;

            sendPlayerPowerfailStatus(player);
        }

        PENDING_UPDATES.clear();
    }

    public static EntityPlayerMP getPlayer(UUID id) {
        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            // integrated server

            MinecraftServer server = MinecraftServer.getServer();

            if (server == null) return null;

            // spotless:off
            return server
                .getConfigurationManager()
                .playerEntityList
                .stream()
                .filter(player -> player.getGameProfile().getId().equals(id))
                .findFirst()
                .orElse(null);
            // spotless:on
        } else {
            // dedicated server
            return PLAYERS_BY_ID.get(id);
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            INSTANCE = (SaveData) event.world.mapStorage.loadData(SaveData.class, DATA_NAME);
            if (INSTANCE == null) {
                INSTANCE = new SaveData(DATA_NAME);
                event.world.mapStorage.setData(DATA_NAME, INSTANCE);
            }
            INSTANCE.markDirty();
        }
    }

    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            INSTANCE = null;
        }
    }

    private static final Gson GSON = new Gson();

    public static class SaveData extends WorldSavedData {

        final Map<MachineOwner, TeamInfo> powerfailInfo = new HashMap<>();

        // Storing rendering info on the server is kinda strange, but this is the simplest place to put it because
        // otherwise we'd have to make some bespoke client-side storage format, whereas this will let us use world save
        // data
        final HashSet<UUID> playerNoRendering = new HashSet<>();

        public SaveData(String key) {
            super(key);
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            powerfailInfo.clear();
            playerNoRendering.clear();
            PENDING_UPDATES.clear();

            // spotless:off
            playerNoRendering.addAll(GSON.fromJson(
                NBTPersist.toJsonObject(tag.getTag("noRendering")),
                new TypeToken<List<UUID>>(){}.getType()));

            Map<String, Map<String, List<Powerfail>>> powerfails = GSON.fromJson(
                NBTPersist.toJsonObject(tag.getTag("powerfails")),
                new TypeToken<Map<String, Map<String, List<Powerfail>>>>(){}.getType());
            // spotless:on

            for (Map.Entry<String, Map<String, List<Powerfail>>> byOwners : powerfails.entrySet()) {
                String[] ownerText = byOwners.getKey()
                    .split(":");

                if (ownerText.length != 2) continue;

                MachineOwner owner;

                try {
                    switch (ownerText[0]) {
                        case "team" -> owner = new TeamOwner(UUID.fromString(ownerText[1]));
                        case "player" -> owner = new PlayerOwner(UUID.fromString(ownerText[1]));
                        default -> {
                            continue;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    continue;
                }

                TeamInfo teamInfo = new TeamInfo();
                powerfailInfo.put(owner, teamInfo);

                for (Map.Entry<String, List<Powerfail>> byWorld : byOwners.getValue()
                    .entrySet()) {
                    int dimId;

                    try {
                        dimId = Integer.parseInt(byWorld.getKey());
                    } catch (NumberFormatException e) {
                        continue;
                    }

                    DimensionInfo dimInfo = new DimensionInfo();
                    teamInfo.byWorld.put(dimId, dimInfo);

                    for (Powerfail powerfail : byWorld.getValue()) {
                        dimInfo.byCoord.put(CoordinatePacker.pack(powerfail.x, powerfail.y, powerfail.z), powerfail);
                    }
                }
            }

            // Now that everything is loaded, queue an update for all connected players (there should be none, but let's
            // be safe)
            // spotless:off
            MinecraftServer.getServer()
                .getConfigurationManager()
                .playerEntityList
                .stream()
                .map(p -> p.getGameProfile().getId())
                    .forEach(PENDING_UPDATES::add);
            // spotless:on
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setTag(
                "noRendering",
                playerNoRendering.stream()
                    .map(UUID::toString)
                    .map(NBTTagString::new)
                    .collect(GTUtility.toNBTTagList()));

            NBTTagCompound powerfails = new NBTTagCompound();
            tag.setTag("powerfails", powerfails);

            for (Map.Entry<MachineOwner, TeamInfo> byOwner : powerfailInfo.entrySet()) {
                String ownerText;

                if (byOwner.getKey() instanceof TeamOwner team) {
                    ownerText = "team:" + team.leader.toString();
                } else if (byOwner.getKey() instanceof PlayerOwner player) {
                    ownerText = "player:" + player.player.toString();
                } else {
                    continue;
                }

                NBTTagCompound byOwnerTag = new NBTTagCompound();

                for (Int2ObjectMap.Entry<DimensionInfo> byWorld : byOwner.getValue().byWorld.int2ObjectEntrySet()) {
                    NBTTagList byDimList = new NBTTagList();

                    for (Powerfail powerfail : byWorld.getValue().byCoord.values()) {
                        byDimList.appendTag(NBTPersist.toNbt(GSON.toJsonTree(powerfail)));
                    }

                    if (!byDimList.tagList.isEmpty())
                        byOwnerTag.setTag(Integer.toString(byWorld.getIntKey()), byDimList);
                }

                if (!byOwnerTag.hasNoTags()) powerfails.setTag(ownerText, byOwnerTag);
            }
        }
    }
}
