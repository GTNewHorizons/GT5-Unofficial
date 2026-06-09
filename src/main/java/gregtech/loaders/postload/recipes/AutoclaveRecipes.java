package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class AutoclaveRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.EnergeticAlloy.getMolten(2 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.ConductiveIron.getMolten(4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(autoclaveRecipes);

        final ItemStack certusQuartzSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 0);
        NBTTagCompound certusQuartzTag = new NBTTagCompound();
        certusQuartzTag.setInteger("progress", 0);
        certusQuartzSeed.setTagCompound(certusQuartzTag);

        final ItemStack netherQuartzSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 600);
        NBTTagCompound netherQuartzTag = new NBTTagCompound();
        netherQuartzTag.setInteger("progress", 600);
        netherQuartzSeed.setTagCompound(netherQuartzTag);

        final ItemStack fluixSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 1200);
        NBTTagCompound fluixTag = new NBTTagCompound();
        fluixTag.setInteger("progress", 1200);
        fluixSeed.setTagCompound(fluixTag);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(1 * QUARTER_INGOTS))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(1 * QUARTER_INGOTS))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(1 * QUARTER_INGOTS))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 8))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 16L))
            .outputChances(10000)
            .fluidInputs(Materials.Polybenzimidazole.getMolten(9L))
            .duration(1 * SECONDS + 17 * TICKS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 4L))
            .outputChances(10000)
            .fluidInputs(Materials.Epoxid.getMolten(9L))
            .duration(18 * TICKS)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 2L))
            .outputChances(10000)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1 * EIGHTH_INGOTS))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 4))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 1L))
            .outputChances(10000)
            .fluidInputs(Materials.Polyethylene.getMolten(1 * QUARTER_INGOTS))
            .duration(1 * SECONDS + 17 * TICKS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1))
            .outputChances(3333)
            .fluidInputs(Materials.UUMatter.getFluid(4 * INGOTS))
            .duration(60 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.QuantumStar.get(1L)))
            .itemOutputs(ItemList.Gravistar.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.Neutronium.getMolten(2 * INGOTS))
            .duration(24 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.Gravistar.get(16L)))
            .itemOutputs(ItemList.NuclearStar.get(1L))
            .outputChances(10000)
            .fluidInputs(Materials.Infinity.getMolten(2 * INGOTS))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(1))
            .itemOutputs(Materials.Quartzite.getGems(1))
            .outputChances(750)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(1))
            .itemOutputs(Materials.Quartzite.getGems(1))
            .outputChances(1000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SiliconDioxide.getDust(1))
            .itemOutputs(Materials.Quartzite.getGems(1))
            .outputChances(10000)
            .fluidInputs(Materials.Void.getMolten(1 * QUARTER_INGOTS))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3))
            .outputChances(7500)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.Olivine, 15))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Asbestos, 18),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnetite, 7))
            .fluidInputs(GTModHandler.getDistilledWater(9_000))
            .fluidOutputs(Materials.Hydrogen.getGas(14_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        // Marble Block
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Marble, 1L))
            .fluidInputs(Materials.Water.getFluid(1_000L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.Marble, 1L))
            .fluidInputs(GTModHandler.getDistilledWater(500L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1L),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Mellion, 32L))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(8L))
            .fluidInputs(Materials.Grade8PurifiedWater.getFluid(32_000L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.round, Materials.MagMatter, 1))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(5))
            .fluidInputs(Materials.PhononCrystalSolution.getFluid(250))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(autoclaveRecipes);

    }
}
