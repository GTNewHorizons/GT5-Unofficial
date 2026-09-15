package gregtech.api.net;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.GTMod;
import gregtech.api.util.GTByteBuffer;
import gregtech.common.data.drone.CameraViewportManager;
import io.netty.buffer.ByteBuf;

public class PacketObserveMachine extends GTPacket {

    private int dim;
    private long centreCoord;
    private long machineCoord;
    private boolean isObserving;
    private double camX, camY, camZ;
    private float yaw;
    private EntityPlayerMP player;

    public NBTTagCompound statusTag;
    public long hoveredCoord = CameraViewportManager.NULL_COORD;

    public PacketObserveMachine() {}

    public PacketObserveMachine(int dim, long centreCoord, long machineCoord, boolean isObserving, double camX,
        double camY, double camZ, float yaw) {
        this.dim = dim;
        this.centreCoord = centreCoord;
        this.machineCoord = machineCoord;
        this.isObserving = isObserving;
        this.camX = camX;
        this.camY = camY;
        this.camZ = camZ;
        this.yaw = yaw;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.OBSERVE_MACHINE.id;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeLong(centreCoord);
        buf.writeLong(machineCoord);
        buf.writeBoolean(isObserving);
        buf.writeDouble(camX);
        buf.writeDouble(camY);
        buf.writeDouble(camZ);
        buf.writeFloat(yaw);
        buf.writeLong(hoveredCoord);
        boolean hasNBT = (statusTag != null);
        buf.writeBoolean(hasNBT);
        if (hasNBT) {
            ByteBufUtils.writeTag(buf, statusTag);
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buf) {
        int dim = buf.readInt();
        long centreCoord = buf.readLong();
        long machineCoord = buf.readLong();
        boolean isObs = buf.readBoolean();
        double camX = buf.readDouble();
        double camY = buf.readDouble();
        double camZ = buf.readDouble();
        float yaw = buf.readFloat();
        long hCoord = buf.readLong();

        NBTTagCompound tag = null;
        boolean hasNBT = buf.readBoolean();
        if (hasNBT) {
            tag = GTByteBuffer.readCompoundTagFromGreggyByteBuf(buf);
        }

        PacketObserveMachine pkt = new PacketObserveMachine(
            dim,
            centreCoord,
            machineCoord,
            isObs,
            camX,
            camY,
            camZ,
            yaw);
        pkt.hoveredCoord = hCoord;
        pkt.statusTag = tag;
        return pkt;
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer handlerPlayServer) {
            player = handlerPlayServer.playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess world) {
        if (player == null) {
            if (this.statusTag != null && GTMod.proxy.cameraViewportManager != null) {
                GTMod.proxy.cameraViewportManager.setObservedMachineStatus(this.statusTag);
            }
            return;
        }
        UUID playerUUID = player.getUniqueID();

        if (!isObserving) {
            CameraViewportManager.ObservationSession session = CameraViewportManager.sessions.remove(playerUUID);
            if (session != null) {
                session.cleanup(player);
            }
        } else {
            CameraViewportManager.ObservationSession session = CameraViewportManager.sessions.get(playerUUID);
            if (session == null) {
                session = new CameraViewportManager.ObservationSession(dim, centreCoord, machineCoord);
                CameraViewportManager.sessions.put(playerUUID, session);
                session.init(player);
            }
            session.update(player, camX, camY, camZ, yaw, hoveredCoord);
        }
    }
}
