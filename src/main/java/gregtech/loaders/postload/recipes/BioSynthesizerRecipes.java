package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.TierEU.*;
import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.AO_DATA;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import net.minecraft.init.Items;
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
import tectech.thing.CustomItemList;

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

        // NeuralFluid
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Neuron_Cell_Cluster.get(32))
            .fluidInputs(Materials.NutrientBroth.getFluid(8000))
            .fluidOutputs(Materials.NeuralFluid.getFluid(8000))
            .duration(15 * SECONDS)
            .eut(RECIPE_UV)
            .addTo(mixerRecipes);

        // BREWERY

        // Nutrient Broth
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Nutrient_Paste.get(64))
            .fluidInputs(getFluidStack("potion.mineralwater", 32000))
            .fluidOutputs(Materials.NutrientBroth.getFluid(32000))
            .duration(60 * SECONDS)
            .eut(RECIPE_LV)
            .addTo(brewingRecipes);

        // Chemical Reactor

        // Psycoflux Substrate
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Salt.getDust(16),
                Materials.RockSalt.getDust(4),
                getModItem(NewHorizonsCoreMod.ID, "item.TCetiESeaweedExtract", 4)

            )
            .fluidInputs(
                Materials.GrowthMediumSterilized.getFluid(4000),
                Materials.NeuralFluid.getFluid(4000)
            )
            .fluidOutputs(Materials.PsycofluxSubstrate.getFluid(8000))
            .duration(60 * SECONDS)
            .eut(RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // SYNTHESIZER

        // stemcell self-replication recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(ItemList.Circuit_Chip_Stemcell.get(16)),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sunnarium, 1))
            .fluidInputs(Materials.NutrientBroth.getFluid(1000))
            .itemOutputs(ItemList.Circuit_Chip_Stemcell.get(64))
            .duration(10 * SECONDS)
            .eut(RECIPE_LuV)
            .metadata(AO_DATA, new AORecipeData(8, 50, 10))
            .addTo(bioSynthesizerRecipes);

        // Neuron Cell Cluster
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_Stemcell.get(16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Adamantium, 4))
            .fluidInputs(
                Materials.GrowthMediumSterilized.getFluid(250),
                Materials.NutrientBroth.getFluid(1000))
            .itemOutputs(ItemList.Neuron_Cell_Cluster.get(64))
            .duration(10 * SECONDS)
            .eut(RECIPE_ZPM)
            .metadata(AO_DATA, new AORecipeData(4, 50, 10))
            .addTo(bioSynthesizerRecipes);

        // Skin Cell Cluster
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_Stemcell.get(16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.CosmicNeutronium, 4))
            .fluidInputs(
                Materials.GrowthMediumSterilized.getFluid(250),
                Materials.NutrientBroth.getFluid(1000))
            .itemOutputs(ItemList.Skin_Cell_Cluster.get(64))
            .duration(10 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(10, 50, 10))
            .addTo(bioSynthesizerRecipes);

        // Muscle Cell Cluster
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_Stemcell.get(16),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfinityCatalyst, 4))
            .fluidInputs(
                Materials.GrowthMediumSterilized.getFluid(250),
                Materials.NutrientBroth.getFluid(1000))
            .itemOutputs(ItemList.Muscle_Cell_Cluster.get(64))
            .duration(10 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(10, 50, 10))
            .addTo(bioSynthesizerRecipes);

        // Self Healing Conductor
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.slime_ball, 16),
                getModItem(NewHorizonsCoreMod.ID, "item.Agar", 16), // placeholder! todo: find the correct entry to Agar
                getModItem(NewHorizonsCoreMod.ID, "item.TCetiESeaweedExtract", 1))
            .fluidInputs(Materials.NeuralFluid.getFluid(8000))
            .itemOutputs(ItemList.Self_Healing_Conductor.get(4))
            .duration(60 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(4, 500, 10))
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

        // Neuro CPU
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Board_Wetware_Extreme.get(4),
                ItemList.Neuron_Cell_Cluster.get(64),
                getModItem(NewHorizonsCoreMod.ID, "item.ReinforcedGlassPlate", 8L, 0),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Polybenzimidazole, 32),
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64},
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 32))
            .fluidInputs(
                Materials.GrowthMediumSterilized.getFluid(1000),
                Materials.NutrientBroth.getFluid(8000),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000))
            .itemOutputs(ItemList.Circuit_Chip_NeuroCPU.get(4))
            .eut(RECIPE_UV)
            .duration(5 * SECONDS)
            .metadata(AO_DATA, new AORecipeData(8, 100, 5))
            .addTo(bioProgrammerRecipes);

        // Living Crystal chip
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Self_Healing_Conductor.get(1),
                ItemList.Neuron_Cell_Cluster.get(16),
                ItemList.Circuit_Chip_CrystalSoC2.get(4))
            .fluidInputs(Materials.NutrientBroth.getFluid(8000))
            .itemOutputs(ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(4))
            .eut(RECIPE_UHV)
            .duration(60 * SECONDS)
            .metadata(AO_DATA, new AORecipeData(11, 500, 5))
            .addTo(bioProgrammerRecipes);

        // Bio-Computing core
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Muscle_Cell_Cluster.get(64),
                ItemList.Neuron_Cell_Cluster.get(16),
                ItemList.Self_Healing_Conductor.get(4),
                ItemList.Circuit_Tissue.get(1))
            .itemOutputs(ItemList.Neural_Electronic_Interface.get(1))
            .duration(10 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(9, 2000, 10))
            .addTo(bioProgrammerRecipes);

        // Neural Electronic Interface
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Muscle_Cell_Cluster.get(64),
                ItemList.Neuron_Cell_Cluster.get(16),
                ItemList.Self_Healing_Conductor.get(4),
                ItemList.Circuit_Tissue.get(1))
            .fluidInputs(Materials.NeuralFluid.getFluid(8000))
            .itemOutputs(ItemList.Neural_Electronic_Interface.get(1))
            .duration(10 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(9, 2000, 10))
            .addTo(bioProgrammerRecipes);

        // Axon Bus
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Neuron_Cell_Cluster.get(64),
                ItemList.Muscle_Cell_Cluster.get(64),
                ItemList.Self_Healing_Conductor.get(4),
                ItemList.Circuit_Tissue.get(1))
            .fluidInputs(Materials.NeuralFluid.getFluid(8000))
            .itemOutputs(ItemList.Axon_Bus.get(1))
            .duration(10 * SECONDS)
            .eut(RECIPE_UV)
            .metadata(AO_DATA, new AORecipeData(9, 2000, 10))
            .addTo(bioProgrammerRecipes);

        // CAL recipes temporarily written in Asssembler

        // Bio processor
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Bio_Computing_Core.get(1L),
                ItemList.Neural_Electronic_Interface.get(2L),
                ItemList.Axon_Bus.get(2),
                ItemList.Circuit_Parts_CapacitorASMD.get(12L),
                ItemList.Circuit_Parts_TransistorXSMD.get(12L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 16))
            .itemOutputs(ItemList.Circuit_Bioprocessor.get(1L))
            .fluidInputs(MaterialsAlloy.INDALLOY_140.getFluidStack(2 * INGOTS))
            .requiresCleanRoom()
            .duration(60 * SECONDS)
            .eut(153600)
            .addTo(assemblerRecipes);

        // Bio SoC
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Optically_Perfected_CPU.get(1L),
                ItemList.Optically_Compatible_Memory.get(2L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(16L),
                ItemList.Circuit_Parts_DiodeXSMD.get(16L),
                CustomItemList.DATApipe.get(4L),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 16))
            .itemOutputs(ItemList.Circuit_OpticalProcessor.get(1L))
            .fluidInputs(MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * INGOTS))
            .requiresCleanRoom()
            .duration(20 * SECONDS)
            .eut(614400)
            .addTo(assemblerRecipes);

    }
}
