package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;

public class CoverDataControllerWidget<T extends ISerializableObject> extends DataControllerWidget<T> {

    protected final GT_CoverBehaviorBase<T> coverBehavior;

    /**
     * @param dataGetter () -> cover data this widget handles
     * @param dataSetter data to set -> if setting cover data is successful
     * @param coverBehavior cover this widget handles data update
     */
    public CoverDataControllerWidget(
            Supplier<T> dataGetter, Function<T, Boolean> dataSetter, GT_CoverBehaviorBase<T> coverBehavior) {
        super(dataGetter, dataSetter);
        this.coverBehavior = coverBehavior;
    }

    /**
     * @param dataToStateGetter given cover data -> state of the child widget
     * @param dataUpdater (current cover data, state of the child widget) -> new cover data to set
     * @param applyForWidget methods to call for widget to add
     */
    public DataControllerWidget<T> addTextField(
            Function<T, String> dataToStateGetter,
            BiFunction<T, String, T> dataUpdater,
            Consumer<CoverDataFollower_TextFieldWidget<T>> applyForWidget) {
        CoverDataFollower_TextFieldWidget<T> widget = new CoverDataFollower_TextFieldWidget<T>();
        applyForWidget.accept(widget);
        addFollower(widget, dataToStateGetter, dataUpdater);
        return this;
    }

    @Override
    protected void writeToPacket(PacketBuffer buffer, T data) {
        try {
            NetworkUtils.writeNBTBase(buffer, data.saveDataToNBT());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected T readFromPacket(PacketBuffer buffer) throws IOException {
        return coverBehavior.createDataObject(NetworkUtils.readNBTBase(buffer));
    }

    /**
     * Uses int index to determine toggle button behaviors.
     */
    public static class CoverDataIndexedControllerWidget_ToggleButtons<T extends ISerializableObject>
            extends CoverDataControllerWidget<T> {

        private final BiFunction<Integer, T, Boolean> dataToStateGetter;
        private final BiFunction<Integer, T, T> dataUpdater;

        /**
         * @param coverDataGetter () -> cover data this widget handles
         * @param coverDataSetter data to set -> if setting cover data is successful
         * @param coverBehavior cover this widget handles data update
         * @param dataToStateGetter (index of button, given cover data) -> button state
         * @param dataUpdater (index of button, current cover data) -> new cover data
         */
        public CoverDataIndexedControllerWidget_ToggleButtons(
                Supplier<T> coverDataGetter,
                Function<T, Boolean> coverDataSetter,
                GT_CoverBehaviorBase<T> coverBehavior,
                BiFunction<Integer, T, Boolean> dataToStateGetter,
                BiFunction<Integer, T, T> dataUpdater) {
            super(coverDataGetter, coverDataSetter, coverBehavior);
            this.dataToStateGetter = dataToStateGetter;
            this.dataUpdater = dataUpdater;
        }

        /**
         * @param index index of widget to add
         * @param applyForWidget methods to call for widget to add
         */
        public CoverDataIndexedControllerWidget_ToggleButtons<T> addToggleButton(
                int index, Consumer<CoverDataFollower_CycleButtonWidget<T>> applyForWidget) {
            CoverDataFollower_CycleButtonWidget.CoverDataFollower_ToggleButtonWidget<T> widget =
                    new CoverDataFollower_CycleButtonWidget.CoverDataFollower_ToggleButtonWidget<>();
            widget.setLength(2);
            applyForWidget.accept(widget);
            addFollower(
                    widget,
                    data -> dataToStateGetter.apply(index, data) ? 1 : 0,
                    (data, state) -> dataUpdater.apply(index, data));
            return this;
        }
    }

    /**
     * Uses int index to determine cycle button behaviors.
     */
    public static class CoverDataIndexedControllerWidget_CycleButtons<T extends ISerializableObject>
            extends CoverDataControllerWidget<T> {

        private final BiFunction<Integer, T, Integer> dataToStateGetter;
        private final BiFunction<Integer, T, T> dataUpdater;

        /**
         * @param coverDataGetter () -> cover data this widget handles
         * @param coverDataSetter data to set -> if setting cover data is successful
         * @param coverBehavior cover this widget handles data update
         * @param dataToStateGetter (index of button, given cover data) -> button state
         * @param dataUpdater (index of button, current cover data) -> new cover data
         */
        public CoverDataIndexedControllerWidget_CycleButtons(
                Supplier<T> coverDataGetter,
                Function<T, Boolean> coverDataSetter,
                GT_CoverBehaviorBase<T> coverBehavior,
                BiFunction<Integer, T, Integer> dataToStateGetter,
                BiFunction<Integer, T, T> dataUpdater) {
            super(coverDataGetter, coverDataSetter, coverBehavior);
            this.dataToStateGetter = dataToStateGetter;
            this.dataUpdater = dataUpdater;
        }

        /**
         * @param index index of widget to add
         * @param applyForWidget methods to call for widget to add
         */
        public CoverDataIndexedControllerWidget_CycleButtons<T> addToggleButton(
                int index, Consumer<CoverDataFollower_CycleButtonWidget<T>> applyForWidget) {
            CoverDataFollower_CycleButtonWidget.CoverDataFollower_ToggleButtonWidget<T> widget =
                    new CoverDataFollower_CycleButtonWidget.CoverDataFollower_ToggleButtonWidget<>();
            applyForWidget.accept(widget);
            addFollower(
                    widget,
                    data -> dataToStateGetter.apply(index, data),
                    (data, state) -> dataUpdater.apply(index, data));
            return this;
        }
    }
}
