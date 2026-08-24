package tectech.voidcraft.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Per-cell face visibility for the ship hologram (adapted from the Amazing Trophies complex-model renderer,
 * which renders trophy structures out of real blocks with face culling).
 *
 * <p>
 * A face is hidden when the neighbor cell in that direction is a solid component block (hologram neighbors
 * are not in the world, so {@link net.minecraft.client.renderer.RenderBlocks}' neighbor checks cannot be used —
 * the culling is precomputed from the blueprint grid instead).
 */
@SideOnly(Side.CLIENT)
public final class ShipRenderFacesInfo {

    private boolean xNeg = true;
    private boolean xPositive = true;
    private boolean yNeg = true;
    private boolean yPositive = true;
    private boolean zNeg = true;
    private boolean zPositive = true;

    public ShipRenderFacesInfo(boolean allVisible) {
        this.xNeg = allVisible;
        this.xPositive = allVisible;
        this.yNeg = allVisible;
        this.yPositive = allVisible;
        this.zNeg = allVisible;
        this.zPositive = allVisible;
    }

    public boolean isXNeg() {
        return xNeg;
    }

    public void setXNeg(boolean v) {
        this.xNeg = v;
    }

    public boolean isXPos() {
        return xPositive;
    }

    public void setXPos(boolean v) {
        this.xPositive = v;
    }

    public boolean isYNeg() {
        return yNeg;
    }

    public void setYNeg(boolean v) {
        this.yNeg = v;
    }

    public boolean isYPos() {
        return yPositive;
    }

    public void setYPos(boolean v) {
        this.yPositive = v;
    }

    public boolean isZNeg() {
        return zNeg;
    }

    public void setZNeg(boolean v) {
        this.zNeg = v;
    }

    public boolean isZPos() {
        return zPositive;
    }

    public void setZPos(boolean v) {
        this.zPositive = v;
    }

    /**
     * @return true when every face is hidden (the cell is fully enclosed — skip it entirely)
     */
    public boolean allHidden() {
        return !xNeg && !xPositive && !yNeg && !yPositive && !zNeg && !zPositive;
    }
}
