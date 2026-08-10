package gregtech.common.render;

import static tectech.rendering.EOH.EOHRenderingUtils.addRenderedBlockInWorld;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;

import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import gregtech.common.render.shader.MeshBuilder;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.SharedShaders;
import gregtech.common.tileentities.render.RenderingTileEntityWormhole;
import tectech.rendering.EOH.EOHRenderingUtils;

public class WormholeRenderer extends TileEntitySpecialRenderer {

    private static final double trimPercentage = .95;
    private static final double corePercentage = trimPercentage / Math.sqrt(3);

    private static final int VERTICES_PER_BLOCK = 36;

    private static final Vector3f SHELL_AXIS = new Vector3f(2, 1, 0).normalize();
    private static final Vector3f CORE_AXIS = new Vector3f(0, -2, .1f).normalize();

    private record Shell(Block block, float tint, float relScale) {}

    private static final Shell[] SHELLS = { new Shell(Blocks.quartz_block, 1f, 1f),
        new Shell(Blocks.coal_block, 0.1f, (float) trimPercentage) };

    private static final int MAX_CACHED_CORES = 16;

    private static final Map<Block, IVertexArrayObject> CUBES = new LinkedHashMap<>();
    private static final Matrix4fStack modelMatrix = new Matrix4fStack(3);

    public static void reload() {
        for (IVertexArrayObject cube : CUBES.values()) {
            cube.delete();
        }
        CUBES.clear();

        if (!SharedShaders.ready()) return;
        for (Shell shell : SHELLS) {
            CUBES.put(shell.block(), buildCube(shell.block()));
        }
    }

    private static IVertexArrayObject cube(Block block) {
        final IVertexArrayObject cached = CUBES.get(block);
        if (cached != null) return cached;

        if (CUBES.size() >= MAX_CACHED_CORES + SHELLS.length) {
            final Iterator<Map.Entry<Block, IVertexArrayObject>> oldest = CUBES.entrySet()
                .iterator();
            for (int i = 0; i < SHELLS.length; i++) {
                oldest.next();
            }
            oldest.next()
                .getValue()
                .delete();
            oldest.remove();
        }

        final IVertexArrayObject built = buildCube(block);
        CUBES.put(block, built);
        return built;
    }

    private static IVertexArrayObject buildCube(Block block) {
        try (MeshBuilder mesh = MeshBuilder.of(SharedShaders.textured(), VERTICES_PER_BLOCK)) {
            addRenderedBlockInWorld(mesh, block, 0, EOHRenderingUtils.IDENTITY);
            return mesh.build();
        }
    }

    private static void renderShell(ShaderHandle shader, Block block, float tint) {
        GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), tint, tint, tint, 1f);
        shader.uploadModel(modelMatrix);
        cube(block).render();
    }

    private static void render(ShaderHandle shader, Block coreBlock, double rotation) {
        modelMatrix.pushMatrix();
        modelMatrix.rotate((float) Math.toRadians(rotation), SHELL_AXIS.x, SHELL_AXIS.y, SHELL_AXIS.z);
        modelMatrix.scale(-1, -1, -1);

        for (Shell shell : SHELLS) {
            modelMatrix.scale(shell.relScale());
            renderShell(shader, shell.block(), shell.tint());
        }

        modelMatrix.popMatrix();

        if (coreBlock != null) {
            modelMatrix.pushMatrix();
            modelMatrix.scale((float) corePercentage);
            modelMatrix.rotate((float) Math.toRadians(rotation), CORE_AXIS.x, CORE_AXIS.y, CORE_AXIS.z);

            renderShell(shader, coreBlock, 1f);

            modelMatrix.popMatrix();
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        if (tile instanceof RenderingTileEntityWormhole wTile) {
            if (!SharedShaders.ready()) return;

            final double radius = wTile.targetRadius;
            if (radius <= 0) return;

            double rotationTimer = wTile.getWorldObj()
                .getWorldInfo()
                .getWorldTotalTime() + timeSinceLastTick;

            this.bindTexture(TextureMap.locationBlocksTexture);

            final ShaderHandle shader = SharedShaders.textured();
            shader.use();

            modelMatrix.clear();
            modelMatrix.translate((float) x + 0.5f, (float) y + 0.5f, (float) z + 0.5f);
            modelMatrix.scale((float) radius);

            render(shader, wTile.getBlock(), rotationTimer);

            ShaderProgram.clear();
        }
    }
}
