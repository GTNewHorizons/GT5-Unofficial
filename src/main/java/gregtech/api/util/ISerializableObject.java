package gregtech.api.util;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTBase;
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
     */
    // the NBT is an unfortunate piece of tech. everything uses it but its API is not as efficient as could be
    void readFromPacket(ByteArrayDataInput aBuf);

}
