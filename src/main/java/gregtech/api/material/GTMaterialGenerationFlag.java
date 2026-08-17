package gregtech.api.material;

/// The nine item-shape capability groups a material can be declared with
/// ([GTMaterialProperties#GENERATION_FLAGS]), gating which shape families it generates.
///
/// Distinct from the final generated-item shape set (`Material#getShapes()`): a material can carry a group
/// flag without every shape in that group actually generating, since a `REMOVED_PREFIXES` entry that
/// [gregtech.loaders.materials.LoaderGTMaterialPasses] adds to `OrePrefixes#mNotGeneratedItems` takes shapes
/// back out.
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
