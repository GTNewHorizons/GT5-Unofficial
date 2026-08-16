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
/// The art is either a [GTMaterialIconSets] icon set's or, where GregTech declared none for the name, the item
/// shape of that name. The set wins because it binds every registered material, where a shape only binds the
/// materials that generate it. Which of the two applies is decided on the first draw and remembered, but the
/// [IIcon] itself is fetched through that handle every time, because MaterialLib rebinds icons on every resource
/// reload and a cached [IIcon] would render stale atlas coordinates after one. Obtain instances from
/// [gregtech.api.material.GTMaterialIcons], which interns them so the textures built from them dedupe.
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

    @Override
    public boolean hasOverrideIcon() {
        resolve();
        if (shape != null) return shape.hasOverrideIcon(material);
        return iconSet != null && iconSet.hasOverrideIcon(material);
    }

    private void resolve() {
        if (resolved) return;
        resolved = true;
        iconSet = GTMaterialIconSets.item(name);
        if (iconSet != null) return;
        shape = ShapeRegistry.instance()
            .getItemShape(name);
        if (shape == null) {
            GTLog.err.println(
                "No item shape or icon set is named " + name
                    + ", asked for by material "
                    + material.getKey()
                    + "; it will render nothing");
        }
    }
}
