package gregtech.common.gui.modularui.singleblock;

import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.Widget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.modularui2.GTGuis;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.common.modularui2.widget.GTProgressWidget;
import gregtech.common.tileentities.boilers.MTEBoiler;

public class MTEBoilerGui {

    // the base gui for all Steam Boilers of all types
    MTEBoiler base;

    public MTEBoilerGui(MTEBoiler base) {
        this.base = base;
    }

    // author: miozune
    public ModularPanel build(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        syncManager.registerSlotGroup("item_inv", 0);
        FloatSyncValue heat = new FloatSyncValue(() -> (float) base.mTemperature / base.maxProgresstime());
        syncManager.syncValue("heat", heat);
        IWidget waterSlots = Flow.column()
            .coverChildren()
            .child(
                new ItemSlot().slot(
                    new ModularSlot(base.inventoryHandler, 0).slotGroup("item_inv")
                        .filter(item -> base.isValidFluidInputSlotItem(item)))
                    .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_IN))
            .child(
                new Widget<>().widgetTheme(GTWidgetThemes.PICTURE_CANISTER)
                    .size(18))
            .child(
                new ItemSlot().slot(
                    new ModularSlot(base.inventoryHandler, 1).slotGroup("item_inv")
                        .accessibility(false, true))
                    .widgetTheme(GTWidgetThemes.OVERLAY_ITEM_SLOT_OUT));

        IWidget indicators = Flow.row()
            .coverChildren()
            .crossAxisAlignment(Alignment.CrossAxis.CENTER)
            .childPadding(3)
            .child(
                new FluidSlot().syncHandler(
                    new FluidSlotSyncHandler(base.getSteamTank()).canDrainSlot(false)
                        .canFillSlot(false)
                        .controlsAmount(false))
                    .alwaysShowFull(false)
                    .size(10, 54))
            .child(
                new FluidSlot().syncHandler(
                    new FluidSlotSyncHandler(base.getFluidStackTank()).canDrainSlot(false)
                        .canFillSlot(false)
                        .controlsAmount(false))
                    .alwaysShowFull(false)
                    .size(10, 54))
            .child(
                new GTProgressWidget().syncHandler("heat")
                    .tooltipDynamic(
                        (a) -> {
                            a.add(
                                NumberFormatUtil.formatNumber((int) (heat.getFloatValue() * base.maxProgresstime()))
                                    + "°C");
                        })
                    .direction(ProgressWidget.Direction.UP)
                    .widgetTheme(GTWidgetThemes.PROGRESSBAR_BOILER_HEAT)
                    .size(10, 54));

        IWidget fuelSlots = Flow.column()
            .coverChildren()
            .childIf(base.doesAddAshSlot(), () -> base.createAshSlot())
            .child(new GTProgressWidget().value(new DoubleSyncValue(() -> {
                if (base.mProcessingEnergy <= 0 || base.fuelMaxEnergy <= 0) return 0f;
                return Math.max(2f / 14f, (float) base.mProcessingEnergy / base.fuelMaxEnergy);
            }))
                .direction(ProgressWidget.Direction.UP)
                .widgetTheme(GTWidgetThemes.PROGRESSBAR_FUEL)
                .size(14)
                .margin(2))
            .childIf(base.doesAddFuelSlot(), () -> base.createFuelSlot());

        return GTGuis.mteTemplatePanelBuilder(base, data, syncManager, uiSettings)
            .build()
            .child(
                Flow.row()
                    .horizontalCenter()
                    .top(25)
                    .coverChildren()
                    .childPadding(9)
                    .child(waterSlots)
                    .child(indicators)
                    .child(fuelSlots));
    }

}
