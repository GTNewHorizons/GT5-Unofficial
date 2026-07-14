package gregtech.loaders.materialrecipes;

import static gregtech.api.enums.OrePrefixes.block;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.gem;
import static gregtech.api.enums.OrePrefixes.gemChipped;
import static gregtech.api.enums.OrePrefixes.gemExquisite;
import static gregtech.api.enums.OrePrefixes.gemFlawed;
import static gregtech.api.enums.OrePrefixes.gemFlawless;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.util.GTLog;

/// Generates the sifter (crushed ore -> gem grade) recipe and the compressor (gem -> block) recipe for
/// MaterialLib's gem materials -- the canonical reader of [GTMaterialProperties#HAS_SIFTER_RECIPE].
///
/// [gregtech.loaders.oreprocessing.ProcessingGem] separately generates a compressor recipe for every gem
/// material through the shape-consumer path ([gregtech.loaders.shapeconsumers.ConsumerGem]) except RockSalt,
/// Salt, and Spodumene ("handled by Werkstoff loader"). This loader registers the compressor recipe for every
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

    private LoaderSifterRecipes() {}

    public static void run() {
        for (Material material : CARRIERS) {
            if (!hasSifterGate(material)) {
                GTLog.err.println(
                    "LoaderSifterRecipes: declared carrier " + material.getName()
                        + " no longer satisfies the sifter gate");
                continue;
            }
            registerCompressor(material);
            registerSifter(material);
        }
    }

    private static boolean hasSifterGate(Material material) {
        if (Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_SIFTER_RECIPE))) return true;
        List<String> prefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
        return prefixes != null && prefixes.contains("ore") && prefixes.contains("dust");
    }

    private static void registerCompressor(Material material) {
        ItemStack gemInput = MU.stack(gem, material, 9);
        ItemStack blockOutput = MU.stack(block, material, 1);
        if (gemInput == null || blockOutput == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(gemInput)
            .itemOutputs(blockOutput)
            .duration(15 * SECONDS)
            .eut(recipeEU(material, 2))
            .addTo(compressorRecipes);
    }

    private static void registerSifter(Material material) {
        ItemStack crushedPurifiedInput = MU.stack(crushedPurified, material, 1);
        ItemStack gemExquisiteOutput = MU.stack(gemExquisite, material, 1);
        ItemStack gemFlawlessOutput = MU.stack(gemFlawless, material, 1);
        ItemStack gemOutput = MU.stack(gem, material, 1);
        ItemStack gemFlawedOutput = MU.stack(gemFlawed, material, 1);
        ItemStack gemChippedOutput = MU.stack(gemChipped, material, 1);
        ItemStack dustOutput = MU.stack(dust, material, 1);
        if (crushedPurifiedInput == null || gemExquisiteOutput == null
            || gemFlawlessOutput == null
            || gemOutput == null
            || gemFlawedOutput == null
            || gemChippedOutput == null
            || dustOutput == null) return;
        GTValues.RA.stdBuilder()
            .itemInputs(crushedPurifiedInput)
            .itemOutputs(
                gemExquisiteOutput,
                gemFlawlessOutput,
                gemOutput,
                gemFlawedOutput,
                gemChippedOutput,
                dustOutput)
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
