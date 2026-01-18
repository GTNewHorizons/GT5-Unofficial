
package gregtech.common.tileentities.machines.multi.nanochip;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import java.util.Collection;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryElement;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryGrid;
import gregtech.common.tileentities.machines.multi.nanochip.factory.VacuumFactoryNetwork;
import tectech.thing.metaTileEntity.pipe.MTEBaseFactoryPipe;

public class MTEVacuumConveyorPipe extends MTEBaseFactoryPipe implements VacuumFactoryElement {

    private static Textures.BlockIcons.CustomIcon CCPipe;
    private static Textures.BlockIcons.CustomIcon CCBarOverlay, CCBarOverlayActive;
    public VacuumFactoryNetwork network;

    public MTEVacuumConveyorPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEVacuumConveyorPipe(MTEVacuumConveyorPipe prototype) {
        super(prototype);
    }

    @Override
    public boolean getGT6StyleConnection() {
        return GTMod.gregtechproxy.gt6Pipe;
    }

    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEVacuumConveyorPipe(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        CCPipe = new Textures.BlockIcons.CustomIcon("iconsets/CC_PIPE");
        CCBarOverlay = new Textures.BlockIcons.CustomIcon("iconsets/CC_BAR_OVERLAY");
        CCBarOverlayActive = new Textures.BlockIcons.CustomIcon("iconsets/CC_BAR_OVERLAY_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int colorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { TextureFactory.of(CCPipe),
            TextureFactory.of(
                getActive() ? CCBarOverlayActive : CCBarOverlay,
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
    public boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Must be " + MTENanochipAssemblyComplexGui.coloredString() + " to work",
            "Transports" + EnumChatFormatting.YELLOW
                + " Circuit Components "
                + EnumChatFormatting.GRAY
                + "between Vacuum Conveyor Hatches" };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        VacuumFactoryGrid.INSTANCE.addElement(this);
        onPostTick(aBaseMetaTileEntity, 31);
        super.onFirstTick(aBaseMetaTileEntity);
    }

    // @Override
    // public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
    // if (aBaseMetaTileEntity.isServerSide()) {
    // if ((aTick & 31) == 31) {
    // if (TecTech.RANDOM.nextInt(15) == 0) {
    // NetworkDispatcher.INSTANCE.sendToAllAround(
    // new PipeActivityMessage.PipeActivityData(this),
    // new NetworkRegistry.TargetPoint(
    // aBaseMetaTileEntity.getWorld().provider.dimensionId,
    // aBaseMetaTileEntity.getXCoord(),
    // aBaseMetaTileEntity.getYCoord(),
    // aBaseMetaTileEntity.getZCoord(),
    // 256));
    // }
    //
    // mConnections = 0;
    // byte myColor = aBaseMetaTileEntity.getColorization();
    // if (aBaseMetaTileEntity.getColorization() < 0) {
    // return;
    // }
    // for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
    //
    // }
    // } else if (aBaseMetaTileEntity.isClientSide() && GTMod.clientProxy()
    // .changeDetected() == 4) {
    // aBaseMetaTileEntity.issueTextureUpdate();
    // }
    // }
    // }

    @Override
    public float getCollisionThickness() {
        if (GTMod.GT.isClientSide() && GTMod.clientProxy()
            .shouldHideThings()) {
            return 0.0625F;
        }
        return 0.375f;
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return true;
    }

    @Override
    public byte getColorization() {
        return getBaseMetaTileEntity().getColorization();
    }

    @Override
    public void getNeighbours(Collection<VacuumFactoryElement> neighbours) {
        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte) {
                if (igte.getColorization() == base.getColorization()) {
                    if (igte.getMetaTileEntity() instanceof VacuumFactoryElement element) {
                        if (element.canConnectOnSide(dir.getOpposite())) {
                            neighbours.add(element);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onNeighbourChanged(VacuumFactoryElement neighbour) {
        mCheckConnections = true;
    }

    @Override
    protected void checkConnections() {
        mConnections = 0;

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(dir) instanceof IGregTechTileEntity igte) {
                if (igte.getColorization() == base.getColorization()) {
                    if (igte.getMetaTileEntity() instanceof VacuumFactoryElement element) {
                        if (element.canConnectOnSide(dir.getOpposite())) {
                            mConnections |= dir.flag;
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void checkActive() {
        mIsActive = getBaseMetaTileEntity().getTimer() % 200 > 100;
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        VacuumFactoryGrid.INSTANCE.removeElement(this);
    }

    @Override
    public VacuumFactoryNetwork getNetwork() {
        return network;
    }

    @Override
    public void setNetwork(VacuumFactoryNetwork network) {
        this.network = network;
    }

    @Override
    public void onColorChangeServer(byte aColor) {
        VacuumFactoryGrid.INSTANCE.addElement(this);
    }
}
