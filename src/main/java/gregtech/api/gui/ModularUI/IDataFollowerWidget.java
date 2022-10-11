package gregtech.api.gui.ModularUI;

import com.gtnewhorizons.modularui.api.widget.Widget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.DataControllerWidget;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Widget whose state is controlled by specific data.
 * Data can be anything, e.g. {@link ISerializableObject} or machine recipe mode.
 * <br> No widgets implementing this interface should not sync;
 * Instead, {@link DataControllerWidget} will sync data, either when this widget triggers update on client
 * or data update is detected on server.
 * @param <T> Data type stored in parent widget
 * @param <U> State type stored in this widget
 * @param <W> Widget type to return for method chains
 * @see DataControllerWidget
 */
public interface IDataFollowerWidget<T, U, W extends Widget> {

    /**
     * Sets function to get widget state from provided data. This function will be called when client receives data
     * from server and {@link DataControllerWidget} updates all children, including this widget.
     */
    W setDataToStateGetter(Function<T, U> dataToStateGetter);

    /**
     * Sets setter called when this widget gets action from player.
     * Basically the same functionality with widgets that have getter/setter.
     */
    @SuppressWarnings("UnusedReturnValue")
    W setSetter(Consumer<U> setter);

    /**
     * Updates state of this widget with provided data.
     * On server {@link DataControllerWidget} won't propagate data update to this widget,
     * so this method is client-only.
     */
    @SideOnly(Side.CLIENT)
    void updateState(T data);
}
