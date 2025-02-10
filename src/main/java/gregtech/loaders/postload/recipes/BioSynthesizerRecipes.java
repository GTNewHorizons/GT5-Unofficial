package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_UV;
import static gregtech.api.enums.TierEU.RECIPE_ZPM;
import static gregtech.api.recipe.RecipeMaps.bioProgrammerRecipes;
import static gregtech.api.recipe.RecipeMaps.bioSynthesizerRecipes;
import static gregtech.api.recipe.RecipeMaps.brewingRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AO_DATA;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.recipe.AORecipeData;

public class BioSynthesizerRecipes implements Runnable {

    // All AO-related recipes will temporarily live here before I finalize the branch and move things over to coremod.

    /**
     * This is an AO Unit - all recipes should use .metadata(AO_DATA, new AORecipeData(x, y, z)) or the multiblock
     * will not use AOs in its logic.
     * AORecipeData constructors, in order:
     * - requiredIntelligence: AO population intelligence required to run this recipe.
     * - requiredCount: Number of AOs that will be drained at the recipe start.
     * - dangerLevel: This is the percentage of AOs that will die while running the recipe. Use values from 0-100 only.
     */

    @Override
    public void run() {

        // MIXER

        // Nutrient Paste
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.MeatRaw.getDust(1), new ItemStack(Items.sugar, 1), ItemList.IC2_Plantball.get(1))
            .itemOutputs(ItemList.Nutrient_Paste.get(3))
            .duration(8 * SECONDS)
            .eut(RECIPE_LV)
            .addTo(mixerRecipes);

        // Neural Fluid
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Neuron_Cell_Cluster.get(32))
            .fluidInputs(Materials.NutrientBroth.getFluid(8000))
            .fluidOutputs(Materials.NeuralFluid.getFluid(8000))
            .duration(15 * SECONDS)
            .eut(RECIPE_UV)
            .noOptimize()
            .addTo(mixerRecipes);

        // BREWERY

        // Nutrient Broth
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Nutrient_Paste.get(64))
            .fluidInputs(getFluidStack("potion.mineralwater", 32000))
            .fluidOutputs(Materials.NutrientBroth.getFluid(32000))
            .duration(60 * SECONDS)
            .eut(RECIPE_LV)
            .noOptimize()
            .addTo(brewingRecipes);

        // SYNTHESIZER

        // Neurogel Substrate
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.slime_ball, 1))
            .fluidInputs(Materials.NeuralFluid.getFluid(1000))
            .itemOutputs(ItemList.Neurogel_Substrate.get(1))
            .duration(20 * SECONDS)
            .eut(RECIPE_ZPM)
            .metadata(AO_DATA, new AORecipeData(4, 200, 10))
            .addTo(bioSynthesizerRecipes);

        // Circuit Tissue
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polybenzimidazole, 2),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64),
                ItemList.Skin_Cell_Cluster.get(64))
            .itemOutputs(ItemList.Circuit_Tissue.get(1))
            .duration(10 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(2, 1000, 30))
            .addTo(bioSynthesizerRecipes);

        // PROGRAMMER

        // Neural Interface
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Muscle_Cell_Cluster.get(64),
                ItemList.Neuron_Cell_Cluster.get(16),
                ItemList.Neurogel_Substrate.get(4),
                ItemList.Circuit_Tissue.get(1))
            .itemOutputs(ItemList.Neural_Interface.get(1))
            .duration(10 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(9, 2000, 10))
            .addTo(bioProgrammerRecipes);

        // Neuro CPU
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Wetware_Extreme.get(4),
                ItemList.Neuron_Cell_Cluster.get(64),
                ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(64),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polybenzimidazole, 32),
                GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.NaquadahEnriched, 16),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 32))
            .fluidInputs(
                Materials.GrowthMediumSterilized.getFluid(1000),
                Materials.NutrientBroth.getFluid(8000),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000))
            .itemOutputs(ItemList.Circuit_Chip_NeuroCPU.get(4))
            .eut(RECIPE_ZPM)
            .duration(30 * SECONDS)
            .metadata(AO_DATA, new AORecipeData(8, 100, 5))
            .addTo(bioProgrammerRecipes);

        // Bio CPU
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Bio_Ultra.get(1),
                ItemList.Circuit_Chip_Biocell.get(64),
                ItemList.Circuit_Tissue.get(1),
                ItemList.Circuit_Parts_Reinforced_Glass_Tube.get(16),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polybenzimidazole, 16),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.HSSS, 32))
            .fluidInputs(
                Materials.BioMediumSterilized.getFluid(500L),
                Materials.UUMatter.getFluid(500L),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000))
            .itemOutputs(ItemList.Circuit_Chip_BioCPU.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV / 2)
            .metadata(AO_DATA, new AORecipeData(8, 100, 5))
            .addTo(bioProgrammerRecipes);
    }
}
