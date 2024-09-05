package bartworks.common.loaders.recipes;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
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
        Materials[] cables = { // Cable material used in the acid gen, diode and energy distributor below
            Materials.Lead, // ULV
            Materials.Tin, // LV
            Materials.AnnealedCopper, // MV
            Materials.Gold, // HV
            Materials.Aluminium, // EV
            Materials.Tungsten, // IV
            Materials.VanadiumGallium, // LuV
            Materials.Naquadah, // ZPM
            Materials.NaquadahAlloy, // UV
            Materials.SuperconductorUV // UHV
        };

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
                ItemList.Circuit_Parts_GlassFiber.get(32),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Electrum, 8),
                WerkstoffLoader.CubicZirconia.get(OrePrefixes.gemExquisite, 2))
            .itemOutputs(
                new ItemStack(
                    ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                    1,
                    ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(72))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        ItemStack[][] converters = ItemRegistry.TecTechLaserAdditions[0];
        ItemStack[][] input = ItemRegistry.TecTechLaserAdditions[1];
        ItemStack[][] dynamo = ItemRegistry.TecTechLaserAdditions[2];

        ItemList[] emitters = { ItemList.Emitter_EV, ItemList.Emitter_IV, ItemList.Emitter_LuV, ItemList.Emitter_ZPM };

        ItemList[] sensors = { ItemList.Sensor_EV, ItemList.Sensor_IV, ItemList.Sensor_LuV, ItemList.Sensor_ZPM };

        OrePrefixes[] prefixes = { OrePrefixes.cableGt04, OrePrefixes.cableGt08, OrePrefixes.cableGt12,
            OrePrefixes.cableGt16 };

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                ItemStack converter = converters[j][i];
                ItemStack eInput = input[j][i];
                ItemStack eDynamo = dynamo[j][i];
                long recipeConsumption = switch (i) {
                    case 0 -> TierEU.RECIPE_EV;
                    case 1 -> TierEU.RECIPE_IV;
                    case 2 -> TierEU.RECIPE_LuV;
                    case 3 -> TierEU.RECIPE_ZPM;
                    default -> TierEU.RECIPE_EV;
                };

                int solderingAmount = Math.max(144 * i, 72) * (j + 1);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(
                            ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                            ((j + 1) * 16),
                            ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                        WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                        GTOreDictUnificator.get(prefixes[j], cables[i + 4], 8),
                        emitters[i].get(2 * (j + 1)),
                        sensors[i].get(2 * (j + 1)),
                        ItemList.TRANSFORMERS[4 + i].get(2 * (j + 1)))
                    .itemOutputs(converter)
                    .fluidInputs(Materials.SolderingAlloy.getMolten(solderingAmount))
                    .duration((10 * (j + 1)) * SECONDS)
                    .eut(recipeConsumption)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(
                            ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                            (j + 1) * 16,
                            ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                        WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                        GTOreDictUnificator.get(prefixes[j], cables[i + 4], 8),
                        sensors[i].get(2 * (j + 1)),
                        ItemList.HATCHES_ENERGY[4 + i].get(2 * (j + 1)))
                    .itemOutputs(eInput)
                    .fluidInputs(Materials.SolderingAlloy.getMolten(solderingAmount))
                    .duration((10 * (j + 1)) * SECONDS)
                    .eut(recipeConsumption)
                    .addTo(assemblerRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(
                            ItemRegistry.TecTechPipeEnergyLowPower.getItem(),
                            (j + 1) * 16,
                            ItemRegistry.TecTechPipeEnergyLowPower.getItemDamage()),
                        WerkstoffLoader.CubicZirconia.get(OrePrefixes.lens),
                        GTOreDictUnificator.get(prefixes[j], cables[i + 4], 8),
                        emitters[i].get(2 * (j + 1)),
                        ItemList.HATCHES_DYNAMO[4 + i].get(2 * (j + 1)))
                    .itemOutputs(eDynamo)
                    .fluidInputs(Materials.SolderingAlloy.getMolten(solderingAmount))
                    .duration((10 * (j + 1) * SECONDS))
                    .eut(recipeConsumption)
                    .addTo(assemblerRecipes);
            }
        }

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
