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
/// [GeneratedMaterialRenderer] family reads from, letting a material's special renderer (assigned through
/// [gregtech.api.material.MaterialRenderers#recordRenderer]) draw shape-backed items. See
/// [IGT_ItemWithMaterialRenderer#resolve] for construction.
///
/// A shape item carries one icon layer stack rather than a per-pass icon list, so this reports a single render
/// pass and hands the stack to the renderer through [#getIconContainer].
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

    /// The number of leading render passes a fluid container spends on its base, ahead of the fill stack.
    private static int basePassOffset(ShapeItem item) {
        return item instanceof ShapeFluidInContainer ? 1 : 0;
    }

    /// The modulation color of the first fill pass, past a fluid container's leading base pass.
    @Override
    public short[] getRGBa(ItemStack aStack) {
        return GTUtil.getRGBaArray(item.getColorFromItemStack(aStack, basePassOffset(item)));
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

    /// One material's icon layer stack on the adapted [ShapeItem]: the fill layers first, with a fluid container's
    /// untinted base as the trailing layer, so layer 0 stays the silhouette the [GeneratedMaterialRenderer] family
    /// depth-clips the contained fluid to. The item itself draws the base as its leading render pass;
    /// [#getLayerIcon] translates between the two orders.
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

        /// Always null; the stack's layers, its trailing overlay included, come through [#getLayerIcon].
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
            int offset = basePassOffset(item);
            int fillLayers = getIconPasses() - offset;
            int pass = layer < fillLayers ? layer + offset : layer - fillLayers;
            return item.getIconFromDamageForRenderPass(material.getIndex(), pass);
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
