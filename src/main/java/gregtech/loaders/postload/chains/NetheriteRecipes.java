package gregtech.loaders.postload.chains;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.ForbiddenMagic;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.plasmaArcFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.COMPRESSION_TIER;

import gregtech.api.enums.MaterialsUEVplus;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import tectech.recipe.TecTechRecipeMaps;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class NetheriteRecipes {

    public static void run() {

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(1000))
            .fluidOutputs(Materials.NetherSemiFluid.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .noOptimize()
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherSemiFluid.getFluid(100000))
            .itemOutputs(ItemList.Heavy_Hellish_Mud.get(2))
            .fluidOutputs(
                Materials.NefariousGas.getFluid(4000),
                FluidUtils.getFluidStack("fluid.coalgas", 16000),
                FluidUtils.getFluidStack("fluid.anthracene", 7000),
                Materials.SulfurTrioxide.getGas(21000),
                Materials.SulfurDioxide.getGas(38000),
                Materials.NitrogenDioxide.getGas(14000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .noOptimize()
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(4000), Materials.NefariousGas.getFluid(16000))
            .fluidOutputs(Materials.NefariousOil.getFluid(12000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .noOptimize()
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Heavy_Hellish_Mud.get(32))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16000))
            .fluidOutputs(Materials.RichNetherWaste.getFluid(16000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .noOptimize()
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(getModItem(EtFuturumRequiem.ID, "netherite_scrap", 0))
            .fluidInputs(Materials.RichNetherWaste.getFluid(2000))
            .itemOutputs(ItemList.Netherite_Crystal_Seed.get(1))
            .outputChances(1000)
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .noOptimize()
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Netherite_Crystal_Seed.get(1))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16000))
            .itemOutputs(ItemList.Brittle_Netherite_Crystal.get(1), ItemList.Netherite_Crystal_Seed.get(1))
            .outputChances(5000, 5000)
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(chemicalBathRecipes);

        if (ForbiddenMagic.isModLoaded()) {
            // new ResearchItem( TODO add to coremod pr
            // "HELLISHMETAL",
            // "NEWHORIZONS",
            // new AspectList().add(Aspect.getAspect("infernus"), 15).add(Aspect.getAspect("lucrum"), 12)
            // .add(Aspect.getAspect("fames"), 6).add(Aspect.getAspect("ignis"), 3),
            // -6,
            // -7,
            // 3,
            // GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HellishMetal, 1))
            // .setConcealed().setRound().setPages(new ResearchPage("TConstruct.research_page.UNDYINGTOTEM.1"))
            // .registerResearchItem();
            // TCHelper.addResearchPage(
            // "HELLISHMETAL",
            // new ResearchPage(
            // Objects.requireNonNull(
            // TCHelper.findInfusionRecipe(
            // GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HellishMetal, 1)))));
            // ThaumcraftApi.addWarpToResearch("HELLISHMETAL", 3);

            ThaumcraftApi.addInfusionCraftingRecipe(
                "HELLISHMETAL",
                GTOreDictUnificator.get(OrePrefixes.block, Materials.HellishMetal, 1),
                1,
                new AspectList().add(Aspect.getAspect("ignis"), 8),
                MaterialsElements.getInstance().RHODIUM.getBlock(1),
                new ItemStack[] { getModItem(ForbiddenMagic.ID, "NetherShard", 1, 0),
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Thaumium, 1),
                    getModItem(ForbiddenMagic.ID, "NetherShard", 1, 0),
                    GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Thaumium, 1), });

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialsElements.getInstance().RHODIUM.getIngot(1),
                    getModItem(ForbiddenMagic.ID, "NetherShard", 8, 0),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedFire, 8))
                .fluidInputs(Materials.Thaumium.getMolten(8 * 144))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.HellishMetal, 1))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(electrolyzerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Brittle_Netherite_Crystal.get(1))
            .itemOutputs(ItemList.Netherite_Nanoparticles.get(1))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(maceratorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Netherite_Nanoparticles.get(1))
            .fluidInputs(Materials.HellishMetal.getMolten(288))
            .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1))
            .fluidOutputs(Materials.Thaumium.getMolten(144))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 7500)
            .addTo(blastFurnaceRecipes);

        //GTValues.RA.stdBuilder() TODO add whenever this has a proper use and recipe thought out
        //    .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 1))
        //    .fluidOutputs(Materials.ActivatedNetherite.getFluid(144))
        //    .duration(400 * SECONDS)
        //    .eut(TierEU.RECIPE_MAX)
        //    .metadata(COIL_HEAT, 45700)
        //    .addTo(TecTechRecipeMaps.godforgeMoltenRecipes);

        NetheriteRecipes.addNetheriteParts();
    }

    private static void addNetheriteParts() {
        addNetheritePartRecipe(OrePrefixes.block, 1,9);
        addNetheritePartRecipe(OrePrefixes.ingot, 1,1);
        addNetheritePartRecipe(OrePrefixes.plate, 1,1);
        addNetheritePartRecipe(OrePrefixes.plateDouble, 1,2);
        addNetheritePartRecipe(OrePrefixes.plateDense, 1,9);
        addNetheritePartRecipe(OrePrefixes.stick, 2,1);
        addNetheritePartRecipe(OrePrefixes.round, 9,1);
        addNetheritePartRecipe(OrePrefixes.bolt, 8,1);
        addNetheritePartRecipe(OrePrefixes.screw, 8,1);
        addNetheritePartRecipe(OrePrefixes.ring, 4,1);
        addNetheritePartRecipe(OrePrefixes.foil, 8,1);
        addNetheritePartRecipe(OrePrefixes.itemCasing, 2,1);
        addNetheritePartRecipe(OrePrefixes.gearGtSmall, 1,1);
        addNetheritePartRecipe(OrePrefixes.rotor, 1,5);
        addNetheritePartRecipe(OrePrefixes.stickLong, 1,1);
        addNetheritePartRecipe(OrePrefixes.gearGt, 1,4);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Netherite, 4), GTUtility.getIntegratedCircuit(4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite,1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Netherite, 64))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Netherite,1))
            .duration(500 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COMPRESSION_TIER, 1)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherite, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold,1), ItemList.Netherite_Nanoparticles.get(1))
            .duration(23 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(centrifugeRecipes);
    }


    private static void addNetheritePartRecipe(OrePrefixes prefix, int multiplier, int inverseMultiplier) {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(prefix, Materials.InfusedGold, multiplier),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(inverseMultiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.Netherite, multiplier))
            .fluidInputs(Materials.Calcium.getPlasma(2L * inverseMultiplier))
            .fluidOutputs(Materials.Calcium.getMolten(2L * inverseMultiplier))
            .duration(12 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(plasmaArcFurnaceRecipes);

    }
}
