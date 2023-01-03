package com.github.bartimaeusnek.bartworks.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BW_EICPistonVisualizer extends EntityFX {

    public BW_EICPistonVisualizer(World world, int x, int y, int z, int age) {
        super(world, (double) x, ((double) y), (double) z);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.particleMaxAge = age;
    }

    @Override
    public void onUpdate() {
        if (this.particleAge++ >= this.particleMaxAge) this.setDead();
    }

    @Override
    public void renderParticle(
            Tessellator p_70539_1_,
            float p_70539_2_,
            float p_70539_3_,
            float p_70539_4_,
            float p_70539_5_,
            float p_70539_6_,
            float p_70539_7_) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);

        RenderBlocks.getInstance().blockAccess = this.worldObj;
        RenderBlocks.getInstance().setRenderFromInside(false);

        IIcon icon = GregTech_API.sBlockMetal5.getIcon(0, 2);

        double x = this.posX + 1;
        double z = this.posZ;

        double f11 = x - interpPosX;
        double f12 = this.posY - interpPosY;
        double f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
                .renderBlockUsingTexture(GregTech_API.sBlockMetal5, (int) x, (int) this.posY, (int) z, icon);

        x = this.posX - 1;
        z = this.posZ;

        f11 = x - interpPosX;
        f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
                .renderBlockUsingTexture(GregTech_API.sBlockMetal5, (int) x, (int) this.posY, (int) z, icon);

        x = this.posX;
        z = this.posZ + 1;

        f11 = x - interpPosX;
        f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
                .renderBlockUsingTexture(GregTech_API.sBlockMetal5, (int) x, (int) this.posY, (int) z, icon);

        x = this.posX;
        z = this.posZ - 1;

        f11 = x - interpPosX;
        f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
                .renderBlockUsingTexture(GregTech_API.sBlockMetal5, (int) x, (int) this.posY, (int) z, icon);

        tessellator.setTranslation(0d, 0d, 0d);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(true);
    }

    @Override
    public int getFXLayer() {
        return 1;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 2;
    }
}
