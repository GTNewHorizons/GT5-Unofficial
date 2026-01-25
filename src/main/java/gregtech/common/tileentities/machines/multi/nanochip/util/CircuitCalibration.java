package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.function.Consumer;

import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;

public enum CircuitCalibration {

    // spotless:off
    //todo: implement the Consumers
    PRIMITIVE(
        new CalibrationThreshold("Crude", 0.2, nac -> {}),
        new CalibrationThreshold("Robust", 0.4, nac -> {}),
        new CalibrationThreshold("Archaic", 0.6, nac -> {})
    ),
    CRYSTALS(
        new CalibrationThreshold("Shiny", 0.2, nac -> {}),
        new CalibrationThreshold("Glimmering", 0.4, nac -> {}),
        new CalibrationThreshold("Prismatic", 0.6, nac -> {})
    ),
    WETWARE(
        new CalibrationThreshold("Wet", 0.2, nac -> {}),
        new CalibrationThreshold("Fleshy", 0.4, nac -> {}),
        new CalibrationThreshold("Wriggling", 0.6, nac -> {})
    ),
    BIO(
        new CalibrationThreshold("Aware", 0.2, nac -> {}),
        new CalibrationThreshold("Sentient", 0.4, nac -> {}),
        new CalibrationThreshold("ALIVE!!!!!!!!!", 0.6, nac -> {})
    ),
    OPTICAL(
        new CalibrationThreshold("Tuned", 0.2, nac -> {}),
        new CalibrationThreshold("Precise", 0.4, nac -> {}),
        new CalibrationThreshold("Perfected", 0.6, nac -> {})
    ),
    EXOTIC(
        new CalibrationThreshold("TODO", 0.2, nac -> {}),
        new CalibrationThreshold("TODO", 0.4, nac -> {}),
        new CalibrationThreshold("TODO", 0.6, nac -> {})
    ),
    COSMIC(
        new CalibrationThreshold("TODO", 0.2, nac -> {}),
        new CalibrationThreshold("TODO", 0.4, nac -> {}),
        new CalibrationThreshold("TODO", 0.6, nac -> {})
    ),
    TEMPORAL(
        new CalibrationThreshold("TODO", 0.2, nac -> {}),
        new CalibrationThreshold("TODO", 0.4, nac -> {}),
        new CalibrationThreshold("TODO", 0.6, nac -> {})
    ),
    SPECIAL(
        new CalibrationThreshold("Foreign", 0.01, nac -> {}),
        new CalibrationThreshold("Alien", 0.5, nac -> {}),
        new CalibrationThreshold("Xeno", 0.2, nac -> {})
    ),; // Pico, Quantum
    // spotless:on

    public final CalibrationThreshold tier1;
    public final CalibrationThreshold tier2;
    public final CalibrationThreshold tier3;

    CircuitCalibration(CalibrationThreshold tier1, CalibrationThreshold tier2, CalibrationThreshold tier3) {
        this.tier1 = tier1;
        this.tier2 = tier2;
        this.tier3 = tier3;
    }

    public static class CalibrationThreshold {

        String title;
        double percentage;
        Consumer<MTENanochipAssemblyComplex> function;

        public CalibrationThreshold(String title, double percentage, Consumer<MTENanochipAssemblyComplex> function) {
            this.title = title;
            this.percentage = percentage;
            this.function = function;
        }
    }
}
