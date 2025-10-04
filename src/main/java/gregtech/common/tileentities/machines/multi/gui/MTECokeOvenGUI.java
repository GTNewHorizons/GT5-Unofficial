package gregtech.common.tileentities.machines.multi.gui;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.modularui2.widget.GTProgressWidget;

public class MTECokeOvenGUI extends MTEMultiBlockBaseGui {

    public MTECokeOvenGUI(MTEMultiBlockBase base) {
        super(base);
    }

    @Override
    public ModularPanel build(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        registerSyncValues(syncManager);
        syncManager.registerSlotGroup("item_inv", 0);

        return GTGuis.mteTemplatePanelBuilder(base, guiData, syncManager, uiSettings)
            .build()
            .child(
                new Row().alignX(Alignment.CENTER)
                    .alignY(0.25f)
                    .size(72, 18)
                    .child(
                        new ItemSlot().slot(new ModularSlot(base.inventoryHandler, 0))
                            .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_FURNACE)
                            .marginRight(8))
                    .child(
                        new GTProgressWidget().neiTransferRect(base.getRecipeMap())
                            .value(new DoubleSyncValue(() -> (double) base.mProgresstime / base.mMaxProgresstime))
                            .texture(GTGuiTextures.PROGRESSBAR_ARROW_BBF, 20)
                            .size(20, 18)
                            .marginRight(8))
                    .child(
                        new ItemSlot().slot(new ModularSlot(base.inventoryHandler, 1).accessibility(false, true))
                            .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_COAL)));
    }
}
