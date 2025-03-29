package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.steamCarpenterRecipes;
import static gregtech.api.recipe.RecipeMaps.steamManufacturerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamWoodcutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.items.MetaGeneratedTool01;

public class AssortedSteamRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .fluidInputs(GTModHandler.getSteam(100000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CompressedSteam, 1))
            .duration(30 * SECONDS)
            .eut(512)
            .metadata(CompressionTierKey.INSTANCE, 2)
            .addTo(compressorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1))
            .duration(1 * SECONDS)
            .eut(4)
            .addTo(steamCarpenterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 6))
            .itemOutputs(ItemList.Iron_Wood_Casing.get(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamCarpenterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 6))
            .itemOutputs(ItemList.Bronze_Wood_Casing.get(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamCarpenterRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6))
            .itemOutputs(ItemList.Steel_Wood_Casing.get(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamCarpenterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CrudeSteel, 3))
            .duration(15 * SECONDS)
            .eut(28)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cactus, 0))
            .itemOutputs(new ItemStack(Blocks.cactus, 8))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamWoodcutterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds, 0))
            .itemOutputs(new ItemStack(Items.reeds, 8))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamWoodcutterRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Motor_Steam.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "gearBronze", 'B', "pipeTinyBronze", 'C', "stickIron", 'D',
                ItemList.Simple_Iron_Turbine.get(1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Piston_Steam.get(1),
            new Object[] { "AAA", "BCC", "DEF", 'A', "plateCrudeSteel", 'D', "pipeTinyBronze", 'C', "stickIron", 'B',
                ItemList.Simple_Iron_Turbine.get(1), 'E', ItemList.Hydraulic_Motor_Steam.get(1), 'F', "gearBronze" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Pump_Steam.get(1),
            new Object[] { "ABC", "sDw", "CEA", 'A', ItemList.Hydraulic_Motor_Steam.get(1), 'B',
                ItemList.Simple_Bronze_Turbine.get(1), 'C', "ringRubber", 'D', "pipeLargeBronze", 'E',
                "screwCrudeSteel" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Arm_Steam.get(1),
            new Object[] { "AAA", "BCB", "DEC", 'A', "plateDoubleCrudeSteel", 'B',
                ItemList.Hydraulic_Motor_Steam.get(1), 'C', "stickIron", 'D', ItemList.Hydraulic_Piston_Steam.get(1),
                'E', "gearCrudeSteel" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Conveyor_Steam.get(1),
            new Object[] { "AAA", "BCB", "AAA", 'A', "plateRubber", 'B', ItemList.Hydraulic_Motor_Steam.get(1), 'C',
                "gearCrudeSteel" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Regulator_Steam.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "gearBronze", 'B', "pipeTinyBronze", 'C',
                MetaGeneratedTool01.INSTANCE.getToolWithStats(170, 1, Materials.Steel, Materials.Steel, null), 'D',
                ItemList.Hydraulic_Pump_Steam.get(1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Sensor_Steam.get(1),
            new Object[] { "ABC", "ACB", "DAA", 'A', "plateCompressedSteam", 'B', "plateRubber", 'C', "pipeHugeStronze",
                'D', ItemList.Hydraulic_Pump_Steam.get(1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Emitter_Steam.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "plateCompressedSteam", 'B', "stickCompressedSteam", 'C',
                "pipeRestrictiveHugeBreel", 'D', "gemExquisiteSalt" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Vapor_Generator.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "plateSuperdenseCompressedSteam", 'B', "frameGtCompressedSteam",
                'C', ItemList.Hydraulic_Emitter_Steam.get(1), 'D', MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(170, 1, Materials.CompressedSteam, Materials.CompressedSteam, null) });

        GTModHandler.addCraftingRecipe(
            ItemList.Simple_Iron_Turbine.get(1),
            new Object[] { "ABf", "BDB", "hBA", 'A', "stickIron", 'B', "plateIron", 'D', "ringIron" });
        GTModHandler.addCraftingRecipe(
            ItemList.Simple_Iron_Turbine.get(1),
            new Object[] { "ABf", "BDB", "hBA", 'A', "stickBronze", 'B', "plateBronze", 'D', "ringBronze" });

        Materials[] Tier1Materials = { Materials.Bronze, Materials.Iron, Materials.Copper, Materials.Tin,
            Materials.Brass, Materials.Steel, Materials.WroughtIron, Materials.Breel, Materials.Stronze,
            Materials.CompressedSteam, Materials.CrudeSteel };

        for (Materials aMat : Tier1Materials) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, aMat, 4), GTUtility.getIntegratedCircuit(24))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, aMat, 1))
                .duration(5 * SECONDS)
                .eut(16)
                .addTo(steamManufacturerRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bronze, 4),
                ItemList.Simple_Iron_Turbine.get(1))
            .itemOutputs(ItemList.Hydraulic_Motor_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bronze, 1),
                ItemList.Simple_Iron_Turbine.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 1),
                ItemList.Hydraulic_Motor_Steam.get(1))
            .itemOutputs(ItemList.Hydraulic_Piston_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Rubber, 2),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CrudeSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Bronze, 1),
                ItemList.Simple_Bronze_Turbine.get(1),
                ItemList.Hydraulic_Motor_Steam.get(2))
            .itemOutputs(ItemList.Hydraulic_Pump_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 2),
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.CrudeSteel, 3),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CrudeSteel, 1),
                ItemList.Hydraulic_Piston_Steam.get(1),
                ItemList.Hydraulic_Motor_Steam.get(2))
            .itemOutputs(ItemList.Hydraulic_Arm_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CrudeSteel, 1),
                ItemList.Hydraulic_Motor_Steam.get(2))
            .itemOutputs(ItemList.Hydraulic_Conveyor_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(170, 2, Materials.Steel, Materials.Steel, null),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bronze, 4),
                ItemList.Hydraulic_Pump_Steam.get(1))
            .itemOutputs(ItemList.Hydraulic_Regulator_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 4),
                ItemList.Hydraulic_Regulator_Steam.get(1))
            .itemOutputs(ItemList.Hydraulic_Sensor_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Salt, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 2),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.CompressedSteam, 4),
                GTOreDictUnificator.get(OrePrefixes.pipeRestrictiveHuge, Materials.Breel, 2))
            .itemOutputs(ItemList.Hydraulic_Emitter_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(170, 1, Materials.CompressedSteam, Materials.CompressedSteam, null),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CompressedSteam, 2),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CompressedSteam, 4),
                ItemList.Hydraulic_Emitter_Steam.get(2))
            .itemOutputs(ItemList.Hydraulic_Vapor_Generator.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 4),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 2))
            .itemOutputs(ItemList.Simple_Iron_Turbine.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 2))
            .itemOutputs(ItemList.Simple_Bronze_Turbine.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.turbineBlade, Materials.Steel, 4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Stronze, 1))
            .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(170, 2, Materials.Steel, Materials.Steel, null))
            .duration(23 * SECONDS)
            .eut(512)
            .addTo(steamManufacturerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.turbineBlade, Materials.CompressedSteam, 4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Breel, 1))
            .itemOutputs(
                MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(170, 2, Materials.CompressedSteam, Materials.CompressedSteam, null))
            .duration(23 * SECONDS)
            .eut(512)
            .addTo(steamManufacturerRecipes);

    }
}
