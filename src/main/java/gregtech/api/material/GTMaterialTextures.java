package gregtech.api.material;

import java.util.function.Supplier;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.ITextureBuilder;
import gregtech.api.render.TextureFactory;
import gregtech.common.render.GTStitchedMaterialTexture;

/// Builds the [ITexture] a material [IIconContainer] draws as, so a material's art stacks the same way everywhere.
/// `of` fixes the layer-0 tint it is given. `stored` re-resolves that tint after each atlas stitch, for a caller
/// that holds one texture across resource reloads.
public final class GTMaterialTextures {

    private GTMaterialTextures() {}

    /// The [ITexture] drawing `container`'s whole icon layer stack: layer 0 tinted with `rgba` and every further
    /// layer with the color the container reports for it, `glow` applying to all of them. Resource-pack override
    /// art draws untinted unless `untintOverrideArt` is false, as for a painted frame that keeps its dye.
    public static ITexture of(IIconContainer container, short[] rgba, boolean glow, boolean untintOverrideArt) {
        ITextureBuilder builder = TextureFactory.builder()
            .addIcon(container)
            .setRGBA(rgba)
            .glow(glow);
        if (untintOverrideArt) builder = builder.untintOverrideIcon();
        return builder.build();
    }

    /// As [#of(IIconContainer, short[], boolean, boolean)] with override art drawing untinted.
    public static ITexture of(IIconContainer container, short[] rgba, boolean glow) {
        return of(container, rgba, glow, true);
    }

    /// As [#of(IIconContainer, short[], boolean)] for art that does not glow.
    public static ITexture of(IIconContainer container, short[] rgba) {
        return of(container, rgba, false);
    }

    /// The [ITexture] composing `container`'s layer stack as [#of(IIconContainer, short[], boolean)] does, resolved
    /// afresh from `rgba` on the first draw after every atlas stitch.
    public static ITexture stored(IIconContainer container, Supplier<short[]> rgba, boolean glow) {
        return new GTStitchedMaterialTexture(container, rgba, glow);
    }

    /// As [#stored(IIconContainer, Supplier, boolean)] for art that does not glow.
    public static ITexture stored(IIconContainer container, Supplier<short[]> rgba) {
        return stored(container, rgba, false);
    }
}
