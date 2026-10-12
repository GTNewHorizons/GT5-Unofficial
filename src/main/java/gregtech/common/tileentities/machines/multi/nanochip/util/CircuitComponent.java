package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTModHandler.getModItem;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Supplier;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.items.CircuitComponentFakeItem;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import tectech.thing.CustomItemList;

public enum CircuitComponent {

    // spotless:off

    // Wires
    // IDs: 0-99 CC, 100-199 PC
    WireNiobiumTitanium(
        0,
        "gt.circuitcomponent.wirenbti",
        () -> MaterialLibAPI.getStack(Materials.NiobiumTitanium, Shapes.wireFine, 1)),
    WireYttriumBariumCuprate(
        1,
        "gt.circuitcomponent.wireybc",
        () -> MaterialLibAPI.getStack(Materials.YttriumBariumCuprate, Shapes.wireFine, 1)),
    WireLumiium(
        2,
        "gt.circuitcomponent.wirelumiium",
        () -> MaterialLibAPI.getStack(Materials.Lumiium, Shapes.wireFine, 1)),
    WireProtoHalkonite(
        3,
        "gt.circuitcomponent.wireprotohalkonite",
        () -> MaterialLibAPI.getStack(Materials.protohalkonite, Shapes.wireFine, 1)),
    WireSpacetime(
        4,
        "gt.circuitcomponent.wirespacetime",
        () -> MaterialLibAPI.getStack(Materials.SpaceTime, TEBlockShapes.wireGt01, 1)),
    CableOpticalFiber(
        5,
        "gt.circuitcomponent.cable.opticalfiber",
        () -> CustomItemList.DATApipe.get(1)),
    WireInfinity(
        6,
        "gt.circuitcomponent.wireinfinity",
        () -> MaterialLibAPI.getStack(Materials.Infinity, TEBlockShapes.wireGt01, 1)),
    WireHypogen(
        7,
        "gt.circuitcomponent.wirehypogen",
        () -> MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 1)),
    WireMagMatter(
        8,
        "gt.circuitcomponent.wiremagmatter",
        () -> MaterialLibAPI.getStack(Materials.Magmatter, Shapes.wireFine, 1)),
    WireCopper(
        9,
        "gt.circuitcomponent.wirecopper",
        () -> MaterialLibAPI.getStack(Materials.Copper, Shapes.wireFine, 1)),
    WireRedAlloy(
        10,
        "gt.circuitcomponent.wireredalloy",
        () -> MaterialLibAPI.getStack(Materials.RedAlloy, Shapes.wireFine, 1)),
    WireElectrum(
        11,
        "gt.circuitcomponent.wireelectrum",
        () -> MaterialLibAPI.getStack(Materials.Electrum, Shapes.wireFine, 1)),
    WirePlatinum(
        12,
        "gt.circuitcomponent.wireplatinum",
        () -> MaterialLibAPI.getStack(Materials.Platinum, Shapes.wireFine, 1)),
    WireShijima(
        13,
        "gt.circuitcomponent.wireshijima",
        () -> MaterialLibAPI.getStack(Materials.Shijima, Shapes.wireFine, 1)),
    WireTairitsu(
        14,
        "gt.circuitcomponent.wiretairitsu",
        () -> MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.wireFine, 1)),
    WireChuritsu(
        15,
        "gt.circuitcomponent.wirechuritsu",
        () -> MaterialLibAPI.getStack(Materials.Churitsu, Shapes.wireFine, 1)),
    WireWhiteDwarfMatter(
        16,
        "gt.circuitcomponent.wirewhitedwarfmatter",
        () -> MaterialLibAPI.getStack(Materials.WhiteDwarfMatter, Shapes.wireFine, 1)),
    WireBlackDwarfMatter(
        17,
        "gt.circuitcomponent.wireblackdwarfmatter",
        () -> MaterialLibAPI.getStack(Materials.BlackDwarfMatter, Shapes.wireFine, 1)),
    WireUniversium(
        18,
        "gt.circuitcomponent.wireuniversium",
        () -> MaterialLibAPI.getStack(Materials.Universium, Shapes.wireFine, 1)),

    ProcessedWireNiobiumTitanium(100, "gt.circuitcomponent.processed.wirenbti", () -> WireNiobiumTitanium, true),
    ProcessedWireYttriumBariumCuprate(101, "gt.circuitcomponent.processed.wireybc", () -> WireYttriumBariumCuprate, true),
    ProcessedWireLumiium(102, "gt.circuitcomponent.processed.wirelumiium", () -> WireLumiium, true),
    ProcessedWireProtoHalkonite(103, "gt.circuitcomponent.processed.wireprotohalkonite", () -> WireProtoHalkonite, true),
    ProcessedWireSpacetime(104, "gt.circuitcomponent.processed.wirespacetime", () -> WireSpacetime, true),
    ProcessedCableOpticalFiber(105, "gt.circuitcomponent.cable.processed.opticalfiber", () -> CableOpticalFiber, true),
    ProcessedWireInfinity(106, "gt.circuitcomponent.processed.wireinfinity", () -> WireInfinity, true),
    ProcessedWireHypogen(107, "gt.circuitcomponent.processed.wirehypogen", () -> WireHypogen, true),
    ProcessedWireMagMatter(108, "gt.circuitcomponent.processed.wiremagmatter", () -> WireMagMatter, true),
    ProcessedWireCopper(109, "gt.circuitcomponent.processed.wirecopper", () -> WireCopper, true),
    ProcessedWireRedAlloy(110, "gt.circuitcomponent.processed.wireredalloy", () -> WireRedAlloy, true),
    ProcessedWireElectrum(111, "gt.circuitcomponent.processed.wireelectrum", () -> WireElectrum, true),
    ProcessedWirePlatinum(112, "gt.circuitcomponent.processed.wireplatinum", () -> WirePlatinum, true),

    ProcessedWireShijima(113, "gt.circuitcomponent.processed.wireshijima", () -> WireShijima, true),
    ProcessedWireTairitsu(114, "gt.circuitcomponent.processed.wiretairitsu", () -> WireTairitsu, true),
    ProcessedWireChuritsu(115, "gt.circuitcomponent.processed.wirechuritsu", () -> WireChuritsu, true),

    ProcessedWireWhiteDwarfMatter(116, "gt.circuitcomponent.processed.wirewhitedwarfmatter", () -> WireWhiteDwarfMatter, true),
    ProcessedWireBlackDwarfMatter(117, "gt.circuitcomponent.processed.wireblackdwarfmatter", () -> WireBlackDwarfMatter, true),
    ProcessedWireUniversium(118, "gt.circuitcomponent.processed.wireuniversium", () -> WireUniversium, true),

    // SMDs
    // IDs: 200-299 CC, 300-399 PC
    // 200-209 reserved for normal SMDs, if they are ever needed
    AdvSMDResistor(
        210,
        "gt.circuitcomponent.asmd.resistor",
        () -> ItemList.Circuit_Parts_ResistorASMD.get(1)),
    AdvSMDTransistor(
        211,
        "gt.circuitcomponent.asmd.transistor",
        () -> ItemList.Circuit_Parts_TransistorASMD.get(1)),
    AdvSMDInductor(
        212,
        "gt.circuitcomponent.asmd.inductor",
        () -> ItemList.Circuit_Parts_InductorASMD.get(1)),
    AdvSMDCapacitor(
        213,
        "gt.circuitcomponent.asmd.capacitor",
        () -> ItemList.Circuit_Parts_CapacitorASMD.get(1)),
    AdvSMDDiode(
        214,
        "gt.circuitcomponent.asmd.diode",
        () -> ItemList.Circuit_Parts_DiodeASMD.get(1)),
    OpticalSMDResistor(
        220,
        "gt.circuitcomponent.xsmd.resistor",
        () -> ItemList.Circuit_Parts_ResistorXSMD.get(1)),
    OpticalSMDTransistor(
        221,
        "gt.circuitcomponent.xsmd.transistor",
        () -> ItemList.Circuit_Parts_TransistorXSMD.get(1)),
    OpticalSMDInductor(
        222,
        "gt.circuitcomponent.xsmd.inductor",
        () -> ItemList.Circuit_Parts_InductorXSMD.get(1)),
    OpticalSMDCapacitor(
        223,
        "gt.circuitcomponent.xsmd.capacitor",
        () -> ItemList.Circuit_Parts_CapacitorXSMD.get(1)),
    OpticalSMDDiode(
        224,
        "gt.circuitcomponent.xsmd.diode",
        () -> ItemList.Circuit_Parts_DiodeXSMD.get(1)),

    // 300-309 reserved for normal SMDs, if they are ever needed
    ProcessedAdvSMDResistor(310, "gt.circuitcomponent.processed.asmd.resistor", () -> AdvSMDResistor, true),
    ProcessedAdvSMDTransistor(311, "gt.circuitcomponent.processed.asmd.transistor", () -> AdvSMDTransistor, true),
    ProcessedAdvSMDInductor(312, "gt.circuitcomponent.processed.asmd.inductor", () -> AdvSMDInductor, true),
    ProcessedAdvSMDCapacitor(313, "gt.circuitcomponent.processed.asmd.capacitor", () -> AdvSMDCapacitor, true),
    ProcessedAdvSMDDiode(314, "gt.circuitcomponent.processed.asmd.diode", () -> AdvSMDDiode, true),
    ProcessedOpticalSMDResistor(320, "gt.circuitcomponent.processed.xsmd.resistor", () -> OpticalSMDResistor, true),
    ProcessedOpticalSMDTransistor(321, "gt.circuitcomponent.processed.xsmd.transistor", () -> OpticalSMDTransistor, true),
    ProcessedOpticalSMDInductor(322, "gt.circuitcomponent.processed.xsmd.inductor", () -> OpticalSMDInductor, true),
    ProcessedOpticalSMDCapacitor(323, "gt.circuitcomponent.processed.xsmd.capacitor", () -> OpticalSMDCapacitor, true),
    ProcessedOpticalSMDDiode(324, "gt.circuitcomponent.processed.xsmd.diode", () -> OpticalSMDDiode, true),

    // Boards
    // IDs: 400-499 CC, 500-599 PC
    BoardMultifiberglassElite(
        400,
        "gt.circuitcomponent.board.multifiberelite",
        () -> ItemList.Circuit_Board_Multifiberglass_Elite.get(1)),
    BoardWetwareLifesupport(
        401,
        "gt.circuitcomponent.board.wetwarelifesupport",
        () -> ItemList.Circuit_Board_Wetware_Extreme.get(1)),
    BoardBioMutated(
        402,
        "gt.circuitcomponent.board.biomutated",
        () -> ItemList.Circuit_Board_Bio_Ultra.get(1)),
    BoardOptical(
        403,
        "gt.circuitcomponent.board.optical",
        () -> ItemList.Circuit_Board_Optical.get(1)),
    // id 404 unused
    BioProcessingUnit(
        405,
        "gt.circuitcomponent.bioprocessingunit",
        () -> ItemList.Circuit_Chip_BioCPU.get(1)),
    LivingBioChip(
        406,
        "gt.circuitcomponent.livingbiochip",
        () -> ItemList.Circuit_Parts_Chip_Bioware.get(1)),
    BoardPico(407, "gt.circuitcomponent.processed.board.pico", null, true),
    BoardQuantum(408, "gt.circuitcomponent.processed.board.quantum", null, true),
    BoardPlanck(409, "gt.circuitcomponent.processed.board.planck", null, true),

    ProcessedBoardMultifiberglassElite(500, "gt.circuitcomponent.processed.board.multifiberelite", () -> BoardMultifiberglassElite, true),
    ProcessedBoardWetwareLifesupport(501, "gt.circuitcomponent.processed.board.wetwarelifesupport", () -> BoardWetwareLifesupport, true),
    ProcessedBoardBioMutated(502, "gt.circuitcomponent.processed.board.biomutated", () -> BoardBioMutated, true),
    ProcessedBoardOptical(503, "gt.circuitcomponent.processed.board.optical", () -> BoardOptical, true),
    // id 504 unused
    ProcessedBioProcessingUnit(505, "gt.circuitcomponent.processed.board.bioprocessingunit", () -> BioProcessingUnit, true),
    ProcessedLivingBioChip(506, "gt.circuitcomponent.processed.board.livingbiochip", () -> LivingBioChip, true),
    UnattunedCircuitry(507,"gt.circuitcomponent.unattunedcircuitry", null, true),
    CleansedBoardPico(508, "gt.circuitcomponent.processed.board.cleansedpico", () -> BoardPico, true),
    CleansedBoardQuantum(509, "gt.circuitcomponent.processed.board.cleansedquantum", () -> BoardQuantum, true),
    CleansedBoardPlanck(510, "gt.circuitcomponent.processed.board.cleansedplanck", () -> BoardPlanck, true),

    // Wafers and chips
    // IDs: 600-699 CC, 700-799 PC
    ChipNanoCPU(
        600,
        "gt.circuitcomponent.chipNanoCPU",
        () -> ItemList.Circuit_Chip_NanoCPU.get(1)),
    ChipRAM(
        601,
        "gt.circuitcomponent.chipram",
        () -> ItemList.Circuit_Chip_Ram.get(1)),
    ChipNOR(
        602,
        "gt.circuitcomponent.chipnor",
        () -> ItemList.Circuit_Chip_NOR.get(1)),
    ChipNAND(
        603,
        "gt.circuitcomponent.chipnand",
        () -> ItemList.Circuit_Chip_NAND.get(1)),
    ChipASOC(
        604,
        "gt.circuitcomponent.chipasoc",
        () -> ItemList.Circuit_Chip_SoC2.get(1)),
    // id 605, 606 unused
    ChipAttoPIC(
        607,
        "gt.circuitcomponent.chipattopic",
        () -> ItemList.Circuit_Chip_APIC.get(1)),
    ChipZeptoPIC(
        608,
        "gt.circuitcomponent.chipzeptopic",
        () -> ItemList.Circuit_Chip_ZPIC.get(1)),
    ChipYoctoPIC(
        609,
        "gt.circuitcomponent.chipyoctopic",
        () -> ItemList.Circuit_Chip_YPIC.get(1)),
    ChipPlanckPIC(
        610,
        "gt.circuitcomponent.chipplanckpic",
        () -> ItemList.Circuit_Chip_PlPIC.get(1)),

    ProcessedChipNanoCPU(700, "gt.circuitcomponent.processed.chipnanocpu", () -> ChipNanoCPU, true),
    ProcessedChipRAM(701, "gt.circuitcomponent.processed.chipram", () -> ChipRAM, true),
    ProcessedChipNOR(702, "gt.circuitcomponent.processed.chipnor", () -> ChipNOR, true),
    ProcessedChipNAND(703, "gt.circuitcomponent.processed.chipnand", () -> ChipNAND, true),
    ProcessedChipASOC(704, "gt.circuitcomponent.processed.chipasoc", () -> ChipASOC, true),
    // id 705, 706 unused
    ProcessedChipAttoPIC(707, "gt.circuitcomponent.processed.chipattopic", () -> ChipAttoPIC, true),
    ProcessedChipZeptoPIC(708, "gt.circuitcomponent.processed.chipzeptopic", () -> ChipZeptoPIC, true),
    ProcessedChipYoctoPIC(709, "gt.circuitcomponent.processed.chipyoctopic", () -> ChipYoctoPIC, true),
    ProcessedChipPlanckPIC(710, "gt.circuitcomponent.processed.chipplanckpic", () -> ChipPlanckPIC, true),

    // Crystal components
    // IDs: 800-899 CC, 900-999 PC
    // id 800 unused
    ChipCrystalSoC(
        801,
        "gt.circuitcomponent.chipcrystalsoc",
        () -> ItemList.Circuit_Chip_CrystalSoC.get(1)),
    ChipLivingCrystal(
        802,
        "gt.circuitcomponent.chiplivingcrystal",
        () -> ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(1)),
    ChipRawAdvancedCrystal(
        803,
        "gt.circuitcomponent.chiprawadvancedcrystal",
        () -> ItemList.Circuit_Chip_CrystalSoC2.get(1)),

    // id 900 unused
    ProcessedChipCrystalSoC(901, "gt.circuitcomponent.processed.chipcrystalsoc", () -> ChipCrystalSoC, true),
    ProcessedChipLivingCrystal(902, "gt.circuitcomponent.processed.chiplivingcrystal", () -> ChipLivingCrystal, true),
    ProcessedChipRawAdvancedCrystal(903, "gt.circuitcomponent.processed.chiprawadvancedcrystal", () -> ChipRawAdvancedCrystal, true),

    // Optical components
    // IDs: 1000-1099 CC, 1100-1199 PC
    ChipOpticalCPU(
        1000,
        "gt.circuitcomponent.opticalcpu",
        () -> ItemList.Optically_Perfected_CPU.get(1)),
    OpticalRAM(
        1001,
        "gt.circuitcomponent.opticalram",
        () -> ItemList.Optically_Compatible_Memory.get(1)),

    ProcessedChipOpticalCPU(1100, "gt.circuitcomponent.processed.opticalcpu", () -> ChipOpticalCPU, true),
    ProcessedOpticalRAM(1101, "gt.circuitcomponent.processed.opticalram", () -> OpticalRAM, true),

    // Superconductors
    // IDs: 1200-1299 CC, 1300-1399 PC
    SuperconductorLuV(
        1200,
        "gt.circuitcomponent.superconductorluv",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorLuV, TEBlockShapes.wireGt01, 1)),
    SuperconductorZPM(
        1201,
        "gt.circuitcomponent.superconductorzpm",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorZPM, TEBlockShapes.wireGt01, 1)),
    SuperconductorUV(
        1202,
        "gt.circuitcomponent.superconductoruv",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorUV, TEBlockShapes.wireGt01, 1)),
    SuperconductorUHV(
        1203,
        "gt.circuitcomponent.superconductoruhv",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorUHV, TEBlockShapes.wireGt01, 1)),
    SuperconductorUEV(
        1204,
        "gt.circuitcomponent.superconductoruev",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorUEV, TEBlockShapes.wireGt01, 1)),
    SuperconductorUIV(
        1205,
        "gt.circuitcomponent.superconductoruiv",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorUIV, TEBlockShapes.wireGt01, 1)),
    SuperconductorUMV(
        1206,
        "gt.circuitcomponent.superconductorumv",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorUMV, TEBlockShapes.wireGt01, 1)),

    ProcessedSuperconductorLuV(1300, "gt.circuitcomponent.processed.superconductorluv", () -> SuperconductorLuV, true),
    ProcessedSuperconductorZPM(1301, "gt.circuitcomponent.processed.superconductorzpm", () -> SuperconductorZPM, true),
    ProcessedSuperconductorUV(1302, "gt.circuitcomponent.processed.superconductoruv", () -> SuperconductorUV, true),
    ProcessedSuperconductorUHV(1303, "gt.circuitcomponent.processed.superconductoruhv", () -> SuperconductorUHV, true),
    ProcessedSuperconductorUEV(1304, "gt.circuitcomponent.processed.superconductoruev", () -> SuperconductorUEV, true),
    ProcessedSuperconductorUIV(1305, "gt.circuitcomponent.processed.superconductoruiv", () -> SuperconductorUIV, true),
    ProcessedSuperconductorUMV(1306, "gt.circuitcomponent.processed.superconductorumv", () -> SuperconductorUMV, true),

    // Frameboxes
    // IDs: 1400-1499 CC, 1500-1599 PC
    FrameboxAluminium(
        1400,
        "gt.circuitcomponent.frame.aluminium",
        () -> MaterialLibAPI.getStack(Materials.Aluminium, TEBlockShapes.frameGt, 1)),
    FrameboxTritanium(
        1401,
        "gt.circuitcomponent.frame.tritanium",
        () -> MaterialLibAPI.getStack(Materials.Tritanium, TEBlockShapes.frameGt, 1)),
    FrameboxCelestialTungsten(
        1402,
        "gt.circuitcomponent.frame.celestialtungsten",
        () -> MaterialLibAPI.getStack(Materials.CelestialTungsten, TEBlockShapes.frameGt, 1)),
    FrameboxHypogen(
        1403,
        "gt.circuitcomponent.frame.hypogen",
        () -> MaterialLibAPI.getStack(Materials.Hypogen, TEBlockShapes.frameGt, 1)),
    FrameboxMagMatter(
        1404,
        "gt.circuitcomponent.frame.magmatter",
        () -> MaterialLibAPI.getStack(Materials.Magmatter, TEBlockShapes.frameGt, 1)),

    ProcessedFrameboxAluminium(1500, "gt.circuitcomponent.processed.frame.aluminium", () -> FrameboxAluminium, true),
    ProcessedFrameboxTritanium(1501, "gt.circuitcomponent.processed.frame.tritanium", () -> FrameboxTritanium, true),
    ProcessedFrameboxCelestialTungsten(1502, "gt.circuitcomponent.processed.frame.celestialtungsten", () -> FrameboxCelestialTungsten, true),
    ProcessedFrameboxHypogen(1503, "gt.circuitcomponent.processed.frame.hypogen", () -> FrameboxHypogen, true),
    ProcessedFrameboxMagMatter(1504, "gt.circuitcomponent.processed.frame.magmatter", () -> FrameboxMagMatter, true),

    // Foils
    // IDs: 1600-1699 CC, 1700-1799 PC
    FoilSiliconeRubber(
        1600,
        "gt.circuitcomponent.sheet.siliconerubber",
        () -> MaterialLibAPI.getStack(Materials.Silicone, Shapes.foil, 1)),
    FoilPolybenzimidazole(
        1601,
        "gt.circuitcomponent.sheet.polybenzimidazole",
        () -> MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.foil, 1)),
    FoilRadoxPolymer(
        1602,
        "gt.circuitcomponent.sheet.radoxpolymer",
        () -> MaterialLibAPI.getStack(Materials.RadoxPoly, Shapes.foil, 1)),
    FoilShirabon(
        1603,
        "gt.circuitcomponent.sheet.shirabon",
        () -> MaterialLibAPI.getStack(Materials.Shirabon, Shapes.foil, 1)),
    FoilStyreneRubber(
        1604,
        "gt.circuitcomponent.sheet.styrenebutadienerubber",
        () -> MaterialLibAPI.getStack(Materials.StyreneButadieneRubber, Shapes.foil, 1)),
    FoilEternity(
        1605,
        "gt.circuitcomponent.sheet.eternity",
        () -> MaterialLibAPI.getStack(Materials.Eternity, Shapes.foil, 1)),

    ProcessedFoilSiliconeRubber(1700, "gt.circuitcomponent.sheet.processed.siliconerubber", () -> FoilSiliconeRubber, true),
    ProcessedFoilPolybenzimidazole(1701, "gt.circuitcomponent.sheet.processed.polybenzimidazole", () -> FoilPolybenzimidazole, true),
    ProcessedFoilRadoxPolymer(1702, "gt.circuitcomponent.sheet.processed.radoxpolymer", () -> FoilRadoxPolymer, true),
    ProcessedFoilShirabon(1703, "gt.circuitcomponent.sheet.processed.shirabon", () -> FoilShirabon, true),
    ProcessedFoilStyreneRubber(1704, "gt.circuitcomponent.sheet.processed.styrenerubber", () -> FoilStyreneRubber, true),
    ProcessedFoilEternity(1705, "gt.circuitcomponent.sheet.processed.eternity", () -> FoilEternity, true),

    // Circuit encasement, item combinations, and specialty items
    // IDs: 1800-1899
    ProcessedBasicMainframeCasing(1800, "gt.circuitcomponent.casing.processed.basicmainframe", null, true),
    ProcessedAdvancedMainframeCasing(1801, "gt.circuitcomponent.casing.processed.advancedmainframe", null, true),
    ProcessedPicoCircuitCasing(1802, "gt.circuitcomponent.casing.processed.pico", null, true),
    ProcessedQuantumCircuitCasing(1803, "gt.circuitcomponent.casing.processed.quantum", null, true),
    ProcessedPlanckCircuitCasing(1804, "gt.circuitcomponent.casing.processed.planck", null, true),
    ProcessedCoiledThermalSuperconductor(1805, "gt.circuitcomponent.planck.processed.coiledthermalsuperconductor", null, true),
    ProcessedCircuitOpticalProcessor(1806, "gt.circuitcomponent.circuit.processed.opticalprocessor", CircuitCalibration.OPTICAL, 0.125f),
    ProcessedCircuitOpticalAssembly(1807, "gt.circuitcomponent.circuit.processed.opticalassembly", CircuitCalibration.OPTICAL, 0.25f),
    ProcessedCircuitOpticalComputer(1808, "gt.circuitcomponent.circuit.processed.opticalcomputer", CircuitCalibration.OPTICAL, 0.5f),
    ProcessedCircuitOpticalMainframe(1809, "gt.circuitcomponent.circuit.processed.opticalmainframe", CircuitCalibration.OPTICAL, 1.0f),

    // Pico Chain
    OpticalMainframeRack(1810, "gt.circuitcomponent.pico.processed.opticalmainframerack", null, true),
    PicoCPU(1811, "gt.circuitcomponent.pico.processed.picocpu", null, true),

    // Quantum Chain
    CompressionResidue(1812, "gt.circuitcomponent.quantum.processed.compressionresidue", null, true),
    OrganizedPicoCircuit(1813, "gt.circuitcomponent.quantum.processed.organizedpicocircuit", null, true),
    PicoCircuitRack(1814, "gt.circuitcomponent.quantum.processed.picocircuitrack", null, true),
    QuantumCPU(1815, "gt.circuitcomponent.quantum.processed.quantumcpu", null, true),

    DisorderlySpool(1816, "gt.circuitcomponent.quantum.processed.disorderlyspool", null, true),

    EntangledSuppressionStrands(1817, "gt.circuitcomponent.quantum.processed.positivewirestrands", null, true),
    EntangledAnnihilationStrands(1818, "gt.circuitcomponent.quantum.processed.negativewirestrands", null, true),
    AlienatedStrands(1819, "gt.circuitcomponent.quantum.processed.neutralwirestrands", null, true),

    SilencedStrands(1820, "gt.circuitcomponent.quantum.processed.silencedstrands", null, true),
    SettledStrands(1821, "gt.circuitcomponent.quantum.processed.settledstrands", null, true),

    BundledUnityWire(1822, "gt.circuitcomponent.quantum.processed.bundledunitywire", null, true),

    // Planck Chain
    CollapsingResidue(1823, "gt.circuitcomponent.planck.processed.collapsingresidue", null, true),
    OrganizedQuantumCircuit(1824, "gt.circuitcomponent.planck.processed.organizedquantumcircuit", null, true),
    LostQuantumCircuitRack(1825, "gt.circuitcomponent.planck.processed.lostquantumcircuitrack", () -> ItemList.LostQuantumCircuitRack.get(1)),
    RealizedQuantumCircuitRack(1826, "gt.circuitcomponent.planck.processed.realizedquantumcircuitrack", () -> ItemList.RealizedQuantumCircuitRack.get(1)),
    PlanckCPU(1827, "gt.circuitcomponent.planck.processed.planckcpu", null, true),

    SupermassiveSpool(1828, "gt.circuitcomponent.planck.processed.supermassivespool", null, true),

    MicrocosmicStrands(1829, "gt.circuitcomponent.planck.processed.microcosmstrands", null, true),
    MacrocosmicStrands(1830, "gt.circuitcomponent.planck.processed.macrocosmstrands", () -> ItemList.MacrocosmicStrands.get(1)),
    CosmologicalStrands(1831, "gt.circuitcomponent.planck.processed.cosmologicalstrands", null, true),

    PulsatingWhiteDwarfMatterStrands(1832, "gt.circuitcomponent.planck.processed.pulsatingwdmstrands", () -> ItemList.PulsatingWDMStrands.get(1)),
    UnboundWhiteDwarfMatterStrands(1833, "gt.circuitcomponent.planck.processed.unboundwdmstrands", () -> ItemList.UnboundWDMStrands.get(1)),
    IsolatedBlackDwarfMatterStrands(1834, "gt.circuitcomponent.planck.processed.isolatedbdmstrands", () -> ItemList.IsolatedBDMStrands.get(1)),
    ReboundBlackDwarfMatterStrands(1835, "gt.circuitcomponent.planck.processed.reboundbdmstrands", null, true),

    BundledStellarHarmonyWire(1836, "gt.circuitcomponent.planck.processed.bundledharmony", null, true),

    CalabiYauManifold(1837, "gt.circuitcomponent.planck.processed.calabiyaumanifold", () -> ItemList.CalabiYauManifold.get(1)),

    // Bolts and other small components
    // IDs: 1900-1999 CC, 2000-2099 PC
    BoltEnrichedHolmium(
        1900,
        "gt.circuitcomponent.bolt.enrichedholmium",
        () -> MaterialLibAPI.getStack(Materials.EnrichedHolmium, Shapes.bolt, 1)),
    BoltTranscendentMetal(
        1901,
        "gt.circuitcomponent.bolt.transcendentmetal",
        () -> MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.bolt, 1)),
    BoltYttriumBariumCuprate(
        1902,
        "gt.circuitcomponent.bolt.yttriumbariumcuprate",
        () -> MaterialLibAPI.getStack(Materials.YttriumBariumCuprate, Shapes.bolt, 1)),
    BoltCosmicNeutronium(
        1903,
        "gt.circuitcomponent.bolt.cosmicneutronium",
        () -> MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.bolt, 1)),
    BoltChromaticGlass(
        1904,
        "gt.circuitcomponent.bolt.chromaticglass",
        () -> MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.bolt, 1)),
    BoltUMVSuperconductor(
        1905,
        "gt.circuitcomponent.bolt.umvsuperconductor",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.bolt, 1)),
    PlateMetastableOganesson(
        1906,
        "gt.circuitcomponent.plate.metastableoganesson",
        () -> MaterialLibAPI.getStack(Materials.MetastableOganesson, Shapes.plate, 1)),
    ScrewAstralTitanium(
        1907,
        "gt.circuitcomponent.screw.astraltitanium",
        () -> MaterialLibAPI.getStack(Materials.AstralTitanium, Shapes.screw, 1)),
    CasingUEVSuperconductor(
        1908,
        "gt.circuitcomponent.casing.uevsuperconductor",
        () -> MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, Shapes.itemCasing, 1)),
    ScrewSixPhasedCopper(
        1909,
        "gt.circuitcomponent.screw.sixphasedcopper",
        () -> MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.screw, 1)),
    CasingCreon(
        1910,
        "gt.circuitcomponent.casing.creon",
        () -> MaterialLibAPI.getStack(Materials.Creon, Shapes.itemCasing, 1)),
    PlateRhugnor(
        1911,
        "gt.circuitcomponent.plate.rhugnor",
        () -> MaterialLibAPI.getStack(Materials.Rhugnor, Shapes.plate, 1)),
    BoltWhiteDwarfMatter(
        1912,
        "gt.circuitcomponent.bolt.whitedwarfmatter",
        () -> MaterialLibAPI.getStack(Materials.WhiteDwarfMatter, Shapes.bolt, 1)),
    PlateHexanite(
        1913,
        "gt.circuitcomponent.plate.hexanite",
        () -> MaterialLibAPI.getStack(Materials.Hexanite, Shapes.plate, 1)),
    CasingBlackDwarfMatter(
        1914,
        "gt.circuitcomponent.casing.blackdwarfmatter",
        () -> MaterialLibAPI.getStack(Materials.BlackDwarfMatter, Shapes.itemCasing, 1)),
    CasingEternity(
        1915,
        "gt.circuitcomponent.casing.eternity",
        () -> MaterialLibAPI.getStack(Materials.Eternity, Shapes.itemCasing, 1)),
    ScrewUniversium(
        1916,
        "gt.circuitcomponent.casing.universium",
        () -> MaterialLibAPI.getStack(Materials.Universium, Shapes.screw, 1)),
    ThermalSuperconductor(
        1917,
        "gt.circuitcomponent.thermalsuperconductor",
        () -> ItemList.Thermal_Superconductor.get(1)),
    BoltCopper(
        1918,
        "gt.circuitcomponent.bolt.copper",
        () -> MaterialLibAPI.getStack(Materials.Copper, Shapes.bolt, 1)),
    BoltRedAlloy(
        1919,
        "gt.circuitcomponent.bolt.redalloy",
        () -> MaterialLibAPI.getStack(Materials.RedAlloy, Shapes.bolt, 1)),
    BoltPlatinum(
        1920,
        "gt.circuitcomponent.bolt.platinum",
        () -> MaterialLibAPI.getStack(Materials.Platinum, Shapes.bolt, 1)),
    BoltNiobiumTitanium(
        1921,
        "gt.circuitcomponent.bolt.niobiumtitanium",
        () -> MaterialLibAPI.getStack(Materials.NiobiumTitanium, Shapes.bolt, 1)),
    CasingKevlar(
        1922,
        "gt.circuitcomponent.casing.kevlar",
        () -> MaterialLibAPI.getStack(Materials.Kevlar, Shapes.itemCasing, 1)),
    ScrewEnrichedNaquadahAlloy(
        1923,
        "gt.circuitcomponent.screw.enrichednaquadahalloy",
        () -> MaterialLibAPI.getStack(Materials.EnrichedNaquadahAlloy, Shapes.screw, 1)),
    ScrewQuantum(
        1924,
        "gt.circuitcomponent.screw.quantum",
        () -> MaterialLibAPI.getStack(Materials.Quantum, Shapes.screw, 1)),
    ScrewEternity(
        1925,
        "gt.circuitcomponent.screw.eternity",
        () -> MaterialLibAPI.getStack(Materials.Eternity, Shapes.screw, 1)),
    BoltRhugnor(
        1926,
        "gt.circuitcomponent.bolt.rhugnor",
        () -> MaterialLibAPI.getStack(Materials.Rhugnor, Shapes.bolt, 1)),
    BoltMetastableOganesson(
        1927,
        "gt.circuitcomponent.bolt.metastableoganesson",
        () -> MaterialLibAPI.getStack(Materials.MetastableOganesson, Shapes.bolt, 1)),
    BoltHexanite(
        1928,
        "gt.circuitcomponent.bolt.hexanite",
        () -> MaterialLibAPI.getStack(Materials.Hexanite, Shapes.bolt, 1)),


    ProcessedBoltEnrichedHolmium(2000, "gt.circuitcomponent.bolt.processed.enrichedholmium", () -> BoltEnrichedHolmium, true),
    ProcessedBoltTranscendentMetal(2001, "gt.circuitcomponent.bolt.processed.transcendentmetal", () -> BoltTranscendentMetal, true),
    ProcessedBoltYttriumBariumCuprate(2002, "gt.circuitcomponent.bolt.processed.yttriumbariumcuprate", () -> BoltYttriumBariumCuprate, true),
    ProcessedBoltCosmicNeutronium(2003, "gt.circuitcomponent.bolt.processed.cosmicneutronium", () -> BoltCosmicNeutronium, true),
    ProcessedBoltChromaticGlass(2004, "gt.circuitcomponent.bolt.processed.chromaticglass", () -> BoltChromaticGlass, true),
    ProcessedBoltUMVSuperconductor(2005, "gt.circuitcomponent.bolt.processed.umvsuperconductor", () -> BoltUMVSuperconductor, true),
    ProcessedPlateMetastableOganesson(2006, "gt.circuitcomponent.plate.processed.metastableoganesson", () -> PlateMetastableOganesson, true),
    ProcessedPlateRhugnor(2007, "gt.circuitcomponent.plate.processed.rhugnor", () -> PlateRhugnor, true),
    ProcessedBoltWhiteDwarfMatter(2008, "gt.circuitcomponent.bolt.processed.whitedwarfmatter", () -> BoltWhiteDwarfMatter, true),
    ProcessedPlateHexanite(2009, "gt.circuitcomponent.plate.processed.hexanite", () -> PlateHexanite, true),
    ProcessedBoltCopper(2010, "gt.circuitcomponent.bolt.processed.copper", () -> BoltCopper, true),
    ProcessedBoltRedAlloy(2011, "gt.circuitcomponent.bolt.processed.redalloy", () -> BoltRedAlloy, true),
    ProcessedBoltPlatinum(2012, "gt.circuitcomponent.bolt.processed.platinum", () -> BoltPlatinum, true),
    ProcessedBoltNiobiumTitanium(2013, "gt.circuitcomponent.bolt.processed.niobiumtitanium", () -> BoltNiobiumTitanium, true),

    // Circuits
    // IDs: 2100-2199
    // The first three circuits in a line can be recursively used in the assembly matrix, and all of them can be turned
    // into a physical circuit item
    CrystalProcessor(
        2100,
        "gt.circuitcomponent.crystalprocessor",
        () -> ItemList.Circuit_Crystalprocessor.get(1), CircuitCalibration.CRYSTAL, 0.125f),
    CrystalAssembly(
        2101,
        "gt.circuitcomponent.crystalasssembly",
        () -> ItemList.Circuit_Crystalcomputer.get(1), CircuitCalibration.CRYSTAL, 0.25f),
    CrystalComputer(
        2102,
        "gt.circuitcomponent.crystalcomputer",
        () -> ItemList.Circuit_Ultimatecrystalcomputer.get(1), CircuitCalibration.CRYSTAL, 0.5f),
    CrystalMainframe(
        2103,
        "gt.circuitcomponent.crystalmainframe",
        () -> ItemList.Circuit_Crystalmainframe.get(1), CircuitCalibration.CRYSTAL, 1),
    WetwareProcessor(
        2104,
        "gt.circuitcomponent.wetwareprocessor",
        () -> ItemList.Circuit_Neuroprocessor.get(1), CircuitCalibration.WETWARE, 0.125f),
    WetwareAssembly(
        2105,
        "gt.circuitcomponent.wetwareassembly",
        () -> ItemList.Circuit_Wetwarecomputer.get(1), CircuitCalibration.WETWARE, 0.25f),
    WetwareComputer(
        2106,
        "gt.circuitcomponent.wetwarecomputer",
        () -> ItemList.Circuit_Wetwaresupercomputer.get(1), CircuitCalibration.WETWARE, 0.5f),
    WetwareMainframe(
        2107,
        "gt.circuitcomponent.wetwaremainframe",
        () -> ItemList.Circuit_Wetwaremainframe.get(1), CircuitCalibration.WETWARE, 1),
    BiowareProcessor(
        2108,
        "gt.circuitcomponent.biowareprocessor",
        () -> ItemList.Circuit_Bioprocessor.get(1), CircuitCalibration.BIO, 0.125f),
    BiowareAssembly(
        2109,
        "gt.circuitcomponent.biowareassembly",
        () -> ItemList.Circuit_Biowarecomputer.get(1), CircuitCalibration.BIO, 0.25f),
    BiowareComputer(
        2110,
        "gt.circuitcomponent.biowarecomputer",
        () -> ItemList.Circuit_Biowaresupercomputer.get(1), CircuitCalibration.BIO, 0.5f),
    BiowareMainframe(
        2111,
        "gt.circuitcomponent.biowaremainframe",
        () -> ItemList.Circuit_Biomainframe.get(1), CircuitCalibration.BIO, 1),
    OpticalProcessor(
        2112,
        "gt.circuitcomponent.opticalprocessor",
        () -> ItemList.Circuit_OpticalProcessor.get(1), CircuitCalibration.OPTICAL, 0.125f),
    OpticalAssembly(
        2113,
        "gt.circuitcomponent.opticalassembly",
        () -> ItemList.Circuit_OpticalAssembly.get(1), CircuitCalibration.OPTICAL, 0.25f),
    OpticalComputer(
        2114,
        "gt.circuitcomponent.opticalcomputer",
        () -> ItemList.Circuit_OpticalComputer.get(1), CircuitCalibration.OPTICAL, 0.5f),
    OpticalMainframe(
        2115,
        "gt.circuitcomponent.opticalmainframe",
        () -> ItemList.Circuit_OpticalMainframe.get(1), CircuitCalibration.OPTICAL, 1),
    PicoCircuit(
        2116,
        "gt.circuitcomponent.picocircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "PikoCircuit", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.SPECIAL, 1),
    QuantumCircuit(
        2117,
        "gt.circuitcomponent.quantumcircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "QuantumCircuit", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.SPECIAL, 1),
    PlanckCircuit(
        2118,
        "gt.circuitcomponent.planckcircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "PlanckCircuit", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.SPECIAL, 1),

    RawManifoldBud(
        2119,
        "gt.circuitcomponent.rawmanifoldbud",
        () -> ItemList.RawManifoldBud.get(1), CircuitCalibration.SPECIAL, 0),

    PrimedUnattunedCircuitry(2120, "gt.circuitcomponent.primedunattunedcircuitry", CircuitCalibration.PRIMITIVE, 0.1f),
    NandChip(2121, "gt.circuitcomponent.nandchip", () -> ItemList.NandChip.get(1), CircuitCalibration.PRIMITIVE, 0),
    Microprocessor(2122, "gt.circuitcomponent.microprocessor", () -> ItemList.Circuit_Microprocessor.get(1), CircuitCalibration.PRIMITIVE, 0),
    IntegratedProcessor(2123, "gt.circuitcomponent.integratedprocessor", () -> ItemList.Circuit_Processor.get(1), CircuitCalibration.PRIMITIVE, 0),
    NanoProcessor(2124, "gt.circuitcomponent.nanoprocessor", () -> ItemList.Circuit_Nanoprocessor.get(1), CircuitCalibration.PRIMITIVE, 0),
    QuantumProcessor(2125, "gt.circuitcomponent.quantumprocessor", () -> ItemList.Circuit_Quantumprocessor.get(1), CircuitCalibration.PRIMITIVE, 0),

    ;

    static {
        int opticalSuccessChance = 5000; // 50%
        OpticalProcessor.xorSuccessChance = opticalSuccessChance;
        OpticalProcessor.xorResult = ProcessedCircuitOpticalProcessor;
        OpticalAssembly.xorSuccessChance = opticalSuccessChance;
        OpticalAssembly.xorResult = ProcessedCircuitOpticalAssembly;
        OpticalComputer.xorSuccessChance = opticalSuccessChance;
        OpticalComputer.xorResult = ProcessedCircuitOpticalComputer;
        OpticalMainframe.xorSuccessChance = opticalSuccessChance;
        OpticalMainframe.xorResult = ProcessedCircuitOpticalMainframe;
    }

    // spotless:on

    public static final CircuitComponent[] VALUES = values();
    private static final Int2ObjectMap<CircuitComponent> META_IDS = new Int2ObjectOpenHashMap<>();

    static {
        for (CircuitComponent cc : VALUES) {
            META_IDS.put(cc.metaId, cc);
        }
    }

    public final int metaId;
    public final String nameKey;
    public final String iconString;

    // CC -> real item association
    public final Supplier<ItemStack> realComponent;
    // PC -> CC association
    public final Supplier<CircuitComponent> componentForProcessed;
    public final boolean isProcessed;

    // Tier used for calibration
    public final CircuitCalibration circuitType;
    public float weight = 0;

    public CircuitComponent xorResult;
    public int xorSuccessChance;

    // CC constructor
    CircuitComponent(int id, String nameKey, Supplier<ItemStack> realComponent) {
        this(id, nameKey, realComponent, null, false, CircuitCalibration.NONE);
    }

    // CC constructor with tier, used for circuits

    // 0 - No tier (not a circuit)
    // 1 - Primitive Circuits (Pre-Crystal)
    // 2 - Crystals
    // 3 - Wetware
    // 4 - Bio
    // 5 - Optical
    // 6 - Exotic
    // 7 - Cosmic
    // 8 - Temporally Transcendent
    // 64 - Special Temporary circuits - Piko/Quantum

    CircuitComponent(int id, String nameKey, Supplier<ItemStack> realComponent, CircuitCalibration circuitType,
        float weight) {
        this(id, nameKey, realComponent, null, false, circuitType);
        this.weight = weight;
    }

    // CC constructor for circuits not meant to be taken out of the NAC
    // and instead further used, while counting for calibration
    CircuitComponent(int id, String nameKey, CircuitCalibration circuitType, float weight) {
        this(id, nameKey, null, null, true, circuitType);
        this.weight = weight;
    }

    // PC constructor
    CircuitComponent(int id, String nameKey, Supplier<CircuitComponent> ccSupplier, boolean isProcessed) {
        this(id, nameKey, null, ccSupplier, isProcessed, CircuitCalibration.NONE);
    }

    private static final String PROCESSED_DIRECTORY = "processed/";

    /**
     * Internal constructor
     * images are stored in gregtech/textures/items/gt.circuitcomponent
     * processed components are found in the processed subdirectory
     */
    CircuitComponent(int id, String nameKey, Supplier<ItemStack> realComponent, Supplier<CircuitComponent> ccSupplier,
        boolean isProcessed, CircuitCalibration circuitType) {
        this.metaId = id;
        this.nameKey = nameKey;
        this.realComponent = realComponent;
        this.componentForProcessed = ccSupplier;
        this.isProcessed = isProcessed;
        this.circuitType = circuitType;

        this.iconString = isProcessed ? PROCESSED_DIRECTORY + name().toLowerCase() : name().toLowerCase();
        if (!isProcessed) codechicken.nei.api.API.hideItem(getFakeStack(1));
    }

    public String getLocalizedName() {
        return translateToLocal(nameKey);
    }

    // ItemStack of a fake item, only for display and recipe checking purposes
    public ItemStack getFakeStack(int amount) {
        return new ItemStack(CircuitComponentFakeItem.INSTANCE, amount, this.metaId);
    }

    public static CircuitComponent tryGetFromMetaId(int metaId) {
        return META_IDS.get(metaId);
    }

    public static CircuitComponent tryGetFromFakeStack(ItemStack stack) {
        return tryGetFromMetaId(stack.getItemDamage());
    }

    public static CircuitComponent getFromFakeStackUnsafe(ItemStack stack) {
        // If this throws an IndexOutOfBounds exception, there is a bug
        return tryGetFromMetaId(stack.getItemDamage());
    }

    /**
     * Try to get a real stack representation from a CC fake ItemStack. Will not work for PCs.
     */
    public static ItemStack tryGetRealStack(ItemStack stack) {
        if (stack == null) return null;
        if (stack.getItem() != CircuitComponentFakeItem.INSTANCE) return null;
        CircuitComponent cc = tryGetFromFakeStack(stack);
        if (cc == null || cc.isProcessed || cc.realComponent == null) return null;
        return cc.realComponent.get();
    }

    @SuppressWarnings("unused") // Used in coremod
    public static class CircuitComponentStack {

        private final CircuitComponent circuitComponent;
        private final int size;

        public CircuitComponentStack(CircuitComponent circuitComponent, int size) {
            this.circuitComponent = circuitComponent;
            this.size = size;
        }

        public CircuitComponent getCircuitComponent() {
            return circuitComponent;
        }

        public int getSize() {
            return size;
        }
    }
}
