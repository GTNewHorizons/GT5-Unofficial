package tectech.voidcraft.uss;

import java.util.Arrays;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The DEFAULT PROGRAM CHIPS (user spec: "include 'default program chips' that apply a basic program to the
 * controller when right-clicking the controller. For miner, starlifter, explorer — others come later").
 *
 * <p>
 * Each chip is the same shape — MOVE to the role's target → WORK → MOVE HOME (delivery happens on the return
 * leg) — differing only in the first target:
 * <ul>
 * <li>Miner → {@link #TARGET_NEAREST_PLANET}</li>
 * <li>Starlifter → {@link #TARGET_STAR}</li>
 * <li>Explorer → {@link #TARGET_RIPPLE_UNSCANNED}</li>
 * </ul>
 *
 * <p>
 * Pure data + NBT — the right-click wiring that applies them to the controller is Phase C. Bare-JVM testable
 * (see {@code USSProgramDefaultsTest}).
 */
public final class USSProgramDefaults {

    /** MOVE param key: the target (one of the {@code TARGET_*} strings). */
    public static final String PARAM_TARGET = "target";
    /** MOVE param key: the target index (PLANET / RIPPLE / SHIP). */
    public static final String PARAM_INDEX = "index";

    // MOVE target values (the string content of the PARAM_TARGET param).
    /** The star (Starlifter work point). */
    public static final String TARGET_STAR = "STAR";
    /** A specific planet, by index (the PARAM_INDEX param). */
    public static final String TARGET_PLANET = "PLANET";
    /** The planet nearest the ship's current position. */
    public static final String TARGET_NEAREST_PLANET = "NEAREST_PLANET";
    /**
     * A RANDOM planet of the current USS (pass-33 UI argument helper "Random planet"): one planet index is picked
     * at resolution time (the world side — the same one-shot pick as {@code RIPPLE_UNSCANNED}); the resolved index
     * is what the WORK leg then mines.
     */
    public static final String TARGET_RANDOM_PLANET = "RANDOM_PLANET";
    /** A specific ripple point, by index (the PARAM_INDEX param). */
    public static final String TARGET_RIPPLE = "RIPPLE";
    /** A random still-unscanned ripple point (the Explorer's scan target). */
    public static final String TARGET_RIPPLE_UNSCANNED = "RIPPLE_UNSCANNED";
    /** A specific in-flight ship, by index (the PARAM_INDEX param). */
    public static final String TARGET_SHIP = "SHIP";
    /** Home — the launch origin / gateway anchor ("Leave the USS"; delivery happens on the return leg). */
    public static final String TARGET_HOME = "HOME";

    /** Miner chip: go to the nearest planet, work (mine), come home. */
    public static USSProgram miner() {
        return basic(TARGET_NEAREST_PLANET);
    }

    /**
     * Constructor chip: go to the anchor target (nearest planet by default — the player edits the target and index
     * to the build site), construct (create or fill the Voidbase construction site there), come home.
     */
    public static USSProgram constructor() {
        NBTTagCompound to = new NBTTagCompound();
        to.setString(PARAM_TARGET, TARGET_NEAREST_PLANET);
        NBTTagCompound home = new NBTTagCompound();
        home.setString(PARAM_TARGET, TARGET_HOME);
        return USSProgram.of(
            Arrays.asList(
                USSNode.command(USSCommand.MOVE, to),
                USSNode.command(USSCommand.CONSTRUCT, new NBTTagCompound()),
                USSNode.command(USSCommand.MOVE, home)));
    }

    /** Starlifter chip: go to the star, work (starlift), come home. */
    public static USSProgram starlifter() {
        return basic(TARGET_STAR);
    }

    /** Explorer chip: go to a random unscanned ripple, work (scan), come home. */
    public static USSProgram explorer() {
        return basic(TARGET_RIPPLE_UNSCANNED);
    }

    /**
     * Derive the default chip from the covers mounted on the controller block (user spec: "default program chips
     * that apply a basic program to the controller when right-clicking the controller. For miner, starlifter,
     * explorer"). The controller block's covers declare what it IS:
     * <ul>
     * <li>a SCANNER_DISH cover → the Explorer chip;</li>
     * <li>a STAR_SIPHON cover → the Starlifter chip;</li>
     * <li>a MINING_ARRAY cover (or no recognizable cover at all) → the Miner chip (the default).</li>
     * </ul>
     *
     * <p>
     * Priority when several are mounted: Explorer &gt; Starlifter &gt; Miner (the scanner is the rarer, more
     * deliberate build).
     *
     * @param scannerDish true when the controller block carries a SCANNER_DISH cover
     * @param starSiphon  true when it carries a STAR_SIPHON cover
     * @param miningArray true when it carries a MINING_ARRAY cover (kept for clarity; the Miner is the default)
     * @return the derived chip program (never null)
     */
    public static USSProgram chip(boolean scannerDish, boolean starSiphon, boolean miningArray) {
        if (scannerDish) {
            return explorer();
        }
        if (starSiphon) {
            return starlifter();
        }
        return miner();
    }

    private static USSProgram basic(String target) {
        NBTTagCompound to = new NBTTagCompound();
        to.setString(PARAM_TARGET, target);
        NBTTagCompound home = new NBTTagCompound();
        home.setString(PARAM_TARGET, TARGET_HOME);
        return USSProgram.of(
            Arrays.asList(
                USSNode.command(USSCommand.MOVE, to),
                USSNode.command(USSCommand.WORK, new NBTTagCompound()),
                USSNode.command(USSCommand.MOVE, home)));
    }

    private USSProgramDefaults() {}
}
