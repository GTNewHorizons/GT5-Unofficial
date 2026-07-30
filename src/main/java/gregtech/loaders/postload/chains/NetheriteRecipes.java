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

import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.materials.RecognitionMaterials;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.fluids.GTPPFluids;
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
                    MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, (int) (32)))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.Grade1PurifiedWater,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (8000)))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.shard, RecognitionMaterials.Prismarine, 8))
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
                            Materials.Grade1PurifiedWater,
                            Materials2FluidShapes.fluidLiquid,
                            (int) (8000)))
                    .itemOutputs(GTOreDictUnificator.get(OrePrefixes.shard, RecognitionMaterials.Prismarine, 16))
                    .duration(15 * SECONDS)
                    .eut(TierEU.RECIPE_LuV)
                    .addTo(autoclaveRecipes);
            }

            GTValues.RA.stdBuilder() // Leaching
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.shard, RecognitionMaterials.Prismarine, 24))
                .fluidInputs(
                    new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 4000),
                    new FluidStack(GTPPFluids.HydrogenPeroxide, 4000))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.CertusQuartz, Shapes.dust, (int) (4)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismarinesolution, Materials2FluidShapes.fluidLiquid, 8000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .metadata(DISSOLUTION_TANK_RATIO, 1)
                .addTo(dissolutionTankRecipes);

            GTValues.RA.stdBuilder() // Looped Leaching
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.shard, RecognitionMaterials.Prismarine, 6))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinecontaminatedhydrogenperoxide,
                        Materials2FluidShapes.fluidLiquid,
                        6000),
                    new FluidStack(GTPPFluids.HydrogenPeroxide, 2000))
                .itemOutputs(
                    MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.dust, (int) (1)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismarinesolution, Materials2FluidShapes.fluidLiquid, 8000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .metadata(DISSOLUTION_TANK_RATIO, 3)
                .addTo(dissolutionTankRecipes);

            GTValues.RA.stdBuilder() // Extraction
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismarinesolution, Materials2FluidShapes.fluidLiquid, 1000),
                    new FluidStack(GTPPFluids.Nitrobenzene, 2000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinecontaminatedhydrogenperoxide,
                        Materials2FluidShapes.fluidLiquid,
                        1000),
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinerichnitrobenzenesolution,
                        Materials2FluidShapes.fluidLiquid,
                        2000))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder() // Looped Extraction
                .circuit(1)
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismarinesolution, Materials2FluidShapes.fluidLiquid, 1000),
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinecontaminatednitrobenzenesolution,
                        Materials2FluidShapes.fluidLiquid,
                        3000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinecontaminatedhydrogenperoxide,
                        Materials2FluidShapes.fluidLiquid,
                        1000),
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinerichnitrobenzenesolution,
                        Materials2FluidShapes.fluidLiquid,
                        2000))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder() // Strontium Hydroxide
                .itemInputs(MaterialLibAPI.getStack(Materials.Strontium, Shapes.dust, (int) (1)))
                .itemOutputs(MaterialLibAPI.getStack(Materials.StrontiumHydroxide, Shapes.dust, 3))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (1000)),
                    MaterialLibAPI
                        .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1000)))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(multiblockChemicalReactorRecipes);

            GTValues.RA.stdBuilder() // Precipitation
                .itemInputs(MaterialLibAPI.getStack(Materials.StrontiumHydroxide, Shapes.dust, 42))
                .itemOutputs(ItemList.Prismarine_Precipitate.get(8))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinerichnitrobenzenesolution,
                        Materials2FluidShapes.fluidLiquid,
                        16000))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismarinecontaminatednitrobenzenesolution,
                        Materials2FluidShapes.fluidLiquid,
                        12000),
                    new FluidStack(GTPPFluids.Nitrobenzene, 4000))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(chemicalPlantRecipes);

            GTValues.RA.stdBuilder() // Crystallization
                .itemInputs(
                    ItemList.Prismarine_Precipitate.get(1),
                    MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.lens, 0))
                .itemOutputs(ItemList.Prismatic_Crystal.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.CrystallineAlloy,
                        Materials2FluidShapes.fluidMolten,
                        (int) (72)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Strontium, Materials2FluidShapes.fluidMolten, (int) (288)))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(laserEngraverRecipes);

            GTValues.RA.stdBuilder() // Gasification
                .itemInputs(ItemList.Prismatic_Crystal.get(1))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (100)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismaticgas, Materials2FluidShapes.fluidLiquid, 1000))
                .duration(80 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .metadata(COIL_HEAT, 7200)
                .addTo(vacuumFurnaceRecipes);

            GTValues.RA.stdBuilder() // Reaction
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismaticgas, Materials2FluidShapes.fluidLiquid, 4000),
                    MaterialLibAPI.getFluidStack(
                        Materials.LiquidNitrogen,
                        Materials2FluidShapes.fluidGas,
                        (int) (12000)))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismaticacid, Materials2FluidShapes.fluidLiquid, 16000))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(vacuumFreezerRecipes);
        }

        // Naquarite
        {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, (int) (32)))
                .circuit(2)
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismaticacid, Materials2FluidShapes.fluidLiquid, 8000))
                .fluidOutputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismaticnaquadah, Materials2FluidShapes.fluidMolten, 2304))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_LuV)
                .addTo(alloyBlastSmelterRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MetaItemCraftingComponent.getInstance()
                        .getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 4),
                    MaterialLibAPI.getStack(Materials.Mica, Shapes.dust, (int) (32)))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismaticnaquadah, Materials2FluidShapes.fluidMolten, 576))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismaticnaquadahcompositeslurry,
                        Materials2FluidShapes.fluidLiquid,
                        2000))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MetaItemCraftingComponent.getInstance()
                        .getStackOfAmountFromDamage(Items.YSZCeramicDust.getMetaID(), 4),
                    MaterialLibAPI.getStack(Materials.RawFluorophlogopite, Shapes.dust, 24))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.prismaticnaquadah, Materials2FluidShapes.fluidMolten, 576))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismaticnaquadahcompositeslurry,
                        Materials2FluidShapes.fluidLiquid,
                        2000))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(mixerNonCellRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.foil, (int) (16)),
                    MaterialLibAPI.getStack(Materials.Lead, Shapes.foil, (int) (16)))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.prismaticnaquadahcompositeslurry,
                        Materials2FluidShapes.fluidLiquid,
                        8000))
                .itemOutputs(ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(16))
                .duration(16 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(chemicalDehydratorNonCellRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(
                    MaterialLibAPI.getStack(Materials.Netherite, Shapes.foil, (int) (4)),
                    ItemList.Radiation_Proof_Prismatic_Naquadah_Composite_Sheet.get(16))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Manyullyn, Materials2FluidShapes.fluidMolten, (int) (1152)))
                .itemOutputs(ItemList.Naquarite_Universal_Insulator_Foil.get(8))
                .duration(24 * SECONDS)
                .eut(TierEU.RECIPE_ZPM)
                .addTo(formingPressRecipes);
        }

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.netherair, Materials2FluidShapes.fluidLiquid, 10_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.NitrogenDioxide, Materials2FluidShapes.fluidGas, (int) (1_400)),
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (3_800)),
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurTrioxide, Materials2FluidShapes.fluidGas, (int) (2_100)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeNonCellRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.netherair, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.nethersemifluid, Materials2FluidShapes.fluidLiquid, 100))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.nethersemifluid, Materials2FluidShapes.fluidLiquid, 100_000))
            .itemOutputs(ItemList.Heavy_Hellish_Mud.get(8))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.nefariousgas, Materials2FluidShapes.fluidLiquid, 8_000),
                new FluidStack(GTPPFluids.CoalGas, 16_000),
                new FluidStack(GTPPFluids.Anthracene, 66_000),
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurTrioxide, Materials2FluidShapes.fluidGas, (int) (210_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (380_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.NitrogenDioxide,
                    Materials2FluidShapes.fluidGas,
                    (int) (140_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Neon, Materials2FluidShapes.fluidLiquid, (int) (36_000)))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Grade1PurifiedWater,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.nefariousgas, Materials2FluidShapes.fluidLiquid, 16_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.nefariousoil, Materials2FluidShapes.fluidLiquid, 12_000))
            .duration(26 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Grade2PurifiedWater,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.nefariousgas, Materials2FluidShapes.fluidLiquid, 16_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.nefariousoil, Materials2FluidShapes.fluidLiquid, 18_000))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Heavy_Hellish_Mud.get(32))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.poornetherwaste, Materials2FluidShapes.fluidLiquid, 16_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.richnetherwaste, Materials2FluidShapes.fluidLiquid, 16_000))
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
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.nefariousoil, Materials2FluidShapes.fluidLiquid, 16_000))
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
                .fluidInputs(GTUtility.getLava(100))
                .itemOutputs(ItemList.Hot_Netherite_Scrap.get(1))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Hot_Netherite_Scrap.get(2))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.richnetherwaste, Materials2FluidShapes.fluidLiquid, 2_000))
                .itemOutputs(
                    ItemList.Netherite_Scrap_Seed.get(1),
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 2, missing))
                .outputChances(1000, 10000)
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_IV)
                .addTo(autoclaveRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.Hot_Netherite_Scrap.get(16), ItemList.Heavy_Hellish_Mud.get(16))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.poornetherwaste, Materials2FluidShapes.fluidLiquid, 8_000))
                .itemOutputs(
                    ItemList.Brittle_Netherite_Scrap.get(3),
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 16, missing))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .addTo(chemicalBathRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Netherite_Scrap_Seed.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.poornetherwaste, Materials2FluidShapes.fluidLiquid, 16_000))
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
                    Materials.HellishMetal,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * INGOTS)))
            .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Thaumium,
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
                        Materials.HellishMetal,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .itemOutputs(ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(2))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.Thaumium,
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
        addNetheritePartRecipe(Shapes.ingot, 1, 1);
        addNetheritePartRecipe(Shapes.plate, 1, 1);
        addNetheritePartRecipe(Shapes.plateDouble, 1, 2);
        addNetheritePartRecipe(Shapes.plateDense, 1, 9);
        addNetheritePartRecipe(Shapes.stick, 2, 1);
        addNetheritePartRecipe(Shapes.round, 9, 1);
        addNetheritePartRecipe(Shapes.bolt, 8, 1);
        addNetheritePartRecipe(Shapes.screw, 8, 1);
        addNetheritePartRecipe(Shapes.ring, 4, 1);
        addNetheritePartRecipe(Shapes.foil, 4, 1);
        addNetheritePartRecipe(Shapes.itemCasing, 2, 1);
        addNetheritePartRecipe(Shapes.gearGtSmall, 1, 1);
        addNetheritePartRecipe(Shapes.rotor, 1, 5);
        addNetheritePartRecipe(Shapes.stickLong, 1, 1);
        addNetheritePartRecipe(Shapes.gearGt, 1, 4);
        if (EtFuturumRequiem.isModLoaded()) {
            addNetheritePartRecipe(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.InfusedGold, 1),
                getModItem(EtFuturumRequiem.ID, "netherite_block", 1, missing),
                9);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Netherite, Shapes.stick, (int) (4)))
            .circuit(4)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Netherite, 1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Netherite, Shapes.plate, (int) (64)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.plateSuperdense, (int) (1)))
            .duration(500 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COMPRESSION_TIER, 1)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.InfusedGold, Shapes.dust, (int) (1)),
                ItemList.Netherite_Nanoparticles.get(1))
            .duration(23 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Netherite, Shapes.ingot, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, (int) (1)))
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
                MaterialLibAPI.getStack(Materials.InfusedGold, Shapes.plateDense, (int) (8)),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(36))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Netherite, Shapes.plateDense, (int) (1)))
            .fluidInputs(new FluidStack(GTPPFluids.Pyrotheum, 128_000))
            .duration(180 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 7600)
            .addTo(blastFurnaceRecipes);
    }

    private static void addNetheritePartRecipe(Shape shape, int multiplier, int inverseMultiplier) {

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.InfusedGold, shape, multiplier),
                ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(inverseMultiplier))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Netherite, shape, multiplier))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Boron,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (2 * inverseMultiplier)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Boron,
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
                    Materials.Boron,
                    Materials2FluidShapes.fluidPlasma,
                    (int) (2 * inverseMultiplier)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Boron,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * inverseMultiplier)))
            .duration(4 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(arcFurnaceRecipes);

    }
}
