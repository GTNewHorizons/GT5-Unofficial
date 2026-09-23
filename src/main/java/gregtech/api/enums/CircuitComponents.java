package gregtech.api.enums;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;

/// The [OrePrefixes#componentCircuit] ore-dictionary entries, keyed by the discrete component they stand for.
///
/// Each component names an ore-dictionary entry that carries no composition and generates no items, and declares
/// the GregTech item that is that entry's unification target. Routing every call site through this enum keeps
/// that mapping in one place.
///
/// [#get(int)] returns the declared [ItemStack] for recipe inputs and outputs that need a concrete stack.
/// [#getIngredient()] returns an [ItemData] for crafting-grid recipes; see
/// [gregtech.api.util.GTModHandler#addCraftingRecipe].
public enum CircuitComponents {

    RESISTOR("Resistor", ItemList.Circuit_Parts_Resistor),
    DIODE("Diode", ItemList.Circuit_Parts_Diode),
    TRANSISTOR("Transistor", ItemList.Circuit_Parts_Transistor),
    CAPACITOR("Capacitor", ItemList.Circuit_Parts_Capacitor),
    INDUCTOR("Inductor", ItemList.Circuit_Parts_Coil);

    private final String materialName;
    private final ItemList item;

    CircuitComponents(String materialName, ItemList item) {
        this.materialName = materialName;
        this.item = item;
    }

    public ItemStack get(int amount) {
        return item.get(amount);
    }

    public ItemData getIngredient() {
        return new ItemData(OrePrefixes.componentCircuit, materialName);
    }

    /// The ore-dictionary material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
