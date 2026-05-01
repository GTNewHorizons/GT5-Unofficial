package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.widget.IWidget;

import gregtech.api.enums.StructureErrorId;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public interface StructureError {

    StructureErrorId getId();

    void serialize(PacketBuffer buffer) throws IOException;

    StructureError deserialize(PacketBuffer buffer) throws IOException;

    IWidget createWidget(MTEMultiBlockBaseGui<?> gui);

    StructureError copy();
}
