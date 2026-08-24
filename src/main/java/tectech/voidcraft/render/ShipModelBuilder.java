package tectech.voidcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;

import com.gtnewhorizon.gtnhlib.client.renderer.TessellatorManager;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.VertexBufferType;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.DefaultVertexFormat;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import tectech.voidcraft.ship.VoidcraftBlueprint;

/**
 * Builds the ship hologram VBO from a {@link VoidcraftBlueprint} (client only).
 *
 * <p>
 * Technique (adapted from the Amazing Trophies "complex model" renderer, which renders trophy structures out
 * of real blocks): every occupied blueprint cell is rendered with the vanilla
 * {@link net.minecraft.client.renderer.RenderBlocks#renderBlockAsItem} pipeline — real block textures, real
 * per-face UVs, ambient occlusion disabled — into a single captured
 * {@link IVertexArrayObject} (gtnhlib {@code TessellatorManager} direct-capture), one draw call at render time.
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

        Tessellator tessellator = TessellatorManager.startCapturingDirect(DefaultVertexFormat.POSITION_TEXTURE_NORMAL);
        try {
            Block machineBlock = GregTechAPI.sBlockMachines;
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    for (int z = 0; z < d; z++) {
                        if (!occupied[x][y][z] || faces[x][y][z].allHidden()) {
                            continue;
                        }
                        int componentMeta = blueprint.grid[cellIndex(w, h, x, y, z)] - 1;
                        renderBlocks.setRenderFacesInfo(faces[x][y][z]);
                        tessellator.setTranslation(x, y, z);
                        renderBlocks.renderBlockAsItem(machineBlock, COMPONENT_MTE_BASE_ID + componentMeta, 1.0f);
                    }
                }
            }
            tessellator.setTranslation(0, 0, 0);
        } finally {
            IVertexArrayObject vao = TessellatorManager.stopCapturingDirectToVBO(VertexBufferType.IMMUTABLE);
            return new ShipModel(vao, w, h, d);
        }
    }

    private static int cellIndex(int w, int h, int x, int y, int z) {
        return x + w * (y + h * z);
    }

    /**
     * Build an empty VAO (no vertices) so callers never see null.
     */
    private static IVertexArrayObject emptyVao() {
        Tessellator tessellator = TessellatorManager.startCapturingDirect(DefaultVertexFormat.POSITION_TEXTURE_NORMAL);
        return TessellatorManager.stopCapturingDirectToVBO(VertexBufferType.IMMUTABLE);
    }
}
