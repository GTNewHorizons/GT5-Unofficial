package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_MV;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_UV;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Cleaner_ZPM;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Creator;
import static gregtech.api.enums.MetaTileEntityIDs.Pollution_Detector;

import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEAtmosphericReconditioner;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEPollutionCreator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.basic.MTEPollutionDetector;

public class GregtechPollutionDevices {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Anti-Pollution Devices.");
        run1();
    }

    private static void run1() {
        GregtechItemList.Pollution_Detector.set(
            new MTEPollutionDetector(
                Pollution_Detector.ID,
                "pollutiondetector.01.tier.single",
                "Pollution Detection Device",
                0,
                "Tells you if you're living in Gwalior yet.",
                0).getStackForm(1L));
        GregtechItemList.Pollution_Creator.set(
            new MTEPollutionCreator(
                MTETieredMachineBlock.Args.builder()
                    .registerToApi(true)
                    .id(Pollution_Creator.ID)
                    .translateKey("pollutioncreator.01.tier.single")
                    .nameEnglish("Smog Device")
                    .tier(4)
                    .descriptionArray(new String[] { "Polluting the skies." })
                    .build()).getStackForm(1L));

        GregtechItemList.Pollution_Cleaner_LV.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_LV.ID,
                "pollutioncleaner.02.tier.single",
                "Upgraded Pollution Scrubber",
                1).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_MV.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_MV.ID,
                "pollutioncleaner.03.tier.single",
                "Advanced Pollution Scrubber",
                2).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_HV.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_HV.ID,
                "pollutioncleaner.04.tier.single",
                "Precision Pollution Scrubber",
                3).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_EV.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_EV.ID,
                "pollutioncleaner.05.tier.single",
                "Air Recycler",
                4).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_IV.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_IV.ID,
                "pollutioncleaner.06.tier.single",
                "Upgraded Air Recycler",
                5).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_LuV.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_LuV.ID,
                "pollutioncleaner.07.tier.single",
                "Advanced Air Recycler",
                6).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_ZPM.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_ZPM.ID,
                "pollutioncleaner.08.tier.single",
                "Precision Air Recycler",
                7).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_UV.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_UV.ID,
                "pollutioncleaner.09.tier.single",
                "Atmospheric Cleaner",
                8).getStackForm(1L));
        GregtechItemList.Pollution_Cleaner_MAX.set(
            new MTEAtmosphericReconditioner(
                Pollution_Cleaner_MAX.ID,
                "pollutioncleaner.10.tier.single",
                "Biosphere Cleanser",
                9).getStackForm(1L));

    }
}
