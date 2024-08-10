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
import com.github.technus.tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import com.github.technus.tectech.mechanics.pipe.PipeActivityMessage;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyTunnel;
import com.github.technus.tectech.util.CommonValues;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.common.GT_Client;

public class GT_MetaTileEntity_Pipe_EnergyMirror extends MetaPipeEntity implements IConnectsToEnergyTunnel, IActivePipe {

    static Textures.BlockIcons.CustomIcon EMcandy, EMCandyActive;
    private static Textures.BlockIcons.CustomIcon EMpipe;
    public byte connectionCount = 0;
    private ForgeDirection[] connectedSides = {null, null};
    private ForgeDirection chainedFrontFacing = null;

    private boolean active;

    public GT_MetaTileEntity_Pipe_EnergyMirror(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public GT_MetaTileEntity_Pipe_EnergyMirror(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_Pipe_EnergyMirror(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMcandy = new Textures.BlockIcons.CustomIcon("iconsets/EM_CANDY");
        EMCandyActive = new Textures.BlockIcons.CustomIcon("iconsets/EM_CANDY_ACTIVE");
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_LASERMIRROR");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { new GT_RenderedTexture(EMpipe),
            new GT_RenderedTexture(
                getActive() ? EMCandyActive : EMcandy,
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
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.pipe.energymirror.desc.0"),
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("gt.blockmachines.pipe.energystream.desc.1"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.energystream.desc.2"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.energymirror.desc.1")
        };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (getBaseMetaTileEntity().isClientSide()) {
            NetworkDispatcher.INSTANCE.sendToServer(new PipeActivityMessage.PipeActivityQuery(this));
        }
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
                connectedSides[0] = null;
                connectedSides[1] = null;
                connectionCount = 0;
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    // We only allow a single bend
                    if (connectionCount < 2) {
                        final ForgeDirection oppositeSide = side.getOpposite();
                        TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
                        if (tTileEntity instanceof IColoredTileEntity) {
                            // if (aBaseMetaTileEntity.getColorization() >= 0) {
                            byte tColor = ((IColoredTileEntity) tTileEntity).getColorization();
                            if (tColor != aBaseMetaTileEntity.getColorization()) {
                                continue;
                            }
                            // }
                        }
                        if (tTileEntity instanceof PowerLogicHost) {
                            PowerLogic logic = ((PowerLogicHost) tTileEntity).getPowerLogic(oppositeSide);
                            if (logic != null && logic.canUseLaser()) {
                                mConnections |= 1 << side.ordinal();
                                connectedSides[connectionCount] = side;
                                connectionCount++;
                                continue;
                            }
                        }
                        if (tTileEntity instanceof IConnectsToEnergyTunnel
                            && ((IConnectsToEnergyTunnel) tTileEntity).canConnect(oppositeSide)) {
                            mConnections |= 1 << side.ordinal();
                            connectionCount++;
                        } else if (tTileEntity instanceof IGregTechTileEntity
                            && ((IGregTechTileEntity) tTileEntity).getMetaTileEntity() instanceof IConnectsToEnergyTunnel) {
                                if (((IConnectsToEnergyTunnel) ((IGregTechTileEntity) tTileEntity).getMetaTileEntity())
                                    .canConnect(oppositeSide)) {
                                    mConnections |= 1 << side.ordinal();
                                    connectedSides[connectionCount] = side;
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

    public ForgeDirection getBendDirection(ForgeDirection dir) {       
        //for (ForgeDirection bendDirection : ForgeDirection.VALID_DIRECTIONS) {
        //    if ((mConnections & (1 << bendDirection.ordinal())) != 0) {
        //        if (bendDirection != dir) return bendDirection;
        //    }
        //}
        //int bendDirection = mConnections & ~(1 << dir.ordinal());
        //if(bendDirection != 0) return ForgeDirection.VALID_DIRECTIONS[(int) (Math.log(bendDirection)/Math.log(2))];
        //return null;
        for (ForgeDirection bendDir : connectedSides) {
            if (bendDir != dir) {
                chainedFrontFacing = bendDir.getOpposite();
                return bendDir;
            }
        }
        return null;
    }

    public ForgeDirection getChainedFrontFacing() {
        return chainedFrontFacing;
    }

    public IGregTechTileEntity bendAround(ForgeDirection inputSide) {
        byte color = getBaseMetaTileEntity().getColorization();
        if (color < 0) {
            return null;
        } else {
            ForgeDirection direction = getBendDirection(inputSide);
            if (direction == null) {
                return null;
            }
            ForgeDirection opposite = direction.getOpposite();
            for (short dist = 1; dist < 1000; dist++) {

                IGregTechTileEntity tGTTileEntity = getBaseMetaTileEntity()
                    .getIGregTechTileEntityAtSideAndDistance(direction, dist);
                if (tGTTileEntity != null && tGTTileEntity.getColorization() == color) {
                    IMetaTileEntity aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                    if (aMetaTileEntity != null) {
                        // If we hit a mirror, use the mirror's view instead
                        if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_EnergyMirror tMirror) {
                            tGTTileEntity = tMirror.bendAround(opposite);
                            if (tGTTileEntity == null) {
                                break;
                            } else {
                                aMetaTileEntity = tGTTileEntity.getMetaTileEntity();
                                chainedFrontFacing = tMirror.getChainedFrontFacing();
                                opposite = chainedFrontFacing;
                            }
                        }

                        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_EnergyTunnel
                            && opposite == tGTTileEntity.getFrontFacing()) {
                            return tGTTileEntity;
                        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Pipe_Energy) {
                            if (((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).connectionCount < 2) {
                                return null;
                            } else {
                                ((GT_MetaTileEntity_Pipe_Energy) aMetaTileEntity).markUsed();
                            }
                        } else {
                            return null;
                        }
                    } else {
                        if (tGTTileEntity instanceof PowerLogicHost) {
                            PowerLogic logic = ((PowerLogicHost) tGTTileEntity).getPowerLogic(opposite);
                            if (logic == null || !logic.canUseLaser() || opposite != tGTTileEntity.getFrontFacing()) {
                                return tGTTileEntity;
                            }
                        }
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
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

    @Override
    public void markUsed() {
        this.active = true;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return true;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ) {
        float tSpace = (1f - 0.5f) / 2;
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
        return 0.6f;
    }

}
