package com.github.bartimaeusnek.bartworks.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class BW_CropVisualizer extends EntityFX {
    int[] meta = new int[8];
    static Field tessellatorHasBrightnessField = null;

    public BW_CropVisualizer(World world, int x, int y, int z, int age) {
        super(world, (double) x, ((double) y - 0.0625d), (double) z);
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.particleMaxAge = age;
        for (int i = 0; i < 8; i++) this.meta[i] = this.rand.nextInt(8);
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
        try {
            if (tessellatorHasBrightnessField == null) {
                tessellatorHasBrightnessField = Tessellator.class.getDeclaredField(
                        (boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment")
                                ? "hasBrightness"
                                : "field_78414_p");
                tessellatorHasBrightnessField.setAccessible(true);
            }
            tessellatorHasBrightnessField.set(tessellator, false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        tessellator.setColorRGBA(255, 255, 255, 255);
        double f12 = this.posY - interpPosY;
        int i = 0;
        for (int x = -1; x <= 1; x++)
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;
                double f11 = (this.posX + (double) x) - interpPosX;
                double f13 = (this.posZ + (double) z) - interpPosZ;
                RenderBlocks.getInstance().renderBlockCropsImpl(Blocks.wheat, meta[i++], f11, f12, f13);
            }

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
