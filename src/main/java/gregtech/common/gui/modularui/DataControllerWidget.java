package gregtech.common.gui.modularui;

import com.gtnewhorizons.modularui.api.widget.ISyncedWidget;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.gui.ModularUI.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.PacketBuffer;

/**
 * Controls state of child widgets with specific data, and allows centralized control of multiple widgets.
 * e.g. clicking button B will set machine mode to B, so button A, whose state is bound to the mode,
 * will be automatically deactivated by this widget.
 * <br> This widget wraps data and handles validation, e.g. tell client to close GUI when tile is broken or cover is removed.
 * <br> Data can be anything, e.g. {@link ISerializableObject} or machine recipe mode.
 * @param <T> Data type stored in this widget
 * @see IDataFollowerWidget
 */
public abstract class DataControllerWidget<T> extends MultiChildWidget implements ISyncedWidget {

    private final Supplier<T> dataGetter;
    private final Function<T, Boolean> dataSetter;

    protected T lastData;

    private boolean needsUpdate;

    /**
     * @param dataGetter () -> data this widget handles
     * @param dataSetter data to set -> if setting data is successful
     */
    public DataControllerWidget(Supplier<T> dataGetter, Function<T, Boolean> dataSetter) {
        this.dataGetter = dataGetter;
        this.dataSetter = dataSetter;
    }

    public T getLastData() {
        return lastData;
    }

    @Override
    public void detectAndSendChanges(boolean init) {
        T actualValue = dataGetter.get();
        if (actualValue == null) {
            // data is in invalid state e.g. tile is broken, cover is removed
            getWindow().closeWindow();
            return;
        }
        if (init || !actualValue.equals(getLastData())) {
            // init sync or someone else edited data
            syncDataToClient(actualValue);
        }
    }

    protected void syncDataToClient(T data) {
        lastData = data;
        syncToClient(0, buffer -> writeToPacket(buffer, data));
    }

    protected void syncDataToServer(T data) {
        lastData = data;
        syncToServer(0, buffer -> writeToPacket(buffer, data));
        updateChildren();
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) throws IOException {
        if (id == 0) {
            lastData = readFromPacket(buf);
            updateChildren();
        }
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (id == 0) {
            lastData = readFromPacket(buf);
            if (dataSetter.apply(getLastData())) {
                markForUpdate();
            } else {
                getWindow().closeWindow();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void updateChildren() {
        for (Widget child : getChildren()) {
            if (child instanceof IDataFollowerWidget) {
                //noinspection rawtypes,unchecked
                ((IDataFollowerWidget) child).updateState(getLastData());
            }
        }
    }

    protected abstract void writeToPacket(PacketBuffer buffer, T data);

    protected abstract T readFromPacket(PacketBuffer buffer) throws IOException;

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
     * @param widget widget to add
     * @param dataToStateGetter given data -> state of the child widget
     * @param dataUpdater (current data, state of the child widget) -> new data to set
     * @param <U> state type stored in child widget
     * @param <W> widget type to add
     */
    @SuppressWarnings("UnusedReturnValue")
    public <U, W extends Widget & IDataFollowerWidget<T, U, W>> DataControllerWidget<T> addFollower(
            W widget, Function<T, U> dataToStateGetter, BiFunction<T, U, T> dataUpdater) {
        widget.setDataToStateGetter(dataToStateGetter).setSetter(state -> {
            T newData = dataUpdater.apply(getLastData(), state);
            syncDataToServer(newData);
        });
        addChild(widget);
        return this;
    }
}
