package gregtech.api.enums;

import net.minecraft.util.StatCollector;
import com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler;

public enum MachineType {

    ALLOY_SMELTER(FunnyTexts.ALLOY_SMELTER, "GT5U.machines.types.alloy_smelter"),
    ARC_FURNACE(FunnyTexts.ARC_FURNACE, "GT5U.machines.types.arc_furnace"),
    ASSEMBLER(FunnyTexts.ASSEMBLER, "GT5U.machines.types.assembler"),
    AUTOCLAVE(FunnyTexts.AUTOCLAVE, "GT5U.machines.types.autoclave"),
    BENDING_MACHINE(FunnyTexts.BENDING_MACHINE, "GT5U.machines.types.bending_machine"),
    CANNER(FunnyTexts.CANNER, "GT5U.machines.types.canner"),
    CENTRIFUGE(FunnyTexts.CENTRIFUGE, "GT5U.machines.types.centrifuge"),
    CHEMICAL_BATH(FunnyTexts.CHEMICAL_BATH, "GT5U.machines.types.chemical_bath"),
    CHEMICAL_REACTOR(FunnyTexts.CHEMICAL_REACTOR, "GT5U.machines.types.chemical_reactor"),
    CIRCUIT_ASSEMBLER(FunnyTexts.CIRCUIT_ASSEMBLER, "GT5U.machines.types.circuit_assembler"),
    COMPRESSOR(FunnyTexts.COMPRESSOR, "GT5U.machines.types.compressor"),
    CUTTING_MACHINE(FunnyTexts.CUTTING_MACHINE, "GT5U.machines.types.cutting_machine"),
    DISTILLERY(FunnyTexts.DISTILLERY, "GT5U.machines.types.distillery"),
    ELECTRIC_FURNACE(FunnyTexts.ELECTRIC_FURNACE, "GT5U.machines.types.electric_furnace"),
    ELECTROLYZER(FunnyTexts.ELECTROLYZER, "GT5U.machines.types.electrolyzer"),
    ELECTROMAGNETIC_SEPARATOR(FunnyTexts.ELECTROMAGNETIC_SEPARATOR, "GT5U.machines.types.electromagnetic_separator"),
    EXTRACTOR(FunnyTexts.EXTRACTOR, "GT5U.machines.types.extractor"),
    EXTRUDER(FunnyTexts.EXTRUDER, "GT5U.machines.types.extruder"),
    FERMENTER(FunnyTexts.FERMENTER, "GT5U.machines.types.fermenter"),
    FLUID_CANNER(FunnyTexts.FLUID_CANNER, "GT5U.machines.types.fluid_canner"),
    FLUID_EXTRACTOR(FunnyTexts.FLUID_EXTRACTOR, "GT5U.machines.types.fluid_extractor"),
    FLUID_SOLIDIFIER(FunnyTexts.FLUID_SOLIDIFIER, "GT5U.machines.types.fluid_solidifier"),
    FORGE_HAMMER(FunnyTexts.FORGE_HAMMER, "GT5U.machines.types.forge_hammer"),
    FORMING_PRESS(FunnyTexts.FORMING_PRESS, "GT5U.machines.types.forming_press"),
    FLUID_HEATER(FunnyTexts.FLUID_HEATER, "GT5U.machines.types.fluid_heater"),
    LASER_ENGRAVER(FunnyTexts.LASER_ENGRAVER, "GT5U.machines.types.laser_engraver"),
    LATHE(FunnyTexts.LATHE, "GT5U.machines.types.lathe"),
    MACERATOR(FunnyTexts.MACERATOR, "GT5U.machines.types.macerator"),
    MACERATOR_PULVERIZER(FunnyTexts.MACERATOR_PULVERIZER, "GT5U.machines.types.macerator_pulverizer"),
    MATTER_AMPLIFIER(FunnyTexts.MATTER_AMPLIFIER, "GT5U.machines.types.matter_amplifier"),
    MICROWAVE(FunnyTexts.MICROWAVE, "GT5U.machines.types.microwave"),
    MIXER(FunnyTexts.MIXER, "GT5U.machines.types.mixer"),
    ORE_WASHER(FunnyTexts.ORE_WASHER, "GT5U.machines.types.ore_washer"),
    OVEN(FunnyTexts.OVEN, "GT5U.machines.types.oven"),
    PLASMA_ARC_FURNACE(FunnyTexts.PLASMA_ARC_FURNACE, "GT5U.machines.types.plasma_arc_furnace"),
    POLARIZER(FunnyTexts.POLARIZER, "GT5U.machines.types.polarizer"),
    PRINTER(FunnyTexts.PRINTER, "GT5U.machines.types.printer"),
    RECYCLER(FunnyTexts.RECYCLER, "GT5U.machines.types.recycler"),
    SIFTER(FunnyTexts.SIFTER, "GT5U.machines.types.sifter"),
    SLICER(FunnyTexts.SLICER, "GT5U.machines.types.slicer"),
    THERMAL_CENTRIFUGE(FunnyTexts.THERMAL_CENTRIFUGE, "GT5U.machines.types.thermal_centrifuge"),
    UNPACKAGER(FunnyTexts.UNPACKAGER, "GT5U.machines.types.unpackager"),
    WIREMILL(FunnyTexts.WIREMILL, "GT5U.machines.types.wiremill");

