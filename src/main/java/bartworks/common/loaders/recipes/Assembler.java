package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.items.GGMaterial;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class Assembler implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 0),
                Materials.Lapis.getPlates(9),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 2L),
                GTUtility.getIntegratedCircuit(17))
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1))
            .fluidInputs(FluidRegistry.getFluidStack("ic2coolant", 1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(ItemRegistry.BW_BLOCKS[0], 1, 1),
                Materials.Lapis.getBlocks(8),
                GTUtility.getIntegratedCircuit(17))
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[1]))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Machine_Multi_BlastFurnace.get(64), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemRegistry.megaMachines[0])
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .duration(1 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Machine_Multi_VacuumFreezer.get(64), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemRegistry.megaMachines[1])
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .duration(1 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Distillation_Tower.get(64), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemRegistry.megaMachines[2])
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .duration(1 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Machine_Multi_LargeChemicalReactor.get(64), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemRegistry.megaMachines[3])
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .duration(1 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.OilCracker.get(64), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemRegistry.megaMachines[4])
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .duration(1 * HOURS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder() // DEHP
            .itemInputs(
                ItemList.OilDrill4.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Ultimate, 8),
                GGMaterial.incoloy903.get(OrePrefixes.gearGt, 32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Polytetrafluoroethylene, 16),
                ItemList.Field_Generator_IV.get(1))
            .itemOutputs(ItemRegistry.dehp)
            .fluidInputs(Materials.HSSE.getMolten(32 * 144))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 64L),
                GTUtility.getIntegratedCircuit(17))
            .itemOutputs(new ItemStack(ItemRegistry.BW_BLOCKS[2], 1, 1))
            .fluidInputs(Materials.Plastic.getMolten(1152L))
            .duration(1 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 1L),
                Materials.Aluminium.getPlates(1),
                ItemList.Circuit_Board_Plastic.get(1L),
                ItemList.Battery_RE_LV_Lithium.get(1L))
            .itemOutputs(new ItemStack(ItemRegistry.CIRCUIT_PROGRAMMER))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288L))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_HV.get(64),
                Materials.LiquidAir.getCells(1),
                GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemRegistry.compressedHatch.copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Hatch_Output_HV.get(64), GTUtility.getIntegratedCircuit(17))
            .itemOutputs(ItemRegistry.giantOutputHatch.copy())
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenSteel, 1))
            .itemOutputs(new ItemStack(GregTechAPI.sBlockCasings3, 1, 12))
            .fluidInputs(Materials.Concrete.getMolten(1296))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(GregTechAPI.sBlockCasings3, 1, 12),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Europium, 6),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Europium, 24))
            .itemOutputs(new ItemStack(GregTechAPI.sBlockCasings8, 1, 5))
            .fluidInputs(Materials.Lead.getMolten(864))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }
}
