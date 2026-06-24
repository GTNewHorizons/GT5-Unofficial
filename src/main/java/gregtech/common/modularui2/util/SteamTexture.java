package gregtech.common.modularui2.util;

import static gregtech.api.enums.Mods.GregTech;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.TieredVariant;

/**
 * Registers {@link UITexture}s for bronze, steel and primitive variants, all at once.
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

    public UITexture get(TieredVariant variant) {
        return switch (variant) {
            case TieredVariant.BRONZE -> bronzeTexture;
            case TieredVariant.STEEL -> steelTexture;
            case TieredVariant.PRIMITIVE -> primitiveTexture;
            default -> null;
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static SteamTexture NONE = new SteamTexture(null, null, null);

    public static class Builder {

        private Builder() {}

        private final UITexture.Builder bronzeBuilder = UITexture.builder();
        private final UITexture.Builder steelBuilder = UITexture.builder();
        private final UITexture.Builder primitiveBuilder = UITexture.builder();

        public Builder location(String pathPlaceholder) {
            bronzeBuilder.location(GregTech.ID, String.format(pathPlaceholder, TieredVariant.BRONZE));
            steelBuilder.location(GregTech.ID, String.format(pathPlaceholder, TieredVariant.STEEL));
            primitiveBuilder.location(GregTech.ID, String.format(pathPlaceholder, TieredVariant.PRIMITIVE));
            return this;
        }

        public Builder imageSize(int w, int h) {
            bronzeBuilder.imageSize(w, h);
            steelBuilder.imageSize(w, h);
            primitiveBuilder.imageSize(w, h);
            return this;
        }

        public Builder fullImage() {
            bronzeBuilder.fullImage();
            steelBuilder.fullImage();
            primitiveBuilder.fullImage();
            return this;
        }

        public Builder adaptable(int border) {
            bronzeBuilder.adaptable(border);
            steelBuilder.adaptable(border);
            primitiveBuilder.adaptable(border);
            return this;
        }

        public Builder canApplyTheme() {
            bronzeBuilder.canApplyTheme();
            steelBuilder.canApplyTheme();
            primitiveBuilder.canApplyTheme();
            return this;
        }

        public Builder name(String bronzeName, String steelName, String primitiveName) {
            bronzeBuilder.name(bronzeName);
            steelBuilder.name(steelName);
            primitiveBuilder.name(primitiveName);
            return this;
        }

        public Builder name(String namePlaceholder) {
            bronzeBuilder.name(String.format(namePlaceholder, TieredVariant.BRONZE));
            steelBuilder.name(String.format(namePlaceholder, TieredVariant.STEEL));
            primitiveBuilder.name(String.format(namePlaceholder, TieredVariant.PRIMITIVE));
            return this;
        }

        public SteamTexture build() {
            return new SteamTexture(bronzeBuilder.build(), steelBuilder.build(), primitiveBuilder.build());
        }
    }
}
