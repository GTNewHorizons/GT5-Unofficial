package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.core.material.Material;

public class GTPPMTECable extends MTECable {

    private final short[] vRGB;

    private Material material;

    public GTPPMTECable(final int aID, final String aName, final String aPrefixKey, final float aThickNess,
        final Materials aMaterial, final long aCableLossPerMeter, final long aAmperage, final long aVoltage,
        final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
        super(
            aID,
            aName,
            aPrefixKey,
            aThickNess,
            aMaterial,
            aCableLossPerMeter,
            aAmperage,
            aVoltage,
            aInsulated,
            aCanShock);
        this.vRGB = aRGB == null || aRGB.length != 4 ? Materials.Iron.mRGBa : aRGB;
    }

    public GTPPMTECable(final int aID, final String aName, final String aPrefixKey, final float aThickNess,
        final long aCableLossPerMeter, final long aAmperage, final long aVoltage,
        final boolean aInsulated, final boolean aCanShock, final Material aMaterial) {
        super(
            aID,
            aName,
            aPrefixKey,
            aThickNess,
            null,
            aCableLossPerMeter,
            aAmperage,
            aVoltage,
            aInsulated,
            aCanShock,
            aMaterial.getRGBA());
        this.material = aMaterial;
    }

    public GTPPMTECable(final String aName, final float aThickNess, final long aCableLossPerMeter, final long aAmperage,
                        final long aVoltage, final boolean aInsulated, final boolean aCanShock, final short[] aRGB) {
        this(aName, aThickNess, null, aCableLossPerMeter, aAmperage, aVoltage, aInsulated, aCanShock, aRGB);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        if (material == null) {
            return super.newMetaEntity(aTileEntity);
        }
        return new GTPPMTECable(
            this.mName,
            this.mThickNess,
            this.material,
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
        if (material == null) {
            return super.getTexture(aBaseMetaTileEntity, side, aConnections, aColorIndex, aConnected, aRedstone);
        }
        return getTextureGTPP(aBaseMetaTileEntity, side, aConnections, aColorIndex, aConnected, aRedstone);
    }

    private ITexture[] getTextureGTPP(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection aSide, int aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {

        Material wireMaterial = material;

        if (!mInsulated) return new ITexture[] { TextureFactory
            .of(wireMaterial.getTextureSet().mTextures[TextureSet.INDEX_wire], Dyes.getModulation(aColorIndex, vRGB)) };
        if (aConnected) {
            float tThickNess = getThickness();
            if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                Textures.BlockIcons.INSULATION_FULL,
                Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.374F) // 0.375 x1
                return new ITexture[] {
                    TextureFactory.of(wireMaterial.getTextureSet().mTextures[TextureSet.INDEX_wire], vRGB),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_TINY,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.499F) // 0.500 x2
                return new ITexture[] {
                    TextureFactory.of(wireMaterial.getTextureSet().mTextures[TextureSet.INDEX_wire], vRGB),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_SMALL,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.624F) // 0.625 x4
                return new ITexture[] {
                    TextureFactory.of(wireMaterial.getTextureSet().mTextures[TextureSet.INDEX_wire], vRGB),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.749F) // 0.750 x8
                return new ITexture[] {
                    TextureFactory.of(wireMaterial.getTextureSet().mTextures[TextureSet.INDEX_wire], vRGB),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_MEDIUM_PLUS,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            if (tThickNess < 0.874F) // 0.825 x12
                return new ITexture[] {
                    TextureFactory.of(wireMaterial.getTextureSet().mTextures[TextureSet.INDEX_wire], vRGB),
                    TextureFactory.of(
                        Textures.BlockIcons.INSULATION_LARGE,
                        Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
            return new ITexture[] {
                TextureFactory.of(wireMaterial.getTextureSet().mTextures[TextureSet.INDEX_wire], vRGB),
                TextureFactory.of(
                    Textures.BlockIcons.INSULATION_HUGE,
                    Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
        }
        return new ITexture[] { TextureFactory.of(
            Textures.BlockIcons.INSULATION_FULL,
            Dyes.getModulation(aColorIndex, Dyes.CABLE_INSULATION.getRGBA())) };
    }

    @Override
    public IOreMaterial getMaterial() {
        if (super.getMaterial() == null) {
            return material;
        }
        return super.getMaterial();
    }
}
