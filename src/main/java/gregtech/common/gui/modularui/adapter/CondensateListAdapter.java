package gregtech.common.gui.modularui.adapter;

import java.io.IOException;
import java.util.Objects;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import tectech.mechanics.boseEinsteinCondensate.CondensateList;

public class CondensateListAdapter implements IByteBufAdapter<CondensateList> {

    @Override
    public CondensateList deserialize(PacketBuffer buffer) throws IOException {
        if (!buffer.readBoolean()) return null;

        CondensateList list = new CondensateList();
        list.readFromPacketBuffer(buffer);
        return list;
    }

    @Override
    public void serialize(PacketBuffer buffer, CondensateList list) throws IOException {
        if (list != null) {
            buffer.writeBoolean(true);
            list.writeToPacketBuffer(buffer);
        } else {
            buffer.writeBoolean(false);
        }
    }

    @Override
    public boolean areEqual(@NotNull CondensateList t1, @NotNull CondensateList t2) {
        return Objects.equals(t1, t2);
    }
}
