package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessComputation;

import java.util.UUID;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import tectech.mechanics.dataTransport.QuantumDataPacket;

public class WirelessComputationPacket {

    public boolean wirelessEnabled = false;

    // The main idea: 'Clearing' the computation net advances the index and sets the computation stored
    // for this index to zero. Uploading is always done to the current index, but data can be downloaded from
    // both indices
    private final long[] computationStored = new long[] { 0, 0 };
    private long computationDownloaded = 0;
    private int currentIndex = 0;

    public Vec3Impl controllerPosition = null;

    public long getTotalComputationStored() {
        return computationStored[0] + computationStored[1];
    }

    private long getAvailableComputationStored() {
        return getTotalComputationStored() - computationDownloaded;
    }

    private QuantumDataPacket download(long dataIn, long aTick) {
        if (!wirelessEnabled || controllerPosition == null) return new QuantumDataPacket(0L);

        // If we have enough computation 'stored', download it
        // Note that this means that if you do not have enough computation to go to all
        // destinations, it won't be distributed equally. This is fine.
        // This also means that if you don't have enough computation for a hatch, it will not receive any computation
        // at all. This is also fine.
        if (getAvailableComputationStored() >= dataIn) {
            computationDownloaded += dataIn;
            return new QuantumDataPacket(dataIn);
        } else return new QuantumDataPacket(0L);
    }

    private void update(IGregTechTileEntity entity, long aTick) {
        // The reason we want this complex index cycling system is because hatches may upload and download computation
        // in the same tick as the currently stored computation is cleared. To avoid interruptions, we want to
        // try to double buffer these updates. This means that we keep two computation values around, and every update
        // we only clear one of them. Only the most recent entry can be used for uploading computation, but we allow
        // downloading computation from both the current and the previous index.

        // Remove downloaded computation previous index (which is also the next index since there are only two),
        // then remove the leftover from current index.
        int nextIndex = (currentIndex + 1) % 2;
        long availableInPrevious = computationStored[nextIndex];
        // Clear stored computation for the next index, since we don't want to allow players to accumulate
        // computation in their wireless network indefinitely. This would allow for cheesing research by passively
        // banking computation and then setting the input hatch to a high value when the computation is needed.
        computationStored[nextIndex] = 0;
        if (computationDownloaded > availableInPrevious) {
            long toDrainFromCurrent = computationDownloaded - availableInPrevious;
            computationStored[currentIndex] -= toDrainFromCurrent;
        }
        // Reset our current tally of downloaded computation
        computationDownloaded = 0;
        // Now advance the current index to the next index
        currentIndex = nextIndex;
    }

    private void setWirelessEnabled(boolean wirelessEnabled) {
        this.wirelessEnabled = wirelessEnabled;
    }

    private void upload(long dataOut, long aTick) {
        // Store computation that is uploaded internally
        computationStored[currentIndex] += dataOut;
    }

    public static QuantumDataPacket downloadData(UUID userId, long dataIn, long aTick) {
        return getPacketByUserId(userId).download(dataIn, aTick);
    }

    public static void uploadData(UUID userId, long dataOut, long aTick) {
        getPacketByUserId(userId).upload(dataOut, aTick);
    }

    public static void updatePacket(IGregTechTileEntity entity, long aTick) {
        getPacketByUserId(entity.getOwnerUuid()).update(entity, aTick);
    }

    public static boolean enableWirelessNetWork(IGregTechTileEntity entity) {
        var packet = getPacketByUserId(entity.getOwnerUuid());
        Vec3Impl pos = new Vec3Impl(entity.getXCoord(), entity.getYCoord(), entity.getZCoord());
        if (packet.wirelessEnabled && packet.controllerPosition != null
            && pos.compareTo(packet.controllerPosition) != 0) return false;
        getPacketByUserId(entity.getOwnerUuid()).setWirelessEnabled(true);
        if (packet.controllerPosition == null) {
            packet.controllerPosition = new Vec3Impl(entity.getXCoord(), entity.getYCoord(), entity.getZCoord());
        }
        return true;
    }

    public static void disableWirelessNetWork(IGregTechTileEntity entity) {
        var packet = getPacketByUserId(entity.getOwnerUuid());
        Vec3Impl pos = new Vec3Impl(entity.getXCoord(), entity.getYCoord(), entity.getZCoord());
        if (packet.controllerPosition != null && packet.controllerPosition.compareTo(pos) != 0) return;
        getPacketByUserId(entity.getOwnerUuid()).setWirelessEnabled(false);
    }

    public static WirelessComputationPacket getPacketByUserId(UUID userId) {
        UUID team = SpaceProjectManager.getLeader(userId);
        if (GlobalWirelessComputation.get(team) == null) {
            GlobalWirelessComputation.put(team, new WirelessComputationPacket());
        }
        return GlobalWirelessComputation.get(team);
    }
}
