package gregtech.client.iconContainers.blocks;

import net.minecraft.util.IIcon;

import com.ruling_0.materiallib.api.IconSet;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeBlock;
import com.ruling_0.materiallib.api.ShapeRegistry;

import gregtech.api.enums.Textures.InvisibleIcon;
import gregtech.api.material.GTMaterialIconSets;
import gregtech.api.util.GTLog;

/// One material's block-atlas icon, drawn from MaterialLib's texture-set resolution rather than owned outright;
/// the block-atlas counterpart of
/// [MLItemIconContainer][gregtech.client.iconContainers.items.MLItemIconContainer], with the same
/// icon-set-before-shape resolution and the same resolve-once, read-the-icon-every-time contract.
///
/// Ore art draws in render pass 1 so the transparent parts of the icon show the stone behind it; every other
/// material block icon is opaque and draws in pass 0.
public final class MLBlockIconContainer extends AbstractBlockIconContainer {

    private final String name;
    private final String variant;
    private final Material material;
    private final int renderPass;

    private ShapeBlock shape;
    private IconSet iconSet;
    private boolean resolved;

    /// `variant` names which backing block of a block shape declared with variants to read the icon from, and is
    /// null for a variant-less shape or an icon set.
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

    @Override
    public IIcon getOverlayIcon() {
        resolve();
        if (shape != null) return shape.getMaterialOverlayIcon(material);
        if (iconSet != null) return iconSet.getOverlayIcon(material);
        return null;
    }

    @Override
    public int getRenderIconPass() {
        return renderPass;
    }

    private void resolve() {
        if (resolved) return;
        resolved = true;
        iconSet = GTMaterialIconSets.block(name);
        if (iconSet != null) return;
        ShapeRegistry shapes = ShapeRegistry.instance();
        shape = variant != null ? shapes.getBlockShape(name, variant) : shapes.getBlockShape(name);
        if (shape == null) {
            GTLog.err.println(
                "No block shape or icon set is named " + name
                    + ", asked for by material "
                    + material.getKey()
                    + "; it will render nothing");
        }
    }
}
