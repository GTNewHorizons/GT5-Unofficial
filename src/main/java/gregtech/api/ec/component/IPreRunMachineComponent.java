package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public interface IPreRunMachineComponent extends IComponent {

    ComponentKey.ListKey<IPreRunMachineComponent> KEY = ComponentKey.list();

    boolean onPreRunMachine(@NotNull IRecipeLogicComponent recipeLogic, IGregTechTileEntity igte, long tick);
}
