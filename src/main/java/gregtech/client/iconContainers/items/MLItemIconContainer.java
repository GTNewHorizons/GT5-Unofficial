package gregtech.client.iconContainers.items;

import net.minecraft.util.IIcon;

import com.ruling_0.materiallib.api.IconSet;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeItem;
import com.ruling_0.materiallib.api.ShapeRegistry;

import gregtech.api.enums.Textures.InvisibleIcon;
import gregtech.api.material.GTMaterialIconSets;
import gregtech.api.util.GTLog;

/// One material's item-atlas icon, drawn from MaterialLib's texture-set resolution rather than owned outright.
///
/// The art is either an item shape's own icon or a [IconSet]'s; which of the two is decided on the first draw and
/// remembered, but the [IIcon] itself is fetched through that handle every time, because MaterialLib rebinds icons
/// on every resource reload and a cached [IIcon] would render the stale atlas coordinates after one. Obtain
/// instances from [gregtech.api.material.GTMaterialIcons], which interns them so the textures built from them
/// dedupe.
public final class MLItemIconContainer extends AbstractItemIconContainer {

    private final String name;
    private final Material material;

    private ShapeItem shape;
    private IconSet iconSet;
    private boolean resolved;

    public MLItemIconContainer(String name, Material material) {
        this.name = name;
        this.material = material;
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

    private void resolve() {
        if (resolved) return;
        resolved = true;
        shape = ShapeRegistry.instance()
            .getItemShape(name);
        if (shape != null) return;
        iconSet = GTMaterialIconSets.item(name);
        if (iconSet == null) {
            GTLog.err.println(
                "No item shape or icon set is named " + name
                    + ", asked for by material "
                    + material.getKey()
                    + "; it will render nothing");
        }
    }
}
