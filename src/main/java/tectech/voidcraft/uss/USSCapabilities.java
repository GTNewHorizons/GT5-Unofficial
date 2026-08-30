package tectech.voidcraft.uss;

/**
 * The COMMAND CAPABILITY SET of a ship (or base) — which program commands the underlying craft can actually run
 * (the capability system: a program view only offers what the craft can do).
 *
 * <p>
 * One bit per command family; the always-available commands (WRITE / READ / WAIT / STOP and the flow blocks)
 * need no bit. The capability set is DERIVED from the craft's stats (the digitized item knows them exactly) or,
 * for the in-world controller editor, from the covers mounted on the controller block itself (an empty
 * controller says nothing about the hull, so it reports {@link #UNIVERSAL}).
 *
 * <p>
 * The editor rejects inserts / preset applications outside the set; the RUNTIME enforces the truth on top (the
 * pilots refuse a work leg the craft has no power for, and a REPAIR on a ship SKIPs) — the capability set gates
 * the authoring surface, the pilots gate execution.
 *
 * <p>
 * Bare JVM (plain int bits — the GUI builds it fresh on both sides from the same source data; no sync of its own).
 */
public final class USSCapabilities {

    /** The craft has thrusters (speed &gt; 0) — the MOVE command. */
    public static final int MOVE = 1;
    /** The craft has mining power — the MINE command. */
    public static final int MINE = 2;
    /** The craft has scan power — the SCAN command. */
    public static final int SCAN = 4;
    /** The craft has siphon (starlifter) power — the SIPHON command. */
    public static final int SIPHON = 8;
    /** The craft has construction power — the CONSTRUCT command. */
    public static final int CONSTRUCT = 16;
    /** The craft carries a repair bay — the REPAIR command (stations only; a ship's REPAIR always SKIPs). */
    public static final int REPAIR = 32;
    /** The craft has logistics power (Cargo Drone Bay covers) — the SEND / TAKE cargo-transfer commands. */
    public static final int LOGISTICS = 64;

    /** Every capability. */
    public static final int ALL = MOVE | MINE | SCAN | SIPHON | CONSTRUCT | REPAIR | LOGISTICS;

    private final int bits;

    private USSCapabilities(int bits) {
        this.bits = bits & ALL;
    }

    /** @param bits arbitrary bit set (masked to {@link #ALL}). */
    public static USSCapabilities of(int bits) {
        return new USSCapabilities(bits);
    }

    /** No command at all (only the always-available flow commands). */
    public static USSCapabilities empty() {
        return new USSCapabilities(0);
    }

    /** Every command (an empty controller block — nothing is known about the hull). */
    public static USSCapabilities universal() {
        return new USSCapabilities(ALL);
    }

    public int bits() {
        return bits;
    }

    public boolean isMove() {
        return (bits & MOVE) != 0;
    }

    public boolean isMine() {
        return (bits & MINE) != 0;
    }

    public boolean isScan() {
        return (bits & SCAN) != 0;
    }

    public boolean isSiphon() {
        return (bits & SIPHON) != 0;
    }

    public boolean isConstruct() {
        return (bits & CONSTRUCT) != 0;
    }

    public boolean isRepair() {
        return (bits & REPAIR) != 0;
    }

    public boolean isLogistics() {
        return (bits & LOGISTICS) != 0;
    }

    public boolean has(int bit) {
        return (bits & bit) != 0;
    }

    /**
     * Whether a command may be INSERTED into a program under this capability set. The always-available commands
     * (WRITE / READ / WAIT / STOP) are allowed unconditionally; an unknown id is allowed here (the executor SKIPs
     * unregistered ids at run time).
     */
    public boolean allowsCommand(int commandId) {
        switch (commandId) {
            case USSCommand.MOVE:
                return isMove();
            case USSCommand.MINE:
                return isMine();
            case USSCommand.SCAN:
                return isScan();
            case USSCommand.SIPHON:
                return isSiphon();
            case USSCommand.CONSTRUCT:
                return isConstruct();
            case USSCommand.REPAIR:
                return isRepair();
            case USSCommand.SEND:
            case USSCommand.TAKE:
                return isLogistics();
            default:
                return true;
        }
    }

    /** The capability bit a preset requires (0 = none — {@code clear} needs nothing). */
    public static int presetRequirement(String preset) {
        if (preset == null) {
            return 0;
        }
        switch (preset) {
            case "miner":
                return MINE;
            case "starlifter":
                return SIPHON;
            case "explorer":
                return SCAN;
            case "constructor":
                return CONSTRUCT;
            default:
                return 0;
        }
    }

    /**
     * @return true when the preset may be applied under this capability set (a preset needing nothing is always
     *         allowed).
     */
    public boolean allowsPreset(String preset) {
        int required = presetRequirement(preset);
        return required == 0 || has(required);
    }

    @Override
    public String toString() {
        return "USSCapabilities[" + bits + "]";
    }
}
