package gregtech.api.enums;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The tiered superconductor wire and cable ore-dictionary ingredients, keyed by voltage tier.
///
/// Each tier's ore-dictionary material name is unrelated to the tier abbreviation (`UHV` is named
/// `Superconductor`), so the ore-dictionary names these entries resolve to cannot be spelled from the tier name.
/// Routing every call site through this enum keeps that mapping in one place.
///
/// Ingredients come in two shapes, so each prefix exposes two accessors: a `get...(int)` returning a unified
/// [ItemStack] for recipe inputs and outputs that need a concrete stack, and a `get...Ingredient()` returning an
/// [ItemData] for crafting-grid recipes.
///
/// Crafting-grid recipes need the [ItemData] rather than the bare ore-dictionary name it stringifies to:
/// [gregtech.api.util.GTModHandler#addCraftingRecipe] derives the recycling output of a reversible recipe from the
/// [ItemData] of each ingredient, and a bare name carries no such association. The ingredient form is built from
/// the name-only [ItemData] constructor, which carries no composition, so these ingredients produce no recycling
/// output of their own.
public enum Superconductors {

    MV("SuperconductorMV"),
    HV("SuperconductorHV"),
    EV("SuperconductorEV"),
    IV("SuperconductorIV"),
    LuV("SuperconductorLuV"),
    ZPM("SuperconductorZPM"),
    UV("SuperconductorUV"),
    UHV("Superconductor"),
    UEV("SuperconductorUEV"),
    UIV("SuperconductorUIV"),
    UMV("SuperconductorUMV");

    private final String materialName;

    Superconductors(String materialName) {
        this.materialName = materialName;
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
        return GTOreDictUnificator.get(prefix.oreDictName(materialName), amount);
    }

    private ItemData ingredient(OrePrefixes prefix) {
        return new ItemData(prefix, materialName);
    }

    /// The ore-dictionary material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
