package gregtech.api.CustomStructureRendering.Base.Util;


import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class CustomRenderBlocks extends RenderBlocks {

    // I made this, so we can turn on and off what faces should be rendered. Usually we would check if a block exists in
    // an adjacent block but since these are all holograms they don't exist.

    public RenderFacesInfo renderFacesInfo;

    public CustomRenderBlocks(World world) {
        super(world);
    }

    @Override
    public void renderFaceYNeg(Block p_147768_1_, double p_147768_2_, double p_147768_4_, double p_147768_6_, IIcon p_147768_8_) {
        if (this.renderFacesInfo.yNeg) {
            super.renderFaceYNeg(p_147768_1_, 0, 0, 0, p_147768_8_);
        }
    }

    @Override
    public void renderFaceYPos(Block p_147806_1_, double p_147806_2_, double p_147806_4_, double p_147806_6_, IIcon p_147806_8_) {
        if (this.renderFacesInfo.yPos) {
            super.renderFaceYPos(p_147806_1_, 0, 0, 0, p_147806_8_);
        }
    }

    @Override
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon p_147761_8_) {
        if (this.renderFacesInfo.zNeg) {
            super.renderFaceZNeg(block, 0, 0, 0, p_147761_8_);
        }
    }

    @Override
    public void renderFaceZPos(Block p_147734_1_, double p_147734_2_, double p_147734_4_, double p_147734_6_, IIcon p_147734_8_) {
        if (this.renderFacesInfo.zPos) {
            super.renderFaceZPos(p_147734_1_, 0, 0, 0, p_147734_8_);
        }
    }

    @Override
    public void renderFaceXNeg(Block p_147798_1_, double p_147798_2_, double p_147798_4_, double p_147798_6_, IIcon p_147798_8_) {
        if (this.renderFacesInfo.xNeg) {
            super.renderFaceXNeg(p_147798_1_, 0, 0, 0, p_147798_8_);
        }
    }

    @Override
    public void renderFaceXPos(Block p_147764_1_, double p_147764_2_, double p_147764_4_, double p_147764_6_, IIcon p_147764_8_) {
        if (this.renderFacesInfo.xPos) {
            super.renderFaceXPos(p_147764_1_, 0, 0, 0, p_147764_8_);
        }
    }
}
