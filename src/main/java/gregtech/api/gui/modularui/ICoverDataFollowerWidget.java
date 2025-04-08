package gregtech.api.gui.modularui;

import java.util.function.Consumer;
import java.util.function.Function;

import com.gtnewhorizons.modularui.api.widget.Widget;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.covers.Cover;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;

/**
 * Widget whose state is controlled by specific data. Data can be anything, e.g. {@link Cover} or machine
 * recipe mode. <br>
 * No widgets implementing this interface should not sync; Instead, {@link CoverDataControllerWidget} will sync data,
 * either
 * when this widget triggers update on client or data update is detected on server.
 *
 * @param <T> Data type stored in the parent widget
 * @param <U> State type stored in this widget
 * @see CoverDataControllerWidget
 */
@SuppressWarnings("UnusedReturnValue")
public interface ICoverDataFollowerWidget<T, U> {

    /**
     * Sets function to get widget state from provided data. This function will be called when client receives data from
     * server and {@link CoverDataControllerWidget} updates all children, including this widget.
     */
    Widget setDataToStateGetter(Function<T, U> dataToStateGetter);

    /**
     * Sets setter called when this widget gets action from player. Basically the same functionality with widgets that
     * have getter/setter.
     */
    Widget setStateSetter(Consumer<U> setter);

    /**
     * Updates state of this widget with provided data. On server {@link CoverDataControllerWidget} won't propagate data
     * update to this widget, so this method is client-only.
     */
    @SideOnly(Side.CLIENT)
    void updateState(T data);

    /**
     * Called on {@link Widget#onPostInit}.
     */
    @SuppressWarnings("OverrideOnly") // So IntelliJ doesn't warn about the Widget#onPostInit link in the javadoc
    default void onPostInit() {}
}
