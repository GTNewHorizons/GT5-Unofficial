package gtnhlanth.loader;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.OrePrefixes.blockCasingAdvanced;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.AssemblyLine;
import static gregtech.api.util.GTRecipeConstants.BlastFurnaceWithGas;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.DISSOLUTION_TANK_RATIO;
import static gregtech.api.util.GTRecipeConstants.SCANNING;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gregtech.api.util.GTRecipeConstants.WaferEngravingRecipes;
import static gregtech.common.items.MetaGeneratedItem01.registerCauldronCleaningFor;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.simpleWasherRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.digesterRecipes;
import static gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;
import static gtnhlanth.common.register.WerkstoffMaterialPool.CeriumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.CeriumDioxide;
import static gtnhlanth.common.register.WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet;
import static gtnhlanth.common.register.WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumOxygenBlend;
import static gtnhlanth.common.register.WerkstoffMaterialPool.CeriumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.CeriumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ChlorinatedRareEarthConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ChlorinatedRareEarthDilutedSolution;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ChlorinatedRareEarthEnrichedSolution;
import static gtnhlanth.common.register.WerkstoffMaterialPool.DephosphatedSamariumConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.DilutedSamariumRareEarthSolution;
import static gtnhlanth.common.register.WerkstoffMaterialPool.DysprosiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.DysprosiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.DysprosiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ErbiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ErbiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ErbiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.EuropiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.EuropiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.EuropiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledCeriumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledDysprosiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledErbiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledEuropiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledGadoliniumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledHolmiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledLanthanumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledLutetiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledNeodymiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledPraseodymiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledSamariumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledTerbiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledThuliumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.FilledYtterbiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.GadoliniumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.GadoliniumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.GadoliniumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.Gangue;
import static gtnhlanth.common.register.WerkstoffMaterialPool.HolmiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.HolmiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.HolmiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ImpureLanthanumChloride;
import static gtnhlanth.common.register.WerkstoffMaterialPool.LanthaniumChloride;
import static gtnhlanth.common.register.WerkstoffMaterialPool.LanthanumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.LanthanumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.LanthanumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.LutetiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.LutetiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.LutetiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.MuddySamariumRareEarthSolution;
import static gtnhlanth.common.register.WerkstoffMaterialPool.NeodymicRareEarthConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.NeodymiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.NeodymiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.NeodymiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.PraseodymiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.PraseodymiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.PraseodymiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.PromethiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.PromethiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.RarestEarthResidue;
import static gtnhlanth.common.register.WerkstoffMaterialPool.SamariumChloride;
import static gtnhlanth.common.register.WerkstoffMaterialPool.SamariumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.SamariumChlorideSodiumChlorideBlend;
import static gtnhlanth.common.register.WerkstoffMaterialPool.SamariumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.SamariumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.SamariumOxalate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.SamariumRareEarthMud;
import static gtnhlanth.common.register.WerkstoffMaterialPool.TerbiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.TerbiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.TerbiumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ThoriumPhosphateConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ThuliumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ThuliumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.ThuliumOreConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.YtterbiumChlorideConcentrate;
import static gtnhlanth.common.register.WerkstoffMaterialPool.YtterbiumExtractingNanoResin;
import static gtnhlanth.common.register.WerkstoffMaterialPool.YtterbiumOreConcentrate;

import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.system.material.BWGTMaterialReference;
import bartworks.system.material.WerkstoffLoader;
import bartworks.system.material.gtenhancement.PlatinumSludgeOverHaul;
import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.recipe.Scanning;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.Tags;
import gtnhlanth.common.item.MaskList;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import ic2.core.Ic2Items;

public class RecipeLoader {

