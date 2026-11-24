package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IPostRunMachineComponent extends IComponent {

    ComponentKey.ListKey<IPostRunMachineComponent> KEY = ComponentKey.list();

    void onPostRunMachine(@NotNull IRecipeLogicComponent recipeLogic, IGregTechTileEntity igte, long tick);
}
