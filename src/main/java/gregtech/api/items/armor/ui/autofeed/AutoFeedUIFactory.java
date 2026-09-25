package gregtech.api.items.armor.ui.autofeed;

import static gregtech.api.util.GTModHandler.getModItem;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.drawable.ItemDrawable;
import com.cleanroommc.modularui.drawable.Rectangle;
import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.gui.modularui.GTUITextures;
import ic2.core.IC2;

public class AutoFeedUIFactory {

    public ModularPanel createMainPanel(final GuiData data, final PanelSyncManager syncManager,
        final UISettings settings) {
        ModularPanel panel = ModularPanel.defaultPanel("Auto Feed Slot");
        panel.bindPlayerInventory();
        syncManager.registerSlotGroup("autofeed_slots", 2);
        OversizedSlotItemHandler slotItemHandler = new OversizedSlotItemHandler(
            data.getPlayer(),
            2,
            4096,
            data.getPlayer().inventory.armorInventory[3]);


        ItemDrawable canBackground = new ItemDrawable(getModItem(IC2.MODID, "itemTinCan"));
        ItemDrawable foodCanBackground = new ItemDrawable(getModItem(IC2.MODID, "itemTinCanFilled"));

        panel.child(
            new ItemSlot().slot(
                new ModularSlot(slotItemHandler, 0) {}.slotGroup("autofeed_slots")
                    .ignoreMaxStackSize(true)
                    .filter(stack -> stack.isItemEqual(getModItem(IC2.MODID, "itemTinCanFilled"))))
                .topRel(0.25F, 0, 0.0F)
                .leftRel(0.5f, -18, 0.5f)
                .backgroundOverlay(foodCanBackground));

        panel.child(
            new ItemSlot().slot(
                new ModularSlot(slotItemHandler, 1) {}.slotGroup("autofeed_slots")
                    .ignoreMaxStackSize(true)
                    .filter(stack -> stack.isItemEqual(getModItem(IC2.MODID, "itemTinCan"))))
                .topRel(0.25F, 0, 0.0F)
                .leftRel(0.5f, 18, 0.5f)
                .backgroundOverlay(canBackground));

        return panel;
    }
}
