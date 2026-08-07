package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.model.wavefront.WavefrontVBOBuilder;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.SharedShaders;

@SideOnly(Side.CLIENT)
public class DroneRender {

    private static final ResourceLocation[] DroneTextures = new ResourceLocation[] { createRl("drone1.png"),
        createRl("drone2.png"), createRl("drone3.png"), createRl("drone4.png") };

    private static IVertexArrayObject drone;
    private static IVertexArrayObject droneBlade;

    private static final Matrix4fStack modelMatrix = new Matrix4fStack(3);

    private static ResourceLocation createRl(String name) {
        return new ResourceLocation(GregTech.ID, "textures/model/drone/" + name);
    }

    public static void reload() {
        release();
        if (!SharedShaders.ready()) return;

        try {
            drone = WavefrontVBOBuilder.compileToVBO(
                createRl("drone.obj"),
                SharedShaders.textured()
                    .vertexFormat());
            droneBlade = WavefrontVBOBuilder.compileToVBO(
                createRl("drone_blade.obj"),
                SharedShaders.textured()
                    .vertexFormat());
        } catch (RuntimeException e) {
            GTMod.GT_FML_LOGGER.error("Failed to load drone model", e);
            release();
        }
    }

    private static void release() {
        if (drone != null) {
            drone.delete();
            drone = null;
        }
        if (droneBlade != null) {
            droneBlade.delete();
            droneBlade = null;
        }
    }

    public static void renderDrone(double x, double y, double z, float timeSinceLastTick, int level) {
        if (level <= 0 || level > DroneTextures.length) return;
        if (drone == null || !SharedShaders.ready()) return;

        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager()
            .bindTexture(DroneTextures[level - 1]);

        final ShaderHandle shader = SharedShaders.textured();
        shader.use();
        GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1f, 1f, 1f, 1f);

        double time = mc.theWorld.getTotalWorldTime() + (double) timeSinceLastTick;

        modelMatrix.clear();
        modelMatrix.translate((float) x + 0.5f, (float) (y + 0.5 + Math.sin(time * 0.1) * 0.15), (float) z + 0.5f);

        shader.uploadModel(modelMatrix);
        drone.render();

        final double bladeOffset = 0.845;
        double rotation = time * 80.0 % 360.0;
        renderBlade(shader, rotation, -bladeOffset, -bladeOffset);
        renderBlade(shader, -rotation, -bladeOffset, bladeOffset);
        renderBlade(shader, -rotation, bladeOffset, -bladeOffset);
        renderBlade(shader, rotation, bladeOffset, bladeOffset);

        ShaderProgram.clear();
    }

    private static void renderBlade(ShaderHandle shader, double rotation, double offsetX, double offsetZ) {
        modelMatrix.pushMatrix();
        modelMatrix.translate((float) offsetX, 1.265f, (float) offsetZ);
        modelMatrix.rotate((float) Math.toRadians(rotation), 0, 1, 0);

        shader.uploadModel(modelMatrix);
        droneBlade.render();

        modelMatrix.popMatrix();
    }
}
