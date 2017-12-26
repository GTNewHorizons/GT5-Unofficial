package com.github.technus.tectech.dataFramework;

import com.github.technus.tectech.Vec3pos;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Tec on 05.04.2017.
 */
public class QuantumDataPacket {
    public static byte maxHistory = 64;

    public long computation = 0;
    public Set<Vec3pos> trace = new LinkedHashSet<>();

    public QuantumDataPacket(Vec3pos pos, long computation) {
        this.computation = computation;
        trace.add(pos);
    }

    public QuantumDataPacket(QuantumDataPacket q, long computation) {
        this.computation = computation;
        trace.addAll(q.trace);
    }

    public QuantumDataPacket(NBTTagCompound nbt) {
        computation = nbt.getLong("qComputation");
        for (int i = 0; i < nbt.getByte("qHistory"); i++) {
            trace.add(new Vec3pos(
                    nbt.getInteger("qX" + i),
                    nbt.getShort("qY" + i),
                    nbt.getInteger("qZ" + i)
            ));
        }
    }

    public NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setLong("qComputation", computation);
        nbt.setByte("qHistory", (byte) trace.size());
        int i = 0;
        for (Vec3pos v : trace) {
            nbt.setInteger("qX" + i, v.x);
            nbt.setShort("qY" + i, v.y);
            nbt.setInteger("qZ" + i, v.z);
            i++;
        }
        return nbt;
    }

    public boolean contains(Vec3pos v) {
        return trace.contains(v);
    }

    public boolean check() {
        return trace.size() <= maxHistory;
    }

    public QuantumDataPacket unifyTraceWith(QuantumDataPacket p) {
        trace.addAll(p.trace);
        return check() ? this : null;
    }

    public QuantumDataPacket unifyPacketWith(QuantumDataPacket p) {
        computation += p.computation;
        trace.addAll(p.trace);
        return check() ? this : null;
    }

    public long computationIfNotContained(Vec3pos pos) {
        if (trace.contains(pos)) {
            return 0;
        }
        return computation;
    }
}
