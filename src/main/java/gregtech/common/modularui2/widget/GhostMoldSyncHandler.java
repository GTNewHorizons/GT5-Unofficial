package gregtech.common.modularui2.widget;

import static gregtech.common.modularui2.factory.SelectItemGuiBuilder.DESELECTED;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.utils.MouseData;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.PhantomItemSlotSH;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;

public class GhostMoldSyncHandler extends PhantomItemSlotSH {

    private final MTEHatchSolidifier hatch;
    private IntSyncValue indexSync;

    @SuppressWarnings("UnstableApiUsage")
    public GhostMoldSyncHandler(ModularSlot slot, MTEHatchSolidifier hatch) {
        super(slot);
        this.hatch = hatch;
        indexSync = new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(MTEHatchSolidifier.moldSlot);
            return current != null ? hatch.findMatchingMoldIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < MTEHatchSolidifier.solidifierMolds.length) {
                hatch.setMold(MTEHatchSolidifier.solidifierMolds[index]);
            } else {
                hatch.setMold(null);
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
    protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
        if (indexSync == null) return;

        int itemIndex = hatch.findMatchingMoldIndex(cursorStack);
        if (cursorStack != null && itemIndex != -1) {
            setSelectedIndex(itemIndex);
        } else {
            if (mouseData.mouseButton == 0) {
                // increment on left-click
                setSelectedIndex(getNextMoldConfig(1));
            } else if (mouseData.mouseButton == 1 && mouseData.shift) {
                // clear on shift-right-click
                setSelectedIndex(DESELECTED);
            } else if (mouseData.mouseButton == 1) {
                // decrement on right-click
                setSelectedIndex(getNextMoldConfig(-1));
            }
        }
    }

    @Override
    protected void phantomScroll(MouseData mouseData) {
        setSelectedIndex(getNextMoldConfig(mouseData.mouseButton));
    }

    private int getNextMoldConfig(int delta) {
        int newIndex = getSelectedIndex() + delta;
        if (newIndex < -1) newIndex = MTEHatchSolidifier.solidifierMolds.length - 1;
        if (newIndex >= MTEHatchSolidifier.solidifierMolds.length) newIndex = -1;
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
