package gregtech.api.ec.component.multiblock;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;

public interface IBatchConfigComponent extends IMultiblockComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IBatchConfigComponent> KEY = ComponentKey.unique();

    boolean isBatchModeEnabled();

    IBatchConfigComponent setBatchMode(boolean enabled);

    int getMaxBatchSize();

    IBatchConfigComponent setMaxBatchSize(int maxBatchSize);
}
