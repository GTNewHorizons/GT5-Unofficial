package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import gregtech.api.gui.ModularUI.GT_UITextures;
import gregtech.api.gui.ModularUI.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Determines button state with cover data.
 */
public class CoverDataFollower_CycleButtonWidget<T extends ISerializableObject> extends CoverCycleButtonWidget
        implements IDataFollowerWidget<T, Integer, CoverDataFollower_CycleButtonWidget<T>> {

    private Function<T, Integer> dataToStateGetter;

    public CoverDataFollower_CycleButtonWidget() {
        super();
        setGetter(() -> 0); // fake getter; used only for init
        setSynced(false, false);
    }

    public CoverDataFollower_CycleButtonWidget<T> setDataToStateGetter(Function<T, Integer> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollower_CycleButtonWidget<T> setSetter(Consumer<Integer> setter) {
        super.setSetter(setter);
        return this;
    }

    @Override
    public void updateState(T data) {
        setState(dataToStateGetter.apply(data), false, false);
    }

    public static class CoverDataFollower_ToggleButtonWidget<T extends ISerializableObject>
            extends CoverDataFollower_CycleButtonWidget<T> {
        @Override
        protected boolean canClick() {
            return getState() == 0;
        }

        @Override
        public IDrawable[] getBackground() {
            if (!canClick()) return new IDrawable[] {GT_UITextures.BUTTON_COVER_NORMAL_DISABLED};
            return super.getBackground();
        }
    }
}
