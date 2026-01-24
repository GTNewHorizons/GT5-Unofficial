package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.Backpack;
import static gregtech.api.enums.Mods.Baubles;
import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.brewingRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.distilleryRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.extractorRecipes;
import static gregtech.api.recipe.RecipeMaps.extruderRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.HOURS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.CHEMPLANT_CASING_TIER;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.FUSION_THRESHOLD;
import static gregtech.api.util.GTRecipeConstants.RESEARCH_ITEM;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalPlantRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.cyclotronRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.thermalBoilerRecipes;

import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gregtech.loaders.postload.chains.NetheriteRecipes;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.crafting.ItemDummyResearch;
import gtPlusPlus.core.item.crafting.ItemDummyResearch.ASSEMBLY_LINE_RESEARCH;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.MaterialsOres;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.material.nuclear.MaterialsFluorides;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import ic2.core.Ic2Items;

public class RecipesGregTech {

    public static void run() {
        electrolyzerRecipes();
        assemblerRecipes();
        distilleryRecipes();
        extractorRecipes();
        fluidExtractorRecipes();
        chemicalReactorRecipes();
        dehydratorRecipes();
        blastFurnaceRecipes();
        largeChemReactorRecipes();
        fusionRecipes();
        compressorRecipes();
        mixerRecipes();
        macerationRecipes();
        centrifugeRecipes();
        cyclotronRecipes();
        blastSmelterRecipes();
        extruderRecipes();
        breweryRecipes();
        laserEngraverRecipes();
        assemblyLineRecipes();
        fluidHeaterRecipes();
        chemplantRecipes();
        alloySmelterRecipes();
        thermalBoilerRecipes();
        craftingTableRecipes();

        /*
         * Special Recipe handlers
         */
        RecipesSeleniumProcessing.init();
        RecipesRareEarthProcessing.init();
        NetheriteRecipes.run();

        addFuels();

        if (Baubles.isModLoaded()) {
            baublesRecipes();
        }
    }

