package gregtech.common.gui.modularui.hatch;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
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
                new Column().marginTop(16)
                    .child(
                        new ItemSlot()
                            .slot(new ModularSlot(hatch.inventoryHandler, hatch.getInputSlot()).slotGroup("item_inv"))
                            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_IN))
                    .child(
                        new ParentWidget<>()
                            .child(new IDrawable.DrawableWidget(GTGuiTextures.PICTURE_GAUGE).size(18, 18))
                            .child(
                                new GhostMoldSlotWidget(hatch, syncManager)
                                    .slot(
                                        new ModularSlot(hatch.inventoryHandler, MTEHatchSolidifier.moldSlot)
                                            .slotGroup("mold_slot"))
                                    .marginLeft(45)))

                    .child(
                        new ItemSlot().slot(
                            new ModularSlot(hatch.inventoryHandler, hatch.getOutputSlot()).accessibility(false, true)
                                .slotGroup("item_inv"))
                            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_OUT)))
            .child(
                new ParentWidget<>().margin(7, 16)
                    .size(71, 45)
                    .background(GTGuiTextures.PICTURE_SCREEN_BLACK)

                    .child(
                        new FluidSlot().syncHandler(hatch.fluidTank)
                            .background(GTGuiTextures.PICTURE_TRANSPARENT)
                            .rightRelOffset(0, 2)
                            .bottomRelOffset(0, 2))

                    .child(
                        new TextWidget<>(IKey.str("Liquid Amount")).margin(3, 3)
                            .color(WHITE))

                    .child(
                        new TextWidget<>(
                            IKey.dynamic(() -> String.valueOf(hatch.mFluid != null ? hatch.mFluid.amount : 0)))
                                .margin(3, 15)
                                .color(WHITE)));
    }
}
