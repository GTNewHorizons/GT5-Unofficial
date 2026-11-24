package gregtech.api.ec.component.multiblock;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;

public interface IParallelLogicComponent extends IUniqueComponent, IMultiblockComponent {

    ComponentKey.UniqueKey<IParallelLogicComponent> KEY = ComponentKey.unique();

    boolean isAlwaysMaxParallel();

    void setAlwaysMaxParallel(boolean alwaysMaxParallel);

    int getPowerPanelMaxParallel();

    void setPowerPanelMaxParallel(int maxParallel);

    int getMaxParallelRecipes();

    int getMaxParallelForPanel();

    void setMaxParallelForPanel(int maxParallel);

    default int getTrueParallel() {
        return Math.max(
            1,
            isAlwaysMaxParallel() ? getMaxParallelRecipes()
                : Math.min(getMaxParallelRecipes(), getPowerPanelMaxParallel()));
    }
}
