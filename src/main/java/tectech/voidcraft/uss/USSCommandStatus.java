package tectech.voidcraft.uss;

/**
 * The status a {@link USSCommandHandler} reports (programming framework, Phase B).
 */
public enum USSCommandStatus {

    /**
     * The command is in flight — the executor keeps polling {@link USSCommandHandler#tick} (long-running: MOVE / WORK /
     * WAIT).
     */
    RUNNING(0),
    /** The command finished — the executor advances to the next node. */
    DONE(1),
    /** The command failed — the executor SKIPS the instruction (user decision #3: log + continue). */
    FAILED(2),
    /** Terminate the program right now (STOP) — the ship HOLDS (user decision #2: no implicit return). */
    STOP(3);

    private final int id;

    USSCommandStatus(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static USSCommandStatus byId(int id) {
        for (USSCommandStatus status : values()) {
            if (status.id == id) return status;
        }
        return null;
    }
}
