
package gregtech.common.tileentities.machines.multi.nanochip;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import java.util.Collection;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.ToolModes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Other;
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
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Must be " + MTENanochipAssemblyComplexGui.TOOLTIP_COLORED + " to work",
            "Transports" + EnumChatFormatting.YELLOW
                + " Circuit Components "
                + EnumChatFormatting.GRAY
                + "between Vacuum Conveyor Hatches" };
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        VacuumFactoryGrid.INSTANCE.updateElement(this);
    }

    @Override
    public void onUnload() {
        VacuumFactoryGrid.INSTANCE.removeElement(this);
        super.onUnload();
    }

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

        if (base == null || base.isDead() || base.getColorization() == -1) return;

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
    public void onEdgeAdded(VacuumFactoryElement adjacent) {
        VacuumFactoryElement.super.onEdgeAdded(adjacent);
        mCheckConnections = true;
    }

    @Override
    public void onEdgeRemoved(VacuumFactoryElement adjacent) {
        VacuumFactoryElement.super.onEdgeRemoved(adjacent);
        mCheckConnections = true;
    }

    @Override
    public void onEdgeChanged(VacuumFactoryElement adjacent) {
        VacuumFactoryElement.super.onEdgeChanged(adjacent);
        mCheckConnections = true;
    }

    @Override
    protected void checkConnections() {
        mConnections = 0;

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead() || base.getColorization() == -1) return;

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

    public void connectPipeOnSide(ForgeDirection side, EntityPlayer entityPlayer) {
        if (!isConnectedAtSide(side)) {
            if (connect(side) > 0) {
                GTUtility.sendChatTrans(entityPlayer, GTUtility.trans("214", "Connected"));
            }
        } else {
            disconnect(side);
            GTUtility.sendChatTrans(entityPlayer, GTUtility.trans("215", "Disconnected"));
        }
        VacuumFactoryGrid.INSTANCE.updateElement(this);
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer entityPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {

        if (GTMod.gregtechproxy.gt6Pipe) {
            final int mode = MetaGeneratedTool.getToolMode(aTool);
            IGregTechTileEntity currentPipeBase = getBaseMetaTileEntity();
            MTEVacuumConveyorPipe currentPipe = (MTEVacuumConveyorPipe) currentPipeBase.getMetaTileEntity();
            final ForgeDirection tSide = GTUtility.determineWrenchingSide(side, aX, aY, aZ);

            if (mode == ToolModes.REGULAR.get()) {
                currentPipe.connectPipeOnSide(tSide, entityPlayer);
                return true;
            }

            if (mode == ToolModes.WRENCH_LINE.get()) {

                boolean wasActionPerformed = false;

                int limit = Other.pipeWrenchingChainRange;
                for (int connected = 0; connected < limit; connected++) {

                    TileEntity nextPipeBaseTile = currentPipeBase.getTileEntityAtSide(tSide);

                    // if next tile doesn't exist or if next tile is not GT tile
                    if (!(nextPipeBaseTile instanceof IGregTechTileEntity nextPipeBase)) {
                        return wasActionPerformed;
                    }

                    MTEVacuumConveyorPipe nextPipe = nextPipeBase.getMetaTileEntity() instanceof MTEVacuumConveyorPipe
                        ? (MTEVacuumConveyorPipe) nextPipeBase.getMetaTileEntity()
                        : null;

                    // if next tile entity is not a pipe
                    if (nextPipe == null) {
                        return wasActionPerformed;
                    }

                    currentPipe.connectPipeOnSide(tSide, entityPlayer);

                    wasActionPerformed = true;

                    currentPipeBase = nextPipeBase;
                    currentPipe = nextPipe;

                }
                return wasActionPerformed;
            }
        }
        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection side, TileEntity tileEntity) {
        final IGregTechTileEntity baseMetaTile = getBaseMetaTileEntity();
        TileEntity tTileEntity = baseMetaTile.getTileEntityAtSide(side);
        if (tTileEntity != null) {
            IMetaTileEntity meta = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
            if (meta == null) return false;
            return meta instanceof VacuumFactoryElement;
        }
        return false;
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
    public void onColorChangeServer(byte color) {
        super.onColorChangeServer(color);
        VacuumFactoryGrid.INSTANCE.updateElement(this);
    }
}
