package gregtech.common.gui.modularui.synchandler;

import java.util.Objects;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

public class TeslaNodeData {

    Vec3Impl coords;
    Integer usedAmps;

    public TeslaNodeData(Vec3Impl coords, Integer usedAmps) {
        this.coords = coords;
        this.usedAmps = usedAmps;
    }

    public Vec3Impl getCoords() {
        return coords;
    }

    public Integer getUsedAmps() {
        return usedAmps;
    }

    public static TeslaNodeData deserialize(PacketBuffer packetBuffer) {
        Vec3Impl coords = new Vec3Impl(packetBuffer.readInt(), packetBuffer.readInt(), packetBuffer.readInt());
        return new TeslaNodeData(coords, packetBuffer.readInt());
    }

    public static void serialize(PacketBuffer packetBuffer, TeslaNodeData teslaNodeData) {
        Vec3Impl coords = teslaNodeData.coords;
        packetBuffer.writeInt(coords.get0());
        packetBuffer.writeInt(coords.get1());
        packetBuffer.writeInt(coords.get2());
        packetBuffer.writeInt(teslaNodeData.usedAmps);
    }

    public boolean areEqual(@NotNull TeslaNodeData teslaNodeData) {
        return Objects.equals(coords, teslaNodeData.coords) && Objects.equals(usedAmps, teslaNodeData.usedAmps);
    }
}
