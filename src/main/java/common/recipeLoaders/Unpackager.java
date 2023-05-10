package common.recipeLoaders;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import net.minecraft.item.ItemStack;

import common.Blocks;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class Unpackager implements Runnable {

    @Override
    public void run() {

        // TFFT Recycling
        {
            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 1))
                    .itemOutputs(
                            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 6))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 2))
                    .itemOutputs(
                            ItemList.Casing_Tank_1.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnergeticSilver, 6))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 3))
                    .itemOutputs(
                            ItemList.Casing_Tank_3.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VividAlloy, 6))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 4))
                    .itemOutputs(
                            ItemList.Casing_Tank_5.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Enderium, 6))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 5))
                    .itemOutputs(
                            ItemList.Casing_Tank_7.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 6))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 6))
                    .itemOutputs(
                            ItemList.Casing_Tank_7.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.CrystallinePinkSlime, 6))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 7))
                    .itemOutputs(
                            ItemList.Casing_Tank_10.get(16),
                            GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.MelodicAlloy, 6))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 8))
                    .itemOutputs(
                            ItemList.Quantum_Tank_IV.get(1),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 12))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 9))
                    .itemOutputs(
                            ItemList.Quantum_Tank_IV.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 12))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.tfftStorageField, 1, 10))
                    .itemOutputs(
                            ItemList.Quantum_Tank_IV.get(16),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 12))
                    .noFluidInputs().noFluidOutputs().duration(5 * SECONDS).eut(TierEU.RECIPE_HV)
                    .addTo(sUnboxinatorRecipes);

        }

        // Capacitor recycling
        {

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7))
                    .itemOutputs(
                            GT_ModHandler.getIC2Item("lapotronCrystal", 1L, 26),
                            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1))
                    .itemOutputs(
                            ItemList.Energy_LapotronicOrb.get(1L),
                            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2))
                    .itemOutputs(
                            ItemList.Energy_LapotronicOrb2.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3))
                    .itemOutputs(
                            ItemList.Energy_Module.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4))
                    .itemOutputs(
                            ItemList.Energy_Cluster.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5))
                    .itemOutputs(
                            ItemList.ZPM3.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8))
                    .itemOutputs(
                            ItemList.ZPM4.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9))
                    .itemOutputs(
                            ItemList.ZPM5.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 24))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10))
                    .itemOutputs(
                            ItemList.ZPM6.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 24))
                    .noFluidInputs().noFluidOutputs().duration(60 * SECONDS).eut(TierEU.RECIPE_LV)
                    .addTo(sUnboxinatorRecipes);

        }
    }
}
