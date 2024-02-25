package gregtech.api.task.tasks;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;

import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.tileentity.MachineProgress;
import gregtech.api.task.TaskHost;
import gregtech.api.task.TickableTask;
import gregtech.common.GT_Pollution;

public class PollutionTask<T extends TaskHost & MachineProgress> extends TickableTask<T> {

    private int pollutionPerSecond;
    private static final int POLLUTION_TICK = TickTime.SECOND;

    public PollutionTask(@Nonnull T taskHost) {
        super(taskHost);
    }

    public PollutionTask<T> setPollutionPerSecond(int pollutionPerSecond) {
        this.pollutionPerSecond = pollutionPerSecond;
        return this;
    }

    public int getPollutionPerSecond() {
        return pollutionPerSecond;
    }

    @Nonnull
    @Override
    public String getName() {
        return "pollution";
    }

    @Override
    public void update(long tick, boolean isServerSide) {
        if (isServerSide && tick % POLLUTION_TICK == 0 && taskHost.hasThingsToDo()) {
            if (taskHost instanceof final TileEntity entity) {
                GT_Pollution.addPollution(entity, getPollutionPerSecond());
            }
        }
    }
}
