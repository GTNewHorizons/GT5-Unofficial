package gtPlusPlus.core.recipe;

import static gregtech.api.enums.Mods.RemoteIO;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.PipeShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.block.ModBlocks;
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
                Circuits.ZPM.get(2))
            .itemOutputs(GregtechItemList.Gregtech_Computer_Cube.get(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Tantalum, FluidShapes.fluidMolten, (int) (16 * INGOTS)))
            .duration(3 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Circuit programmer
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Robot_Arm_LV.get(4),
                ItemList.Cover_Controller.get(1),
                ItemList.Hull_MV.get(1),
                Circuits.LV.get(2),
                Circuits.MV.get(2))
            .itemOutputs(new ItemStack(ModBlocks.blockCircuitProgrammer))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Iron, FluidShapes.fluidMolten, (int) (4 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(assemblerRecipes);

        // Lead Lined Chest
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_LV.get(1),
                new OreDictItemStack("plateAnyRubber", 32),
                MaterialLibAPI.getStack(Materials.Lead, Shapes.plateDense, (int) (9)),
                new ItemStack(Blocks.chest))
            .itemOutputs(new ItemStack(ModBlocks.blockDecayablesChest))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidMolten, (int) (16 * INGOTS)))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(assemblerRecipes);

        // RTG
        GTValues.RA.stdBuilder()
            .itemInputs(
                getModItem(Mods.IndustrialCraft2.ID, "blockGenerator", 1, 6),
                MaterialLibAPI.getStack(Materials.Nitinol60, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.MaragingSteel350, Shapes.gearGt, 4),
                ItemList.Field_Generator_EV.get(8),
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.wireFine, (int) (32)),
                Circuits.LuV.get(4))
            .itemOutputs(GregtechItemList.RTG.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.NiobiumCarbide, 16 * INGOTS))
            .duration(10 * MINUTES)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);

        // Flask Configurator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_MV.get(1),
                new ItemStack(ModBlocks.blockCircuitProgrammer),
                ItemList.VOLUMETRIC_FLASK.get(8),
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.StainlessSteel, 2),
                MaterialLibAPI.getStack(Materials.EglinSteel, Shapes.plate, 4))
            .circuit(17)
            .itemOutputs(new ItemStack(ModBlocks.blockVolumetricFlaskSetter, 1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.SiliconCarbide, 8 * INGOTS))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

        // Tesseract Generator
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Tesseract_Generator.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "CEC", "PXP", 'P', OrePrefixes.plate.ingredient(Materials.Titanium), 'C',
                "circuitMaster", 'E', new ItemStack(Blocks.ender_chest), 'X',
                GregtechItemList.Gregtech_Computer_Cube });

        // Tesseract Terminal
        GTModHandler.addCraftingRecipe(
            GregtechItemList.GT4_Tesseract_Terminal.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "CEC", "PHP", 'P', OrePrefixes.plate.ingredient(Materials.Titanium), 'C',
                "circuitElite", 'E', new ItemStack(Blocks.ender_chest), 'H', ItemList.Hull_EV });

        // Air Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "PRP", "IHI", 'P', OrePrefixes.plate.ingredient(Materials.Redstone), 'C',
                ItemList.Casing_Grate, 'R', ItemList.FluidRegulator_IV, 'I', "circuitElite", 'H',
                ItemList.Hatch_Input_IV });

        // Extreme Air Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake_Extreme.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "PRP", "IHI", 'P', MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 1),
                'C', GregtechItemList.Hatch_Air_Intake, 'R', ItemList.FluidRegulator_ZPM, 'I', "circuitUltimate", 'H',
                ItemList.Hatch_Input_ZPM });

        // Atmospheric Intake Hatch
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Hatch_Air_Intake_Atmospheric.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "PRP", "IHI", 'P', MaterialLibAPI.getStack(Materials.Octiron, Shapes.plate, 1), 'C',
                GregtechItemList.Hatch_Air_Intake_Extreme, 'R', ItemList.FluidRegulator_UHV, 'I', "circuitInfinite",
                'H', ItemList.Hatch_Input_UHV });

        // Large Semifluid Burner
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_LargeSemifluidGenerator.get(1L),
            BITS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Pump_EV, 'C', Circuits.EV.getIngredient(), 'W',
                OrePrefixes.cableGt08.ingredient(Materials.Electrum), 'G',
                MaterialLibAPI.getStack(Materials.Inconel792, Shapes.gearGt, 1) });

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
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.plate, 8),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 4))
            .itemOutputs(new ItemStack(DimensionEverglades.blockPortalFrame, 2))
            .fluidInputs(MaterialUtils.anyFluid(Materials.Zeron100, 8 * INGOTS))
            .duration(4 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);

        // Industrial Multi Tank Casing (unused but craftable)
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_MultitankExterior.get(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "RPR", "PFP", "PPP", 'R', MaterialLibAPI.getStack(Materials.Grisium, Shapes.stick, 1), 'P',
                MaterialLibAPI.getStack(Materials.Grisium, Shapes.plate, 1), 'F',
                MaterialLibAPI.getStack(Materials.Grisium, PipeShapes.frameGt, 1) });

        // Trinium Plated Casing (unused but craftable)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.TriniumNaquadahCarbonite, PipeShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.TriniumTitaniumAlloy, Shapes.plateDouble, 1),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.gearGt, 2),
                MaterialLibAPI.getStack(Materials.ArceusAlloy2B, Shapes.plateDouble, 4),
                ItemList.Hull_LuV.get(1))
            .itemOutputs(GregtechItemList.Casing_BedrockMiner.get(1))
            .fluidInputs(MaterialUtils.anyFluid(Materials.MaragingSteel350, 16 * INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
    }
}
