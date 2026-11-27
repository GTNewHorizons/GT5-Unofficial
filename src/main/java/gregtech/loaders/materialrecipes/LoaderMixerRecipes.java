package gregtech.loaders.materialrecipes;

import static gregtech.GTLoggers.GT_FML_LOGGER;
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
import gregtech.api.util.GTUtility;

/// Generates mixer recipes (composition -> dust) for the [#CARRIERS] that declare
/// [GTMaterialProperties#HAS_MIXER_RECIPE].
///
/// Each recipe mixes the smallest whole batch of the [MaterialUtils#reducedComposition]. Each entry becomes a dust
/// input. At most one gas-only entry (e.g. Hydrogen) is supported, and it becomes a fluid input.
/// [GTMaterialProperties#MIX_CIRCUIT] adds an integrated circuit input when set to 1 or higher. Duration scales
/// with the carrier's [MaterialAtomics#mass], and EU with its [GTMaterialProperties#PROCESSING_MATERIAL_TIER_EU].
public final class LoaderMixerRecipes {

    private static final Material[] CARRIERS = { Materials.CircuitCompoundMK3, Materials.MagnetoResonatic,
        Materials.RhodiumPlatedPalladium, Materials.Ruridit, Materials.HighDurabilityCompoundSteel,
        Materials.RawAdemicSteel, Materials.GraphiteUraniumMixture, Materials.UraniumCarbideThoriumMixture,
        Materials.PlutoniumOxideUraniumMixture, Materials.Zircaloy4, Materials.Zircaloy2, Materials.Incoloy903,
        Materials.AdamantiumAlloy, Materials.MARM200Steel, Materials.ArtheriumSn, Materials.TanmolyiumBetaC,
        Materials.Dalisenite, Materials.Hikarium, Materials.Tairitsu, Materials.PreciousMetalsAlloy,
        Materials.EnrichedNaquadahAlloy, Materials.Permalloy, Materials.Arcanite, Materials.BabbitAlloy,
        Materials.BlackMetal, Materials.BloodSteel, Materials.Botmium, Materials.EglinSteel,
        Materials.EglinSteelBaseCompound, Materials.EnergyCrystal, Materials.Incoloy020, Materials.IncoloyDS,
        Materials.IncoloyMA956, Materials.Inconel690, Materials.Inconel792, Materials.NiobiumCarbide,
        Materials.Nitinol60, Materials.Potin, Materials.SiliconCarbide, Materials.Staballoy, Materials.Stellite,
        Materials.Talonite, Materials.Tantalloy60, Materials.Tantalloy61, Materials.TantalumCarbide,
        Materials.Titansteel, Materials.TriniumNaquadahAlloy, Materials.TriniumNaquadahCarbonite,
        Materials.TriniumTitaniumAlloy, Materials.Tumbaga, Materials.TungstenTitaniumCarbide, Materials.WoodsGlass,
        Materials.ZirconiumCarbide };

    private LoaderMixerRecipes() {}

    public static void run() {
        for (Material material : CARRIERS) {
            if (!Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_MIXER_RECIPE))) {
                GT_FML_LOGGER.error(
                    "LoaderMixerRecipes: declared carrier {} does not declare HAS_MIXER_RECIPE",
                    material.getName());
                continue;
            }
            if (!material.hasShape(Shapes.dust)) {
                GT_FML_LOGGER.error("LoaderMixerRecipes: declared carrier {} has no dust shape", material.getName());
                continue;
            }
            registerMixer(material);
        }
    }

    private static void registerMixer(Material material) {
        List<MaterialRefStack> composition = MaterialUtils.reducedComposition(material);
        if (composition.isEmpty()) {
            GT_FML_LOGGER.error("LoaderMixerRecipes: {} has no composition to mix from", material.getName());
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
                GT_FML_LOGGER.error(
                    "LoaderMixerRecipes: {} has an unsupported non-dust composition entry: {}",
                    material.getName(),
                    entry.material()
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
            .eut(GTUtility.calculateRecipeEU(material, Math.min(4, componentCount) * 5))
            .addTo(mixerRecipes);
    }
}
