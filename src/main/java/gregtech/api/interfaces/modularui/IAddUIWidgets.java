package gregtech.api.interfaces.modularui;

import java.util.function.Function;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

public interface IAddUIWidgets {

    default Function<Integer, BaseSlot> getSlotCreator() {
        return null;
    }

    default Function<BaseSlot, SlotWidget> getSlotWidgetCreator() {
        return null;
    }

    default void modifySlotWidget(SlotWidget widget) {

    }

    default void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {}
}
