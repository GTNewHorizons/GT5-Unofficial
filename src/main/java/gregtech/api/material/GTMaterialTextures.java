package gregtech.api.material;

import gregtech.GTMod;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import gregtech.api.render.TextureFactory;
import gregtech.client.iconContainers.LayerIconContainer;

/// Builds the [ITexture] a material [IIconContainer] draws as: one texture per icon layer, composed bottom up, so
/// every material block and frame stacks its art the same way. Decorative covers deliberately stay off this path
/// and draw their single base texture.
public final class GTMaterialTextures {

    private GTMaterialTextures() {}

    /// The [ITexture] drawing `container`'s whole icon layer stack: layer 0 tinted with `rgba` and every further
    /// layer with the color the container reports for it, `glow` applying to all of them. Art bound from a
    /// resource-pack override draws untinted at every layer; a caller passing `untintOverrideArt` false has
    /// already resolved override art into `rgba` itself, where something outranks the white-out (a painted frame
    /// keeps its dye).
    public static ITexture of(IIconContainer container, short[] rgba, boolean glow, boolean untintOverrideArt) {
        // IIconContainer's layer accessors are client-only, and the getTextures surfaces calling in carry no side
        // annotation, so a dedicated server takes the single-texture path.
        if (!GTMod.proxy.isClientSide()) return layerTexture(container, rgba, glow, untintOverrideArt);

        int tintedLayers = container.getOverlayIcon() != null ? container.getIconPasses() - 1
            : container.getIconPasses();
        if (tintedLayers < 2) return layerTexture(container, rgba, glow, untintOverrideArt);

        ITexture[] layers = new ITexture[container.getIconPasses()];
        layers[0] = layerTexture(new LayerIconContainer(container, 0), rgba, glow, untintOverrideArt);
        for (int layer = 1; layer < layers.length; layer++) {
            layers[layer] = layerTexture(
                new LayerIconContainer(container, layer),
                container.getIconColor(layer),
                glow,
                untintOverrideArt);
        }
        return TextureFactory.of(layers);
    }

    /// As [#of(IIconContainer, short[], boolean, boolean)] with override art drawing untinted.
    public static ITexture of(IIconContainer container, short[] rgba, boolean glow) {
        return of(container, rgba, glow, true);
    }

    /// As [#of(IIconContainer, short[], boolean)] for art that does not glow.
    public static ITexture of(IIconContainer container, short[] rgba) {
        return of(container, rgba, false);
    }

    private static ITexture layerTexture(IIconContainer container, short[] rgba, boolean glow,
        boolean untintOverrideArt) {
        ITextureBuilder builder = TextureFactory.builder()
            .addIcon(container)
            .setRGBA(rgba)
            .glow(glow);
        if (untintOverrideArt) builder = builder.untintOverrideIcon();
        return builder.build();
    }
}
