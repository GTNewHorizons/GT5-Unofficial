package tectech.voidcraft.uss;

import gregtech.api.enums.Materials;

/**
 * The Voidcraft star catalog — the sixteen star classes registered into the registration-based system
 * ({@link USSStarRegistry}); the {@code id} is the star's stable key (the lowercased enum name) and the
 * {@code type} field is the <em>display</em> name ("Red Dwarf", "Yellow Dwarf", ...).
 *
 * <p>
 * <strong>Placeholder note.</strong> The colors and the evolution targets follow the star-evolution design table;
 * the sizes are tuned per class (dwarfs 0.05–0.75, neutron star 0.15–0.25, quark star 1.2–1.8, giants up to 10.0 —
 * the rendered size is (2/3)·√size, so the top end compresses). The remaining fields (weights,
 * luminosity, planet range, ripple range, texture) are reasonable, valid starting values to be tuned by game
 * design. Each star produces 1–3 of its three materials as starlifter fluids (a zero-capacity slot produces
 * none), with per-fluid capacities in millions of mB. The materials are "primarily fluids" (stellar composition).
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
     * Register the sixteen catalog stars into the {@link USSStarRegistry}. Idempotent — a second call is a no-op.
     */
    public static synchronized void registerAll() {
        if (registered) {
            return;
        }
        registered = true;

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("red_dwarf")
                .nameMethod(() -> "Red Dwarf")
                .type("Red Dwarf")
                .sizeRange(0.05f, 0.25f)
                .main(Materials.Hydrogen, 3.0, 100L)
                .secondary(Materials.Helium, 2.0, 50L)
                .tertiary(Materials.Lithium, 1.0, 25L)
                .luminosity(1.0f)
                .planetRange(0, 4)
                .rippleRange(4, 16)
                .texture("star_red_dwarf")
                .evolutionTarget("white_dwarf")
                .color(0xFFF2A0B8) // pink
                .shellColor(0xFFC03028) // red
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("yellow_dwarf")
                .nameMethod(() -> "Yellow Dwarf")
                .type("Yellow Dwarf")
                .sizeRange(0.25f, 0.75f)
                .main(Materials.Hydrogen, 3.0, 200L)
                .secondary(Materials.Helium, 2.0, 100L)
                .tertiary(Materials.Oxygen, 1.0, 50L)
                .luminosity(5.0f)
                .planetRange(3, 9)
                .rippleRange(8, 32)
                .texture("star_yellow_dwarf")
                .evolutionTarget("red_giant")
                .color(0xFFE07020) // orange
                .shellColor(0xFFFFD24A) // yellow
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("red_giant")
                .nameMethod(() -> "Red Giant")
                .type("Red Giant")
                .sizeRange(8.0f, 10.0f)
                .main(Materials.Helium, 3.0, 400L)
                .secondary(Materials.Carbon, 2.0, 200L)
                .tertiary(Materials.Oxygen, 1.0, 100L)
                .luminosity(8.0f)
                .planetRange(0, 6)
                .rippleRange(16, 48)
                .texture("star_red_giant")
                .evolutionTarget("white_dwarf")
                .color(0xFFE07020) // orange
                .shellColor(0xFFC03028) // red
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("white_dwarf")
                .nameMethod(() -> "White Dwarf")
                .type("White Dwarf")
                .sizeRange(0.25f, 0.75f)
                .main(Materials.Oxygen, 3.0, 200L)
                .secondary(Materials.Carbon, 2.0, 100L)
                .tertiary(Materials.Helium, 1.0, 50L)
                .luminosity(2.0f)
                .planetRange(2, 8)
                .rippleRange(16, 64)
                .texture("star_white_dwarf")
                .evolutionTarget("black_dwarf")
                .color(0xFF9FC8FF) // light blue
                .shellColor(0xFFFFFFFF) // white
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("black_dwarf")
                .nameMethod(() -> "Black Dwarf")
                .type("Black Dwarf")
                .sizeRange(0.25f, 0.75f)
                .main(Materials.Carbon, 3.0, 200L)
                .secondary(Materials.Iron, 2.0, 100L)
                .tertiary(Materials.Cobalt, 1.0, 50L)
                .luminosity(0.1f)
                .planetRange(0, 4)
                .rippleRange(8, 32)
                .texture("star_black_dwarf")
                .evolutionTarget(null)
                .color(0xFF15181D) // near black
                .shellColor(0xFF585E68) // dark gray
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("blue_giant")
                .nameMethod(() -> "Blue Giant")
                .type("Blue Giant")
                .sizeRange(4.0f, 8.0f)
                .main(Materials.Iron, 3.0, 400L)
                .secondary(Materials.Nickel, 2.0, 200L)
                .tertiary(Materials.Cobalt, 1.0, 100L)
                .luminosity(10.0f)
                .planetRange(4, 12)
                .rippleRange(32, 96)
                .texture("star_blue_giant")
                .evolutionTarget("red_supergiant")
                .color(0xFF9FC8FF) // light blue
                .shellColor(0xFF9FC8FF) // light blue
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("red_supergiant")
                .nameMethod(() -> "Red Supergiant")
                .type("Red Supergiant")
                .sizeRange(8.0f, 10.0f)
                .main(Materials.Carbon, 3.0, 500L)
                .secondary(Materials.Silicon, 2.0, 250L)
                .tertiary(Materials.Oxygen, 1.0, 125L)
                .luminosity(9.0f)
                .planetRange(0, 8)
                .rippleRange(24, 80)
                .texture("star_red_supergiant")
                .evolutionTarget("supernova")
                .color(0xFFE07020) // orange
                .shellColor(0xFFE07020) // orange
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("supernova")
                .nameMethod(() -> "Supernova")
                .type("Supernova")
                .sizeRange(2.0f, 4.0f)
                .main(Materials.Silicon, 3.0, 400L)
                .secondary(Materials.Iron, 2.0, 200L)
                .tertiary(Materials.Nickel, 1.0, 100L)
                .luminosity(10.0f)
                .planetRange(0, 2)
                .rippleRange(32, 128)
                .texture("star_supernova")
                .evolutionTarget("black_hole")
                .color(0xFF9040D0) // purple
                .shellColor(0xFFFFFFFF) // white
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("black_hole")
                .nameMethod(() -> "Black Hole")
                .type("Black Hole")
                .sizeRange(2.0f, 4.0f)
                .main(Materials.Osmium, 3.0, 500L)
                .secondary(Materials.Iron, 2.0, 0L)
                .tertiary(Materials.Platinum, 1.0, 0L)
                .luminosity(0.0f)
                .planetRange(0, 3)
                .rippleRange(96, 128)
                .texture("star_black_hole")
                .evolutionTarget(null)
                .color(0xFF000000) // black
                .shellColor(0xFFFFB000) // amber
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("quasi_star")
                .nameMethod(() -> "Quasi-Star")
                .type("Quasi-Star")
                .sizeRange(0.2f, 1.0f)
                .main(Materials.Hydrogen, 3.0, 200L)
                .secondary(Materials.Helium, 2.0, 0L)
                .tertiary(Materials.Iron, 1.0, 100L)
                .luminosity(10.0f)
                .planetRange(0, 5)
                .rippleRange(16, 64)
                .texture("star_quasi_star")
                .evolutionTarget(null)
                .color(0xFFFFD24A) // yellow
                .shellColor(0xFFFFD24A) // yellow
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("blue_supergiant")
                .nameMethod(() -> "Blue Supergiant")
                .type("Blue Supergiant")
                .sizeRange(8.0f, 10.0f)
                .main(Materials.Helium, 3.0, 500L)
                .secondary(Materials.Silicon, 2.0, 250L)
                .tertiary(Materials.Iron, 1.0, 125L)
                .luminosity(9.0f)
                .planetRange(4, 12)
                .rippleRange(32, 96)
                .texture("star_blue_supergiant")
                .evolutionTarget("hypernova")
                .color(0xFF2040C0) // deep blue
                .shellColor(0xFF4A6AE0) // blue
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("hypernova")
                .nameMethod(() -> "Hypernova")
                .type("Hypernova")
                .sizeRange(1.0f, 3.0f)
                .main(Materials.Iron, 3.0, 400L)
                .secondary(Materials.Iridium, 2.0, 200L)
                .tertiary(Materials.Osmium, 1.0, 100L)
                .luminosity(10.0f)
                .planetRange(0, 2)
                .rippleRange(48, 128)
                .texture("star_hypernova")
                .evolutionTarget("neutron_star")
                .color(0xFFFFFFFF) // white
                .shellColor(0xFFFFFFFF) // white
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("neutron_star")
                .nameMethod(() -> "Neutron Star")
                .type("Neutron Star")
                .sizeRange(0.15f, 0.25f)
                .main(Materials.Iron, 3.0, 600L)
                .secondary(Materials.Cobalt, 2.0, 300L)
                .tertiary(Materials.Nickel, 1.0, 150L)
                .luminosity(0.5f)
                .planetRange(0, 4)
                .rippleRange(24, 80)
                .texture("star_neutron_star")
                .evolutionTarget("magnetar")
                .color(0xFF40E0E0) // cyan
                .shellColor(0xFF9FC8FF) // light blue
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("magnetar")
                .nameMethod(() -> "Magnetar")
                .type("Magnetar")
                .sizeRange(1.0f, 3.0f)
                .main(Materials.Cobalt, 3.0, 500L)
                .secondary(Materials.Iron, 2.0, 250L)
                .tertiary(Materials.Nickel, 1.0, 0L)
                .luminosity(1.0f)
                .planetRange(0, 3)
                .rippleRange(32, 96)
                .texture("star_magnetar")
                .evolutionTarget(null)
                .color(0xFF40E0E0) // cyan
                .shellColor(0xFF9FC8FF) // light blue
                .renderType(USSStarRenderType.MAGNETAR)
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("gravastar")
                .nameMethod(() -> "Gravastar")
                .type("Gravastar")
                .sizeRange(2.0f, 4.0f)
                .main(Materials.Iridium, 3.0, 600L)
                .secondary(Materials.Iron, 2.0, 300L)
                .tertiary(Materials.Cobalt, 1.0, 0L)
                .luminosity(0.0f)
                .planetRange(0, 3)
                .rippleRange(64, 128)
                .texture("star_gravastar")
                .evolutionTarget(null)
                .color(0xFF000000) // black
                .shellColor(0xFF9040D0) // purple
                .build());

        USSStarRegistry.register(
            USSStarDefinition.builder()
                .id("quark_star")
                .nameMethod(() -> "Quark Star")
                .type("Quark Star")
                .sizeRange(1.2f, 1.8f)
                .main(Materials.Gold, 3.0, 800L)
                .secondary(Materials.Iridium, 2.0, 0L)
                .tertiary(Materials.Osmium, 1.0, 0L)
                .luminosity(1.5f)
                .planetRange(0, 4)
                .rippleRange(8, 32)
                .texture("star_quark_star")
                .evolutionTarget(null)
                .color(0xFFFFFFFF) // white
                .shellColor(0xFF40E0E0) // cyan
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
