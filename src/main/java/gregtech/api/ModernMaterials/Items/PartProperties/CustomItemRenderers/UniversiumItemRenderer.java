package gregtech.api.ModernMaterials.Items.PartProperties.CustomItemRenderers;

import fox.spiteful.avaritia.render.CosmicRenderShenanigans;
import gregtech.api.ModernMaterials.Items.PartProperties.IconWrapper;
import gregtech.api.ModernMaterials.Items.PartProperties.ModernMaterialItemRenderer;
import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.Items.PartsClasses.CustomPartInfo;
import gregtech.api.ModernMaterials.Items.PartsClasses.ItemsEnum;
import gregtech.api.ModernMaterials.Items.PartsClasses.MaterialPart;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.GL_CURRENT_BIT;

public class UniversiumItemRenderer implements IItemRenderer {
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
    public void renderItem(ItemRenderType type, ItemStack itemStack, Object... data) {

        ModernMaterial material = ModernMaterialItemRenderer.getMaterialFromItemStack(itemStack);
        MaterialPart materialPart = (MaterialPart) itemStack.getItem();
        Color materialColor = material.getColor();

        ItemsEnum partsEnum = materialPart.getPart();
        CustomPartInfo customPartInfo = material.getCustomPartInfo(partsEnum);

        GL11.glPushMatrix();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderPositionCorrection(type);

        // Iterate over the items layers and render them.
        for (IconWrapper iconWrapper : customPartInfo.getTextureType().getStandardTextureArray(partsEnum)) {

            GL11.glPushAttrib(GL_CURRENT_BIT);

            CosmicRenderShenanigans.useShader();
            CosmicRenderShenanigans.inventoryRender = true;

            ModernMaterialItemRenderer.renderLayer(iconWrapper.icon, type);

            CosmicRenderShenanigans.releaseShader();

            GL11.glPopAttrib();

        }

        GL11.glPopMatrix();



    }
}
