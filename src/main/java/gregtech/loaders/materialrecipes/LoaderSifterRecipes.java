package gregtech.loaders.materialrecipes;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.List;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.util.GTLog;

/// Generates the sifter (crushed ore -> gem grade) recipe and the compressor (gem -> block) recipe for
/// MaterialLib's gem materials -- the canonical reader of [GTMaterialProperties#HAS_SIFTER_RECIPE].
///
/// [gregtech.loaders.oreprocessing.ProcessingGem] separately generates a compressor recipe for every gem
/// material through the shape-consumer path ([gregtech.loaders.shapeconsumers.ConsumerGem]) except RockSalt,
/// Salt, and Spodumene ("handled by the bartworks recipe loaders"). This loader registers the compressor recipe for
/// every
/// carrier uniformly, so for [#CARRIERS] other than RockSalt it is a second, redundant source of the same
/// recipe; recipe maps tolerate the duplicate entry.
///
/// [#CARRIERS] covers RockSalt (gated by [GTMaterialProperties#HAS_SIFTER_RECIPE] alone) and fourteen
/// materials whose [GTMaterialProperties#WERKSTOFF_PREFIXES] -- the narrower, dump-sourced prefix list the
/// bartworks facade honors, rather than every shape the unified material itself carries -- separately list
/// both `ore` and `dust`.
///
/// Salt and Spodumene also declare [GTMaterialProperties#HAS_SIFTER_RECIPE] and are likewise excluded from
/// [gregtech.loaders.oreprocessing.ProcessingGem]'s own generation, but they are deliberately not carriers:
/// the bartworks facade's `GenerationFeatures` state never honors their `gem` prefix, so no sifter or
/// compressor recipe exists for them, and declaring them here would introduce recipes those materials do not
/// otherwise have.
public final class LoaderSifterRecipes {

    private static final Material[] CARRIERS = { Materials2Materials.RockSalt, Materials2Materials.Bismutite,
        Materials2Materials.FluorBuergerite, Materials2Materials.ChromoAluminoPovondraite,
        Materials2Materials.VanadioOxyDravite, Materials2Materials.Olenite, Materials2Materials.RedZircon,
        Materials2Materials.Fayalite, Materials2Materials.Forsterite, Materials2Materials.Hedenbergite,
        Materials2Materials.Prasiolite, Materials2Materials.BArTiMaEuSNeK, Materials2Materials.Tiberium,
        Materials2Materials.Fluorspar, Materials2Materials.Orundum };

    private static final Shape[] COMPRESSOR_SHAPES = { Materials2Shapes.gem, Materials2BlockShapes.block };

    private static final Shape[] SIFTER_SHAPES = { Materials2Shapes.crushedPurified, Materials2Shapes.gemExquisite,
        Materials2Shapes.gemFlawless, Materials2Shapes.gem, Materials2Shapes.gemFlawed, Materials2Shapes.gemChipped,
        Materials2Shapes.dust };

    private LoaderSifterRecipes() {}

    public static void run() {
        for (Material material : CARRIERS) {
            if (!hasSifterGate(material)) {
                GTLog.err.println(
                    "LoaderSifterRecipes: declared carrier " + material.getName()
                        + " no longer satisfies the sifter gate");
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

    /// Whether `material` carries every one of `shapes`, logging the first it is missing. Checked separately
    /// from [#hasSifterGate] because that gate reads `ore` and `dust`, which say nothing about the gem grades:
    /// carrier-set membership is what makes those present, and this is what keeps the set honest.
    private static boolean declares(Material material, Shape[] shapes) {
        for (Shape shape : shapes) {
            if (!material.hasShape(shape)) {
                GTLog.err.println(
                    "LoaderSifterRecipes: declared carrier " + material.getName()
                        + " no longer carries "
                        + shape.getName());
                return false;
            }
        }
        return true;
    }

    private static void registerCompressor(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, Materials2Shapes.gem, 9))
            .itemOutputs(MaterialLibAPI.getStack(material, Materials2BlockShapes.block, 1))
            .duration(15 * SECONDS)
            .eut(recipeEU(material, 2))
            .addTo(compressorRecipes);
    }

    private static void registerSifter(Material material) {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(material, Materials2Shapes.crushedPurified, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(material, Materials2Shapes.gemExquisite, 1),
                MaterialLibAPI.getStack(material, Materials2Shapes.gemFlawless, 1),
                MaterialLibAPI.getStack(material, Materials2Shapes.gem, 1),
                MaterialLibAPI.getStack(material, Materials2Shapes.gemFlawed, 1),
                MaterialLibAPI.getStack(material, Materials2Shapes.gemChipped, 1),
                MaterialLibAPI.getStack(material, Materials2Shapes.dust, 1))
            .outputChances(200, 1000, 2500, 2000, 4000, 5000)
            .duration(40 * SECONDS)
            .eut(recipeEU(material, (int) (TierEU.RECIPE_LV / 2)))
            .addTo(sifterRecipes);
    }

    private static int recipeEU(Material material, int defaultEuPerTick) {
        Integer tier = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        return tier != null && tier != 0 ? tier : defaultEuPerTick;
    }
}
