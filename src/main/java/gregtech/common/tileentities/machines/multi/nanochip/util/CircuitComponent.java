package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.enums.Materials;
import gregtech.api.items.GT_CircuitComponent_FakeItem;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;

public enum CircuitComponent {

    // TODO: Consider if this whole fake stack system is overcomplicated and if we can't just use the real stacks
    // The main drawback of this is that either we have to override a LOT of things in the NEI display, or simply accept
    // that
    // the circuit components will be displayed with the same textures and names as the real stacks backing them.
    // This also wouldn't work well with components that don't have a 'real' backing ItemStack, such as components
    // that are only created through a module in the NAC

    // When adding to this list, PLEASE only add to the end! The ordinals are used as item ids for the fake items!
    WireNiobiumTitanium("gt.circuitcomponent.wire", null, Materials.NiobiumTitanium), // todo: figure out if we still
                                                                                      // need the material
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
    ProcessedOpticalSMDDiode("gt.circuitcomponent.processed.xsmd.diode", RecipeMaps.nanochipAssemblyMatrixRecipes),;

    public final String unlocalizedName;
    public final Materials material;
    // This is the recipe map that this component is used in as an input item
    public final RecipeMap<?> processingMap;

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap) {
        this(unlocalizedName, processingMap, null);
    }

    CircuitComponent(String unlocalizedName, RecipeMap<?> processingMap, Materials material) {
        this.unlocalizedName = unlocalizedName;
        // Hide the fake stack in NEI
        codechicken.nei.api.API.hideItem(getFakeStack(1));
        this.material = material;
        this.processingMap = processingMap;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal(unlocalizedName);
    }

    // ItemStack of a fake item, only for display and recipe checking purposes
    public ItemStack getFakeStack(int amount) {
        return new ItemStack(GT_CircuitComponent_FakeItem.INSTANCE, amount, this.ordinal());
    }

    public static CircuitComponent getFromFakeStack(ItemStack stack) {
        // If this throws an IndexOutOfBounds exception, there is a bug
        return CircuitComponent.values()[stack.getItemDamage()];
    }
}
