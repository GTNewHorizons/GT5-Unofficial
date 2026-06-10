package gregtech.api.interfaces;

import org.jetbrains.annotations.Nullable;

public interface IBiodomeCompatible {

    /**
     * Called by the Biodome whenever its dimension state changes (calibration completes, structure breaks, etc.).
     * Implementors should store the override string and use it in place of the machine's physical dimension
     * when processing.
     *
     * @param dimensionName the biodome's active dimension name, or {@code null} when the biodome is
     *                      removed or shut down. A null value means the machine should fall back to its
     *                      regular (physical) dimension behavior.
     */
    void updateBiodome(@Nullable String dimensionName);
}
