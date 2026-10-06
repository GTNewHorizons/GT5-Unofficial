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

/// Adapts a MaterialLib [ShapeItem] to [IGT_ItemWithMaterialRenderer], so a material's
/// [GeneratedMaterialRenderer] can draw shape items. Obtain instances through
/// [IGT_ItemWithMaterialRenderer#resolve].
///
/// Reports a single render pass and hands the whole layer stack to the renderer through [#getIconContainer].
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

    /// The number of leading render passes spent on a fluid container's base.
    private static int basePassOffset(ShapeItem item) {
        return item instanceof ShapeFluidInContainer ? 1 : 0;
    }

    /// The color of the first fill pass.
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

    /// One material's icon layers on the adapted [ShapeItem]. Fill layers come first and a fluid container's base
    /// comes last, so layer 0 is the silhouette [GeneratedMaterialRenderer] clips fluid to. The item draws the base
    /// first, and [#getLayerIcon] translates between the two orders.
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

        /// Null, since every layer comes through [#getLayerIcon].
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
