package gregtech.common.render.items;

import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeItem;

import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTUtil;

/// Adapts a MaterialLib [ShapeItem] to the [IGT_ItemWithMaterialRenderer] surface the
/// [GeneratedMaterialRenderer] family reads from, so a material's special renderer (assigned through
/// [gregtech.api.material.MaterialRenderers#recordRenderer]) works on shape-backed items. See
/// [IGT_ItemWithMaterialRenderer#resolve] for construction.
///
/// A shape item has no per-pass icon list -- only one icon layer stack -- so this always reports a single render
/// pass and hands the stack to the renderer through [#getIconContainer]. The stack is the item's own render
/// passes in the item's own order, a fluid container's untinted base included as its final pass.
public final class ShapeItemMaterialRenderAdapter implements IGT_ItemWithMaterialRenderer {

    private final ShapeItem item;

    public ShapeItemMaterialRenderAdapter(ShapeItem item) {
        this.item = item;
    }

    @Override
    public boolean shouldUseCustomRenderer(int aMetaData) {
        return true;
    }

    @Override
    public GeneratedMaterialRenderer getMaterialRenderer(int aMetaData) {
        return null;
    }

    @Override
    public boolean allowMaterialRenderer(int aMetaData) {
        return true;
    }

    @Override
    public IIcon getIcon(int aMetaData, int pass) {
        return item.getIconFromDamageForRenderPass(aMetaData, 0);
    }

    @Override
    public IIcon getOverlayIcon(int aMetaData, int pass) {
        return null;
    }

    @Override
    public IIconContainer getIconContainer(int aMetaData) {
        Material material = materialForDamage(aMetaData);
        return material != null ? new LayerStack(item, material) : null;
    }

    private static Material materialForDamage(int damage) {
        return MaterialLibAPI.getMaterialByIndex(damage);
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        return GTUtil.getRGBaArray(item.getColorFromItemStack(aStack, 0));
    }

    @Override
    public boolean hasOverrideIcon(ItemStack stack) {
        Material material = materialForDamage(stack.getItemDamage());
        return material != null && item.hasOverrideIcon(material);
    }

    @Override
    public int getSpriteNumber() {
        return item.getSpriteNumber();
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @Override
    public int getRenderPasses(int metadata) {
        return 1;
    }

    /// One material's icon layer stack on the adapted [ShapeItem]: the item's render passes, forwarded in the
    /// item's own order. Layer 0 reports the shape's own tint, which [IIconContainer#isUsingColorModulation]
    /// leaves to the consumer, so it arrives through [ShapeItemMaterialRenderAdapter#getRGBa] instead; a layer
    /// past the material's own stack is a fluid container's untinted base.
    private static final class LayerStack implements IIconContainer {

        private final ShapeItem item;
        private final Material material;

        LayerStack(ShapeItem item, Material material) {
            this.item = item;
            this.material = material;
        }

        @Override
        public IIcon getIcon() {
            return getLayerIcon(0);
        }

        /// Null: the stack's layers, its trailing overlay included, come through [#getLayerIcon].
        @Override
        public IIcon getOverlayIcon() {
            return null;
        }

        @Override
        public int getIconPasses() {
            return item.getRenderPasses(material.getIndex());
        }

        @Override
        public IIcon getLayerIcon(int layer) {
            return item.getIconFromDamageForRenderPass(material.getIndex(), layer);
        }

        @Override
        public short[] getIconColor(int layer) {
            if (layer >= item.getMaterialLayerCount(material)) return UNCOLORED_RGBA;
            return GTUtil.getRGBaArray(item.getMaterialLayerColor(material, layer));
        }

        @Override
        public boolean hasOverrideIcon() {
            return item.hasOverrideIcon(material);
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationItemsTexture;
        }
    }
}
