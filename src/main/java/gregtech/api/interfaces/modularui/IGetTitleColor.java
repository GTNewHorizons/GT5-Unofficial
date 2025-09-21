package gregtech.api.interfaces.modularui;

import gregtech.api.enums.Dyes;

public interface IGetTitleColor {

    default int getTitleColor() {
        return Dyes.dyeWhite.toInt();
    }
}
