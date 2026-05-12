package gregtech.common.gui.modularui.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

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

import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.FluidLockSlotWidget;

public class MTEHatchOutputGui extends MTEHatchBaseGui<MTEHatchOutput> {

    public MTEHatchOutputGui(MTEHatchOutput hatch) {
        super(hatch);
    }

    protected boolean supportsScreen() {
        return true;
    }

    protected boolean supportsIO() {
        return true;
    }

    protected boolean supportsFilterScreen() {
        return true;
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1)
            .paddingLeft(4)
            .paddingTop(10)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.childIf(supportsScreen(), this::createScreen);
        mainRow.childIf(supportsIO(), this::createIO);
        mainRow.childIf(supportsFilterScreen(), this::createFilterScreen);

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    protected ParentWidget<?> createScreen() {
        ParentWidget<?> screen = new ParentWidget<>().size(71, 45)
            .padding(3, 2, 3, 2)
            .background(GTGuiTextures.PICTURE_SCREEN_BLACK);

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        // liquid amount label
        textColumn.child(
            IKey.lang("GT5U.machines.basic_tank.liquid_amount")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        // liquid amount
        textColumn.child(
            IKey.dynamic(
                () -> formatNumber(
                    hatch.getFluidTank()
                        .getFluidAmount()))
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        screen.child(textColumn);

        // fluid slot
        screen.child(
            new FluidSlot().syncHandler(new FluidSlotSyncHandler(hatch.getFluidTank()).canFillSlot(false))
                .bottomRel(0)
                .rightRel(0)
                .backgroundOverlay(GTGuiTextures.SLOT_FLUID_TANK));

        return screen;
    }

    protected Flow createIO() {
        Flow ioColumn = Flow.column()
            .coverChildren()
            .childPadding(9);

        // input slot
        ioColumn.child(
            new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, hatch.getInputSlot()).singletonSlotGroup())
                .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_IN_STANDARD));

        // output slot
        ioColumn.child(
            new ItemSlot().slot(new ModularSlot(hatch.inventoryHandler, hatch.getOutputSlot()).canPut(false))
                .backgroundOverlay(GTGuiTextures.OVERLAY_SLOT_OUT_STANDARD));

        return ioColumn;
    }

    protected ParentWidget<?> createFilterScreen() {
        ParentWidget<?> screen = new ParentWidget<>().size(71, 45)
            .padding(3, 2, 3, 2)
            .background(GTGuiTextures.PICTURE_SCREEN_BLACK);

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        FluidLockSlotWidget fluidLockSlotWidget = new FluidLockSlotWidget(hatch);

        // fluid amount label
        textColumn.child(
            IKey.lang("GT5U.machines.hatch_output.lockfluid.label")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        // fluid name
        textColumn.child(IKey.dynamic(() -> {
            if (fluidLockSlotWidget.getFluid() != null) {
                return fluidLockSlotWidget.getFluid()
                    .getLocalizedName();
            }
            return GTUtility.translate("GT5U.machines.hatch_output.lockfluid.empty");
        })
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        screen.child(textColumn);

        // fluid lock slot
        screen.child(
            fluidLockSlotWidget.syncHandler(new FluidSlotSyncHandler(fluidLockSlotWidget).phantom(true))
                .bottomRel(0)
                .rightRel(0)
                .backgroundOverlay(GTGuiTextures.SLOT_FLUID_TANK));

        return screen;
    }
}
