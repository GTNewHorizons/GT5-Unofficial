package goodgenerator.blocks.tileEntity.base;

import static net.minecraft.util.StatCollector.translateToLocal;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;

import gregtech.api.logic.ProcessingLogic;

public abstract class LargeFusionComputerPP extends LargeFusionComputer {

    protected Parameters.Group.ParameterIn batchSetting;

    /** Name of the batch setting */
    private static final INameFunction<LargeFusionComputerPP> BATCH_SETTING_NAME = (base,
            p) -> translateToLocal("gt.blockmachines.LargeFusionComputerPP.cfgi.0"); // Batch size
    /** Status of the batch setting */
    private static final IStatusFunction<LargeFusionComputerPP> BATCH_STATUS = (base, p) -> LedStatus
            .fromLimitsInclusiveOuterBoundary(p.get(), 1, 0, 32, 128);

    public LargeFusionComputerPP(String name) {
        super(name);
    }

    public LargeFusionComputerPP(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return super.createProcessingLogic().setOverclock(2, 2);
    }

    @Override
    protected long getSingleHatchPower() {
        // Multiply by 8 so that we can have the original input power per hatch
        return super.getSingleHatchPower() * 8;
    }

    @Override
    protected void parametersInstantiation_EM() {
        batchSetting = parametrization.getGroup(9, false).makeInParameter(1, 1, BATCH_SETTING_NAME, BATCH_STATUS);
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
