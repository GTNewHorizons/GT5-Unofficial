package gregtech.loaders.materialrecipes;

import static gregtech.api.enums.OrePrefixes.dust;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialAtomics;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTRecipeConstants;

/// Generates the chemical-reactor recipe (composition -> dust) for MaterialLib materials -- the canonical
/// reader of [GTMaterialProperties#HAS_CHEMICAL_RECIPE]. Registers through [GTRecipeConstants#UniversalChemical],
/// which itself fans a single recipe out to both the single-block chemical reactor and the multiblock large
/// chemical reactor.
///
/// Each carrier's [GTMaterialProperties#COMPOSITION] entries resolve to a dust input per entry
/// ([MU#compositionDust]), except a single gas-only entry (e.g. Oxygen), which resolves to a fluid input
/// instead ([MU#compositionGas]) rather than an item; [#CARRIERS] never declares more than one such entry, so
/// this carries no cell-item byproduct accounting. Duration and EU scale off the carrier's own
/// [MaterialAtomics#protons] and [GTMaterialProperties#PROCESSING_MATERIAL_TIER_EU], divided/multiplied by its
/// composition's entry count.
public final class LoaderChemicalRecipes {

    private static final Material[] CARRIERS = { Materials2Materials.BismuthTelluride,
        Materials2Materials.Dibismuthhydroborat, Materials2Materials.PotassiumDisulfate };

    private LoaderChemicalRecipes() {}

    public static void run() {
        for (Material material : CARRIERS) {
            if (!Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.HAS_CHEMICAL_RECIPE))) {
                GTLog.err.println(
                    "LoaderChemicalRecipes: declared carrier " + material.getName()
                        + " no longer declares HAS_CHEMICAL_RECIPE");
                continue;
            }
            registerChemical(material);
        }
    }

    private static void registerChemical(Material material) {
        List<MaterialRefStack> composition = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composition == null || composition.isEmpty()) {
            GTLog.err.println("LoaderChemicalRecipes: " + material.getName() + " has no composition to build from");
            return;
        }
        long totalAmount = 0;
        for (MaterialRefStack entry : composition) totalAmount += entry.amount();
        ItemStack output = MU.stack(dust, material, totalAmount);
        if (output == null) return;

        List<ItemStack> itemInputs = new ArrayList<>();
        FluidStack fluidInput = null;
        for (MaterialRefStack entry : composition) {
            ItemStack dustInput = MU.compositionDust(entry);
            if (dustInput != null) {
                itemInputs.add(dustInput);
                continue;
            }
            FluidStack gasInput = MU.compositionGas(entry);
            if (gasInput == null || fluidInput != null) {
                GTLog.err.println(
                    "LoaderChemicalRecipes: " + material.getName()
                        + " has an unsupported non-dust composition entry: "
                        + entry.material()
                            .name());
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
            .eut(recipeEU(material, Math.min(4, componentCount) * 30))
            .addTo(GTRecipeConstants.UniversalChemical);
    }

    private static int recipeEU(Material material, int defaultEuPerTick) {
        Integer tier = material.getProperty(GTMaterialProperties.PROCESSING_MATERIAL_TIER_EU);
        return tier != null && tier != 0 ? tier : defaultEuPerTick;
    }
}
