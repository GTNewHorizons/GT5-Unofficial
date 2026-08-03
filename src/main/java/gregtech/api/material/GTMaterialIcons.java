package gregtech.api.material;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.ShapeRegistry;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.client.iconContainers.blocks.MLBlockIconContainer;
import gregtech.client.iconContainers.items.MLItemIconContainer;

/// The lookup from a material and a texture name to the [IIconContainer] GregTech's own renderers draw it through.
///
/// Tools, pipes, ores and covers pick their art per material at draw time from an MTE field or a stack's NBT, so
/// they cannot reach it through a shape's item or block the way a generated item does. Each container handed out
/// here resolves its art from the MaterialLib shape or the [GTMaterialIconSets] icon set of the same name, which
/// gives GregTech's art the same texture-set chain, fallbacks, unification alternatives, and `_OVERLAY` handling
/// every shape gets.
///
/// Containers are interned per name and material because renderers ask for them once per frame and
/// [gregtech.api.render.TextureFactory] deduplicates the textures it builds by container identity. Materials are
/// singletons, so they key by reference and no numeric index enters the cache.
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

    /// `material`'s ore art from the `ore` or `oreSmall` shape named `name`, drawn in render pass 1 so the stone
    /// background the caller composites underneath shows through; see [OreShapes#ICON_VARIANT].
    public static IIconContainer oreBlock(String name, Material material) {
        return intern(
            oreContainers,
            name,
            material,
            () -> new MLBlockIconContainer(name, OreShapes.ICON_VARIANT, material, 1));
    }

    /// Whether any item art exists under `prefix`'s name, replacing the legacy "does this prefix carry a texture
    /// index" gate. Answerable on a dedicated server: the shape registry is common data and the icon-set names are
    /// plain strings. Fails when the name belongs to a block or fluid shape, which would mean a caller asked for
    /// the item form of art that has none.
    public static boolean hasItemIcon(OrePrefixes prefix) {
        String name = prefix.name();
        return GTMaterialIconSets.ITEM_NAMES.contains(name) || ShapeRegistry.instance()
            .getItemShape(name) != null;
    }

    private static IIconContainer intern(Map<String, Map<Material, IIconContainer>> containers, String name,
        Material material, Supplier<IIconContainer> create) {
        return containers.computeIfAbsent(name, key -> new ConcurrentHashMap<>())
            .computeIfAbsent(material, key -> create.get());
    }
}
