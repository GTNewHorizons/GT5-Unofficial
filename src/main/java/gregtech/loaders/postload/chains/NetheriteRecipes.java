package gregtech.loaders.postload.chains;

import static bartworks.API.recipe.BartWorksRecipeMaps.electricImplosionCompressorRecipes;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.ThaumicTinkerer;
import static gregtech.api.recipe.RecipeMaps.*;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.*;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.COMPRESSION_TIER;
import static gregtech.api.util.GTRecipeConstants.DISSOLUTION_TANK_RATIO;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.centrifugeNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.mixerNonCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialMisc;
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
                    MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.dust, (int) (32)))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Grade1PurifiedWater,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (8000)))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.shard, Materials.Prismarine, 8))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(autoclaveRecipes);

            if (Forestry.isModLoaded()) {
                GTValues.RA.stdBuilder() // Prismarine + Comb
                    .itemInputs(
                        GregtechItemList.RedAlgaeBiomass.get(32),
                        GTBees.combs.getStackForType(CombType.PRISMATIC, 8))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(
                            Materials2Materials.Grade1PurifiedWater,
                            Materials2FluidShapes.fluidLiquid,
                            (int) (8000)))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.shard, Materials.Prismarine, 16))
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(autoclaveRecipes);
            }

            GTValues.RA.stdBuilder() // Leaching
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.shard, Materials.Prismarine, 24))
                .fluidInputs(
                    new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 4000),
                    new FluidStack(GTPPFluids.HydrogenPeroxide, 4000))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.dust, (int) (4)))
                .fluidOutputs(Materials.PrismarineSolution.getFluid(8000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .metadata(DISSOLUTION_TANK_RATIO, 1)
                .addTo(dissolutionTankRecipes);

            GTValues.RA.stdBuilder() // Looped Leaching
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.shard, Materials.Prismarine, 6))
                .fluidInputs(
                    Materials.PrismarineContaminatedHydrogenPeroxide.getFluid(6000),
                    new FluidStack(GTPPFluids.HydrogenPeroxide, 2000))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials2Materials.NetherQuartz, Materials2Shapes.dust, (int) (1)))
                .fluidOutputs(Materials.PrismarineSolution.getFluid(8000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .metadata(DISSOLUTION_TANK_RATIO, 3)
                .addTo(dissolutionTankRecipes);

            GTValues.RA.stdBuilder() // Extraction
                .circuit(1)
                .fluidInputs(Materials.PrismarineSolution.getFluid(1000), new FluidStack(GTPPFluids.Nitrobenzene, 2000))
                .fluidOutputs(
                    Materials.PrismarineContaminatedHydrogenPeroxide.getFluid(1000),
                    Materials.PrismarineRichNitrobenzeneSolution.getFluid(2000))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder() // Looped Extraction
                .circuit(1)
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
                .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Strontium, Materials2Shapes.dust, (int) (1)))
                .itemOutputs(MaterialMisc.STRONTIUM_HYDROXIDE.getDust(3))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1000)),
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1000)))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(multiblockChemicalReactorRecipes);

            GTValues.RA.stdBuilder() // Precipitation
                .itemInputs(MaterialMisc.STRONTIUM_HYDROXIDE.getDust(42))
                .itemOutputs(ItemList.Prismarine_Precipitate.get(8))
                .fluidInputs(Materials.PrismarineRichNitrobenzeneSolution.getFluid(16000))
                .fluidOutputs(
                    Materials.PrismarineContaminatedNitrobenzeSolution.getFluid(12000),
                    new FluidStack(GTPPFluids.Nitrobenzene, 4000))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(chemicalPlantRecipes);

            GTValues.RA.stdBuilder() // Crystallization
                .itemInputs(
                    ItemList.Prismarine_Precipitate.get(1),
                    WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 0))
                .itemOutputs(ItemList.Prismatic_Crystal.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.CrystallineAlloy,
                        Materials2FluidShapes.fluidMolten,
                        (int) (72)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Strontium, Materials2FluidShapes.fluidMolten, (int) (288)))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(laserEngraverRecipes);

            GTValues.RA.stdBuilder() // Gasification
                .itemInputs(ItemList.Prismatic_Crystal.get(1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (100)))
                .fluidOutputs(Materials.PrismaticGas.getFluid(1000))
                .duration(80 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(COIL_HEAT, 7200)
                .addTo(vacuumFurnaceRecipes);

            GTValues.RA.stdBuilder() // Reaction
                .fluidInputs(
                    Materials.PrismaticGas.getFluid(4000),
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.LiquidNitrogen,
                        Materials2FluidShapes.fluidGas,
                        (int) (12000)))
                .fluidOutputs(Materials.PrismaticAcid.getFluid(16000))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(vacuumFreezerRecipes);
        }

        // Naquarite
        {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.NaquadahEnriched, Materials2Shapes.dust, (int) (32)))
                .circuit(2)
                .fluidInputs(Materials.PrismaticAcid.getFluid(8000))
                .fluidOutputs(Materials.PrismaticNaquadah.getMolten(2304))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(alloyBlastSmelterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MetaItemCraftingComponent.getInstance()
                        .getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 4),
                    MaterialLibAPI.getStack(Materials2Materials.Mica, Materials2Shapes.dust, (int) (32)))
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
                    MaterialLibAPI.getStack(Materials2Materials.TungstenCarbide, Materials2Shapes.foil, (int) (16)),
                    MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.foil, (int) (16)))
                .fluidInputs(Materials.PrismaticNaquadahCompositeSlurry.getFluid(8000))
                .itemOutputs(ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(16))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(chemicalDehydratorNonCellRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.foil, (int) (4)),
                    ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(16))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Manyullyn, Materials2FluidShapes.fluidMolten, (int) (1152)))
                .itemOutputs(ItemList.Naquarite_Universal_Insulator_Foil.get(8))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(formingPressRecipes);
        }

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NetherAir.getFluid(10_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.fluidGas, (int) (1_400)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (3_800)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.fluidGas, (int) (2_100)))
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
                new FluidStack(GTPPFluids.CoalGas, 16_000),
                new FluidStack(GTPPFluids.Anthracene, 66_000),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurTrioxide, Materials2FluidShapes.fluidGas, (int) (210_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (380_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.NitrogenDioxide,
                    Materials2FluidShapes.fluidGas,
                    (int) (140_000)),
                WerkstoffLoader.Neon.getFluidOrGas(36_000))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Grade1PurifiedWater,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)),
                Materials.NefariousGas.getFluid(16_000))
            .fluidOutputs(Materials.NefariousOil.getFluid(12_000))
            .duration(26 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Grade2PurifiedWater,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                Materials.NefariousGas.getFluid(16_000))
            .fluidOutputs(Materials.NefariousOil.getFluid(18_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

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
            .itemInputs(ItemList.Netherite_Nanoparticles.get(1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.HellishMetal,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Thaumium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * NUGGETS)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 7500)
            .addTo(blastFurnaceRecipes);

        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Netherite_Nanoparticles.get(1),
                    GTBees.combs.getStackForType(CombType.NETHERITE, 8))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.HellishMetal,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(2))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Thaumium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (2 * NUGGETS)))
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
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.stick, (int) (4)))
            .circuit(4)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite, 1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.plate, (int) (64)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.plateSuperdense, (int) (1)))
            .duration(500 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COMPRESSION_TIER, 1)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.InfusedGold, Materials2Shapes.dust, (int) (1)),
                ItemList.Netherite_Nanoparticles.get(1))
            .duration(23 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.ingot, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.dust, (int) (1)))
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
                MaterialLibAPI.getStack(Materials2Materials.InfusedGold, Materials2Shapes.plateDense, (int) (8)),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(36))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Netherite, Materials2Shapes.plateDense, (int) (1)))
            .fluidInputs(new FluidStack(GTPPFluids.Pyrotheum, 128_000))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Boron,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (2 * inverseMultiplier)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Boron,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * inverseMultiplier)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(arcFurnaceRecipes);

    }

    private static void addNetheritePartRecipe(ItemStack inputStack, ItemStack outputStack, int inverseMultiplier) {

        GTValues.RA.stdBuilder()
            .itemInputs(inputStack, ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(inverseMultiplier))
            .itemOutputs(outputStack)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Boron,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (2 * inverseMultiplier)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Boron,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * inverseMultiplier)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(arcFurnaceRecipes);

    }
}
