package gregtech.api.gui.modularui;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import net.minecraft.item.ItemStack;

public interface IItemUIFactory {

    ModularWindow createWindow(UIBuildContext buildContext, ItemStack stack);
}
