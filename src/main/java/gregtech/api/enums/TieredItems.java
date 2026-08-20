package gregtech.api.enums;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

/// The non-circuit ore-dictionary entries that hang off a voltage tier -- batteries, high-pressure fluid pipes,
/// quadruple wires and alloy plates -- keyed by voltage tier.
///
/// Each tier's ore-dictionary material name is unrelated to the tier abbreviation (`LV` is named `Basic`, `UHV`
/// is named `Infinite`, and so on). Routing every call site through this enum keeps that mapping in one place.
///
/// Every prefix is offered at every tier even though only some combinations are registered; asking for an
/// unregistered one yields no stack.
///
/// Each prefix exposes two accessors. A `get...(int)` returns a unified [ItemStack] for recipe inputs and
/// outputs that need a concrete stack. A `get...Ingredient()` returns the name-only [ItemData] form for
/// crafting-grid recipes; see [gregtech.api.util.GTModHandler#addCraftingRecipe].
public enum TieredItems {

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

    TieredItems(String materialName) {
        this.materialName = materialName;
    }

    public ItemStack getBattery(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.battery.oreDictName(materialName), amount);
    }

    public ItemData getBatteryIngredient() {
        return new ItemData(OrePrefixes.battery, materialName);
    }

    public ItemStack getPipeSmall(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.pipeSmall.oreDictName(materialName), amount);
    }

    public ItemData getPipeSmallIngredient() {
        return new ItemData(OrePrefixes.pipeSmall, materialName);
    }

    public ItemStack getPipeMedium(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.pipeMedium.oreDictName(materialName), amount);
    }

    public ItemData getPipeMediumIngredient() {
        return new ItemData(OrePrefixes.pipeMedium, materialName);
    }

    public ItemStack getPipeLarge(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.pipeLarge.oreDictName(materialName), amount);
    }

    public ItemData getPipeLargeIngredient() {
        return new ItemData(OrePrefixes.pipeLarge, materialName);
    }

    public ItemStack getPipeHuge(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.pipeHuge.oreDictName(materialName), amount);
    }

    public ItemData getPipeHugeIngredient() {
        return new ItemData(OrePrefixes.pipeHuge, materialName);
    }

    public ItemStack getWireGt04(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.wireGt04.oreDictName(materialName), amount);
    }

    public ItemData getWireGt04Ingredient() {
        return new ItemData(OrePrefixes.wireGt04, materialName);
    }

    public ItemStack getPlateAlloy(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.plateAlloy.oreDictName(materialName), amount);
    }

    public ItemData getPlateAlloyIngredient() {
        return new ItemData(OrePrefixes.plateAlloy, materialName);
    }

    /// The ore-dictionary material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
