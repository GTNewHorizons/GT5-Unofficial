/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.client.effect;

import static net.minecraft.client.renderer.entity.RenderManager.instance;
import static net.minecraft.client.renderer.entity.RenderManager.renderPosX;
import static net.minecraft.client.renderer.entity.RenderManager.renderPosY;
import static net.minecraft.client.renderer.entity.RenderManager.renderPosZ;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;

import com.kuba6000.mobsinfo.api.utils.MobUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kubatech.Tags;
import kubatech.config.Config;

@SideOnly(Side.CLIENT)
public class EntityRenderer extends EntityFX {

    private static final Logger LOG = LogManager.getLogger(Tags.MODID + "[Entity Renderer]");
    private Vector3f verticalAxis;
    private Vector3f horizontalAxis;
    private EntityLiving entityToRender = null;
    private static final float RAD_90 = (float) Math.PI / 2f;
    private static final float RAD_180 = (float) Math.PI;

    public EntityRenderer(ExtendedFacing extendedFacing, World world, double x, double y, double z, int age) {
        super(world, x + 0.5d, y, z + 0.5d);
        this.getAxesFromFacing(extendedFacing);
        this.particleMaxAge = age;
        this.particleAge = 0;
    }

    public EntityRenderer(EntityRenderer r, int age) {
        super(r.worldObj, r.posX, r.posY, r.posZ);
        this.verticalAxis = r.verticalAxis;
        this.horizontalAxis = r.horizontalAxis;
        this.particleMaxAge = age;
        this.particleAge = 0;
        this.ticksExisted = r.ticksExisted;
        this.entityToRender = r.entityToRender;
    }

    @Override
    public void onUpdate() {
        if (this.entityToRender == null) return;
        this.ticksExisted++;
        if (ticksExisted % 20 == 0) entityToRender.hurtTime = 10;
        else if (entityToRender.hurtTime > 0) entityToRender.hurtTime--;
        if (this.particleAge++ == this.particleMaxAge) {
            this.setDead();
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 3;
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    public void setEntity(EntityLiving entity) {
        this.entityToRender = entity;
    }

    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
        float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        if (entityToRender == null) return;

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);

        entityToRender.worldObj = this.worldObj;

        // quiver still bugged a bit, but it is on the skeleton now
        entityToRender.setPosition(this.posX, this.posY + 1d /* for some reason quiver renders too low? */, this.posZ);
        entityToRender.lastTickPosX = entityToRender.posX;
        entityToRender.lastTickPosY = entityToRender.posY;
        entityToRender.lastTickPosZ = entityToRender.posZ;

        entityToRender.prevRotationYawHead = entityToRender.rotationYawHead;
        entityToRender.prevRenderYawOffset = entityToRender.renderYawOffset;
        entityToRender.prevRotationPitch = entityToRender.rotationPitch;

        // TODO: Use new scale calculator
        final float desiredScale = MobUtils.getDesiredScale(entityToRender, 2f);

        final double diffH1;
        final double diffH2;
        final double diffV1;
        final Vector3f diffH;
        final Vector3f diffV;

        if (this.verticalAxis.x != 0) {
            diffH1 = renderPosY - (this.posY + 0.5f);
            diffH2 = renderPosZ - this.posZ;
            diffV1 = renderPosX - (this.posX + (entityToRender.getEyeHeight() * desiredScale - 0.5f) * this.verticalAxis.x);
            diffH = new Vector3f(0f, (float) diffH1, (float) diffH2);
            diffV = new Vector3f((float) diffV1, (float) diffH1, (float) diffH2);
        }
        else if (this.verticalAxis.y != 0) {
            diffH1 = renderPosX - this.posX;
            diffH2 = renderPosZ - this.posZ;
            diffV1 = renderPosY - (this.posY + entityToRender.getEyeHeight() * desiredScale);
            diffH = new Vector3f((float) diffH1, 0f, (float) diffH2).normalize();
            diffV = new Vector3f((float) diffH1, (float) diffV1, (float) diffH2).normalize();
        }
        else {
            diffH1 = renderPosX - this.posX;
            diffH2 = renderPosY - (this.posY + 0.5f);
            diffV1 = renderPosZ - (this.posZ + (entityToRender.getEyeHeight() * desiredScale - 0.5f) * this.verticalAxis.z);
            diffH = new Vector3f((float) diffH1, (float) diffH2, 0f);
            diffV = new Vector3f((float) diffH1, (float) diffH2, (float) diffV1);
        }

        final float angleH = (float) Math.toDegrees(this.horizontalAxis.angleSigned(diffH, new Vector3f(this.verticalAxis).negate()));
        final float angleV = (float) Math.toDegrees(this.verticalAxis.angleSigned(diffV, new Vector3f(this.verticalAxis).cross(diffV).normalize()));

        // 0 degree yaw appears to be SOUTH (pos Z)
        // Add 180 here since other angle convention uses NORTH as basis
        entityToRender.renderYawOffset = angleH + 180f;
        entityToRender.rotationYawHead = angleH + 180f;
        // -90 is straight up
        entityToRender.rotationPitch = angleV - 90f;

        float p_147936_2_ = 0.5f;

        float f1 = entityToRender.prevRotationYaw
            + (entityToRender.rotationYaw - entityToRender.prevRotationYaw) * p_147936_2_;
        int i = entityToRender.getBrightnessForRender(p_147936_2_);

        if (entityToRender.isBurning()) {
            i = 15728880;
        }

        int j = i % 65536;
        int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j, (float) k);
        GL11.glColor4f(1f, 1f, 1f, 1F);
        RenderHelper.enableStandardItemLighting();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        int stackdepth = GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
        GL11.glPushMatrix();
        if (this.verticalAxis.x != 0) {
            GL11.glTranslatef(
                (float) ((this.posX - 0.5f * this.verticalAxis.x) - renderPosX),
                (float) ((this.posY + 0.5f) - renderPosY),
                (float) ((this.posZ) - renderPosZ));
            final float azi = (float) Math.toDegrees(new Vector3f(0f, 0f, -1f).angleSigned(this.horizontalAxis, this.verticalAxis));
            GL11.glRotatef(azi, this.verticalAxis.x, this.verticalAxis.y, this.verticalAxis.z);
            GL11.glRotatef(-90f * this.verticalAxis.x, 0f, 0f, 1f);
        }
        else if (this.verticalAxis.y != 0) {
            GL11.glTranslatef(
                (float) ((this.posX) - renderPosX),
                (float) ((this.posY + (this.verticalAxis.y < 0 ? 1.0f : 0.0f)) - renderPosY),
                (float) ((this.posZ) - renderPosZ));
            final float azi = (float) Math.toDegrees(new Vector3f(0f, 0f, -1f).angleSigned(this.horizontalAxis, this.verticalAxis));
            GL11.glRotatef(azi, this.verticalAxis.x, this.verticalAxis.y, this.verticalAxis.z);
            if (this.verticalAxis.y < 0) {
                GL11.glRotatef(180f, 1f, 0f, 0f);
                GL11.glRotatef(180f, 0f, -1f, 0f);
            }
        }
        else {
            GL11.glTranslatef(
                (float) ((this.posX) - renderPosX),
                (float) ((this.posY + 0.5f) - renderPosY),
                (float) ((this.posZ - 0.5f * this.verticalAxis.z) - renderPosZ));
            final float azi = (float) Math.toDegrees(new Vector3f(0f, this.verticalAxis.z, 0f).angleSigned(this.horizontalAxis, this.verticalAxis));
            GL11.glRotatef(azi, this.verticalAxis.x, this.verticalAxis.y, this.verticalAxis.z);
            GL11.glRotatef(90f * this.verticalAxis.z, 1f, 0f, 0f);
        }
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if (desiredScale < 1f) GL11.glScalef(desiredScale, desiredScale, desiredScale);

