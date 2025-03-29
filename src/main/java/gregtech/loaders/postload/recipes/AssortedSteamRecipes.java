package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.steamCarpenterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.MetaGeneratedTool01;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

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
                new ItemStack(Blocks.cobblestone, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1),
                new ItemStack(Blocks.gravel, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CrudeSteel, 4))
            .duration(15 * SECONDS)
            .eut(28)
            .addTo(alloySmelterRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Motor_Steam.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "gearBronze", 'B', "pipeTinyBronze", 'C', "stickIron", 'D',
                GregtechItemList.BasicIronTurbine.get(1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Piston_Steam.get(1),
            new Object[] { "AAA", "BCC", "DEF", 'A', "plateCrudeSteel", 'D', "pipeTinyBronze", 'C', "stickIron", 'B',
                GregtechItemList.BasicIronTurbine.get(1), 'E', ItemList.Hydraulic_Motor_Steam.get(1), 'F',
                "gearBronze" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Pump_Steam.get(1),
            new Object[] { "ABC", "sDw", "CEA", 'A', ItemList.Hydraulic_Motor_Steam.get(1), 'B',
                GregtechItemList.BasicBronzeTurbine.get(1), 'C', "ringRubber", 'D', "pipeLargeBronze", 'E',
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
            new Object[] { "ABC", "BDB", "CBA", 'A', "plateCompressedSteam", 'B', "foilCompressedSteam", 'C',
                "pipeRestrictiveHugeBreel", 'D', "gemExquisiteSalt" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Vapor_Generator.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "plateSuperdenseCompressedSteam", 'B', "frameGtCompressedSteam",
                'C', ItemList.Hydraulic_Emitter_Steam.get(1), 'D', MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(170, 1, Materials.CompressedSteam, Materials.CompressedSteam, null) });

    }
}
