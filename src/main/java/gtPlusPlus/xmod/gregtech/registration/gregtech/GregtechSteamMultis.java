package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.ControllerSteamForgeHammer;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.ControllerSteamMixerMulti;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Controller_SteamCentrifugeMulti;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Controller_SteamCompressorMulti;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Controller_SteamMaceratorMulti;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Controller_SteamWasherMulti;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Hatch_Input_Bus_Steam;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.MetaTileEntityIDs.Hatch_Output_Bus_Steam;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusOutput;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamCentrifuge;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamCompressor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamForgeHammer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamMixer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamWasher;

public class GregtechSteamMultis {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Steam Multiblocks.");

        GregtechItemList.Controller_SteamMaceratorMulti.set(
            new GregtechMetaTileEntity_SteamMacerator(
                Controller_SteamMaceratorMulti.ID,
                "gtpp.multimachine.steam.macerator",
                "Steam Grinder").getStackForm(1L));
        GregtechItemList.Controller_SteamCompressorMulti.set(
            new GregtechMetaTileEntity_SteamCompressor(
                Controller_SteamCompressorMulti.ID,
                "gtpp.multimachine.steam.compressor",
                "Steam Squasher").getStackForm(1L));
        GregtechItemList.Controller_SteamCentrifugeMulti.set(
            new GregtechMetaTileEntity_SteamCentrifuge(
                Controller_SteamCentrifugeMulti.ID,
                "gtpp.multimachine.steam.centrifuge",
                "Steam Centrifuge").getStackForm(1));
        GregtechItemList.Controller_SteamWasherMulti.set(
            new GregtechMetaTileEntity_SteamWasher(
                Controller_SteamWasherMulti.ID,
                "gtpp.multimachine.steam.washer",
                "Steam Washer").getStackForm(1));
        GregtechItemList.Controller_SteamForgeHammerMulti.set(
            new GregtechMetaTileEntity_SteamForgeHammer(
                ControllerSteamForgeHammer.ID,
                "gtpp.multimachine.steam.forge.hammer",
                "Steam Forge Hammer").getStackForm(1));
        GregtechItemList.Controller_SteamMixerMulti.set(
            new GregtechMetaTileEntity_SteamMixer(
                ControllerSteamMixerMulti.ID,
                "gtpp.multimachine.steam.mixer",
                "Steam Mixer").getStackForm(1));

        GregtechItemList.Hatch_Input_Bus_Steam.set(
            new GT_MetaTileEntity_Hatch_Steam_BusInput(
                Hatch_Input_Bus_Steam.ID,
                "hatch.input_bus.tier.steam",
                "Input Bus (Steam)",
                0).getStackForm(1L));
        GregtechItemList.Hatch_Output_Bus_Steam.set(
            new GT_MetaTileEntity_Hatch_Steam_BusOutput(
                Hatch_Output_Bus_Steam.ID,
                "hatch.output_bus.tier.steam",
                "Output Bus (Steam)",
                0).getStackForm(1L));
    }
}
