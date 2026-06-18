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
    private int centreX, centreY, centreZ;
    private int machineX, machineY, machineZ;
    private boolean isObserving;
    private double camX, camY, camZ;
    private EntityPlayerMP player;

    public NBTTagCompound statusTag;
    public int hoveredX = -1;
    public int hoveredY = -1;
    public int hoveredZ = -1;

    public PacketObserveMachine() {}

    public PacketObserveMachine(int dim, int centreX, int centreY, int centreZ, int machineX, int machineY,
        int machineZ, boolean isObserving, double camX, double camY, double camZ) {
        this.dim = dim;
        this.centreX = centreX;
        this.centreY = centreY;
        this.centreZ = centreZ;
        this.machineX = machineX;
        this.machineY = machineY;
        this.machineZ = machineZ;
        this.isObserving = isObserving;
        this.camX = camX;
        this.camY = camY;
        this.camZ = camZ;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.OBSERVE_MACHINE.id;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(centreX);
        buf.writeInt(centreY);
        buf.writeInt(centreZ);
        buf.writeInt(machineX);
        buf.writeInt(machineY);
        buf.writeInt(machineZ);
        buf.writeBoolean(isObserving);
        buf.writeDouble(camX);
        buf.writeDouble(camY);
        buf.writeDouble(camZ);
        buf.writeInt(hoveredX);
        buf.writeInt(hoveredY);
        buf.writeInt(hoveredZ);
        boolean hasNBT = (statusTag != null);
        buf.writeBoolean(hasNBT);
        if (hasNBT) {
            ByteBufUtils.writeTag(buf, statusTag);
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buf) {
        int dim = buf.readInt();
        int cX = buf.readInt();
        int cY = buf.readInt();
        int cZ = buf.readInt();
        int mX = buf.readInt();
        int mY = buf.readInt();
        int mZ = buf.readInt();
        boolean isObs = buf.readBoolean();
        double camX = buf.readDouble();
        double camY = buf.readDouble();
        double camZ = buf.readDouble();
        int hX = buf.readInt();
        int hY = buf.readInt();
        int hZ = buf.readInt();

        NBTTagCompound tag = null;
        boolean hasNBT = buf.readBoolean();
        if (hasNBT) {
            tag = GTByteBuffer.readCompoundTagFromGreggyByteBuf(buf);
        }

        PacketObserveMachine pkt = new PacketObserveMachine(dim, cX, cY, cZ, mX, mY, mZ, isObs, camX, camY, camZ);
        pkt.hoveredX = hX;
        pkt.hoveredY = hY;
        pkt.hoveredZ = hZ;
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
                session = new CameraViewportManager.ObservationSession(
                    dim,
                    centreX,
                    centreY,
                    centreZ,
                    machineX,
                    machineY,
                    machineZ);
                CameraViewportManager.sessions.put(playerUUID, session);
                session.init(player);
            }
            session.update(player, camX, camY, camZ, hoveredX, hoveredY, hoveredZ);
        }
    }
}
