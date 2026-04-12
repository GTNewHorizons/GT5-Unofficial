package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;

public class MTEBasicTankBaseGui<T extends MTEBasicTank> extends MTETieredMachineBlockBaseGui<T> {

    public MTEBasicTankBaseGui(T machine) {
        super(machine);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {

        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1)
            .paddingLeft(4)
            .paddingTop(10)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.child(createScreen(panel, syncManager));
        mainRow.child(createIO(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    protected Flow createScreen(ModularPanel panel, PanelSyncManager syncManager) {
        Flow screen = Flow.col()
            .size(71, 45)
            .padding(3, 2, 3, 2)
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START)
            .background(GTGuiTextures.PICTURE_SCREEN_BLACK);

        // liquid amount label
        screen.child(
            IKey.lang("GT5U.machines.basic_tank.liquid_amount")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        // liquid amount
        screen.child(
            IKey.dynamic(
                () -> formatNumber(
                    machine.getFluidTank()
                        .getFluidAmount()))
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        // fluid slot
        screen.child(createFluidSlot(panel, syncManager));

        return screen;
    }

    protected FluidSlot createFluidSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new FluidSlot().syncHandler(new FluidSlotSyncHandler(machine.getFluidTank()))
            .align(Alignment.BottomRight)
            .background(IDrawable.EMPTY);
    }

    protected Flow createIO(ModularPanel panel, PanelSyncManager syncManager) {
        Flow ioColumn = Flow.col()
            .coverChildren()
            .childPadding(9);

        ioColumn.child(createInputSlot(panel, syncManager));
        ioColumn.child(createOutputSlot(panel, syncManager));

        return ioColumn;
    }

    protected ItemSlot createInputSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new ItemSlot()
            .slot(new ModularSlot(machine.inventoryHandler, machine.getInputSlot()).singletonSlotGroup())
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_IN_STANDARD);
    }

    protected ItemSlot createOutputSlot(ModularPanel panel, PanelSyncManager syncManager) {
        return new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.getOutputSlot()).canPut(false))
            .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD);
    }
}
