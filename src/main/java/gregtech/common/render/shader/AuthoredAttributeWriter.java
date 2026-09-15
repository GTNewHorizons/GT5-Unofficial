package gregtech.common.render.shader;

import net.minecraft.client.renderer.Tessellator;

import com.gtnewhorizon.gtnhlib.client.renderer.vertex.writers.IVertexAttributeWriter;

final class AuthoredAttributeWriter implements IVertexAttributeWriter {

    @Override
    public int writeAttribute(long pointer, int[] data, int index) {
        throw new UnsupportedOperationException("authored-only attribute; build the mesh with MeshBuilder");
    }

    @Override
    public int writeAttribute(long pointer, Tessellator tessellator) {
        throw new UnsupportedOperationException("authored-only attribute; build the mesh with MeshBuilder");
    }

    @Override
    public int readAttribute(long pointer, Tessellator tessellator) {
        throw new UnsupportedOperationException("authored-only attribute; build the mesh with MeshBuilder");
    }
}
