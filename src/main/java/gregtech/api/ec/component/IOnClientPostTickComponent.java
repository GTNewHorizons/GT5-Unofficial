package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IOnClientPostTickComponent extends IComponent {

    ComponentKey.ListKey<IOnClientPostTickComponent> KEY = ComponentKey.list();

    void onClientPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick);
}
