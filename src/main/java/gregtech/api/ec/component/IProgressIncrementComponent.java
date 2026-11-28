package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;

public interface IProgressIncrementComponent extends IUniqueComponent {

    ComponentKey.UniqueKey<IProgressIncrementComponent> KEY = ComponentKey.unique();

    void incrementProgressTime(@NotNull IRecipeLogicComponent recipeLogic);
}
