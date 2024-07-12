package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusInput;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Steam_BusOutput;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamCentrifuge;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamCompressor;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamMacerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam.GregtechMetaTileEntity_SteamWasher;

public class GregtechSteamMultis {

    public static void run() {

        // id 31079 and 31081 are occupied by another machine

        Logger.INFO("Gregtech5u Content | Registering Steam Multiblocks.");

        GregtechItemList.Controller_SteamMaceratorMulti.set(
            new GregtechMetaTileEntity_SteamMacerator(31041, "gtpp.multimachine.steam.macerator", "Steam Grinder")
                .getStackForm(1L));
        GregtechItemList.Controller_SteamCompressorMulti.set(
            new GregtechMetaTileEntity_SteamCompressor(31078, "gtpp.multimachine.steam.compressor", "Steam Squasher")
                .getStackForm(1L));
        GregtechItemList.Controller_SteamCentrifugeMulti.set(
            new GregtechMetaTileEntity_SteamCentrifuge(31080, "gtpp.multimachine.steam.centrifuge", "Steam Centrifuge")
                .getStackForm(1));
        GregtechItemList.Controller_SteamWasherMulti.set(
            new GregtechMetaTileEntity_SteamWasher(31082, "gtpp.multimachine.steam.washer", "Steam Washer")
                .getStackForm(1));

        GregtechItemList.Hatch_Input_Bus_Steam.set(
            new GT_MetaTileEntity_Hatch_Steam_BusInput(31046, "hatch.input_bus.tier.steam", "Input Bus (Steam)", 0)
                .getStackForm(1L));
        GregtechItemList.Hatch_Output_Bus_Steam.set(
            new GT_MetaTileEntity_Hatch_Steam_BusOutput(31047, "hatch.output_bus.tier.steam", "Output Bus (Steam)", 0)
                .getStackForm(1L));
    }
}
