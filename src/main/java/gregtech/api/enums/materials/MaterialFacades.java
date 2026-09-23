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
import com.ruling_0.materiallib.api.TextureSet;

import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;

/// The five wildcard markers (`AnyCopper`/`AnyCarbon`/`AnyIron`/`AnyRubber`/`AnySyntheticRubber`): materials
/// that back an ore-dictionary entry without carrying composition of their own, so an Iron ore entry also
/// answers to `AnyIron` and a crafting recipe naming `AnyIron` accepts any of the members.
///
/// Naming a crafting ingredient is all a wildcard ever does. [gregtech.api.objects.ItemData] resolves one to a
/// concrete member the moment it merges materials into a composition ([#resolveWildcard]), so no composition,
/// and hence nothing the recycling registrators read, ever holds one.
public class MaterialFacades {

    public static Material AnyCarbon;
    public static Material AnyCopper;
    public static Material AnyIron;
    public static Material AnyRubber;
    public static Material AnySyntheticRubber;

    private static Map<Material, List<Material>> oreReRegistrations;
    private static Map<Material, Material> wildcardResolutions;

    /// The concrete member a wildcard marker stands for, or `material` itself when it is not a wildcard.
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

    /// Whether `material` is one of the five wildcard markers.
    public static boolean isWildcard(@Nullable Material material) {
        return material != null && resolveWildcard(material) != material;
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
        AnyCopper = registerWildcard("AnyCopper", "SHINY", GTMaterialFlag.METAL);
        AnyCarbon = registerWildcard("AnyCarbon", "DULL", null);
        AnyIron = registerWildcard("AnyIron", "SHINY", GTMaterialFlag.METAL);
        AnyRubber = registerWildcard("AnyRubber", "SHINY", null);
        AnySyntheticRubber = registerWildcard("AnySyntheticRubber", "SHINY", null);
    }

    /// Registers one wildcard marker with the metal flag and the non-unifiable setting it carries. No shape
    /// table adds to these materials.
    private static Material registerWildcard(String name, String texture, @Nullable GTMaterialFlag flag) {
        MaterialBuilder builder = MaterialLibAPI.newMaterial("gregtech", name, TextureSet.of("gregtech", texture))
            .setProperty(GTMaterialProperties.LOCAL_NAME, name)
            .setTint(0xFFFFFFFF)
            .setProperty(GTMaterialProperties.UNIFIABLE, false);
        if (flag != null) builder = builder.setProperty(GTMaterialProperties.FLAGS, EnumSet.of(flag));
        return builder.build();
    }
}
