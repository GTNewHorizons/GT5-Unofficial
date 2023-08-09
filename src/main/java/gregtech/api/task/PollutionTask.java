package gregtech.api.task;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.common.GT_Pollution;

public class PollutionTask<T extends TaskHost & IMachineProgress> extends TickableTask<T> {

    private int pollutionPerSecond;
    private static final int POLLUTION_TICK = TickTime.SECOND;

    public PollutionTask(@NotNull T taskHost) {
        super(taskHost);
    }

    public PollutionTask<T> setPollutionPerSecond(int pollutionPerSecond) {
        this.pollutionPerSecond = pollutionPerSecond;
        return this;
    }

    public int getPollutionPerSecond() {
        return pollutionPerSecond;
    }

    @NotNull
    @Override
    public String getName() {
        return "pollution";
    }

    @Override
    public void update(long tick, boolean isServerSide) {
        if (isServerSide && tick % POLLUTION_TICK == 0 && taskHost.hasThingsToDo()) {
            GT_Pollution.addPollution(taskHost, getPollutionPerSecond());
        }
    }
}
