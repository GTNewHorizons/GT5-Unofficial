package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.check.CheckRecipeResult;

public interface IPostRecipeCheckComponent extends IComponent {

    ComponentKey.ListKey<IPostRecipeCheckComponent> KEY = ComponentKey.list();

    @NotNull
    CheckRecipeResult postCheckRecipe(@NotNull CheckRecipeResult result, @NotNull ProcessingLogic logic);
}
