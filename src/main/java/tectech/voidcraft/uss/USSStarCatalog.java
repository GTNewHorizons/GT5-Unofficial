package tectech.voidcraft.uss;

import gregtech.api.enums.Materials;

/**
 * The initial Voidcraft star catalog — the three star classes (previously the fixed {@link USSStarType} enum)
 * re-registered into the new registration-based system ({@link USSStarRegistry}).
 *
 * <p>
 * The {@code id}s are the stable keys ({@code main_sequence}, {@code white_dwarf}, {@code supermassive}); the
 * {@code type} field is the <em>display</em> name ("Main Sequence", "White Dwarf", "Supermassive").
 *
 * <p>
 * <strong>Placeholder note.</strong> The new fields (name method, size range, materials + weights, luminosity, planet
 * range, texture, evolution target) are set to reasonable, valid starting values to be tuned by game design. The
 * planet range for {@code main_sequence} preserves the legacy 3–9 system; the others are illustrative. The materials
 * are "primarily fluids" (stellar composition). The evolution chain is illustrative (main sequence → white dwarf);
 * the others are terminal for now.
 *
 * <p>
 * Call {@link #registerAll()} once (idempotent) to populate the registry. Bare-JVM safe: only
 * {@link Materials}/{@link java.util.function.Supplier} data.
 */
public final class USSStarCatalog {

    private static boolean registered;

    private USSStarCatalog() {
        throw new AssertionError("Catalog holder");
    }

    /**
     * Register the three catalog stars into the {@link USSStarRegistry}. Idempotent — a second call is a no-op.
     */
    public static synchronized void registerAll() {
        if (registered) {
            return;
        }
        registered = true;

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("main_sequence")
                .nameMethod(() -> "Main Sequence")
                .type("Main Sequence")
                .sizeRange(1.0f, 5.0f)
                .main(Materials.Hydrogen, 3.0)
                .secondary(Materials.Helium, 2.0)
                .tertiary(Materials.Oxygen, 1.0)
                .luminosity(5.0f)
                .planetRange(3, 9)
                .rippleRange(8, 32)
                .texture("star_main")
                .evolutionTarget("white_dwarf")
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("white_dwarf")
                .nameMethod(() -> "White Dwarf")
                .type("White Dwarf")
                .sizeRange(0.5f, 3.0f)
                .main(Materials.Oxygen, 3.0)
                .secondary(Materials.Carbon, 2.0)
                .tertiary(Materials.Helium, 1.0)
                .luminosity(2.0f)
                .planetRange(2, 8)
                .rippleRange(16, 64)
                .texture("star_dwarf")
                .evolutionTarget(null)
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("supermassive")
                .nameMethod(() -> "Supermassive")
                .type("Supermassive")
                .sizeRange(4.0f, 8.0f)
                .main(Materials.Iron, 3.0)
                .secondary(Materials.Nickel, 2.0)
                .tertiary(Materials.Cobalt, 1.0)
                .luminosity(10.0f)
                .planetRange(4, 12)
                .rippleRange(32, 96)
                .texture("star_supermassive")
                .evolutionTarget(null)
                .build());
    }

    /**
     * Test hook: reset the {@link #registered} flag so {@link #registerAll()} can run again after a
     * {@link USSStarRegistry#clear()}.
     */
    public static synchronized void resetForTests() {
        registered = false;
    }
}
