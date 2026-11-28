package gregtech.api.ec.component;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnRecipeStartComponent extends IComponent {

    ComponentKey.ListKey<IOnRecipeStartComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IOnRecipeStartComponent::getRecipeStartPriority));

    enum RecipeStartPriority {

        EFFICIENCY(1000),
        SOUND(2000);

        public final int priority;

        RecipeStartPriority(int priority) {
            this.priority = priority;
        }
    }

    void onRecipeStart(@NotNull IRecipeLogicComponent recipeLogicComponent);

    int getRecipeStartPriority();
}
