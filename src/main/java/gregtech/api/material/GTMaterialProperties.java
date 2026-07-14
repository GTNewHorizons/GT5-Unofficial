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
    /// `Material.boilingPointC`, converted with the same formula `Material`'s own `MathUtils#celsiusToKelvin`
    /// used). GregTech's own dump never carried a boiling point, so unlike [#MELTING_POINT] this has no
    /// gt-materials.json-sourced counterpart.
    public static final Property<Integer> BOILING_POINT = Property.of("gregtech", "boilingPoint");
    public static final Property<Integer> BYPRODUCT_MULTIPLIER = Property.of("gregtech", "byProductMultiplier");
    public static final Property<Boolean> CAN_BE_CRACKED = Property.of("gregtech", "canBeCracked");
    public static final Property<List<MaterialRefStack>> COMPOSITION = Property.of("gregtech", "composition");
    /// The blast-furnace gas recipe's consumed-amount multiplier, as `BlastFurnaceGasStat`'s recipe-time
    /// counterpart to [#EBF_GAS_TIME_MULTIPLIER].
    public static final Property<Double> EBF_GAS_AMOUNT_MULTIPLIER = Property.of("gregtech", "ebfGasAmountMultiplier");
    /// The blast-furnace gas recipe's duration multiplier, read by `BlastFurnaceGasStat` when generating a
    /// blast-furnace gas recipe for this material.
    public static final Property<Double> EBF_GAS_TIME_MULTIPLIER = Property.of("gregtech", "ebfGasTimeMultiplier");
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
    /// The single chemical-formula display string [MaterialFormulas] resolves, whichever legacy system
    /// sourced it: a werkstoff-backed material carries its `Werkstoff` formula-tooltip string (it beat any
    /// same-name gtpp value), a gregtech-dumped material the `Materials#mChemicalFormula` override its legacy
    /// declaration set explicitly (see [gregtech.api.enums.materials2.Materials2Formulas], which also beat
    /// any same-name gtpp value), and a remaining gtpp material its `Material#vChemicalFormula` as displayed
    /// (the legacy renderer's `StringUtils#sanitizeStringKeepBrackets` cleanup baked in). A gregtech-dumped
    /// material without this property still has a real chemical-formula tooltip: `Materials`'s own
    /// constructor derives one from [#ELEMENT] or [#COMPOSITION] when no override is given, and
    /// [LegacyMaterials] reproduces that derivation unconditionally.
    public static final Property<String> FORMULA = Property.of("gregtech", "formula");
    /// Whether [#FORMULA] is a `GTLanguageManager` localization key rather than literal text (the legacy
    /// `setChemicalFormula` localized overload / `Werkstoff#isFormulaNeededLocalized`).
    public static final Property<Boolean> FORMULA_LOCALIZED = Property.of("gregtech", "formulaLocalized");
    public static final Property<EnumSet<GTMaterialGenerationFlag>> GENERATION_FLAGS = Property
        .of("gregtech", "generationFlags");
    public static final Property<MaterialRef> HANDLE_MATERIAL = Property.of("gregtech", "handleMaterial");
    /// Whether a chemical-reactor recipe assembles this material's dust from its [#COMPOSITION] (the werkstoff
    /// facade's `GenerationFeatures#hasChemicalRecipes` marker). No canonical `Processing*` generator reads
    /// this yet.
    public static final Property<Boolean> HAS_CHEMICAL_RECIPE = Property.of("gregtech", "hasChemicalRecipe");
    public static final Property<Boolean> HAS_CENTRIFUGE_RECIPE = Property.of("gregtech", "hasCentrifugeRecipe");
    public static final Property<Boolean> HAS_CORRESPONDING_FLUID = Property.of("gregtech", "hasCorrespondingFluid");
    public static final Property<Boolean> HAS_CORRESPONDING_GAS = Property.of("gregtech", "hasCorrespondingGas");
    public static final Property<Boolean> HAS_ELECTROLYZER_RECIPE = Property.of("gregtech", "hasElectrolyzerRecipe");
    /// Whether this material registers a gas-state fluid (the legacy `Materials#mHasGas`/bartworks
    /// `Werkstoff.Stats#isGas`), read by the werkstoff facade's fluid-registration-temperature formula and
    /// composition-decomposition item-vs-fluid split. Distinct from [#HAS_CORRESPONDING_GAS], which every
    /// cell-bearing werkstoff carries unconditionally for a different purpose (the bridge `Materials#mGas`
    /// mirror) and is not gated on this.
    public static final Property<Boolean> HAS_GAS = Property.of("gregtech", "hasGas");
    public static final Property<Boolean> HAS_GLOWING_ORE = Property.of("gregtech", "hasGlowingOre");
    /// Whether the werkstoff facade's fluid-solidifier autogen builds the stickLong/stick/plate molds from
    /// [MoltenCellLoader] (the `GenerationFeatures#hasMetalCraftingSolidifierRecipes` marker). The canonical
    /// legacy-pipeline solidifier
    /// autogen (`gregtech.loaders.oreprocessing.ProcessingIngot` et al) is unconditional on a registered molten
    /// fluid and does not need this gate, but it only reaches a material once its item shapes are `ShapeConsumer`-
    /// reachable -- bartworks item generation has not cut over to MaterialLib shapes yet (see
    /// `WerkstoffLoader`'s own meta-item system), so removing this gate outright drops the recipe entirely for
    /// its carriers instead of falling through to the canonical generator; kept as a marker until that cutover.
    public static final Property<Boolean> HAS_METAL_CRAFTING_SOLIDIFIER_RECIPE = Property
        .of("gregtech", "hasMetalCraftingSolidifierRecipe");
    /// As [#HAS_METAL_CRAFTING_SOLIDIFIER_RECIPE], for `MoltenCellLoader`'s screw/gear/gearSmall/bolt/ring/rotor
    /// molds (the `GenerationFeatures#hasMetaSolidifierRecipes` marker).
    public static final Property<Boolean> HAS_METAL_SOLIDIFIER_RECIPE = Property
        .of("gregtech", "hasMetalSolidifierRecipe");
    /// Whether the auto-generated Mixer recipe (from [#COMPOSITION] plus [#MIX_CIRCUIT]) should be built (the
    /// werkstoff facade's `GenerationFeatures#hasMixerRecipes` marker). No canonical `Processing*` generator
    /// reads this yet.
    public static final Property<Boolean> HAS_MIXER_RECIPE = Property.of("gregtech", "hasMixerRecipe");
    /// Whether the auto-generated Sifter recipe (crushed ore -> gem grades) should be built (the werkstoff
    /// facade's `GenerationFeatures#hasSifterRecipes` marker). No canonical `Processing*` generator reads
    /// this yet.
    public static final Property<Boolean> HAS_SIFTER_RECIPE = Property.of("gregtech", "hasSifterRecipe");
    public static final Property<Float> HEAT_DAMAGE = Property.of("gregtech", "heatDamage");
    /// Whether the material is radioactive, unifying the legacy `Werkstoff.Stats.isRadioactive` and
    /// `Material.isRadioactive`.
    public static final Property<Boolean> IS_RADIOACTIVE = Property.of("gregtech", "isRadioactive");
    /// The `gtPlusPlus.core.material.Material` scalar data of a material that was (or merged with) a legacy
    /// gtpp material, decomposed into individual keys rather than kept in one composite property so a reader
    /// needing a single value does not depend on the whole gtpp record shape. Every `GTPP_*` property below
    /// exists solely so [gtPlusPlus.core.material.MaterialReconstruction] can rebuild the deprecated
    /// gtPlusPlus `Material` facade, and is removed together with that facade in 5.10.0.0.
    ///
    /// [#GTPP_STATE] is always present on a material carrying any gtpp data -- reconstruction and other
    /// consumers use it (rather than any single scalar below, several of which elide their common default) as
    /// the "this material has gtpp data" signal.
    ///
    /// The legacy `Material` constructor's `vGenerateCells` flag, elided when `false`.
    public static final Property<Boolean> GTPP_GENERATES_CELLS = Property.of("gregtech", "gtppGeneratesCells");
    /// The legacy `Material` constructor's `generateFluid` flag, elided when `false`.
    public static final Property<Boolean> GTPP_GENERATES_FLUID = Property.of("gregtech", "gtppGeneratesFluid");
    /// The exact `FluidRegistry` name a legacy gtpp `Material#performFluidAndCellRegistration` registered
    /// this material's plasma fluid under, present only for the 37 materials where it is not
    /// [FluidNames#plasma] on [GTMaterialProperties#LEGACY_FLUIDS] -- unlike the non-plasma fluid slot (whose
    /// [FluidNames#molten]/[FluidNames#fluid]/[FluidNames#gas] priority reliably reconstructs
    /// [#GTPP_GENERATES_FLUID]'s fluid), a merged material's combined `LEGACY_FLUIDS.plasma` may instead be a
    /// gregtech-side plasma sharing the slot (e.g. every noble gas/metal element gregtech itself plasma-ionizes,
    /// none of which gtpp itself ever registered a plasma for), so it cannot be trusted as gtpp's own
    /// contribution without this pin.
    public static final Property<String> GTPP_PLASMA_NAME = Property.of("gregtech", "gtppPlasmaName");
    /// The legacy `gtPlusPlus.core.material.state.MaterialState` enum constant name -- see the class javadoc
    /// for why this, not a scalar below, is the presence signal for "this material carries gtpp data".
    public static final Property<String> GTPP_STATE = Property.of("gregtech", "gtppState");
    public static final Property<FluidNames> LEGACY_FLUIDS = Property.of("gregtech", "legacyFluids");
    public static final Property<String> LOCAL_NAME = Property.of("gregtech", "localName");
    public static final Property<MaterialRef> MACERATE_INTO = Property.of("gregtech", "macerateInto");
    /// The programmed-circuit number for the auto-generated Mixer recipe, elided when unset.
    public static final Property<Integer> MIX_CIRCUIT = Property.of("gregtech", "mixCircuit");
    /// The material's true legacy `mName`, when it contains characters `Names#validate` rejects (`:` or
    /// whitespace, e.g. `"Computation Base"`) and MaterialLib's own registration name is therefore a sanitized
    /// variant. Absent when the two already match -- see `ml_name()` in `scripts/mu/gen_materials.py`.
    public static final Property<String> LEGACY_NAME = Property.of("gregtech", "legacyName");
    public static final Property<Integer> MELTING_POINT = Property.of("gregtech", "meltingPoint");
    /// The EU/t voltage tier the auto-generated blast furnace recipe should require, elided when unset.
    public static final Property<Integer> MELTING_VOLTAGE = Property.of("gregtech", "meltingVoltage");
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
    /// The radiation level a radioactive material's carried items/blocks inflict, unifying the legacy
    /// `Material.vRadiationLevel` with bartworks' equivalent (which carried no scalar level of its own -- see
    /// [#IS_RADIOACTIVE]). Elided when `0`.
    public static final Property<Integer> RADIATION_LEVEL = Property.of("gregtech", "radiationLevel");
    public static final Property<List<String>> REMOVED_PREFIXES = Property.of("gregtech", "removedPrefixes");
    public static final Property<MaterialRef> SMELT_INTO = Property.of("gregtech", "smeltInto");
    public static final Property<Integer> SMELTING_MULTIPLIER = Property.of("gregtech", "smeltingMultiplier");
    public static final Property<Float> STEAM_MULTIPLIER = Property.of("gregtech", "steamMultiplier");
    /// The explicitly-added `SubTag` names (contents-derived tags stay dynamic), elided when empty. From the
    /// legacy bartworks `Werkstoff` `SUBTAGS`, consulted broadly by recipe-gen/tooltip logic gating on a
    /// material's kind (gas-type gating, no-blast gating, etc.).
    public static final Property<List<String>> SUB_TAGS = Property.of("gregtech", "subTags");
    /// The legacy `Material.vTier`; no gregtech equivalent. Elided when `0`. Read pervasively by gtPlusPlus
    /// `RecipeGen*` codegen (plates, recycling, shaped crafting, conduits) and Forestry bee-drop classes to set
    /// recipe EU cost/tier.
    public static final Property<Integer> TIER = Property.of("gregtech", "tier");
    public static final Property<String> TOOL_ENCHANTMENT = Property.of("gregtech", "toolEnchantment");
    public static final Property<Integer> TOOL_ENCHANTMENT_LEVEL = Property.of("gregtech", "toolEnchantmentLevel");
    public static final Property<Integer> TOOL_QUALITY = Property.of("gregtech", "toolQuality");
    public static final Property<Float> TOOL_SPEED = Property.of("gregtech", "toolSpeed");
    /// Whether the material poisons the carrier of an item/block made from it, from the legacy
    /// `Werkstoff.Stats.isToxic`.
    public static final Property<Boolean> TOXIC = Property.of("gregtech", "toxic");
    public static final Property<Boolean> UNIFIABLE = Property.of("gregtech", "unifiable");
    /// Whether item resolution should skip [gregtech.api.util.GTOreDictUnificator]'s existing association and
    /// forcibly re-`set` it to this material's own item for every prefix it carries (the werkstoff facade's
    /// `GenerationFeatures#enforceUnification` marker, read by
    /// `StaticRecipeChangeLoaders#unificationRecipeEnforcer` and
    /// `WerkstoffLoader#getCorrespondingItemStackUnsafe`). Opposite in spirit to [#UNIFIABLE]`(false)` (which
    /// opts a material *out* of unification) -- this opts a material *in* to forcibly winning the ore-dict slot,
    /// so the two are not interchangeable.
    public static final Property<Boolean> ENFORCE_ORE_DICT_UNIFICATION = Property
        .of("gregtech", "enforceOreDictUnification");
    /// The legacy `Material.vVoltageMultiplier`; no gregtech equivalent. Elided when `16` (the value every
    /// tier-0 material carries).
    public static final Property<Long> VOLTAGE_MULTIPLIER = Property.of("gregtech", "voltageMultiplier");
    /// The bartworks-side data of a material that was (or merged with) a `Werkstoff`, decomposed into
    /// individual keys rather than kept in one composite property so a reader needing a single value
    /// does not depend on the whole werkstoff record shape. Every `WERKSTOFF_*` property
    /// below exists solely so [bartworks.system.material.WerkstoffReconstruction] can rebuild the deprecated
    /// bartworks `Werkstoff` facade, and is removed together with that facade in 5.10.0.0.
    ///
    /// [#WERKSTOFF_IDS] is always present on a material carrying any werkstoff data -- reconstruction and
    /// other consumers use it as the "this material has werkstoff data" signal, and its first element as the
    /// declaration-order sort key (every legacy pool declares its werkstoffe in ascending id order).
    /// Every legacy werkstoff `mID` this material covers (more than one when two same-name werkstoffe folded
    /// into one MaterialLib declaration) -- see the class javadoc for its role as both the presence signal and
    /// reconstruction's declaration-order sort key.
    public static final Property<List<Integer>> WERKSTOFF_IDS = Property.of("gregtech", "werkstoffIds");
    /// The declaring pool identifier (`WerkstoffLoader`/`GGMaterial`/`WerkstoffMaterialPool`/
    /// `BotWerkstoffMaterialPool`/...), consulted by reconstruction's `ownerOf` for the legacy
    /// `Werkstoff#getOwner` attribution.
    public static final Property<String> WERKSTOFF_POOL = Property.of("gregtech", "werkstoffPool");
    /// The dumped `generatedPrefixes` ground truth (every `OrePrefixes` name `hasItemType` reported),
    /// including the prefixes that stay on legacy blocks (`sheetmetal`, `frameGt`) and so have no MaterialLib
    /// shape. Elided when empty.
    public static final Property<List<String>> WERKSTOFF_PREFIXES = Property.of("gregtech", "werkstoffPrefixes");
    /// The `Werkstoff.Types` enum constant name.
    public static final Property<String> WERKSTOFF_TYPE = Property.of("gregtech", "werkstoffType");

    private GTMaterialProperties() {}
}
