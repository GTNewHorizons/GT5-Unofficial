package tectech.voidcraft.uss;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * The program carried by a digitized blueprint item (ship / base), editable after digitization: the program
 * lives in the item NBT ({@link VoidcraftNbt#TAG_PROGRAM}), and each edit is applied with
 * {@link USSProgramSync#handle} (server authoritative) and written back to the item.
 *
 * <p>
 * Bare JVM (no entity / MUI2 dependencies — the inventory write-back lives in the GUI source wrapper).
 */
public final class USSBlueprintProgram {

    private USSProgram program;
    private final USSCapabilities caps;
    private String note = "";

    /**
     * @param nbt the blueprint item NBT (may be null — a program-less item starts empty)
     */
    public USSBlueprintProgram(NBTTagCompound nbt) {
        this(nbt, null);
    }

    /**
     * @param nbt  the blueprint item NBT (may be null — a program-less item starts empty)
     * @param caps the craft's capability set (the capability system — inserts / copies / presets outside it are
     *             rejected; null = no capability check)
     */
    public USSBlueprintProgram(NBTTagCompound nbt, USSCapabilities caps) {
        this.program = readProgram(nbt);
        this.caps = caps;
    }

    /** The program stored in an item NBT, or the empty program when the tag is absent. */
    public static USSProgram readProgram(NBTTagCompound nbt) {
        if (nbt != null && nbt.hasKey(VoidcraftNbt.TAG_PROGRAM)) {
            USSProgram p = USSProgram.readFromNBT(nbt.getTagList(VoidcraftNbt.TAG_PROGRAM, 10));
            if (p != null) {
                return p;
            }
        }
        return USSProgram.empty();
    }

    /**
     * Store the program in an item NBT: an empty program REMOVES the tag (the assembler "no program" convention),
     * a non-empty one stores the node list.
     */
    public static void writeProgram(NBTTagCompound nbt, USSProgram program) {
        if (nbt == null) {
            return;
        }
        if (program == null || program.isEmpty()) {
            nbt.removeTag(VoidcraftNbt.TAG_PROGRAM);
        } else {
            nbt.setTag(VoidcraftNbt.TAG_PROGRAM, program.writeToNBT());
        }
    }

    /** The current program (never null). */
    public USSProgram getProgram() {
        return program;
    }

    /** The current program as flat row wire strings for the GUI list sync. */
    public List<String> getProgramRows() {
        return USSProgramView.rowsJsonList(program);
    }

    /** The last rejected action reason ("" when none). */
    public String getNote() {
        return note;
    }

    /**
     * Apply one GUI action JSON. Accepted: the program is replaced and the note cleared. Rejected: the program
     * is unchanged and the reason becomes the note. Never throws (bad action → rejection).
     *
     * @return the outcome
     */
    public USSProgramSync.Outcome applyAction(String actionJson) {
        USSProgramSync.Outcome outcome = USSProgramSync.handle(program, actionJson, caps);
        if (!outcome.ok) {
            note = outcome.message;
            return outcome;
        }
        program = outcome.program;
        note = "";
        return outcome;
    }
}
