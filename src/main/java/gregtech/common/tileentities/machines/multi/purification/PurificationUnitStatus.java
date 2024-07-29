package gregtech.common.tileentities.machines.multi.purification;

/**
 * Represents the status of a linked purification unit. This is updated by the main purification plant
 * controller by checking the linked machines.
 */
public enum PurificationUnitStatus {
    // The purification unit is online and ready to work
    ONLINE,
    // The purification unit is correctly formed, but switched off.
    DISABLED,
    // The purification unit has failed its structure check
    INCOMPLETE_STRUCTURE,
}
