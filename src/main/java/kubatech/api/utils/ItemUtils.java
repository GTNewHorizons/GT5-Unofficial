package kubatech.api.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemUtils {

    public static NBTTagCompound writeItemStackToNBT(ItemStack stack) {
        NBTTagCompound compound = new NBTTagCompound();

        stack.writeToNBT(compound);
        compound.setInteger("IntCount", stack.stackSize);

        return compound;
    }

    public static ItemStack readItemStackFromNBT(NBTTagCompound compound) {
        ItemStack stack = ItemStack.loadItemStackFromNBT(compound);

        if (stack == null) return null;

        if (compound.hasKey("IntCount")) stack.stackSize = compound.getInteger("IntCount");

        return stack;
    }
}
