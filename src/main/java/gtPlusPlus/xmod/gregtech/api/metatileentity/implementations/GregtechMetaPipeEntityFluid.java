package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;

public class GregtechMetaPipeEntityFluid extends GT_MetaPipeEntity_Fluid {

    public final GT_Materials mMaterial;

    public GregtechMetaPipeEntityFluid(int aID, String aName, String aNameRegional, float aThickNess,
            GT_Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof) {
        this(aID, aName, aNameRegional, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GregtechMetaPipeEntityFluid(final String aName, final float aThickNess, final GT_Materials aMaterial,
            final int aCapacity, final int aHeatResistance, final boolean aGasProof) {
        this(aName, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GregtechMetaPipeEntityFluid(int aID, String aName, String aNameRegional, float aThickNess,
            GT_Materials aMaterial, int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aID, aName, aNameRegional, aThickNess, null, aCapacity, aHeatResistance, aGasProof);
        this.mLastReceivedFrom = 0;
        this.oLastReceivedFrom = 0;
        this.mMaterial = aMaterial;
    }

    public GregtechMetaPipeEntityFluid(String aName, float aThickNess, GT_Materials aMaterial, int aCapacity,
            int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aName, aThickNess, null, aCapacity, aHeatResistance, aGasProof);
        this.mLastReceivedFrom = 0;
        this.oLastReceivedFrom = 0;
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
        return new GregtechMetaPipeEntityFluid(
                this.mName,
                this.mThickNess,
                this.mMaterial,
                this.mCapacity,
                this.mHeatResistance,
                this.mGasProof);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
            int aColorIndex, boolean aConnected, boolean aRedstone) {
        float tThickNess = getThickNess();
        if (mDisableInput == 0)
            return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, aColorIndex)
                    : TextureFactory.of(
                            mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                            Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
        int tMask = 0;
        int[][] sRestrictionArray = { { 2, 3, 5, 4 }, { 2, 3, 4, 5 }, { 1, 0, 4, 5 }, { 1, 0, 4, 5 }, { 1, 0, 2, 3 },
                { 1, 0, 2, 3 } };
        if (side != ForgeDirection.UNKNOWN) {
            for (int i = 0; i < 4; i++)
                if (isInputDisabledAtSide(ForgeDirection.getOrientation(sRestrictionArray[side.ordinal()][i])))
                    tMask |= 1 << i;
            // Full block size renderer flips side 5 and 2 textures, flip restrictor textures to compensate
            if (side == ForgeDirection.EAST || side == ForgeDirection.UP)
                if (tMask > 3 && tMask < 12) tMask = (tMask ^ 12);
        }
        return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, aColorIndex)
                : TextureFactory.of(
                        mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                        Dyes.getModulation(aColorIndex, mMaterial.mRGBa)),
                getRestrictorTexture(tMask) };
    }

    protected static ITexture getBaseTexture(float aThickNess, int aPipeAmount, GT_Materials aMaterial,
            int aColorIndex) {
        if (aPipeAmount >= 9) return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipeNonuple.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
        if (aPipeAmount >= 4) return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipeQuadruple.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.124F) return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.374F) return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.499F) return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.749F) return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
        if (aThickNess < 0.874F) return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
        return TextureFactory.of(
                aMaterial.mIconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
                Dyes.getModulation(aColorIndex, aMaterial.mRGBa));
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                EnumChatFormatting.BLUE + "Fluid Capacity: %%%"
                        + (mCapacity * 20)
                        + "%%% L/sec"
                        + EnumChatFormatting.GRAY,
                EnumChatFormatting.RED + "Heat Limit: %%%" + mHeatResistance + "%%% K" + EnumChatFormatting.GRAY,
                EnumChatFormatting.DARK_GREEN + "Gas Proof: " + (this.mGasProof) + EnumChatFormatting.GRAY,
                // CORE.GT_Tooltip
        };
    }
}
