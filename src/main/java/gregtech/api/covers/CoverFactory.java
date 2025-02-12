package gregtech.api.covers;

import gregtech.common.covers.CoverInfo;

@FunctionalInterface
public interface CoverFactory {

    CoverInfo buildCover(CoverContext context);
}
