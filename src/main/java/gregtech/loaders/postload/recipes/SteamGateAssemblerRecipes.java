package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.steamGateAssemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class SteamGateAssemblerRecipes implements Runnable {

    @Override
    public void run() {
        // Steam components
        ItemStack steamMotor = ItemList.Hydraulic_Motor_Steam.get(1);
        ItemStack steamPump = ItemList.Hydraulic_Pump_Steam.get(1);
        ItemStack steamPiston = ItemList.Hydraulic_Piston_Steam.get(1);
        ItemStack steamArm = ItemList.Hydraulic_Arm_Steam.get(1);
        ItemStack steamConveyor = ItemList.Hydraulic_Conveyor_Steam.get(1);
        ItemStack steamRegulator = ItemList.Hydraulic_Regulator_Steam.get(1);
        ItemStack steamFieldGen = ItemList.Hydraulic_Vapor_Generator.get(1);
        ItemStack steamEmitter = ItemList.Hydraulic_Emitter_Steam.get(1);
        ItemStack steamSensor = ItemList.Hydraulic_Sensor_Steam.get(1);
        // Steamgate parts
        ItemStack gatePlate = ItemList.Steamgate_Plate.get(1);
        ItemStack gateFrame = ItemList.Steamgate_Frame.get(1);
        ItemStack gateChevron = ItemList.Steamgate_Chevron.get(1);
        ItemStack gateChevronUpgrade = ItemList.Steamgate_Chevron_Upgrade.get(1);
        ItemStack gateCrystal = ItemList.Steamgate_Core_Crystal.get(1);
        ItemStack gateIris = ItemList.Steamgate_Iris_Blade.get(1);
        ItemStack gateIrisUpgrade = ItemList.Steamgate_Iris_Upgrade.get(1);
        ItemStack gateRingBlock = ItemList.Steamgate_Ring_Block.get(1);
        ItemStack gateChevronBlock = ItemList.Steamgate_Chevron_Block.get(1);
        // Blocks
        ItemStack steelBlock = GTOreDictUnificator.get(OrePrefixes.block, Materials.Steel, 1);
        ItemStack bronzeBlock = GTOreDictUnificator.get(OrePrefixes.block, Materials.Bronze, 1);
        // Superdense plates
        ItemStack superdenseBronze = GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Bronze, 1);
        ItemStack superdenseSteel = GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Steel, 1);
        ItemStack superdenseBreel = GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Breel, 1);
        ItemStack superdenseStronze = GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Stronze, 1);
        ItemStack superdenseSteam = GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CompressedSteam, 1);
        // Pipes
        ItemStack stronzePipe = GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Stronze, 1);
        ItemStack stronzePipeH = GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Stronze, 1);
        ItemStack breelPipeL = GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Breel, 1);
        // Plates
        ItemStack stronzePlate = GTOreDictUnificator.get(OrePrefixes.plate, Materials.Stronze, 1);
        ItemStack breelPlate = GTOreDictUnificator.get(OrePrefixes.plate, Materials.Breel, 1);
        // Rings
        ItemStack stronzeRing = GTOreDictUnificator.get(OrePrefixes.ring, Materials.Stronze, 1);
        ItemStack breelRing = GTOreDictUnificator.get(OrePrefixes.ring, Materials.Breel, 1);
        // Gears
        ItemStack stronzeGear = GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Stronze, 1);
        ItemStack breelGear = GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Breel, 1);
        ItemStack steamGear = GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.CompressedSteam, 1);
        // Small gears
        ItemStack stronzeSmallGear = GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Stronze, 1);
        ItemStack breelSmallGear = GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Breel, 1);
        ItemStack steamSmallGear = GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CompressedSteam, 1);
        // Long rods
        ItemStack stronzeLongRod = GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Stronze, 1);
        ItemStack breelLongRod = GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Breel, 1);
        ItemStack steamLongRod = GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.CompressedSteam, 1);
        // Reinforced wood
        ItemStack ironWood = ItemList.Iron_Wood_Casing.get(1);
        ItemStack bronzeWood = ItemList.Bronze_Wood_Casing.get(1);
        ItemStack steelWood = ItemList.Steel_Wood_Casing.get(1);
        // Gems
        ItemStack exDiam = GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Diamond, 1);
        ItemStack exEmer = GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Emerald, 1);
        ItemStack exRuby = GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Ruby, 1);
        ItemStack exSalt = GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Salt, 1);
        // Misc
        ItemStack ironPressure = new ItemStack(Blocks.heavy_weighted_pressure_plate, 1);
        ItemStack goldPressure = new ItemStack(Blocks.light_weighted_pressure_plate, 1);
        ItemStack megaCompressor = GregtechItemList.Controller_MegaSteamCompressor.get(1);
        ItemStack extractinator = GregtechItemList.Controller_SteamExtractinator.get(1);
        ItemStack pipelessHatch = ItemList.Pipeless_Hatch_Jetstream.get(1);

        // Blank recipe for copy-paste convenience
        /*
         * GTValues.RA.stdBuilder()
         * .itemInputs(
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null,
         * null, null, null, null, null, null, null, null, null)
         * .itemOutputs(null)
         * .duration(20 * SECONDS)
         * .eut((int) TierEU.RECIPE_LV)
         * .addTo(steamGateAssemblerRecipes);
         */

        // spotless:off

        // Steamgate controller
        GTValues.RA.stdBuilder()
            .itemInputs(
                ironWood, steamFieldGen, steamFieldGen, steamEmitter, steamGear, steamEmitter, steamFieldGen, steamFieldGen, ironWood,
                steamFieldGen, steelWood, superdenseBreel, gatePlate, steamConveyor, gatePlate, superdenseStronze, steelWood, steamFieldGen,
                steamFieldGen, bronzeWood, pipelessHatch, gatePlate, steamArm, gatePlate, pipelessHatch, bronzeWood, steamFieldGen,
                steamEmitter, stronzeRing, extractinator, gateChevronBlock, gateChevronBlock, gateChevronBlock, extractinator, stronzeRing, steamEmitter,
                steamGear, breelRing, megaCompressor, gateChevronBlock, gateCrystal, gateChevronBlock, megaCompressor, breelRing, steamGear,
                steamEmitter, stronzeRing, extractinator, gateChevronBlock, gateChevronBlock, gateChevronBlock, extractinator, stronzeRing, steamEmitter,
                steamFieldGen, bronzeWood, pipelessHatch, gatePlate, gateIrisUpgrade, gatePlate, pipelessHatch, bronzeWood, steamFieldGen,
                steamFieldGen, steelWood, superdenseStronze, gatePlate, steamConveyor, gatePlate, superdenseBreel, steelWood, steamFieldGen,
                ironWood, steamFieldGen, steamFieldGen, steamEmitter, steamGear, steamEmitter, steamFieldGen, steamFieldGen, ironWood)
            .itemOutputs(GregtechItemList.Controller_Steamgate.get(1))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate Ring Block
        GTValues.RA.stdBuilder()
            .itemInputs(
                bronzeWood, bronzeWood, bronzeWood, gateFrame, gateChevron, gateFrame, gateFrame, gatePlate, gatePlate,
                ironWood, ironWood, ironWood, gateFrame, gateFrame, gateFrame, gatePlate, steamFieldGen, steamFieldGen,
                bronzeWood, bronzeWood, bronzeWood, gateFrame, gateFrame, gatePlate, steamFieldGen, null, null,
                steelWood, steelWood, steelWood, gateFrame, gateFrame, gatePlate, steamFieldGen, null, null,
                steelWood, steelWood, steelWood, gateFrame, gateChevron, gatePlate, steamFieldGen, null, null,
                steelWood, steelWood, steelWood, gateFrame, gateFrame, gatePlate, steamFieldGen, null, null,
                bronzeWood, bronzeWood, bronzeWood, gateFrame, gateFrame, gatePlate, steamFieldGen, null, null,
                ironWood, ironWood, ironWood, gateFrame, gateFrame, gateFrame, gatePlate, steamFieldGen, steamFieldGen,
                bronzeWood, bronzeWood, bronzeWood, gateFrame, gateChevron, gateFrame, gateFrame, gatePlate, gatePlate)
            .itemOutputs(gateRingBlock)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate Chevron Block
        GTValues.RA.stdBuilder()
            .itemInputs(
                null, null, null, null, bronzeWood, null, null, null, null,
                null, null, null, bronzeWood, ironWood, bronzeWood, null, null, null,
                null, null, bronzeWood, ironWood, steamFieldGen, ironWood, bronzeWood, null, null,
                null, bronzeWood, ironWood, steelWood, gateChevronUpgrade, steelWood, ironWood, bronzeWood, null,
                bronzeWood, ironWood, steamFieldGen, gateChevronUpgrade, gateRingBlock, gateChevronUpgrade, steamFieldGen, ironWood, bronzeWood,
                null, bronzeWood, ironWood, steelWood, gateChevronUpgrade, steelWood, ironWood, bronzeWood, null,
                null, null, bronzeWood, ironWood, steamFieldGen, ironWood, bronzeWood, null, null,
                null, null, null, bronzeWood, ironWood, bronzeWood, null, null, null,
                null, null, null, null, bronzeWood, null, null, null, null)
            .itemOutputs(gateChevronBlock)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate Iris Blade
        GTValues.RA.stdBuilder()
            .itemInputs(
                null, null, null, null, null, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze,
                null, null, null, null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseBronze, null,
                null, null, null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseBronze, null, null,
                null, null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseBronze, null, null, null,
                null, superdenseBronze, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null, null, null,
                superdenseBronze, steamPiston, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null, null, null,
                superdenseBronze, steamPiston, superdenseSteel, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null, null,
                superdenseBronze, steelWood, steamPiston, steamPiston, superdenseSteel, superdenseSteel, superdenseSteel, superdenseBronze, null,
                bronzeWood, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze, superdenseBronze)
            .itemOutputs(ItemList.Steamgate_Iris_Blade.get(1))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate Frame
        GTValues.RA.stdBuilder()
            .itemInputs(
                steamLongRod, stronzeLongRod, breelLongRod, stronzeLongRod, steamLongRod, stronzeLongRod, breelLongRod, stronzeLongRod, steamLongRod,
                stronzeLongRod, breelGear, steamSmallGear, stronzeGear, steamSmallGear, stronzeGear, steamSmallGear, breelGear, stronzeLongRod,
                breelLongRod, stronzeSmallGear, steamGear, stronzeSmallGear, stronzeGear, stronzeSmallGear, steamGear, stronzeSmallGear, breelLongRod,
                stronzeLongRod, breelGear, breelSmallGear, stronzeGear, breelSmallGear, stronzeGear, breelSmallGear, breelGear, stronzeLongRod,
                steamLongRod, steamSmallGear, stronzeGear, breelSmallGear, steamGear, breelSmallGear, stronzeGear, steamSmallGear, steamLongRod,
                stronzeLongRod, breelGear, breelSmallGear, stronzeGear, breelSmallGear, stronzeGear, breelSmallGear, breelGear, stronzeLongRod,
                breelLongRod, stronzeSmallGear, steamGear, stronzeSmallGear, stronzeGear, stronzeSmallGear, steamGear, stronzeSmallGear, breelLongRod,
                stronzeLongRod, breelGear, steamSmallGear, stronzeGear, steamSmallGear, stronzeGear, steamSmallGear, breelGear, stronzeLongRod,
                steamLongRod, stronzeLongRod, breelLongRod, stronzeLongRod, steamLongRod, stronzeLongRod, breelLongRod, stronzeLongRod, steamLongRod)
            .itemOutputs(gateFrame)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate core crystal
        GTValues.RA.stdBuilder()
            .itemInputs(
                null, null, null, null, steamFieldGen, null, null, null, null,
                null, null, null, null, steamMotor, null, null, null, null,
                null, null, null, null, steamPiston, null, null, null, null,
                null, null, null, steamMotor, superdenseSteam, steamMotor, null, null, null,
                steamFieldGen, steamMotor, steamPiston, superdenseSteam, superdenseSteam, superdenseSteam, steamPiston, steamMotor, steamFieldGen,
                null, null, null, steamMotor, superdenseSteam, steamMotor, null, null, null,
                null, null, null, null, steamPiston, null, null, null, null,
                null, null, null, null, steamMotor, null, null, null, null,
                null, null, null, null, steamFieldGen, null, null, null, null)
            .itemOutputs(gateCrystal)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate chevron
        GTValues.RA.stdBuilder()
            .itemInputs(
                steelWood, steelWood, steelWood, steelWood, steelWood, steelWood, steelWood, steelWood, steelWood,
                steelWood, exDiam, exDiam, exDiam, steamConveyor, exDiam, exDiam, exDiam, steelWood,
                steelWood, exEmer, exEmer, exEmer, breelPipeL, exEmer, exEmer, exEmer, steelWood,
                steelWood, exRuby, exRuby, exRuby, breelPipeL, exRuby, exRuby, exRuby, steelWood,
                steelWood, exSalt, exSalt, exSalt, breelPipeL, exSalt, exSalt, exSalt, steelWood,
                null, steelWood, stronzePlate, stronzePlate, breelPipeL, stronzePlate, stronzePlate, steelWood, null,
                null, null, steelWood, stronzePlate, breelPipeL, stronzePlate, steelWood, null, null,
                null, null, null, steelWood, steamArm, steelWood, null, null, null,
                null, null, null, null, steelWood, null, null, null, null)
            .itemOutputs(gateChevron)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate chevron upgrade
        GTValues.RA.stdBuilder()
            .itemInputs(
                null, null, null, null, null, null, null, null, null,
                gateFrame, steamPiston, gateFrame, steamFieldGen, gateFrame, steamPiston, gateFrame, null, null,
                null, gateFrame, gateChevron, steamPiston, gateChevron, gateFrame, null, null, null,
                null, null, steamSensor, steamFieldGen, steamSensor, null, null, null, null,
                null, null, gateFrame, steamPiston, gateFrame, steamFieldGen, gateFrame, steamPiston, gateFrame,
                null, null, null, gateFrame, gateChevron, steamPiston, gateChevron, gateFrame, null,
                null, null, null, null, steamEmitter, steamFieldGen, steamEmitter, null, null,
                null, null, null, null, null, gateFrame, null, null, null,
                null, null, null, null, null, null, null, null, null)
            .itemOutputs(gateChevronUpgrade)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        // Steamgate plate
        GTValues.RA.stdBuilder()
            .itemInputs(
                ironPressure, goldPressure, ironPressure, goldPressure, ironPressure, goldPressure, ironPressure, goldPressure, ironPressure,
                superdenseSteel, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, superdenseSteel,
                superdenseBronze, stronzePlate, stronzePlate, stronzePlate, stronzePlate, stronzePlate, stronzePlate, stronzePlate, superdenseBronze,
                superdenseSteel, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, superdenseSteel,
                stronzePipe, stronzePipe, stronzePipe, stronzePipe, steamRegulator, stronzePipe, stronzePipe, stronzePipe, stronzePipe,
                superdenseSteel, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, superdenseSteel,
                superdenseBronze, stronzePlate, stronzePlate, stronzePlate, stronzePlate, stronzePlate, stronzePlate, stronzePlate, superdenseBronze,
                superdenseSteel, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, breelPlate, superdenseSteel,
                ironPressure, goldPressure, ironPressure, goldPressure, ironPressure, goldPressure, ironPressure, goldPressure, ironPressure)
            .itemOutputs(gatePlate)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                bronzeWood, steelWood, gateIris, gateIris, gateIris, gateIris, gateIris, steelWood, bronzeWood,
                steelWood, gateIris, null, null, breelGear, null, null, gateIris, steelWood,
                gateIris, null, null, null, null, stronzeSmallGear, null, null, gateIris,
                gateIris, null, stronzeSmallGear, breelGear, null, breelGear, null, null, gateIris,
                gateIris, breelGear, null, null, superdenseSteam, null, null, breelGear, gateIris,
                gateIris, null, null, breelGear, null, breelGear, stronzeSmallGear, null, gateIris,
                gateIris, null, null, stronzeSmallGear, null, null, null, null, gateIris,
                steelWood, gateIris, null, null, breelGear, null, null, gateIris, steelWood,
                bronzeWood, steelWood, gateIris, gateIris, gateIris, gateIris, gateIris, steelWood, bronzeWood)
            .itemOutputs(gateIrisUpgrade)
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                stronzePipeH, breelPlate, breelPlate, breelPlate, stronzePipeH, breelPlate, breelPlate, breelPlate, stronzePipeH,
                breelPlate, stronzePipeH, breelPlate, breelPlate, stronzePipeH, breelPlate, breelPlate, stronzePipeH, breelPlate,
                breelPlate, breelPlate, stronzePipeH, gateFrame, stronzePipeH, gateFrame, stronzePipeH, breelPlate, breelPlate,
                breelPlate, breelPlate, gateFrame, stronzePipeH, stronzePipeH, stronzePipeH, gateFrame, breelPlate, breelPlate,
                stronzePipeH, stronzePipeH, stronzePipeH, stronzePipeH, pipelessHatch, stronzePipeH, stronzePipeH, stronzePipeH, stronzePipeH,
                breelPlate, breelPlate, gateFrame, stronzePipeH, stronzePipeH, stronzePipeH, gateFrame, breelPlate, breelPlate,
                breelPlate, breelPlate, stronzePipeH, gateFrame, stronzePipeH, gateFrame, stronzePipeH, breelPlate, breelPlate,
                breelPlate, stronzePipeH, breelPlate, breelPlate, stronzePipeH, breelPlate, breelPlate, stronzePipeH, breelPlate,
                stronzePipeH, breelPlate, breelPlate, breelPlate, stronzePipeH, breelPlate, breelPlate, breelPlate, stronzePipeH)
            .itemOutputs(ItemList.Steamgate_Dialing_Device.get(1))
            .duration(20 * SECONDS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(steamGateAssemblerRecipes);

        //spotless:on
    }
}
