package tectech.mechanics.dataTransport;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import tectech.recipe.TTRecipeAdder;

public class InventoryDataPacket extends DataPacket<ItemStack[]> {

    public InventoryDataPacket(ItemStack[] content) {
        super(content);
    }

    public InventoryDataPacket(NBTTagCompound compound) {
        super(compound);
    }

    @Override
    protected ItemStack[] contentFromNBT(NBTTagCompound nbt) {
        int count = nbt.getInteger("count");
        if (count > 0) {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                ItemStack stack = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag(Integer.toString(i)));
                if (stack != null) {
                    stacks.add(stack);
                }
            }
            return stacks.size() > 0 ? stacks.toArray(TTRecipeAdder.nullItem) : null;
        }
        return null;
    }

    @Override
    protected NBTTagCompound contentToNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        if (content != null && content.length > 0) {
            compound.setInteger("count", content.length);
            for (int i = 0; i < content.length; i++) {
                compound.setTag(Integer.toString(i), content[i].writeToNBT(new NBTTagCompound()));
            }
        }
        return compound;
    }

    @Override
    public boolean extraCheck() {
        return true;
    }

    @Override
    protected ItemStack[] unifyContentWith(ItemStack[] content) {
        throw new NoSuchMethodError("Unavailable to unify item stack data packet");
    }

    @Override
    public String getContentString() {
        return "Stack Count: " + (content == null ? 0 : content.length);
    }
}
