package gregtech.api.ec.component;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;

public interface IRecipeCheckHandlerComponent extends IComponent {

    ComponentKey.ListKey<IRecipeCheckHandlerComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IRecipeCheckHandlerComponent::getRecipeCheckPriority));

    enum RecipeCheckPriority {

        DUAL_INPUT(1000),
        CUSTOM_HATCHES(2000),
        STAND_INPUT(3000);

        public final int priority;

        RecipeCheckPriority(int priority) {
            this.priority = priority;
        }
    }

    @NotNull
    CheckRecipeResult tryFindRecipe(@NotNull ProcessingLogic logic, @NotNull CheckRecipeResult lastResult);

    int getRecipeCheckPriority();
}
