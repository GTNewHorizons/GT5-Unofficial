package gregtech.common.gui.modularui.widget;

import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.nbt.NBTBase;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.covers.CoverRegistry;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.ICoverDataFollowerWidget;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.ISerializableObject;

public class CoverDataControllerWidget<T extends ISerializableObject>
    extends com.gtnewhorizons.modularui.common.widget.MultiChildWidget
    implements com.gtnewhorizons.modularui.api.widget.ISyncedWidget {

    protected final Function<NBTBase, T> nbtParser;
    private final Supplier<T> dataGetter;
    private final CoverUIBuildContext coverUiContext;
    protected T lastData;
    private boolean needsUpdate;

    /**
     * @param dataGetter     () -> cover data this widget handles
     * @param nbtParser      cover this widget handles data update
     * @param coverUiContext identifies and locates the cover we're interacting with
     */
    public CoverDataControllerWidget(Supplier<T> dataGetter, Function<NBTBase, T> nbtParser,
        CoverUIBuildContext coverUiContext) {
        this.dataGetter = dataGetter;
        this.nbtParser = nbtParser;
        this.coverUiContext = coverUiContext;
    }

    protected boolean updateCoverInWorld(T data) {
        if (!isCoverValid()) return false;
        ForgeDirection side = coverUiContext.getCoverSide();
        ICoverable coverable = coverUiContext.getTile();
        coverable.updateAttachedCover(
            CoverRegistry.getRegistration(coverUiContext.getCoverID())
                .buildCover(side, coverable, data));
        return true;
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
            updateCoverInWorld(getLastData());
            syncDataToServer(newData);
        });
        applyForWidget.accept(widget);
        addChild(widget);
        return this;
    }

    protected void writeToPacket(PacketBuffer buffer, T data) {
        try {
            NetworkUtils.writeNBTBase(buffer, data.saveDataToNBT());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected T readFromPacket(PacketBuffer buffer) throws IOException {
        return nbtParser.apply(NetworkUtils.readNBTBase(buffer));
    }

    protected T getLastData() {
        return lastData;
    }

    @Override
    public void onPostInit() {
        // client _should_ have received initial cover data from `GT_UIInfos#openCoverUI`
        lastData = dataGetter.get();
        if (NetworkUtils.isClient()) {
            updateChildren(true);
        }
    }

    @Override
    public void detectAndSendChanges(boolean init) {
        T actualValue = dataGetter.get();
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
            lastData = readFromPacket(buf);
            updateCoverInWorld(getLastData());
            updateChildren();
        }
    }

    @Override
    public void readOnServer(int id, PacketBuffer buf) throws IOException {
        if (id == 0) {
            lastData = readFromPacket(buf);
            if (updateCoverInWorld(getLastData())) {
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
    public static class CoverDataIndexedControllerWidget_ToggleButtons<T extends ISerializableObject>
        extends CoverDataControllerWidget<T> {

        private final BiFunction<Integer, T, Boolean> dataToStateGetter;
        private final BiFunction<Integer, T, T> dataUpdater;

        /**
         * @param coverDataGetter   () -> cover data this widget handles
         * @param nbtParser         method that can read cover data from NBT
         * @param dataToStateGetter (index of button, given cover data) -> button state
         * @param dataUpdater       (index of button, current cover data) -> new cover data
         * @param coverUiContext    identifies and locates the cover we're interacting with
         */
        public CoverDataIndexedControllerWidget_ToggleButtons(Supplier<T> coverDataGetter,
            Function<NBTBase, T> nbtParser, BiFunction<Integer, T, Boolean> dataToStateGetter,
            BiFunction<Integer, T, T> dataUpdater, CoverUIBuildContext coverUiContext) {
            super(coverDataGetter, nbtParser, coverUiContext);
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
