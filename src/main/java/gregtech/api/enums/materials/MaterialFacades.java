package gregtech.api.enums.materials;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.TextureSet;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialRef;

/// The six wildcard markers (`AnyBronze`/`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`):
/// materials that back an ore-dictionary entry without carrying composition of their own, so an Iron ore entry
/// also answers to `AnyIron`. Held as declared fields so call sites reference them directly instead of looking
/// them up by registry name. [gregtech.api.enums.materials.RecognitionMaterials] holds the separate backings
/// for foreign mods' ore-dictionary names.
public class MaterialFacades {

    public static Material AnyBronze;
    public static Material AnyCarbon;
    public static Material AnyCopper;
    public static Material AnyIron;
    public static Material AnyRubber;
    public static Material AnySyntheticRubber;

    private static final int DEFAULT_ARGB = 0x00ffffff;

    private static Map<Material, List<Material>> oreReRegistrations;

    /// The wildcard markers an ore entry for `material` is additionally registered under. Empty for a material
    /// with no wildcard alias.
    public static List<Material> oreReRegistrationsOf(@Nullable Material material) {
        if (material == null) return Collections.emptyList();
        if (oreReRegistrations == null) {
            if (Materials.Iron == null) {
                throw new IllegalStateException("Ore re-registration table consulted before MaterialSystem.init");
            }
            Map<Material, List<Material>> m = new HashMap<>();
            m.put(Materials.Iron, Collections.singletonList(AnyIron));
            m.put(Materials.PigIron, Collections.singletonList(AnyIron));
            m.put(Materials.CastIron, Collections.singletonList(AnyIron));
            m.put(Materials.Copper, Collections.singletonList(AnyCopper));
            m.put(Materials.AnnealedCopper, Collections.singletonList(AnyCopper));
            m.put(Materials.Bronze, Collections.singletonList(AnyBronze));
            m.put(Materials.Rubber, Collections.singletonList(AnyRubber));
            m.put(Materials.StyreneButadieneRubber, Arrays.asList(AnyRubber, AnySyntheticRubber));
            m.put(Materials.Silicone, Arrays.asList(AnyRubber, AnySyntheticRubber));
            m.put(Materials.Carbon, Collections.singletonList(AnyCarbon));
            m.put(Materials.Coal, Collections.singletonList(AnyCarbon));
            m.put(Materials.Charcoal, Collections.singletonList(AnyCarbon));
            m.put(Materials.Lignite, Collections.singletonList(AnyCarbon));
            oreReRegistrations = m;
        }
        return oreReRegistrations.getOrDefault(material, Collections.emptyList());
    }

    private MaterialFacades() {}

    /// Registers a shapeless MaterialLib [Material] backing each wildcard marker, which
    /// [gregtech.api.material.MaterialUtils#byLegacyName] resolves by registry name.
    public static void registerBackingMaterials() {
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

    /// Ports the smelt/macerate/arc targets, the metal flag, and `setUnifiable(false)` a wildcard marker
    /// carries. No shape table adds to these materials, which is what keeps them shapeless.
    private static Material registerWildcard(String name, String localName, TextureSet texture, GTMaterialFlag flag,
        String smeltInto, String macerateInto, String arcSmeltInto) {
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
}
