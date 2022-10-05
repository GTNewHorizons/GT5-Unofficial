package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.api.widget.Widget;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Widget whose properties are controlled by cover data ({@link gregtech.api.util.ISerializableObject}).
 * <br> No widgets implementing this interface should not sync <b>to client</b>;
 * Instead, {@link gregtech.common.gui.modularui.GT_CoverDataControlWidget} will sync cover data to client
 * when action specified by {@link #setOnClick} is fired.
 * @param <T> Widget type to return for method chains
 */
public interface IGT_CoverDataDependentWidget<T extends Widget> {

    /**
     * Sets getter for button state.
     * <br> Button state should be dependent only on cover data ({@link gregtech.api.util.ISerializableObject});
     * In that way, controlling other buttons becomes easier, e.g. clicking button A will set cover data property B to false,
     * so button B, whose state is bound to data property B, will be automatically deactivated.
     * <br> On server side, getter should fetch actual cover data stored in TileEntity, as other players
     * might be modifying cover setting in parallel.
     * On client side, cover data cannot (or should not) be actively fetched from server,
     * so getter should return just a cached value.
     * @see com.gtnewhorizons.modularui.common.widget.CycleButtonWidget#state
     */
    @SuppressWarnings("JavadocReference")
    T setStateGetter(Supplier<Integer> stateGetter);

    /**
     * Sets action that will be fired when server receives packet of this widget being clicked.
     */
    T setOnClick(BiConsumer<Widget.ClickData, Widget> onClick);
}
