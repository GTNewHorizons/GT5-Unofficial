package gregtech.common.render.items;

import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeFluidInContainer;
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
/// pass and folds the base/overlay distinction into that one pass, matching how [GeneratedMaterialRenderer]
/// already treats a single non-multi-pass item. Everything between the two reaches the renderer through
/// [#getIconContainer].
///
/// [GeneratedMaterialRenderer] wants that pair the same way round every time: [#getIcon] is the layer it tints and
/// clips the contained fluid to, and [#getOverlayIcon] is the untinted layer it draws last. A plain shape item
/// holds them in that order, but a [ShapeFluidInContainer] is the other way round -- its pass 0 is the untinted
/// container and its pass 1 the tinted fill -- so this swaps them back. Handing the container over as the tinted
/// layer instead lets the contained fluid's depth test spread it across the whole cell rather than confining it
/// to the fill.
public final class ShapeItemMaterialRenderAdapter implements IGT_ItemWithMaterialRenderer {

    private final ShapeItem item;
    private final boolean fluidContainer;

    public ShapeItemMaterialRenderAdapter(ShapeItem item) {
        this.item = item;
        this.fluidContainer = item instanceof ShapeFluidInContainer;
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
        return item.getIconFromDamageForRenderPass(aMetaData, tintedPass());
    }

    @Override
    public IIcon getOverlayIcon(int aMetaData, int pass) {
        if (fluidContainer) return item.getIconFromDamageForRenderPass(aMetaData, 0);
        Material material = materialForDamage(aMetaData);
        return material != null ? item.getMaterialOverlayIcon(material) : null;
    }

    @Override
    public IIconContainer getIconContainer(int aMetaData) {
        Material material = materialForDamage(aMetaData);
        return material != null ? new LayerStack(item, material, fluidContainer) : null;
    }

    /// The render pass holding the tinted layer: a fluid container's fill, or a plain shape item's base icon.
    private int tintedPass() {
        return fluidContainer ? 1 : 0;
    }

    private static Material materialForDamage(int damage) {
        return MaterialLibAPI.getMaterialByIndex(damage);
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        return GTUtil.getRGBaArray(item.getColorFromItemStack(aStack, tintedPass()));
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

    /// One material's icon layer stack on the adapted [ShapeItem], ordered the way the adapter presents it: the
    /// tinted layers first and, for a [ShapeFluidInContainer], the untinted empty-container base last. Layer 0
    /// reports the shape's own tint, which [IIconContainer#isUsingColorModulation] leaves to the consumer, so a
    /// fluid container's fill tint arrives through [ShapeItemMaterialRenderAdapter#getRGBa] instead.
    private static final class LayerStack implements IIconContainer {

        private final ShapeItem item;
        private final Material material;
        private final boolean fluidContainer;

        LayerStack(ShapeItem item, Material material, boolean fluidContainer) {
            this.item = item;
            this.material = material;
            this.fluidContainer = fluidContainer;
        }

        @Override
        public IIcon getIcon() {
            return getLayerIcon(0);
        }

        @Override
        public IIcon getOverlayIcon() {
            return fluidContainer ? emptyContainerIcon() : item.getMaterialOverlayIcon(material);
        }

        @Override
        public int getIconPasses() {
            int layers = item.getMaterialLayerCount(material);
            return fluidContainer ? layers + 1 : layers;
        }

        @Override
        public IIcon getLayerIcon(int layer) {
            if (isEmptyContainerLayer(layer)) return emptyContainerIcon();
            return item.getMaterialLayerIcon(material, layer);
        }

        @Override
        public short[] getIconColor(int layer) {
            if (isEmptyContainerLayer(layer)) return UNCOLORED_RGBA;
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

        private boolean isEmptyContainerLayer(int layer) {
            return fluidContainer && layer == item.getMaterialLayerCount(material);
        }

        private IIcon emptyContainerIcon() {
            return item.getIconFromDamageForRenderPass(material.getIndex(), 0);
        }
    }
}
