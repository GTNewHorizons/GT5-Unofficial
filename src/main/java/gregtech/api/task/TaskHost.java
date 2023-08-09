package gregtech.api.task;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.ApiStatus;

/**
 * Classes implementing this interface can have {@link TickableTask} to run. Tasks with conflicting name should not be
 * allowed, to prevent them from overwriting others' NBT load/save.
 */
public interface TaskHost {

    /**
     * This method should be called ONLY by {@link TickableTask} constructor.
     */
    @ApiStatus.Internal
    void registerTask(@Nonnull TickableTask<?> task);
}
