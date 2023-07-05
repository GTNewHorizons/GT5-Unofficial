package gregtech.api.gui.modularui;

/// **
// * Widget whose state is controlled by specific data. Data can be anything, e.g. {@link ISerializableObject} or
/// machine
// * recipe mode. <br>
// * No widgets implementing this interface should not sync; Instead, {@link DataControllerWidget} will sync data,
/// either
// * when this widget triggers update on client or data update is detected on server.
// *
// * @param <T> Data type stored in the parent widget
// * @param <U> State type stored in this widget
// * @see DataControllerWidget
// */
// @SuppressWarnings("UnusedReturnValue")
// public interface IDataFollowerWidget<T, U> {
//
// /**
// * Sets function to get widget state from provided data. This function will be called when client receives data from
// * server and {@link DataControllerWidget} updates all children, including this widget.
// */
// Widget setDataToStateGetter(Function<T, U> dataToStateGetter);
//
// /**
// * Sets setter called when this widget gets action from player. Basically the same functionality with widgets that
// * have getter/setter.
// */
// Widget setStateSetter(Consumer<U> setter);
//
// /**
// * Updates state of this widget with provided data. On server {@link DataControllerWidget} won't propagate data
// * update to this widget, so this method is client-only.
// */
// @SideOnly(Side.CLIENT)
// void updateState(T data);
//
// /**
// * Called on {@link Widget#onPostInit}.
// */
// @SuppressWarnings("OverrideOnly") // So IntelliJ doesn't warn about the Widget#onPostInit link in the javadoc
// default void onPostInit() {}
// }
