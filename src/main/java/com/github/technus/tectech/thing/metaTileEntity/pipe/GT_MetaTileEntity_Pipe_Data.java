package com.github.technus.tectech.thing.metaTileEntity.pipe;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.pipe.IActivePipe;
import com.github.technus.tectech.mechanics.pipe.IConnectsToDataPipe;
import com.github.technus.tectech.mechanics.pipe.PipeActivityMessage;
import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.GT_Client;

/**
 * Created by Tec on 26.02.2017.
 */
public class GT_MetaTileEntity_Pipe_Data extends MetaPipeEntity implements IConnectsToDataPipe, IActivePipe {

    private static Textures.BlockIcons.CustomIcon EMpipe;
    private static Textures.BlockIcons.CustomIcon EMbar, EMbarActive;
    public byte connectionCount = 0;

    private boolean active;

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
        EMbarActive = new Textures.BlockIcons.CustomIcon("iconsets/EM_BAR_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { new GT_RenderedTexture(EMpipe),
            new GT_RenderedTexture(
                getActive() ? EMbarActive : EMbar,
                Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA())) };
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        active = nbtTagCompound.getBoolean("eActive");
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setBoolean("eActive", active);
    }

    @Override
    public boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.pipe.datastream.desc.0"), // Advanced
                                                                                                                     // data
                                                                                                                     // transmission
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("gt.blockmachines.pipe.datastream.desc.1"), // Don't stare at the beam!
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.datastream.desc.2"), // Must be
                                                                                                   // painted to
                                                                                                   // work
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.datastream.desc.3") // Do not cross or
                                                                                                  // split
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
                if (TecTech.RANDOM.nextInt(15) == 0) {
                    NetworkDispatcher.INSTANCE.sendToAllAround(
                        new PipeActivityMessage.PipeActivityData(this),
                        aBaseMetaTileEntity.getWorld().provider.dimensionId,
                        aBaseMetaTileEntity.getXCoord(),
                        aBaseMetaTileEntity.getYCoord(),
                        aBaseMetaTileEntity.getZCoord(),
                        256);
                }
                if (active) {
                    active = false;
                }
                mConnections = 0;
                connectionCount = 0;
                byte myColor = aBaseMetaTileEntity.getColorization();
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    final ForgeDirection oppositeSide = side.getOpposite();
                    TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
                    if (tTileEntity instanceof IConnectsToDataPipe) {
                        byte tColor = ((IConnectsToDataPipe) tTileEntity).getColorization();
                        if (tColor != myColor) {
                            continue;
                        }
                        if (((IConnectsToDataPipe) tTileEntity).canConnectData(oppositeSide)) {
                            mConnections |= 1 << side.ordinal();
                            connectionCount++;
                        }
                    } else if (tTileEntity instanceof IGregTechTileEntity) {
                        IMetaTileEntity meta = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                        if (meta instanceof IConnectsToDataPipe) {
                            byte tColor = ((IConnectsToDataPipe) meta).getColorization();
                            if (tColor != myColor) {
                                continue;
                            }
                            if (((IConnectsToDataPipe) meta).canConnectData(oppositeSide)) {
                                mConnections |= 1 << side.ordinal();
                                connectionCount++;
                            }
                        }
                    }
                }
            }
        } else if (aBaseMetaTileEntity.isClientSide() && GT_Client.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return true;
    }

    @Override
    public IConnectsToDataPipe getNext(IConnectsToDataPipe source) {
        if (connectionCount != 2) {
            return null;
        }
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((mConnections & 1 << side.ordinal()) == 0) {
                continue; // if not connected continue
            }
            TileEntity next = getBaseMetaTileEntity().getTileEntityAtSide(side);
            if (next instanceof IConnectsToDataPipe && next != source) {
                if (((IConnectsToDataPipe) next).isDataInputFacing(side.getOpposite())) {
                    return (IConnectsToDataPipe) next;
                }
            } else if (next instanceof IGregTechTileEntity) {
                IMetaTileEntity meta = ((IGregTechTileEntity) next).getMetaTileEntity();
                if (meta instanceof IConnectsToDataPipe connecsToPipe && meta != source) {
                    if (meta instanceof GT_MetaTileEntity_Pipe_Data pipeData && pipeData.connectionCount == 2) {
                        pipeData.markUsed();
                        return connecsToPipe;
                    }
                    if (connecsToPipe.isDataInputFacing(side.getOpposite())) {
                        return connecsToPipe;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - 0.375f) / 2;
        float tSide0 = tSpace;
        float tSide1 = 1f - tSpace;
        float tSide2 = tSpace;
        float tSide3 = 1f - tSpace;
        float tSide4 = tSpace;
        float tSide5 = 1f - tSpace;

        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.DOWN) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.UP) != 0) {
            tSide2 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.NORTH) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.SOUTH) != 0) {
            tSide0 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.WEST) != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide3 = 1;
        }
        if (getBaseMetaTileEntity().getCoverIDAtSide(ForgeDirection.EAST) != 0) {
            tSide0 = tSide2 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }

        byte tConn = ((BaseMetaPipeEntity) getBaseMetaTileEntity()).mConnections;
        if ((tConn & 1 << ForgeDirection.DOWN.ordinal()) != 0) {
            tSide0 = 0f;
        }
        if ((tConn & 1 << ForgeDirection.UP.ordinal()) != 0) {
            tSide1 = 1f;
        }
        if ((tConn & 1 << ForgeDirection.NORTH.ordinal()) != 0) {
            tSide2 = 0f;
        }
        if ((tConn & 1 << ForgeDirection.SOUTH.ordinal()) != 0) {
            tSide3 = 1f;
        }
        if ((tConn & 1 << ForgeDirection.WEST.ordinal()) != 0) {
            tSide4 = 0f;
        }
        if ((tConn & 1 << ForgeDirection.EAST.ordinal()) != 0) {
            tSide5 = 1f;
        }

        return AxisAlignedBB
            .getBoundingBox(aX + tSide4, aY + tSide0, aZ + tSide2, aX + tSide5, aY + tSide1, aZ + tSide3);
    }

    @Override
    public float getThickNess() {
        if (GT_Mod.instance.isClientSide() && GT_Client.hideValue == 1) {
            return 0.0625F;
        }
        return 0.375f;
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return true;
    }

    @Override
    public byte getColorization() {
        return getBaseMetaTileEntity().getColorization();
    }

    @Override
    public void markUsed() {
        this.active = true;
    }

    @Override
    public void setActive(boolean state) {
        if (state != active) {
            active = state;
            getBaseMetaTileEntity().issueTextureUpdate();
        }
    }

    @Override
    public boolean getActive() {
        return active;
    }

}
