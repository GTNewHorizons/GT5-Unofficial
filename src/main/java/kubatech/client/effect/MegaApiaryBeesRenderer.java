package kubatech.client.effect;

import static net.minecraft.client.renderer.entity.RenderManager.*;

import kubatech.api.enums.ItemList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class MegaApiaryBeesRenderer extends EntityFX {
    public MegaApiaryBeesRenderer(World world, double x, double y, double z, int age) {
        super(world, x, y + 2, z);
        this.particleMaxAge = age;
    }

    @Override
    public void onUpdate() {
        if (this.particleAge++ == this.particleMaxAge) this.setDead();
        if (this.particleAge % 4 == 0)
            if (this.particleAge % 8 == 0) this.posY += 0.1;
            else this.posY -= 0.1;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 3;
    }

    @Override
    public int getFXLayer() {
        return 3;
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

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        GL11.glPushMatrix();
        GL11.glTranslatef(
                (float) (this.posX - renderPosX), (float) (this.posY - renderPosY), (float) (this.posZ - renderPosZ));
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(180f, 1f, 0f, 0f);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);

        GL11.glColor4f(1f, 1f, 1f, 1F);

        IIcon icon = ItemList.Beeeeee.get(1).getIconIndex();

        GL11.glPushMatrix();
        GL11.glTranslatef(0f, 0f, -4f);
        GL11.glScalef(0.1f, 0.1f, 0.1f);
        RenderItem.getInstance().renderIcon(0, 0, icon, 16, 16);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(1f, 0f, 3f);
        GL11.glRotatef(180f, 0f, 1f, 0f);
        GL11.glScalef(0.1f, 0.1f, 0.1f);
        RenderItem.getInstance().renderIcon(0, 0, icon, 16, 16);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(4f, 0f, -1f);
        GL11.glRotatef(-90f, 0f, 1f, 0f);
        GL11.glScalef(0.1f, 0.1f, 0.1f);
        RenderItem.getInstance().renderIcon(0, 0, icon, 16, 16);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(-3f, 0f, 1f);
        GL11.glRotatef(90f, 0f, 1f, 0f);
        GL11.glScalef(0.1f, 0.1f, 0.1f);
        RenderItem.getInstance().renderIcon(0, 0, icon, 16, 16);
        GL11.glPopMatrix();

        GL11.glPopMatrix();
    }
}
