package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.PIPE_RESTRICTOR;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEItem;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;

public class GTPPMTEItem extends MTEItem {

    public final GregtechOrePrefixes.GT_Materials mMaterial;

    public GTPPMTEItem(int aID, String aName, String aNameRegional, float aThickNess,
        GregtechOrePrefixes.GT_Materials aMaterial, int aInvSlotCount, int aStepSize, boolean aIsRestrictive) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aInvSlotCount, aStepSize, aIsRestrictive, 20);
    }

    public GTPPMTEItem(String aName, String aNameRegional, float aThickNess, GregtechOrePrefixes.GT_Materials aMaterial,
        int aInvSlotCount, int aStepSize, boolean aIsRestrictive) {
        this(aName, aThickNess, aMaterial, aInvSlotCount, aStepSize, aIsRestrictive, 20);
    }

    public GTPPMTEItem(int aID, String aName, String aNameRegional, float aThickNess,
        GregtechOrePrefixes.GT_Materials aMaterial, int aInvSlotCount, int aStepSize, boolean aIsRestrictive,
        int aTickTime) {
        super(aID, aName, aNameRegional, aThickNess, null, aInvSlotCount, aStepSize, aIsRestrictive, aTickTime);
        this.mLastReceivedFrom = ForgeDirection.UNKNOWN;
        this.oLastReceivedFrom = ForgeDirection.UNKNOWN;
        this.mMaterial = aMaterial;
    }

    public GTPPMTEItem(String aName, float aThickNess, GregtechOrePrefixes.GT_Materials aMaterial, int aInvSlotCount,
        int aStepSize, boolean aIsRestrictive, int aTickTime) {
        super(aName, aThickNess, null, aInvSlotCount, aStepSize, aIsRestrictive, aTickTime);
        this.mLastReceivedFrom = ForgeDirection.UNKNOWN;
        this.oLastReceivedFrom = ForgeDirection.UNKNOWN;
        this.mMaterial = aMaterial;
    }

    @Override
    public byte getTileEntityBaseType() {
        return this.mMaterial == null ? 4
            : (byte) ((this.mMaterial.contains(SubTag.WOOD) ? 12 : 4)
                + Math.max(0, Math.min(3, this.mMaterial.mToolQuality)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GTPPMTEItem(
            this.mName,
            this.mThickNess,
            this.mMaterial,
            this.mInventory.length,
            this.mStepSize,
            this.mIsRestrictive,
            this.mTickTime);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {
        if (mIsRestrictive) {
            if (aConnected) {
                float tThickNess = getThickNess();
                if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.374F) // 0.375
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.499F) // 0.500
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.749F) // 0.750
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                if (tThickNess < 0.874F) // 0.825
                    return new ITexture[] { TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
            }
            return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)), TextureFactory.of(PIPE_RESTRICTOR) };
        }
        if (aConnected) {
            float tThickNess = getThickNess();
            if (tThickNess < 0.124F) return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.374F) // 0.375
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.499F) // 0.500
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.749F) // 0.750
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            if (tThickNess < 0.874F) // 0.825
                return new ITexture[] { TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
            return new ITexture[] { TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
        }
        return new ITexture[] { TextureFactory.of(
            mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
            Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
    }

}
