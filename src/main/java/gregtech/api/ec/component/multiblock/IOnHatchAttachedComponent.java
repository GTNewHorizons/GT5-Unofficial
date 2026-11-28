package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

public interface IOnHatchAttachedComponent extends IMultiblockComponent {

    ComponentKey.ListKey<IOnHatchAttachedComponent> KEY = ComponentKey.list();

    void onHatchAttached(@NotNull IMetaTileEntity metaTile, int baseCasingIndex);
}
