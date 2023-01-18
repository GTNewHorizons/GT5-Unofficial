package gregtech.api.ModernMaterials.PartProperties.Rendering;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.common.render.GT_RenderUtil;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static gregtech.api.ModernMaterials.PartProperties.ModernMaterialUtilities.ModernMaterialUtilities.getMaterialFromItemStack;

public class ModernMaterialRenderer implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack itemStack, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED
            || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY
            || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack itemStack, ItemRendererHelper itemRendererHelper) {
        return type == ItemRenderType.ENTITY;
    }

    public void renderPositionCorrection(ItemRenderType type) {

        // Rendering correct positions.
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            if (RenderItem.renderInFrame) {
                GL11.glScalef(0.85F, 0.85F, 0.85F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-0.5D, -0.42D, 0.0D); // todo review this, seems to clip into floor more than normal.
            } else {
                GL11.glTranslated(-0.5D, -0.42D, 0.0D);
            }
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... objects) {

        GL11.glPushMatrix();

        renderPositionCorrection(type);

        ModernMaterial material = getMaterialFromItemStack(itemStack);
        Color materialColor = material.getMaterialColor();
        IIcon partIcon = itemStack.getItem().getIconFromDamage(0); // todo review this later, seems hacky.

        GL11.glColor4f(materialColor.getRed(), materialColor.getGreen(), materialColor.getBlue(), materialColor.getAlpha());

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
