package gregtech.common.gui.modularui.multiblock;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.theme.WidgetThemeKey;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.modularui2.widget.GTProgressWidget;
import gregtech.common.tileentities.machines.multi.MTEBrickedBlastFurnace;

public class MTEBrickedBlastFurnaceGui {

    /**
     * Does not extend {@link gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui} as
     * {@link gregtech.common.tileentities.machines.multi.MTEBrickedBlastFurnace} is a MetaTileEntity,
     * and has a completely custom UI
     */
    MTEBrickedBlastFurnace base;

    public MTEBrickedBlastFurnaceGui(MTEBrickedBlastFurnace base) {
        this.base = base;
    }

    // author: miozune
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.registerSlotGroup("item_inv", 0);
        return GTGuis.mteTemplatePanelBuilder(base, data, syncManager, uiSettings)
            .build()
            .child(
                SlotGroupWidget.builder()
                    .matrix("I", "I", "I")
                    .key('I', index -> {
                        WidgetThemeKey<?> textureTheme = switch (index) {
                            case 0 -> GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT;
                            case 1 -> GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST;
                            case 2 -> GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE;
                            default -> throw new IllegalStateException("Unexpected value: " + index);
                        };
                        return new ItemSlot().slot(new ModularSlot(base.inventoryHandler, index).slotGroup("item_inv"))
                            .widgetTheme(textureTheme);
                    })
                    .build()
                    .pos(33, 15))
            .child(
                SlotGroupWidget.builder()
                    .matrix("III")
                    .key('I', index -> {
                        WidgetThemeKey<?> textureThemeId = switch (index) {
                            case 0 -> GTWidgetThemes.OVERLAY_ITEM_SLOT_INGOT;
                            case 1, 2 -> GTWidgetThemes.OVERLAY_ITEM_SLOT_DUST;
                            default -> throw new IllegalStateException("Unexpected value: " + index);
                        };
                        return new ItemSlot()
                            .slot(
                                new ModularSlot(base.inventoryHandler, index + 3).accessibility(false, true)
                                    .slotGroup("item_inv"))
                            .widgetTheme(textureThemeId);
                    })
                    .build()
                    .pos(85, 24))
            .child(
                new GTProgressWidget().neiTransferRect(base.getRecipeMap())
                    .value(new DoubleSyncValue(() -> (double) base.mProgresstime / base.mMaxProgresstime))
                    .texture(GTGuiTextures.PROGRESSBAR_ARROW_BBF, 20)
                    .pos(58, 24)
                    .size(20, 18));
    }
}
