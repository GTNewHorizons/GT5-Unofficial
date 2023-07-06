package gregtech.api.gui.modularui;

import static gregtech.api.gui.modularui.GT_UITextures.GUI_PATH;

import com.cleanroommc.modularui.drawable.AdaptableUITexture;
import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.SteamVariant;

/**
 * Wrapper for {@link UITexture}s used to ease in choosing between Bronze, Steel and Primitive textures.
 */
public class SteamTexture {

    private final UITexture bronzeTexture;
    private final UITexture steelTexture;
    private final UITexture primitiveTexture;

    private SteamTexture(UITexture bronzeTexture, UITexture steelTexture, UITexture primitiveTexture) {
        this.bronzeTexture = bronzeTexture;
        this.steelTexture = steelTexture;
        this.primitiveTexture = primitiveTexture;
    }

    public static SteamTexture fullImage(String location, boolean canApplyTheme) {
        return new SteamTexture(
            UITexture.fullImage(GUI_PATH.apply(String.format(location, SteamVariant.BRONZE)), canApplyTheme),
            UITexture.fullImage(GUI_PATH.apply(String.format(location, SteamVariant.STEEL)), canApplyTheme),
            UITexture.fullImage(GUI_PATH.apply(String.format(location, SteamVariant.PRIMITIVE)), canApplyTheme));
    }

    public static SteamTexture adaptableTexture(String location, int imageWidth, int imageHeight,
        int borderWidthPixel) {
        return new SteamTexture(
            UITexture.builder()
                .location(GUI_PATH.apply(String.format(location, SteamVariant.BRONZE)))
                .imageSize(imageWidth, imageHeight)
                .adaptable(borderWidthPixel)
                .canApplyTheme()
                .build(),
            UITexture.builder()
                .location(GUI_PATH.apply(String.format(location, SteamVariant.STEEL)))
                .imageSize(imageWidth, imageHeight)
                .adaptable(borderWidthPixel)
                .canApplyTheme()
                .build(),
            UITexture.builder()
                .location(GUI_PATH.apply(String.format(location, SteamVariant.PRIMITIVE)))
                .imageSize(imageWidth, imageHeight)
                .adaptable(borderWidthPixel)
                .canApplyTheme()
                .build());
    }

    public UITexture get(SteamVariant variant) {
        return switch (variant) {
            case BRONZE -> bronzeTexture;
            case STEEL -> steelTexture;
            case PRIMITIVE -> primitiveTexture;
            default -> null;
        };
    }

    public AdaptableUITexture getAdaptable(SteamVariant variant) {
        return switch (variant) {
            case BRONZE -> (AdaptableUITexture) bronzeTexture;
            case STEEL -> (AdaptableUITexture) steelTexture;
            case PRIMITIVE -> (AdaptableUITexture) primitiveTexture;
            default -> null;
        };
    }

    public UITexture get(boolean isHighPressure) {
        return isHighPressure ? steelTexture : bronzeTexture;
    }
}
