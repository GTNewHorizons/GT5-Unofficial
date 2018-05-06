package openmodularturrets.entity.projectiles;

import com.github.technus.tectech.Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Tec on 29.07.2017.
 */
@SideOnly(Side.CLIENT)
public class projectileRenderEM extends Render {
    private static final ResourceLocation textures = new ResourceLocation(Reference.MODID + ":textures/entity/projectileEM.png");

    private void render(projectileEM entity, double par2, double par4, double par6, float par9) {
        bindEntityTexture(entity);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4 + 0.3F, (float)par6);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        Tessellator var18 = Tessellator.instance;
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(b0 * 10) / 32.0F;
        float f5 = (float)(5 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        GL11.glEnable('\u803a');
        float f11 = - par9;
        if(f11 > 0.0F) {
            float f = -MathHelper.sin(f11 * 3.0F) * f11;
            GL11.glRotatef(f, 0.0F, 0.0F, 1.0F);
        }

        GL11.glDisable(2896);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(0.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);

        for(int var19 = 0; var19 < 4; ++var19) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            var18.startDrawingQuads();
            var18.addVertexWithUV(-16.0D, -2.0D, 0.0D, (double)f2, (double)f4);
            var18.addVertexWithUV(16.0D, -2.0D, 0.0D, (double)f3, (double)f4);
            var18.addVertexWithUV(16.0D, 2.0D, 0.0D, (double)f3, (double)f5);
            var18.addVertexWithUV(-16.0D, 2.0D, 0.0D, (double)f2, (double)f5);
            var18.draw();
        }

        GL11.glEnable(2896);
        GL11.glDisable('\u803a');
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity par1Entity) {
        return textures;
    }

    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        render((projectileEM)par1Entity, par2, par4, par6, par9);
    }
}
