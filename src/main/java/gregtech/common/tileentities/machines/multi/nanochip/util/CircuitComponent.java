package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTModHandler.getModItem;

import java.util.function.Supplier;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import goodgenerator.items.GGMaterial;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.CircuitComponentFakeItem;
import gregtech.api.util.GTOreDictUnificator;
import tectech.thing.CustomItemList;

public enum CircuitComponent {

    // When adding to this list, PLEASE only add to the end!
    // The ordinals are used as item ids for the fake items, so adding in the middle will break saved state!

    // spotless:off

    // Wires
    WireNiobiumTitanium(
        "gt.circuitcomponent.wirenbti",
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 1)),
    WireYttriumBariumCuprate(
        "gt.circuitcomponent.wireybc",
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 1)),
    WireLumiium(
        "gt.circuitcomponent.wirelumiium",
        () -> GGMaterial.lumiium.get(OrePrefixes.wireFine, 1)),
    WireLanthanum(
        "gt.circuitcomponent.wirelanthanum",
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 1)),
    WireSpacetime(
        "gt.circuitcomponent.wirespacetime",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 1)),

    ProcessedWireNiobiumTitanium("gt.circuitcomponent.processed.wirenbti", true),
    ProcessedWireYttriumBariumCuprate("gt.circuitcomponent.processed.wireybc", true),
    ProcessedWireLumiium("gt.circuitcomponent.processed.wirelumiium", true),
    ProcessedWireLanthanum("gt.circuitcomponent.processed.wirelanthanum", true),
    ProcessedWireSpacetime("gt.circuitcomponent.processed.wirespacetime", true),

    // SMDs
    SMDResistor(
        "gt.circuitcomponent.smd.resistor",
        () -> ItemList.Circuit_Parts_ResistorSMD.get(1)),
    SMDTransistor(
        "gt.circuitcomponent.smd.transistor",
        () -> ItemList.Circuit_Parts_TransistorSMD.get(1)),
    SMDInductor(
        "gt.circuitcomponent.smd.inductor",
        () -> ItemList.Circuit_Parts_InductorSMD.get(1)),
    SMDCapacitor(
        "gt.circuitcomponent.smd.capacitor",
        () -> ItemList.Circuit_Parts_CapacitorSMD.get(1)),
    SMDDiode(
        "gt.circuitcomponent.smd.diode",
        () -> ItemList.Circuit_Parts_DiodeSMD.get(1)),
    AdvSMDResistor(
        "gt.circuitcomponent.asmd.resistor",
        () -> ItemList.Circuit_Parts_ResistorASMD.get(1)),
    AdvSMDTransistor(
        "gt.circuitcomponent.asmd.transistor",
        () -> ItemList.Circuit_Parts_TransistorASMD.get(1)),
    AdvSMDInductor(
        "gt.circuitcomponent.asmd.inductor",
        () -> ItemList.Circuit_Parts_InductorASMD.get(1)),
    AdvSMDCapacitor(
        "gt.circuitcomponent.asmd.capacitor",
        () -> ItemList.Circuit_Parts_CapacitorASMD.get(1)),
    AdvSMDDiode(
        "gt.circuitcomponent.asmd.diode",
        () -> ItemList.Circuit_Parts_DiodeASMD.get(1)),
    OpticalSMDResistor(
        "gt.circuitcomponent.xsmd.resistor",
        () -> ItemList.Circuit_Parts_ResistorXSMD.get(1)),
    OpticalSMDTransistor(
        "gt.circuitcomponent.xsmd.transistor",
        () -> ItemList.Circuit_Parts_TransistorXSMD.get(1)),
    OpticalSMDInductor(
        "gt.circuitcomponent.xsmd.inductor",
        () -> ItemList.Circuit_Parts_InductorXSMD.get(1)),
    OpticalSMDCapacitor(
        "gt.circuitcomponent.xsmd.capacitor",
        () -> ItemList.Circuit_Parts_CapacitorXSMD.get(1)),
    OpticalSMDDiode(
        "gt.circuitcomponent.xsmd.diode",
        () -> ItemList.Circuit_Parts_DiodeXSMD.get(1)),

    ProcessedSMDResistor("gt.circuitcomponent.processed.smd.resistor", true),
    ProcessedSMDTransistor("gt.circuitcomponent.processed.smd.transistor", true),
    ProcessedSMDInductor("gt.circuitcomponent.processed.smd.inductor", true),
    ProcessedSMDCapacitor("gt.circuitcomponent.processed.smd.capacitor", true),
    ProcessedSMDDiode("gt.circuitcomponent.processed.smd.diode", true),
    ProcessedAdvSMDResistor("gt.circuitcomponent.processed.asmd.resistor", true),
    ProcessedAdvSMDTransistor("gt.circuitcomponent.processed.asmd.transistor", true),
    ProcessedAdvSMDInductor("gt.circuitcomponent.processed.asmd.inductor", true),
    ProcessedAdvSMDCapacitor("gt.circuitcomponent.processed.asmd.capacitor", true),
    ProcessedAdvSMDDiode("gt.circuitcomponent.processed.asmd.diode", true),
    ProcessedOpticalSMDResistor("gt.circuitcomponent.processed.xsmd.resistor", true),
    ProcessedOpticalSMDTransistor("gt.circuitcomponent.processed.xsmd.transistor", true),
    ProcessedOpticalSMDInductor("gt.circuitcomponent.processed.xsmd.inductor", true),
    ProcessedOpticalSMDCapacitor("gt.circuitcomponent.processed.xsmd.capacitor", true),
    ProcessedOpticalSMDDiode("gt.circuitcomponent.processed.xsmd.diode", true),

    // Boards
    BoardMultifiberglassElite(
        "gt.circuitcomponent.board.multifiberelite",
        () -> ItemList.Circuit_Board_Multifiberglass_Elite.get(1)),
    BoardWetwareLifesupport(
        "gt.circuitcomponent.board.wetwarelifesupport",
        () -> ItemList.Circuit_Board_Wetware_Extreme.get(1)),
    BoardBioMutated(
        "gt.circuitcomponent.board.biomutated",
        () -> ItemList.Circuit_Board_Bio_Ultra.get(1)),
    BoardOptical(
        "gt.circuitcomponent.board.optical",
        () -> ItemList.Circuit_Board_Optical.get(1)),
    NeuroProcessingUnit(
        "gt.circuitcomponent.neuroprocessingunit",
        () -> ItemList.Circuit_Chip_NeuroCPU.get(1)),
    BioProcessingUnit(
        "gt.circuitcomponent.bioprocessingunit",
        () -> ItemList.Circuit_Chip_BioCPU.get(1)),

    ProcessedBoardMultifiberglassElite("gt.circuitcomponent.processed.board.multifiberelite", true),
    ProcessedBoardWetwareLifesupport("gt.circuitcomponent.processed.board.wetwarelifesupport", true),
    ProcessedBoardBioMutated("gt.circuitcomponent.processed.board.biomutated", true),
    ProcessedBoardOptical("gt.circuitcomponent.processed.board.optical", true),
    ProcessedNeuroProcessingUnit("gt.circuitcomponent.processed.neuroprocessingunit", true),
    ProcessedBioProcessingUnit("gt.circuitcomponent.processed.board.bioprocessingunit", true),

    // Wafers and chips
    WaferNanoCPU(
        "gt.circuitcomponent.waferNanoCPU",
        () -> ItemList.Circuit_Wafer_NanoCPU.get(1)),
    WaferRAM(
        "gt.circuitcomponent.waferram",
        () -> ItemList.Circuit_Wafer_Ram.get(1)),
    WaferNOR(
        "gt.circuitcomponent.wafernor",
        () -> ItemList.Circuit_Wafer_NOR.get(1)),
    WaferNAND(
        "gt.circuitcomponent.wafernand",
        () -> ItemList.Circuit_Wafer_NAND.get(1)),
    WaferASOC(
        "gt.circuitcomponent.waferasoc",
        () -> ItemList.Circuit_Wafer_SoC2.get(1)),
    WaferPikoPIC(
        "gt.circuitcomponent.waferpikopic",
        () -> ItemList.Circuit_Wafer_PPIC.get(1)),
    WaferQuantumPIC(
        "gt.circuitcomponent.waferquantumpic",
        () -> ItemList.Circuit_Wafer_QPIC.get(1)),
    WaferPico(
        "gt.circuitcomponent.waferpico",
        () -> getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 1, 0, new ItemStack(Blocks.fire))),

    ProcessedChipNanoCPU("gt.circuitcomponent.processed.chipnanocpu", true),
    ProcessedChipRAM("gt.circuitcomponent.processed.chipram", true),
    ProcessedChipNOR("gt.circuitcomponent.processed.chipnor", true),
    ProcessedChipNAND("gt.circuitcomponent.processed.chipnand", true),
    ProcessedChipASOC("gt.circuitcomponent.processed.chipasoc", true),
    ProcessedChipPikoPIC("gt.circuitcomponent.processed.chippikopic", true),
    ProcessedChipQuantumPIC("gt.circuitcomponent.processed.chipquantumpic", true),
    ProcessedChipPico("gt.circuitcomponent.processed.chippico", true),

    // Crystal components
    ChipCrystalCPU(
        "gt.circuitcomponent.chipcrystalcpu",
        () -> ItemList.Circuit_Chip_CrystalCPU.get(1)),
    ChipAdvCrystalCPU(
        "gt.circuitcomponent.chipadvcrystalcpu",
        () -> ItemList.Circuit_Chip_CrystalSoC.get(1)),

    ProcessedChipCrystalCPU("gt.circuitcomponent.processed.chipcrystalcpu", true),
    ProcessedChipAdvCrystalCPU("gt.circuitcomponent.processed.chipadvcrystalcpu", true),

    // Optical components
    ChipOpticalCPU(
        "gt.circuitcomponent.opticalcpu",
        () -> ItemList.Optically_Perfected_CPU.get(1)),
    OpticalRAM(
        "gt.circuitcomponent.opticalram",
        () -> ItemList.Optically_Compatible_Memory.get(1)),

    ProcessedChipOpticalCPU("gt.circuitcomponent.processed.opticalcpu", true),
    ProcessedOpticalRAM("gt.circuitcomponent.processed.opticalram", true),

    // Superconductors
    SuperconductorLuV(
        "gt.circuitcomponent.superconductorluv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1)),
    SuperconductorZPM(
        "gt.circuitcomponent.superconductorzpm",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1)),
    SuperconductorUHV(
        "gt.circuitcomponent.superconductoruhv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1)),
    SuperconductorUEV(
        "gt.circuitcomponent.superconductoruev",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1)),
    SuperconductorUIV(
        "gt.circuitcomponent.superconductoruiv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1)),
    SuperconductorUMV(
        "gt.circuitcomponent.superconductorumv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1)),

    ProcessedSuperconductorLuV("gt.circuitcomponent.processed.superconductorluv", true),
    ProcessedSuperconductorZPM("gt.circuitcomponent.processed.superconductorzpm", true),
    ProcessedSuperconductorUHV("gt.circuitcomponent.processed.superconductoruhv", true),
    ProcessedSuperconductorUEV("gt.circuitcomponent.processed.superconductoruev", true),
    ProcessedSuperconductorUIV("gt.circuitcomponent.processed.superconductoruiv", true),
    ProcessedSuperconductorUMV("gt.circuitcomponent.processed.superconductorumv", true),

    // Frameboxes
    FrameboxAluminium(
        "gt.circuitcomponent.frame.aluminium",
        () -> GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1)),
    FrameboxTritanium("gt.circuitcomponent.frame.tritanium",
        () -> GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 1)),
    FrameboxNeutronium("gt.circuitcomponent.frame.neutronium",
        () -> GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1)),

    ProcessedFrameboxAluminium("gt.circuitcomponent.processed.frame.aluminium", true),
    ProcessedFrameboxTritanium("gt.circuitcomponent.processed.frame.tritanium", true),
    ProcessedFrameboxNeutronium("gt.circuitcomponent.processed.frame.neutronium", true),

    // Foils
    FoilSiliconeRubber(
        "gt.circuitcomponent.sheet.siliconerubber",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.RubberSilicone, 1)),
    FoilPolybenzimidazole(
        "gt.circuitcomponent.sheet.polybenzimidazole",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 1)),
    FoilRadoxPolymer(
        "gt.circuitcomponent.sheet.radoxpolymer",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.RadoxPolymer, 1)),
    FoilShirabon(
        "gt.circuitcomponent.sheet.shirabon",
        () -> GGMaterial.shirabon.get(OrePrefixes.foil, 1)),

    ProcessedFoilSiliconeRubber("gt.circuitcomponent.sheet.siliconerubber", true),
    ProcessedFoilPolybenzimidazole("gt.circuitcomponent.sheet.polybenzimidazole", true),
    ProcessedFoilRadoxPolymer("gt.circuitcomponent.sheet.radoxpolymer", true),
    ProcessedFoilShirabon("gt.circuitcomponent.sheet.shirabon", true),

    // Misc
    CableOpticalFiber(
        "gt.circuitcomponent.cable.opticalfiber",
        () -> CustomItemList.DATApipe.get(1)),

    // Bolts
    BoltEnrichedHolmium(
        "gt.circuitcomponent.bolt.enrichedholmium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 1)),
    BoltTranscendentMetal(
        "gt.circuitcomponent.bolt.transcendentmetal",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.TranscendentMetal, 1)),
    BoltNeutronium(
        "gt.circuitcomponent.bolt.neutronium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Neutronium, 1)),
    BoltIndium(
        "gt.circuitcomponent.bolt.indium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Indium, 1)),

    // Circuits
    // The first three circuits in a line can be recursively used in the assembly matrix, and all of them can be turned
    // into a physical circuit item
    CrystalProcessor(
        "gt.circuitcomponent.crystalprocessor",
        () -> ItemList.Circuit_Crystalprocessor.get(1)),
    CrystalAssembly(
        "gt.circuitcomponent.crystalasssembly",
        () -> ItemList.Circuit_Crystalcomputer.get(1)),
    CrystalComputer(
        "gt.circuitcomponent.crystalcomputer",
        () -> ItemList.Circuit_Ultimatecrystalcomputer.get(1)),
    CrystalMainframe(
        "gt.circuitcomponent.crystalmainframe",
        () -> ItemList.Circuit_Crystalmainframe.get(1)),
    WetwareProcessor(
        "gt.circuitcomponent.wetwareprocessor",
        () -> ItemList.Circuit_Neuroprocessor.get(1)),
    WetwareAssembly(
        "gt.circuitcomponent.wetwareassembly",
        () -> ItemList.Circuit_Wetwarecomputer.get(1)),
    WetwareComputer(
        "gt.circuitcomponent.wetwarecomputer",
        () -> ItemList.Circuit_Wetwaresupercomputer.get(1)),
    WetwareMainframe(
        "gt.circuitcomponent.wetwaremainframe",
        () -> ItemList.Circuit_Wetwaremainframe.get(1)),
    BiowareProcessor(
        "gt.circuitcomponent.biowareprocessor",
        () -> ItemList.Circuit_Bioprocessor.get(1)),
    BiowareAssembly(
        "gt.circuitcomponent.biowareassembly",
        () -> ItemList.Circuit_Biowarecomputer.get(1)),
    BiowareComputer(
        "gt.circuitcomponent.biowarecomputer",
        () -> ItemList.Circuit_Biowaresupercomputer.get(1)),
    BiowareMainframe(
        "gt.circuitcomponent.biowaremainframe",
        () -> ItemList.Circuit_Biomainframe.get(1)),
    OpticalProcessor(
        "gt.circuitcomponent.opticalprocessor",
        () -> ItemList.Circuit_OpticalProcessor.get(1)),
    OpticalAssembly(
        "gt.circuitcomponent.opticalassembly",
        () -> ItemList.Circuit_OpticalAssembly.get(1)),
    OpticalComputer(
        "gt.circuitcomponent.opticalcomputer",
        () -> ItemList.Circuit_OpticalComputer.get(1)),
    OpticalMainframe(
        "gt.circuitcomponent.opticalmainframe",
        () -> ItemList.Circuit_OpticalMainframe.get(1)),
    PicoCircuit(
        "gt.circuitcomponent.picocircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "item.PikoCircuit", 1, 0, new ItemStack(Blocks.fire))),
    QuantumCircuit(
        "gt.circuitcomponent.quantumcircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "item.QuantumCircuit", 1, 0, new ItemStack(Blocks.fire))),

    ;

    public static final CircuitComponent[] VALUES = values();

    // spotless:on

    public final String unlocalizedName;
    public String fallbackLocalizedName = null;

    public final String iconString;
    public final Supplier<ItemStack> realComponent;
    public final boolean isProcessed;

    // CC constructor
    CircuitComponent(String unlocalizedName, Supplier<ItemStack> realComponent) {
        this(unlocalizedName, realComponent, false);
    }

    // PC constructor
    CircuitComponent(String unlocalizedName, boolean isProcessed) {
        this(unlocalizedName, null, isProcessed);
    }

    // Internal constructor
    CircuitComponent(String unlocalizedName, Supplier<ItemStack> realComponent, boolean isProcessed) {
        this.unlocalizedName = unlocalizedName;
        this.realComponent = realComponent;
        this.isProcessed = isProcessed;

        this.iconString = name().toLowerCase();
        codechicken.nei.api.API.hideItem(getFakeStack(1));
    }

    public String getLocalizedName() {
        // If a translation key is set, use it, otherwise use the automatically generated fallback name
        if (StatCollector.canTranslate(unlocalizedName)) {
            return StatCollector.translateToLocal(unlocalizedName);
        }
        return fallbackLocalizedName;
    }

    // ItemStack of a fake item, only for display and recipe checking purposes
    public ItemStack getFakeStack(int amount) {
        return new ItemStack(CircuitComponentFakeItem.INSTANCE, amount, this.ordinal());
    }

    public static CircuitComponent tryGetFromFakeStack(ItemStack stack) {
        if (stack.getItemDamage() >= CircuitComponent.VALUES.length) return null;
        return getFromFakeStackUnsafe(stack);
    }

    public static CircuitComponent getFromFakeStackUnsafe(ItemStack stack) {
        // If this throws an IndexOutOfBounds exception, there is a bug
        return CircuitComponent.VALUES[stack.getItemDamage()];
    }

    public static class CircuitComponentStack {

        private final CircuitComponent mCircuitComponent;
        private final int mSize;

        public CircuitComponentStack(CircuitComponent mCircuitComponent, int mSize) {
            this.mCircuitComponent = mCircuitComponent;
            this.mSize = mSize;
        }

        public CircuitComponent getCircuitComponent() {
            return mCircuitComponent;
        }

        public int getSize() {
            return mSize;
        }
    }
}
