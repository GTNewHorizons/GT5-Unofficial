package tectech.thing.metaTileEntity.multi.base.parameter;

import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ToggleButton;

import gregtech.api.modularui2.GTGuiTextures;

public class BooleanParameter extends Parameter<Boolean> {

    public BooleanParameter(Boolean value, String langKey) {
        super(value, langKey);
    }

    @Override
    public Widget<?> createInputWidget() {
        return new ToggleButton().value(new BooleanSyncValue(this::getValue, this::setValue))
            .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
            .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS);
    }
}
