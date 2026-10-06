package gregtech.common.render;

import java.util.function.Supplier;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.material.GTMaterialTextures;
import gregtech.api.render.ISBRContext;

/// A material texture that stays valid across atlas stitches and resource reloads. It rebuilds on the first face
/// drawn after each stitch, taking the tint from the supplier at that point.
///
/// Faces draw from several chunk meshing threads, so the rebuild is lock free. A race costs at most a duplicate
/// build.
///
/// [#isValidTexture] is constant since [gregtech.api.covers.CoverRegistry] checks cover textures on dedicated
/// servers, which have no atlas.
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

    /// Marks every stored texture stale; each recomposes against the new atlas on its next drawn face.
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
