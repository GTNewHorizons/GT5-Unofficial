package gregtech.api.util;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

/**
 * We could well have used {@link java.io.Serializable}, but that's too slow and should generally be avoided
 *
 * @author glease
 */
public interface ISerializableObject {

    @Nonnull
    ISerializableObject copy();

    /**
     * If you are overriding this, you must <b>NOT</b> return {@link NBTTagInt} here! That return type is how we tell
     * that we are loading legacy data, and only {@link LegacyCoverData} is allowed to return it. You probably want to
     * return {@link NBTTagCompound} anyway.
     */
    @Nonnull
    NBTBase saveDataToNBT();

    /**
     * Write data to given ByteBuf The data saved this way is intended to be stored for short amount of time over
     * network. DO NOT store it to disks.
     */
    // the NBT is an unfortunate piece of tech. everything uses it but its API is not as efficient as could be
    void writeToByteBuf(ByteBuf aBuf);

    void loadDataFromNBT(NBTBase aNBT);

    /**
     * Read data from given parameter and return this. The data read this way is intended to be stored for short amount
     * of time over network.
     * 
     * @param aPlayer the player who is sending this packet to server. null if it's client reading data.
     */
    // the NBT is an unfortunate piece of tech. everything uses it but its API is not as efficient as could be
    @Nonnull
    ISerializableObject readFromPacket(ByteArrayDataInput aBuf, @Nullable EntityPlayerMP aPlayer);

    /**
     * Reverse engineered and adapted {@link cpw.mods.fml.common.network.ByteBufUtils#readTag(ByteBuf)} Given buffer
     * must contain a serialized NBTTagCompound in minecraft encoding
     */
    static NBTTagCompound readCompoundTagFromGreggyByteBuf(ByteArrayDataInput aBuf) {
        short size = aBuf.readShort();
        if (size < 0) return null;
        else {
            byte[] buf = new byte[size]; // this is shit, how many copies have we been doing?
            aBuf.readFully(buf);
            try {
                return CompressedStreamTools.func_152457_a(buf, new NBTSizeTracker(2097152L));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Reverse engineered and adapted {@link cpw.mods.fml.common.network.ByteBufUtils#readItemStack(ByteBuf)} Given
     * buffer must contain a serialized ItemStack in minecraft encoding
     */
    static ItemStack readItemStackFromGreggyByteBuf(ByteArrayDataInput aBuf) {
        ItemStack stack = null;
        short id = aBuf.readShort();
        if (id >= 0) {
            byte size = aBuf.readByte();
            short meta = aBuf.readShort();
            stack = new ItemStack(Item.getItemById(id), size, meta);
            stack.stackTagCompound = readCompoundTagFromGreggyByteBuf(aBuf);
        }
        return stack;
    }

    final class LegacyCoverData implements ISerializableObject {

        private int mData;

        public LegacyCoverData() {}

        public LegacyCoverData(int mData) {
            this.mData = mData;
        }

        @Override
        @Nonnull
        public ISerializableObject copy() {
            return new LegacyCoverData(mData);
        }

        @Override
        @Nonnull
        public NBTBase saveDataToNBT() {
            return new NBTTagInt(mData);
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(mData);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            mData = aNBT instanceof NBTTagInt ? ((NBTTagInt) aNBT).func_150287_d() : 0;
        }

        @Override
        @Nonnull
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mData = aBuf.readInt();
            return this;
        }

        public int get() {
            return mData;
        }

        public void set(int mData) {
            this.mData = mData;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LegacyCoverData that = (LegacyCoverData) o;

            return mData == that.mData;
        }

        @Override
        public int hashCode() {
            return mData;
        }

        @Override
        public String toString() {
            return String.valueOf(mData);
        }
    }
}
