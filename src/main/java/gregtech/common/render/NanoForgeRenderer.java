package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;
import static java.lang.Math.sin;

import java.nio.FloatBuffer;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;

import org.joml.Matrix4fStack;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.vbo.IModelCustomExt;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileEntityNanoForgeRenderer;

public class NanoForgeRenderer extends TileEntitySpecialRenderer {

    private boolean initialized = false;

    private static final Matrix4fStack modelMatrixStack = new Matrix4fStack(4);

    private static IModelCustomExt nanoforgeCoreModel;
    private static ResourceLocation coreTexture;
    private static IModelCustomExt nanoforgeShieldModel;
    private static ResourceLocation shieldTexture;
    private static IModelCustomExt nanoforgeRingOneModel;
    private static IModelCustomExt nanoforgeRingTwoModel;
    private static IModelCustomExt nanoforgeRingThreeModel;
    private static ResourceLocation ringTexture;

    public NanoForgeRenderer() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityNanoForgeRenderer.class, this);
    }

    private void init() {
        nanoforgeCoreModel = (IModelCustomExt) AdvancedModelLoader
        .loadModel(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-core.obj"));
        coreTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/Core.png");
        nanoforgeShieldModel = (IModelCustomExt) AdvancedModelLoader
            .loadModel(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-shield.obj"));
        shieldTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/Shield.png");
        nanoforgeRingOneModel = (IModelCustomExt) AdvancedModelLoader
        .loadModel(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-one.obj"));
        nanoforgeRingTwoModel = (IModelCustomExt) AdvancedModelLoader
        .loadModel(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-two.obj"));
        nanoforgeRingThreeModel = (IModelCustomExt) AdvancedModelLoader
        .loadModel(new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-three.obj"));
        ringTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/RING.png");

        initialized = true;
    }

    private void renderNanoForge(TileEntityNanoForgeRenderer tile, double x, double y, double z) {
        long timer = tile.getTimer();
        modelMatrixStack.clear();
        bindTexture(coreTexture);
        renderCore(x, y, z, timer);
        bindTexture(ringTexture);
        renderRingOne(x, y, z, timer);
        renderRingTwo(x, y, z, timer);
        renderRingThree(x, y, z, timer);
        bindTexture(shieldTexture);
        renderShield(x, y, z, timer);
    }

    private void renderCore(double x, double y, double z, long timer) {
        float chaos = Math.min(timer / 1000.0f, 1);
        chaos = Math.max(chaos, 0.05f);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glScalef(chaos, chaos, chaos);
        GL11.glRotatef(timer,
            (float)(0.3 * sin(timer / 1000.0) + sin(timer / 1000.0 * 0.5) + 0.5 * sin(timer / 1000.0 * 3)),
            (float)(1 * sin(timer / 1000.0 * 0.3) + 3 * sin(timer / 1000.0) + 0.3 * sin(timer / 1000.0 * 3)),
            (float)(2 * sin(timer / 1000.0 * 0.4) + sin(timer / 1000.0 * 1.5) + 1.2 * sin(timer / 1000.0 * 1)));
        nanoforgeCoreModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingOne(double x, double y, double z, long timer) {
        float chaos = Math.min(timer / 1000.0f, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(timer,
            (float)(chaos*(2 * sin(timer / 1000.0) + sin(timer / 1000.0 * 0.5) + 0.5 * sin(timer / 1000.0 * 3))),
            (float)(chaos*(0.2 * sin(timer / 1000.0 * 0.3) + 0.3 * sin(timer / 1000.0) + 0.3 * sin(timer / 1000.0 * 3))),
            (float)(chaos*(0.5 * sin(timer / 1000.0 * 0.4) + 0.2 * sin(timer / 1000.0 * 1.5) + 0.4 * sin(timer / 1000.0 * 1))));
        nanoforgeRingOneModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingTwo(double x, double y, double z, long timer) {
        float chaos = Math.min(timer / 1000.0f, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(timer,
            (float)(chaos*(2 * sin(timer / 1000.0) + sin(timer / 1000.0 * 0.5) + 0.5 * sin(timer / 1000.0 * 3))),
            (float)(chaos*(0.2 * sin(timer / 1000.0 * 0.3) + 0.3 * sin(timer / 1000.0) + 0.3 * sin(timer / 1000.0 * 3))),
            (float)(chaos*(0.5 * sin(timer / 1000.0 * 0.4) + 0.2 * sin(timer / 1000.0 * 1.5) + 0.4 * sin(timer / 1000.0 * 1))));
        nanoforgeRingTwoModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingThree(double x, double y, double z, long timer) {
        float chaos = Math.min(timer / 1000.0f, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(timer,
            (float)(chaos*(2 * sin(timer / 1000.0) + sin(timer / 1000.0 * 0.5) + 0.5 * sin(timer / 1000.0 * 3))),
            (float)(chaos*(0.2 * sin(timer / 1000.0 * 0.3) + 0.3 * sin(timer / 1000.0) + 0.3 * sin(timer / 1000.0 * 3))),
            (float)(chaos*(0.5 * sin(timer / 1000.0 * 0.4) + 0.2 * sin(timer / 1000.0 * 1.5) + 0.4 * sin(timer / 1000.0 * 1))));
        nanoforgeRingThreeModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderShield(double x, double y, double z, long timer) {
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false); // Disable depth writing for transparency
        GL11.glColor4f(1f, 1f, 1f, 0.4f);
        GL11.glRotatef(timer,
            (float)(2 * sin(timer / 1000.0) + sin(timer / 1000.0 * 0.5) + 0.5 * sin(timer / 1000.0 * 3)),
            (float)(1 * sin(timer / 1000.0 * 0.3) + 3 * sin(timer / 1000.0) + 0.3 * sin(timer / 1000.0 * 3)),
            (float)(0.5 * sin(timer / 1000.0 * 0.4) + sin(timer / 1000.0 * 1.5) + 1.2 * sin(timer / 1000.0 * 1)));
        nanoforgeShieldModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileEntityNanoForgeRenderer nanoforge)) return;

        if (!initialized) {
            init();
            if (!initialized) return;
        }

        renderNanoForge(nanoforge, x, y, z);
    }

}
