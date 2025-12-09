package gregtech.loaders.postload.chains;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.*;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.DISSOLUTION_TANK_RATIO;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.mixerNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.semiFluidFuels;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsGTNH;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import kekztech.Items;
import kekztech.common.items.MetaItemCraftingComponent;

public class NetheriteRecipes {

    static ItemStack missing = new ItemStack(Blocks.fire);

    public static void run() {

        // Prismatic Acid
        {
            GTValues.RA.stdBuilder() // Prismarine
                .itemInputs(
                    GregtechItemList.RedAlgaeBiomass.get(32),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 32))
                .fluidInputs(Materials.Grade1PurifiedWater.getFluid(8000))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.shard, MaterialsGTNH.Prismarine, 8))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(autoclaveRecipes);

            if (Forestry.isModLoaded()) {
                GTValues.RA.stdBuilder() // Prismarine + Comb
                    .itemInputs(
                        GregtechItemList.RedAlgaeBiomass.get(32),
                        GTBees.combs.getStackForType(CombType.PRISMATIC, 32))
                    .fluidInputs(Materials.Grade1PurifiedWater.getFluid(8000))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.shard, MaterialsGTNH.Prismarine, 16))
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(autoclaveRecipes);
            }

            GTValues.RA.stdBuilder() // Leaching
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.shard, MaterialsGTNH.Prismarine, 24))
                .fluidInputs(
                    FluidUtils.getHydrofluoricAcid(4000), // Industrial Strength Hydrofluoric Acid
                    FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 4000)) // Hydrogen Peroxide
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 4))
                .fluidOutputs(Materials.PrismarineSolution.getFluid(8000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .metadata(DISSOLUTION_TANK_RATIO, 1)
                .addTo(dissolutionTankRecipes);

            GTValues.RA.stdBuilder() // Looped Leaching
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.shard, MaterialsGTNH.Prismarine, 6))
                .fluidInputs(
                    Materials.PrismarineContaminatedHydrogenPeroxide.getFluid(6000),
                    FluidRegistry.getFluidStack("fluid.hydrogenperoxide", 2000)) // Hydrogen Peroxide
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 1))
                .fluidOutputs(Materials.PrismarineSolution.getFluid(8000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .metadata(DISSOLUTION_TANK_RATIO, 3)
                .addTo(dissolutionTankRecipes);

            GTValues.RA.stdBuilder() // Extraction
                .itemInputs(GTUtility.getIntegratedCircuit(1))
                .fluidInputs(
                    Materials.PrismarineSolution.getFluid(1000),
                    new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 2000))
                .fluidOutputs(
                    Materials.PrismarineContaminatedHydrogenPeroxide.getFluid(1000),
                    Materials.PrismarineRichNitrobenzeneSolution.getFluid(2000))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder() // Looped Extraction
                .itemInputs(GTUtility.getIntegratedCircuit(1))
                .fluidInputs(
                    Materials.PrismarineSolution.getFluid(1000),
                    Materials.PrismarineContaminatedNitrobenzeSolution.getFluid(3000))
                .fluidOutputs(
                    Materials.PrismarineContaminatedHydrogenPeroxide.getFluid(1000),
                    Materials.PrismarineRichNitrobenzeneSolution.getFluid(2000))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder() // Strontium Hydroxide
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Strontium, 1))
                .itemOutputs(MaterialMisc.STRONTIUM_HYDROXIDE.getDust(3))
                .fluidInputs(Materials.Oxygen.getGas(1000), Materials.Hydrogen.getGas(1000))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(multiblockChemicalReactorRecipes);

            GTValues.RA.stdBuilder() // Precipitation
                .itemInputs(MaterialMisc.STRONTIUM_HYDROXIDE.getDust(42))
                .itemOutputs(ItemList.Prismarine_Precipitate.get(8))
                .fluidInputs(Materials.PrismarineRichNitrobenzeneSolution.getFluid(16000))
                .fluidOutputs(
                    Materials.PrismarineContaminatedNitrobenzeSolution.getFluid(12000),
                    new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 4000))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(chemicalPlantRecipes);

            GTValues.RA.stdBuilder() // Crystallization
                .itemInputs(
                    ItemList.Prismarine_Precipitate.get(1),
                    WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0))
                .itemOutputs(ItemList.Prismatic_Crystal.get(1))
                .fluidInputs(Materials.CrystallineAlloy.getMolten(72))
                .fluidOutputs(Materials.Strontium.getMolten(288))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(laserEngraverRecipes);

            GTValues.RA.stdBuilder() // Gasification
                .itemInputs(ItemList.Prismatic_Crystal.get(1))
                .fluidInputs(Materials.Boron.getPlasma(100))
                .fluidOutputs(Materials.PrismaticGas.getFluid(1000))
                .duration(80 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(COIL_HEAT, 7200)
                .addTo(vacuumFurnaceRecipes);

            GTValues.RA.stdBuilder() // Reaction
                .fluidInputs(Materials.PrismaticGas.getFluid(4000), Materials.LiquidNitrogen.getGas(12000))
                .fluidOutputs(Materials.PrismaticAcid.getFluid(16000))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(vacuumFreezerRecipes);
        }

        // Naquarite
        {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(2),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 32))
                .fluidInputs(Materials.PrismaticAcid.getFluid(8000))
                .fluidOutputs(Materials.PrismaticNaquadah.getMolten(2304))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(alloyBlastSmelterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MetaItemCraftingComponent.getInstance()
                        .getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 4),
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Mica, 32))
                .fluidInputs(Materials.PrismaticNaquadah.getMolten(576))
                .fluidOutputs(Materials.PrismaticNaquadahCompositeSlurry.getFluid(2000))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MetaItemCraftingComponent.getInstance()
                        .getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 4),
                    WerkstoffLoader.RawFluorophlogopite.get(OrePrefixes.dust, 24))
                .fluidInputs(Materials.PrismaticNaquadah.getMolten(576))
                .fluidOutputs(Materials.PrismaticNaquadahCompositeSlurry.getFluid(2000))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.TungstenCarbide, 16),
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Lead, 16))
                .fluidInputs(Materials.PrismaticNaquadahCompositeSlurry.getFluid(8000))
                .itemOutputs(ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(16))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(chemicalDehydratorNonCellRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get(OrePrefixes.foil, Materials.Netherite, 4),
                    ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(16))
                .fluidInputs(Materials.Manyullyn.getMolten(1152))
                .itemOutputs(ItemList.Naquarite_Universal_Insulator_Foil.get(8))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(formingPressRecipes);
        }

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(10_000))
            .fluidOutputs(
                Materials.NitrogenDioxide.getGas(1_400),
                Materials.SulfurDioxide.getGas(3_800),
                Materials.SulfurTrioxide.getGas(2_100))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeNonCellRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(1_000))
            .fluidOutputs(Materials.NetherSemiFluid.getFluid(100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherSemiFluid.getFluid(100_000))
            .itemOutputs(ItemList.Heavy_Hellish_Mud.get(8))
            .fluidOutputs(
                Materials.NefariousGas.getFluid(8_000),
                FluidUtils.getFluidStack("fluid.coalgas", 16_000),
                FluidUtils.getFluidStack("fluid.anthracene", 66_000),
                Materials.SulfurTrioxide.getGas(210_000),
                Materials.SulfurDioxide.getGas(380_000),
                Materials.NitrogenDioxide.getGas(140_000),
                WerkstoffLoader.Neon.getFluidOrGas(36_000))
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
            .fluidInputs(Materials.Grade1PurifiedWater.getFluid(4_000), Materials.NefariousGas.getFluid(16_000))
            .fluidOutputs(Materials.NefariousOil.getFluid(12_000))
            .duration(26 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Grade2PurifiedWater.getFluid(1_000), Materials.NefariousGas.getFluid(16_000))
            .fluidOutputs(Materials.NefariousOil.getFluid(18_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NefariousOil.getFluid(1_000))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 572)
            .addTo(semiFluidFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Heavy_Hellish_Mud.get(32))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16_000))
            .fluidOutputs(Materials.RichNetherWaste.getFluid(16_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(mixerRecipes);

        if (EtFuturumRequiem.isModLoaded()) {
            if (ThaumicTinkerer.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GregtechItemList.TripleCompressedNetherrack.get(1),
                        getModItem(ThaumicTinkerer.ID, "kamiResource", 64, 6, missing))
                    .itemOutputs(getModItem(EtFuturumRequiem.ID, "ancient_debris", 1))
                    .duration(1 * TICKS)
                    .eut(TierEU.RECIPE_UEV)
                    .addTo(electricImplosionCompressorRecipes);
            }

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(EtFuturumRequiem.ID, "netherite_scrap", 3, missing))
                .fluidInputs(Materials.NefariousOil.getFluid(16_000))
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
                .fluidInputs(Materials.Lava.getFluid(100))
                .itemOutputs(ItemList.Hot_Netherite_Scrap.get(1))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Hot_Netherite_Scrap.get(2))
                .fluidInputs(Materials.RichNetherWaste.getFluid(2_000))
                .itemOutputs(
                    ItemList.Netherite_Scrap_Seed.get(1),
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 2, missing))
                .outputChances(1000, 10000)
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(autoclaveRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Hot_Netherite_Scrap.get(16), ItemList.Heavy_Hellish_Mud.get(16))
                .fluidInputs(Materials.PoorNetherWaste.getFluid(8_000))
                .itemOutputs(
                    ItemList.Brittle_Netherite_Scrap.get(3),
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 16, missing))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(chemicalBathRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Netherite_Scrap_Seed.get(1))
            .fluidInputs(Materials.PoorNetherWaste.getFluid(16_000))
            .itemOutputs(ItemList.Brittle_Netherite_Scrap.get(3), ItemList.Netherite_Scrap_Seed.get(1))
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
            .itemInputs(GTUtility.getIntegratedCircuit(1), ItemList.Netherite_Nanoparticles.get(1))
            .fluidInputs(Materials.HellishMetal.getMolten(1 * INGOTS))
            .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1))
            .fluidOutputs(Materials.Thaumium.getMolten(2 * NUGGETS))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 7500)
            .addTo(blastFurnaceRecipes);

        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.getIntegratedCircuit(2),
                    ItemList.Netherite_Nanoparticles.get(1),
                    GTBees.combs.getStackForType(CombType.NETHERITE, 32))
                .fluidInputs(Materials.HellishMetal.getMolten(1 * INGOTS))
                .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(2))
                .fluidOutputs(Materials.Thaumium.getMolten(2 * NUGGETS))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .metadata(COIL_HEAT, 7500)
                .addTo(blastFurnaceRecipes);
        }

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
        addNetheritePartRecipe(OrePrefixes.foil, 4, 1);
        addNetheritePartRecipe(OrePrefixes.itemCasing, 2, 1);
        addNetheritePartRecipe(OrePrefixes.gearGtSmall, 1, 1);
        addNetheritePartRecipe(OrePrefixes.rotor, 1, 5);
        addNetheritePartRecipe(OrePrefixes.stickLong, 1, 1);
        addNetheritePartRecipe(OrePrefixes.gearGt, 1, 4);
        if (EtFuturumRequiem.isModLoaded()) {
            addNetheritePartRecipe(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.InfusedGold, 1),
                getModItem(EtFuturumRequiem.ID, "netherite_block", 1, missing),
                9);
        }

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

        if (EtFuturumRequiem.isModLoaded()) {

            GTValues.RA.stdBuilder()
                .itemInputs(getModItem(EtFuturumRequiem.ID, "netherite_block", 3, missing))
                .itemOutputs(getModItem(EtFuturumRequiem.ID, "netherite_stairs", 4, missing))
                .duration(1 * HOURS)
                .eut(TierEU.RECIPE_IV)
                .addTo(cutterRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.InfusedGold, 8),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(36))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Netherite, 1))
            .fluidInputs(FluidUtils.getFluidStack("pyrotheum", 128000))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 7600)
            .addTo(blastFurnaceRecipes);
    }

    private static void addNetheritePartRecipe(OrePrefixes prefix, int multiplier, int inverseMultiplier) {

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(prefix, Materials.InfusedGold, multiplier),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(inverseMultiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.Netherite, multiplier))
            .fluidInputs(Materials.Boron.getPlasma(2L * inverseMultiplier))
            .fluidOutputs(Materials.Boron.getMolten(2L * inverseMultiplier))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(plasmaArcFurnaceRecipes);

    }

    private static void addNetheritePartRecipe(ItemStack inputStack, ItemStack outputStack, int inverseMultiplier) {

        GTValues.RA.stdBuilder()
            .itemInputs(inputStack, ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(inverseMultiplier))
            .itemOutputs(outputStack)
            .fluidInputs(Materials.Boron.getPlasma(2L * inverseMultiplier))
            .fluidOutputs(Materials.Boron.getMolten(2L * inverseMultiplier))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(plasmaArcFurnaceRecipes);

    }
}
