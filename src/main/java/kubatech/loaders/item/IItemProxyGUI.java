package kubatech.loaders.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;

public interface IItemProxyGUI {

    ModularWindow createWindow(ItemStack stack, EntityPlayer player);
}
