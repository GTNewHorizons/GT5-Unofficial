package gregtech.api.enums;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The tiered superconductor wire and cable ore-dictionary ingredients, keyed by voltage tier.
///
/// The wires and cables are `MTECable` MetaPipeEntities registered into the ore dictionary against a `Materials`
/// marker, so the only handle on them is the [OrePrefixes#wireGt01]..[OrePrefixes#wireGt16] and
/// [OrePrefixes#cableGt04] prefix on that marker. Each tier is backed by the marker rather than spelling an
/// ore-dictionary name, because the marker name does not track the tier abbreviation (`UHV` is named
/// `Superconductor`), so the resolved name cannot be reconstructed from the tier. Routing every call site through
/// this enum keeps that mapping in one place.
///
/// Ingredients come in two shapes, so each prefix exposes two accessors: a `get...(int)` returning a unified
/// [ItemStack] for recipe inputs and outputs that need a concrete stack, and a `get...Ingredient()` returning an
/// [ItemData] for crafting-grid recipes.
///
/// Crafting-grid recipes need the [ItemData] rather than the bare ore-dictionary name it stringifies to:
/// [gregtech.api.util.GTModHandler#addCraftingRecipe] derives the recycling output of a reversible recipe from the
/// [ItemData] of each ingredient, and a bare name carries no such association. The wire and cable prefixes define a
/// material amount, so their [ItemData] contributes to that output.
public enum Superconductors {

    MV(() -> Materials.SuperconductorMV),
    HV(() -> Materials.SuperconductorHV),
    EV(() -> Materials.SuperconductorEV),
    IV(() -> Materials.SuperconductorIV),
    LuV(() -> Materials.SuperconductorLuV),
    ZPM(() -> Materials.SuperconductorZPM),
    UV(() -> Materials.SuperconductorUV),
    UHV(() -> Materials.SuperconductorUHV),
    UEV(() -> Materials.SuperconductorUEV),
    UIV(() -> Materials.SuperconductorUIV),
    UMV(() -> Materials.SuperconductorUMV);

    private final Supplier<Materials> marker;

    Superconductors(Supplier<Materials> marker) {
        this.marker = marker;
    }

    public ItemStack getWireGt01(int amount) {
        return get(OrePrefixes.wireGt01, amount);
    }

    public ItemData getWireGt01Ingredient() {
        return ingredient(OrePrefixes.wireGt01);
    }

    public ItemStack getWireGt02(int amount) {
        return get(OrePrefixes.wireGt02, amount);
    }

    public ItemData getWireGt02Ingredient() {
        return ingredient(OrePrefixes.wireGt02);
    }

    public ItemStack getWireGt04(int amount) {
        return get(OrePrefixes.wireGt04, amount);
    }

    public ItemData getWireGt04Ingredient() {
        return ingredient(OrePrefixes.wireGt04);
    }

    public ItemStack getWireGt08(int amount) {
        return get(OrePrefixes.wireGt08, amount);
    }

    public ItemData getWireGt08Ingredient() {
        return ingredient(OrePrefixes.wireGt08);
    }

    public ItemStack getWireGt12(int amount) {
        return get(OrePrefixes.wireGt12, amount);
    }

    public ItemData getWireGt12Ingredient() {
        return ingredient(OrePrefixes.wireGt12);
    }

    public ItemStack getWireGt16(int amount) {
        return get(OrePrefixes.wireGt16, amount);
    }

    public ItemData getWireGt16Ingredient() {
        return ingredient(OrePrefixes.wireGt16);
    }

    public ItemStack getCableGt04(int amount) {
        return get(OrePrefixes.cableGt04, amount);
    }

    public ItemData getCableGt04Ingredient() {
        return ingredient(OrePrefixes.cableGt04);
    }

    private ItemStack get(OrePrefixes prefix, int amount) {
        return GTOreDictUnificator.get(prefix, marker.get(), amount);
    }

    private ItemData ingredient(OrePrefixes prefix) {
        return new ItemData(prefix, marker.get());
    }
}
