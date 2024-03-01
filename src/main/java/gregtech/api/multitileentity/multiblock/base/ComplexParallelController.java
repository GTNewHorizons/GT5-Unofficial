package gregtech.api.multitileentity.multiblock.base;

import javax.annotation.Nonnull;

import gregtech.api.logic.ComplexParallelProcessingLogic;

public abstract class ComplexParallelController<C extends ComplexParallelController<C, P>, P extends ComplexParallelProcessingLogic<P>>
    extends Controller<C, P> {

    protected int maxComplexParallels = 0;
    protected int currentComplexParallels = 0;

    public ComplexParallelController() {
        isSimpleMachine = false;
    }

    protected void setMaxComplexParallels(int parallel, boolean stopMachine) {
        if (parallel != maxComplexParallels && maxComplexParallels != 0 && stopMachine) {
            stopMachine(false);
        }
        maxComplexParallels = parallel;
        setProcessingUpdate(true);
    }

    @Override
    protected void stopMachine(boolean powerShutDown) {
        super.stopMachine(powerShutDown);
    }

    protected boolean hasPerfectOverclock() {
        return false;
    }

    @Override
    public void setProcessingLogicPower(@Nonnull P processingLogic) {
        processingLogic.setAmperageOC(true);
        processingLogic.setAvailableAmperage(getPowerLogic().getMaxAmperage() / maxComplexParallels);
        processingLogic.setAvailableVoltage(getPowerLogic().getVoltage() / maxComplexParallels);
    }

    @Override
    public void updateProcessingLogic(@Nonnull P processingLogic) {
        processingLogic.setMaxComplexParallel(maxComplexParallels);
    }
}
