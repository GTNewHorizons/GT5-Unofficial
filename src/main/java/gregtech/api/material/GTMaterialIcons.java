package gregtech.api.material;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeRegistry;

import gregtech.GTLoggers;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.client.ResourceUtils;
import gregtech.client.iconContainers.blocks.MLBlockIconContainer;
import gregtech.client.iconContainers.items.MLItemIconContainer;

/// Looks up the [IIconContainer] GregTech's own renderers draw a material's art through.
///
/// Tools, pipes, ores and covers pick their art per material at draw time, so they cannot reach it through a
/// shape's item or block. Each container resolves its art from the MaterialLib shape or [GTMaterialIconSets] icon
/// set of the same name, with the same texture-set handling every shape gets.
///
/// Containers are interned per name and material, since [gregtech.api.render.TextureFactory] deduplicates
/// textures by container identity.
public final class GTMaterialIcons {

    private GTMaterialIcons() {}

    private static final Map<String, Map<Material, IIconContainer>> itemContainers = new ConcurrentHashMap<>();
    private static final Map<String, Map<Material, IIconContainer>> blockContainers = new ConcurrentHashMap<>();
    private static final Map<String, Map<Material, IIconContainer>> oreContainers = new ConcurrentHashMap<>();

    /// `material`'s item-atlas art filed under `name`.
    public static IIconContainer item(String name, Material material) {
        return intern(itemContainers, name, material, () -> new MLItemIconContainer(name, material));
    }

    /// `material`'s item-atlas art for `prefix`, filed under the prefix's own name.
    public static IIconContainer item(OrePrefixes prefix, Material material) {
        return item(prefix.name(), material);
    }

    /// `material`'s block-atlas art filed under `name`.
    public static IIconContainer block(String name, Material material) {
        return intern(blockContainers, name, material, () -> new MLBlockIconContainer(name, null, material, 0));
    }

    /// `material`'s ore art from the `ore` or `oreSmall` shape named `name`, in the shape's
    /// [OreShapes#ICON_VARIANT] variant. For ore art drawn with no stone type behind it.
    public static IIconContainer oreBlock(String name, Material material) {
        return oreBlock(name, OreShapes.ICON_VARIANT, material);
    }

    /// `material`'s ore art from the `variant` backing block of the `ore` or `oreSmall` shape named `name`, drawn
    /// in render pass 1 over a stone background the caller composites underneath.
    ///
    /// A texture set shipping `<name>_<variant>.png` reskins that one stone type. Every other variant uses the
    /// set's shared `<name>.png`. `variant` must be one the shape declares (see [OreShapes#iconVariantOf]).
    public static IIconContainer oreBlock(String name, String variant, Material material) {
        return intern(
            oreContainers,
            name + "_" + variant,
            material,
            () -> new MLBlockIconContainer(name, variant, material, 1));
    }

    /// Whether any item art exists under `prefix`'s name. Safe to call on a dedicated server. False for a name
    /// belonging to a block or fluid shape.
    public static boolean hasItemIcon(OrePrefixes prefix) {
        String name = prefix.name();
        return GTMaterialIconSets.ITEM_NAMES.contains(name) || ShapeRegistry.instance()
            .getItemShape(name) != null;
    }

    /// The item-atlas icon identifier for `name` in the texture set `setName`, falling back to the `NONE` set when
    /// that set ships no such file. For items that register material art straight into the atlas.
    public static String itemIconPath(String setName, String name) {
        String path = "gregtech:materials/" + setName + "/" + name;
        if (itemTextureExists(path)) return path;
        String fallback = "gregtech:materials/NONE/" + name;
        if (!itemTextureExists(fallback)) {
            GTLoggers.GT_FML_LOGGER.warn("No item texture for icon set {} under the name {}", setName, name);
        }
        return fallback;
    }

    /// Whether the item atlas ships a file for an icon identifier.
    public static boolean itemTextureExists(String path) {
        int separator = path.indexOf(':');
        String domain = separator < 0 ? GregTech.resourceDomain : path.substring(0, separator);
        String key = separator < 0 ? path : path.substring(separator + 1);
        return ResourceUtils.resourceExists(ResourceUtils.getCompleteItemTextureResourceLocation(domain, key));
    }

    private static IIconContainer intern(Map<String, Map<Material, IIconContainer>> containers, String name,
        Material material, Supplier<IIconContainer> create) {
        return containers.computeIfAbsent(name, key -> new ConcurrentHashMap<>())
            .computeIfAbsent(material, key -> create.get());
    }
}
