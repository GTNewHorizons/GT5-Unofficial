package gregtech.api.interfaces.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;

public interface IGetSlotBackground {
    default IDrawable getSlotBackground() {
        return null;
    }
}
