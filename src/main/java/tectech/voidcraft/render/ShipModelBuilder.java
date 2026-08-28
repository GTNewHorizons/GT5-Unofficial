package tectech.voidcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;

import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.VertexBufferType;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.common.render.shader.SharedShaders;
import tectech.voidcraft.VoidcraftTextures;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftCoverComponent;

/**
 * Builds the ship hologram VAO from a {@link VoidcraftBlueprint} (client only).
 *
 * <p>
 * Every occupied blueprint cell is rendered with the vanilla
 * {@link net.minecraft.client.renderer.RenderBlocks#renderBlockAsItem} pipeline — real block textures, real
 * per-face UVs, ambient occlusion disabled — into a single captured {@link IVertexArrayObject} in the shared
 * textured shader's vertex format (position + UV), one draw call at render time. Covers mounted on a visible
 * face are added as thin textured quads just outside that face (the cover's own icon from the block atlas).
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

    /**
     * Component MTE ids start here (MetaTileEntityIDs 32058–32066, one per {@link VoidcraftComponent} in meta
     * order). All component blocks live on the single GT machine block.
     */
    public static final int COMPONENT_MTE_BASE_ID = 32058;

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

        ShipRenderBlocks renderBlocks = new ShipRenderBlocks(mc.theWorld);
        renderBlocks.enableAO = false;

        Tessellator tessellator = TessellatorManager.startCapturingDirect(captureFormat());
        try {
            // Pass 1: the hull blocks (real machine textures), in the renderer's default GL state.
            Block machineBlock = GregTechAPI.sBlockMachines;
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    for (int z = 0; z < d; z++) {
                        if (!occupied[x][y][z] || faces[x][y][z].allHidden()) {
                            continue;
                        }
                        int cell = cellIndex(w, h, x, y, z);
                        int componentMeta = blueprint.grid[cell] - 1;
                        renderBlocks.setRenderFacesInfo(faces[x][y][z]);
                        tessellator.setTranslation(x, y, z);
                        renderBlocks.renderBlockAsItem(machineBlock, COMPONENT_MTE_BASE_ID + componentMeta, 1.0f);
                    }
                }
            }

            // Pass 2: the covers — one textured quad per mounted cover on a visible face. The quads are wound
            // CCW-from-outside (matching the block faces) and float 0.003 outside the hull, so they render
            // correctly under whatever culling/depth state the ship is drawn with.
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    for (int z = 0; z < d; z++) {
                        if (!occupied[x][y][z]) {
                            continue;
                        }
                        int cell = cellIndex(w, h, x, y, z);
                        tessellator.setTranslation(x, y, z);
                        renderCovers(tessellator, blueprint, cell, faces[x][y][z]);
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
     * the hull face (0.003 offset — no z-fight) and is textured with the cover's own icon, so the in-flight
     * ship shows exactly what the in-world face shows.
     */
    private static void renderCovers(Tessellator tess, VoidcraftBlueprint blueprint, int cell,
        ShipRenderFacesInfo info) {
        boolean[] visible = { info.isYNeg(), info.isYPos(), info.isZNeg(), info.isZPos(), info.isXNeg(),
            info.isXPos() };
        TextureMap textureMap = (TextureMap) Minecraft.getMinecraft()
            .getTextureManager()
            .getTexture(TextureMap.locationBlocksTexture);
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
            IIcon icon = textureMap.getAtlasSprite(VoidcraftTextures.coverIconName(cover));
            if (icon == null) {
                continue; // icon never registered — skip rather than NPE
            }
            renderCoverFace(tess, side, icon);
        }
    }

    /**
     * One cover quad on the unit cube's {@code side} face (the caller has already translated to the cell), UVs
     * from the cover icon, upright when the face is viewed from outside (side faces: world +Y up; top/bottom:
     * arbitrary orientation).
     */
    private static void renderCoverFace(Tessellator tess, int side, IIcon icon) {
        // unit-cube corners of the face, outward, in draw order — (x, y, z)
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
        final float o = 0.003f; // float the quad just outside the hull face
        float u0 = icon.getMinU();
        float u1 = icon.getMaxU();
        float v0 = icon.getMinV(); // icon top
        float v1 = icon.getMaxV(); // icon bottom
        float[] UV = { u0, v1, u1, v1, u1, v0, u0, v0 };
        tess.setNormal(N[0], N[1], N[2]); // all four corners share the flat face normal
        for (int i = 0; i < 4; i++) {
            tess.addVertexWithUV(C[i][0] + N[0] * o, C[i][1] + N[1] * o, C[i][2] + N[2] * o, UV[i * 2], UV[i * 2 + 1]);
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
