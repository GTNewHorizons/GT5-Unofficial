package tectech.mechanics.dataTransport;

import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.Nullable;

import gregtech.api.util.GTUtility;

public class QuantumDataPacket extends DataPacket<Long> {

    public QuantumDataPacket(Long content) {
        super(content);
    }

    public QuantumDataPacket(NBTTagCompound compound) {
        super(compound);
    }

    @Override
    protected Long contentFromNBT(NBTTagCompound nbt) {
        return nbt.getLong("computation");
    }

    @Override
    protected NBTTagCompound contentToNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setLong("computation", content);
        return compound;
    }

    @Override
    protected Long unifyContentWith(Long content) {
        return GTUtility.addSafe(this.content, content);
    }

    public @Nullable QuantumDataPacket unifyTraceWith(long... positions) {
        return (QuantumDataPacket) super.unifyTrace(positions);
    }

    public QuantumDataPacket unifyTraceWith(QuantumDataPacket p) {
        return (QuantumDataPacket) super.unifyTrace(p);
    }

    public QuantumDataPacket unifyPacketWith(QuantumDataPacket p) {
        return (QuantumDataPacket) super.unifyWith(p);
    }
}
