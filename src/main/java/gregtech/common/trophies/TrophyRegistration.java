package gregtech.common.trophies;

import glowredman.amazingtrophies.api.AmazingTrophiesAPI;
import glowredman.amazingtrophies.api.TrophyProperties;
import glowredman.amazingtrophies.model.complex.ComplexTrophyModelHandler;
import gregtech.common.trophies.definitions.Model_Cleanroom;
import gregtech.common.trophies.definitions.Model_DTPF;
import gregtech.common.trophies.definitions.Model_EBF;
import gregtech.common.trophies.definitions.Model_FusionMK1;
import gregtech.common.trophies.definitions.Model_NanoForge;

public class TrophyRegistration {

    public static void load() {

        // TODO: remove before merging

        // DTPF
        for (int index = 11; index < 14; index++) {
            AmazingTrophiesAPI.registerTrophy(
                "DTPF_" + index,
                new TrophyProperties(new ComplexTrophyModelHandler(new Model_DTPF(index))));
        }

        // EBF
        for (int index = 0; index < 14; index++) {
            AmazingTrophiesAPI.registerTrophy(
                "EBF_" + index,
                new TrophyProperties(new ComplexTrophyModelHandler(new Model_EBF(index))));
        }

        AmazingTrophiesAPI
            .registerTrophy("NanoForge", new TrophyProperties(new ComplexTrophyModelHandler(new Model_NanoForge())));

        AmazingTrophiesAPI
            .registerTrophy("FusionMK1", new TrophyProperties(new ComplexTrophyModelHandler(new Model_FusionMK1())));
        AmazingTrophiesAPI
            .registerTrophy("Cleanroom", new TrophyProperties(new ComplexTrophyModelHandler(new Model_Cleanroom())));
    }
}
