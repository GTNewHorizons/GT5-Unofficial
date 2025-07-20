package gregtech.api.gui.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;

import gregtech.api.gui.modularui.GTUITextures;

public class CheckboxWidget extends ButtonWidget {

    private final BooleanSupplier getter;
    private final CheckboxClicked setter;

    public interface CheckboxClicked {

        void onClicked(CheckboxWidget widget, boolean checked);
    }

    public CheckboxWidget(BooleanSupplier getter, CheckboxClicked setter) {
        this.getter = getter;
        this.setter = setter;

        setPlayClickSound(true);
        setOnClick((clickData, widget) -> setter.onClicked(this, !getter.getAsBoolean()));
    }

    @Override
    public IDrawable[] getBackground() {
        List<UITexture> ret = new ArrayList<>();
        if (getter.getAsBoolean()) {
            ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
            ret.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
        } else {
            ret.add(GTUITextures.BUTTON_STANDARD);
            ret.add(GTUITextures.OVERLAY_BUTTON_CROSS);
        }
        return ret.toArray(new IDrawable[0]);
    }
}
