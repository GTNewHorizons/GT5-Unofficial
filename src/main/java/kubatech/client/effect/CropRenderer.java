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

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CropRenderer extends EntityFX {

    private static final int CROP_WIDTH = 3;
    private static final int CROP_LENGTH = 7;
    private static final int CROP_COUNT = CROP_WIDTH * CROP_LENGTH;
    private static final int MAX_MISSED_UPDATES = 3;

    private final int[] meta = new int[CROP_COUNT];
    private final double[] offsets = new double[CROP_COUNT * 2];
    private ExtendedFacing facing;
    private int missedUpdates;

    public CropRenderer(World world, int x, int y, int z, ExtendedFacing extendedFacing, int age) {
        super(world, x, y - 0.0625d, z);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.particleMaxAge = age;

        refreshGrowthStages();
        updateFacing(extendedFacing);
    }

    public boolean isFacing(ExtendedFacing extendedFacing) {
        return this.facing == extendedFacing;
    }

    public void updateOrigin(int x, int y, int z) {
        setPosition(x, y - 0.0625d, z);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
    }

    public void refreshGrowthStages() {
        for (int i = 0; i < CROP_COUNT; i++) {
            this.meta[i] = this.rand.nextInt(8);
        }
    }

    public boolean keepAlive() {
        if (this.isDead || ++this.missedUpdates >= MAX_MISSED_UPDATES) return false;
        this.particleAge = 0;
        return true;
    }

    public void updateFacing(ExtendedFacing extendedFacing) {
        if (this.facing == extendedFacing) return;
        this.facing = extendedFacing;

        // recompute cached offsets whenever the greenhouse rotates
        double[] abc = new double[3];
        double[] xyz = new double[3];
        int k = 0;
        for (int ox = -CROP_WIDTH / 2; ox <= CROP_WIDTH / 2; ox++) {
            for (int oz = 0; oz < CROP_LENGTH; oz++) {
                abc[0] = ox;
                abc[2] = oz;
                extendedFacing.getWorldOffset(abc, xyz);
                offsets[k++] = xyz[0];
                offsets[k++] = xyz[2];
            }
        }
    }

    @Override
    public void onUpdate() {
        this.missedUpdates = 0;
        if (this.particleAge++ >= this.particleMaxAge) this.setDead();
    }

    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
        float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        if (this.isDead) return;

        Tessellator tessellator = Tessellator.instance;
        Minecraft.getMinecraft()
            .getTextureManager()
            .bindTexture(TextureMap.locationBlocksTexture);
        tessellator.startDrawingQuads();
        tessellator.disableColor();
        GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDepthMask(true);
        tessellator.setBrightness(
            Blocks.wheat.getMixedBrightnessForBlock(this.worldObj, (int) this.posX, (int) this.posY, (int) this.posZ));
        tessellator.setColorRGBA(255, 255, 255, 255);
        double f12 = this.posY - interpPosY;
        RenderBlocks renderer = RenderBlocks.getInstance();

        for (int idx = 0; idx < CROP_COUNT; idx++) {
            double f11 = this.posX + offsets[idx * 2] - interpPosX;
            double f13 = this.posZ + offsets[idx * 2 + 1] - interpPosZ;
            renderer.renderBlockCropsImpl(Blocks.wheat, meta[idx], f11, f12, f13);
        }
        tessellator.draw();
    }

    @Override
    public int getFXLayer() {
        return 3;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 3;
    }
}
