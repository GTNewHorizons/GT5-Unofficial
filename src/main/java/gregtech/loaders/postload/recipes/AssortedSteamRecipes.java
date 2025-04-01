package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.enums.Mods.BuildCraftFactory;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.StorageDrawers;
import static gregtech.api.recipe.RecipeMaps.alloySmelterRecipes;
import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.lavaMakerRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamCarpenterRecipes;
import static gregtech.api.recipe.RecipeMaps.steamConformerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamFusionReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.steamManufacturerRecipes;
import static gregtech.api.recipe.RecipeMaps.steamWoodcutterRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.recipe.metadata.SteamFusionTierKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class AssortedSteamRecipes implements Runnable {

    @Override
    public void run() {

        RA.stdBuilder()
            .fluidInputs(GTModHandler.getSteam(16000), Materials.Creosote.getFluid(4000))
            .fluidOutputs(FluidUtils.getSuperHeatedSteam(16000))
            .duration(10 * TICKS)
            .eut(0)
            .addTo(steamFusionReactorRecipes);

        RA.stdBuilder()
            .fluidInputs(FluidUtils.getSuperHeatedSteam(16000), Materials.Creosote.getFluid(4000))
            .fluidOutputs(FluidRegistry.getFluidStack("supercriticalsteam", 16000))
            .duration(10 * TICKS)
            .eut(0)
            .addTo(steamFusionReactorRecipes);

        RA.stdBuilder()
            .fluidInputs(Materials.Water.getFluid(100), Materials.Lava.getFluid(125))
            .fluidOutputs(FluidRegistry.getFluidStack("supercriticalsteam", 16000))
            .duration(10 * TICKS)
            .eut(0)
            .metadata(SteamFusionTierKey.INSTANCE, 1)
            .addTo(steamFusionReactorRecipes);

        RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1))
            .fluidInputs(Materials.Water.getFluid(100))
            .itemOutputs(new ItemStack(Items.clay_ball, 1))
            .duration(4 * TICKS)
            .eut(2)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stone, 1))
            .fluidOutputs(FluidUtils.getLava(1000))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(lavaMakerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 1))
            .itemOutputs(ItemList.IC2_Mixed_Metal_Ingot.get(3))
            .duration(5 * SECONDS)
            .eut(28)
            .addTo(alloySmelterRecipes);

        RA.stdBuilder()
            .fluidInputs(GTModHandler.getSteam(100000))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CompressedSteam, 1))
            .duration(4 * SECONDS)
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
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Silver, 3), GTUtility.getIntegratedCircuit(3))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateTriple, Materials.Silver, 1))
            .duration(2 * SECONDS)
            .eut(28)
            .addTo(hammerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 5), GTUtility.getIntegratedCircuit(5))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Steel, 1))
            .duration(2 * SECONDS)
            .eut(28)
            .addTo(hammerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.ingot, Materials.Iron, 1))
            .fluidInputs(FluidUtils.getSuperHeatedSteam(1))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.WroughtIron, 1))
            .duration(4*TICKS)
            .eut(28)
            .addTo(mixerRecipes);

        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Items.clay_ball, 16), ItemList.Shape_Extruder_Pipe_Large.get(0))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Clay, 1))
            .duration(4*TICKS)
            .eut(28)
            .addTo(steamConformerRecipes);

        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.cobblestone,8), GTUtility.getIntegratedCircuit(8))
            .itemOutputs(new ItemStack(Blocks.furnace,8))
            .duration(1 * SECONDS)
            .eut(8)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(IndustrialCraft2.ID, "blockAlloyGlass", 3),
                GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Steel, 2),
                GTOreDictUnificator.get(OrePrefixes.plateTriple, Materials.Silver, 3),
                ItemList.Hull_HP_Bricks.get(1))
            .itemOutputs(ItemList.Machine_HP_Solar.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.WroughtIron, 5),
                new ItemStack(Blocks.brick_block,3))
            .itemOutputs(ItemList.Hull_HP_Bricks.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.cactus, 0))
            .itemOutputs(new ItemStack(Blocks.cactus, 64))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(steamWoodcutterRecipes);

        RA.stdBuilder()
            .itemInputs(new ItemStack(Items.reeds, 0))
            .itemOutputs(new ItemStack(Items.reeds, 64))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(16)
            .addTo(steamWoodcutterRecipes);

        RA.stdBuilder()
            .itemInputs(ItemList.Firebrick.get(6), GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gypsum, 2))
            .fluidInputs(Materials.Concrete.getMolten(500))
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
                "boltCrudeSteel" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Arm_Steam.get(1),
            new Object[] { "AAA", "BCB", "DEC", 'A', "plateCrudeSteel", 'B', ItemList.Hydraulic_Motor_Steam.get(1), 'C',
                "stickIron", 'D', ItemList.Hydraulic_Piston_Steam.get(1), 'E', "gearCrudeSteel" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Conveyor_Steam.get(1),
            new Object[] { "AAA", "BCB", "AAA", 'A', "plateRubber", 'B', ItemList.Hydraulic_Motor_Steam.get(1), 'C',
                "gearCrudeSteel" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Regulator_Steam.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "gearBronze", 'B', "pipeTinyBronze", 'C',
                ItemList.Steel_Turbine.get(1), 'D', ItemList.Hydraulic_Pump_Steam.get(1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Sensor_Steam.get(1),
            new Object[] { "ABC", "ACB", "DAA", 'A', "plateCompressedSteam", 'B', "plateRubber", 'C', "pipeHugeStronze",
                'D', ItemList.Hydraulic_Pump_Steam.get(1) });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Emitter_Steam.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "plateCompressedSteam", 'B', "stickCompressedSteam", 'C',
                "pipeHugeBreel", 'D', "gemExquisiteSalt" });
        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Vapor_Generator.get(1),
            new Object[] { "ABC", "BDB", "CBA", 'A', "plateSuperdenseCompressedSteam", 'B', "frameGtCompressedSteam",
                'C', ItemList.Hydraulic_Emitter_Steam.get(1), 'D', ItemList.Steam_Turbine.get(1) });

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

        Materials[] pipeMaterials = { Materials.Bronze, Materials.WroughtIron, Materials.Copper, Materials.Steel,
            Materials.Stronze, Materials.CompressedSteam };

        for (Materials aMat : pipeMaterials) {
            RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.pipeMedium, aMat, 4), GTUtility.getIntegratedCircuit(9))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeQuadruple, aMat, 1))
                .duration(3 * SECONDS)
                .eut(4)
                .addTo(steamManufacturerRecipes);

            RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get(OrePrefixes.pipeSmall, aMat, 9), GTUtility.getIntegratedCircuit(9))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.pipeNonuple, aMat, 1))
                .duration(3 * SECONDS)
                .eut(4)
                .addTo(steamManufacturerRecipes);
        }

        RA.stdBuilder()
            .itemInputs(MaterialsAlloy.TUMBAGA.getRod(4), GTUtility.getIntegratedCircuit(24))
            .itemOutputs(MaterialsAlloy.TUMBAGA.getFrameBox(1))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

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
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.CrudeSteel, 1),
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CrudeSteel, 3),
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
                ItemList.Steel_Turbine.get(2),
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
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Breel, 2))
            .itemOutputs(ItemList.Hydraulic_Emitter_Steam.get(1))
            .duration(1 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.Steam_Turbine.get(1),
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
            .itemOutputs(ItemList.Steel_Turbine.get(1))
            .duration(23 * SECONDS)
            .eut(512)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.turbineBlade, Materials.CompressedSteam, 4),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Breel, 1))
            .itemOutputs(ItemList.Steam_Turbine.get(1))
            .duration(23 * SECONDS)
            .eut(512)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Wood, 3),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Iron, 1),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1),
                new ItemStack(Blocks.cobblestone, 4))
            .itemOutputs(new ItemStack(Blocks.piston, 1))
            .duration(3 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        if (StorageDrawers.isModLoaded()) {
            // Drawer template
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.piston, 1), GTOreDictUnificator.get("drawerBasic", 1))
                .itemOutputs(GTModHandler.getModItem(StorageDrawers.ID, "upgradeTemplate", 3, 0))
                .duration(10 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(steamManufacturerRecipes);
        }

        // Spotless:off

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tin, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Steel, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Clay, 1),
                GTUtility.getIntegratedCircuit(4))
            .itemOutputs(GregtechItemList.GTFluidTank_ULV.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.chest, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Iron, 5),
                GTUtility.getIntegratedCircuit(5))
            .itemOutputs(new ItemStack(Blocks.hopper, 1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Hatches

        // Steam Hatch

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.GTFluidTank_ULV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 6),
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Stronze, 2),
                GTUtility.getIntegratedCircuit(3))
            .itemOutputs(GregtechItemList.Hatch_Input_Steam.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Pipeless

        RA.stdBuilder()
            .itemInputs(
                ItemList.Bronze_Wood_Casing.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Breel, 1),
                ItemList.Hatch_Input_Bus_LV.get(1),
                ItemList.Hydraulic_Regulator_Steam.get(2),
                GTUtility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Pipeless_Hatch_Steam.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.Bronze_Wood_Casing.get(4),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Breel, 1),
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 1),
                ItemList.Hatch_Output_Bus_LV.get(1),
                ItemList.Hydraulic_Regulator_Steam.get(2),
                GTUtility.getIntegratedCircuit(3))
            .itemOutputs(ItemList.Pipeless_Vent_Steam.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Fluid hatches

        RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0),
                ItemList.Casing_BronzePlatedBricks.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Rubber, 1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Hatch_Input_ULV.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);
        RA.stdBuilder()
            .itemInputs(
                GTModHandler.getModItem(BuildCraftFactory.ID, "tankBlock", 1L, 0),
                ItemList.Casing_BronzePlatedBricks.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Rubber, 6),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Rubber, 1),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Hatch_Output_ULV.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_ULV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Stronze, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Stronze, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Stronze, 1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Hatch_Input_LV.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);
        RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Output_ULV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Stronze, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Stronze, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Stronze, 1),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Hatch_Output_LV.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Input_LV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.CompressedSteam, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(ItemList.Hatch_Input_MV.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);
        RA.stdBuilder()
            .itemInputs(
                ItemList.Hatch_Output_LV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.CompressedSteam, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 1),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(ItemList.Hatch_Output_MV.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Buses

        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.hopper, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4),
                MaterialsAlloy.TUMBAGA.getPlate(4),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Hatch_Input_Bus_Steam.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);
        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.hopper, 1),
                MaterialsAlloy.TUMBAGA.getPlate(4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(GregtechItemList.Hatch_Output_Bus_Steam.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Hatch_Input_Bus_Steam.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Stronze, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Stronze, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Stronze, 1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Hatch_Input_Bus_SteamMK2.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);
        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Hatch_Output_Bus_Steam.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Stronze, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Stronze, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Stronze, 1),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(GregtechItemList.Hatch_Output_Bus_SteamMK2.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Hatch_Input_Bus_SteamMK2.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.CompressedSteam, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 1),
                GTUtility.getIntegratedCircuit(1))
            .itemOutputs(GregtechItemList.Hatch_Input_Bus_SteamMK3.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);
        RA.stdBuilder()
            .itemInputs(
                GregtechItemList.Hatch_Output_Bus_SteamMK2.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.CompressedSteam, 1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 1),
                GTUtility.getIntegratedCircuit(2))
            .itemOutputs(GregtechItemList.Hatch_Output_Bus_SteamMK3.get(1L))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Machine Casings

        // Bronze
        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.brick_block, 1),
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
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Bronze, 1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Bronze, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Bronze, 4))
            .itemOutputs(ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 2, 1))
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
            new Object[] { "AAA", "BCB", "AAA", 'A', "plateCrudeSteel", 'B', "frameGtBreel", 'C',
                ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 0, 1) });

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
            new Object[] { "AAA", "wBh", "AAA", 'A', "stickCrudeSteel", 'B', "frameGtBreel" });

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
            new Object[] { "ABA", "BBB", "ABA", 'A', "plateStronze", 'B', "frameGtCrudeSteel" });

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
            new Object[] { "ABA", "BCB", "ABA", 'A', "plateCrudeSteel", 'B', "pipeMediumBreel", 'C',
                "frameGtCrudeSteel" });

        // Solar Cell Casing
        RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.glass, 3),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Stronze, 2),
                ItemList.Machine_HP_Solar.get(1))
            .itemOutputs(ItemList.Solar_Boiler_Cell.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Solar_Boiler_Cell.get(1),
            new Object[] { "AAA", "BCB", 'A', new ItemStack(Blocks.glass, 1), 'B',
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Stronze, 1), 'C',
                ItemList.Machine_HP_Solar.get(1) });

        // Hydraulic Assembling Casing
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Stronze, 4),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 2),
                ItemList.Hydraulic_Arm_Steam.get(3))
            .itemOutputs(ItemList.Hydraulic_Assembling_Casing.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Hydraulic_Assembling_Casing.get(1),
            new Object[] { "ABA", "CCC", "ABA", 'A',
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Stronze, 1), 'B',
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 1), 'C',
                ItemList.Hydraulic_Arm_Steam.get(1) });

        // Hyper Pressure Breel Casing
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Beryllium, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Beryllium, 1))
            .itemOutputs(ItemList.Hyper_Pressure_Breel_Casing.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Hyper_Pressure_Breel_Casing.get(1),
            new Object[] { "AAA", "BCB", "AAA", 'A', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 1),
                'B', GTOreDictUnificator.get(OrePrefixes.stick, Materials.Beryllium, 1), 'C',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Beryllium, 1) });

        // Breel-Plated Casing
        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 2),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Breel, 6),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1))
            .itemOutputs(ItemList.Breel_Casing.get(1))
            .duration(2 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        GTModHandler.addCraftingRecipe(
            ItemList.Breel_Casing.get(1),
            new Object[] { "AAA", "BCB", "AAA", 'A', GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.Breel, 1),
                'B', GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 1), 'C',
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1) });

        // Compact Pipe Casing
        RA.stdBuilder()
            .itemInputs(
                ItemList.Breel_Pipe_Casing.get(1),
                GTOreDictUnificator.get(OrePrefixes.pipeTiny, Materials.CompressedSteam, 2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CompressedSteam, 6))
            .itemOutputs(ItemList.Compact_Steam_Pipe_Casing.get(1))
            .duration(6 * SECONDS)
            .eut(24)
            .addTo(steamManufacturerRecipes);

        // Weighted Pressure Plates
        RA.stdBuilder()
            .itemInputs(Materials.Gold.getPlates(2), GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Steel, 4))
            .itemOutputs(new ItemStack(Blocks.light_weighted_pressure_plate))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        RA.stdBuilder()
            .itemInputs(Materials.Iron.getPlates(2), GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Steel, 4))
            .itemOutputs(new ItemStack(Blocks.heavy_weighted_pressure_plate))
            .duration(5 * SECONDS)
            .eut(16)
            .addTo(steamManufacturerRecipes);

        // Machine Controllers
        {

            // Steam Fuser
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamAlloySmelterMulti.get(1),
                new Object[] { "ABA", "CDC", "ABA", 'A', ItemList.Casing_BronzePlatedBricks.get(1), 'B',
                    new ItemStack(Blocks.furnace, 1), 'C',
                    GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Steel, 1), 'D',
                    new ItemStack(Blocks.nether_brick, 1) });

            // Steam Conformer
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamExtruder.get(1),
                new Object[] { "BCA", "DEF", "AGA", 'A', ItemList.Casing_BronzePlatedBricks.get(1), 'B',
                    ItemList.Hydraulic_Motor_Steam.get(1), 'C', ItemList.Hydraulic_Piston_Steam.get(1), 'D',
                    new ItemStack(Blocks.anvil, 1), 'E', GregtechItemList.Controller_SteamForgeHammerMulti.get(1), 'F',
                    GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Steel, 1), 'G',
                    ItemList.Hydraulic_Conveyor_Steam.get(1) });

            // Open Heart Blast Furnace
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamBlastFurnace.get(1),
                new Object[] { "ABA", "BCB", "ABA", 'A', ItemList.Stronze_Casing.get(1), 'B',
                    GTOreDictUnificator.get(OrePrefixes.plateQuintuple, Materials.Steel, 1), 'C',
                    ItemList.Machine_Bricked_BlastFurnace.get(1) });

            // Steam Forge
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamMultiSmelter.get(1),
                new Object[] { "ABA", "CDC", "ABA", 'A', ItemList.Breel_Pipe_Casing.get(1), 'B',
                    new ItemStack(Blocks.furnace, 1), 'C', ItemList.Stronze_Casing.get(1), 'D',
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Stronze, 1) });

            // Steam Rock Breaker
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamRockBreaker.get(1),
                new Object[] { "ABA", "CDE", "ABA", 'A', ItemList.Casing_BronzePlatedBricks.get(1), 'B',
                    ItemUtils.simpleMetaStack(GregTechAPI.sBlockCasings2, 12, 1), 'C',
                    new ItemStack(Items.water_bucket, 1), 'D',
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CrudeSteel, 1), 'E',
                    new ItemStack(Items.lava_bucket, 1) });

            // Steam Extractinator
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamExtractinator.get(1),
                new Object[] { "ABA", "CDC", "ABA", 'A', ItemList.Vibration_Safe_Casing.get(1), 'B',
                    ItemList.Casing_BronzePlatedBricks.get(1), 'C', ItemList.Hydraulic_Pump_Steam.get(1), 'D',
                    GTOreDictUnificator.get(OrePrefixes.pipeNonuple, Materials.Stronze, 1) });

            // Steam Carpenter
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamCarpenter.get(1),
                new Object[] { "ABA", "CDC", "EEE", 'A', ItemList.Casing_BronzePlatedBricks.get(1), 'B',
                    ItemList.Hydraulic_Piston_Steam.get(1), 'D', new ItemStack(Blocks.glass, 1), 'C',
                    ItemList.Hydraulic_Arm_Steam.get(1), 'E',
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1) });

            // Steam Wood Cutter
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamWoodcutter.get(1),
                new Object[] { "AAA", "DCD", "EBE", 'A', ItemList.Bronze_Wood_Casing.get(1), 'B',
                    ItemList.Hydraulic_Pump_Steam.get(1), 'D', new ItemStack(Blocks.glass, 1), 'C',
                    new ItemStack(Blocks.dirt, 1), 'E',
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Wood, 1) });

            // Steam Manufactrer
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamManufacturer.get(1),
                new Object[] { "AAA", "DCD", "EBE", 'A', ItemList.Hydraulic_Arm_Steam.get(1), 'B',
                    ItemList.Hydraulic_Conveyor_Steam.get(1), 'D', ItemList.Hydraulic_Assembling_Casing.get(1), 'C',
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Breel, 1), 'E',
                    ItemList.Casing_Gearbox_Steel.get(1) });

            // Mega Solar Boiler
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_MegaSolarBoiler.get(1),
                new Object[] { "AAA", "DCD", "EBE", 'A', ItemList.Solar_Boiler_Cell.get(1), 'B',
                    ItemList.Casing_SolidSteel.get(1), 'D', ItemList.Hydraulic_Pump_Steam.get(1), 'C', ItemList.Hull_HP,
                    'E', ItemList.Casing_BronzePlatedBricks });

            // Cactus Wonder
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_CactusWonder.get(1),
                new Object[] { "ABA", "ACA", "ABA", 'A', new ItemStack(Blocks.cactus, 1), 'B',
                    ItemList.Casing_BronzePlatedBricks.get(1), 'C', ItemList.Hydraulic_Regulator_Steam.get(1) });

            // Lavamaker
            RA.stdBuilder()
                .itemInputs(
                    ItemList.Stronze_Casing.get(1),
                    ItemList.Hydraulic_Motor_Steam.get(2),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Stronze, 2),
                    GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Breel, 2))
                .itemOutputs(GregtechItemList.Controller_SteamLavaMaker.get(1))
                .duration(10 * SECONDS)
                .eut(200)
                .addTo(steamManufacturerRecipes);

            // Pipeless Hatch
            GTModHandler.addCraftingRecipe(
                ItemList.Pipeless_Hatch_Steam.get(1),
                new Object[] { "AEA", "CBD", "AEA", 'A', ItemList.Bronze_Wood_Casing.get(1), 'B',
                    ItemList.Hatch_Input_Bus_LV.get(1), 'C',
                    GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 1), 'D',
                    GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Breel, 1), 'E',
                    ItemList.Hydraulic_Regulator_Steam.get(1) });

            // Pipeless Vent
            GTModHandler.addCraftingRecipe(
                ItemList.Pipeless_Vent_Steam.get(1),
                new Object[] { "AEA", "CBD", "AEA", 'A', ItemList.Bronze_Wood_Casing.get(1), 'B',
                    ItemList.Hatch_Output_Bus_LV.get(1), 'C',
                    GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Breel, 1), 'D',
                    GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 1), 'E',
                    ItemList.Hydraulic_Regulator_Steam.get(1) });

            // Jetstream Hatch
            RA.stdBuilder()
                .itemInputs(
                    ItemList.Pipeless_Hatch_Steam.get(4),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.CompressedSteam, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Breel, 1))
                .itemOutputs(ItemList.Pipeless_Hatch_Jetstream.get(1))
                .duration(20 * SECONDS)
                .eut(400)
                .addTo(steamManufacturerRecipes);

            // Jetstream Vent
            RA.stdBuilder()
                .itemInputs(
                    ItemList.Pipeless_Vent_Steam.get(4),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.CompressedSteam, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Stronze, 1))
                .itemOutputs(ItemList.Pipeless_Vent_Jetstream.get(1))
                .duration(20 * SECONDS)
                .eut(400)
                .addTo(steamManufacturerRecipes);

            // Supercompressor
            RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.Controller_SteamCompressorMulti.get(64),
                    ItemList.Hydraulic_Pump_Steam.get(4))
                .itemOutputs(GregtechItemList.Controller_MegaSteamCompressor.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Fuser
            RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.Controller_SteamAlloySmelterMulti.get(64),
                    ItemList.Hydraulic_Emitter_Steam.get(2))
                .itemOutputs(GregtechItemList.Controller_SteamMegaAlloySmelter.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Progenitor
            RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.Controller_SteamManufacturer.get(64),
                    ItemList.Hydraulic_Regulator_Steam.get(16),
                    ItemList.Hydraulic_Arm_Steam.get(8),
                    ItemList.Hydraulic_Sensor_Steam.get(4),
                    ItemList.Hydraulic_Emitter_Steam.get(4),
                    ItemList.Hydraulic_Vapor_Generator.get(2),
                    ItemList.Stronze_Casing.get(32),
                    ItemList.Breel_Pipe_Casing.get(32),
                    ItemList.Steel_Wood_Casing.get(32))
                .itemOutputs(GregtechItemList.Controller_SteamGateAssembler.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Fusion Reactor
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_SteamFusionReactor.get(1),
                new Object[] { "ABA", "DCD", "ABA", 'A',
                    GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 1), 'C',
                    GregtechItemList.Controller_MegaSolarBoiler.get(1), 'B',
                    GTOreDictUnificator.get(OrePrefixes.gem, Materials.Emerald, 1), 'D',
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium, 1) });

            // Infernal Coke Oven
            GTModHandler.addCraftingRecipe(
                GregtechItemList.Controller_InfernalCokeOven.get(1),
                new Object[] { "ABA", "DCD", "ABA", 'A', new ItemStack(Blocks.nether_brick, 1), 'C',
                    ItemList.Machine_Bricked_BlastFurnace.get(1), 'B',
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 1), 'D',
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Stronze, 1) });

            // Compact Fusion
            RA.stdBuilder()
                .itemInputs(
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    GregtechItemList.Controller_SteamFusionReactor.get(64),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    ItemList.Hydraulic_Vapor_Generator.get(1),
                    ItemList.Hydraulic_Vapor_Generator.get(1))
                .itemOutputs(GregtechItemList.Controller_SteamCompactFusionReactor.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Oreproc

            // Mega Grinder
            RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.Controller_SteamMaceratorMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaMaceratorMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Washer
            RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.Controller_SteamWasherMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaWasherMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Hammer
            RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.Controller_SteamForgeHammerMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaHammerMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

            // Mega Centrifuge
            RA.stdBuilder()
                .itemInputs(
                    GregtechItemList.Controller_SteamCentrifugeMulti.get(64),
                    GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 8))
                .itemOutputs(GregtechItemList.Controller_SteamMegaCentrifugeMulti.get(1))
                .duration(120 * SECONDS)
                .eut(1600)
                .addTo(steamManufacturerRecipes);

        }

        // Spotless:on

    }
}
