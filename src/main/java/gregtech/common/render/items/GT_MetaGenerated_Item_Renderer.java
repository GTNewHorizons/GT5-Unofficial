package gregtech.common.render.items;

import static gregtech.api.util.GT_OreDictUnificator.getAssociation;

import gregtech.api.enums.Materials;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_Utility;
import java.util.Objects;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;

public class GT_MetaGenerated_Item_Renderer implements IItemRenderer {

    private final IItemRenderer mItemRenderer = new GT_GeneratedItem_Renderer();
    ;
    private final IItemRenderer mMaterialRenderer = new GT_GeneratedMaterial_Renderer();

    public GT_MetaGenerated_Item_Renderer() {
        for (GT_MetaGenerated_Item item : GT_MetaGenerated_Item.sInstances.values()) {
            MinecraftForgeClient.registerItemRenderer(item, this);
        }
    }

    @Override
    public boolean handleRenderType(ItemStack aStack, ItemRenderType aType) {
        if ((GT_Utility.isStackInvalid(aStack)) || (aStack.getItemDamage() < 0)) {
            return false;
        }
        return getRendererForItemStack(aStack).handleRenderType(aStack, aType);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType aType, ItemStack aStack, ItemRendererHelper aHelper) {
        if (GT_Utility.isStackInvalid(aStack)) {
            return false;
        }
        return getRendererForItemStack(aStack).shouldUseRenderHelper(aType, aStack, aHelper);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        if (type == IItemRenderer.ItemRenderType.ENTITY) {
            if (RenderItem.renderInFrame) {
                GL11.glScalef(0.85F, 0.85F, 0.85F);
                GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslated(-0.5D, -0.42D, 0.0D);
            } else {
                GL11.glTranslated(-0.5D, -0.42D, 0.0D);
            }
        }

        IItemRenderer itemRenderer = getRendererForItemStack(aStack);
        itemRenderer.renderItem(type, aStack, data);
    }

    private IItemRenderer getRendererForItemStack(ItemStack aStack) {
        short aMetaData = (short) aStack.getItemDamage();
        GT_MetaGenerated_Item aItem = (GT_MetaGenerated_Item) aStack.getItem();

        if (aMetaData < aItem.mOffset) {
            IItemRenderer aMaterialRenderer = aItem.getMaterialRenderer(aMetaData);

            // Handle fluid rendering.
            if (aMaterialRenderer == null) {
                Materials material = Objects.requireNonNull(getAssociation(aStack)).mMaterial.mMaterial;
                if (material.renderer != null) {
                    aMaterialRenderer = material.renderer;
                }
            }

            return aMaterialRenderer != null ? aMaterialRenderer : mMaterialRenderer;
        }

        return mItemRenderer;
    }
}
