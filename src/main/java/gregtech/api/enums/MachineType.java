package gregtech.api.enums;

import net.minecraft.util.StatCollector;
import com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler;

public enum MachineType {

    ALLOY_SMELTER(FunnyTexts.ALLOY_SMELTER, "gt.recipe.alloy_smelter"),
    ARC_FURNACE(FunnyTexts.ARC_FURNACE, "gt.recipe.arc_furnace"),
    ASSEMBLER(FunnyTexts.ASSEMBLER, "gt.recipe.assembler"),
    AUTOCLAVE(FunnyTexts.AUTOCLAVE, "gt.recipe.autoclave"),
    BENDING_MACHINE(FunnyTexts.BENDING_MACHINE, "gt.recipe.bending_machine"),
    CANNER(FunnyTexts.CANNER, "gt.recipe.canner"),
    CENTRIFUGE(FunnyTexts.CENTRIFUGE, "gt.recipe.centrifuge"),
    CHEMICAL_BATH(FunnyTexts.CHEMICAL_BATH, "gt.recipe.chemical_bath"),
    CHEMICAL_REACTOR(FunnyTexts.CHEMICAL_REACTOR, "gt.recipe.chemical_reactor"),
    CIRCUIT_ASSEMBLER(FunnyTexts.CIRCUIT_ASSEMBLER, "gt.recipe.circuit_assembler"),
    COMPRESSOR(FunnyTexts.COMPRESSOR, "gt.recipe.compressor"),
    CUTTING_MACHINE(FunnyTexts.CUTTING_MACHINE, "gt.recipe.cutting_machine"),
    DISTILLERY(FunnyTexts.DISTILLERY, "gt.recipe.distillery"),
    ELECTRIC_FURNACE(FunnyTexts.ELECTRIC_FURNACE, "gt.recipe.electric_furnace"),
    ELECTROLYZER(FunnyTexts.ELECTROLYZER, "gt.recipe.electrolyzer"),
    ELECTROMAGNETIC_SEPARATOR(FunnyTexts.ELECTROMAGNETIC_SEPARATOR, "gt.recipe.electromagnetic_separator"),
    EXTRACTOR(FunnyTexts.EXTRACTOR, "gt.recipe.extractor"),
    EXTRUDER(FunnyTexts.EXTRUDER, "gt.recipe.extruder"),
    FERMENTER(FunnyTexts.FERMENTER, "gt.recipe.fermenter"),
    FLUID_CANNER(FunnyTexts.FLUID_CANNER, "gt.recipe.fluid_canner"),
    FLUID_EXTRACTOR(FunnyTexts.FLUID_EXTRACTOR, "gt.recipe.fluid_extractor"),
    FLUID_SOLIDIFIER(FunnyTexts.FLUID_SOLIDIFIER, "gt.recipe.fluid_solidifier"),
    FORGE_HAMMER(FunnyTexts.FORGE_HAMMER, "gt.recipe.forge_hammer"),
    FORMING_PRESS(FunnyTexts.FORMING_PRESS, "gt.recipe.forming_press"),
    FLUID_HEATER(FunnyTexts.FLUID_HEATER, "gt.recipe.fluid_heater"),
    LASER_ENGRAVER(FunnyTexts.LASER_ENGRAVER, "gt.recipe.laser_engraver"),
    LATHE(FunnyTexts.LATHE, "gt.recipe.lathe"),
    MACERATOR(FunnyTexts.MACERATOR, "gt.recipe.macerator"),
    MACERATOR_PULVERIZER(FunnyTexts.MACERATOR_PULVERIZER, "gt.recipe.macerator_pulverizer"),
    MATTER_AMPLIFIER(FunnyTexts.MATTER_AMPLIFIER, "gt.recipe.matter_amplifier"),
    MICROWAVE(FunnyTexts.MICROWAVE, "gt.recipe.microwave"),
    MIXER(FunnyTexts.MIXER, "gt.recipe.mixer"),
    ORE_WASHER(FunnyTexts.ORE_WASHER, "gt.recipe.orewasher"),
    OVEN(FunnyTexts.OVEN, "gt.recipe.oven"),
    PLASMA_ARC_FURNACE(FunnyTexts.PLASMA_ARC_FURNACE, "gt.recipe.plasma_arc_furnace"),
    POLARIZER(FunnyTexts.POLARIZER, "gt.recipe.polarizer"),
    PRINTER(FunnyTexts.PRINTER, "gt.recipe.printer"),
    RECYCLER(FunnyTexts.RECYCLER, "gt.recipe.recycler"),
    SIFTER(FunnyTexts.SIFTER, "gt.recipe.sifter"),
    SLICER(FunnyTexts.SLICER, "gt.recipe.slicer"),
    THERMAL_CENTRIFUGE(FunnyTexts.THERMAL_CENTRIFUGE, "gt.recipe.thermal_centrifuge"),
    UNPACKAGER(FunnyTexts.UNPACKAGER, "gt.recipe.unpackager"),
    WIREMILL(FunnyTexts.WIREMILL, "gt.recipe.wiremill");

