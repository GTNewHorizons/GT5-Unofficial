package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static goodgenerator.loader.Loaders.advancedRadiationProtectionPlate;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
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

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GTBees;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
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
            .itemInputs(WerkstoffLoader.PTMetallicPowder.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Platinum.getDust(64),
                Materials.Palladium.getDust(64),
                Materials.Iridium.getDust(64),
                Materials.Osmium.getDust(64),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Partial platline (from Pd, Os, Ir, Rh and leach)
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Palladium.getDust(64),
                Materials.Platinum.getDust(64),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.IrLeachResidue.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Iridium.getDust(64),
                Materials.Platinum.getDust(64),
                Materials.Osmiridium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.IrOsLeachResidue.get(OrePrefixes.dust, 32))
            .itemOutputs(Materials.Osmium.getDust(64), Materials.Iridium.getDust(64), Materials.Osmiridium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.CrudeRhMetall.get(OrePrefixes.dust, 32))
            .itemOutputs(
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                Materials.Palladium.getDust(64),
                Materials.Platinum.getDust(64),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.LeachResidue.get(OrePrefixes.dust, 32))
            .itemOutputs(
                Materials.Iridium.getDust(64),
                Materials.Osmium.getDust(64),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Plastics
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64))
            .fluidInputs(
                Materials.Oxygen.getGas(16_000),
                Materials.Hydrogen.getGas(16_000),
                Materials.Chlorine.getGas(16_000),
                Materials.Fluorine.getGas(16_000))
            .fluidOutputs(
                Materials.Polyethylene.getMolten(4 * STACKS),
                Materials.PolyvinylChloride.getMolten(2 * STACKS),
                Materials.Polystyrene.getMolten(1 * STACKS),
                Materials.Polytetrafluoroethylene.getMolten(2 * STACKS),
                Materials.Epoxid.getMolten(1 * STACKS),
                Materials.Polybenzimidazole.getMolten(1 * STACKS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.PlasticPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Rubbers/Cable Materials
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64))
            .fluidInputs(
                Materials.Oxygen.getGas(16_000),
                Materials.Hydrogen.getGas(16_000),
                Materials.Chlorine.getGas(16_000))
            .fluidOutputs(
                Materials.RubberSilicone.getMolten(1 * STACKS),
                Materials.StyreneButadieneRubber.getMolten(1 * STACKS),
                Materials.PolyphenyleneSulfide.getMolten(2 * STACKS),
                Materials.Rubber.getMolten(4 * STACKS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_CATALYST, GregtechItemList.RubberPolymerCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .noBuffer()
            .addTo(quantumForceTransformerRecipes);
        // Glues and Solders
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(32), Materials.Bismuth.getDust(32))
            .itemOutputs(ItemList.StableAdhesive.get(1))
            .fluidInputs(Materials.Oxygen.getGas(10_000), Materials.Hydrogen.getGas(10_000))
            .fluidOutputs(
                MaterialMisc.ETHYL_CYANOACRYLATE.getFluidStack(32_000),
                Materials.GlueAdvanced.getFluid(16_000),
                MaterialsAlloy.INDALLOY_140.getFluidStack(1 * STACKS),
                Materials.SolderingAlloy.getMolten(2 * STACKS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.AdhesionPromoterCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Titanium, Tungsten, Indium
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Lead.getDust(16), Materials.Bauxite.getDust(32), Materials.Tungstate.getDust(16))
            .itemOutputs(
                Materials.Titanium.getDust(64),
                Materials.TungstenSteel.getDust(64),
                Materials.TungstenCarbide.getDust(64),
                Materials.Indium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Rutile.getDust(32), Materials.Scheelite.getDust(16), Materials.Ilmenite.getDust(16))
            .itemOutputs(
                Materials.Titanium.getDust(64),
                Materials.TungstenSteel.getDust(64),
                Materials.Tantalum.getDust(64),
                Materials.Indium.getDust(64),
                Materials.Niobium.getDust(64),
                MaterialsElements.getInstance().HAFNIUM.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.TitaTungstenIndiumCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Thorium, Uranium, Plutonium
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Thorium.getDust(32), Materials.Uranium.getDust(32))
            .itemOutputs(
                MaterialsElements.getInstance().THORIUM232.getDust(64),
                MaterialsElements.getInstance().URANIUM233.getDust(64),
                Materials.Uranium235.getDust(64),
                MaterialsElements.getInstance().PLUTONIUM238.getDust(64),
                Materials.Plutonium.getDust(64),
                Materials.Plutonium241.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_CATALYST, GregtechItemList.RadioactivityCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Monaline
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Monazite.getDust(32))
            .itemOutputs(
                Materials.Cerium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Samarium.getDust(64),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 11002),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 3),
                ItemList.SuperconductorComposite.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Bastnasite.getDust(32))
            .itemOutputs(
                Materials.Holmium.getDust(64),
                Materials.Cerium.getDust(64),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Lanthanum.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline from Cerium-rich mixture
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 16))
            .itemOutputs(
                Materials.Holmium.getDust(64),
                Materials.Cerium.getDust(64),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Lanthanum.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.RareEarthGroupCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        if (EtFuturumRequiem.isModLoaded()) {
            // Netherite
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility
                        .copyAmountUnsafe(64 * 16, GTOreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1)),
                    getModItem(EtFuturumRequiem.ID, "ancient_debris", 1))
                .fluidInputs(
                    Materials.NetherAir.getFluid(64_000),
                    Materials.HellishMetal.getMolten(8 * INGOTS),
                    Materials.Lava.getFluid(256_000))
                .itemOutputs(
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 16),
                    ItemList.Intensely_Bonded_Netherite_Nanoparticles.get(64))
                .fluidOutputs(Materials.NefariousOil.getFluid(64_000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UHV)
                .metadata(QFT_CATALYST, GregtechItemList.HellishForceCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 2)
                .addTo(quantumForceTransformerRecipes);
        }
        // Prismarine
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.shard, Materials.Prismarine, 12),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.NaquadahEnriched, 32))
            .fluidInputs(
                Materials.Hydrogen.getGas(2_000),
                Materials.Oxygen.getGas(2_000),
                Materials.Nitrogen.getGas(24_000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.PrismaticNaquadah, 16))
            .fluidOutputs(Materials.PrismaticAcid.getFluid(32_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_CATALYST, GregtechItemList.CrystalColorizationCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Stem Cells
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Calcium.getDust(32),
                Materials.MeatRaw.getDust(32),
                getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 32, 2))
            .itemOutputs(stemcells)
            .fluidOutputs(
                Materials.GrowthMediumRaw.getFluid(1_024_000),
                Materials.GrowthMediumSterilized.getFluid(512_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_CATALYST, GregtechItemList.RawIntelligenceCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Unknown Particles
        GTValues.RA.stdBuilder()
            .itemOutputs(
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.GRAVITON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.ELECTRON))
            .fluidInputs(Materials.Hydrogen.getGas(10_000), Materials.Deuterium.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getPlasma(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_CATALYST, GregtechItemList.ParticleAccelerationCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Lategame Plastics (Missing Radox Polymer and Heavy Radox)
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64), Materials.Osmium.getDust(24))
            .fluidInputs(Materials.Hydrogen.getGas(16_000), Materials.Nitrogen.getGas(16_000))
            .fluidOutputs(
                Materials.Xenoxene.getFluid(16_000),
                Materials.RadoxPolymer.getMolten(1 * STACKS),
                Materials.RadoxHeavy.getFluid(16_000),
                Materials.Kevlar.getMolten(1 * STACKS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.UltimatePlasticCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        if (Mods.Forestry.isModLoaded()) {
            // Lategame Kevlar using Kevlar bee comb
            GTValues.RA.stdBuilder()
                .itemInputs(GTBees.combs.getStackForType(CombType.KEVLAR, 24), Materials.Carbon.getDust(64))
                .fluidInputs(Materials.Nitrogen.getGas(16_000), Materials.Hydrogen.getGas(16_000))
                .fluidOutputs(
                    Materials.PolyurethaneResin.getFluid(32_000),
                    Materials.LiquidCrystalKevlar.getFluid(32 * INGOTS),
                    Materials.Kevlar.getMolten(1 * STACKS))
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
                    Materials.Osmium.getMolten(4 * STACKS),
                    Materials.Palladium.getMolten(4 * STACKS),
                    Materials.Iridium.getMolten(4 * STACKS),
                    Materials.Platinum.getMolten(4 * STACKS))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(QFT_CATALYST, GregtechItemList.PlatinumGroupCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 1)
                .addTo(quantumForceTransformerRecipes);
        }
        // Bio Cells and Mutated Solder
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Chip_Stemcell.get(16), Materials.InfinityCatalyst.getDust(4))
            .itemOutputs(biocells)
            .fluidOutputs(
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * STACKS),
                Materials.BioMediumSterilized.getFluid(1000 * 256),
                Materials.BioMediumRaw.getFluid(1000 * 512))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.BiologicalIntelligenceCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        // Rare Particles
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Laser_Lens_Special.get(1))
            .itemOutputs(
                Particle.getBaseParticle(Particle.Z_BOSON),
                Particle.getBaseParticle(Particle.W_BOSON),
                Particle.getBaseParticle(Particle.LAMBDA),
                Particle.getBaseParticle(Particle.OMEGA),
                Particle.getBaseParticle(Particle.HIGGS_BOSON),
                Particle.getBaseParticle(Particle.ETA_MESON))
            .fluidInputs(
                Materials.Hydrogen.getPlasma(30_000),
                Materials.Helium.getPlasma(30_000),
                Materials.Americium.getPlasma(30_000),
                new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 30_000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_CATALYST, GregtechItemList.SynchrotronCapableCatalyst.get(0))
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);

        if (GalaxySpace.isModLoaded()) {
            // Seaweed
            ItemStack seaweed = GTUtility
                .copyAmountUnsafe(64 * 32, getModItem(GalaxySpace.ID, "tcetiedandelions", 1, 4));
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("cropSeaweed", 64), Materials.Mytryl.getDust(16))
                .itemOutputs(seaweed, getModItem(NewHorizonsCoreMod.ID, "TCetiESeaweedExtract", 16))
                .fluidInputs(FluidRegistry.getFluidStack("unknowwater", 25_000))
                .fluidOutputs(
                    FluidRegistry.getFluidStack("seaweedbroth", 50_000),
                    WerkstoffMaterialPool.Iodine.getFluidOrGas(64_000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .metadata(QFT_CATALYST, GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(0))
                .metadata(QFT_FOCUS_TIER, 4)
                .addTo(quantumForceTransformerRecipes);

        }
    }

    private static void fusionReactorRecipes() {
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Radon.getPlasma(100), Materials.Nitrogen.getPlasma(100))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Americium.getPlasma(100), Materials.Boron.getPlasma(100))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000L)
            .addTo(fusionRecipes);

        // MK5 versions
        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.getInstance().XENON.getPlasma(), 4 * INGOTS),
                Materials.Yttrium.getMolten(4 * INGOTS))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 4 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                new FluidStack(MaterialsElements.STANDALONE.FORCE.getPlasma(), 4 * INGOTS),
                Materials.Rubidium.getMolten(4 * INGOTS))
            .fluidOutputs(new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 4 * INGOTS))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 6_000_000_000L)
            .addTo(fusionRecipes);
    }

    private static void catalystRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 88),
                Materials.Osmiridium.getDust(64),
                Materials.Carbon.getNanite(64))
            .circuit(10)
            .itemOutputs(GregtechItemList.PlatinumGroupCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.Polybenzimidazole.getDust(64),
                Materials.Polytetrafluoroethylene.getDust(64),
                Materials.Carbon.getNanite(64))
            .circuit(10)
            .itemOutputs(GregtechItemList.PlasticPolymerCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.RubberSilicone.getDust(64),
                Materials.StyreneButadieneRubber.getDust(64),
                Materials.Carbon.getNanite(64))
            .circuit(10)
            .itemOutputs(GregtechItemList.RubberPolymerCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialsAlloy.INDALLOY_140.getDust(64),
                MaterialMisc.ETHYL_CYANOACRYLATE.getCell(64),
                Materials.Carbon.getNanite(64))
            .circuit(10)
            .itemOutputs(GregtechItemList.AdhesionPromoterCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.TungstenSteel.getDust(64),
                Materials.Indium.getDust(64),
                Materials.Carbon.getNanite(64))
            .circuit(10)
            .itemOutputs(GregtechItemList.TitaTungstenIndiumCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                MaterialsElements.getInstance().URANIUM235.getDust(64),
                MaterialsElements.getInstance().PLUTONIUM241.getDust(64),
                Materials.Carbon.getNanite(64))
            .circuit(10)
            .itemOutputs(GregtechItemList.RadioactivityCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(2 * INGOTS + 1 * HALF_INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Silver.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.RareEarthGroupCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.Naquadah.getDust(64),
                Materials.Adamantium.getDust(64),
                Materials.Silver.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.SimpleNaquadahCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        if (EtFuturumRequiem.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.EmptyCatalystCarrier.get(1),
                    Materials.Netherite.getDust(64),
                    Materials.InfusedGold.getDust(64),
                    getModItem(EtFuturumRequiem.ID, "netherite_scrap", 1),
                    Materials.Silver.getNanite(1))
                .circuit(10)
                .itemOutputs(GregtechItemList.HellishForceCatalyst.get(1))
                .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(1 * STACKS))
                .duration(60 * SECONDS)
                .eut(TierEU.RECIPE_UEV)
                .addTo(assemblerRecipes);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.PrismaticNaquadah.getDust(64),
                GTOreDictUnificator.get(OrePrefixes.shard, Materials.Prismarine, 64),
                Materials.Silver.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.CrystalColorizationCatalyst.get(1))
            .fluidInputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.Naquadria.getDust(64),
                Materials.Trinium.getDust(64),
                Materials.Gold.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.AdvancedNaquadahCatalyst.get(1))
            .fluidInputs(Materials.SpaceTime.getMolten(1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                ItemList.Circuit_Chip_Stemcell.get(64),
                Materials.Gold.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.RawIntelligenceCatalyst.get(1))
            .fluidInputs(Materials.SpaceTime.getMolten(1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                GregtechItemList.Laser_Lens_Special.get(64),
                GTModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 10))
            .circuit(10)
            .itemOutputs(GregtechItemList.ParticleAccelerationCatalyst.get(1))
            .fluidInputs(Materials.SpaceTime.getMolten(1 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Materials.Kevlar.getDust(64),
                Materials.TranscendentMetal.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.UltimatePlasticCatalyst.get(1))
            .fluidInputs(GGMaterial.shirabon.getMolten(10 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                ItemList.Circuit_Chip_Biocell.get(64),
                Materials.TranscendentMetal.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.BiologicalIntelligenceCatalyst.get(1))
            .fluidInputs(GGMaterial.shirabon.getMolten(10 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                Particle.getBaseParticle(Particle.HIGGS_BOSON),
                Particle.getIon("Helium", 0),
                Particle.getIon("Hydrogen", 0),
                Materials.Eternity.getNanite(16))
            .circuit(10)
            .itemOutputs(GregtechItemList.SynchrotronCapableCatalyst.get(1))
            .fluidInputs(GGMaterial.shirabon.getMolten(10 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                GTOreDictUnificator.get("blockShirabon", 16),
                Materials.Universium.getNanite(1),
                ItemList.Timepiece.get(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.TemporalHarmonyCatalyst.get(1))
            .fluidInputs(Materials.DarkIron.getMolten(10 * STACKS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(1),
                getModItem(NewHorizonsCoreMod.ID, "TCetiESeaweedExtract", 64),
                GTOreDictUnificator.get("dustIodine", 64),
                Materials.TranscendentMetal.getNanite(1))
            .circuit(10)
            .itemOutputs(GregtechItemList.AlgagenicGrowthPromoterCatalyst.get(1))
            .fluidInputs(GGMaterial.shirabon.getMolten(10 * STACKS))
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
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(1), Materials.Carbon.getNanite(4),
                ItemList.Emitter_UV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 2) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 10), Materials.ExcitedDTCC.getFluid(5_000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 500),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 500) },
            GregtechItemList.NeutronPulseManipulator.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronPulseManipulator.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(2), Materials.Carbon.getNanite(8),
                ItemList.Emitter_UEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 4),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 12), Materials.ExcitedDTPC.getFluid(5_000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 2_500),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 2_500) },
            GregtechItemList.CosmicFabricManipulator.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricManipulator.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(4), Materials.Carbon.getNanite(16),
                ItemList.Emitter_UIV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 8),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 15), Materials.ExcitedDTRC.getFluid(5_000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 10_000),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 10_000) },
            GregtechItemList.InfinityInfusedManipulator.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedManipulator.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(8), Materials.Carbon.getNanite(32),
                ItemList.Emitter_UMV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(20 * INGOTS), Materials.ExcitedDTEC.getFluid(5_000),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 20_000),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 20_000) },
            GregtechItemList.SpaceTimeContinuumRipper.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UXV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_AdvancedRadiationProof.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(1),
                GTOreDictUnificator.get("plateDensePreciousMetalsAlloy", 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Netherite, 2),
                ItemList.Field_Generator_UV.get(1), MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getScrew(16) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS), },
            GregtechItemList.NeutronShieldingCore.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronShieldingCore.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(2),
                GTOreDictUnificator.get("plateDenseEnrichedNaquadahAlloy", 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 2),
                ItemList.Field_Generator_UEV.get(1),
                // Radox polymer screw.
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.get("RadoxPoly"), 16),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS), },
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_UHV,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(4),
                MaterialsElements.STANDALONE.HYPOGEN.getPlateDense(4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.ProtoHalkonite, 2),
                ItemList.Field_Generator_UIV.get(1), GTOreDictUnificator.get("screwMetastableOganesson", 16),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(40 * INGOTS), },
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            8192 * 30 * 20,
            8192,
            (int) TierEU.RECIPE_UEV,
            32,
            new ItemStack[] { MaterialsAlloy.QUANTUM.getFrameBox(8), GTOreDictUnificator.get("plateDenseShirabon", 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SpaceTime, 2),
                ItemList.Field_Generator_UMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Dilithium, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS + 16 * INGOTS), },
            GregtechItemList.SpaceTimeBendingCore.get(1),
            120 * 20,
            (int) TierEU.RECIPE_UXV);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(BlockQuantumGlass.INSTANCE, 1),
                ItemList.Field_Generator_ZPM.get(1),
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getLongRod(4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Netherite, 4),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlate(6))
            .itemOutputs(GregtechItemList.ForceFieldGlass.get(1))
            .fluidInputs(MaterialsAlloy.QUANTUM.getFluidStack(6 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

    }
}
