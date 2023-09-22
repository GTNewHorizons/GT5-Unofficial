package gregtech.api.task.tasks;

import javax.annotation.Nonnull;

import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.logic.interfaces.PowerLogicHost;
import gregtech.api.task.TaskHost;
import gregtech.api.task.TickableTask;

public class PowerOutputTask<T extends PowerLogicHost & TaskHost & IMachineProgress> extends TickableTask<T> {

    private static final String NAME = "powerTask";

    public PowerOutputTask(@Nonnull T taskHost) {
        super(taskHost);
    }

    @Override
    @Nonnull
    public String getName() {
        return NAME;
    }

    @Override
    public void update(long tick, boolean isServerSide) {
        if (!isServerSide) return;
        if (!taskHost.isActive()) return;
        if (!taskHost.isEnergyEmitter()) return;
         
    }
    
}
