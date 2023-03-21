package gregtech.api.ModernMaterials.PartProperties.Rendering;

import static gregtech.api.ModernMaterials.PartProperties.ModernMaterialUtilities.ModernMaterialUtilities.getMaterialFromItemStack;

import java.awt.*;

import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.common.render.GT_RenderUtil;

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

    public void renderPositionCorrection(ItemRenderType type) {

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
        ModernMaterial material = getMaterialFromItemStack(itemStack);
        if (material == null) {
            return;
        }
        Color materialColor = material.getColor();
        IIcon partIcon = itemStack.getItem().getIconFromDamage(material.getMaterialID()); // todo review this later, seems
                                                                                  // hacky.
        GL11.glPushMatrix();

        renderPositionCorrection(type);

        GL11.glColor4f(
                materialColor.getRed(),
                materialColor.getGreen(),
                materialColor.getBlue(),
                materialColor.getAlpha());

        if (type.equals(ItemRenderType.INVENTORY)) {
            GT_RenderUtil.renderItemIcon(partIcon, 16.0D, 0.001D, 0.0F, 0.0F, -1.0F);
        } else {
            // Render when held in hand, on floor etc.
            ItemRenderer.renderItemIn2D(
                    Tessellator.instance,
                    partIcon.getMaxU(),
                    partIcon.getMinV(),
                    partIcon.getMinU(),
                    partIcon.getMaxV(),
                    partIcon.getIconWidth(),
                    partIcon.getIconHeight(),
                    0.0625F);
        }

        GL11.glPopMatrix();
    }
}
