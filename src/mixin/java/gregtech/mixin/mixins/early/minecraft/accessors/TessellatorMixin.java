package gregtech.mixin.mixins.early.minecraft.accessors;

import net.minecraft.client.renderer.Tessellator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import gregtech.mixin.interfaces.accessors.TesselatorAccessor;

@Mixin(Tessellator.class)
public class TessellatorMixin implements TesselatorAccessor {

    @Shadow
    private boolean isDrawing;

    @Override
    public boolean gt5u$isDrawing() {
        return this.isDrawing;
    }

    @Shadow
    private int vertexCount;

    @Override
    public boolean gt5u$hasVertices() {
        return this.vertexCount > 0;
    }
}
