package gregtech.common.render.items;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.google.common.base.Objects;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.render.GTRenderUtil;

public class MetaGeneratedItemRenderer implements IItemRenderer {

    private final IItemRenderer mItemRenderer = new GeneratedItemRenderer();
    private final IItemRenderer mMaterialRenderer = new GeneratedMaterialRenderer();

    private static final Map<RendererKey, IItemRenderer> specialRenderers = new HashMap<>();

    public MetaGeneratedItemRenderer() {}

    public <T extends Item & IGT_ItemWithMaterialRenderer> void registerItem(T item) {
        MinecraftForgeClient.registerItemRenderer(item, this);
    }

    public static void registerSpecialRenderer(ItemList item, IItemRenderer renderer) {
        specialRenderers.put(
            new RendererKey(
                Item.getIdFromItem(item.getItem()),
                (short) item.getInternalStack_unsafe()
                    .getItemDamage()),
            renderer);
    }

    @Override
    public boolean handleRenderType(ItemStack aStack, ItemRenderType aType) {
        if ((GTUtility.isStackInvalid(aStack)) || (aStack.getItemDamage() < 0)
            || !(aStack.getItem() instanceof IGT_ItemWithMaterialRenderer)
            || !((IGT_ItemWithMaterialRenderer) aStack.getItem()).shouldUseCustomRenderer(aStack.getItemDamage())) {
            return false;
        }
        return getRendererForItemStack(aStack).handleRenderType(aStack, aType);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType aType, ItemStack aStack, ItemRendererHelper aHelper) {
        if (GTUtility.isStackInvalid(aStack)) {
            return false;
        }
        return getRendererForItemStack(aStack).shouldUseRenderHelper(aType, aStack, aHelper);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        GTRenderUtil.applyStandardItemTransform(type);

        IItemRenderer itemRenderer = getRendererForItemStack(aStack);
        itemRenderer.renderItem(type, aStack, data);
    }

    private IItemRenderer getRendererForItemStack(ItemStack aStack) {
        final short aMetaData = (short) aStack.getItemDamage();
        final RendererKey key = new RendererKey(Item.getIdFromItem(aStack.getItem()), aMetaData);

        if (specialRenderers.containsKey(key)) {
            return specialRenderers.get(key);
        }

        IGT_ItemWithMaterialRenderer aItem = (IGT_ItemWithMaterialRenderer) aStack.getItem();

        if (aItem != null && aItem.allowMaterialRenderer(aMetaData)) {
            IItemRenderer aMaterialRenderer = aItem.getMaterialRenderer(aMetaData);

            // Handle fluid rendering.
            if (aMaterialRenderer == null) {
                ItemData itemData = GTOreDictUnificator.getAssociation(aStack);
                if (itemData != null) {
                    if (itemData.mMaterial != null && itemData.mMaterial.mMaterial.renderer != null) {
                        aMaterialRenderer = itemData.mMaterial.mMaterial.renderer;
                    }
                }
            }

            return aMaterialRenderer != null ? aMaterialRenderer : mMaterialRenderer;
        }

        return mItemRenderer;
    }

    @SuppressWarnings("ClassCanBeRecord")
    private static class RendererKey {

        private final int id;
        private final short metadata;

        private RendererKey(final int id, final short metadata) {
            this.id = id;
            this.metadata = metadata;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final RendererKey that = (RendererKey) o;
            return id == that.id && metadata == that.metadata;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(id, metadata);
        }
    }
}
