package gregtech.api.gui.modularui;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
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

    public static SteamTexture fullImage(String mod, String location) {
        return new SteamTexture(
                UITexture.fullImage(mod, String.format(location, SteamVariant.BRONZE)),
                UITexture.fullImage(mod, String.format(location, SteamVariant.STEEL)),
                UITexture.fullImage(mod, String.format(location, SteamVariant.PRIMITIVE)));
    }

    public static SteamTexture adaptableTexture(String mod, String location, int imageWidth, int imageHeight,
            int borderWidthPixel) {
        return new SteamTexture(
                AdaptableUITexture.of(
                        mod,
                        String.format(location, SteamVariant.BRONZE),
                        imageWidth,
                        imageHeight,
                        borderWidthPixel),
                AdaptableUITexture.of(
                        mod,
                        String.format(location, SteamVariant.STEEL),
                        imageWidth,
                        imageHeight,
                        borderWidthPixel),
                AdaptableUITexture.of(
                        mod,
                        String.format(location, SteamVariant.PRIMITIVE),
                        imageWidth,
                        imageHeight,
                        borderWidthPixel));
    }

    public UITexture get(SteamVariant variant) {
        switch (variant) {
            case BRONZE:
                return bronzeTexture;
            case STEEL:
                return steelTexture;
            case PRIMITIVE:
                return primitiveTexture;
            default:
                return null;
        }
    }

    public AdaptableUITexture getAdaptable(SteamVariant variant) {
        switch (variant) {
            case BRONZE:
                return (AdaptableUITexture) bronzeTexture;
            case STEEL:
                return (AdaptableUITexture) steelTexture;
            case PRIMITIVE:
                return (AdaptableUITexture) primitiveTexture;
            default:
                return null;
        }
    }

    public UITexture get(boolean isHighPressure) {
        return isHighPressure ? steelTexture : bronzeTexture;
    }
}
