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

    int[] meta = new int[7 * 3];
    boolean isOldStructure;
    ExtendedFacing facing;

    public CropRenderer(World world, int x, int y, int z, ExtendedFacing extendedFacing, int age,
        boolean isOldStructure) {
        super(world, x, ((double) y - 0.0625d), z);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.particleMaxAge = age;
        this.isOldStructure = isOldStructure;
        this.facing = extendedFacing;
        for (int i = 0; i < 7 * 3; i++) this.meta[i] = this.rand.nextInt(8);
    }

    @Override
    public void onUpdate() {
        if (this.particleAge++ >= this.particleMaxAge) this.setDead();
    }

    @Override
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
        float p_70539_5_, float p_70539_6_, float p_70539_7_) {
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
        int zmin = -1, zmax = 1;
        if (!isOldStructure) {
            zmin = 0;
            zmax = 6;
        }
        int i = 0;
        for (int x = -1; x <= 1; x++) for (int z = zmin; z <= zmax; z++) {
            if (isOldStructure && x == 0 && z == 0) continue;
            double[] abc = new double[] { x, 0, z };
            double[] xyz = new double[] { 0, 0, 0 };
            facing.getWorldOffset(abc, xyz);
            double f11 = (this.posX + xyz[0]) - interpPosX;
            double f13 = (this.posZ + xyz[2]) - interpPosZ;

            RenderBlocks.getInstance()
                .renderBlockCropsImpl(Blocks.wheat, meta[i++], f11, f12, f13);
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
