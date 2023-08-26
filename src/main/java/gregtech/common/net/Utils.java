package gregtech.common.net;

import java.io.IOException;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

public class Utils {

    public static FluidStack readFluidStack(PacketBuffer buffer) {
        try {
            return NetworkUtils.readFluidStack(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeItemStack(PacketBuffer buffer, @Nullable ItemStack stack) {
        if (stack == null) {
            buffer.writeShort(-1);
            return;
        }

        buffer.writeShort(Item.getIdFromItem(stack.getItem()));
        buffer.writeInt(stack.stackSize);
        buffer.writeShort(stack.getItemDamage());
        NBTTagCompound nbttagcompound = null;

        if (stack.getItem()
            .isDamageable()
            || stack.getItem()
                .getShareTag()) {
            nbttagcompound = stack.stackTagCompound;
        }

        try {
            buffer.writeNBTTagCompoundToBuffer(nbttagcompound);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ItemStack readItemStack(PacketBuffer buffer) {
        try {
            ItemStack itemstack = null;
            short short1 = buffer.readShort();

            if (short1 >= 0) {
                int b0 = buffer.readInt();
                short short2 = buffer.readShort();
                itemstack = new ItemStack(Item.getItemById(short1), b0, short2);
                itemstack.stackTagCompound = buffer.readNBTTagCompoundFromBuffer();
            }

            return itemstack;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
