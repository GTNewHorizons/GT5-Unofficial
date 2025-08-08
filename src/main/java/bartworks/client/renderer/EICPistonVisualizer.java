package bartworks.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;

@SideOnly(Side.CLIENT)
public class EICPistonVisualizer extends EntityFX {

    public EICPistonVisualizer(World world, int x, int y, int z, int age) {
        super(world, x, y, z);
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
    public void renderParticle(Tessellator p_70539_1_, float p_70539_2_, float p_70539_3_, float p_70539_4_,
        float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);

        RenderBlocks.getInstance().blockAccess = this.worldObj;
        RenderBlocks.getInstance()
            .setRenderFromInside(false);

        // Look at nearby block for correct tiered block to use
        Pair<Block, Integer> tieredBlock;
        if (this.worldObj != null) {
            int nX = (int) this.posX;
            int nY = (int) this.posY + 1;
            int nZ = (int) this.posZ;
            Block neighborBlock = this.worldObj.getBlock(nX, nY, nZ);
            int neighborMeta = this.worldObj.getBlockMetadata(nX, nY, nZ);
            tieredBlock = Pair.of(neighborBlock, neighborMeta);
        } else {
            tieredBlock = Pair.of(GregTechAPI.sBlockMetal5, 2);
        }

        IIcon icon = tieredBlock.getKey()
            .getIcon(0, tieredBlock.getValue());

        double x = this.posX + 1;
        double z = this.posZ;

        double f11 = x - interpPosX;
        double f12 = this.posY - interpPosY;
        double f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
            .renderBlockUsingTexture(tieredBlock.getKey(), (int) x, (int) this.posY, (int) z, icon);

        x = this.posX - 1;
        z = this.posZ;

        f11 = x - interpPosX;
        f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
            .renderBlockUsingTexture(tieredBlock.getKey(), (int) x, (int) this.posY, (int) z, icon);

        x = this.posX;
        z = this.posZ + 1;

        f11 = x - interpPosX;
        f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
            .renderBlockUsingTexture(tieredBlock.getKey(), (int) x, (int) this.posY, (int) z, icon);

        x = this.posX;
        z = this.posZ - 1;

        f11 = x - interpPosX;
        f13 = z - interpPosZ;
        tessellator.setTranslation(f11 - x, f12 - this.posY, f13 - z);
        RenderBlocks.getInstance()
            .renderBlockUsingTexture(tieredBlock.getKey(), (int) x, (int) this.posY, (int) z, icon);

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
