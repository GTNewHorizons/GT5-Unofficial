package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.recipe.check.SingleRecipeCheck;

public interface IRecipeLockingComponent extends IMultiblockComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IRecipeLockingComponent> KEY = ComponentKey.unique();

    boolean isRecipeLockingEnabled();

    void setRecipeLocking(boolean enabled);

    SingleRecipeCheck getSingleRecipeCheck();

    void setSingleRecipeCheck(@Nullable SingleRecipeCheck recipeCheck);
}
