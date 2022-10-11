package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;
import gregtech.api.gui.ModularUI.GT_UITextures;
import gregtech.api.gui.ModularUI.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;
import java.util.function.Consumer;
import java.util.function.Function;

public class CoverDataFollower_TextFieldWidget<T extends ISerializableObject> extends TextFieldWidget
        implements IDataFollowerWidget<T, String, CoverDataFollower_TextFieldWidget<T>> {

    private Function<T, String> dataToStateGetter;

    public CoverDataFollower_TextFieldWidget() {
        super();
        setGetter(() -> ""); // fake getter; used only for init
        setSynced(false, false);
        setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD.withOffset(-1, -1, 2, 2));
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
