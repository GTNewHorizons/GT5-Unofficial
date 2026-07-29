package gregtech.api.enums.materials2;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.TextureSet;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialRef;

/// The shapeless marker backings for materials with no real ore/item/fluid data: the eleven superconductor
/// wire markers (`SuperconductorMV`..`SuperconductorUMV`) and the six wildcard markers
/// (`AnyBronze`/`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`). Held as declared fields
/// so call sites reference them directly instead of looking them up by registry name.
///
/// [#registerBackingMaterials] registers them during material registration (from
/// [gregtech.api.enums.Materials2#init], after [Materials2Materials#init]) and assigns the fields from the
/// same builders; `SuperconductorUHV` in particular must bind by builder reference, since its backing's
/// registry name is `Superconductor`, not `SuperconductorUHV`.
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

    private static final int DEFAULT_ARGB = 0x00ffffff;

    private static Set<Material> superconductorMarkers;

    private static Map<Material, List<Material>> oreReRegistrations;

    /// The wildcard markers an ore entry for `material` is additionally registered under, so an Iron ore entry
    /// also answers to `AnyIron`. Empty for a material with no wildcard alias.
    public static List<Material> oreReRegistrationsOf(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        if (oreReRegistrations == null) {
            Map<Material, List<Material>> m = new HashMap<>();
            m.put(Materials2Materials.Iron, Collections.singletonList(AnyIron));
            m.put(Materials2Materials.PigIron, Collections.singletonList(AnyIron));
            m.put(Materials2Materials.CastIron, Collections.singletonList(AnyIron));
            m.put(Materials2Materials.Copper, Collections.singletonList(AnyCopper));
            m.put(Materials2Materials.AnnealedCopper, Collections.singletonList(AnyCopper));
            m.put(Materials2Materials.Bronze, Collections.singletonList(AnyBronze));
            m.put(Materials2Materials.Rubber, Collections.singletonList(AnyRubber));
            m.put(Materials2Materials.StyreneButadieneRubber, Arrays.asList(AnyRubber, AnySyntheticRubber));
            m.put(Materials2Materials.Silicone, Arrays.asList(AnyRubber, AnySyntheticRubber));
            m.put(Materials2Materials.Carbon, Collections.singletonList(AnyCarbon));
            m.put(Materials2Materials.Coal, Collections.singletonList(AnyCarbon));
            m.put(Materials2Materials.Charcoal, Collections.singletonList(AnyCarbon));
            m.put(Materials2Materials.Lignite, Collections.singletonList(AnyCarbon));
            oreReRegistrations = m;
        }
        return oreReRegistrations.getOrDefault(material, Collections.emptyList());
    }

    private Materials2Markers() {}

    private record Backing(Consumer<Material> field, String internalName, String localName, int argb) {}

    // spotless:off
    private static final Backing[] SUPERCONDUCTOR_BACKINGS = {
        new Backing(m -> SuperconductorMV = m, "SuperconductorMV", "Superconductor MV", 0x00555555),
        new Backing(m -> SuperconductorHV = m, "SuperconductorHV", "Superconductor HV", 0x00331900),
        new Backing(m -> SuperconductorEV = m, "SuperconductorEV", "Superconductor EV", 0x00008700),
        new Backing(m -> SuperconductorIV = m, "SuperconductorIV", "Superconductor IV", 0x00330033),
        new Backing(m -> SuperconductorLuV = m, "SuperconductorLuV", "Superconductor LuV", 0x00994c00),
        new Backing(m -> SuperconductorZPM = m, "SuperconductorZPM", "Superconductor ZPM", 0x000a0a0a),
        new Backing(m -> SuperconductorUV = m, "SuperconductorUV", "Superconductor UV", 0x00e0d207),
        new Backing(m -> SuperconductorUHV = m, "Superconductor", "Superconductor UHV", 0x002681bd),
        new Backing(m -> SuperconductorUEV = m, "SuperconductorUEV", "Superconductor UEV", 0x00ae0808),
        new Backing(m -> SuperconductorUIV = m, "SuperconductorUIV", "Superconductor UIV", 0x00e558b1),
        new Backing(m -> SuperconductorUMV = m, "SuperconductorUMV", "Superconductor UMV", 0x00b526cd), };
    // spotless:on

    /// Registers a shapeless MaterialLib [Material] backing each superconductor marker, which
    /// `MaterialUtils#byLegacyName`
    /// resolves by registry name, and assigns the superconductor fields from the same builders --
    /// `SuperconductorUHV`'s backing name is `Superconductor`, so binding by name lookup would be wrong. A
    /// backing whose internal name already names a MaterialLib material is not re-registered; the field binds
    /// that existing material, matching `MaterialUtils#byLegacyName`'s registry fallback. Runs during material
    /// registration,
    /// after [Materials2Materials#init], so the skip check sees every real material.
    public static void registerBackingMaterials() {
        for (Backing sc : SUPERCONDUCTOR_BACKINGS) {
            Material existing = MaterialLibAPI.getMaterial("gregtech", sc.internalName());
            if (existing != null) {
                sc.field()
                    .accept(existing);
                continue;
            }
            sc.field()
                .accept(
                    MaterialLibAPI
                        .newMaterial(
                            "gregtech",
                            sc.internalName(),
                            com.ruling_0.materiallib.api.TextureSet.of("gregtech", TextureSet.SET_SHINY.mSetName))
                        .setProperty(GTMaterialProperties.LOCAL_NAME, sc.localName())
                        .setProperty(GTMaterialProperties.ARGB, sc.argb())
                        .build());
        }
        if (SuperconductorUHV == null || !"Superconductor".equals(SuperconductorUHV.getName())) {
            throw new IllegalStateException("SuperconductorUHV must bind the \"Superconductor\" backing material");
        }
        AnyBronze = registerWildcard(
            "AnyBronze",
            "AnyBronze",
            TextureSet.SET_SHINY,
            GTMaterialFlag.METAL,
            null,
            null,
            null);
        AnyCopper = registerWildcard(
            "AnyCopper",
            "AnyCopper",
            TextureSet.SET_SHINY,
            GTMaterialFlag.METAL,
            "Copper",
            "Copper",
            "AnnealedCopper");
        AnyCarbon = registerWildcard("AnyCarbon", "AnyCarbon", TextureSet.SET_DULL, null, null, null, null);
        AnyIron = registerWildcard(
            "AnyIron",
            "AnyIron",
            TextureSet.SET_SHINY,
            GTMaterialFlag.METAL,
            "Iron",
            "Iron",
            null);
        AnyRubber = registerWildcard("AnyRubber", "AnyRubber", TextureSet.SET_SHINY, null, null, "Rubber", null);
        AnySyntheticRubber = registerWildcard(
            "AnySyntheticRubber",
            "AnySyntheticRubber",
            TextureSet.SET_SHINY,
            null,
            null,
            null,
            null);
    }

    /// Registers a shapeless MaterialLib backing for a wildcard marker material (`AnyCopper`, `AnyIron`, ...),
    /// which `MaterialUtils#byLegacyName` resolves by [GTMaterialProperties#LEGACY_NAME]. Ports the smelt/macerate/arc
    /// targets, the metal flag, and `setUnifiable(false)` that every wildcard marker carries; a name that
    /// already names a real MaterialLib material is returned as-is instead of re-registered.
    private static Material registerWildcard(String name, String localName, TextureSet texture, GTMaterialFlag flag,
        String smeltInto, String macerateInto, String arcSmeltInto) {
        Material existing = MaterialLibAPI.getMaterial("gregtech", name);
        if (existing != null) return existing;
        com.ruling_0.materiallib.api.MaterialBuilder builder = MaterialLibAPI
            .newMaterial("gregtech", name, com.ruling_0.materiallib.api.TextureSet.of("gregtech", texture.mSetName))
            .setProperty(GTMaterialProperties.LOCAL_NAME, localName)
            .setProperty(GTMaterialProperties.ARGB, DEFAULT_ARGB)
            .setProperty(GTMaterialProperties.UNIFIABLE, false);
        if (flag != null) builder = builder.setProperty(GTMaterialProperties.FLAGS, EnumSet.of(flag));
        if (smeltInto != null)
            builder = builder.setProperty(GTMaterialProperties.SMELT_INTO, new MaterialRef(smeltInto));
        if (macerateInto != null)
            builder = builder.setProperty(GTMaterialProperties.MACERATE_INTO, new MaterialRef(macerateInto));
        if (arcSmeltInto != null)
            builder = builder.setProperty(GTMaterialProperties.ARC_SMELT_INTO, new MaterialRef(arcSmeltInto));
        return builder.build();
    }

    /// The eleven superconductor wire marker backings, in tier order. The markers fall outside the legacy
    /// name domain ([gregtech.loaders.materials.LegacyNameDomain]), so the legacy-named lang-registration
    /// pass in `gregtech.loaders.preload.GTPreLoad` skips them; their display-name keys are registered from
    /// this array instead. Evaluated after [#registerBackingMaterials] has populated the fields.
    public static Material[] getSuperconductorMarkers() {
        return new Material[] { SuperconductorMV, SuperconductorHV, SuperconductorEV, SuperconductorIV,
            SuperconductorLuV, SuperconductorZPM, SuperconductorUV, SuperconductorUHV, SuperconductorUEV,
            SuperconductorUIV, SuperconductorUMV };
    }

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
}
