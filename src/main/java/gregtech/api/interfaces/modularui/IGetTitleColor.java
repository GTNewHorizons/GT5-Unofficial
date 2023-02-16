package gregtech.api.interfaces.modularui;

import gregtech.api.enums.Dyes;
import gregtech.api.util.GT_Util;

public interface IGetTitleColor {

    default int getTitleColor() {
        return GT_Util.getRGBaInt(Dyes.dyeWhite.getRGBA());
    }
}
