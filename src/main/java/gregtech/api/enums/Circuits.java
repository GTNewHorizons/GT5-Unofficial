package gregtech.api.enums;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The [OrePrefixes#circuit] and [OrePrefixes#wrapCircuit] ore-dictionary entries, keyed by voltage tier.
///
/// Each tier names an ore-dictionary entry whose material name is unrelated to the tier abbreviation
/// (`LV` is named `Basic`, `UHV` is named `Infinite`, and so on), so the ore-dictionary names these entries resolve
/// to cannot be spelled from the tier name. Routing every call site through this enum keeps that mapping in one
/// place.
///
/// Ingredients come in two shapes, so each prefix exposes two accessors:
/// - [#get(int)] and [#getWrap(int)] return a unified [ItemStack] for recipe inputs and outputs that need a
/// concrete stack.
/// - [#getIngredient()] and [#getWrapIngredient()] return an [ItemData] for crafting-grid recipes, which derive
/// a reversible recipe's recycling output from each ingredient's [ItemData]
/// ([gregtech.api.util.GTModHandler#addCraftingRecipe]).
public enum Circuits {

    ULV("Primitive"),
    LV("Basic"),
    MV("Good"),
    HV("Advanced"),
    EV("Data"),
    IV("Elite"),
    LuV("Master"),
    ZPM("Ultimate"),
    UV("Superconductor"),
    UHV("Infinite"),
    UEV("Bio"),
    UIV("Optical"),
    UMV("Exotic"),
    UXV("Cosmic"),
    MAX("Transcendent");

    private final String materialName;

    Circuits(String materialName) {
        this.materialName = materialName;
    }

    public ItemStack get(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.circuit.oreDictName(materialName), amount);
    }

    public ItemData getIngredient() {
        return new ItemData(OrePrefixes.circuit, materialName);
    }

    public ItemStack getWrap(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.wrapCircuit.oreDictName(materialName), amount);
    }

    public ItemData getWrapIngredient() {
        return new ItemData(OrePrefixes.wrapCircuit, materialName);
    }

    /// The ore-dictionary material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
