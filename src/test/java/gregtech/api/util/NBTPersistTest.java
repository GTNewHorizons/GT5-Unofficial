package gregtech.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

public class NBTPersistTest {

    @Test
    void testNBTExact() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte("a", (byte) 0xF0);
        tag.setShort("b", (short) 0xF0F0);
        tag.setInteger("c", 0xF0F0F0F0);
        tag.setLong("d", 0xF0F0F0F0F0F0F0F0L);
        tag.setFloat("e", 123.456f);
        tag.setDouble("f", 123.456);
        tag.setString("g", "hello world");
        tag.setIntArray("h", new int[] { 1, 2, 3, 4, 5 });
        tag.setByteArray("i", new byte[] { 1, 2, 3, 4, 5 });

        NBTTagList list = new NBTTagList();
        list.appendTag(tag);

        assertEquals(list, NBTPersist.toNbtExact(NBTPersist.toJsonObjectExact(list)));
    }
}
