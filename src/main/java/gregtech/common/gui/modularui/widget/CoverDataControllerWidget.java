package gregtech.common.gui.modularui.widget;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.ICoverDataFollowerWidget;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.common.covers.Cover;

public class CoverDataControllerWidget<T extends Cover>
    extends com.gtnewhorizons.modularui.common.widget.MultiChildWidget
    implements com.gtnewhorizons.modularui.api.widget.ISyncedWidget {

    private final Supplier<T> coverSupplier;
    private final CoverUIBuildContext coverUiContext;
    protected T lastData;
    private boolean needsUpdate;

    /**
     * @param coverSupplier  retrieves the cover from the world
     * @param coverUiContext identifies and locates the cover we're interacting with
     */
    public CoverDataControllerWidget(Supplier<T> coverSupplier, CoverUIBuildContext coverUiContext) {
        this.coverSupplier = coverSupplier;
        this.coverUiContext = coverUiContext;
    }

    /**
     * Can return null when cover data is invalid e.g. tile is broken or cover is removed
     */
    @Nullable
    protected T getCover() {
        if (isCoverValid()) {
            return this.coverSupplier.get();
        } else {
            return null;
        }
    }

    protected boolean updateCoverInWorld(PacketBuffer buf) throws IOException {
        ICoverable coverable = coverUiContext.getTile();
        if (isCoverValid() && NetworkUtils.readNBTBase(buf) instanceof NBTTagCompound tag) {
            coverable.updateAttachedCover(coverUiContext.getCoverID(), coverUiContext.getCoverSide(), tag);
            return true;
        }
        return false;
    }

    private boolean isCoverValid() {
        ICoverable tile = coverUiContext.getTile();
        return !tile.isDead() && tile.getCoverAtSide(coverUiContext.getCoverSide())
            .isValid();
    }

    public <U, W extends Widget & ICoverDataFollowerWidget<T, U>> CoverDataControllerWidget<T> addFollower(W widget,
        Function<T, U> dataToStateGetter, BiFunction<T, U, T> dataUpdater, Consumer<W> applyForWidget) {
        widget.setDataToStateGetter(dataToStateGetter);
        widget.setStateSetter(state -> {
            T newData = dataUpdater.apply(getLastData(), state);
            lastData = newData;
            syncDataToServer(newData);
        });
        applyForWidget.accept(widget);
        addChild(widget);
        return this;
    }

    protected void writeToPacket(PacketBuffer buffer, T cover) {
        try {
            NBTTagCompound nbt = new NBTTagCompound();
            CoverRegistry.writeCoverToNbt(cover, nbt);
            NetworkUtils.writeNBTBase(buffer, nbt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected T getLastData() {
        return lastData;
    }

    @Override
    public void onPostInit() {
        // client _should_ have received initial cover data from `GT_UIInfos#openCoverUI`
        lastData = getCover();
        if (NetworkUtils.isClient()) {
            updateChildren(true);
        }
    }

    @Override
    public void detectAndSendChanges(boolean init) {
        T actualValue = getCover();
        if (actualValue == null) {
            // data is in invalid state e.g. tile is broken, cover is removed
            getWindow().tryClose();
            return;
        }
        if (init || !actualValue.equals(getLastData())) {
            // init sync or someone else edited data
            lastData = actualValue;
            syncDataToClient(actualValue);
        }
    }

    protected void syncDataToClient(T data) {
        syncToClient(0, buffer -> writeToPacket(buffer, data));
    }

    protected void syncDataToServer(T data) {
        syncToServer(0, buffer -> writeToPacket(buffer, data));
        updateChildren();
    }

    @Override
    public void readOnClient(int id, PacketBuffer buf) throws IOException {
        if (id == 0) {
            updateCoverInWorld(buf);
            lastData = getCover();
            updateChildren();
        }
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (id == 0) {
            boolean coverIsValid = updateCoverInWorld(buf);
            lastData = getCover();
            if (coverIsValid) {
                markForUpdate();
            } else {
                getWindow().closeWindow();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    protected void updateChildren(boolean postInit) {
        for (Widget child : getChildren()) {
            if (child instanceof ICoverDataFollowerWidget) {
                ((ICoverDataFollowerWidget<T, ?>) child).updateState(getLastData());
                if (postInit) {
                    ((ICoverDataFollowerWidget<T, ?>) child).onPostInit();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void updateChildren() {
        updateChildren(false);
    }

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
     * Uses int index to determine toggle button behaviors.
     */
    public static class CoverDataIndexedControllerWidget_ToggleButtons<T extends Cover>
        extends CoverDataControllerWidget<T> {

        private final BiFunction<Integer, T, Boolean> dataToStateGetter;
        private final BiFunction<Integer, T, T> dataUpdater;

        /**
         * @param coverSupplier     retrieves the cover from the world
         * @param dataToStateGetter (index of button, given cover data) -> button state
         * @param dataUpdater       (index of button, current cover data) -> new cover data
         * @param coverUiContext    identifies and locates the cover we're interacting with
         */
        public CoverDataIndexedControllerWidget_ToggleButtons(Supplier<T> coverSupplier,
            BiFunction<Integer, T, Boolean> dataToStateGetter, BiFunction<Integer, T, T> dataUpdater,
            CoverUIBuildContext coverUiContext) {
            super(coverSupplier, coverUiContext);
            this.dataToStateGetter = dataToStateGetter;
            this.dataUpdater = dataUpdater;
        }

        /**
         * @param index          index of widget to add
         * @param widget         widget to add
         * @param applyForWidget methods to call for widget to add
         */
        public <W extends CoverDataFollowerToggleButtonWidget<T>> CoverDataIndexedControllerWidget_ToggleButtons<T> addToggleButton(
            int index, W widget, Consumer<CoverDataFollowerToggleButtonWidget<T>> applyForWidget) {
            addFollower(
                widget,
                data -> dataToStateGetter.apply(index, data),
                (data, state) -> dataUpdater.apply(index, data),
                applyForWidget);
            return this;
        }
    }
}
