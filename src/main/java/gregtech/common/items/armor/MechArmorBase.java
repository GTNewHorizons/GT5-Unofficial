package gregtech.common.items.armor;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

public class MechArmorBase extends ItemArmor {

    protected IIcon coreIcon;

    static final int SLOT_HELMET = 0;
    static final int SLOT_CHEST = 1;
    static final int SLOT_LEGS = 2;
    static final int SLOT_BOOTS = 3;

    public MechArmorBase(int slot) {
        super(ArmorMaterial.IRON, 2, slot);
    }

    public IIcon getCoreIcon() {
        return coreIcon;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        NBTTagCompound tag = aStack.getTagCompound();
        if (tag != null) {
            if (tag.hasKey("core")) {
                aList.add("Installed Core: " + tag.getInteger("core"));
            }
        }
    }
}
