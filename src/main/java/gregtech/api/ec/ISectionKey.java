package gregtech.api.ec;

import org.jetbrains.annotations.NotNull;

public interface ISectionKey {

    @NotNull
    String getSectionId();

    int getPriority();
}
