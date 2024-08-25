package com.elisis.gtnhlanth.loader;

import static com.elisis.gtnhlanth.api.recipe.LanthanidesRecipeMaps.digesterRecipes;
import static com.elisis.gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.CeriumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.CeriumDioxide;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumGarnet;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.CeriumDopedLutetiumAluminiumOxygenBlend;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.CeriumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.CeriumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ChlorinatedRareEarthConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ChlorinatedRareEarthDilutedSolution;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ChlorinatedRareEarthEnrichedSolution;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.DephosphatedSamariumConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.DilutedSamariumRareEarthSolution;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.DysprosiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.DysprosiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.DysprosiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ErbiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ErbiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ErbiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.EuropiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.EuropiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.EuropiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledCeriumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledDysprosiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledErbiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledEuropiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledGadoliniumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledHolmiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledLanthanumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledLutetiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledNeodymiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledPraseodymiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledPromethiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledSamariumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledTerbiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledThuliumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.FilledYtterbiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.GadoliniumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.GadoliniumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.GadoliniumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.Gangue;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.HolmiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.HolmiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.HolmiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ImpureLanthanumChloride;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.LanthaniumChloride;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.LanthanumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.LanthanumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.LanthanumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.LutetiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.LutetiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.LutetiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.MuddySamariumRareEarthSolution;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.NeodymicRareEarthConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.NeodymiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.NeodymiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.NeodymiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.PraseodymiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.PraseodymiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.PraseodymiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.PromethiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.PromethiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.PromethiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.RarestEarthResidue;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumChloride;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumChlorideSodiumChlorideBlend;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumOxalate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumOxide;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.SamariumRareEarthMud;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.TerbiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.TerbiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.TerbiumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ThoriumPhosphateConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ThuliumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ThuliumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.ThuliumOreConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.YtterbiumChlorideConcentrate;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.YtterbiumExtractingNanoResin;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.YtterbiumOreConcentrate;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.OrePrefixes.blockCasingAdvanced;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.AssemblyLine;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.DISSOLUTION_TANK_RATIO;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;
import static gregtech.api.util.GT_RecipeConstants.WaferEngravingRecipes;
import static gregtech.common.items.GT_MetaGenerated_Item_01.registerCauldronCleaningFor;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.simpleWasherRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;

import java.util.HashSet;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.elisis.gtnhlanth.Tags;
import com.elisis.gtnhlanth.common.item.MaskList;
import com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool;
import com.elisis.gtnhlanth.common.register.LanthItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.system.material.BW_GT_MaterialReference;
import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.PlatinumSludgeOverHaul;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IRecipeMutableAccess;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;

public class RecipeLoader {

