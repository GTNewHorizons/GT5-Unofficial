package gregtech.api.enums;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import gregtech.api.material.MarkerMaterial;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The non-circuit ore-dictionary entries that hang off a voltage tier -- batteries, high-pressure fluid pipes,
/// quadruple wires and alloy plates -- keyed by voltage tier.
///
/// Each tier is backed by a [MarkerMaterial] whose name is unrelated to the tier abbreviation (`LV` is named
/// `Basic`, `UHV` is named `Infinite`, and so on), so the ore-dictionary names these entries resolve to cannot be
/// spelled from the tier name. Routing every call site through this enum keeps that mapping in one place.
///
/// Every prefix is offered at every tier even though only some combinations are registered; asking for an
/// unregistered one yields no stack, exactly as looking the ore-dictionary name up directly would.
///
/// Ingredients come in two shapes, so each prefix exposes two accessors: a `get...(int)` returning a unified
/// [ItemStack] for recipe inputs and outputs that need a concrete stack, and a `get...Ingredient()` returning an
/// [ItemData] for crafting-grid recipes.
///
/// Crafting-grid recipes need the [ItemData] rather than the bare ore-dictionary name it stringifies to:
/// [gregtech.api.util.GTModHandler#addCraftingRecipe] derives the recycling output of a reversible recipe from the
/// [ItemData] of each ingredient, and a bare name carries no such association. The pipe and quadruple-wire prefixes
/// make this load-bearing -- they define a material amount, so their [ItemData] contributes to that output.
public enum TieredItems {

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

    TieredItems(Supplier<MarkerMaterial> marker) {
        this.marker = marker;
    }

    public ItemStack getBattery(int amount) {
        return get(OrePrefixes.battery, amount);
    }

    public ItemData getBatteryIngredient() {
        return ingredient(OrePrefixes.battery);
    }

    public ItemStack getPipeSmall(int amount) {
        return get(OrePrefixes.pipeSmall, amount);
    }

    public ItemData getPipeSmallIngredient() {
        return ingredient(OrePrefixes.pipeSmall);
    }

    public ItemStack getPipeMedium(int amount) {
        return get(OrePrefixes.pipeMedium, amount);
    }

    public ItemData getPipeMediumIngredient() {
        return ingredient(OrePrefixes.pipeMedium);
    }

    public ItemStack getPipeLarge(int amount) {
        return get(OrePrefixes.pipeLarge, amount);
    }

    public ItemData getPipeLargeIngredient() {
        return ingredient(OrePrefixes.pipeLarge);
    }

    public ItemStack getPipeHuge(int amount) {
        return get(OrePrefixes.pipeHuge, amount);
    }

    public ItemData getPipeHugeIngredient() {
        return ingredient(OrePrefixes.pipeHuge);
    }

    public ItemStack getWireGt04(int amount) {
        return get(OrePrefixes.wireGt04, amount);
    }

    public ItemData getWireGt04Ingredient() {
        return ingredient(OrePrefixes.wireGt04);
    }

    public ItemStack getPlateAlloy(int amount) {
        return get(OrePrefixes.plateAlloy, amount);
    }

    public ItemData getPlateAlloyIngredient() {
        return ingredient(OrePrefixes.plateAlloy);
    }

    private ItemStack get(OrePrefixes prefix, int amount) {
        return GTOreDictUnificator.get(prefix, marker.get(), amount);
    }

    private ItemData ingredient(OrePrefixes prefix) {
        return new ItemData(prefix, marker.get());
    }
}