        float healthScale = BossStatus.healthScale;
        int statusBarTime = BossStatus.statusBarTime;
        String bossName = BossStatus.bossName;
        boolean hasColorModifier = BossStatus.hasColorModifier;

        try {
            instance.renderEntityWithPosYaw(entityToRender, 0f, 0f, 0f, f1, p_147936_2_);
        } catch (Throwable ex) {
            Tessellator tes = Tessellator.instance;
            try {
                tes.draw();
            } catch (Exception ignored) {}
        }

        BossStatus.healthScale = healthScale;
        BossStatus.statusBarTime = statusBarTime;
        BossStatus.bossName = bossName;
        BossStatus.hasColorModifier = hasColorModifier;

        GL11.glMatrixMode(GL11.GL_MODELVIEW_MATRIX);
        stackdepth -= GL11.glGetInteger(GL11.GL_MODELVIEW_STACK_DEPTH);
        if (stackdepth < 0) for (; stackdepth < 0; stackdepth++) GL11.glPopMatrix();
        if (stackdepth > 0) for (; stackdepth > 0; stackdepth--) GL11.glPushMatrix();

        GL11.glPopAttrib();

        int err;
        while ((err = GL11.glGetError()) != GL11.GL_NO_ERROR) if (Config.Debug.showRenderErrors) LOG.error(
            EntityList.getEntityString(entityToRender) + " | GL ERROR: " + err + " / " + GLU.gluErrorString(err));

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_COLOR_MATERIAL);
    }

    private void getAxesFromFacing(ExtendedFacing extendedFacing) {
        ForgeDirection direction = extendedFacing.getDirection();
        Rotation rotation = extendedFacing.getRotation();

        // Initial axes defined for default facing north
        Vector3f vAxis = new Vector3f(0f, 1f, 0f);
        Vector3f hAxis = new Vector3f(0f, 0f, -1f);

        switch(direction) {
            case EAST -> hAxis.rotateY(-RAD_90);
            case SOUTH -> hAxis.rotateY(RAD_180);
            case WEST -> hAxis.rotateY(RAD_90);
            case UP -> {
                vAxis.rotateX(RAD_90);
                hAxis.rotateX(RAD_90);
                // For some reason the UP rotation for controllers has an extra 180
                vAxis.rotateAxis(RAD_180, hAxis.x, hAxis.y, hAxis.z);
            }
            case DOWN -> {
                vAxis.rotateX(-RAD_90);
                hAxis.rotateX(-RAD_90);
            }
            case NORTH, UNKNOWN -> {}
        }

        switch(rotation) {
            case CLOCKWISE -> vAxis.rotateAxis(-RAD_90, hAxis.x, hAxis.y, hAxis.z);
            case COUNTER_CLOCKWISE -> vAxis.rotateAxis(RAD_90, hAxis.x, hAxis.y, hAxis.z);
            case UPSIDE_DOWN -> vAxis.negate();
        }

        this.verticalAxis = vAxis;
        this.horizontalAxis = hAxis;
    }
}
