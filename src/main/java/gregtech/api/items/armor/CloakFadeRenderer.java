package gregtech.api.items.armor;

import java.util.Map;
import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class CloakFadeRenderer {

    private static final float FADE_DURATION_TICKS = 20.0F;
    private static final float NEVER_CHANGED = -1000.0F;

    private static final float PLAYER_MODEL_SCALE = 0.9375F;
    private static final float PLAYER_MODEL_Y_OFFSET = -1.5078125F;
    private static final float MODEL_PIXEL_SCALE = 0.0625F;
    private static final double SNEAK_Y_OFFSET = 0.125D;

    private static final float SHIMMER_INFLATE = 0.4F;
    private static final float SHIMMER_MAX_ALPHA = 0.35F;
    private static final int SHIMMER_LAYERS = 2;

    private static final ModelBiped BODY_MODEL = new ModelBiped(0.0F);
    private static final ModelBiped SHIMMER_MODEL = new ModelBiped(SHIMMER_INFLATE);

    private static final Map<EntityPlayer, CloakState> CLOAK_STATES = new WeakHashMap<>();

    private CloakFadeRenderer() {}

    private static final class CloakState {

        private boolean cloaked;
        private float changedAtTick = NEVER_CHANGED;

        private CloakState(boolean cloaked) {
            this.cloaked = cloaked;
        }
    }

    private static final class Pose {

        private final double x;
        private final double y;
        private final double z;
        private final float bodyYaw;
        private final float headYaw;
        private final float pitch;
        private final float limbSwing;
        private final float limbSwingAmount;
        private final float ageInTicks;

        private Pose(AbstractClientPlayer player, float partialTicks) {
            x = lerp(player.lastTickPosX, player.posX, partialTicks) - RenderManager.renderPosX;
            z = lerp(player.lastTickPosZ, player.posZ, partialTicks) - RenderManager.renderPosZ;
            double feetY = lerp(player.lastTickPosY, player.posY, partialTicks) - RenderManager.renderPosY
                - player.yOffset;
            y = isSneakingOtherPlayer(player) ? feetY - SNEAK_Y_OFFSET : feetY;

            bodyYaw = lerpAngle(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
            headYaw = lerpAngle(player.prevRotationYawHead, player.rotationYawHead, partialTicks) - bodyYaw;
            pitch = lerpAngle(player.prevRotationPitch, player.rotationPitch, partialTicks);
            limbSwingAmount = Math
                .min(1.0F, (float) lerp(player.prevLimbSwingAmount, player.limbSwingAmount, partialTicks));
            limbSwing = player.limbSwing - player.limbSwingAmount * (1.0F - partialTicks);
            ageInTicks = player.ticksExisted + partialTicks;
        }
    }

    public static boolean renderFade(EntityPlayer player, boolean cloaked, float partialTicks) {
        if (!(player instanceof AbstractClientPlayer clientPlayer)) return false;

        float progress = updateFadeProgress(player, cloaked, partialTicks);
        if (progress >= 1.0F) return false;

        float visibility = cloaked ? 1.0F - progress : progress;
        drawFadingPlayer(clientPlayer, partialTicks, visibility, progress);

        boolean isFadingIn = !cloaked;
        return isFadingIn;
    }

    private static float updateFadeProgress(EntityPlayer player, boolean cloaked, float partialTicks) {
        float now = player.worldObj.getTotalWorldTime() + partialTicks;

        CloakState state = CLOAK_STATES.get(player);
        if (state == null) {
            CLOAK_STATES.put(player, new CloakState(cloaked));
            return 1.0F;
        }

        if (state.cloaked != cloaked) {
            state.cloaked = cloaked;
            state.changedAtTick = now;
        }

        return MathHelper.clamp_float((now - state.changedAtTick) / FADE_DURATION_TICKS, 0.0F, 1.0F);
    }

    private static void drawFadingPlayer(AbstractClientPlayer player, float partialTicks, float visibility,
        float progress) {
        Pose pose = new Pose(player, partialTicks);
        copyPlayerState(BODY_MODEL, player, partialTicks);
        copyPlayerState(SHIMMER_MODEL, player, partialTicks);

        GL11.glPushMatrix();
        applyPlayerTransform(pose);
        beginTranslucentRendering();

        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(player.getLocationSkin());

        drawTranslucentBody(player, pose, visibility);
        drawShimmerOutline(player, pose, progress);

        endTranslucentRendering();
        GL11.glPopMatrix();
    }

    private static void applyPlayerTransform(Pose pose) {
        GL11.glTranslated(pose.x, pose.y, pose.z);
        GL11.glRotatef(180.0F - pose.bodyYaw, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glScalef(-1.0F, -1.0F, 1.0F);
        GL11.glScalef(PLAYER_MODEL_SCALE, PLAYER_MODEL_SCALE, PLAYER_MODEL_SCALE);
        GL11.glTranslatef(0.0F, PLAYER_MODEL_Y_OFFSET, 0.0F);
    }

    private static void drawTranslucentBody(AbstractClientPlayer player, Pose pose, float visibility) {
        float distortion = MathHelper.sin(pose.ageInTicks * 2.3F) * 0.03F * (1.0F - visibility);

        GL11.glPushMatrix();
        GL11.glTranslatef(distortion, 0.0F, -distortion);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(0.85F, 0.95F, 1.0F, visibility * visibility);
        renderModel(BODY_MODEL, player, pose);
        GL11.glPopMatrix();
    }

    private static void drawShimmerOutline(AbstractClientPlayer player, Pose pose, float progress) {
        float strength = MathHelper.sin(progress * (float) Math.PI);
        if (strength <= 0.01F) return;

        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glColor4f(0.45F, 0.8F, 1.0F, strength * SHIMMER_MAX_ALPHA);

        for (int layer = 0; layer < SHIMMER_LAYERS; layer++) {
            float offset = MathHelper.sin(pose.ageInTicks * 1.7F + layer * 2.1F) * 0.04F;

            GL11.glPushMatrix();
            GL11.glTranslatef(offset, -offset * 0.5F, offset);
            renderModel(SHIMMER_MODEL, player, pose);
            GL11.glPopMatrix();
        }
    }

    private static void renderModel(ModelBiped model, AbstractClientPlayer player, Pose pose) {
        model.render(
            player,
            pose.limbSwing,
            pose.limbSwingAmount,
            pose.ageInTicks,
            pose.headYaw,
            pose.pitch,
            MODEL_PIXEL_SCALE);
    }

    private static void beginTranslucentRendering() {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDepthMask(false);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
    }

    private static void endTranslucentRendering() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
    }

    private static void copyPlayerState(ModelBiped model, EntityPlayer player, float partialTicks) {
        model.isChild = false;
        model.isSneak = player.isSneaking();
        model.isRiding = player.isRiding();
        model.heldItemRight = player.getHeldItem() != null ? 1 : 0;
        model.onGround = player.getSwingProgress(partialTicks);
    }

    private static boolean isSneakingOtherPlayer(EntityPlayer player) {
        return player.isSneaking() && !(player instanceof EntityPlayerSP);
    }

    private static double lerp(double previous, double current, float partialTicks) {
        return previous + (current - previous) * partialTicks;
    }

    private static float lerpAngle(float previous, float current, float partialTicks) {
        return previous + MathHelper.wrapAngleTo180_float(current - previous) * partialTicks;
    }
}
