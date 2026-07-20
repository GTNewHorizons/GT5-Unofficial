package gregtech.api.enums;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import gregtech.api.material.MarkerMaterial;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The [OrePrefixes#circuit] and [OrePrefixes#wrapCircuit] ore-dictionary entries, keyed by voltage tier.
///
/// Each tier is backed by a [MarkerMaterial] whose name is unrelated to the tier abbreviation
/// (`LV` is named `Basic`, `UHV` is named `Infinite`, and so on), so the ore-dictionary names these entries resolve
/// to cannot be spelled from the tier name. Routing every call site through this enum keeps that mapping in one
/// place.
///
/// Ingredients come in two shapes, so each prefix exposes two accessors:
/// - [#get(int)] and [#getWrap(int)] return a unified [ItemStack] for recipe inputs and outputs that need a
/// concrete stack.
/// - [#getIngredient()] and [#getWrapIngredient()] return an [ItemData] for crafting-grid recipes.
///
/// Crafting-grid recipes need the [ItemData] rather than the bare ore-dictionary name it stringifies to:
/// [gregtech.api.util.GTModHandler#addCraftingRecipe] derives the recycling output of a reversible recipe from the
/// [ItemData] of each ingredient, and a bare name carries no such association.
public enum Circuits {

    ULV(() -> Materials.ULV),
    LV(() -> Materials.LV),
    MV(() -> Materials.MV),
    HV(() -> Materials.HV),
    EV(() -> Materials.EV),
    IV(() -> Materials.IV),
    LuV(() -> Materials.LuV),
    ZPM(() -> Materials.ZPM),
    UV(() -> Materials.UV),
    UHV(() -> Materials.UHV),
    UEV(() -> Materials.UEV),
    UIV(() -> Materials.UIV),
    UMV(() -> Materials.UMV),
    UXV(() -> Materials.UXV),
    MAX(() -> Materials.MAX);

    private final Supplier<MarkerMaterial> marker;

    Circuits(Supplier<MarkerMaterial> marker) {
        this.marker = marker;
    }

    public ItemStack get(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.circuit, marker.get(), amount);
    }

    public ItemData getIngredient() {
        return new ItemData(OrePrefixes.circuit, marker.get());
    }

    public ItemStack getWrap(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.wrapCircuit, marker.get(), amount);
    }

    public ItemData getWrapIngredient() {
        return new ItemData(OrePrefixes.wrapCircuit, marker.get());
    }
}
