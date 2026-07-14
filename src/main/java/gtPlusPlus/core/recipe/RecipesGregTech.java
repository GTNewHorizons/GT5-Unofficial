package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.Backpack;
import static gregtech.api.enums.Mods.Baubles;
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

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gregtech.loaders.postload.chains.AcidRecipes;
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
        AcidRecipes.run();

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
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.fluidGas, (int) (4_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.fluidGas, (int) (4_000)),
                Materials.Water.getFluid(2_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Advanced recipe for Fluorine Production
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PurpleMetalCatalyst.get(0),
                new ItemStack(Blocks.sand, 64),
                new ItemStack(Blocks.sand, 64))
            .circuit(17)
            .itemOutputs(MaterialsFluorides.FLUORITE.getRawOre(10))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (5_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.fluidGas, (int) (12_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        // Advanced recipe for Fluorine Production
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.PurpleMetalCatalyst.get(0),
                new ItemStack(Blocks.sandstone, 64),
                new ItemStack(Blocks.sandstone, 64))
            .circuit(17)
            .itemOutputs(MaterialsFluorides.FLUORITE.getRawOre(20))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (4_000)),
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.fluidGas, (int) (8_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .metadata(CHEMPLANT_CASING_TIER, 5)
            .addTo(chemicalPlantRecipes);

        // 3NO2 + H2O = 2HNO3 + NO
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PinkMetalCatalyst.get(0))
            .circuit(16)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.fluidGas, (int) (3_000)),
                GTModHandler.getDistilledWater(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricOxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 2)
            .addTo(chemicalPlantRecipes);

        // Produce Boric Acid
        // Na2B4O7·10H2O + 2HCl = 4B(OH)3 + 2NaCl + 5H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Borax, Materials2Shapes.dust, (int) (23)))
            .circuit(21)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.dust, (int) (4)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 2_000))
            .fluidOutputs(new FluidStack(GTPPFluids.BoricAcid, 4_000), Materials.Water.getFluid(5_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(CHEMPLANT_CASING_TIER, 3)
            .addTo(chemicalPlantRecipes);

        // Produce Th232
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.dust, (int) (4)))
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
            .eut(TierEU.RECIPE_MV / 2)
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
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.HydrofluoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 5_000))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Infinity, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)),
                MaterialsAlloy.QUANTUM.getFluidStack(2 * INGOTS))
            .fluidOutputs(MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(1 * INGOTS))
            .duration(25 * SECONDS + 12 * TICKS)
            .eut(TierEU.RECIPE_UV)
            .metadata(FUSION_THRESHOLD, 2_000_000_000L)
            .addTo(fusionRecipes);

        // Rhugnor Mk5
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.QuarkGluonPlasma,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1 * HALF_INGOTS)),
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
         * Gem Battery Recipes
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
                ItemList.StableBosonContainmentUnit.get(2),
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
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SolderingAlloy,
                    Materials2FluidShapes.fluidMolten,
                    (int) (10 * INGOTS)))
            .itemOutputs(GregtechItemList.Mega_AlloyBlastSmelter.get(1))
            .eut(TierEU.RECIPE_UHV / 2)
            .duration(1 * MINUTES)
            .addTo(AssemblyLine);

        // Expandable Hand Pump
        GTValues.RA.stdBuilder()
            .metadata(RESEARCH_ITEM, GregtechItemList.UltimateHandPump.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 30 * SECONDS, TierEU.RECIPE_IV))
            .itemInputs(
                ItemList.Electric_Pump_LuV.get(4),
                ItemList.Electric_Motor_LuV.get(4),
                GregtechItemList.VOLUMETRIC_FLASK_32k.get(4),
                MaterialsAlloy.LAFIUM.getScrew(16),
                WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.ring, 8),
                WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.stick, 16),
                MaterialLibAPI.getStack(Materials2Materials.Osmiridium, Materials2Shapes.plate, (int) (32)))
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
                MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.wireFine, (int) (32)),
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
                MaterialLibAPI.getStack(Materials2Materials.Tungsten, Materials2Shapes.dust, (int) (6)),
                GregtechItemList.Laser_Lens_Special.get(0))
            .itemOutputs(MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getDust(1))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_UEV)
            .addTo(laserEngraverRecipes);

        // Astral Titanium Dust
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, (int) (8)),
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
                MaterialLibAPI.getStack(Materials2Materials.Glass, Materials2Shapes.dust, (int) (64)),
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
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Biomass, Materials2FluidShapes.fluidLiquid, (int) (100)))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Honey, Materials2FluidShapes.fluidLiquid, (int) (100)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Biomass, Materials2FluidShapes.fluidLiquid, (int) (100)))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(BOPBlockRegistrator.sapling_Rainforest))
            .fluidInputs(FluidRegistry.getFluidStack("juice", 100))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Biomass, Materials2FluidShapes.fluidLiquid, (int) (100)))
            .duration(1 * MINUTES)
            .eut(3)
            .addTo(brewingRecipes);
    }

    private static void electrolyzerRecipes() {
        // Radium -> Radon electrolysis
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.DecayedRadium226Dust.get(1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidGas, (int) (1 * INGOTS)))
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
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Thaumium, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.EnderPearl, Materials2Shapes.dust, (int) (2)))
            .circuit(5)
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Enderium, Materials2FluidShapes.fluidMolten, (int) (8 * INGOTS)))
            .eut(TierEU.RECIPE_EV)
            .duration(20 * SECONDS + 1 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Signalium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.AnnealedCopper, Materials2Shapes.dust, (int) (30)),
                MaterialLibAPI.getStack(Materials2Materials.Ardite, Materials2Shapes.dust, (int) (10)),
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.dust, (int) (50)))
            .circuit(3)
            .fluidOutputs(GGMaterial.signalium.getMolten(5 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Lumiium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, (int) (10)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (10)),
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, (int) (8)),
                GGMaterial.lumiinessence.get(OrePrefixes.dust, 10),
                MaterialLibAPI.getStack(Materials2Materials.Glowstone, Materials2Shapes.dust, (int) (10)))
            .circuit(6)
            .fluidOutputs(GGMaterial.lumiium.getMolten(5 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(5 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Eglin Steel
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Kanthal, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.dust, (int) (5)),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.dust, (int) (4)))
            .circuit(6)
            .fluidOutputs(MaterialsAlloy.EGLIN_STEEL.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_MV)
            .duration(45 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // HG1223
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Barium, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, (int) (3)))
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (8_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Mercury, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialsAlloy.HG1223.getFluidStack(16 * INGOTS))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Nitinol 60
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (2)))
            .circuit(2)
            .fluidOutputs(MaterialsAlloy.NITINOL_60.getFluidStack(5 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Indalloy 140
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Bismuth, Materials2Shapes.dust, (int) (47)),
                MaterialLibAPI.getStack(Materials2Materials.Lead, Materials2Shapes.dust, (int) (25)),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, (int) (13)),
                MaterialLibAPI.getStack(Materials2Materials.Cadmium, Materials2Shapes.dust, (int) (10)),
                MaterialLibAPI.getStack(Materials2Materials.Indium, Materials2Shapes.dust, (int) (5)))
            .circuit(5)
            .fluidOutputs(MaterialsAlloy.INDALLOY_140.getFluidStack(1 * STACKS + 36 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(40 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Germanium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Sphalerite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (4)))
            .circuit(15)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (250)))
            .fluidOutputs(MaterialsElements.getInstance().GERMANIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        // Rhenium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Scheelite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (4)))
            .circuit(20)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_250)))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * EIGHTH_INGOTS))
            .eut(4_000)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Molybdenite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (8)))
            .circuit(20)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_875)))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Molybdenum, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (4)))
            .circuit(20)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (625)))
            .fluidOutputs(MaterialsElements.getInstance().RHENIUM.getFluidStack(1 * QUARTER_INGOTS))
            .eut(4_000)
            .duration(37 * SECONDS + 10 * TICKS)
            .addTo(alloyBlastSmelterRecipes);

        // Thallium Roasting
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.crushedPurified, (int) (3)),
                MaterialLibAPI.getStack(Materials2Materials.Pyrite, Materials2Shapes.crushedPurified, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (16)))
            .circuit(21)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_250)))
            .fluidOutputs(MaterialsElements.getInstance().THALLIUM.getFluidStack(2 * INGOTS))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * MINUTES + 15 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Strontium processing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialMisc.STRONTIUM_OXIDE.getDust(8),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, (int) (8)))
            .circuit(21)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (8_000)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Aluminium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Strontium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (8 * INGOTS)))
            .eut(TierEU.RECIPE_EV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Botmium
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.NITINOL_60.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.dust, (int) (6)),
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
                MaterialLibAPI.getStack(Materials2Materials.Praseodymium, Materials2Shapes.dust, (int) (15)),
                MaterialLibAPI.getStack(Materials2Materials.SuperconductorUIVBase, Materials2Shapes.dust, (int) (6)))
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhononCrystalSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.PhononMedium, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UIV)
            .duration(2 * MINUTES)
            .addTo(alloyBlastSmelterRecipes);

        // Proto-Halkonite Steel Base
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.TranscendentMetal, Materials2Shapes.dust, (int) (2)),
                GGMaterial.tairitsu.get(OrePrefixes.dust, 2),
                MaterialLibAPI.getStack(Materials2Materials.Tartarite, Materials2Shapes.dust, (int) (2)),
                MaterialsAlloy.TITANSTEEL.getDust(1),
                MaterialLibAPI.getStack(Materials2Materials.Infinity, Materials2Shapes.dust, (int) (1)))
            .circuit(5)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DimensionallyTranscendentResidue,
                    Materials2FluidShapes.fluidLiquid,
                    1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.protohalkonitebase,
                    Materials2FluidShapes.fluidLiquid,
                    8 * INGOTS))
            .eut(TierEU.RECIPE_UEV)
            .duration(60 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Computation Base
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Uraniumtriplatinid, Materials2Shapes.dust, 61),
                MaterialLibAPI.getStack(Materials2Materials.Vanadiumtriindinid, Materials2Shapes.dust, 59),
                MaterialLibAPI.getStack(
                    Materials2Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
                    Materials2Shapes.dust,
                    53),
                MaterialLibAPI
                    .getStack(Materials2Materials.Tetranaquadahdiindiumhexaplatiumosminid, Materials2Shapes.dust, 47),
                MaterialLibAPI
                    .getStack(Materials2Materials.Longasssuperconductornameforuvwire, Materials2Shapes.dust, 43),
                MaterialLibAPI
                    .getStack(Materials2Materials.Longasssuperconductornameforuhvwire, Materials2Shapes.dust, 41),
                MaterialLibAPI.getStack(Materials2Materials.SuperconductorUEVBase, Materials2Shapes.dust, (int) (37)),
                MaterialLibAPI.getStack(Materials2Materials.SuperconductorUIVBase, Materials2Shapes.dust, (int) (31)),
                MaterialLibAPI.getStack(Materials2Materials.SuperconductorUMVBase, Materials2Shapes.dust, (int) (29)))
            .fluidInputs(
                MaterialsAlloy.INDALLOY_140.getFluidStack(1_000_000),
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(100_000),
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.BoundlessCosmicSolder,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ComputationBase,
                    Materials2FluidShapes.fluidMolten,
                    (int) (100_000)))
            .eut(TierEU.RECIPE_UXV)
            .duration(600 * SECONDS)
            .addTo(alloyBlastSmelterRecipes);

        // Incoloy-903
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (12)),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (10)),
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.dust, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Molybdenum, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, (int) (1)))
            .circuit(6)
            .fluidOutputs(GGMaterial.incoloy903.getMolten(37 * INGOTS))
            .eut(TierEU.RECIPE_EV)
            .duration(2 * MINUTES)
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
                MaterialLibAPI.getStack(Materials2Materials.Clay, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Iron, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Gold, Materials2Shapes.dust, (int) (1)))
            .outputChances(20_00, 5_00, 10, 7, 6, 5, 4, 3, 2)
            .fluidInputs(FluidRegistry.getFluidStack("sludge", 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Methane, Materials2FluidShapes.fluidGas, (int) (100)))
            .eut(TierEU.RECIPE_HV)
            .duration(2 * SECONDS)
            .addTo(chemicalDehydratorRecipes);

        // Ethylbenzene
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Cell_Empty.get(3))
            .circuit(17)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Styrene, Materials2CellShapes.cell, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.AceticAcid, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
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
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Styrene, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (2_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);
    }

    private static void assemblerRecipes() {
        // Half Complete Casing I
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Vanadium, Materials2Shapes.plate, (int) (32)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.VanadiumSteel, 8))
            .itemOutputs(GregtechItemList.HalfCompleteCasing_I.get(4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Oxygen, Materials2FluidShapes.fluidGas, (int) (8_000)))
            .duration(16 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);

        // Half Complete Casing II
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.HalfCompleteCasing_I.get(2),
                MaterialLibAPI.getStack(Materials2Materials.VanadiumGallium, Materials2Shapes.plate, (int) (8)))
            .itemOutputs(GregtechItemList.HalfCompleteCasing_II.get(8))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Tantalum, Materials2FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(32 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Mining Explosives
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(2, Ic2Items.industrialTnt),
                new ItemStack(Blocks.tnt, 4),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (2)),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Iron, 1))
            .itemOutputs(GregtechItemList.MiningExplosives.get(3))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);

        // Wither Cage
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.plate, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.BlackSteel, Materials2Shapes.stick, (int) (8)))
            .itemOutputs(GregtechItemList.WitherGuard.get(64))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Simple Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_LV.get(1),
                ItemList.Electric_Motor_LV.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.bolt, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.ring, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Brass, Materials2Shapes.stick, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.plate, (int) (2)))
            .itemOutputs(GregtechItemList.SimpleHandPump.get(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Advanced Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_MV.get(1),
                ItemList.Electric_Motor_MV.get(1),
                MaterialsElements.STANDALONE.BLACK_METAL.getBolt(8),
                MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.ring, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Invar, Materials2Shapes.stick, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.plate, (int) (2)))
            .itemOutputs(GregtechItemList.AdvancedHandPump.get(1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Super Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_HV.get(1),
                ItemList.Electric_Motor_HV.get(1),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.bolt, (int) (8)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.ring, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Chrome, Materials2Shapes.stick, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.StainlessSteel, Materials2Shapes.plate, (int) (2)))
            .itemOutputs(GregtechItemList.SuperHandPump.get(1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Ultimate Hand Pump
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.FluidRegulator_EV.get(1),
                ItemList.Electric_Motor_EV.get(1),
                MaterialsAlloy.HASTELLOY_N.getBolt(8),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.ring, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.stick, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.TungstenSteel, Materials2Shapes.plate, (int) (2)))
            .itemOutputs(GregtechItemList.UltimateHandPump.get(1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Low tier Charge Packs

        // LV
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TUMBAGA.getPlate(8),
                MaterialLibAPI.getStack(Materials2Materials.Aluminium, Materials2Shapes.ring, (int) (12)),
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
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.ring, (int) (12)),
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
                MaterialLibAPI.getStack(Materials2Materials.Trinium, Materials2Shapes.plate, (int) (64)),
                ItemList.Sensor_LuV.get(6),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getBolt(64),
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.wireFine, (int) (64)),
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (1)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(distilleryRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Air, Materials2FluidShapes.fluidGas, (int) (20_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidGas, (int) (25)))
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
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfurDioxide, Materials2FluidShapes.fluidGas, (int) (500)),
                Materials.Water.getFluid(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.AlgaeBiomass.get(32))
            .itemOutputs(GregtechItemList.GreenAlgaeBiomass.get(4))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(distilleryRecipes);
    }

    private static void thermalBoilerRecipes() {
        // Lava
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Lava.getFluid(1_000), Materials.Water.getFluid(16_000 / GTValues.STEAM_PER_WATER))
            .fluidOutputs(GTModHandler.getPahoehoeLava(1_000), Materials.Steam.getGas(16_000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Copper, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tin, Materials2Shapes.ingot, (int) (1)),
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 1),
                MaterialLibAPI.getStack(Materials2Materials.Silver, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tantalum, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tungstate, Materials2Shapes.dust, (int) (1)),
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
                MaterialLibAPI.getStack(Materials2Materials.Bronze, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Electrum, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tantalum, Materials2Shapes.ingot, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tungstate, Materials2Shapes.dust, (int) (1)),
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

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Pyrotheum, Materials2Shapes.dust, (int) (1)))
            .metadata(FUEL_VALUE, 62)
            .metadata(FUEL_TYPE, 2)
            .duration(0)
            .eut(0)
            .addTo(GTRecipeConstants.Fuel);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Cryotheum, Materials2Shapes.dust, (int) (1)))
            .metadata(FUEL_VALUE, 62)
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
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Cryotheum, Materials2Shapes.dust, (int) (1)))
            .fluidOutputs(new FluidStack(TFFluids.fluidCryotheum, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidExtractionRecipes);

        // Blazing Pyrotheum
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Pyrotheum, Materials2Shapes.dust, (int) (1)))
            .fluidOutputs(new FluidStack(TFFluids.fluidPyrotheum, 250))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(fluidExtractionRecipes);
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
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Potassium, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Lithium, Materials2Shapes.dust, (int) (9)),
                MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (9)))
            .circuit(2)
            .itemOutputs(MaterialsAlloy.LEAGRISIUM.getDust(50))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (5_000)))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Steel, Materials2Shapes.dust, (int) (16)),
                MaterialLibAPI.getStack(Materials2Materials.Molybdenum, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Titanium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Nickel, Materials2Shapes.dust, (int) (4)),
                MaterialLibAPI.getStack(Materials2Materials.Cobalt, Materials2Shapes.dust, (int) (2)))
            .circuit(2)
            .itemOutputs(MaterialsAlloy.MARAGING250.getDust(24))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);
    }

    private static void chemicalReactorRecipes() {
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Apatite, Materials2Shapes.dust, (int) (32)))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Sulfur, Materials2Shapes.dust, (int) (2)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(new FluidStack(GTPPFluids.SulfuricApatiteMix, 8_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // KOH + HNO3 = KNO3 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GregtechItemList.PotassiumHydroxide.get(3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.PotassiumNitrate, Materials2Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Na2CO3 + 2HNO3 = 2NaNO3 + CO2 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SodiumCarbonate, Materials2Shapes.dust, (int) (6)))
            .circuit(1)
            .itemOutputs(MaterialMisc.SODIUM_NITRATE.getDust(10))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.CarbonDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)))
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
                MaterialLibAPI.getStack(Materials2Materials.Graphite, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.dustSmall, (int) (1)))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .metadata(COIL_HEAT, 4500)
            .metadata(ADDITIVE_AMOUNT, 500)
            .addTo(BlastFurnaceWithGas);
    }

    private static void compressorRecipes() {
        // Clay Plate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Clay, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Clay, Materials2Shapes.plate, (int) (1)))
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
                .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.dust, (int) (4)))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(maceratorRecipes);
        }
    }

    // TODO: remove cyclotron recipes for 2.10
    private static void cyclotronRecipes() {
        // Polonium
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_PO210.get(1))
            .outputChances(100)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Bismuth, Materials2FluidShapes.fluidMolten, (int) (1)))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(cyclotronRecipes);

        // Americium
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_AM241.get(4))
            .outputChances(2500)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidMolten, (int) (1)))
            .duration(8 * HOURS + 20 * MINUTES)
            .eut(4080)
            .addTo(cyclotronRecipes);

        // Strontium u235
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(GregtechItemList.Pellet_RTG_SR90.get(1))
            .outputChances(570)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Uranium235, Materials2FluidShapes.fluidMolten, (int) (10)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Plutonium, Materials2FluidShapes.fluidMolten, (int) (10)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Deuterium, Materials2FluidShapes.fluidGas, (int) (400)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidPlasma, (int) (100)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_500)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Helium, Materials2FluidShapes.fluidPlasma, (int) (1_500)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Americium, Materials2FluidShapes.fluidPlasma, (int) (2_500)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidPlasma, (int) (100)))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(cyclotronRecipes);

        // Quantum Anomaly
        GTValues.RA.stdBuilder()
            .itemInputs(Particle.getBaseParticle(Particle.UNKNOWN))
            .circuit(24)
            .itemOutputs(GregtechItemList.Laser_Lens_Special.get(1))
            .outputChances(100)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Duranium, Materials2FluidShapes.fluidMolten, (int) (40)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidPlasma, (int) (100)))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (100)))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (100)))
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
