package bartworks.API.enums;

import static bartworks.system.material.CircuitGeneration.CircuitPartsItem.getCircuitParts;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;

import bartworks.common.tileentities.multis.MTECircuitAssemblyLine;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

/**
 * Enum representing the necessary data to add new CAL imprints.
 * The id field corresponds to the meta ID of the item representing the imprints.
 * <ul>
 * <li>0 and 1 are taken respectively for the old imprinted and sliced circuits.</li>
 * <li>2 and 3 are taken respectively for the raw imprinting supporting board and the imprinting supporting board.</li>
 * </ul>
 * Sliced circuits use imprints ID with an offset of 1000, and the hardcoded limit of IDs reserved for circuit
 * imprinting
 * is 10000, which leaves ~5000 IDs possible for imprinted circuits. But if possible, please use contiguous IDs.
 */
public enum CircuitImprint {

    NANDChipArray(4, ItemList.NandChip, ItemList.SlicedCircuit_NANDChipArray, ItemList.CircuitImprint_NANDChipArray,
        Mods.GregTech),
    Microprocessor(5, ItemList.Circuit_Microprocessor, ItemList.SlicedCircuit_Microprocessor,
        ItemList.CircuitImprint_Microprocessor, Mods.GregTech),
    ElectronicCircuit(6, ItemList.Circuit_Basic, ItemList.SlicedCircuit_ElectronicCircuit,
        ItemList.CircuitImprint_ElectronicCircuit, Mods.IndustrialCraft2),
    GoodElectronicCircuit(7, ItemList.Circuit_Good, ItemList.SlicedCircuit_GoodElectronicCircuit,
        ItemList.CircuitImprint_GoodElectronicCircuit, Mods.GregTech),
    IntegratedLogicCircuit(8, ItemList.Circuit_Chip_ILC, ItemList.SlicedCircuit_IntegratedLogicCircuit,
        ItemList.CircuitImprint_IntegratedLogicCircuit, Mods.GregTech),
    GoodIntegratedCircuit(9, ItemList.Circuit_Integrated_Good, ItemList.SlicedCircuit_GoodIntegratedCircuit,
        ItemList.CircuitImprint_GoodIntegratedCircuit, Mods.GregTech),
    AdvancedCircuit(10, ItemList.Circuit_Advanced, ItemList.SlicedCircuit_AdvancedCircuit,
        ItemList.CircuitImprint_AdvancedCircuit, Mods.IndustrialCraft2),
    IntegratedProcessor(11, ItemList.Circuit_Processor, ItemList.SlicedCircuit_IntegratedProcessor,
        ItemList.CircuitImprint_IntegratedProcessor, Mods.GregTech),
    ProcessorAssembly(12, ItemList.Circuit_Advanced, ItemList.SlicedCircuit_ProcessorAssembly,
        ItemList.CircuitImprint_ProcessorAssembly, Mods.GregTech),
    Workstation(13, ItemList.Circuit_Data, ItemList.SlicedCircuit_Workstation, ItemList.CircuitImprint_Workstation,
        Mods.GregTech),
    Mainframe(14, ItemList.Circuit_Elite, ItemList.SlicedCircuit_Mainframe, ItemList.CircuitImprint_Mainframe,
        Mods.GregTech),
    NanoProcessor(15, ItemList.Circuit_Nanoprocessor, ItemList.SlicedCircuit_NanoProcessor,
        ItemList.CircuitImprint_NanoProcessor, Mods.GregTech),
    NanoAssembly(16, ItemList.Circuit_Nanocomputer, ItemList.SlicedCircuit_NanoAssembly,
        ItemList.CircuitImprint_NanoAssembly, Mods.GregTech),
    NanoSupercomputer(17, ItemList.Circuit_Elitenanocomputer, ItemList.SlicedCircuit_NanoSupercomputer,
        ItemList.CircuitImprint_NanoSupercomputer, Mods.GregTech),
    NanoMainframe(18, ItemList.Circuit_Master, ItemList.SlicedCircuit_NanoMainframe,
        ItemList.CircuitImprint_NanoMainframe, Mods.GregTech),
    QuantumProcessor(19, ItemList.Circuit_Quantumprocessor, ItemList.SlicedCircuit_QuantumProcessor,
        ItemList.CircuitImprint_QuantumProcessor, Mods.GregTech),
    QuantumAssembly(20, ItemList.Circuit_Quantumcomputer, ItemList.SlicedCircuit_QuantumAssembly,
        ItemList.CircuitImprint_QuantumAssembly, Mods.GregTech),
    QuantumSupercomputer(21, ItemList.Circuit_Masterquantumcomputer, ItemList.SlicedCircuit_QuantumSupercomputer,
        ItemList.CircuitImprint_QuantumSupercomputer, Mods.GregTech),
    QuantumMainframe(22, ItemList.Circuit_Quantummainframe, ItemList.SlicedCircuit_QuantumMainframe,
        ItemList.CircuitImprint_QuantumMainframe, Mods.GregTech),
    CrystalProcessor(23, ItemList.Circuit_Crystalprocessor, ItemList.SlicedCircuit_CrystalProcessor,
        ItemList.CircuitImprint_CrystalProcessor, Mods.GregTech),
    CrystalAssembly(24, ItemList.Circuit_Crystalcomputer, ItemList.SlicedCircuit_CrystalAssembly,
        ItemList.CircuitImprint_CrystalAssembly, Mods.GregTech),
    CrystalSupercomputer(25, ItemList.Circuit_Ultimatecrystalcomputer, ItemList.SlicedCircuit_CrystalSupercomputer,
        ItemList.CircuitImprint_CrystalSupercomputer, Mods.GregTech),
    CrystalMainframe(26, ItemList.Circuit_Crystalmainframe, ItemList.SlicedCircuit_CrystalMainframe,
        ItemList.CircuitImprint_CrystalMainframe, Mods.GregTech),
    WetwareProcessor(27, ItemList.Circuit_Neuroprocessor, ItemList.SlicedCircuit_WetwareProcessor,
        ItemList.CircuitImprint_WetwareProcessor, Mods.GregTech),
    WetwareAssembly(28, ItemList.Circuit_Wetwarecomputer, ItemList.SlicedCircuit_WetwareAssembly,
        ItemList.CircuitImprint_WetwareAssembly, Mods.GregTech),
    WetwareSupercomputer(29, ItemList.Circuit_Wetwaresupercomputer, ItemList.SlicedCircuit_WetwareSupercomputer,
        ItemList.CircuitImprint_WetwareSupercomputer, Mods.GregTech),
    BiowareProcessor(30, ItemList.Circuit_Bioprocessor, ItemList.SlicedCircuit_BiowareProcessor,
        ItemList.CircuitImprint_BiowareProcessor, Mods.GregTech),
    BiowareAssembly(31, ItemList.Circuit_Biowarecomputer, ItemList.SlicedCircuit_BiowareAssembly,
        ItemList.CircuitImprint_BiowareAssembly, Mods.GregTech),
    OpticalProcessor(32, ItemList.Circuit_OpticalProcessor, ItemList.SlicedCircuit_OpticalProcessor,
        ItemList.CircuitImprint_OpticalProcessor, Mods.GregTech),

