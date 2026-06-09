package gregtech.common.gui.modularui.adapter;

import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.utils.serialization.IByteBufAdapter;

import gregtech.common.tileentities.machines.MTEHatchInputME.Slot;

public class MTEHatchInputMESlotAdapter implements IByteBufAdapter<Slot> {

    @Override
    public Slot deserialize(PacketBuffer packetBuffer) throws IOException {
        if (!packetBuffer.readBoolean()) return null; // slot is null

        Slot slot = new Slot(FluidStack.loadFluidStackFromNBT(packetBuffer.readNBTTagCompoundFromBuffer()));

        if (packetBuffer.readBoolean()) {
            slot.extracted = FluidStack.loadFluidStackFromNBT(packetBuffer.readNBTTagCompoundFromBuffer());
            slot.extractedAmount = packetBuffer.readInt();
        }

        return slot;
    }

    @Override
    public void serialize(PacketBuffer packetBuffer, Slot slot) throws IOException {
        if (slot != null) {
            packetBuffer.writeBoolean(true);
            packetBuffer.writeNBTTagCompoundToBuffer(slot.config.writeToNBT(new NBTTagCompound()));
            if (slot.extracted != null) {
                packetBuffer.writeBoolean(true);
                packetBuffer.writeNBTTagCompoundToBuffer(slot.extracted.writeToNBT(new NBTTagCompound()));
                packetBuffer.writeInt(slot.extractedAmount);
            } else packetBuffer.writeBoolean(false);

        } else packetBuffer.writeBoolean(false);
    }

    @Override
    public boolean areEqual(@NotNull Slot t1, @NotNull Slot t2) {
        return t1.equals(t2);
    }
}
