package gregtech.common.items.matterManipulator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IBuildable {

    public void tryPlaceBlocks(ItemStack stack, EntityPlayer player);
}
