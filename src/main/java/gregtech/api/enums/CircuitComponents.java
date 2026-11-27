package gregtech.api.enums;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;

/// The [OrePrefixes#componentCircuit] oredict entries for discrete circuit components, each paired with the
/// GregTech item it unifies to.
///
/// [#get(int)] returns that item where a recipe needs a concrete stack. [#getIngredient()] returns the oredict
/// form for crafting recipes; see [gregtech.api.util.GTModHandler#addCraftingRecipe].
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

    /// The oredict material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
