package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CALIBRATION_MAX;

import java.util.function.Consumer;

import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex;

public enum CircuitCalibration {

    // spotless:off
    NONE(CalibrationThreshold.EMPTY, CalibrationThreshold.EMPTY, CalibrationThreshold.EMPTY),

    PRIMITIVE(
        new CalibrationThreshold("primitive.1", 0.4, nac -> nac.primitiveT1Active = true),
        new CalibrationThreshold("primitive.2", 0.8, nac -> nac.primitiveT2Active = true),
        new CalibrationThreshold("primitive.3", 1,   nac -> nac.primitiveT3Active = true)
    ),
    CRYSTAL(
        new CalibrationThreshold("crystal.1", 0.3, nac -> nac.globalEUMultiplier = 0.8f),
        new CalibrationThreshold("crystal.2", 0.6, nac -> nac.globalDurationMultiplier = 0.8f),
        new CalibrationThreshold("crystal.3", 0.9, nac -> nac.crystalT3Active = true)
    ),
    WETWARE(
        new CalibrationThreshold("wetware.1", 0.3, nac -> nac.globalEUMultiplier = 0.8f),
        new CalibrationThreshold("wetware.2", 0.6, nac -> nac.globalDurationMultiplier = 0.8f),
        new CalibrationThreshold("wetware.3", 0.9, nac -> nac.wetwareT3Active = true)
    ),
    BIO(
        new CalibrationThreshold("bioware.1", 0.25, nac -> nac.globalEUMultiplier = 0.8f),
        new CalibrationThreshold("bioware.2", 0.5,  nac -> nac.globalDurationMultiplier = 0.8f),
        new CalibrationThreshold("bioware.3", 0.75, nac -> nac.bioT3Active = true)
    ),
    OPTICAL(
        new CalibrationThreshold("optical.1", 0.25, nac -> nac.globalEUMultiplier = 0.8f),
        new CalibrationThreshold("optical.2", 0.5,  nac -> nac.globalDurationMultiplier = 0.8f),
        new CalibrationThreshold("optical.3", 0.75, nac -> nac.opticalT3Active = true)
    ),
    EXOTIC(
        new CalibrationThreshold("Intertwined", 0.2, nac -> {nac.globalEUMultiplier = 0.8f;}),
        new CalibrationThreshold("Superposed", 0.4, nac -> {nac.globalDurationMultiplier = 0.8f;}),
        new CalibrationThreshold("Eternal", 0.6, nac -> {nac.exoticT3Active = true;})
    ),
    COSMIC( // Unimplemented
        new CalibrationThreshold("", 0.2),
        new CalibrationThreshold("", 0.4),
        new CalibrationThreshold("", 0.6)
    ),
    TEMPORAL( // Unimplemented
        new CalibrationThreshold("", 0.2),
        new CalibrationThreshold("", 0.4),
        new CalibrationThreshold("", 0.6)
    ),
    SPECIAL( // Pico, Quantum, Planck. Until the above are implemented
        new CalibrationThreshold("special.1", 0.01, nac -> nac.globalDurationMultiplier -= 0.2f),
        new CalibrationThreshold("special.2", 0.05, nac -> nac.globalDurationMultiplier -= 0.2f),
        new CalibrationThreshold("special.3", 0.2,  nac -> nac.globalDurationMultiplier -= 0.1f)
    );
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

        private static final Consumer<MTENanochipAssemblyComplex> NOOP = _ -> {};
        public static final CalibrationThreshold EMPTY = new CalibrationThreshold("", 0, NOOP);

        public CalibrationThreshold(String title, double percentage) {
            this(title, percentage, NOOP);
        }

        public CalibrationThreshold(String title, double percentage, Consumer<MTENanochipAssemblyComplex> function) {
            this.title = "GT5U.gui.text.nac.title." + title;
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
