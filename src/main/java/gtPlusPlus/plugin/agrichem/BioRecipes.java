package gtPlusPlus.plugin.agrichem;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cokeOvenRecipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;
import ic2.core.Ic2Items;

public class BioRecipes {

    public static void init() {
        recipeGreenAlgae();
        recipeBrownAlgae();
        recipeGoldenBrownAlgae();
        recipeRedAlgae();
        recipeWoodPellets();
        recipeWoodBricks();
        recipeCellulosePulp();
        recipeCatalystCarrier();
        recipeAluminiumSilverCatalyst();
        recipeAceticAcid();
        recipePropionicAcid();
        recipeEthanol();
        recipeCelluloseFibre();
        recipeGoldenBrownCelluloseFiber();
        recipeRedCelluloseFiber();
        recipeSodiumHydroxide();
        recipeSodiumCarbonate();
        recipePelletMold();
        recipeAluminiumPellet();
        recipeLithiumChloride();
        recipeSulfuricAcid();
        recipeUrea();
        recipeRawBioResin();
        recipeLiquidResin();
        recipeCompost();
        recipeMethane();
        recipeBenzene();
        recipeStyrene();
        registerFuels();
    }

    private static void registerFuels() {
        // Burnables
        GTPPCore.burnables.add(Pair.of(800, GregtechItemList.WoodPellet.get(1)));
        GTPPCore.burnables.add(Pair.of(4800, GregtechItemList.WoodBrick.get(1)));

        // Combustion Fuels
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("cellButanol", 1))
            .metadata(FUEL_VALUE, 400)
            .metadata(FUEL_TYPE, 0)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);
    }

    private static void recipeGreenAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenAlgaeBiomass.get(4))
            .itemOutputs(GregtechItemList.Compost.get(1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Turn into Cellulose
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenAlgaeBiomass.get(10))
            .itemOutputs(GregtechItemList.CelluloseFiber.get(5))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void recipeBrownAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BrownAlgaeBiomass.get(2))
            .itemOutputs(GregtechItemList.Compost.get(1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Alginic acid
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BrownAlgaeBiomass.get(5))
            .itemOutputs(GregtechItemList.AlginicAcid.get(1))
            .duration(1 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(extractorRecipes);

        // Lithium Chloride
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BrownAlgaeBiomass.get(4))
            .itemOutputs(GGMaterial.lithiumChloride.get(OrePrefixes.dust, 1))
            .duration(1 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Sodium Carbonate
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.BrownAlgaeBiomass.get(40))
            .itemOutputs(Materials.SodiumCarbonate.getDust(20))
            .fluidInputs(GTModHandler.getDistilledWater(2_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void recipeGoldenBrownAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GoldenBrownAlgaeBiomass.get(1))
            .itemOutputs(GregtechItemList.Compost.get(1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Turn into Cellulose
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GoldenBrownAlgaeBiomass.get(10))
            .itemOutputs(GregtechItemList.GoldenBrownCelluloseFiber.get(5))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);
    }

    private static void recipeRedAlgae() {
        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.RedAlgaeBiomass.get(1))
            .itemOutputs(GregtechItemList.Compost.get(2))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Turn into Cellulose
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.RedAlgaeBiomass.get(10))
            .itemOutputs(GregtechItemList.RedCelluloseFiber.get(5))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(assemblerRecipes);
    }

    private static void recipeCelluloseFibre() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CelluloseFiber.get(8), GregtechItemList.AlginicAcid.get(2))
            .itemOutputs(GregtechItemList.CellulosePulp.get(10))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(UniversalChemical);

        // Methanol Extraction
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CelluloseFiber.get(3))
            .fluidOutputs(Materials.Methanol.getFluid(250))
            .duration(1 * SECONDS + 17 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidExtractionRecipes);

        // Compost
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CelluloseFiber.get(3))
            .itemOutputs(GregtechItemList.Compost.get(1))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        // Plastic
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CellulosePulp.get(4))
            .circuit(16)
            .fluidInputs(Materials.AceticAcid.getFluid(500), new FluidStack(GTPPFluids.PropionicAcid, 500))
            .fluidOutputs(Materials.Polyethylene.getMolten(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeWoodPellets() {
        // Shapeless Recipe
        GTModHandler.addShapelessCraftingRecipe(
            GregtechItemList.WoodBrick.get(2),
            new ItemStack[] { GregtechItemList.WoodPellet.get(1), GregtechItemList.WoodPellet.get(1),
                GregtechItemList.WoodPellet.get(1), GregtechItemList.WoodPellet.get(1),
                GregtechItemList.WoodPellet.get(1), GregtechItemList.WoodPellet.get(1),
                GregtechItemList.WoodPellet.get(1), GregtechItemList.WoodPellet.get(1) });

        // Extruder Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CelluloseFiber.get(1), GregtechItemList.Pellet_Mold.get(0))
            .itemOutputs(GregtechItemList.WoodPellet.get(1))
            .duration(2 * SECONDS + 8 * TICKS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(extruderRecipes);

        // Assembly Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.WoodPellet.get(8))
            .itemOutputs(GregtechItemList.WoodBrick.get(2))
            .duration(5 * SECONDS)
            .eut(8)
            .addTo(assemblerRecipes);

        // CO2
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.WoodPellet.get(1))
            .fluidOutputs(Materials.CarbonDioxide.getGas(70))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidExtractionRecipes);

        // Add Charcoal Recipe
        if (Railcraft.isModLoaded()) {
            RailcraftUtils.addCokeOvenRecipe(
                GregtechItemList.WoodPellet.get(2),
                true,
                true,
                Materials.Charcoal.getGems(3),
                GTValues.NF,
                1200);
        }
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.WoodPellet.get(2))
            .itemOutputs(Materials.Charcoal.getGems(3))
            .eut(TierEU.RECIPE_LV / 2)
            .duration(6 * SECONDS)
            .addTo(cokeOvenRecipes);
    }

    private static void recipeWoodBricks() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Wood.getDust(50))
            .circuit(3)
            .itemOutputs(GregtechItemList.WoodBrick.get(1))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void recipeCellulosePulp() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CellulosePulp.get(4))
            .itemOutputs(new ItemStack(Items.paper, 4))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void recipeCatalystCarrier() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 8),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 4),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Tin, 6))
            .circuit(2)
            .itemOutputs(GregtechItemList.EmptyCatalystCarrier.get(1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(assemblerRecipes);
    }

    private static void recipeAluminiumSilverCatalyst() {
        // Assembler Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.EmptyCatalystCarrier.get(10),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 4))
            .circuit(4)
            .itemOutputs(GregtechItemList.GreenMetalCatalyst.get(10))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);
    }

    private static void recipeAceticAcid() {
        // CH4O + CO = C2H4O2
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenMetalCatalyst.get(0))
            .fluidInputs(Materials.Methanol.getFluid(700), Materials.CarbonMonoxide.getGas(700))
            .fluidOutputs(Materials.AceticAcid.getFluid(700))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.FermentationBase, 1_000))
            .fluidOutputs(Materials.AceticAcid.getFluid(1_000))
            .itemOutputs(GregtechItemList.Compost.get(2))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void recipePropionicAcid() {
        // C2H4 + CO + H2O = C3H6O2
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenMetalCatalyst.get(0))
            .fluidInputs(
                Materials.Ethylene.getGas(1_000),
                Materials.CarbonMonoxide.getGas(1_000),
                GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(new FluidStack(GTPPFluids.PropionicAcid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeEthanol() {
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(new FluidStack(GTPPFluids.FermentationBase, 40))
            .fluidOutputs(Materials.Ethanol.getFluid(4))
            .duration(16 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distilleryRecipes);
    }

    private static void recipeGoldenBrownCelluloseFiber() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GoldenBrownCelluloseFiber.get(1))
            .fluidOutputs(Materials.Ammonia.getGas(100))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidExtractionRecipes);
    }

    private static void recipeRedCelluloseFiber() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.RedCelluloseFiber.get(3))
            .itemOutputs(GregtechItemList.CalciumCarbonateDust.get(5))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(extractorRecipes);
    }

    private static void recipeSodiumHydroxide() {
        // NaCl·H2O = NaOH + Cl + H
        GTValues.RA.stdBuilder()
            .circuit(4)
            .itemOutputs(Materials.SodiumHydroxide.getDust(3))
            .fluidInputs(Materials.SaltWater.getFluid(1_000))
            .fluidOutputs(Materials.Chlorine.getGas(1_000), Materials.Hydrogen.getGas(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        // Na + H2O = NaOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(5))
            .circuit(5)
            .itemOutputs(Materials.SodiumHydroxide.getDust(15))
            .fluidInputs(GTModHandler.getDistilledWater(5_000))
            .fluidOutputs(Materials.Hydrogen.getGas(5_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeSodiumCarbonate() {
        if (!GTOreDictUnificator.getOres("fuelCoke")
            .isEmpty()) {
            // Na2CO3 + Al2O3 =C= 2NaAlO2 + CO2
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTOreDictUnificator.get("fuelCoke", 1),
                    Materials.SodiumCarbonate.getDust(6),
                    GregtechItemList.AluminiumPellet.get(5))
                .circuit(18)
                .itemOutputs(Materials.SodiumAluminate.getDust(8))
                .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
                .duration(2 * MINUTES)
                .eut(TierEU.RECIPE_MV)
                .metadata(CHEMPLANT_CASING_TIER, 1)
                .addTo(chemicalPlantRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Coal.getDust(2),
                Materials.SodiumCarbonate.getDust(6),
                GregtechItemList.AluminiumPellet.get(5))
            .circuit(18)
            .itemOutputs(Materials.SodiumAluminate.getDust(8))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipePelletMold() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsAlloy.TUMBAGA.getBlock(1))
            .itemOutputs(GregtechItemList.Pellet_Mold.get(1))
            .duration(7 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 4 * 3)
            .addTo(latheRecipes);
    }

    private static void recipeAluminiumPellet() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sapphire, 5))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(3))
            .fluidInputs(Materials.Steam.getGas(10_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 300))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.GreenSapphire, 5))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(3))
            .fluidInputs(Materials.Steam.getGas(10_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 300))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Ruby, 6))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(3))
            .fluidInputs(Materials.Steam.getGas(12_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 300))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Grossular, 20))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(3))
            .fluidInputs(Materials.Steam.getGas(40_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 300))
            .duration(60 * SECONDS)
            .eut(90)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pyrope, 20))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(3))
            .fluidInputs(Materials.Steam.getGas(40_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 300))
            .duration(60 * SECONDS)
            .eut(90)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sodalite, 11))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(5))
            .fluidInputs(Materials.Steam.getGas(22_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 500))
            .duration(60 * SECONDS)
            .eut(90)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Spodumene, 10))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(2))
            .fluidInputs(Materials.Steam.getGas(20_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 200))
            .duration(60 * SECONDS)
            .eut(90)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 39))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(23))
            .fluidInputs(Materials.Steam.getGas(78_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 2_300))
            .duration(60 * SECONDS)
            .eut(90)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Lazurite, 14))
            .circuit(14)
            .itemOutputs(GregtechItemList.CleanAluminiumMix.get(5))
            .fluidInputs(Materials.Steam.getGas(28_000))
            .fluidOutputs(new FluidStack(GTPPFluids.RedMud, 500))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CleanAluminiumMix.get(3), GregtechItemList.Pellet_Mold.get(0))
            .itemOutputs(GregtechItemList.AluminiumPellet.get(4))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(extruderRecipes);
    }

    private static void recipeLithiumChloride() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.RockSalt, 8),
                GGMaterial.lithiumChloride.get(OrePrefixes.dust, 10))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 2),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 3),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 3),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 5),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 2),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Potassium, 5))
            .outputChances(7500, 8000, 8500, 9000, 7500, 8500)
            .fluidInputs(Materials.Air.getGas(4_000))
            .fluidOutputs(Materials.Chlorine.getGas(500))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Potash, 10),
                GGMaterial.lithiumChloride.get(OrePrefixes.dust, 16))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Lithium, 3),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 5),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Lithium, 5),
                GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Lithium, 7),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 2))
            .outputChances(7500, 8000, 8500, 9000, 9000, 9000)
            .fluidInputs(FluidRegistry.getFluidStack("ic2hotwater", 2_000))
            .fluidOutputs(Materials.Chlorine.getGas(250))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_MV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void recipeSulfuricAcid() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenAlgaeBiomass.get(10), GregtechItemList.BrownAlgaeBiomass.get(6))
            .circuit(7)
            .fluidInputs(GTModHandler.getDistilledWater(5_000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(5_000))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GoldenBrownAlgaeBiomass.get(2), GregtechItemList.BrownAlgaeBiomass.get(10))
            .circuit(7)
            .fluidInputs(GTModHandler.getDistilledWater(5_000))
            .fluidOutputs(Materials.SulfuricAcid.getFluid(5_000))
            .duration(6 * SECONDS)
            .eut(180)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeUrea() {
        // 2NH3 + CO2 = CH4N2O + H2O
        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(Materials.Ammonia.getGas(600), Materials.CarbonDioxide.getGas(300))
            .fluidOutputs(new FluidStack(GTPPFluids.Urea, 300), GTModHandler.getDistilledWater(300))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(new FluidStack(GTPPFluids.Urea, 200), new FluidStack(GTPPFluids.Formaldehyde, 200))
            .fluidOutputs(new FluidStack(GTPPFluids.LiquidResin, 200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeRawBioResin() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenAlgaeBiomass.get(5), new ItemStack(Blocks.dirt, 1))
            .circuit(3)
            .itemOutputs(GregtechItemList.RawBioResin.get(1))
            .fluidInputs(GTModHandler.getDistilledWater(100))
            .duration(20 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeLiquidResin() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.RawBioResin.get(1))
            .circuit(3)
            .fluidInputs(Materials.Ethanol.getFluid(200))
            .fluidOutputs(new FluidStack(GTPPFluids.LiquidResin, 500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.CellulosePulp.get(8))
            .circuit(3)
            .itemOutputs(GTUtility.copyAmount(32, Ic2Items.resin))
            .fluidInputs(new FluidStack(GTPPFluids.LiquidResin, 1 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeCompost() {
        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GregtechItemList.GreenAlgaeBiomass.get(16), GregtechItemList.Compost.get(8))
                .circuit(11)
                .itemOutputs(ItemList.FR_Fertilizer.get(32))
                .fluidInputs(new FluidStack(GTPPFluids.Urea, 200))
                .duration(30 * SECONDS)
                .eut(TierEU.RECIPE_MV / 2)
                .metadata(CHEMPLANT_CASING_TIER, 1)
                .addTo(chemicalPlantRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenAlgaeBiomass.get(16), GregtechItemList.Compost.get(8))
            .circuit(12)
            .itemOutputs(ItemList.IC2_Fertilizer.get(32))
            .fluidInputs(new FluidStack(GTPPFluids.Urea, 200))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

    }

    private static void recipeMethane() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.AlgaeBiomass.get(10))
            .circuit(12)
            .fluidInputs(GTModHandler.getDistilledWater(500))
            .fluidOutputs(Materials.Methane.getGas(500))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.CelluloseFiber.get(8),
                GregtechItemList.GoldenBrownCelluloseFiber.get(6),
                GregtechItemList.RedCelluloseFiber.get(4))
            .circuit(13)
            .fluidInputs(Materials.Methane.getGas(2_000))
            .fluidOutputs(Materials.Ethylene.getGas(2_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 1)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeBenzene() {
        // 6CH4 = C6H6 + 18H
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenMetalCatalyst.get(0))
            .circuit(19)
            .fluidInputs(Materials.Methane.getGas(6_000))
            .fluidOutputs(Materials.Benzene.getFluid(1_000), Materials.Hydrogen.getGas(18_000))
            .duration(8 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }

    private static void recipeStyrene() {
        // C8H10 = C8H8 + 2H
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.GreenMetalCatalyst.get(0))
            .circuit(20)
            .fluidInputs(new FluidStack(GTPPFluids.Ethylbenzene, 100))
            .fluidOutputs(Materials.Styrene.getFluid(100), Materials.Hydrogen.getGas(200))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);
    }
}
