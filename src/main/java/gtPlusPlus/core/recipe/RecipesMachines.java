package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.RemoteIO;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITSD;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import toxiceverglades.dimension.DimensionEverglades;

public class RecipesMachines {

    public static void loadRecipes() {
        RecipesMachinesCustom.loadRecipes();
        RecipesMachinesMulti.loadRecipes();
        RecipesMachinesTiered.loadRecipes();

        // Computer Cube
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Tool_DataOrb.get(4),
                ItemList.Cover_Screen.get(4),
                ItemList.Hull_IV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 2))
            .itemOutputs(GregtechItemList.Gregtech_Computer_Cube.get(1))
            .fluidInputs(Materials.Tantalum.getMolten(16 * INGOTS))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Circuit programmer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Robot_Arm_LV.get(4),
                ItemList.Cover_Controller.get(1),
                ItemList.Hull_MV.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LV, 2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 2))
            .itemOutputs(new ItemStack(ModBlocks.blockCircuitProgrammer))
            .fluidInputs(Materials.Iron.getMolten(4 * INGOTS))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Lead Lined Chest
        for (ItemStack plateRubber : OreDictionary.getOres("plateAnyRubber")) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hull_LV.get(1),
                    GTUtility.copyAmount(32, plateRubber),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Lead, 9),
                    new ItemStack(Blocks.chest))
                .itemOutputs(new ItemStack(ModBlocks.blockDecayablesChest))
                .fluidInputs(Materials.Lead.getMolten(16 * INGOTS))
                .duration(1 * MINUTES + 30 * SECONDS)
                .eut(60)
                .addTo(assemblerRecipes);
        }

        // RTG
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(Mods.IndustrialCraft2.ID, "blockGenerator", 1, 6),
                MaterialsAlloy.NITINOL_60.getPlate(8),
                MaterialsAlloy.MARAGING350.getGear(4),
                ItemList.Field_Generator_EV.get(8),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Platinum, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 4))
            .itemOutputs(GregtechItemList.RTG.get(1))
            .fluidInputs(MaterialsAlloy.NIOBIUM_CARBIDE.getFluidStack(16 * INGOTS))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Agricultural Sewer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                ItemList.FluidRegulator_MV.get(2),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 2),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4),
                MaterialsAlloy.POTIN.getScrew(6))
            .itemOutputs(new ItemStack(ModBlocks.blockPooCollector))
            .fluidInputs(MaterialsAlloy.TUMBAGA.getFluidStack(4 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Biocomposite Collector
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                new ItemStack(ModBlocks.blockPooCollector),
                ItemList.FluidRegulator_IV.get(2),
                GTOreDictUnificator.get("pipeHugeStaballoy", 4),
                MaterialsAlloy.ZERON_100.getScrew(16))
            .itemOutputs(new ItemStack(ModBlocks.blockPooCollector, 1, 8))
            .fluidInputs(MaterialsAlloy.ARCANITE.getFluidStack(9 * INGOTS))
            .duration(5 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);

        // Flask Configurator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                new ItemStack(ModBlocks.blockCircuitProgrammer),
                ItemList.VOLUMETRIC_FLASK.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.StainlessSteel, 2),
                MaterialsAlloy.EGLIN_STEEL.getPlate(4))
            .circuit(17)
            .itemOutputs(new ItemStack(ModBlocks.blockVolumetricFlaskSetter, 1))
            .fluidInputs(MaterialsAlloy.SILICON_CARBIDE.getFluidStack(8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Tesseract Generator
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Tesseract_Generator.get(1),
            new Object[] { "PCP", "CEC", "PXP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'C', "circuitMaster",
                'E', new ItemStack(Blocks.ender_chest), 'X', GregtechItemList.Gregtech_Computer_Cube });

        // Tesseract Terminal
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Tesseract_Terminal.get(1),
            new Object[] { "PCP", "CEC", "PHP", 'P', OrePrefixes.plate.get(Materials.Titanium), 'C', "circuitElite",
                'E', new ItemStack(Blocks.ender_chest), 'H', ItemList.Hull_EV });

        // Air Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake.get(1),
            new Object[] { "PCP", "PRP", "IHI", 'P', OrePrefixes.plate.get(Materials.Redstone), 'C',
                ItemList.Casing_Grate, 'R', ItemList.FluidRegulator_IV, 'I', "circuitElite", 'H',
                ItemList.Hatch_Input_IV });

        // Extreme Air Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake_Extreme.get(1),
            new Object[] { "PCP", "PRP", "IHI", 'P', MaterialsAlloy.PIKYONIUM.getPlate(1), 'C',
                GregtechItemList.Hatch_Air_Intake, 'R', ItemList.FluidRegulator_ZPM, 'I', "circuitUltimate", 'H',
                ItemList.Hatch_Input_ZPM });

        // Atmospheric Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake_Atmospheric.get(1),
            new Object[] { "PCP", "PRP", "IHI", 'P', MaterialsAlloy.OCTIRON.getPlate(1), 'C',
                GregtechItemList.Hatch_Air_Intake_Extreme, 'R', ItemList.FluidRegulator_UHV, 'I', "circuitInfinite",
                'H', ItemList.Hatch_Input_UHV });

        // Large Semifluid Burner
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_LargeSemifluidGenerator.get(1L),
            BITSD,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Pump_EV, 'C', OrePrefixes.circuit.get(Materials.EV), 'W',
                OrePrefixes.cableGt08.get(Materials.Electrum), 'G', MaterialsAlloy.INCONEL_792.getGear(1) });

        // Project Table
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Emitter_EV.get(2),
                ItemList.Robot_Arm_EV.get(2),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.MV, 8),
                MaterialsAlloy.TANTALUM_CARBIDE.getScrew(8),
                MaterialsAlloy.INCONEL_625.getPlate(4))
            .itemOutputs(new ItemStack(ModBlocks.blockProjectTable))
            .fluidInputs(MaterialsAlloy.ARCANITE.getFluidStack(4 * INGOTS))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);

        // Reservoir Hatch
        if (RemoteIO.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(
                    ItemList.Hatch_Input_EV.get(1),
                    getModItem(RemoteIO.ID, "tile.machine", 1),
                    ItemList.Electric_Pump_EV.get(1))
                .itemOutputs(GregtechItemList.Hatch_Reservoir.get(1))
                .duration(5 * SECONDS)
                .eut(TierEU.RECIPE_EV)
                .addTo(assemblerRecipes);
        }

        // Containment Frame (Everglades Portal)
        GTValues.RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Casing_Multi_Use.get(1),
                ItemList.Field_Generator_MV.get(1),
                ItemList.Field_Generator_HV.get(1),
                ItemList.Emitter_HV.get(1),
                ItemList.Sensor_HV.get(1),
                MaterialsAlloy.PIKYONIUM.getPlate(8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 4))
            .itemOutputs(new ItemStack(DimensionEverglades.blockPortalFrame, 2))
            .fluidInputs(MaterialsAlloy.ZERON_100.getFluidStack(8 * INGOTS))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Industrial Multi Tank Casing (unused but craftable)
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MultitankExterior.get(1),
            new Object[] { "RPR", "PFP", "PPP", 'R', MaterialsAlloy.LEAGRISIUM.getRod(1), 'P',
                MaterialsAlloy.LEAGRISIUM.getPlate(1), 'F', MaterialsAlloy.LEAGRISIUM.getFrameBox(1) });

        // Trinium Plated Casing (unused but craftable)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialsAlloy.TRINIUM_NAQUADAH_CARBON.getFrameBox(4),
                MaterialsAlloy.TRINIUM_TITANIUM.getPlateDouble(1),
                MaterialsAlloy.PIKYONIUM.getGear(2),
                MaterialsAlloy.TRINIUM_REINFORCED_STEEL.getPlateDouble(4),
                ItemList.Hull_LuV.get(1))
            .itemOutputs(GregtechItemList.Casing_BedrockMiner.get(1))
            .fluidInputs(MaterialsAlloy.MARAGING350.getFluidStack(16 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }
}
