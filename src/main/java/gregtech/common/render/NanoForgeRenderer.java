package gregtech.common.render;

import static gregtech.api.enums.Mods.GregTech;
import static java.lang.Math.sin;

import java.util.function.Function;

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

    private static final float WARM_UP_TIME = 25.0f;
    private static final float FULL_CHAOS_TIME = 75.0f;
    private static final float SPEED_MULTIPLIER = 10.0f;
    private static final float CHAOS_SPEED_MULTIPLIER = 90.0f;
    private static final float SINUS_DIVIDER = 50.0f;
    private static final float MAX_CHAOS_SPEED_UP = 2.0f;
    private static final float RING_ROTATION_NORMAL = 1.0f;
    private static final Function<Float, Float> RING_ROTATION_MAJOR = (
        t) -> (float) (Math.sin(t / SINUS_DIVIDER) + 1.5 * Math.sin(t / SINUS_DIVIDER * 0.5)
            + 0.5 * Math.sin(t / SINUS_DIVIDER * 0.1));
    private static final Function<Float, Float> RING_ROTATION_MINOR = (
        t) -> (float) (Math.sin(t / SINUS_DIVIDER) + 1.5 * Math.sin(t / SINUS_DIVIDER * 0.2)
            + 0.5 * Math.sin(t / SINUS_DIVIDER * 0.1));

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

        if (timer < 0) {
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
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);
        float chaosScale = Math.min(Math.max(chaos, 0.05f), 1);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glScalef(chaosScale, chaosScale, chaosScale);
        GL11.glRotatef(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            (float) (0.3 * sin(timer / SINUS_DIVIDER) + sin(timer / SINUS_DIVIDER * 0.5)
                + 0.5 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (1 * sin(timer / SINUS_DIVIDER * 0.3) + 3 * sin(timer / SINUS_DIVIDER)
                + 0.3 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (2 * sin(timer / SINUS_DIVIDER * 0.4) + sin(timer / SINUS_DIVIDER * 1.5)
                + 1.2 * sin(timer / SINUS_DIVIDER * 1)));
        nanoforgeCoreModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingOne(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            RING_ROTATION_MAJOR.apply(timer) * chaos,
            0.5f + RING_ROTATION_NORMAL * chaos,
            RING_ROTATION_MINOR.apply(timer) * chaos);
        nanoforgeRingOneModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingTwo(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            0.5f + RING_ROTATION_NORMAL * chaos,
            RING_ROTATION_MINOR.apply(timer) * chaos,
            RING_ROTATION_MAJOR.apply(timer) * chaos);
        nanoforgeRingTwoModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderRingThree(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glRotatef(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            RING_ROTATION_MAJOR.apply(timer) * chaos,
            RING_ROTATION_MINOR.apply(timer) * chaos,
            0.5f + RING_ROTATION_NORMAL * chaos);
        nanoforgeRingThreeModel.renderAllVBO();

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void renderShield(double x, double y, double z, float timer) {
        float chaos = Math.min(Math.max((timer - WARM_UP_TIME), 0) / FULL_CHAOS_TIME, MAX_CHAOS_SPEED_UP);
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        GL11.glTranslated(x + .5f, y + .5f, z + .5f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false); // Disable depth writing for transparency
        GL11.glColor4f(1f, 1f, 1f, 0.4f);
        GL11.glRotatef(
            timer * SPEED_MULTIPLIER + timer * CHAOS_SPEED_MULTIPLIER * chaos,
            (float) (2 * sin(timer / SINUS_DIVIDER) + sin(timer / SINUS_DIVIDER * 0.5)
                + 0.5 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (1 * sin(timer / SINUS_DIVIDER * 0.3) + 3 * sin(timer / SINUS_DIVIDER)
                + 0.3 * sin(timer / SINUS_DIVIDER * 3)),
            (float) (0.5 * sin(timer / SINUS_DIVIDER * 0.4) + sin(timer / SINUS_DIVIDER * 1.5)
                + 1.2 * sin(timer / SINUS_DIVIDER * 1)));
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
