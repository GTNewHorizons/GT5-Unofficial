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
import gregtech.api.interfaces.tileentity.IColoredTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import tectech.TecTech;
import tectech.loader.NetworkDispatcher;
import tectech.mechanics.pipe.IActivePipe;
import tectech.mechanics.pipe.IConnectsToEnergyTunnel;
import tectech.mechanics.pipe.PipeActivityMessage;
import tectech.util.CommonValues;

public class MTEPipeLaser extends MetaPipeEntity implements IConnectsToEnergyTunnel, IActivePipe {

    static IIconContainer EMcandy, EMCandyActive;
    private static IIconContainer EMpipe;
    public byte connectionCount = 0;

    private boolean active;

    public MTEPipeLaser(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTEPipeLaser(String aName) {
        super(aName, 0);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEPipeLaser(mName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        EMcandy = Textures.BlockIcons.CustomIcon.create("iconsets/EM_CANDY");
        EMCandyActive = Textures.BlockIcons.CustomIcon.create("iconsets/EM_CANDY_ACTIVE");
        EMpipe = Textures.BlockIcons.CustomIcon.create("iconsets/EM_LASER");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { TextureFactory.of(EMpipe), TextureFactory
            .of(getActive() ? EMCandyActive : EMcandy, Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA())) };
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

    public void updateNeighboringNetworks() {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            IGregTechTileEntity gregTechTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityAtSide(side);

            if (gregTechTileEntity != null && gregTechTileEntity.getMetaTileEntity() instanceof MTEPipeLaser neighbor) {
                neighbor.updateNetwork(true);
            }
        }
    }

    public void updateNetwork(boolean nestedCall) {
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();

        active = false;

        mConnections = 0;
        connectionCount = 0;

        if (aBaseMetaTileEntity.getColorization() < 0) {
            return;
        }
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final ForgeDirection oppositeSide = side.getOpposite();

            TileEntity tTileEntity = aBaseMetaTileEntity.getTileEntityAtSide(side);
            if (tTileEntity instanceof IColoredTileEntity coloredTileEntity) {
                byte tColor = coloredTileEntity.getColorization();
                if (tColor != aBaseMetaTileEntity.getColorization()) {
                    continue;
                }
            }
            if (tTileEntity instanceof IConnectsToEnergyTunnel tunnel && tunnel.canConnect(oppositeSide)) {
                mConnections |= side.flag;
                connectionCount++;
            } else if (tTileEntity instanceof IGregTechTileEntity gregTechTileEntity
                && gregTechTileEntity.getMetaTileEntity() instanceof IConnectsToEnergyTunnel tunnel) {
                    if (tunnel.canConnect(oppositeSide)) {
                        mConnections |= side.flag;
                        connectionCount++;
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

            if (gregTechTileEntity != null && gregTechTileEntity.getMetaTileEntity() instanceof MTEPipeLaser neighbor
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
    public float getCollisionThickness() {
        return 0.5f;
    }

}
