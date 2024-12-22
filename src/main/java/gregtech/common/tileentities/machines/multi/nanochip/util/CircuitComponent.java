package gregtech.common.tileentities.machines.multi.nanochip.util;

import java.util.HashMap;
import java.util.Map;

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
    WireNiobiumTitanium("gt.circuitcomponent.wirenbti", RecipeMaps.nanochipWireTracer, Materials.NiobiumTitanium),
    ProcessedWireNiobiumTitanium("gt.circuitcomponent.processed.wirenbti", RecipeMaps.nanochipAssemblyMatrixRecipes,
        Materials.NiobiumTitanium),
    SMDResistor("gt.circuitcomponent.smd.resistor", RecipeMaps.nanochipSMDProcessorRecipes),
    SMDTransistor("gt.circuitcomponent.smd.transistor", RecipeMaps.nanochipSMDProcessorRecipes),
    SMDInductor("gt.circuitcomponent.smd.inductor", RecipeMaps.nanochipSMDProcessorRecipes),
    SMDCapacitor("gt.circuitcomponent.smd.capacitor", RecipeMaps.nanochipSMDProcessorRecipes),
    SMDDiode("gt.circuitcomponent.smd.diode", RecipeMaps.nanochipSMDProcessorRecipes),
    AdvSMDResistor("gt.circuitcomponent.asmd.resistor", RecipeMaps.nanochipSMDProcessorRecipes),
    AdvSMDTransistor("gt.circuitcomponent.asmd.transistor", RecipeMaps.nanochipSMDProcessorRecipes),
    AdvSMDInductor("gt.circuitcomponent.asmd.inductor", RecipeMaps.nanochipSMDProcessorRecipes),
    AdvSMDCapacitor("gt.circuitcomponent.asmd.capacitor", RecipeMaps.nanochipSMDProcessorRecipes),
    AdvSMDDiode("gt.circuitcomponent.asmd.diode", RecipeMaps.nanochipSMDProcessorRecipes),
    OpticalSMDResistor("gt.circuitcomponent.xsmd.resistor", RecipeMaps.nanochipSMDProcessorRecipes),
    OpticalSMDTransistor("gt.circuitcomponent.xsmd.transistor", RecipeMaps.nanochipSMDProcessorRecipes),
    OpticalSMDInductor("gt.circuitcomponent.xsmd.inductor", RecipeMaps.nanochipSMDProcessorRecipes),
    OpticalSMDCapacitor("gt.circuitcomponent.xsmd.capacitor", RecipeMaps.nanochipSMDProcessorRecipes),
    OpticalSMDDiode("gt.circuitcomponent.xsmd.diode", RecipeMaps.nanochipSMDProcessorRecipes),
    ProcessedSMDResistor("gt.circuitcomponent.processed.smd.resistor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedSMDTransistor("gt.circuitcomponent.processed.smd.transistor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedSMDInductor("gt.circuitcomponent.processed.smd.inductor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedSMDCapacitor("gt.circuitcomponent.processed.smd.capacitor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedSMDDiode("gt.circuitcomponent.processed.smd.diode", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedAdvSMDResistor("gt.circuitcomponent.processed.asmd.resistor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedAdvSMDTransistor("gt.circuitcomponent.processed.asmd.transistor",
        RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedAdvSMDInductor("gt.circuitcomponent.processed.asmd.inductor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedAdvSMDCapacitor("gt.circuitcomponent.processed.asmd.capacitor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedAdvSMDDiode("gt.circuitcomponent.processed.asmd.diode", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedOpticalSMDResistor("gt.circuitcomponent.processed.xsmd.resistor",
        RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedOpticalSMDTransistor("gt.circuitcomponent.processed.xsmd.transistor",
        RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedOpticalSMDInductor("gt.circuitcomponent.processed.xsmd.inductor",
        RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedOpticalSMDCapacitor("gt.circuitcomponent.processed.xsmd.capacitor",
        RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedOpticalSMDDiode("gt.circuitcomponent.processed.xsmd.diode", RecipeMaps.nanochipAssemblyMatrixRecipes),
    BoardMultifiberglassElite("gt.circuitcomponent.boardmultifiberelite", RecipeMaps.nanochipBoardProcessorRecipes),
    ProcessedBoardMultifiberglassElite("gt.circuitcomponent.processed.boardmultifiberelite",
        RecipeMaps.nanochipAssemblyMatrixRecipes),
    ChipNanoCPU("gt.circuitcomponent.chipnanocpu", RecipeMaps.nanochipCuttingChamber),
    ChipRAM("gt.circuitcomponent.chipram", RecipeMaps.nanochipCuttingChamber),
    ChipNOR("gt.circuitcomponent.chipnor", RecipeMaps.nanochipCuttingChamber),
    ChipNAND("gt.circuitcomponent.chipnand", RecipeMaps.nanochipCuttingChamber),
    ChipCrystalCPU("gt.circuitcomponent.chipcrystalcpu", RecipeMaps.nanochipEtchingArray),
    ProcessedChipNanoCPU("gt.circuitcomponent.processed.chipnanocpu", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedChipCrystalCPU("gt.circuitcomponent.processed.chipcrystalcpu", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedChipRAM("gt.circuitcomponent.processed.chipram", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedChipNOR("gt.circuitcomponent.processed.chipnor", RecipeMaps.nanochipAssemblyMatrixRecipes),
    ProcessedChipNAND("gt.circuitcomponent.processed.chipnand", RecipeMaps.nanochipAssemblyMatrixRecipes),
    SuperconductorLuV("gt.circuitcomponent.superconductorluv", RecipeMaps.nanochipSuperconductorSplitter,
        Materials.SuperconductorLuV),
    ProcessedSuperconductorLuV("gt.circuitcomponent.processed.superconductorluv",
        RecipeMaps.nanochipAssemblyMatrixRecipes, Materials.SuperconductorLuV),
    FrameboxAluminium("gt.circuitcomponent.frame.aluminium", RecipeMaps.nanochipCuttingChamber, Materials.Aluminium),
    ProcessedFrameboxAluminium("gt.circuitcomponent.processed.frame.aluminium",
        RecipeMaps.nanochipAssemblyMatrixRecipes),
    // The first three circuits in a line can be recursively used in the assembly matrix, and all of them can be turned
    // into a physical circuit item
    CrystalProcessor("gt.circuitcomponent.crystalprocessor", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Crystalprocessor.get(1)),
    CrystalAssembly("gt.circuitcomponent.crystassembly", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Crystalcomputer.get(1)),
    CrystalComputer("gt.circuitcomponent.crystalcomputer", RecipeMaps.nanochipAssemblyMatrixRecipes,
        ItemList.Circuit_Ultimatecrystalcomputer.get(1)),
    CrystalMainframe("gt.circuitcomponent.crystalmainframe", null, ItemList.Circuit_Crystalmainframe.get(1)),;

    public final String unlocalizedName;
    public String fallbackLocalizedName = null;
    public final Materials material;
    // This is the recipe map that this component is used in as an input item
    public final RecipeMap<?> processingMap;
    public final ItemStack realCircuit;

    // No need to use a full recipe map for conversions to real circuits, this also makes things a little easier
    // since we won't need to match outputs of recipes
    public static final Map<GTUtility.ItemId, CircuitComponent> realCircuitToComponent = new HashMap<>();

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap) {
        this(unlocalizedName, processingMap, null, null);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, ItemStack realCircuit) {
        this(unlocalizedName, processingMap, realCircuit, null);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, Materials material) {
        this(unlocalizedName, processingMap, null, material);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, ItemStack realCircuit, Materials material) {
        this.unlocalizedName = unlocalizedName;
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

    public static CircuitComponent getFromFakeStack(ItemStack stack) {
        // If this throws an IndexOutOfBounds exception, there is a bug
        return CircuitComponent.values()[stack.getItemDamage()];
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
