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

/// One material's item-atlas icon, drawn from MaterialLib's texture-set resolution rather than owned outright.
///
/// The art is either a [GTMaterialIconSets] icon set's or, where GregTech declared none for the name, the item
/// shape of that name. The set wins because it binds every registered material, where a shape only binds the
/// materials that generate it. Which of the two applies is decided on the first draw and remembered; the
/// [IIcon] itself is fetched through that handle every time, since MaterialLib rebinds icons on every resource
/// reload. Obtain instances from [gregtech.api.material.GTMaterialIcons], which interns them so the textures
/// built from them dedupe.
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

    /// Null: the stack's layers, its trailing overlay included, come through [#getLayerIcon].
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
