package gregtech.common.gui.modularui.widget;

/**
 * Determines button state with cover data.
 */
// public class CoverDataFollower_CycleButtonWidget<T extends ISerializableObject> extends CoverCycleButtonWidget
// implements IDataFollowerWidget<T, Integer> {
//
// private Function<T, Integer> dataToStateGetter;
//
// public CoverDataFollower_CycleButtonWidget() {
// super();
// setGetter(() -> 0); // fake getter; used only for init
// setSynced(false, false);
// }
//
// @Override
// public CoverDataFollower_CycleButtonWidget<T> setDataToStateGetter(Function<T, Integer> dataToStateGetter) {
// this.dataToStateGetter = dataToStateGetter;
// return this;
// }
//
// @Override
// public CoverDataFollower_CycleButtonWidget<T> setStateSetter(Consumer<Integer> setter) {
// super.setSetter(setter);
// return this;
// }
//
// @Override
// public void updateState(T data) {
// setState(dataToStateGetter.apply(data), false, false);
// }
// }
