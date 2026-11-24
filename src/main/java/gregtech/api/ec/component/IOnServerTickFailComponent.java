package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IOnServerTickFailComponent extends IComponent {

    ComponentKey.ListKey<IOnServerTickFailComponent> KEY = ComponentKey.list();

    void onServerTickFail(IGregTechTileEntity aBaseMetaTileEntity, long aTick);
}
