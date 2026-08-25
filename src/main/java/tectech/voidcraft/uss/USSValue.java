package tectech.voidcraft.uss;

import net.minecraft.nbt.NBTTagCompound;

/**
 * One program VALUE of a Voidcraft program (programming framework, Phase A).
 *
 * <p>
 * User spec: "the value is always a string". A value is one of three kinds:
 * <ul>
 * <li>{@link Kind#LITERAL} — a fixed string (at most {@link USSProgram#MAX_LITERAL_LENGTH} characters);</li>
 * <li>{@link Kind#VAR} — a slot (0..255) of the USS global variable space (the external in/out channel);</li>
 * <li>{@link Kind#STAT} — a ship stat id (the executor's stat registry, Phase B).</li>
 * </ul>
 *
 * <p>
 * Immutable, NBT round-trip, null-safe reads (corrupt → a safe literal, no backwards-compat) and pure (no
 * Minecraft runtime) — unit-testable in a bare JVM (see {@code USSValueTest}).
 */
public final class USSValue {

    /** The value kind (persisted as a stable id). */
    public enum Kind {

        /** A fixed string. */
        LITERAL(0),
        /** A USS variable slot (0..255). */
        VAR(1),
        /** A ship stat id (Phase B registry). */
        STAT(2);

        private final int id;

        Kind(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Kind byId(int id) {
            for (Kind kind : values()) {
                if (kind.id == id) return kind;
            }
            return null;
        }
    }

    private final Kind kind;
    private final String literal;
    private final int slot;
    private final int statId;

    private USSValue(Kind kind, String literal, int slot, int statId) {
        this.kind = kind;
        this.literal = literal;
        this.slot = slot;
        this.statId = statId;
    }

    /**
     * @param value the literal string (null → {@code ""}; truncated to {@link USSProgram#MAX_LITERAL_LENGTH})
     */
    public static USSValue literal(String value) {
        String s = value == null ? "" : value;
        if (s.length() > USSProgram.MAX_LITERAL_LENGTH) {
            s = s.substring(0, USSProgram.MAX_LITERAL_LENGTH);
        }
        return new USSValue(Kind.LITERAL, s, 0, 0);
    }

    /**
     * @param slot the USS variable slot (clamped to 0..255)
     */
    public static USSValue variable(int slot) {
        return new USSValue(Kind.VAR, "", clampSlot(slot), 0);
    }

    /**
     * @param statId the ship stat id (clamped to ≥ 0)
     */
    public static USSValue stat(int statId) {
        return new USSValue(Kind.STAT, "", 0, Math.max(0, statId));
    }

    public Kind kind() {
        return kind;
    }

    public String literal() {
        return literal;
    }

    public int slot() {
        return slot;
    }

    public int statId() {
        return statId;
    }

    static int clampSlot(int slot) {
        return Math.max(0, Math.min(USSVariableSpace.SLOT_COUNT - 1, slot));
    }

    // region NBT

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("k", kind.getId());
        nbt.setString("s", literal);
        nbt.setInteger("v", slot);
        nbt.setInteger("st", statId);
        return nbt;
    }

    /**
     * @param nbt the compound as written by {@link #writeToNBT()} (may be null)
     * @return the value — NEVER null; a missing kind or corrupt NBT falls back to an empty literal
     */
    public static USSValue readFromNBT(NBTTagCompound nbt) {
        if (nbt == null) {
            return literal("");
        }
        Kind kind = Kind.byId(nbt.getInteger("k"));
        if (kind == null) {
            return literal(nbt.getString("s")); // unknown kind → treat whatever string is there as the literal
        }
        switch (kind) {
            case VAR:
                return variable(nbt.getInteger("v"));
            case STAT:
                return stat(nbt.getInteger("st"));
            case LITERAL:
            default:
                return literal(nbt.getString("s"));
        }
    }

    // endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof USSValue)) return false;
        USSValue other = (USSValue) o;
        return slot == other.slot && statId == other.statId && kind == other.kind && literal.equals(other.literal);
    }

    @Override
    public int hashCode() {
        int h = kind.hashCode();
        h = 31 * h + literal.hashCode();
        h = 31 * h + slot;
        h = 31 * h + statId;
        return h;
    }

    @Override
    public String toString() {
        switch (kind) {
            case VAR:
                return "VAR" + slot;
            case STAT:
                return "STAT" + statId;
            case LITERAL:
            default:
                return '"' + literal + '"';
        }
    }
}
