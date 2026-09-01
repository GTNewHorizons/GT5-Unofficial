package tectech.voidcraft.render;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.VertexBufferType;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.render.shader.SharedShaders;
import tectech.TecTech;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;

/**
 * Builds the ship hologram VAO from a {@link VoidcraftBlueprint} (client only).
 *
 * <p>
 * Every occupied blueprint cell is drawn as direct face quads textured with the component's own block-atlas icon
 * — the same art the in-world block face and the item use — into a single captured {@link IVertexArrayObject} in
 * the shared textured shader's vertex format (position + UV), one draw call at render time. Covers mounted on a
 * visible face are added as thin textured quads just outside that face (the cover's own icon from the block
 * atlas).
 *
 * <p>
 * The icons are read LIVE from the block atlas by their registered name. Two other resolution routes exist and
 * both fail this use case: the machine block's {@code getIcon(side, meta)} returns one generic LV machine
 * texture for every meta, and the MTE's cached {@code ITexture} can hold an icon whose container was never
 * registered (a name resolved after GT's {@code cleanup()} — see the {@code VoidcraftTextures} javadoc). Either
 * way the failure is silent: the face simply never gets drawn. A name missing from the atlas instead logs once.
 *
 * <p>
 * Face culling is precomputed from the grid itself: a face touching an occupied neighbor cell is hidden
 * (all ship components are opaque machine blocks), and fully enclosed cells are skipped entirely.
 *
 * <p>
 * Must be called on the client main thread (texture binding + tessellator capture).
 */
@SideOnly(Side.CLIENT)
public final class ShipModelBuilder {

    /** Cover quads sit this far outside the hull face. */
    public static final float COVER_QUAD_OFFSET = 0.003f;

    /** One warning per missing icon name per session. */
    private static final Set<String> WARNED_MISSING_ICONS = new HashSet<>();

    private ShipModelBuilder() {
        throw new AssertionError("Static helpers");
    }

