package gregtech.api.gui.modularui2;

import com.cleanroommc.modularui.drawable.AdaptableUITexture;
import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.Mods;

public class UITextures {

    public static final UITexture BACKGROUND_SCREEN_BLUE = AdaptableUITexture.builder()
        .location(Mods.TecTech.ID, "gui/background/screen_blue")
        .adaptable(2)
        .imageSize(90, 72)
        .build();
}
