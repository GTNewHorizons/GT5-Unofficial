package gregtech.loaders.postload.chains;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.*;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.semiFluidFuels;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class NetheriteRecipes {

    static ItemStack missing = new ItemStack(Blocks.fire);

    public static void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(10000))
            .fluidOutputs(
                Materials.NitrogenDioxide.getGas(1400),
                Materials.SulfurDioxide.getGas(3800),
                Materials.SulfurTrioxide.getGas(2100))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeNonCellRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(1000))
            .fluidOutputs(Materials.NetherSemiFluid.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherSemiFluid.getFluid(100000))
            .itemOutputs(ItemList.Heavy_Hellish_Mud.get(2))
            .fluidOutputs(
                Materials.NefariousGas.getFluid(4000),
                FluidUtils.getFluidStack("fluid.coalgas", 16000),
                FluidUtils.getFluidStack("fluid.anthracene", 70000),
                Materials.SulfurTrioxide.getGas(210000),
                Materials.SulfurDioxide.getGas(380000),
                Materials.NitrogenDioxide.getGas(140000),
                WerkstoffLoader.Neon.getFluidOrGas(36000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.NefariousGas, 1))
            .metadata(FUEL_VALUE, 1200)
            .metadata(FUEL_TYPE, 1)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Grade1PurifiedWater.getFluid(8000), Materials.NefariousGas.getFluid(16000))
            .fluidOutputs(Materials.NefariousOil.getFluid(12000))
            .duration(26 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(4000), Materials.NefariousGas.getFluid(16000))
            .fluidOutputs(Materials.NefariousOil.getFluid(12000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NefariousOil.getFluid(1000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 572)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Heavy_Hellish_Mud.get(32))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16000))
            .fluidOutputs(Materials.RichNetherWaste.getFluid(16000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.TripleCompressedNetherrack.get(1),
                getModItem(ThaumicTinkerer.ID, "kamiResource", 64, 6, missing))
            .itemOutputs(getModItem(EtFuturumRequiem.ID, "ancient_debris", 1))
            .duration(1 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(electricImplosionCompressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(EtFuturumRequiem.ID, "ancient_debris", 1, missing))
            .itemOutputs(
                getModItem(EtFuturumRequiem.ID, "netherite_scrap", 1, missing),
                getModItem(EtFuturumRequiem.ID, "netherite_scrap", 1, missing))
            .outputChances(10000, 2500)
            .duration(27 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(EtFuturumRequiem.ID, "netherite_scrap", 1, missing))
            .fluidInputs(FluidUtils.getLava(100))
            .itemOutputs(ItemList.Hot_Netherite_Scrap.get(1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Hot_Netherite_Scrap.get(2))
            .fluidInputs(Materials.RichNetherWaste.getFluid(2000))
            .itemOutputs(
                ItemList.Netherite_Scrap_Seed.get(1),
                getModItem(EtFuturumRequiem.ID, "netherite_scrap", 2, missing))
            .outputChances(1000, 10000)
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Hot_Netherite_Scrap.get(64), ItemList.Heavy_Hellish_Mud.get(64))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(32000))
            .itemOutputs(
                ItemList.Brittle_Netherite_Scrap.get(4),
                getModItem(EtFuturumRequiem.ID, "netherite_scrap", 64, missing))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Netherite_Scrap_Seed.get(1))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16000))
            .itemOutputs(ItemList.Brittle_Netherite_Scrap.get(1), ItemList.Netherite_Scrap_Seed.get(1))
            .outputChances(5000, 5000)
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Brittle_Netherite_Scrap.get(1))
            .itemOutputs(ItemList.Netherite_Nanoparticles.get(1))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Netherite_Nanoparticles.get(1))
            .fluidInputs(Materials.HellishMetal.getMolten(144))
            .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1))
            .fluidOutputs(Materials.Thaumium.getMolten(32))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 7500)
            .addTo(blastFurnaceRecipes);

        NetheriteRecipes.addNetheriteParts();
    }

    private static void addNetheriteParts() {
        addNetheritePartRecipe(OrePrefixes.ingot, 1, 1);
        addNetheritePartRecipe(OrePrefixes.plate, 1, 1);
        addNetheritePartRecipe(OrePrefixes.plateDouble, 1, 2);
        addNetheritePartRecipe(OrePrefixes.plateDense, 1, 9);
        addNetheritePartRecipe(OrePrefixes.stick, 2, 1);
        addNetheritePartRecipe(OrePrefixes.round, 9, 1);
        addNetheritePartRecipe(OrePrefixes.bolt, 8, 1);
        addNetheritePartRecipe(OrePrefixes.screw, 8, 1);
        addNetheritePartRecipe(OrePrefixes.ring, 4, 1);
        addNetheritePartRecipe(OrePrefixes.foil, 8, 1);
        addNetheritePartRecipe(OrePrefixes.itemCasing, 2, 1);
        addNetheritePartRecipe(OrePrefixes.gearGtSmall, 1, 1);
        addNetheritePartRecipe(OrePrefixes.rotor, 1, 5);
        addNetheritePartRecipe(OrePrefixes.stickLong, 1, 1);
        addNetheritePartRecipe(OrePrefixes.gearGt, 1, 4);
        addNetheritePartRecipe(
            GTOreDictUnificator.get(OrePrefixes.block, Materials.InfusedGold, 1),
            getModItem(EtFuturumRequiem.ID, "netherite_block", 1, missing),
            9);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Netherite, 4),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite, 1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Netherite, 64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Netherite, 1))
            .duration(500 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(CompressionTierKey.INSTANCE, 1)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 2))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold, 1),
                ItemList.Netherite_Nanoparticles.get(1))
            .duration(23 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Netherite, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 1))
            .duration(4 * SECONDS + 18 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(EtFuturumRequiem.ID, "netherite_block", 3, missing))
            .itemOutputs(getModItem(EtFuturumRequiem.ID, "netherite_stairs", 4, missing))
            .duration(1 * HOURS)
            .eut(TierEU.RECIPE_IV)
            .addTo(cutterRecipes);
    }

    private static void addNetheritePartRecipe(OrePrefixes prefix, int multiplier, int inverseMultiplier) {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(prefix, Materials.InfusedGold, multiplier),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(inverseMultiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.Netherite, multiplier))
            .fluidInputs(Materials.Boron.getPlasma(2L * inverseMultiplier))
            .fluidOutputs(Materials.Boron.getMolten(2L * inverseMultiplier))
            .duration(34 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(plasmaArcFurnaceRecipes);

    }

    private static void addNetheritePartRecipe(ItemStack inputStack, ItemStack outputStack, int inverseMultiplier) {

        GTValues.RA.stdBuilder()
            .itemInputs(inputStack, ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(inverseMultiplier))
            .itemOutputs(outputStack)
            .fluidInputs(Materials.Boron.getPlasma(2L * inverseMultiplier))
            .fluidOutputs(Materials.Boron.getMolten(2L * inverseMultiplier))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(plasmaArcFurnaceRecipes);

    }
}
