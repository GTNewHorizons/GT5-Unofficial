package gregtech.api.enums;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public enum MachineType {

    ALLOY_SMELTER(FunnyTexts.ALLOY_SMELTER, "gt.recipe.alloysmelter"),
    ARC_FURNACE(FunnyTexts.ARC_FURNACE, "gt.recipe.arcfurnace"),
    ASSEMBLER(FunnyTexts.ASSEMBLER, "gt.recipe.assembler"),
    AUTOCLAVE(FunnyTexts.AUTOCLAVE, "gt.recipe.autoclave"),
    BENDING_MACHINE(FunnyTexts.BENDING_MACHINE, "gt.recipe.metalbender"),
    CANNER(FunnyTexts.CANNER, "gt.recipe.canner"),
    CENTRIFUGE(FunnyTexts.CENTRIFUGE, "gt.recipe.centrifuge"),
    CHEMICAL_BATH(FunnyTexts.CHEMICAL_BATH, "gt.recipe.chemicalbath"),
    CHEMICAL_REACTOR(FunnyTexts.CHEMICAL_REACTOR, "gt.recipe.chemicalreactor"),
    CIRCUIT_ASSEMBLER(FunnyTexts.CIRCUIT_ASSEMBLER, "gt.recipe.circuitassembler"),
    COMPRESSOR(FunnyTexts.COMPRESSOR, "gt.recipe.compressor"),
    CUTTING_MACHINE(FunnyTexts.CUTTING_MACHINE, "gt.recipe.cuttingsaw"),
    DISTILLERY(FunnyTexts.DISTILLERY, "gt.recipe.distillery"),
    ELECTRIC_FURNACE(FunnyTexts.ELECTRIC_FURNACE, "gt.recipe.furnace"),
    ELECTROLYZER(FunnyTexts.ELECTROLYZER, "gt.recipe.electrolyzer"),
    ELECTROMAGNETIC_SEPARATOR(FunnyTexts.ELECTROMAGNETIC_SEPARATOR, "gt.recipe.electromagneticseparator"),
    EXTRACTOR(FunnyTexts.EXTRACTOR, "gt.recipe.extractor"),
    EXTRUDER(FunnyTexts.EXTRUDER, "gt.recipe.extruder"),
    FERMENTER(FunnyTexts.FERMENTER, "gt.recipe.fermenter"),
    FLUID_CANNER(FunnyTexts.FLUID_CANNER, "gt.recipe.fluidcanner"),
    FLUID_EXTRACTOR(FunnyTexts.FLUID_EXTRACTOR, "gt.recipe.fluidextractor"),
    FLUID_SOLIDIFIER(FunnyTexts.FLUID_SOLIDIFIER, "gt.recipe.fluidsolidifier"),
    FORGE_HAMMER(FunnyTexts.FORGE_HAMMER, "gt.recipe.hammer"),
    FORMING_PRESS(FunnyTexts.FORMING_PRESS, "gt.recipe.press"),
    FLUID_HEATER(FunnyTexts.FLUID_HEATER, "gt.recipe.fluidheater"),
    LASER_ENGRAVER(FunnyTexts.LASER_ENGRAVER, "gt.recipe.laserengraver"),
    LATHE(FunnyTexts.LATHE, "gt.recipe.lathe"),
    MACERATOR(FunnyTexts.MACERATOR, "gt.recipe.macerator"),
    MACERATOR_PULVERIZER(FunnyTexts.MACERATOR_PULVERIZER, "gt.recipe.macerator_pulverizer"),
    MATTER_AMPLIFIER(FunnyTexts.MATTER_AMPLIFIER, "gt.recipe.uuamplifier"),
    MATTER_FABRICATOR(FunnyTexts.MATTER_FABRICATOR, "gt.recipe.massfab"),
    MICROWAVE(FunnyTexts.MICROWAVE, "gt.recipe.microwave"),
    MIXER(FunnyTexts.MIXER, "gt.recipe.mixer"),
    ORE_WASHER(FunnyTexts.ORE_WASHER, "gt.recipe.orewasher"),
    OVEN(FunnyTexts.OVEN, "gt.recipe.oven"),
    PACKAGER(FunnyTexts.PACKAGER, "gt.recipe.packager"),
    PLASMA_ARC_FURNACE(FunnyTexts.PLASMA_ARC_FURNACE, "gt.recipe.plasmaarcfurnace"),
    POLARIZER(FunnyTexts.POLARIZER, "gt.recipe.polarizer"),
    PRINTER(FunnyTexts.PRINTER, "gt.recipe.printer"),
    RECYCLER(FunnyTexts.RECYCLER, "ic.recipe.recycler"),
    REPLICATOR(FunnyTexts.REPLICATOR, "gt.recipe.replicator"),
    SCANNER(FunnyTexts.SCANNER, "gt.recipe.scanner"),
    ROCKBREAKER(FunnyTexts.ROCKBREAKER, "gt.recipe.rockbreaker"),
    SIFTER(FunnyTexts.SIFTER, "gt.recipe.sifter"),
    SLICER(FunnyTexts.SLICER, "gt.recipe.slicer"),
    THERMAL_CENTRIFUGE(FunnyTexts.THERMAL_CENTRIFUGE, "gt.recipe.thermalcentrifuge"),
    UNPACKAGER(FunnyTexts.UNPACKAGER, "gt.recipe.unpackager"),
    WIREMILL(FunnyTexts.WIREMILL, "gt.recipe.wiremill");

    private static class FunnyTexts {

        static final String ALLOY_SMELTER = "gt.recipe.alloysmelter.description";
        static final String ARC_FURNACE = "gt.recipe.arcfurnace.description";
        static final String ASSEMBLER = "gt.recipe.assembler.description";
        static final String AUTOCLAVE = "gt.recipe.autoclave.description";
        static final String BENDING_MACHINE = "gt.recipe.metalbender.description";
        static final String CANNER = "gt.recipe.canner.description";
        static final String CENTRIFUGE = "gt.recipe.centrifuge.description";
        static final String CHEMICAL_BATH = "gt.recipe.chemicalbath.description";
        static final String CHEMICAL_REACTOR = "gt.recipe.chemicalreactor.description";
        static final String CIRCUIT_ASSEMBLER = "gt.recipe.circuitassembler.description";
        static final String COMPRESSOR = "gt.recipe.compressor.description";
        static final String CUTTING_MACHINE = "gt.recipe.cuttingsaw.description";
        static final String DISTILLERY = "gt.recipe.distillery.description";
        static final String ELECTRIC_FURNACE = "gt.recipe.furnace.description";
        static final String ELECTROLYZER = "gt.recipe.electrolyzer.description";
        static final String ELECTROMAGNETIC_SEPARATOR = "gt.recipe.electromagneticseparator.description";
        static final String EXTRACTOR = "gt.recipe.extractor.description";
        static final String EXTRUDER = "gt.recipe.extruder.description";
        static final String FERMENTER = "gt.recipe.fermenter.description";
        static final String FLUID_CANNER = "gt.recipe.fluidcanner.description";
        static final String FLUID_EXTRACTOR = "gt.recipe.fluidextractor.description";
        static final String FLUID_HEATER = "gt.recipe.fluidheater.description";
        static final String FLUID_SOLIDIFIER = "gt.recipe.fluidsolidifier.description";
        static final String FORGE_HAMMER = "gt.recipe.hammer.description";
        static final String FORMING_PRESS = "gt.recipe.press.description";
        static final String LASER_ENGRAVER = "gt.recipe.laserengraver.description";
        static final String LATHE = "gt.recipe.lathe.description";
        static final String MACERATOR = "gt.recipe.macerator.description";
        static final String MACERATOR_PULVERIZER = "gt.recipe.macerator_pulverizer.description";
        static final String MATTER_AMPLIFIER = "gt.recipe.uuamplifier.description";
        static final String MATTER_FABRICATOR = "gt.recipe.massfab.description";
        static final String MICROWAVE = "gt.recipe.microwave.description";
        static final String MIXER = "gt.recipe.mixer.description";
        static final String ORE_WASHER = "gt.recipe.orewasher.description";
        static final String OVEN = "gt.recipe.oven.description";
        static final String PACKAGER = "gt.recipe.packager.description";
        static final String PLASMA_ARC_FURNACE = "gt.recipe.plasmaarcfurnace.description";
        static final String POLARIZER = "gt.recipe.polarizer.description";
        static final String PRINTER = "gt.recipe.printer.description";
        static final String RECYCLER = "ic.recipe.recycler.description";
        static final String REPLICATOR = "gt.recipe.replicator.description";
        static final String ROCKBREAKER = "gt.recipe.rockbreaker.description";
        static final String SIFTER = "gt.recipe.sifter.description";
        static final String SCANNER = "gt.recipe.scanner.description";
        static final String SLICER = "gt.recipe.slicer.description";
        static final String THERMAL_CENTRIFUGE = "gt.recipe.thermalcentrifuge.description";
        static final String UNPACKAGER = "gt.recipe.unpackager.description";
        static final String WIREMILL = "gt.recipe.wiremill.description";
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
            TT_machineType + ": " + EnumChatFormatting.YELLOW + this.name + EnumChatFormatting.RESET };
    }
}