    private static void alloySmelterRecipes() {
        // Wood's Glass Laser Lens
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialMisc.WOODS_GLASS.getDust(5), ItemList.Shape_Mold_Ball.get(0))
            .itemOutputs(GregtechItemList.Laser_Lens_WoodsGlass.get(1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(alloySmelterRecipes);
    }

    private static void chemplantRecipes() {
        // Advanced method for Nitric Acid Production
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PinkMetalCatalyst.get(0))
            .circuit(17)
            .fluidInputs(
                Materials.NitrogenDioxide.getGas(4_000),
                Materials.Air.getGas(4_000),
                Materials.Water.getFluid(2_000))
            .fluidOutputs(Materials.NitricAcid.getFluid(4_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Advanced recipe for Fluorine Production
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PurpleMetalCatalyst.get(0),
                new ItemStack(Blocks.sandstone, 64),
                new ItemStack(Blocks.sandstone, 64))
            .circuit(17)
            .itemOutputs(
                MaterialsFluorides.FLUORITE.getOre(8),
                MaterialsFluorides.FLUORITE.getOre(4),
                MaterialsFluorides.FLUORITE.getOre(4),
                MaterialsFluorides.FLUORITE.getOre(4))
            .fluidInputs(Materials.NitricAcid.getFluid(4_000), Materials.Air.getGas(8_000))
            .duration(10 * SECONDS)
            .eut(1024)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        // Advanced recipe for Fluorine Production
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PurpleMetalCatalyst.get(0),
                new ItemStack(Blocks.sand, 64),
                new ItemStack(Blocks.sand, 64))
            .circuit(17)
            .itemOutputs(
                MaterialsFluorides.FLUORITE.getOre(4),
                MaterialsFluorides.FLUORITE.getOre(2),
                MaterialsFluorides.FLUORITE.getOre(2),
                MaterialsFluorides.FLUORITE.getOre(2))
            .fluidInputs(Materials.NitricAcid.getFluid(5_000), Materials.Air.getGas(12_000))
            .duration(10 * SECONDS)
            .eut(1024)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        // 3NO2 + H2O = 2HNO3 + NO
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PinkMetalCatalyst.get(0))
            .circuit(16)
            .fluidInputs(Materials.NitrogenDioxide.getGas(3_000), GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(Materials.NitricAcid.getFluid(2_000), Materials.NitricOxide.getGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

        // Produce Boric Acid
        // Na2B4O7·10H2O + 2HCl = 4B(OH)3 + 2NaCl + 5H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Borax, 23))
            .circuit(21)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 4))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.BoricAcid, 4_000), Materials.Water.getFluid(5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Produce Th232
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Thorium, 4))
            .itemOutputs(
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 3),
                MaterialsElements.getInstance().THORIUM232.getDust(1))
            .fluidInputs(GTModHandler.getDistilledWater(2_000), new FluidStack(GTPPFluids.BoricAcid, 500))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(CHEMPLANT_CASING_TIER, 4)
            .addTo(chemicalPlantRecipes);

        // Modify Sapling into Pine Sapling
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.sapling, 32))
            .circuit(6)
            .itemOutputs(new ItemStack(BOPBlockRegistrator.sapling_Pine, 16))
            .fluidInputs(new FluidStack(GTPPFluids.GeneticMutagen, 2_000), GTModHandler.getDistilledWater(8_000))
            .duration(120 * SECONDS)
            .eut(64)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 2))
            .circuit(20)
            .fluidInputs(FluidRegistry.getFluidStack("mobessence", 5_000))
            .fluidOutputs(new FluidStack(GTPPFluids.GeneticMutagen, 8_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Vigorous Laurenium Machine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_LuV.get(1),
                MaterialsAlloy.LAURENIUM.getPlate(8),
                MaterialsAlloy.PIKYONIUM.getGear(2))
            .itemOutputs(GregtechItemList.Casing_Machine_Custom_3.get(1))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(2 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(4 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(6 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        // Rugged Botmium Machine Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_ZPM.get(1),
                MaterialsAlloy.BOTMIUM.getPlate(8),
                MaterialsAlloy.TITANSTEEL.getGear(2))
            .itemOutputs(GregtechItemList.Casing_Machine_Custom_4.get(1))
            .fluidInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(2 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(4 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(6 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .metadata(CHEMPLANT_CASING_TIER, 6)
            .addTo(chemicalPlantRecipes);

        // Refine HF into Industrial Strength HF
        GTValues.RA.stdBuilder()
            .circuit(22)
            .fluidInputs(
                new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 2_000),
                Materials.HydrofluoricAcid.getFluid(5_000))
            .fluidOutputs(new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 4_500))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);
    }

    private static void fluidHeaterRecipes() {
        GTValues.RA.stdBuilder()
            .circuit(20)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(GTModHandler.getHotWater(1_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(fluidHeaterRecipes);
    }

    private static void fusionRecipes() {
        // Hypogen
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(1 * INGOTS),
                MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(2 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(36))
            .duration(6 * MINUTES + 49 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UHV)
            .metadata(FUSION_THRESHOLD, 1_200_000_000L)
            .addTo(fusionRecipes);

        // Rhugnor
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Infinity.getMolten(1 * INGOTS), MaterialsAlloy.QUANTUM.getFluidStack(2 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(1 * INGOTS))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(fusionRecipes);

        // Rhugnor Mk5
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.QuarkGluonPlasma.getFluid(1 * HALF_INGOTS),
                MaterialsAlloy.QUANTUM.getFluidStack(4 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(4 * INGOTS))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UEV)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(fusionRecipes);
    }

    private static void assemblyLineRecipes() {
        // Turbine Housing / Turbine Automation Port
        GTValues.RA.stdBuilder()
            .metadata(
                RESEARCH_ITEM,
                ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_8_TURBINE_AUTOMATION, 1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_ZPM))
            .itemInputs(
                ItemList.Hull_UV.get(4),
                ItemList.Conveyor_Module_UV.get(24),
                ItemList.Electric_Motor_ZPM.get(32),
                ItemList.Electric_Piston_ZPM.get(16),
                GregtechItemList.Energy_Core_LuV.get(8),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(24),
                MaterialsAlloy.TITANSTEEL.getScrew(48),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(32),
                MaterialsAlloy.ZERON_100.getRod(12),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 20 },
                MaterialsAlloy.LAFIUM.getRotor(16))
            .fluidInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.BABBIT_ALLOY.getFluidStack(128 * INGOTS))
            .itemOutputs(GregtechItemList.Hatch_Input_TurbineHousing.get(4))
            .eut(TierEU.RECIPE_UV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Containment Unit I
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Particle.getBaseParticle(Particle.UNKNOWN))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                MaterialsAlloy.INCONEL_625.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.YttriumBariumCuprate, 32),
                MaterialsAlloy.INCONEL_625.getGear(4),
                GregtechItemList.DehydratorCoilWireEV.get(64))
            .fluidInputs(
                MaterialsAlloy.ZERON_100.getFluidStack(8 * INGOTS),
                MaterialsAlloy.HASTELLOY_N.getFluidStack(16 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Casing_Gem_1.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Containment Unit II
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Battery_Casing_Gem_1.get(1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                MaterialsAlloy.ZERON_100.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.Naquadah, 32),
                MaterialsAlloy.ZERON_100.getGear(4),
                GregtechItemList.DehydratorCoilWireIV.get(64))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(8 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Casing_Gem_2.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Advanced Containment Unit
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Battery_Casing_Gem_2.get(1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                MaterialsAlloy.PIKYONIUM.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.Duranium, 32),
                MaterialsAlloy.PIKYONIUM.getGear(4),
                GregtechItemList.DehydratorCoilWireLuV.get(64))
            .fluidInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(8 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Casing_Gem_3.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        // Exotic Containment Unit
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Battery_Casing_Gem_3.get(1))
            .metadata(SCANNING, new Scanning(50 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 32),
                MaterialsAlloy.TITANSTEEL.getGear(4),
                GregtechItemList.DehydratorCoilWireZPM.get(64))
            .fluidInputs(
                MaterialsAlloy.ABYSSAL.getFluidStack(8 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.OCTIRON.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Casing_Gem_4.get(1))
            .eut(TierEU.RECIPE_UHV)
            .duration(2 * MINUTES)
            .addTo(AssemblyLine);

        /*
         * Old Gem Battery Recipes
         * todo: deprecated, remove after 2.9
         */

        // Proton Cell
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Particle.getBaseParticle(Particle.PROTON))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_1.get(1),
                Particle.getBaseParticle(Particle.PROTON, 16),
                MaterialsAlloy.ZERON_100.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.YttriumBariumCuprate, 32),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(8),
                MaterialsAlloy.INCONEL_625.getScrew(8))
            .fluidInputs(
                MaterialsAlloy.ZERON_100.getFluidStack(16 * INGOTS),
                MaterialsAlloy.HASTELLOY_N.getFluidStack(32 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(16 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_1.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Electron Cell
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Particle.getBaseParticle(Particle.ELECTRON))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_2.get(1),
                Particle.getBaseParticle(Particle.ELECTRON, 16),
                MaterialsAlloy.PIKYONIUM.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 32),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(8),
                MaterialsAlloy.ZERON_100.getScrew(8))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(16 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_2.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Quark Entanglement
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Particle.getBaseParticle(Particle.CHARM))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_UHV))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_3.get(1),
                Particle.getBaseParticle(Particle.CHARM, 16),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Duranium, 32),
                MaterialsAlloy.TITANSTEEL.getBolt(8),
                MaterialsAlloy.PIKYONIUM.getScrew(8))
            .fluidInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_3.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Graviton Anomaly
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, Particle.getBaseParticle(Particle.GRAVITON))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_UEV))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_4.get(1),
                Particle.getBaseParticle(Particle.GRAVITON, 16),
                MaterialsAlloy.ABYSSAL.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 32),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getBolt(8),
                MaterialsAlloy.TITANSTEEL.getScrew(8))
            .fluidInputs(
                MaterialsAlloy.ABYSSAL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.OCTIRON.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_4.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        if (Baubles.isModLoaded()) {
            // Personal Healing Nanobooster
            GTValues.RA.stdBuilder()
                .metadata(RESEARCH_ITEM, new ItemStack(Items.golden_apple, 1, 1))
                .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_LuV))
                .itemInputs(
                    GregtechItemList.Battery_Casing_Gem_3.get(4),
                    MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(32),
                    new Object[] { "circuitUltimate", 16 },
                    GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.YttriumBariumCuprate, 16),
                    MaterialsAlloy.ZERON_100.getGear(6),
                    MaterialsAlloy.PIKYONIUM.getScrew(16),
                    MaterialsAlloy.ENERGYCRYSTAL.getBolt(24),
                    MaterialsAlloy.HASTELLOY_N.getFrameBox(12),
                    GregtechItemList.DehydratorCoilWireZPM.get(64))
                .fluidInputs(
                    MaterialsAlloy.PIKYONIUM.getFluidStack(4 * STACKS + 32 * INGOTS),
                    MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(4 * STACKS + 32 * INGOTS),
                    MaterialsAlloy.LAFIUM.getFluidStack(4 * STACKS + 32 * INGOTS),
                    MaterialsAlloy.CINOBITE.getFluidStack(4 * STACKS + 32 * INGOTS))
                .itemOutputs(GregtechItemList.PersonalHealingDevice.get(1))
                .eut(TierEU.RECIPE_ZPM)
                .duration(3 * MINUTES)
                .addTo(AssemblyLine);

            // Charge Pack LuV-UHV

            ItemStack[] aChargeResearch = new ItemStack[] { GregtechItemList.Energy_Core_LuV.get(1),
                GregtechItemList.Energy_Core_ZPM.get(1), GregtechItemList.Energy_Core_UV.get(1),
                GregtechItemList.Energy_Core_UHV.get(1) };

            ItemStack[] aChargeOutputs = new ItemStack[] { GregtechItemList.ChargePack_LuV.get(1),
                GregtechItemList.ChargePack_ZPM.get(1), GregtechItemList.ChargePack_UV.get(1),
                GregtechItemList.ChargePack_UHV.get(1), };

            ItemStack[] aCoilWire = new ItemStack[] { GregtechItemList.DehydratorCoilWireEV.get(64),
                GregtechItemList.DehydratorCoilWireIV.get(64), GregtechItemList.DehydratorCoilWireLuV.get(64),
                GregtechItemList.DehydratorCoilWireZPM.get(64) };

            ItemStack[] aTieredPlates_ChargePack = new ItemStack[] { MaterialsAlloy.ZERON_100.getPlate(8),
                MaterialsAlloy.PIKYONIUM.getPlate(8), MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(8),
                MaterialsAlloy.ABYSSAL.getPlate(8) };

            ItemStack[] aTieredCircuits0_ChargePack = new ItemStack[] {
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 4) };

            ItemStack[] aTieredCircuits1_ChargePack = new ItemStack[] {
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 8) };

            ItemStack[] aTieredCable12 = new ItemStack[] {
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Nichrome, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Platinum, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.YttriumBariumCuprate, 16),
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Naquadah, 16) };

            ItemStack[] aTieredScrew_ChargePack = new ItemStack[] { MaterialsAlloy.ZERON_100.getScrew(16),
                MaterialsAlloy.PIKYONIUM.getScrew(16), MaterialsAlloy.TITANSTEEL.getScrew(16),
                MaterialsAlloy.ABYSSAL.getScrew(16) };

            ItemStack[] aTieredBolt_ChargePack = new ItemStack[] { MaterialsAlloy.HASTELLOY_N.getBolt(32),
                MaterialsAlloy.ENERGYCRYSTAL.getBolt(32), MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(32),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(32) };

            ItemStack[] aTieredFieldGenerator = new ItemStack[] { ItemList.Field_Generator_IV.get(1),
                ItemList.Field_Generator_LuV.get(1), ItemList.Field_Generator_ZPM.get(1),
                ItemList.Field_Generator_UV.get(1), };

            FluidStack[] aFluid0_ChargePack = new FluidStack[] { MaterialsAlloy.ZERON_100.getFluidStack(32 * INGOTS),
                MaterialsAlloy.PIKYONIUM.getFluidStack(32 * INGOTS),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.ABYSSAL.getFluidStack(32 * INGOTS) };

            FluidStack[] aFluid1_ChargePack = new FluidStack[] {
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(32 * INGOTS) };

            FluidStack[] aFluid2_ChargePack = new FluidStack[] { MaterialsAlloy.ARCANITE.getFluidStack(32 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(32 * INGOTS), MaterialsAlloy.CINOBITE.getFluidStack(32 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(32 * INGOTS) };

            FluidStack[] aFluid3_ChargePack = new FluidStack[] { MaterialsAlloy.INCONEL_792.getFluidStack(32 * INGOTS),
                MaterialsAlloy.ARCANITE.getFluidStack(32 * INGOTS), MaterialsAlloy.LAFIUM.getFluidStack(32 * INGOTS) };

            ItemStack[] aGemBatteries = new ItemStack[] { GregtechItemList.Battery_Casing_Gem_1.get(2),
                GregtechItemList.Battery_Casing_Gem_2.get(2), GregtechItemList.Battery_Casing_Gem_3.get(2),
                GregtechItemList.Battery_Casing_Gem_4.get(2),

            };

            int aCurrSlot = 0;
            for (int h = 6; h < 9; h++) {
                GTValues.RA.stdBuilder()
                    .metadata(RESEARCH_ITEM, aChargeResearch[aCurrSlot])
                    .metadata(SCANNING, new Scanning(30 * SECONDS, GTValues.VP[aCurrSlot + 5]))
                    .itemInputs(
                        aGemBatteries[aCurrSlot],
                        aCoilWire[aCurrSlot],
                        aTieredPlates_ChargePack[aCurrSlot],
                        aTieredCircuits0_ChargePack[aCurrSlot],
                        aTieredCircuits1_ChargePack[aCurrSlot],
                        aTieredCable12[aCurrSlot],
                        aTieredScrew_ChargePack[aCurrSlot],
                        aTieredBolt_ChargePack[aCurrSlot],
                        aTieredFieldGenerator[aCurrSlot])
                    .fluidInputs(
                        aFluid0_ChargePack[aCurrSlot],
                        aFluid1_ChargePack[aCurrSlot],
                        aFluid2_ChargePack[aCurrSlot],
                        aFluid3_ChargePack[aCurrSlot])
                    .itemOutputs(aChargeOutputs[aCurrSlot])
                    .eut(GTValues.VP[h])
                    .duration(3 * MINUTES)
                    .addTo(AssemblyLine);
                aCurrSlot++;
            }
        }

        /*
         * Gem Battery Recipes (post LHC/beamcrafting)
         */

        // Proton Cell
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.StableBaryonContainmentUnit.get(1))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_1.get(1),
                ItemList.StableBaryonContainmentUnit.get(16),
                MaterialsAlloy.ZERON_100.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.YttriumBariumCuprate, 32),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(8),
                MaterialsAlloy.INCONEL_625.getScrew(8))
            .fluidInputs(
                MaterialsAlloy.ZERON_100.getFluidStack(16 * INGOTS),
                MaterialsAlloy.HASTELLOY_N.getFluidStack(32 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(16 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_1.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Electron Cell
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.StableLeptonContainmentUnit.get(1))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_2.get(1),
                ItemList.StableLeptonContainmentUnit.get(16),
                MaterialsAlloy.PIKYONIUM.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 32),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(8),
                MaterialsAlloy.ZERON_100.getScrew(8))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(16 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_2.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Quark Entanglement
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.StableMesonContainmentUnit.get(1))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_UHV))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_3.get(1),
                ItemList.StableMesonContainmentUnit.get(16),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Duranium, 32),
                MaterialsAlloy.TITANSTEEL.getBolt(8),
                MaterialsAlloy.PIKYONIUM.getScrew(8))
            .fluidInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_3.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(AssemblyLine);

        // Graviton Anomaly
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemList.StableBosonContainmentUnit.get(1))
            .metadata(SCANNING, new Scanning(60 * SECONDS, TierEU.RECIPE_UEV))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_4.get(1),
                ItemList.StableBosonContainmentUnit.get(16),
                MaterialsAlloy.ABYSSAL.getPlate(16),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UHV, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 32),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getBolt(8),
                MaterialsAlloy.TITANSTEEL.getScrew(8))
            .fluidInputs(
                MaterialsAlloy.ABYSSAL.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.OCTIRON.getFluidStack(16 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(16 * INGOTS))
            .itemOutputs(GregtechItemList.Battery_Gem_4.get(1))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(AssemblyLine);

        // Mega Alloy Blast Smelter
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Industrial_AlloyBlastSmelter.get(1))
            .metadata(SCANNING, new Scanning(2 * MINUTES + 20 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64),
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64),
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64),
                GregtechItemList.Industrial_AlloyBlastSmelter.get(64),
                ItemList.UV_Coil.get(16),
                ItemList.Conveyor_Module_UV.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 16 },
                ItemList.Circuit_Chip_PPIC.get(16),
                MaterialsAlloy.PIKYONIUM.getPlate(16),
                MaterialsAlloy.CINOBITE.getScrew(32))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(8 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS),
                Materials.SolderingAlloy.getMolten(10 * INGOTS))
            .itemOutputs(GregtechItemList.Mega_AlloyBlastSmelter.get(1))
            .eut(TierEU.RECIPE_UHV / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

        // Expandable Hand Pump
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.HandPumpToken_IV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Electric_Pump_LuV.get(4),
                ItemList.Electric_Motor_LuV.get(4),
                GregtechItemList.VOLUMETRIC_FLASK_32k.get(4),
                MaterialsAlloy.LAFIUM.getScrew(16),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.ring, 8),
                WerkstoffLoader.LuVTierMaterial.get(OrePrefixes.stick, 16),
                Materials.Osmiridium.getPlates(32))
            .fluidInputs(
                MaterialsAlloy.HELICOPTER.getFluidStack(32 * INGOTS),
                MaterialsAlloy.INDALLOY_140.getFluidStack(1 * STACKS))
            .itemOutputs(GregtechItemList.ExpandableHandPump.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(30 * SECONDS)
            .addTo(AssemblyLine);
    }

    private static void baublesRecipes() {
        // Personal Healing Nanobooster
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, new ItemStack(Items.golden_apple, 1, 1))
            .metadata(SCANNING, new Scanning(2 * MINUTES, TierEU.RECIPE_LuV))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_3.get(4),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(32),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 16 },
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.YttriumBariumCuprate, 16),
                MaterialsAlloy.ZERON_100.getGear(6),
                MaterialsAlloy.PIKYONIUM.getScrew(16),
                MaterialsAlloy.ENERGYCRYSTAL.getBolt(24),
                MaterialsAlloy.HASTELLOY_N.getFrameBox(12),
                GregtechItemList.DehydratorCoilWireZPM.get(64))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(4 * STACKS + 32 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(4 * STACKS + 32 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(4 * STACKS + 32 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(4 * STACKS + 32 * INGOTS))
            .itemOutputs(GregtechItemList.PersonalHealingDevice.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);

        // High-tier charge packs

        // LuV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Energy_Core_LuV.get(1))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                GregtechItemList.Battery_Gem_1.get(2),
                GregtechItemList.DehydratorCoilWireEV.get(64),
                MaterialsAlloy.ZERON_100.getPlate(8),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.IV), 8 },
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Nichrome, 16),
                MaterialsAlloy.ZERON_100.getScrew(16),
                MaterialsAlloy.HASTELLOY_N.getBolt(32),
                ItemList.Field_Generator_IV.get(1))
            .fluidInputs(
                MaterialsAlloy.ZERON_100.getFluidStack(32 * INGOTS),
                MaterialsAlloy.ENERGYCRYSTAL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.ARCANITE.getFluidStack(32 * INGOTS),
                MaterialsAlloy.INCONEL_792.getFluidStack(32 * INGOTS))
            .itemOutputs(GregtechItemList.ChargePack_LuV.get(1))
            .eut(TierEU.RECIPE_LuV)
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);

        // ZPM
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Energy_Core_ZPM.get(1))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_LuV))
            .itemInputs(
                GregtechItemList.Battery_Gem_2.get(2),
                GregtechItemList.DehydratorCoilWireIV.get(64),
                MaterialsAlloy.PIKYONIUM.getPlate(8),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8 },
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Platinum, 16),
                MaterialsAlloy.PIKYONIUM.getScrew(16),
                MaterialsAlloy.ENERGYCRYSTAL.getBolt(32),
                ItemList.Field_Generator_LuV.get(1))
            .fluidInputs(
                MaterialsAlloy.PIKYONIUM.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFluidStack(32 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(32 * INGOTS),
                MaterialsAlloy.ARCANITE.getFluidStack(32 * INGOTS))
            .itemOutputs(GregtechItemList.ChargePack_ZPM.get(1))
            .eut(TierEU.RECIPE_ZPM)
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);

        // UV
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Energy_Core_UV.get(1))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                GregtechItemList.Battery_Gem_3.get(2),
                GregtechItemList.DehydratorCoilWireLuV.get(64),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 8 },
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.YttriumBariumCuprate, 16),
                MaterialsAlloy.TITANSTEEL.getScrew(16),
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getBolt(32),
                ItemList.Field_Generator_ZPM.get(1))
            .fluidInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(32 * INGOTS),
                MaterialsAlloy.LAFIUM.getFluidStack(32 * INGOTS))
            .itemOutputs(GregtechItemList.ChargePack_UV.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.Energy_Core_UHV.get(1))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_UV))
            .itemInputs(
                GregtechItemList.Battery_Gem_4.get(2),
                GregtechItemList.DehydratorCoilWireZPM.get(64),
                MaterialsAlloy.ABYSSAL.getPlate(8),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8 },
                GTOreDictUnificator.get(OrePrefixes.cableGt12, Materials.Naquadah, 16),
                MaterialsAlloy.ABYSSAL.getScrew(16),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(32),
                ItemList.Field_Generator_UV.get(1))
            .fluidInputs(
                MaterialsAlloy.ABYSSAL.getFluidStack(32 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(64 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(32 * INGOTS))
            .itemOutputs(GregtechItemList.ChargePack_UHV.get(1))
            .eut(GTValues.VP[9])
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);

        // Research on Cloaking Technologies
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.LFTRControlCircuit.get(4),
                ItemList.Field_Generator_LuV.get(16),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Palladium, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 12))
            .circuit(17)
            .itemOutputs(ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_9_CLOAKING, 1))
            .fluidInputs(MaterialsAlloy.CINOBITE.getFluidStack(32 * INGOTS))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Personal Cloaking Device
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_9_CLOAKING, 1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 40 * SECONDS, TierEU.RECIPE_ZPM))
            .itemInputs(
                GregtechItemList.Battery_Casing_Gem_4.get(4),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getPlate(32),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 16 },
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Naquadah, 16),
                MaterialsAlloy.PIKYONIUM.getGear(6),
                MaterialsAlloy.TITANSTEEL.getScrew(16),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(24),
                MaterialsAlloy.ENERGYCRYSTAL.getFrameBox(12),
                GregtechItemList.DehydratorCoilWireZPM.get(64))
            .fluidInputs(
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(4 * STACKS + 32 * INGOTS),
                MaterialsAlloy.TITANSTEEL.getFluidStack(4 * STACKS + 32 * INGOTS),
                MaterialsAlloy.CINOBITE.getFluidStack(9 * STACKS))
            .itemOutputs(GregtechItemList.PersonalCloakingDevice.get(1))
            .eut(TierEU.RECIPE_UV)
            .duration(3 * MINUTES)
            .addTo(AssemblyLine);
    }

    private static void laserEngraverRecipes() {
        // Transmission Components

        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_LV.get(2), ItemList.Sensor_LV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_LV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(laserEngraverRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_MV.get(2), ItemList.Sensor_MV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_MV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(laserEngraverRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_HV.get(2), ItemList.Sensor_HV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_HV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(laserEngraverRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_EV.get(2), ItemList.Sensor_EV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_EV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(laserEngraverRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_IV.get(2), ItemList.Sensor_IV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_IV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(laserEngraverRecipes);

        // LuV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_LuV.get(2), ItemList.Sensor_LuV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_LuV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(laserEngraverRecipes);

        // ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_ZPM.get(2), ItemList.Sensor_ZPM.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_ZPM.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(laserEngraverRecipes);

        // UV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_UV.get(2), ItemList.Sensor_UV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_UV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(laserEngraverRecipes);

        // UHV
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Emitter_UHV.get(2), ItemList.Sensor_UHV.get(2))
            .itemOutputs(GregtechItemList.TransmissionComponent_UHV.get(1))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        // Celestial Tungsten Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungsten, 6),
                GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_UEV)
            .addTo(laserEngraverRecipes);

        // Astral Titanium Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Titanium, 8),
                GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getDust(1))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);

        // Advanced Nitinol Block
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsAlloy.NITINOL_60.getBlock(2), GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.ADVANCED_NITINOL.getBlock(1))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_UV)
            .addTo(laserEngraverRecipes);

        // Chromatic Glass Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glass, 64),
                GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getDust(1))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_UHV)
            .addTo(laserEngraverRecipes);
    }

    private static void breweryRecipes() {
        // Mob Essence compatibility recipes
        if (Mods.OpenBlocks.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .circuit(14)
                .fluidInputs(FluidRegistry.getFluidStack("mobessence", 100))
                .fluidOutputs(FluidRegistry.getFluidStack("xpjuice", 1_332))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(brewingRecipes);

            GTValues.RA.stdBuilder()
                .circuit(14)
                .fluidInputs(FluidRegistry.getFluidStack("xpjuice", 1_332))
                .fluidOutputs(FluidRegistry.getFluidStack("mobessence", 100))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(brewingRecipes);
        }

        // Sapling compatibility recipes
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(Materials.Water.getFluid(100))
            .fluidOutputs(Materials.Biomass.getFluid(100))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(Materials.Honey.getFluid(100))
            .fluidOutputs(Materials.Biomass.getFluid(100))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(FluidRegistry.getFluidStack("juice", 100))
            .fluidOutputs(Materials.Biomass.getFluid(100))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);
    }

    private static void electrolyzerRecipes() {
        // Radium -> Radon electrolysis
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.DecayedRadium226Dust.get(1))
            .fluidOutputs(Materials.Radon.getGas(1 * INGOTS))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(electrolyzerRecipes);
    }

    private static void extruderRecipes() {
        // Osmium Credit
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Osmium, 1),
                ItemList.Shape_Mold_Credit.get(0))
            .itemOutputs(ItemList.Credit_Greg_Osmium.get(1))
            .duration(6 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(extruderRecipes);
    }

    private static void blastSmelterRecipes() {
        // Enderium
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Tin.getDust(2),
                Materials.Platinum.getDust(1),
                Materials.Silver.getDust(1),
                Materials.Thaumium.getDust(2),
                Materials.EnderPearl.getDust(2))
            .circuit(5)
            .fluidOutputs(Materials.Enderium.getMolten(8 * INGOTS))
            .eut(TierEU.RECIPE_EV)
            .duration(20 * SECONDS + 1 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Signalium
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.AnnealedCopper.getDust(30),
                Materials.Ardite.getDust(10),
                Materials.Redstone.getDust(50))
            .circuit(3)
            .fluidOutputs(GGMaterial.signalium.getMolten(5 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Lumiium
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Tin.getDust(10),
                Materials.Iron.getDust(10),
                Materials.Copper.getDust(2),
                Materials.Silver.getDust(8),
                GGMaterial.lumiinessence.get(OrePrefixes.dust, 10),
                Materials.Glowstone.getDust(10))
            .circuit(6)
            .fluidOutputs(GGMaterial.lumiium.getMolten(5 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Eglin Steel
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Iron.getDust(4),
                Materials.Kanthal.getDust(1),
                Materials.Invar.getDust(5),
                Materials.Sulfur.getDust(1),
                Materials.Carbon.getDust(1),
                Materials.Silicon.getDust(4))
            .circuit(6)
            .fluidOutputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_MV)
            .duration(45 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // HG1223
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Barium.getDust(2), Materials.Calcium.getDust(2), Materials.Copper.getDust(3))
            .circuit(5)
            .fluidInputs(Materials.Oxygen.getGas(8_000), Materials.Mercury.getFluid(1_000))
            .fluidOutputs(MaterialsAlloy.HG1223.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Nitinol 60
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Titanium.getDust(3), Materials.Nickel.getDust(2))
            .circuit(2)
            .fluidOutputs(MaterialsAlloy.NITINOL_60.getFluidStack(5 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Indalloy 140
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Bismuth.getDust(47),
                Materials.Lead.getDust(25),
                Materials.Tin.getDust(13),
                Materials.Cadmium.getDust(10),
                Materials.Indium.getDust(5))
            .circuit(5)
            .fluidOutputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1 * STACKS + 36 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(40 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Germanium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sphalerite, 1),
                Materials.Carbon.getDust(4))
            .circuit(15)
            .fluidInputs(Materials.SulfuricAcid.getFluid(250))
            .fluidOutputs(MaterialsElements.getInstance().GERMANIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        // Rhenium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Scheelite, 1),
                Materials.Carbon.getDust(4))
            .circuit(20)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_250))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * EIGHTH_INGOTS))
            .eut(4_000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Molybdenite, 1),
                Materials.Carbon.getDust(8))
            .circuit(20)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_875))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Molybdenum, 1),
                Materials.Carbon.getDust(4))
            .circuit(20)
            .fluidInputs(Materials.SulfuricAcid.getFluid(625))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        // Thallium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Zinc, 3),
                GTOreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pyrite, 4),
                Materials.Carbon.getDust(16))
            .circuit(21)
            .fluidInputs(Materials.SulfuricAcid.getFluid(1_250))
            .fluidOutputs(MaterialsElements.getInstance().THALLIUM.getFluidStack(2 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Strontium processing
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialMisc.STRONTIUM_OXIDE.getDust(8), Materials.Aluminium.getDust(8))
            .circuit(21)
            .fluidOutputs(
                Materials.Oxygen.getGas(8_000),
                Materials.Aluminium.getMolten(8 * INGOTS),
                Materials.Strontium.getMolten(8 * INGOTS))
            .eut(TierEU.RECIPE_EV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Botmium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.NITINOL_60.getDust(1),
                Materials.Osmium.getDust(6),
                WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 6),
                MaterialsElements.getInstance().THALLIUM.getDust(3))
            .circuit(4)
            .fluidOutputs(MaterialsAlloy.BOTMIUM.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_UV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Lossless Phonon Transfer Medium
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.dust, 5),
                GGMaterial.metastableOganesson.get(OrePrefixes.dust, 1),
                Materials.Praseodymium.getDust(15),
                Materials.SuperconductorUIVBase.getDust(6))
            .circuit(5)
            .fluidInputs(Materials.PhononCrystalSolution.getFluid(4_000))
            .fluidOutputs(Materials.PhononMedium.getFluid(1_000))
            .eut(TierEU.RECIPE_UIV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Proto-Halkonite Steel Base
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.TranscendentMetal.getDust(2),
                GGMaterial.tairitsu.get(OrePrefixes.dust, 2),
                Materials.Tartarite.getDust(2),
                MaterialsAlloy.TITANSTEEL.getDust(1),
                Materials.Infinity.getDust(1))
            .circuit(5)
            .fluidInputs(Materials.DTR.getFluid(1_000))
            .fluidOutputs(Materials.MoltenProtoHalkoniteBase.getFluid(8 * INGOTS))
            .eut(TierEU.RECIPE_UEV)
            .duration(60 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Incoloy-903
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Iron.getDust(12),
                Materials.Nickel.getDust(10),
                Materials.Cobalt.getDust(8),
                Materials.Titanium.getDust(4),
                Materials.Molybdenum.getDust(2),
                Materials.Aluminium.getDust(1))
            .circuit(6)
            .fluidOutputs(GGMaterial.incoloy903.getMolten(37 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);
    }

    private static void dehydratorRecipes() {
        ItemStack cropGrape = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cropGrape", 1);
        ItemStack foodRaisins = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("foodRaisins", 1);

        if (cropGrape != null && foodRaisins != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(cropGrape)
                .itemOutputs(foodRaisins)
                .eut(2)
                .duration(10 * TICKS)
                .addTo(chemicalDehydratorRecipes);
        }

        // Process Waste Water
        GTValues.RA.stdBuilder()
            .itemOutputs(
                new ItemStack(Blocks.dirt),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gold, 1))
            .outputChances(20_00, 5_00, 10, 7, 6, 5, 4, 3, 2)
            .fluidInputs(FluidRegistry.getFluidStack("sludge", 1_000))
            .fluidOutputs(Materials.Methane.getGas(100))
            .eut(TierEU.RECIPE_HV)
            .duration(2 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Ethylbenzene
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(3))
            .circuit(17)
            .itemOutputs(Materials.Styrene.getCells(1), Materials.Hydrogen.getCells(2))
            .fluidInputs(new FluidStack(GTPPFluids.Ethylbenzene, 1_000))
            .eut(TierEU.RECIPE_LV)
            .duration(3 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        /*
         * Add custom recipes for drying leather
         */
        if (Backpack.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Items.leather, 2))
                .circuit(18)
                .itemOutputs(getModItem(Backpack.ID, "tannedLeather", 1))
                .fluidInputs(new FluidStack(GTPPFluids.Ethylbenzene, 1_000))
                .eut(180)
                .duration(5 * SECONDS)
                .addTo(chemicalDehydratorRecipes);

            if (NewHorizonsCoreMod.isModLoaded()) {
                GTValues.RA.stdBuilder()
                    .itemInputs(getModItem(NewHorizonsCoreMod.ID, "ArtificialLeather", 2L, 0))
                    .circuit(18)
                    .itemOutputs(getModItem(Backpack.ID, "tannedLeather", 1))
                    .fluidInputs(new FluidStack(GTPPFluids.Ethylbenzene, 1_000))
                    .eut(180)
                    .duration(5 * SECONDS)
                    .addTo(chemicalDehydratorRecipes);
            }
        }

        // Alternative Acetic Anhydride recipe for Kevlar Line
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(1))
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(Materials.AceticAcid.getFluid(2_000))
            .fluidOutputs(MaterialMisc.ACETIC_ANHYDRIDE.getFluidStack(1_000))
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(chemicalDehydratorRecipes);
    }

    private static void largeChemReactorRecipes() {
        // Styrene
        GTValues.RA.stdBuilder()
            .circuit(24)
            .itemOutputs()
            .fluidInputs(new FluidStack(GTPPFluids.Ethylbenzene, 1_000))
            .fluidOutputs(Materials.Styrene.getFluid(1_000), Materials.Hydrogen.getGas(2_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void assemblerRecipes() {
        // Half Complete Casing I
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Vanadium, 32),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.VanadiumSteel, 8))
            .itemOutputs(GregtechItemList.HalfCompleteCasing_I.get(4))
            .fluidInputs(Materials.Oxygen.getGas(8_000))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);

        // Half Complete Casing II
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.HalfCompleteCasing_I.get(2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.VanadiumGallium, 8))
            .itemOutputs(GregtechItemList.HalfCompleteCasing_II.get(8))
            .fluidInputs(Materials.Tantalum.getMolten(4 * INGOTS))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Mining Explosives
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(2, Ic2Items.industrialTnt),
                new ItemStack(Blocks.tnt, 4),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iron, 1))
            .itemOutputs(GregtechItemList.MiningExplosives.get(3))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);

        // Wither Cage
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 8),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.BlackSteel, 8))
            .itemOutputs(GregtechItemList.WitherGuard.get(64))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Hand Pump Trade Token I
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_LV.get(1),
                ItemList.Electric_Motor_LV.get(1),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Aluminium, 8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Brass, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Brass, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 2))
            .itemOutputs(GregtechItemList.HandPumpToken_I.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Hand Pump Trade Token II
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_MV.get(1),
                ItemList.Electric_Motor_MV.get(1),
                MaterialsElements.STANDALONE.BLACK_METAL.getBolt(8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Invar, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Invar, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Aluminium, 2))
            .itemOutputs(GregtechItemList.HandPumpToken_II.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Hand Pump Trade Token III
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_HV.get(1),
                ItemList.Electric_Motor_HV.get(1),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Titanium, 8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Chrome, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Chrome, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 2))
            .itemOutputs(GregtechItemList.HandPumpToken_III.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Hand Pump Trade Token IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_EV.get(1),
                ItemList.Electric_Motor_EV.get(1),
                MaterialsAlloy.HASTELLOY_N.getBolt(8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Titanium, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2))
            .itemOutputs(GregtechItemList.HandPumpToken_IV.get(1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Simple Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.HandPumpToken_I.get(1))
            .circuit(20)
            .itemOutputs(GregtechItemList.SimpleHandPump.get(1))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Advanced Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.HandPumpToken_II.get(1))
            .circuit(20)
            .itemOutputs(GregtechItemList.AdvancedHandPump.get(1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Super Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.HandPumpToken_III.get(1))
            .circuit(20)
            .itemOutputs(GregtechItemList.SuperHandPump.get(1))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Ultimate Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.HandPumpToken_IV.get(1))
            .circuit(20)
            .itemOutputs(GregtechItemList.UltimateHandPump.get(1))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Low tier Charge Packs

        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TUMBAGA.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Aluminium, 12),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Cobalt, 6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 4),
                ItemList.Battery_RE_LV_Lithium.get(4),
                ItemList.Sensor_LV.get(4))
            .itemOutputs(GregtechItemList.ChargePack_LV.get(1))
            .fluidInputs(MaterialsAlloy.TUMBAGA.getFluidStack(4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // MV
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.EGLIN_STEEL.getPlate(8),
                MaterialsElements.STANDALONE.BLACK_METAL.getRing(12),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.AnnealedCopper, 6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 4),
                ItemList.Battery_RE_MV_Lithium.get(4),
                ItemList.Sensor_MV.get(4))
            .itemOutputs(GregtechItemList.ChargePack_MV.get(1))
            .fluidInputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // HV
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TANTALUM_CARBIDE.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Titanium, 12),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Gold, 6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 4),
                ItemList.Battery_RE_HV_Lithium.get(4),
                ItemList.Sensor_HV.get(4))
            .itemOutputs(GregtechItemList.ChargePack_HV.get(1))
            .fluidInputs(MaterialsAlloy.TANTALUM_CARBIDE.getFluidStack(4 * INGOTS))
            .duration(90 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // EV
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.INCOLOY_DS.getPlate(8),
                MaterialsAlloy.HASTELLOY_N.getRing(12),
                GTOreDictUnificator.get(OrePrefixes.wireGt12, Materials.Titanium, 6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4),
                GregtechItemList.Battery_RE_EV_Lithium.get(4),
                ItemList.Sensor_EV.get(4))
            .itemOutputs(GregtechItemList.ChargePack_EV.get(1))
            .fluidInputs(MaterialsAlloy.INCOLOY_DS.getFluidStack(4 * INGOTS))
            .duration(120 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // IV
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.INCONEL_625.getPlate(8),
                MaterialsAlloy.ENERGYCRYSTAL.getRing(12),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Nichrome, 6),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 4),
                ItemList.Energy_LapotronicOrb.get(4),
                ItemList.Sensor_IV.get(4))
            .itemOutputs(GregtechItemList.ChargePack_IV.get(1))
            .fluidInputs(MaterialsAlloy.INCONEL_625.getFluidStack(4 * INGOTS))
            .duration(150 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Research on Turbine Automation
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Trinium, 64),
                ItemList.Sensor_LuV.get(6),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(64),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 64),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 12))
            .circuit(17)
            .itemOutputs(ItemDummyResearch.getResearchStack(ASSEMBLY_LINE_RESEARCH.RESEARCH_8_TURBINE_AUTOMATION, 1))
            .fluidInputs(MaterialsAlloy.CINOBITE.getFluidStack(32 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }

    private static void distilleryRecipes() {
        GTValues.RA.stdBuilder()
            .circuit(4)
            .fluidInputs(Materials.Air.getGas(1_000))
            .fluidOutputs(Materials.Helium.getGas(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Air.getGas(20_000))
            .fluidOutputs(Materials.Helium.getGas(25))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distillationTowerRecipes);

        // Apatite Distillation
        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.SulfuricApatiteMix, 5_200))
            .fluidOutputs(
                new FluidStack(GTPPFluids.SulfurousAcid, 3_800),
                new FluidStack(GTPPFluids.IndustrialStrengthHydrogenChloride, 1_000),
                new FluidStack(GTPPFluids.IndustrialStrengthHydrofluoricAcid, 400))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(new FluidStack(GTPPFluids.SulfurousAcid, 1_000))
            .fluidOutputs(Materials.SulfurDioxide.getGas(500), Materials.Water.getFluid(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.AlgaeBiomass.get(32))
            .itemOutputs(GregtechItemList.GreenAlgaeBiomass.get(4))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(distilleryRecipes);
    }

    private static void thermalBoilerRecipes() {
        // Lava
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Lava.getFluid(1_000), Materials.Water.getFluid(16_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(GTModHandler.getPahoehoeLava(1_000), Materials.Steam.getGas(16_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Silver, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tantalum, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 1),
                new ItemStack(Blocks.obsidian, 1))
            .outputChances(444, 222, 56, 56, 56, 125, 1000)
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);

        // Pahoehoe Lava
        GTValues.RA.stdBuilder()
            .fluidInputs(
                GTModHandler.getPahoehoeLava(1_000),
                Materials.Water.getFluid(16_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(Materials.Steam.getGas(16_000))
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Electrum, 1),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Tantalum, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Tungstate, 1),
                new ItemStack(Blocks.obsidian, 1))
            .outputChances(167, 56, 56, 125, 3700)
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);

        // Hot Coolant
        GTValues.RA.stdBuilder()
            .fluidInputs(GTModHandler.getHotCoolant(500), Materials.Water.getFluid(100_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(GTModHandler.getIC2Coolant(500), GTModHandler.getSuperHeatedSteam(100_000))
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);

        // Solar Salt (Hot)
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialMisc.SOLAR_SALT_HOT.getFluidStack(100),
                Materials.Water.getFluid(100_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(MaterialMisc.SOLAR_SALT_COLD.getFluidStack(100), GTModHandler.getSuperHeatedSteam(100_000))
            .duration(1 * SECONDS)
            .eut(0)
            .addTo(thermalBoilerRecipes);
    }

    private static void addFuels() {
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.lava_bucket))
            .metadata(FUEL_VALUE, 32)
            .metadata(FUEL_TYPE, 2)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(2, Ic2Items.lavaCell))
            .metadata(FUEL_VALUE, 32)
            .metadata(FUEL_TYPE, 2)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(Ic2Items.pahoehoelavaCell.copy())
            .metadata(FUEL_VALUE, 24)
            .metadata(FUEL_TYPE, 2)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);
    }

    private static void extractorRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Battery_RE_EV_Sodium.get(1))
            .itemOutputs(ItemList.Battery_Hull_HV.get(4))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Battery_RE_EV_Cadmium.get(1))
            .itemOutputs(ItemList.Battery_Hull_HV.get(4))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.Battery_RE_EV_Lithium.get(1))
            .itemOutputs(ItemList.Battery_Hull_HV.get(4))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(extractorRecipes);
    }

    private static void fluidExtractorRecipes() {
        // Gelid Cryotheum
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Cryotheum, 1))
            .fluidOutputs(new FluidStack(TFFluids.fluidCryotheum, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidExtractionRecipes);

        // Blazing Pyrotheum
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Pyrotheum, 1))
            .fluidOutputs(new FluidStack(TFFluids.fluidPyrotheum, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidExtractionRecipes);

        // Forestry fertilizer to GT++ liquid Fertilizer
        if (Forestry.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemList.FR_Fertilizer.get(1))
                .fluidOutputs(new FluidStack(GTPPFluids.BasicFertilizer, 36))
                .duration(5 * TICKS)
                .eut(16)
                .addTo(fluidExtractionRecipes);
        }
    }

    private static void centrifugeRecipes() {
        // Solar Salt (Cold) processing
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialMisc.SOLAR_SALT_COLD.getCell(1))
            .circuit(2)
            .itemOutputs(
                MaterialMisc.SODIUM_NITRATE.getDust(3),
                MaterialMisc.POTASSIUM_NITRATE.getDust(2),
                ItemList.Cell_Empty.get(1))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(centrifugeRecipes);
    }

    private static void mixerRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Cell_Empty.get(1),
                MaterialMisc.SODIUM_NITRATE.getDust(3),
                MaterialMisc.POTASSIUM_NITRATE.getDust(2))
            .circuit(2)
            .itemOutputs(MaterialMisc.SOLAR_SALT_COLD.getCell(1))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Titanium.getDust(9),
                Materials.Carbon.getDust(9),
                Materials.Potassium.getDust(9),
                Materials.Lithium.getDust(9),
                Materials.Sulfur.getDust(9))
            .circuit(2)
            .itemOutputs(MaterialsAlloy.LEAGRISIUM.getDust(50))
            .fluidInputs(Materials.Hydrogen.getGas(5_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Steel.getDust(16),
                Materials.Molybdenum.getDust(1),
                Materials.Titanium.getDust(1),
                Materials.Nickel.getDust(4),
                Materials.Cobalt.getDust(2))
            .circuit(2)
            .itemOutputs(MaterialsAlloy.MARAGING250.getDust(24))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);
    }

    private static void chemicalReactorRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 32))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 2))
            .fluidInputs(Materials.SulfuricAcid.getFluid(4_000))
            .fluidOutputs(new FluidStack(GTPPFluids.SulfuricApatiteMix, 8_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // KOH + HNO3 = KNO3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PotassiumHydroxide.get(3))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.PotassiumNitrade, 5))
            .fluidInputs(Materials.NitricAcid.getFluid(1_000))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Na2CO3 + 2HNO3 = 2NaNO3 + CO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SodiumCarbonate.getDust(6))
            .circuit(1)
            .itemOutputs(MaterialMisc.SODIUM_NITRATE.getDust(10))
            .fluidInputs(Materials.NitricAcid.getFluid(2_000))
            .fluidOutputs(Materials.CarbonDioxide.getGas(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);
    }

    private static void blastFurnaceRecipes() {
        // Synthetic Graphite
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsAlloy.SILICON_CARBIDE.getDust(2))
            .circuit(22)
            .itemOutputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Graphite, 1),
                GTOreDictUnificator.get(OrePrefixes.dustSmall, Materials.Silicon, 1))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .metadata(ADDITIVE_AMOUNT, 500)
            .addTo(BlastFurnaceWithGas);
    }

    private static void compressorRecipes() {
        // Clay Plate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Clay, 1))
            .duration(15 * SECONDS)
            .eut(2)
            .addTo(compressorRecipes);

        // Hypervisor Matrix (Fusion)
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.FusionComputer_UV.get(9))
            .itemOutputs(GregtechItemList.Compressed_Fusion_Reactor.get(1))
            .duration(1 * HOURS + 50 * MINUTES)
            .eut(TierEU.RECIPE_UV)
            .addTo(compressorRecipes);
    }

    private static void macerationRecipes() {
        if (getModItem(Mods.Chisel.ID, "limestone", 1, 0) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(ItemUtils.getItemStackOfAmountFromOreDict("limestone", 1))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 4))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
    }

    private static void cyclotronRecipes() {
        // Polonium
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_PO210.get(1))
            .outputChances(100)
            .fluidInputs(Materials.Bismuth.getMolten(1))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(cyclotronRecipes);

        // Americium
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_AM241.get(4))
            .outputChances(2500)
            .fluidInputs(Materials.Americium.getMolten(1))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Strontium u235
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .outputChances(570)
            .fluidInputs(Materials.Uranium235.getMolten(10))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Strontium u233
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .outputChances(660)
            .fluidInputs(MaterialsElements.getInstance().URANIUM233.getFluidStack(10))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Strontium pu239
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .outputChances(220)
            .fluidInputs(Materials.Plutonium.getMolten(10))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Plutonium
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_PU238.get(2))
            .outputChances(780)
            .fluidInputs(MaterialsElements.getInstance().PLUTONIUM238.getFluidStack(1))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Neptunium
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().URANIUM238.getDust(1))
            .itemOutputs(GregtechItemList.Neptunium238Dust.get(1))
            .outputChances(500)
            .fluidInputs(Materials.Deuterium.getGas(400))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(cyclotronRecipes);

        /*
         * Particle Science
         */

        // Quark Smash
        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(
                Particle.getBaseParticle(Particle.UP),
                Particle.getBaseParticle(Particle.DOWN),
                Particle.getBaseParticle(Particle.CHARM),
                Particle.getBaseParticle(Particle.STRANGE),
                Particle.getBaseParticle(Particle.TOP),
                Particle.getBaseParticle(Particle.BOTTOM))
            .outputChances(100, 100, 100, 100, 100, 100)
            .fluidInputs(Materials.Hydrogen.getPlasma(100))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);
        // Lepton Smash
        GTValues.RA.stdBuilder()
            .circuit(6)
            .itemOutputs(
                Particle.getBaseParticle(Particle.ELECTRON),
                Particle.getBaseParticle(Particle.MUON),
                Particle.getBaseParticle(Particle.TAU),
                Particle.getBaseParticle(Particle.ELECTRON_NEUTRINO),
                Particle.getBaseParticle(Particle.MUON_NEUTRINO),
                Particle.getBaseParticle(Particle.TAU_NEUTRINO))
            .outputChances(600, 40, 20, 15, 10, 5)
            .fluidInputs(Materials.Helium.getPlasma(1_500))
            .duration(40 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);
        // Boson Smash
        GTValues.RA.stdBuilder()
            .circuit(9)
            .itemOutputs(
                Particle.getBaseParticle(Particle.GLUON),
                Particle.getBaseParticle(Particle.PHOTON),
                Particle.getBaseParticle(Particle.Z_BOSON),
                Particle.getBaseParticle(Particle.W_BOSON),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .outputChances(160, 260, 150, 150, 1)
            .fluidInputs(Materials.Helium.getPlasma(1_500))
            .duration(30 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);
        // Mixed Smash 1
        GTValues.RA.stdBuilder()
            .circuit(12)
            .itemOutputs(
                Particle.getBaseParticle(Particle.GRAVITON),
                Particle.getBaseParticle(Particle.ETA_MESON),
                Particle.getBaseParticle(Particle.PION),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.NEUTRON),
                Particle.getBaseParticle(Particle.LAMBDA),
                Particle.getBaseParticle(Particle.OMEGA),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .outputChances(50, 50, 50, 40, 30, 20, 20, 10)
            .fluidInputs(Materials.Americium.getPlasma(2_500))
            .duration(16 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(cyclotronRecipes);
        // Mixed Smash 1
        GTValues.RA.stdBuilder()
            .circuit(12)
            .itemOutputs(
                Particle.getBaseParticle(Particle.GRAVITON),
                Particle.getBaseParticle(Particle.ETA_MESON),
                Particle.getBaseParticle(Particle.PION),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.NEUTRON),
                Particle.getBaseParticle(Particle.LAMBDA),
                Particle.getBaseParticle(Particle.OMEGA),
                Particle.getBaseParticle(Particle.HIGGS_BOSON))
            .outputChances(5000, 200, 200, 100, 80, 60, 40, 30)
            .fluidInputs(new FluidStack(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getPlasma(), 2_500))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(cyclotronRecipes);
        // Graviton Smash
        GTValues.RA.stdBuilder()
            .circuit(15)
            .itemOutputs(Particle.getBaseParticle(Particle.GRAVITON), Particle.getBaseParticle(Particle.UNKNOWN))
            .outputChances(1000, 100)
            .fluidInputs(Materials.Hydrogen.getPlasma(100))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);

        // Quantum Anomaly
        GTValues.RA.stdBuilder()
            .itemInputs(Particle.getBaseParticle(Particle.UNKNOWN))
            .circuit(24)
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1))
            .outputChances(100)
            .fluidInputs(Materials.Duranium.getMolten(40))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(cyclotronRecipes);

        /*
         * Ions
         */

        int IonCount = 2;
        int tenCountA = 2;
        int tenCountB = 0;
        for (String y : IonParticles.MetaToNameMap.values()) {
            if (y.toLowerCase()
                .contains("hydrogen")) {
                continue;
            }
            FluidStack plasmaFromName = FluidRegistry.getFluidStack("plasma." + y.toLowerCase(), 1_000);

            Materials particleMaterial = MaterialUtils.getMaterial(y);
            FluidStack recipePlasma = particleMaterial != null ? particleMaterial.getPlasma(1_000) : plasmaFromName;

            // Ionize Plasma
            if (recipePlasma != null && !recipePlasma.isFluidEqual(Materials._NULL.getPlasma(1))) {
                GTValues.RA.stdBuilder()
                    .circuit(1 + (tenCountA - 1))
                    .itemOutputs(
                        Particle.getIon(y, 1),
                        Particle.getIon(y, 2),
                        Particle.getIon(y, 3),
                        Particle.getIon(y, -1),
                        Particle.getIon(y, -2),
                        Particle.getIon(y, -3),
                        Particle.getIon(y, 1),
                        Particle.getIon(y, 2),
                        Particle.getIon(y, -1))
                    .outputChances(275, 250, 225, 275, 250, 225, 275, 250, 275)
                    .fluidInputs(recipePlasma)
                    .duration(20 * SECONDS * (IonCount++) * tenCountA)
                    .eut(TierEU.RECIPE_ZPM)
                    .addTo(cyclotronRecipes);
            }

            if (tenCountB == 12) {
                tenCountB = 0;
                tenCountA++;
            } else {
                tenCountB++;
            }
        }

        // Generate Hydrogen Ion Recipe
        GTValues.RA.stdBuilder()
            .circuit(24)
            .itemOutputs(
                Particle.getIon("Hydrogen", 1),
                Particle.getIon("Hydrogen", 2),
                Particle.getIon("Hydrogen", 3),
                Particle.getIon("Hydrogen", 1),
                Particle.getIon("Hydrogen", 2),
                Particle.getIon("Hydrogen", 3),
                Particle.getIon("Hydrogen", -1),
                Particle.getIon("Hydrogen", -2),
                Particle.getIon("Hydrogen", -3))
            .outputChances(500, 500, 500, 500, 500, 500, 500, 500, 500)
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);

        // Generate Hydrogen Plasma Recipe
        GTValues.RA.stdBuilder()
            .itemInputs(Particle.getIon("Hydrogen", 0))
            .circuit(21)
            .itemOutputs(
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.NEUTRON),
                Particle.getBaseParticle(Particle.ELECTRON),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN))
            .outputChances(1250, 1250, 1250, 750, 750, 750)
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .fluidOutputs(Materials.Hydrogen.getPlasma(100))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);

        // Generate Protons Easily
        GTValues.RA.stdBuilder()
            .itemInputs(Particle.getIon("Hydrogen", 0))
            .circuit(20)
            .itemOutputs(
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON))
            .outputChances(750, 750, 750, 750, 750, 750, 750, 750, 750)
            .fluidInputs(Materials.Hydrogen.getGas(100))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Particle.getBaseParticle(Particle.UNKNOWN))
            .circuit(22)
            .itemOutputs(
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON),
                Particle.getBaseParticle(Particle.PROTON))
            .outputChances(375, 375, 375, 375, 375, 375, 375, 375, 375)
            .fluidInputs(Materials.Hydrogen.getGas(100))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);

        // Create Strange Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsElements.getInstance().PLUTONIUM238.getDust(1),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN),
                Particle.getBaseParticle(Particle.UNKNOWN))
            .itemOutputs(MaterialsOres.DEEP_EARTH_REACTOR_FUEL_DEPOSIT.getDust(1))
            .outputChances(2500)
            .fluidInputs(new FluidStack(TFFluids.fluidEnder, 1_000))
            .duration(15 * MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(cyclotronRecipes);
    }

    private static void craftingTableRecipes() {
        List<ItemList> tankList = Arrays.asList(
            ItemList.Super_Tank_LV,
            ItemList.Super_Tank_MV,
            ItemList.Super_Tank_HV,
            ItemList.Super_Tank_EV,
            ItemList.Super_Tank_IV,
            ItemList.Quantum_Tank_LV,
            ItemList.Quantum_Tank_MV,
            ItemList.Quantum_Tank_HV,
            ItemList.Quantum_Tank_EV,
            ItemList.Quantum_Tank_IV);

        for (int i = 0; i < 10; i++) {
            ItemStack tank = tankList.get(i)
                .get(1);
            ItemStack handPump = GregtechItemList.ExpandableHandPump.get(1);
            ItemStack pumpWithNBT = handPump.copy();
            NBTTagCompound nbt = new NBTTagCompound();
            int capacity = i == 9 ? Integer.MAX_VALUE : 4_000_000 * (int) GTUtility.powInt(2, i);
            nbt.setInteger("mMeta", 4);
            nbt.setBoolean("mInit", true);
            nbt.setString("mFluid", "@@@@@");
            nbt.setInteger("mFluidAmount", 0);
            nbt.setInteger("mCapacity", capacity);
            nbt.setBoolean("capacityInit", true);
            pumpWithNBT.setTagCompound(nbt);
            GTModHandler.addShapelessCraftingRecipe(pumpWithNBT, new Object[] { handPump, tank });
        }
    }
}
