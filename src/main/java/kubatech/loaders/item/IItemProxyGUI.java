package kubatech.loaders.item;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IItemProxyGUI {
    ModularWindow createWindow(ItemStack stack, EntityPlayer player);
}
