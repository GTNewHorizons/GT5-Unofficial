package bartworks.API.enums;

import bartworks.system.material.CircuitGeneration.BWMetaItems;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gregtech.api.util.GTModHandler;

import static bartworks.system.material.CircuitGeneration.CircuitImprintLoader.getTagFromStack;

public enum CircuitImprints {
    NANDChipArray(ItemList.NandChip, ItemList.SlicedCircuit_NANDChipArray, ItemList.CircuitImprint_NANDChipArray, Mods.GregTech),
    Microprocessor(ItemList.Circuit_Microprocessor, ItemList.SlicedCircuit_Microprocessor, ItemList.CircuitImprint_Microprocessor, Mods.GregTech),
    ElectronicCircuit(ItemList.Circuit_Basic, ItemList.SlicedCircuit_ElectronicCircuit ,ItemList.CircuitImprint_ElectronicCircuit, Mods.IndustrialCraft2),
    GoodElectronicCircuit(ItemList.Circuit_Good, ItemList.SlicedCircuit_GoodElectronicCircuit, ItemList.CircuitImprint_GoodElectronicCircuit, Mods.GregTech),
    IntegratedLogicCircuit(ItemList.Circuit_Chip_ILC, ItemList.SlicedCircuit_IntegratedLogicCircuit, ItemList.CircuitImprint_IntegratedLogicCircuit, Mods.GregTech),
    GoodIntegratedCircuit( ItemList.Circuit_Integrated_Good, ItemList.SlicedCircuit_GoodIntegratedCircuit, ItemList.CircuitImprint_GoodIntegratedCircuit, Mods.GregTech),
    AdvancedCircuit(ItemList.Circuit_Advanced, ItemList.SlicedCircuit_AdvancedCircuit, ItemList.CircuitImprint_AdvancedCircuit, Mods.IndustrialCraft2),
    IntegratedProcessor(ItemList.Circuit_Processor, ItemList.SlicedCircuit_IntegratedProcessor, ItemList.CircuitImprint_IntegratedProcessor, Mods.GregTech),
    ProcessorAssembly(ItemList.Circuit_Advanced, ItemList.SlicedCircuit_ProcessorAssembly, ItemList.CircuitImprint_ProcessorAssembly, Mods.GregTech),
    Workstation(ItemList.Circuit_Data, ItemList.SlicedCircuit_Workstation, ItemList.CircuitImprint_Workstation, Mods.GregTech),
    Mainframe(ItemList.Circuit_Elite, ItemList.SlicedCircuit_Mainframe, ItemList.CircuitImprint_Mainframe, Mods.GregTech),
    NanoProcessor(ItemList.Circuit_Nanoprocessor, ItemList.SlicedCircuit_NanoProcessor, ItemList.CircuitImprint_NanoProcessor, Mods.GregTech),
    NanoAssembly(ItemList.Circuit_Nanocomputer, ItemList.SlicedCircuit_NanoAssembly, ItemList.CircuitImprint_NanoAssembly, Mods.GregTech),
    NanoSupercomputer(ItemList.Circuit_Nanocomputer, ItemList.SlicedCircuit_NanoAssembly, ItemList.CircuitImprint_NanoAssembly, Mods.GregTech),
    NanoMainframe(ItemList.Circuit_Master, ItemList.SlicedCircuit_NanoMainframe, ItemList.CircuitImprint_NanoMainframe, Mods.GregTech),
    QuantumProcessor(ItemList.Circuit_Quantumprocessor, ItemList.SlicedCircuit_QuantumProcessor, ItemList.CircuitImprint_QuantumProcessor, Mods.GregTech),
    QuantumAssembly(ItemList.Circuit_Quantumcomputer, ItemList.SlicedCircuit_QuantumAssembly, ItemList.CircuitImprint_QuantumAssembly, Mods.GregTech),
    QuantumSupercomputer(ItemList.Circuit_Masterquantumcomputer, ItemList.SlicedCircuit_QuantumSupercomputer, ItemList.CircuitImprint_QuantumSupercomputer, Mods.GregTech),
    QuantumMainframe(ItemList.Circuit_Quantummainframe, ItemList.SlicedCircuit_QuantumMainframe, ItemList.CircuitImprint_QuantumMainframe, Mods.GregTech),
    CrystalProcessor(ItemList.Circuit_Crystalprocessor, ItemList.SlicedCircuit_CrystalProcessor, ItemList.CircuitImprint_CrystalProcessor, Mods.GregTech),
    CrystalAssembly(ItemList.Circuit_Crystalcomputer, ItemList.SlicedCircuit_CrystalAssembly, ItemList.CircuitImprint_CrystalAssembly, Mods.GregTech),
    CrystalSupercomputer(ItemList.Circuit_Ultimatecrystalcomputer, ItemList.SlicedCircuit_CrystalSupercomputer, ItemList.CircuitImprint_CrystalSupercomputer, Mods.GregTech),
    CrystalMainframe(ItemList.Circuit_Crystalmainframe, ItemList.SlicedCircuit_CrystalMainframe, ItemList.CircuitImprint_CrystalMainframe, Mods.GregTech),
    WetwareProcessor(ItemList.Circuit_Neuroprocessor, ItemList.SlicedCircuit_WetwareProcessor, ItemList.CircuitImprint_WetwareProcessor, Mods.GregTech),
    WetwareAssembly(ItemList.Circuit_Wetwarecomputer, ItemList.SlicedCircuit_WetwareAssembly, ItemList.CircuitImprint_WetwareAssembly, Mods.GregTech),
    WetwareSupercomputer(ItemList.Circuit_Wetwaresupercomputer, ItemList.SlicedCircuit_WetwareSupercomputer, ItemList.CircuitImprint_WetwareSupercomputer, Mods.GregTech),
    BiowareProcessor(ItemList.Circuit_Bioprocessor, ItemList.SlicedCircuit_BiowareProcessor, ItemList.CircuitImprint_BiowareProcessor, Mods.GregTech),
    BiowareAssembly(ItemList.Circuit_Biowarecomputer, ItemList.SlicedCircuit_BiowareAssembly, ItemList.CircuitImprint_BiowareAssembly, Mods.GregTech),
    OpticalProcessor(ItemList.Circuit_OpticalProcessor, ItemList.SlicedCircuit_OpticalProcessor, ItemList.CircuitImprint_OpticalProcessor, Mods.GregTech),

