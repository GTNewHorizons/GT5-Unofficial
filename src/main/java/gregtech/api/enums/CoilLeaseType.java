package gregtech.api.enums;

/**
 * Determines what Type the coil lease is. Useful if a multi wants active components to conditionally turn off/on.
 * see {@link gregtech.common.tileentities.machines.multi.MTEPlasmaForge#tryActivateCoilLease tryActivateCoilLease}
 */
public enum CoilLeaseType {

    COIL,
    BRIDGE;

}
