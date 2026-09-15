package gregtech.api.interfaces.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

public interface IAddInventorySlots {

    default void add1by1Slot(ModularWindow.Builder builder, IDrawable... background) {}

    default void add2by2Slots(ModularWindow.Builder builder, IDrawable... background) {}

    default void add3by3Slots(ModularWindow.Builder builder, IDrawable... background) {}

    default void add4by4Slots(ModularWindow.Builder builder, IDrawable... background) {}
}
