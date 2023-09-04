package gregtech.api.task.tasks;

import javax.annotation.Nonnull;

import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.task.TaskHost;
import gregtech.api.task.TickableTask;

public class ProcessingTask<T extends TaskHost & ProcessingLogicHost<P> & IMachineProgress, P extends ProcessingLogic<P>>
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
        P logic = taskHost.getProcessingLogic();
        if (taskHost.hasThingsToDo() && logic.canWork() && tick % 100 == 0) {
            if (tick % 100 == 0) {
                logic.startCheck();
            }
        } else {
            logic.process();
        }
    }

}
