package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTModHandler.getModItem;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.function.Supplier;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.items.CircuitComponentFakeItem;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.material.MaterialsElements;
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
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 1)),
    WireYttriumBariumCuprate(
        1,
        "gt.circuitcomponent.wireybc",
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.YttriumBariumCuprate, 1)),
    WireLumiium(
        2,
        "gt.circuitcomponent.wirelumiium",
        () -> GGMaterial.lumiium.get(OrePrefixes.wireFine, 1)),
    WireProtoHalkonite(
        3,
        "gt.circuitcomponent.wireprotohalkonite",
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 1)),
    WireSpacetime(
        4,
        "gt.circuitcomponent.wirespacetime",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 1)),
    CableOpticalFiber(
        5,
        "gt.circuitcomponent.cable.opticalfiber",
        () -> CustomItemList.DATApipe.get(1)),
    WireInfinity(
        6,
        "gt.circuitcomponent.wireinfinity",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.Infinity, 1)),
    WireHypogen(
        7,
        "gt.circuitcomponent.wirehypogen",
        () -> MaterialsElements.STANDALONE.HYPOGEN.getFineWire(1)),
    WireMagMatter(
        8,
        "gt.circuitcomponent.wiremagmatter",
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MagMatter, 1)),

    ProcessedWireNiobiumTitanium(100, "gt.circuitcomponent.processed.wirenbti", () -> WireNiobiumTitanium, true),
    ProcessedWireYttriumBariumCuprate(101, "gt.circuitcomponent.processed.wireybc", () -> WireYttriumBariumCuprate, true),
    ProcessedWireLumiium(102, "gt.circuitcomponent.processed.wirelumiium", () -> WireLumiium, true),
    ProcessedWireProtoHalkonite(103, "gt.circuitcomponent.processed.wireprotohalkonite", () -> WireProtoHalkonite, true),
    ProcessedWireSpacetime(104, "gt.circuitcomponent.processed.wirespacetime", () -> WireSpacetime, true),
    ProcessedCableOpticalFiber(105, "gt.circuitcomponent.cable.processed.opticalfiber", () -> CableOpticalFiber, true),
    ProcessedWireInfinity(106, "gt.circuitcomponent.processed.wireinfinity", () -> WireInfinity, true),
    ProcessedWireHypogen(107, "gt.circuitcomponent.processed.wirehypogen", () -> WireHypogen, true),
    ProcessedWireMagMatter(108, "gt.circuitcomponent.processed.wiremagmatter", () -> WireMagMatter, true),

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
    NeuroProcessingUnit(
        404,
        "gt.circuitcomponent.neuroprocessingunit",
        () -> ItemList.Circuit_Chip_NeuroCPU.get(1)),
    BioProcessingUnit(
        405,
        "gt.circuitcomponent.bioprocessingunit",
        () -> ItemList.Circuit_Chip_BioCPU.get(1)),
    LivingBioChip(
        406,
        "gt.circuitcomponent.livingbiochip",
        () -> ItemList.Circuit_Parts_Chip_Bioware.get(1)),

    ProcessedBoardMultifiberglassElite(500, "gt.circuitcomponent.processed.board.multifiberelite", () -> BoardMultifiberglassElite, true),
    ProcessedBoardWetwareLifesupport(501, "gt.circuitcomponent.processed.board.wetwarelifesupport", () -> BoardWetwareLifesupport, true),
    ProcessedBoardBioMutated(502, "gt.circuitcomponent.processed.board.biomutated", () -> BoardBioMutated, true),
    ProcessedBoardOptical(503, "gt.circuitcomponent.processed.board.optical", () -> BoardOptical, true),
    ProcessedNeuroProcessingUnit(504, "gt.circuitcomponent.processed.neuroprocessingunit", () -> NeuroProcessingUnit, true),
    ProcessedBioProcessingUnit(505, "gt.circuitcomponent.processed.board.bioprocessingunit", () -> BioProcessingUnit, true),
    ProcessedLivingBioChip(506, "gt.circuitcomponent.processed.board.livingbiochip", () -> LivingBioChip, true),

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
    ChipPikoPIC(
        605,
        "gt.circuitcomponent.chippikopic",
        () -> ItemList.Circuit_Chip_PPIC.get(1)),
    ChipQuantumPIC(
        606,
        "gt.circuitcomponent.chipquantumpic",
        () -> ItemList.Circuit_Chip_QPIC.get(1)),
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
    ProcessedChipPikoPIC(705, "gt.circuitcomponent.processed.chippikopic", () -> ChipPikoPIC, true),
    ProcessedChipQuantumPIC(706, "gt.circuitcomponent.processed.chipquantumpic", () -> ChipQuantumPIC, true),
    ProcessedChipAttoPIC(707, "gt.circuitcomponent.processed.chipattopic", () -> ChipAttoPIC, true),
    ProcessedChipZeptoPIC(708, "gt.circuitcomponent.processed.chipzeptopic", () -> ChipZeptoPIC, true),
    ProcessedChipYoctoPIC(709, "gt.circuitcomponent.processed.chipyoctopic", () -> ChipYoctoPIC, true),
    ProcessedChipPlanckPIC(710, "gt.circuitcomponent.processed.chipplanckpic", () -> ChipPlanckPIC, true),

    // Crystal components
    // IDs: 800-899 CC, 900-999 PC
    ChipCrystalCPU(
        800,
        "gt.circuitcomponent.chipcrystalcpu",
        () -> ItemList.Circuit_Chip_CrystalCPU.get(1)),
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

    ProcessedChipCrystalCPU(900, "gt.circuitcomponent.processed.chipcrystalcpu", () -> ChipCrystalCPU, true),
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
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 1)),
    SuperconductorZPM(
        1201,
        "gt.circuitcomponent.superconductorzpm",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 1)),
    SuperconductorUV(
        1202,
        "gt.circuitcomponent.superconductoruv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1)),
    SuperconductorUHV(
        1203,
        "gt.circuitcomponent.superconductoruhv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1)),
    SuperconductorUEV(
        1204,
        "gt.circuitcomponent.superconductoruev",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1)),
    SuperconductorUIV(
        1205,
        "gt.circuitcomponent.superconductoruiv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1)),
    SuperconductorUMV(
        1206,
        "gt.circuitcomponent.superconductorumv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 1)),

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
        () -> GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1)),
    FrameboxTritanium(
        1401,
        "gt.circuitcomponent.frame.tritanium",
        () -> GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 1)),
    FrameboxCelestialTungsten(
        1402,
        "gt.circuitcomponent.frame.celestialtungsten",
        () -> MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFrameBox(1)),
    FrameboxHypogen(
        1403,
        "gt.circuitcomponent.frame.hypogen",
        () -> MaterialsElements.STANDALONE.HYPOGEN.getFrameBox(1)),
    FrameboxMagMatter(
        1404,
        "gt.circuitcomponent.frame.magmatter",
        () -> GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MagMatter, 1)),

    ProcessedFrameboxAluminium(1500, "gt.circuitcomponent.processed.frame.aluminium", () -> FrameboxAluminium, true),
    ProcessedFrameboxTritanium(1501, "gt.circuitcomponent.processed.frame.tritanium", () -> FrameboxTritanium, true),
    ProcessedFrameboxCelestialTungsten(1502, "gt.circuitcomponent.processed.frame.celestialtungsten", () -> FrameboxCelestialTungsten, true),
    ProcessedFrameboxHypogen(1503, "gt.circuitcomponent.processed.frame.hypogen", () -> FrameboxHypogen, true),
    ProcessedFrameboxMagMatter(1504,"gt.circuitcomponent.processed.frame.magmatter", () -> FrameboxMagMatter, true),

    // Foils
    // IDs: 1600-1699 CC, 1700-1799 PC
    FoilSiliconeRubber(
        1600,
        "gt.circuitcomponent.sheet.siliconerubber",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.RubberSilicone, 1)),
    FoilPolybenzimidazole(
        1601,
        "gt.circuitcomponent.sheet.polybenzimidazole",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 1)),
    FoilRadoxPolymer(
        1602,
        "gt.circuitcomponent.sheet.radoxpolymer",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.RadoxPolymer, 1)),
    FoilShirabon(
        1603,
        "gt.circuitcomponent.sheet.shirabon",
        () -> GGMaterial.shirabon.get(OrePrefixes.foil, 1)),
    FoilStyreneRubber(
        1604,
        "gt.circuitcomponent.sheet.styrenebutadienerubber",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.StyreneButadieneRubber, 1)),
    FoilEternity(
        1605,
        "gt.circuitcomponent.sheet.eternity",
        () -> GTOreDictUnificator.get(OrePrefixes.foil, Materials.Eternity, 1)),

    ProcessedFoilSiliconeRubber(1700, "gt.circuitcomponent.sheet.processed.siliconerubber", () -> FoilSiliconeRubber, true),
    ProcessedFoilPolybenzimidazole(1701, "gt.circuitcomponent.sheet.processed.polybenzimidazole", () -> FoilPolybenzimidazole, true),
    ProcessedFoilRadoxPolymer(1702, "gt.circuitcomponent.sheet.processed.radoxpolymer", () -> FoilRadoxPolymer, true),
    ProcessedFoilShirabon(1703, "gt.circuitcomponent.sheet.processed.shirabon", () -> FoilShirabon, true),
    ProcessedFoilStyreneRubber(1704, "gt.circuitcomponent.sheet.processed.styrenerubber", () -> FoilStyreneRubber, true),
    ProcessedFoilEternity(1705, "gt.circuitcomponent.sheet.processed.eternity", () -> FoilEternity, true),
    // Circuit encasement and item combinations
    // IDs: 1800-1899
    ProcessedBasicMainframeCasing(1800, "gt.circuitcomponent.casing.processed.basicmainframe", null, true),
    ProcessedAdvancedMainframeCasing(1801, "gt.circuitcomponent.casing.processed.advancedmainframe", null, true),
    ProcessedPicoCircuitCasing(1802, "gt.circuitcomponent.casing.processed.pico", null, true),
    ProcessedQuantumCircuitCasing(1803, "gt.circuitcomponent.casing.processed.quantum", null, true),
    ProcessedPlanckCircuitCasing(1804, "gt.circuitcomponent.casing.processed.planck", null, true),
    ProcessedCoiledThermalSuperconductor(1805, "gt.circuitcomponent.other.processed.coiledthermalsuperconductor", null, true),

    // Bolts and other small components
    // IDs: 1900-1999 CC, 2000-2099 PC
    BoltEnrichedHolmium(
        1900,
        "gt.circuitcomponent.bolt.enrichedholmium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 1)),
    BoltTranscendentMetal(
        1901,
        "gt.circuitcomponent.bolt.transcendentmetal",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.TranscendentMetal, 1)),
    BoltYttriumBariumCuprate(
        1902,
        "gt.circuitcomponent.bolt.yttriumbariumcuprate",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.YttriumBariumCuprate, 1)),
    BoltCosmicNeutronium(
        1903,
        "gt.circuitcomponent.bolt.cosmicneutronium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.CosmicNeutronium, 1)),
    BoltChromaticGlass(
        1904,
        "gt.circuitcomponent.bolt.chromaticglass",
        () -> MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getBolt(1)),
    BoltUMVSuperconductor(
        1905,
        "gt.circuitcomponent.bolt.umvsuperconductor",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.SuperconductorUMVBase, 1)),
    PlateMetastableOganesson(
        1906,
        "gt.circuitcomponent.plate.metastableoganesson",
        () -> GGMaterial.metastableOganesson.get(OrePrefixes.plate, 1)),
    ScrewAstralTitanium(
        1907,
        "gt.circuitcomponent.screw.astraltitanium",
        () -> MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getScrew(1)),
    CasingUEVSuperconductor(
        1908,
        "gt.circuitcomponent.casing.uevsuperconductor",
        () -> GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.SuperconductorUEVBase, 1)),
    ScrewSixPhasedCopper(
        1909,
        "gt.circuitcomponent.screw.sixphasedcopper",
        () -> GTOreDictUnificator.get(OrePrefixes.screw, Materials.SixPhasedCopper, 1)),
    CasingCreon(
        1910,
        "gt.circuitcomponent.casing.creon",
        () -> GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Creon, 1)),
    PlateRhugnor(
        1911,
        "gt.circuitcomponent.plate.rhugnor",
        () -> MaterialsElements.STANDALONE.RHUGNOR.getPlate(1)),
    BoltWhiteDwarfMatter(
        1912,
        "gt.circuitcomponent.bolt.whitedwarfmatter",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.WhiteDwarfMatter, 1)),
    PlateHexanite(
        1913,
        "gt.circuitcomponent.plate.hexanite",
        () -> GTOreDictUnificator.get(OrePrefixes.plate, Materials.Hexanite, 1)),
    CasingBlackDwarfMatter(
        1914,
        "gt.circuitcomponent.casing.blackdwarfmatter",
        () -> GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.BlackDwarfMatter, 1)),
    CasingEternity(
        1915,
        "gt.circuitcomponent.casing.eternity",
        () -> GTOreDictUnificator.get(OrePrefixes.itemCasing, Materials.Eternity, 1)),
    ScrewUniversium(
        1916,
        "gt.circuitcomponent.casing.universium",
        () -> GTOreDictUnificator.get(OrePrefixes.screw, Materials.Universium, 1)),
    ThermalSuperconductor(
        1917,
        "gt.circuitcomponent.thermalsuperconductor",
        () -> ItemList.Thermal_Superconductor.get(1)),

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

    // Circuits
    // IDs: 2100-2199
    // The first three circuits in a line can be recursively used in the assembly matrix, and all of them can be turned
    // into a physical circuit item
    CrystalProcessor(
        2100,
        "gt.circuitcomponent.crystalprocessor",
        () -> ItemList.Circuit_Crystalprocessor.get(1), CircuitCalibration.CRYSTAL),
    CrystalAssembly(
        2101,
        "gt.circuitcomponent.crystalasssembly",
        () -> ItemList.Circuit_Crystalcomputer.get(1), CircuitCalibration.CRYSTAL),
    CrystalComputer(
        2102,
        "gt.circuitcomponent.crystalcomputer",
        () -> ItemList.Circuit_Ultimatecrystalcomputer.get(1), CircuitCalibration.CRYSTAL),
    CrystalMainframe(
        2103,
        "gt.circuitcomponent.crystalmainframe",
        () -> ItemList.Circuit_Crystalmainframe.get(1), CircuitCalibration.CRYSTAL),
    WetwareProcessor(
        2104,
        "gt.circuitcomponent.wetwareprocessor",
        () -> ItemList.Circuit_Neuroprocessor.get(1), CircuitCalibration.WETWARE),
    WetwareAssembly(
        2105,
        "gt.circuitcomponent.wetwareassembly",
        () -> ItemList.Circuit_Wetwarecomputer.get(1), CircuitCalibration.WETWARE),
    WetwareComputer(
        2106,
        "gt.circuitcomponent.wetwarecomputer",
        () -> ItemList.Circuit_Wetwaresupercomputer.get(1), CircuitCalibration.WETWARE),
    WetwareMainframe(
        2107,
        "gt.circuitcomponent.wetwaremainframe",
        () -> ItemList.Circuit_Wetwaremainframe.get(1), CircuitCalibration.WETWARE),
    BiowareProcessor(
        2108,
        "gt.circuitcomponent.biowareprocessor",
        () -> ItemList.Circuit_Bioprocessor.get(1), CircuitCalibration.BIO),
    BiowareAssembly(
        2109,
        "gt.circuitcomponent.biowareassembly",
        () -> ItemList.Circuit_Biowarecomputer.get(1), CircuitCalibration.BIO),
    BiowareComputer(
        2110,
        "gt.circuitcomponent.biowarecomputer",
        () -> ItemList.Circuit_Biowaresupercomputer.get(1), CircuitCalibration.BIO),
    BiowareMainframe(
        2111,
        "gt.circuitcomponent.biowaremainframe",
        () -> ItemList.Circuit_Biomainframe.get(1), CircuitCalibration.BIO),
    OpticalProcessor(
        2112,
        "gt.circuitcomponent.opticalprocessor",
        () -> ItemList.Circuit_OpticalProcessor.get(1), CircuitCalibration.OPTICAL),
    OpticalAssembly(
        2113,
        "gt.circuitcomponent.opticalassembly",
        () -> ItemList.Circuit_OpticalAssembly.get(1), CircuitCalibration.OPTICAL),
    OpticalComputer(
        2114,
        "gt.circuitcomponent.opticalcomputer",
        () -> ItemList.Circuit_OpticalComputer.get(1), CircuitCalibration.OPTICAL),
    OpticalMainframe(
        2115,
        "gt.circuitcomponent.opticalmainframe",
        () -> ItemList.Circuit_OpticalMainframe.get(1), CircuitCalibration.OPTICAL),
    PicoCircuit(
        2116,
        "gt.circuitcomponent.picocircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "PikoCircuit", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.SPECIAL),
    QuantumCircuit(
        2117,
        "gt.circuitcomponent.quantumcircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "QuantumCircuit", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.SPECIAL),
    PlanckCircuit(
        2118,
        "gt.circuitcomponent.planckcircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "PlanckCircuit", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.SPECIAL),
    PlanckManifold(
        2119,
        "gt.circuitcomponent.planckmanifold",
        () -> ItemList.Planck_Manifold.get(1), CircuitCalibration.NONE),
    CircuitArrayULV(
        2120,
        "gt.circuitcomponent.circuitarrayULV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayULV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayLV(
        2121,
        "gt.circuitcomponent.circuitarrayLV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayLV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayMV(
        2122,
        "gt.circuitcomponent.circuitarrayMV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayMV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayHV(
        2123,
        "gt.circuitcomponent.circuitarrayHV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayHV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayEV(
        2124,
        "gt.circuitcomponent.circuitarrayEV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayEV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayIV(
        2125,
        "gt.circuitcomponent.circuitarrayIV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayIV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayLuV(
        2126,
        "gt.circuitcomponent.circuitarrayLuV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayLuV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayZPM(
        2127,
        "gt.circuitcomponent.circuitarrayZPM",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayZPM", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayUV(
        2128,
        "gt.circuitcomponent.circuitarrayUV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayUV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayUHV(
        2129,
        "gt.circuitcomponent.circuitarrayUHV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayUHV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayUEV(
        2130,
        "gt.circuitcomponent.circuitarrayUEV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayUEV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayUIV(
        2131,
        "gt.circuitcomponent.circuitarrayUIV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayUIV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayUMV(
        2132,
        "gt.circuitcomponent.circuitarrayUMV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayUMV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayUXV(
        2133,
        "gt.circuitcomponent.circuitarrayUXV",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayUXV", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),
    CircuitArrayMAX(
        2134,
        "gt.circuitcomponent.circuitarrayMAX",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayMAX", 1, 0, new ItemStack(Blocks.fire)), CircuitCalibration.NONE),

    // Circuit Array Parts (ONLY CC)
    // IDs: 2200-2399
    CircuitArrayEmpty(
        2200,
        "gt.arrayparts.circuitarrayEmpty",
        () -> getModItem(NewHorizonsCoreMod.ID, "CircuitArrayEmpty", 1, 0)),
    NANDChip(
        2201,
        "gt.arrayparts.nandchip",
        () -> ItemList.NandChip.get(1)),
    Microprocessor(
        2202,
        "gt.arrayparts.microprocessor",
        () -> ItemList.Circuit_Microprocessor.get(1)),
    IntegratedProcessor(
        2203,
        "gt.arrayparts.integratedprocessor",
        () -> ItemList.Circuit_Processor.get(1)),
    NanoProcessor(
        2204,
        "gt.arrayparts.nanoprocessor",
        () -> ItemList.Circuit_Nanoprocessor.get(1)),
    QuantumProcessor(
        2205,
        "gt.arrayparts.quantumprocessor",
        () -> ItemList.Circuit_Quantumprocessor.get(1)),

    //Long Rods
    LongRodULV(
        2206,
        "gt.arrayparts.longrodULV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.CastIron, 1)),
    LongRodLV(
        2207,
        "gt.arrayparts.longrodLV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Steel, 1)),
    LongRodMV(
        2208,
        "gt.arrayparts.longrodMV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Aluminium, 1)),
    LongRodHV(
        2209,
        "gt.arrayparts.longrodHV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.StainlessSteel, 1)),
    LongRodEV(
        2210,
        "gt.arrayparts.longrodEV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Titanium, 1)),
    LongRodIV(
        2211,
        "gt.arrayparts.longrodIV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TungstenSteel, 1)),
    LongRodLuV(
        2212,
        "gt.arrayparts.longrodLuV",
        () -> WerkstoffLoader.RhodiumPlatedPalladium.get(OrePrefixes.stickLong, 1)),
    LongRodZPM(
        2213,
        "gt.arrayparts.longrodZPM",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Iridium, 1)),
    LongRodUV(
        2214,
        "gt.arrayparts.longrodUV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmium, 1)),
    LongRodUHV(
        2215,
        "gt.arrayparts.longrodUHV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 1)),
    LongRodUEV(
        2216,
        "gt.arrayparts.longrodUEV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Bedrockium, 1)),
    LongRodUIV(
        2217,
        "gt.arrayparts.longrodUIV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.BlackPlutonium, 1)),
    LongRodUMV(
        2218,
        "gt.arrayparts.longrodUMV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 1)),
    LongRodUXV(
        2219,
        "gt.arrayparts.longrodUXV",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.MHDCSM, 1)),
    LongRodMAX(
        2220,
        "gt.arrayparts.longrodMAX",
        () -> GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.MagMatter, 1)),

    // Superdense Plates
    SDRedAlloy(
        2221,
        "gt.arrayparts.sdredalloy",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.RedAlloy, 1)),
    SDCopper(
        2222,
        "gt.arrayparts.sdcopper",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Copper, 1)),
    SDAnnealedCopper(
        2223,
        "gt.arrayparts.sdannealedcopper",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.AnnealedCopper, 1)),
    SDElectrum(
        2224,
        "gt.arrayparts.sdelectrum",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Electrum, 1)),
    SDPlatinum(
        2225,
        "gt.arrayparts.sdplatinum",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Platinum, 1)),
    SDNiobiumTitanium(
        2226,
        "gt.arrayparts.sdniobiumtitanium",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NiobiumTitanium, 1)),
    SDYBCO(
        2227,
        "gt.arrayparts.sdybco",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.YttriumBariumCuprate, 1)),
    SDCosmicNeutronium(
        2228,
        "gt.arrayparts.sdcosmicneutronium",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 1)),
    SDChromaticGlass(
        2229,
        "gt.arrayparts.sdchromaticglass",
        () -> MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlateSuperdense(1)),
    SDAluminium(
        2230,
        "gt.arrayparts.sdaluminium",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Aluminium, 1)),
    SDEnrichedNaq(
        2231,
        "gt.arrayparts.sdenrichednaq",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.NaquadahEnriched, 1)),
    SDFluxedElectrum(
        2232,
        "gt.arrayparts.sdfluxedelectrum",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.ElectrumFlux, 1)),
    SDTritanium(
        2233,
        "gt.arrayparts.sdtritanium",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Tritanium, 1)),
    SDLuVSCB(
        2234,
        "gt.arrayparts.sdluvscb",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorLuVBase, 1)),
    SDUVSCB(
        2235,
        "gt.arrayparts.sduvscb",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUVBase, 1)),
    SDUHVSCB(
        2236,
        "gt.arrayparts.sduhvscb",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUHVBase, 1)),
    SDUEVSCB(
        2237,
        "gt.arrayparts.sduevscb",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUEVBase, 1)),
    SDTranscendentMetal(
        2238,
        "gt.arrayparts.sdtranscendentmetal",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, 1)),
    SDRhugnor(
        2239,
        "gt.arrayparts.sdrhugnor",
        () -> MaterialsElements.STANDALONE.RHUGNOR.getPlateSuperdense(1)),
    SDUMVSCB(
        2240,
        "gt.arrayparts.sdumvscb",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SuperconductorUMVBase, 1)),
    SDMetastableOg(
        2241,
        "gt.arrayparts.sdmetastableog",
        () -> GGMaterial.metastableOganesson.get(OrePrefixes.plateSuperdense, 1)),
    SDWhiteDwarf(
        2242,
        "gt.arrayparts.sdwhitedwarf",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.WhiteDwarfMatter, 1)),
    SDHexanite(
        2243,
        "gt.arrayparts.sdhexanite",
        () -> GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Hexanite, 1)),

    //Cables
    CableULV(
        2244,
        "gt.arrayparts.cableulv",
        () -> GTOreDictUnificator.get(OrePrefixes.cableGt16, Materials.RedAlloy, 1)),
    CableLV(
        2245,
        "gt.arrayparts.cablelv",
        () -> GTOreDictUnificator.get(OrePrefixes.cableGt16, Materials.RedstoneAlloy, 1)),
    CableMV(
        2246,
        "gt.arrayparts.cablemv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorMV, 1)),
    CableHV(
        2247,
        "gt.arrayparts.cablehv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorHV, 1)),
    CableEV(
        2248,
        "gt.arrayparts.cableev",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorEV, 1)),
    CableIV(
        2249,
        "gt.arrayparts.cableiv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorIV, 1)),
    CableLuV(
        2250,
        "gt.arrayparts.cableluv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorLuV, 1)),
    CableZPM(
        2251,
        "gt.arrayparts.cablezpm",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorZPM, 1)),
    CableUV(
        2252,
        "gt.arrayparts.cableuv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUV, 1)),
    CableUHV(
        2253,
        "gt.arrayparts.cableuhv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUHV, 1)),
    CableUEV(
        2254,
        "gt.arrayparts.cableuev",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 1)),
    CableUIV(
        2255,
        "gt.arrayparts.cableuiv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 1)),
    CableUMV(
        2256,
        "gt.arrayparts.cableumv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 1)),
    CableUXV(
        2257,
        "gt.arrayparts.cableuxv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Infinity, 1)),
    CableMAX(
        2258,
        "gt.arrayparts.cablemax",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SpaceTime, 1)),

    // Huge Pipes
    PipeULV(
        2259,
        "gt.arrayparts.pipeulv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Lead, 1)),
    PipeLV(
        2260,
        "gt.arrayparts.pipelv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Steel, 1)),
    PipeMV(
        2261,
        "gt.arrayparts.pipemv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.StainlessSteel, 1)),
    PipeHV(
        2262,
        "gt.arrayparts.pipehv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Titanium, 1)),
    PipeEV(
        2263,
        "gt.arrayparts.pipeev",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.TungstenSteel, 1)),
    PipeIV(
        2264,
        "gt.arrayparts.pipeiv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.NiobiumTitanium, 1)),
    PipeLuV(
        2265,
        "gt.arrayparts.pipeluv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Enderium, 1)),
    PipeZPM(
        2266,
        "gt.arrayparts.pipezpm",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Naquadah, 1)),
    PipeUV(
        2267,
        "gt.arrayparts.pipeuv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Neutronium, 1)),
    PipeUHV(
        2268,
        "gt.arrayparts.pipeuhv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Bedrockium, 1)),
    PipeUEV(
        2269,
        "gt.arrayparts.pipeuev",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Infinity, 1)),
    PipeUIV(
        2270,
        "gt.arrayparts.pipeuiv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.TranscendentMetal, 1)),
    PipeUMV(
        2271,
        "gt.arrayparts.pipeumv",
        () -> GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.SpaceTime, 1)),
    PipeUXV(
        2272,
        "gt.arrayparts.pipeuxv",
        () -> CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(1)),
    PipeMAX(
        2273,
        "gt.arrayparts.pipemax",
        () -> ItemList.SuperconductivePlasmaEnergyConduit.get(1)),

    // Dynamos
    DynamoULV(2274, "gt.arrayparts.dynamoulv", () -> ItemList.Hatch_Dynamo_ULV.get(1)),
    DynamoLV(2275, "gt.arrayparts.dynamolv", () -> ItemList.Hatch_Dynamo_LV.get(1)),
    DynamoMV(2276, "gt.arrayparts.dynamomv", () -> ItemList.Hatch_Dynamo_MV.get(1)),
    DynamoHV(2277, "gt.arrayparts.dynamohv", () -> ItemList.Hatch_Dynamo_HV.get(1)),
    DynamoEV(2278, "gt.arrayparts.dynamoev", () -> ItemList.Hatch_Dynamo_EV.get(1)),
    DynamoIV(2279, "gt.arrayparts.dynamoiv", () -> ItemList.Hatch_Dynamo_IV.get(1)),
    DynamoLuV(2280, "gt.arrayparts.dynamoluv", () -> ItemList.Hatch_Dynamo_LuV.get(1)),
    DynamoZPM(2281, "gt.arrayparts.dynamozpm", () -> ItemList.Hatch_Dynamo_ZPM.get(1)),
    DynamoUV(2282, "gt.arrayparts.dynamouuv", () -> ItemList.Hatch_Dynamo_UV.get(1)),
    DynamoUHV(2283, "gt.arrayparts.dynamouhv", () -> ItemList.Hatch_Dynamo_UHV.get(1)),
    DynamoUEV(2284, "gt.arrayparts.dynamouev", () -> ItemList.Hatch_Dynamo_UEV.get(1)),
    DynamoUIV(2285, "gt.arrayparts.dynamouiv", () -> ItemList.Hatch_Dynamo_UIV.get(1)),
    DynamoUMV(2287, "gt.arrayparts.dynamoumv", () -> ItemList.Hatch_Dynamo_UMV.get(1)),
    DynamoUXV(2288, "gt.arrayparts.dynamouxv", () -> ItemList.Hatch_Dynamo_UXV.get(1)),
    DynamoMAX(2289, "gt.arrayparts.dynamomax", () -> CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1)),

    // Energy Hatches
    EnergyULV(2290, "gt.arrayparts.energyulv", () -> ItemList.Hatch_Energy_ULV.get(1)),
    EnergyLV(2291, "gt.arrayparts.energylv", () -> ItemList.Hatch_Energy_LV.get(1)),
    EnergyMV(2292, "gt.arrayparts.energymv", () -> ItemList.Hatch_Energy_MV.get(1)),
    EnergyHV(2293, "gt.arrayparts.energyhv", () -> ItemList.Hatch_Energy_HV.get(1)),
    EnergyEV(2294, "gt.arrayparts.energyev", () -> ItemList.Hatch_Energy_EV.get(1)),
    EnergyIV(2295, "gt.arrayparts.energyiv", () -> ItemList.Hatch_Energy_IV.get(1)),
    EnergyLuV(2296, "gt.arrayparts.energyluv", () -> ItemList.Hatch_Energy_LuV.get(1)),
    EnergyZPM(2297, "gt.arrayparts.energyzpm", () -> ItemList.Hatch_Energy_ZPM.get(1)),
    EnergyUV(2298, "gt.arrayparts.energyuv", () -> ItemList.Hatch_Energy_UV.get(1)),
    EnergyUHV(2299, "gt.arrayparts.energyuhv", () -> ItemList.Hatch_Energy_UHV.get(1)),
    EnergyUEV(2300, "gt.arrayparts.energyuev", () -> ItemList.Hatch_Energy_UEV.get(1)),
    EnergyUIV(2301, "gt.arrayparts.energyuiv", () -> ItemList.Hatch_Energy_UIV.get(1)),
    EnergyUMV(2302, "gt.arrayparts.energyumv", () -> ItemList.Hatch_Energy_UMV.get(1)),
    EnergyUXV(2303, "gt.arrayparts.energyuxv", () -> ItemList.Hatch_Energy_UXV.get(1)),
    EnergyMAX(2304, "gt.arrayparts.energymax", () -> CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1)),

    // Power Transformers
    TransformerULV(2305, "gt.arrayparts.transformerulv", () -> ItemList.WetTransformer_LV_ULV.get(1)),
    TransformerLV(2306, "gt.arrayparts.transformerlv", () -> ItemList.WetTransformer_MV_LV.get(1)),
    TransformerMV(2307, "gt.arrayparts.transformermv", () -> ItemList.WetTransformer_HV_MV.get(1)),
    TransformerHV(2308, "gt.arrayparts.transformerhv", () -> ItemList.WetTransformer_EV_HV.get(1)),
    TransformerEV(2309, "gt.arrayparts.transformerev", () -> ItemList.WetTransformer_IV_EV.get(1)),
    TransformerIV(2310, "gt.arrayparts.transformeriv", () -> ItemList.WetTransformer_LuV_IV.get(1)),
    TransformerLuV(2311, "gt.arrayparts.transformerluv", () -> ItemList.WetTransformer_ZPM_LuV.get(1)),
    TransformerZPM(2312, "gt.arrayparts.transformerzpm", () -> ItemList.WetTransformer_UV_ZPM.get(1)),
    TransformerUV(2313, "gt.arrayparts.transformeruv", () -> ItemList.WetTransformer_UHV_UV.get(1)),
    TransformerUHV(2314, "gt.arrayparts.transformeruhv", () -> ItemList.WetTransformer_UEV_UHV.get(1)),
    TransformerUEV(2315, "gt.arrayparts.transformeruev", () -> ItemList.WetTransformer_UIV_UEV.get(1)),
    TransformerUIV(2316, "gt.arrayparts.transformeruiv", () -> ItemList.WetTransformer_UMV_UIV.get(1)),
    TransformerUMV(2317, "gt.arrayparts.transformerumv", () -> ItemList.WetTransformer_UXV_UMV.get(1)),
    TransformerUXV(2318, "gt.arrayparts.transformeruxv", () -> ItemList.WetTransformer_MAX_UXV.get(1)),
    TransformerMAX(2319, "gt.arrayparts.transformermax", () -> CustomItemList.StabilisationFieldGeneratorTier8.get(1)),
    ;

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

    CircuitComponent(int id, String nameKey, Supplier<ItemStack> realComponent, CircuitCalibration circuitType) {
        this(id, nameKey, realComponent, null, false, circuitType);
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
        codechicken.nei.api.API.hideItem(getFakeStack(1));
    }

    public String getLocalizedName() {
        return translateToLocal(nameKey);
    }

    // ItemStack of a fake item, only for display and recipe checking purposes
    public ItemStack getFakeStack(int amount) {
        return new ItemStack(CircuitComponentFakeItem.INSTANCE, amount, this.metaId);
    }

    public static CircuitComponent tryGetFromFakeStack(ItemStack stack) {
        if (!META_IDS.containsKey(stack.getItemDamage())) return null;
        return getFromFakeStackUnsafe(stack);
    }

    public static CircuitComponent getFromFakeStackUnsafe(ItemStack stack) {
        // If this throws an IndexOutOfBounds exception, there is a bug
        return META_IDS.get(stack.getItemDamage());
    }

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
