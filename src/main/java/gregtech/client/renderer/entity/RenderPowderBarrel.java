package gregtech.client.renderer.entity;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.common.entity.EntityPowderBarrelPrimed;

@SideOnly(Side.CLIENT)
public class RenderPowderBarrel extends Render {

    private final RenderBlocks blockRenderer = new RenderBlocks();

    public RenderPowderBarrel() {
        this.shadowSize = 0.5F;
        this.renderManager = RenderManager.instance;
    }

    @Override
    public void doRender(final Entity tnt, final double x, final double y, final double z, final float unused,
        final float life) {
        this.renderPrimedTNT((EntityPowderBarrelPrimed) tnt, x, y, z, life);
    }

    public void renderPrimedTNT(EntityPowderBarrelPrimed tnt, double x, double y, double z, float life) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x, (float) y, (float) z);
        float var;

        if ((float) tnt.fuse - life + 1.0F < 10.0F) {
            var = 1.0F - ((float) tnt.fuse - life + 1.0F) / 10.0F;

            if (var < 0.0F) {
                var = 0.0F;
            }

            if (var > 1.0F) {
                var = 1.0F;
            }

            var *= var;
            var *= var;
            float scale = 1.0F + var * 0.3F;
            GL11.glScalef(scale, scale, scale);
        }

        var = (1.0F - ((float) tnt.fuse - life + 1.0F) / 100.0F) * 0.8F;
        this.bindEntityTexture(tnt);
        this.blockRenderer.renderBlockAsItem(ItemList.Block_Powderbarrel.getBlock(), 5, tnt.getBrightness(life));

        if (tnt.fuse / 5 % 2 == 0) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, var);
            this.blockRenderer.renderBlockAsItem(ItemList.Block_Powderbarrel.getBlock(), 5, 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return TextureMap.locationBlocksTexture;
    }
}
