package gregtech.client.iconContainers.blocks;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;

import net.minecraft.util.IIcon;

import com.ruling_0.materiallib.api.IconSet;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeBlock;
import com.ruling_0.materiallib.api.ShapeRegistry;

import gregtech.api.enums.Textures.InvisibleIcon;
import gregtech.api.material.GTMaterialIconSets;
import gregtech.api.util.GTUtil;

/// One material's block-atlas icon from MaterialLib. The block counterpart of
/// [MLItemIconContainer][gregtech.client.iconContainers.items.MLItemIconContainer], resolving the same way.
///
/// Ore art draws in render pass 1 so the stone shows through it. Other material block icons draw in pass 0.
public final class MLBlockIconContainer extends AbstractBlockIconContainer {

    private final String name;
    private final String variant;
    private final Material material;
    private final int renderPass;

    private ShapeBlock shape;
    private IconSet iconSet;
    private boolean resolved;

    /// `variant` picks the backing block of a variant block shape, and is null otherwise.
    public MLBlockIconContainer(String name, String variant, Material material, int renderPass) {
        this.name = name;
        this.variant = variant;
        this.material = material;
        this.renderPass = renderPass;
    }

    @Override
    public IIcon getIcon() {
        resolve();
        if (shape != null) return shape.getMaterialIcon(material);
        if (iconSet != null) return iconSet.getIcon(material);
        return InvisibleIcon.INVISIBLE_ICON;
    }

    /// Returns null. Every layer comes through [#getLayerIcon].
    @Override
    public IIcon getOverlayIcon() {
        return null;
    }

    @Override
    public int getIconPasses() {
        resolve();
        if (shape != null) return shape.getMaterialLayerCount(material);
        if (iconSet != null) return iconSet.getLayerCount(material);
        return 1;
    }

    @Override
    public IIcon getLayerIcon(int layer) {
        resolve();
        if (shape != null) return shape.getMaterialLayerIcon(material, layer);
        if (iconSet != null) return iconSet.getLayerIcon(material, layer);
        return InvisibleIcon.INVISIBLE_ICON;
    }

    @Override
    public short[] getIconColor(int layer) {
        resolve();
        if (shape != null) return GTUtil.getRGBaArray(shape.getMaterialLayerColor(material, layer));
        if (iconSet != null) return GTUtil.getRGBaArray(iconSet.getLayerColor(material, layer));
        return UNCOLORED_RGBA;
    }

    @Override
    public int getRenderIconPass() {
        return renderPass;
    }

    @Override
    public boolean hasOverrideIcon() {
        resolve();
        if (shape != null) return shape.hasOverrideIcon(material);
        return iconSet != null && iconSet.hasOverrideIcon(material);
    }

    private void resolve() {
        if (resolved) return;
        resolved = true;
        iconSet = GTMaterialIconSets.block(name);
        if (iconSet != null) return;
        ShapeRegistry shapes = ShapeRegistry.instance();
        shape = variant != null ? shapes.getBlockShape(name, variant) : shapes.getBlockShape(name);
        if (shape == null) {
            GT_FML_LOGGER.error(
                "No block shape or icon set is named {}, asked for by material {}; it will render nothing",
                name,
                material.getKey());
        }
    }
}
