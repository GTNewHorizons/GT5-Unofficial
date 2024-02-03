package gregtech.api.net.data;

import javax.annotation.Nonnull;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public abstract class PacketData<T extends Process> implements Comparable<PacketData<T>> {

    /**
     * This should return the Id of the packet. The Id is is used to bit-shift to be added a header for the packet its
     * used in
     */
    public abstract int getId();

    /**
     * Called by the packet it is held by to store the data it needs
     */
    public abstract void encode(@Nonnull ByteBuf out);

    /**
     * Called by the packet it is held by to decode the data to later be used in {@link #process()}
     */
    public abstract void decode(@Nonnull ByteArrayDataInput in);

    /**
     * Called by the packet it is held by to process the data it decoded.
     */
    public abstract void process(T processData);

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof PacketData otherData)) return false;
        return this.getId() == otherData.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    @Override
    public int compareTo(PacketData<T> other) {
        return Integer.compare(this.getId(), other.getId());
    }
}
