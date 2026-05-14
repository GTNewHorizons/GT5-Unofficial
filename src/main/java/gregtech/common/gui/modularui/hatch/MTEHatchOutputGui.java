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
            .paddingTop(3)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        mainRow.childIf(supportsScreen(), () -> createScreen(syncManager));
        mainRow.childIf(supportsIO(), this::createIO);
        mainRow.childIf(supportsFilterScreen(), () -> createFilterScreen(syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    protected ParentWidget<?> createScreen(PanelSyncManager syncManager) {
        ByteSyncValue modeSyncer = syncManager.findSyncHandler("mode", ByteSyncValue.class);

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

        FluidSlotSyncHandler mainTank = new FluidSlotSyncHandler(hatch.getFluidTank());
        mainTank.setChangeListener(() -> {
            byte mode = modeSyncer.getValue();
            hatch.lockFluid(mode == 8 || mode == 9);
        });

        // fluid slot
        screen.child(
            new FluidSlot().syncHandler(mainTank)
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
        ByteSyncValue modeSyncer = syncManager.findSyncHandler("mode", ByteSyncValue.class);
        FluidSlotSyncHandler lockedTankSyncer = syncManager.findSyncHandler("lockedTank", FluidSlotSyncHandler.class);

        return super.createLeftCornerFlow(panel, syncManager).paddingBottom(2)
            .child(
                new CycleButtonWidget().stateCount(10)
                    .value(modeSyncer)
                    .overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
                    .tooltipDynamic(t -> {
                        String[] args = new String[2];
                        byte mode = modeSyncer.getByteValue();
                        FluidStack fluid = lockedTankSyncer.getValue();

                        if (mode == 8 || mode == 9) {
                            if (fluid == null) {
                                args[0] = GTUtility.translate("GT5U.gui.text.hatch.output.filter.none.0");
                                args[1] = GTUtility.translate("GT5U.gui.text.hatch.output.filter.none.1");
                            } else args[0] = GTUtility.translate(fluid.getUnlocalizedName());
                        }

                        t.addLine(GTUtility.translate(MTEHatchOutput.getLangKeyForMode(mode), args[0]));
                        if (args[1] != null) t.addLine(args[1]);
                        t.addLine(EnumChatFormatting.GRAY + GTUtility.translate("GT5U.gui.text.hatch.output.cycle"));
                    })
                    .tooltipAutoUpdate(true));
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("mode", new ByteSyncValue(hatch::getMode, hatch::setMode));
    }
}
