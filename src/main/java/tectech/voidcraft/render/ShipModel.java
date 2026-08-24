package tectech.voidcraft.render;

import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * A built ship hologram: one immutable VBO holding every visible face of every component block (single draw
 * call), plus the model dimensions for centering.
 *
 * <p>
 * Built once per distinct blueprint (see {@link VoidcraftShipModelCache}) and cached on the client.
 */
@SideOnly(Side.CLIENT)
public final class ShipModel {

    /** The captured geometry (position/texture/normal). */
    public final IVertexArrayObject vao;

    /** Blueprint width (X). */
    public final int width;

    /** Blueprint height (Y). */
    public final int height;

    /** Blueprint depth (Z). */
    public final int depth;

    ShipModel(IVertexArrayObject vao, int width, int height, int depth) {
        this.vao = vao;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    /**
     * @return the longest axis in blocks (used for scaling)
     */
    public int maxAxis() {
        return Math.max(width, Math.max(height, depth));
    }
}
