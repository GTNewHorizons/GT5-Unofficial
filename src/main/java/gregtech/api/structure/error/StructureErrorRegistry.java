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
    public static final StructureError UNKNOWN_STRUCTURE_ERROR = registerSingleton(
        StructureErrorId.UNKNOWN_STRUCTURE_ERROR,
        "GT5U.gui.text.unknown_structure_error");

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