    public static void loadAccelerator() {

        /* Actual Beamline Multiblocks */

        // SC
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(2 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                ItemList.Conveyor_Module_LuV.get(4),
                GTUtility.copyAmount(2, LanthItemList.BEAMLINE_PIPE),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2))
            .circuit(15)
            .itemOutputs(LanthItemList.SOURCE_CHAMBER)
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // LINAC
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(2 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                ItemList.Casing_Coil_Superconductor.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                ItemList.Electric_Pump_LuV.get(2),
                GTUtility.copyAmount(2, LanthItemList.BEAMLINE_PIPE),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.VanadiumGallium, 2))
            .circuit(15)
            .itemOutputs(LanthItemList.LINAC)
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // TC
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(2 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2),
                GTUtility.copyAmount(2, LanthItemList.BEAMLINE_PIPE),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.VanadiumGallium, 1))
            .circuit(15)
            .itemOutputs(LanthItemList.TARGET_CHAMBER)
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Synchrotron
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(48 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                ItemList.Casing_Coil_Superconductor.get(12),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 8),
                GTUtility.copyAmount(8, LanthItemList.BEAMLINE_PIPE),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.NiobiumTitanium, 8))
            .circuit(15)
            .itemOutputs(LanthItemList.SYNCHROTRON)
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Add .iCircuit(4) to this if it ever gets re-activated
        /*
         * //Permalloy GT_Values.RA.addMixerRecipe( Materials.Nickel.getDust(4),
         * Materials.Iron.getDust(1), Materials.Molybdenum.getDust(1), null, null,
         * WerkstoffMaterialPool.Permalloy.get(OrePrefixes.dust, 6), 1920, 200 );
         */
        // Mu-metal
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.Permalloy.get(OrePrefixes.dust, 9),
                Materials.Copper.getDust(1),
                Materials.Chrome.getDust(1))
            .circuit(2)
            .itemOutputs(WerkstoffMaterialPool.MuMetal.get(OrePrefixes.ingot, 11))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .specialValue(4500)
            .addTo(blastFurnaceRecipes);

        // Shielded Accelerator Casing -- Maybe assline recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_RadiationProof.get(1L),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 6))
            .circuit(6)
            .fluidInputs(Materials.SolderingAlloy.getMolten(1 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_CASING, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Accelerator Electrode Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                BWGTMaterialReference.Silver.get(blockCasingAdvanced, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Silver, 12),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Gold, 6))
            .circuit(6)
            .fluidInputs(Materials.SolderingAlloy.getMolten(2 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.ELECTRODE_CASING, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        ItemStack insulator = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MicaInsulatorSheet", 1);

        // Coolant Delivery Casing

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                Materials.Copper.getPlates(6),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2),
                ItemList.Electric_Pump_LuV.get(3L),
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 3),
                GTUtility.copyAmount(6, insulator),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(2 * INGOTS), Materials.Lubricant.getFluid(8 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.COOLANT_DELIVERY_CASING))
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Casing_Pipe_TungstenSteel.get(1L))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_IV))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // T1 Antenna Casing
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(48 * INGOTS),
                Materials.Gold.getMolten(32 * INGOTS),
                WerkstoffLoader.Xenon.getFluidOrGas(2_000))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tungsten, 1),
                Materials.Copper.getPlates(4),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 5),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorLuV, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                ItemList.Emitter_LuV.get(6),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Electrum, 6))
            .itemOutputs(new ItemStack(LanthItemList.ANTENNA_CASING_T1))
            .metadata(GTRecipeConstants.RESEARCH_ITEM, Materials.Niobium.getBlocks(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // T2 Antenna Casing
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(96 * INGOTS),
                Materials.Gold.getMolten(48 * INGOTS),
                WerkstoffLoader.Xenon.getFluidOrGas(2_000))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenCarbide, 1),
                Materials.Copper.getPlates(4),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 5),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                ItemList.Emitter_ZPM.get(6),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Electrum, 6))
            .itemOutputs(new ItemStack(LanthItemList.ANTENNA_CASING_T2))
            .metadata(GTRecipeConstants.RESEARCH_ITEM, new ItemStack(LanthItemList.ANTENNA_CASING_T1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 10 * SECONDS, TierEU.RECIPE_LuV))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(AssemblyLine);

        // Niobium Cavity Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Niobium, 1),
                Materials.Niobium.getPlates(6))
            .fluidInputs(Materials.Helium.getGas(2_000))
            .itemOutputs(new ItemStack(LanthItemList.NIOBIUM_CAVITY_CASING, 1))
            .duration(12 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Focus Manipulator
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(64 * INGOTS),
                Materials.Gold.getMolten(16 * INGOTS),
                Materials.Argon.getGas(1_000))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),

                new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                ItemList.Robot_Arm_LuV.get(4),
                ItemList.Conveyor_Module_LuV.get(2),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 32),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 2))
            .itemOutputs(new ItemStack(LanthItemList.FOCUS_MANIPULATION_CASING))
            .metadata(GTRecipeConstants.RESEARCH_ITEM, new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK1)))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Target Holder
        GameRegistry.addShapedRecipe(
            new ItemStack(LanthItemList.TARGET_HOLDER),
            "MCM",
            "MHM",
            "MCM",
            'M',
            WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 1),
            'H',
            ItemList.Hull_LuV.get(1),
            'C',
            ItemList.Conveyor_Module_LuV.get(1));

        GameRegistry.addShapedRecipe(

            new ItemStack(LanthItemList.FOCUS_HOLDER),
            "MCM",
            "R R",
            "MHM",
            'M',
            WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 1),
            'C',
            ItemList.Conveyor_Module_LuV.get(1),
            'R',
            ItemList.Robot_Arm_LuV.get(1),
            'H',
            ItemList.Hull_LuV.get(1));

        // Focus Input Bus
        GameRegistry.addShapedRecipe(
            LanthItemList.BEAMLINE_FOCUS_INPUT_BUS,
            "MCM",
            "McM",
            "RCR",
            'M',
            WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 1),
            'C',
            ItemList.Conveyor_Module_HV.get(1),
            'R',
            ItemList.Robot_Arm_HV.get(1),
            'c',
            new ItemStack(Blocks.chest, 1, 32767));

        // Target Receptacle, same thing as Focus Manipulator basically
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(64 * INGOTS),
                Materials.Gold.getMolten(16 * INGOTS),
                Materials.Argon.getGas(1_000))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                ItemList.Robot_Arm_LuV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 2))
            .itemOutputs(new ItemStack(LanthItemList.TARGET_RECEPTACLE_CASING))
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Circuit_Silicon_Wafer.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Capillary Exchange
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Copper, 2),
                Materials.Titanium.getPlates(6),
                GTUtility.copyAmount(4, insulator),
                ItemList.Electric_Pump_LuV.get(1),
                Materials.Silver.getDust(2))
            .fluidInputs(Materials.RubberSilicone.getMolten(2 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Mu-metal lattice
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.wireFine, 12),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(1 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.MM_LATTICE, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Shielded Accelerator Glass
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.MM_LATTICE, 4))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(1 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidSolidifierRecipes);

        // Beamline Pipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenCarbide, 4),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TungstenCarbide, 8),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 4))
            .circuit(7)
            .fluidInputs(Materials.SolderingAlloy.getMolten(1 * INGOTS))
            .itemOutputs(LanthItemList.BEAMLINE_PIPE)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Beamline Input Hatch
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(64 * INGOTS),
                Materials.Argon.getGas(1_000),
                Materials.Helium.getGas(2_000))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 2),
                ItemList.Electric_Pump_LuV.get(1),
                LanthItemList.BEAMLINE_PIPE,
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 4))
            .itemOutputs(LanthItemList.LUV_BEAMLINE_INPUT_HATCH)
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Hatch_Input_LuV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 20 * SECONDS, TierEU.RECIPE_IV))
            .duration(2 * GTRecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Beamline Output Hatch
        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(64 * INGOTS),
                Materials.Argon.getGas(1_000),
                Materials.Helium.getGas(6_000))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 6 },
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 4),
                ItemList.Electric_Pump_LuV.get(2),
                ItemList.Electric_Motor_LuV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 8),
                LanthItemList.BEAMLINE_PIPE,
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 8)

            )
            .itemOutputs(LanthItemList.LUV_BEAMLINE_OUTPUT_HATCH)
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Hatch_Output_LuV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 20 * SECONDS, TierEU.RECIPE_IV))
            .duration(4 * GTRecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Chloroform.getFluid(1_000), Materials.HydrofluoricAcid.getFluid(3_000))
            .fluidOutputs(
                WerkstoffMaterialPool.Fluoroform.getFluidOrGas(1_000),
                Materials.HydrochloricAcid.getFluid(3_000))
            .duration(15 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Nitrogen.getPlasma(4_000))
            .itemInputs(Materials.Silane.getCells(2))
            .fluidOutputs(WerkstoffMaterialPool.NitrogenPlasmaSilaneMix.getFluidOrGas(6_000))
            .itemOutputs(Materials.Empty.getCells(2))
            .duration(20 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        // NB: http://www.smfl.rit.edu/pdf/process/process_nitride_etch_paper.pdf
        // Reactive Ion Etchant
        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.Fluoroform.getFluidOrGas(3_000))
            .itemInputs(Materials.Oxygen.getCells(4))
            .fluidOutputs(WerkstoffMaterialPool.FluoroformOxygenMix.getFluidOrGas(5_000))
            .itemOutputs(Materials.Empty.getCells(4))
            .duration(15 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.FluoroformOxygenMix.getFluidOrGas(5_000))
            .itemInputs(Materials.Empty.getCells(4))
            .fluidOutputs(WerkstoffMaterialPool.Fluoroform.getFluidOrGas(3_000))
            .itemOutputs(Materials.Oxygen.getCells(4))
            .duration(5 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getDust(2), Materials.NetherQuartz.getPlates(1))
            .itemOutputs(new ItemStack(LanthItemList.IRON_COATED_QUARTZ))
            .duration(10 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Masks
        // Quartz + Fe2O3 T1
        // " + Cr T2
        //
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.IRON_COATED_QUARTZ))
            .fluidInputs(Materials.Oxygen.getGas(1_000))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK1)))
            .outputChances(10000)
            .requiresCleanRoom()
            .duration(144 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(autoclaveRecipes);

        /*
         * Unsure what was intended with this recipe? GT_Values.RA.stdBuilder() .itemInputs(new
         * ItemStack(LanthItemList.IRON_COATED_QUARTZ), Materials.Chrome.getDust(1))
         * .fluidInputs(Materials.Oxygen.getGas(1_000)) .itemOutputs(new
         * ItemStack(LanthItemList.maskMap.get(MaskList.BLANK2))) .outputChances(10000).requiresCleanRoom().duration(12
         * * SECONDS).eut(7980).addTo(autoclaveRecipes);
         */

        // Grow the first silicon
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Glass.getPlates(1))
            .fluidInputs(Materials.Silane.getGas(4_000))
            .itemOutputs(new ItemStack(LanthItemList.SUBSTRATE_PRECURSOR))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(autoclaveRecipes);

        // Now to deposit nitride
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.SUBSTRATE_PRECURSOR))
            .fluidInputs(WerkstoffMaterialPool.NitrogenPlasmaSilaneMix.getFluidOrGas(4_000))
            .fluidOutputs(Materials.Nitrogen.getGas(3_000))
            .itemOutputs(new ItemStack(LanthItemList.MASK_SUBSTRATE))
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .specialValue(3200)
            .requiresCleanRoom()
            .addTo(blastFurnaceRecipes);

        /*
         * GT_Values.RA.stdBuilder().itemInputs(GTOreDictUnificator.get(OrePrefixes.foil, Materials.Silicon, 1))
         * .fluidInputs(Materials.SiliconTetrachloride.getFluid(3_000), Materials.Ammonia.getFluid(4_000))
         * .fluidOutputs(Materials.HydrochloricAcid.getFluid(12_000))
         * .itemOutputs(WerkstoffMaterialPool.SiliconNitride.get(OrePrefixes.plate)) .duration(GTRecipeBuilder.SECONDS *
         * 30) .eut(TierEU.EV) .addTo(GTRecipe.GTRecipe_Map.sPlasmaArcFurnaceRecipes);
         */

        for (ItemStack lens : OreDictionary.getOres("craftingLensYellow")) {

            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(0, lens), new ItemStack(LanthItemList.MASK_SUBSTRATE))
                .itemOutputs(new ItemStack(LanthItemList.MASKED_MASK))
                .duration(30 * GTRecipeBuilder.SECONDS)
                .eut(TierEU.RECIPE_IV)
                .requiresCleanRoom()
                .addTo(WaferEngravingRecipes);

        }

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.MASKED_MASK))
            .fluidInputs(WerkstoffMaterialPool.FluoroformOxygenMix.getFluidOrGas(4_000))
            .itemOutputs(new ItemStack(LanthItemList.ETCHED_MASK_1))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(autoclaveRecipes);

        // Etch pt. 2 with LiCl
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(LanthItemList.MASKED_MASK),
                GGMaterial.lithiumChloride.get(OrePrefixes.dust, 2),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PotassiumHydroxideDust", 4))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK2)))
            .duration(80 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 2400)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(LanthItemList.ETCHED_MASK_1),
                Materials.Sodium.getDust(1),
                Materials.Chrome.getPlates(1),
                Materials.Gold.getPlates(2))
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK3)))
            .duration(2 * GTRecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .specialValue(3600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(LanthItemList.ETCHED_MASK_1, 4),
                Materials.Glass.getPlates(2),
                GregtechItemList.LithiumHydroxideDust.get(2),
                Materials.Epoxid.getDust(4))
            .fluidInputs(Materials.Hydrogen.getGas(2_000))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.CBLANK)))
            .duration(4 * GTRecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .specialValue(4200)
            .addTo(blastFurnaceRecipes);

        for (MaskList mask : MaskList.values()) {

            MaskList maskIngredient = mask.getPrecursor();
            Dyes lensColour = mask.getLensColour();

            if (maskIngredient == null) continue;

            if (mask.getLensColour() == null) { // CR Recipe

                if (mask == MaskList.PrPIC) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.PIC)),
                            Materials.IndiumGalliumPhosphide.getDust(2))
                        .fluidInputs(Materials.VanadiumGallium.getMolten(2 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrPIC)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrHPIC) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.HPIC)),
                            Materials.IndiumGalliumPhosphide.getDust(8))
                        .fluidInputs(Materials.Naquadah.getMolten(4 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrHPIC)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrNPIC) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.NPIC)),
                            Materials.IndiumGalliumPhosphide.getDust(64))
                        .fluidInputs(Materials.Sunnarium.getMolten(10 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrNPIC)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrCPU) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.CPU)),
                            GTUtility.copyAmount(16, Ic2Items.carbonFiber))
                        .fluidInputs(Materials.Glowstone.getMolten(4 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrCPU)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrNCPU) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.NCPU)),
                            Materials.IndiumGalliumPhosphide.getDust(1))
                        .fluidInputs(Materials.Radon.getGas(50))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrNCPU)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                    GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(LanthItemList.maskMap.get(MaskList.NCPU)), ItemList.QuantumEye.get(2))
                        .fluidInputs(Materials.GalliumArsenide.getMolten(2 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrNCPU)))
                        .duration(45 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                }

                continue;

            }

            if (mask == MaskList.NAND) {

                // Very copy-paste heavy, could possibly offload most of this into one case and just assign an
                // otherIngredient variable or something, wouldn't save much space though. Plus: lazy
                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                        GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.EnderPearl, 1)))
                    .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                    .requiresCleanRoom()
                    .duration(120 * SECONDS)
                    .eut(mask.getEngraverEUt())
                    .addTo(WaferEngravingRecipes);

            } else if (mask == MaskList.NOR) {

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                        GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.lens, Materials.EnderEye, 1)))
                    .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                    .requiresCleanRoom()
                    .duration(120 * SECONDS)
                    .eut(mask.getEngraverEUt())
                    .addTo(WaferEngravingRecipes);

            }

            else {

                for (ItemStack lens : OreDictionary.getOres("craftingLens" + lensColour.mName.replace(" ", ""))) {

                    if (lens == null) continue;
                    if (mask == MaskList.LCC) {

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                                GTUtility.copyAmount(0, lens))
                            .fluidInputs(
                                // damage * 4 (chips per recipe) * 50 (L per chip normally) * 3 / 4 (75% of the
                                // cost)
                                Materials.BioMediumSterilized.getFluid((mask.getDamage() + 1) * 4L * 50 * 3 / 4))
                            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                            .requiresCleanRoom()
                            .duration(120 * SECONDS)
                            .eut(mask.getEngraverEUt())
                            .addTo(WaferEngravingRecipes);

                    } else {

                        GTValues.RA.stdBuilder()
                            .itemInputs(
                                new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                                GTUtility.copyAmount(0, lens))
                            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                            .requiresCleanRoom()
                            .duration(120 * SECONDS)
                            .eut(mask.getEngraverEUt())
                            .addTo(WaferEngravingRecipes);
                    }

                }
            }
        }

    }

    public static void loadGeneral() {

        /* ZIRCONIUM */
        // ZrCl4
        // ZrO2 + 4HCl = ZrCl4 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 3))
            .circuit(1)
            .itemOutputs(WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ZrCl4-H2O
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 5))
            .circuit(1)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr
        // ZrCl4·H2O + 2Mg = Zr + 2MgCl2
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2))
            .circuit(2)
            .itemOutputs(
                WerkstoffLoader.Zirconium.get(OrePrefixes.ingotHot, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 6))
            .fluidInputs(WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4500)
            .addTo(blastFurnaceRecipes);

        /* HAFNIUM */
        // HfCl4
        // HfO2 + 4HCl = HfCl4 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 3))
            .circuit(1)
            .itemOutputs(WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4_000))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HfCl4-H2O
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 5))
            .circuit(1)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LP-Hf
        // HfCl4 + 2Mg = ??Hf?? + 2MgCl2
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2))
            .circuit(2)
            .itemOutputs(
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 6))
            .fluidInputs(WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 2700)
            .addTo(blastFurnaceRecipes);

        // HfI4
        // ??Hf?? + 4I = HfI4
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1))
            .circuit(1)
            .itemOutputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .fluidInputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.Iodine.get(OrePrefixes.dust, 4))
            .itemOutputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Hf
        // HfI4 = Hf + 4I
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .circuit(12)
            .itemOutputs(
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 1),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dustTiny, 1))
            .fluidOutputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(4_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Hf * 9
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 45))
            .circuit(13)
            .itemOutputs(
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 9),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dust, 1))
            .fluidOutputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(36_000))
            .duration(4 * MINUTES + 30 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 1))
            .itemOutputs(WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingot, 1))
            .duration(26 * SECONDS + 14 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        // Zirconia-Hafnia
        // ??HfZr?? = HfO2 + ZrO2
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 3),
                WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 3))
            .eut(TierEU.RECIPE_EV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        // Ammonium Nitrate
        // HNO3 + NH3 = NH4NO3
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.NitricAcid.getCells(1))
            .circuit(12)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Ammonia.getGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(12)
            .fluidInputs(Materials.NitricAcid.getFluid(1_000), Materials.Ammonia.getGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(multiblockChemicalReactorRecipes);

        // IODINE-START
        // SeaweedAsh
        GTModHandler.addSmeltingRecipe(
            GTModHandler.getModItem(PamsHarvestCraft.ID, "seaweedItem", 1),
            WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dustSmall, 1));

        // SeaweedConcentrate
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Calcite.getDust(1))
            .fluidInputs(Materials.DilutedSulfuricAcid.getFluid(1_200))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(1_200))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);

        // Iodine
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Benzene.getCells(1))
            .itemOutputs(WerkstoffMaterialPool.Iodine.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(2_000))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedByproducts.getFluidOrGas(200))
            .eut(TierEU.RECIPE_HV)
            .duration(38 * SECONDS)
            .addTo(centrifugeRecipes);

        // IODINE-END

        // 2MnO2 + 2KOH + KClO3 = 2KMnO4 + H2O + KCl
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Pyrolusite.getDust(6),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PotassiumHydroxideDust", 6),
                WerkstoffMaterialPool.PotassiumChlorate.get(OrePrefixes.dust, 5))
            .itemOutputs(
                WerkstoffMaterialPool.PotassiumPermanganate.get(OrePrefixes.dust, 12),
                Materials.RockSalt.getDust(2))
            .fluidOutputs(Materials.Water.getFluid(1_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Mn + 2O = MnO2
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Manganese.getDust(1))
            .circuit(1)
            .itemOutputs(Materials.Pyrolusite.getDust(3))
            .fluidInputs(Materials.Oxygen.getGas(2_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 6KOH + 6Cl = KClO3 + 5KCl + 3H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PotassiumHydroxideDust", 18))
            .circuit(3)
            .itemOutputs(
                Materials.RockSalt.getDust(10),
                WerkstoffMaterialPool.PotassiumChlorate.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.Chlorine.getGas(6_000))
            .fluidOutputs(Materials.Water.getFluid(3_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluorosilicate
        // 2NaCl + H2SiF6 = 2HCl + Na2SiF6
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2), Materials.Salt.getDust(4))
            .itemOutputs(Materials.HydrochloricAcid.getCells(2))
            .fluidInputs(WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(1_000))
            .duration(30 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // La + 6HCl = LaCl3 + 3H
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Lanthanum.getDust(1))
            .circuit(1)
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3_000))
            .itemOutputs(WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dust, 4))
            .fluidOutputs(Materials.Hydrogen.getGas(3_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Lanthanum Oxide
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(3_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(6_000))
            .itemInputs(WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dust, 8))
            .itemOutputs(WerkstoffMaterialPool.LanthanumOxide.get(OrePrefixes.dust, 1))
            .duration(10 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Boron Trioxide
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(FluidRegistry.getFluidStack("boricacid", 2_000))
            .fluidOutputs(new FluidStack(FluidRegistry.WATER, 3_000))
            .itemOutputs(WerkstoffMaterialPool.BoronTrioxide.get(OrePrefixes.dust, 1))
            .duration(15 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Boron, 2))
            .circuit(2)
            .fluidInputs(Materials.Oxygen.getGas(3_000))
            .itemOutputs(WerkstoffMaterialPool.BoronTrioxide.get(OrePrefixes.dust, 1))
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        // Boron Trichloride
        GTValues.RA.stdBuilder()
            .fluidInputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(3_000))
            .fluidOutputs(WerkstoffMaterialPool.BoronTrichloride.getFluidOrGas(2_000))
            .itemInputs(WerkstoffMaterialPool.BoronTrioxide.get(OrePrefixes.dust, 1), Materials.Empty.getCells(3))
            .itemOutputs(Materials.CarbonMonoxide.getCells(3))
            .duration(10 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Lanthanum Hexaboride
        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.BoronTrichloride.getFluidOrGas(8_000))
            .fluidOutputs(FluidRegistry.getFluidStack("boricacid", 1_000))
            .itemInputs(WerkstoffMaterialPool.LanthanumOxide.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.gemFlawless))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);
    }

    public static void loadLanthanideRecipes() {
        // Methanol
        // CH4O + CO + 3O =V2O5= H2C2O4 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1))
            .fluidInputs(
                Materials.Methanol.getFluid(1_000),
                Materials.CarbonMonoxide.getGas(1_000),
                Materials.Oxygen.getGas(3_000))
            .fluidOutputs(GGMaterial.oxalate.getFluidOrGas(1_000), Materials.Water.getFluid(1_000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .circuit(9)
            .fluidInputs(
                Materials.Methanol.getFluid(9_000),
                Materials.CarbonMonoxide.getGas(9_000),
                Materials.Oxygen.getGas(27_000))
            .fluidOutputs(GGMaterial.oxalate.getFluidOrGas(9_000), Materials.Water.getFluid(9_000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        // Ethanol
        // C2H6O + 5O =V2O5= H2C2O4 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1))
            .itemOutputs()
            .fluidInputs(Materials.Ethanol.getFluid(1_000), Materials.Oxygen.getGas(5_000))
            .fluidOutputs(GGMaterial.oxalate.getFluidOrGas(1_000), Materials.Water.getFluid(2_000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GGMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .circuit(9)
            .fluidInputs(Materials.Ethanol.getFluid(9_000), Materials.Oxygen.getGas(45_000))
            .fluidOutputs(GGMaterial.oxalate.getFluidOrGas(9_000), Materials.Water.getFluid(18_000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Oxalate
        // 2CeCl3 + 3H2C2O4 = 6HCl + Ce2(C2O4)3
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 8))
            .circuit(1)
            .itemOutputs(WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 5))
            .fluidInputs(GGMaterial.oxalate.getFluidOrGas(3_000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(6_000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // CHAIN BEGIN
        // MONAZITE
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Monazite, 2))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(400))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .eut(TierEU.RECIPE_EV)
            .duration(400)
            .metadata(COIL_HEAT, 800)
            .addTo(digesterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Saltpeter.getDust(1))
            .circuit(1)
            .itemOutputs(
                WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dustTiny, 4),
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 1),
                Materials.Monazite.getDustTiny(2))
            .fluidInputs(
                Materials.Water.getFluid(10_000),
                WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(11_000))
            .eut(TierEU.RECIPE_HV)
            .duration(900)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .addTo(dissolutionTankRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Saltpeter.getDust(9))
            .circuit(9)
            .itemOutputs(
                WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 9),
                Materials.Monazite.getDust(2))
            .fluidInputs(
                Materials.Water.getFluid(90_000),
                WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(9_000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(99_000))
            .eut(TierEU.RECIPE_HV)
            .duration(8100)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .addTo(dissolutionTankRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(1_000))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1),
                Materials.SiliconDioxide.getDust(1),
                Materials.Rutile.getDust(1),
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1),
                Materials.Ilmenite.getDust(1))
            .outputChances(90_00, 75_00, 20_00, 5_00, 20_00)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Water.getFluid(6_000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(7_000))
            .duration(24 * SECONDS)
            .eut(400)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(13)
            .itemOutputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dustTiny, 3))
            .fluidInputs(
                WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(1_000),
                WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(200))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(14)
            .itemOutputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 3))
            .fluidInputs(
                WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(9_000),
                WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1_800))
            .duration(3 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1))
            .outputChances(9000, 7000)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1500)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust))
            .itemOutputs(Materials.Thorium.getDust(1), Materials.Phosphate.getDust(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(320))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1))
            .outputChances(9000, 5000, 4000)
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.NeutralizedUraniumFiltrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(100))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeutralizedUraniumFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                Materials.Uranium.getDust(1),
                Materials.Uranium.getDust(1),
                Materials.Uranium.getDust(1),
                Materials.Uranium235.getDust(1),
                Materials.Uranium235.getDust(1))
            .outputChances(4500, 4000, 3000, 3000, 2000)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.NitricAcid.getFluid(500))
            .fluidOutputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 3))
            .fluidInputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(2_000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1_000))
            .itemOutputs(WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1))
            .outputChances(11_11)
            .fluidOutputs(WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(sifterRecipes);

        // BEGIN Cerium
        // Cerium-rich mixture + 3HCl = CeCl3 + Monazite (to allow cerium processing without bastnazite/monazite)
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 15))
            .itemOutputs(WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 1), Materials.Monazite.getDust(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(750))
            .fluidOutputs(Materials.Water.getFluid(750))
            .duration(25 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // CeO2 + 3NH4Cl + H = 3NH3 + CeCl3 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 3),
                WerkstoffLoader.AmmoniumChloride.get(OrePrefixes.cell, 3))
            .itemOutputs(WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 4), Materials.Ammonia.getCells(3))
            .fluidInputs(Materials.Hydrogen.getGas(1_000))
            .fluidOutputs(Materials.Steam.getGas(2_000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // Ce2(C2O4)3 + 3C = Ce2O3 + 9CO
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 5), Materials.Carbon.getDust(3))
            .itemOutputs(WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust, 5))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(9_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 800)
            .addTo(blastFurnaceRecipes);

        // END Cerium (NMLC)

        GTValues.RA.stdBuilder()
            .itemOutputs(WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.EuropiumIIIOxide.get(OrePrefixes.dust, 5))
            .outputChances(9000, 500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(electroMagneticSeparatorRecipes);

        // 5Eu2O3 + Eu = 4EuO
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumIIIOxide.get(OrePrefixes.dust, 5), Materials.Europium.getDust(1))
            .itemOutputs(WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 6))
            .duration(15 * SECONDS)
            .eut(8400)
            .addTo(UniversalChemical);

        // 4 EuO = 2 Eu + 2O2
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Europium.getDust(1))
            .fluidOutputs(Materials.Oxygen.getGas(1_000))
            .duration(15 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        // EuS = Eu + S
        // TODO old recipe. for compat only. remove material and recipe half a year later, i.e. after September 2023.
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumSulfide.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Europium.getDust(1), Materials.Sulfur.getDust(1))
            .duration(30 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Chlorine.getGas(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Salt.getDust(1),
                WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Acetone.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2))
            .itemOutputs(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 3))
            .fluidInputs(Materials.Acetone.getFluid(1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 4))
            .itemOutputs(WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 3))
            .fluidOutputs(Materials.Chloromethane.getGas(400))
            .eut(TierEU.RECIPE_EV)
            .duration(2 * MINUTES + 37 * SECONDS + 10 * TICKS)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 3))
            .itemOutputs(Materials.Samarium.getDust(2), Materials.Gadolinium.getDust(1))
            .outputChances(10000, 10000)
            .duration(6 * SECONDS + 13 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sifterRecipes);

        // BASTNASITE (god help me)
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 2))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(400))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .eut(TierEU.RECIPE_EV)
            .duration(400)
            .metadata(COIL_HEAT, 800)
            .addTo(digesterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(1_000),
                Materials.Steam.getGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.getFluidOrGas(2_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.get(OrePrefixes.cell, 1))
            .circuit(6)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(320))
            .fluidOutputs(WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1_320))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                Materials.Water.getFluid(10_000),
                WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1_000))
            .itemInputs(Materials.Saltpeter.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(11_000))
            .itemOutputs(Gangue.get(OrePrefixes.dust, 1))
            .eut(TierEU.RECIPE_EV)
            .duration(1000)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .addTo(dissolutionTankRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                Materials.SiliconDioxide.getDust(1),
                Materials.Rutile.getDust(1),
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1),
                Materials.Ilmenite.getDust(1))
            .outputChances(90_00, 75_00, 10_00, 5_00)
            .fluidInputs(WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(400))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(20 * SECONDS)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(1_000))
            .duration(25 * SECONDS)
            .eut(600)
            .metadata(COIL_HEAT, 1400)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1))
            .circuit(1)
            .itemOutputs(WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Oxygen.getGas(1_000))
            .fluidOutputs(Materials.Fluorine.getGas(13))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1))
            .circuit(7)
            .itemOutputs(WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Water.getFluid(200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Fluorine.getGas(4_000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1))
            .outputChances(100_00, 90_00)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.NitricAcid.getFluid(400))
            .fluidOutputs(WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Acetone.getFluid(1_000))
            .fluidOutputs(WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1_000))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .outputChances(80_00, 50_00)
            .fluidInputs(WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedAcetone.getFluidOrGas(750))
            .eut(TierEU.RECIPE_HV)
            .duration(45 * SECONDS)
            .addTo(centrifugeRecipes);

        // Nd RE
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 2))
            .itemOutputs(
                WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.NeodymiumOxide.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2_000))
            .duration(45 * SECONDS)
            .eut(800)
            .addTo(UniversalChemical);

        // Sm RE
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .circuit(1)
            .itemOutputs(WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 2))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(2_000))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 8),
                Materials.Calcium.getDust(4))
            .itemOutputs(
                Materials.Holmium.getDust(1),
                WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 4))
            .fluidOutputs(WerkstoffMaterialPool.CalciumFluoride.getFluidOrGas(12_000))
            .duration(1 * MINUTES + 20 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 1),
                BotWerkstoffMaterialPool.AmmoniumNitrate.get(OrePrefixes.dust, 9))
            .itemOutputs(WerkstoffMaterialPool.NitratedSamariumTerbiumMixture.get(OrePrefixes.dust, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.NitratedSamariumTerbiumMixture.get(OrePrefixes.dust, 4),
                Materials.Copper.getDust(1))
            .itemOutputs(
                WerkstoffMaterialPool.TerbiumNitrate.get(OrePrefixes.dust, 2),
                WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 2) // Potentially make only Samarium
            )
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2),
                Materials.Calcium.getDust(3))
            .itemOutputs(
                WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1),
                Materials.TricalciumPhosphate.getDust(5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 6))
            .itemOutputs(Materials.Samarium.getDust(1), WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 2))
            .outputChances(90_00, 80_00)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(centrifugeRecipes);

        // TODO UV Tier Ion Extracting Method

        // Lanthanum Part
        // Digester to produce Lanthanum Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Lanthanum, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(LanthanumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(LanthanumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(LanthanumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1_000),
                LanthanumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Praseodymium Part
        // Digester to produce Praseodymium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Praseodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(PraseodymiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(PraseodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(PraseodymiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                PraseodymiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Part
        // Digester to produce Cerium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Cerium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(CeriumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(CeriumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(CeriumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(CeriumExtractingNanoResin.getFluidOrGas(1_000), CeriumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Neodymium Part
        // Digester to produce Neodymium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Neodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(NeodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                NeodymiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Promethium Part
        // Digester to produce Neodymium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Promethium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(PromethiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);

        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal

        // Samarium Part
        // Digester to produce Samarium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Samarium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(SamariumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(SamariumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(SamariumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1_000),
                SamariumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Europium Part
        // Digester to produce Europium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Europium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(EuropiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(EuropiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(EuropiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1_000),
                EuropiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Gadolinium Part
        // Digester to produce Gadolinium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Gadolinium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(GadoliniumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(GadoliniumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(GadoliniumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                GadoliniumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Terbium Part
        // Digester to produce Terbium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Terbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(TerbiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(TerbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(TerbiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1_000),
                TerbiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dysprosium Part
        // Digester to produce Dysprosium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Dysprosium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(DysprosiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(DysprosiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(DysprosiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                DysprosiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Holmium Part
        // Digester to produce Holmium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Holmium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(HolmiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(HolmiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(HolmiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1_000),
                HolmiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Erbium Part
        // Digester to produce Erbium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Erbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(ErbiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ErbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(ErbiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(ErbiumExtractingNanoResin.getFluidOrGas(1_000), ErbiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Thulium Part
        // Digester to produce Thulium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Thulium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(ThuliumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(ThuliumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(ThuliumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1_000),
                ThuliumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ytterbium Part
        // Digester to produce Ytterbium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Ytterbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(YtterbiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(YtterbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(YtterbiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                YtterbiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lutetium Part
        // Digester to produce Lutetium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Lutetium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36_000))
            .fluidOutputs(LutetiumChlorideConcentrate.getFluidOrGas(3_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(LutetiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12_000))
            .fluidOutputs(LutetiumChlorideConcentrate.getFluidOrGas(1_000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1_000),
                LutetiumChlorideConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ion Extracting Process to produce Rare Earth Element (example Samarium) by Nano Resin
        // Get Extracting Nano Resin

        // Lanthanum
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Lanthanum.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(LanthanumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Praseodymium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Praseodymium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(PraseodymiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Cerium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Cerium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(CeriumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Neodymium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Neodymium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(NeodymiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Sm
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Samarium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(SamariumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Europium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Europium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(EuropiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Gadolinium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Gadolinium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(GadoliniumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Terbium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Terbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(TerbiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Dysprosium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Dysprosium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(DysprosiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Holmium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Holmium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(HolmiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Erbium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Erbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(ErbiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Thulium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Thulium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(ThuliumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Ytterbium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Ytterbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(YtterbiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Lutetium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                Materials.Lutetium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(GGMaterial.P507.getFluidOrGas(4_000))
            .fluidOutputs(LutetiumExtractingNanoResin.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // TODO Electrolyzer recycle Nano Resin and produce molten rare earth metal,

        // La
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledLanthanumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                LanthanumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lanthanum, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Pr
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                PraseodymiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Praseodymium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Ce
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledCeriumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                CeriumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Cerium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Nd
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledNeodymiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                NeodymiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Neodymium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Sm
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledSamariumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                SamariumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Samarium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Eu
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledEuropiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                EuropiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Europium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Ga
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledGadoliniumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                GadoliniumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Gadolinium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Tb
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledTerbiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                TerbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Terbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Dy
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledDysprosiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                DysprosiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Dysprosium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Ho
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledHolmiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                HolmiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Holmium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Er
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledErbiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                ErbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Erbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Tm
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledThuliumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                ThuliumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Thulium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Yb
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledYtterbiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                YtterbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Ytterbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Lu
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledLutetiumExtractingNanoResin.getFluidOrGas(1_000))
            .itemOutputs(
                LutetiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lutetium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // TODO ChlorinitedRareEarthConcentrate process with every 15 Rare Earth Extracting Nano Resin

        // La
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Pr
        GTValues.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ce
        GTValues.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Nd
        GTValues.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Sm
        GTValues.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Eu
        GTValues.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ga
        GTValues.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tb
        GTValues.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dy
        GTValues.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ho
        GTValues.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Er
        GTValues.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tm
        GTValues.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Yb
        GTValues.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lu
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1_000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1_000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1_000),
                GGMaterial.wasteLiquid.getFluidOrGas(1_000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // TODO Samarium Ore Concentrate Dust Processing Line Start

        // 16 SmOreDust + 200L NitricAcid =EV@10s= 800L MuddySamariumRareEarthSolution + 1 ?ThP?ConcentrateDust
        GTValues.RA.stdBuilder()
            .itemInputs(SamariumOreConcentrate.get(OrePrefixes.dust, 16))
            .fluidInputs(Materials.NitricAcid.getFluid(200))
            .itemOutputs(ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(800))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1 CrushedSamariumOre = 3 SamariumOreConcentrate in process
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.crushed, Materials.Samarium, 8))
            .fluidInputs(Materials.NitricAcid.getFluid(300))
            .itemOutputs(ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 3))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(1_200))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1B MuddySmSolution + 1B NitricAcid =EV@10s= 2B SamariumRareEarthMud + 0.8 CeriumDioxide + 0.6
        // CeriumRichMixture(CeriumOreConcentrate)
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 1), CeriumOreConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.NitricAcid.getFluid(1_000), MuddySamariumRareEarthSolution.getFluidOrGas(1_000))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(2_000))
            .metadata(DISSOLUTION_TANK_RATIO, 1)
            .outputChances(8000, 6000)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .circuit(9)
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 9), CeriumOreConcentrate.get(OrePrefixes.dust, 9))
            .fluidInputs(Materials.NitricAcid.getFluid(9_000), MuddySamariumRareEarthSolution.getFluidOrGas(9_000))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(18_000))
            .metadata(DISSOLUTION_TANK_RATIO, 1)
            .outputChances(8000, 6000)
            .eut(TierEU.RECIPE_IV)
            .duration(300)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.NitricAcid.getFluid(1_000), MuddySamariumRareEarthSolution.getFluidOrGas(1_000))
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 1))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(1_000))
            .outputChances(5000)
            .eut(TierEU.RECIPE_EV)
            .duration(300)
            .addTo(multiblockChemicalReactorRecipes);

        // 1B SamariumRareEarthMud + 9B water =EV@30s= 10B DilutedSamariumRareEarthSolution
        // + (90% + 60%) NeodymiumREConcentrate
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(Materials.Water.getFluid(9_000), SamariumRareEarthMud.getFluidOrGas(1_000))
            .itemOutputs(
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(10_000))
            .metadata(DISSOLUTION_TANK_RATIO, 9)
            .outputChances(9000, 6000)
            .eut(TierEU.RECIPE_EV)
            .duration(600)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(Materials.Water.getFluid(81_000), SamariumRareEarthMud.getFluidOrGas(9_000))
            .itemOutputs(
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 9),
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 9))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(90_000))
            .metadata(DISSOLUTION_TANK_RATIO, 9)
            .outputChances(9000, 6000)
            .eut(TierEU.RECIPE_IV)
            .duration(900)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(SamariumRareEarthMud.getFluidOrGas(1_000), Materials.Water.getFluid(16_000))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(8_000))
            .eut(TierEU.RECIPE_EV)
            .duration(1200)
            .addTo(multiblockChemicalReactorRecipes);

        // 2B DilutedSamariumRareEarthSolution + 3B Oxalate
        // =EV@10s=
        // 5 ImpureSamariumOxalate + 50L MuddySamariumRareEarthSolution + 0.1*2 LepersonniteDust
        // LepersonniteDust -> DephosphatedSamariumConcentrate
        GTValues.RA.stdBuilder()
            .circuit(13)
            .fluidInputs(DilutedSamariumRareEarthSolution.getFluidOrGas(2_000), GGMaterial.oxalate.getFluidOrGas(3_000))
            .itemOutputs(
                SamariumOxalate.get(OrePrefixes.dust, 5),
                DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 3))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(50))
            .outputChances(10000, 1000)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // 5 ImpureSamariumOxalate + 6B HCL = 8 ImpureSamariumChloride + 6B CO
        GTValues.RA.stdBuilder()
            .itemInputs(SamariumOxalate.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(6_000))
            .itemOutputs(SamariumChloride.get(OrePrefixes.dust, 8))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(6_000))
            .eut(TierEU.RECIPE_EV / 2)
            .duration(10 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        /**
         * ImpureSamariumChloride has 2 method to process 1. In IV-LuV, fix with NcCL then use electrolyzer to process
         * the mixture, get Samarium dust & Chlorine & Sodium. 2. In ZPM, put molten ImpureSamariumChloride and
         * LanthanumDust in Distillation Tower to get molten Samarium and impure Lanthanum Chloride.
         */

        // 2 ImpureSamariumChloride + 1 NaCl =LV@5s= 3 SamariumChlorideSodiumChlorideBlend
        GTValues.RA.stdBuilder()
            .itemInputs(SamariumChloride.get(OrePrefixes.dust, 2), Materials.Salt.getDust(1))
            .itemOutputs(SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 3))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);

        // 6 SamariumChlorideSodiumChlorideBlend =IV@1s= 1 SamariumDust + 1 SodiumDust + 2/9 RarestEarthResidue + 4B
        // Chlorine
        GTValues.RA.stdBuilder()
            .itemInputs(SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 6))
            .circuit(1)
            .itemOutputs(
                Materials.Samarium.getDust(1),
                Materials.Sodium.getDust(1),
                RarestEarthResidue.get(OrePrefixes.dustTiny, 2))
            .fluidOutputs(Materials.Chlorine.getGas(4_000))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * SECONDS)
            .addTo(electrolyzerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 54))
            .circuit(9)
            .itemOutputs(
                Materials.Samarium.getDust(9),
                Materials.Sodium.getDust(9),
                RarestEarthResidue.get(OrePrefixes.dust, 2))
            .fluidOutputs(Materials.Chlorine.getGas(36_000))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * SECONDS)
            .addTo(electrolyzerRecipes);

        // distill with LanthanumDust 36*144L moltenSmCl3 = 16*144L moltenSm + 27B Cl
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Lanthanum.getDust(9))
            .itemOutputs(ImpureLanthanumChloride.get(OrePrefixes.dust, 36))
            .fluidInputs(SamariumChloride.getMolten(5_184))
            .fluidOutputs(Materials.Samarium.getMolten(16 * INGOTS))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(distillationTowerRecipes);

        // Centrifuge ImpureLanthanumChlorideDust
        GTValues.RA.stdBuilder()
            .itemInputs(ImpureLanthanumChloride.get(OrePrefixes.dust, 36))
            .itemOutputs(LanthaniumChloride.get(OrePrefixes.dust, 36), RarestEarthResidue.get(OrePrefixes.dust, 5))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * SECONDS)
            .addTo(centrifugeRecipes);
    }

    public static void addRandomChemCrafting() {

        // PTMEG Elastomer
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Butanediol.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(4_000))
            .fluidOutputs(WerkstoffMaterialPool.PTMEGElastomer.getMolten(4_000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Toluene Tetramethyl Diisocyanate
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.TolueneDiisocyanate.get(OrePrefixes.cell, 3),
                Materials.Hydrogen.getCells(2))
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(2_000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // PTHF
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.TungstophosphoricAcid.get(OrePrefixes.cell, 1),
                Materials.Oxygen.getCells(1))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(1 * INGOTS))
            .fluidOutputs(WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(3 * INGOTS))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // THF
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.AcidicButanediol.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Ethanol.getFluid(1_000))
            .fluidOutputs(WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(1_000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Acidicised Butanediol
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.SulfuricAcid.getCells(1))
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.AcidicButanediol.getFluidOrGas(1_000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        // Butanediol
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dustTiny, 1))
            .fluidInputs(Materials.Butane.getGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(1_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dust, 1))
            .circuit(9)
            .fluidInputs(Materials.Butane.getGas(9_000))
            .fluidOutputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(9_000))
            .duration(6 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Moly-Te-Oxide Catalyst
        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MolybdenumIVOxide.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.TelluriumIVOxide.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dust, 2))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // Tungstophosphoric Acid
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.PhosphoricAcid.getCells(1), Materials.HydrochloricAcid.getCells(24))
            .itemOutputs(Materials.Salt.getDust(24), Materials.Empty.getCells(25))
            .fluidInputs(BotWerkstoffMaterialPool.SodiumTungstate.getFluidOrGas(12_000))
            .fluidOutputs(WerkstoffMaterialPool.TungstophosphoricAcid.getFluidOrGas(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(UniversalChemical);

        // Toluene Diisocyanate
        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Diaminotoluene.get(OrePrefixes.cell, 1), Materials.Empty.getCells(3))
            .itemOutputs()
            .fluidInputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(2_000))
            .fluidOutputs(WerkstoffMaterialPool.TolueneDiisocyanate.getFluidOrGas(1_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Diaminotoluene
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Hydrogen.getCells(4))
            .itemOutputs()
            .fluidInputs(WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1_000))
            .fluidOutputs(WerkstoffMaterialPool.Diaminotoluene.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Dinitrotoluene
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.NitricAcid.getCells(2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.Toluene.getFluid(1_000))
            .fluidOutputs(WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1_000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Digester Control Block
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1L),
                ItemList.Super_Tank_EV.get(2L),
                ItemList.Electric_Motor_IV.get(4L),
                ItemList.Electric_Pump_IV.get(4L),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Desh, 4L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4L))
            .circuit(1)
            .itemOutputs(LanthItemList.DIGESTER)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(10 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_IV / 2)
            .addTo(assemblerRecipes);
        // Dissolution Tank
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1L),
                ItemList.Super_Tank_HV.get(2L),
                ItemList.Electric_Motor_EV.get(4L),
                ItemList.Electric_Pump_EV.get(2L),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.VibrantAlloy, 4L),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4L))
            .circuit(2)
            .itemOutputs(LanthItemList.DISSOLUTION_TANK)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(5 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.DilutedAcetone.getFluidOrGas(50))
            .fluidOutputs(Materials.Acetone.getFluid(30))
            .duration(1 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.HotSuperCoolant.getFluidOrGas(1_000))
            .fluidOutputs(Materials.SuperCoolant.getFluid(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(vacuumFreezerRecipes);

        // TODO Cerium-doped Lutetium Aluminium Garnet (Ce:LuAG)
        /**
         * 1/9 Ce + 3 Lu + 5 Sapphire = 8 LuAG Blend 1/9 Ce + 3 Lu + 10 Green Sapphire = 8 LuAG Blend 2/9 Ce + 6 Lu + 25
         * Alumina + 9 Oxygen = 12 LuAG Blend
         * <p>
         * 1 Ce + 60 Lu + 100 Sapphire = 160 LuAG Blend 1 Ce + 60 Lu +200 Green Sapphire = 160 LuAG Blend
         *
         */
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cerium.getDust(1), Materials.Lutetium.getDust(3), Materials.Sapphire.getDust(5))
            .circuit(4)
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 9))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cerium.getDust(1), Materials.Lutetium.getDust(3), Materials.GreenSapphire.getDust(5))
            .circuit(4)
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 9))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cerium.getDust(1), Materials.Lutetium.getDust(3), Materials.Aluminiumoxide.getDust(5))
            .circuit(4)
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 9))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cerium.getDust(9), Materials.Lutetium.getDust(27), Materials.Sapphire.getDust(45))
            .circuit(5)
            .itemOutputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 17))
            .eut(TierEU.RECIPE_UV)
            .duration(45 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Cerium.getDust(9),
                Materials.Lutetium.getDust(27),
                Materials.GreenSapphire.getDust(45))
            .circuit(5)
            .itemOutputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 17))
            .eut(TierEU.RECIPE_UV)
            .duration(45 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Cerium.getDust(9),
                Materials.Lutetium.getDust(27),
                Materials.Aluminiumoxide.getDust(45))
            .circuit(5)
            .itemOutputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 17))
            .eut(TierEU.RECIPE_UV)
            .duration(45 * SECONDS)
            .addTo(mixerRecipes);

        // Get LuAG Crystal seed
        GTValues.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .fluidInputs(Materials.Lutetium.getMolten(8 * INGOTS))
            .outputChances(514)
            .eut(TierEU.RECIPE_UV)
            .duration(500)
            .addTo(autoclaveRecipes);

        // 1 LuAG Blend = 1.1(Og) 1.0(Xe) 0.99(Kr) LuAG in Autoclave
        GTValues.RA.stdBuilder()
            .itemInputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(40))
            .outputChances(8000, 1900)
            .eut(TierEU.RECIPE_UHV)
            .duration(512)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(WerkstoffLoader.Xenon.getFluidOrGas(25))
            .outputChances(9000, 1000)
            .eut(TierEU.RECIPE_UHV)
            .duration(256)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .circuit(1)
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(10))
            .outputChances(10000, 100)
            .eut(TierEU.RECIPE_UHV)
            .duration(128)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 2))
            .fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(10))
            .outputChances(10000, 2000)
            .eut(TierEU.RECIPE_UHV)
            .duration(128)
            .addTo(autoclaveRecipes);

        // 1 LuAG Blend = 1 LuAG in Vacuum Furnace
        GTValues.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dust, 1))
            .itemOutputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(CeriumDopedLutetiumAluminiumOxygenBlend.getMolten(108))
            .metadata(COIL_HEAT, 9100)
            .eut(TierEU.RECIPE_UHV)
            .duration(5 * SECONDS)
            .addTo(vacuumFurnaceRecipes);

        // 16 Adv Crystal SoC
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensBlue")) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(0, itemStack),
                    CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
                .itemOutputs(ItemList.Circuit_Chip_CrystalSoC2.get(16))
                .requiresCleanRoom()
                .eut(160000)
                .duration(40 * SECONDS)
                .addTo(laserEngraverRecipes);
        }

        // 16 Crystal SoC
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensGreen")) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(0, itemStack),
                    CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
                .itemOutputs(ItemList.Circuit_Chip_CrystalSoC.get(16))
                .requiresCleanRoom()
                .eut(160000)
                .duration(40 * SECONDS)
                .addTo(laserEngraverRecipes);
        }

    }

    public static void removeCeriumMacerator() {
        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);

        GTLog.out.print(Tags.MODID + ": processing macerator recipes");
        for (GTRecipe recipe : maceratorRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) {
                continue;
            }

            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!(oreName.startsWith("ore") || oreName.startsWith("rawOre") || oreName.startsWith("crushed"))) {
                    continue;
                }

                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;

                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        modified = true;
                        GTLog.out.println(
                            "in the recipe of '" + recipe.mInputs[0].getDisplayName()
                                + "', replacing Cerium dust by Cerium Rich Mixture dust");
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        modified = true;
                        GTLog.out.println(
                            "in the recipe of '" + recipe.mInputs[0].getDisplayName()
                                + "', replacing Samarium dust by Samarium Ore Concentrate dust");
                    }
                }
                if (modified) {
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;

            }

        }
        maceratorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(maceratorRecipes::add);
        maceratorRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GTLog.out.print(Tags.MODID + ": macerator recipes done!");
    }

    public static void removeCeriumWasher() {
        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);

        GTLog.out.println(Tags.MODID + ": processing orewasher recipes");
        for (GTRecipe recipe : oreWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                if (!OreDictionary.getOreName(oreDictID)
                    .startsWith("crushed")) {
                    continue;
                }

                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input: "
                                + input.getDisplayName()
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input: "
                                + input.getDisplayName()
                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                        modified = true;
                    }
                }
                if (modified) {
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;
            }
        }

        GTLog.out.println(Tags.MODID + ": regenerating ore washer recipes");
        oreWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(oreWasherRecipes::add);
        oreWasherRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GTLog.out.println(Tags.MODID + ": ore washer recipes done!");
    }

    public static void removeCeriumThermalCentrifuge() {

        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);

        GTLog.out.println(Tags.MODID + ": processing thermal centrifuge recipes");
        for (GTRecipe recipe : thermalCentrifugeRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                if (!(OreDictionary.getOreName(oreDictID)
                    .startsWith("crushed")
                    || OreDictionary.getOreName(oreDictID)
                        .startsWith("purified"))) {
                    continue;
                }

                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input "
                                + input.getDisplayName()
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input "
                                + input.getDisplayName()
                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                        modified = true;
                    }
                }
                if (modified) {
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;
            }
        }

        GTLog.out.println(Tags.MODID + ": regenerating thermal centrifuge recipes");
        thermalCentrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(thermalCentrifugeRecipes::add);
        thermalCentrifugeRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GTLog.out.println(Tags.MODID + ": thermal centrifuge recipes done!");
    }

    public static void removeCeriumCentrifuge() {

        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);
        GTLog.out.println(Tags.MODID + ": processing centrifuge recipes");
        for (GTRecipe recipe : centrifugeRecipes.getAllRecipes()) {
            ItemStack input = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!oreName.startsWith("dust") || oreName.contains("Dephosphated") || oreName.startsWith("dustMAR")) {
                    break;
                }
                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustTiny(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustTiny, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium tiny dust turned into Cerium Rich Mixture tiny dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustSmall(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustSmall, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium small dust turned into Cerium Rich Mixture small dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustTiny(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustTiny, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate tiny dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustSmall(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustSmall, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate small dust.");
                        modified = true;
                    }
                }
                if (modified) {
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;
            }
        }

        GTLog.out.println(Tags.MODID + ": regenerating centrifuge recipes");
        centrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(centrifugeRecipes::add);
        centrifugeRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GTLog.out.println(Tags.MODID + ": centrifuge recipes done!");
    }

    public static void removeCeriumHammer() {

        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);

        GTLog.out.println(Tags.MODID + ": processing forge hammer recipes");

        for (GTRecipe recipe : hammerRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!oreName.startsWith("crushed")) {
                    continue;
                }

                GTRecipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                        modified = true;
                    }
                }
                if (modified) {
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;
            }
        }

        GTLog.out.println(Tags.MODID + ": regenerating forge hammer recipes");
        hammerRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(hammerRecipes::add);
        hammerRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GTLog.out.println(Tags.MODID + ": forge hammer recipes done!");
    }

    public static void removeCeriumElectrolyzer() {
        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);
        GTLog.out.println(Tags.MODID + ": Processing electrolyzer recipes");
        for (GTRecipe recipe : electrolyzerRecipes.getAllRecipes()) {
            for (ItemStack input : recipe.mInputs) {
                if (!GTUtility.isStackValid(input)) {
                    continue;
                }

                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    final String oreName = OreDictionary.getOreName(oreDictID);
                    if (!(oreName.startsWith("dust")
                        && (oreName.equals("dustHibonite") || oreName.equals("dustLanthaniteCe")
                            || oreName.equals("dustZirconolite")
                            || oreName.equals("dustYttrocerite")
                            || oreName.equals("dustXenotime")
                            || oreName.equals("dustBastnasite")
                            || oreName.equals("dustFlorencite")))) {
                        continue;
                    }

                    GTRecipe tRecipe = recipe.copy();
                    boolean modified = false;
                    for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                        if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                        if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            GTLog.out.println(
                                Tags.MODID + ": recipe with input oredict: "
                                    + oreName
                                    + " get Cerium dust turned into Cerium Rich Mixture dust.");
                            modified = true;
                        } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            GTLog.out.println(
                                Tags.MODID + ": recipe with input oredict: "
                                    + oreName
                                    + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                            modified = true;
                        }
                    }
                    if (modified) {
                        reAdd.add(tRecipe);
                        remove.add(recipe);
                    }
                    break;
                }
            }
        }

        GTLog.out.println(Tags.MODID + ": regenerating electrolyzer recipes");
        electrolyzerRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(electrolyzerRecipes::add);
        electrolyzerRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GTLog.out.println(Tags.MODID + ": Electrolyzer recipe done!");
    }

    public static void removeCeriumSimpleWasher() {
        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);
        GTLog.out.println(Tags.MODID + ": processing simple washer recipes.");
        for (GTRecipe recipe : simpleWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GTUtility.isStackValid(input)) {
                continue;
            }

            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);

                if (!(oreName.startsWith("dustImpureCerium") || oreName.startsWith("dustImpureSamarium")
                    || oreName.startsWith("dustPureSamarium")
                    || oreName.startsWith("dustPureCerium"))) {
                    continue;
                }

                GTRecipe tRecipe = recipe.copy();
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                    }
                }
                if (!tRecipe.equals(recipe)) {
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;
            }
        }

        GTLog.out.println(Tags.MODID + ": regenerating simple washer recipes");
        simpleWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(simpleWasherRecipes::add);
        simpleWasherRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GTLog.out.println(Tags.MODID + ": Simple washer recipes done!");
    }

    public static void removeCeriumDehydrator() {
        HashSet<GTRecipe> remove = new HashSet<>(5000);
        HashSet<GTRecipe> reAdd = new HashSet<>(5000);
        GTLog.out.println(Tags.MODID + ": processing chemical dehydrator recipes.");

        for (GTRecipe recipe : chemicalDehydratorRecipes.getAllRecipes()) {
            if (recipe.mInputs.length == 0) {
                continue;
            }
            ItemStack input = recipe.mInputs[0];

            if (!GTUtility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!oreName.startsWith("dust")) {
                    continue;
                }
                if (!(oreName.equals("dustCerite") || oreName.equals("dustFluorcaphite")
                    || oreName.equals("dustZirkelite")
                    || oreName.equals("dustGadoliniteCe")
                    || oreName.equals("dustGadoliniteY")
                    || oreName.equals("dustPolycrase")
                    || oreName.equals("dustBastnasite"))) {
                    continue;
                }

                GTRecipe tRecipe = recipe.copy();
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(tRecipe.mOutputs[i])) continue;

                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            WerkstoffMaterialPool.CeriumRichMixture
                                .get(OrePrefixes.dust, tRecipe.mOutputs[i].stackSize));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                    }
                }
                if (!tRecipe.equals(recipe)) {
                    reAdd.add(tRecipe);
                    remove.add(recipe);
                }
                break;

            }

        }

        GTLog.out.println(Tags.MODID + ": regenerating chemical dehydrator recipes");
        chemicalDehydratorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(chemicalDehydratorRecipes::add);
        chemicalDehydratorRecipes.getBackend()
            .reInit();

        GTLog.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GTLog.out.println(Tags.MODID + ": chemical dehydrator recipes done!");

    }

    public static void removeCeriumChemicalBath() {
        HashSet<GTRecipe> remove = new HashSet<>(5000);

        GTLog.out.println(Tags.MODID + ": marking recipes in chem bath for removal!");
        for (GTRecipe recipe : chemicalBathRecipes.getAllRecipes()) {
            for (ItemStack input : recipe.mInputs) {
                if (!GTUtility.isStackValid(input)) {
                    continue;
                }
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    String oreName = OreDictionary.getOreName(oreDictID);
                    if (oreName.equals("dustTin")) {
                        GTLog.out.println(Tags.MODID + ": chem bath recipe with tin dust detected, removing it.");
                        remove.add(recipe);
                        break;
                    }
                    if (oreName.equals("dustRutile")) {
                        GTLog.out.println(Tags.MODID + ": chem bath recipe with rutile dust detected, removing it.");
                        remove.add(recipe);
                        break;
                    }
                }
            }
        }

        GTLog.out.println(Tags.MODID + ": regenerating chem bath recipes");
        chemicalBathRecipes.getBackend()
            .removeRecipes(remove);
        chemicalBathRecipes.getBackend()
            .reInit();

        GTLog.out.println("Chemical Bath done!");
    }

    public static void removeCeriumSources() {

        removeCeriumMacerator();
        removeCeriumWasher();
        removeCeriumThermalCentrifuge();
        removeCeriumCentrifuge();
        removeCeriumHammer();
        removeCeriumElectrolyzer();
        removeCeriumSimpleWasher();
        removeCeriumDehydrator();
        removeCeriumChemicalBath();

        // For Cauldron Wash
        GTLog.out.println(Tags.MODID + ": processing cauldron washing recipes to replace cerium sources");
        registerCauldronCleaningFor(Materials.Cerium, WerkstoffMaterialPool.CeriumRichMixture.getBridgeMaterial());
        registerCauldronCleaningFor(
            Materials.Samarium,
            WerkstoffMaterialPool.SamariumOreConcentrate.getBridgeMaterial());
        GTLog.out.println(Tags.MODID + ": processing cauldron washing recipes done!");

        // For Crafting Table
        GTLog.out.println(Tags.MODID + ": processing crafting recipes to replace cerium sources");
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(RecipeLoader::replaceInCraftTable);
        GTLog.out.println(Tags.MODID + ": processing crafting recipes done!");
    }

    public static void replaceInCraftTable(Object obj) {
        IRecipe recipe = (IRecipe) obj;
        ItemStack result = recipe.getRecipeOutput();
        if (!(recipe instanceof IRecipeMutableAccess mutableRecipe)) {
            return;
        }

        Object input = mutableRecipe.gt5u$getRecipeInputs();

        if (GTUtility.areStacksEqual(result, Materials.Cerium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Cerium)) {
                return;
            }
            GTLog.out.println("replacing crafting recipe of Cerium dust by Cerium Rich Mixture");
            mutableRecipe.gt5u$setRecipeOutputItem(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 2));
        } else if (GTUtility.areStacksEqual(result, Materials.Samarium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Samarium)) {
                return;
            }
            mutableRecipe
                .gt5u$setRecipeOutputItem(WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2));
            GTLog.out.println("replacing crafting recipe of Samarium dust by Samarium Ore Concentrate");
        }
    }

}
