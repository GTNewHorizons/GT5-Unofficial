package gregtech.api.ec.component;

import java.util.Comparator;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IOnRecipeTickComponent extends IComponent {

    ComponentKey.ListKey<IOnRecipeTickComponent> KEY = ComponentKey
        .list(Comparator.comparingInt(IOnRecipeTickComponent::getRecipeTickPriority));

    enum RecipeTickPriority {

        CONTROLLER_PART_CHECK(1000),
        MAINTAIN_CHECK(2000),
        RANDOM_MAINTAIN_ISSUE(3000),
        CONSUME_ENERGY(4000),
        POLLUTE(5000);

        public final int priority;

        RecipeTickPriority(int priority) {
            this.priority = priority;
        }
    }

    boolean onRecipeTick(@NotNull IRecipeLogicComponent recipeLogicComponent);

    int getRecipeTickPriority();
}
