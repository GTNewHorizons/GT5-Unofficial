package gregtech.common.net;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.IHasFluidDisplayItem;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_New;
import io.netty.buffer.ByteBuf;

public class MessageUpdateFluidDisplayItem extends GT_Packet_New {

    private int mBlockX, mBlockY, mBlockZ, mDim;

    public MessageUpdateFluidDisplayItem() {
        super(true);
    }

    public MessageUpdateFluidDisplayItem(int mBlockX, int mBlockY, int mBlockZ, int mDim) {
        super(false);
        this.mBlockX = mBlockX;
        this.mBlockY = mBlockY;
        this.mBlockZ = mBlockZ;
        this.mDim = mDim;
    }

    @Override
    public byte getPacketID() {
        return 8;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mBlockX);
        aOut.writeInt(mBlockY);
        aOut.writeInt(mBlockZ);
        aOut.writeInt(mDim);
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new MessageUpdateFluidDisplayItem(aData.readInt(), aData.readInt(), aData.readInt(), aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        WorldServer world = DimensionManager.getWorld(mDim);
        if (world != null) {
            if (world.blockExists(mBlockX, mBlockY, mBlockZ)) {
                TileEntity tileEntity = world.getTileEntity(mBlockX, mBlockY, mBlockZ);
                if (tileEntity instanceof IGregTechTileEntity gtTile) {
                    if (gtTile.getMetaTileEntity() instanceof IHasFluidDisplayItem) {
                        ((IHasFluidDisplayItem) gtTile.getMetaTileEntity()).updateFluidDisplayItem();
                    }
                }
            }
        }
    }
}
