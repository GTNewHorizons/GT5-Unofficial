package gregtech.client.iconContainers.items;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.GTValues.UNCOLORED_RGBA;

import net.minecraft.util.IIcon;

import com.ruling_0.materiallib.api.IconSet;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeItem;
import com.ruling_0.materiallib.api.ShapeRegistry;

import gregtech.api.enums.Textures.InvisibleIcon;
import gregtech.api.material.GTMaterialIconSets;
import gregtech.api.util.GTUtil;

/// One material's item-atlas icon from MaterialLib.
///
/// The art comes from the [GTMaterialIconSets] icon set of that name, or from the item shape of that name if
/// there is no such set. An icon set binds every material, while a shape only binds the materials it generates
/// for. The source is resolved on first draw, but the [IIcon] is fetched every time since MaterialLib rebinds
/// icons on resource reload. Obtain instances from [gregtech.api.material.GTMaterialIcons].
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

    /// Null, since every layer comes through [#getLayerIcon].
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
            GT_FML_LOGGER.error(
                "No item shape or icon set is named {}, asked for by material {}; it will render nothing",
                name,
                material.getKey());
        }
    }
}
