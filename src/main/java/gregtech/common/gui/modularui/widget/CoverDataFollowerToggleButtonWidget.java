package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Function;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;

public class CoverDataFollowerToggleButtonWidget<T extends ISerializableObject> extends CoverCycleButtonWidget
    implements IDataFollowerWidget<T, Boolean> {

    private Function<T, Boolean> dataToStateGetter;

    public CoverDataFollowerToggleButtonWidget() {
        super();
        setGetter(() -> 0); // fake getter; used only for init
        setSynced(false, false);
        setLength(2);
    }

    @Override
    public CoverDataFollowerToggleButtonWidget<T> setDataToStateGetter(Function<T, Boolean> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollowerToggleButtonWidget<T> setStateSetter(Consumer<Boolean> setter) {
        super.setSetter(val -> setter.accept(val == 1));
        return this;
    }

    @Override
    public void updateState(T data) {
        setState(dataToStateGetter.apply(data) ? 1 : 0, false, false);
    }

    public CoverDataFollowerToggleButtonWidget<T> setToggleTexture(IDrawable active, IDrawable inactive) {
        setTextureGetter(state -> state == 1 ? active : inactive);
        return this;
    }

    public static <T extends ISerializableObject> CoverDataFollowerToggleButtonWidget<T> ofCheckAndCross() {
        return new CoverDataFollowerToggleButtonWidget<T>()
            .setToggleTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK, GTUITextures.OVERLAY_BUTTON_CROSS);
    }

    public static <T extends ISerializableObject> CoverDataFollowerToggleButtonWidget<T> ofCheck() {
        return new CoverDataFollowerToggleButtonWidget<T>()
            .setToggleTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK, GTUITextures.TRANSPARENT);
    }

    public static <T extends ISerializableObject> CoverDataFollowerToggleButtonWidget<T> ofRedstone() {
        return new CoverDataFollowerToggleButtonWidget<T>()
            .setToggleTexture(GTUITextures.OVERLAY_BUTTON_REDSTONE_ON, GTUITextures.OVERLAY_BUTTON_REDSTONE_OFF);
    }

    public static <T extends ISerializableObject> CoverDataFollowerToggleButtonWidget<T> ofDisableable() {
        return new CoverDataFollower_DisableableToggleButtonWidget<>();
    }

    /**
     * Disables clicking if button is already pressed.
     */
    public static class CoverDataFollower_DisableableToggleButtonWidget<T extends ISerializableObject>
        extends CoverDataFollowerToggleButtonWidget<T> {

        public CoverDataFollower_DisableableToggleButtonWidget() {
            super();
        }

        @Override
        protected boolean canClick() {
            return getState() == 0;
        }

        @Override
        public IDrawable[] getBackground() {
            if (!canClick()) return new IDrawable[] { GTUITextures.BUTTON_COVER_NORMAL_DISABLED };
            return super.getBackground();
        }
    }
}
