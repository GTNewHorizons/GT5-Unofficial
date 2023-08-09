package gregtech.api.task;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;

/**
 * This class aims at separating logic run on {@link TaskHost}, rather than using interface layers.
 * It has two main functionalities: Run tick and Save/Load.
 */
public abstract class TickableTask {

    @Nonnull
    protected final TaskHost taskHost;

    public TickableTask(@Nonnull TaskHost taskHost) {
        this.taskHost = taskHost;
        taskHost.registerTask(this);
    }

    /**
     * @return Name of this task. Tasks with conflicting name cannot be registered to the same machine.
     */
    @Nonnull
    public abstract String getName();

    /**
     * Called once per world tick.
     */
    public abstract void update(long tick, boolean isServerSide);

    /**
     * Save info to NBT.
     */
    public abstract void writeToNBT(@Nonnull NBTTagCompound nbt);

    /**
     * Read info from NBT.
     */
    public abstract void readFromNBT(@Nonnull NBTTagCompound nbt);

    @Override
    public String toString() {
        return "TickableTask{" + "name=" + getName() + ", taskHost=" + taskHost + "}";
    }
}
