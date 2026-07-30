package gtnhlanth.loader;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
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

import java.util.HashSet;

import gregtech.api.enums.materials2.MaterialFacades;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.gtenhancement.PlatinumSludgeOverHaul;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.material.MaterialUtils;
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
import gtnhlanth.common.register.LanthItemList;
import ic2.core.Ic2Items;

public class RecipeLoader {

    public static void loadAccelerator() {

        /* Actual Beamline Multiblocks */

        // SC
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 2 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 8),
                Circuits.ZPM.get(4),
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 2 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 8),
                ItemList.Casing_Coil_Superconductor.get(2),
                Circuits.ZPM.get(8),
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
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 2 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 8),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.plateDense, 4),
                Circuits.ZPM.get(4),
                Circuits.UV.get(2),
                GTUtility.copyAmount(2, LanthItemList.BEAMLINE_PIPE),
                GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.VanadiumGallium, 1))
            .circuit(15)
            .itemOutputs(LanthItemList.TARGET_CHAMBER)
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Synchrotron
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 48 * INGOTS))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 8),
                ItemList.Casing_Coil_Superconductor.get(12),
                Circuits.ZPM.get(8),
                Circuits.UV.get(8),
                GTUtility.copyAmount(8, LanthItemList.BEAMLINE_PIPE),
                GTOreDictUnificator.get(OrePrefixes.cableGt08, Materials.NiobiumTitanium, 8))
            .circuit(15)
            .itemOutputs(LanthItemList.SYNCHROTRON)
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);

        // Add .iCircuit(4) to this if it ever gets re-activated
        /*
         * //Permalloy GT_Values.RA.addMixerRecipe( MaterialLibAPI.getStack(Materials.Nickel,
         * Shapes.dust, 4),
         * MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 1),
         * MaterialLibAPI.getStack(Materials.Molybdenum, Shapes.dust, 1), null, null,
         * MaterialLibAPI.getStack(Materials.Permalloy, Shapes.dust, 6), 1920, 200 );
         */
        // Mu-metal
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Permalloy, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.dust, 1))
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Mumetal, Shapes.ingot, 11))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .specialValue(4500)
            .addTo(blastFurnaceRecipes);

        // Shielded Accelerator Casing -- Maybe assline recipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_RadiationProof.get(1L),
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 6))
            .circuit(6)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_CASING, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Accelerator Electrode Casing
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Silver, Materials2BlockShapes.blockCasingAdvanced, 1),
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Silver, 12),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.plateDense, 6))
            .circuit(6)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 2 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.ELECTRODE_CASING, 1))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        ItemStack insulator = GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MicaInsulatorSheet", 1);

        // Coolant Delivery Casing

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.plate, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2),
                ItemList.Electric_Pump_LuV.get(3L),
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 3),
                GTUtility.copyAmount(6, insulator),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 8 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.COOLANT_DELIVERY_CASING))
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Casing_Pipe_TungstenSteel.get(1L))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_IV))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.plate, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 2),
                ItemList.Electric_Pump_LuV.get(3L),
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 3),
                MaterialLibAPI.getStack(Materials.Fluorophlogopite, Shapes.plate, 6),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Tungsten, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 2 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Lubricant, Materials2FluidShapes.fluidLiquid, 8 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.COOLANT_DELIVERY_CASING))
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Casing_Pipe_TungstenSteel.get(1L))
            .metadata(SCANNING, new Scanning(30 * SECONDS, TierEU.RECIPE_IV))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // T1 Antenna Casing
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 48 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Gold, Materials2FluidShapes.fluidMolten, 32 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tungsten, 1),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plate, 5),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorLuV, 4),
                new Object[] { Circuits.ZPM.getIngredient(), 4 },
                ItemList.Emitter_LuV.get(6),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.wireFine, 32),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.plateDense, 6))
            .itemOutputs(new ItemStack(LanthItemList.ANTENNA_CASING_T1))
            .metadata(
                GTRecipeConstants.RESEARCH_ITEM,
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Niobium, 1))
            .metadata(SCANNING, new Scanning(1 * MINUTES, TierEU.RECIPE_IV))
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // T2 Antenna Casing
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 96 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Gold, Materials2FluidShapes.fluidMolten, 48 * INGOTS),
                MaterialLibAPI
                    .getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TungstenCarbide, 1),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plate, 5),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, MaterialFacades.SuperconductorZPM, 4),
                new Object[] { Circuits.UV.getIngredient(), 4 },
                ItemList.Emitter_ZPM.get(6),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.plateDense, 6))
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
                MaterialLibAPI.getStack(Materials.Niobium, Shapes.plate, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, 2_000))
            .itemOutputs(new ItemStack(LanthItemList.NIOBIUM_CAVITY_CASING, 1))
            .duration(12 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Focus Manipulator
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 64 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Gold, Materials2FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, 1_000))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),

                new Object[] { Circuits.IV.getIngredient(), 4 },
                ItemList.Robot_Arm_LuV.get(4),
                ItemList.Conveyor_Module_LuV.get(2),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.wireFine, 32),
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
            MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 1),
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
            MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 1),
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
            MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plateDense, 1),
            'C',
            ItemList.Conveyor_Module_HV.get(1),
            'R',
            ItemList.Robot_Arm_HV.get(1),
            'c',
            new ItemStack(Blocks.chest, 1, 32767));

        // Target Receptacle, same thing as Focus Manipulator basically
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 64 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Gold, Materials2FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, 1_000))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1),
                new Object[] { Circuits.IV.getIngredient(), 4 },
                ItemList.Robot_Arm_LuV.get(4),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.wireFine, 16),
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
                MaterialLibAPI.getStack(Materials.Copper, Shapes.plateDouble, 2),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plate, 6),
                GTUtility.copyAmount(4, insulator),
                ItemList.Electric_Pump_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Silicone, Materials2FluidShapes.fluidMolten, 2 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.TungstenSteel, 8),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.plateDouble, 2),
                MaterialLibAPI.getStack(Materials.Titanium, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Fluorophlogopite, Shapes.plate, 4),
                ItemList.Electric_Pump_LuV.get(1),
                MaterialLibAPI.getStack(Materials.Silver, Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Silicone, Materials2FluidShapes.fluidMolten, 2 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Mu-metal lattice
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.wireFine, 12),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stick, 4))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.MM_LATTICE, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Shielded Accelerator Glass
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.MM_LATTICE, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.BorosilicateGlass,
                    Materials2FluidShapes.fluidMolten,
                    1 * INGOTS))
            .itemOutputs(new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 1))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(fluidSolidifierRecipes);

        // Beamline Pipe
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.TungstenCarbide, Shapes.stick, 8),
                MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.foil, 4))
            .circuit(7)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 1 * INGOTS))
            .itemOutputs(LanthItemList.BEAMLINE_PIPE)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Beam Input Hatch
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 64 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, 2_000))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                new Object[] { Circuits.LuV.getIngredient(), 2 },
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 2),
                ItemList.Electric_Pump_LuV.get(1),
                LanthItemList.BEAMLINE_PIPE,
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plate, 4))
            .itemOutputs(LanthItemList.LUV_BEAMLINE_INPUT_HATCH)
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Hatch_Input_LuV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 20 * SECONDS, TierEU.RECIPE_IV))
            .duration(2 * GTRecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        // Beam Output Hatch
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SolderingAlloy, Materials2FluidShapes.fluidMolten, 64 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Argon, Materials2FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Helium, Materials2FluidShapes.fluidGas, 6_000))
            .itemInputs(
                ItemList.Hull_LuV.get(1),
                new Object[] { Circuits.LuV.getIngredient(), 6 },
                new ItemStack(LanthItemList.CAPILLARY_EXCHANGE, 4),
                ItemList.Electric_Pump_LuV.get(2),
                ItemList.Electric_Motor_LuV.get(4),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, MaterialFacades.SuperconductorLuV, 8),
                LanthItemList.BEAMLINE_PIPE,
                MaterialLibAPI.getStack(Materials.Mumetal, Shapes.plate, 8)

            )
            .itemOutputs(LanthItemList.LUV_BEAMLINE_OUTPUT_HATCH)
            .metadata(GTRecipeConstants.RESEARCH_ITEM, ItemList.Hatch_Output_LuV.get(1))
            .metadata(SCANNING, new Scanning(1 * MINUTES + 20 * SECONDS, TierEU.RECIPE_IV))
            .duration(4 * GTRecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_LuV)
            .addTo(AssemblyLine);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chloroform, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI
                    .getFluidStack(Materials.HydrofluoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Fluoroform, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 3_000))
            .duration(15 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidPlasma, 4_000))
            .itemInputs(MaterialLibAPI.getStack(Materials.Silane, Materials2CellShapes.cell, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SilaneNitrogenPlasmaMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (6_000)))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .duration(20 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(mixerRecipes);

        // NB: http://www.smfl.rit.edu/pdf/process/process_nitride_etch_paper.pdf
        // Reactive Ion Etchant
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Fluoroform, Materials2FluidShapes.fluidLiquid, (int) (3_000)))
            .itemInputs(MaterialLibAPI.getStack(Materials.Oxygen, Materials2CellShapes.cell, 4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ReactiveIonEtchingMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (5_000)))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .duration(15 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ReactiveIonEtchingMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (5_000)))
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 4))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Fluoroform, Materials2FluidShapes.fluidLiquid, (int) (3_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Oxygen, Materials2CellShapes.cell, 4))
            .duration(5 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Iron, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.NetherQuartz, Shapes.plate, 1))
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 1_000))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK1)))
            .outputChances(10000)
            .requiresCleanRoom()
            .duration(144 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(autoclaveRecipes);

        /*
         * Unsure what was intended with this recipe? GT_Values.RA.stdBuilder() .itemInputs(new
         * ItemStack(LanthItemList.IRON_COATED_QUARTZ), MaterialLibAPI.getStack(Materials.Chrome,
         * Shapes.dust, 1))
         * .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas,
         * 1_000)) .itemOutputs(new
         * ItemStack(LanthItemList.maskMap.get(MaskList.BLANK2))) .outputChances(10000).requiresCleanRoom().duration(12
         * * SECONDS).eut(7980).addTo(autoclaveRecipes);
         */

        // Grow the first silicon
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Glass, Shapes.plate, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Silane, Materials2FluidShapes.fluidGas, 4_000))
            .itemOutputs(new ItemStack(LanthItemList.SUBSTRATE_PRECURSOR))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(autoclaveRecipes);

        // Now to deposit nitride
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(LanthItemList.SUBSTRATE_PRECURSOR))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SilaneNitrogenPlasmaMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Nitrogen, Materials2FluidShapes.fluidGas, 3_000))
            .itemOutputs(new ItemStack(LanthItemList.MASK_SUBSTRATE))
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .specialValue(3200)
            .requiresCleanRoom()
            .addTo(blastFurnaceRecipes);

        /*
         * GT_Values.RA.stdBuilder().itemInputs(MaterialLibAPI.getStack(Materials.Silicon,
         * Shapes.foil, 1))
         * .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SiliconTetrachloride,
         * Materials2FluidShapes.fluidLiquid, 3_000), MaterialUtils.fluid(Materials.Ammonia, 4_000))
         * .fluidOutputs(MaterialUtils.fluid(Materials.HydrochloricAcidGT5U, 12_000))
         * .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconNitride, Shapes.plate, 1))
         * .duration(GTRecipeBuilder.SECONDS *
         * 30) .eut(TierEU.RECIPE_EV) .addTo(GTRecipe.GTRecipe_Map.sPlasmaArcFurnaceRecipes);
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
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ReactiveIonEtchingMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (4_000)))
            .itemOutputs(new ItemStack(LanthItemList.ETCHED_MASK_1))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .requiresCleanRoom()
            .addTo(autoclaveRecipes);

        // Etch pt. 2 with LiCl
        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(LanthItemList.MASKED_MASK),
                MaterialLibAPI.getStack(Materials.LithiumChloride, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.PotassiumHydroxideGT5U, Shapes.dust, 4))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK2)))
            .duration(80 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .metadata(COIL_HEAT, 2400)
            .metadata(ADDITIVE_AMOUNT, 1000)
            .addTo(BlastFurnaceWithGas);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(LanthItemList.ETCHED_MASK_1),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Chrome, Shapes.plate, 1),
                MaterialLibAPI.getStack(Materials.Gold, Shapes.plate, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 1_000))
            .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.BLANK3)))
            .duration(2 * GTRecipeBuilder.MINUTES)
            .eut(TierEU.RECIPE_ZPM)
            .specialValue(3600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(LanthItemList.ETCHED_MASK_1, 4),
                MaterialLibAPI.getStack(Materials.Glass, Shapes.plate, 2),
                GregtechItemList.LithiumHydroxideDust.get(2),
                MaterialLibAPI.getStack(Materials.Epoxid, Shapes.dust, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 2_000))
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
                            MaterialLibAPI
                                .getStack(Materials.IndiumGalliumPhosphide, Shapes.dust, 2))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials.VanadiumGallium,
                                Materials2FluidShapes.fluidMolten,
                                2 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrPIC)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrHPIC) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.HPIC)),
                            MaterialLibAPI
                                .getStack(Materials.IndiumGalliumPhosphide, Shapes.dust, 8))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials.Naquadah,
                                Materials2FluidShapes.fluidMolten,
                                4 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrHPIC)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrNPIC) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.NPIC)),
                            MaterialLibAPI
                                .getStack(Materials.IndiumGalliumPhosphide, Shapes.dust, 64))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials.Sunnarium,
                                Materials2FluidShapes.fluidMolten,
                                10 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrNPIC)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrQPIC) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.QPIC)),
                            MaterialLibAPI.getStack(Materials.Iodine, Shapes.dust, 64))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials.InfinityCatalyst,
                                Materials2FluidShapes.fluidMolten,
                                4 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrQPIC)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrCPU) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.CPU)),
                            GTUtility.copyAmount(16, Ic2Items.carbonFiber))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials.Glowstone,
                                Materials2FluidShapes.fluidMolten,
                                4 * INGOTS))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrCPU)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                } else if (mask == MaskList.PrNCPU) {

                    GTValues.RA.stdBuilder()
                        .itemInputs(
                            new ItemStack(LanthItemList.maskMap.get(MaskList.NCPU)),
                            MaterialLibAPI
                                .getStack(Materials.IndiumGalliumPhosphide, Shapes.dust, 1))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(Materials.Radon, Materials2FluidShapes.fluidGas, 50))
                        .itemOutputs(new ItemStack(LanthItemList.maskMap.get(MaskList.PrNCPU)))
                        .duration(60 * GTRecipeBuilder.SECONDS)
                        .eut(mask.getEngraverEUt())
                        .requiresCleanRoom()
                        .addTo(UniversalChemical);

                    GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(LanthItemList.maskMap.get(MaskList.NCPU)), ItemList.QuantumEye.get(2))
                        .fluidInputs(
                            MaterialLibAPI.getFluidStack(
                                Materials.GalliumArsenide,
                                Materials2FluidShapes.fluidMolten,
                                2 * INGOTS))
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
                        GTUtility.copyAmount(
                            0,
                            MaterialLibAPI.getStack(Materials.EnderPearl, Shapes.lens, 1)))
                    .itemOutputs(new ItemStack(LanthItemList.maskMap.get(mask)))
                    .requiresCleanRoom()
                    .duration(120 * SECONDS)
                    .eut(mask.getEngraverEUt())
                    .addTo(WaferEngravingRecipes);

            } else if (mask == MaskList.NOR) {

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        new ItemStack(LanthItemList.maskMap.get(maskIngredient)),
                        GTUtility.copyAmount(
                            0,
                            MaterialLibAPI.getStack(Materials.EnderEye, Shapes.lens, 1)))
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
                                MaterialLibAPI.getFluidStack(
                                    Materials.BiohMediumSterilized,
                                    Materials2FluidShapes.fluidLiquid,
                                    (int) ((mask.getDamage() + 1) * 4L * 50 * 3 / 4)))
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
            .itemInputs(MaterialLibAPI.getStack(Materials.Zirconia, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.ZirconiumTetrachloride, Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ZrCl4-H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ZirconiumTetrachloride, Shapes.dust, 5))
            .circuit(1)
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ZirconiumTetrachlorideSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr
        // ZrCl4·H2O + 2Mg = Zr + 2MgCl2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Zirconium, Shapes.ingotHot, 1),
                MaterialLibAPI.getStack(Materials.Magnesiumchloride, Shapes.dust, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ZirconiumTetrachlorideSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4500)
            .addTo(blastFurnaceRecipes);

        /* HAFNIUM */
        // HfCl4
        // HfO2 + 4HCl = HfCl4 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Hafnia, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HafniumTetrachloride, Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 4_000))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HfCl4-H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HafniumTetrachloride, Shapes.dust, 5))
            .circuit(1)
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HafniumTetrachlorideSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LP-Hf
        // HfCl4 + 2Mg = ??Hf?? + 2MgCl2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Magnesium, Shapes.dust, 2))
            .circuit(2)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.LowPurityHafnium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Magnesiumchloride, Shapes.dust, 6))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HafniumTetrachlorideSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 2700)
            .addTo(blastFurnaceRecipes);

        // HfI4
        // ??Hf?? + 4I = HfI4
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LowPurityHafnium, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.HafniumIodide, Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Iodine, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LowPurityHafnium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iodine, Shapes.dust, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials.HafniumIodide, Shapes.dust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Hf
        // HfI4 = Hf + 4I
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HafniumIodide, Shapes.dust, 5))
            .circuit(12)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Hafnium, Shapes.ingotHot, 1),
                MaterialLibAPI.getStack(Materials.HafniumRunoff, Shapes.dustTiny, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Iodine, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Hf * 9
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HafniumIodide, Shapes.dust, 45))
            .circuit(13)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Hafnium, Shapes.ingotHot, 9),
                MaterialLibAPI.getStack(Materials.HafniumRunoff, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Iodine, Materials2FluidShapes.fluidLiquid, (int) (36_000)))
            .duration(4 * MINUTES + 30 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Hafnium, Shapes.ingotHot, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Hafnium, Shapes.ingot, 1))
            .duration(26 * SECONDS + 14 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        // Zirconia-Hafnia
        // ??HfZr?? = HfO2 + ZrO2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HafniaZirconiaBlend, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Hafnia, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Zirconia, Shapes.dust, 3))
            .eut(TierEU.RECIPE_EV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        // Ammonium Nitrate
        // HNO3 + NH3 = NH4NO3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitricAcid, Materials2CellShapes.cell, 1))
            .circuit(12)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, Materials2FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AmmoniumNitrateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(12)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Ammonia, Materials2FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AmmoniumNitrateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(multiblockChemicalReactorRecipes);

        // IODINE-START
        // SeaweedAsh
        GTModHandler.addSmeltingRecipe(
            GTModHandler.getModItem(PamsHarvestCraft.ID, "seaweedItem", 1),
            MaterialLibAPI.getStack(Materials.SeaweedAsh, Shapes.dustSmall, 1));

        // SeaweedConcentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SeaweedAsh, Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Calcite, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DilutedSulfuricAcid, Materials2FluidShapes.fluidLiquid, 1_200))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SeaweedConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_200)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);

        // Iodine
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Benzene, Materials2CellShapes.cell, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Iodine, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SeaweedConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SeaweedByproducts,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (200)))
            .eut(TierEU.RECIPE_HV)
            .duration(38 * SECONDS)
            .addTo(centrifugeRecipes);

        // IODINE-END

        // 2MnO2 + 2KOH + KClO3 = 2KMnO4 + H2O + KCl
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Pyrolusite, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.PotassiumHydroxideGT5U, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.PotassiumChlorate, Shapes.dust, 5))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PotassiumPermanganate, Shapes.dust, 12),
                MaterialLibAPI.getStack(Materials.RockSalt, Shapes.dust, 2))
            .fluidOutputs(GTUtility.getWater(1_000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Mn + 2O = MnO2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Manganese, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Pyrolusite, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 2_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 6KOH + 6Cl = KClO3 + 5KCl + 3H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PotassiumHydroxideGT5U, Shapes.dust, 18))
            .circuit(3)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RockSalt, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.PotassiumChlorate, Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 6_000))
            .fluidOutputs(GTUtility.getWater(3_000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluorosilicate
        // 2NaCl + H2SiF6 = 2HCl + Na2SiF6
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, Materials2CellShapes.cell, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HexafluorosilicicAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Sodiumfluorosilicate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(30 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // La + 6HCl = LaCl3 + 3H
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 3_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.LanthaniumChloride, Shapes.dust, 4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 3_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Lanthanum Oxide
        GTValues.RA.stdBuilder()
            .fluidInputs(GTUtility.getWater(3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 6_000))
            .itemInputs(MaterialLibAPI.getStack(Materials.LanthaniumChloride, Shapes.dust, 8))
            .itemOutputs(MaterialLibAPI.getStack(Materials.LanthanumOxide, Shapes.dust, 1))
            .duration(10 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Boron Trioxide
        GTValues.RA.stdBuilder()
            .circuit(2)
            .fluidInputs(FluidRegistry.getFluidStack("boricacid", 2_000))
            .fluidOutputs(new FluidStack(FluidRegistry.WATER, 3_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BoronTrioxide, Shapes.dust, 1))
            .duration(15 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Boron, Shapes.dust, 2))
            .circuit(2)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 3_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.BoronTrioxide, Shapes.dust, 1))
            .duration(30 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(UniversalChemical);

        // Boron Trichloride
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Phosgene, Materials2FluidShapes.fluidLiquid, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.BoronTrichloride,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)))
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BoronTrioxide, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CarbonMonoxide, Materials2CellShapes.cell, 3))
            .duration(10 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Lanthanum Hexaboride
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.BoronTrichloride,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (8_000)))
            .fluidOutputs(FluidRegistry.getFluidStack("boricacid", 1_000))
            .itemInputs(MaterialLibAPI.getStack(Materials.LanthanumOxide, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.LanthanumHexaboride, Shapes.gemFlawless, 1))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);
    }

    public static void loadLanthanideRecipes() {
        // Methanol
        // CH4O + CO + 3O =V2O5= H2C2O4 + H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.VanadiumPentoxide, Shapes.dustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 3_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.OxalicAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                GTUtility.getWater(1_000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.VanadiumPentoxide, Shapes.dust, 1))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Methanol, Materials2FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 27_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.OxalicAcid, Materials2FluidShapes.fluidLiquid, (int) (9_000)),
                GTUtility.getWater(9_000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        // Ethanol
        // C2H6O + 5O =V2O5= H2C2O4 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.VanadiumPentoxide, Shapes.dustTiny, 1))
            .itemOutputs()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethanol, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 5_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.OxalicAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)),
                GTUtility.getWater(2_000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.VanadiumPentoxide, Shapes.dust, 1))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ethanol, Materials2FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 45_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.OxalicAcid, Materials2FluidShapes.fluidLiquid, (int) (9_000)),
                GTUtility.getWater(18_000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Oxalate
        // 2CeCl3 + 3H2C2O4 = 6HCl + Ce2(C2O4)3
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CeriumChloride, Shapes.dust, 8))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.CeriumOxalate, Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.OxalicAcid, Materials2FluidShapes.fluidLiquid, (int) (3_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 6_000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // CHAIN BEGIN
        // MONAZITE
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 700))
            .itemInputs(MaterialLibAPI.getStack(Materials.Monazite, Shapes.crushed, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MuddyMonaziteRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (400)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .eut(TierEU.RECIPE_EV)
            .duration(400)
            .metadata(COIL_HEAT, 800)
            .addTo(digesterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HafniaZirconiaBlend, Shapes.dustTiny, 4),
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Monazite, Shapes.dustTiny, 2))
            .fluidInputs(
                GTUtility.getWater(10_000),
                MaterialLibAPI.getFluidStack(
                    Materials.MuddyMonaziteRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedMonaziteRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (11_000)))
            .eut(TierEU.RECIPE_HV)
            .duration(900)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .addTo(dissolutionTankRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HafniaZirconiaBlend, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Monazite, Shapes.dust, 2))
            .fluidInputs(
                GTUtility.getWater(90_000),
                MaterialLibAPI.getFluidStack(
                    Materials.MuddyMonaziteRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (9_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedMonaziteRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (99_000)))
            .eut(TierEU.RECIPE_HV)
            .duration(8100)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .addTo(dissolutionTankRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedMonaziteRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MonaziteSulfate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RedZircon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Ilmenite, Shapes.dust, 1))
            .outputChances(90_00, 75_00, 20_00, 5_00, 20_00)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.MonaziteSulfate, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(6_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedMonaziteSulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (7_000)))
            .duration(24 * SECONDS)
            .eut(400)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(13)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.AcidicMonazitePowder, Shapes.dustTiny, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedMonaziteSulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.AmmoniumNitrateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (200)))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(14)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AcidicMonazitePowder, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedMonaziteSulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (9_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.AmmoniumNitrateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_800)))
            .duration(3 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.AcidicMonazitePowder, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MonaziteRareEarthFiltrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.ThoriumPhosphateCake, Shapes.dust, 1))
            .outputChances(9000, 7000)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ThoriumPhosphateCake, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ThoriumPhosphateConcentrate, Shapes.dust, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1500)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ThoriumPhosphateConcentrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Thorium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Phosphate, Shapes.dust, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MonaziteRareEarthFiltrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.NeutralizedMonaziteRareEarthFiltrate, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.AmmoniumNitrateSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (320)))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.NeutralizedMonaziteRareEarthFiltrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.MonaziteRareEarthHydroxideConcentrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.UraniumFiltrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.UraniumFiltrate, Shapes.dust, 1))
            .outputChances(9000, 5000, 4000)
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.UraniumFiltrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NeutralizedUraniumFiltrate, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrofluoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 100))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NeutralizedUraniumFiltrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Uranium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Uranium235, Shapes.dust, 1))
            .outputChances(4500, 4000, 3000, 3000, 2000)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.MonaziteRareEarthHydroxideConcentrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.DriedMonaziteRareEarthConcentrate, Shapes.dust, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.DriedMonaziteRareEarthConcentrate, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitrogenatedMonaziteRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitrogenatedMonaziteRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitricLeachedMonaziteMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitrogenatedMonaziteRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitricLeachedMonaziteMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitricLeachedMonaziteMixture,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CeriumDioxide, Shapes.dust, 1))
            .outputChances(11_11)
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitricMonaziteLeachedConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(sifterRecipes);

        // BEGIN Cerium
        // Cerium-rich mixture + 3HCl = CeCl3 + Monazite (to allow cerium processing without bastnazite/monazite)
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 15))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumChloride, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Monazite, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 750))
            .fluidOutputs(GTUtility.getWater(750))
            .duration(25 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // CeO2 + 3NH4Cl + H = 3NH3 + CeCl3 + 2H2O
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CeriumDioxide, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.AmmoniumChloride, Materials2CellShapes.cell, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumChloride, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Ammonia, Materials2CellShapes.cell, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Hydrogen, Materials2FluidShapes.fluidGas, 1_000))
            .fluidOutputs(MaterialUtils.gas(Materials.Steam, 2_000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // Ce2(C2O4)3 + 3C = Ce2O3 + 9CO
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CeriumOxalate, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Carbon, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CeriumIIIOxide, Shapes.dust, 5))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, 9_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 800)
            .addTo(blastFurnaceRecipes);

        // END Cerium (NMLC)

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CooledMonaziteRareEarthConcentrate, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitricMonaziteLeachedConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(vacuumFreezerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.CooledMonaziteRareEarthConcentrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MonaziteRarerEarthSediment, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.EuropiumIIIOxide, Shapes.dust, 5))
            .outputChances(9000, 500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(electroMagneticSeparatorRecipes);

        // 5Eu2O3 + Eu = 4EuO
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.EuropiumIIIOxide, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.EuropiumOxide, Shapes.dust, 6))
            .duration(15 * SECONDS)
            .eut(8400)
            .addTo(UniversalChemical);

        // 4 EuO = 2 Eu + 2O2
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.EuropiumOxide, Shapes.dust, 2))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Europium, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 1_000))
            .duration(15 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        // EuS = Eu + S
        // TODO old recipe. for compat only. remove material and recipe half a year later, i.e. after September 2023.
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.EuropiumSulfide, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Europium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Sulfur, Shapes.dust, 1))
            .duration(30 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MonaziteRarerEarthSediment, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(
                    Materials.HeterogenousHalogenicMonaziteRareEarthMixture,
                    Shapes.dust,
                    1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 1),
                MaterialLibAPI.getStack(
                    Materials.HeterogenousHalogenicMonaziteRareEarthMixture,
                    Shapes.dust,
                    1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SaturatedMonaziteRareEarth, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(
                    Materials.HeterogenousHalogenicMonaziteRareEarthMixture,
                    Shapes.dust,
                    1),
                MaterialLibAPI.getStack(Materials.SamariumOreConcentrate, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SaturatedMonaziteRareEarth, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, Materials2FluidShapes.fluidLiquid, 1_000))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SaturatedMonaziteRareEarth, Shapes.dust, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SamaricResidue, Shapes.dust, 3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chloromethane, Materials2FluidShapes.fluidGas, 400))
            .eut(TierEU.RECIPE_EV)
            .duration(2 * MINUTES + 37 * SECONDS + 10 * TICKS)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SamaricResidue, Shapes.dust, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, 1))
            .outputChances(10000, 10000)
            .duration(6 * SECONDS + 13 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sifterRecipes);

        // BASTNASITE (god help me)
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 700))
            .itemInputs(MaterialLibAPI.getStack(Materials.Bastnasite, Shapes.crushed, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MuddyBastnasiteRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (400)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .eut(TierEU.RECIPE_EV)
            .duration(400)
            .metadata(COIL_HEAT, 800)
            .addTo(digesterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MuddyBastnasiteRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialUtils.gas(Materials.Steam, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SteamCrackedBastnasiteMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(crackingRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SteamCrackedBastnasiteMud, Materials2CellShapes.cell, 1))
            .circuit(6)
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Sodiumfluorosilicate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (320)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ConditionedBastnasiteMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_320)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                GTUtility.getWater(10_000),
                MaterialLibAPI.getFluidStack(
                    Materials.ConditionedBastnasiteMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemInputs(MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedBastnasiteMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (11_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Gangue, Shapes.dust, 1))
            .eut(TierEU.RECIPE_EV)
            .duration(1000)
            .metadata(DISSOLUTION_TANK_RATIO, 10)
            .addTo(dissolutionTankRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Rutile, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RedZircon, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Ilmenite, Shapes.dust, 1))
            .outputChances(90_00, 75_00, 10_00, 5_00)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedBastnasiteMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilteredBastnasiteMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (400)))
            .eut(TierEU.RECIPE_HV / 2)
            .duration(20 * SECONDS)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.BastnasiteRareEarthOxides, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilteredBastnasiteMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(25 * SECONDS)
            .eut(600)
            .metadata(COIL_HEAT, 1400)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BastnasiteRareEarthOxides, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.AcidLeachedBastnasiteRareEarthOxides, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.AcidLeachedBastnasiteRareEarthOxides, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.RoastedRareEarthOxides, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Oxygen, Materials2FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, 13))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RoastedRareEarthOxides, Shapes.dust, 1))
            .circuit(7)
            .itemOutputs(MaterialLibAPI.getStack(Materials.WetRareEarthOxides, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.WetRareEarthOxides, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumOxidisedRareEarthOxides, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Fluorine, Materials2FluidShapes.fluidGas, 4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrofluoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 4_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CeriumOxidisedRareEarthOxides, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.BastnasiteRarerEarthOxides, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.CeriumDioxide, Shapes.dust, 1))
            .outputChances(100_00, 90_00)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.BastnasiteRarerEarthOxides, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 400))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NitrogenatedBastnasiteRarerEarthOxides,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.NitrogenatedBastnasiteRarerEarthOxides, Materials2CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.BastnasiteRarerEarthOxideSuspension,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NeodymiumRareEarthConcentrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SamaricRareEarthConcentrate, Shapes.dust, 1))
            .outputChances(80_00, 50_00)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.BastnasiteRarerEarthOxideSuspension,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DilutedAcetone, Materials2FluidShapes.fluidLiquid, (int) (750)))
            .eut(TierEU.RECIPE_HV)
            .duration(45 * SECONDS)
            .addTo(centrifugeRecipes);

        // Nd RE
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.NeodymiumRareEarthConcentrate, Shapes.dust, 2))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.LanthaniumChloride, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.NeodymiumOxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 2_000))
            .duration(45 * SECONDS)
            .eut(800)
            .addTo(UniversalChemical);

        // Sm RE
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SamaricRareEarthConcentrate, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.FluorinatedSamaricConcentrate, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrofluoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SamaricRareEarthConcentrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SamariumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.FluorinatedSamaricConcentrate, Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrofluoricAcidGT5U, Materials2FluidShapes.fluidLiquid, 2_000))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.FluorinatedSamaricConcentrate, Shapes.dust, 8),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Holmium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.SamariumTerbiumMixture, Shapes.dust, 4))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CalciumFluoride,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (12_000)))
            .duration(1 * MINUTES + 20 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SamariumTerbiumMixture, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.AmmoniumNitrate, Shapes.dust, 9))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.NitrogenatedSamariumTerbiumMixture, Shapes.dust, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.NitrogenatedSamariumTerbiumMixture, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Copper, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.TerbiumNitrate, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.SamaricResidue, Shapes.dust, 2) // Potentially
                                                                                                      // make
            // only Samarium
            )
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SamariumOreConcentrate, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, 3))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.DephosphatedSamariumConcentrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.TricalciumPhosphate, Shapes.dust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.DephosphatedSamariumConcentrate, Shapes.dust, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Thorianite, Shapes.dust, 2))
            .outputChances(90_00, 80_00)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(centrifugeRecipes);

        // TODO UV Tier Ion Extracting Method

        // Lanthanum Part
        // Digester to produce Lanthanum Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LanthanumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Praseodymium Part
        // Digester to produce Praseodymium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Praseodymium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.PraseodymiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledPraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Part
        // Digester to produce Cerium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Cerium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledCeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Neodymium Part
        // Digester to produce Neodymium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Neodymium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NeodymiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledNeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Promethium Part
        // Digester to produce Neodymium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Promethium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PromethiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PromethiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PromethiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);

        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal

        // Samarium Part
        // Digester to produce Samarium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Samarium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SamariumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledSamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Europium Part
        // Digester to produce Europium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Europium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.EuropiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledEuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Gadolinium Part
        // Digester to produce Gadolinium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.GadoliniumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledGadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Terbium Part
        // Digester to produce Terbium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Terbium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.TerbiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledTerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dysprosium Part
        // Digester to produce Dysprosium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Dysprosium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.DysprosiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledDysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Holmium Part
        // Digester to produce Holmium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Holmium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HolmiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledHolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Erbium Part
        // Digester to produce Erbium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Erbium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ErbiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Thulium Part
        // Digester to produce Thulium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Thulium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ThuliumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ytterbium Part
        // Digester to produce Ytterbium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ytterbium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.YtterbiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledYtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lutetium Part
        // Digester to produce Lutetium Chloride Concentrate
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lutetium, Shapes.crushed, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 3))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.LutetiumOreConcentrate, Shapes.dust, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SiliconDioxide, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 12_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumChlorideConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ion Extracting Process to produce Rare Earth Element (example Samarium) by Nano Resin
        // Get Extracting Nano Resin

        // Lanthanum
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Praseodymium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Praseodymium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Cerium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Neodymium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Neodymium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Sm
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Europium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Europium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Gadolinium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Gadolinium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Terbium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Terbium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Dysprosium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Dysprosium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Holmium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Holmium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Erbium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Erbium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Thulium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Thulium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Ytterbium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Ytterbium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Lutetium
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "MysteriousCrystalLens", 0),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 1),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Carbon, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.P507, Materials2FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // TODO Electrolyzer recycle Nano Resin and produce molten rare earth metal,

        // La
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.LanthanumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lanthanum, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Pr
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledPraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.PraseodymiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Praseodymium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Ce
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledCeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Cerium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Nd
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledNeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NeodymiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Neodymium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Sm
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledSamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SamariumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Samarium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Eu
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledEuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.EuropiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Europium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Ga
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledGadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.GadoliniumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Gadolinium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Tb
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledTerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.TerbiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Terbium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Dy
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledDysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.DysprosiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Dysprosium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Ho
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledHolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.HolmiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Holmium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Er
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ErbiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Erbium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Tm
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ThuliumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Thulium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Yb
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledYtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.YtterbiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Ytterbium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // Lu
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.LutetiumExtractingNanoResin, Materials2CellShapes.cell, 1),
                GTOreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lutetium, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 3_000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(electrolyzerRecipes);

        // TODO ChlorinitedRareEarthConcentrate process with every 15 Rare Earth Extracting Nano Resin

        // La
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLanthanumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Pr
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledPraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledPraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledPraseodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ce
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledCeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledCeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledCeriumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Nd
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledNeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledNeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.NeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledNeodymiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Sm
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledSamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledSamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledSamariumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Eu
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledEuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledEuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.EuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledEuropiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ga
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledGadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledGadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.GadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledGadoliniumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tb
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledTerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledTerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.TerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledTerbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dy
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledDysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledDysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledDysprosiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ho
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledHolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledHolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledHolmiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Er
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledErbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tm
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.ThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledThuliumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Yb
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledYtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledYtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.YtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledYtterbiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lu
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthConcentrate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthEnrichedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.LutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI.getFluidStack(
                    Materials.ChlorinatedRareEarthDilutedSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.FilledLutetiumExtractingNanoResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.WasteLiquid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // TODO Samarium Ore Concentrate Dust Processing Line Start

        // 16 SmOreDust + 200L NitricAcid =EV@10s= 800L MuddySamariumRareEarthSolution + 1 ?ThP?ConcentrateDust
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SamariumOreConcentrate, Shapes.dust, 16))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 200))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ThoriumPhosphateConcentrate, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MuddySamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (800)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1 CrushedSamariumOre = 3 SamariumOreConcentrate in process
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Samarium, Shapes.crushed, 8))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 300))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ThoriumPhosphateConcentrate, Shapes.dust, 3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MuddySamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_200)))
            .metadata(COIL_HEAT, 800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1B MuddySmSolution + 1B NitricAcid =EV@10s= 2B SamariumRareEarthMud + 0.8 CeriumDioxide + 0.6
        // CeriumRichMixture(CeriumOreConcentrate)
        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(
                    Materials.MuddySamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)))
            .metadata(DISSOLUTION_TANK_RATIO, 1)
            .outputChances(8000, 6000)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.CeriumDioxide, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 9))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 9_000),
                MaterialLibAPI.getFluidStack(
                    Materials.MuddySamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (9_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (18_000)))
            .metadata(DISSOLUTION_TANK_RATIO, 1)
            .outputChances(8000, 6000)
            .eut(TierEU.RECIPE_IV)
            .duration(300)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(
                    Materials.MuddySamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.CeriumDioxide, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .outputChances(5000)
            .eut(TierEU.RECIPE_EV)
            .duration(300)
            .addTo(multiblockChemicalReactorRecipes);

        // 1B SamariumRareEarthMud + 9B water =EV@30s= 10B DilutedSamariumRareEarthSolution
        // + (90% + 60%) NeodymiumREConcentrate
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                GTUtility.getWater(9_000),
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NeodymiumRareEarthConcentrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.NeodymiumRareEarthConcentrate, Shapes.dust, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (10_000)))
            .metadata(DISSOLUTION_TANK_RATIO, 9)
            .outputChances(9000, 6000)
            .eut(TierEU.RECIPE_EV)
            .duration(600)
            .addTo(dissolutionTankRecipes);
        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                GTUtility.getWater(81_000),
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (9_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.NeodymiumRareEarthConcentrate, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.NeodymiumRareEarthConcentrate, Shapes.dust, 9))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (90_000)))
            .metadata(DISSOLUTION_TANK_RATIO, 9)
            .outputChances(9000, 6000)
            .eut(TierEU.RECIPE_IV)
            .duration(900)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GTValues.RA.stdBuilder()
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumRareEarthMud,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)),
                GTUtility.getWater(16_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (8_000)))
            .eut(TierEU.RECIPE_EV)
            .duration(1200)
            .addTo(multiblockChemicalReactorRecipes);

        // 2B DilutedSamariumRareEarthSolution + 3B Oxalate
        // =EV@10s=
        // 5 ImpureSamariumOxalate + 50L MuddySamariumRareEarthSolution + 0.1*2 LepersonniteDust
        // LepersonniteDust -> DephosphatedSamariumConcentrate
        GTValues.RA.stdBuilder()
            .circuit(13)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.DilutedSamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (2_000)),
                MaterialLibAPI
                    .getFluidStack(Materials.OxalicAcid, Materials2FluidShapes.fluidLiquid, (int) (3_000)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SamariumIIIOxalate, Shapes.dust, 5),
                MaterialLibAPI.getStack(Materials.DephosphatedSamariumConcentrate, Shapes.dust, 3))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.MuddySamariumRareEarthSolution,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (50)))
            .outputChances(10000, 1000)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // 5 ImpureSamariumOxalate + 6B HCL = 8 ImpureSamariumChloride + 6B CO
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SamariumIIIOxalate, Shapes.dust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HydrochloricAcidGT5U, Materials2FluidShapes.fluidLiquid, 6_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SamariumIIIChloride, Shapes.dust, 8))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CarbonMonoxide, Materials2FluidShapes.fluidGas, 6_000))
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
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SamariumIIIChloride, Shapes.dust, 2),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.SamariumChlorideSodiumChlorideBlend, Shapes.dust, 3))
            .eut(TierEU.RECIPE_LV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);

        // 6 SamariumChlorideSodiumChlorideBlend =IV@1s= 1 SamariumDust + 1 SodiumDust + 2/9 RarestEarthResidue + 4B
        // Chlorine
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.SamariumChlorideSodiumChlorideBlend, Shapes.dust, 6))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RarestEarthResidue, Shapes.dustTiny, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 4_000))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * SECONDS)
            .addTo(electrolyzerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.SamariumChlorideSodiumChlorideBlend, Shapes.dust, 54))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.RarestEarthResidue, Shapes.dust, 2))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Chlorine, Materials2FluidShapes.fluidGas, 36_000))
            .eut(TierEU.RECIPE_LuV)
            .duration(2 * SECONDS)
            .addTo(electrolyzerRecipes);

        // distill with LanthanumDust 36*144L moltenSmCl3 = 16*144L moltenSm + 27B Cl
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Lanthanum, Shapes.dust, 9))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ImpureLanthanumChloride, Shapes.dust, 36))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.SamariumIIIChloride,
                    Materials2FluidShapes.fluidMolten,
                    (int) (5_184)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Samarium, Materials2FluidShapes.fluidMolten, 16 * INGOTS))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .addTo(distillationTowerRecipes);

        // Centrifuge ImpureLanthanumChlorideDust
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ImpureLanthanumChloride, Shapes.dust, 36))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.LanthaniumChloride, Shapes.dust, 36),
                MaterialLibAPI.getStack(Materials.RarestEarthResidue, Shapes.dust, 5))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * SECONDS)
            .addTo(centrifugeRecipes);
    }

    public static void addRandomChemCrafting() {

        // Butanediol
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.TelluriumMolybdenumOxideCatalyst, Shapes.dustTiny, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Butane, Materials2FluidShapes.fluidGas, 1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials._14Butanediol, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TelluriumMolybdenumOxideCatalyst, Shapes.dust, 1))
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Butane, Materials2FluidShapes.fluidGas, 9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials._14Butanediol, Materials2FluidShapes.fluidLiquid, (int) (9_000)))
            .duration(6 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Moly-Te-Oxide Catalyst
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.MolybdenumIVOxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.TelluriumIVOxide, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.TelluriumMolybdenumOxideCatalyst, Shapes.dust, 2))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // Diaminotoluene
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Hydrogen, Materials2CellShapes.cell, 4))
            .itemOutputs()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Dinitrotoluene,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Diaminotoluene,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Dinitrotoluene
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitricAcid, Materials2CellShapes.cell, 2))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.cell, Materials.Empty, 2))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Toluene, Materials2FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Dinitrotoluene,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
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
                MaterialLibAPI.getStack(Materials.Desh, Shapes.rotor, 4),
                Circuits.LuV.get(4))
            .circuit(1)
            .itemOutputs(LanthItemList.DIGESTER)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Polytetrafluoroethylene,
                    Materials2FluidShapes.fluidMolten,
                    10 * INGOTS))
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
                MaterialLibAPI.getStack(Materials.VibrantAlloy, Shapes.rotor, 4),
                Circuits.EV.get(4))
            .circuit(2)
            .itemOutputs(LanthItemList.DISSOLUTION_TANK)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.Polytetrafluoroethylene,
                    Materials2FluidShapes.fluidMolten,
                    5 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV / 2)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.DilutedAcetone, Materials2FluidShapes.fluidLiquid, (int) (50)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Acetone, Materials2FluidShapes.fluidLiquid, 30))
            .duration(1 * SECONDS + 4 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.HotSuperCoolant,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.SuperCoolant, Materials2FluidShapes.fluidLiquid, 1_000))
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
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Sapphire, Shapes.dust, 5))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 9))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.GreenSapphire, Shapes.dust, 5))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 9))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 5))
            .circuit(4)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 9))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 27),
                MaterialLibAPI.getStack(Materials.Sapphire, Shapes.dust, 45))
            .circuit(5)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 64),
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 17))
            .eut(TierEU.RECIPE_UV)
            .duration(45 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 27),
                MaterialLibAPI.getStack(Materials.GreenSapphire, Shapes.dust, 45))
            .circuit(5)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 64),
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 17))
            .eut(TierEU.RECIPE_UV)
            .duration(45 * SECONDS)
            .addTo(mixerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 9),
                MaterialLibAPI.getStack(Materials.Lutetium, Shapes.dust, 27),
                MaterialLibAPI.getStack(Materials.Alumina, Shapes.dust, 45))
            .circuit(5)
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 64),
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 17))
            .eut(TierEU.RECIPE_UV)
            .duration(45 * SECONDS)
            .addTo(mixerRecipes);

        // Get LuAG Crystal seed
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gem, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Lutetium, Materials2FluidShapes.fluidMolten, 8 * INGOTS))
            .outputChances(514)
            .eut(TierEU.RECIPE_UV)
            .duration(500)
            .addTo(autoclaveRecipes);

        // 1 LuAG Blend = 1.1(Og) 1.0(Xe) 0.99(Kr) LuAG in Autoclave
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 1),
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gem, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1),
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Krypton, Materials2FluidShapes.fluidLiquid, (int) (40)))
            .outputChances(8000, 1900)
            .eut(TierEU.RECIPE_UHV)
            .duration(512)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 1),
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gem, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1),
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Xenon, Materials2FluidShapes.fluidLiquid, (int) (25)))
            .outputChances(9000, 1000)
            .eut(TierEU.RECIPE_UHV)
            .duration(256)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1),
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oganesson, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .outputChances(10000, 100)
            .eut(TierEU.RECIPE_UHV)
            .duration(128)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumOxygenBlend, Shapes.dust, 1),
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gem, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1),
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Oganesson, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .outputChances(10000, 2000)
            .eut(TierEU.RECIPE_UHV)
            .duration(128)
            .addTo(autoclaveRecipes);

        // 1 LuAG Blend = 1 LuAG in Vacuum Furnace
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(
                    Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                    Shapes.gemExquisite,
                    1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.CeriumdopedLutetiumAluminiumOxygenBlend,
                    Materials2FluidShapes.fluidMolten,
                    (int) (108)))
            .metadata(COIL_HEAT, 9100)
            .eut(TierEU.RECIPE_UHV)
            .duration(5 * SECONDS)
            .addTo(vacuumFurnaceRecipes);

        // Amalgatite Gems
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Magmatter, 1)),
                MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gemFlawed, 3),
                MaterialLibAPI.getStack(Materials.Amber, Shapes.gem, 64),
                MaterialLibAPI.getStack(Materials.Topaz, Shapes.gem, 64),
                MaterialLibAPI
                    .getStack(Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG, Shapes.gem, 64),
                MaterialLibAPI.getStack(Materials.Jade, Shapes.gem, 64))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.Protomatter, Materials2FluidShapes.fluidLiquid, 250_000))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Amalgatite, Shapes.gem, 1))
            .metadata(COIL_HEAT, 13500)
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(vacuumFurnaceRecipes);

        // 16 Adv Crystal SoC
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensBlue")) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    GTUtility.copyAmount(0, itemStack),
                    MaterialLibAPI.getStack(
                        Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                        Shapes.gemExquisite,
                        1))
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
                    MaterialLibAPI.getStack(
                        Materials.CeriumdopedLutetiumAluminiumGarnetCeLuAG,
                        Shapes.gemExquisite,
                        1))
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

                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1));
                        modified = true;
                        GTLog.out.println(
                            "in the recipe of '" + recipe.mInputs[0].getDisplayName()
                                + "', replacing Cerium dust by Cerium Rich Mixture dust");
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2,
                                MaterialLibAPI
                                    .getStack(Materials.SamariumOreConcentrate, Shapes.dust, 1));
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
                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input: "
                                + input.getDisplayName()
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2,
                                MaterialLibAPI
                                    .getStack(Materials.SamariumOreConcentrate, Shapes.dust, 1));
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
                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input "
                                + input.getDisplayName()
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2,
                                MaterialLibAPI
                                    .getStack(Materials.SamariumOreConcentrate, Shapes.dust, 1));
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
                    if (tRecipe.mOutputs[i].isItemEqual(
                        MaterialLibAPI.getStack(Materials.Cerium, Shapes.dustTiny, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            MaterialLibAPI
                                .getStack(Materials.CeriumRichMixture, Shapes.dustTiny, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium tiny dust turned into Cerium Rich Mixture tiny dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2,
                                MaterialLibAPI
                                    .getStack(Materials.CeriumRichMixture, Shapes.dust, 1));
                            GTLog.out.println(
                                Tags.MODID + ": recipe with input oredict: "
                                    + oreName
                                    + " get Cerium dust turned into Cerium Rich Mixture dust.");
                            modified = true;
                        } else if (tRecipe.mOutputs[i].isItemEqual(
                            MaterialLibAPI.getStack(Materials.Cerium, Shapes.dustSmall, 1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    MaterialLibAPI.getStack(
                                        Materials.CeriumRichMixture,
                                        Shapes.dustSmall,
                                        1));
                                GTLog.out.println(
                                    Tags.MODID + ": recipe with input oredict: "
                                        + oreName
                                        + " get Cerium small dust turned into Cerium Rich Mixture small dust.");
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(
                                MaterialLibAPI.getStack(Materials.Samarium, Shapes.dustTiny, 1))) {
                                    tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize * 2,
                                        MaterialLibAPI.getStack(
                                            Materials.SamariumOreConcentrate,
                                            Shapes.dustTiny,
                                            1));
                                    GTLog.out.println(
                                        Tags.MODID + ": recipe with input oredict: "
                                            + oreName
                                            + " get Samarium dust turned into Samarium Ore Concentrate tiny dust.");
                                    modified = true;
                                } else if (tRecipe.mOutputs[i].isItemEqual(
                                    MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1))) {
                                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                            tRecipe.mOutputs[i].stackSize * 2,
                                            MaterialLibAPI.getStack(
                                                Materials.SamariumOreConcentrate,
                                                Shapes.dust,
                                                1));
                                        GTLog.out.println(
                                            Tags.MODID + ": recipe with input oredict: "
                                                + oreName
                                                + " get Samarium dust turned into Samarium Ore Concentrate dust.");
                                        modified = true;
                                    } else if (tRecipe.mOutputs[i].isItemEqual(
                                        MaterialLibAPI
                                            .getStack(Materials.Samarium, Shapes.dustSmall, 1))) {
                                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                                    tRecipe.mOutputs[i].stackSize * 2,
                                                    MaterialLibAPI.getStack(
                                                        Materials.SamariumOreConcentrate,
                                                        Shapes.dustSmall,
                                                        1));
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
                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize * 2,
                            MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                        modified = true;
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize * 2,
                                MaterialLibAPI
                                    .getStack(Materials.SamariumOreConcentrate, Shapes.dust, 1));
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
                        if (tRecipe.mOutputs[i].isItemEqual(
                            MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                MaterialLibAPI
                                    .getStack(Materials.CeriumRichMixture, Shapes.dust, 1));
                            GTLog.out.println(
                                Tags.MODID + ": recipe with input oredict: "
                                    + oreName
                                    + " get Cerium dust turned into Cerium Rich Mixture dust.");
                            modified = true;
                        } else if (tRecipe.mOutputs[i].isItemEqual(
                            MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1))) {
                                tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    MaterialLibAPI.getStack(
                                        Materials.SamariumOreConcentrate,
                                        Shapes.dust,
                                        1));
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
                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 1));
                        GTLog.out.println(
                            Tags.MODID + ": recipe with input oredict: "
                                + oreName
                                + " get Cerium dust turned into Cerium Rich Mixture dust.");
                    } else if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1))) {
                            tRecipe.mOutputs[i] = GTUtility.copyAmount(
                                tRecipe.mOutputs[i].stackSize,
                                MaterialLibAPI
                                    .getStack(Materials.SamariumOreConcentrate, Shapes.dust, 1));
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

                    if (tRecipe.mOutputs[i]
                        .isItemEqual(MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1))) {
                        tRecipe.mOutputs[i] = GTUtility.copyAmount(
                            tRecipe.mOutputs[i].stackSize,
                            MaterialLibAPI.getStack(
                                Materials.CeriumRichMixture,
                                Shapes.dust,
                                tRecipe.mOutputs[i].stackSize));
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
        registerCauldronCleaningFor(Materials.Cerium, Materials.CeriumRichMixture);
        registerCauldronCleaningFor(Materials.Samarium, Materials.SamariumOreConcentrate);
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

        if (GTUtility.areStacksEqual(
            result,
            MaterialLibAPI.getStack(Materials.Cerium, Shapes.dust, 1),
            true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Cerium)) {
                return;
            }
            GTLog.out.println("replacing crafting recipe of Cerium dust by Cerium Rich Mixture");
            mutableRecipe.gt5u$setRecipeOutputItem(
                MaterialLibAPI.getStack(Materials.CeriumRichMixture, Shapes.dust, 2));
        } else if (GTUtility.areStacksEqual(
            result,
            MaterialLibAPI.getStack(Materials.Samarium, Shapes.dust, 1),
            true)) {
                if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Samarium)) {
                    return;
                }
                mutableRecipe.gt5u$setRecipeOutputItem(
                    MaterialLibAPI.getStack(Materials.SamariumOreConcentrate, Shapes.dust, 2));
                GTLog.out.println("replacing crafting recipe of Samarium dust by Samarium Ore Concentrate");
            }
    }

}
