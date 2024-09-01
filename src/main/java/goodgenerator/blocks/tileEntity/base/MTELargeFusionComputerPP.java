package goodgenerator.blocks.tileEntity.base;

import static net.minecraft.util.StatCollector.translateToLocal;

import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.util.AdvancedFusionOverclockDescriber;
import tectech.thing.metaTileEntity.multi.base.INameFunction;
import tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import tectech.thing.metaTileEntity.multi.base.LedStatus;
import tectech.thing.metaTileEntity.multi.base.Parameters;

public abstract class MTELargeFusionComputerPP extends MTELargeFusionComputer {

    protected Parameters.Group.ParameterIn batchSetting;

    /** Name of the batch setting */
    private static final INameFunction<MTELargeFusionComputerPP> BATCH_SETTING_NAME = (base,
        p) -> translateToLocal("gt.blockmachines.LargeFusionComputerPP.cfgi.0"); // Batch size
    /** Status of the batch setting */
    private static final IStatusFunction<MTELargeFusionComputerPP> BATCH_STATUS = (base, p) -> LedStatus
        .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 32, 128);

    public MTELargeFusionComputerPP(String name) {
        super(name);
    }

    public MTELargeFusionComputerPP(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    protected OverclockDescriber createOverclockDescriber() {
        return new AdvancedFusionOverclockDescriber((byte) tier(), capableStartupCanonical());
    }

    @Override
    protected void parametersInstantiation_EM() {
        batchSetting = parametrization.getGroup(9, false)
            .makeInParameter(1, 1, BATCH_SETTING_NAME, BATCH_STATUS);
    }

    @Override
    protected int getMaxBatchSize() {
        // Batch size 1~128
        return (int) Math.min(Math.max(batchSetting.get(), 1.0D), 128.0D);
    }

    @Override
    public boolean getDefaultBatchMode() {
        return true;
    }
}
