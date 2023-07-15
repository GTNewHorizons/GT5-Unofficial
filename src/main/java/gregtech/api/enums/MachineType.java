package gregtech.api.enums;

import com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler;

public enum MachineType {

    ALLOY_SMELTER("Alloy Smelter"),
    ARC_FURNACE("Arc furnace"),
    ASSEMBLER("Assembler"),
    AUTOCLAVE("Autoclave"),
    BENDING_MACHINE("Bending machine"),
    CANNER("Canner"),
    CENTRIFUGE("Centrifuge"),
    CHEMICAL_BATH("Chemical bath"),
    CHEMICAL_REACTOR("Chemical Reactor"),
    CIRCUIT_ASSEMBLER("Circuit Assembler"),
    COMPRESSOR("Compressor"),
    CUTTING_MACHINE("Cutting machine"),
    DISTILLERY("Distillery"),
    ELECTROLYZER("Electrolyzer"),
    ELECTROMAGNETIC_SEPARATOR("Electromagnetic separator"),
    EXTRACTOR("Extractor"),
    EXTRUDER("Extruder"),
    FERMENTER("Fermenter"),
    FLUID_CANNER("Fluid canner"),
    FLUID_EXTRACTOR("Fluid extractor"),
    FLUID_SOLIDIFIER("Fluid solidifier"),
    FURNACE("Furnace"),
    FORGE_HAMMER("Forge Hammer"),
    FORMING_PRESS("Forming press"),
    FLUID_HEATER("Fluid heater"),
    LASER_ENGRAVER("Laser engraver"),
    LATHE("Lathe"),
    MACERATOR("Macerator"),
    MACERATOR_PULVERIZER("Macerator/Pulverizer"),
    MATTER_AMPLIFIER("Matter amplifier"),
    MIXER("Mixer"),
    ORE_WASHER("Ore washer"),
    PLASMA_ARC_FURNACE("Plasma arc furnace"),
    POLARIZER("Polarizer"),
    PRINTER("Printer"),
    RECYCLER("Recycler"),
    SIFTER("Sifter"),
    SLICER("Slicer"),
    THERMAL_CENTRIFUGE("Thermal Centrifuge"),
    UNPACKAGER("Unpackager"),
    WIREMILL("Wiremill");

    private final String name;

    private MachineType(String machineType) {
        this.name = "Machine type: " + AnimatedTooltipHandler.YELLOW + machineType;
    }

    public String type() {
        return this.name;
    }
}
