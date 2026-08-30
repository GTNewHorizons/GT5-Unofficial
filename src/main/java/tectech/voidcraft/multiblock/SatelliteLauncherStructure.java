package tectech.voidcraft.multiblock;

/**
 * The Satellite Rail Launcher's structure definition as plain shape data (kept free of MTE references so the
 * cell-count and anchor invariants are testable in a bare JVM).
 *
 * <p>
 * The shape is authored as {@code shape[y][z]}: one row-string per vertical layer (y, bottom to top), each string
 * one depth slice of that layer (z, front plane to back), the string's characters the x axis (left to right) —
 * the same transpose convention as the assemblers. {@code A} = the plain casing block, {@code B} = the launch-deck
 * panel (the top three layers), {@code ~} = the controller anchor (the machine block itself, bottom-center).
 *
 * <p>
 * 7 wide × 12 tall × 7 deep: 588 cells = 1 controller + 440 plain casings + 147 deck panels. The structure is
 * 7×7×12 (user spec) — it only fits the Voidbase Assembler's 15×15×15 scan volume, which is why the component is
 * base-only (a ship build containing it is rejected outright).
 */
public final class SatelliteLauncherStructure {

    public static final String[][] RAW_SHAPE = {
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAA~AAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
        { "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" },
        { "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" },
        { "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" } };

    /** Total structure cell count (every shape character, anchor included): 1 controller + 586 casings. */
    public static final int EXPECTED_CELLS = countCells(RAW_SHAPE);

    /** The anchor ({@code ~}) cell's offset from the machine position: bottom-center (x 3, y 0, z 3). */
    public static final int[] ANCHOR = findAnchor(RAW_SHAPE);

    private SatelliteLauncherStructure() {}

    /**
     * @return the total cell count of a shape (every character of every slice, including the anchor)
     */
    public static int countCells(String[][] shape) {
        int count = 0;
        for (String[] layer : shape) {
            for (String slice : layer) {
                count += slice.length();
            }
        }
        return count;
    }

    /**
     * @param element the element character to count ('A', 'B', or '~')
     * @return the number of cells of that element in the shape
     */
    public static int countCells(char element, String[][] shape) {
        int count = 0;
        for (String[] layer : shape) {
            for (String slice : layer) {
                for (int i = 0; i < slice.length(); i++) {
                    if (slice.charAt(i) == element) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * The anchor ({@code ~}) cell's offset from the machine position, as (x, y, z): x = character index (left to
     * right), y = layer index (bottom to top), z = slice index (front to back).
     *
     * @throws IllegalStateException when the shape has no anchor, or more than one
     */
    public static int[] findAnchor(String[][] shape) {
        int[] anchor = null;
        for (int y = 0; y < shape.length; y++) {
            for (int z = 0; z < shape[y].length; z++) {
                String slice = shape[y][z];
                for (int x = 0; x < slice.length(); x++) {
                    if (slice.charAt(x) == '~') {
                        if (anchor != null) {
                            throw new IllegalStateException("Multiple anchors in shape");
                        }
                        anchor = new int[] { x, y, z };
                    }
                }
            }
        }
        if (anchor == null) {
            throw new IllegalStateException("No anchor in shape");
        }
        return anchor;
    }
}
