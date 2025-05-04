package gregtech.common.tileentities.machines.multi.nanochip;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.GTClient;
import gregtech.common.tileentities.machines.multi.nanochip.util.IConnectsToVacuumConveyor;
import tectech.TecTech;
import tectech.loader.NetworkDispatcher;
import tectech.mechanics.pipe.IActivePipe;
import tectech.mechanics.pipe.PipeActivityMessage;

public class MTEVacuumConveyorPipe extends MetaPipeEntity implements IConnectsToVacuumConveyor, IActivePipe {

    private static Textures.BlockIcons.CustomIcon EMpipe;
    private static Textures.BlockIcons.CustomIcon EMbar, EMbarActive;
    public byte connectionCount = 0;

    private boolean active;

    public MTEVacuumConveyorPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTEVacuumConveyorPipe(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEVacuumConveyorPipe(mName);
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
        return new ITexture[] { TextureFactory.of(EMpipe), TextureFactory
            .of(getActive() ? EMbarActive : EMbar, Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA())) };
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
        return new String[] {};
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isClientSide()) {
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
                connectionCount = 0;
                byte myColor = aBaseMetaTileEntity.getColorization();
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    final ForgeDirection oppositeSide = side.getOpposite();
                    TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
                    if (tTileEntity instanceof IConnectsToVacuumConveyor) {
                        byte tColor = ((IConnectsToVacuumConveyor) tTileEntity).getColorization();
                        if (tColor != myColor) {
                            continue;
                        }
                        if (((IConnectsToVacuumConveyor) tTileEntity).canConnect(oppositeSide)) {
                            mConnections |= 1 << side.ordinal();
                            connectionCount++;
                        }
                    } else if (tTileEntity instanceof IGregTechTileEntity) {
                        IMetaTileEntity meta = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                        if (meta instanceof IConnectsToVacuumConveyor) {
                            byte tColor = ((IConnectsToVacuumConveyor) meta).getColorization();
                            if (tColor != myColor) {
                                continue;
                            }
                            if (((IConnectsToVacuumConveyor) meta).canConnect(oppositeSide)) {
                                mConnections |= 1 << side.ordinal();
                                connectionCount++;
                            }
                        }
                    }
                }
            }
        } else if (aBaseMetaTileEntity.isClientSide() && GTClient.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return true;
    }

    @Override
    public IConnectsToVacuumConveyor getNext(IConnectsToVacuumConveyor source) {
        if (connectionCount != 2) {
            return null;
        }
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if ((mConnections & 1 << side.ordinal()) == 0) {
                continue; // if not connected continue
            }
            TileEntity next = getBaseMetaTileEntity().getTileEntityAtSide(side);
            if (next instanceof IConnectsToVacuumConveyor && next != source) {
                if (((IConnectsToVacuumConveyor) next).isComponentsInputFacing(side.getOpposite())) {
                    return (IConnectsToVacuumConveyor) next;
                }
            } else if (next instanceof IGregTechTileEntity) {
                IMetaTileEntity meta = ((IGregTechTileEntity) next).getMetaTileEntity();
                if (meta instanceof IConnectsToVacuumConveyor connecsToPipe && meta != source) {
                    if (meta instanceof MTEVacuumConveyorPipe pipeData && pipeData.connectionCount == 2) {
                        pipeData.markUsed();
                        return connecsToPipe;
                    }
                    if (connecsToPipe.isComponentsInputFacing(side.getOpposite())) {
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

        if (getBaseMetaTileEntity().getCoverAtSide(ForgeDirection.DOWN)
            .getCoverID() != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverAtSide(ForgeDirection.UP)
            .getCoverID() != 0) {
            tSide2 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverAtSide(ForgeDirection.NORTH)
            .getCoverID() != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverAtSide(ForgeDirection.SOUTH)
            .getCoverID() != 0) {
            tSide0 = tSide4 = 0;
            tSide1 = tSide3 = tSide5 = 1;
        }
        if (getBaseMetaTileEntity().getCoverAtSide(ForgeDirection.WEST)
            .getCoverID() != 0) {
            tSide0 = tSide2 = tSide4 = 0;
            tSide1 = tSide3 = 1;
        }
        if (getBaseMetaTileEntity().getCoverAtSide(ForgeDirection.EAST)
            .getCoverID() != 0) {
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
    public float getCollisionThickness() {
        if (GTMod.instance.isClientSide() && GTClient.shouldHideThings()) {
            return 0.0625F;
        }
        return 0.375f;
    }

    @Override
    public boolean isComponentsInputFacing(ForgeDirection side) {
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
