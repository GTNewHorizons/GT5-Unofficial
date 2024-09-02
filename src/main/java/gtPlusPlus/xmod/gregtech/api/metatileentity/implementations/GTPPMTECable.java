package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.metatileentity.IMetaTileEntityCable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.GTUtility;

public class GTPPMTECable extends MTECable implements IMetaTileEntityCable {

    private static Textures.BlockIcons INSULATION_MEDIUM_PLUS;

    static {
        try {
            INSULATION_MEDIUM_PLUS = (Textures.BlockIcons) GTUtility
                .getField(Textures.BlockIcons.class, "INSULATION_MEDIUM_PLUS")
                .get(null);
        } catch (IllegalAccessException | NullPointerException e) {
            throw new Error(e);
        }
    }

    private short[] vRGB = null;

    public GTPPMTECable(final int aID, final String aName, final String aNameRegional, final float aThickNess,
        final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage,
        final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
        super(
            aID,
            aName,
            aNameRegional,
            aThickNess,
            aMaterial,
            aCableLossPerMeter,
            aAmperage,
            aVoltage,
            aInsulated,
            aCanShock);
        this.vRGB = aRGB == null || aRGB.length != 4 ? Materials.Iron.mRGBa : aRGB;
    }

    public GTPPMTECable(final String aName, final float aThickNess, final Materials aMaterial,
        final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated,
        final boolean aCanShock, final short[] aRGB) {
        super(aName, aThickNess, aMaterial, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock);
        this.vRGB = aRGB == null || aRGB.length != 4 ? Materials.Iron.mRGBa : aRGB;
    }

    public GTPPMTECable(final int aID, final String aName, final String aNameRegional, final float aThickNess,
        final long aCableLossPerMeter, final long aAmperage, final long aVoltage, final boolean aInsulated,
        final boolean aCanShock, final short[] aRGB) {
        this(
            aID,
            aName,
            aNameRegional,
            aThickNess,
            null,
            aCableLossPerMeter,
            aAmperage,
            aVoltage,
            aInsulated,
            aCanShock,
            aRGB);
    }

    public GTPPMTECable(final String aName, final float aThickNess, final long aCableLossPerMeter, final long aAmperage,
        final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
        this(aName, aThickNess, null, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock, aRGB);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GTPPMTECable(
            this.mName,
            this.mThickNess,
            this.mMaterial,
            this.mCableLossPerMeter,
            this.mAmperage,
            this.mVoltage,
            this.mInsulated,
            this.mCanShock,
            this.vRGB);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {
        return getTextureGTNH(aBaseMetaTileEntity, side, aConnections, aColorIndex, aConnected, aRedstone);
    }

    private ITexture[] getTextureGTNH(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection aSide, int aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {

        Materials wireMaterial = mMaterial;
        if (wireMaterial == null) {
            wireMaterial = Materials.Iron;
        }

        if (!mInsulated) return new ITexture[] { new GTRenderedTexture(
            wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire],
            Dyes.getModulation(aColorIndex, vRGB)) };
        if (aConnected) {
            float tThickNess = getThickNess();
            if (tThickNess < 0.124F) return new ITexture[] { new GTRenderedTexture(
                Textures.BlockIcons.INSULATION_FULL,
                Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.374F) // 0.375 x1
                return new ITexture[] {
                    new GTRenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB),
                    new GTRenderedTexture(
                        Textures.BlockIcons.INSULATION_TINY,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.499F) // 0.500 x2
                return new ITexture[] {
                    new GTRenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB),
                    new GTRenderedTexture(
                        Textures.BlockIcons.INSULATION_SMALL,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.624F) // 0.625 x4
                return new ITexture[] {
                    new GTRenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB),
                    new GTRenderedTexture(
                        Textures.BlockIcons.INSULATION_MEDIUM,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.749F) // 0.750 x8
                return new ITexture[] {
                    new GTRenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB),
                    new GTRenderedTexture(
                        INSULATION_MEDIUM_PLUS,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            if (tThickNess < 0.874F) // 0.825 x12
                return new ITexture[] {
                    new GTRenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB),
                    new GTRenderedTexture(
                        Textures.BlockIcons.INSULATION_LARGE,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
            return new ITexture[] { new GTRenderedTexture(wireMaterial.mIconSet.mTextures[TextureSet.INDEX_wire], vRGB),
                new GTRenderedTexture(
                    Textures.BlockIcons.INSULATION_HUGE,
                    Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
        }
        return new ITexture[] { new GTRenderedTexture(
            Textures.BlockIcons.INSULATION_FULL,
            Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.mRGBa)) };
    }
}
