package gregtech.api.gui.modularui;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.api.enums.TieredVariant;

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
            UITexture.fullImage(mod, String.format(location, TieredVariant.BRONZE)),
            UITexture.fullImage(mod, String.format(location, TieredVariant.STEEL)),
            UITexture.fullImage(mod, String.format(location, TieredVariant.PRIMITIVE)));
    }

    public static SteamTexture adaptableTexture(String mod, String location, int imageWidth, int imageHeight,
        int borderWidthPixel) {
        return new SteamTexture(
            AdaptableUITexture
                .of(mod, String.format(location, TieredVariant.BRONZE), imageWidth, imageHeight, borderWidthPixel),
            AdaptableUITexture
                .of(mod, String.format(location, TieredVariant.STEEL), imageWidth, imageHeight, borderWidthPixel),
            AdaptableUITexture
                .of(mod, String.format(location, TieredVariant.PRIMITIVE), imageWidth, imageHeight, borderWidthPixel));
    }

    public UITexture get(TieredVariant variant) {
        return switch (variant) {
            case BRONZE -> bronzeTexture;
            case STEEL -> steelTexture;
            case PRIMITIVE -> primitiveTexture;
            default -> null;
        };
    }

    public AdaptableUITexture getAdaptable(TieredVariant variant) {
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
