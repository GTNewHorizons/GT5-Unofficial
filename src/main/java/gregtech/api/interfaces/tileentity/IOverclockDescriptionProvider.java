package gregtech.api.interfaces.tileentity;

import javax.annotation.Nullable;

import gregtech.common.power.Power;

/**
 * Classes implementing this interface can provide {@link Power} to provide overclock behavior and NEI description.
 */
public interface IOverclockDescriptionProvider {

    @Nullable
    Power getOverclockDescriber();
}
