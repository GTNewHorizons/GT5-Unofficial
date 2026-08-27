package tectech.voidcraft.gui;

import java.util.List;

/**
 * Where the programming GUI (see {@link VoidcraftProgramGui}) reads the program from and sends its edits to.
 *
 * <p>
 * Two sources exist: the in-world CONTROLLER component (program in block NBT) and the digitized blueprint ITEM
 * (ship / base) in the player inventory (program in item NBT, see {@link VoidcraftProgramItemSource}).
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
}
