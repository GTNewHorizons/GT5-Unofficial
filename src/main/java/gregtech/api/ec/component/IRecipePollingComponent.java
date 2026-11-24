package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IRecipePollingComponent extends IComponent {

    ComponentKey.ListKey<IRecipePollingComponent> KEY = ComponentKey.list();

    boolean shouldPollRecipe(long totalRuntime, long timeElapsed);
}
