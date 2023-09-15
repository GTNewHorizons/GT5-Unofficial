package gregtech.crossmod.holoinventory;

import static net.dries007.holoInventory.util.NBTKeys.*;

import java.util.List;

import net.dries007.holoInventory.compat.InventoryDecoder;
import net.dries007.holoInventory.compat.InventoryDecoderRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import gregtech.api.metatileentity.BaseTileEntity;

public class GT_InventoryDecoder extends InventoryDecoder {

    public GT_InventoryDecoder() {
        super(BaseTileEntity.class);
    }

    @Override
    public NBTTagList toNBT(IInventory inv) {
        List<ItemStack> items = ((BaseTileEntity) inv).getItemsForHoloGlasses();
        if (items == null) {
            return InventoryDecoderRegistry.DEFAULT.toNBT(inv);
        }
        NBTTagList tagList = new NBTTagList();
        for (ItemStack stack : items) {
            if (stack != null) {
                NBTTagCompound tag = stack.writeToNBT(new NBTTagCompound());
                tag.setInteger(NBT_KEY_COUNT, stack.stackSize);
                tagList.appendTag(tag);
            }
        }
        return tagList;
    }
}
