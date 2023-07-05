package gregtech.common.gui.modularui.widget;

/**
 * Controls state of child widgets with specific data, and allows centralized control of multiple widgets. e.g. clicking
 * button B will set machine mode to B, so button A, whose state is bound to the mode, will be automatically deactivated
 * by this widget. <br>
 * This widget wraps data and handles validation, e.g. tell client to close GUI when tile is broken or cover is removed.
 * <br>
 * Data can be anything, e.g. {@link ISerializableObject} or machine recipe mode.
 *
 * @param <T> Data type stored in this widget
 * @see IDataFollowerWidget
 */
// public abstract class DataControllerWidget<T> extends MultiChildWidget implements ISyncedWidget {
//
// private final Supplier<T> dataGetter;
// private final Function<T, Boolean> dataSetter;
//
// protected T lastData;
//
// private boolean needsUpdate;
//
// /**
// * @param dataGetter () -> data this widget handles
// * @param dataSetter data to set -> if setting data is successful
// */
// public DataControllerWidget(Supplier<T> dataGetter, Function<T, Boolean> dataSetter) {
// this.dataGetter = dataGetter;
// this.dataSetter = dataSetter;
// }
//
// protected T getLastData() {
// return lastData;
// }
//
// @Override
// public void onPostInit() {
// // client _should_ have received initial cover data from `GT_UIInfos#openCoverUI`
// lastData = dataGetter.get();
// if (NetworkUtils.isClient()) {
// updateChildren(true);
// }
// }
//
// @Override
// public void detectAndSendChanges(boolean init) {
// T actualValue = dataGetter.get();
// if (actualValue == null) {
// // data is in invalid state e.g. tile is broken, cover is removed
// getWindow().tryClose();
// return;
// }
// if (init || !actualValue.equals(getLastData())) {
// // init sync or someone else edited data
// lastData = actualValue;
// syncDataToClient(actualValue);
// }
// }
//
// protected void syncDataToClient(T data) {
// syncToClient(0, buffer -> writeToPacket(buffer, data));
// }
//
// protected void syncDataToServer(T data) {
// syncToServer(0, buffer -> writeToPacket(buffer, data));
// updateChildren();
// }
//
// @Override
// public void readOnClient(int id, PacketBuffer buf) throws IOException {
// if (id == 0) {
// lastData = readFromPacket(buf);
// dataSetter.apply(getLastData());
// updateChildren();
// }
// }
//
// @Override
// public void readOnServer(int id, PacketBuffer buf) throws IOException {
// if (id == 0) {
// lastData = readFromPacket(buf);
// if (dataSetter.apply(getLastData())) {
// markForUpdate();
// } else {
// getWindow().closeWindow();
// }
// }
// }
//
// @SuppressWarnings("unchecked")
// @SideOnly(Side.CLIENT)
// protected void updateChildren(boolean postInit) {
// for (Widget child : getChildren()) {
// if (child instanceof IDataFollowerWidget) {
// ((IDataFollowerWidget<T, ?>) child).updateState(getLastData());
// if (postInit) {
// ((IDataFollowerWidget<T, ?>) child).onPostInit();
// }
// }
// }
// }
//
// @SideOnly(Side.CLIENT)
// protected void updateChildren() {
// updateChildren(false);
// }
//
// protected abstract void writeToPacket(PacketBuffer buffer, T data);
//
// protected abstract T readFromPacket(PacketBuffer buffer) throws IOException;
//
// @Override
// public void markForUpdate() {
// needsUpdate = true;
// }
//
// @Override
// public void unMarkForUpdate() {
// needsUpdate = false;
// }
//
// @Override
// public boolean isMarkedForUpdate() {
// return needsUpdate;
// }
//
// /**
// * @param widget widget to add that implements {@link IDataFollowerWidget}
// * @param dataToStateGetter given data -> state of the widget to add
// * @param dataUpdater (current data, state of the widget to add) -> new data to set
// * @param applyForWidget methods to call for the widget to add
// * @param <U> state type stored in the widget to add
// * @param <W> widget type to add
// */
// public <U, W extends Widget & IDataFollowerWidget<T, U>> DataControllerWidget<T> addFollower(W widget,
// Function<T, U> dataToStateGetter, BiFunction<T, U, T> dataUpdater, Consumer<W> applyForWidget) {
// widget.setDataToStateGetter(dataToStateGetter);
// widget.setStateSetter(state -> {
// T newData = dataUpdater.apply(getLastData(), state);
// lastData = newData;
// dataSetter.apply(getLastData());
// syncDataToServer(newData);
// });
// applyForWidget.accept(widget);
// addChild(widget);
// return this;
// }
// }
