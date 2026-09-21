package gregtech.api.enums.materials;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialBuilder;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.MaterialRef;
import com.ruling_0.materiallib.api.TextureSet;

import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;

/// The five wildcard markers (`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`): materials
/// that back an ore-dictionary entry without carrying composition of their own, so an Iron ore entry also
/// answers to `AnyIron`. A crafting recipe naming one accepts any of the members;
/// [gregtech.api.enums.materials.RecognitionMaterials] holds the separate backings for foreign mods'
/// ore-dictionary names.
public class MaterialFacades {

    public static Material AnyCarbon;
    public static Material AnyCopper;
    public static Material AnyIron;
    public static Material AnyRubber;
    public static Material AnySyntheticRubber;

    private static Map<Material, List<Material>> oreReRegistrations;
    private static Map<Material, Material> wildcardResolutions;

    /// The material whose parts stand in for a wildcard marker wherever an item has to be produced, such as the
    /// recycling outputs of a composition holding the marker. `material` itself when it is not a wildcard.
    public static @Nullable Material resolveWildcard(@Nullable Material material) {
        if (material == null) return null;
        if (wildcardResolutions == null) {
            if (AnyIron == null) {
                throw new IllegalStateException("Wildcard resolution table consulted before MaterialSystem.init");
            }
            Map<Material, Material> m = new HashMap<>();
            m.put(AnyIron, Materials.Iron);
            m.put(AnyCopper, Materials.Copper);
            m.put(AnyCarbon, Materials.Carbon);
            m.put(AnyRubber, Materials.Rubber);
            m.put(AnySyntheticRubber, Materials.Silicone);
            wildcardResolutions = m;
        }
        return wildcardResolutions.getOrDefault(material, material);
    }

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

    /// Registers a shapeless MaterialLib [Material] backing each wildcard marker.
    public static void registerBackingMaterials() {
        AnyCopper = registerWildcard(
            "AnyCopper",
            "AnyCopper",
            "SHINY",
            GTMaterialFlag.METAL,
            MaterialRef.of(() -> Materials.Copper),
            MaterialRef.of(() -> Materials.Copper),
            MaterialRef.of(() -> Materials.AnnealedCopper));
        AnyCarbon = registerWildcard("AnyCarbon", "AnyCarbon", "DULL", null, null, null, null);
        AnyIron = registerWildcard(
            "AnyIron",
            "AnyIron",
            "SHINY",
            GTMaterialFlag.METAL,
            MaterialRef.of(() -> Materials.Iron),
            MaterialRef.of(() -> Materials.Iron),
            null);
        AnyRubber = registerWildcard(
            "AnyRubber",
            "AnyRubber",
            "SHINY",
            null,
            null,
            MaterialRef.of(() -> Materials.Rubber),
            null);
        AnySyntheticRubber = registerWildcard(
            "AnySyntheticRubber",
            "AnySyntheticRubber",
            "SHINY",
            null,
            null,
            null,
            null);
    }

    /// Registers one wildcard marker with the smelt/macerate/arc targets, the metal flag, and the
    /// non-unifiable setting it carries. No shape table adds to these materials.
    private static Material registerWildcard(String name, String localName, String texture, GTMaterialFlag flag,
        @Nullable MaterialRef smeltInto, @Nullable MaterialRef macerateInto, @Nullable MaterialRef arcSmeltInto) {
        MaterialBuilder builder = MaterialLibAPI.newMaterial("gregtech", name, TextureSet.of("gregtech", texture))
            .setProperty(GTMaterialProperties.LOCAL_NAME, localName)
            .setTint(0xFFFFFFFF)
            .setProperty(GTMaterialProperties.UNIFIABLE, false);
        if (flag != null) builder = builder.setProperty(GTMaterialProperties.FLAGS, EnumSet.of(flag));
        if (smeltInto != null) builder = builder.setProperty(GTMaterialProperties.SMELT_INTO, smeltInto);
        if (macerateInto != null) builder = builder.setProperty(GTMaterialProperties.MACERATE_INTO, macerateInto);
        if (arcSmeltInto != null) builder = builder.setProperty(GTMaterialProperties.ARC_SMELT_INTO, arcSmeltInto);
        return builder.build();
    }
}
