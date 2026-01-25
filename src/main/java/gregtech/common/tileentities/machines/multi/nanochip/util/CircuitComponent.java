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
    WireLanthanum(
        3,
        "gt.circuitcomponent.wirelanthanum",
        () -> GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 1)),
    WireSpacetime(
        4,
        "gt.circuitcomponent.wirespacetime",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 1)),

    ProcessedWireNiobiumTitanium(100, "gt.circuitcomponent.processed.wirenbti", true),
    ProcessedWireYttriumBariumCuprate(101, "gt.circuitcomponent.processed.wireybc", true),
    ProcessedWireLumiium(102, "gt.circuitcomponent.processed.wirelumiium", true),
    ProcessedWireLanthanum(103, "gt.circuitcomponent.processed.wirelanthanum", true),
    ProcessedWireSpacetime(104, "gt.circuitcomponent.processed.wirespacetime", true),

    // SMDs
    // IDs: 200-299 CC, 300-399 PC
    SMDResistor(
        200,
        "gt.circuitcomponent.smd.resistor",
        () -> ItemList.Circuit_Parts_ResistorSMD.get(1)),
    SMDTransistor(
        201,
        "gt.circuitcomponent.smd.transistor",
        () -> ItemList.Circuit_Parts_TransistorSMD.get(1)),
    SMDInductor(
        202,
        "gt.circuitcomponent.smd.inductor",
        () -> ItemList.Circuit_Parts_InductorSMD.get(1)),
    SMDCapacitor(
        203,
        "gt.circuitcomponent.smd.capacitor",
        () -> ItemList.Circuit_Parts_CapacitorSMD.get(1)),
    SMDDiode(
        204,
        "gt.circuitcomponent.smd.diode",
        () -> ItemList.Circuit_Parts_DiodeSMD.get(1)),
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

    ProcessedSMDResistor(300, "gt.circuitcomponent.processed.smd.resistor", true),
    ProcessedSMDTransistor(301, "gt.circuitcomponent.processed.smd.transistor", true),
    ProcessedSMDInductor(302, "gt.circuitcomponent.processed.smd.inductor", true),
    ProcessedSMDCapacitor(303, "gt.circuitcomponent.processed.smd.capacitor", true),
    ProcessedSMDDiode(304, "gt.circuitcomponent.processed.smd.diode", true),
    ProcessedAdvSMDResistor(310, "gt.circuitcomponent.processed.asmd.resistor", true),
    ProcessedAdvSMDTransistor(311, "gt.circuitcomponent.processed.asmd.transistor", true),
    ProcessedAdvSMDInductor(312, "gt.circuitcomponent.processed.asmd.inductor", true),
    ProcessedAdvSMDCapacitor(313, "gt.circuitcomponent.processed.asmd.capacitor", true),
    ProcessedAdvSMDDiode(314, "gt.circuitcomponent.processed.asmd.diode", true),
    ProcessedOpticalSMDResistor(320, "gt.circuitcomponent.processed.xsmd.resistor", true),
    ProcessedOpticalSMDTransistor(321, "gt.circuitcomponent.processed.xsmd.transistor", true),
    ProcessedOpticalSMDInductor(322, "gt.circuitcomponent.processed.xsmd.inductor", true),
    ProcessedOpticalSMDCapacitor(323, "gt.circuitcomponent.processed.xsmd.capacitor", true),
    ProcessedOpticalSMDDiode(324, "gt.circuitcomponent.processed.xsmd.diode", true),

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

    ProcessedBoardMultifiberglassElite(500, "gt.circuitcomponent.processed.board.multifiberelite", true),
    ProcessedBoardWetwareLifesupport(501, "gt.circuitcomponent.processed.board.wetwarelifesupport", true),
    ProcessedBoardBioMutated(502, "gt.circuitcomponent.processed.board.biomutated", true),
    ProcessedBoardOptical(503, "gt.circuitcomponent.processed.board.optical", true),
    ProcessedNeuroProcessingUnit(504, "gt.circuitcomponent.processed.neuroprocessingunit", true),
    ProcessedBioProcessingUnit(505, "gt.circuitcomponent.processed.board.bioprocessingunit", true),

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
    WaferPico(
        607,
        "gt.circuitcomponent.waferpico",
        () -> getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 1, 0, new ItemStack(Blocks.fire))),

    ProcessedChipNanoCPU(700, "gt.circuitcomponent.processed.chipnanocpu", true),
    ProcessedChipRAM(701, "gt.circuitcomponent.processed.chipram", true),
    ProcessedChipNOR(702, "gt.circuitcomponent.processed.chipnor", true),
    ProcessedChipNAND(703, "gt.circuitcomponent.processed.chipnand", true),
    ProcessedChipASOC(704, "gt.circuitcomponent.processed.chipasoc", true),
    ProcessedChipPikoPIC(705, "gt.circuitcomponent.processed.chippikopic", true),
    ProcessedChipQuantumPIC(706, "gt.circuitcomponent.processed.chipquantumpic", true),
    ProcessedChipPico(707, "gt.circuitcomponent.processed.chippico", true),

    // Crystal components
    // IDs: 800-899 CC, 900-999 PC
    ChipCrystalCPU(
        800,
        "gt.circuitcomponent.chipcrystalcpu",
        () -> ItemList.Circuit_Chip_CrystalCPU.get(1)),
    ChipAdvCrystalCPU(
        801,
        "gt.circuitcomponent.chipadvcrystalcpu",
        () -> ItemList.Circuit_Chip_CrystalSoC.get(1)),

    ProcessedChipCrystalCPU(900, "gt.circuitcomponent.processed.chipcrystalcpu", true),
    ProcessedChipAdvCrystalCPU(901, "gt.circuitcomponent.processed.chipadvcrystalcpu", true),

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

    ProcessedChipOpticalCPU(1100, "gt.circuitcomponent.processed.opticalcpu", true),
    ProcessedOpticalRAM(1101, "gt.circuitcomponent.processed.opticalram", true),

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
    SuperconductorUHV(
        1202,
        "gt.circuitcomponent.superconductoruhv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 1)),
    SuperconductorUEV(
        1203,
        "gt.circuitcomponent.superconductoruev",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1)),
    SuperconductorUIV(
        1204,
        "gt.circuitcomponent.superconductoruiv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 1)),
    SuperconductorUMV(
        1205,
        "gt.circuitcomponent.superconductorumv",
        () -> GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 1)),

    ProcessedSuperconductorLuV(1300, "gt.circuitcomponent.processed.superconductorluv", true),
    ProcessedSuperconductorZPM(1301, "gt.circuitcomponent.processed.superconductorzpm", true),
    ProcessedSuperconductorUHV(1302, "gt.circuitcomponent.processed.superconductoruhv", true),
    ProcessedSuperconductorUEV(1303, "gt.circuitcomponent.processed.superconductoruev", true),
    ProcessedSuperconductorUIV(1304, "gt.circuitcomponent.processed.superconductoruiv", true),
    ProcessedSuperconductorUMV(1305, "gt.circuitcomponent.processed.superconductorumv", true),

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
    FrameboxNeutronium(
        1402,
        "gt.circuitcomponent.frame.neutronium",
        () -> GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1)),

    ProcessedFrameboxAluminium(1500, "gt.circuitcomponent.processed.frame.aluminium", true),
    ProcessedFrameboxTritanium(1501, "gt.circuitcomponent.processed.frame.tritanium", true),
    ProcessedFrameboxNeutronium(1502, "gt.circuitcomponent.processed.frame.neutronium", true),

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

    ProcessedFoilSiliconeRubber(1700, "gt.circuitcomponent.sheet.processed.siliconerubber", true),
    ProcessedFoilPolybenzimidazole(1701, "gt.circuitcomponent.sheet.processed.polybenzimidazole", true),
    ProcessedFoilRadoxPolymer(1702, "gt.circuitcomponent.sheet.processed.radoxpolymer", true),
    ProcessedFoilShirabon(1703, "gt.circuitcomponent.sheet.processed.shirabon", true),

    // Circuit encasement
    // IDs: 1800-1899
    BasicMainframeCasing(1800, "gt.circuitcomponent.casing.basicmainframe", true),
    AdvancedMainframeCasing(1801, "gt.circuitcomponent.casing.advancedmainframe", true),
    PicoCircuitCasing(1802, "gt.circuitcomponent.casing.pico", true),
    QuantumCircuitCasing(1803, "gt.circuitcomponent.casing.quantum", true),

    // Misc
    // IDs: 1900-1999
    CableOpticalFiber(
        1900,
        "gt.circuitcomponent.cable.opticalfiber",
        () -> CustomItemList.DATApipe.get(1)),

    // Bolts
    // IDs: 2000-2099
    BoltEnrichedHolmium(
        2000,
        "gt.circuitcomponent.bolt.enrichedholmium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 1)),
    BoltTranscendentMetal(
        2001,
        "gt.circuitcomponent.bolt.transcendentmetal",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.TranscendentMetal, 1)),
    BoltNeutronium(
        2002,
        "gt.circuitcomponent.bolt.neutronium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Neutronium, 1)),
    BoltIndium(
        2003,
        "gt.circuitcomponent.bolt.indium",
        () -> GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Indium, 1)),

    // Circuits
    // IDs: 2100-2199
    // The first three circuits in a line can be recursively used in the assembly matrix, and all of them can be turned
    // into a physical circuit item
    CrystalProcessor(
        2100,
        "gt.circuitcomponent.crystalprocessor",
        () -> ItemList.Circuit_Crystalprocessor.get(1), (byte) 2),
    CrystalAssembly(
        2101,
        "gt.circuitcomponent.crystalasssembly",
        () -> ItemList.Circuit_Crystalcomputer.get(1), (byte) 2),
    CrystalComputer(
        2102,
        "gt.circuitcomponent.crystalcomputer",
        () -> ItemList.Circuit_Ultimatecrystalcomputer.get(1), (byte) 2),
    CrystalMainframe(
        2103,
        "gt.circuitcomponent.crystalmainframe",
        () -> ItemList.Circuit_Crystalmainframe.get(1), (byte) 2),
    WetwareProcessor(
        2104,
        "gt.circuitcomponent.wetwareprocessor",
        () -> ItemList.Circuit_Neuroprocessor.get(1), (byte) 3),
    WetwareAssembly(
        2105,
        "gt.circuitcomponent.wetwareassembly",
        () -> ItemList.Circuit_Wetwarecomputer.get(1), (byte) 3),
    WetwareComputer(
        2106,
        "gt.circuitcomponent.wetwarecomputer",
        () -> ItemList.Circuit_Wetwaresupercomputer.get(1), (byte) 3),
    WetwareMainframe(
        2107,
        "gt.circuitcomponent.wetwaremainframe",
        () -> ItemList.Circuit_Wetwaremainframe.get(1), (byte) 3),
    BiowareProcessor(
        2108,
        "gt.circuitcomponent.biowareprocessor",
        () -> ItemList.Circuit_Bioprocessor.get(1), (byte) 4),
    BiowareAssembly(
        2109,
        "gt.circuitcomponent.biowareassembly",
        () -> ItemList.Circuit_Biowarecomputer.get(1), (byte) 4),
    BiowareComputer(
        2110,
        "gt.circuitcomponent.biowarecomputer",
        () -> ItemList.Circuit_Biowaresupercomputer.get(1), (byte) 4),
    BiowareMainframe(
        2111,
        "gt.circuitcomponent.biowaremainframe",
        () -> ItemList.Circuit_Biomainframe.get(1), (byte) 4),
    OpticalProcessor(
        2112,
        "gt.circuitcomponent.opticalprocessor",
        () -> ItemList.Circuit_OpticalProcessor.get(1), (byte) 5),
    OpticalAssembly(
        2113,
        "gt.circuitcomponent.opticalassembly",
        () -> ItemList.Circuit_OpticalAssembly.get(1), (byte) 5),
    OpticalComputer(
        2114,
        "gt.circuitcomponent.opticalcomputer",
        () -> ItemList.Circuit_OpticalComputer.get(1), (byte) 5),
    OpticalMainframe(
        2115,
        "gt.circuitcomponent.opticalmainframe",
        () -> ItemList.Circuit_OpticalMainframe.get(1), (byte) 5),
    PicoCircuit(
        2116,
        "gt.circuitcomponent.picocircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "item.PikoCircuit", 1, 0, new ItemStack(Blocks.fire)), (byte) 64),
    QuantumCircuit(
        2117,
        "gt.circuitcomponent.quantumcircuit",
        () -> getModItem(NewHorizonsCoreMod.ID, "item.QuantumCircuit", 1, 0, new ItemStack(Blocks.fire)), (byte) 64),

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
    public final String unlocalizedName;
    public String fallbackLocalizedName = null;

    public final String iconString;
    public final Supplier<ItemStack> realComponent;
    public final boolean isProcessed;

    // Tier used for calibration
    public final byte circuitTier;

    // CC constructor
    CircuitComponent(int id, String unlocalizedName, Supplier<ItemStack> realComponent) {
        this(id, unlocalizedName, realComponent, false, (byte) 0);
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

    CircuitComponent(int id, String unlocalizedName, Supplier<ItemStack> realComponent, byte circuitTier) {
        this(id, unlocalizedName, realComponent, false, circuitTier);
    }

    // PC constructor
    CircuitComponent(int id, String unlocalizedName, boolean isProcessed) {
        this(id, unlocalizedName, null, isProcessed, (byte) 0);
    }

    private static final String PROCESSED_DIRECTORY = "processed/";

    /**
     * Internal constructor
     * images are stored in gregtech/textures/items/gt.circuitcomponent
     * processed components are found in the processed subdirectory
     */
    CircuitComponent(int id, String unlocalizedName, Supplier<ItemStack> realComponent, boolean isProcessed,
        byte circuitTier) {
        this.metaId = id;
        this.unlocalizedName = unlocalizedName;
        this.realComponent = realComponent;
        this.isProcessed = isProcessed;
        this.circuitTier = circuitTier;

        this.iconString = isProcessed ? PROCESSED_DIRECTORY + name().toLowerCase() : name().toLowerCase();
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
