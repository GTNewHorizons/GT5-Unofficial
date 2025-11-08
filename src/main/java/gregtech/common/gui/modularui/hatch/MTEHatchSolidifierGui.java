package gregtech.common.gui.modularui.hatch;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.drawable.DynamicDrawable;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTGuis;
import gregtech.common.modularui2.widget.GhostMoldSlotWidget;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.MTEHatchSolidifier;

/**
 * MUI2 GUI for Solidifier hatch, including phantom mold slot.
 */
public class MTEHatchSolidifierGui {

    private final MTEHatchSolidifier hatch;
    private static final int WHITE = 0xffffff;

    public MTEHatchSolidifierGui(MTEHatchSolidifier hatch) {
        this.hatch = hatch;
    }

    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {

        syncManager.registerSlotGroup("item_inv", 1);
        syncManager.registerSlotGroup("mold_slot", 1);
        syncManager.registerSlotGroup("circuit_slot", 1);

        IntSyncValue moldSyncHandler = new IntSyncValue(() -> {
            ItemStack current = hatch.inventoryHandler.getStackInSlot(MTEHatchSolidifier.moldSlot);
            return current != null ? hatch.findMatchingMoldIndex(current) : -1;
        }, index -> {
            if (index >= 0 && index < MTEHatchSolidifier.solidifierMolds.length) {
                hatch.setMold(MTEHatchSolidifier.solidifierMolds[index]);
            } else {
                hatch.setMold(null);
            }
        });

        syncManager.syncValue("moldIndex", moldSyncHandler);

        return GTGuis.mteTemplatePanelBuilder(hatch, data, syncManager, uiSettings)
            .build()
            .child(
                new GhostMoldSlotWidget(hatch, syncManager)
                    .slot(new ModularSlot(hatch.inventoryHandler, MTEHatchSolidifier.moldSlot).slotGroup("mold_slot"))
                    .pos(124, 34))
            .child(
                new DynamicDrawable(() -> GTGuiTextures.PICTURE_SCREEN_BLACK).asWidget()
                    .pos(7, 16)
                    .size(71, 45))
            .child(
                new DynamicDrawable(() -> GTGuiTextures.PICTURE_GAUGE).asWidget()
                    .pos(79, 34)
                    .size(18, 18))
            .child(
                new FluidSlot().syncHandler(hatch.fluidTank)
                    .pos(58, 41))

            .child(
                new TextWidget<>(IKey.str("Liquid Amount")).marginLeft(10)
                    .marginTop(20)
                    .color(WHITE))
            .child(
                new TextWidget<>(IKey.dynamic(() -> String.valueOf(hatch.mFluid != null ? hatch.mFluid.amount : 0)))
                    .marginLeft(10)
                    .marginTop(30)
                    .color(WHITE))
            .child(
                new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, hatch.getInputSlot()).slotGroup("item_inv"))
                    .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_IN)
                    .pos(79, 16))
            .child(
                new ItemSlot()
                    .slot(
                        new ModularSlot(hatch.inventoryHandler, hatch.getOutputSlot()).accessibility(false, true)
                            .slotGroup("item_inv"))
                    .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_OUT)
                    .pos(79, 52));
    }
}
