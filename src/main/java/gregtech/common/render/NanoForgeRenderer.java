package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;
import static java.lang.Math.sin;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.client.renderer.vbo.IModelCustomExt;

import cpw.mods.fml.client.registry.ClientRegistry;
import gregtech.common.tileentities.render.TileEntityNanoForgeRenderer;

public class NanoForgeRenderer extends TileEntitySpecialRenderer {

    private boolean initialized = false;

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
        nanoforgeRingThreeModel = (IModelCustomExt) AdvancedModelLoader.loadModel(
            new ResourceLocation(GregTech.resourceDomain, "textures/model/nano-forge-render-ring-three.obj"));
        ringTexture = new ResourceLocation(GregTech.resourceDomain, "textures/model/RING.png");

        initialized = true;
    }

    private void renderNanoForge(TileEntityNanoForgeRenderer tile, double x, double y, double z, float deltaT) {
        float timer = tile.getTimer();
        if (!tile.getRunning()) {
            timer -= deltaT;
        } else {
            timer += deltaT;
        }

        if (timer > 36_000_000.0f) {
            timer = 0;
        }

        if (timer < -36_000_000.0f) {
            timer = 0;
        }

        tile.setTimer(timer);

        bindTexture(coreTexture);
        renderCore(x, y, z, timer);
        bindTexture(ringTexture);
        renderRingOne(x, y, z, timer);
        renderRingTwo(x, y, z, timer);
        renderRingThree(x, y, z, timer);
        bindTexture(shieldTexture);
        renderShield(x, y, z, timer);
    }

    private void renderCore(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - 25.0f), 0) / 80.0f, 1);
        chaos = Math.max(chaos, 0.05f);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glScalef(chaos, chaos, chaos);
        GL11.glRotatef(
            timer * 4 + timer * 36 * chaos,
            (float) (0.3 * sin(timer / 700.0) + sin(timer / 700.0 * 0.5) + 0.5 * sin(timer / 700.0 * 3)),
            (float) (1 * sin(timer / 700.0 * 0.3) + 3 * sin(timer / 700.0) + 0.3 * sin(timer / 700.0 * 3)),
            (float) (2 * sin(timer / 700.0 * 0.4) + sin(timer / 700.0 * 1.5) + 1.2 * sin(timer / 700.0 * 1)));
        nanoforgeCoreModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingOne(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - 25.0f), 0) / 80.0f, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(
            timer * 4 + timer * 36 * chaos,
            (float) (chaos * (2 * sin(timer / 700.0) + 0.5 * sin(timer / 700.0) + 2.5 * sin(timer / 700.0 * 0.2))),
            (float) (0.5 + chaos * (2 * sin(timer / 700.0 * 0.3) + sin(timer / 700.0) + 0.5 * sin(timer / 700.0 * 3))),
            (float) (chaos
                * (0.5 * sin(timer / 700.0 * 0.4) + 0.2 * sin(timer / 700.0 * 1.5) + 0.4 * sin(timer / 700.0 * 1))));
        nanoforgeRingOneModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingTwo(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - 25.0f), 0) / 80.0f, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(
            timer * 4 + timer * 36 * chaos,
            (float) (0.5
                + chaos * (2 * sin(timer / 700.0 * 0.5) + sin(timer / 700.0) + 1.5 * sin(timer / 700.0 * 0.3))),
            (float) (chaos
                * (1.3 * sin(timer / 700.0 * 1.2) + 0.4 * sin(timer / 700.0) + 1.1 * sin(timer / 700.0 * 1.5))),
            (float) (chaos
                * (0.5 * sin(timer / 700.0 * 0.4) + 0.2 * sin(timer / 700.0 * 1.5) + 0.4 * sin(timer / 700.0 * 1))));
        nanoforgeRingTwoModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingThree(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - 25.0f), 0) / 80.0f, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(
            timer * 4 + timer * 36 * chaos,
            (float) (chaos * (2 * sin(timer / 700.0 * 0.5) + sin(timer / 700.0) + 1.5 * sin(timer / 700.0 * 0.3))),
            (float) (chaos
                * (1.3 * sin(timer / 700.0 * 1.2) + 0.4 * sin(timer / 700.0) + 1.1 * sin(timer / 700.0 * 1.5))),
            (float) (0.5
                + chaos * (2.2 * sin(timer / 700.0) + 0.4 * sin(timer / 700.0 * 3) + 1.2 * sin(timer / 700.0 * 0.5))));
        nanoforgeRingThreeModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderShield(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - 25.0f), 0) / 80.0f, 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false); // Disable depth writing for transparency
        GL11.glColor4f(1f, 1f, 1f, 0.4f);
        GL11.glRotatef(
            timer * 4 + timer * 36 * chaos,
            (float) (2 * sin(timer / 700.0) + sin(timer / 700.0 * 0.5) + 0.5 * sin(timer / 700.0 * 3)),
            (float) (1 * sin(timer / 700.0 * 0.3) + 3 * sin(timer / 700.0) + 0.3 * sin(timer / 700.0 * 3)),
            (float) (0.5 * sin(timer / 700.0 * 0.4) + sin(timer / 700.0 * 1.5) + 1.2 * sin(timer / 700.0 * 1)));
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

        // Manually calculating deltaT - annoying minecraft
        long systemTime = System.currentTimeMillis() % (36_000_000);
        long diff = systemTime - nanoforge.getLastSystemTime();
        nanoforge.setLastSystemTime(systemTime);
        float deltaT = diff / 1000.0f;
        // Making sure the first frame doesn't freak out
        if (deltaT > 1) {
            deltaT = 0;
        }

        renderNanoForge(nanoforge, x, y, z, deltaT);
    }

}
