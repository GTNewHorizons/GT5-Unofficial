package gregtech.common.modularui.widget;

// public class CoverDataControllerWidget<T extends ISerializableObject> extends DataControllerWidget<T> {
//
// protected final GT_CoverBehaviorBase<T> coverBehavior;
//
// /**
// * @param dataGetter () -> cover data this widget handles
// * @param dataSetter data to set -> if setting cover data is successful
// * @param coverBehavior cover this widget handles data update
// */
// public CoverDataControllerWidget(Supplier<T> dataGetter, Function<T, Boolean> dataSetter,
// GT_CoverBehaviorBase<T> coverBehavior) {
// super(dataGetter, dataSetter);
// this.coverBehavior = coverBehavior;
// }
//
// @Override
// public <U, W extends Widget & IDataFollowerWidget<T, U>> CoverDataControllerWidget<T> addFollower(W widget,
// Function<T, U> dataToStateGetter, BiFunction<T, U, T> dataUpdater, Consumer<W> applyForWidget) {
// super.addFollower(widget, dataToStateGetter, dataUpdater, applyForWidget);
// return this;
// }
//
// @Override
// protected void writeToPacket(PacketBuffer buffer, T data) {
// try {
// NetworkUtils.writeNBTBase(buffer, data.saveDataToNBT());
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
//
// @Override
// protected T readFromPacket(PacketBuffer buffer) throws IOException {
// return coverBehavior.createDataObject(NetworkUtils.readNBTBase(buffer));
// }
//
// /**
// * Uses int index to determine toggle button behaviors.
// */
// public static class CoverDataIndexedControllerWidget_ToggleButtons<T extends ISerializableObject>
// extends CoverDataControllerWidget<T> {
//
// private final BiFunction<Integer, T, Boolean> dataToStateGetter;
// private final BiFunction<Integer, T, T> dataUpdater;
//
// /**
// * @param coverDataGetter () -> cover data this widget handles
// * @param coverDataSetter data to set -> if setting cover data is successful
// * @param coverBehavior cover this widget handles data update
// * @param dataToStateGetter (index of button, given cover data) -> button state
// * @param dataUpdater (index of button, current cover data) -> new cover data
// */
// public CoverDataIndexedControllerWidget_ToggleButtons(Supplier<T> coverDataGetter,
// Function<T, Boolean> coverDataSetter, GT_CoverBehaviorBase<T> coverBehavior,
// BiFunction<Integer, T, Boolean> dataToStateGetter, BiFunction<Integer, T, T> dataUpdater) {
// super(coverDataGetter, coverDataSetter, coverBehavior);
// this.dataToStateGetter = dataToStateGetter;
// this.dataUpdater = dataUpdater;
// }
//
// /**
// * @param index index of widget to add
// * @param widget widget to add
// * @param applyForWidget methods to call for widget to add
// */
// public <W extends CoverDataFollower_ToggleButtonWidget<T>> CoverDataIndexedControllerWidget_ToggleButtons<T>
// addToggleButton(
// int index, W widget, Consumer<CoverDataFollower_ToggleButtonWidget<T>> applyForWidget) {
// addFollower(
// widget,
// data -> dataToStateGetter.apply(index, data),
// (data, state) -> dataUpdater.apply(index, data),
// applyForWidget);
// return this;
// }
// }
//
// /**
// * Uses int index to determine cycle button behaviors.
// */
// public static class CoverDataIndexedControllerWidget_CycleButtons<T extends ISerializableObject>
// extends CoverDataControllerWidget<T> {
//
// private final BiFunction<Integer, T, Integer> dataToStateGetter;
// private final BiFunction<Integer, T, T> dataUpdater;
//
// /**
// * @param coverDataGetter () -> cover data this widget handles
// * @param coverDataSetter data to set -> if setting cover data is successful
// * @param coverBehavior cover this widget handles data update
// * @param dataToStateGetter (index of button, given cover data) -> button state
// * @param dataUpdater (index of button, current cover data) -> new cover data
// */
// public CoverDataIndexedControllerWidget_CycleButtons(Supplier<T> coverDataGetter,
// Function<T, Boolean> coverDataSetter, GT_CoverBehaviorBase<T> coverBehavior,
// BiFunction<Integer, T, Integer> dataToStateGetter, BiFunction<Integer, T, T> dataUpdater) {
// super(coverDataGetter, coverDataSetter, coverBehavior);
// this.dataToStateGetter = dataToStateGetter;
// this.dataUpdater = dataUpdater;
// }
//
// /**
// * @param index index of widget to add
// * @param widget widget to add
// * @param applyForWidget methods to call for the widget to add
// */
// public <W extends CoverDataFollower_CycleButtonWidget<T>> CoverDataIndexedControllerWidget_CycleButtons<T>
// addCycleButton(
// int index, W widget, Consumer<CoverDataFollower_CycleButtonWidget<T>> applyForWidget) {
// addFollower(
// widget,
// data -> dataToStateGetter.apply(index, data),
// (data, state) -> dataUpdater.apply(index, data),
// applyForWidget);
// return this;
// }
// }
// }
