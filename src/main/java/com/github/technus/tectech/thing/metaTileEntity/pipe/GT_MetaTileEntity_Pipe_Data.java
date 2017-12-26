package com.github.technus.tectech.thing.metaTileEntity.pipe;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputData;
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

import static gregtech.api.enums.Dyes.MACHINE_METAL;

/**
 * Created by Tec on 26.02.2017.
 */
public class GT_MetaTileEntity_Pipe_Data extends MetaPipeEntity implements iConnectsToDataPipe {
    private static Textures.BlockIcons.CustomIcon EMpipe;
    private static Textures.BlockIcons.CustomIcon EMbar;
    public byte connectionCount = 0;

    public GT_MetaTileEntity_Pipe_Data(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public GT_MetaTileEntity_Pipe_Data(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_Data(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_DATA");
        EMbar = new Textures.BlockIcons.CustomIcon("iconsets/EM_BAR");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aConnections, byte aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[]{new GT_RenderedTexture(EMpipe), new GT_RenderedTexture(EMbar, Dyes.getModulation(aColorIndex, MACHINE_METAL.getRGBA()))};
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
                "Advanced data transmission",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Don't stare at the beam!",
                EnumChatFormatting.AQUA + "Must be painted to work",
                EnumChatFormatting.AQUA + "Do not cross or split"
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
                    if (tTileEntity instanceof iConnectsToDataPipe && ((iConnectsToDataPipe) tTileEntity).canConnect(b1)) {
                        mConnections |= 1 << b0;
                        connectionCount++;
                    } else if (tTileEntity instanceof IGregTechTileEntity && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof iConnectsToDataPipe) {
                        if (//((IGregTechTileEntity) tTileEntity).getCoverBehaviorAtSide(b1).alwaysLookConnected(b1, ((IGregTechTileEntity) tTileEntity).getCoverIDAtSide(b1), ((IGregTechTileEntity) tTileEntity).getCoverDataAtSide(b1), ((IGregTechTileEntity) tTileEntity)) ||
                                ((iConnectsToDataPipe) ((IGregTechTileEntity) tTileEntity).getMetaTileEntity()).canConnect(b1)) {
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
    public boolean canConnect(byte side) {
        return true;
    }

    @Override
    public iConnectsToDataPipe getNext(iConnectsToDataPipe source) {
        if (connectionCount != 2) {
            return null;
        }
        for (byte b = 0; b < 6; b++) {
            if ((mConnections & 1 << b) == 0) {
                continue;//if not connected continue
            }
            IGregTechTileEntity next = getBaseMetaTileEntity().getIGregTechTileEntityAtSide(b);
            if (next == null) {
                continue;
            }
            IMetaTileEntity meta = next.getMetaTileEntity();
            if (meta instanceof iConnectsToDataPipe && meta != source) {
                if (meta instanceof GT_MetaTileEntity_Hatch_InputData) {
                    return (iConnectsToDataPipe) meta;
                }
                if (meta instanceof GT_MetaTileEntity_Pipe_Data &&
                        ((GT_MetaTileEntity_Pipe_Data) meta).connectionCount == 2) {
                    return (iConnectsToDataPipe) meta;
                }
            }
        }
        return null;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - 0.375f)/2;
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
        return 0.375f;
    }
}
