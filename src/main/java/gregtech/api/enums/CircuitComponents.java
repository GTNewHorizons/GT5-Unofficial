package gregtech.api.enums;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The [OrePrefixes#componentCircuit] ore-dictionary entries, keyed by the discrete component they stand for.
///
/// Each component is backed by a `Materials` marker that carries no composition and generates no items; it exists
/// only to name the ore-dictionary entry. Routing every call site through this enum keeps that mapping in one place.
///
/// Ingredients come in two shapes: [#get(int)] returns a unified [ItemStack] for recipe inputs and outputs that need
/// a concrete stack, and [#getIngredient()] returns an [ItemData] for crafting-grid recipes.
///
/// Crafting-grid recipes need the [ItemData] rather than the bare ore-dictionary name it stringifies to:
/// [gregtech.api.util.GTModHandler#addCraftingRecipe] derives the recycling output of a reversible recipe from the
/// [ItemData] of each ingredient, and a bare name carries no such association.
public enum CircuitComponents {

    RESISTOR(() -> Materials.Resistor),
    DIODE(() -> Materials.Diode),
    TRANSISTOR(() -> Materials.Transistor),
    CAPACITOR(() -> Materials.Capacitor),
    INDUCTOR(() -> Materials.Inductor);

    private final Supplier<Materials> marker;

    CircuitComponents(Supplier<Materials> marker) {
        this.marker = marker;
    }

    public ItemStack get(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.componentCircuit, marker.get(), amount);
    }

    public ItemData getIngredient() {
        return new ItemData(OrePrefixes.componentCircuit, marker.get());
    }
}
