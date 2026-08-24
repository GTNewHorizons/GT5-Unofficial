package tectech.voidcraft.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * {@link RenderBlocks} subclass that renders only the faces marked visible in the per-cell
 * {@link ShipRenderFacesInfo} (adapted from the Amazing Trophies complex-model renderer).
 *
 * <p>
 * The ship hologram is rendered out of real component blocks (the actual digitized ship), but the blocks do
 * not exist in the world — so the usual "neighbor is opaque, skip the face" checks cannot work. Instead the
 * visibility is precomputed from the blueprint grid ({@link ShipModelBuilder}) and fed in per cell.
 */
@SideOnly(Side.CLIENT)
public class ShipRenderBlocks extends RenderBlocks {

    private ShipRenderFacesInfo renderFacesInfo;

    public ShipRenderBlocks(IBlockAccess world) {
        super(world);
    }

    public void setRenderFacesInfo(ShipRenderFacesInfo renderFacesInfo) {
        this.renderFacesInfo = renderFacesInfo;
    }

    @Override
    public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
        if (renderFacesInfo != null && renderFacesInfo.isYNeg()) {
            super.renderFaceYNeg(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
        if (renderFacesInfo != null && renderFacesInfo.isYPos()) {
            super.renderFaceYPos(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
        if (renderFacesInfo != null && renderFacesInfo.isZNeg()) {
            super.renderFaceZNeg(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
        if (renderFacesInfo != null && renderFacesInfo.isZPos()) {
            super.renderFaceZPos(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
        if (renderFacesInfo != null && renderFacesInfo.isXNeg()) {
            super.renderFaceXNeg(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
        if (renderFacesInfo != null && renderFacesInfo.isXPos()) {
            super.renderFaceXPos(block, 0, 0, 0, icon);
        }
    }
}
