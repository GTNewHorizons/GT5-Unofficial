package gregtech.api.ec.component;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnRecipeIdleComponent extends IComponent {

    ComponentKey.ListKey<IOnRecipeIdleComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IOnRecipeIdleComponent::getRecipeIdlePriority));

    enum RecipeIdlePriority {

        EFFICIENCY(1000);

        public final int priority;

        RecipeIdlePriority(int priority) {
            this.priority = priority;
        }
    }

    void onRecipeIdle(@NotNull IRecipeLogicComponent recipeLogicComponent);

    int getRecipeIdlePriority();
}
