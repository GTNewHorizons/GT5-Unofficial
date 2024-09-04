package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Function;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;

public class CoverDataFollowerNumericWidget<T extends ISerializableObject> extends NumericWidget
    implements IDataFollowerWidget<T, Double> {

    private Function<T, Double> dataToStateGetter;

    public CoverDataFollowerNumericWidget() {
        super();
        setGetter(() -> 0); // fake getter; used only for init
        setSynced(false, false);
        setTextColor(Color.WHITE.dark(1));
        setTextAlignment(Alignment.CenterLeft);
        setBackground(GTUITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2));
    }

    @Override
    public void onPostInit() {
        // Widget#onPostInit is called earlier than IDataFollowerWidget#onPostInit,
        // so we make sure cursor is set after text is set
        super.onPostInit();

        // On first call #handler does not contain text.
        // On second call, it contains correct text to update #lastText,
        // but #shouldGetFocus call is skipped by Cursor#updateFocused,
        // so we need to manually call this.
        if (focusOnGuiOpen) {
            shouldGetFocus();
        }
    }

    @Override
    public CoverDataFollowerNumericWidget<T> setDataToStateGetter(Function<T, Double> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollowerNumericWidget<T> setStateSetter(Consumer<Double> setter) {
        super.setSetter(setter::accept);
        return this;
    }

    @Override
    public void updateState(T data) {
        setValue(dataToStateGetter.apply(data));
    }

}
