package gregtech.api.ec.component;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnRecipeFinishComponent extends IComponent {

    ComponentKey.ListKey<IOnRecipeFinishComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IOnRecipeFinishComponent::getRecipeFinishPriority));

    enum RecipeFinishPriority {

        DRONE_COLLECT_DATA(1000),
        EFFICIENCY(2000);

        public final int priority;

        RecipeFinishPriority(int priority) {
            this.priority = priority;
        }
    }

    void onRecipeFinish(@NotNull IRecipeLogicComponent recipeLogicComponent);

    int getRecipeFinishPriority();
}
