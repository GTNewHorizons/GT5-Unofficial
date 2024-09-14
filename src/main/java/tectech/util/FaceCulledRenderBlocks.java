package tectech.util;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class FaceCulledRenderBlocks extends RenderBlocks {

    FaceVisibility faceVisibility;

    public FaceCulledRenderBlocks(IBlockAccess world) {
        super(world);
    }

    public void setFaceVisibility(FaceVisibility faceVisibility) {
        this.faceVisibility = faceVisibility;
    }

    @Override
    public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
        if (this.faceVisibility.bottom) {
            super.renderFaceYNeg(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
        if (this.faceVisibility.top) {
            super.renderFaceYPos(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
        if (this.faceVisibility.back) {
            super.renderFaceZNeg(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
        if (this.faceVisibility.front) {
            super.renderFaceZPos(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
        if (this.faceVisibility.left) {
            super.renderFaceXNeg(block, 0, 0, 0, icon);
        }
    }

    @Override
    public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
        if (this.faceVisibility.right) {
            super.renderFaceXPos(block, 0, 0, 0, icon);
        }
    }

}
