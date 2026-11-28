package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IPreStructureCheckComponent extends IMultiblockComponent {

    ComponentKey.ListKey<IPreStructureCheckComponent> KEY = ComponentKey.list();

    void onPreStructureCheck(@NotNull IGregTechTileEntity te);
}
