package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IOnFirstTickComponent extends IComponent {

    ComponentKey.ListKey<IOnFirstTickComponent> KEY = ComponentKey.list();

    void onFirstTick(@NotNull IGregTechTileEntity igte);
}
