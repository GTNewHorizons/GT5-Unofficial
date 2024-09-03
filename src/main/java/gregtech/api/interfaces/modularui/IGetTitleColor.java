package gregtech.api.interfaces.modularui;

import gregtech.api.enums.Dyes;
import gregtech.api.util.GTUtil;

public interface IGetTitleColor {

    default int getTitleColor() {
        return GTUtil.getRGBaInt(Dyes.dyeWhite.getRGBA());
    }
}
