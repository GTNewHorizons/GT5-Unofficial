package gregtech.api.material;

import java.util.EnumSet;
import java.util.List;

import com.ruling_0.materiallib.api.MaterialRef;
import com.ruling_0.materiallib.api.Property;

/// Typed property keys GregTech attaches to MaterialLib materials; values referencing other materials use
/// [MaterialRef] because the target's static field is not assigned until its own registration runs, and
/// registration order across mods is unspecified.
///
/// The gtPlusPlus-originated (`GTPP_*`) and bartworks-originated (`WERKSTOFF_*`) groups are each decomposed
/// into individual keys rather than one composite property, so a reader needing a single value does not depend
/// on the whole record shape. [#GTPP_STATE] is the "this material carries gtPlusPlus data" signal and
/// [#WERKSTOFF_IDS] the bartworks equivalent; both are consulted directly by shape gating and the ore
/// adapters to tell the three material origins apart.
public class GTMaterialProperties {

    public static final Property<MaterialRef> ARC_SMELT_INTO = Property.of("gregtech", "arcSmeltInto");
    public static final Property<String> ARMOR_ENCHANTMENT = Property.of("gregtech", "armorEnchantment");
    public static final Property<Integer> ARMOR_ENCHANTMENT_LEVEL = Property.of("gregtech", "armorEnchantmentLevel");
    public static final Property<List<String>> ADDED_PREFIXES = Property.of("gregtech", "addedPrefixes");
    public static final Property<List<AspectRefStack>> ASPECTS = Property.of("gregtech", "aspects");
    public static final Property<Boolean> AUTO_BLAST_FURNACE_RECIPES = Property
        .of("gregtech", "autoBlastFurnaceRecipes", true);
    public static final Property<Boolean> AUTO_RECYCLE_RECIPES = Property.of("gregtech", "autoRecycleRecipes", true);
    public static final Property<Boolean> AUTO_VACUUM_FREEZER_RECIPES = Property
        .of("gregtech", "autoVacuumFreezerRecipes", true);
    public static final Property<Boolean> BLAST_REQUIRED = Property.of("gregtech", "blastRequired", false);
    public static final Property<Integer> BLAST_TEMP = Property.of("gregtech", "blastTemp", 0);
    /// The Kelvin boiling point, carried only by the gtPlusPlus-originated materials: unlike
    /// [#MELTING_POINT], no gregtech-originated material declares one.
    public static final Property<Integer> BOILING_POINT = Property.of("gregtech", "boilingPoint");
    public static final Property<Integer> BYPRODUCT_MULTIPLIER = Property.of("gregtech", "byProductMultiplier", 1);
    public static final Property<Boolean> CAN_BE_CRACKED = Property.of("gregtech", "canBeCracked", false);
    public static final Property<List<MaterialRefStack>> COMPOSITION = Property.of("gregtech", "composition");
    /// The blast-furnace gas recipe's consumed-amount multiplier, as `BlastFurnaceGasStat`'s recipe-time
    /// counterpart to [#EBF_GAS_TIME_MULTIPLIER].
    public static final Property<Double> EBF_GAS_AMOUNT_MULTIPLIER = Property.of("gregtech", "ebfGasAmountMultiplier");
    /// The blast-furnace gas recipe's duration multiplier, read by `BlastFurnaceGasStat` when generating a
    /// blast-furnace gas recipe for this material.
    public static final Property<Double> EBF_GAS_TIME_MULTIPLIER = Property.of("gregtech", "ebfGasTimeMultiplier");
    public static final Property<Integer> DENSITY_DIVIDER = Property.of("gregtech", "densityDivider");
    public static final Property<Integer> DENSITY_MULTIPLIER = Property.of("gregtech", "densityMultiplier");
    public static final Property<MaterialRef> DIRECT_SMELTING = Property.of("gregtech", "directSmelting");
    public static final Property<Integer> DURABILITY = Property.of("gregtech", "durability", 0);
    public static final Property<String> DYE = Property.of("gregtech", "dye");
    public static final Property<String> ELEMENT = Property.of("gregtech", "element");
    public static final Property<EnumSet<GTMaterialFlag>> FLAGS = Property.of("gregtech", "flags");
    public static final Property<Float> GAS_MULTIPLIER = Property.of("gregtech", "gasMultiplier");
    public static final Property<Integer> GAS_TEMP = Property.of("gregtech", "gasTemp");
    public static final Property<Integer> FUEL_POWER = Property.of("gregtech", "fuelPower", 0);
    public static final Property<Integer> FUEL_TYPE = Property.of("gregtech", "fuelType", 0);
    /// The single chemical-formula display string [MaterialFormulas] resolves, already cleaned up for display.
    public static final Property<String> FORMULA = Property.of("gregtech", "formula");
    /// Whether [#FORMULA] is a `GTLanguageManager` localization key rather than literal text (the legacy
    /// `setChemicalFormula` localized overload / the bartworks material's own localized-formula flag).
    public static final Property<Boolean> FORMULA_LOCALIZED = Property.of("gregtech", "formulaLocalized");
    public static final Property<EnumSet<GTMaterialGenerationFlag>> GENERATION_FLAGS = Property
        .of("gregtech", "generationFlags");
    public static final Property<MaterialRef> HANDLE_MATERIAL = Property.of("gregtech", "handleMaterial");
    /// Whether a chemical-reactor recipe assembles this material's dust from its [#COMPOSITION].
    public static final Property<Boolean> HAS_CHEMICAL_RECIPE = Property.of("gregtech", "hasChemicalRecipe");
    public static final Property<Boolean> HAS_CENTRIFUGE_RECIPE = Property.of("gregtech", "hasCentrifugeRecipe", false);
    public static final Property<Boolean> HAS_CORRESPONDING_FLUID = Property
        .of("gregtech", "hasCorrespondingFluid", false);
    public static final Property<Boolean> HAS_CORRESPONDING_GAS = Property.of("gregtech", "hasCorrespondingGas", false);
    public static final Property<Boolean> HAS_ELECTROLYZER_RECIPE = Property
        .of("gregtech", "hasElectrolyzerRecipe", false);
    /// Whether this material registers a gas-state fluid. Distinct from [#HAS_CORRESPONDING_GAS], which every
    /// cell-bearing werkstoff carries unconditionally and which is not gated on this.
    public static final Property<Boolean> HAS_GAS = Property.of("gregtech", "hasGas");
    public static final Property<Boolean> HAS_GLOWING_ORE = Property.of("gregtech", "hasGlowingOre");
    /// Whether the auto-generated Mixer recipe (from [#COMPOSITION] plus [#MIX_CIRCUIT]) should be built.
    public static final Property<Boolean> HAS_MIXER_RECIPE = Property.of("gregtech", "hasMixerRecipe");
    /// Whether the auto-generated Sifter recipe (crushed ore -> gem grades) should be built.
    public static final Property<Boolean> HAS_SIFTER_RECIPE = Property.of("gregtech", "hasSifterRecipe");
    public static final Property<Float> HEAT_DAMAGE = Property.of("gregtech", "heatDamage", 0f);
    /// Whether the material is radioactive, unifying the legacy bartworks material's own radioactivity flag and
    /// `Material.isRadioactive`.
    public static final Property<Boolean> IS_RADIOACTIVE = Property.of("gregtech", "isRadioactive");
    /// Whether the material is one of the superconductor wire markers -- the material a tier's superconductor
    /// wires unify under, carrying no composition of its own. Set on
    /// [gregtech.api.enums.materials.Families#superconductors].
    public static final Property<Boolean> IS_SUPERCONDUCTOR = Property.of("gregtech", "isSuperconductor", false);
    /// The `FluidRegistry` name of a gtPlusPlus-originated material's plasma fluid, pinned here because a
    /// merged material's plasma slot may hold a gregtech-side plasma instead. Backs
    /// [MaterialUtils#legacyGtppPlasmaOf].
    public static final Property<String> GTPP_PLASMA_NAME = Property.of("gregtech", "gtppPlasmaName");
    /// The gtPlusPlus material state (`SOLID`/`LIQUID`/`GAS`/...). Like [#WERKSTOFF_IDS], its presence doubles
    /// as the "this material carries gtPlusPlus data" signal, and is permanent for that reason.
    public static final Property<String> GTPP_STATE = Property.of("gregtech", "gtppState");
    public static final Property<String> LOCAL_NAME = Property.of("gregtech", "localName");
    public static final Property<MaterialRef> MACERATE_INTO = Property.of("gregtech", "macerateInto");
    /// A declared atomic mass that replaces the [MaterialAtomics] composition formula for this material, the
    /// [#PROTONS] counterpart. Composition sums over components still use their computed values.
    public static final Property<Long> MASS = Property.of("gregtech", "mass");
    /// The programmed-circuit number for the auto-generated Mixer recipe, elided when unset.
    public static final Property<Integer> MIX_CIRCUIT = Property.of("gregtech", "mixCircuit");
    /// The material's true legacy `mName`, when it contains characters `Names#validate` rejects (`:` or
    /// whitespace, e.g. `"Computation Base"`) and MaterialLib's own registration name is therefore a sanitized
    /// variant. Absent when the two already match.
    public static final Property<String> LEGACY_NAME = Property.of("gregtech", "legacyName");
    /// The Kelvin melting point, `-1` when the material declares none. A declared `0` is distinct from that
    /// default: it pins the material's fluids to 0 K, where an undeclared point is filled in from
    /// [#COMPOSITION] and otherwise falls back to room temperature (see [MaterialUtils#meltingPoint]).
    public static final Property<Integer> MELTING_POINT = Property.of("gregtech", "meltingPoint", -1);
    /// The EU/t voltage tier the auto-generated blast furnace recipe should require, elided when unset.
    public static final Property<Integer> MELTING_VOLTAGE = Property.of("gregtech", "meltingVoltage");
    /// The exact ARGB color declared for a material's molten-state tint. Absent when it would equal the
    /// material's [com.ruling_0.materiallib.api.StandardProperties#TINT], i.e. no separate molten tint was
    /// declared for the material. [MaterialUtils#moltenRgba] resolves the declared value through the lang key
    /// `color.resource.gregtech.<MaterialName>.molten`.
    public static final Property<Integer> MOLTEN_ARGB = Property.of("gregtech", "moltenArgb");
    public static final Property<Integer> OLD_SUB_ID = Property.of("gregtech", "oldSubId");
    public static final Property<Integer> ORE_MULTIPLIER = Property.of("gregtech", "oreMultiplier", 1);
    public static final Property<List<MaterialRefStack>> ORE_BYPRODUCTS = Property.of("gregtech", "oreByProducts");
    public static final Property<Float> PLASMA_MULTIPLIER = Property.of("gregtech", "plasmaMultiplier");
    public static final Property<Integer> PROCESSING_MATERIAL_TIER_EU = Property
        .of("gregtech", "processingMaterialTierEU", 0);
    /// A declared proton count that replaces the [MaterialAtomics] composition formula for this material.
    /// Composition sums over components still use their computed values.
    public static final Property<Long> PROTONS = Property.of("gregtech", "protons");
    /// The radiation level a radioactive material's carried items/blocks inflict, unifying the legacy
    /// `Material.vRadiationLevel` with bartworks' equivalent (which carried no scalar level of its own -- see
    /// [#IS_RADIOACTIVE]). Elided when `0`.
    public static final Property<Integer> RADIATION_LEVEL = Property.of("gregtech", "radiationLevel");
    public static final Property<List<String>> REMOVED_PREFIXES = Property.of("gregtech", "removedPrefixes");
    public static final Property<MaterialRef> SMELT_INTO = Property.of("gregtech", "smeltInto");
    public static final Property<Integer> SMELTING_MULTIPLIER = Property.of("gregtech", "smeltingMultiplier", 1);
    public static final Property<Float> STEAM_MULTIPLIER = Property.of("gregtech", "steamMultiplier");
    /// The explicitly-added `SubTag` names (contents-derived tags stay dynamic), elided when empty.
    public static final Property<List<String>> SUB_TAGS = Property.of("gregtech", "subTags");
    /// The legacy `Material.vTier`; no gregtech equivalent. Elided when `0`.
    public static final Property<Integer> TIER = Property.of("gregtech", "tier", 0);
    public static final Property<String> TOOL_ENCHANTMENT = Property.of("gregtech", "toolEnchantment");
    public static final Property<Integer> TOOL_ENCHANTMENT_LEVEL = Property.of("gregtech", "toolEnchantmentLevel");
    public static final Property<Integer> TOOL_QUALITY = Property.of("gregtech", "toolQuality", 0);
    public static final Property<Float> TOOL_SPEED = Property.of("gregtech", "toolSpeed", 1.0f);
    /// Whether the material poisons the carrier of an item/block made from it, from the legacy bartworks
    /// material's own toxicity flag.
    public static final Property<Boolean> TOXIC = Property.of("gregtech", "toxic");
    public static final Property<Boolean> UNIFIABLE = Property.of("gregtech", "unifiable", true);
    /// Whether item resolution should skip [gregtech.api.util.GTOreDictUnificator]'s existing association and
    /// forcibly re-`set` it to this material's own item for every prefix it carries. Not the inverse of
    /// [#UNIFIABLE]: that key opts a material out of unification, this one makes it win the ore-dict slot.
    public static final Property<Boolean> ENFORCE_ORE_DICT_UNIFICATION = Property
        .of("gregtech", "enforceOreDictUnification");
    /// The legacy `Material.vVoltageMultiplier`; no gregtech equivalent. Elided when `16` (the value every
    /// tier-0 material carries).
    public static final Property<Long> VOLTAGE_MULTIPLIER = Property.of("gregtech", "voltageMultiplier", 16L);
    /// Every legacy werkstoff `mID` this material covers -- more than one when two same-name werkstoffe folded
    /// into one MaterialLib declaration. This property is PERMANENT: saved worlds address bartworks items and
    /// ore blocks by their legacy `mID`, so [LegacyWerkstoffIndex] must keep
    /// decoding those ids indefinitely. It doubles as the "this material carries werkstoff data" signal, which
    /// is how the ore adapters and shape gating distinguish the werkstoff-origin materials from the gregtech
    /// and gtPlusPlus families.
    public static final Property<List<Integer>> WERKSTOFF_IDS = Property.of("gregtech", "werkstoffIds");
    /// The frozen list of `OrePrefixes` names a werkstoff-backed material's legacy part set covered, elided
    /// when empty. `sheetmetal` and `frameGt` never appear here: their membership is declared as a MaterialLib
    /// shape on the material itself ([gregtech.api.enums.materials.Materials]).
    public static final Property<List<String>> WERKSTOFF_PREFIXES = Property.of("gregtech", "werkstoffPrefixes");
    /// The legacy bartworks material-type enum constant name.
    public static final Property<String> WERKSTOFF_TYPE = Property.of("gregtech", "werkstoffType");

    private GTMaterialProperties() {}
}
