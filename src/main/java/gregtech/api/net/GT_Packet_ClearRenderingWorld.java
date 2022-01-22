package gregtech.api.net;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_RenderingWorld;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

/**
 * Server -> Client : ask to clear rendering world
 */
public class GT_Packet_ClearRenderingWorld extends GT_Packet_New {
    protected int mX;
    protected short mY;
    protected int mZ;

    protected int blockId;
    protected byte meta;

    protected EntityPlayerMP mPlayer;

    public GT_Packet_ClearRenderingWorld() {
        super(true);
    }

    public GT_Packet_ClearRenderingWorld(int mX, short mY, int mZ, int blockId, byte meta) {
        super(false);
        this.mX = mX;
        this.mY = mY;
        this.mZ = mZ;

        this.blockId = blockId;
        this.meta = meta;
    }
    public GT_Packet_ClearRenderingWorld(Block block, byte meta, ICoverable tile) {
        super(false);
        this.mX = tile.getXCoord();
        this.mY = tile.getYCoord();
        this.mZ = tile.getZCoord();

        this.blockId = Block.getIdFromBlock(block);
        this.meta = meta;
    }

    @Override
    public byte getPacketID() {
        return 18;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeInt(blockId);
        aOut.writeByte(meta);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_ClearRenderingWorld(
                aData.readInt(),
                aData.readShort(),
                aData.readInt(),

                aData.readInt(),
                aData.readByte()
            );
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (aWorld == null) return;
        GT_RenderingWorld.getInstance().unregister(mX, mY, mZ, Block.getBlockById(blockId), meta);
    }
}