    BasicCircuitBoard(33, ItemList.BasicCircuitBoard, ItemList.SlicedCircuit_BasicCircuitBoard,
        ItemList.CircuitImprint_BasicCircuitBoard, Mods.Forestry),
    EnhancedCircuitBoard(34, ItemList.EnhancedCircuitBoard, ItemList.SlicedCircuit_EnhancedCircuitBoard,
        ItemList.CircuitImprint_EnhancedCircuitBoard, Mods.Forestry),
    RefinedCircuitBoard(35, ItemList.RefinedCircuitBoard, ItemList.SlicedCircuit_RefinedCircuitBoard,
        ItemList.CircuitImprint_RefinedCircuitBoard, Mods.Forestry),
    IntricateCircuitBoard(36, ItemList.IntricateCircuitBoard, ItemList.SlicedCircuit_IntricateCircuitBoard,
        ItemList.CircuitImprint_IntricateCircuitBoard, Mods.Forestry),

    ControllerCircuit(37, ItemList.ControllerCircuit, ItemList.SlicedCircuit_ControllerCircuit,
        ItemList.CircuitImprint_ControllerCircuit, Mods.Railcraft),
    ReceiverCircuit(38, ItemList.ReceiverCircuit, ItemList.SlicedCircuit_ReceiverCircuit,
        ItemList.CircuitImprint_ReceiverCircuit, Mods.Railcraft),
    SignalCircuit(39, ItemList.SignalCircuit, ItemList.SlicedCircuit_SignalCircuit,
        ItemList.CircuitImprint_SignalCircuit, Mods.Railcraft),

