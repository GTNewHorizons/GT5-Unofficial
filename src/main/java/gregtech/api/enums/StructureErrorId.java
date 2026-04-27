package gregtech.api.enums;

/**
 * Used as a bit set for {@link gregtech.api.metatileentity.implementations.MTEMultiBlockBase#structureErrors}. You can
 * reorder these as needed.
 */
public enum StructureErrorId {

    WRONG_BLOCK,
    BLOCK_NOT_LOADED,
    MISSING_MAINTENANCE,
    MISSING_MUFFLER,
    UNNEEDED_MUFFLER,
    TOO_FEW_CASINGS,
    MISSING_CRYO_HATCH,
    TOO_MANY_CRYO_HATCHES,
    MISSING_STEAM_HATCH,
    MISSING_STRUCTURE_WRAPPER_CASINGS;

}
