package gregtech.api.material;

import java.util.EnumSet;
import java.util.List;

import com.ruling_0.materiallib.api.MaterialRef;
import com.ruling_0.materiallib.api.Property;

/// Typed property keys GregTech attaches to MaterialLib materials. Values referencing other materials use
/// [MaterialRef], since registration order across mods is unspecified.
///
/// The `WERKSTOFF_IDS` key carries the identity saved `bw.*` blocks and items address a material by. It describes no
/// behaviour of the material itself.
public class GTMaterialProperties {

    /// The lang key of the NEI search alias every oredict-registered item of the material carries.
    public static final Property<String> ALIAS_KEY = Property.of("gregtech", "aliasKey");
    public static final Property<MaterialRef> ARC_SMELT_INTO = Property.of("gregtech", "arcSmeltInto");
    public static final Property<List<AspectRefStack>> ASPECTS = Property.of("gregtech", "aspects");
    public static final Property<Boolean> AUTO_BLAST_FURNACE_RECIPES = Property
        .of("gregtech", "autoBlastFurnaceRecipes", true);
    public static final Property<Boolean> AUTO_RECYCLE_RECIPES = Property.of("gregtech", "autoRecycleRecipes", true);
    public static final Property<Boolean> AUTO_VACUUM_FREEZER_RECIPES = Property
        .of("gregtech", "autoVacuumFreezerRecipes", true);
    /// The duration in ticks of the blast furnace recipe smelting this material's dust, in place of the formula
    /// derived from its mass and [#BLAST_TEMP].
    public static final Property<Integer> BLAST_DURATION = Property.of("gregtech", "blastDuration");
    public static final Property<Boolean> BLAST_REQUIRED = Property.of("gregtech", "blastRequired", false);
    public static final Property<Integer> BLAST_TEMP = Property.of("gregtech", "blastTemp", 0);
    /// The EU/t of the blast furnace recipe smelting this material's dust, in place of MV.
    public static final Property<Integer> BLAST_VOLTAGE = Property.of("gregtech", "blastVoltage");
    public static final Property<Integer> BYPRODUCT_MULTIPLIER = Property.of("gregtech", "byProductMultiplier", 1);
    public static final Property<Boolean> CAN_BE_CRACKED = Property.of("gregtech", "canBeCracked", false);
    public static final Property<List<MaterialRefStack>> COMPOSITION = Property.of("gregtech", "composition");
    public static final Property<Integer> DENSITY_DIVIDER = Property.of("gregtech", "densityDivider");
    public static final Property<Integer> DENSITY_MULTIPLIER = Property.of("gregtech", "densityMultiplier");
    public static final Property<MaterialRef> DIRECT_SMELTING = Property.of("gregtech", "directSmelting");
    public static final Property<Integer> DURABILITY = Property.of("gregtech", "durability", 0);
    /// The litres of this material's liquid or gas form one dust extracts into and solidifies from -- see
    /// [#HAS_CORRESPONDING_FLUID].
    public static final Property<Integer> DUST_FLUID_AMOUNT = Property.of("gregtech", "dustFluidAmount", 1000);
    public static final Property<String> DYE = Property.of("gregtech", "dye");
    public static final Property<String> ELEMENT = Property.of("gregtech", "element");
    public static final Property<EnumSet<GTMaterialFlag>> FLAGS = Property.of("gregtech", "flags");
    public static final Property<Float> GAS_MULTIPLIER = Property.of("gregtech", "gasMultiplier");
    public static final Property<Integer> FUEL_POWER = Property.of("gregtech", "fuelPower", 0);
    public static final Property<Integer> FUEL_TYPE = Property.of("gregtech", "fuelType", 0);
    /// The chemical formula display string, read through [MaterialFormulas].
    public static final Property<String> FORMULA = Property.of("gregtech", "formula");
    /// Whether the formula is translated from the material's `.ChemicalFormula` lang key instead of shown as
    /// [#FORMULA]'s literal text (see [MaterialFormulas]).
    public static final Property<Boolean> FORMULA_LOCALIZED = Property.of("gregtech", "formulaLocalized");
    /// The literal flavor line shown in italics under the formula on every stack of the material.
    public static final Property<String> FLAVOR_TEXT = Property.of("gregtech", "flavorText");
    /// The light level of the material's fluids, in place of each fluid shape's own.
    public static final Property<Integer> FLUID_LUMINOSITY = Property.of("gregtech", "fluidLuminosity");
    public static final Property<MaterialRef> HANDLE_MATERIAL = Property.of("gregtech", "handleMaterial");
    /// Whether a chemical-reactor recipe assembles this material's dust from its [#COMPOSITION].
    public static final Property<Boolean> HAS_CHEMICAL_RECIPE = Property.of("gregtech", "hasChemicalRecipe");
    public static final Property<Boolean> HAS_CENTRIFUGE_RECIPE = Property.of("gregtech", "hasCentrifugeRecipe", false);
    /// Whether this material's dust extracts into and solidifies from its liquid form, [#DUST_FLUID_AMOUNT] litres
    /// per dust.
    public static final Property<Boolean> HAS_CORRESPONDING_FLUID = Property
        .of("gregtech", "hasCorrespondingFluid", false);
    /// [#HAS_CORRESPONDING_FLUID], for its gas form, which takes precedence over the liquid form.
    public static final Property<Boolean> HAS_CORRESPONDING_GAS = Property.of("gregtech", "hasCorrespondingGas", false);
    public static final Property<Boolean> HAS_ELECTROLYZER_RECIPE = Property
        .of("gregtech", "hasElectrolyzerRecipe", false);
    public static final Property<Boolean> HAS_GLOWING_ORE = Property.of("gregtech", "hasGlowingOre");
    /// Whether a mixer recipe is generated from [#COMPOSITION] and [#MIX_CIRCUIT].
    public static final Property<Boolean> HAS_MIXER_RECIPE = Property.of("gregtech", "hasMixerRecipe");
    public static final Property<Float> HEAT_DAMAGE = Property.of("gregtech", "heatDamage", 0f);
    /// Whether the material is a superconductor. Set on members of
    /// [gregtech.api.enums.materials.Families#superconductors].
    public static final Property<Boolean> IS_SUPERCONDUCTOR = Property.of("gregtech", "isSuperconductor", false);
    /// Whether this material's composition decomposition feeds the lanthanide line, turning its cerium and samarium
    /// outputs into their ore concentrates (see `LanthanidesRecipeOutputs`).
    public static final Property<Boolean> LANTHANIDE_DECOMPOSITION = Property
        .of("gregtech", "lanthanideDecomposition", false);
    public static final Property<String> LOCAL_NAME = Property.of("gregtech", "localName");
    public static final Property<MaterialRef> MACERATE_INTO = Property.of("gregtech", "macerateInto");
    /// A declared atomic mass that replaces the [MaterialAtomics] composition formula for this material.
    /// Composition sums over components still use their computed values.
    public static final Property<Long> MASS = Property.of("gregtech", "mass");
    /// The programmed circuit number for the generated mixer recipe. No circuit is added when unset.
    public static final Property<Integer> MIX_CIRCUIT = Property.of("gregtech", "mixCircuit");
    /// The material's internal name when it contains characters MaterialLib names reject (`:` or whitespace,
    /// e.g. `"Computation Base"`). Absent when it matches the registration name.
    public static final Property<String> LEGACY_NAME = Property.of("gregtech", "legacyName");
    /// The Kelvin melting point, `-1` when undeclared. An undeclared point is derived from [#COMPOSITION] (see
    /// [MaterialUtils#meltingPoint]), while a declared `0` stays 0 K.
    public static final Property<Integer> MELTING_POINT = Property.of("gregtech", "meltingPoint", -1);
    /// The Kelvin temperature of the material's [gregtech.api.enums.materials.FluidShapes#fluidLiquid] fluid.
    public static final Property<Integer> LIQUID_TEMPERATURE = Property.of("gregtech", "liquidTemperature", 295);
    /// The Kelvin temperature of the material's [gregtech.api.enums.materials.FluidShapes#fluidGas] fluid.
    public static final Property<Integer> GAS_TEMPERATURE = Property.of("gregtech", "gasTemperature", 295);
    /// The Kelvin temperature of the material's [gregtech.api.enums.materials.FluidShapes#fluidPlasma] fluid.
    public static final Property<Integer> PLASMA_TEMPERATURE = Property.of("gregtech", "plasmaTemperature", 10000);
    /// The Kelvin temperature of the material's [gregtech.api.enums.materials.FluidShapes#fluidMolten] fluid, `-1`
    /// when undeclared. An undeclared temperature is the declared [#MELTING_POINT], or 1000 K without one.
    public static final Property<Integer> MOLTEN_TEMPERATURE = Property.of("gregtech", "moltenTemperature", -1);
    /// The Kelvin temperature of the material's six hydro- and steam-cracked fluids.
    public static final Property<Integer> CRACKED_TEMPERATURE = Property.of("gregtech", "crackedTemperature", 775);
    /// The EU/t of every fluid extractor recipe melting a part into this material's molten fluid, in place of the
    /// formula derived from the fluid's temperature.
    public static final Property<Integer> MELTING_VOLTAGE = Property.of("gregtech", "meltingVoltage");
    /// The ARGB molten-state tint, absent when the material uses its
    /// [com.ruling_0.materiallib.api.StandardProperties#TINT]. A resource pack can override it through the lang
    /// key `color.resource.gregtech.<MaterialName>.molten` (see [MaterialUtils#moltenRgba]).
    public static final Property<Integer> MOLTEN_ARGB = Property.of("gregtech", "moltenArgb");
    public static final Property<Integer> OLD_SUB_ID = Property.of("gregtech", "oldSubId");
    public static final Property<Integer> ORE_MULTIPLIER = Property.of("gregtech", "oreMultiplier", 1);
    public static final Property<List<MaterialRefStack>> ORE_BYPRODUCTS = Property.of("gregtech", "oreByProducts");
    /// The harvest level of this material's ore, in place of its [#TOOL_QUALITY]; its small ore takes one less.
    public static final Property<Integer> ORE_HARVEST_LEVEL = Property.of("gregtech", "oreHarvestLevel");
    public static final Property<Float> PLASMA_MULTIPLIER = Property.of("gregtech", "plasmaMultiplier");
    public static final Property<Integer> PROCESSING_MATERIAL_TIER_EU = Property
        .of("gregtech", "processingMaterialTierEU", 0);
    /// A declared proton count that replaces the [MaterialAtomics] composition formula for this material.
    /// Composition sums over components still use their computed values.
    public static final Property<Long> PROTONS = Property.of("gregtech", "protons");
    /// The radiation level a radioactive material's items and blocks inflict on the carrier; see
    /// [gregtech.api.hazards.ItemHazards#radiationLevel]. Absent when the material is not radioactive.
    public static final Property<Integer> RADIATION_LEVEL = Property.of("gregtech", "radiationLevel");
    public static final Property<MaterialRef> SMELT_INTO = Property.of("gregtech", "smeltInto");
    public static final Property<Integer> SMELTING_MULTIPLIER = Property.of("gregtech", "smeltingMultiplier", 1);
    /// The Forge fluid name of the material's solid form, such as crushed ice.
    public static final Property<String> SOLID_FLUID = Property.of("gregtech", "solidFluid");
    public static final Property<Float> STEAM_MULTIPLIER = Property.of("gregtech", "steamMultiplier");
    /// The material's tier, read by some recipe generators.
    public static final Property<Integer> TIER = Property.of("gregtech", "tier", 0);
    public static final Property<String> TOOL_ENCHANTMENT = Property.of("gregtech", "toolEnchantment");
    public static final Property<Integer> TOOL_ENCHANTMENT_LEVEL = Property.of("gregtech", "toolEnchantmentLevel");
    public static final Property<Integer> TOOL_QUALITY = Property.of("gregtech", "toolQuality", 0);
    public static final Property<Float> TOOL_SPEED = Property.of("gregtech", "toolSpeed", 1.0f);
    /// Whether the material poisons the carrier of an item or block made from it.
    public static final Property<Boolean> TOXIC = Property.of("gregtech", "toxic");
    public static final Property<Boolean> UNIFIABLE = Property.of("gregtech", "unifiable", true);
    /// Whether items in a player's inventory registered under one of this material's oredict names are swapped
    /// for its own item (see `bartworks.server.EventHandler.ServerEventHandler`). Not the inverse of
    /// [#UNIFIABLE], which opts a material out of unification.
    public static final Property<Boolean> ENFORCE_ORE_DICT_UNIFICATION = Property
        .of("gregtech", "enforceOreDictUnification");
    /// The duration in ticks of the vacuum freezer recipe cooling this material's hot ingot, in place of three ticks
    /// per unit of mass.
    public static final Property<Integer> VACUUM_FREEZER_DURATION = Property.of("gregtech", "vacuumFreezerDuration");
    /// The EU/t of the vacuum freezer recipe cooling this material's hot ingot, in place of MV.
    public static final Property<Integer> VACUUM_FREEZER_VOLTAGE = Property.of("gregtech", "vacuumFreezerVoltage");
    /// The material's voltage multiplier, read by some recipe generators.
    public static final Property<Long> VOLTAGE_MULTIPLIER = Property.of("gregtech", "voltageMultiplier", 16L);
    /// Every werkstoff id saved bartworks items and ore blocks address this material by. More than one when two
    /// same-name werkstoffe share this material. Permanent: saved worlds keep these ids indefinitely.
    public static final Property<List<Integer>> WERKSTOFF_IDS = Property.of("gregtech", "werkstoffIds");

    private GTMaterialProperties() {}
}
