package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
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
    protected ITexture getBaseTexture(boolean connected, int colorIndex) {
        return getBaseTexture(getThickness(), mPipeAmount, pipeStats.iconSet, pipeStats.rgba, connected, colorIndex);
    }
}
