package gregtech.api.material;

/// The shape groups a material generates, declared through [GTMaterialProperties#GENERATION_FLAGS]. A shape
/// generates for a material when the two share a group (see [GTShapeProperties#GENERATION_FLAGS]).
///
/// A flag does not guarantee every shape in its group generates: the material's
/// [GTMaterialProperties#REMOVED_PREFIXES] take shapes back out.
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
