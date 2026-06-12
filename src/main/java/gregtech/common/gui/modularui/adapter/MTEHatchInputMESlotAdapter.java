package gregtech.common.gui.modularui.adapter;

import java.io.IOException;

import net.minecraft.network.PacketBuffer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.MTEHatchInputME.Slot;

public class MTEHatchInputMESlotAdapter implements IByteBufAdapter<Slot> {

    @Override
    public Slot deserialize(PacketBuffer packetBuffer) throws IOException {
        if (!packetBuffer.readBoolean()) return null; // slot is null

        Slot slot = new Slot(GTUtility.loadFluid(packetBuffer.readNBTTagCompoundFromBuffer()));

        if (packetBuffer.readBoolean()) {
            slot.extracted = GTUtility.loadFluid(packetBuffer.readNBTTagCompoundFromBuffer());
            slot.extractedAmount = packetBuffer.readLong();
            slot.stockedAmount = packetBuffer.readLong();
            if (slot.extracted != null) slot.extracted = GTUtility.copyAmount(slot.extractedAmount, slot.extracted);
        }

        return slot;
    }

    @Override
    public void serialize(PacketBuffer packetBuffer, Slot slot) throws IOException {
        if (slot != null) {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeNBTTagCompoundToBuffer(GTUtility.saveFluid(slot.config));
            if (slot.extracted != null) {
                packetBuffer.writeBoolean(true);
                packetBuffer.writeNBTTagCompoundToBuffer(GTUtility.saveFluid(slot.extracted));
                packetBuffer.writeLong(slot.extractedAmount);
                packetBuffer.writeLong(slot.stockedAmount);
            } else packetBuffer.writeBoolean(false);

        } else packetBuffer.writeBoolean(false);
    }

    @Override
    public boolean areEqual(@NotNull Slot t1, @NotNull Slot t2) {
        return t1.equals(t2);
    }
}
