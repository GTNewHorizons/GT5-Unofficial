package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CALIBRATION_MAX;

import java.util.function.Consumer;

import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;

public enum CircuitCalibration {

    // spotless:off
    //todo: implement the Consumers
    PRIMITIVE(
        (byte) 1,
        new CalibrationThreshold("Crude", 0.2, nac -> {}),
        new CalibrationThreshold("Robust", 0.4, nac -> {}),
        new CalibrationThreshold("Archaic", 0.6, nac -> {})
    ),
    CRYSTALS(
        (byte) 2,
        new CalibrationThreshold("Shiny", 0.2, nac -> {}),
        new CalibrationThreshold("Glimmering", 0.4, nac -> {}),
        new CalibrationThreshold("Prismatic", 0.6, nac -> {})
    ),
    WETWARE(
        (byte) 3,
        new CalibrationThreshold("Wet", 0.2, nac -> {}),
        new CalibrationThreshold("Fleshy", 0.4, nac -> {}),
        new CalibrationThreshold("Wriggling", 0.6, nac -> {})
    ),
    BIO(
        (byte) 4,
        new CalibrationThreshold("Aware", 0.2, nac -> {}),
        new CalibrationThreshold("Sentient", 0.4, nac -> {}),
        new CalibrationThreshold("ALIVE!!!!!!!!!", 0.6, nac -> {})
    ),
    OPTICAL(
        (byte) 5,
        new CalibrationThreshold("Tuned", 0.2, nac -> {}),
        new CalibrationThreshold("Precise", 0.4, nac -> {}),
        new CalibrationThreshold("Perfected", 0.6, nac -> {})
    ),
    EXOTIC(
        (byte) 6,
        new CalibrationThreshold("TODO", 0.2, nac -> {}),
        new CalibrationThreshold("TODO", 0.4, nac -> {}),
        new CalibrationThreshold("TODO", 0.6, nac -> {})
    ),
    COSMIC(
        (byte) 7,
        new CalibrationThreshold("TODO", 0.2, nac -> {}),
        new CalibrationThreshold("TODO", 0.4, nac -> {}),
        new CalibrationThreshold("TODO", 0.6, nac -> {})
    ),
    TEMPORAL(
        (byte) 8,
        new CalibrationThreshold("TODO", 0.2, nac -> {}),
        new CalibrationThreshold("TODO", 0.4, nac -> {}),
        new CalibrationThreshold("TODO", 0.6, nac -> {})
    ),
    SPECIAL(
        (byte) 64,
        new CalibrationThreshold("Foreign", 0.01, nac -> {}),
        new CalibrationThreshold("Alien", 0.05, nac -> {}),
        new CalibrationThreshold("Xeno", 0.2, nac -> {})
    ),; // Pico, Quantum
    // spotless:on

    public final byte circuitID;
    public final CalibrationThreshold tier1;
    public final CalibrationThreshold tier2;
    public final CalibrationThreshold tier3;

    CircuitCalibration(byte circuitID, CalibrationThreshold tier1, CalibrationThreshold tier2,
        CalibrationThreshold tier3) {
        this.circuitID = circuitID;
        this.tier1 = tier1;
        this.tier2 = tier2;
        this.tier3 = tier3;
    }

    public static class CalibrationThreshold {

        public final String title;
        public final double percentage;
        public final Consumer<MTENanochipAssemblyComplex> function;

        public CalibrationThreshold(String title, double percentage, Consumer<MTENanochipAssemblyComplex> function) {
            this.title = title;
            this.percentage = percentage;
            this.function = function;
        }
    }

    public static CalibrationThreshold getCurrentCalibration(MTENanochipAssemblyComplex nac) {
        for (CircuitCalibration type : CircuitCalibration.values()) {
            double percentage = (double) nac.getTotalCircuit(type.circuitID) / CALIBRATION_MAX;
            if (percentage >= type.tier3.percentage) return type.tier3;
        }
        for (CircuitCalibration type : CircuitCalibration.values()) {
            double percentage = (double) nac.getTotalCircuit(type.circuitID) / CALIBRATION_MAX;
            if (percentage >= type.tier2.percentage) return type.tier2;
        }
        for (CircuitCalibration type : CircuitCalibration.values()) {
            double percentage = (double) nac.getTotalCircuit(type.circuitID) / CALIBRATION_MAX;
            if (percentage >= type.tier1.percentage) return type.tier1;
        }
        return null;
    }
}
