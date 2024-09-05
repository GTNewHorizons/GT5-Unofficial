package tectech.thing.metaTileEntity.pipe;

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

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
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
import gregtech.api.objects.GTRenderedTexture;
import gregtech.common.GTClient;
import tectech.TecTech;
import tectech.loader.NetworkDispatcher;
import tectech.mechanics.pipe.IActivePipe;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.mechanics.pipe.PipeActivityMessage;
import tectech.util.CommonValues;

public class MTEPipeEnergy extends MetaPipeEntity implements IConnectsToEnergyTunnel, IActivePipe {

    static Textures.BlockIcons.CustomIcon EMcandy, EMCandyActive;
    private static Textures.BlockIcons.CustomIcon EMpipe;
    public byte connectionCount = 0;

    private boolean active;

    public MTEPipeEnergy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTEPipeEnergy(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeEnergy(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMcandy = new Textures.BlockIcons.CustomIcon("iconsets/EM_CANDY");
        EMCandyActive = new Textures.BlockIcons.CustomIcon("iconsets/EM_CANDY_ACTIVE");
        EMpipe = new Textures.BlockIcons.CustomIcon("iconsets/EM_LASER");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { new GTRenderedTexture(EMpipe),
            new GTRenderedTexture(
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
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.pipe.energystream.desc.0"), // Laser
                                                                                                                       // tunneling
                                                                                                                       // device.
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD
                + translateToLocal("gt.blockmachines.pipe.energystream.desc.1"), // Bright Vacuum!!!
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.energystream.desc.2"), // Must be
                                                                                                     // painted to
                                                                                                     // work
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.pipe.energystream.desc.3") // Do not split
                                                                                                    // or turn
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
                if (aBaseMetaTileEntity.getColorization() < 0) {
                    return;
                }
                for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    final ForgeDirection oppositeSide = side.getOpposite();
                    // if (!aBaseMetaTileEntity.getCoverBehaviorAtSide(b0).alwaysLookConnected(b0,
                    // aBaseMetaTileEntity.getCoverIDAtSide(b0), aBaseMetaTileEntity.getCoverDataAtSide(b0),
                    // aBaseMetaTileEntity)) {
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
                                connectionCount++;
                            }
                        }
                }
            }

        } else if (aBaseMetaTileEntity.isClientSide() && GTClient.changeDetected == 4) {
            aBaseMetaTileEntity.issueTextureUpdate();
        }
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
        if (GTMod.instance.isClientSide() && GTClient.hideValue == 1) {
            return 0.0625F;
        }
        return 0.5f;
    }

}
