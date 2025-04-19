package gregtech.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import appeng.api.storage.data.IAEItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LMACraftingFX extends EntityFX {

    private final EntityItem entityItem;
    private final boolean isItemBlock;

    public LMACraftingFX(World w, double x, double y, double z, int age, IAEItemStack itemStack) {
        super(w, x, y, z, 0, 0, 0);
        this.entityItem = new EntityItem(this.worldObj, 0, 0, 0, itemStack.getItemStack());
        this.entityItem.hoverStart = 0;
        this.isItemBlock = itemStack.getItemStack()
            .getItem() instanceof ItemBlock;

        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.particleMaxAge = age + 1;
        this.noClip = true;
    }

    @Override
    public void renderParticle(Tessellator tessellator, float renderPartialTicks, float rX, float rY, float rZ,
        float rYZ, float rXY) {
        Tessellator.instance.draw();
        GL11.glPushMatrix();

        float ticks = Minecraft.getMinecraft().renderViewEntity.ticksExisted;
        double x = (prevPosX + (posX - prevPosX) * renderPartialTicks - interpPosX);
        double y = (prevPosY + (posY - prevPosY) * renderPartialTicks - interpPosY);
        double z = (prevPosZ + (posZ - prevPosZ) * renderPartialTicks - interpPosZ);
        float h = MathHelper.sin(ticks % 32767.0f / 16.0f) * 0.05f;
        float scale = isItemBlock ? 3.6f : 1.8f;

        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.15f + h, (float) z + 0.5f);
        GL11.glRotatef(ticks % 360.0f, 0, 1, 0);
        GL11.glScalef(scale, scale, scale);

        RenderManager.instance.renderEntityWithPosYaw(entityItem, 0, 0, 0, 0, 0);

        GL11.glPopMatrix();
        Tessellator.instance.startDrawingQuads();
    }

    @Override
    public int getBrightnessForRender(float partialTickTime) {
        return super.getBrightnessForRender((15 << 20) | (15 << 4));
    }

    @Override
    public int getFXLayer() {
        return isItemBlock ? 1 : 2;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 2;
    }
}
