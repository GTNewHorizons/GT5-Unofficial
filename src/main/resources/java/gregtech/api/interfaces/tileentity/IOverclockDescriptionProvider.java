package gregtech.api.interfaces.tileentity;

import javax.annotation.Nullable;

import gregtech.api.objects.overclockdescriber.OverclockDescriber;

/**
 * Classes implementing this interface can provide {@link OverclockDescriber} to provide overclock behavior and NEI
 * description.
 */
public interface IOverclockDescriptionProvider {

    @Nullable
    OverclockDescriber getOverclockDescriber();
}
