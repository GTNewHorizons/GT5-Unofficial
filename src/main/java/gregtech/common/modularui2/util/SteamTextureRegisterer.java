package gregtech.common.modularui2.util;

import static gregtech.api.enums.Mods.GregTech;

import com.cleanroommc.modularui.drawable.UITexture;

import gregtech.api.enums.SteamVariant;

/**
 * Registers {@link UITexture}s for bronze, steel and primitive variants, all at once.
 */
public class SteamTextureRegisterer {

    public static SteamTextureRegisterer builder() {
        return new SteamTextureRegisterer();
    }

    private final UITexture.Builder bronzeBuilder = UITexture.builder();
    private final UITexture.Builder steelBuilder = UITexture.builder();
    private final UITexture.Builder primitiveBuilder = UITexture.builder();

    private SteamTextureRegisterer() {}

    public SteamTextureRegisterer location(String pathPlaceholder) {
        bronzeBuilder.location(GregTech.ID, String.format(pathPlaceholder, SteamVariant.BRONZE));
        steelBuilder.location(GregTech.ID, String.format(pathPlaceholder, SteamVariant.STEEL));
        primitiveBuilder.location(GregTech.ID, String.format(pathPlaceholder, SteamVariant.PRIMITIVE));
        return this;
    }

    public SteamTextureRegisterer imageSize(int w, int h) {
        bronzeBuilder.imageSize(w, h);
        steelBuilder.imageSize(w, h);
        primitiveBuilder.imageSize(w, h);
        return this;
    }

    public SteamTextureRegisterer fullImage() {
        bronzeBuilder.fullImage();
        steelBuilder.fullImage();
        primitiveBuilder.fullImage();
        return this;
    }

    public SteamTextureRegisterer adaptable(int border) {
        bronzeBuilder.adaptable(border);
        steelBuilder.adaptable(border);
        primitiveBuilder.adaptable(border);
        return this;
    }

    public SteamTextureRegisterer canApplyTheme() {
        bronzeBuilder.canApplyTheme();
        steelBuilder.canApplyTheme();
        primitiveBuilder.canApplyTheme();
        return this;
    }

    public SteamTextureRegisterer name(String bronzeName, String steelName, String primitiveName) {
        bronzeBuilder.name(bronzeName);
        steelBuilder.name(steelName);
        primitiveBuilder.name(primitiveName);
        return this;
    }

    public SteamTextureRegisterer build() {
        bronzeBuilder.build();
        steelBuilder.build();
        primitiveBuilder.build();
        return this;
    }
}