    public static class FunnyTexts {

        public static final String ALLOY_SMELTER = "GT5U.machines.types.alloy_smelter.description";
        public static final String ARC_FURNACE = "GT5U.machines.types.arc_furnace.description";
        public static final String ASSEMBLER = "GT5U.machines.types.assembler.description";
        public static final String AUTOCLAVE = "GT5U.machines.types.autoclave.description";
        public static final String BENDING_MACHINE = "GT5U.machines.types.bending_machine.description";
        public static final String CANNER = "GT5U.machines.types.canner.description";
        public static final String CENTRIFUGE = "GT5U.machines.types.centrifuge.description";
        public static final String CHEMICAL_BATH = "GT5U.machines.types.chemical_bath.description";
        public static final String CHEMICAL_REACTOR = "GT5U.machines.types.chemical_reactor.description";
        public static final String CIRCUIT_ASSEMBLER = "GT5U.machines.types.circuit_assembler.description";
        public static final String COMPRESSOR = "GT5U.machines.types.compressor.description";
        public static final String CUTTING_MACHINE = "GT5U.machines.types.cutting_machine.description";
        public static final String DISTILLERY = "GT5U.machines.types.distillery.description";
        public static final String ELECTRIC_FURNACE = "GT5U.machines.types.electric_furnace.description";
        public static final String ELECTROLYZER = "GT5U.machines.types.electrolyzer.description";
        public static final String ELECTROMAGNETIC_SEPARATOR = "GT5U.machines.types.electromagnetic_separator.description";
        public static final String EXTRACTOR = "GT5U.machines.types.extractor.description";
        public static final String EXTRUDER = "GT5U.machines.types.extruder.description";
        public static final String FERMENTER = "GT5U.machines.types.fermenter.description";
        public static final String FLUID_CANNER = "GT5U.machines.types.fluid_canner.description";
        public static final String FLUID_EXTRACTOR = "GT5U.machines.types.fluid_extractor.description";
        public static final String FLUID_HEATER = "GT5U.machines.types.fluid_heater.description";
        public static final String FLUID_SOLIDIFIER = "GT5U.machines.types.fluid_solidifier.description";
        public static final String FORGE_HAMMER = "GT5U.machines.types.forge_hammer.description";
        public static final String FORMING_PRESS = "GT5U.machines.types.forming_press.description";
        public static final String LASER_ENGRAVER = "GT5U.machines.types.laser_engraver.description";
        public static final String LATHE = "GT5U.machines.types.lathe.description";
        public static final String MACERATOR = "GT5U.machines.types.macerator.description";
        public static final String MACERATOR_PULVERIZER = "GT5U.machines.types.macerator_pulverizer.description";
        public static final String MATTER_AMPLIFIER = "GT5U.machines.types.matter_amplifier.description";
        public static final String MICROWAVE = "GT5U.machines.types.microwave.description";
        public static final String MIXER = "GT5U.machines.types.mixer.description";
        public static final String ORE_WASHER = "GT5U.machines.types.ore_washer.description";
        public static final String OVEN = "GT5U.machines.types.oven.description";
        public static final String PLASMA_ARC_FURNACE = "GT5U.machines.types.plasma_arc_furnace.description";
        public static final String POLARIZER = "GT5U.machines.types.polarizer.description";
        public static final String PRINTER = "GT5U.machines.types.printer.description";
        public static final String RECYCLER = "GT5U.machines.types.recycler.description";
        public static final String SIFTER = "GT5U.machines.types.sifter.description";
        public static final String SLICER = "GT5U.machines.types.slicer.description";
        public static final String THERMAL_CENTRIFUGE = "GT5U.machines.types.thermal_centrifuge.description";
        public static final String UNPACKAGER = "GT5U.machines.types.unpackager.description";
        public static final String WIREMILL = "GT5U.machines.types.wiremill.description";
    }


    private static final String TT_machineType = StatCollector.translateToLocal("GT5U.MBTT.MachineType");

    private final String name;
    private final String description;

    private MachineType(String machineDescription, String machineType) {
        this.description = StatCollector.translateToLocal(machineDescription);
        this.name = StatCollector.translateToLocal(machineType);
    }

    public String type() {
        return this.name;
    }

    public String[] tooltipDescription() {
        return new String[] { this.description,
            TT_machineType + ": " + AnimatedTooltipHandler.YELLOW + this.name + AnimatedTooltipHandler.RESET};
    }
}
