package gregtech.api.ec.component.multiblock;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IOnRecipeTickComponent;
import gregtech.api.ec.component.IRecipeLogicComponent;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;

public interface IControllerPartComponent extends IMultiblockComponent, IOnRecipeTickComponent {

    ComponentKey.ListKey<IControllerPartComponent> KEY = ComponentKey.list();

    /**
     * Check the ItemStack in the controller slot.
     *
     * @param aStack the ItemStack in the controller slot.
     * @return {@code true} if the item is valid for this machine or the machine doesn't have restrictions on the
     *         controller slot.
     */
    boolean isCorrectMachinePart(ItemStack aStack);

    @Override
    default boolean onRecipeTick(@NotNull IRecipeLogicComponent recipeLogicComponent) {
        var machine = getMachine();
        var controllerTool = machine.getControllerSlot();
        if (!isCorrectMachinePart(controllerTool)) {
            machine.stopMachine(ShutDownReasonRegistry.NO_MACHINE_PART);
            return false;
        }
        return true;
    }

    @Override
    default int getRecipeTickPriority() {
        return RecipeTickPriority.CONTROLLER_PART_CHECK.priority;
    }
}
