package gregtech.api.structure.error;

import java.io.IOException;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.widget.IWidget;

import gregtech.api.enums.StructureErrorId;

public class StructureErrorRegistry {

    private static final StructureError[] registry = new StructureError[StructureErrorId.values().length];

    public static final StructureError BLOCK_NOT_LOADED = registerSingleton(
        StructureErrorId.BLOCK_NOT_LOADED,
        "GT5U.gui.not_loaded");
    public static final StructureError MISSING_INPUT_BUS = registerSingleton(
        StructureErrorId.MISSING_INPUT_BUS,
        "GT5U.gui.text.missing_input_bus");
    public static final StructureError MISSING_OUTPUT_BUS = registerSingleton(
        StructureErrorId.MISSING_OUTPUT_BUS,
        "GT5U.gui.text.missing_output_bus");
    public static final StructureError MISSING_INPUT_HATCH = registerSingleton(
        StructureErrorId.MISSING_INPUT_HATCH,
        "GT5U.gui.text.missing_input_hatch");
    public static final StructureError MISSING_OUTPUT_HATCH = registerSingleton(
        StructureErrorId.MISSING_OUTPUT_HATCH,
        "GT5U.gui.text.missing_output_hatch");
    public static final StructureError TOO_MANY_OUTPUT_HATCH = registerSingleton(
        StructureErrorId.TOO_MANY_OUTPUT_HATCH,
        "GT5U.gui.text.too_many_output_hatch");
    public static final StructureError MISSING_ENERGY_HATCH = registerSingleton(
        StructureErrorId.MISSING_ENERGY_HATCH,
        "GT5U.gui.text.missing_energy_hatch");
    public static final StructureError ONE_ENERGY_HATCH_ON_MULTI_OR_LASER = registerSingleton(
        StructureErrorId.ONE_ENERGY_HATCH_ON_MULTI_OR_LASER,
        "GT5U.gui.text.one_energy_hatch_on_laser");
    public static final StructureError MISSING_MAINTENANCE = registerSingleton(
        StructureErrorId.MISSING_MAINTENANCE,
        "GT5U.gui.text.no_maintenance");
    public static final StructureError TOO_MANY_MAINTENANCE = registerSingleton(
        StructureErrorId.TOO_MANY_MAINTENANCE,
        "GT5U.gui.text.too_many_maintenance");
    public static final StructureError MISSING_MUFFLER = registerSingleton(
        StructureErrorId.MISSING_MUFFLER,
        "GT5U.gui.text.no_muffler");
    public static final StructureError UNNEEDED_MUFFLER = registerSingleton(
        StructureErrorId.UNNEEDED_MUFFLER,
        "GT5U.gui.text.unneeded_muffler");
    public static final StructureError TOO_MANY_MUFFLER = registerSingleton(
        StructureErrorId.TOO_MANY_MUFFLER,
        "GT5U.gui.text.too_many_muffler");
    public static final StructureError UNKNOWN_STRUCTURE_ERROR = registerSingleton(
        StructureErrorId.UNKNOWN_STRUCTURE_ERROR,
        "GT5U.gui.text.unknown_structure_error");
    public static final StructureError MISSING_ENERGY_HATCH = registerSingleton(
        StructureErrorId.MISSING_ENERGY_HATCH,
        "GT5U.gui.text.no_energy_hatch");
    public static final StructureError MISSING_DYNAMO_HATCH = registerSingleton(
        StructureErrorId.MISSING_DYNAMO_HATCH,
        "GT5U.gui.text.no_dynamo_hatch");
    public static final StructureError MISSING_INPUT_HATCH = registerSingleton(
        StructureErrorId.MISSING_INPUT_HATCH,
        "GT5U.gui.text.no_input_hatch");
    public static final StructureError MISSING_OUTPUT_HATCH = registerSingleton(
        StructureErrorId.MISSING_OUTPUT_HATCH,
        "GT5U.gui.text.no_output_hatch");
    public static final StructureError MISSING_INPUT_BUS = registerSingleton(
        StructureErrorId.MISSING_INPUT_BUS,
        "GT5U.gui.text.no_input_bus");
    public static final StructureError MISSING_OUTPUT_BUS = registerSingleton(
        StructureErrorId.MISSING_OUTPUT_BUS,
        "GT5U.gui.text.no_output_bus");
    public static final StructureError MISSING_BEAMLINE_INPUT = registerSingleton(
        StructureErrorId.MISSING_BEAMLINE_INPUT,
        "GT5U.gui.text.no_beamline_input");
    public static final StructureError MISSING_BEAMLINE_OUTPUT = registerSingleton(
        StructureErrorId.MISSING_BEAMLINE_OUTPUT,
        "GT5U.gui.text.no_beamline_output");

    static {
        StructureErrorRegistry.register(new WrongBlockError(0, 0, 0));
        StructureErrorRegistry.register(new TooFewCasings(0, 0));
        StructureErrorRegistry.register(new MissingHatch(0, 0));
        StructureErrorRegistry.register(new TooManyHatch(0, 0, 0));
        StructureErrorRegistry.register(new SimpleStructureError(""));
        StructureErrorRegistry.register(new MissingStructureWrapperCasings(new NBTTagList()));
        StructureErrorRegistry.register(new MissingOutputHatchDT(0));
    }

    public static StructureError registerSingleton(StructureErrorId id, String lang_key) {
        return register(new SingletonStructureError(id, lang_key));
    }

    public static StructureError registerSingleton(StructureErrorId id, Supplier<IWidget> widget) {
        return register(new SingletonStructureError(id, widget));
    }

    public static StructureError register(StructureError error) {
        registry[error.getId()
            .ordinal()] = error;
        return error;
    }

    public static void serialize(PacketBuffer buffer, StructureError error) throws IOException {
        buffer.writeInt(
            error.getId()
                .ordinal());
        error.serialize(buffer);
    }

    public static StructureError deserialize(PacketBuffer buffer) throws IOException {
        int id = buffer.readInt();
        return registry[id].deserialize(buffer);
    }
}
