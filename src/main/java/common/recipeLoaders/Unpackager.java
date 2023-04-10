package common.recipeLoaders;

import net.minecraft.item.ItemStack;

import com.github.bartimaeusnek.bartworks.util.BW_Util;
import common.Blocks;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

import static gregtech.api.util.GT_Recipe.GT_Recipe_Map.sUnboxinatorRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

public class Unpackager implements Runnable {

    @Override
    public void run() {

        // TFFT Recycling
        {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 1),
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 2),
                    ItemList.Casing_Tank_1.get(1)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.EnergeticSilver, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 3),
                    ItemList.Casing_Tank_3.get(1)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.VividAlloy, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 4),
                    ItemList.Casing_Tank_5.get(1)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Enderium, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 5),
                    ItemList.Casing_Tank_7.get(1)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CrystallineAlloy, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 6),
                    ItemList.Casing_Tank_7.get(4)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.CrystallinePinkSlime, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 7),
                    ItemList.Casing_Tank_10.get(16)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.MelodicAlloy, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 8),
                    ItemList.Quantum_Tank_IV.get(1)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.StellarAlloy, 12)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 9),
                    ItemList.Quantum_Tank_IV.get(4)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 12)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.tfftStorageField, 1, 10),
                    ItemList.Quantum_Tank_IV.get(16)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 12)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(sUnboxinatorRecipes);

        }

        // Capacitor recycling
        {

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7),
                    GT_ModHandler.getIC2Item("lapotronCrystal", 1L, 26)
                )
                .itemOutputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1),
                    ItemList.Energy_LapotronicOrb.get(1L)
                )
                .itemOutputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2),
                    ItemList.Energy_LapotronicOrb2.get(1L)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Osmiridium, 24)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3),
                    ItemList.Energy_Module.get(1L)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NaquadahAlloy, 24)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4),
                    ItemList.Energy_Cluster.get(1L)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 24)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5),
                    ItemList.ZPM3.get(1L)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 24)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8),
                    ItemList.ZPM4.get(1L)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 24)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9),
                    ItemList.ZPM5.get(1L)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 24)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

            GT_Values.RA.stdBuilder()
                .itemInputs(
                    new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10),
                    ItemList.ZPM6.get(1L)
                )
                .itemOutputs(
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 24)
                )
                .noFluidInputs()
                .noFluidOutputs()
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(sUnboxinatorRecipes);

        }
    }
}
