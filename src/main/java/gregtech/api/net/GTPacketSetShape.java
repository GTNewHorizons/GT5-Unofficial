package gregtech.api.net;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.google.common.io.ByteArrayDataInput;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.util.GTByteBuffer;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;
import io.netty.buffer.ByteBuf;

public class GTPacketSetShape extends GTPacket {

    protected int mX;
    protected short mY;
    protected int mZ;
    protected int dimId;
    protected ItemStack shape;

    public GTPacketSetShape() {
        super();
    }

    public GTPacketSetShape(MTEHatchExtrusion hatch, ItemStack shape) {
        this(
            hatch.getBaseMetaTileEntity()
                .getXCoord(),
            hatch.getBaseMetaTileEntity()
                .getYCoord(),
            hatch.getBaseMetaTileEntity()
                .getZCoord(),
            shape);
    }

    public GTPacketSetShape(int x, short y, int z, ItemStack shape) {
        super();
        this.mX = x;
        this.mY = y;
        this.mZ = z;
        this.shape = shape;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SET_SHAPE.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);
        ByteBufUtils.writeItemStack(aOut, shape);
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer handler) {
            dimId = handler.playerEntity.dimension;
        } else {
            dimId = -2;
        }
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        return new GTPacketSetShape(
            aData.readInt(),
            aData.readShort(),
            aData.readInt(),
            GTByteBuffer.readItemStackFromGreggyByteBuf(aData));
    }

    @Override
    public void process(IBlockAccess aWorld) {
        World world = DimensionManager.getWorld(dimId);
        if (world == null) return;

        TileEntity tile = world.getTileEntity(mX, mY, mZ);
        if (!(tile instanceof BaseTileEntity baseTile)) return;

        IGregTechTileEntity gregTechTile = baseTile.getIGregTechTileEntityOffset(0, 0, 0);
        ItemStack phantom = MTEHatchExtrusion.findMatchingShape(shape);
        if (phantom == null) return;

        if (gregTechTile != null && gregTechTile.getMetaTileEntity() instanceof MTEHatchExtrusion meta) {
            meta.setInventorySlotContents(MTEHatchExtrusion.shapeSlot, phantom);
            meta.markDirty();
        }
    }
}
