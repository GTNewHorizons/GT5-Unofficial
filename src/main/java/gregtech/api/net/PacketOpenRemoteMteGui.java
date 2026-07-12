package gregtech.api.net;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.modularui2.ProxiedMteGui;
import gregtech.common.data.drone.CameraViewportManager;
import io.netty.buffer.ByteBuf;

public class PacketOpenRemoteMteGui extends GTPacket {

    private long MTEcoord;
    private boolean openedFromItem;
    private EntityPlayerMP player;

    public PacketOpenRemoteMteGui() {}

    public PacketOpenRemoteMteGui(long coord, boolean openedFromItem) {
        MTEcoord = coord;
        this.openedFromItem = openedFromItem;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.OPEN_REMOTE_MTE_GUI.id;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeLong(MTEcoord);
        buf.writeBoolean(openedFromItem);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buf) {
        long coord = buf.readLong();
        boolean openedFromItem = buf.readBoolean();
        return new PacketOpenRemoteMteGui(coord, openedFromItem);
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
            TileEntity te = serverWorld.getTileEntity(
                CoordinatePacker.unpackX(MTEcoord),
                CoordinatePacker.unpackY(MTEcoord),
                CoordinatePacker.unpackZ(MTEcoord));
            if (te instanceof BaseMetaTileEntity baseMte) {
                IMetaTileEntity mte = baseMte.getMetaTileEntity();
                if (mte instanceof MetaTileEntity realMte) {
                    ProxiedMteGui.open(realMte, player);
                }
            }
        }
    }
}
