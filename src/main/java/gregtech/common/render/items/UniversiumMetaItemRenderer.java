package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.postprocessing.shaders.UniversiumShader;
import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.MetaGeneratedItem;
import gregtech.common.config.Client;

public class UniversiumMetaItemRenderer implements IItemRenderer {

    private final IIconContainer mask;

    public UniversiumMetaItemRenderer(IIconContainer mask) {
        this.mask = mask;
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return Client.render.renderUniversiumFancy
            && (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
                || type == ItemRenderType.INVENTORY
                || type == ItemRenderType.ENTITY);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY && helper == ItemRendererHelper.ENTITY_BOBBING
            || (helper == ItemRendererHelper.ENTITY_ROTATION && Minecraft.getMinecraft().gameSettings.fancyGraphics);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (item.getItem() instanceof MetaGeneratedItem mgItem) {
            IIcon[] icons = mgItem.mIconList[item.getItemDamage() - mgItem.mOffset];
            if (icons != null && icons.length > 0 && icons[0] != null) {
                if (mask != null) {
                    magicRenderMethod(type, icons[0], mask.getIcon(), data);
                }
            }
        }
    }

    private void magicRenderMethod(ItemRenderType type, IIcon tIcon, IIcon mask, Object... data) {
        final UniversiumShader shader = UniversiumShader.getInstance();

        processLightLevel(type, shader, data);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (type == ItemRenderType.INVENTORY) {
            RenderHelper.enableGUIStandardItemLighting();

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            // Draw item
            ItemRenderUtil.renderItem(type, tIcon);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableGUIStandardItemLighting();

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            shader.setRenderInInventory()
                .use();

            GL11.glColor4f(1, 1, 1, 1);

            // Draw cosmic overlay
            ItemRenderUtil.renderItem(type, mask);

            UniversiumShader.clear();

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        } else {
            // RENDER ITEM
            ItemRenderUtil.renderItem(type, tIcon);

            int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDepthFunc(GL11.GL_EQUAL);
            shader.use();

            // RENDER COSMIC OVERLAY
            ItemRenderUtil.renderItem(type, mask);
            UniversiumShader.unbind();
            GL11.glDepthFunc(GL11.GL_LEQUAL);

            GL20.glUseProgram(program);
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void processLightLevel(ItemRenderType type, UniversiumShader shader, Object... data) {
        switch (type) {
            case ENTITY -> {
                EntityItem ent = (EntityItem) (data[1]);
                if (ent != null) {
                    shader.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
            }
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> {
                EntityLivingBase ent = (EntityLivingBase) (data[1]);
                if (ent != null) {
                    shader.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
            }
            default -> {
                shader.setLightLevel(1.0f);
            }
        }
    }
}
