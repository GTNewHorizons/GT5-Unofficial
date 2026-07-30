package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static goodgenerator.loader.Loaders.advancedRadiationProtectionPlate;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.quantumForceTransformerRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;

import gregtech.api.enums.materials2.Materials;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2Backings;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.materials.RecognitionMaterials;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.block.BlockQuantumGlass;

public class RecipeLoaderChemicalSkips {

    public static void generate() {
        createRecipes();
    }

    private static void createRecipes() {
        quantumTransformerRecipes();
        fusionReactorRecipes();
        catalystRecipes();
        tieredCasingRecipes();
    }

    // All the recipes that the QFT can do. Each recipe has a machine tier.
    // -> Tier 1 is UEV (UEV circuits and 1 Eternal Singularity);
    // -> Tier 2 needs new item from QFT, plus stacks of Infinity;
    // -> Tier 3 needs new item from QFT, plus stacks of Transcendent Metal;
    // -> Tier 4 needs new item from QFT, plus stacks of Spacetime;
    // (Until they are created, the new items are represented by
    // HSS-G for Tier 2, HSS-S for Tier 3 and HSS-E for Tier 4)

    private static void quantumTransformerRecipes() {
        ItemStack stemcells = GTUtility.copyAmountUnsafe(64 * 32, ItemList.Circuit_Chip_Stemcell.get(1));
        ItemStack biocells = GTUtility.copyAmountUnsafe(64 * 32, ItemList.Circuit_Chip_Biocell.get(1));
        // Platline
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Materials2Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Platinum, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Palladium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iridium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Rhodium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Ruthenium, Materials2Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Partial platline (from Pd, Os, Ir, Rh and leach)
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Materials2Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Palladium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Platinum, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Materials2Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Materials2Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iridium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Platinum, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmiridium, Materials2Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RarestMetalResidue, Materials2Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Osmium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iridium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmiridium, Materials2Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CrudeRhodiumMetal, Materials2Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Rhodium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Palladium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Platinum, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Materials2Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LeachResidue, Materials2Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iridium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Rhodium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Ruthenium, Materials2Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Plastics
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, (int) (64)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, (int) (16_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Plastic, Materials2FluidShapes.fluidMolten, (int) (4 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials.PolyvinylChloride,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials.Polystyrene,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials.Polytetrafluoroethylene,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * STACKS)),
                MaterialLibAPI
                    .getFluidStack(Materials.Epoxid, Materials2FluidShapes.fluidMolten, (int) (1 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials.Polybenzimidazole,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * STACKS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.PlasticPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Rubbers/Cable Materials
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, (int) (64)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (16_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Silicone, Materials2FluidShapes.fluidMolten, (int) (1 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials.StyreneButadieneRubber,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * STACKS)),
                MaterialLibAPI.getFluidStack(
                    Materials.PolyphenyleneSulfide,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * STACKS)),
                MaterialLibAPI
                    .getFluidStack(Materials.Rubber, Materials2FluidShapes.fluidMolten, (int) (4 * STACKS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.RubberPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .noBuffer()
            .addTo(quantumForceTransformerRecipes);
        // Glues and Solders
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Bismuth, Materials2Shapes.dust, (int) (32)))
            .itemOutputs(ItemList.StableAdhesive.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (10_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (10_000)))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 32_000),
                MaterialLibAPI
                    .getFluidStack(Materials.AdvancedGlue, Materials2FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialUtils.legacyGtppFluid(Materials.Indalloy140, 1 * STACKS),
                MaterialLibAPI.getFluidStack(
                    Materials.SolderingAlloy,
                    Materials2FluidShapes.fluidMolten,
                    (int) (2 * STACKS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.AdhesionPromoterCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Titanium, Tungsten, Indium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Lead, Materials2Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials.Bauxite, Materials2Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Tungstate, Materials2Shapes.dust, (int) (16)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Titanium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Indium, Materials2Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Rutile, Materials2Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Scheelite, Materials2Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials.Ilmenite, Materials2Shapes.dust, (int) (16)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Titanium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Tantalum, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Indium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Niobium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Hafnium, Materials2Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Thorium, Uranium, Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Thorium, Materials2Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Uranium, Materials2Shapes.dust, (int) (32)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorium232, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Uranium233, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Uranium235, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Plutonium238, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Plutonium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Materials2Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.RadioactivityCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Monaline
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Monazite, Materials2Shapes.dust, (int) (32)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Cerium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Samarium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Hafnia, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Zirconium, Materials2Shapes.dust, 64),
                ItemList.SuperconductorComposite.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Bastnasite, Materials2Shapes.dust, (int) (32)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Holmium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Cerium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Samarium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Lanthanum, Materials2Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline from Cerium-rich mixture
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CeriumRichMixture, Materials2Shapes.dust, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Holmium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Cerium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Samarium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Lanthanum, Materials2Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        if (EtFuturumRequiem.isModLoaded()) {
            // Netherite
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmountUnsafe(
                        64 * 16,
                        MaterialLibAPI.getStack(Materials.Netherrack, Materials2Shapes.dust, (int) (1))),
                    getModItem(EtFuturumRequiem.ID, "ancient_debris", 1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.netherair,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (64_000)),
                    MaterialLibAPI.getFluidStack(
                        Materials.HellishMetal,
                        Materials2FluidShapes.fluidMolten,
                        (int) (8 * INGOTS)),
                    GTUtility.getLava(256_000))
                .itemOutputs(
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 16),
                    ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(64))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.nefariousoil,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (64_000)))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(QFT_CATALYST, GregtechItemList.HellishForceCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 2)
                .addTo(quantumForceTransformerRecipes);
        }
        // Prismarine
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.shard, RecognitionMaterials.Prismarine, 12),
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Materials2Shapes.dust, (int) (32)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (24_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.prismaticnaquadah, Materials2Shapes.dust, (int) (16)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.prismaticacid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (32_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Strontium, Materials2FluidShapes.fluidMolten, (int) (144 * 16)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.CrystalColorizationCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Stem Cells
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcium, Materials2Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.MeatRaw, Materials2Shapes.dust, (int) (32)),
                getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 32, 2))
            .itemOutputs(stemcells)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GrowthMediumRaw,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_024_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.GrowthMediumSterilized,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (512_000)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_CATALYST, GregtechItemList.RawIntelligenceCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Lategame Plastics (Missing Radox Polymer and Heavy Radox)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmium, Materials2Shapes.dust, (int) (24)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (16_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Xenoxene, Materials2FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.RadoxPoly,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * STACKS)),
                MaterialLibAPI
                    .getFluidStack(Materials.HeavyRadox, Materials2FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.Kevlar, Materials2FluidShapes.fluidMolten, (int) (1 * STACKS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.UltimatePlasticCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        if (Mods.Forestry.isModLoaded()) {
            // Lategame Kevlar using Kevlar bee comb
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTBees.combs.getStackForType(CombType.KEVLAR, 24),
                    MaterialLibAPI.getStack(Materials.Carbon, Materials2Shapes.dust, (int) (64)))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, (int) (16_000)),
                    MaterialLibAPI
                        .getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (16_000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.PolyurethaneResin,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (32_000)),
                    MaterialLibAPI.getFluidStack(
                        Materials.LiquidCrystalKevlar,
                        Materials2FluidShapes.fluidLiquid,
                        (int) (32 * INGOTS)),
                    MaterialLibAPI.getFluidStack(
                        Materials.Kevlar,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * STACKS)))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .metadata(QFT_CATALYST, GregtechItemList.UltimatePlasticCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 4)
                .addTo(quantumForceTransformerRecipes);
            // Platline skip using Platline Combs (Palladium, Osmium, Iridium, Platinum)
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTBees.combs.getStackForType(CombType.PLATINUM, 32),
                    GTBees.combs.getStackForType(CombType.PALLADIUM, 32),
                    GTBees.combs.getStackForType(CombType.OSMIUM, 32),
                    GTBees.combs.getStackForType(CombType.IRIDIUM, 32))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(
                        Materials.Osmium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (4 * STACKS)),
                    MaterialLibAPI.getFluidStack(
                        Materials.Palladium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (4 * STACKS)),
                    MaterialLibAPI.getFluidStack(
                        Materials.Iridium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (4 * STACKS)),
                    MaterialLibAPI.getFluidStack(
                        Materials.Platinum,
                        Materials2FluidShapes.fluidMolten,
                        (int) (4 * STACKS)))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 1)
                .addTo(quantumForceTransformerRecipes);
        }
        // Bio Cells and Mutated Solder
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_Stemcell.get(16),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Materials2Shapes.dust, (int) (4)))
            .itemOutputs(biocells)
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.MutatedLivingSolder, 2 * STACKS),
                MaterialLibAPI.getFluidStack(
                    Materials.BiohMediumSterilized,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1000 * 256)),
                MaterialLibAPI.getFluidStack(
                    Materials.BioMediumRaw,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1000 * 512)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.BiologicalIntelligenceCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);

        if (GalaxySpace.isModLoaded()) {
            // Seaweed
            ItemStack seaweed = GTUtility
                .copyAmountUnsafe(64 * 32, getModItem(GalaxySpace.ID, "tcetiedandelions", 1, 4));
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get("cropSeaweed", 64),
                    MaterialLibAPI.getStack(Materials.Mytryl, Materials2Shapes.dust, (int) (16)))
                .itemOutputs(seaweed, getModItem(NewHorizonsCoreMod.ID, "TCetiESeaweedExtract", 16))
                .fluidInputs(FluidRegistry.getFluidStack("unknowwater", 25_000))
                .fluidOutputs(
                    FluidRegistry.getFluidStack("seaweedbroth", 50_000),
                    MaterialLibAPI
                        .getFluidStack(Materials.Iodine, Materials2FluidShapes.fluidLiquid, (int) (64_000)))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .metadata(QFT_CATALYST, GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 4)
                .addTo(quantumForceTransformerRecipes);

        }
    }

    private static void fusionReactorRecipes() {
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Radon, Materials2FluidShapes.fluidPlasma, (int) (100)),
                MaterialLibAPI
                    .getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, (int) (100)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Americium, Materials2FluidShapes.fluidPlasma, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Boron, Materials2FluidShapes.fluidPlasma, (int) (100)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        // MK5 versions
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Xenon), 4 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Yttrium, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 4 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Force), 4 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Rubidium, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 4 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);
    }

    private static void catalystRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Osmiridium, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64))
            .circuit(10)
            .itemOutputs(GregtechItemList.PlatinumGroupCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Polytetrafluoroethylene, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64))
            .circuit(10)
            .itemOutputs(GregtechItemList.PlasticPolymerCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Silicone, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.StyreneButadieneRubber, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64))
            .circuit(10)
            .itemOutputs(GregtechItemList.RubberPolymerCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Indalloy140, Materials2Shapes.dust, 64),
                MaterialLibAPI
                    .getStack(Materials.EthylCyanoacrylateSuperGlue, Materials2CellShapes.cellMolten, 64),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64))
            .circuit(10)
            .itemOutputs(GregtechItemList.AdhesionPromoterCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Indium, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64))
            .circuit(10)
            .itemOutputs(GregtechItemList.TitaTungstenIndiumCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Uranium235, Materials2Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Plutonium241, Materials2Shapes.dust, 64),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 64))
            .circuit(10)
            .itemOutputs(GregtechItemList.RadioactivityCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Samarium, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.RareEarthGroupCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Naquadah, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Adamantium, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.SimpleNaquadahCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        if (EtFuturumRequiem.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.EmptyCatalystCarrier.get(1),
                    MaterialLibAPI.getStack(Materials.Netherite, Materials2Shapes.dust, (int) (64)),
                    MaterialLibAPI.getStack(Materials.InfusedGold, Materials2Shapes.dust, (int) (64)),
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 1),
                    GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1))
                .circuit(10)
                .itemOutputs(GregtechItemList.HellishForceCatalyst.get(1))
                .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 1 * STACKS))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(assemblerRecipes);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.prismaticnaquadah, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.shard, RecognitionMaterials.Prismarine, 64),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.CrystalColorizationCatalyst.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Hypogen, 1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Naquadria, Materials2Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Trinium, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.AdvancedNaquadahCatalyst.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SpaceTime,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * STACKS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                ItemList.Circuit_Chip_Stemcell.get(64),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.RawIntelligenceCatalyst.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SpaceTime,
                    Materials2FluidShapes.fluidMolten,
                    (int) (1 * STACKS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Kevlar, Materials2Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.UltimatePlasticCatalyst.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Shirabon,
                    Materials2FluidShapes.fluidMolten,
                    (int) (10 * STACKS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                ItemList.Circuit_Chip_Biocell.get(64),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.BiologicalIntelligenceCatalyst.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Shirabon,
                    Materials2FluidShapes.fluidMolten,
                    (int) (10 * STACKS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                GTOreDictUnificator.get("blockShirabon", 16),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Universium, 1),
                ItemList.Timepiece.get(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.TemporalHarmonyCatalyst.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DarkIron,
                    Materials2FluidShapes.fluidMolten,
                    (int) (10 * STACKS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                getModItem(NewHorizonsCoreMod.ID, "TCetiESeaweedExtract", 64),
                GTOreDictUnificator.get("dustIodine", 64),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Shirabon,
                    Materials2FluidShapes.fluidMolten,
                    (int) (10 * STACKS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
    }

    private static void tieredCasingRecipes() {
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.ForceFieldGlass.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 4), ItemList.Emitter_UV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials2Backings.SuperconductorUHV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 2) },
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials.Thulium, Materials2FluidShapes.fluidMolten, (int) (144 * 10)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTCC, Materials2FluidShapes.fluidLiquid, (int) (5_000)),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 500),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 500) },
            GregtechItemList.NeutronPulseManipulator.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronPulseManipulator.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(2),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 8), ItemList.Emitter_UEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials2Backings.SuperconductorUEV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 4),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials.Thulium, Materials2FluidShapes.fluidMolten, (int) (144 * 12)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTPC, Materials2FluidShapes.fluidLiquid, (int) (5_000)),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 2_500),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 2_500) },
            GregtechItemList.CosmicFabricManipulator.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricManipulator.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(4),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 16),
                ItemList.Emitter_UIV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials2Backings.SuperconductorUIV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 8),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials.Thulium, Materials2FluidShapes.fluidMolten, (int) (144 * 15)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTRC, Materials2FluidShapes.fluidLiquid, (int) (5_000)),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 10_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 10_000) },
            GregtechItemList.InfinityInfusedManipulator.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedManipulator.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 32),
                ItemList.Emitter_UMV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials2Backings.SuperconductorUMV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] {
                MaterialLibAPI
                    .getFluidStack(Materials.Thulium, Materials2FluidShapes.fluidMolten, (int) (20 * INGOTS)),
                MaterialLibAPI
                    .getFluidStack(Materials.ExcitedDTEC, Materials2FluidShapes.fluidLiquid, (int) (5_000)),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 20_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 20_000) },
            GregtechItemList.SpaceTimeContinuumRipper.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UXV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_AdvancedRadiationProof.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, Materials2PipeShapes.frameGt, 1),
                GTOreDictUnificator.get("plateDensePreciousMetalsAlloy", 4),
                MaterialLibAPI.getStack(Materials.Netherite, Materials2Shapes.plateSuperdense, (int) (2)),
                ItemList.Field_Generator_UV.get(1),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Materials2Shapes.screw, 16) },
            new FluidStack[] { MaterialUtils.legacyGtppFluid(Materials.MutatedLivingSolder, 10 * INGOTS), },
            GregtechItemList.NeutronShieldingCore.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronShieldingCore.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, Materials2PipeShapes.frameGt, 2),
                GTOreDictUnificator.get("plateDenseEnrichedNaquadahAlloy", 4),
                MaterialLibAPI.getStack(Materials.Infinity, Materials2Shapes.plateSuperdense, (int) (2)),
                ItemList.Field_Generator_UEV.get(1),
                MaterialLibAPI.getStack(Materials.RadoxPoly, Materials2Shapes.screw, (int) (16)),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] { MaterialUtils.legacyGtppFluid(Materials.MutatedLivingSolder, 20 * INGOTS), },
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_UHV,
            32,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, Materials2PipeShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.Hypogen, Materials2Shapes.plateDense, 4),
                MaterialLibAPI
                    .getStack(Materials.protohalkonite, Materials2Shapes.plateSuperdense, (int) (2)),
                ItemList.Field_Generator_UIV.get(1), GTOreDictUnificator.get("screwMetastableOganesson", 16),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] { MaterialUtils.legacyGtppFluid(Materials.MutatedLivingSolder, 40 * INGOTS), },
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            8192 * 30 * 20,
            8192,
            (int) TierEU.RECIPE_UEV,
            32,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, Materials2PipeShapes.frameGt, 8),
                GTOreDictUnificator.get("plateDenseShirabon", 4),
                MaterialLibAPI.getStack(Materials.SpaceTime, Materials2Shapes.plateSuperdense, (int) (2)),
                ItemList.Field_Generator_UMV.get(1),
                MaterialLibAPI.getStack(Materials.Dilithium, Materials2Shapes.screw, (int) (16)),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] {
                MaterialUtils.legacyGtppFluid(Materials.MutatedLivingSolder, 1 * STACKS + 16 * INGOTS), },
            GregtechItemList.SpaceTimeBendingCore.get(1),
            120 * 20,
            (int) TierEU.RECIPE_UXV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(BlockQuantumGlass.INSTANCE, 1),
                ItemList.Field_Generator_ZPM.get(1),
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Materials2Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Netherite, Materials2Shapes.stickLong, (int) (4)),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Materials2Shapes.plate, 6))
            .itemOutputs(GregtechItemList.ForceFieldGlass.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Quantum, 6 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

    }
}
