package gregtech.common.data;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GRAY;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.world.WorldEvent;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.GTMod;
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

public class GTPowerfailTracker {

    public static final String DATA_NAME = "gt.powerfails";

    /** The powerfail state. When a world is loaded, this will never be null. */
    private SaveData INSTANCE;

    /** Players with pending powerfail syncs. Used to debounce updates to once per tick. */
    private final HashSet<UUID> PENDING_UPDATES = new HashSet<>();

    /** Something that can own a machine, usually a player but may also be a team. */
    interface MachineOwner {

        Set<UUID> getPlayers();

        boolean isOnline();

        void sendMessage(String message);

        default List<EntityPlayerMP> getPlayerEntities() {
            return getPlayers().stream()
                .map(GTPowerfailTracker::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
    }

    @Desugar
    private record PlayerOwner(UUID player) implements MachineOwner {

        @Override
        public Set<UUID> getPlayers() {
            return new HashSet<>(Collections.singletonList(player));
        }

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

        @Override
        public Set<UUID> getPlayers() {
            HashSet<UUID> inTeam = new HashSet<>();

            for (var e : SpaceProjectManager.spaceTeams.entrySet()) {
                if (e.getValue()
                    .equals(leader)) inTeam.add(e.getKey());
            }

            return inTeam;
        }

        @Override
        public boolean isOnline() {
            return !getPlayers().isEmpty();
        }

        @Override
        public void sendMessage(String message) {
            for (EntityPlayerMP player : getPlayerEntities()) {
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

    public void createPowerfailEvent(IGregTechTileEntity igte) {
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

            for (UUID playerId : owner.getPlayers()) {
                EntityPlayerMP player = getPlayer(playerId);

                // if they aren't online, don't send them a hint message
                if (player == null) continue;

                if (INSTANCE.playerUsageHints.add(playerId)) {
                    // spotless:off
                    GTUtility.sendChatToPlayer(player, GRAY + "Use /powerfails help for more info.");
                    GTUtility.sendChatToPlayer(player, GRAY + "Use /powerfails clear or /powerfails clear-dim to clear powerfails.");
                    GTUtility.sendChatToPlayer(player, GRAY + "Use /powerfails show or /powerfails hide to toggle overlay rendering.");
                    // spotless:on
                }
            }
        }

        PENDING_UPDATES.addAll(owner.getPlayers());
        INSTANCE.markDirty();
    }

    public List<Powerfail> getPowerfails(UUID player, OptionalInt inDim) {
        MachineOwner owner = getMachineOwner(player);

        // spotless:off
        return GTDataUtils.ofNullableStream(INSTANCE.powerfailInfo.get(owner))
            .flatMap(team -> inDim.isPresent() ? GTDataUtils.ofNullableStream(team.byWorld.get(inDim.getAsInt())) : team.byWorld.values().stream())
            .flatMap(dim -> dim.byCoord.values().stream())
            .collect(Collectors.toList());
        // spotless:on
    }

    public void clearPowerfails(EntityPlayerMP player, OptionalInt inDim) {
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

        PENDING_UPDATES.addAll(owner.getPlayers());
        INSTANCE.markDirty();
    }

    public void sendPlayerPowerfailStatus(EntityPlayerMP player) {
        GTPowerfailStatusPacket packet = GTPowerfailStatusPacket.set(
            player.dimension,
            getPowerfails(
                player.getGameProfile()
                    .getId(),
                OptionalInt.of(player.dimension)));

        GTValues.NW.sendToPlayer(packet, player);
    }

    @SubscribeEvent
    public void onPlayerJoined(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.player instanceof EntityPlayerMP playerMP)) return;

        List<Powerfail> powerfails = getPowerfails(
            event.player.getGameProfile()
                .getId(),
            OptionalInt.empty());

        if (!powerfails.isEmpty()) {
            // spotless:off
            GTUtility.sendChatToPlayer(event.player, GRAY + "You have " + GOLD + powerfails.size() + GRAY + " uncleared powerfail" + (powerfails.size() > 1 ? "s" : "") + ".");
            GTUtility.sendChatToPlayer(event.player, GRAY + "Use /powerfails list for more info.");
            GTUtility.sendChatToPlayer(event.player, GRAY + "Use /powerfails clear or /powerfails clear-dim to clear powerfails.");
            GTUtility.sendChatToPlayer(event.player, GRAY + "Use /powerfails show or /powerfails hide to toggle overlay rendering.");
            // spotless:on
        }

        sendPlayerPowerfailStatus(playerMP);
    }

    @SubscribeEvent
    public void onPlayerTeleported(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.player instanceof EntityPlayerMP playerMP)) return;
        sendPlayerPowerfailStatus(playerMP);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.player instanceof EntityPlayerMP playerMP)) return;
        sendPlayerPowerfailStatus(playerMP);
    }

    @SubscribeEvent
    public void onPostTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        for (UUID playerId : PENDING_UPDATES) {
            EntityPlayerMP player = getPlayer(playerId);

            if (player == null) continue;

            sendPlayerPowerfailStatus(player);
        }

        PENDING_UPDATES.clear();
    }

    private static EntityPlayerMP getPlayer(UUID id) {
        return GTMod.proxy.getPlayerMP(id);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            INSTANCE = (SaveData) event.world.mapStorage.loadData(SaveData.class, DATA_NAME);
            if (INSTANCE == null) {
                INSTANCE = new SaveData(DATA_NAME);
                event.world.mapStorage.setData(DATA_NAME, INSTANCE);
            }
            INSTANCE.markDirty();
        }
    }

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(MachineOwner.class, new TeamAdapter())
        .registerTypeAdapter(TeamOwner.class, new TeamAdapter())
        .registerTypeAdapter(PlayerOwner.class, new TeamAdapter())
        .create();

    private static class TeamAdapter implements JsonSerializer<MachineOwner>, JsonDeserializer<MachineOwner> {

        @Override
        public MachineOwner deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            if (!(json instanceof JsonPrimitive primitive)) throw new JsonParseException("team must be a string");
            if (!primitive.isString()) throw new JsonParseException("team must be a string");

            String string = json.getAsString();

            String[] ownerText = string.split(":");

            if (ownerText.length != 2) {
                throw new JsonParseException("team must be a string of the format [prefix:uuid]");
            }

            try {
                switch (ownerText[0]) {
                    case "team" -> {
                        return new TeamOwner(UUID.fromString(ownerText[1]));
                    }
                    case "player" -> {
                        return new PlayerOwner(UUID.fromString(ownerText[1]));
                    }
                    default -> {
                        throw new JsonParseException("team prefix must be 'team' or 'player'");
                    }
                }
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("invalid team uuid: " + ownerText[1]);
            }
        }

        @Override
        public JsonElement serialize(MachineOwner src, Type typeOfSrc, JsonSerializationContext context) {
            if (src instanceof TeamOwner team) {
                return new JsonPrimitive("team:" + team.leader.toString());
            } else if (src instanceof PlayerOwner player) {
                return new JsonPrimitive("player:" + player.player.toString());
            } else {
                throw new IllegalArgumentException("expected TeamOwner or PlayerOwner");
            }
        }
    }

    public class SaveData extends WorldSavedData {

        final Map<MachineOwner, TeamInfo> powerfailInfo = new HashMap<>();

        final HashSet<UUID> playerUsageHints = new HashSet<>();

        public SaveData(String key) {
            super(key);
        }

        static class DimState {

            public ArrayList<Powerfail> powerfails = new ArrayList<>();
        }

        static class TeamState {

            public Int2ObjectOpenHashMap<DimState> byWorld = new Int2ObjectOpenHashMap<>();
        }

        // Gson won't use type adapters for map keys, so this is a workaround for that problem
        static class TeamPair {

            public MachineOwner owner;
            public TeamState teamState;

            // Used by gson
            @SuppressWarnings("unused")
            public TeamPair() {}

            public TeamPair(MachineOwner owner, TeamState teamState) {
                this.owner = owner;
                this.teamState = teamState;
            }
        }

        static class State {

            final ArrayList<TeamPair> powerfailInfo = new ArrayList<>();
            final HashSet<UUID> playerUsageHints = new HashSet<>();
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            powerfailInfo.clear();
            PENDING_UPDATES.clear();

            try {
                State state = GSON.fromJson(NBTPersist.toJsonObject(tag), State.class);

                if (state != null) {
                    playerUsageHints.addAll(state.playerUsageHints);

                    for (TeamPair pair : state.powerfailInfo) {
                        TeamInfo teamInfo = new TeamInfo();
                        powerfailInfo.put(pair.owner, teamInfo);

                        for (Int2ObjectMap.Entry<DimState> dimState : pair.teamState.byWorld.int2ObjectEntrySet()) {
                            DimensionInfo dimInfo = new DimensionInfo();
                            teamInfo.byWorld.put(dimState.getIntKey(), dimInfo);

                            for (Powerfail powerfail : dimState.getValue().powerfails) {
                                dimInfo.byCoord
                                    .put(CoordinatePacker.pack(powerfail.x, powerfail.y, powerfail.z), powerfail);
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                GTMod.GT_FML_LOGGER.warn("Could not load powerfail data", t);
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
            State state = new State();

            state.playerUsageHints.addAll(playerUsageHints);

            powerfailInfo.forEach((machineOwner, teamInfo) -> {
                TeamState teamState = new TeamState();

                state.powerfailInfo.add(new TeamPair(machineOwner, teamState));

                for (Int2ObjectMap.Entry<DimensionInfo> dimInfo : teamInfo.byWorld.int2ObjectEntrySet()) {
                    DimState dimState = new DimState();

                    dimState.powerfails.addAll(dimInfo.getValue().byCoord.values());

                    if (!dimState.powerfails.isEmpty()) {
                        teamState.byWorld.put(dimInfo.getIntKey(), dimState);
                    }
                }
            });

            JsonElement json = GSON.toJsonTree(state);
            NBTTagCompound nbt = (NBTTagCompound) NBTPersist.toNbt(json);
            // noinspection unchecked
            tag.tagMap.putAll(nbt.tagMap);
        }
    }
}
