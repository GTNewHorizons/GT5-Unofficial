package tectech.mechanics.dataTransport;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

/**
 * Created by Tec on 05.04.2017.
 */
public abstract class DataPacket<T> {

    private static final byte MAX_HISTORY = 64;
    private final Set<Vec3Impl> trace = new LinkedHashSet<>();

    protected T content;

    protected DataPacket(T content) {
        this.content = content;
    }

    protected DataPacket(NBTTagCompound nbt) {
        content = contentFromNBT(nbt.getCompoundTag("qContent"));
        for (int i = 0; i < nbt.getByte("qHistory"); i++) {
            trace.add(new Vec3Impl(nbt.getInteger("qX" + i), nbt.getInteger("qY" + i), nbt.getInteger("qZ" + i)));
        }
    }

    public final NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound contentTag = contentToNBT();
        if (contentTag != null) {
            nbt.setTag("qContent", contentTag);
        }
        nbt.setByte("qHistory", (byte) trace.size());
        int i = 0;
        for (Vec3Impl v : trace) {
            nbt.setInteger("qX" + i, v.get0());
            nbt.setInteger("qY" + i, v.get1());
            nbt.setInteger("qZ" + i, v.get2());
            i++;
        }
        return nbt;
    }

    protected abstract NBTTagCompound contentToNBT();

    protected abstract T contentFromNBT(NBTTagCompound nbt);

    protected abstract T unifyContentWith(T content);

    public final boolean contains(Vec3Impl v) {
        return trace.contains(v);
    }

    public final boolean check() {
        return trace.size() <= MAX_HISTORY;
    }

    public abstract boolean extraCheck();

    protected final DataPacket<T> unifyTrace(Vec3Impl... positions) {
        Collections.addAll(trace, positions);
        return (check() && extraCheck()) ? this : null;
    }

    protected final DataPacket<T> unifyTrace(DataPacket<T> p) {
        if (p == null) return this;
        trace.addAll(p.trace);
        return (check() && extraCheck()) ? this : null;
    }

    protected final DataPacket<T> unifyWith(DataPacket<T> p) {
        if (p == null) return this;
        trace.addAll(p.trace);
        if (check() && extraCheck()) {
            content = unifyContentWith(p.content);
            return this;
        }
        return null;
    }

    public final T contentIfNotInTrace(Vec3Impl pos) {
        if (trace.contains(pos)) {
            return null;
        }
        return getContent();
    }

    public T getContent() {
        return content;
    }

    public String getContentString() {
        return content.toString();
    }

    public final int getTraceSize() {
        return trace.size();
    }
}