    /**
     * Build the hologram for this blueprint.
     *
     * @param blueprint the digitized ship (w/h/d + component grid)
     * @return the built model (never null — an empty grid yields an empty model, which renders nothing)
     */
    public static ShipModel build(VoidcraftBlueprint blueprint) {
        int w = blueprint.width;
        int h = blueprint.height;
        int d = blueprint.depth;

        // Occupancy (grid value 0 = empty).
        boolean[][][] occupied = new boolean[w][h][d];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    occupied[x][y][z] = blueprint.grid[cellIndex(w, h, x, y, z)] != 0;
                }
            }
        }

        // Per-cell face visibility (neighbor occupied → hide the shared face).
        ShipRenderFacesInfo[][][] faces = new ShipRenderFacesInfo[w][h][d];
        int visibleCells = 0;
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                for (int z = 0; z < d; z++) {
                    if (!occupied[x][y][z]) {
                        continue;
                    }
                    ShipRenderFacesInfo info = new ShipRenderFacesInfo(true);
                    if (x > 0 && occupied[x - 1][y][z]) info.setXNeg(false);
                    if (x < w - 1 && occupied[x + 1][y][z]) info.setXPos(false);
                    if (y > 0 && occupied[x][y - 1][z]) info.setYNeg(false);
                    if (y < h - 1 && occupied[x][y + 1][z]) info.setYPos(false);
                    if (z > 0 && occupied[x][y][z - 1]) info.setZNeg(false);
                    if (z < d - 1 && occupied[x][y][z + 1]) info.setZPos(false);
                    faces[x][y][z] = info;
                    if (!info.allHidden()) {
                        visibleCells++;
                    }
                }
            }
        }

        if (visibleCells == 0) {
            return new ShipModel(emptyVao(), w, h, d);
        }

        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);
        TextureMap textureMap = (TextureMap) mc.getTextureManager()
            .getTexture(TextureMap.locationBlocksTexture);

        Tessellator tessellator = TessellatorManager.startCapturingDirect(captureFormat());
        // DirectTessellator.uploadToVBO only routes through VertexOptimizer.optimizeQuads when the tessellator's
        // drawMode is GL_QUADS; it defaults to 0 (GL_POINTS), which uploads the whole model as a point list.
        tessellator.startDrawingQuads();
        try {
            // Pass 1: the hull cells — one quad per visible face, the component's own icon from the block atlas.
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    for (int z = 0; z < d; z++) {
                        if (!occupied[x][y][z] || faces[x][y][z].allHidden()) {
                            continue;
                        }
                        int cell = cellIndex(w, h, x, y, z);
                        VoidcraftComponent component = VoidcraftComponent.fromGridValue(blueprint.grid[cell])
                            .orElse(null);
                        if (component == null) {
                            continue; // corrupt grid value (blueprint validation rejects these)
                        }
                        String iconName = VoidcraftTextures.componentIconName(component);
                        IIcon icon = textureMap.getAtlasSprite(iconName);
                        if (icon == null || isMissingSprite(icon)) {
                            warnMissingIcon(iconName);
                            continue;
                        }
                        tessellator.setTranslation(x, y, z);
                        renderHullFaces(tessellator, blueprint, cell, faces[x][y][z], icon);
                    }
                }
            }

            // Pass 2: the covers — one textured quad per mounted cover on a visible face, floating just outside
            // the hull (COVER_QUAD_OFFSET).
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    for (int z = 0; z < d; z++) {
                        if (!occupied[x][y][z]) {
                            continue;
                        }
                        int cell = cellIndex(w, h, x, y, z);
                        tessellator.setTranslation(x, y, z);
                        renderCovers(tessellator, blueprint, cell, faces[x][y][z], textureMap);
                    }
                }
            }
            tessellator.setTranslation(0, 0, 0);
        } finally {
            IVertexArrayObject vao = TessellatorManager.stopCapturingDirectToVBO(VertexBufferType.IMMUTABLE);
            return new ShipModel(vao, w, h, d);
        }
    }

    /**
     * The vertex format the ship model is captured into: the shared textured shader's format (position + UV,
     * in that shader's attribute order). The direct tessellator writes only the elements present in the format
     * (a normal for the hull faces is dropped), and the model is drawn with that shader.
     */
    private static VertexFormat captureFormat() {
        if (SharedShaders.ready()) {
            return SharedShaders.textured()
                .vertexFormat();
        }
        // The shaders are not (re)loaded yet — capture nothing meaningful; the capture stays a no-op empty VAO
        // and the next resource reload re-bakes the shader and clears the model cache.
        return DefaultVertexFormat.POSITION_TEXTURE_NORMAL;
    }

    /**
     * Draw a cover quad on every VISIBLE face of the cell that has a mounted cover. The quad sits just outside
     * the hull face and is textured with the cover's own icon, so the in-flight ship shows exactly what the
     * in-world face shows.
     */
    private static void renderCovers(Tessellator tess, VoidcraftBlueprint blueprint, int cell, ShipRenderFacesInfo info,
        TextureMap textureMap) {
        boolean[] visible = { info.isYNeg(), info.isYPos(), info.isZNeg(), info.isZPos(), info.isXNeg(),
            info.isXPos() };
        for (int side = 0; side < 6; side++) {
            int coverValue = blueprint.coverGrid[cell * 6 + side];
            if (coverValue == 0 || !visible[side]) {
                continue; // no cover, or buried in the hull (not an exposed face)
            }
            VoidcraftCoverComponent cover = VoidcraftCoverComponent.fromGridValue(coverValue)
                .orElse(null);
            if (cover == null) {
                continue; // corrupt grid value
            }
            // the icon lives in the block atlas (GTCustomBlockIconContainer) — read the live IIcon by its
            // registered name (avoids the BlockIcons dedup-map, which cleanup() clears)
            String iconName = VoidcraftTextures.coverIconName(cover);
            IIcon icon = textureMap.getAtlasSprite(iconName);
            if (icon == null || isMissingSprite(icon)) {
                warnMissingIcon(iconName);
                continue;
            }
            renderFace(tess, side, icon, COVER_QUAD_OFFSET);
        }
    }

    /**
     * One quad per VISIBLE face of the unit cube (the caller has already translated to the cell), UVs from the
     * component icon. Faces carrying a mounted cover are skipped: the cover pass draws that face itself (same
     * footprint, floating COVER_QUAD_OFFSET along the normal) — with the model CELL_SIZE-scaled, a hull quad
     * behind the cover sits under a depth-buffer step at range, so the pair z-fights; the cover alone reads the
     * face.
     */
    private static void renderHullFaces(Tessellator tess, VoidcraftBlueprint blueprint, int cell,
        ShipRenderFacesInfo info, IIcon icon) {
        if (info.isYNeg() && blueprint.coverGrid[cell * 6] == 0) {
            renderFace(tess, 0, icon, 0f);
        }
        if (info.isYPos() && blueprint.coverGrid[cell * 6 + 1] == 0) {
            renderFace(tess, 1, icon, 0f);
        }
        if (info.isZNeg() && blueprint.coverGrid[cell * 6 + 2] == 0) {
            renderFace(tess, 2, icon, 0f);
        }
        if (info.isZPos() && blueprint.coverGrid[cell * 6 + 3] == 0) {
            renderFace(tess, 3, icon, 0f);
        }
        if (info.isXNeg() && blueprint.coverGrid[cell * 6 + 4] == 0) {
            renderFace(tess, 4, icon, 0f);
        }
        if (info.isXPos() && blueprint.coverGrid[cell * 6 + 5] == 0) {
            renderFace(tess, 5, icon, 0f);
        }
    }

    /**
     * One quad on the unit cube's {@code side} face, {@code offset} along the face normal (the caller has already
     * translated to the cell), UVs from the icon, upright when the face is viewed from outside (side faces: world
     * +Y up; top/bottom: arbitrary orientation).
     */
    private static void renderFace(Tessellator tess, int side, IIcon icon, float offset) {
        // unit-cube corners of the face, in draw order (corner i takes UV pair i) — (x, y, z)
        final float[][] C;
        final float[] N;
        switch (side) {
            case 0: // -Y
                C = new float[][] { { 0, 0, 1 }, { 1, 0, 1 }, { 1, 0, 0 }, { 0, 0, 0 } };
                N = new float[] { 0, -1, 0 };
                break;
            case 1: // +Y
                C = new float[][] { { 0, 1, 0 }, { 1, 1, 0 }, { 1, 1, 1 }, { 0, 1, 1 } };
                N = new float[] { 0, 1, 0 };
                break;
            case 2: // -Z
                C = new float[][] { { 1, 0, 0 }, { 0, 0, 0 }, { 0, 1, 0 }, { 1, 1, 0 } };
                N = new float[] { 0, 0, -1 };
                break;
            case 3: // +Z
                C = new float[][] { { 0, 0, 1 }, { 1, 0, 1 }, { 1, 1, 1 }, { 0, 1, 1 } };
                N = new float[] { 0, 0, 1 };
                break;
            case 4: // -X
                C = new float[][] { { 0, 0, 0 }, { 0, 0, 1 }, { 0, 1, 1 }, { 0, 1, 0 } };
                N = new float[] { -1, 0, 0 };
                break;
            default: // +X
                C = new float[][] { { 1, 0, 1 }, { 1, 0, 0 }, { 1, 1, 0 }, { 1, 1, 1 } };
                N = new float[] { 1, 0, 0 };
                break;
        }
        float u0 = icon.getMinU();
        float u1 = icon.getMaxU();
        float v0 = icon.getMinV(); // icon top
        float v1 = icon.getMaxV(); // icon bottom
        float[] UV = { u0, v1, u1, v1, u1, v0, u0, v0 };
        for (int i = 0; i < 4; i++) {
            tess.addVertexWithUV(
                C[i][0] + N[0] * offset,
                C[i][1] + N[1] * offset,
                C[i][2] + N[2] * offset,
                UV[i * 2],
                UV[i * 2 + 1]);
        }
    }

    /**
     * {@code getAtlasSprite} falls back to the atlas' missing-image sprite instead of returning null — the
     * fallback is the unregistered-icon case (a face that would otherwise be drawn checkerboard).
     */
    private static boolean isMissingSprite(IIcon icon) {
        return icon instanceof TextureAtlasSprite && ((TextureAtlasSprite) icon).getIconName()
            .equals("missingno");
    }

    private static void warnMissingIcon(String name) {
        if (WARNED_MISSING_ICONS.add(name)) {
            TecTech.LOGGER
                .warn("Voidcraft ship model: block icon missing from the atlas, faces will be blank: {}", name);
        }
    }

    private static int cellIndex(int w, int h, int x, int y, int z) {
        return x + w * (y + h * z);
    }

    /**
     * Build an empty VAO (no vertices) so callers never see null.
     */
    private static IVertexArrayObject emptyVao() {
        Tessellator tessellator = TessellatorManager.startCapturingDirect(captureFormat());
        return TessellatorManager.stopCapturingDirectToVBO(VertexBufferType.IMMUTABLE);
    }
}
