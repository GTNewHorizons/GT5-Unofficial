package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;

public enum ModuleTypes {

    AssemblyMatrix("Assembly Matrix", ItemList.NanoChipModule_AssemblyMatrix.get(1)),
    BiologicalCoordinator("Biological Coordinator", ItemList.NanoChipModule_BiologicalCoordinator.get(1)),
    BoardProcessor("Board Processor", ItemList.NanoChipModule_BoardProcessor.get(1)),
    CuttingChamber("Cutting Chamber", ItemList.NanoChipModule_CuttingChamber.get(1)),
    EtchingArray("Etching Array", ItemList.NanoChipModule_EtchingArray.get(1)),
    OpticalOrganizer("Optical Organizer", ItemList.NanoChipModule_OpticalOrganizer.get(1)),
    EncasementWrapper("Encasement Wrapper", ItemList.NanoChipModule_EncasementWrapper.get(1)),
    SMDProcessor("SMD Processor", ItemList.NanoChipModule_SMDProcessor.get(1)),
    Splitter("Splitter", ItemList.NanoChipModule_Splitter.get(1)),
    SuperconductorSplitter("Superconductor Splitter", ItemList.NanoChipModule_SuperconductorSplitter.get(1)),
    WireTracer("Wire Tracer", ItemList.NanoChipModule_WireTracer.get(1)),

    ;

    public static final ModuleTypes[] VALUES = values();

    private final String name;
    private final ItemStack displayStack;

    ModuleTypes(String name, ItemStack displayStack) {
        this.name = name;
        this.displayStack = displayStack;
    }

    public String getName() {
        return name;
    }

    public ItemStack getDisplayStack() {
        return displayStack;
    }
}
