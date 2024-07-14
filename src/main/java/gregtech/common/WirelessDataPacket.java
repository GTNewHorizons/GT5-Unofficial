package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessData;

import java.util.UUID;

import com.github.technus.tectech.mechanics.dataTransport.QuantumDataPacket;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class WirelessDataPacket {

    public boolean wirelessEnabled = false;

    public long latestPacketUpdate = -1000000;
    public long[] latestUpload = new long[20];
    public long[] latestDownload = new long[20];

    public final long[] previewUploaded = new long[20];

    public final long[] previewDownloaded = new long[20];

    public Vec3Impl controllerPosition = null;
    public int loopTags = 0;

    private QuantumDataPacket download(long dataIn) {
        long time = System.currentTimeMillis();
        if (!wirelessEnabled || Math.abs(time - latestPacketUpdate) > 10000 || controllerPosition == null)
            return new QuantumDataPacket(0L);
        latestDownload[loopTags] += dataIn;
        double totalRequired = 1, totalUploaded = 1;
        for (int i = 0; i < 20; i++) {
            totalRequired += latestDownload[i];
            totalUploaded += latestUpload[i];
        }
        long result = (long) (Math.min(1.0, totalUploaded / totalRequired) * dataIn);
        return new QuantumDataPacket(result).unifyTraceWith(controllerPosition);
    }

    private void update(IGregTechTileEntity entity, long aTick) {
        latestPacketUpdate = System.currentTimeMillis();
        loopTags = (loopTags + 1) % 20;
        latestUpload[loopTags] -= previewUploaded[loopTags];
        latestDownload[loopTags] -= previewDownloaded[loopTags];
        previewUploaded[loopTags] = latestUpload[loopTags];
        previewDownloaded[loopTags] = latestDownload[loopTags];
    }

    private void setWirelessEnabled(boolean wirelessEnabled) {
        this.wirelessEnabled = wirelessEnabled;
    }

    private void upload(long dataOut) {
        long time = System.currentTimeMillis();
        if (!wirelessEnabled || Math.abs(time - latestPacketUpdate) > 10000) return;
        latestUpload[loopTags] += dataOut;
    }

    public static QuantumDataPacket downloadData(UUID userId, long dataIn) {
        return getPacketByUserId(userId).download(dataIn);
    }

    public static void uploadData(UUID userId, long dataOut) {
        getPacketByUserId(userId).upload(dataOut);
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

    public static WirelessDataPacket getPacketByUserId(UUID userId) {
        UUID team = SpaceProjectManager.getLeader(userId);
        if (GlobalWirelessData.get(team) == null) {
            GlobalWirelessData.put(team, new WirelessDataPacket());
        }
        return GlobalWirelessData.get(team);
    }
}