    private static class FunnyTexts {

        public static final String ALLOY_SMELTER = "gt.recipe.alloy_smelter.description";
        public static final String ARC_FURNACE = "gt.recipe.arc_furnace.description";
        public static final String ASSEMBLER = "gt.recipe.assembler.description";
        public static final String AUTOCLAVE = "gt.recipe.autoclave.description";
        public static final String BENDING_MACHINE = "gt.recipe.bending_machine.description";
        public static final String CANNER = "gt.recipe.canner.description";
        public static final String CENTRIFUGE = "gt.recipe.centrifuge.description";
        public static final String CHEMICAL_BATH = "gt.recipe.chemical_bath.description";
        public static final String CHEMICAL_REACTOR = "gt.recipe.chemical_reactor.description";
        public static final String CIRCUIT_ASSEMBLER = "gt.recipe.circuit_assembler.description";
        public static final String COMPRESSOR = "gt.recipe.compressor.description";
        public static final String CUTTING_MACHINE = "gt.recipe.cutting_machine.description";
        public static final String DISTILLERY = "gt.recipe.distillery.description";
        public static final String ELECTRIC_FURNACE = "gt.recipe.electric_furnace.description";
        public static final String ELECTROLYZER = "gt.recipe.electrolyzer.description";
        public static final String ELECTROMAGNETIC_SEPARATOR = "gt.recipe.electromagnetic_separator.description";
        public static final String EXTRACTOR = "gt.recipe.extractor.description";
        public static final String EXTRUDER = "gt.recipe.extruder.description";
        public static final String FERMENTER = "gt.recipe.fermenter.description";
        public static final String FLUID_CANNER = "gt.recipe.fluid_canner.description";
        public static final String FLUID_EXTRACTOR = "gt.recipe.fluid_extractor.description";
        public static final String FLUID_HEATER = "gt.recipe.fluid_heater.description";
        public static final String FLUID_SOLIDIFIER = "gt.recipe.fluid_solidifier.description";
        public static final String FORGE_HAMMER = "gt.recipe.forge_hammer.description";
        public static final String FORMING_PRESS = "gt.recipe.forming_press.description";
        public static final String LASER_ENGRAVER = "gt.recipe.laser_engraver.description";
        public static final String LATHE = "gt.recipe.lathe.description";
        public static final String MACERATOR = "gt.recipe.macerator.description";
        public static final String MACERATOR_PULVERIZER = "gt.recipe.macerator_pulverizer.description";
        public static final String MATTER_AMPLIFIER = "gt.recipe.matter_amplifier.description";
        public static final String MICROWAVE = "gt.recipe.microwave.description";
        public static final String MIXER = "gt.recipe.mixer.description";
        public static final String ORE_WASHER = "gt.recipe.orewasher.description";
        public static final String OVEN = "gt.recipe.oven.description";
        public static final String PLASMA_ARC_FURNACE = "gt.recipe.plasma_arc_furnace.description";
        public static final String POLARIZER = "gt.recipe.polarizer.description";
        public static final String PRINTER = "gt.recipe.printer.description";
        public static final String RECYCLER = "gt.recipe.recycler.description";
        public static final String SIFTER = "gt.recipe.sifter.description";
        public static final String SLICER = "gt.recipe.slicer.description";
        public static final String THERMAL_CENTRIFUGE = "gt.recipe.thermal_centrifuge.description";
        public static final String UNPACKAGER = "gt.recipe.unpackager.description";
        public static final String WIREMILL = "gt.recipe.wiremill.description";
    }


    private static final String TT_machineType = StatCollector.translateToLocal("GT5U.MBTT.MachineType");

    private final String name;
    private final String description;

    MachineType(String machineDescription, String machineType) {
        this.description = StatCollector.translateToLocal(machineDescription);
        this.name = StatCollector.translateToLocal(machineType);
    }

    public String type() {
        return this.name;
    }

    public String description() {
        return this.name;
    }

    public String[] tooltipDescription() {
        return new String[] { this.description,
            TT_machineType + ": " + AnimatedTooltipHandler.YELLOW + this.name + AnimatedTooltipHandler.RESET};
    }
}
