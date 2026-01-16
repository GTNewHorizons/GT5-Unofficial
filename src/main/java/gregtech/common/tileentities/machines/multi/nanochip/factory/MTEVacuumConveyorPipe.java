package gregtech.common.tileentities.machines.multi.nanochip.factory;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.factory.test.TestFactoryElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraftforge.common.util.ForgeDirection;
import tectech.thing.metaTileEntity.pipe.MTEBaseFactoryPipe;

import java.util.Collection;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

public class MTEVacuumConveyorPipe extends MTEBaseFactoryPipe implements VacuumFactoryElement {

    private static Textures.BlockIcons.CustomIcon CCPipe;
    private static Textures.BlockIcons.CustomIcon CCBarOverlay, CCBarOverlayActive;

    private VacuumFactoryNetwork network;

    public MTEVacuumConveyorPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public void registerIcons(IIconRegister blockIconRegister) {
        CCPipe = new Textures.BlockIcons.CustomIcon("iconsets/CC_PIPE");
        CCBarOverlay = new Textures.BlockIcons.CustomIcon("iconsets/CC_BAR_OVERLAY");
        CCBarOverlayActive = new Textures.BlockIcons.CustomIcon("iconsets/CC_BAR_OVERLAY_ACTIVE");
        super.registerIcons(blockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[]{TextureFactory.of(CCPipe),
            TextureFactory.of(
                getActive() ? CCBarOverlayActive : CCBarOverlay,
                Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA()))
        };
    }

    @Override
    public void getNeighbours(Collection<VacuumFactoryElement> neighbours) {

        IGregTechTileEntity base = getBaseMetaTileEntity();

        if (base == null || base.isDead()) return;
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (base.getTileEntityAtSide(side) instanceof IGregTechTileEntity gtEntity) {
                if (gtEntity.getColorization() == base.getColorization()) {
                    if (gtEntity.getMetaTileEntity() instanceof VacuumFactoryElement element) {
                        if (element.canConnectOnSide(side.getOpposite())) {
                            neighbours.add(element);

                        }


                    }
                }

            }
        }
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
    public VacuumFactoryNetwork getNetwork() {
        return this.network;
    }

    @Override
    public void setNetwork(VacuumFactoryNetwork network) {

    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new gregtech.common.tileentities.machines.multi.nanochip.MTEVacuumConveyorPipe(mName);
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {
        return true;
    }

    @Override
    public byte getColorization() {
        return this.getBaseMetaTileEntity().getColorization();
    }

}