    HighEnergyFlowCircuit(40, ItemList.HighEnergyFlowCircuit, ItemList.SlicedCircuit_HighEnergyFlowCircuit,
        ItemList.CircuitImprint_HighEnergyFlowCircuit, Mods.NewHorizonsCoreMod);

    public final ItemList circuit;
    public final ItemList slicedCircuit;
    public final ItemList imprint;
    public final Mods sourceMod;
    public final int id;

    /**
     * Map solely used to convert old format imprint into the new one. This is left for backward purposes.
     * This has been deprecated during the 2.9 dev cycle.
     */
    @Deprecated
    public static final Map<String, CircuitImprint> IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES = new HashMap<>();
    public static final Map<Integer, CircuitImprint> IMPRINT_LOOKUPS_BY_IDS = new HashMap<>();

    CircuitImprint(int id, ItemList circuit, ItemList slicedCircuit, ItemList imprint, Mods sourceMod) {
        this.id = id;
        this.circuit = circuit;
        this.slicedCircuit = slicedCircuit;
        this.imprint = imprint;
        this.sourceMod = sourceMod;
    }

    public static void registerSlicedCircuitsAndImprints() {
        for (CircuitImprint entry : CircuitImprint.values()) {
            if (!entry.sourceMod.isModLoaded()) continue;
            entry.slicedCircuit.set(getCircuitParts().getSlicedStack(entry.id));
            entry.imprint.set(getCircuitParts().getImprintStack(entry.id));
        }
    }

    public static void registerExternalModCircuitsInItemList() {
        if (Mods.Forestry.isModLoaded()) {
            ItemList.BasicCircuitBoard.set(GTModHandler.getModItem(Mods.Forestry.ID, "chipsets", 1, 0));
            ItemList.EnhancedCircuitBoard.set(GTModHandler.getModItem(Mods.Forestry.ID, "chipsets", 1, 1));
            ItemList.RefinedCircuitBoard.set(GTModHandler.getModItem(Mods.Forestry.ID, "chipsets", 1, 2));
            ItemList.IntricateCircuitBoard.set(GTModHandler.getModItem(Mods.Forestry.ID, "chipsets", 1, 3));
        }
        if (Mods.Railcraft.isModLoaded()) {
            ItemList.ControllerCircuit.set(GTModHandler.getModItem(Mods.Railcraft.ID, "part.circuit", 1, 0));
            ItemList.ReceiverCircuit.set(GTModHandler.getModItem(Mods.Railcraft.ID, "part.circuit", 1, 1));
            ItemList.SignalCircuit.set(GTModHandler.getModItem(Mods.Railcraft.ID, "part.circuit", 1, 2));
        }
        if (Mods.NewHorizonsCoreMod.isModLoaded()) {
            ItemList.HighEnergyFlowCircuit
                .set(GTModHandler.getModItem(Mods.NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 1, 0));
        }
    }

    public static void populateImprintLookups() {
        for (CircuitImprint entry : CircuitImprint.values()) {
            if (!entry.sourceMod.isModLoaded()) continue;
            IMPRINT_LOOKUPS_BY_UNLOCALISED_NAMES.put(
                entry.circuit.get(1)
                    .getUnlocalizedName(),
                entry);
            IMPRINT_LOOKUPS_BY_IDS.put(entry.id, entry);
        }
    }

    public static CircuitImprint findCircuitImprintByImprintStack(ItemStack imprint) {
        if (!MTECircuitAssemblyLine.isValidImprint(imprint)) return null;
        return CircuitImprint.IMPRINT_LOOKUPS_BY_IDS.get(imprint.getItemDamage());
    }
}
