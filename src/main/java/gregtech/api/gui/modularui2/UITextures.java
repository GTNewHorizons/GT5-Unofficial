package gregtech.api.gui.modularui2;

import com.cleanroommc.modularui.drawable.AdaptableUITexture;
import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.Mods;
import gregtech.api.gui.modularui.GT_UITextures;

public class UITextures {

    public static final UITexture BACKGROUND_SCREEN_BLUE = AdaptableUITexture.builder()
        .location(Mods.TecTech.ID, "gui/background/screen_blue")
        .adaptable(2)
        .imageSize(90, 72)
        .build();

    public static final UITexture BUTTON_STANDARD_TOGGLE_UP = new UITextureAdapter(
        GT_UITextures.BUTTON_STANDARD_TOGGLE.getSubArea(0f, 0f, 0.5f, 0.5f));
    public static final UITexture BUTTON_STANDARD_TOGGLE_DOWN = new UITextureAdapter(
        GT_UITextures.BUTTON_STANDARD_TOGGLE.getSubArea(0.5f, 0.5f, 1f, 1f));
}
