package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
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

    public GTPPMTEFluidPipe(final String aName, final float aThickNess, final PipeStats pipeStats,
        final int aCapacity, final int aHeatResistance, final boolean aGasProof) {
        this(aName, aThickNess, aMaterial, aCapacity, aHeatResistance, aGasProof, 1);
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
        return this.mMaterial == null ? 4
            : (byte) ((this.mMaterial.contains(SubTag.WOOD) ? 12 : 4)
                + Math.max(0, Math.min(3, this.mMaterial.mToolQuality)));
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
        float tThickNess = getThickNess();
        if (mDisableInput == 0)
            return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, aColorIndex)
                : TextureFactory.of(
                    mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                    Dyes.getModulation(aColorIndex, mMaterial.mRGBa)) };
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
        return new ITexture[] { aConnected ? getBaseTexture(tThickNess, mPipeAmount, mMaterial, aColorIndex)
            : TextureFactory.of(
                mMaterial.mIconSet.mTextures[OrePrefixes.pipe.mTextureIndex],
                Dyes.getModulation(aColorIndex, mMaterial.mRGBa)),
            getRestrictorTexture(tMask) };
    }
}
