package gregtech.common.tileentities.machines.multi.nanochip.util;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTModHandler.getModItem;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.items.CircuitComponentFakeItem;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;

public enum CircuitComponent {

    // TODO: Consider if this whole fake stack system is overcomplicated and if we can't just use the real stacks
    // The main drawback of this is that either we have to override a LOT of things in the NEI display, or simply accept
    // that
    // the circuit components will be displayed with the same textures and names as the real stacks backing them.
    // This also wouldn't work well with components that don't have a 'real' backing ItemStack, such as components
    // that are only created through a module in the NAC

    // When adding to this list, PLEASE only add to the end! The ordinals are used as item ids for the fake items, so
    // adding in the middle will break saved state!

    // Wires
    WireNiobiumTitanium("gt.circuitcomponent.wirenbti", RecipeMaps.nanochipWireTracer, Materials.NiobiumTitanium,
        "wireniobiumtitanium"),
    WireYttriumBariumCuprate("gt.circuitcomponent.wireybc", RecipeMaps.nanochipWireTracer,
        Materials.YttriumBariumCuprate, "wireyttriumbariumcuprate"),
    // Lumiium is a werkstoff, not a material, can't insert yet
    WireLumiium("gt.circuitcomponent.wirelumiium", RecipeMaps.nanochipWireTracer, "wirelumiium"),
    WireLanthanum("gt.circuitcomponent.wirelanthanum", RecipeMaps.nanochipWireTracer, Materials.Lanthanum,
        "wirelanthanum"),
    WireSpacetime("gt.circuitcomponent.wirespacetime", RecipeMaps.nanochipWireTracer, Materials.SpaceTime,
        "wirespacetime"),
    ProcessedWireNiobiumTitanium("gt.circuitcomponent.processed.wirenbti", RecipeMaps.nanochipAssemblyMatrixRecipes,
        Materials.NiobiumTitanium, "processedwireniobiumtitanium"),
    ProcessedWireYttriumBariumCuprate("gt.circuitcomponent.processed.wireybc", RecipeMaps.nanochipAssemblyMatrixRecipes,
        Materials.YttriumBariumCuprate, "processedwireyttriumbariumcuprate"),
    ProcessedWireLumiium("gt.circuitcomponent.processed.wirelumiium", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedwirelumiium"),
    ProcessedWireLanthanum("gt.circuitcomponent.processed.wirelanthanum", RecipeMaps.nanochipAssemblyMatrixRecipes,
        Materials.Lanthanum, "processedwirelanthanum"),
    ProcessedWireSpacetime("gt.circuitcomponent.processed.wirespacetime", RecipeMaps.nanochipAssemblyMatrixRecipes,
        Materials.SpaceTime, "processedwirespacetime"),

    // SMDs
    SMDResistor("gt.circuitcomponent.smd.resistor", RecipeMaps.nanochipSMDProcessorRecipes, "smdresistor"),
    SMDTransistor("gt.circuitcomponent.smd.transistor", RecipeMaps.nanochipSMDProcessorRecipes, "smdtransistor"),
    SMDInductor("gt.circuitcomponent.smd.inductor", RecipeMaps.nanochipSMDProcessorRecipes, "smdinductor"),
    SMDCapacitor("gt.circuitcomponent.smd.capacitor", RecipeMaps.nanochipSMDProcessorRecipes, "smdcapacitor"),
    SMDDiode("gt.circuitcomponent.smd.diode", RecipeMaps.nanochipSMDProcessorRecipes, "smddiode"),
    AdvSMDResistor("gt.circuitcomponent.asmd.resistor", RecipeMaps.nanochipSMDProcessorRecipes, "advsmdresistor"),
    AdvSMDTransistor("gt.circuitcomponent.asmd.transistor", RecipeMaps.nanochipSMDProcessorRecipes, "advsmdtransistor"),
    AdvSMDInductor("gt.circuitcomponent.asmd.inductor", RecipeMaps.nanochipSMDProcessorRecipes, "advsmdinductor"),
    AdvSMDCapacitor("gt.circuitcomponent.asmd.capacitor", RecipeMaps.nanochipSMDProcessorRecipes, "advsmdcapacitor"),
    AdvSMDDiode("gt.circuitcomponent.asmd.diode", RecipeMaps.nanochipSMDProcessorRecipes, "advsmddiode"),
    OpticalSMDResistor("gt.circuitcomponent.xsmd.resistor", RecipeMaps.nanochipSMDProcessorRecipes,
        "opticalsmdresistor"),
    OpticalSMDTransistor("gt.circuitcomponent.xsmd.transistor", RecipeMaps.nanochipSMDProcessorRecipes,
        "opticalsmdtransistor"),
    OpticalSMDInductor("gt.circuitcomponent.xsmd.inductor", RecipeMaps.nanochipSMDProcessorRecipes,
        "opticalsmdinductor"),
    OpticalSMDCapacitor("gt.circuitcomponent.xsmd.capacitor", RecipeMaps.nanochipSMDProcessorRecipes,
        "opticalsmdcapacitor"),
    OpticalSMDDiode("gt.circuitcomponent.xsmd.diode", RecipeMaps.nanochipSMDProcessorRecipes, "opticalsmddiode"),
    ProcessedSMDResistor("gt.circuitcomponent.processed.smd.resistor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedsmdresistor"),
    ProcessedSMDTransistor("gt.circuitcomponent.processed.smd.transistor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedsmdtransistor"),
    ProcessedSMDInductor("gt.circuitcomponent.processed.smd.inductor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedsmdinductor"),
    ProcessedSMDCapacitor("gt.circuitcomponent.processed.smd.capacitor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedsmdcapacitor"),
    ProcessedSMDDiode("gt.circuitcomponent.processed.smd.diode", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedsmddiode"),
    ProcessedAdvSMDResistor("gt.circuitcomponent.processed.asmd.resistor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedadvsmdresistor"),
    ProcessedAdvSMDTransistor("gt.circuitcomponent.processed.asmd.transistor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedadvsmdtransistor"),
    ProcessedAdvSMDInductor("gt.circuitcomponent.processed.asmd.inductor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedadvsmdinductor"),
    ProcessedAdvSMDCapacitor("gt.circuitcomponent.processed.asmd.capacitor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedadvsmdcapacitor"),
    ProcessedAdvSMDDiode("gt.circuitcomponent.processed.asmd.diode", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedadvsmddiode"),
    ProcessedOpticalSMDResistor("gt.circuitcomponent.processed.xsmd.resistor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedopticalsmdresistor"),
    ProcessedOpticalSMDTransistor("gt.circuitcomponent.processed.xsmd.transistor",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedopticalsmdtransistor"),
    ProcessedOpticalSMDInductor("gt.circuitcomponent.processed.xsmd.inductor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedopticalsmdinductor"),
    ProcessedOpticalSMDCapacitor("gt.circuitcomponent.processed.xsmd.capacitor",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedopticalsmdcapacitor"),
    ProcessedOpticalSMDDiode("gt.circuitcomponent.processed.xsmd.diode", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedopticalsmddiode"),

    // Boards
    BoardMultifiberglassElite("gt.circuitcomponent.board.multifiberelite", RecipeMaps.nanochipBoardProcessorRecipes,
        "boardmultifiberglasselite"),
    BoardWetwareLifesupport("gt.circuitcomponent.board.wetwarelifesupport", RecipeMaps.nanochipBoardProcessorRecipes,
        "boardwetwarelifesupport"),
    BoardBioMutated("gt.circuitcomponent.board.biomutated", RecipeMaps.nanochipBoardProcessorRecipes,
        "boardbiomutated"),
    BoardOptical("gt.circuitcomponent.board.optical", RecipeMaps.nanochipBoardProcessorRecipes, "boardoptical"),
    Neuroprocessor("gt.circuitcomponent.neuroprocessor", RecipeMaps.nanochipAssemblyMatrixRecipes, "neuroprocessor"),
    Bioprocessor("gt.circuitcomponent.neuroprocessor", RecipeMaps.nanochipAssemblyMatrixRecipes, "bioprocessor"),
    ProcessedBoardMultifiberglassElite("gt.circuitcomponent.processed.board.multifiberelite",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedboardmultifiberglasselite"),
    ProcessedBoardWetwareLifesupport("gt.circuitcomponent.processed.board.wetwarelifesupport",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedboardwetwarelifesupport"),
    ProcessedBoardBioMutated("gt.circuitcomponent.processed.board.biomutated", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedboardbiomutated"),
    ProcessedBoardOptical("gt.circuitcomponent.processed.board.optical", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedboardoptical"),

    // Wafers and chips
    WaferNanoCPU("gt.circuitcomponent.waferNanoCPU", RecipeMaps.nanochipCuttingChamber, "wafernanocpu"),
    WaferRAM("gt.circuitcomponent.waferram", RecipeMaps.nanochipCuttingChamber, "waferram"),
    WaferNOR("gt.circuitcomponent.wafernor", RecipeMaps.nanochipCuttingChamber, "wafernor"),
    WaferNAND("gt.circuitcomponent.wafernand", RecipeMaps.nanochipCuttingChamber, "wafernand"),
    WaferASOC("gt.circuitcomponent.waferasoc", RecipeMaps.nanochipCuttingChamber, "waferasoc"),
    WaferPikoPIC("gt.circuitcomponent.waferpikopic", RecipeMaps.nanochipCuttingChamber, "waferpikopic"),
    WaferQuantumPIC("gt.circuitcomponent.waferquantumpic", RecipeMaps.nanochipCuttingChamber, "waferquantumpic"),
    WaferPico("gt.circuitcomponent.waferpico", RecipeMaps.nanochipCuttingChamber, "waferpico"),
    ProcessedChipNanoCPU("gt.circuitcomponent.processed.chipnanocpu", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipnanocpu"),
    ProcessedChipRAM("gt.circuitcomponent.processed.chipram", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipram"),
    ProcessedChipNOR("gt.circuitcomponent.processed.chipnor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipnor"),
    ProcessedChipNAND("gt.circuitcomponent.processed.chipnand", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipnand"),
    ProcessedChipASOC("gt.circuitcomponent.processed.chipasoc", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipasoc"),
    ProcessedChipPikoPIC("gt.circuitcomponent.processed.chippikopic", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchippikopic"),
    ProcessedChipQuantumPIC("gt.circuitcomponent.processed.chipquantumpic", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipquantumpic"),
    ProcessedChipPico("gt.circuitcomponent.processed.chippico", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchippico"),

    // Crystal components
    ChipCrystalCPU("gt.circuitcomponent.chipcrystalcpu", RecipeMaps.nanochipEtchingArray, "chipcrystalcpu"),
    ChipAdvCrystalCPU("gt.circuitcomponent.chipadvcrystalcpu", RecipeMaps.nanochipEtchingArray, "chipadvcrystalcpu"),
    ProcessedChipCrystalCPU("gt.circuitcomponent.processed.chipcrystalcpu", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipcrystalcpu"),
    ProcessedChipAdvCrystalCPU("gt.circuitcomponent.processed.chipadvcrystalcpu",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedchipadvcrystalcpu"),

    // Optical components
    ChipOpticalCPU("gt.circuitcomponent.opticalcpu", RecipeMaps.nanochipOpticalOrganizer, "chipopticalcpu"),
    ProcessedChipOpticalCPU("gt.circuitcomponent.processed.opticalcpu", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedchipopticalcpu"),
    OpticalRAM("gt.circuitcomponent.opticalram", RecipeMaps.nanochipOpticalOrganizer, "opticalram"),
    ProcessedOpticalRAM("gt.circuitcomponent.processed.opticalram", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedopticalram"),

    // Superconductors
    SuperconductorLuV("gt.circuitcomponent.superconductorluv", RecipeMaps.nanochipSuperconductorSplitter,
        Materials.SuperconductorLuV, "superconductorluv"),
    SuperconductorZPM("gt.circuitcomponent.superconductorzpm", RecipeMaps.nanochipSuperconductorSplitter,
        Materials.SuperconductorZPM, "superconductorzpm"),
    SuperconductorUHV("gt.circuitcomponent.superconductoruhv", RecipeMaps.nanochipSuperconductorSplitter,
        Materials.SuperconductorUHV, "superconductoruhv"),
    SuperconductorUEV("gt.circuitcomponent.superconductoruev", RecipeMaps.nanochipSuperconductorSplitter,
        Materials.SuperconductorUEV, "superconductoruev"),
    SuperconductorUIV("gt.circuitcomponent.superconductoruiv", RecipeMaps.nanochipSuperconductorSplitter,
        Materials.SuperconductorUIV, "superconductoruiv"),
    SuperconductorUMV("gt.circuitcomponent.superconductorumv", RecipeMaps.nanochipSuperconductorSplitter,
        Materials.SuperconductorUMV, "superconductorumv"),
    ProcessedSuperconductorLuV("gt.circuitcomponent.processed.superconductorluv",
        RecipeMaps.nanochipAssemblyMatrixRecipes, Materials.SuperconductorLuV, "processedsuperconductorluv"),
    ProcessedSuperconductorZPM("gt.circuitcomponent.processed.superconductorzpm",
        RecipeMaps.nanochipAssemblyMatrixRecipes, Materials.SuperconductorZPM, "processedsuperconductorzpm"),
    ProcessedSuperconductorUHV("gt.circuitcomponent.processed.superconductoruhv",
        RecipeMaps.nanochipAssemblyMatrixRecipes, Materials.SuperconductorUHV, "processedsuperconductoruhv"),
    ProcessedSuperconductorUEV("gt.circuitcomponent.processed.superconductoruev",
        RecipeMaps.nanochipAssemblyMatrixRecipes, Materials.SuperconductorUEV, "processedsuperconductoruev"),
    ProcessedSuperconductorUIV("gt.circuitcomponent.processed.superconductoruiv",
        RecipeMaps.nanochipAssemblyMatrixRecipes, Materials.SuperconductorUIV, "processedsuperconductoruiv"),
    ProcessedSuperconductorUMV("gt.circuitcomponent.processed.superconductorumv",
        RecipeMaps.nanochipAssemblyMatrixRecipes, Materials.SuperconductorUMV, "processedsuperconductorumv"),

    // Frameboxes
    FrameboxAluminium("gt.circuitcomponent.frame.aluminium", RecipeMaps.nanochipCuttingChamber, Materials.Aluminium,
        "frameboxaluminium"),
    FrameboxTritanium("gt.circuitcomponent.frame.tritanium", RecipeMaps.nanochipCuttingChamber, Materials.Tritanium,
        "frameboxtritanium"),
    FrameboxNeutronium("gt.circuitcomponent.frame.neutronium", RecipeMaps.nanochipCuttingChamber, Materials.Neutronium,
        "frameboxneutronium"),
    ProcessedFrameboxAluminium("gt.circuitcomponent.processed.frame.aluminium",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedframeboxaluminium"),
    ProcessedFrameboxTritanium("gt.circuitcomponent.processed.frame.tritanium",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedframeboxtritanium"),
    ProcessedFrameboxNeutronium("gt.circuitcomponent.processed.frame.neutronium",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedframeboxneutronium"),

    // Foils
    FoilSiliconeRubber("gt.circuitcomponent.sheet.siliconerubber", RecipeMaps.nanochipSheetSupervisor,
        "foilsiliconerubber"),
    FoilPolybenzimidazole("gt.circuitcomponent.sheet.polybenzimidazole", RecipeMaps.nanochipSheetSupervisor,
        "foilpolybenzimidazole"),
    FoilRadoxPolymer("gt.circuitcomponent.sheet.radoxpolymer", RecipeMaps.nanochipSheetSupervisor, "foilradoxpolymer"),
    FoilShirabon("gt.circuitcomponent.sheet.shirabon", RecipeMaps.nanochipSheetSupervisor, "foilshirabon"),
    ProcessedFoilSiliconeRubber("gt.circuitcomponent.sheet.siliconerubber", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedfoilsiliconerubber"),
    ProcessedFoilPolybenzimidazole("gt.circuitcomponent.sheet.polybenzimidazole",
        RecipeMaps.nanochipAssemblyMatrixRecipes, "processedfoilpolybenzimidazole"),
    ProcessedFoilRadoxPolymer("gt.circuitcomponent.sheet.radoxpolymer", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedfoilradoxpolymer"),
    ProcessedFoilShirabon("gt.circuitcomponent.sheet.shirabon", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "processedfoilshirabon"),

    // Misc
    CableOpticalFiber("gt.circuitcomponent.cable.opticalfiber", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "cableopticalfiber"),

    // Bolts
    BoltEnrichedHolmium("gt.circuitcomponent.bolt.enrichedholmium", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "boltenrichedholmium"),
    BoltTranscendentMetal("gt.circuitcomponent.bolt.transcendentmetal", RecipeMaps.nanochipAssemblyMatrixRecipes,
        "bolttranscendentmetal"),
    BoltNeutronium("gt.circuitcomponent.bolt.neutronium", RecipeMaps.nanochipAssemblyMatrixRecipes, "boltneutronium"),
    BoltIndium("gt.circuitcomponent.bolt.indium", RecipeMaps.nanochipAssemblyMatrixRecipes, "boltindium"),

    // Circuits
    // The first three circuits in a line can be recursively used in the assembly matrix, and all of them can be turned
    // into a physical circuit item
    CrystalProcessor("gt.circuitcomponent.crystalprocessor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Crystalprocessor.get(1)),
    CrystalAssembly("gt.circuitcomponent.crystalasssembly", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Crystalcomputer.get(1)),
    CrystalComputer("gt.circuitcomponent.crystalcomputer", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Ultimatecrystalcomputer.get(1)),
    CrystalMainframe("gt.circuitcomponent.crystalmainframe", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Crystalmainframe.get(1)),
    WetwareProcessor("gt.circuitcomponent.wetwareprocessor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Neuroprocessor.get(1)),
    WetwareAssembly("gt.circuitcomponent.wetwareassembly", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Wetwarecomputer.get(1)),
    WetwareComputer("gt.circuitcomponent.wetwarecomputer", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Wetwaresupercomputer.get(1)),
    WetwareMainframe("gt.circuitcomponent.wetwaremainframe", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Wetwaremainframe.get(1)),
    BiowareProcessor("gt.circuitcomponent.biowareprocessor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Bioprocessor.get(1)),
    BiowareAssembly("gt.circuitcomponent.biowareassembly", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Biowarecomputer.get(1)),
    BiowareComputer("gt.circuitcomponent.biowarecomputer", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Biowaresupercomputer.get(1)),
    BiowareMainframe("gt.circuitcomponent.biowaremainframe", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Biomainframe.get(1)),
    OpticalProcessor("gt.circuitcomponent.opticalprocessor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_OpticalProcessor.get(1)),
    OpticalAssembly("gt.circuitcomponent.opticalassembly", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_OpticalAssembly.get(1)),
    OpticalComputer("gt.circuitcomponent.opticalcomputer", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_OpticalComputer.get(1)),
    OpticalMainframe("gt.circuitcomponent.opticalmainframe", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_OpticalMainframe.get(1)),
    // todo consider doing fallbacks better
    PicoCircuit("gt.circuitcomponent.picocircuit", RecipeMaps.nanochipAssemblyMatrixRecipes,
        getModItem(NewHorizonsCoreMod.ID, "item.PikoCircuit", 1, 0, new ItemStack(Blocks.dirt))),
    QuantumCircuit("gt.circuitcomponent.quantumcircuit", RecipeMaps.nanochipAssemblyMatrixRecipes,
        getModItem(NewHorizonsCoreMod.ID, "item.QuantumCircuit", 1, 0, new ItemStack(Blocks.stone))),

    ;

    public final String unlocalizedName;
    public String fallbackLocalizedName = null;

    public String iconString = "";
    public final Materials material;
    // This is the recipe map that this component is used in as an input item
    public final RecipeMap<?> processingMap;
    public final ItemStack realCircuit;

    // No need to use a full recipe map for conversions to real circuits, this also makes things a little easier
    // since we won't need to match outputs of recipes
    public static final Map<GTUtility.ItemId, CircuitComponent> realCircuitToComponent = new HashMap<>();

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, String iconString) {
        this(unlocalizedName, processingMap, null, null, iconString);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap) {
        this(unlocalizedName, processingMap, null, null, null);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, ItemStack realCircuit, String iconString) {
        this(unlocalizedName, processingMap, realCircuit, null, iconString);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, ItemStack realCircuit) {
        this(unlocalizedName, processingMap, realCircuit, null, null);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, Materials material, String iconString) {
        this(unlocalizedName, processingMap, null, material, iconString);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, Materials material) {
        this(unlocalizedName, processingMap, null, material, null);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, ItemStack realCircuit, Materials material,
        String iconString) {
        this.unlocalizedName = unlocalizedName;
        this.iconString = iconString;
        // Hide the fake stack in NEI
        codechicken.nei.api.API.hideItem(getFakeStack(1));
        this.material = material;
        this.processingMap = processingMap;
        this.realCircuit = realCircuit;
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
        if (stack.getItemDamage() >= CircuitComponent.values().length) return null;
        return getFromFakeStackUnsafe(stack);
    }

    public static CircuitComponent getFromFakeStackUnsafe(ItemStack stack) {
        // If this throws an IndexOutOfBounds exception, there is a bug
        return CircuitComponent.values()[stack.getItemDamage()];
    }

    public static CircuitComponent getFromMetaDataUnsafe(int metadata) {
        return CircuitComponent.values()[metadata];
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

    static {
        // Populate real circuit conversion hashmap
        for (CircuitComponent component : CircuitComponent.values()) {
            if (component.realCircuit != null) {
                GTUtility.ItemId id = GTUtility.ItemId.createNoCopy(component.realCircuit);
                realCircuitToComponent.put(id, component);
            }
        }
    }
}
