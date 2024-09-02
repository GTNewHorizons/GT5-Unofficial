package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import kekztech.common.Blocks;

public class Unpackager implements Runnable {

    @Override
    public void run() {

        // TFFT Recycling
        {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 1))
                .itemOutputs(
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 6))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 2))
                .itemOutputs(
                    ItemList.Casing_Tank_1.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.EnergeticSilver, 6))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 3))
                .itemOutputs(
                    ItemList.Casing_Tank_3.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.VividAlloy, 6))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 4))
                .itemOutputs(
                    ItemList.Casing_Tank_5.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Enderium, 6))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 5))
                .itemOutputs(
                    ItemList.Casing_Tank_7.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 6))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 6))
                .itemOutputs(
                    ItemList.Casing_Tank_7.get(4),
                    GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.CrystallinePinkSlime, 6))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 7))
                .itemOutputs(
                    ItemList.Casing_Tank_10.get(16),
                    GTOreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.MelodicAlloy, 6))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 8))
                .itemOutputs(
                    ItemList.Quantum_Tank_IV.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 12))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 9))
                .itemOutputs(
                    ItemList.Quantum_Tank_IV.get(4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 12))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 10))
                .itemOutputs(
                    ItemList.Quantum_Tank_IV.get(16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 12))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(unpackagerRecipes);

        }

        // Capacitor recycling
        {

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7))
                .itemOutputs(
                    GTModHandler.getIC2Item("lapotronCrystal", 1L, 26),
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1))
                .itemOutputs(ItemList.Energy_LapotronicOrb.get(1L), new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2))
                .itemOutputs(
                    ItemList.Energy_LapotronicOrb2.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3))
                .itemOutputs(
                    ItemList.Energy_Module.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4))
                .itemOutputs(
                    ItemList.Energy_Cluster.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5))
                .itemOutputs(
                    ItemList.ZPM3.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8))
                .itemOutputs(ItemList.ZPM4.get(1L), GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9))
                .itemOutputs(
                    ItemList.ZPM5.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 24))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10))
                .itemOutputs(
                    ItemList.ZPM6.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 24))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(unpackagerRecipes);

        }
    }
}
