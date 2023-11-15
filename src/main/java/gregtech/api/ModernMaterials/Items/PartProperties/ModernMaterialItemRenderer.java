package gregtech.api.ModernMaterials.Items.PartProperties;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;
import gregtech.api.ModernMaterials.Items.PartsClasses.MaterialPart;
import gregtech.common.render.GT_RenderUtil;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.GL_CURRENT_BIT;

public class ModernMaterialItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack,
        ItemRendererHelper itemRendererHelper) {
        return type == ItemRenderType.ENTITY;
    }

    private void renderPositionCorrection(ItemRenderType type) {

        // Rendering correct positions.
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            if (RenderItem.renderInFrame) {
                GL11.glScalef(0.85F, 0.85F, 0.85F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-0.5D, -0.43D, 0.0D); // todo review this, seems to clip into floor more than normal.
                                                        // -0.42D
            } else {
                GL11.glTranslated(-0.5D, -0.12D, 0.0D);
            }
        }
    }


    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... objects) {

        if (!(itemStack.getItem() instanceof MaterialPart materialPart)) return;

        ModernMaterial material = ModernMaterial.getMaterialFromItemStack(itemStack);

        // Branch off to custom logic.
        if (material.getCustomItemRenderer() != null) {
            material.getCustomItemRenderer().renderItem(type, itemStack, objects);
            return;
        }

        Color materialColor = material.getColor();

        ItemsEnum partsEnum = materialPart.getPart();

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderPositionCorrection(type);

        // Iterate over the items layers and render them.
        for (IconWrapper iconWrapper : material.getTextureType().getStandardTextureArray(partsEnum)) {

            GL11.glPushAttrib(GL_CURRENT_BIT);

            // Determines if the layer needs colouring.
            if (iconWrapper.isColoured) {
                GL11.glColor3f(
                    materialColor.getRed() / 255.0f,
                    materialColor.getGreen() / 255.0f,
                    materialColor.getBlue() / 255.0f);
            }

            renderLayer(iconWrapper.icon, type);

            GL11.glPopAttrib();

        }

        GL11.glPopMatrix();
    }

    public static void renderLayer(IIcon icon, ItemRenderType renderType) {
        if (renderType.equals(ItemRenderType.INVENTORY)) {
            GT_RenderUtil.renderItemIcon(icon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            // Render when held in hand, on floor etc.
            ItemRenderer.renderItemIn2D(
                Tessellator.instance,
                icon.getMaxU(),
                icon.getMinV(),
                icon.getMinU(),
                icon.getMaxV(),
                icon.getIconWidth(),
                icon.getIconHeight(),
                0.0625F);
        }
    }
}
