package gregtech.common.data;

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
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;

import org.jetbrains.annotations.Nullable;

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
import gregtech.api.enums.ChatMessage;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacket;
import gregtech.api.net.GTPacketClearPowerfail;
import gregtech.api.net.GTPacketOnPowerfail;
import gregtech.api.net.GTPacketUpdatePowerfails;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTTextBuilder;
import gregtech.api.util.Localized;
import gregtech.api.util.NBTPersist;
import gregtech.common.config.Gregtech;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class GTPowerfailTracker {

    public static final String DATA_NAME = "gt.powerfails";

    /** The powerfail state. When a world is loaded, this will never be null. */
    private SaveData instance;

    /** Players with pending powerfail syncs. Used to debounce updates to once per tick. */
    private final HashSet<UUID> pendingUpdates = new HashSet<>();

    /** Something that can own a machine, usually a player but may also be a team. */
    interface MachineOwner {

        Set<UUID> getPlayers();

        boolean isOnline();

        default List<EntityPlayerMP> getPlayerEntities() {
            return getPlayers().stream()
                .map(GTPowerfailTracker::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }

        default void sendPacket(GTPacket packet) {
            for (EntityPlayerMP player : getPlayerEntities()) {
                GTValues.NW.sendToPlayer(packet, player);
            }
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
    }

    @Desugar
    private record TeamOwner(UUID leader) implements MachineOwner {

        @Override
        public Set<UUID> getPlayers() {
            return SpaceProjectManager.getTeamMembers(leader);
        }

        @Override
        public boolean isOnline() {
            return !getPlayers().isEmpty();
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

        public Powerfail copy() {
            Powerfail copy = new Powerfail();
            copy.dim = dim;
            copy.x = x;
            copy.y = y;
            copy.z = z;
            copy.mteId = mteId;
            copy.count = count;
            copy.lastOccurrence = (Date) lastOccurrence.clone();
            return copy;
        }

        public long getCoord() {
            return CoordinatePacker.pack(x, y, z);
        }

        public void setCoord(long coord) {
            x = CoordinatePacker.unpackX(coord);
            y = CoordinatePacker.unpackY(coord);
            z = CoordinatePacker.unpackZ(coord);
        }

        public float getSecs() {
            return lastOccurrence == null ? 0 : (new Date().getTime() - lastOccurrence.getTime()) / 1000f;
        }

        public void update(IGregTechTileEntity igte) {
            this.dim = igte.getWorld().provider.dimensionId;
            this.x = igte.getXCoord();
            this.y = igte.getYCoord();
            this.z = igte.getZCoord();
            this.mteId = igte.getMetaTileID();
            this.count++;
            this.lastOccurrence = new Date();
        }

        public @Nullable IMetaTileEntity getMTE() {
            return GTDataUtils.getIndexSafe(GregTechAPI.METATILEENTITIES, mteId);
        }

        public String getMTEName() {
            IMetaTileEntity imte = getMTE();

            return imte == null ? "<error>" : imte.getLocalName();
        }

        public Localized getDurationText() {
            float secs = getSecs();

            int value;
            String key;

            if (secs > 60f * 60f * 24f) {
                value = (int) (secs / 60f / 60f / 24f);
                key = "GT5U.gui.text.day";
            } else if (secs > 60f * 60f) {
                value = (int) (secs / 60f / 60f);
                key = "GT5U.gui.text.hour";
            } else if (secs > 60f) {
                value = (int) (secs / 60f);
                key = "GT5U.gui.text.minute";
            } else {
                value = (int) secs;
                key = "GT5U.gui.text.second";
            }

            if (value != 1) key += ".plural";

            return new Localized(key, value).setBase(GTTextBuilder.NUMERIC);
        }

        public Localized toSummary() {
            // spotless:off
            return new GTTextBuilder(ChatMessage.PowerfailWaypoint).setBase(EnumChatFormatting.GRAY)
                .addName(getMTEName())
                .addNumber(count)
                .addLocalized(getDurationText())
                .toLocalized();
            // spotless:on
        }

        public Localized toDescription() {
            // spotless:off
            return new GTTextBuilder(ChatMessage.PowerfailDescription).setBase(EnumChatFormatting.GRAY)
                .addName(getMTEName())
                .addCoord(x, y, z)
                .addLocalized(new Localized(ChatMessage.Dimension, dim).setBase(GTTextBuilder.VALUE))
                .addNumber(count)
                .addLocalized(getDurationText())
                .toLocalized();
            // spotless:on
        }
    }

    private static MachineOwner getMachineOwner(UUID player) {
        UUID leader = SpaceProjectManager.getLeader(player);

        if (leader == null) return new PlayerOwner(player);

        return new TeamOwner(leader);
    }

    public void createPowerfailEvent(IGregTechTileEntity igte) {
        if (!Gregtech.machines.enablePowerfailNotifications) return;

        MachineOwner owner = getMachineOwner(igte.getOwnerUuid());

        TeamInfo teamInfo = instance.powerfailInfo.computeIfAbsent(owner, ignored -> new TeamInfo());

        DimensionInfo dimensionInfo = teamInfo.byWorld
            .computeIfAbsent(igte.getWorld().provider.dimensionId, ignored -> new DimensionInfo());

        long coord = CoordinatePacker.pack(igte.getXCoord(), igte.getYCoord(), igte.getZCoord());

        Powerfail powerfail = dimensionInfo.byCoord.computeIfAbsent(coord, ignored -> new Powerfail());

        powerfail.update(igte);

        owner.sendPacket(new GTPacketOnPowerfail(powerfail));

        instance.markDirty();
    }

    public void removePowerfailEvents(IGregTechTileEntity igte) {
        removePowerfailEvents(
            igte.getOwnerUuid(),
            igte.getWorld().provider.dimensionId,
            igte.getXCoord(),
            igte.getYCoord(),
            igte.getZCoord());
    }

    public void removePowerfailEvents(UUID ownerId, int worldId, int x, int y, int z) {
        MachineOwner owner = getMachineOwner(ownerId);

        TeamInfo teamInfo = instance.powerfailInfo.get(owner);

        if (teamInfo == null) return;

        DimensionInfo dimensionInfo = teamInfo.byWorld.get(worldId);

        if (dimensionInfo == null) return;

        long coord = CoordinatePacker.pack(x, y, z);

        Powerfail p = dimensionInfo.byCoord.remove(coord);

        if (p != null) {
            owner.sendPacket(new GTPacketClearPowerfail(p));

            instance.markDirty();
        }
    }

    public List<Powerfail> getPowerfails(UUID player, OptionalInt inDim) {
        MachineOwner owner = getMachineOwner(player);

        // spotless:off
        return GTDataUtils.ofNullableStream(instance.powerfailInfo.get(owner))
            .flatMap(team -> inDim.isPresent() ? GTDataUtils.ofNullableStream(team.byWorld.get(inDim.getAsInt())) : team.byWorld.values().stream())
            .flatMap(dim -> dim.byCoord.values().stream())
            .collect(Collectors.toList());
        // spotless:on
    }

    public void clearPowerfails(EntityPlayerMP player, OptionalInt inDim) {
        MachineOwner owner = getMachineOwner(
            player.getGameProfile()
                .getId());

        TeamInfo teamInfo = instance.powerfailInfo.get(owner);

        if (teamInfo == null) return;

        if (inDim.isPresent()) {
            teamInfo.byWorld.remove(inDim.getAsInt());
        } else {
            teamInfo.byWorld.clear();
        }

        pendingUpdates.addAll(owner.getPlayers());
        instance.markDirty();
    }

    public void sendPlayerPowerfailStatus(EntityPlayerMP player) {
        List<Powerfail> powerfails = getPowerfails(
            player.getGameProfile()
                .getId(),
            OptionalInt.of(player.dimension));

        GTPacketUpdatePowerfails packet = new GTPacketUpdatePowerfails(player.dimension, powerfails);

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
            new GTTextBuilder(ChatMessage.PowerfailGreeting).setBase(EnumChatFormatting.GRAY)
                .addNumber(powerfails.size())
                .toLocalized()
                .sendChat(event.player);

            ChatMessage.PowerfailCommandHint.send(event.player);
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

        for (UUID playerId : pendingUpdates) {
            EntityPlayerMP player = getPlayer(playerId);

            if (player == null) continue;

            sendPlayerPowerfailStatus(player);
        }

        pendingUpdates.clear();
    }

    private static EntityPlayerMP getPlayer(UUID id) {
        return GTMod.proxy.getPlayerMP(id);
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            instance = (SaveData) event.world.mapStorage.loadData(SaveData.class, DATA_NAME);
            if (instance == null) {
                instance = new SaveData(DATA_NAME);
                event.world.mapStorage.setData(DATA_NAME, instance);
            }
            instance.markDirty();

            pendingUpdates.clear();

            // Now that everything is loaded, queue an update for all connected players (there should be none, but let's
            // be safe)
            // spotless:off
            MinecraftServer.getServer()
                .getConfigurationManager()
                .playerEntityList
                .stream()
                .map(p -> p.getGameProfile().getId())
                .forEach(pendingUpdates::add);
            // spotless:on
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

    public static class SaveData extends WorldSavedData {

        final Map<MachineOwner, TeamInfo> powerfailInfo = new HashMap<>();

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
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            powerfailInfo.clear();

            try {
                State state = GSON.fromJson(NBTPersist.toJsonObject(tag), State.class);

                if (state != null) {
                    for (TeamPair pair : state.powerfailInfo) {
                        TeamInfo teamInfo = new TeamInfo();
                        powerfailInfo.put(pair.owner, teamInfo);

                        for (Int2ObjectMap.Entry<DimState> dimState : pair.teamState.byWorld.int2ObjectEntrySet()) {
                            DimensionInfo dimInfo = new DimensionInfo();
                            teamInfo.byWorld.put(dimState.getIntKey(), dimInfo);

                            for (Powerfail powerfail : dimState.getValue().powerfails) {
                                dimInfo.byCoord.put(powerfail.getCoord(), powerfail);
                            }
                        }
                    }
                }
            } catch (Exception t) {
                GTMod.GT_FML_LOGGER.warn("Could not load powerfail data", t);
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            State state = new State();

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
