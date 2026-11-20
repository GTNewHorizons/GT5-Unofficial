package tectech.mechanics.dataTransport;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

/**
 * Created by Tec on 05.04.2017.
 */
public abstract class DataPacket<T> {

    private static final byte MAX_HISTORY = 64;
    private final LongSet trace = new LongOpenHashSet(MAX_HISTORY);

    protected T content;

    protected DataPacket(T content) {
        this.content = content;
    }

    protected DataPacket(NBTTagCompound nbt) {
        content = contentFromNBT(nbt.getCompoundTag("qContent"));
        NBTTagList list = nbt.getTagList("qTrace", 4);
        var length = list.tagCount();
        for (int i = 0; i < length; i++) {
            trace.add(((NBTTagLong) list.removeTag(i)).func_150291_c());
        }
    }

    public final NBTTagCompound toNbt() {
        NBTTagCompound nbt = new NBTTagCompound();
        NBTTagCompound contentTag = contentToNBT();
        if (contentTag != null) {
            nbt.setTag("qContent", contentTag);
        }
        int i = 0;
        NBTTagList list = new NBTTagList();
        for (long v : trace) {
            list.appendTag(new NBTTagLong(v));
        }
        nbt.setTag("qTrace", list);
        return nbt;
    }

    protected abstract NBTTagCompound contentToNBT();

    protected abstract T contentFromNBT(NBTTagCompound nbt);

    protected abstract T unifyContentWith(T content);

    public final boolean contains(long v) {
        return trace.contains(v);
    }

    public final boolean check() {
        return trace.size() <= MAX_HISTORY;
    }

    protected final DataPacket<T> unifyTrace(long... positions) {
        if (trace.size() + positions.length > MAX_HISTORY) return null;
        for (long position : positions) {
            trace.add(position);
        }
        return (check()) ? this : null;
    }

    protected final DataPacket<T> unifyTrace(DataPacket<T> p) {
        if (p == null) return this;
        trace.addAll(p.trace);
        return (check()) ? this : null;
    }

    protected final DataPacket<T> unifyWith(DataPacket<T> p) {
        if (p == null) return this;
        trace.addAll(p.trace);
        if (check()) {
            content = unifyContentWith(p.content);
            return this;
        }
        return null;
    }

    public final T contentIfNotInTrace(long pos) {
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
