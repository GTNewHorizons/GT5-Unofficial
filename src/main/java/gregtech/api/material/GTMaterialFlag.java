package gregtech.api.material;

/// Boolean material flags, declared through [GTMaterialProperties#FLAGS].
///
/// A constant named like a [gregtech.api.enums.SubTag] also answers [MaterialUtils#hasSubTag] for that
/// sub-tag.
public enum GTMaterialFlag {
    BLASTFURNACE_CALCITE_DOUBLE,
    BLASTFURNACE_CALCITE_TRIPLE,
    DONT_ADD_DEFAULT_BBF_RECIPE,
    WASHING_SODIUMPERSULFATE,
    WASHING_MERCURY,
    WASHING_MERCURY_99_PERCENT,
    ELECTROMAGNETIC_SEPERATION_GOLD,
    ELECTROMAGNETIC_SEPERATION_IRON,
    ELECTROMAGNETIC_SEPERATION_NEODYMIUM,
    NO_WORKING,
    NO_SMASHING,
    NO_RECIPES,
    NO_RECYCLING,
    NO_RECYCLING_RECIPES,
    NO_SMELTING,
    NO_ORE_PROCESSING,
    SMELTING_TO_FLUID,
    SMELTING_TO_GEM,
    WOOD,
    STONE,
    PEARL,
    QUARTZ,
    CRYSTALLISABLE,
    CRYSTAL,
    MAGICAL,
    METAL,
    PAPER,
    FLAMMABLE,
    UNBURNABLE,
    EXPLOSIVE,
    BOUNCY,
    TRANSPARENT,
    STRETCHY,
    SOFT,
    MORTAR_GRINDABLE,
    SOLDERING_MATERIAL,
    SOLDERING_MATERIAL_BAD,
    SOLDERING_MATERIAL_GOOD,
    BLACK_HOLE,
    ICE_ORE,
    MULTI_PLATE,
    TRANSMUTABLE_NUGGETS,
    /// Materials Tinkers' Gregworks offers as tool materials.
    TOOL_MATERIAL,
    /// Gems that never get an implosion recipe.
    NO_BLAST,
    /// Metals whose blast furnace recipes take any [gregtech.api.util.BlastFurnaceGasStat] gas.
    NOBLE_GAS_SMELTING
}
