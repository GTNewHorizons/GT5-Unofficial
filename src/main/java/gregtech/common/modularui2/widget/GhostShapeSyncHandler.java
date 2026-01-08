package gregtech.common.modularui2.widget;

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

    public GhostShapeSyncHandler(ModularSlot slot, MTEHatchExtrusion hatch) {
        super(slot);
        this.hatch = hatch;
    }

    @Override
    public void init(String key, PanelSyncManager syncHandler) {
        super.init(key, syncHandler);
        indexSync = new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(MTEHatchExtrusion.shapeSlot);
            return current != null ? hatch.findMatchingShapeIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < MTEHatchExtrusion.extruderShapes.length) {
                hatch.setShape(MTEHatchExtrusion.extruderShapes[index]);
            } else {
                hatch.setShape(null);
            }
        });

        syncHandler.syncValue(key, indexSync);
    }

    @Override
    protected void phantomClick(MouseData mouseData, ItemStack cursorStack) {
        if (indexSync == null) return;
        int delta = mouseData.mouseButton == 1 ? -1 : 1;
        int newIndex = getSelectedIndex() + delta;
        if (newIndex < 0) newIndex = MTEHatchExtrusion.extruderShapes.length - 1;
        if (newIndex >= MTEHatchExtrusion.extruderShapes.length) newIndex = 0;
        setSelectedIndex(newIndex);
    }

    @Override
    protected void phantomScroll(MouseData mouseData) {
        phantomClick(mouseData, null);
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
