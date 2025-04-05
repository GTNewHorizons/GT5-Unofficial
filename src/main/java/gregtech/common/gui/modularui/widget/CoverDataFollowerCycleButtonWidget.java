package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Function;

import gregtech.api.gui.modularui.ICoverDataFollowerWidget;
import gregtech.common.covers.Cover;

/**
 * Determines button state with cover data.
 */
public class CoverDataFollowerCycleButtonWidget<T extends Cover> extends CoverCycleButtonWidget
    implements ICoverDataFollowerWidget<T, Integer> {

    private Function<T, Integer> dataToStateGetter;

    public CoverDataFollowerCycleButtonWidget() {
        super();
        setGetter(() -> 0); // fake getter; used only for init
        setSynced(false, false);
    }

    @Override
    public CoverDataFollowerCycleButtonWidget<T> setDataToStateGetter(Function<T, Integer> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollowerCycleButtonWidget<T> setStateSetter(Consumer<Integer> setter) {
        super.setSetter(setter);
        return this;
    }

    @Override
    public void updateState(T data) {
        setState(dataToStateGetter.apply(data), false, false);
    }
}
