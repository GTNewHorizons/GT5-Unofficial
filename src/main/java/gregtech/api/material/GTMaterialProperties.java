package gregtech.api.material;

import java.util.EnumSet;
import java.util.List;

import com.ruling_0.materiallib.api.Property;

/// Typed property keys GregTech attaches to MaterialLib materials; values referencing other materials use
/// [MaterialRef] because registration order is unspecified.
public class GTMaterialProperties {

    public static final Property<MaterialRef> ARC_SMELT_INTO = Property.of("gregtech", "arcSmeltInto");
    public static final Property<List<AspectRefStack>> ASPECTS = Property.of("gregtech", "aspects");
    public static final Property<Boolean> BLAST_REQUIRED = Property.of("gregtech", "blastRequired");
    public static final Property<Integer> BLAST_TEMP = Property.of("gregtech", "blastTemp");
    public static final Property<Integer> BYPRODUCT_MULTIPLIER = Property.of("gregtech", "byProductMultiplier");
    public static final Property<List<MaterialRefStack>> COMPOSITION = Property.of("gregtech", "composition");
    public static final Property<MaterialRef> DIRECT_SMELTING = Property.of("gregtech", "directSmelting");
    public static final Property<Integer> DURABILITY = Property.of("gregtech", "durability");
    public static final Property<String> ELEMENT = Property.of("gregtech", "element");
    public static final Property<EnumSet<GTMaterialFlag>> FLAGS = Property.of("gregtech", "flags");
    public static final Property<Integer> FUEL_POWER = Property.of("gregtech", "fuelPower");
    public static final Property<Integer> FUEL_TYPE = Property.of("gregtech", "fuelType");
    public static final Property<Integer> GAS_TEMP = Property.of("gregtech", "gasTemp");
    public static final Property<MaterialRef> HANDLE_MATERIAL = Property.of("gregtech", "handleMaterial");
    public static final Property<Float> HEAT_DAMAGE = Property.of("gregtech", "heatDamage");
    public static final Property<FluidNames> LEGACY_FLUIDS = Property.of("gregtech", "legacyFluids");
    public static final Property<String> LOCAL_NAME = Property.of("gregtech", "localName");
    public static final Property<MaterialRef> MACERATE_INTO = Property.of("gregtech", "macerateInto");
    public static final Property<MaterialRef> MATERIAL_INTO = Property.of("gregtech", "materialInto");
    public static final Property<Integer> MELTING_POINT = Property.of("gregtech", "meltingPoint");
    public static final Property<Integer> MOLTEN_TINT = Property.of("gregtech", "moltenTint");
    public static final Property<Integer> OLD_SUB_ID = Property.of("gregtech", "oldSubId");
    public static final Property<Integer> ORE_MULTIPLIER = Property.of("gregtech", "oreMultiplier");
    public static final Property<List<MaterialRefStack>> ORE_BYPRODUCTS = Property.of("gregtech", "oreByProducts");
    public static final Property<MaterialRef> SMELT_INTO = Property.of("gregtech", "smeltInto");
    public static final Property<Integer> SMELTING_MULTIPLIER = Property.of("gregtech", "smeltingMultiplier");
    public static final Property<Integer> TOOL_QUALITY = Property.of("gregtech", "toolQuality");
    public static final Property<Float> TOOL_SPEED = Property.of("gregtech", "toolSpeed");

    private GTMaterialProperties() {}
}
