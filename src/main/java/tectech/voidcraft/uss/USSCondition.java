package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * One binary condition of a Voidcraft program (programming framework, Phase A): {@code left OP right} over
 * {@link USSValue}s.
 *
 * <p>
 * This class is the CONDITION DATA plus the pure comparison of already-resolved strings. Resolving a
 * {@link USSValue} against the ship's stats and the USS variable space is the executor context's job (Phase B) —
 * keeping it out here is what makes this bare-JVM testable.
 *
 * <p>
 * Immutable, NBT round-trip, null-safe (corrupt NBT → a safe default condition), no backwards-compat.
 */
public final class USSCondition {

    private final USSValue left;
    private final USSConditionOp op;
    private final USSValue right;

    private USSCondition(USSValue left, USSConditionOp op, USSValue right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    /** Null-safe factory: nulls fall back to the empty-literal / EQ defaults. */
    public static USSCondition of(USSValue left, USSConditionOp op, USSValue right) {
        return new USSCondition(
            left == null ? USSValue.literal("") : left,
            op == null ? USSConditionOp.EQ : op,
            right == null ? USSValue.literal("") : right);
    }

    public USSValue left() {
        return left;
    }

    public USSConditionOp op() {
        return op;
    }

    public USSValue right() {
        return right;
    }

    /**
     * Pure comparison of two ALREADY-RESOLVED string values:
     * <ul>
     * <li>EQ / NEQ: exact string comparison (case-sensitive); null sides count as {@code ""}.</li>
     * <li>LT / GT: numeric comparison (trimmed {@code Double.parseDouble}); if either side is not a number the
     * condition is false.</li>
     * </ul>
     */
    public boolean evaluate(String leftValue, String rightValue) {
        String l = leftValue == null ? "" : leftValue;
        String r = rightValue == null ? "" : rightValue;
        switch (op) {
            case NEQ:
                return !l.equals(r);
            case LT:
                return compare(l, r) < 0;
            case GT:
                return compare(l, r) > 0;
            case EQ:
            default:
                return l.equals(r);
        }
    }

    private static int compare(String a, String b) {
        double x = parse(a);
        double y = parse(b);
        if (Double.isNaN(x) || Double.isNaN(y)) {
            return 0; // not numeric → neither LT nor GT holds
        }
        return Double.compare(x, y);
    }

    private static double parse(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    // region NBT

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("l", left.writeToNBT());
        nbt.setInteger("op", op.getId());
        nbt.setTag("r", right.writeToNBT());
        return nbt;
    }

    /**
     * @param nbt the compound as written by {@link #writeToNBT()} (may be null)
     * @return the condition — NEVER null; corrupt NBT falls back to {@code "" EQ ""}
     */
    public static USSCondition readFromNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return of(USSValue.literal(""), USSConditionOp.EQ, USSValue.literal(""));
        }
        USSConditionOp op = USSConditionOp.byId(nbt.getInteger("op"));
        return of(USSValue.readFromNBT(nbt.getCompoundTag("l")), op, USSValue.readFromNBT(nbt.getCompoundTag("r")));
    }

    // endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof USSCondition)) return false;
        USSCondition other = (USSCondition) o;
        return op == other.op && left.equals(other.left) && right.equals(other.right);
    }

    @Override
    public int hashCode() {
        int h = op.hashCode();
        h = 31 * h + left.hashCode();
        h = 31 * h + right.hashCode();
        return h;
    }

    @Override
    public String toString() {
        return left + " " + op + " " + right;
    }
}
