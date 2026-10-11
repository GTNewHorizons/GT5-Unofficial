package gregtech.common.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import gregtech.api.interfaces.ITexture;
import gregtech.api.render.ISBRContext;
import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

public abstract class GTTextureBase implements ITexture {

    protected final boolean beginDrawingQuads(ISBRContext ctx, float aNormalX, float aNormalY, float aNormalZ) {
        final RenderBlocks renderer = ctx.getRenderBlocks();
        if (renderer.useInventoryTint && ctx instanceof SBRInventoryContext inventoryContext) {
            inventoryContext.prepareInventoryBatch();
        }
        return beginDrawingQuads(renderer, aNormalX, aNormalY, aNormalZ);
    }

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
            drawInventoryBatch(Tessellator.instance);
        }
    }

    static void drawInventoryBatch(Tessellator tessellator) {
        GL11.glPushAttrib(GL11.GL_CURRENT_BIT);
        try {
            tessellator.draw();
        } finally {
            GL11.glPopAttrib();
        }
    }
}
