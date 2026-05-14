package gregtech.common.gui.modularui.hatch;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.ByteSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
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
            .paddingLeft(3)
            .paddingTop(10)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.childIf(supportsScreen(), this::createScreen);
        mainRow.childIf(supportsIO(), this::createIO);
        mainRow.childIf(supportsFilterScreen(), () -> createFilterScreen(syncManager));

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
            new FluidSlot().syncHandler(new FluidSlotSyncHandler(hatch.getFluidTank()))
                .bottomRel(0)
                .rightRel(0)
                .background(GTGuiTextures.SLOT_FLUID_TANK));

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

    protected ParentWidget<?> createFilterScreen(PanelSyncManager syncManager) {
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
            FluidStack fluid = fluidLockSlotWidget.getFluid();
            return GTUtility
                .translate(fluid == null ? "GT5U.machines.hatch_output.lockfluid.empty" : fluid.getUnlocalizedName());
        })
            .asWidget()
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT));

        screen.child(textColumn);

        // needed for the mode setter button tooltip
        syncManager.syncValue("lockedTank", new FluidSlotSyncHandler(fluidLockSlotWidget));
        FluidSlotSyncHandler lockedTank = new FluidSlotSyncHandler(fluidLockSlotWidget).phantom(true);

        // fluid lock slot
        screen.child(
            fluidLockSlotWidget.syncHandler(lockedTank)
                .bottomRel(0)
                .rightRel(0)
                .background(GTGuiTextures.SLOT_FLUID_TANK));

        return screen;
    }

    @Override
    protected boolean supportsLeftCornerFlow() {
        return true;
    }

    @Override
    protected Flow createLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        ByteSyncValue modeSyncer = new ByteSyncValue(hatch::getMode, hatch::setMode);
        FluidSlotSyncHandler lockedTankSyncer = syncManager.findSyncHandler("lockedTank", FluidSlotSyncHandler.class);

        return super.createLeftCornerFlow(panel, syncManager).paddingBottom(2)
            .child(
                new CycleButtonWidget().stateCount(10)
                    .value(modeSyncer)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
                    .tooltipDynamic(t -> {
                        Object[] args = new Object[1];
                        byte mode = modeSyncer.getByteValue();

                        if (mode == 8 || mode == 9) {
                            FluidStack fluid = lockedTankSyncer.getValue();
                            args[0] = GTUtility.translate(
                                fluid == null ? "GT5U.chat.hatch.output.in_brackets.none" : fluid.getUnlocalizedName());
                        }

                        t.addLine(GTUtility.translate(MTEHatchOutput.getLangKeyForMode(mode), args));
                        t.addLine(EnumChatFormatting.GRAY + GTUtility.translate("GT5U.gui.text.hatch.output.cycle"));
                    })
                    .tooltipAutoUpdate(true));
    }
}
