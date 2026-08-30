package gregtech.common.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import gregtech.api.interfaces.ITexture;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

public abstract class GTTextureBase implements ITexture {

    protected final boolean beginDrawingQuads(RenderBlocks aRenderer, float aNormalX, float aNormalY, float aNormalZ) {
        final Tessellator tess = Tessellator.instance;
        if (aRenderer.useInventoryTint) {
            final boolean startedDrawing = !((TesselatorAccessor) tess).gt5u$isDrawing();
            if (startedDrawing) tess.startDrawingQuads();
            tess.setNormal(aNormalX, aNormalY, aNormalZ);
            return startedDrawing;
        }
        return false;
    }

    protected final void endDrawingQuads(RenderBlocks aRenderer, boolean startedDrawing) {
        if (aRenderer.useInventoryTint && startedDrawing) {
            Tessellator.instance.draw();
        }
    }
}
