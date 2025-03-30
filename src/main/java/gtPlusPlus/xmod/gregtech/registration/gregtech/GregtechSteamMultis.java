package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.addItemTooltip;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_CactusWonder;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_InfernalCokeOven;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_MegaSolarBoiler;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamAlloySmelterMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamBlastFurnace;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamCarpenter;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamCentrifugeMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamCompressorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamExtractinator;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamExtruder;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamForgeHammer;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamGateAssembler;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMaceratorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamManufacturer;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMegaCentrifugeMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMegaCompressor;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMegaHammerMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMegaMaceratorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMegaWasherMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMixerMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamRockBreaker;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamWasherMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamWoodcutter;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_Steamgate;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Bus_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Bus_SteamMK2;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Bus_SteamMK3;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Output_Bus_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Output_Bus_SteamMK2;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Output_Bus_SteamMK3;
import static gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList.Controller_SteamMultiSmelter;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.MetaTileEntityIDs;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTEMegaSolarBoiler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamAlloySmelter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamBlastFurnace;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCactusWonder;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCarpenter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCentrifuge;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCompressor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamExtractinator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamExtruder;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamForgeHammer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamGateAssembler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamInfernalCokeOven;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamManufacturer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMegaCentrifuge;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMegaCompressor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMegaForgeHammer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMegaMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMegaWasher;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMixer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMultiSmelter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamRockBreaker;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamWasher;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamWoodcutter;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamgate;

public class GregtechSteamMultis {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Steam Multiblocks.");

        GregtechItemList.Controller_SteamMaceratorMulti.set(
            new MTESteamMacerator(
                Controller_SteamMaceratorMulti.ID,
                "gtpp.multimachine.steam.macerator",
                "Steam Grinder").getStackForm(1L));
        GregtechItemList.Controller_SteamMegaMaceratorMulti.set(
            new MTESteamMegaMacerator(
                Controller_SteamMegaMaceratorMulti.ID,
                "gtpp.multimachine.steam.megamacerator",
                "Mega Steam Grinder").getStackForm(1L));
        addItemTooltip(GregtechItemList.Controller_SteamMegaMaceratorMulti.get(1), GTValues.PipeBluez);
        GregtechItemList.Controller_SteamMegaWasherMulti.set(
            new MTESteamMegaWasher(
                Controller_SteamMegaWasherMulti.ID,
                "gtpp.multimachine.steam.megawasher",
                "Mega Steam Purifier").getStackForm(1L));
        GregtechItemList.Controller_SteamMegaHammerMulti.set(
            new MTESteamMegaForgeHammer(
                Controller_SteamMegaHammerMulti.ID,
                "gtpp.multimachine.steam.megahammer",
                "Mega Steam Presser").getStackForm(1L));
        GregtechItemList.Controller_SteamMegaCentrifugeMulti.set(
            new MTESteamMegaCentrifuge(
                Controller_SteamMegaCentrifugeMulti.ID,
                "gtpp.multimachine.steam.megacentrifuge",
                "Mega Steam Separator").getStackForm(1L));
        GregtechItemList.Controller_SteamCompressorMulti.set(
            new MTESteamCompressor(
                Controller_SteamCompressorMulti.ID,
                "gtpp.multimachine.steam.compressor",
                "Steam Squasher").getStackForm(1L));
        GregtechItemList.Controller_SteamCentrifugeMulti.set(
            new MTESteamCentrifuge(
                Controller_SteamCentrifugeMulti.ID,
                "gtpp.multimachine.steam.centrifuge",
                "Steam Separator").getStackForm(1));
        GregtechItemList.Controller_SteamWasherMulti.set(
            new MTESteamWasher(Controller_SteamWasherMulti.ID, "gtpp.multimachine.steam.washer", "Steam Purifier")
                .getStackForm(1));
        GregtechItemList.Controller_SteamForgeHammerMulti.set(
            new MTESteamForgeHammer(
                Controller_SteamForgeHammer.ID,
                "gtpp.multimachine.steam.forge.hammer",
                "Steam Presser").getStackForm(1));
        GregtechItemList.Controller_SteamMixerMulti.set(
            new MTESteamMixer(Controller_SteamMixerMulti.ID, "gtpp.multimachine.steam.mixer", "Steam Blender")
                .getStackForm(1));
        GregtechItemList.Controller_SteamAlloySmelterMulti.set(
            new MTESteamAlloySmelter(
                Controller_SteamAlloySmelterMulti.ID,
                "gtpp.multimachine.steam.alloysmelter",
                "Steam Fuser").getStackForm(1));
        GregtechItemList.Controller_SteamGateAssembler.set(
            new MTESteamGateAssembler(
                Controller_SteamGateAssembler.ID,
                "gtpp.multimachine.steam.gateassembler",
                "Perfect Steam Progenitor").getStackForm(1));
        GregtechItemList.Controller_Steamgate.set(
            new MTESteamgate(Controller_Steamgate.ID, "gtpp.multimachine.steamgate", "Steamgate Base Block")
                .getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_Steamgate.get(1), GTValues.SteamgateCredits);
        GregtechItemList.Controller_SteamBlastFurnace.set(
            new MTESteamBlastFurnace(
                Controller_SteamBlastFurnace.ID,
                "gtpp.multimachine.steamblastfurnace",
                "Open Hearth Blast Furnace").getStackForm(1));
        Controller_SteamMultiSmelter.set(
            new MTESteamMultiSmelter(
                MetaTileEntityIDs.Controller_SteamMultiSmelter.ID,
                "gtpp.multimachine.steammultismelter",
                "Enhanced Steam Forge").getStackForm(1));
        GregtechItemList.Controller_SteamExtruder.set(
            new MTESteamExtruder(Controller_SteamExtruder.ID, "gtpp.multimachine.steam.extruder", "Steam Conformer")
                .getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_SteamExtruder.get(1), GTValues.AuthorNoc);
        GregtechItemList.Controller_SteamRockBreaker.set(
            new MTESteamRockBreaker(
                Controller_SteamRockBreaker.ID,
                "gtpp.multimachine.steam.rockbreaker",
                "Steam Cobbler").getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_SteamRockBreaker.get(1), GTValues.AuthorNoc);
        GregtechItemList.Controller_SteamExtractinator.set(
            new MTESteamExtractinator(
                Controller_SteamExtractinator.ID,
                "gtpp.multimachine.steam.extractinator",
                "Steam Extractinator").getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_SteamExtractinator.get(1), GTValues.AuthorNoc);
        GregtechItemList.Controller_SteamCarpenter.set(
            new MTESteamCarpenter(Controller_SteamCarpenter.ID, "gtpp.multimachine.steam.carpenter", "Steam Carpenter")
                .getStackForm(1));
        GregtechItemList.Controller_SteamWoodcutter.set(
            new MTESteamWoodcutter(
                Controller_SteamWoodcutter.ID,
                "gtpp.multimachine.steam.woodcutter",
                "Steam Woodcutter").getStackForm(1));
        GregtechItemList.Controller_MegaSteamCompressor.set(
            new MTESteamMegaCompressor(
                Controller_SteamMegaCompressor.ID,
                "gtpp.multimachine.steam.megacompressor",
                "Steam Supercompressor").getStackForm(1));
        GregtechItemList.Controller_CactusWonder.set(
            new MTESteamCactusWonder(
                Controller_CactusWonder.ID,
                "gtpp.multimachine.steam.cactuswonder",
                "Cactus Wonder").getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_CactusWonder.get(1), GTValues.AuthorNoc);
        GregtechItemList.Controller_InfernalCokeOven.set(
            new MTESteamInfernalCokeOven(
                Controller_InfernalCokeOven.ID,
                "gtpp.multimachine.steam.infernalcokeoven",
                "Infernal Coke Oven").getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_InfernalCokeOven.get(1), GTValues.AuthorNoc);
        GregtechItemList.Controller_SteamManufacturer.set(
            new MTESteamManufacturer(
                Controller_SteamManufacturer.ID,
                "gtpp.multimachine.steam.manufacturer",
                "Steam Manufacturer").getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_SteamManufacturer.get(1), GTValues.AuthorNoc);
        GregtechItemList.Controller_MegaSolarBoiler.set(
            new MTEMegaSolarBoiler(
                Controller_MegaSolarBoiler.ID,
                "gtpp.multimachine.steam.megasolarboiler",
                "Mega Pressure Solar Boiler").getStackForm(1));
        addItemTooltip(GregtechItemList.Controller_MegaSolarBoiler.get(1), GTValues.AuthorNoc);

