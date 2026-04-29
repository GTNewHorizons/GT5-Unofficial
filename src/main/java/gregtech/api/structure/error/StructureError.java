package gregtech.api.structure.error;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.api.widget.IWidget;

import gregtech.api.enums.StructureErrorId;

public interface StructureError {

    StructureErrorId getId();

    void serialize(PacketBuffer buffer) throws IOException;

    StructureError deserialize(PacketBuffer buffer) throws IOException;

    IWidget createWidget();

    StructureError copy();
}
