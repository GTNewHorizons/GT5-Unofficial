package gregtech.common.render;

import java.util.function.Supplier;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.render.ISBRContext;

/// A material texture held by its owner rather than rebuilt per draw. The composed layer stack is resolved from
/// the container on the first face drawn after each atlas stitch, so the layer count and the layer-0 tint follow
/// the art the atlas currently holds: a texture built before the first stitch, or kept across a resource reload,
/// still draws the current art.
///
/// Faces draw from several chunk meshing threads, so the rebuild is lock free -- one volatile field holds an
/// immutable generation-and-texture pair, and a race costs at most a duplicate build.
///
/// [#isValidTexture] is constant: [gregtech.api.covers.CoverRegistry] vets a cover texture at registration, on a
/// dedicated server and before any atlas exists, and that check must not force a build.
public final class GTStitchedMaterialTexture implements ITexture {

    private static volatile int generation;

    private final IIconContainer container;
    private final Supplier<short[]> rgba;
    private final boolean glow;

    private volatile Composition composition;

    public GTStitchedMaterialTexture(IIconContainer container, Supplier<short[]> rgba, boolean glow) {
        this.container = container;
        this.rgba = rgba;
        this.glow = glow;
    }

    /// Marks every stored texture stale, so the next face each one draws recomposes it against the new atlas.
    public static void onAtlasStitched() {
        generation++;
    }

    @Override
    public void renderXPos(ISBRContext ctx) {
        texture().renderXPos(ctx);
    }

    @Override
    public void renderXNeg(ISBRContext ctx) {
        texture().renderXNeg(ctx);
    }

    @Override
    public void renderYPos(ISBRContext ctx) {
        texture().renderYPos(ctx);
    }

    @Override
    public void renderYNeg(ISBRContext ctx) {
        texture().renderYNeg(ctx);
    }

    @Override
    public void renderZPos(ISBRContext ctx) {
        texture().renderZPos(ctx);
    }

    @Override
    public void renderZNeg(ISBRContext ctx) {
        texture().renderZNeg(ctx);
    }

    @Override
    public boolean isValidTexture() {
        return true;
    }

    private ITexture texture() {
        int current = generation;
        Composition composed = composition;
        if (composed == null || composed.generation() != current) {
            composed = new Composition(current, GTMaterialTextures.of(container, rgba.get(), glow));
            composition = composed;
        }
        return composed.texture();
    }

    private record Composition(int generation, ITexture texture) {}
}
