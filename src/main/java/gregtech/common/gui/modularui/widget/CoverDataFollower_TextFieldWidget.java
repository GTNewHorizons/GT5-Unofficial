package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.client.gui.GuiScreen;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.MathExpression;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;

public class CoverDataFollower_TextFieldWidget<T extends ISerializableObject> extends TextFieldWidget
    implements IDataFollowerWidget<T, String> {

    private Function<T, String> dataToStateGetter;

    public CoverDataFollower_TextFieldWidget() {
        super();
        setGetter(() -> ""); // fake getter; used only for init
        setSynced(false, false);
        setTextColor(Color.WHITE.dark(1));
        setTextAlignment(Alignment.CenterLeft);
        setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2));
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
    public CoverDataFollower_TextFieldWidget<T> setDataToStateGetter(Function<T, String> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollower_TextFieldWidget<T> setStateSetter(Consumer<String> setter) {
        super.setSetter(setter);
        return this;
    }

    @Override
    public void updateState(T data) {
        setText(dataToStateGetter.apply(data));
    }

    public CoverDataFollower_TextFieldWidget<T> setOnScrollNumbers(int baseStep, int ctrlStep, int shiftStep) {
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

    public CoverDataFollower_TextFieldWidget<T> setOnScrollNumbers() {
        return setOnScrollNumbers(1, 50, 1000);
    }

    public CoverDataFollower_TextFieldWidget<T> setOnScrollText(int baseStep, int ctrlStep, int shiftStep) {
        setOnScroll((text, direction) -> {
            int val = (int) MathExpression.parseMathExpression(text, -1);
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

    public CoverDataFollower_TextFieldWidget<T> setOnScrollText() {
        return setOnScrollText(1, 5, 50);
    }

    public CoverDataFollower_TextFieldWidget<T> setOnScrollNumbersLong(long baseStep, long ctrlStep, long shiftStep) {
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
