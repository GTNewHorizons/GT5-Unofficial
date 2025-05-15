package gregtech.api.covers;

import gregtech.common.covers.Cover;

@FunctionalInterface
public interface CoverFactory {

    Cover buildCover(CoverContext context);
}
