package gregtech.api.ec.component.multiblock;

import org.jetbrains.annotations.NotNull;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.enums.HeatingCoilLevel;
import it.unimi.dsi.fastutil.longs.LongArrayList;

public interface ICoilLogicComponent extends IUniqueComponent, IMultiblockComponent {

    ComponentKey.UniqueKey<ICoilLogicComponent> KEY = ComponentKey.unique();

    void addCoilCoordinate(long packedCorrdinate);

    @NotNull
    LongArrayList getCoilCoordinates();

    @NotNull
    HeatingCoilLevel getCoilLevel();

    void setCoilLevel(@NotNull HeatingCoilLevel level);
}
