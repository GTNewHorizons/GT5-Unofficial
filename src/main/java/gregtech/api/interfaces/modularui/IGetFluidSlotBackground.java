package gregtech.api.interfaces.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;

public interface IGetFluidSlotBackground {
    default IDrawable getFluidSlotBackground() {
        return null;
    }
}
