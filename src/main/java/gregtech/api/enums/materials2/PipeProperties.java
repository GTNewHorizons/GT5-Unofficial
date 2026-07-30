package gregtech.api.enums.materials2;

import com.ruling_0.materiallib.api.Property;

/// Typed property keys for the base stats of the wire/cable, fluid pipe, and item pipe families. Each key
/// holds a material's base value; per-size values derive from it through the transforms in
/// [gregtech.api.material.PipeStats]. The values themselves are set by [PipeMaterials].
public class PipeProperties {

    /// Base amperage of the 1x wire and cable; thicker sizes multiply it.
    public static final Property<Integer> BASE_CABLE_AMP = Property.of("gregtech", "baseCableAmp");
    /// Maximum voltage the material's wires and cables carry, constant across sizes.
    public static final Property<Long> BASE_CABLE_VOLT = Property.of("gregtech", "baseCableVolt");
    /// Voltage loss per meter of insulated cable; bare wire loss defaults to twice this (see [#WIRE_LOSS]).
    public static final Property<Integer> BASE_CABLE_LOSS = Property.of("gregtech", "baseCableLoss");
    /// Explicit bare-wire voltage loss per meter, for a material where the twice-[#BASE_CABLE_LOSS] rule does
    /// not hold or where [#NO_CABLE] leaves no cable loss to derive from.
    public static final Property<Integer> WIRE_LOSS = Property.of("gregtech", "wireLoss");
    /// The material generates bare wires only, no insulated cable variants.
    public static final Property<Boolean> NO_CABLE = Property.of("gregtech", "noCable");
    /// The material's bare wires deal no contact damage.
    public static final Property<Boolean> NO_SHOCK = Property.of("gregtech", "noShock");
    /// Fluid capacity of the medium fluid pipe; other sizes derive from it.
    public static final Property<Integer> BASE_PIPE_FLOW = Property.of("gregtech", "basePipeFlow");
    /// Maximum fluid temperature the material's fluid pipes withstand, constant across sizes.
    public static final Property<Integer> PIPE_HEAT_RESISTANCE = Property.of("gregtech", "pipeHeatResistance");
    /// Whether the material's fluid pipes carry gases.
    public static final Property<Boolean> PIPE_GAS_PROOF = Property.of("gregtech", "pipeGasProof", true);
    /// Inventory slot count of the huge item pipe; other sizes derive from it.
    public static final Property<Integer> BASE_ITEM_PIPE_SLOTS = Property.of("gregtech", "baseItemPipeSlots");
    /// The material generates no tiny or small item pipes.
    public static final Property<Boolean> NO_SMALL_ITEM_PIPES = Property.of("gregtech", "noSmallItemPipes");

    private PipeProperties() {}
}
