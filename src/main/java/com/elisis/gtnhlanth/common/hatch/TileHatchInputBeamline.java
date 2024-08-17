package com.elisis.gtnhlanth.common.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.elisis.gtnhlanth.common.beamline.BeamLinePacket;
import com.elisis.gtnhlanth.common.beamline.IConnectsToBeamline;
import com.github.technus.tectech.util.TT_Utility;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;

public class TileHatchInputBeamline extends TileHatchBeamlineConnector<BeamLinePacket> {

    private boolean delay = true;

    private static final String activeIconPath = "iconsets/OVERLAY_BI_ACTIVE";
    private static final String sideIconPath = "iconsets/OVERLAY_BI_SIDES";
    private static final String connIconPath = "iconsets/BI_CONN";

    private static final Textures.BlockIcons.CustomIcon activeIcon = new Textures.BlockIcons.CustomIcon(activeIconPath);
    private static final Textures.BlockIcons.CustomIcon sideIcon = new Textures.BlockIcons.CustomIcon(sideIconPath);
    private static final Textures.BlockIcons.CustomIcon connIcon = new Textures.BlockIcons.CustomIcon(connIconPath);

    public TileHatchInputBeamline(int id, String name, String nameRegional, int tier) {

        super(id, name, nameRegional, tier, "");
        TT_Utility.setTier(tier, this);
    }

    public TileHatchInputBeamline(String name, int tier, String[] desc, ITexture[][][] textures) {
        super(name, tier, desc, textures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GT_RenderedTexture(
                activeIcon,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GT_RenderedTexture(connIcon) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            new GT_RenderedTexture(
                sideIcon,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            new GT_RenderedTexture(connIcon) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new TileHatchInputBeamline(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected BeamLinePacket loadPacketFromNBT(NBTTagCompound tag) {
        return new BeamLinePacket(tag);
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean isOutputFacing(ForgeDirection aSide) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public IConnectsToBeamline getNext(IConnectsToBeamline source) {
        return null;
    }

    @Override
    public String[] getDescription() {
        return null;
    }

    public void setContents(BeamLinePacket in) {
        if (in == null) {
            this.q = null;
        } else {
            if (in.getContent()
                .getRate() > 0) {
                this.q = in;
                delay = true;
            } else {
                this.q = null;
            }
        }
    }

    @Override
    public void moveAround(IGregTechTileEntity tile) {
        if (delay) {
            delay = false;
        } else {
            this.setContents(null);
        }
    }
}
