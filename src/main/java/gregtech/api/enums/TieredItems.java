package gregtech.api.enums;

import static gregtech.api.enums.GTValues.W;

import java.util.function.Supplier;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTUtility;

/// The tiered battery and fluid pipe oredict entries by voltage tier. Like [Circuits], it keeps the mapping from
/// tier to oredict name in one place.
///
/// A `get...(int)` returns the tier's concrete item and throws for a tier without one. Only `LV` to `EV` have
/// batteries and only `ZPM` has pipes. A `get...Ingredient()` returns the oredict form for crafting recipes at
/// any tier; see [gregtech.api.util.GTModHandler#addCraftingRecipe].
public enum TieredItems {

    ULV("Primitive", null, null),
    LV("Basic", () -> ItemList.IC2_ReBattery.get(1), null),
    MV("Good", () -> ItemList.IC2_AdvBattery.getWithDamage(1, W), null),
    HV("Advanced", () -> ItemList.IC2_EnergyCrystal.getWithDamage(1, W), null),
    EV("Data", () -> ItemList.IC2_LapotronCrystal.getWithDamage(1, W), null),
    IV("Elite", null, null),
    LuV("Master", null, null),
    ZPM("Ultimate", null, () -> Materials.Redstone),
    UV("Superconductor", null, null),
    UHV("Infinite", null, null),
    UEV("Bio", null, null),
    UIV("Optical", null, null),
    UMV("Exotic", null, null),
    UXV("Cosmic", null, null),
    MAX("Transcendent", null, null);

    private final String materialName;
    private final Supplier<ItemStack> battery;
    private final Supplier<Material> pipeMaterial;

    TieredItems(String materialName, Supplier<ItemStack> battery, Supplier<Material> pipeMaterial) {
        this.materialName = materialName;
        this.battery = battery;
        this.pipeMaterial = pipeMaterial;
    }

    public boolean hasBattery() {
        return battery != null;
    }

    public ItemStack getBattery(int amount) {
        if (battery == null) throw new IllegalStateException("TieredItems." + name() + " declares no battery");
        return GTUtility.copyAmount(amount, battery.get());
    }

    public ItemData getBatteryIngredient() {
        return new ItemData(OrePrefixes.battery, materialName);
    }

    public ItemStack getPipeSmall(int amount) {
        return MaterialLibAPI.getStack(pipeMaterial(), TEBlockShapes.pipeSmall, amount);
    }

    public ItemData getPipeSmallIngredient() {
        return new ItemData(OrePrefixes.pipeSmall, materialName);
    }

    public ItemStack getPipeMedium(int amount) {
        return MaterialLibAPI.getStack(pipeMaterial(), TEBlockShapes.pipeMedium, amount);
    }

    public ItemData getPipeMediumIngredient() {
        return new ItemData(OrePrefixes.pipeMedium, materialName);
    }

    public ItemStack getPipeLarge(int amount) {
        return MaterialLibAPI.getStack(pipeMaterial(), TEBlockShapes.pipeLarge, amount);
    }

    public ItemData getPipeLargeIngredient() {
        return new ItemData(OrePrefixes.pipeLarge, materialName);
    }

    public ItemData getPipeHugeIngredient() {
        return new ItemData(OrePrefixes.pipeHuge, materialName);
    }

    private Material pipeMaterial() {
        if (pipeMaterial == null) throw new IllegalStateException("TieredItems." + name() + " declares no pipes");
        return pipeMaterial.get();
    }

    /// The oredict material name this constant stands for.
    public String materialName() {
        return materialName;
    }
}
