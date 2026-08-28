package tectech.voidcraft.gui;

import java.util.List;

import tectech.voidcraft.uss.USSCapabilities;
import tectech.voidcraft.uss.USSCommand;

/**
 * Where the programming GUI (see {@link VoidcraftProgramGui}) reads the program from and sends its edits to.
 *
 * <p>
 * Two sources exist: the in-world CONTROLLER component (program in block NBT) and the digitized blueprint ITEM
 * (ship / base) in the player inventory (program in item NBT, see {@link VoidcraftProgramItemSource}).
 *
 * <p>
 * The source also reports the craft's CAPABILITY SET (the capability system) + a per-command STAT LINE (the
 * command tooltip): the GUI only offers the commands the craft can run, and every command row shows what the
 * craft actually does with it (its speed / mining power / scan power / siphon power / construction power).
 */
public interface VoidcraftProgramSource {

    /**
     * The program as flat row wire strings for the GUI list sync (see
     * {@link tectech.voidcraft.uss.USSProgramView#rowsJsonList}).
     */
    List<String> getProgramRows();

    /** The last server-side rejection reason ("" when none). */
    String getNote();

    /**
     * Apply one GUI action JSON (see {@link tectech.voidcraft.uss.USSProgramSync}). Runs on the server; must
     * NEVER throw (bad action → rejection → the note is set).
     */
    void applyAction(String actionJson);

    /**
     * The craft's COMMAND CAPABILITY SET (the capability system): which program commands the underlying ship /
     * base can actually run (the GUI hides the rest and the server rejects edits outside it).
     */
    USSCapabilities getCommandCaps();

    /**
     * The per-command STAT LINE for a command row's tooltip ("Speed: 3.2", "Mining power: 120", …; "" when the
     * command has no stat or the craft has none of it). Indexed by command id (0..{@link USSCommand#MAX_ID}).
     */
    String getCommandStatLine(int commandId);
}
