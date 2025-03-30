package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamCarpenterRecipes;
import static gregtech.api.recipe.RecipeMaps.steamManufacturerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamWoodcutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import cpw.mods.fml.common.Mod;
import gregtech.api.GregTechAPI;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
        RA.stdBuilder()
            .fluidInputs(GTModHandler.getSteam(100000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CompressedSteam, 1))
            .duration(30 * SECONDS)
            .eut(512)
            .metadata(CompressionTierKey.INSTANCE, 2)
            .addTo(compressorRecipes);

        RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Wood, 4))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1))
            .duration(1 * SECONDS)
            .eut(4)
            .addTo(steamCarpenterRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 6))
            .itemOutputs(ItemList.Iron_Wood_Casing.get(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamCarpenterRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 6))
            .itemOutputs(ItemList.Bronze_Wood_Casing.get(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamCarpenterRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6))
            .itemOutputs(ItemList.Steel_Wood_Casing.get(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamCarpenterRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CrudeSteel, 3))
            .duration(15 * SECONDS)
            .eut(28)
            .addTo(alloySmelterRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cactus, 0))
            .itemOutputs(new ItemStack(Blocks.cactus, 8))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamWoodcutterRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds, 0))
            .itemOutputs(new ItemStack(Items.reeds, 8))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamWoodcutterRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 2),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.QuartzSand, 1))
            .fluidOutputs(Materials.Concrete.getMolten(1000))
            .duration(4 * SECONDS)
            .eut(16)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Firebrick.get(6), GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gypsum, 2))
            .fluidInputs(Materials.Concrete.getMolten(1000))
            .itemOutputs(ItemList.Casing_Firebricks.get(1))
            .duration(4 * SECONDS)
            .eut(16)
            .addTo(compressorRecipes);

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
            new Object[] { "ABw", "BDB", "hBA", 'A', "stickIron", 'B', "plateIron", 'D', "ringIron" });
        GTModHandler.addCraftingRecipe(
            ItemList.Simple_Bronze_Turbine.get(1),
            new Object[] { "ABw", "BDB", "hBA", 'A', "stickBronze", 'B', "plateBronze", 'D', "ringBronze" });

        Materials[] Tier1Materials = { Materials.Bronze, Materials.Iron, Materials.Copper, Materials.Tin,
            Materials.Brass, Materials.Steel, Materials.WroughtIron, Materials.Breel, Materials.Stronze,
            Materials.CompressedSteam, Materials.CrudeSteel };

        for (Materials aMat : Tier1Materials) {
            RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, aMat, 4), GTUtility.getIntegratedCircuit(24))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.frameGt, aMat, 1))
                .duration(5 * SECONDS)
                .eut(16)
                .addTo(steamManufacturerRecipes);
        }

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 2),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bronze, 4),
                ItemList.Simple_Iron_Turbine.get(1))
            .itemOutputs(ItemList.Hydraulic_Motor_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
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

        RA.stdBuilder()
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

        RA.stdBuilder()
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

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CrudeSteel, 1),
                ItemList.Hydraulic_Motor_Steam.get(2))
            .itemOutputs(ItemList.Hydraulic_Conveyor_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                MetaGeneratedTool01.INSTANCE.getToolWithStats(170, 2, Materials.Steel, Materials.Steel, null),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Bronze, 4),
                ItemList.Hydraulic_Pump_Steam.get(1))
            .itemOutputs(ItemList.Hydraulic_Regulator_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 4),
                ItemList.Hydraulic_Regulator_Steam.get(1))
            .itemOutputs(ItemList.Hydraulic_Sensor_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Salt, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 2),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.CompressedSteam, 4),
                GTOreDictUnificator.get(OrePrefixes.pipeRestrictiveHuge, Materials.Breel, 2))
            .itemOutputs(ItemList.Hydraulic_Emitter_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
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

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 4),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Iron, 2))
            .itemOutputs(ItemList.Simple_Iron_Turbine.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 2))
            .itemOutputs(ItemList.Simple_Bronze_Turbine.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.turbineBlade, Materials.Steel, 4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Stronze, 1))
            .itemOutputs(MetaGeneratedTool01.INSTANCE.getToolWithStats(170, 2, Materials.Steel, Materials.Steel, null))
            .duration(23 * SECONDS)
            .eut(512)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.turbineBlade, Materials.CompressedSteam, 4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Breel, 1))
            .itemOutputs(
                MetaGeneratedTool01.INSTANCE
                    .getToolWithStats(170, 2, Materials.CompressedSteam, Materials.CompressedSteam, null))
            .duration(23 * SECONDS)
            .eut(512)
            .addTo(steamManufacturerRecipes);

        // Spotless:off

        // Machine Casings

        // Bronze
        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.brick_block,1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 6))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings1, 10, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 2, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 12, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Bronze, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings3, 13, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Steel
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 0, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Steel, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 3, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 13, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Steel, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 4))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings3, 14, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Steel, 2),
                ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 3, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 6))
            .itemOutputs(ItemUtils.simpleMetaStack(ModBlocks.blockCasingsMisc, 3, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Vibration Casing
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Breel, 2),
                ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 0, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 6))
            .itemOutputs(ItemList.Vibration_Safe_Casing.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Vibration_Safe_Casing.get(1),
            new Object[] { "AAA", "BCB", "AAA", 'A', "plateCrudeSteel", 'B', "frameGtBreel", 'C', ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 0, 1) });

        // Extractinator Solid Casing
        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.brick_block, 6),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 3))
            .itemOutputs(ItemList.Extractinator_Casing.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Extractinator_Casing.get(1),
            new Object[] { "AAA", "BBB", "BBB", 'A', "plateCrudeSteel", 'B', new ItemStack(Blocks.brick_block, 1) });

        // Extractinator Solid Casing
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Breel, 1),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.CrudeSteel, 6))
            .itemOutputs(ItemList.Extractinator_Sieve_Mesh.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Extractinator_Sieve_Mesh.get(1),
            new Object[] {
                "AAA",
                "wBh",
                "AAA",
                'A', "stickCrudeSteel",
                'B', "frameGtBreel" });

        // Stronze Wrapped Casingg
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 5),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Stronze, 4))
            .itemOutputs(ItemList.Stronze_Casing.get(4))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Stronze_Casing.get(4),
            new Object[] {
                "ABA",
                "BBB",
                "ABA",
                'A', "plateStronze",
                'B', "frameGtCrudeSteel" });

        // Breel Pipe Casingg
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Breel, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 4))
            .itemOutputs(ItemList.Breel_Pipe_Casing.get(2))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Breel_Pipe_Casing.get(2),
            new Object[] {
                "ABA",
                "BCB",
                "ABA",
                'A', "plateCrudeSteel",
                'B', "pipeMediumBreel",
                'C', "frameGtCrudeSteel" });

        // Machine Controllers
        {

            // Steam Fuser
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamAlloySmelterMulti.get(1),
                new Object[] {
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', ItemList.Casing_BronzePlatedBricks.get(1),
                    'B', new ItemStack(Blocks.furnace, 1),
                    'C', GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Steel,1),
                    'D', new ItemStack(Blocks.nether_brick, 1)
                });

            // Steam Conformer
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamExtruder.get(1),
                new Object[] {
                    "BCA",
                    "DEF",
                    "AGA",
                    'A', ItemList.Casing_BronzePlatedBricks.get(1),
                    'B', ItemList.Hydraulic_Motor_Steam.get(1),
                    'C', ItemList.Hydraulic_Piston_Steam.get(1),
                    'D', new ItemStack(Blocks.anvil, 1),
                    'E', GregtechItemList.Controller_SteamForgeHammerMulti.get(1),
                    'F', GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Steel,1),
                    'G', ItemList.Hydraulic_Conveyor_Steam.get(1)
                });

            // Open Heart Blast Furnace
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamBlastFurnace.get(1),
                new Object[] {
                    "ABA",
                    "BCB",
                    "ABA",
                    'A', ItemList.Stronze_Casing.get(1),
                    'B', GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Steel,1),
                    'C', ItemList.Machine_Bricked_BlastFurnace.get(1)
                });

            // Steam Forge
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamMultiSmelter.get(1),
                new Object[] {
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', ItemList.Breel_Pipe_Casing.get(1),
                    'B', new ItemStack(Blocks.furnace, 1),
                    'C', ItemList.Stronze_Casing.get(1),
                    'D', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Stronze, 1)
                });

            // Steam Rock Breaker
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamRockBreaker.get(1),
                new Object[] {
                    "ABA",
                    "CDE",
                    "ABA",
                    'A', ItemList.Casing_BronzePlatedBricks.get(1),
                    'B', ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 12, 1),
                    'C', new ItemStack(Items.water_bucket, 1),
                    'D', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1),
                    'E', new ItemStack(Items.lava_bucket, 1)
                });

            // Steam Extractinator
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamExtractinator.get(1),
                new Object[] {
                    "ABA",
                    "CDC",
                    "ABA",
                    'A', ItemList.Vibration_Safe_Casing.get(1),
                    'B', ItemList.Casing_BronzePlatedBricks.get(1),
                    'C', ItemList.Hydraulic_Pump_Steam.get(1),
                    'D', GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Stronze, 1)
                });

            // Steam Carpenter
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamCarpenter.get(1),
                new Object[] {
                    "ABA",
                    "CDC",
                    "EEE",
                    'A', ItemList.Casing_BronzePlatedBricks.get(1),
                    'B', ItemList.Hydraulic_Piston_Steam.get(1),
                    'D', new ItemStack(Blocks.glass, 1),
                    'C', ItemList.Hydraulic_Arm_Steam.get(1),
                    'E', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1)
                });

            // Steam Wood Cutter
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamWoodcutter.get(1),
                new Object[] {
                    "AAA",
                    "DCD",
                    "EBE",
                    'A', ItemList.Bronze_Wood_Casing.get(1),
                    'B', ItemList.Hydraulic_Pump_Steam.get(1),
                    'D', new ItemStack(Blocks.glass, 1),
                    'C', new ItemStack(Blocks.dirt, 1),
                    'E', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1)
                });

            // Steam Manufactrer
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamManufacturer.get(1),
                new Object[] {
                    "AAA",
                    "DCD",
                    "EBE",
                    'A', ItemList.Hydraulic_Arm_Steam.get(1),
                    'B', ItemList.Hydraulic_Conveyor_Steam.get(1),
                    'D', ItemList.Casing_SolidSteel.get(1),
                    'C', GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Breel, 1),
                    'E', ItemList.Casing_Gearbox_Steel.get(1)
                });

            // Mega Oreproc

            // Mega Grinder
            RA.stdBuilder()
                .itemInputs(GregtechItemList.Controller_SteamMaceratorMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaMaceratorMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Washer
            RA.stdBuilder()
                .itemInputs(GregtechItemList.Controller_SteamWasherMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaWasherMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Hammer
            RA.stdBuilder()
                .itemInputs(GregtechItemList.Controller_SteamForgeHammerMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaHammerMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Centrifuge
            RA.stdBuilder()
                .itemInputs(GregtechItemList.Controller_SteamCentrifugeMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaCentrifugeMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

        }

        // Spotless:on

    }
}