        GregtechItemList.Hatch_Input_Bus_Steam.set(
            new MTEHatchSteamBusInput(Hatch_Input_Bus_Steam.ID, "hatch.input_bus.tier.steam", "Simple Input Bus", 0)
                .getStackForm(1L));
        GregtechItemList.Hatch_Input_Bus_SteamMK2.set(
            new MTEHatchSteamBusInput(
                Hatch_Input_Bus_SteamMK2.ID,
                "hatch.input_bus.tier.steammk2",
                "Breel-Reinforced Input Bus",
                1).getStackForm(1L));
        GregtechItemList.Hatch_Input_Bus_SteamMK3.set(
            new MTEHatchSteamBusInput(
                Hatch_Input_Bus_SteamMK3.ID,
                "hatch.input_bus.tier.steammk3",
                "Steam-Attuned Input Bus",
                2).getStackForm(1L));
        GregtechItemList.Hatch_Output_Bus_Steam.set(
            new MTEHatchSteamBusOutput(Hatch_Output_Bus_Steam.ID, "hatch.output_bus.tier.steam", "Simple Output Bus", 0)
                .getStackForm(1L));
        GregtechItemList.Hatch_Output_Bus_SteamMK2.set(
            new MTEHatchSteamBusOutput(
                Hatch_Output_Bus_SteamMK2.ID,
                "hatch.output_bus.tier.steammk2",
                "Breel-Reinforced Output Bus",
                1).getStackForm(1L));
        GregtechItemList.Hatch_Output_Bus_SteamMK3.set(
            new MTEHatchSteamBusOutput(
                Hatch_Output_Bus_SteamMK3.ID,
                "hatch.output_bus.tier.steammk3",
                "Steam-Attuned Output Bus",
                2).getStackForm(1L));
    }
}
