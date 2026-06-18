package gtPlusPlus.core.item.wearable.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ISpecialArmor;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

public abstract class BaseItemWearable extends ItemArmor implements ISpecialArmor {

    public BaseItemWearable(ArmorMaterial material, int renderIndex, int armourType) {
        super(material, renderIndex, armourType);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void func_82813_b(ItemStack stack, int color) {
        final NBTTagCompound nbt = ItemStackNBT.get(stack);
        final NBTTagCompound display = nbt.getCompoundTag("display");
        if (!nbt.hasKey("display", 10)) {
            nbt.setTag("display", display);
        }
        display.setInteger("color", color);
    }

    @Override
    public void removeColor(ItemStack stack) {
        final NBTTagCompound display = ItemStackNBT.getCompoundTag(stack, "display");
        if (display != null) {
            display.removeTag("color");
        }
    }
}
