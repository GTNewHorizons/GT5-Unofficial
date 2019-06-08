package com.github.technus.tectech.thing.metaTileEntity.pipe;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public interface IActivePipe extends IMetaTileEntity {
    void setActive(boolean active);
    boolean getActive();
    void markUsed();
}
