package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.render.TextureFactory;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.PipeStats;

public class GTPPMTEFluidPipe extends MTEFluidPipe {

    public final PipeStats pipeStats;

    public GTPPMTEFluidPipe(int aID, String aName, String aNameRegional, float aThickNess, PipeStats pipeStats,
        int aCapacity, int aHeatResistance, boolean aGasProof) {
        this(aID, aName, aNameRegional, aThickNess, pipeStats, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GTPPMTEFluidPipe(final String aName, final float aThickNess, final PipeStats pipeStats, final int aCapacity,
        final int aHeatResistance, final boolean aGasProof) {
        this(aName, aThickNess, pipeStats, aCapacity, aHeatResistance, aGasProof, 1);
    }

    public GTPPMTEFluidPipe(int aID, String aName, String aNameRegional, float aThickNess, PipeStats pipeStats,
        int aCapacity, int aHeatResistance, boolean aGasProof, int aFluidTypes) {
        super(aID, aName, aNameRegional, aThickNess, null, aCapacity, aHeatResistance, aGasProof, aFluidTypes);
        this.mLastReceivedFrom = 0;
        this.oLastReceivedFrom = 0;
        this.pipeStats = pipeStats;
    }

    public GTPPMTEFluidPipe(String aName, float aThickNess, PipeStats pipeStats, int aCapacity, int aHeatResistance,
        boolean aGasProof, int aFluidTypes) {
        super(aName, aThickNess, null, aCapacity, aHeatResistance, aGasProof, aFluidTypes);
        this.mLastReceivedFrom = 0;
        this.oLastReceivedFrom = 0;
        this.pipeStats = pipeStats;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GTPPMTEFluidPipe(
            this.mName,
            this.mThickNess,
            this.pipeStats,
            this.mCapacity,
            this.mHeatResistance,
            this.mGasProof);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, int aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {
        float tThickNess = getThickness();
        if (mDisableInput == 0)
            return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, aColorIndex)
                : TextureFactory.of(
                    pipeStats.iconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                    Dyes.getModulation(aColorIndex, pipeStats.rgba)) };
        int tMask = 0;
        int[][] sRestrictionArray = { { 2, 3, 5, 4 }, { 2, 3, 5, 4 }, { 1, 0, 5, 4 }, { 1, 0, 4, 5 }, { 1, 0, 2, 3 },
            { 1, 0, 2, 3 } };
        if (side != ForgeDirection.UNKNOWN) {
            for (int i = 0; i < 4; i++)
                if (isInputDisabledAtSide(ForgeDirection.getOrientation(sRestrictionArray[side.ordinal()][i])))
                    tMask |= 1 << i;
            // Full block size renderer flips side 5 and 2 textures, flip restrictor textures to compensate
            if (side == ForgeDirection.EAST || side == ForgeDirection.UP)
                if (tMask > 3 && tMask < 12) tMask = (tMask ^ 12);
        }
        return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, aColorIndex)
            : TextureFactory.of(
                pipeStats.iconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(aColorIndex, pipeStats.rgba)),
            getRestrictorTexture(tMask) };
    }

    protected ITexture getBaseTexture(float aThickNess, int aPipeAmount, int colorIndex) {
        if (aPipeAmount >= 9) return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipeNonuple.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
        if (aPipeAmount >= 4) return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipeQuadruple.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
        if (aThickNess < 0.124F) return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
        if (aThickNess < 0.374F) return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipeTiny.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
        if (aThickNess < 0.499F) return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipeSmall.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
        if (aThickNess < 0.749F) return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipeMedium.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
        if (aThickNess < 0.874F) return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipeLarge.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
        return TextureFactory.of(
            pipeStats.iconSet.mTextures[OrePrefixes.pipeHuge.mTextureIndex],
            Dyes.getModulation(colorIndex, pipeStats.rgba));
    }
}
