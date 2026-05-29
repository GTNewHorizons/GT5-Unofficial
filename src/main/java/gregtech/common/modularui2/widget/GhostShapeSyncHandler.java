package gregtech.common.modularui2.widget;

import static gregtech.common.modularui2.factory.SelectItemGuiBuilder.DESELECTED;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;

public class GhostShapeSyncHandler extends PhantomItemSlotSH {

    private final MTEHatchExtrusion hatch;
    private IntSyncValue indexSync;

    @SuppressWarnings("UnstableApiUsage")
    public GhostShapeSyncHandler(ModularSlot slot, MTEHatchExtrusion hatch) {
        super(slot);
        this.hatch = hatch;
        indexSync = new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(hatch.shapeSlot);
            return current != null ? hatch.findMatchingShapeIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < MTEHatchExtrusion.extruderShapes.length) {
                hatch.setShape(MTEHatchExtrusion.extruderShapes[index]);
            } else {
                hatch.setShape(null);
            }
        }).allowC2S();
    }

    /**
     * Registers the index sync value on the given sync manager. Must be called during widget construction,
     * before {@link com.cleanroommc.modularui.value.sync.PanelSyncManager#initialize} runs, to avoid
     * modifying the sync handler map during iteration.
     */
    public void registerIndexSync(PanelSyncManager syncManager, String key) {
        if (indexSync != null) {
            syncManager.syncValue(key, 0, indexSync);
        }
    }

    @Override
    public void init(String key, PanelSyncManager syncHandler) {
        super.init(key, syncHandler);
    }

    @Override
    protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
        if (indexSync == null) return;

        int itemIndex = hatch.findMatchingShapeIndex(cursorStack);
        if (cursorStack != null && itemIndex != -1) {
            setSelectedIndex(itemIndex);
        } else {
            if (mouseData.mouseButton == 0) {
                // increment on left-click
                setSelectedIndex(getNextShapeConfig(1));
            } else if (mouseData.mouseButton == 1 && mouseData.shift) {
                // clear on shift-right-click
                setSelectedIndex(DESELECTED);
            } else if (mouseData.mouseButton == 1) {
                // decrement on right-click
                setSelectedIndex(getNextShapeConfig(-1));
            }
        }
    }

    @Override
    protected void phantomScroll(MouseData mouseData) {
        setSelectedIndex(getNextShapeConfig(mouseData.mouseButton));
    }

    private int getNextShapeConfig(int delta) {
        int newIndex = getSelectedIndex() + delta;
        if (newIndex < -1) newIndex = MTEHatchExtrusion.extruderShapes.length - 1;
        if (newIndex >= MTEHatchExtrusion.extruderShapes.length) newIndex = -1;
        return newIndex;
    }

    public int getSelectedIndex() {
        return indexSync != null ? indexSync.getIntValue() : -1;
    }

    public void setSelectedIndex(int index) {
        if (indexSync != null) indexSync.setIntValue(index);
    }

    public IntSyncValue getIndexSync() {
        return indexSync;
    }
}
