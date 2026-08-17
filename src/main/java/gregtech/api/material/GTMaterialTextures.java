package gregtech.api.material;

import gregtech.GTMod;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.client.iconContainers.LayerIconContainer;

/// Builds the [ITexture] a material [IIconContainer] draws as: one texture per icon layer, composed bottom up, so
/// every material block, frame and cover stacks its art the same way.
public final class GTMaterialTextures {

    private GTMaterialTextures() {}

    /// The [ITexture] drawing `container`'s whole icon layer stack: layer 0 tinted with `rgba` and every further
    /// layer with the color the container reports for it, `glow` applying to all of them. Art bound from a
    /// resource-pack override draws untinted at every layer.
    public static ITexture of(IIconContainer container, short[] rgba, boolean glow) {
        // A dedicated server builds cover textures too, and IIconContainer's layer accessors are client-only.
        if (!GTMod.proxy.isClientSide()) return layerTexture(container, rgba, glow);

        int tintedLayers = container.getOverlayIcon() != null ? container.getIconPasses() - 1
            : container.getIconPasses();
        if (tintedLayers < 2) return layerTexture(container, rgba, glow);

        ITexture[] layers = new ITexture[container.getIconPasses()];
        layers[0] = layerTexture(new LayerIconContainer(container, 0), rgba, glow);
        for (int layer = 1; layer < layers.length; layer++) {
            layers[layer] = layerTexture(new LayerIconContainer(container, layer), container.getIconColor(layer), glow);
        }
        return TextureFactory.of(layers);
    }

    /// As [#of(IIconContainer, short[], boolean)] for art that does not glow.
    public static ITexture of(IIconContainer container, short[] rgba) {
        return of(container, rgba, false);
    }

    private static ITexture layerTexture(IIconContainer container, short[] rgba, boolean glow) {
        return TextureFactory.builder()
            .addIcon(container)
            .setRGBA(rgba)
            .glow(glow)
            .untintOverrideIcon()
            .build();
    }
}
