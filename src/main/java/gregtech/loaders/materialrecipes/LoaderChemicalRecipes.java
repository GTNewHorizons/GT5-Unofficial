package gregtech.loaders.materialrecipes;

import static gregtech.GTLoggers.GT_FML_LOGGER;

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
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;

/// Generates the chemical-reactor recipe (composition -> dust) for MaterialLib materials -- the canonical
/// reader of [GTMaterialProperties#HAS_CHEMICAL_RECIPE]. Registers through [GTRecipeConstants#UniversalChemical],
/// which itself fans a single recipe out to both the single-block chemical reactor and the multiblock large
/// chemical reactor.
///
/// Each carrier's [GTMaterialProperties#COMPOSITION] entries resolve to a dust input per entry
/// ([MaterialUtils#compositionDust]), except a single gas-only entry (e.g. Oxygen), which resolves to a fluid
/// input ([MaterialUtils#compositionGas]). [#CARRIERS] never declares more than one such entry, so this
/// carries no cell-item byproduct accounting. Duration and EU scale off the carrier's own
/// [MaterialAtomics#protons] and [GTMaterialProperties#PROCESSING_MATERIAL_TIER_EU], divided/multiplied by its
/// composition's entry count.
public final class LoaderChemicalRecipes {

    private static final Material[] CARRIERS = { Materials.BismuthTelluride, Materials.Dibismuthhydroborat,
        Materials.PotassiumDisulfate };

    private LoaderChemicalRecipes() {}

    public static void run() {
        for (Material material : CARRIERS) {
            if (!Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_CHEMICAL_RECIPE))) {
                GT_FML_LOGGER.error(
                    "LoaderChemicalRecipes: declared carrier {} no longer declares HAS_CHEMICAL_RECIPE",
                    material.getName());
                continue;
            }
            if (!material.hasShape(Shapes.dust)) {
                GT_FML_LOGGER
                    .error("LoaderChemicalRecipes: declared carrier {} no longer carries dust", material.getName());
                continue;
            }
            registerChemical(material);
        }
    }

    private static void registerChemical(Material material) {
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) {
            GT_FML_LOGGER.error("LoaderChemicalRecipes: {} has no composition to build from", material.getName());
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
                    "LoaderChemicalRecipes: {} has an unsupported non-dust composition entry: {}",
                    material.getName(),
                    entry.material()
                        .resolve()
                        .getName());
                return;
            }
            fluidInput = gasInput;
        }

        int componentCount = composition.size();
        GTValues.RA.stdBuilder()
            .itemInputs(itemInputs.toArray(new ItemStack[0]))
            .itemOutputs(output)
            .fluidInputs(fluidInput == null ? new FluidStack[0] : new FluidStack[] { fluidInput })
            .duration((int) Math.max(1L, Math.abs(MaterialAtomics.protons(material) / componentCount)))
            .eut(GTUtility.calculateRecipeEU(material, Math.min(4, componentCount) * 30))
            .addTo(GTRecipeConstants.UniversalChemical);
    }
}
