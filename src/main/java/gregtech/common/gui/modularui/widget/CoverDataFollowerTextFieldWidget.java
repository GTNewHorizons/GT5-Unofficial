package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizon.gtnhlib.util.parsing.MathExpressionParser;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.gui.modularui.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;

public class CoverDataFollowerTextFieldWidget<T extends ISerializableObject> extends TextFieldWidget
    implements IDataFollowerWidget<T, String> {

    private Function<T, String> dataToStateGetter;

    public CoverDataFollowerTextFieldWidget() {
        super();
        setGetter(() -> ""); // fake getter; used only for init
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
    public CoverDataFollowerTextFieldWidget<T> setDataToStateGetter(Function<T, String> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollowerTextFieldWidget<T> setStateSetter(Consumer<String> setter) {
        super.setSetter(setter);
        return this;
    }

    @Override
    public void updateState(T data) {
        setText(dataToStateGetter.apply(data));
    }

    /**
     * @deprecated Use {@link CoverDataFollowerNumericWidget}
     */
    @Deprecated
    public CoverDataFollowerTextFieldWidget<T> setOnScrollNumbers(int baseStep, int ctrlStep, int shiftStep) {
        setOnScrollNumbers((val, direction) -> {
            int step = (GuiScreen.isShiftKeyDown() ? shiftStep : GuiScreen.isCtrlKeyDown() ? ctrlStep : baseStep)
                * direction;
            try {
                val = Math.addExact(val, step);
            } catch (ArithmeticException ignored) {
                val = Integer.MAX_VALUE;
            }
            return val;
        });
        return this;
    }

    /**
     * @deprecated Use {@link CoverDataFollowerNumericWidget}
     */
    @Deprecated
    public CoverDataFollowerTextFieldWidget<T> setOnScrollNumbers() {
        return setOnScrollNumbers(1, 50, 1000);
    }

    /**
     * @deprecated Use {@link CoverDataFollowerNumericWidget}
     */
    @Deprecated
    public CoverDataFollowerTextFieldWidget<T> setOnScrollText(int baseStep, int ctrlStep, int shiftStep) {
        setOnScroll((text, direction) -> {
            int val = (int) MathExpressionParser.parse(text);
            int step = (GuiScreen.isShiftKeyDown() ? shiftStep : GuiScreen.isCtrlKeyDown() ? ctrlStep : baseStep)
                * direction;
            try {
                val = Math.addExact(val, step);
            } catch (ArithmeticException ignored) {
                val = Integer.MAX_VALUE;
            }
            return this.getDecimalFormatter()
                .format(val);
        });
        return this;
    }

    /**
     * @deprecated Use {@link CoverDataFollowerNumericWidget}
     */
    @Deprecated
    public CoverDataFollowerTextFieldWidget<T> setOnScrollText() {
        return setOnScrollText(1, 5, 50);
    }

    /**
     * @deprecated Use {@link CoverDataFollowerNumericWidget}
     */
    @Deprecated
    public CoverDataFollowerTextFieldWidget<T> setOnScrollNumbersLong(long baseStep, long ctrlStep, long shiftStep) {
        setOnScrollNumbersLong((val, direction) -> {
            long step = (GuiScreen.isShiftKeyDown() ? shiftStep : GuiScreen.isCtrlKeyDown() ? ctrlStep : baseStep)
                * direction;
            try {
                val = Math.addExact(val, step);
            } catch (ArithmeticException ignored) {
                val = Long.MAX_VALUE;
            }
            return val;
        });
        return this;
    }
}
