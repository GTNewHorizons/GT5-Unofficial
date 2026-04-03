package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.common.tileentities.machines.multi.nanochip.util.NanochipTooltipValues.NAC_MODULE;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;

public enum ModuleTypes {

    // spotless:off
    AssemblyMatrix("GT5U.tooltip.nac.module.assembly_matrix.name", ItemList.NanoChipModule_AssemblyMatrix.get(1)),

    BiologicalCoordinator("GT5U.tooltip.nac.module.biological_coordinator.name", ItemList.NanoChipModule_BiologicalCoordinator.get(1)),

    BoardProcessor("GT5U.tooltip.nac.module.board_processor.name", ItemList.NanoChipModule_BoardProcessor.get(1)),

    CuttingChamber("GT5U.tooltip.nac.module.cutting_chamber.name", ItemList.NanoChipModule_CuttingChamber.get(1)),

    EtchingArray("GT5U.tooltip.nac.module.etching_array.name", ItemList.NanoChipModule_EtchingArray.get(1)),

    OpticalOrganizer("GT5U.tooltip.nac.module.optical_organizer.name", ItemList.NanoChipModule_OpticalOrganizer.get(1)),

    EncasementWrapper("GT5U.tooltip.nac.module.encasement_wrapper.name", ItemList.NanoChipModule_EncasementWrapper.get(1)),

    SMDProcessor("GT5U.tooltip.nac.module.smd_processor.name", ItemList.NanoChipModule_SMDProcessor.get(1)),

    Splitter("GT5U.tooltip.nac.module.splitter.name", ItemList.NanoChipModule_Splitter.get(1)),

    SuperconductorSplitter("GT5U.tooltip.nac.module.superconductor_splitter.name", ItemList.NanoChipModule_SuperconductorSplitter.get(1)),

    WireTracer("GT5U.tooltip.nac.module.wire_tracer.name", ItemList.NanoChipModule_WireTracer.get(1)),

    ;
    // spotless:on

    public static final ModuleTypes[] VALUES = values();

    private final String nameKey;
    private final ItemStack displayStack;

    ModuleTypes(String nameKey, ItemStack displayStack) {
        this.nameKey = nameKey;
        this.displayStack = displayStack;
    }

    public String getName() {
        return translateToLocal(nameKey);
    }

    public String getMachineModeText() {
        return translateToLocalFormatted("GT5U.tooltip.nac.interface.machine_info", getName(), NAC_MODULE);
    }

    public ItemStack getDisplayStack() {
        return displayStack;
    }
}
