package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.api.drawable.AdaptableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import java.util.Locale;

/**
 * Wrapper around {@link UITexture}s used to
 * ease in choosing between Bronze, Steel and Primitive textures.
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
                UITexture.fullImage(mod, String.format(location, Variant.BRONZE)),
                UITexture.fullImage(mod, String.format(location, Variant.STEEL)),
                UITexture.fullImage(mod, String.format(location, Variant.PRIMITIVE)));
    }

    public static SteamTexture adaptableTexture(
            String mod, String location, int imageWidth, int imageHeight, int borderWidthPixel) {
        return new SteamTexture(
                AdaptableUITexture.of(
                        mod, String.format(location, Variant.BRONZE), imageWidth, imageHeight, borderWidthPixel),
                AdaptableUITexture.of(
                        mod, String.format(location, Variant.STEEL), imageWidth, imageHeight, borderWidthPixel),
                AdaptableUITexture.of(
                        mod, String.format(location, Variant.PRIMITIVE), imageWidth, imageHeight, borderWidthPixel));
    }

    public UITexture get(Variant variant) {
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

    public UITexture get(boolean isHighPressure) {
        return isHighPressure ? steelTexture : bronzeTexture;
    }

    public enum Variant {
        BRONZE,
        STEEL,
        PRIMITIVE;

        @Override
        public String toString() {
            return super.toString().toLowerCase(Locale.ENGLISH);
        }
    }
}
