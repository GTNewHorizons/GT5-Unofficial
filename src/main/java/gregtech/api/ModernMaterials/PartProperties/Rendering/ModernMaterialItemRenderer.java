package gregtech.api.ModernMaterials.PartProperties.Rendering;

import static gregtech.api.ModernMaterials.PartProperties.ModernMaterialUtilities.ModernMaterialUtilities.getMaterialFromItemStack;
import static org.lwjgl.opengl.GL11.GL_CURRENT_BIT;

import java.awt.*;

import gregtech.api.ModernMaterials.PartProperties.Textures.TextureType;
import gregtech.api.ModernMaterials.PartsClasses.MaterialPart;
import gregtech.api.ModernMaterials.PartsClasses.PartsEnum;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.common.render.GT_RenderUtil;
import singulariteam.eternalsingularity.render.CosmicRenderStuffs;

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
        ModernMaterial material = getMaterialFromItemStack(itemStack);
        if (material == null) {
            return;
        }

        Color materialColor = material.getColor();
        MaterialPart item = (MaterialPart) itemStack.getItem();
        if (item == null) {
            return;
        }
        PartsEnum partsEnum = item.getPart();

        GL11.glPushMatrix();

        // Enable transparency.
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Adjusts the items position to render correctly in world.
        renderPositionCorrection(type);

        // Iterate over the items layers and render them.
        for (IconWrapper iconWrapper : TextureType.Metallic.getTextureArray(partsEnum)) {

            GL11.glPushAttrib(GL_CURRENT_BIT);

            // Determines if the layer needs colouring.
            if (iconWrapper.isColoured) {
                GL11.glColor3f(
                    materialColor.getRed(),
                    materialColor.getGreen(),
                    materialColor.getBlue()
                );
            }

            renderLayer(iconWrapper.icon, type);

            GL11.glPopAttrib();

        }

        GL11.glPopMatrix();
    }

    private void renderLayer(IIcon icon, ItemRenderType renderType) {
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
