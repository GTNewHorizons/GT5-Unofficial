package gtPlusPlus.core.item.wearable.base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ISpecialArmor;

public abstract class BaseItemWearable extends ItemArmor implements ISpecialArmor {

    public BaseItemWearable(ArmorMaterial material, int renderIndex, int armourType) {
        super(material, renderIndex, armourType);
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void func_82813_b(ItemStack p_82813_1_, int p_82813_2_) {
        NBTTagCompound nbttagcompound = p_82813_1_.getTagCompound();
        if (nbttagcompound == null) {
            nbttagcompound = new NBTTagCompound();
            p_82813_1_.setTagCompound(nbttagcompound);
        }
        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
        if (!nbttagcompound.hasKey("display", 10)) {
            nbttagcompound.setTag("display", nbttagcompound1);
        }
        nbttagcompound1.setInteger("color", p_82813_2_);
    }

    @Override
    public void removeColor(ItemStack p_82815_1_) {
        NBTTagCompound nbttagcompound = p_82815_1_.getTagCompound();
        if (nbttagcompound != null) {
            NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
            if (nbttagcompound1.hasKey("color")) {
                nbttagcompound1.removeTag("color");
            }
        }
    }
}
