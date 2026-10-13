package gregtech.common.render.shader;

import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFlags;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormatElement.Type;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.writers.ColorVertexAttributeWriter;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.writers.IVertexAttributeWriter;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.writers.NormalVertexAttributeWriter;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.writers.PositionVertexAttributeWriter;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.writers.TextureVertexAttributeWriter;

public enum VertexAttribute {

    POSITION(Type.FLOAT, 3, VertexFlags.POSITION_BIT, new PositionVertexAttributeWriter(), 0),
    UV(Type.FLOAT, 2, VertexFlags.TEXTURE_BIT, new TextureVertexAttributeWriter(), 0),
    COLOR(Type.UBYTE, 4, VertexFlags.COLOR_BIT, new ColorVertexAttributeWriter(), 0),
    NORMAL(Type.BYTE, 3, VertexFlags.NORMAL_BIT, new NormalVertexAttributeWriter(), 1),
    // Hand-rolled gl_InstanceID for GL 2.1. Handled by MeshBuilder.instanceIndex, not sourced from Tessellator.
    INSTANCE_INDEX(Type.FLOAT, 1, VertexFlags.POSITION_BIT, new AuthoredAttributeWriter(), 0);

    final Type type;
    final int count;
    final int vertexBit;
    final IVertexAttributeWriter writer;
    final int padding;

    VertexAttribute(Type type, int count, int vertexBit, IVertexAttributeWriter writer, int padding) {
        this.type = type;
        this.count = count;
        this.vertexBit = vertexBit;
        this.writer = writer;
        this.padding = padding;
    }

    int byteSize() {
        return type.getSize() * count + padding;
    }
}
