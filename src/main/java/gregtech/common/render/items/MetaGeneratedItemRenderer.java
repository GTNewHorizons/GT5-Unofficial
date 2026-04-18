package gregtech.common.render.items;

import java.util.Objects;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public final class MetaGeneratedItemRenderer implements IItemRenderer {

    private final Long2ObjectOpenHashMap<IItemRenderer> specialRenderers = new Long2ObjectOpenHashMap<>();
    private final IItemRenderer mItemRenderer = new GeneratedItemRenderer();
    private final IItemRenderer mMaterialRenderer = new GeneratedMaterialRenderer();

    public <T extends Item & IGT_ItemWithMaterialRenderer> void registerItem(T item) {
        MinecraftForgeClient.registerItemRenderer(item, this);
    }

    public void registerSpecialRenderer(ItemList item, IItemRenderer renderer) {
        registerSpecialRenderer(item.getItem(), item.getInternalStack_unsafe(), renderer);
    }

    public void registerSpecialRenderer(Item aItem, ItemStack aStack, IItemRenderer renderer) {
        specialRenderers.put(pack(aItem, aStack.getItemDamage()), renderer);
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
        ItemRenderUtil.applyStandardItemTransform(type);
        IItemRenderer itemRenderer = getRendererForItemStack(aStack);
        itemRenderer.renderItem(type, aStack, data);
    }

    private IItemRenderer getRendererForItemStack(ItemStack aStack) {
        final int aMetaData = aStack.getItemDamage();
        final IItemRenderer renderer = specialRenderers.get(pack(aStack.getItem(), aMetaData));
        if (renderer != null) {
            return renderer;
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

    private static long pack(Item item, int meta) {
        final int a = Objects.hashCode(item);
        return (long) a << 32 | meta & 0xFFFFFFFFL;
    }
}
