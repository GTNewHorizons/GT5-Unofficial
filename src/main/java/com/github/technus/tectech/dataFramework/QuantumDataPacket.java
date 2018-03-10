package com.github.technus.tectech.dataFramework;

import com.github.technus.tectech.Vec3pos;
import net.minecraft.nbt.NBTTagCompound;

public class QuantumDataPacket extends DataPacket<Long> {
    public QuantumDataPacket(Long content){
        super(content);
    }

    public QuantumDataPacket(NBTTagCompound compound){
        super(compound);
    }

    @Override
    protected Long contentFromNBT(NBTTagCompound nbt) {
        return nbt.getLong("computation");
    }

    @Override
    protected NBTTagCompound contentToNBT() {
        NBTTagCompound compound=new NBTTagCompound();
        compound.setLong("computation",content);
        return compound;
    }

    @Override
    public boolean extraCheck() {
        return true;
    }

    @Override
    protected Long unifyContentWith(Long content) {
        return this.content+content;
    }

    public QuantumDataPacket unifyTraceWith(Vec3pos... positions) {
        return (QuantumDataPacket) super.unifyTrace(positions);
    }

    public QuantumDataPacket unifyTraceWith(QuantumDataPacket p) {
        return (QuantumDataPacket) super.unifyTrace(p);
    }

    public QuantumDataPacket unifyPacketWith(QuantumDataPacket p) {
        return (QuantumDataPacket) super.unifyWith(p);
    }
}
