package tectech.mechanics.pipe;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public interface IActivePipe extends IMetaTileEntity {

    void setActive(boolean active);

    boolean getActive();

    void markUsed();
}
