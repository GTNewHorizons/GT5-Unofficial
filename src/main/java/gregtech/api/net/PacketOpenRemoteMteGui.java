package gregtech.api.net;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.ProxiedMteGui;
import gregtech.common.data.drone.CameraViewportManager;
import io.netty.buffer.ByteBuf;

public class PacketOpenRemoteMteGui extends GTPacket {

    private int x, y, z;
    private boolean openedFromItem;
    private EntityPlayerMP player;

    public PacketOpenRemoteMteGui() {}

    public PacketOpenRemoteMteGui(int x, int y, int z, boolean openedFromItem) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.openedFromItem = openedFromItem;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.OPEN_REMOTE_MTE_GUI.id;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeBoolean(openedFromItem);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buf) {
        int x = buf.readInt();
        int y = buf.readInt();
        int z = buf.readInt();
        boolean openedFromItem = buf.readBoolean();
        return new PacketOpenRemoteMteGui(x, y, z, openedFromItem);
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer handlerPlayServer) {
            player = handlerPlayServer.playerEntity;
        }
    }

    @Override
    public void process(IBlockAccess world) {
        if (player == null) return;
        UUID playerUUID = player.getUniqueID();
        CameraViewportManager.ObservationSession session = CameraViewportManager.sessions.get(playerUUID);
        if (session != null) {
            session.openedFromItem = this.openedFromItem;
            World serverWorld = player.getServerForPlayer();
            TileEntity te = serverWorld.getTileEntity(x, y, z);
            if (te instanceof BaseMetaTileEntity baseMte) {
                IMetaTileEntity mte = baseMte.getMetaTileEntity();
                if (mte instanceof MetaTileEntity realMte) {
                    ProxiedMteGui.open(realMte, player);
                }
            }
        }
    }
}
