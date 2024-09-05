package gtnhlanth.common.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.IConnectsToBeamline;
import gtnhlanth.common.hatch.MTEHatchBeamlineConnector;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import tectech.util.TTUtility;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gtnhlanth.common.beamline.MTEBeamlinePipe;

public class MTEHatchOutputBeamline extends MTEHatchBeamlineConnector<BeamLinePacket> implements IConnectsToBeamline {

    private static final String activeIconPath = "iconsets/OVERLAY_BO_ACTIVE";
    private static final String sideIconPath = "iconsets/OVERLAY_BO_SIDES";
    private static final String connIconPath = "iconsets/BO_CONN";

    private static final Textures.BlockIcons.CustomIcon activeIcon = new Textures.BlockIcons.CustomIcon(activeIconPath);
    private static final Textures.BlockIcons.CustomIcon sideIcon = new Textures.BlockIcons.CustomIcon(sideIconPath);
    private static final Textures.BlockIcons.CustomIcon connIcon = new Textures.BlockIcons.CustomIcon(connIconPath);

    public MTEHatchOutputBeamline(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, "");
        TTUtility.setTier(tier, this);
    }

    public MTEHatchOutputBeamline(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GTRenderedTexture(
                activeIcon,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GTRenderedTexture(connIcon) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GTRenderedTexture(
                sideIcon,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GTRenderedTexture(connIcon) };
    }

    @Override
    public IConnectsToBeamline getNext(IConnectsToBeamline source) {

        IGregTechTileEntity base = this.getBaseMetaTileEntity();
        IGregTechTileEntity next = base.getIGregTechTileEntityAtSide(base.getFrontFacing());

        if (next == null) {
            return null;
        }

        IMetaTileEntity meta = next.getMetaTileEntity();
        if (meta instanceof MTEBeamlinePipe) {

            ((MTEBeamlinePipe) meta).markUsed();
            return (IConnectsToBeamline) meta;

        } else if (meta instanceof MTEHatchInputBeamline && ((MTEHatchInputBeamline) meta).canConnect(
            base.getFrontFacing()
                .getOpposite())) {

                    return (IConnectsToBeamline) meta;
                }

        return null;
    }

    @Override
    public void moveAround(IGregTechTileEntity aBaseMetaTileEntity) {
        IConnectsToBeamline current = this, source = this, next;
        int range = 0;

        ForgeDirection front = this.getBaseMetaTileEntity()
            .getFrontFacing();

        for (int distance = 1; distance <= 129; distance++) { // 128 pipes max

            IGregTechTileEntity nextTE = (IGregTechTileEntity) this.getBaseMetaTileEntity()
                .getTileEntityAtSideAndDistance(front, distance); // Straight line transmission only

            if (nextTE == null) {
                return;
            }

            IMetaTileEntity nextMeta = nextTE.getMetaTileEntity();

            if (nextMeta == null || !(nextMeta instanceof IConnectsToBeamline)) { // Non-beamliney block
                return;
            }

            if (((IConnectsToBeamline) nextMeta) instanceof MTEHatchInputBeamline) {
                ((MTEHatchInputBeamline) nextMeta).setContents(q); // Reached another multi
                break;

            } else if (((IConnectsToBeamline) nextMeta) instanceof MTEBeamlinePipe) { // Another pipe follows

                if (((MTEBeamlinePipe) nextMeta).isDataInputFacing(front.getOpposite())) { // Connected to previous pipe
                    ((MTEBeamlinePipe) nextMeta).markUsed();
                } else {
                    return;
                }

            } else {
                return;
            }

        }

        /*
         * while ((next = current.getNext(source)) != null && range++ < 100) {
         * if (next instanceof TileHatchInputBeamline) {
         * ((TileHatchInputBeamline) next).setContents(q);
         * break;
         * }
         * source = current;
         * current = next;
         * }
         */
        q = null;
    }

    @Override
    protected BeamLinePacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new BeamLinePacket(nbt);
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return this.isOutputFacing(side);
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return this.isInputFacing(side);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection aSide) {
        return false;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public String[] getDescription() {
        return null;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new MTEHatchOutputBeamline(mName, mTier, mDescriptionArray, mTextures);
    }
}
