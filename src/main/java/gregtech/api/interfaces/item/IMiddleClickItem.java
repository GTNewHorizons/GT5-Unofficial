package gregtech.api.interfaces.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IMiddleClickItem {
    default boolean onMiddleClick(ItemStack itemStack, EntityPlayer player) {
        return false;
    }
}
