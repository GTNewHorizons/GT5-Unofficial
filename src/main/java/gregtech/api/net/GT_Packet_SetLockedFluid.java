package gregtech.api.net;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.interfaces.metatileentity.IFluidLockable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import io.netty.buffer.ByteBuf;

public class GT_Packet_SetLockedFluid extends GT_Packet_New {

    protected int mX;
    protected short mY;
    protected int mZ;

    protected int mFluidID;

    private EntityPlayerMP mPlayer;

    public GT_Packet_SetLockedFluid() {
        super(true);
    }

    public GT_Packet_SetLockedFluid(IGregTechTileEntity aTile, FluidStack aSource) {
        this(aTile.getXCoord(), aTile.getYCoord(), aTile.getZCoord(), aSource.getFluidID());
    }

    public GT_Packet_SetLockedFluid(int x, short y, int z, int aFluidID) {
        super(false);

        this.mX = x;
        this.mY = y;
        this.mZ = z;

        this.mFluidID = aFluidID;
    }

    @Override
    public byte getPacketID() {
        return 14;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(mX);
        aOut.writeShort(mY);
        aOut.writeInt(mZ);

        aOut.writeInt(mFluidID);
    }

    @Override
    public void setINetHandler(INetHandler aHandler) {
        if (aHandler instanceof NetHandlerPlayServer) {
            mPlayer = ((NetHandlerPlayServer) aHandler).playerEntity;
        }
    }

    @Override
    public GT_Packet_New decode(ByteArrayDataInput aData) {
        return new GT_Packet_SetLockedFluid(aData.readInt(), aData.readShort(), aData.readInt(), aData.readInt());
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (mPlayer == null) return;
        World world = mPlayer.worldObj;
        TileEntity tile = world.getTileEntity(mX, mY, mZ);
        if (!(tile instanceof IGregTechTileEntity) || ((IGregTechTileEntity) tile).isDead()) return;
        IMetaTileEntity mte = ((IGregTechTileEntity) tile).getMetaTileEntity();
        if (!(mte instanceof IFluidLockable mteToLock)) return;
        Fluid tFluid = FluidRegistry.getFluid(mFluidID);
        if (tFluid == null) return;
        if (!mteToLock.allowChangingLockedFluid(tFluid.getName())) return;

        mteToLock.lockFluid(true);
        mteToLock.setLockedFluidName(tFluid.getName());
        GT_Utility.sendChatToPlayer(
                mPlayer,
                String.format(
                        GT_LanguageManager.addStringLocalization(
                                "Interaction_DESCRIPTION_Index_151.4",
                                "Successfully locked Fluid to %s",
                                false),
                        new FluidStack(tFluid, 1).getLocalizedName()));

        mteToLock.onFluidLockPacketReceived(tFluid.getName());
    }
}
