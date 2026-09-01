package tectech.voidcraft.ship;

/**
 * The thruster families a ship may fly with.
 *
 * <p>
 * A ship may mount only ONE engine type (blueprint validation — {@code voidcraft_engine_mismatch}); its stats
 * ({@code engineType}) carry that single id. {@link #NONE} marks a ship with no thruster at all (speed 0 —
 * invalid for digitization anyway: {@code voidcraft_no_engine}); {@link #STANDARD} is the fuel-less baseline
 * nozzles of the original engine. The other three types burn fuel while travelling (see
 * {@link #requiresFuel}): the fuel tank ({@code fuelCapacity}) holds the fluid the type consumes, filled full at
 * launch and consumed per travel tick.
 *
 * <p>
 * This class is plain Java (no Minecraft types) so blueprint validation and the bare-JVM tests can use it; the
 * fuel-FLUID identity for each type lives in the runtime registry ({@code VoidcraftFuel}, load phase).
 */
public enum VoidcraftEngineType {

    /** No thruster covers mounted (speed 0). */
    NONE(0),
    /** The baseline nozzles — no fuel, energy only. */
    STANDARD(1),
    /** Ion thrusters — burn Xenon (liquid). */
    ION(2),
    /** Fusion torches — burn Water (liquid). */
    FUSION(3),
    /** Antimatter engines — burn Semi-Stable Antimatter (liquid). */
    ANTIMATTER(4);

    private final int id;

    VoidcraftEngineType(int id) {
        this.id = id;
    }

    /**
     * @return the id this type carries in stats and NBT ({@code vc_engine}).
     */
    public int id() {
        return id;
    }

    /**
     * @param id the id carried in stats and NBT.
     * @return the type for that id, or {@link #NONE} for unknown ids (forward compatibility).
     */
    public static VoidcraftEngineType byId(int id) {
        for (VoidcraftEngineType type : values()) {
            if (type.id == id) return type;
        }
        return NONE;
    }

    /**
     * @return true when travel legs burn the tank fluid (every type except {@link #NONE} and
     *         {@link #STANDARD}).
     */
    public boolean requiresFuel() {
        return this == ION || this == FUSION || this == ANTIMATTER;
    }
}
