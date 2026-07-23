package gregtech.api.enums.materials2;

import java.util.Set;

import com.ruling_0.materiallib.api.Material;

/// The shapeless marker backings [gregtech.loaders.materials.LegacyMarkerMaterials#registerBackingMaterials]
/// registers, held as declared fields so call sites reference them directly instead of looking them up by
/// registry name. Assigned there, during material registration, from the same builders that register the
/// backings; `SuperconductorUHV` in particular must bind by builder reference, since its backing's registry
/// name is `Superconductor`, not `SuperconductorUHV`.
public class Materials2Markers {

    public static Material AnyBronze;
    public static Material AnyCarbon;
    public static Material AnyCopper;
    public static Material AnyIron;
    public static Material AnyRubber;
    public static Material AnySyntheticRubber;

    public static Material SuperconductorMV;
    public static Material SuperconductorHV;
    public static Material SuperconductorEV;
    public static Material SuperconductorIV;
    public static Material SuperconductorLuV;
    public static Material SuperconductorZPM;
    public static Material SuperconductorUV;
    public static Material SuperconductorUHV;
    public static Material SuperconductorUEV;
    public static Material SuperconductorUIV;
    public static Material SuperconductorUMV;

    private static Set<Material> superconductorMarkers;

    /// Whether `material` is one of the eleven superconductor wire marker backings. The set is built lazily
    /// so callers constructed before the marker fields are assigned can still hold a reference to this check.
    public static boolean isSuperconductorMarker(Material material) {
        if (superconductorMarkers == null) {
            superconductorMarkers = Set.of(
                SuperconductorMV,
                SuperconductorHV,
                SuperconductorEV,
                SuperconductorIV,
                SuperconductorLuV,
                SuperconductorZPM,
                SuperconductorUV,
                SuperconductorUHV,
                SuperconductorUEV,
                SuperconductorUIV,
                SuperconductorUMV);
        }
        return superconductorMarkers.contains(material);
    }

    private Materials2Markers() {}
}
