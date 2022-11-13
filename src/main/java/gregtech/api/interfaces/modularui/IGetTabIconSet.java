package gregtech.api.interfaces.modularui;

import gregtech.api.metatileentity.BaseTileEntity;

public interface IGetTabIconSet {
    default BaseTileEntity.GT_GuiTabIconSet getTabIconSet() {
        return null;
    }
}
