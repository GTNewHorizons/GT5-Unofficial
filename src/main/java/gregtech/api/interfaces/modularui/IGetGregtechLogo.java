package gregtech.api.interfaces.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;

public interface IGetGregtechLogo {
    default IDrawable getGregTechLogo() {
        return null;
    }
}
