package com.github.technus.tectech.thing.metaTileEntity.pipe;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.network.PipeActivityMessage;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.network.PipeActivityPacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gregtech.common.GT_Client;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_EM.EMCandyActive;
import static com.github.technus.tectech.thing.metaTileEntity.pipe.GT_MetaTileEntity_Pipe_EM.EMcandy;
import static gregtech.api.enums.Dyes.MACHINE_METAL;

public class GT_MetaTileEntity_Pipe_Energy extends MetaPipeEntity implements IConnectsToEnergyTunnel,IActivePipe {
    private static Textures.BlockIcons.CustomIcon EMpipe;
    public byte connectionCount = 0;

    private boolean activity,active;

    public GT_MetaTileEntity_Pipe_Energy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public GT_MetaTileEntity_Pipe_Energy(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_Energy(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_LASER");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[]{new GT_RenderedTexture(EMpipe), new GT_RenderedTexture(getActive()?EMCandyActive:EMcandy, Dyes.getModulation(aColorIndex, MACHINE_METAL.getRGBA()))};
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, byte b, ItemStack itemStack) {
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
    }

    @Override
    public boolean renderInside(byte b) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Laser tunneling device.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Bright Vacuum!!!",
                EnumChatFormatting.AQUA + "Must be painted to work",
                EnumChatFormatting.AQUA + "Do not cross,split or turn"
        };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        onPostTick(aBaseMetaTileEntity, 31);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick & 31) == 31) {
                if(activity){
                    if(TecTech.RANDOM.nextInt(31)==0) {
                        setActive(true);
                    }
                    activity=false;
                }else if(getActive()){
                    if(TecTech.RANDOM.nextInt(31)==0) {
                        setActive(false);
                    }
                }
                mConnections = 0;
                connectionCount = 0;
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (byte b0 = 0, b1; b0 < 6; b0++) {
                    b1 = GT_Utility.getOppositeSide(b0);
                    //if (!aBaseMetaTileEntity.getCoverBehaviorAtSide(b0).alwaysLookConnected(b0, aBaseMetaTileEntity.getCoverIDAtSide(b0), aBaseMetaTileEntity.getCoverDataAtSide(b0), aBaseMetaTileEntity)) {
                    TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(b0);
                    if (tTileEntity instanceof IColoredTileEntity) {
                        //if (aBaseMetaTileEntity.getColorization() >= 0) {
                        byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                        if (tColor != aBaseMetaTileEntity.getColorization()) {
                            continue;
                        }
                        //}
                    }
                    if (tTileEntity instanceof IConnectsToEnergyTunnel && ((IConnectsToEnergyTunnel) tTileEntity).canConnect(b1)) {
                        mConnections |= 1 << b0;
                        connectionCount++;
                    } else if (tTileEntity instanceof IGregTechTileEntity && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof IConnectsToEnergyTunnel) {
                        if (//((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(b1).alwaysLookConnected(b1, ((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(b1), ((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(b1), ((IGregTechTileEntity) tTileEntity)) ||
                                ((IConnectsToEnergyTunnel) ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()).canConnect(b1)) {
                            mConnections |= 1 << b0;
                            connectionCount++;
                        }
                    }
                    //}
                    //else {
                    //    mConnections |= (1 << b0);
                    //    if (mOld != mConnections) {
                    //        connectionCount++;
                    //        mOld = mConnections;
                    //    }
                    //}
                }
            }

        } else if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public void setActive(boolean state){
        this.active=state;
        IGregTechTileEntity base=getBaseMetaTileEntity();
        if(base.isServerSide()) {
            PipeActivityPacketDispatcher.INSTANCE.sendToAllAround(new PipeActivityMessage.PipeActivityData(this),
                    base.getWorld().provider.dimensionId,
                    base.getXCoord(),
                    base.getYCoord(),
                    base.getZCoord(),
                    256);
        }
    }

    @Override
    public boolean getActive() {
        return active;
    }

    @Override
    public boolean canConnect(byte side) {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - 0.5f)/2;
        float tSide0 = tSpace;
        float tSide1 = 1f - tSpace;
        float tSide2 = tSpace;
        float tSide3 = 1f - tSpace;
        float tSide4 = tSpace;
        float tSide5 = 1f - tSpace;

        if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 0) != 0){tSide0=tSide2=tSide4=0;tSide3=tSide5=1;}
        if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 1) != 0){tSide2=tSide4=0;tSide1=tSide3=tSide5=1;}
        if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 2) != 0){tSide0=tSide2=tSide4=0;tSide1=tSide5=1;}
        if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 3) != 0){tSide0=tSide4=0;tSide1=tSide3=tSide5=1;}
        if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 4) != 0){tSide0=tSide2=tSide4=0;tSide1=tSide3=1;}
        if(getBaseMetaTileEntity().getCoverIDAtSide((byte) 5) != 0){tSide0=tSide2=0;tSide1=tSide3=tSide5=1;}

        byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
        if((tConn & 1 << ForgeDirection.DOWN.ordinal()) != 0) {
            tSide0 = 0f;
        }
        if((tConn & 1 << ForgeDirection.UP.ordinal()) != 0) {
            tSide1 = 1f;
        }
        if((tConn & 1 << ForgeDirection.NORTH.ordinal()) != 0) {
            tSide2 = 0f;
        }
        if((tConn & 1 << ForgeDirection.SOUTH.ordinal()) != 0) {
            tSide3 = 1f;
        }
        if((tConn & 1 << ForgeDirection.WEST.ordinal()) != 0) {
            tSide4 = 0f;
        }
        if((tConn & 1 << ForgeDirection.EAST.ordinal()) != 0) {
            tSide5 = 1f;
        }

        return AxisAlignedBB.getBoundingBox(aX + tSide4, aY + tSide0, aZ + tSide2, aX + tSide5, aY + tSide1, aZ + tSide3);
    }

    @Override
    public float getThickNess() {
        if(GT_Mod.instance.isClientSide() && GT_Client.hideValue==1) {
            return 0.0625F;
        }
        return 0.5f;
    }

    public void markUsed() {
        this.activity = true;
    }
}