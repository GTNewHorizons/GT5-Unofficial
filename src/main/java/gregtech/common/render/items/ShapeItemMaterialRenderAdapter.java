package gregtech.common.render.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.ruling_0.materiallib.api.ShapeFluidInContainer;
import com.ruling_0.materiallib.api.ShapeItem;

import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;

/// Adapts a MaterialLib [ShapeItem] to the [IGT_ItemWithMaterialRenderer] surface the
/// [GeneratedMaterialRenderer] family reads from, so a material's special renderer (assigned through
/// [gregtech.api.material.MaterialRenderers#recordRenderer]) works on shape-backed items. See
/// [IGT_ItemWithMaterialRenderer#resolve] for construction.
///
/// A shape item has no per-pass icon list -- only a base icon and an optional overlay -- so this always reports
/// a single render pass and folds the base/overlay distinction into that one pass, matching how
/// [GeneratedMaterialRenderer] already treats a single non-multi-pass item.
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
        return item.getIconFromDamageForRenderPass(aMetaData, fluidContainer ? 0 : 1);
    }

    /// The render pass holding the tinted layer: a fluid container's fill, or a plain shape item's base icon.
    private int tintedPass() {
        return fluidContainer ? 1 : 0;
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        int argb = item.getColorFromItemStack(aStack, tintedPass());
        return new short[] { (short) (argb >> 16 & 0xFF), (short) (argb >> 8 & 0xFF), (short) (argb & 0xFF),
            (short) (argb >>> 24 & 0xFF) };
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
}
