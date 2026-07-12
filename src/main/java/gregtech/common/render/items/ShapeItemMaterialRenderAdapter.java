package gregtech.common.render.items;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import com.ruling_0.materiallib.api.ShapeItem;

import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;

/// Adapts a MaterialLib {@link ShapeItem} to the legacy {@link IGT_ItemWithMaterialRenderer} surface the
/// {@link GeneratedMaterialRenderer} family reads from, so a material's special renderer (assigned through
/// {@code Materials.<field>.renderer}) keeps working once that material's item shapes cut over from
/// {@code MetaGeneratedItem} to MaterialLib. See {@link IGT_ItemWithMaterialRenderer#resolve} for construction.
///
/// A shape item has no per-pass icon list -- only a base icon and an optional overlay -- so this always reports
/// a single render pass and folds the base/overlay distinction into that one pass, matching how
/// {@link GeneratedMaterialRenderer} already treats a single non-multi-pass item.
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
        return item.getIconFromDamage(aMetaData);
    }

    @Override
    public IIcon getOverlayIcon(int aMetaData, int pass) {
        return item.getIconFromDamageForRenderPass(aMetaData, 1);
    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        int argb = item.getColorFromItemStack(aStack, 0);
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
