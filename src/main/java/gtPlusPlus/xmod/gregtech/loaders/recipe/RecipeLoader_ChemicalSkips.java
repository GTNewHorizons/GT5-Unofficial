package gtPlusPlus.xmod.gregtech.loaders.recipe;

import static goodgenerator.loader.Loaders.advancedRadiationProtectionPlate;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GT_RecipeConstants.QFT_FOCUS_TIER;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.items.CombType;
import gregtech.loaders.misc.GT_Bees;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class RecipeLoader_ChemicalSkips {

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
        ItemStack stemcells = GT_Utility.copyAmountUnsafe(64 * 32, ItemList.Circuit_Chip_Stemcell.get(1));
        ItemStack biocells = GT_Utility.copyAmountUnsafe(64 * 32, ItemList.Circuit_Chip_Biocell.get(1));
        // Platline
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.PTMetallicPowder.get(OrePrefixes.dust, 32),
                ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 0))
            .itemOutputs(
                Materials.Platinum.getDust(64),
                Materials.Palladium.getDust(64),
                Materials.Iridium.getDust(64),
                Materials.Osmium.getDust(64),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Partial platline (from Pd, Os, Ir, Rh and leach)
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.PDMetallicPowder.get(OrePrefixes.dust, 32),
                ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 0))
            .itemOutputs(
                Materials.Palladium.getDust(64),
                Materials.Platinum.getDust(64),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.IrLeachResidue.get(OrePrefixes.dust, 32),
                ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 0))
            .itemOutputs(
                Materials.Iridium.getDust(64),
                Materials.Platinum.getDust(64),
                Materials.Osmiridium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.IrOsLeachResidue.get(OrePrefixes.dust, 32),
                ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 0))
            .itemOutputs(Materials.Osmium.getDust(64), Materials.Iridium.getDust(64), Materials.Osmiridium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.CrudeRhMetall.get(OrePrefixes.dust, 32),
                ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 0))
            .itemOutputs(
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                Materials.Palladium.getDust(64),
                Materials.Platinum.getDust(64),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.LeachResidue.get(OrePrefixes.dust, 32),
                ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 0))
            .itemOutputs(
                Materials.Iridium.getDust(64),
                Materials.Osmium.getDust(64),
                WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 64),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Plastics
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64), ItemUtils.getSimpleStack(GenericChem.mPlasticPolymerCatalyst, 0))
            .fluidInputs(
                Materials.Oxygen.getGas(1000 * 16),
                Materials.Hydrogen.getGas(1000 * 16),
                Materials.Chlorine.getGas(1000 * 16),
                Materials.Fluorine.getGas(1000 * 16))
            .fluidOutputs(
                Materials.Plastic.getMolten(144 * 256),
                Materials.PolyvinylChloride.getMolten(144 * 128),
                Materials.Polystyrene.getMolten(144 * 64),
                Materials.Polytetrafluoroethylene.getMolten(144 * 128),
                Materials.Epoxid.getMolten(144 * 64),
                Materials.Polybenzimidazole.getMolten(144 * 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Early Rubbers/Cable Materials
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(64), ItemUtils.getSimpleStack(GenericChem.mRubberPolymerCatalyst, 0))
            .fluidInputs(
                Materials.Oxygen.getGas(1000 * 16),
                Materials.Hydrogen.getGas(1000 * 16),
                Materials.Chlorine.getGas(1000 * 16))
            .fluidOutputs(
                Materials.Silicone.getMolten(144 * 64),
                Materials.StyreneButadieneRubber.getMolten(144 * 64),
                Materials.PolyphenyleneSulfide.getMolten(144 * 128),
                Materials.Rubber.getMolten(144 * 256))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Glues and Solders
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Carbon.getDust(32),
                Materials.Bismuth.getDust(32),
                ItemUtils.getSimpleStack(GenericChem.mAdhesionPromoterCatalyst, 0))
            .itemOutputs(ItemList.StableAdhesive.get(1))
            .fluidInputs(Materials.Oxygen.getGas(10000), Materials.Hydrogen.getGas(10000))
            .fluidOutputs(
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getFluidStack(1000 * 32),
                Materials.AdvancedGlue.getFluid(1000 * 16),
                ALLOY.INDALLOY_140.getFluidStack(144 * 64),
                Materials.SolderingAlloy.getMolten(144 * 128))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Titanium, Tungsten, Indium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Lead.getDust(16),
                Materials.Bauxite.getDust(32),
                Materials.Tungstate.getDust(16),
                ItemUtils.getSimpleStack(GenericChem.mTitaTungstenIndiumCatalyst, 0))
            .itemOutputs(
                Materials.Titanium.getDust(64),
                Materials.TungstenSteel.getDust(64),
                Materials.TungstenCarbide.getDust(64),
                Materials.Indium.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Thorium, Uranium, Plutonium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Thorium.getDust(32),
                Materials.Uranium.getDust(32),
                ItemUtils.getSimpleStack(GenericChem.mRadioactivityCatalyst, 0))
            .itemOutputs(
                ELEMENT.getInstance().THORIUM232.getDust(64),
                ELEMENT.getInstance().URANIUM233.getDust(64),
                Materials.Uranium235.getDust(64),
                ELEMENT.getInstance().PLUTONIUM238.getDust(64),
                Materials.Plutonium.getDust(64),
                Materials.Plutonium241.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .metadata(QFT_FOCUS_TIER, 1)
            .addTo(quantumForceTransformerRecipes);
        // Monaline
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Monazite.getDust(32),
                ItemUtils.getSimpleStack(GenericChem.mRareEarthGroupCatalyst, 0))
            .itemOutputs(
                Materials.Cerium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Samarium.getDust(64),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 11002),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 11007),
                ItemList.SuperconductorComposite.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Bastnasite.getDust(32),
                ItemUtils.getSimpleStack(GenericChem.mRareEarthGroupCatalyst, 0))
            .itemOutputs(
                Materials.Holmium.getDust(64),
                Materials.Cerium.getDust(64),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Lanthanum.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Bastline from Cerium-rich mixture
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 16),
                ItemUtils.getSimpleStack(GenericChem.mRareEarthGroupCatalyst, 0))
            .itemOutputs(
                Materials.Holmium.getDust(64),
                Materials.Cerium.getDust(64),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Lanthanum.getDust(64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Early Waterline skip (first 4 tiers)
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(GenericChem.mLimpidWaterCatalyst, 0))
            .fluidInputs(Materials.Water.getFluid(40000L))
            .fluidOutputs(
                Materials.Grade1PurifiedWater.getFluid(20000L),
                Materials.Grade2PurifiedWater.getFluid(10000L),
                Materials.Grade3PurifiedWater.getFluid(5000L),
                Materials.Grade4PurifiedWater.getFluid(1000L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(QFT_FOCUS_TIER, 2)
            .addTo(quantumForceTransformerRecipes);
        // Stem Cells
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Calcium.getDust(32),
                Materials.MeatRaw.getDust(32),
                getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 32, 2),
                ItemUtils.getSimpleStack(GenericChem.mRawIntelligenceCatalyst, 0))
            .itemOutputs(stemcells)
            .fluidOutputs(
                Materials.GrowthMediumRaw.getFluid(1000 * 1024),
                Materials.GrowthMediumSterilized.getFluid(1000 * 512))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Unknown Particles
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(GenericChem.mParticleAccelerationCatalyst, 0))
            .itemOutputs(
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.GRAVITON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.ELECTRON))
            .fluidInputs(Materials.Hydrogen.getGas(10000L), Materials.Deuterium.getGas(1000L))
            .fluidOutputs(FluidUtils.getFluidStack("plasma.hydrogen", 1000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(QFT_FOCUS_TIER, 3)
            .addTo(quantumForceTransformerRecipes);
        // Lategame Plastics (Missing Radox Polymer and Heavy Radox)
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Carbon.getDust(64),
                Materials.Osmium.getDust(24),
                ItemUtils.getSimpleStack(GenericChem.mUltimatePlasticCatalyst, 0))
            .fluidInputs(Materials.Hydrogen.getGas(1000 * 16), Materials.Nitrogen.getGas(1000 * 16))
            .fluidOutputs(
                FluidUtils.getFluidStack("xenoxene", 1000 * 16),
                FluidUtils.getFluidStack("molten.radoxpoly", 144 * 64),
                FluidUtils.getFluidStack("heavyradox", 1000 * 16),
                MaterialsKevlar.Kevlar.getMolten(144 * 64))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        if (Mods.Forestry.isModLoaded()) {
            // Lategame Kevlar using Kevlar bee comb
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Bees.combs.getStackForType(CombType.KEVLAR, 24),
                    Materials.Carbon.getDust(64),
                    ItemUtils.getSimpleStack(GenericChem.mUltimatePlasticCatalyst, 0))
                .fluidInputs(Materials.Nitrogen.getGas(1000 * 16), Materials.Hydrogen.getGas(1000 * 16))
                .fluidOutputs(
                    MaterialsKevlar.PolyurethaneResin.getFluid(1000 * 32),
                    MaterialsKevlar.LiquidCrystalKevlar.getFluid(144 * 32),
                    MaterialsKevlar.Kevlar.getMolten(144 * 64))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .metadata(QFT_FOCUS_TIER, 4)
                .addTo(quantumForceTransformerRecipes);
            // Platline skip using Platline Combs (Palladium, Osmium, Iridium, Platinum)
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Bees.combs.getStackForType(CombType.PLATINUM, 32),
                    GT_Bees.combs.getStackForType(CombType.PALLADIUM, 32),
                    GT_Bees.combs.getStackForType(CombType.OSMIUM, 32),
                    GT_Bees.combs.getStackForType(CombType.IRIDIUM, 32),
                    ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 0))
                .fluidOutputs(
                    Materials.Osmium.getMolten(144 * 256),
                    Materials.Palladium.getMolten(144 * 256),
                    Materials.Iridium.getMolten(144 * 256),
                    Materials.Platinum.getMolten(144 * 256))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UV)
                .metadata(QFT_FOCUS_TIER, 1)
                .addTo(quantumForceTransformerRecipes);
        }
        // Bio Cells and Mutated Solder
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Circuit_Chip_Stemcell.get(16),
                Materials.InfinityCatalyst.getDust(4),
                ItemUtils.getSimpleStack(GenericChem.mBiologicalIntelligenceCatalyst, 0))
            .itemOutputs(biocells)
            .fluidOutputs(
                MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 128),
                Materials.BioMediumSterilized.getFluid(1000 * 256),
                Materials.BioMediumRaw.getFluid(1000 * 512))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        // Advanced Waterline skip (last 4 tiers)
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(GenericChem.mFlawlessWaterCatalyst, 0))
            .fluidInputs(Materials.Water.getFluid(40000L))
            .fluidOutputs(
                Materials.Grade5PurifiedWater.getFluid(20000L),
                Materials.Grade6PurifiedWater.getFluid(10000L),
                Materials.Grade7PurifiedWater.getFluid(5000L),
                Materials.Grade8PurifiedWater.getFluid(1000L))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);
        // Rare Particles
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(GenericChem.mSynchrotronCapableCatalyst, 0),
                GregtechItemList.Laser_Lens_Special.get(1))
            .itemOutputs(
                Particle.getBaseParticle(Particle.Z_BOSON),
                Particle.getBaseParticle(Particle.W_BOSON),
                Particle.getBaseParticle(Particle.LAMBDA),
                Particle.getBaseParticle(Particle.OMEGA),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .fluidInputs(
                FluidUtils.getFluidStack("plasma.hydrogen", 30000),
                Materials.Helium.getPlasma(30000L),
                Materials.Americium.getPlasma(30000L),
                new FluidStack(ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 30000))
            .duration(3 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .metadata(QFT_FOCUS_TIER, 4)
            .addTo(quantumForceTransformerRecipes);

        if (GalaxySpace.isModLoaded()) {
            // Seaweed
            ItemStack seaweed = GT_Utility
                .copyAmountUnsafe(64 * 32, getModItem(GalaxySpace.ID, "tcetiedandelions", 1, 4));
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_OreDictUnificator.get("cropSeaweed", 64),
                    Materials.Mytryl.getDust(16),
                    ItemUtils.getSimpleStack(GenericChem.mAlgagenicGrowthPromoterCatalyst, 0))
                .itemOutputs(seaweed, getModItem(NewHorizonsCoreMod.ID, "item.TCetiESeaweedExtract", 16))
                .fluidInputs(FluidUtils.getFluidStack("unknowwater", 25_000))
                .fluidOutputs(
                    FluidUtils.getFluidStack("seaweedbroth", 50_000),
                    FluidUtils.getFluidStack("iodine", 64_000))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_UIV)
                .metadata(QFT_FOCUS_TIER, 4)
                .addTo(quantumForceTransformerRecipes);

        }
    }

    private static void fusionReactorRecipes() {
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Radon.getPlasma(100), Materials.Nitrogen.getPlasma(100))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Americium.getPlasma(100), Materials.Boron.getPlasma(100))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 100))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000)
            .addTo(fusionRecipes);

        // MK5 versions
        GT_Values.RA.stdBuilder()
            .fluidInputs(new FluidStack(ELEMENT.getInstance().XENON.getPlasma(), 288), Materials.Yttrium.getMolten(288))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 288))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000)
            .addTo(fusionRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(new FluidStack(ELEMENT.STANDALONE.FORCE.getPlasma(), 288), Materials.Rubidium.getMolten(288))
            .fluidOutputs(new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 288))
            .duration(1 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 1_000_000_000)
            .addTo(fusionRecipes);
    }

    private static void catalystRecipes() {
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.dust), 64, 88),
                Materials.Osmiridium.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mPlatinumGroupCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Polybenzimidazole.getDust(64),
                Materials.Polytetrafluoroethylene.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mPlasticPolymerCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Silicone.getDust(64),
                Materials.StyreneButadieneRubber.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mRubberPolymerCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                ALLOY.INDALLOY_140.getDust(64),
                MISC_MATERIALS.ETHYL_CYANOACRYLATE.getCell(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mAdhesionPromoterCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.TungstenSteel.getDust(64),
                Materials.Indium.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mTitaTungstenIndiumCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                ELEMENT.getInstance().URANIUM235.getDust(64),
                ELEMENT.getInstance().PLUTONIUM241.getDust(64),
                Materials.Carbon.getNanite(64))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mRadioactivityCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(360))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Samarium.getDust(64),
                Materials.Gadolinium.getDust(64),
                Materials.Silver.getNanite(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mRareEarthGroupCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(9216))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Naquadah.getDust(64),
                Materials.Adamantium.getDust(64),
                Materials.Silver.getNanite(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mSimpleNaquadahCatalyst, 1))
            .fluidInputs(ELEMENT.STANDALONE.HYPOGEN.getFluidStack(9216))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Materials.Naquadria.getDust(64),
                Materials.Trinium.getDust(64),
                Materials.Gold.getNanite(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mAdvancedNaquadahCatalyst, 1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(9216L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                ItemList.Circuit_Chip_Stemcell.get(64),
                Materials.Gold.getNanite(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mRawIntelligenceCatalyst, 1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(9216L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                GregtechItemList.Laser_Lens_Special.get(64),
                GT_ModHandler.getModItem(EternalSingularity.ID, "eternal_singularity", 10))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mParticleAccelerationCatalyst, 1))
            .fluidInputs(MaterialsUEVplus.SpaceTime.getMolten(9216L))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                MaterialsKevlar.Kevlar.getDust(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mUltimatePlasticCatalyst, 1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                ItemList.Circuit_Chip_Biocell.get(64),
                MaterialsUEVplus.TranscendentMetal.getNanite(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mBiologicalIntelligenceCatalyst, 1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                Particle.getBaseParticle(Particle.HIGGS_BOSON),
                Particle.getIon("Helium", 0),
                Particle.getIon("Hydrogen", 0),
                MaterialsUEVplus.Eternity.getNanite(16))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mSynchrotronCapableCatalyst, 1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                GT_OreDictUnificator.get("blockShirabon", 16),
                MaterialsUEVplus.Universium.getNanite(1),
                ItemList.Timepiece.get(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.TemporalHarmonyCatalyst, 1))
            .fluidInputs(Materials.DarkIron.getMolten(92160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(10),
                CI.getEmptyCatalyst(1),
                getModItem(NewHorizonsCoreMod.ID, "item.TCetiESeaweedExtract", 64),
                GT_OreDictUnificator.get("dustIodine", 64),
                MaterialsUEVplus.TranscendentMetal.getNanite(1))
            .itemOutputs(ItemUtils.getSimpleStack(GenericChem.mAlgagenicGrowthPromoterCatalyst, 1))
            .fluidInputs(FluidUtils.getFluidStack("molten.shirabon", 92_160))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(assemblerRecipes);
    }

    private static void tieredCasingRecipes() {
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.ForceFieldGlass.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(1), Materials.Carbon.getNanite(4),
                ItemList.Emitter_UV.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 2) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 10), MaterialsUEVplus.ExcitedDTCC.getFluid(5000),
                new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 500),
                new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 500) },
            GregtechItemList.NeutronPulseManipulator.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronPulseManipulator.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(2), Materials.Carbon.getNanite(8),
                ItemList.Emitter_UEV.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 4),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 12), MaterialsUEVplus.ExcitedDTPC.getFluid(5000),
                new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 2500),
                new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 2500) },
            GregtechItemList.CosmicFabricManipulator.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricManipulator.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(4), Materials.Carbon.getNanite(16),
                ItemList.Emitter_UIV.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 8),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 15), MaterialsUEVplus.ExcitedDTRC.getFluid(5000),
                new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 1000 * 10),
                new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 1000 * 10) },
            GregtechItemList.InfinityInfusedManipulator.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedManipulator.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { GregtechItemList.ForceFieldGlass.get(8), Materials.Carbon.getNanite(32),
                ItemList.Emitter_UMV.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 8),
                GregtechItemList.Laser_Lens_Special.get(1), new ItemStack(advancedRadiationProtectionPlate, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] { Materials.Thulium.getMolten(144 * 20), MaterialsUEVplus.ExcitedDTEC.getFluid(5000),
                new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 2000 * 10),
                new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 2000 * 10) },
            GregtechItemList.SpaceTimeContinuumRipper.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UXV);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_AdvancedRadiationProof.get(1),
            1024 * 30 * 20,
            1024,
            (int) TierEU.RECIPE_ZPM,
            32,
            new ItemStack[] { ALLOY.QUANTUM.getFrameBox(1),
                GT_OreDictUnificator.get("plateDensePreciousMetalsAlloy", 4),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 16),
                ItemList.Field_Generator_UV.get(1), ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getScrew(16) },
            new FluidStack[] { MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 10), },
            GregtechItemList.NeutronShieldingCore.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.NeutronShieldingCore.get(1),
            2048 * 30 * 20,
            2048,
            (int) TierEU.RECIPE_UV,
            32,
            new ItemStack[] { ALLOY.QUANTUM.getFrameBox(2),
                GT_OreDictUnificator.get("plateDenseEnrichedNaquadahAlloy", 4),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 16),
                ItemList.Field_Generator_UEV.get(1),
                // Radox polymer screw.
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.get("RadoxPoly"), 16),
                ItemList.StableAdhesive.get(4) },
            new FluidStack[] { MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 20), },
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            75 * 20,
            (int) TierEU.RECIPE_UIV);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.CosmicFabricShieldingCore.get(1),
            4096 * 30 * 20,
            4096,
            (int) TierEU.RECIPE_UHV,
            32,
            new ItemStack[] { ALLOY.QUANTUM.getFrameBox(4), ELEMENT.STANDALONE.HYPOGEN.getPlateDense(4),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 16),
                ItemList.Field_Generator_UIV.get(1), GT_OreDictUnificator.get("screwMetastableOganesson", 16),
                ItemList.SuperconductorComposite.get(4) },
            new FluidStack[] { MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 40), },
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            90 * 20,
            (int) TierEU.RECIPE_UMV);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.InfinityInfusedShieldingCore.get(1),
            8192 * 30 * 20,
            8192,
            (int) TierEU.RECIPE_UEV,
            32,
            new ItemStack[] { ALLOY.QUANTUM.getFrameBox(8), GT_OreDictUnificator.get("plateDenseShirabon", 4),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 16),
                ItemList.Field_Generator_UMV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Dilithium, 16),
                ItemList.NaquadriaSupersolid.get(4) },
            new FluidStack[] { MISC_MATERIALS.MUTATED_LIVING_SOLDER.getFluidStack(144 * 80), },
            GregtechItemList.SpaceTimeBendingCore.get(1),
            120 * 20,
            (int) TierEU.RECIPE_UXV);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(QuantumGlassBlock.INSTANCE, 1),
                ItemList.Field_Generator_ZPM.get(1),
                ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getLongRod(6),
                ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getPlate(6))
            .itemOutputs(GregtechItemList.ForceFieldGlass.get(1))
            .fluidInputs(ALLOY.QUANTUM.getFluidStack(144 * 6))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);

    }
}
