package gregtech.api.enums;

import gregtech.api.structure.ActiveBlockLeaseType;
import gregtech.api.structure.ActiveBlockLeaseTypeRegistry;
import gregtech.common.tileentities.machines.multi.MTEPlasmaForge;

/**
 * @deprecated Register an {@link ActiveBlockLeaseType} through {@link ActiveBlockLeaseTypeRegistry} instead.
 */
@Deprecated
public enum CoilLeaseType {

    COIL,
    BRIDGE;

    public ActiveBlockLeaseType toLeaseType() {
        return switch (this) {
            case COIL -> ActiveBlockLeaseTypeRegistry.DEFAULT;
            case BRIDGE -> MTEPlasmaForge.CONVERGENCE_BRIDGE_LEASE;
        };
    }
}
