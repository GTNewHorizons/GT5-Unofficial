package gregtech.api.material;

/// The nine legacy `MaterialBuilder#addDustItems`-style item-shape capability groups a material was built with,
/// mirroring `gregtech.api.enums.Materials#hasDustItems` and its siblings.
///
/// These are distinct from the final generated-item shape set (`Material#getShapes()`): a material can carry a
/// group flag without every shape in that group actually generating (removed via `MaterialBuilder#removeOrePrefix`),
/// and a handful of consumers (recipe/block/pipe loaders) read the group flag itself rather than checking for a
/// specific shape's existence.
public enum GTMaterialGenerationFlag {
    DUST,
    METAL,
    GEM,
    ORE,
    CELL,
    PLASMA,
    TOOL_HEAD,
    GEAR,
    EMPTY
}
