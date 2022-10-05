package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.widget.ISyncedWidget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;

/**
 * Controls state of child widgets with cover data.
 * @param <T> Data type used for cover
 */
public class GT_CoverDataControlWidget<T extends ISerializableObject> extends MultiChildWidget
        implements ISyncedWidget {

    protected final Supplier<T> coverDataGetter;
    /**
     * Data to set -> If setter was successful
     */
    protected final Function<T, Boolean> coverDataSetter;

    private final GT_CoverBehaviorBase<T> coverBehavior;

    protected T lastValue;

    private boolean needsUpdate;

    public GT_CoverDataControlWidget(
            Supplier<T> coverDataGetter, Function<T, Boolean> coverDataSetter, GT_CoverBehaviorBase<T> coverBehavior) {
        this.coverDataGetter = coverDataGetter;
        this.coverDataSetter = coverDataSetter;
        this.coverBehavior = coverBehavior;
    }

    public static <T extends ISerializableObject> GT_CoverDataIndexedControlWidget<T> ofIndexed(
            Supplier<T> coverDataGetter,
            Function<T, Boolean> coverDataSetter,
            GT_CoverBehaviorBase<T> coverBehavior,
            BiFunction<Integer, T, Integer> stateGetter,
            BiFunction<Integer, ClickData, T> onClickNewValue) {
        return new GT_CoverDataIndexedControlWidget<>(
                coverDataGetter, coverDataSetter, coverBehavior, stateGetter, onClickNewValue);
    }

    public T getLastData() {
        return lastValue;
    }

    @Override
    public void detectAndSendChanges(boolean init) {
        T actualValue = coverDataGetter.get();
        if (actualValue == null) {
            // tile is broken or cover is not present
            getWindow().closeWindow();
            return;
        }
        if (init || !actualValue.equals(lastValue)) {
            // init sync or someone else edited cover setting
            syncDataToClient(actualValue);
        }
    }

    protected void syncDataToClient(T data) {
        lastValue = data;
        syncToClient(0, buffer -> {
            try {
                NetworkUtils.writeNBTBase(buffer, data.saveDataToNBT());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) throws IOException {
        if (id == 0) {
            lastValue = coverBehavior.createDataObject(NetworkUtils.readNBTBase(buf));
        }
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {}

    @Override
    public void markForUpdate() {
        needsUpdate = true;
    }

    @Override
    public void unMarkForUpdate() {
        needsUpdate = false;
    }

    @Override
    public boolean isMarkedForUpdate() {
        return needsUpdate;
    }

    /**
     * Uses int index to determine button behavior.
     * Typically for covers using {@link gregtech.api.util.ISerializableObject.LegacyCoverData}.
     */
    public static class GT_CoverDataIndexedControlWidget<T extends ISerializableObject>
            extends GT_CoverDataControlWidget<T> {

        /**
         * (Index of button, current data) -> button state
         */
        private final BiFunction<Integer, T, Integer> stateGetter;
        /**
         * (Index of button clicked, click data) -> new cover data
         */
        private final BiFunction<Integer, ClickData, T> onClickNewValue;

        public GT_CoverDataIndexedControlWidget(
                Supplier<T> coverDataGetter,
                Function<T, Boolean> coverDataSetter,
                GT_CoverBehaviorBase<T> coverBehavior,
                BiFunction<Integer, T, Integer> stateGetter,
                BiFunction<Integer, ClickData, T> onClickNewValue) {
            super(coverDataGetter, coverDataSetter, coverBehavior);
            this.stateGetter = stateGetter;
            this.onClickNewValue = onClickNewValue;
        }

        public GT_CoverDataIndexedControlWidget<T> addCycleButton(
                int index, Function<Integer, IDrawable> overlayGetter, int stateLength, String tooltip, int x, int y) {
            return setupButton(
                    new GT_CoverDataDependentCycleButtonWidget(), index, overlayGetter, stateLength, tooltip, x, y);
        }

        public GT_CoverDataIndexedControlWidget<T> addToggleButton(
                int index, IDrawable overlay, String tooltip, int x, int y) {
            return setupButton(
                    new GT_CoverDataDependentCycleButtonWidget.GT_CoverDataDependentToggleButtonWidget(),
                    index,
                    state -> overlay,
                    2,
                    tooltip,
                    x,
                    y);
        }

        private GT_CoverDataIndexedControlWidget<T> setupButton(
                GT_CoverDataDependentCycleButtonWidget button,
                int index,
                Function<Integer, IDrawable> overlayGetter,
                int stateLength,
                String tooltip,
                int x,
                int y) {
            addChild(button.setStateGetter(() -> NetworkUtils.isClient()
                            ? stateGetter.apply(index, getLastData())
                            : stateGetter.apply(index, coverDataGetter.get()))
                    .setOnClick((clickData, widget) -> {
                        T newValue = onClickNewValue.apply(index, clickData);
                        if (coverDataSetter.apply(newValue)) {
                            syncDataToClient(newValue);
                            markForUpdate();
                        } else {
                            widget.getWindow().closeWindow();
                        }
                    })
                    .addCoverTooltip(tooltip)
                    .setLength(stateLength)
                    .setTextureGetter(overlayGetter)
                    .setPos(x, y));
            return this;
        }
    }
}
