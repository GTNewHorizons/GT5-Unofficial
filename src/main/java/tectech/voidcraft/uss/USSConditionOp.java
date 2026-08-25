package tectech.voidcraft.uss;

/**
 * Condition operators of a Voidcraft program (programming framework, Phase A).
 *
 * <p>
 * EQ / NEQ compare strings exactly (case-sensitive). LT / GT compare numerically: both sides are trimmed and
 * parsed as doubles; if EITHER side is not a number, the condition is false (see
 * {@link USSCondition#evaluate(String, String)}).
 */
public enum USSConditionOp {

    /** String equality (case-sensitive). */
    EQ(0),
    /** String inequality. */
    NEQ(1),
    /** Numeric less-than (unparseable side → false). */
    LT(2),
    /** Numeric greater-than (unparseable side → false). */
    GT(3);

    private final int id;

    USSConditionOp(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static USSConditionOp byId(int id) {
        for (USSConditionOp op : values()) {
            if (op.id == id) return op;
        }
        return null;
    }
}
