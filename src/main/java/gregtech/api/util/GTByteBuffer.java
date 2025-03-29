package gregtech.api.util;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.io.ByteArrayDataInput;

import io.netty.buffer.ByteBuf;

public class GTByteBuffer {

    /**
     * Reverse engineered and adapted {@link cpw.mods.fml.common.network.ByteBufUtils#readItemStack(ByteBuf)} Given
     * buffer must contain a serialized ItemStack in minecraft encoding
     */
    public static ItemStack readItemStackFromGreggyByteBuf(ByteArrayDataInput aBuf) {
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

    /**
     * Reverse engineered and adapted {@link cpw.mods.fml.common.network.ByteBufUtils#readTag(ByteBuf)} Given buffer
     * must contain a serialized NBTTagCompound in minecraft encoding
     */
    public static NBTTagCompound readCompoundTagFromGreggyByteBuf(ByteArrayDataInput aBuf) {
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
}
