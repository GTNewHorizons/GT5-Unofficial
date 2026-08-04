package gregtech.loaders.materialrecipes;

import static gregtech.api.recipe.RecipeMaps.mixerRecipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialAtomics;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTUtility;

/// Generates the Mixer recipe (composition -> dust) for MaterialLib materials -- the canonical reader of
/// [GTMaterialProperties#HAS_MIXER_RECIPE].
///
/// Each carrier's [GTMaterialProperties#COMPOSITION] entries resolve to a dust input per entry
/// ([MaterialUtils#compositionDust]), except a single gas-only entry (e.g. Hydrogen, Oxygen), which resolves to a fluid
/// input instead ([MaterialUtils#compositionGas]) rather than an item; [#CARRIERS] never declares more than one such
/// entry, so this carries no cell-item byproduct accounting. [GTMaterialProperties#MIX_CIRCUIT] adds an
/// integrated-circuit input when set to 1 or higher. Duration and EU scale off the carrier's own
/// [MaterialAtomics#mass] and [GTMaterialProperties#PROCESSING_MATERIAL_TIER_EU], divided/multiplied by its
/// composition's entry count.
public final class LoaderMixerRecipes {

    private static final Material[] CARRIERS = { Materials.CircuitCompoundMK3, Materials.MagnetoResonatic,
        Materials.RhodiumPlatedPalladium, Materials.Ruridit, Materials.HighDurabilityCompoundSteel,
        Materials.RawAdemicSteel, Materials.GraphiteUraniumMixture, Materials.UraniumCarbideThoriumMixture,
        Materials.PlutoniumOxideUraniumMixture, Materials.Zircaloy4, Materials.Zircaloy2, Materials.Incoloy903,
        Materials.AdamantiumAlloy, Materials.MARM200Steel, Materials.ArtheriumSn, Materials.TanmolyiumBetaC,
        Materials.Dalisenite, Materials.Hikarium, Materials.Tairitsu, Materials.PreciousMetalsAlloy,
        Materials.EnrichedNaquadahAlloy, Materials.Permalloy };

    private LoaderMixerRecipes() {}

    public static void run() {
        for (Material material : CARRIERS) {
            if (!Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_MIXER_RECIPE))) {
                GTLog.err.println(
                    "LoaderMixerRecipes: declared carrier " + material.getName()
                        + " no longer declares HAS_MIXER_RECIPE");
                continue;
            }
            if (!material.hasShape(Shapes.dust)) {
                GTLog.err
                    .println("LoaderMixerRecipes: declared carrier " + material.getName() + " no longer carries dust");
                continue;
            }
            registerMixer(material);
        }
    }

    private static void registerMixer(Material material) {
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) {
            GTLog.err.println("LoaderMixerRecipes: " + material.getName() + " has no composition to mix from");
            return;
        }
        long totalAmount = 0;
        for (MaterialRefStack entry : composition) totalAmount += entry.amount();
        ItemStack output = MaterialLibAPI.getStack(material, Shapes.dust, (int) totalAmount);

        List<ItemStack> itemInputs = new ArrayList<>();
        FluidStack fluidInput = null;
        for (MaterialRefStack entry : composition) {
            ItemStack dustInput = MaterialUtils.compositionDust(entry);
            if (dustInput != null) {
                itemInputs.add(dustInput);
                continue;
            }
            FluidStack gasInput = MaterialUtils.compositionGas(entry);
            if (gasInput == null || fluidInput != null) {
                GTLog.err.println(
                    "LoaderMixerRecipes: " + material.getName()
                        + " has an unsupported non-dust composition entry: "
                        + entry.material()
                            .resolve()
                            .getName());
                return;
            }
            fluidInput = gasInput;
        }

        Integer circuitId = material.getProperty(GTMaterialProperties.MIX_CIRCUIT);
        if (circuitId != null && circuitId >= 1) itemInputs.add(GTUtility.getIntegratedCircuit(circuitId));

        int componentCount = composition.size();
        GTValues.RA.stdBuilder()
            .itemInputs(itemInputs.toArray(new ItemStack[0]))
            .itemOutputs(output)
            .fluidInputs(fluidInput == null ? new FluidStack[0] : new FluidStack[] { fluidInput })
            .duration((int) Math.max(1L, Math.abs(MaterialAtomics.mass(material) / componentCount)))
            .eut(recipeEU(material, Math.min(4, componentCount) * 5))
            .addTo(mixerRecipes);
    }

    private static int recipeEU(Material material, int defaultEuPerTick) {
        Integer tier = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        return tier != null && tier != 0 ? tier : defaultEuPerTick;
    }
}
