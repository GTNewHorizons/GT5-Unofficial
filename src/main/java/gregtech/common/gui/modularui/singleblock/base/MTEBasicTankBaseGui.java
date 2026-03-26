package gregtech.common.gui.modularui.singleblock.base;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.slot.FluidSlot;
import com.cleanroommc.modularui.widgets.slot.ItemSlot;
import com.cleanroommc.modularui.widgets.slot.ModularSlot;

import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.modularui2.GTGuiTextures;

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
                .color(Color.WHITE.main));

        // liquid amount
        screen.child(
            IKey.dynamic(
                () -> formatNumber(
                    machine.getFluidTank()
                        .getFluidAmount()))
                .asWidget()
                .color(Color.WHITE.main));

        // fluid slot
        screen.child(
            new FluidSlot().syncHandler(new FluidSlotSyncHandler(machine.getFluidTank()))
                .align(Alignment.BottomRight)
                .background(IDrawable.EMPTY));

        return screen;
    }

    protected Flow createIO(ModularPanel panel, PanelSyncManager syncManager) {
        Flow ioColumn = Flow.col()
            .coverChildren()
            .childPadding(1);

        // input slot
        ioColumn.child(
            new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.getInputSlot()).singletonSlotGroup())
                .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_IN_STANDARD)
                .marginBottom(supportsGauge() ? 0 : 8));

        // gauge icon
        ioColumn.childIf(
            supportsGauge(),
            () -> GTGuiTextures.PICTURE_GAUGE.asWidget()
                .size(18));

        // output slot
        ioColumn.child(
            new ItemSlot().slot(new ModularSlot(machine.inventoryHandler, machine.getOutputSlot()).canPut(false))
                .background(GTGuiTextures.SLOT_ITEM_STANDARD, GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD));

        return ioColumn;
    }

    protected boolean supportsGauge() {
        return true;
    }
}
