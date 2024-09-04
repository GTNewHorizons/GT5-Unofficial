package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamCentrifugeMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamCompressorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamForgeHammer;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMaceratorMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamMixerMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Controller_SteamWasherMulti;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Input_Bus_Steam;
import static gregtech.api.enums.MetaTileEntityIDs.Hatch_Output_Bus_Steam;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSteamBusOutput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MteHatchSteamBusInput;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCentrifuge;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamCompressor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamForgeHammer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamMixer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.MTESteamWasher;

public class GregtechSteamMultis {

    public static void run() {

        Logger.INFO("Gregtech5u Content | Registering Steam Multiblocks.");

        GregtechItemList.Controller_SteamMaceratorMulti.set(
            new MTESteamMacerator(
                Controller_SteamMaceratorMulti.ID,
                "gtpp.multimachine.steam.macerator",
                "Steam Grinder").getStackForm(1L));
        GregtechItemList.Controller_SteamCompressorMulti.set(
            new MTESteamCompressor(
                Controller_SteamCompressorMulti.ID,
                "gtpp.multimachine.steam.compressor",
                "Steam Squasher").getStackForm(1L));
        GregtechItemList.Controller_SteamCentrifugeMulti.set(
            new MTESteamCentrifuge(
                Controller_SteamCentrifugeMulti.ID,
                "gtpp.multimachine.steam.centrifuge",
                "Steam Centrifuge").getStackForm(1));
        GregtechItemList.Controller_SteamWasherMulti.set(
            new MTESteamWasher(Controller_SteamWasherMulti.ID, "gtpp.multimachine.steam.washer", "Steam Washer")
                .getStackForm(1));
        GregtechItemList.Controller_SteamForgeHammerMulti.set(
            new MTESteamForgeHammer(
                Controller_SteamForgeHammer.ID,
                "gtpp.multimachine.steam.forge.hammer",
                "Steam Forge Hammer").getStackForm(1));
        GregtechItemList.Controller_SteamMixerMulti.set(
            new MTESteamMixer(Controller_SteamMixerMulti.ID, "gtpp.multimachine.steam.mixer", "Steam Mixer")
                .getStackForm(1));

        GregtechItemList.Hatch_Input_Bus_Steam.set(
            new MteHatchSteamBusInput(Hatch_Input_Bus_Steam.ID, "hatch.input_bus.tier.steam", "Input Bus (Steam)", 0)
                .getStackForm(1L));
        GregtechItemList.Hatch_Output_Bus_Steam.set(
            new MTEHatchSteamBusOutput(
                Hatch_Output_Bus_Steam.ID,
                "hatch.output_bus.tier.steam",
                "Output Bus (Steam)",
                0).getStackForm(1L));
    }
}
