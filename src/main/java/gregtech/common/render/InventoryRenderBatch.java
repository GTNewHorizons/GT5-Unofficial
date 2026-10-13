package gregtech.common.render;

import net.minecraft.client.renderer.Tessellator;

import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

final class InventoryRenderBatch {

    private Tessellator tessellator;
    private boolean hasBrightness;

    boolean begin(Tessellator tessellator) {
        if (this.tessellator != null || ((TesselatorAccessor) tessellator).gt5u$isDrawing()) return false;
        tessellator.startDrawingQuads();
        this.tessellator = tessellator;
        hasBrightness = false;
        return true;
    }

    void prepare(boolean hasBrightness) {
        if (tessellator == null || this.hasBrightness == hasBrightness) return;

        GTTextureBase.drawInventoryBatch(tessellator);
        tessellator.startDrawingQuads();
        this.hasBrightness = hasBrightness;
    }

    void end() {
        final Tessellator tessellator = this.tessellator;
        this.tessellator = null;
        if (tessellator != null && ((TesselatorAccessor) tessellator).gt5u$isDrawing()) {
            GTTextureBase.drawInventoryBatch(tessellator);
        }
    }
}
