package gregtech.common.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import gregtech.api.interfaces.ITexture;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

public abstract class GTTextureBase implements ITexture {

    protected boolean isDrawing = false;

    @Override
    public void startDrawingQuads(RenderBlocks aRenderer, float aNormalX, float aNormalY, float aNormalZ) {
        final Tessellator tess = Tessellator.instance;
        if (aRenderer.useInventoryTint && !((TesselatorAccessor) tess).gt5u$isDrawing()) {
            isDrawing = true;
            tess.startDrawingQuads();
            tess.setNormal(aNormalX, aNormalY, aNormalZ);
        }
    }

    @Override
    public void draw(RenderBlocks aRenderer) {
        if (aRenderer.useInventoryTint && isDrawing) {
            isDrawing = false;
            Tessellator.instance.draw();
        }
    }
}
