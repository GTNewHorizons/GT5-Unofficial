package gregtech.api.enums;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTModHandler;

/// The [OrePrefixes#circuit] and [OrePrefixes#wrapCircuit] ore-dictionary entries, keyed by voltage tier.
///
/// Each tier names an ore-dictionary entry whose material name is unrelated to the tier abbreviation (`LV` is
/// named `Basic`, `UHV` is named `Infinite`, and so on). Routing every call site through this enum keeps that
/// mapping in one place.
///
/// Each tier declares the concrete items standing for its entries: NewHorizonsCoreMod's placeholder circuit,
/// registered under the [OrePrefixes#circuit] name, and GoodGenerator's circuit wrap, registered under the
/// [OrePrefixes#wrapCircuit] name.
///
/// Each prefix exposes two accessors:
/// - [#get(int)] and [#getWrap(int)] return the declared [ItemStack] for recipe inputs and outputs that need a
/// concrete stack.
/// - [#getIngredient()] and [#getWrapIngredient()] return an [ItemData] for crafting-grid recipes; see
/// [gregtech.api.util.GTModHandler#addCraftingRecipe].
public enum Circuits {

    ULV("Primitive", "CircuitULV", 0),
    LV("Basic", "CircuitLV", 1),
    MV("Good", "CircuitMV", 2),
    HV("Advanced", "CircuitHV", 3),
    EV("Data", "CircuitEV", 4),
    IV("Elite", "CircuitIV", 5),
    LuV("Master", "CircuitLuV", 6),
    ZPM("Ultimate", "CircuitZPM", 7),
    UV("Superconductor", "CircuitUV", 8),
    UHV("Infinite", "CircuitUHV", 9),
    UEV("Bio", "CircuitUEV", 10),
    UIV("Optical", "CircuitUIV", 11),
    UMV("Exotic", "CircuitUMV", 12),
    UXV("Cosmic", "CircuitUXV", 13),
    MAX("Transcendent", "CircuitMAX", 14);

    private final String materialName;
    private final String placeholderName;
    private final int wrapMeta;

    Circuits(String materialName, String placeholderName, int wrapMeta) {
        this.materialName = materialName;
        this.placeholderName = placeholderName;
        this.wrapMeta = wrapMeta;
    }

    public ItemStack get(int amount) {
        return GTModHandler.getModItem(Mods.NewHorizonsCoreMod.ID, placeholderName, amount);
    }

    public ItemData getIngredient() {
        return new ItemData(OrePrefixes.circuit, materialName);
    }

    public ItemStack getWrap(int amount) {
        return GTModHandler.getModItem(Mods.GoodGenerator.ID, "circuitWrap", amount, wrapMeta);
    }

    public ItemData getWrapIngredient() {
        return new ItemData(OrePrefixes.wrapCircuit, materialName);
    }

    /// The ore-dictionary material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
