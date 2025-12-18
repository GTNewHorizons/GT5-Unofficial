package gregtech.common.gui.modularui.hatch;

import java.util.function.IntFunction;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuis;
import gregtech.common.modularui2.widget.GhostShapeSlotWidget;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchExtrusion;

public class MTEHatchExtrusionGui {

    private final MTEHatchExtrusion hatch;

    public MTEHatchExtrusionGui(MTEHatchExtrusion hatch) {
        this.hatch = hatch;
    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        int cols = 9;
        int rows = 4 + (hatch.mTier - 5) * 2;

        int baseWidth = 176;
        int baseHeight = 169;
        int extraHeight = (rows - 4) * 18;

        int guiHeight = baseHeight + extraHeight;

        int gridX = 7;
        int gridY = 7;

        int ghostX = 133;
        int ghostY = 64 + (rows - 4) * 18;

        syncManager.registerSlotGroup("item_inv", 1);
        syncManager.registerSlotGroup("shape_slot", 1);
        syncManager.registerSlotGroup("circuit_slot", 1);

        syncManager
            .syncValue("oneStackLimit", new BooleanSyncValue(() -> hatch.oneStackLimit, v -> hatch.oneStackLimit = v));

        IntSyncValue shapeSyncHandler = new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(MTEHatchExtrusion.shapeSlot);
            return current != null ? hatch.findMatchingShapeIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < MTEHatchExtrusion.extruderShapes.length) {
                hatch.setShape(MTEHatchExtrusion.extruderShapes[index]);
            } else {
                hatch.setShape(null);
            }
        });

        return GTGuis.mteTemplatePanelBuilder(hatch, data, syncManager, uiSettings)
            .setWidth(baseWidth)
            .setHeight(guiHeight)
            .build()
            .child(gridTemplate(cols, rows, index -> {
                int actualIndex = index;
                if (actualIndex >= MTEHatchExtrusion.shapeSlot) actualIndex++;
                if (actualIndex >= MTEHatchExtrusion.circuitSlot) actualIndex++;
                if (actualIndex >= hatch.getSizeInventory()) return null;
                return new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, actualIndex).slotGroup("item_inv"));
            }).pos(gridX, gridY))

            .child(
                new GhostShapeSlotWidget(hatch, syncManager)
                    .slot(new ModularSlot(hatch.inventoryHandler, MTEHatchExtrusion.shapeSlot))
                    .pos(ghostX, ghostY));
    }

    // TODO move this method to CommonGuiComponents.java in the next pr
    public static Grid gridTemplate(int cols, int rows, IntFunction<IWidget> widgetCreator) {
        final int baseX = 79;
        final int baseY = 34;
        final int step = 18;

        int posX = baseX - step * (cols - 1) / 2;
        int posY = baseY - step * (rows - 1) / 2;

        int total = cols * rows;
        return new Grid().coverChildren()
            .pos(posX, posY)
            .mapTo(cols, total, i -> {
                if (i >= total) return null;
                return widgetCreator.apply(i);
            });
    }
}
