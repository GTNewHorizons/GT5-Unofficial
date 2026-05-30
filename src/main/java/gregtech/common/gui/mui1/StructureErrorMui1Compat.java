package gregtech.common.gui.mui1;

import java.io.IOException;
import java.util.List;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;

/**
 * MUI1 compatibility layer for displaying structure error diagnostics in the old multiblock GUI.
 * Delete this entire class when MUI1 multiblock support is removed.
 */
public class StructureErrorMui1Compat {

    public static void writeStructureError(PacketBuffer buffer, StructureError error) {
        try {
            StructureErrorRegistry.serialize(buffer, error);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static StructureError readStructureError(PacketBuffer buffer) {
        try {
            return StructureErrorRegistry.deserialize(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull DynamicPositionedColumn getDynamicPositionedColumn(List<StructureError> mui1ClientErrors) {
        DynamicPositionedColumn errorColumn = new DynamicPositionedColumn();
        errorColumn.setSynced(false)
            .setSpace(0);
        for (StructureError error : mui1ClientErrors) {
            if (error == StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR) continue;
            String text = error.getDisplayString();
            for (String line : text.split("\n")) {
                errorColumn.widget(
                    new TextWidget(line).setTextAlignment(Alignment.CenterLeft)
                        .setDefaultColor(0x777777));
            }
        }
        return errorColumn;
    }
}
