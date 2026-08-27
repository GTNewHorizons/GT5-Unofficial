package tectech.voidcraft.uss;

/**
 * The render color of a USS star — the opaque ARGB int the star's mesh is tinted with (see
 * {@link USSStarDefinition#getColor()}), resolved from the star's registered definition. The shared star mesh is a
 * single texture; this color is what visually distinguishes the star classes (main sequence yellow, white dwarf
 * white, supermassive blue, ...).
 *
 * <p>
 * Pure data: unit-testable in the bare JVM.
 */
public final class USSStarColor {

    /** The legacy EoH orange ((1.0, 0.4, 0.05)) — the fallback when no definition carries a color. */
    public static final int DEFAULT = 0xFFFF660D;

    private USSStarColor() {
        throw new AssertionError("Static helpers");
    }

    /**
     * @param star a registered star definition (null → {@link #DEFAULT})
     * @return the star's opaque ARGB color
     */
    public static int colorFor(USSStarDefinition star) {
        return star == null ? DEFAULT : star.getColor();
    }
}