    BasicCircuitBoard(ItemList.BasicCircuitBoard, ItemList.SlicedCircuit_BasicCircuitBoard, ItemList.CircuitImprint_BasicCircuitBoard, Mods.Forestry),
    EnhancedCircuitBoard(ItemList.EnhancedCircuitBoard, ItemList.SlicedCircuit_EnhancedCircuitBoard, ItemList.CircuitImprint_EnhancedCircuitBoard, Mods.Forestry),
    RefinedCircuitBoard(ItemList.RefinedCircuitBoard,ItemList.SlicedCircuit_RefinedCircuitBoard, ItemList.CircuitImprint_RefinedCircuitBoard, Mods.Forestry),
    IntricateCircuitBoard(ItemList.IntricateCircuitBoard, ItemList.SlicedCircuit_IntricateCircuitBoard, ItemList.CircuitImprint_IntricateCircuitBoard, Mods.Forestry),

    ControllerCircuit(ItemList.ControllerCircuit, ItemList.SlicedCircuit_ControllerCircuit, ItemList.CircuitImprint_ControllerCircuit, Mods.Railcraft),
    ReceiverCircuit(ItemList.ReceiverCircuit, ItemList.SlicedCircuit_ReceiverCircuit, ItemList.CircuitImprint_ReceiverCircuit, Mods.Railcraft),
    SignalCircuit(ItemList.SignalCircuit, ItemList.SlicedCircuit_SignalCircuit, ItemList.CircuitImprint_SignalCircuit, Mods.Railcraft),

    HighEnergyFlowCircuit(ItemList.HighEnergyFlowCircuit, ItemList.SlicedCircuit_HighEnergyFlowCircuit, ItemList.CircuitImprint_HighEnergyFlowCircuit, Mods.NewHorizonsCoreMod)
    ;


        public final ItemList circuit;
        public final ItemList slicedCircuit;
        public final ItemList imprint;
        public final Mods sourceMod;
        private CircuitImprints(ItemList circuit, ItemList slicedCircuit, ItemList imprint, Mods sourceMod){
            this.circuit = circuit;
            this.slicedCircuit = slicedCircuit;
            this.imprint = imprint;
            this.sourceMod = sourceMod;
        }

        public static void registerSlicedCircuitsAndImprints(){
            for (CircuitImprints entry : CircuitImprints.values()){
                if (!entry.sourceMod.isModLoaded()) continue;
                entry.slicedCircuit.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(entry.circuit.get(1)), 1, 1));
                entry.imprint.set(BWMetaItems.getCircuitParts().getStackWithNBT(getTagFromStack(entry.circuit.get(1)), 0, 1));
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
                ItemList.HighEnergyFlowCircuit.set(GTModHandler.getModItem(Mods.NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 1, 0));
            }
        }
}
