package gregtech.api.material;

import java.util.EnumSet;
import java.util.List;

import com.ruling_0.materiallib.api.Property;

/// Typed property keys GregTech attaches to MaterialLib materials; values referencing other materials use
/// [MaterialRef] because registration order is unspecified.
public class GTMaterialProperties {

    public static final Property<MaterialRef> ARC_SMELT_INTO = Property.of("gregtech", "arcSmeltInto");
    public static final Property<String> ARMOR_ENCHANTMENT = Property.of("gregtech", "armorEnchantment");
    public static final Property<Integer> ARMOR_ENCHANTMENT_LEVEL = Property.of("gregtech", "armorEnchantmentLevel");
    public static final Property<List<String>> ADDED_PREFIXES = Property.of("gregtech", "addedPrefixes");
    /// The exact `argb` legacy `MaterialBuilder#setARGB` was given, alpha byte included. Unlike
    /// [com.ruling_0.materiallib.api.StandardProperties#TINT], never substitutes alpha 0x00 (the common legacy
    /// default, meaningful to `Materials#mRGBa`) for 0xFF (needed for MaterialLib's own rendering only).
    public static final Property<Integer> ARGB = Property.of("gregtech", "argb");
    public static final Property<List<AspectRefStack>> ASPECTS = Property.of("gregtech", "aspects");
    public static final Property<Boolean> AUTO_BLAST_FURNACE_RECIPES = Property
        .of("gregtech", "autoBlastFurnaceRecipes");
    public static final Property<Boolean> AUTO_RECYCLE_RECIPES = Property.of("gregtech", "autoRecycleRecipes");
    public static final Property<Boolean> AUTO_VACUUM_FREEZER_RECIPES = Property
        .of("gregtech", "autoVacuumFreezerRecipes");
    public static final Property<Boolean> BLAST_REQUIRED = Property.of("gregtech", "blastRequired");
    public static final Property<Integer> BLAST_TEMP = Property.of("gregtech", "blastTemp");
    /// The Kelvin boiling point, present only for materials sourced from `gtpp-materials.json` (gtPlusPlus's
    /// `Material.boilingPointC`, converted -- see [GTppData]). GregTech's own dump never carried a boiling
    /// point, so unlike [#MELTING_POINT] this has no gt-materials.json-sourced counterpart.
    public static final Property<Integer> BOILING_POINT = Property.of("gregtech", "boilingPoint");
    public static final Property<Integer> BYPRODUCT_MULTIPLIER = Property.of("gregtech", "byProductMultiplier");
    public static final Property<Boolean> CAN_BE_CRACKED = Property.of("gregtech", "canBeCracked");
    public static final Property<List<MaterialRefStack>> COMPOSITION = Property.of("gregtech", "composition");
    /// The three hydro-cracked fluids (light/moderate/severe), present only when the dump captured a legacy
    /// hydro-cracked fluid for this material (see [#CAN_BE_CRACKED]).
    public static final Property<List<FluidRef>> CRACKED_HYDRO_FLUIDS = Property.of("gregtech", "crackedHydroFluids");
    /// As [#CRACKED_HYDRO_FLUIDS], for the steam-cracked fluids.
    public static final Property<List<FluidRef>> CRACKED_STEAM_FLUIDS = Property.of("gregtech", "crackedSteamFluids");
    public static final Property<Integer> DENSITY_DIVIDER = Property.of("gregtech", "densityDivider");
    public static final Property<Integer> DENSITY_MULTIPLIER = Property.of("gregtech", "densityMultiplier");
    public static final Property<MaterialRef> DIRECT_SMELTING = Property.of("gregtech", "directSmelting");
    public static final Property<Integer> DURABILITY = Property.of("gregtech", "durability");
    public static final Property<String> DYE = Property.of("gregtech", "dye");
    public static final Property<String> ELEMENT = Property.of("gregtech", "element");
    public static final Property<EnumSet<GTMaterialFlag>> FLAGS = Property.of("gregtech", "flags");
    public static final Property<Float> GAS_MULTIPLIER = Property.of("gregtech", "gasMultiplier");
    public static final Property<Integer> GAS_TEMP = Property.of("gregtech", "gasTemp");
    public static final Property<Integer> FUEL_POWER = Property.of("gregtech", "fuelPower");
    public static final Property<Integer> FUEL_TYPE = Property.of("gregtech", "fuelType");
    /// The legacy `Materials#mChemicalFormula` override string, present only for a material whose legacy
    /// declaration called `MaterialBuilder#setChemicalFormula` explicitly (the deleted `MaterialsInit`'s ~170
    /// non-derivable formulas -- isotope notation, joke/flavor formulas, etc.). A material without this
    /// property still has a real chemical-formula tooltip: `Materials`'s own constructor derives one from
    /// [#ELEMENT] or [#COMPOSITION] when no override is given, and [LegacyMaterials] reproduces that
    /// derivation unconditionally. Materials whose formula instead comes from a bartworks or gtPlusPlus fold
    /// carry it in [#WERKSTOFF]/[#GTPP] instead, never here.
    public static final Property<String> FORMULA = Property.of("gregtech", "formula");
    /// Whether [#FORMULA] is a `GTLanguageManager` localization key rather than literal text -- mirrors
    /// [GTWerkstoffFlag#LOCALIZED_FORMULA], the same distinction for a [#WERKSTOFF] formula.
    public static final Property<Boolean> FORMULA_LOCALIZED = Property.of("gregtech", "formulaLocalized");
    public static final Property<EnumSet<GTMaterialGenerationFlag>> GENERATION_FLAGS = Property
        .of("gregtech", "generationFlags");
    public static final Property<MaterialRef> HANDLE_MATERIAL = Property.of("gregtech", "handleMaterial");
    public static final Property<Boolean> HAS_CENTRIFUGE_RECIPE = Property.of("gregtech", "hasCentrifugeRecipe");
    public static final Property<Boolean> HAS_CORRESPONDING_FLUID = Property.of("gregtech", "hasCorrespondingFluid");
    public static final Property<Boolean> HAS_CORRESPONDING_GAS = Property.of("gregtech", "hasCorrespondingGas");
    public static final Property<Boolean> HAS_ELECTROLYZER_RECIPE = Property.of("gregtech", "hasElectrolyzerRecipe");
    public static final Property<Boolean> HAS_GLOWING_ORE = Property.of("gregtech", "hasGlowingOre");
    public static final Property<Float> HEAT_DAMAGE = Property.of("gregtech", "heatDamage");
    /// The gtPlusPlus-side data of a material that was (or merged with) a `gtPlusPlus.core.material.Material`
    /// -- see [GTppData].
    public static final Property<GTppData> GTPP = Property.of("gregtech", "gtpp");
    public static final Property<FluidNames> LEGACY_FLUIDS = Property.of("gregtech", "legacyFluids");
    public static final Property<String> LOCAL_NAME = Property.of("gregtech", "localName");
    public static final Property<MaterialRef> MACERATE_INTO = Property.of("gregtech", "macerateInto");
    /// The material's true legacy `mName`, when it contains characters `Names#validate` rejects (`:` or
    /// whitespace, e.g. `"Computation Base"`) and MaterialLib's own registration name is therefore a sanitized
    /// variant. Absent when the two already match -- see `ml_name()` in `scripts/mu/gen_materials.py`.
    public static final Property<String> LEGACY_NAME = Property.of("gregtech", "legacyName");
    public static final Property<MaterialRef> MATERIAL_INTO = Property.of("gregtech", "materialInto");
    public static final Property<Integer> MELTING_POINT = Property.of("gregtech", "meltingPoint");
    /// The exact `argbMolten` legacy `MaterialBuilder#setMoltenARGB` was given; see [#ARGB]. Absent when it
    /// equals [#ARGB] (legacy `Materials#mMoltenRGBa` then defaults to `mRGBa`, matching `setARGB`'s own
    /// default of setting both from one value).
    public static final Property<Integer> MOLTEN_ARGB = Property.of("gregtech", "moltenArgb");
    public static final Property<Integer> MOLTEN_TINT = Property.of("gregtech", "moltenTint");
    public static final Property<Integer> OLD_SUB_ID = Property.of("gregtech", "oldSubId");
    public static final Property<Integer> ORE_MULTIPLIER = Property.of("gregtech", "oreMultiplier");
    public static final Property<List<MaterialRefStack>> ORE_BYPRODUCTS = Property.of("gregtech", "oreByProducts");
    public static final Property<Float> PLASMA_MULTIPLIER = Property.of("gregtech", "plasmaMultiplier");
    public static final Property<Integer> PROCESSING_MATERIAL_TIER_EU = Property
        .of("gregtech", "processingMaterialTierEU");
    public static final Property<List<String>> REMOVED_PREFIXES = Property.of("gregtech", "removedPrefixes");
    public static final Property<MaterialRef> SMELT_INTO = Property.of("gregtech", "smeltInto");
    public static final Property<Integer> SMELTING_MULTIPLIER = Property.of("gregtech", "smeltingMultiplier");
    public static final Property<Float> STEAM_MULTIPLIER = Property.of("gregtech", "steamMultiplier");
    public static final Property<String> TOOL_ENCHANTMENT = Property.of("gregtech", "toolEnchantment");
    public static final Property<Integer> TOOL_ENCHANTMENT_LEVEL = Property.of("gregtech", "toolEnchantmentLevel");
    public static final Property<Integer> TOOL_QUALITY = Property.of("gregtech", "toolQuality");
    public static final Property<Float> TOOL_SPEED = Property.of("gregtech", "toolSpeed");
    public static final Property<Boolean> UNIFIABLE = Property.of("gregtech", "unifiable");
    /// The bartworks-side data of a material that was (or merged with) a `Werkstoff` -- see [WerkstoffData].
    public static final Property<WerkstoffData> WERKSTOFF = Property.of("gregtech", "werkstoff");

    private GTMaterialProperties() {}
}
