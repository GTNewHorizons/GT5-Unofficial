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
        if (aRenderer.useInventoryTint && (!isOldTexture() || !((TesselatorAccessor) tess).gt5u$isDrawing())) {
            // Draw if we're not an old texture OR we are an old texture AND we're not already drawing
            isDrawing = true;
            tess.startDrawingQuads();
            tess.setNormal(aNormalX, aNormalY, aNormalZ);
        }
    }

    @Override
    public void draw(RenderBlocks aRenderer) {
        if (aRenderer.useInventoryTint && (!isOldTexture() || isDrawing)) {
            // Draw if we're not an old texture OR we initiated the drawing
            isDrawing = false;
            Tessellator.instance.draw();
        }
    }
}
