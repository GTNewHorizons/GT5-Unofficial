package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static goodgenerator.loader.Loaders.advancedRadiationProtectionPlate;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;
import static gregtech.api.util.GTRecipeConstants.QFT_FOCUS_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.MaterialFacades;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.PipeShapes;
import gregtech.api.enums.materials.Shapes;
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
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Partial platline (from Pd, Os, Ir, Rh and leach)
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CrudeRhodiumMetal, Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, 32))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Plastics
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (64)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.Fluorine, FluidShapes.fluidGas, (int) (16_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, (int) (4 * STACKS)),
                MaterialLibAPI.getFluidStack(Materials.PolyvinylChloride, FluidShapes.fluidMolten, (int) (2 * STACKS)),
                MaterialLibAPI.getFluidStack(Materials.Polystyrene, FluidShapes.fluidMolten, (int) (1 * STACKS)),
                MaterialLibAPI
                    .getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, (int) (2 * STACKS)),
                MaterialLibAPI.getFluidStack(Materials.Epoxid, FluidShapes.fluidMolten, (int) (1 * STACKS)),
                MaterialLibAPI.getFluidStack(Materials.Polybenzimidazole, FluidShapes.fluidMolten, (int) (1 * STACKS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.PlasticPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Rubbers/Cable Materials
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (64)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (16_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Silicone, FluidShapes.fluidMolten, (int) (1 * STACKS)),
                MaterialLibAPI
                    .getFluidStack(Materials.StyreneButadieneRubber, FluidShapes.fluidMolten, (int) (1 * STACKS)),
                MaterialLibAPI
                    .getFluidStack(Materials.PolyphenyleneSulfide, FluidShapes.fluidMolten, (int) (2 * STACKS)),
                MaterialLibAPI.getFluidStack(Materials.Rubber, FluidShapes.fluidMolten, (int) (4 * STACKS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.RubberPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .noBuffer()
            .addTo(quantumForceTransformerRecipes);
        // Glues and Solders
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Bismuth, Shapes.dust, (int) (32)))
            .itemOutputs(ItemList.StableAdhesive.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (10_000)),
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (10_000)))
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.EthylCyanoacrylateSuperGlue, 32_000),
                MaterialLibAPI.getFluidStack(Materials.AdvancedGlue, FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialUtils.legacyGtppFluid(Materials.Indalloy140, 1 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.SolderingAlloy, FluidShapes.fluidMolten, (int) (2 * STACKS)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.AdhesionPromoterCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Titanium, Tungsten, Indium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Lead, Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials.Bauxite, Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Tungstate, Shapes.dust, (int) (16)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Scheelite, Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials.Ilmenite, Shapes.dust, (int) (16)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Tantalum, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Niobium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Hafnium, Shapes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Thorium, Uranium, Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, (int) (32)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorium232, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Uranium233, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Plutonium238, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Plutonium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.RadioactivityCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Monaline
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Monazite, Shapes.dust, (int) (32)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Hafnia, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.dust, 64),
                ItemList.SuperconductorComposite.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Bastnasite, Shapes.dust, (int) (32)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Holmium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.dust, (int) (64)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline from Cerium-rich mixture
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 16))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Holmium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.dust, (int) (64)))
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
                        MaterialLibAPI.getStack(Materials.Netherrack, Shapes.dust, (int) (1))),
                    getModItem(EtFuturumRequiem.ID, "ancient_debris", 1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.netherair, FluidShapes.fluidLiquid, (int) (64_000)),
                    MaterialLibAPI.getFluidStack(Materials.HellishMetal, FluidShapes.fluidMolten, (int) (8 * INGOTS)),
                    GTUtility.getLava(256_000))
                .itemOutputs(
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 16),
                    ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(64))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(Materials.nefariousoil, FluidShapes.fluidLiquid, (int) (64_000)))
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
                MaterialLibAPI.getStack(Materials.NaquadahEnriched, Shapes.dust, (int) (32)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, FluidShapes.fluidGas, (int) (2_000)),
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (24_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.prismaticnaquadah, Shapes.dust, (int) (16)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.prismaticacid, FluidShapes.fluidLiquid, (int) (32_000)),
                MaterialLibAPI.getFluidStack(Materials.Strontium, FluidShapes.fluidMolten, (int) (144 * 16)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.CrystalColorizationCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Stem Cells
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (32)),
                MaterialLibAPI.getStack(Materials.MeatRaw, Shapes.dust, (int) (32)),
                getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 32, 2))
            .itemOutputs(stemcells)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.GrowthMediumRaw, FluidShapes.fluidLiquid, (int) (1_024_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.GrowthMediumSterilized, FluidShapes.fluidLiquid, (int) (512_000)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_CATALYST, GregtechItemList.RawIntelligenceCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Lategame Plastics (Missing Radox Polymer and Heavy Radox)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, (int) (24)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (16_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Xenoxene, FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.RadoxPoly, FluidShapes.fluidMolten, (int) (1 * STACKS)),
                MaterialLibAPI.getFluidStack(Materials.HeavyRadox, FluidShapes.fluidLiquid, (int) (16_000)),
                MaterialLibAPI.getFluidStack(Materials.Kevlar, FluidShapes.fluidMolten, (int) (1 * STACKS)))
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
                    MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, (int) (64)))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidGas, (int) (16_000)),
                    MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (16_000)))
                .fluidOutputs(
                    MaterialLibAPI.getFluidStack(Materials.PolyurethaneResin, FluidShapes.fluidLiquid, (int) (32_000)),
                    MaterialLibAPI
                        .getFluidStack(Materials.LiquidCrystalKevlar, FluidShapes.fluidLiquid, (int) (32 * INGOTS)),
                    MaterialLibAPI.getFluidStack(Materials.Kevlar, FluidShapes.fluidMolten, (int) (1 * STACKS)))
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
                    MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidMolten, (int) (4 * STACKS)),
                    MaterialLibAPI.getFluidStack(Materials.Palladium, FluidShapes.fluidMolten, (int) (4 * STACKS)),
                    MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, (int) (4 * STACKS)),
                    MaterialLibAPI.getFluidStack(Materials.Platinum, FluidShapes.fluidMolten, (int) (4 * STACKS)))
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
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.dust, (int) (4)))
            .itemOutputs(biocells)
            .fluidOutputs(
                MaterialUtils.legacyGtppFluid(Materials.MutatedLivingSolder, 2 * STACKS),
                MaterialLibAPI
                    .getFluidStack(Materials.BiohMediumSterilized, FluidShapes.fluidLiquid, (int) (1000 * 256)),
                MaterialLibAPI.getFluidStack(Materials.BioMediumRaw, FluidShapes.fluidLiquid, (int) (1000 * 512)))
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
                    MaterialLibAPI.getStack(Materials.Mytryl, Shapes.dust, (int) (16)))
                .itemOutputs(seaweed, getModItem(NewHorizonsCoreMod.ID, "TCetiESeaweedExtract", 16))
                .fluidInputs(FluidRegistry.getFluidStack("unknowwater", 25_000))
                .fluidOutputs(
                    FluidRegistry.getFluidStack("seaweedbroth", 50_000),
                    MaterialLibAPI.getFluidStack(Materials.Iodine, FluidShapes.fluidLiquid, (int) (64_000)))
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
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, FluidShapes.fluidPlasma, (int) (100)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Americium, FluidShapes.fluidPlasma, (int) (100)),
                MaterialLibAPI.getFluidStack(Materials.Boron, FluidShapes.fluidPlasma, (int) (100)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        // MK5 versions
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Xenon), 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Yttrium, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .fluidOutputs(new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 4 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Force), 4 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Rubidium, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
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
                MaterialLibAPI.getStack(Materials.RhodiumPlatedPalladium, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.dust, (int) (64)),
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
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Polytetrafluoroethylene, Shapes.dust, (int) (64)),
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
                MaterialLibAPI.getStack(Materials.Silicone, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.StyreneButadieneRubber, Shapes.dust, (int) (64)),
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
                MaterialLibAPI.getStack(Materials.Indalloy140, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.EthylCyanoacrylateSuperGlue, CellShapes.cellMolten, 64),
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
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Indium, Shapes.dust, (int) (64)),
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
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, 64),
                MaterialLibAPI.getStack(Materials.Plutonium241, Shapes.dust, 64),
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
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, (int) (64)),
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
                MaterialLibAPI.getStack(Materials.Naquadah, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Adamantium, Shapes.dust, (int) (64)),
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
                    MaterialLibAPI.getStack(Materials.Netherite, Shapes.dust, (int) (64)),
                    MaterialLibAPI.getStack(Materials.InfusedGold, Shapes.dust, (int) (64)),
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
                MaterialLibAPI.getStack(Materials.prismaticnaquadah, Shapes.dust, (int) (64)),
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
                MaterialLibAPI.getStack(Materials.Naquadria, Shapes.dust, (int) (64)),
                MaterialLibAPI.getStack(Materials.Trinium, Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.AdvancedNaquadahCatalyst.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, (int) (1 * STACKS)))
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, (int) (1 * STACKS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialLibAPI.getStack(Materials.Kevlar, Shapes.dust, (int) (64)),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1))
            .circuit(10)
            .itemOutputs(GregtechItemList.UltimatePlasticCatalyst.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (10 * STACKS)))
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (10 * STACKS)))
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.DarkIron, FluidShapes.fluidMolten, (int) (10 * STACKS)))
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
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (10 * STACKS)))
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
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialFacades.SuperconductorUHV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 2) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Thulium, FluidShapes.fluidMolten, (int) (144 * 10)),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTCC, FluidShapes.fluidLiquid, (int) (5_000)),
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
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialFacades.SuperconductorUEV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 4),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Thulium, FluidShapes.fluidMolten, (int) (144 * 12)),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTPC, FluidShapes.fluidLiquid, (int) (5_000)),
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
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 16), ItemList.Emitter_UIV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialFacades.SuperconductorUIV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 8),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Thulium, FluidShapes.fluidMolten, (int) (144 * 15)),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTRC, FluidShapes.fluidLiquid, (int) (5_000)),
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
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 32), ItemList.Emitter_UMV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, MaterialFacades.SuperconductorUMV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Thulium, FluidShapes.fluidMolten, (int) (20 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, (int) (5_000)),
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
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, PipeShapes.frameGt, 1),
                GTOreDictUnificator.get("plateDensePreciousMetalsAlloy", 4),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.plateSuperdense, (int) (2)),
                ItemList.Field_Generator_UV.get(1),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.screw, 16) },
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
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, PipeShapes.frameGt, 2),
                GTOreDictUnificator.get("plateDenseEnrichedNaquadahAlloy", 4),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateSuperdense, (int) (2)),
                ItemList.Field_Generator_UEV.get(1),
                MaterialLibAPI.getStack(Materials.RadoxPoly, Shapes.screw, (int) (16)),
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
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, PipeShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.plateDense, 4),
                MaterialLibAPI.getStack(Materials.protohalkonite, Shapes.plateSuperdense, (int) (2)),
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
            new ItemStack[] { MaterialLibAPI.getStack(Materials.Quantum, PipeShapes.frameGt, 8),
                GTOreDictUnificator.get("plateDenseShirabon", 4),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plateSuperdense, (int) (2)),
                ItemList.Field_Generator_UMV.get(1),
                MaterialLibAPI.getStack(Materials.Dilithium, Shapes.screw, (int) (16)),
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
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.Netherite, Shapes.stickLong, (int) (4)),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.plate, 6))
            .itemOutputs(GregtechItemList.ForceFieldGlass.get(1))
            .fluidInputs(MaterialUtils.legacyGtppFluid(Materials.Quantum, 6 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

    }
}
