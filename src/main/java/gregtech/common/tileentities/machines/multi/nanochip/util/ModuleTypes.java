package gregtech.common.tileentities.machines.multi.nanochip.util;

public enum ModuleTypes {

    AssemblyMatrix("Assembly Matrix"),
    BiologicalCoordinator("Biological Coordinator"),
    BoardProcessor("Board Processor"),
    CuttingChamber("Cutting Chamber"),
    EtchingArray("Etching Array"),
    OpticalOrganizer("Optical Organizer"),
    EncasementWrapper("Encasement Wrapper"),
    SMDProcessor("SMD Processor"),
    Splitter("Splitter"),
    SuperconductorSplitter("Superconductor Splitter"),
    WireTracer("Wire Tracer"),

    ;

    public static final ModuleTypes[] VALUES = values();

    private final String name;

    ModuleTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
