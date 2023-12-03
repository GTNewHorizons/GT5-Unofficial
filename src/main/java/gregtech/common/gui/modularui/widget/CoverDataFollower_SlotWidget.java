package gregtech.common.gui.modularui.widget;

import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.item.ItemStack;

import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.widget.Interactable;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.IDataFollowerWidget;
import gregtech.api.util.ISerializableObject;

public class CoverDataFollower_SlotWidget<T extends ISerializableObject> extends SlotWidget
    implements IDataFollowerWidget<T, ItemStack> {

    private Function<T, ItemStack> dataToStateGetter;
    private Consumer<ItemStack> dataSetter;

    public CoverDataFollower_SlotWidget(BaseSlot slot) {
        super(slot);
    }

    public CoverDataFollower_SlotWidget(IItemHandlerModifiable handler, int index, boolean phantom) {
        this(new BaseSlot(handler, index, phantom));
    }

    public CoverDataFollower_SlotWidget(IItemHandlerModifiable handler, int index) {
        this(handler, index, false);
    }

    @Override
    public CoverDataFollower_SlotWidget<T> setDataToStateGetter(Function<T, ItemStack> dataToStateGetter) {
        this.dataToStateGetter = dataToStateGetter;
        return this;
    }

    @Override
    public CoverDataFollower_SlotWidget<T> setStateSetter(Consumer<ItemStack> setter) {
        this.dataSetter = setter;
        return this;
    }

    @Override
    public void updateState(T data) {
        getMcSlot().putStack(dataToStateGetter.apply(data));
    }

    @Override
    public void detectAndSendChanges(boolean init) {}

    // Slot sync is handled differently from other DataFollowers,
    // so we need to also sync slot content directly to server.

    @Override
    public ClickResult onClick(int buttonId, boolean doubleClick) {
        if (interactionDisabled) return ClickResult.REJECT;
        if (isPhantom()) {
            ClickData clickData = ClickData.create(buttonId, doubleClick);
            syncToServer(2, clickData::writeToPacket);
            phantomClick(clickData);
            dataSetter.accept(getMcSlot().getStack());
            return ClickResult.ACCEPT;
        }
        return ClickResult.REJECT;
    }

    @Override
    public boolean onMouseScroll(int direction) {
        if (interactionDisabled) return false;
        if (isPhantom()) {
            if (Interactable.hasShiftDown()) {
                direction *= 8;
            }
            final int finalDirection = direction;
            syncToServer(3, buffer -> buffer.writeVarIntToBuffer(finalDirection));
            phantomScroll(finalDirection);
            dataSetter.accept(getMcSlot().getStack());
            return true;
        }
        return false;
    }

    @Override
    public boolean handleDragAndDrop(ItemStack draggedStack, int button) {
        if (interactionDisabled) return false;
        if (!isPhantom()) return false;
        ClickData clickData = ClickData.create(button, false);
        syncToServer(5, buffer -> {
            clickData.writeToPacket(buffer);
            NetworkUtils.writeItemStack(buffer, draggedStack);
        });
        phantomClick(clickData, draggedStack);
        dataSetter.accept(getMcSlot().getStack());
        draggedStack.stackSize = 0;
        return true;
    }
}
