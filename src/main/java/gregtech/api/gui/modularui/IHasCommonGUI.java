package gregtech.api.gui.modularui;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;

/**
 * Defines some methods needed to create {@link gregtech.common.modularui.uifactory.MachineUIFactory}.
 */
public interface IHasCommonGUI {

    @Nullable
    ItemStack getStackForm(long aAmount);

    @Nonnull
    String getLocalName();

    byte getColorization();
}
