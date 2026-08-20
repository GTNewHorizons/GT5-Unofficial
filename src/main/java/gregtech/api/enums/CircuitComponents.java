package gregtech.api.enums;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The [OrePrefixes#componentCircuit] ore-dictionary entries, keyed by the discrete component they stand for.
///
/// Each component names an ore-dictionary entry that carries no composition and generates no items. Routing every
/// call site through this enum keeps that mapping in one place.
///
/// [#get(int)] returns a unified [ItemStack] for recipe inputs and outputs that need a concrete stack.
/// [#getIngredient()] returns an [ItemData] for crafting-grid recipes; see
/// [gregtech.api.util.GTModHandler#addCraftingRecipe].
public enum CircuitComponents {

    RESISTOR("Resistor"),
    DIODE("Diode"),
    TRANSISTOR("Transistor"),
    CAPACITOR("Capacitor"),
    INDUCTOR("Inductor");

    private final String materialName;

    CircuitComponents(String materialName) {
        this.materialName = materialName;
    }

    public ItemStack get(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.componentCircuit.oreDictName(materialName), amount);
    }

    public ItemData getIngredient() {
        return new ItemData(OrePrefixes.componentCircuit, materialName);
    }

    /// The ore-dictionary material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
