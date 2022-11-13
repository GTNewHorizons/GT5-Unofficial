package gregtech.api.interfaces.modularui;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

public interface IGetBackground {
    default UITexture getBackground() {
        return null;
    }
    ;
}
