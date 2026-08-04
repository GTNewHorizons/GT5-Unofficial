package tectech.mechanics.pipe;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public interface IActivePipe extends IMetaTileEntity {

    boolean getActive();

    void markUsed();
}
