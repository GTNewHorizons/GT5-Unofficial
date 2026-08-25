package tectech.voidcraft.uss;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * The USS global variable space — the "global variable list" of the programming framework (user spec:
 * "Voidcraft can write and read from a global variable list that is stored in the USS … the value is always a
 * string and the X is a number from 0 to 255. This allows sending commands to Voidcraft externally and
 * communicating data out of the USS").
 *
 * <p>
 * 256 slots, each holding a string. An UNWRITTEN slot reads as the empty string; an explicit empty write is
 * recorded (a written slot and an unwritten one differ in {@link #isWritten(int)}).
 *
 * <p>
 * Immutable (copy-on-write — {@link #set(int, String)} returns a NEW instance), NBT round-trip is SPARSE (only
 * written slots are serialized), null-safe, and pure — bare-JVM testable (see {@code USSVariableSpaceTest}).
 * It lives on the USS (Phase C wiring) so ships, external machines, and the (next-pass) UI all share one space.
 */
public final class USSVariableSpace {

    /** The number of slots (user-pinned: 0..255). */
    public static final int SLOT_COUNT = 256;

    private final String[] slots;

    private USSVariableSpace(String[] slots) {
        this.slots = slots;
    }

    /** A fresh space with all 256 slots unwritten. */
    public static USSVariableSpace fresh() {
        return new USSVariableSpace(new String[SLOT_COUNT]);
    }

    /**
     * @param slot the slot (0..255)
     * @return the slot's string — {@code ""} when unwritten or the slot is out of range
     */
    public String get(int slot) {
        if (slot < 0 || slot >= SLOT_COUNT) {
            return "";
        }
        String value = slots[slot];
        return value == null ? "" : value;
    }

    /** @param slot the slot (0..255; out of range → false) */
    public boolean isWritten(int slot) {
        return slot >= 0 && slot < SLOT_COUNT && slots[slot] != null;
    }

    /**
     * Copy-on-write set.
     *
     * @param slot  the slot (0..255; out of range → this instance, unchanged)
     * @param value the string (null → {@code ""})
     * @return a NEW space with the slot written (or this instance when the slot is out of range)
     */
    public USSVariableSpace set(int slot, String value) {
        if (slot < 0 || slot >= SLOT_COUNT) {
            return this;
        }
        String[] copy = slots.clone();
        copy[slot] = value == null ? "" : value;
        return new USSVariableSpace(copy);
    }

    /** The number of written slots. */
    public int writtenCount() {
        int n = 0;
        for (String value : slots) {
            if (value != null) {
                n++;
            }
        }
        return n;
    }

    // region NBT

    /**
     * @return a SPARSE NBT list of {@code {i: slot, s: value}} compounds (written slots only)
     */
    public NBTTagList writeToNBT() {
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setInteger("i", i);
                tag.setString("s", slots[i]);
                list.appendTag(tag);
            }
        }
        return list;
    }

    /**
     * @param list the list as written by {@link #writeToNBT()} (may be null)
     * @return the space — NEVER null; out-of-range slots and non-compound entries are dropped
     */
    public static USSVariableSpace readFromNBT(NBTTagList list) {
        String[] slots = new String[SLOT_COUNT];
        if (list != null) {
            for (int i = 0; i < list.tagCount(); i++) {
                Object tag = list.tagList.get(i); // raw element (1.7.10 NBTTagList has no untyped get(int))
                if (!(tag instanceof NBTTagCompound)) {
                    continue;
                }
                NBTTagCompound compound = (NBTTagCompound) tag;
                int slot = compound.getInteger("i");
                if (slot < 0 || slot >= SLOT_COUNT) {
                    continue;
                }
                slots[slot] = compound.getString("s");
            }
        }
        return new USSVariableSpace(slots);
    }

    // endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof USSVariableSpace)) return false;
        return Arrays.equals(slots, ((USSVariableSpace) o).slots);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(slots);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("USSVariableSpace[");
        boolean first = true;
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (slots[i] != null) {
                if (!first) sb.append(", ");
                first = false;
                sb.append(i)
                    .append('=')
                    .append(slots[i]);
            }
        }
        return sb.append(']')
            .toString();
    }
}
