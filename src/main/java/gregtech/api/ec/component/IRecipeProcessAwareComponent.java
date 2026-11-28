package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.recipe.check.CheckRecipeResult;

public interface IRecipeProcessAwareComponent extends IComponent {

    ComponentKey.ListKey<IRecipeProcessAwareComponent> KEY = ComponentKey.list();

    void startRecipeProcessing();

    @NotNull
    CheckRecipeResult endRecipeProcessing();
}
