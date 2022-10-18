package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;
import java.util.function.Consumer;
import java.util.function.Function;

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
        shouldGetFocus();
    }

    public CoverDataFollower_TextFieldWidget<T> setDataToStateGetter(Function<T, String> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollower_TextFieldWidget<T> setSetter(Consumer<String> setter) {
        super.setSetter(setter);
        return this;
    }

    @Override
    public void updateState(T data) {
        setText(dataToStateGetter.apply(data));
    }
}
