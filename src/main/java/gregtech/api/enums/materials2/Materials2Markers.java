package gregtech.api.enums.materials2;

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

    private Materials2Markers() {}
}
