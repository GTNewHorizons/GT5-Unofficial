package gregtech.common.gui.modularui.adapter;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.common.tileentities.machines.MTEHatchInputBusME.Slot;

public class MTEHatchInputBusMESlotAdapter implements IByteBufAdapter<Slot> {

    @Override
    public Slot deserialize(PacketBuffer buffer) throws IOException {
        if (!buffer.readBoolean()) return null; // slot is null

        Slot slot = new Slot(buffer.readItemStackFromBuffer());

        if (buffer.readBoolean()) {
            slot.extracted = buffer.readItemStackFromBuffer();
            slot.extractedAmount = buffer.readInt();
        }

        return slot;
    }

    @Override
    public void serialize(PacketBuffer buffer, Slot slot) throws IOException {
        if (slot != null) {
            buffer.writeBoolean(true);
            buffer.writeItemStackToBuffer(slot.config);
            if (slot.extracted != null) {
                buffer.writeBoolean(true);
                buffer.writeItemStackToBuffer(slot.extracted);
                buffer.writeInt(slot.extractedAmount);
            } else buffer.writeBoolean(false);

        } else buffer.writeBoolean(false);
    }

    @Override
    public boolean areEqual(@NotNull Slot t1, @NotNull Slot t2) {
        return t1.equals(t2);
    }
}
