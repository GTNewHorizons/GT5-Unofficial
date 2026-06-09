package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessComputation;

import java.util.UUID;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class WirelessComputationPacket {

    public boolean wirelessEnabled = false;

    // The main idea: 'Clearing' the computation net advances the index and sets the computation stored
    // for this index to zero. Uploading is always done to the current index, downloading is always done from the
    // other index. This is essentially just a double buffered computation storage. The reason for this is that
    // every upload needs to be done before every download happens.
    private final long[] computationStored = new long[] { 0, 0 };
    private int currentIndex = 0;
    private long lastUpdateTick = -1;

    private int uploadIndex() {
        return currentIndex;
    }

    private int downloadIndex() {
        return (currentIndex + 1) % 2;
    }

    public long getAvailableComputationStored() {
        return computationStored[downloadIndex()];
    }

    private long download(long dataIn, long aTick) {
        if (!wirelessEnabled) return 0L;

        // If the net hasn't been updated yet this tick, make sure to do so
        if (lastUpdateTick < aTick) {
            this.update();
            lastUpdateTick = aTick;
        }

        long avail = computationStored[downloadIndex()];
        if (avail <= 0) return 0L;

        long take = Math.min(dataIn, avail);
        computationStored[downloadIndex()] -= take;
        if (computationStored[downloadIndex()] < 0) computationStored[downloadIndex()] = 0;

        return take;
    }

    private void update() {
        // The reason we want this complex index cycling system is because hatches may upload and download computation
        // in the same tick as the currently stored computation is cleared. To avoid interruptions, we want to
        // try to double buffer these updates. This means that we keep two computation values around, and every update
        // we only clear one of them.

        // Remove downloaded computation previous index (which is also the next index since there are only two),
        // then remove the leftover from current index.
        computationStored[downloadIndex()] = 0;
        currentIndex = (currentIndex + 1) % 2;
    }

    private void setWirelessEnabled(boolean wirelessEnabled) {
        this.wirelessEnabled = wirelessEnabled;
    }

    private void upload(long dataOut, long aTick) {
        // If the net hasn't been updated yet this tick, make sure to do so
        if (lastUpdateTick < aTick) {
            this.update();
            lastUpdateTick = aTick;
        }
        // Store computation that is uploaded internally
        computationStored[uploadIndex()] += dataOut;
    }

    public static long downloadData(UUID userId, long dataIn, long aTick) {
        return getPacketByUserId(userId).download(dataIn, aTick);
    }

    public static void uploadData(UUID userId, long dataOut, long aTick) {
        getPacketByUserId(userId).upload(dataOut, aTick);
    }

    public static void enableWirelessNetWork(IGregTechTileEntity entity) {
        getPacketByUserId(entity.getOwnerUuid()).setWirelessEnabled(true);
    }

    public static void disableWirelessNetWork(IGregTechTileEntity entity) {
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
