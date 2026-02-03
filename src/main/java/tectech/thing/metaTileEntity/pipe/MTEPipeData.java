package tectech.thing.metaTileEntity.pipe;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import tectech.TecTech;
import tectech.loader.NetworkDispatcher;
import tectech.mechanics.pipe.IActivePipe;
import tectech.mechanics.pipe.IConnectsToDataPipe;
import tectech.mechanics.pipe.PipeActivityMessage;
import tectech.util.CommonValues;

/**
 * Created by Tec on 26.02.2017.
 */
public class MTEPipeData extends MetaPipeEntity implements IConnectsToDataPipe, IActivePipe {

    private static IIconContainer EMpipe;
    private static IIconContainer EMbar, EMbarActive;
    public byte connectionCount = 0;

    private boolean active;

    public MTEPipeData(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTEPipeData(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeData(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMpipe = Textures.BlockIcons.CustomIcon.create("iconsets/EM_DATA");
        EMbar = Textures.BlockIcons.CustomIcon.create("iconsets/EM_BAR");
        EMbarActive = Textures.BlockIcons.CustomIcon.create("iconsets/EM_BAR_ACTIVE");
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
        return HarvestTool.WrenchPipeLevel0.toTileEntityBaseType();
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

    public void updateNeighboringNetworks() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            IGregTechTileEntity gregTechTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(side);

            if (gregTechTileEntity != null && gregTechTileEntity.getMetaTileEntity() instanceof MTEPipeData neighbor) {
                neighbor.updateNetwork(true);
            }
        }
    }

    public void updateNetwork(boolean nestedCall) {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();

        active = false;

        mConnections = 0;
        connectionCount = 0;

        byte myColor = aBaseMetaTileEntity.getColorization();
        if (aBaseMetaTileEntity.getColorization() < 0) {
            return;
        }

        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final ForgeDirection oppositeSide = side.getOpposite();
            TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
            if (tTileEntity instanceof IConnectsToDataPipe pipe) {
                byte tColor = pipe.getColorization();
                if (tColor != myColor) {
                    continue;
                }
                if (pipe.canConnectData(oppositeSide)) {
                    mConnections |= side.flag;
                    connectionCount++;
                }
            } else if (tTileEntity instanceof IGregTechTileEntity gregTechTileEntity) {
                IMetaTileEntity meta = gregTechTileEntity.getMetaTileEntity();
                if (meta instanceof IConnectsToDataPipe pipe) {
                    byte tColor = pipe.getColorization();
                    if (tColor != myColor) {
                        continue;
                    }
                    if (pipe.canConnectData(oppositeSide)) {
                        mConnections |= side.flag;
                        connectionCount++;
                    }
                }
            }
        }

        if (!nestedCall) updateNeighboringNetworks();
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        this.updateNetwork(false);
        super.onColorChangeServer(aColor);
    }

    @Override
    public void onBlockDestroyed() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            IGregTechTileEntity gregTechTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(side);

            if (gregTechTileEntity != null && gregTechTileEntity.getMetaTileEntity() instanceof MTEPipeData neighbor
                && neighbor.isConnectedAtSide(side.getOpposite())) {
                neighbor.mConnections &= ~side.getOpposite().flag;
                neighbor.connectionCount--;
            }
        }

        super.onBlockDestroyed();
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        this.updateNetwork(false);
        super.onFirstTick(aBaseMetaTileEntity);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick & 31) == 31) {
                if (TecTech.RANDOM.nextInt(15) == 0) {
                    NetworkDispatcher.INSTANCE.sendToAllAround(
                        new PipeActivityMessage.PipeActivityData(this),
                        new NetworkRegistry.TargetPoint(
                            aBaseMetaTileEntity.getWorld().provider.dimensionId,
                            aBaseMetaTileEntity.getXCoord(),
                            aBaseMetaTileEntity.getYCoord(),
                            aBaseMetaTileEntity.getZCoord(),
                            256));
                }
            }
        } else if (aBaseMetaTileEntity.isClientSide() && GTMod.clientProxy()
            .changeDetected() == 4) {
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
            if ((mConnections & side.flag) == 0) {
                continue; // if not connected continue
            }
            TileEntity next = getBaseMetaTileEntity().getTileEntityAtSide(side);
            if (next instanceof IConnectsToDataPipe connectsToPipe && next != source) {
                if (connectsToPipe.isDataInputFacing(side.getOpposite())) {
                    return connectsToPipe;
                }
            } else if (next instanceof IGregTechTileEntity gregTechTileEntity) {
                IMetaTileEntity meta = gregTechTileEntity.getMetaTileEntity();
                if (meta instanceof IConnectsToDataPipe connectsToPipe && meta != source) {
                    if (meta instanceof MTEPipeData pipeData && pipeData.connectionCount == 2) {
                        pipeData.markUsed();
                        return connectsToPipe;
                    }
                    if (connectsToPipe.isDataInputFacing(side.getOpposite())) {
                        return connectsToPipe;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public float getCollisionThickness() {
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
