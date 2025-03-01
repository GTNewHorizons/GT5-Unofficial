package gregtech.common.modularui2.sync;

import net.minecraft.network.PacketBuffer;

import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.SyncHandler;

import gregtech.common.modularui2.factory.SelectItemGuiBuilder;

/**
 * Specifies action executed on server when item gets selected on item selection screen created via
 * {@link SelectItemGuiBuilder}.
 */
public class SelectItemServerAction extends SyncHandler {

    private final SelectItemGuiBuilder.OnSelectedAction onSelected;

    public SelectItemServerAction(SelectItemGuiBuilder.OnSelectedAction onSelected) {
        this.onSelected = onSelected;
    }

    public void send(int selected, MouseData mouseData) {
        syncToServer(0, buffer -> {
            buffer.writeVarIntToBuffer(selected);
            mouseData.writeToPacket(buffer);
        });
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) {}

    @Override
    public void readOnServer(int id, PacketBuffer buf) {
        if (id == 0) {
            int selected = buf.readVarIntFromBuffer();
            MouseData mouseData = MouseData.readPacket(buf);
            onSelected.accept(selected, mouseData);
        }
    }
}
