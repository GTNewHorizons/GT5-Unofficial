package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CALIBRATION_MAX;

import java.util.function.Consumer;

import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;

public enum CircuitCalibration {

    // spotless:off
    NONE(CalibrationThreshold.EMPTY, CalibrationThreshold.EMPTY, CalibrationThreshold.EMPTY),

    PRIMITIVE(
        new CalibrationThreshold("Crude", 0.2, nac -> {}),
        new CalibrationThreshold("Robust", 0.4, nac -> {}),
        new CalibrationThreshold("Archaic", 0.6, nac -> {})
    ),
    CRYSTAL(
        new CalibrationThreshold("Shiny", 0.2, nac -> {nac.globalEUMultiplier = 0.8f;}),
        new CalibrationThreshold("Glimmering", 0.4, nac -> {nac.globalDurationMultiplier = 0.8f;}),
        new CalibrationThreshold("Prismatic", 0.6, nac -> {nac.crystalT3Active = true;})
    ),
    WETWARE(
        new CalibrationThreshold("Wet", 0.2, nac -> {nac.globalEUMultiplier = 0.8f;}),
        new CalibrationThreshold("Fleshy", 0.4, nac -> {nac.globalDurationMultiplier = 0.8f;}),
        new CalibrationThreshold("Wriggling", 0.6, nac -> {nac.wetwareT3Active = true;})
    ),
    BIO(
        new CalibrationThreshold("Aware", 0.2, nac -> {nac.globalEUMultiplier = 0.8f;}),
        new CalibrationThreshold("Sentient", 0.4, nac -> {nac.globalDurationMultiplier = 0.8f;}),
        new CalibrationThreshold("ALIVE!!!!!!!!!", 0.6, nac -> {nac.wetwareT3Active = true; nac.bioT3Active = true;})
    ),
    OPTICAL(
        new CalibrationThreshold("Tuned", 0.2, nac -> {nac.globalEUMultiplier = 0.8f;}),
        new CalibrationThreshold("Precise", 0.4, nac -> {nac.globalDurationMultiplier = 0.8f;}),
        new CalibrationThreshold("Perfected", 0.6, nac -> {nac.opticalT3Active = true;})
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
        new CalibrationThreshold("Foreign", 0.01, nac -> {nac.globalDurationMultiplier -= 0.2f;}),
        new CalibrationThreshold("Alien", 0.05, nac -> { nac.globalDurationMultiplier -= 0.2f;}),
        new CalibrationThreshold("Xeno", 0.2, nac -> { nac.globalDurationMultiplier -= 0.1f;})
    ),; // Pico, Quantum
    // spotless:on

    public final CalibrationThreshold tier1;
    public final CalibrationThreshold tier2;
    public final CalibrationThreshold tier3;

    CircuitCalibration(CalibrationThreshold tier1, CalibrationThreshold tier2, CalibrationThreshold tier3) {
        this.tier1 = tier1;
        this.tier2 = tier2;
        this.tier3 = tier3;
        // makes consumers apply previous consumers
        this.tier2.function = this.tier2.function.andThen(this.tier1.function);
        this.tier3.function = this.tier3.function.andThen(this.tier2.function);

        this.tier1.calibrationType = this;
        this.tier2.calibrationType = this;
        this.tier3.calibrationType = this;
    }

    public static class CalibrationThreshold {

        public final String title;
        public final double percentage;
        public Consumer<MTENanochipAssemblyComplex> function;
        public CircuitCalibration calibrationType;
        public static final CalibrationThreshold EMPTY = new CalibrationThreshold("", 0, nac -> {});

        public CalibrationThreshold(String title, double percentage, Consumer<MTENanochipAssemblyComplex> function) {
            this.title = title;
            this.percentage = percentage;
            this.function = function;
        }
    }

    public static CalibrationThreshold getCurrentCalibration(MTENanochipAssemblyComplex nac) {
        for (CircuitCalibration type : CircuitCalibration.values()) {
            if (type == CircuitCalibration.NONE) continue;
            double percentage = (double) nac.getTotalCircuit(type) / CALIBRATION_MAX;
            if (percentage >= type.tier3.percentage) return type.tier3;
        }
        for (CircuitCalibration type : CircuitCalibration.values()) {
            if (type == CircuitCalibration.NONE) continue;
            double percentage = (double) nac.getTotalCircuit(type) / CALIBRATION_MAX;
            if (percentage >= type.tier2.percentage) return type.tier2;
        }
        for (CircuitCalibration type : CircuitCalibration.values()) {
            if (type == CircuitCalibration.NONE) continue;
            double percentage = (double) nac.getTotalCircuit(type) / CALIBRATION_MAX;
            if (percentage >= type.tier1.percentage) return type.tier1;
        }
        return null;
    }
}
