package gregtech.common.gui.modularui.hatch;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.modularui2.widget.GhostMoldSlotWidget;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;

/**
 * MUI2 GUI for Solidifier hatch, including phantom mold slot.
 */
public class MTEHatchSolidifierGui {

    private final MTEHatchSolidifier hatch;

    public MTEHatchSolidifierGui(MTEHatchSolidifier hatch) {
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
        int ghostX = 124;
        int ghostY = 34;

        syncManager.registerSlotGroup("item_inv", 1);
        syncManager.registerSlotGroup("mold_slot", 1);
        syncManager.registerSlotGroup("circuit_slot", 1);

        IntSyncValue moldSync = new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(MTEHatchSolidifier.moldSlot);
            return current != null ? hatch.findMatchingMoldIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < MTEHatchSolidifier.solidifierMolds.length) {
                hatch.setMold(MTEHatchSolidifier.solidifierMolds[index]);
            } else {
                hatch.setMold(null);
            }
        });

        syncManager.syncValue("moldIndex", moldSync);

        // Inventory grid
        // Phantom mold slot
        // Circuit slot
        // Decorative widgets (from MUI1)

        return GTGuis.mteTemplatePanelBuilder(hatch, data, syncManager, uiSettings)
            .setWidth(baseWidth)
            .setHeight(guiHeight)
            .build()

            // Inventory grid
            .child(gridTemplate(cols, rows, index -> {
                int actualIndex = index;
                if (actualIndex >= MTEHatchSolidifier.moldSlot) actualIndex++;
                if (actualIndex >= MTEHatchSolidifier.circuitSlot) actualIndex++;
                if (actualIndex >= hatch.getSizeInventory()) return null;
                return new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, actualIndex).slotGroup("item_inv"));
            }).pos(gridX, gridY))

            // Phantom mold slot
            .child(
                new GhostMoldSlotWidget(hatch, syncManager)
                    .slot(new ModularSlot(hatch.inventoryHandler, MTEHatchSolidifier.moldSlot).slotGroup("mold_slot"))
                    .pos(ghostX, ghostY))

            // Circuit slot
            .child(
                new ItemSlot()
                    .slot(new ModularSlot(hatch.inventoryHandler, hatch.getCircuitSlot()).slotGroup("circuit_slot"))
                    .pos(hatch.getCircuitSlotX(), hatch.getCircuitSlotY()))

            // Decorative widgets (from MUI1)
            .child(
                new DrawableWidget().drawable(GTGuiTextures.PICTURE_SCREEN_BLACK)
                    .pos(7, 16)
                    .size(71, 45))
            .child(
                new DrawableWidget().drawable(GTGuiTextures.PICTURE_GAUGE)
                    .pos(79, 34)
                    .size(18, 18))
            .child(
                new TextWidget().string("Liquid Amount:")
                    .pos(10, 20))
            .child(
                new TextWidget().stringSupplier(() -> String.valueOf(hatch.mFluid != null ? hatch.mFluid.amount : 0))
                    .pos(10, 30));
    }

    public static Grid gridTemplate(int cols, int rows, java.util.function.IntFunction<IWidget> widgetCreator) {
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
