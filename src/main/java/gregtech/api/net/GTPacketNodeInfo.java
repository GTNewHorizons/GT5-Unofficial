package gregtech.api.net;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.MTEResearchCompleter;
import io.netty.buffer.ByteBuf;

public class GTPacketNodeInfo extends GTPacket {

    private int mBlockX, mBlockY, mBlockZ, mDim, nodeDistance, nodeColor;

    public GTPacketNodeInfo() {
        super();
    }

    public GTPacketNodeInfo(int mBlockX, int mBlockY, int mBlockZ, int mDim, int nodeDistance, int nodeColor) {
        this.mBlockX = mBlockX;
        this.mBlockY = mBlockY;
        this.mBlockZ = mBlockZ;
        this.mDim = mDim;
        this.nodeDistance = nodeDistance;
        this.nodeColor = nodeColor;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.NODE_INFO.id;
    }

    @Override
    public void encode(ByteBuf buf) {
        buf.writeInt(mBlockX);
        buf.writeInt(mBlockY);
        buf.writeInt(mBlockZ);
        buf.writeInt(mDim);
        buf.writeInt(nodeDistance);
        buf.writeInt(nodeColor);
    }

    @Override
    public GTPacket decode(ByteArrayDataInput buf) {
        return new GTPacketNodeInfo(
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt());
    }

    @Override
    public void process(IBlockAccess world) {
        if (world instanceof WorldClient worldClient) {
            if (worldClient.blockExists(mBlockX, mBlockY, mBlockZ)) {
                TileEntity tileEntity = world.getTileEntity(mBlockX, mBlockY, mBlockZ);
                if (tileEntity instanceof IGregTechTileEntity igtte
                    && igtte.getMetaTileEntity() instanceof MTEResearchCompleter completer) {
                    completer.setNodeValues(nodeDistance, nodeColor);
                }
            }
        }
    }
}
