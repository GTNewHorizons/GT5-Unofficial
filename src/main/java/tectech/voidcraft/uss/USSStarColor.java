package tectech.voidcraft.uss;

import java.util.EnumSet;
import java.util.Set;

/**
 * The render colors of a USS star — the opaque ARGB ints the star's render mesh is tinted with (see
 * {@link USSStarDefinition#getColor()} / {@link USSStarDefinition#getShellColor()}), resolved from the star's
 * registered definition. The shared star mesh is a single texture; these colors are what visually distinguishes the
 * star classes (yellow dwarf orange core, white dwarf light-blue core with a white halo, ...).
 *
 * <p>
 * Pure data: unit-testable in the bare JVM.
 */
public final class USSStarColor {

    /** The legacy EoH orange ((1.0, 0.4, 0.05)) — the fallback when no definition carries a color. */
    public static final int DEFAULT = 0xFFFF660D;

    /**
     * The star classes whose shell layers render outside-in as a glow ring beyond the core's rim (the halo
     * treatment — EOHRenderingUtils).
     */
    private static final Set<USSStarType> HALO_STAR_TYPES = EnumSet
        .of(USSStarType.BLACK_DWARF, USSStarType.BLACK_HOLE, USSStarType.GRAVASTAR);

    private USSStarColor() {
        throw new AssertionError("Static helpers");
    }

    /**
     * @param star a registered star definition (null → {@link #DEFAULT})
     * @return the star's opaque ARGB core color
     */
    public static int colorFor(USSStarDefinition star) {
        return star == null ? DEFAULT : star.getColor();
    }

    /**
     * @param star a registered star definition (null → {@link #DEFAULT})
     * @return the star's opaque ARGB shell color — the registered shell when set, the core color when unset (0)
     */
    public static int shellColorFor(USSStarDefinition star) {
        if (star == null) {
            return DEFAULT;
        }
        return star.getShellColor() != 0 ? star.getShellColor() : star.getColor();
    }

    /**
     * @param star a registered star definition (null → {@link USSStarRenderType#STANDARD})
     * @return the star's custom render treatment (the extra geometry drawn on top of the shared three-layer sphere)
     */
    public static USSStarRenderType renderTypeFor(USSStarDefinition star) {
        return star == null ? USSStarRenderType.STANDARD : star.getRenderType();
    }

    /**
     * @param starType the star's class (null → false)
     * @return whether the star's shell layers render outside-in as a glow ring (the halo treatment)
     */
    public static boolean isHaloStar(USSStarType starType) {
        return starType != null && HALO_STAR_TYPES.contains(starType);
    }
}