    public static void loadAccelerator() {

        /* Actual Beamline Multiblocks */

        // SC
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                ItemList.Conveyor_Module_LuV.get(4),
                GT_Utility.copyAmount(2, LanthItemList.BEAMLINE_PIPE),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.VanadiumGallium, 2),
                GT_Utility.getIntegratedCircuit(16)

            )
            .itemOutputs(LanthItemList.SOURCE_CHAMBER)
            .duration(30 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // LINAC
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                ItemList.Casing_Coil_Superconductor.get(2),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                ItemList.Electric_Pump_LuV.get(2),
                GT_Utility.copyAmount(2, LanthItemList.BEAMLINE_PIPE),
                GT_OreDictUnificator.get(OrePrefixes.cableGt08, Materials.VanadiumGallium, 2),
                GT_Utility.getIntegratedCircuit(16)

            )
            .itemOutputs(LanthItemList.LINAC)
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // TC
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 2),
                GT_Utility.copyAmount(2, LanthItemList.BEAMLINE_PIPE),
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.VanadiumGallium, 1),
                GT_Utility.getIntegratedCircuit(16)

            )
            .itemOutputs(LanthItemList.TARGET_CHAMBER)
            .duration(30 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Synchrotron
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.SolderingAlloy.getMolten(6000))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 8),
                ItemList.Casing_Coil_Superconductor.get(12),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UV, 8),
                GT_Utility.copyAmount(8, LanthItemList.BEAMLINE_PIPE),
                GT_OreDictUnificator.get(OrePrefixes.cableGt08, Materials.NiobiumTitanium, 8),
                GT_Utility.getIntegratedCircuit(16))
            .itemOutputs(LanthItemList.SYNCHROTRON)
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        /*
         * //Permalloy GT_Values.RA.addMixerRecipe( GT_Utility.getIntegratedCircuit(4), Materials.Nickel.getDust(4),
         * Materials.Iron.getDust(1), Materials.Molybdenum.getDust(1), null, null,
         * WerkstoffMaterialPool.Permalloy.get(OrePrefixes.dust, 6), 1920, 200 );
         */
        // Mu-metal
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                WerkstoffMaterialPool.Permalloy.get(OrePrefixes.dust, 9),
                Materials.Copper.getDust(1),
                Materials.Chrome.getDust(1))
            .itemOutputs(WerkstoffMaterialPool.MuMetal.get(OrePrefixes.ingot, 11))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .specialValue(4500)
            .addTo(blastFurnaceRecipes);

        // Shielded Accelerator Casing -- Maybe assline recipe
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_RadiationProof.get(1L),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 6),
                GT_Utility.getIntegratedCircuit(6))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .itemOutputs(new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_CASING, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Accelerator Electrode Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                BW_GT_MaterialReference.Silver.get(blockCasingAdvanced, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Silver, 12),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Gold, 6),
                GT_Utility.getIntegratedCircuit(6))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288))
            .itemOutputs(new ItemStack(LanthItemList.ELECTRODE_CASING, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        ItemStack insulator = GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorSheet", 1);

        // Coolant Delivery Casing

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                Materials.Copper.getPlates(6),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2),
                ItemList.Electric_Pump_LuV.get(3L),
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 1),
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 1),
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 1),
                GT_Utility.copyAmount(2, insulator),
                GT_Utility.copyAmount(2, insulator),
                GT_Utility.copyAmount(2, insulator),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(288), Materials.Lubricant.getFluid(1152))
            .itemOutputs(new ItemStack(LanthItemList.COOLANT_DELIVERY_CASING))
            .metadata(GT_RecipeConstants.RESEARCH_ITEM, ItemList.Casing_Pipe_TungstenSteel.get(1L))
            .metadata(GT_RecipeConstants.RESEARCH_TIME, 72000)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(AssemblyLine);

        // T1 Antenna Casing
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(6000),
                Materials.Gold.getMolten(4000),
                WerkstoffLoader.Xenon.getFluidOrGas(2000))
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tungsten, 1),
                Materials.Copper.getPlates(4),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 5),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorLuV, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 4 },
                ItemList.Emitter_LuV.get(6),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 32),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Electrum, 6))
            .itemOutputs(new ItemStack(LanthItemList.ANTENNA_CASING_T1))

            .metadata(GT_RecipeConstants.RESEARCH_ITEM, Materials.Niobium.getBlocks(1))
            .metadata(GT_RecipeConstants.RESEARCH_TIME, 30 * GT_RecipeBuilder.MINUTES)

            .duration(30 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(AssemblyLine);

        // T2 Antenna Casing
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(12000),
                Materials.Gold.getMolten(6000),
                WerkstoffLoader.Xenon.getFluidOrGas(2000))
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenCarbide, 1),
                Materials.Copper.getPlates(4),
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 5),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorZPM, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4 },
                ItemList.Emitter_ZPM.get(6),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 64),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Electrum, 6))
            .itemOutputs(new ItemStack(LanthItemList.ANTENNA_CASING_T2))
            .metadata(GT_RecipeConstants.RESEARCH_ITEM, new ItemStack(LanthItemList.ANTENNA_CASING_T1))
            .metadata(GT_RecipeConstants.RESEARCH_TIME, 40 * GT_RecipeBuilder.MINUTES)
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(AssemblyLine);

        // Niobium Cavity Casing
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Niobium, 1),
                Materials.Niobium.getPlates(6))
            .fluidInputs(Materials.Helium.getGas(2000))
            .itemOutputs(new ItemStack(LanthItemList.NIOBIUM_CAVITY_CASING, 1))
            .duration(12 * GT_RecipeBuilder.SECONDS)
            .eut(7680)
            .addTo(assemblerRecipes);

        // Focus Manipulator
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(8000),
                Materials.Gold.getMolten(2000),
                Materials.Argon.getGas(1000))
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),

                new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                ItemList.Robot_Arm_LuV.get(2),
                ItemList.Robot_Arm_LuV.get(2),
                ItemList.Conveyor_Module_LuV.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 2))
            .itemOutputs(new ItemStack(LanthItemList.FOCUS_MANIPULATION_CASING))
            .metadata(GT_RecipeConstants.RESEARCH_ITEM, new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK1)))
            .metadata(GT_RecipeConstants.RESEARCH_TIME, 20 * GT_RecipeBuilder.MINUTES)
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(AssemblyLine);

        // Target Holder
        GameRegistry.addShapedRecipe(
            new ItemStack(LanthItemList.TARGET_HOLDER),
            new Object[] { "MCM", "MHM", "MCM", 'M', WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 1), 'H',
                ItemList.Hull_LuV.get(1), 'C', ItemList.Conveyor_Module_LuV.get(1) });

        GameRegistry.addShapedRecipe(

            new ItemStack(LanthItemList.FOCUS_HOLDER),
            new Object[] { "MCM", "R R", "MHM", 'M', WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plateDense, 1), 'C',
                ItemList.Conveyor_Module_LuV.get(1), 'R', ItemList.Robot_Arm_LuV.get(1), 'H',
                ItemList.Hull_LuV.get(1) });

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
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(8000),
                Materials.Gold.getMolten(2000),
                Materials.Argon.getGas(1000))
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                new Object[] { OrePrefixes.circuit.get(Materials.IV), 4 },
                ItemList.Robot_Arm_LuV.get(2),
                ItemList.Robot_Arm_LuV.get(2),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 2))
            .itemOutputs(new ItemStack(LanthItemList.TARGET_RECEPTACLE_CASING))
            .metadata(GT_RecipeConstants.RESEARCH_ITEM, ItemList.Circuit_Silicon_Wafer.get(1))
            .metadata(GT_RecipeConstants.RESEARCH_TIME, 20 * GT_RecipeBuilder.MINUTES)
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(AssemblyLine);

        // Capillary Exchange
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 8),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Copper, 2),
                Materials.Titanium.getPlates(6),
                GT_Utility.copyAmount(4, insulator),
                ItemList.Electric_Pump_LuV.get(1),
                Materials.Silver.getDust(2))
            .fluidInputs(Materials.Silicone.getMolten(288L))
            .itemOutputs(new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Mu-metal lattice
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.wireFine, 12),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenSteel, 4))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .itemOutputs(new ItemStack(LanthItemList.MM_LATTICE, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Shielded Accelerator Glass
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.MM_LATTICE, 4))
            .fluidInputs(Materials.BorosilicateGlass.getMolten(144))
            .itemOutputs(new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(fluidSolidifierRecipes);

        // Beamline Pipe
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 4),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenCarbide, 4),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TungstenCarbide, 8),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 4),
                GT_Utility.getIntegratedCircuit(7))
            .fluidInputs(Materials.SolderingAlloy.getMolten(144))
            .itemOutputs(LanthItemList.BEAMLINE_PIPE)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Beamline Input Hatch
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(6000),
                Materials.Argon.getGas(1000),
                Materials.Helium.getGas(2000)

            )
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 2 },
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 2),
                ItemList.Electric_Pump_LuV.get(1),
                LanthItemList.BEAMLINE_PIPE,
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 4)

            )
            .itemOutputs(LanthItemList.LUV_BEAMLINE_INPUT_HATCH)
            .metadata(GT_RecipeConstants.RESEARCH_ITEM, ItemList.Hatch_Input_LuV.get(1))
            .metadata(GT_RecipeConstants.RESEARCH_TIME, 30 * GT_RecipeBuilder.MINUTES)
            .duration(2 * GT_RecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Beamline Output Hatch
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.SolderingAlloy.getMolten(8000),
                Materials.Argon.getGas(1000),
                Materials.Helium.getGas(6000)

            )
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 6 },
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 4),
                ItemList.Electric_Pump_LuV.get(2),
                ItemList.Electric_Motor_LuV.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 8),
                LanthItemList.BEAMLINE_PIPE,
                WerkstoffMaterialPool.MuMetal.get(OrePrefixes.plate, 8)

            )
            .itemOutputs(LanthItemList.LUV_BEAMLINE_OUTPUT_HATCH)
            .metadata(GT_RecipeConstants.RESEARCH_ITEM, ItemList.Hatch_Output_LuV.get(1))
            .metadata(GT_RecipeConstants.RESEARCH_TIME, 40 * GT_RecipeBuilder.MINUTES)
            .duration(4 * GT_RecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Chloroform.getFluid(1000), Materials.HydrofluoricAcid.getFluid(3000))
            .fluidOutputs(
                WerkstoffMaterialPool.Fluoroform.getFluidOrGas(1000),
                Materials.HydrochloricAcid.getFluid(3000))
            .duration(15 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        /*
         * GT_Values.RA.stdBuilder()
         * .fluidInputs(Materials.Nitrogen.getPlasma(4000), Materials.Silane.getFluid(2000))
         * .fluidOutputs(WerkstoffMaterialPool.NitrogenPlasmaSilaneMix.getFluidOrGas(6000))
         * .duration(20 * GT_RecipeBuilder.SECONDS)
         * .eut(7680)
         * .addTo(mixerNonCellRecipes);
         */

        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Nitrogen.getPlasma(4000))
            .itemInputs(Materials.Silane.getCells(2))
            .fluidOutputs(WerkstoffMaterialPool.NitrogenPlasmaSilaneMix.getFluidOrGas(6000))
            .duration(20 * GT_RecipeBuilder.SECONDS)
            .eut(7680)
            .noOptimize()
            .addTo(mixerRecipes);

        // NB: http://www.smfl.rit.edu/pdf/process/process_nitride_etch_paper.pdf
        // Reactive Ion Etchant
        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.Fluoroform.getFluidOrGas(3000))
            .itemInputs(Materials.Oxygen.getCells(4))
            .fluidOutputs(WerkstoffMaterialPool.FluoroformOxygenMix.getFluidOrGas(5000))
            .itemOutputs(Materials.Empty.getCells(4))
            .duration(15 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Iron.getDust(2), Materials.NetherQuartz.getPlates(1))
            .itemOutputs(new ItemStack(LanthItemList.IRON_COATED_QUARTZ))
            .duration(10 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Masks
        // Quartz + Fe2O3 T1
        // " + Cr T2
        //
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.IRON_COATED_QUARTZ))
            .fluidInputs(Materials.Oxygen.getGas(1000))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK1)))
            .outputChances(10000)
            .requiresCleanRoom()
            .duration(144 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(autoclaveRecipes);

        /*
         * Unsure what was intended with this recipe?
         * GT_Values.RA.stdBuilder()
         * .itemInputs(new ItemStack(LanthItemList.IRON_COATED_QUARTZ), Materials.Chrome.getDust(1))
         * .fluidInputs(Materials.Oxygen.getGas(1000))
         * .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK2)))
         * .outputChances(10000).requiresCleanRoom().duration(12 * SECONDS).eut(7980).addTo(autoclaveRecipes);
         */

        // Grow the first silicon
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Glass.getPlates(1))
            .fluidInputs(Materials.Silane.getGas(4000))
            .itemOutputs(new ItemStack(LanthItemList.SUBSTRATE_PRECURSOR))
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(autoclaveRecipes);

        // Now to deposit nitride
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.SUBSTRATE_PRECURSOR))
            .fluidInputs(WerkstoffMaterialPool.NitrogenPlasmaSilaneMix.getFluidOrGas(4000))
            .fluidOutputs(Materials.Nitrogen.getGas(3000))
            .itemOutputs(new ItemStack(LanthItemList.MASK_SUBSTRATE), Materials.Empty.getCells(2))
            .duration(30 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .specialValue(3200)
            .requiresCleanRoom()
            .addTo(blastFurnaceRecipes);

        /*
         * GT_Values.RA.stdBuilder().itemInputs(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicon, 1))
         * .fluidInputs(Materials.SiliconTetrachloride.getFluid(3000), Materials.Ammonia.getFluid(4000))
         * .fluidOutputs(Materials.HydrochloricAcid.getFluid(12000))
         * .itemOutputs(WerkstoffMaterialPool.SiliconNitride.get(OrePrefixes.plate)) .duration(GT_RecipeBuilder.SECONDS
         * * 30) .eut(TierEU.EV) .addTo(GT_Recipe.GT_Recipe_Map.sPlasmaArcFurnaceRecipes);
         */

        for (ItemStack lens : OreDictionary.getOres("craftingLensYellow")) {

            GT_Values.RA.stdBuilder()
                .itemInputs(GT_Utility.copyAmount(0, lens), new ItemStack(LanthItemList.MASK_SUBSTRATE))
                .itemOutputs(new ItemStack(LanthItemList.MASKED_MASK))
                .duration(30 * GT_RecipeBuilder.SECONDS)
                .eut(TierEU.RECIPE_IV)
                .requiresCleanRoom()
                .addTo(WaferEngravingRecipes);

        }

        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.MASKED_MASK))
            .fluidInputs(WerkstoffMaterialPool.FluoroformOxygenMix.getFluidOrGas(4000))
            .itemOutputs(new ItemStack(LanthItemList.ETCHED_MASK_1))
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(autoclaveRecipes);

        // Etch pt. 2 with LiCl
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Argon.getGas(1000))
            .itemInputs(
                new ItemStack(LanthItemList.MASKED_MASK),
                MyMaterial.lithiumChloride.get(OrePrefixes.dust, 2),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PotassiumHydroxideDust", 4))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK2)))
            .duration(80 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .specialValue(2400)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                new ItemStack(LanthItemList.ETCHED_MASK_1),
                Materials.Sodium.getDust(1),
                Materials.Chrome.getPlates(1),
                Materials.Gold.getPlates(2))
            .fluidInputs(Materials.Hydrogen.getGas(1000))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK3)))
            .duration(2 * GT_RecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .specialValue(3600)
            .addTo(blastFurnaceRecipes);

        for (MaskList mask : MaskList.values()) {

            MaskList maskIngredient = mask.getPrecursor();
            Dyes lensColour = mask.getLensColour();

            if (maskIngredient == null) continue;

            if (mask.getLensColour() == null) { // CR Recipe

                if (mask == MaskList.PPIC) {

                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.NPIC)),
                            Materials.IndiumGalliumPhosphide.getDust(64))
                        .fluidInputs(Materials.Sunnarium.getMolten(1440L))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PPIC)))
                        .duration(60 * GT_RecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical); // This is incredibly boring and doesn't make much sense, fix at some
                                                   // point. Maybe engrave again from precursor?

                } else if (mask == MaskList.HPIC) {
                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.PIC)),
                            Materials.IndiumGalliumPhosphide.getDust(2))
                        .fluidInputs(Materials.VanadiumGallium.getMolten(288L))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.HPIC)))
                        .duration(60 * GT_RecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.UHPIC) {

                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.HPIC)),
                            Materials.IndiumGalliumPhosphide.getDust(8))
                        .fluidInputs(Materials.Naquadah.getMolten(576L))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.UHPIC)))
                        .duration(60 * GT_RecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.NCPU) {

                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.CPU)),
                            GT_Utility.copyAmount(16, Ic2Items.carbonFiber))
                        .fluidInputs(Materials.Glowstone.getMolten(576L))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.NCPU)))
                        .duration(60 * GT_RecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.QBIT) {

                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.NCPU)),
                            Materials.IndiumGalliumPhosphide.getDust(1))
                        .fluidInputs(Materials.Radon.getGas(50L))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.QBIT)))
                        .duration(60 * GT_RecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                    GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(LanthItemList.maskMap.get(MaskList.NCPU)), ItemList.QuantumEye.get(2))
                        .fluidInputs(Materials.GalliumArsenide.getMolten(288L))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.QBIT)))
                        .duration(45 * GT_RecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                }

                continue;

            }

            if (mask == MaskList.NAND) {

                // Very copy-paste heavy, could possibly offload most of this into one case and just assign an
                // otherIngredient variable or something, wouldn't save much space though. Plus: lazy
                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                        GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderPearl, 1)))
                    .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                    .requiresCleanRoom()
                    .duration(120 * SECONDS)
                    .eut(mask.getEngraverEUt())
                    .addTo(WaferEngravingRecipes);

            } else if (mask == MaskList.NOR) {

                GT_Values.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                        GT_Utility.copyAmount(0, GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderEye, 1)))
                    .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                    .requiresCleanRoom()
                    .duration(120 * SECONDS)
                    .eut(mask.getEngraverEUt())
                    .addTo(WaferEngravingRecipes);

            }

            else {

                for (ItemStack lens : OreDictionary.getOres("craftingLens" + lensColour.mName.replace(" ", ""))) {

                    if (lens == null) continue;

                    GT_Values.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                            GT_Utility.copyAmount(0, lens))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                        .requiresCleanRoom()
                        .duration(120 * SECONDS)
                        .eut(mask.getEngraverEUt())
                        .addTo(WaferEngravingRecipes);

                }
            }
        }

    }

    public static void loadGeneral() {

        /* ZIRCONIUM */
        // ZrCl4
        // ZrO2 + 4HCl = ZrCl4 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1), WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 3))
            .itemOutputs(WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4000))
            .fluidOutputs(Materials.Water.getFluid(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ZrCl4-H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr
        // ZrCl4·H2O + 2Mg = Zr + 2MgCl2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2))
            .itemOutputs(
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.ingotHot, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 6))
            .fluidInputs(WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4500)
            .addTo(blastFurnaceRecipes);

        /* HAFNIUM */
        // HfCl4
        // HfO2 + 4HCl = HfCl4 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1), WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 3))
            .itemOutputs(WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4000))
            .fluidOutputs(Materials.Water.getFluid(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HfCl4-H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LP-Hf
        // HfCl4 + 2Mg = ??Hf?? + 2MgCl2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2))
            .itemOutputs(
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 6))
            .fluidInputs(WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 2700)
            .addTo(blastFurnaceRecipes);

        // HfI4
        // ??Hf?? + 4I = HfI4
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .fluidInputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.Iodine.get(OrePrefixes.dust, 4))
            .itemOutputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Hf
        // HfI4 = Hf + 4I
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(12),
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .itemOutputs(
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 1),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dustTiny, 1))
            .fluidOutputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(4000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Hf * 9
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(13),
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 45))
            .itemOutputs(
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 9),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dust, 1))
            .fluidOutputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(36000))
            .duration(4 * MINUTES + 30 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Zirconia-Hafnia
        // ??HfZr?? = HfO2 + ZrO2
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 3),
                WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 3))
            .eut(TierEU.RECIPE_EV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        // Ammonium Nitrate
        // HNO3 + NH3 = NH4NO3
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(12), Materials.NitricAcid.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Ammonia.getGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(chemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(12))
            .fluidInputs(Materials.NitricAcid.getFluid(1000), Materials.Ammonia.getGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(multiblockChemicalReactorRecipes);

        // IODINE-START
        // SeaweedAsh
        GT_ModHandler.addSmeltingRecipe(
            GT_ModHandler.getModItem(PamsHarvestCraft.ID, "seaweedItem", 1),
            WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dustSmall, 1));

        // SeaweedConcentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Calcite.getDust(1))
            .fluidInputs(Materials.DilutedSulfuricAcid.getFluid(1200))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(1200))
            .duration(30 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);
        // SeaweedConcentrate * 4
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dust, 4))
            .itemOutputs(Materials.Calcite.getDust(2))
            .fluidInputs(Materials.DilutedSulfuricAcid.getFluid(2400))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(2400))
            .duration(60 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);

        // Iodine
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Benzene.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(2000))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedByproducts.getFluidOrGas(200))
            .eut(TierEU.RECIPE_HV)
            .duration(38 * SECONDS)
            .addTo(centrifugeRecipes);

        // IODINE-END

        // 2MnO2 + 2KOH + KClO3 = 2KMnO4 + H2O + KCl
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Pyrolusite.getDust(6),
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PotassiumHydroxideDust", 6),
                WerkstoffMaterialPool.PotassiumChlorate.get(OrePrefixes.dust, 5))
            .itemOutputs(
                WerkstoffMaterialPool.PotassiumPermanganate.get(OrePrefixes.dust, 12),
                Materials.RockSalt.getDust(2))
            .fluidOutputs(Materials.Water.getFluid(1000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Mn + 2O = MnO2
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Manganese.getDust(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Pyrolusite.getDust(3))
            .fluidInputs(Materials.Oxygen.getGas(2000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 6KOH + 6Cl = KClO3 + 5KCl + 3H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PotassiumHydroxideDust", 18),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(
                Materials.RockSalt.getDust(10),
                WerkstoffMaterialPool.PotassiumChlorate.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.Chlorine.getGas(6000))
            .fluidOutputs(Materials.Water.getFluid(3000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluorosilicate
        // 2NaCl + H2SiF6 = 2HCl + Na2SiF6
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2), Materials.Salt.getDust(4))
            .itemOutputs(Materials.HydrochloricAcid.getCells(2))
            .fluidInputs(WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // La + 6HCl = LaCl3 + 3H
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Lanthanum.getDust(1), GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3000))
            .itemOutputs(WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dust, 4))
            .fluidOutputs(Materials.Hydrogen.getGas(3000))
            .duration(10 * SECONDS)
            .eut(480)
            .addTo(UniversalChemical);

        // Lanthanum Oxide
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(3000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(6000))
            .itemInputs(WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dust, 4))
            .itemOutputs(WerkstoffMaterialPool.LanthanumOxide.get(OrePrefixes.dust, 1))
            .duration(10 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Boron Trioxide
        GT_Values.RA.stdBuilder()
            .fluidInputs(FluidRegistry.getFluidStack("boricacid", 2000))
            .fluidOutputs(new FluidStack(FluidRegistry.WATER, 3000))
            .itemOutputs(WerkstoffMaterialPool.BoronTrioxide.get(OrePrefixes.dust, 1))
            .duration(15 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // Boron Trichloride
        GT_Values.RA.stdBuilder()
            .fluidInputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(3000))
            .fluidOutputs(WerkstoffMaterialPool.BoronTrichloride.getFluidOrGas(2000))
            .itemInputs(WerkstoffMaterialPool.BoronTrioxide.get(OrePrefixes.dust, 1), Materials.Empty.getCells(3))
            .itemOutputs(Materials.CarbonMonoxide.getCells(3))
            .duration(10 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Lanthanum Hexaboride
        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.BoronTrichloride.getFluidOrGas(8000))
            .fluidOutputs(FluidRegistry.getFluidStack("boricacid", 1000))
            .itemInputs(WerkstoffMaterialPool.LanthanumOxide.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.LanthanumHexaboride.get(OrePrefixes.gemFlawless))
            .duration(60 * GT_RecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);
    }

    public static void loadLanthanideRecipes() {
        // Methanol
        // CH4O + CO + 3O =V2O5= H2C2O4 + H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1))
            .fluidInputs(
                Materials.Methanol.getFluid(1000),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.Oxygen.getGas(3000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(1000), Materials.Water.getFluid(1000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9), MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .fluidInputs(
                Materials.Methanol.getFluid(9000),
                Materials.CarbonMonoxide.getGas(9000),
                Materials.Oxygen.getGas(27000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(9000), Materials.Water.getFluid(9000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        // Ethanol
        // C2H6O + 5O =V2O5= H2C2O4 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1))
            .itemOutputs()
            .fluidInputs(Materials.Ethanol.getFluid(1000), Materials.Oxygen.getGas(5000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(1000), Materials.Water.getFluid(2000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9), MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Ethanol.getFluid(9000), Materials.Oxygen.getGas(45000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(9000), Materials.Water.getFluid(18000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Oxalate
        // 2CeCl3 + 3H2C2O4 = 6HCl + Ce2(C2O4)3
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 8))
            .itemOutputs(WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 5))
            .fluidInputs(MyMaterial.oxalate.getFluidOrGas(3000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(6000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // Cerium
        // Ce2O3 = 2Ce + 3O
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust, 5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 2))
            .fluidOutputs(Materials.Oxygen.getGas(3000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // CHAIN BEGIN
        // MONAZITE
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Monazite, 2))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(400))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .eut(1920)
            .duration(400)
            .metadata(COIL_HEAT, 800)
            .addTo(digesterRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Water.getFluid(10000),
                WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(1000))
            .itemInputs(GT_Utility.getIntegratedCircuit(1), Materials.Saltpeter.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(11000))
            .itemOutputs(
                WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dustTiny, 4),
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 1),
                Materials.Monazite.getDustTiny(2))
            .eut(TierEU.RECIPE_HV)
            .duration(900)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .noOptimize()
            .addTo(dissolutionTankRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Water.getFluid(90000),
                WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(9000))
            .itemInputs(GT_Utility.getIntegratedCircuit(9), Materials.Saltpeter.getDust(9))
            .fluidOutputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(99000))
            .itemOutputs(
                WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 9),
                Materials.Monazite.getDust(2))
            .eut(TierEU.RECIPE_HV)
            .duration(8100)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .noOptimize()
            .addTo(dissolutionTankRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(1000))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1),
                Materials.SiliconDioxide.getDust(1),
                Materials.Rutile.getDust(1),
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1),
                Materials.Ilmenite.getDust(1))
            .outputChances(90_00, 75_00, 20_00, 5_00, 20_00)
            .duration(20 * SECONDS)
            .eut(240)
            .noOptimize()
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Water.getFluid(6000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(7000))
            .duration(24 * SECONDS)
            .eut(400)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(13))
            .itemOutputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dustTiny, 3))
            .fluidInputs(
                WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(1000),
                WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(200))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(14))
            .itemOutputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 3))
            .fluidInputs(
                WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(9000),
                WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1800))
            .duration(3 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1))
            .outputChances(9000, 7000)
            .duration(30 * SECONDS)
            .eut(256)
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1500)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust))
            .itemOutputs(Materials.Thorium.getDust(1), Materials.Phosphate.getDust(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(320))
            .duration(6 * SECONDS)
            .eut(240)
            .addTo(chemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1))
            .outputChances(9000, 5000, 4000)
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.NeutralizedUraniumFiltrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(100))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        GT_Values.RA.stdBuilder()
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

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.NitricAcid.getFluid(500))
            .fluidOutputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 3))
            .fluidInputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(2000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1000))
            .itemOutputs(WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1))
            .outputChances(11_11)
            .fluidOutputs(WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1000))
            .duration(20 * SECONDS)
            .eut(240)
            .noOptimize()
            .addTo(sifterRecipes);

        // BEGIN Cerium
        // Cerium-rich mixture + 3HCl = CeCl3 + Monazite (to allow cerium processing without bastnazite/monazite)
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 15))
            .itemOutputs(WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 1), Materials.Monazite.getDust(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(750))
            .fluidOutputs(Materials.Water.getFluid(750))
            .duration(25 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // CeO2 + 3NH4Cl + H = 3NH3 + CeCl3 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 3),
                WerkstoffLoader.AmmoniumChloride.get(OrePrefixes.cell, 3))
            .itemOutputs(WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 4), Materials.Ammonia.getCells(3))
            .fluidInputs(Materials.Hydrogen.getGas(1000))
            .fluidOutputs(Materials.Water.getGas(2000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // Ce2(C2O4)3 + 3C = Ce2O3 + 9CO
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 5), Materials.Carbon.getDust(3))
            .itemOutputs(WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust, 5))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(9000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 800)
            .addTo(blastFurnaceRecipes);

        // END Cerium (NMLC)

        GT_Values.RA.stdBuilder()
            .itemOutputs(WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1000))
            .duration(5 * SECONDS)
            .eut(240)
            .noOptimize()
            .addTo(vacuumFreezerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.EuropiumIIIOxide.get(OrePrefixes.dust, 5))
            .outputChances(9000, 500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(electroMagneticSeparatorRecipes);

        // 5Eu2O3 + Eu = 4EuO
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumIIIOxide.get(OrePrefixes.dust, 5), Materials.Europium.getDust(1))
            .itemOutputs(WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 6))
            .duration(15 * SECONDS)
            .eut(8400)
            .addTo(UniversalChemical);

        // 4 EuO = 2 Eu + 2O2
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Europium.getDust(1))
            .fluidOutputs(Materials.Oxygen.getGas(1000L))
            .duration(15 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        // EuS = Eu + S
        // TODO old recipe. for compat only. remove material and recipe half a year later, i.e. after September 2023.
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumSulfide.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Europium.getDust(1), Materials.Sulfur.getDust(1))
            .duration(30 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Chlorine.getGas(1000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Salt.getDust(1),
                WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2))
            .itemOutputs(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 3))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .duration(20 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 8))
            .itemOutputs(WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 6))
            .fluidOutputs(Materials.Chloromethane.getGas(800))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * MINUTES + 15 * SECONDS)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 9))
            .itemOutputs(Materials.Samarium.getDust(6), Materials.Gadolinium.getDust(3))
            .outputChances(10000, 10000)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sifterRecipes);

        // BASTNASITE (god help me)
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 2))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(400))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .eut(TierEU.RECIPE_EV)
            .duration(400)
            .metadata(COIL_HEAT, 800)
            .addTo(digesterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(
                WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(1000),
                GT_ModHandler.getSteam(1000))
            .fluidOutputs(WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.getFluidOrGas(2000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(crackingRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(6),
                WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(320))
            .fluidOutputs(WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1320))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Water.getFluid(10000),
                WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1000))
            .itemInputs(Materials.Saltpeter.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(11000))
            .itemOutputs(Gangue.get(OrePrefixes.dust, 1))
            .eut(TierEU.RECIPE_EV)
            .duration(1000)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .noOptimize()
            .addTo(dissolutionTankRecipes);

        GT_Values.RA.stdBuilder()
            .itemOutputs(
                Materials.SiliconDioxide.getDust(1),
                Materials.Rutile.getDust(1),
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1),
                Materials.Ilmenite.getDust(1))
            .outputChances(90_00, 75_00, 10_00, 5_00)
            .fluidInputs(WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(400))
            .eut(240)
            .duration(20 * SECONDS)
            .noOptimize()
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(600)
            .metadata(COIL_HEAT, 1400)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Oxygen.getGas(1000))
            .fluidOutputs(Materials.Fluorine.getGas(13))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(7),
                WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Water.getFluid(200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Fluorine.getGas(4000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1))
            .outputChances(100_00, 90_00)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.NitricAcid.getFluid(400))
            .fluidOutputs(WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.getFluidOrGas(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1000))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemOutputs(
                WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .outputChances(80_00, 50_00)
            .fluidInputs(WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedAcetone.getFluidOrGas(750))
            .eut(TierEU.RECIPE_HV)
            .duration(45 * SECONDS)
            .addTo(centrifugeRecipes);

        // Nd RE
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 2))
            .itemOutputs(
                WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.NeodymiumOxide.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2000))
            .duration(45 * SECONDS)
            .eut(800)
            .addTo(UniversalChemical);

        // Sm RE
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 2))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(2000))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 8),
                Materials.Calcium.getDust(4))
            .itemOutputs(
                Materials.Holmium.getDust(1),
                WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 4))
            .fluidOutputs(WerkstoffMaterialPool.CalciumFluoride.getFluidOrGas(12000))
            .duration(1 * MINUTES + 20 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 1),
                BotWerkstoffMaterialPool.AmmoniumNitrate.get(OrePrefixes.dust, 9))
            .itemOutputs(WerkstoffMaterialPool.NitratedSamariumTerbiumMixture.get(OrePrefixes.dust, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
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

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2),
                Materials.Calcium.getDust(3))
            .itemOutputs(
                WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1),
                Materials.TricalciumPhosphate.getDust(5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 6))
            .itemOutputs(Materials.Samarium.getDust(1), WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 2))
            .outputChances(90_00, 80_00)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(centrifugeRecipes);

        // TODO UV Tier Ion Extracting Method

        // Lanthanum Part
        // Digester to produce Lanthanum Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Lanthanum, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(LanthanumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(LanthanumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(LanthanumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                LanthanumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Praseodymium Part
        // Digester to produce Praseodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Praseodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(PraseodymiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(PraseodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(PraseodymiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                PraseodymiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Part
        // Digester to produce Cerium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Cerium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(CeriumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(CeriumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(CeriumExtractingNanoResin.getFluidOrGas(1000), CeriumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Neodymium Part
        // Digester to produce Neodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Neodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(NeodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Neodymium Part
        // Digester to produce Neodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Neodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(NeodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Promethium Part
        // Digester to produce Neodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Promethium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(PromethiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Promethium Part
        // Digester to produce Promethium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Promethium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(PromethiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Samarium Part
        // Digester to produce Samarium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Samarium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(SamariumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(SamariumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                SamariumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Europium Part
        // Digester to produce Europium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Europium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(EuropiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(EuropiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(EuropiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                EuropiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Gadolinium Part
        // Digester to produce Gadolinium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Gadolinium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(GadoliniumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GadoliniumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(GadoliniumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                GadoliniumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Terbium Part
        // Digester to produce Terbium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Terbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(TerbiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(TerbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(TerbiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(TerbiumExtractingNanoResin.getFluidOrGas(1000), TerbiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dysprosium Part
        // Digester to produce Dysprosium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Dysprosium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(DysprosiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(DysprosiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(DysprosiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                DysprosiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Holmium Part
        // Digester to produce Holmium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Holmium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(HolmiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(HolmiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(HolmiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(HolmiumExtractingNanoResin.getFluidOrGas(1000), HolmiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Erbium Part
        // Digester to produce Erbium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Erbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(ErbiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ErbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(ErbiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(ErbiumExtractingNanoResin.getFluidOrGas(1000), ErbiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Thulium Part
        // Digester to produce Thulium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Thulium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(ThuliumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ThuliumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(ThuliumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(ThuliumExtractingNanoResin.getFluidOrGas(1000), ThuliumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ytterbium Part
        // Digester to produce Ytterbium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Ytterbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(YtterbiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(YtterbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(YtterbiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                YtterbiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lutetium Part
        // Digester to produce Lutetium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Lutetium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(LutetiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(LutetiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(LutetiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                LutetiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ion Extracting Process to produce Rare Earth Element (example Samarium) by Nano Resin
        // Get Extracting Nano Resin

        // Lanthanum
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Lanthanum.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(LanthanumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Praseodymium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Praseodymium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(PraseodymiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Cerium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Cerium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(CeriumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Neodymium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Neodymium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(NeodymiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Promethium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Promethium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(PromethiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Sm
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Samarium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(SamariumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Europium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Europium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(EuropiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Gadolinium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Gadolinium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(GadoliniumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Terbium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Terbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(TerbiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Dysprosium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Dysprosium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(DysprosiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Holmium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Holmium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(HolmiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Erbium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Erbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(ErbiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Thulium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Thulium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(ThuliumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Ytterbium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Ytterbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(YtterbiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Lutetium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.MysteriousCrystalLens", 0),
                Materials.Lutetium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(LutetiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // TODO Electrolyzer recycle Nano Resin and produce molten rare earth metal,

        // La
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledLanthanumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                LanthanumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lanthanum, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Pr
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                PraseodymiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Praseodymium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Ce
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledCeriumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                CeriumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Cerium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Nd
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                NeodymiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Neodymium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Po
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledPromethiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                PromethiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Promethium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Sm
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledSamariumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                SamariumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Samarium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Eu
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledEuropiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                EuropiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Europium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Ga
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                GadoliniumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Gadolinium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Tb
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledTerbiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                TerbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Terbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Dy
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                DysprosiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Dysprosium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Ho
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledHolmiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                HolmiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Holmium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Er
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledErbiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                ErbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Erbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Tm
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledThuliumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                ThuliumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Thulium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Yb
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                YtterbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Ytterbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Lu
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledLutetiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                LutetiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lutetium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // TODO ChlorinitedRareEarthConcentrate process with every 15 Rare Earth Extracting Nano Resin

        // La
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Pr
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ce
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Nd
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Pm
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Sm
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Eu
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ga
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tb
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dy
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ho
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Er
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tm
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Yb
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lu
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // TODO Samarium Ore Concentrate Dust Processing Line Start

        // 16 SmOreDust + 200L NitricAcid =EV@10s= 800L MuddySamariumRareEarthSolution + 1 ?ThP?ConcentrateDust
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumOreConcentrate.get(OrePrefixes.dust, 16))
            .fluidInputs(Materials.NitricAcid.getFluid(200))
            .itemOutputs(ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(800))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1 CrushedSamariumOre = 3 SamariumOreConcentrate in process
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Samarium, 8))
            .fluidInputs(Materials.NitricAcid.getFluid(300))
            .itemOutputs(ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 3))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(1200))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1B MuddySmSolution + 1B NitricAcid =EV@10s= 2B SamariumRareEarthMud + 0.8 CeriumDioxide + 0.6
        // CeriumRichMixture(CeriumOreConcentrate)
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.NitricAcid.getFluid(1000), MuddySamariumRareEarthSolution.getFluidOrGas(1000))
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 1), CeriumOreConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(2000))
            .metadata(DISSOLUTION_TANK_RATIO, 1)
            .outputChances(8000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(dissolutionTankRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9))
            .fluidInputs(Materials.NitricAcid.getFluid(9000), MuddySamariumRareEarthSolution.getFluidOrGas(9000))
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 9), CeriumOreConcentrate.get(OrePrefixes.dust, 9))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(18000))
            .metadata(DISSOLUTION_TANK_RATIO, 1)
            .outputChances(8000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_IV)
            .duration(300)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.NitricAcid.getFluid(1000), MuddySamariumRareEarthSolution.getFluidOrGas(1000))
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 1))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(1000))
            .outputChances(5000)
            .eut(TierEU.RECIPE_EV)
            .duration(300)
            .addTo(multiblockChemicalReactorRecipes);

        // 1B SamariumRareEarthMud + 9B water =EV@30s= 10B DilutedSamariumRareEarthSolution
        // + (90% + 60%) NeodymiumREConcentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Water.getFluid(9000), SamariumRareEarthMud.getFluidOrGas(1000))
            .itemOutputs(
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(10000))
            .metadata(DISSOLUTION_TANK_RATIO, 9)
            .outputChances(9000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .duration(600)
            .addTo(dissolutionTankRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9))
            .fluidInputs(Materials.Water.getFluid(81000), SamariumRareEarthMud.getFluidOrGas(9000))
            .itemOutputs(
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 9),
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 9))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(90000))
            .metadata(DISSOLUTION_TANK_RATIO, 9)
            .outputChances(9000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_IV)
            .duration(900)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(SamariumRareEarthMud.getFluidOrGas(1000), Materials.Water.getFluid(16000))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(8000))
            .eut(TierEU.RECIPE_EV)
            .duration(1200)
            .addTo(multiblockChemicalReactorRecipes);

        // 2B DilutedSamariumRareEarthSolution + 3B Oxalate
        // =EV@10s=
        // 5 ImpureSamariumOxalate + 50L MuddySamariumRareEarthSolution + 0.1*2 LepersonniteDust
        // LepersonniteDust -> DephosphatedSamariumConcentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(13))
            .fluidInputs(DilutedSamariumRareEarthSolution.getFluidOrGas(2000), MyMaterial.oxalate.getFluidOrGas(3000))
            .itemOutputs(
                SamariumOxalate.get(OrePrefixes.dust, 5),
                DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 3))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(50))
            .outputChances(10000, 1000)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // 5 ImpureSamariumOxalate + 6B HCL = 8 ImpureSamariumChloride + 6B CO
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumOxalate.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(6000))
            .itemOutputs(SamariumChloride.get(OrePrefixes.dust, 8))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(6000))
            .eut(960)
            .duration(10 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        /**
         * ImpureSamariumChloride has 2 method to process 1. In IV-LuV, fix with NcCL then use electrolyzer to process
         * the mixture, get Samarium dust & Chlorine & Sodium. 2. In ZPM, put molten ImpureSamariumChloride and
         * LanthanumDust in Distillation Tower to get molten Samarium and impure Lanthanum Chloride.
         */

        // 2 ImpureSamariumChloride + 1 NaCl =LV@5s= 3 SamariumChlorideSodiumChlorideBlend
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumChloride.get(OrePrefixes.dust, 2), Materials.Salt.getDust(1))
            .itemOutputs(SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 3))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumChloride.get(OrePrefixes.dust, 2), Materials.Sodium.getDust(1))
            .itemOutputs(SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 3))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerNonCellRecipes);

        // 6 SamariumChlorideSodiumChlorideBlend =IV@1s= 1 SamariumDust + 1 SodiumDust + 2/9 RarestEarthResidue + 4B
        // Chlorine
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 6))
            .itemOutputs(
                Materials.Samarium.getDust(1),
                Materials.Sodium.getDust(1),
                RarestEarthResidue.get(OrePrefixes.dustTiny, 2))
            .fluidOutputs(Materials.Chlorine.getGas(4000))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * SECONDS)
            .addTo(electrolyzerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(9),
                SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 54))
            .itemOutputs(
                Materials.Samarium.getDust(9),
                Materials.Sodium.getDust(9),
                RarestEarthResidue.get(OrePrefixes.dust, 2))
            .fluidOutputs(Materials.Chlorine.getGas(36000))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * SECONDS)
            .addTo(electrolyzerRecipes);

        // ZPM molten distilling method

        // melt ImpureSamariumChloride
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumChloride.get(OrePrefixes.dust, 1))
            .fluidOutputs(SamariumChloride.getMolten(144))
            .eut(TierEU.RECIPE_EV)
            .duration(24)
            .addTo(fluidExtractionRecipes);

        // distill with LanthanumDust 36*144L moltenSmCl3 = 16*144L moltenSm + 27B Cl
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Lanthanum.getDust(9))
            .itemOutputs(ImpureLanthanumChloride.get(OrePrefixes.dust, 36))
            .fluidInputs(SamariumChloride.getMolten(5184))
            .fluidOutputs(Materials.Samarium.getMolten(2304))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(distillationTowerRecipes);

        // Centrifuge ImpureLanthanumChlorideDust
        GT_Values.RA.stdBuilder()
            .itemInputs(ImpureLanthanumChloride.get(OrePrefixes.dust, 36))
            .itemOutputs(LanthaniumChloride.get(OrePrefixes.dust, 36), RarestEarthResidue.get(OrePrefixes.dust, 5))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * SECONDS)
            .addTo(centrifugeRecipes);

        /**
         * DephosphatedSamariumConcentrate has a simple and not shit process. Just burn in furnace, then use
         * electolyzer.
         */
        GameRegistry.addSmelting(
            DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1),
            SamariumOxide.get(OrePrefixes.dustTiny, 2),
            114);
        GT_Values.RA.stdBuilder()
            .itemInputs(DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(SamariumOxide.get(OrePrefixes.dust, 1))
            .metadata(COIL_HEAT, 1200)
            .eut(514)
            .duration(2 * SECONDS)
            .addTo(blastFurnaceRecipes);

    }

    public static void addRandomChemCrafting() {

        // PTMEG Elastomer
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Butanediol.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(4000))
            .fluidOutputs(WerkstoffMaterialPool.PTMEGElastomer.getMolten(4000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Toluene Tetramethyl Diisocyanate
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.TolueneDiisocyanate.get(OrePrefixes.cell, 3),
                Materials.Hydrogen.getCells(2))
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(2000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // PTHF
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.TungstophosphoricAcid.get(OrePrefixes.cell, 1),
                Materials.Oxygen.getCells(1))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(144))
            .fluidOutputs(WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(432))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // THF
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.AcidicButanediol.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Ethanol.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(1000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Acidicised Butanediol
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SulfuricAcid.getCells(1))
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.AcidicButanediol.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        // Butanediol
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dustTiny, 1))
            .fluidInputs(Materials.Butane.getGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(9),
                WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Butane.getGas(9000))
            .fluidOutputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(9000))
            .duration(6 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Moly-Te-Oxide Catalyst
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MolybdenumIVOxide.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.TelluriumIVOxide.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dust, 2))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // Tungstophosphoric Acid
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.PhosphoricAcid.getCells(1), Materials.HydrochloricAcid.getCells(24))
            .itemOutputs(Materials.Salt.getDust(24), Materials.Empty.getCells(25))
            .fluidInputs(BotWerkstoffMaterialPool.SodiumTungstate.getFluidOrGas(12000))
            .fluidOutputs(WerkstoffMaterialPool.TungstophosphoricAcid.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(1024)
            .addTo(UniversalChemical);

        // Toluene Diisocyanate
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Diaminotoluene.get(OrePrefixes.cell, 1), Materials.Empty.getCells(3))
            .itemOutputs()
            .fluidInputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(2000))
            .fluidOutputs(WerkstoffMaterialPool.TolueneDiisocyanate.getFluidOrGas(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Diaminotoluene
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Hydrogen.getCells(4))
            .itemOutputs()
            .fluidInputs(WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.Diaminotoluene.getFluidOrGas(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Dinitrotoluene
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.NitricAcid.getCells(2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.Toluene.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Digester Control Block
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1L),
                ItemList.Super_Tank_EV.get(2L),
                ItemList.Electric_Motor_IV.get(4L),
                ItemList.Electric_Pump_IV.get(4L),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Desh, 4L),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(LanthItemList.DIGESTER)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1440))
            .duration(30 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);
        // Dissolution Tank
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1L),
                ItemList.Super_Tank_HV.get(2L),
                ItemList.Electric_Motor_EV.get(4L),
                ItemList.Electric_Pump_EV.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.VibrantAlloy, 4L),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 4L),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(LanthItemList.DISSOLUTION_TANK)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(720))
            .duration(20 * SECONDS)
            .eut(960)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.DilutedAcetone.getFluidOrGas(250))
            .fluidOutputs(Materials.Acetone.getFluid(150))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        // PTMEG Manipulation

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(WerkstoffMaterialPool.PTMEGElastomer.get(OrePrefixes.ingot, 1))
            .fluidInputs(WerkstoffMaterialPool.PTMEGElastomer.getMolten(144))
            .duration(2 * SECONDS)
            .eut(64)
            .addTo(fluidSolidifierRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Plate.get(0L))
            .itemOutputs(WerkstoffMaterialPool.PTMEGElastomer.get(OrePrefixes.plate, 1))
            .fluidInputs(WerkstoffMaterialPool.PTMEGElastomer.getMolten(144))
            .duration(2 * SECONDS)
            .eut(64)
            .addTo(fluidSolidifierRecipes);

        // TODO Cerium-doped Lutetium Aluminium Garnet (Ce:LuAG)
        /**
         * 1/9 Ce + 3 Lu + 5 Sapphire = 8 LuAG Blend 1/9 Ce + 3 Lu + 10 Green Sapphire = 8 LuAG Blend 2/9 Ce + 6 Lu + 25
         * Alumina + 9 Oxygen = 12 LuAG Blend
         *
         * 1 Ce + 60 Lu + 100 Sapphire = 160 LuAG Blend 1 Ce + 60 Lu +200 Green Sapphire = 160 LuAG Blend
         *
         */
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Cerium.getDustTiny(1),
                Materials.Lutetium.getDust(3),
                Materials.Sapphire.getDust(5))
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 8))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Cerium.getDustTiny(1),
                Materials.Lutetium.getDust(3),
                Materials.GreenSapphire.getDust(10))
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 8))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Cerium.getDustTiny(2),
                Materials.Lutetium.getDust(6),
                Materials.Aluminiumoxide.getDust(25))
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 12))
            .fluidInputs(Materials.Oxygen.getGas(9000))
            .eut(TierEU.RECIPE_UV)
            .duration(400)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Cerium.getDust(1),
                Materials.Lutetium.getDust(60),
                Materials.Sapphire.getDust(64),
                Materials.Sapphire.getDust(36))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 32))
            .eut(TierEU.RECIPE_UV)
            .duration(1800)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Cerium.getDust(1),
                Materials.Lutetium.getDust(60),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(8))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 32))
            .eut(TierEU.RECIPE_UV)
            .duration(1800)
            .addTo(mixerRecipes);

        // Get LuAG Crystal seed
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .fluidInputs(Materials.Lutetium.getMolten(144 * 8))
            .outputChances(514)
            .eut(TierEU.RECIPE_UV)
            .duration(500)
            .addTo(autoclaveRecipes);

        // 1 LuAG Blend = 1.1(Og) 1.0(Xe) 0.99(Kr) LuAG in Autoclave
        GT_Values.RA.stdBuilder()
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
        GT_Values.RA.stdBuilder()
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
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(10))
            .outputChances(10000, 100)
            .eut(TierEU.RECIPE_UHV)
            .duration(128)
            .addTo(autoclaveRecipes);
        GT_Values.RA.stdBuilder()
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
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dust, 1))
            .itemOutputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(CeriumDopedLutetiumAluminiumOxygenBlend.getMolten(108))
            .metadata(COIL_HEAT, 9100)
            .eut(TierEU.RECIPE_UHV)
            .duration(5 * SECONDS)
            .addTo(vacuumFurnaceRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .fluidOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.getMolten(144))
            .eut(TierEU.RECIPE_LuV)
            .duration(1 * SECONDS)
            .addTo(fluidExtractionRecipes);

        // 16 Adv Crystal SoC
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensBlue")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(0, itemStack),
                    CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
                .itemOutputs(ItemList.Circuit_Chip_CrystalSoC2.get(16))
                .requiresCleanRoom()
                .eut(160000)
                .duration(40 * SECONDS)
                .addTo(laserEngraverRecipes);
        }

        // 16 Crystal SoC
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensGreen")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(0, itemStack),
                    CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
                .itemOutputs(ItemList.Circuit_Chip_CrystalSoC.get(16))
                .requiresCleanRoom()
                .eut(160000)
                .duration(40 * SECONDS)
                .addTo(laserEngraverRecipes);
        }

    }

    public static void removeCeriumMacerator() {
        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        GT_Log.out.print(Tags.MODID + ": processing macerator recipes");
        for (GT_Recipe recipe : maceratorRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GT_Utility.isStackValid(input)) {
                continue;
            }

            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!(oreName.startsWith("ore") || oreName.startsWith("rawOre") || oreName.startsWith("crushed"))) {
                    continue;
                }

                GT_Recipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;

                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        modified = true;
                        GT_Log.out.println(
                            "in the recipe of '" + recipe.mInputs[0].getDisplayName()
                                + "', replacing Cerium dust by Cerium Rich Mixture dust");
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        modified = true;
                        GT_Log.out.println(
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

        GT_Log.out.print(Tags.MODID + ": macerator recipes done!");
    }

    public static void removeCeriumWasher() {
        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        GT_Log.out.println(Tags.MODID + ": processing orewasher recipes");
        for (GT_Recipe recipe : oreWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GT_Utility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                if (!OreDictionary.getOreName(oreDictID)
                    .startsWith("crushed")) {
                    continue;
                }

                GT_Recipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input: "
                                + input.getDisplayName()
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating ore washer recipes");
        oreWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(oreWasherRecipes::add);
        oreWasherRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": ore washer recipes done!");
    }

    public static void removeCeriumThermalCentrifuge() {

        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        GT_Log.out.println(Tags.MODID + ": processing thermal centrifuge recipes");
        for (GT_Recipe recipe : thermalCentrifugeRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GT_Utility.isStackValid(input)) {
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

                GT_Recipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input "
                                + input.getDisplayName()
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating thermal centrifuge recipes");
        thermalCentrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(thermalCentrifugeRecipes::add);
        thermalCentrifugeRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": thermal centrifuge recipes done!");
    }

    public static void removeCeriumCentrifuge() {

        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);
        GT_Log.out.println(Tags.MODID + ": processing centrifuge recipes");
        for (GT_Recipe recipe : centrifugeRecipes.getAllRecipes()) {
            ItemStack input = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (!GT_Utility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!(oreName.startsWith("dust") && (!oreName.contains("Dephosphated")))) {
                    continue;
                }
                GT_Recipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustTiny(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustTiny, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium tiny dust turned into Cerium Rich Mixture tiny dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustSmall(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustSmall, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium small dust turned into Cerium Rich Mixture small dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustTiny(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustTiny, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate tiny dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustSmall(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustSmall, 1));
                        GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating centrifuge recipes");
        centrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(centrifugeRecipes::add);
        centrifugeRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": centrifuge recipes done!");
    }

    public static void removeCeriumCentrifugeNonCell() {

        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);
        GT_Log.out.println(Tags.MODID + ": processing centrifuge non cell recipes");

        for (GT_Recipe recipe : centrifugeNonCellRecipes.getAllRecipes()) {
            ItemStack input = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (!GT_Utility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!((oreName.startsWith("dust") && (!oreName.contains("Dephosphated"))))) {
                    continue;
                }
                GT_Recipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustTiny(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustTiny, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium tiny dust turned into Cerium Rich Mixture tiny dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustSmall(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustSmall, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium small dust turned into Cerium Rich Mixture small dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustTiny(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustTiny, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium tiny  dust turned into Samarium Ore Concentrate tiny dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustSmall(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustSmall, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Samarium small dust turned into Samarium Ore Concentrate small dust.");
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

        GT_Log.out.println(Tags.MODID + ": regenerating centrifuge non cell recipes");
        centrifugeNonCellRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(centrifugeNonCellRecipes::add);
        centrifugeNonCellRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": centrifuge non cell recipes done!");
    }

    public static void removeCeriumHammer() {

        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        GT_Log.out.println(Tags.MODID + ": processing forge hammer recipes");

        for (GT_Recipe recipe : hammerRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GT_Utility.isStackValid(input)) {
                continue;
            }
            int[] oreDict = OreDictionary.getOreIDs(input);
            for (int oreDictID : oreDict) {
                final String oreName = OreDictionary.getOreName(oreDictID);
                if (!oreName.startsWith("crushed")) {
                    continue;
                }

                GT_Recipe tRecipe = recipe.copy();
                boolean modified = false;
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating forge hammer recipes");
        hammerRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(hammerRecipes::add);
        hammerRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": forge hammer recipes done!");
    }

    public static void removeCeriumElectrolyzer() {
        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);
        GT_Log.out.println(Tags.MODID + ": Processing electrolyzer recipes");
        for (GT_Recipe recipe : electrolyzerRecipes.getAllRecipes()) {
            for (ItemStack input : recipe.mInputs) {
                if (!GT_Utility.isStackValid(input)) {
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

                    GT_Recipe tRecipe = recipe.copy();
                    boolean modified = false;
                    for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                        if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                        if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                            tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            GT_Log.out.println(
                                Tags.MODID + ": recipe with input oredict: "
                                    + oreName
                                    + " get Cerium dust turned into Cerium Rich Mixture dust.");
                            modified = true;
                        } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                            tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating electrolyzer recipes");
        electrolyzerRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(electrolyzerRecipes::add);
        electrolyzerRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": Electrolyzer recipe done!");
    }

    public static void removeCeriumElectrolyzerNonCell() {
        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);
        GT_Log.out.println(Tags.MODID + ": processing electrolyzer non cell recipes");
        for (GT_Recipe recipe : electrolyzerNonCellRecipes.getAllRecipes()) {
            for (ItemStack input : recipe.mInputs) {
                if (!GT_Utility.isStackValid(input)) {
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

                    GT_Recipe tRecipe = recipe.copy();
                    boolean modified = false;
                    for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                        if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                        if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                            tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            GT_Log.out.println(
                                Tags.MODID + ": recipe with input oredict: "
                                    + oreName
                                    + " get Cerium dust turned into Cerium Rich Mixture dust.");
                            modified = true;
                        } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                            tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating electrolyzer non cell recipes");
        electrolyzerNonCellRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(electrolyzerNonCellRecipes::add);
        electrolyzerNonCellRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": Electrolyzer non cell recipes done!");
    }

    public static void removeCeriumSimpleWasher() {
        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);
        GT_Log.out.println(Tags.MODID + ": processing simple washer recipes.");
        for (GT_Recipe recipe : simpleWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (!GT_Utility.isStackValid(input)) {
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

                GT_Recipe tRecipe = recipe.copy();
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                    } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                        GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating simple washer recipes");
        simpleWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(simpleWasherRecipes::add);
        simpleWasherRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": Simple washer recipes done!");
    }

    public static void removeCeriumDehydrator() {
        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);
        GT_Log.out.println(Tags.MODID + ": processing chemical dehydrator recipes.");

        for (GT_Recipe recipe : chemicalDehydratorRecipes.getAllRecipes()) {
            if (recipe.mInputs.length == 0) {
                continue;
            }
            ItemStack input = recipe.mInputs[0];

            if (!GT_Utility.isStackValid(input)) {
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

                GT_Recipe tRecipe = recipe.copy();
                for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                    if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;

                    if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                        tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            WerkstoffMaterialPool.CeriumRichMixture
                                .get(OrePrefixes.dust, tRecipe.mOutputs[i].stackSize));
                        GT_Log.out.println(
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

        GT_Log.out.println(Tags.MODID + ": regenerating chemical dehydrator recipes");
        chemicalDehydratorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(chemicalDehydratorRecipes::add);
        chemicalDehydratorRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.println(Tags.MODID + ": chemical dehydrator recipes done!");

    }

    public static void removeCeriumChemicalBath() {
        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        GT_Log.out.println(Tags.MODID + ": marking recipes in chem bath for removal!");
        for (GT_Recipe recipe : chemicalBathRecipes.getAllRecipes()) {
            for (ItemStack input : recipe.mInputs) {
                if (!GT_Utility.isStackValid(input)) {
                    continue;
                }
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    String oreName = OreDictionary.getOreName(oreDictID);
                    if (oreName.equals("dustTin")) {
                        GT_Log.out.println(Tags.MODID + ": chem bath recipe with tin dust detected, removing it.");
                        remove.add(recipe);
                        break;
                    }
                    if (oreName.equals("dustRutile")) {
                        GT_Log.out.println(Tags.MODID + ": chem bath recipe with rutile dust detected, removing it.");
                        remove.add(recipe);
                        break;
                    }
                }
            }
        }

        GT_Log.out.println(Tags.MODID + ": regenerating chem bath recipes");
        chemicalBathRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(chemicalBathRecipes::add);
        chemicalBathRecipes.getBackend()
            .reInit();

        remove.clear();
        reAdd.clear();

        GT_Log.out.println("Chemical Bath done!");
    }

    public static void removeCeriumSources() {

        removeCeriumMacerator();
        removeCeriumWasher();
        removeCeriumThermalCentrifuge();
        removeCeriumCentrifuge();
        removeCeriumCentrifugeNonCell();
        removeCeriumHammer();
        removeCeriumElectrolyzer();
        removeCeriumElectrolyzerNonCell();
        removeCeriumSimpleWasher();
        removeCeriumDehydrator();
        removeCeriumChemicalBath();

        // For Cauldron Wash
        GT_Log.out.println(Tags.MODID + ": processing cauldron washing recipes to replace cerium sources");
        registerCauldronCleaningFor(Materials.Cerium, WerkstoffMaterialPool.CeriumRichMixture.getBridgeMaterial());
        registerCauldronCleaningFor(
            Materials.Samarium,
            WerkstoffMaterialPool.SamariumOreConcentrate.getBridgeMaterial());
        GT_Log.out.println(Tags.MODID + ": processing cauldron washing recipes done!");

        // For Crafting Table
        GT_Log.out.println(Tags.MODID + ": processing crafting recipes to replace cerium sources");
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(RecipeLoader::replaceInCraftTable);
        GT_Log.out.println(Tags.MODID + ": processing crafting recipes done!");
    }

    public static void replaceInCraftTable(Object obj) {
        IRecipe recipe = (IRecipe) obj;
        ItemStack result = recipe.getRecipeOutput();
        if (!(recipe instanceof IRecipeMutableAccess mutableRecipe)) {
            return;
        }

        Object input = mutableRecipe.gt5u$getRecipeInputs();

        if (GT_Utility.areStacksEqual(result, Materials.Cerium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Cerium)) {
                return;
            }
            GT_Log.out.println("replacing crafting recipe of Cerium dust by Cerium Rich Mixture");
            mutableRecipe.gt5u$setRecipeOutputItem(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 2));
        } else if (GT_Utility.areStacksEqual(result, Materials.Samarium.getDust(1), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Samarium)) {
                return;
            }
            mutableRecipe
                .gt5u$setRecipeOutputItem(WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2));
            GT_Log.out.println("replacing crafting recipe of Samarium dust by Samarium Ore Concentrate");
        }
    }

}
