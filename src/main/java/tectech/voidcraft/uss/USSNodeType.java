package tectech.voidcraft.uss;

/**
 * Node types of a Voidcraft program (programming framework, Phase A).
 *
 * <p>
 * COMMAND — a registered command (MOVE / WORK / WRITE / READ / WAIT / STOP — see {@link USSCommand}); IF — a
 * one-shot conditional body; WHILE — loop while the condition holds; REPEAT — loop the body N times.
 *
 * <p>
 * Deliberately no FOR (user decision: "skip FOR for now — likely to not have any use").
 */
public enum USSNodeType {

    /** A command: {@code cmdId} + free-form params. */
    COMMAND(0),
    /** A one-shot conditional body. */
    IF(1),
    /** A loop that runs while its condition holds. */
    WHILE(2),
    /** A loop that runs its body a fixed number of times. */
    REPEAT(3);

    private final int id;

    USSNodeType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static USSNodeType byId(int id) {
        for (USSNodeType type : values()) {
            if (type.id == id) return type;
        }
        return null;
    }
}
