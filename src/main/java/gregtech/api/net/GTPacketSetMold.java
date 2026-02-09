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
import gregtech.api.interfaces.tileentity.IHasInventory;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.util.GTByteBuffer;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;
import io.netty.buffer.ByteBuf;

public class GTPacketSetMold extends GTPacket {

    protected int mX;
    protected short mY;
    protected int mZ;
    protected int dimId;
    protected ItemStack mold;

    public GTPacketSetMold() {
        super();
    }

    public GTPacketSetMold(MTEHatchSolidifier hatch, ItemStack mold) {
        this(
            hatch.getBaseMetaTileEntity()
                .getXCoord(),
            hatch.getBaseMetaTileEntity()
                .getYCoord(),
            hatch.getBaseMetaTileEntity()
                .getZCoord(),
            mold);
    }

    public GTPacketSetMold(int x, short y, int z, ItemStack mold) {
        super();
        this.mX = x;
        this.mY = y;
        this.mZ = z;
        this.mold = mold;
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SET_MOLD.id;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);
        ByteBufUtils.writeItemStack(aOut, mold);
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
        return new GTPacketSetMold(
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
        ItemStack phantom = MTEHatchSolidifier.findMatchingMold(mold);
        if (phantom == null) return;

        if (tile instanceof IHasInventory inv) {
            inv.setInventorySlotContents(MTEHatchSolidifier.moldSlot, phantom);
            baseTile.markDirty();
            world.markBlockForUpdate(mX, mY, mZ);
        }

        if (gregTechTile != null && gregTechTile.getMetaTileEntity() instanceof MTEHatchSolidifier meta) {
            meta.setInventorySlotContents(MTEHatchSolidifier.moldSlot, phantom);
            meta.markDirty();
        }
    }
}
