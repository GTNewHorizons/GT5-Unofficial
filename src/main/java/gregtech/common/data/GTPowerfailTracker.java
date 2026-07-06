package gregtech.common.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import cpw.mods.fml.common.eventhandler.EventPriority;
import gregtech.api.net.GTPacket;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.world.WorldEvent;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.teams.ITeamData;
import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataTransferReason;
import com.gtnewhorizon.gtnhlib.teams.TeamManager;
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
import gregtech.api.net.GTPacketClearPowerfail;
import gregtech.api.net.GTPacketOnPowerfail;
import gregtech.api.net.GTPacketUpdatePowerfails;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTTextBuilder;
import gregtech.api.util.Localized;
import gregtech.common.config.Gregtech;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class GTPowerfailTracker {

    public static final String DATA_NAME = "gt.powerfails";

    /**
     * GTNHLib Team or player with pending powerfail syncs. Used to debounce updates to once per tick.
     * player uuids are secondary and only added specifically when a player & not the entire team should
     * receive updates
     */
    private static final HashSet<UUID> pendingUpdates = new HashSet<>();

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

    private static Team getMachineOwner(UUID player) {
        return TeamManager.getTeamByPlayer(player);
    }

    public void createPowerfailEvent(IGregTechTileEntity igte) {
        if (!Gregtech.machines.enablePowerfailNotifications) return;

        Team owner = getMachineOwner(igte.getOwnerUuid());

        PowerfailData data = (PowerfailData) owner.getData(GTPowerfailTracker.DATA_NAME);
        if (data == null) return;

        var dimensionInfo = data.byWorld
            .computeIfAbsent(igte.getWorld().provider.dimensionId, _ -> new PowerfailData.DimensionInfo());

        long coord = CoordinatePacker.pack(igte.getXCoord(), igte.getYCoord(), igte.getZCoord());

        Powerfail powerfail = dimensionInfo.byCoord.computeIfAbsent(coord, _ -> new Powerfail());

        powerfail.update(igte);
        GTPacket packet = new GTPacketOnPowerfail(powerfail);

        TeamManager.forEachOnlineTeamMember(
            owner,
            player -> GTValues.NW.sendToPlayer(packet, player));

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
        Team owner = getMachineOwner(ownerId);

        PowerfailData data = (PowerfailData) owner.getData(GTPowerfailTracker.DATA_NAME);
        if (data == null) return;

        var dimensionInfo = data.byWorld.get(worldId);

        if (dimensionInfo == null) return;

        long coord = CoordinatePacker.pack(x, y, z);

        Powerfail p = dimensionInfo.byCoord.remove(coord);

        owner.markDirty();

        GTPacket packet = new GTPacketClearPowerfail(p);

        if (p != null) {
            TeamManager.forEachOnlineTeamMember(
                owner,
                player -> GTValues.NW.sendToPlayer(packet, player));
        }
    }

    public List<Powerfail> getPowerfails(UUID player, OptionalInt inDim) {
        Team owner = getMachineOwner(player);
        PowerfailData data = (PowerfailData) owner.getData(GTPowerfailTracker.DATA_NAME);
        if (data == null) return Collections.emptyList();

        if (inDim.isPresent()) {
            return GTDataUtils.ofNullableStream(data.byWorld.get(inDim.getAsInt()))
                .flatMap(
                    dim -> dim.byCoord.values()
                        .stream())
                .collect(Collectors.toList());
        } else {
            return GTDataUtils.ofNullableStream(data.byWorld)
                .flatMap(
                    dimInfos -> dimInfos.values()
                        .stream())
                .flatMap(
                    dim -> dim.byCoord.values()
                        .stream())
                .collect(Collectors.toList());
        }
    }

    public void clearPowerfails(EntityPlayerMP player, OptionalInt inDim) {
        Team owner = getMachineOwner(
            player.getGameProfile()
                .getId());

        PowerfailData data = (PowerfailData) owner.getData(GTPowerfailTracker.DATA_NAME);
        if (data == null) return;

        if (inDim.isPresent()) {
            data.byWorld.remove(inDim.getAsInt());
        } else {
            data.byWorld.clear();
        }

        owner.markDirty();

        pendingUpdates.add(owner.getTeamId());
    }

    public void sendPlayerPowerfailStatus(EntityPlayerMP player) {
        List<Powerfail> powerfails = getPowerfails(
            player.getGameProfile()
                .getId(),
            OptionalInt.of(player.dimension));

        GTPacketUpdatePowerfails packet = new GTPacketUpdatePowerfails(player.dimension, powerfails);

        GTValues.NW.sendToPlayer(packet, player);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
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

        for (UUID uuid : pendingUpdates) {
            Team teamById = TeamManager.getTeamById(uuid);
            if (teamById != null) {
                TeamManager.forEachOnlineTeamMember(teamById, this::sendPlayerPowerfailStatus);
            } else {
                sendPlayerPowerfailStatus(getPlayer(uuid));
            }
        }

        pendingUpdates.clear();
    }

    private static EntityPlayerMP getPlayer(UUID id) {
        return GTMod.proxy.getPlayerMP(id);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
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

    public static class PowerfailData implements ITeamData {

        private static class DimensionInfo {

            public Long2ObjectOpenHashMap<Powerfail> byCoord = new Long2ObjectOpenHashMap<>();
        }

        final Int2ObjectOpenHashMap<DimensionInfo> byWorld = new Int2ObjectOpenHashMap<>();

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            byWorld.clear();
            byte[] bytes = tag.getByteArray("blob");
            fromBytes(bytes);
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            tag.setByteArray("blob", asBytes());
        }

        @Override
        public void mergeData(Team consumed, Team surviving, ITeamData oldTeamData) {
            if (!(oldTeamData instanceof PowerfailData pfData)) return;

            pfData.byWorld.forEach((dimId, oldDimInfo) -> {
                DimensionInfo dimensionInfo = byWorld.computeIfAbsent(dimId, _ -> new DimensionInfo());
                dimensionInfo.byCoord.putAll(oldDimInfo.byCoord);
            });

            pendingUpdates.remove(consumed.getTeamId());
            TeamManager.forEachOnlineTeamMember(
                consumed,
                player -> pendingUpdates.add(
                    player.getGameProfile()
                        .getId()));
        }

        @Override
        public void transferData(Team prevTeam, Team newTeam, UUID playerId, ITeamData prevTeamData,
            TeamDataTransferReason reason) {
            if (!(prevTeamData instanceof PowerfailData pfData)) return;

            pfData.byWorld.forEach((dimId, oldDimInfo) -> {
                DimensionInfo dimensionInfo = byWorld.computeIfAbsent(dimId, _ -> new DimensionInfo());
                LongList coords = new LongArrayList(16);
                oldDimInfo.byCoord.forEach((packedCoord, powerfail) -> {
                    if (Objects.requireNonNull(powerfail.getMTE())
                        .getBaseMetaTileEntity()
                        .getOwnerUuid() == playerId) {
                        coords.add(packedCoord);
                        dimensionInfo.byCoord.computeIfAbsent(packedCoord, _ -> powerfail);
                    }
                });

                coords.forEach(coord -> oldDimInfo.byCoord.remove(coord));
            });

            pendingUpdates.add(newTeam.getTeamId());
        }

        private byte[] asBytes() {
            if (byWorld.isEmpty()) return new byte[0];

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (GZIPOutputStream gz = new GZIPOutputStream(bos); DataOutputStream out = new DataOutputStream(gz)) {

                out.writeInt(byWorld.size());
                for (Map.Entry<Integer, DimensionInfo> dimEntrySet : byWorld.entrySet()) {
                    int dimId = dimEntrySet.getKey();
                    DimensionInfo dimInfo = dimEntrySet.getValue();

                    out.writeInt(dimId);

                    out.writeInt(dimInfo.byCoord.size());
                    for (Map.Entry<Long, Powerfail> coordEntrySet : dimInfo.byCoord.entrySet()) {
                        long coord = coordEntrySet.getKey();
                        Powerfail powerfail = coordEntrySet.getValue();

                        out.writeLong(coord);
                        out.writeLong(powerfail.lastOccurrence.getTime());
                        out.writeInt(powerfail.count);
                        out.writeInt(powerfail.mteId);
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to encode team powerfail blob", e);
            }

            return bos.toByteArray();
        }

        private void fromBytes(byte[] bytes) {
            if (bytes.length == 0) return;

            try (GZIPInputStream gz = new GZIPInputStream(new ByteArrayInputStream(bytes));
                DataInputStream in = new DataInputStream(gz)) {

                int numDims = in.readInt();
                for (int i = 0; i < numDims; i++) {
                    int dimId = in.readInt();

                    int dimEntries = in.readInt();
                    for (int j = 0; j < dimEntries; j++) {
                        long coord = in.readLong();
                        long last = in.readLong();
                        int count = in.readInt();
                        int mteId = in.readInt();

                        DimensionInfo dimInfo = byWorld.computeIfAbsent(dimId, _ -> new DimensionInfo());
                        Powerfail powerfail = dimInfo.byCoord.computeIfAbsent(coord, _ -> new Powerfail());

                        powerfail.lastOccurrence = new Date(last);
                        powerfail.count = count;
                        powerfail.mteId = mteId;
                        powerfail.setCoord(coord);
                        // inferred from the outer dimension read
                        powerfail.dim = dimId;
                    }
                }
            } catch (IOException e) {
                throw new UncheckedIOException("Failed to decode team powerfail blob", e);
            }
        }
    }
}
