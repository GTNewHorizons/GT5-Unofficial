package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import gregtech.api.enums.StructureErrorId;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIndustrialVacuumFreezer;

public class StructureErrorRegistry {

    private static final StructureError[] registry = new StructureError[StructureErrorId.values().length];

    public static final StructureError BLOCK_NOT_LOADED = register(
        new NullaryStructureError(StructureErrorId.BLOCK_NOT_LOADED, "GT5U.gui.not_loaded"));
    public static final StructureError MISSING_MAINTENANCE = register(
        new NullaryStructureError(StructureErrorId.MISSING_MAINTENANCE, "GT5U.gui.text.no_maintenance"));
    public static final StructureError MISSING_MUFFLER = register(
        new NullaryStructureError(StructureErrorId.MISSING_MUFFLER, "GT5U.gui.text.no_muffler"));
    public static final StructureError UNNEEDED_MUFFLER = register(
        new NullaryStructureError(StructureErrorId.UNNEEDED_MUFFLER, "GT5U.gui.text.unneeded_muffler"));
    public static final StructureError MISSING_CRYO_HATCH = register(
        new NullaryStructureError(
            StructureErrorId.MISSING_CRYO_HATCH,
            "GT5U.gui.missing_hatch",
            new Object[] { MTEIndustrialVacuumFreezer.HATCH_NAME }));
    public static final StructureError TOO_MANY_CRYO_HATCHES = register(
        new NullaryStructureError(
            StructureErrorId.TOO_MANY_CRYO_HATCHES,
            "GT5U.gui.too_many_hatches",
            new Object[] { MTEIndustrialVacuumFreezer.HATCH_NAME, 1 }));
    public static final StructureError MISSING_STEAM_HATCH = register(
        new NullaryStructureError(
            StructureErrorId.MISSING_STEAM_HATCH,
            "GT5U.gui.missing_hatch",
            new Object[] { GregtechItemList.Hatch_Input_Steam.get(1)
                .getDisplayName() }));

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
