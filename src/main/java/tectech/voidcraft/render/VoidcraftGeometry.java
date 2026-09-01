package tectech.voidcraft.render;

import static com.gtnewhorizon.gtnhlib.bytebuf.MemoryUtilities.memAlloc;
import static com.gtnewhorizon.gtnhlib.bytebuf.MemoryUtilities.memFree;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.VertexBufferType;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;

import gregtech.common.render.shader.MeshBuilder;
import gregtech.common.render.shader.QuadSink;

/**
 * The static unit geometry shared by the voidcraft effect shaders. Everything is unit-sized (corners at ±1, or
 * radius 1) so a single VAO serves every draw: the per-instance position / size / orientation is the model
 * matrix the draw uploads.
 *
 * <ul>
 * <li>{@link #unitCube()} — the six faces of the unit cube as triangles: the Explorer scan cube, the Voidbase
 * site fill box, the gateway event plane and tube are all unit geometry scaled by a model matrix.</li>
 * <li>{@link #unitCubeLines()} — the unit cube's 12 edges as GL_LINES: the Voidbase site wireframes and the
 * assembler wireframes.</li>
 * <li>{@link #unitQuad()} — one quad (z = 0, x/y ∈ [-1, 1]): the assembler scan planes.</li>
 * <li>{@link #rippleTriangle()} — the unit equilateral triangle (circumradius 1, apex up, z = 0): the Explorer
 * ripple billboards.</li>
 * <li>{@link #beamRod()} — the laser rod cross-section (four side quads, t ∈ [0, 1] along the axis): every
 * beam; endpoints / axes / half-width come from the beam shader's uniforms.</li>
 * <li>{@link #unitTube()} — the open unit cylinder along +Z (z = 0 to 1, radius 1): the thruster trail's
 * per-section mesh; the per-section radius, length and orientation come from the draw's model matrix.</li>
 *
 * <p>
 * Each VAO is built lazily from the vertex format of its owning shader and is deleted by {@link #release()}
 * (resource reload) — the attribute locations may move on a re-bake.
 */
public final class VoidcraftGeometry {

    private static IVertexArrayObject unitCube;
    private static IVertexArrayObject unitCubeLines;
    private static IVertexArrayObject unitQuad;
    private static IVertexArrayObject rippleTriangle;
    private static IVertexArrayObject beamRod;
    private static IVertexArrayObject unitTube;

    private VoidcraftGeometry() {}

    /** The unit cube (corners at ±1 on every axis), six faces as triangles. */
    public static IVertexArrayObject unitCube() {
        if (unitCube == null) {
            final MeshBuilder mesh = MeshBuilder.of(VoidcraftShaders.color(), 6 * 6);
            cubeFaces(mesh);
            unitCube = mesh.build();
        }
        return unitCube;
    }

    /** The unit cube's 12 edges as GL_LINES. */
    public static IVertexArrayObject unitCubeLines() {
        if (unitCubeLines == null) {
            unitCubeLines = lineMesh(
                VoidcraftShaders.color()
                    .vertexFormat(),
                cubeEdges());
        }
        return unitCubeLines;
    }

    /** One unit quad (x/y ∈ [-1, 1] at z = 0). */
    public static IVertexArrayObject unitQuad() {
        if (unitQuad == null) {
            final MeshBuilder mesh = MeshBuilder.of(VoidcraftShaders.color(), 6);
            mesh.vertex(-1, -1, 0, 0, 0);
            mesh.vertex(1, -1, 0, 0, 0);
            mesh.vertex(1, 1, 0, 0, 0);
            mesh.vertex(-1, 1, 0, 0, 0);
            unitQuad = mesh.build();
        }
        return unitQuad;
    }

    /** The unit equilateral triangle (circumradius 1, apex up) at z = 0. */
    public static IVertexArrayObject rippleTriangle() {
        if (rippleTriangle == null) {
            final MeshBuilder mesh = MeshBuilder.of(VoidcraftShaders.ripple(), 3);
            mesh.triangleVertex(0.0, 1.0, 0.0, 0, 0);
            mesh.triangleVertex(-0.866, -0.5, 0.0, 0, 0);
            mesh.triangleVertex(0.866, -0.5, 0.0, 0, 0);
            rippleTriangle = mesh.build();
        }
        return rippleTriangle;
    }

    /**
     * The laser rod: four side quads spanning t ∈ [0, 1] (the start → end parameter) and the (±1, ±1)
     * cross-section corners.
     */
    public static IVertexArrayObject beamRod() {
        if (beamRod == null) {
            final MeshBuilder mesh = MeshBuilder.of(VoidcraftShaders.beam(), 4 * 6);
            final double[] sx = { -1, 1, 1, -1 };
            final double[] sz = { -1, -1, 1, 1 };
            for (int i = 0; i < 4; i++) {
                final int j = (i + 1) % 4;
                mesh.vertex(0, sx[i], sz[i], 0, 0);
                mesh.vertex(1, sx[i], sz[i], 0, 0);
                mesh.vertex(1, sx[j], sz[j], 0, 0);
                mesh.vertex(0, sx[j], sz[j], 0, 0);
            }
            beamRod = mesh.build();
        }
        return beamRod;
    }

