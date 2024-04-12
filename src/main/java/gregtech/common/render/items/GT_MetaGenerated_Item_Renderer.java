package gregtech.common.render.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.render.GT_RenderUtil;

public class GT_MetaGenerated_Item_Renderer implements IItemRenderer {

    private final IItemRenderer mItemRenderer = new GT_GeneratedItem_Renderer();
    private final IItemRenderer mMaterialRenderer = new GT_GeneratedMaterial_Renderer();

    private final IItemRenderer mDataStickRenderer = new GT_DataStick_Renderer();

    public GT_MetaGenerated_Item_Renderer() {}

    public <T extends Item & IGT_ItemWithMaterialRenderer> void registerItem(T item) {
        MinecraftForgeClient.registerItemRenderer(item, this);
    }

    @Override
    public boolean handleRenderType(ItemStack aStack, ItemRenderType aType) {
        if ((GT_Utility.isStackInvalid(aStack)) || (aStack.getItemDamage() < 0)
            || !(aStack.getItem() instanceof IGT_ItemWithMaterialRenderer)
            || !((IGT_ItemWithMaterialRenderer) aStack.getItem()).shouldUseCustomRenderer(aStack.getItemDamage())) {
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
        GT_RenderUtil.applyStandardItemTransform(type);

        IItemRenderer itemRenderer = getRendererForItemStack(aStack);
        itemRenderer.renderItem(type, aStack, data);
    }

    private IItemRenderer getRendererForItemStack(ItemStack aStack) {
        short aMetaData = (short) aStack.getItemDamage();
        IGT_ItemWithMaterialRenderer aItem = (IGT_ItemWithMaterialRenderer) aStack.getItem();

        if (aItem != null && aItem.allowMaterialRenderer(aMetaData)) {
            IItemRenderer aMaterialRenderer = aItem.getMaterialRenderer(aMetaData);

            // Handle fluid rendering.
            if (aMaterialRenderer == null) {
                ItemData itemData = GT_OreDictUnificator.getAssociation(aStack);
                if (itemData != null) {
                    Materials material = itemData.mMaterial.mMaterial;
                    if (material.renderer != null) {
                        aMaterialRenderer = material.renderer;
                    }
                }
            }

            return aMaterialRenderer != null ? aMaterialRenderer : mMaterialRenderer;
        }

        // handle data stick
        if (aStack.getItem() == ItemList.Tool_DataStick.getItem() && aStack.hasTagCompound()
            && aStack.getTagCompound()
                .hasKey("output")) {
            return mDataStickRenderer;
        }

        return mItemRenderer;
    }
}
