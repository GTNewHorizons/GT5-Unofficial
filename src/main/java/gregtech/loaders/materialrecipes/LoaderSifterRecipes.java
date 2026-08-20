package gregtech.loaders.materialrecipes;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.List;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.util.GTUtility;

/// Generates the sifter (crushed ore -> gem grade) recipe and the compressor (gem -> block) recipe for
/// MaterialLib's gem materials -- the canonical reader of [GTMaterialProperties#HAS_SIFTER_RECIPE].
///
/// The compressor recipe is registered for every carrier uniformly, so for [#CARRIERS] other than RockSalt it
/// duplicates the one [gregtech.loaders.oreprocessing.ProcessingGem] already generates. Recipe maps tolerate
/// the duplicate entry.
///
/// [#CARRIERS] covers RockSalt, gated by [GTMaterialProperties#HAS_SIFTER_RECIPE] alone, plus the materials
/// whose [GTMaterialProperties#WERKSTOFF_PREFIXES] list both `ore` and `dust`. Salt and Spodumene declare
/// [GTMaterialProperties#HAS_SIFTER_RECIPE] but are deliberately not carriers: neither has a sifter or a
/// compressor recipe.
public final class LoaderSifterRecipes {

    private static final Material[] CARRIERS = { Materials.RockSalt, Materials.Bismutite, Materials.FluorBuergerite,
        Materials.ChromoAluminoPovondraite, Materials.VanadioOxyDravite, Materials.Olenite, Materials.RedZircon,
        Materials.Fayalite, Materials.Forsterite, Materials.Hedenbergite, Materials.Prasiolite, Materials.BArTiMaEuSNeK,
        Materials.Tiberium, Materials.Fluorspar, Materials.Orundum };

    private static final Shape[] COMPRESSOR_SHAPES = { Shapes.gem, BlockShapes.block };

    private static final Shape[] SIFTER_SHAPES = { Shapes.crushedPurified, Shapes.gemExquisite, Shapes.gemFlawless,
        Shapes.gem, Shapes.gemFlawed, Shapes.gemChipped, Shapes.dust };

    private LoaderSifterRecipes() {}

    /// Whether [#run] generates `material`'s sifter recipe, so that
    /// [gregtech.loaders.oreprocessing.ProcessingCrushedOre] can leave the `crushedPurified` sifter recipe to
    /// this loader rather than register a second one with different gem chances.
    public static boolean ownsSifterRecipe(Material material) {
        for (Material carrier : CARRIERS) {
            if (carrier == material) return true;
        }
        return false;
    }

    public static void run() {
        for (Material material : CARRIERS) {
            if (!hasSifterGate(material)) {
                GT_FML_LOGGER.error(
                    "LoaderSifterRecipes: declared carrier {} no longer satisfies the sifter gate",
                    material.getName());
                continue;
            }
            if (declares(material, COMPRESSOR_SHAPES)) registerCompressor(material);
            if (declares(material, SIFTER_SHAPES)) registerSifter(material);
        }
    }

    private static boolean hasSifterGate(Material material) {
        if (Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_SIFTER_RECIPE))) return true;
        List<String> prefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
        return prefixes != null && prefixes.contains("ore") && prefixes.contains("dust");
    }

    /// Whether `material` carries every one of `shapes`.
    private static boolean declares(Material material, Shape[] shapes) {
        for (Shape shape : shapes) {
            if (!material.hasShape(shape)) {
                GT_FML_LOGGER.error(
                    "LoaderSifterRecipes: declared carrier {} no longer carries {}",
                    material.getName(),
                    shape.getName());
                return false;
            }
        }
        return true;
    }

    private static void registerCompressor(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, Shapes.gem, 9))
            .itemOutputs(MaterialLibAPI.getStack(material, BlockShapes.block, 1))
            .duration(15 * SECONDS)
            .eut(GTUtility.calculateRecipeEU(material, 2))
            .addTo(compressorRecipes);
    }

    private static void registerSifter(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, Shapes.crushedPurified, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(material, Shapes.gemExquisite, 1),
                MaterialLibAPI.getStack(material, Shapes.gemFlawless, 1),
                MaterialLibAPI.getStack(material, Shapes.gem, 1),
                MaterialLibAPI.getStack(material, Shapes.gemFlawed, 1),
                MaterialLibAPI.getStack(material, Shapes.gemChipped, 1),
                MaterialLibAPI.getStack(material, Shapes.dust, 1))
            .outputChances(200, 1000, 2500, 2000, 4000, 5000)
            .duration(40 * SECONDS)
            .eut(GTUtility.calculateRecipeEU(material, TierEU.RECIPE_LV / 2))
            .addTo(sifterRecipes);
    }
}
