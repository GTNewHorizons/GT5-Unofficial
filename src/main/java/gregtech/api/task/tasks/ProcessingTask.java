package gregtech.api.task.tasks;

import javax.annotation.Nonnull;

import gregtech.api.interfaces.tileentity.MachineProgress;
import gregtech.api.logic.MuTEProcessingLogic;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.task.TaskHost;
import gregtech.api.task.TickableTask;

public class ProcessingTask<T extends TaskHost & ProcessingLogicHost<P> & MachineProgress, P extends MuTEProcessingLogic<P>>
    extends TickableTask<T> {

    public ProcessingTask(@Nonnull T taskHost) {
        super(taskHost);
    }

    private static final String NAME = "processing";

    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    @Override
    public void update(long tick, boolean isServerSide) {
        if (!isServerSide) return;
        if (!taskHost.isAllowedToWork()) return;
        final P logic = taskHost.getProcessingLogic();
        if (taskHost.needsUpdate()) {
            taskHost.updateProcessingLogic(logic);
            taskHost.setProcessingUpdate(false);
        }
        if (logic.canWork() && tick % 100 == 0) {
            taskHost.setProcessingLogicPower(logic);
            logic.startCheck();
            if (logic.getResult()
                .wasSuccessful()) {
                taskHost.setActive(true);
            }
        }

        if (taskHost.hasThingsToDo()) {
            logic.progress();
        } else {
            taskHost.setActive(false);
        }
    }

}