    /** Radial segments of the unit tube. */
    private static final int TUBE_SEGMENTS = 16;

    /**
     * The unit tube: an OPEN cylinder along +Z (z = 0 to 1, radius 1) — the thruster trail's per-section mesh;
     * the per-section radius, length and orientation are the model matrix's scale / rotation.
     */
    public static IVertexArrayObject unitTube() {
        if (unitTube == null) {
            final MeshBuilder mesh = MeshBuilder.of(VoidcraftShaders.color(), TUBE_SEGMENTS * 6);
            final double twoPi = 2.0 * Math.PI;
            for (int i = 0; i < TUBE_SEGMENTS; i++) {
                final double a0 = twoPi * i / TUBE_SEGMENTS;
                final double a1 = twoPi * (i + 1) / TUBE_SEGMENTS;
                mesh.vertex(Math.cos(a0), Math.sin(a0), 0.0, 0.0, 0.0);
                mesh.vertex(Math.cos(a0), Math.sin(a0), 1.0, 0.0, 0.0);
                mesh.vertex(Math.cos(a1), Math.sin(a1), 1.0, 0.0, 0.0);
                mesh.vertex(Math.cos(a1), Math.sin(a1), 0.0, 0.0, 0.0);
            }
            unitTube = mesh.build();
        }
        return unitTube;
    }

    /** Deletes every cached VAO (attribute locations may move on a shader re-bake). */
    public static void release() {
        delete(unitCube);
        unitCube = null;
        delete(unitCubeLines);
        unitCubeLines = null;
        delete(unitQuad);
        unitQuad = null;
        delete(rippleTriangle);
        rippleTriangle = null;
        delete(beamRod);
        beamRod = null;
        delete(unitTube);
        unitTube = null;
    }

    private static void delete(IVertexArrayObject vao) {
        if (vao != null) {
            vao.delete();
        }
    }

    /** Emits the six faces of the unit cube (±1 corners) as quads. */
    private static void cubeFaces(QuadSink sink) {
        sink.vertex(-1, -1, -1, 0, 0);
        sink.vertex(1, -1, -1, 0, 0);
        sink.vertex(1, 1, -1, 0, 0);
        sink.vertex(-1, 1, -1, 0, 0);
        // z = +1
        sink.vertex(-1, -1, 1, 0, 0);
        sink.vertex(-1, 1, 1, 0, 0);
        sink.vertex(1, 1, 1, 0, 0);
        sink.vertex(1, -1, 1, 0, 0);
        // x = -1
        sink.vertex(-1, -1, -1, 0, 0);
        sink.vertex(-1, 1, -1, 0, 0);
        sink.vertex(-1, 1, 1, 0, 0);
        sink.vertex(-1, -1, 1, 0, 0);
        // x = +1
        sink.vertex(1, -1, -1, 0, 0);
        sink.vertex(1, -1, 1, 0, 0);
        sink.vertex(1, 1, 1, 0, 0);
        sink.vertex(1, 1, -1, 0, 0);
        // y = -1
        sink.vertex(-1, -1, -1, 0, 0);
        sink.vertex(-1, -1, 1, 0, 0);
        sink.vertex(1, -1, 1, 0, 0);
        sink.vertex(1, -1, -1, 0, 0);
        // y = +1
        sink.vertex(-1, 1, -1, 0, 0);
        sink.vertex(1, 1, -1, 0, 0);
        sink.vertex(1, 1, 1, 0, 0);
        sink.vertex(-1, 1, 1, 0, 0);
    }

    /** The unit cube's 12 edges as (x, y, z) × 24 floats. */
    private static float[] cubeEdges() {
        // corner c0 = (-1,-1,-1) ... c7 = (-1,1,1)
        final float[] corners = { -1, -1, -1, 1, -1, -1, 1, 1, -1, -1, 1, -1, -1, -1, 1, 1, -1, 1, 1, 1, 1, -1, 1, 1 };
        final int[] edges = { 0, 1, 1, 2, 2, 3, 3, 0, 4, 5, 5, 6, 6, 7, 7, 4, 0, 4, 1, 5, 2, 6, 3, 7 };
        final float[] xyz = new float[edges.length * 3];
        for (int i = 0; i < edges.length; i++) {
            final int base = i * 3;
            final int c = edges[i] * 3;
            xyz[base] = corners[c];
            xyz[base + 1] = corners[c + 1];
            xyz[base + 2] = corners[c + 2];
        }
        return xyz;
    }

    /** A GL_LINES VAO from flat (x, y, z) data, in the given position-only format. */
    private static IVertexArrayObject lineMesh(VertexFormat format, float[] xyz) {
        final ByteBuffer buffer = memAlloc(xyz.length * 4);
        try {
            for (int i = 0; i < xyz.length; i++) {
                buffer.putFloat(xyz[i]);
            }
            buffer.position(0)
                .limit(xyz.length * 4);
            return VertexBufferType.IMMUTABLE.allocate(format, GL11.GL_LINES, buffer, xyz.length / 3);
        } finally {
            memFree(buffer);
        }
    }
}
