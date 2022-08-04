package gregtech.nei;

import codechicken.nei.guihook.GuiContainerManager;
import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.util.GT_Recipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class GT_NEI_EECHandler extends GT_NEI_DefaultHandler {
    public GT_NEI_EECHandler(GT_Recipe.GT_Recipe_Map aRecipeMap) {
        super(aRecipeMap);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new GT_NEI_EECHandler(this.mRecipeMap);
    }

    @Override
    public void drawForeground(int recipe) {
        super.drawForeground(recipe);

        GuiContainerManager.enable3DRender();
        GL11.glColor4f(1f, 1f, 1f, 1f);

        Minecraft mc = Minecraft.getMinecraft();

        ScaledResolution scale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int factor = scale.getScaleFactor();

        int width = scale.getScaledWidth();
        int height = scale.getScaledHeight();
        int mouseX = Mouse.getX() * width / mc.displayWidth;
        int mouseZ = height - Mouse.getY() * height / mc.displayHeight - 1;

        // Get current x,y from matrix
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buf);
        float x = buf.get(12);
        float y = buf.get(13);

        ItemStack s = getIngredientStacks(recipe).get(0).item;
        try {
            EntityLivingBase e = (EntityLivingBase)EntityList.createEntityByID(s.getItemDamage(), Minecraft.getMinecraft().theWorld);
            if(e instanceof EntitySlime) {
                NBTTagCompound nbt = new NBTTagCompound();
                e.writeEntityToNBT(nbt);
                nbt.setInteger("Size", 0);
                e.readEntityFromNBT(nbt);
            }

            float ehight = e.height;
            int desiredhight = 27;

            int scaled = (int) (desiredhight / ehight);
            // ARGS: x, y, scale, rot, rot, entity
            GuiInventory.func_147046_a(25, 37, scaled, (float)(x + 25) - mouseX, (float)(y + 37 - ehight * scaled) - mouseZ, e);
        }
        catch (Throwable ignored)
        {

        }

        GuiContainerManager.enable2DRender();
    }
}
