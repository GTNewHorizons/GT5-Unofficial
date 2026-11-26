package gregtech.common.gui.modularui.hatch;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.ByteSyncValue;
import com.cleanroommc.modularui.value.sync.FluidSlotSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.CycleButtonWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import goodgenerator.blocks.tileEntity.AntimatterOutputHatch;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.modularui2.common.CommonWidgets;
import gregtech.common.gui.modularui.hatch.base.MTEHatchBaseGui;
import gregtech.common.modularui2.widget.FluidLockSlotWidget;

public class AntimatterOutputHatchGui extends MTEHatchBaseGui<AntimatterOutputHatch> {

    private static final int FILTER_TEXT_MAX_SCALE_LENGTH = 32;

    public AntimatterOutputHatchGui(AntimatterOutputHatch hatch) {
        super(hatch);
    }

    @Override
    protected ParentWidget<?> createContentSection(ModularPanel panel, PanelSyncManager syncManager) {
        Flow mainRow = Flow.row()
            .coverChildren()
            .childPadding(1);

        mainRow.childIf(supportsScreen(), () -> createScreen(panel, syncManager));
        mainRow.childIf(supportsScreenIOColumn(), () -> createScreenIO(panel, syncManager));
        mainRow.childIf(supportsFilterScreen(), () -> createFilterScreen(panel, syncManager));

        return super.createContentSection(panel, syncManager).child(mainRow);
    }

    @Override
    protected ParentWidget<?> createFilterScreen(ModularPanel panel, PanelSyncManager syncManager) {
        ParentWidget<?> screen = CommonWidgets.createHatchScreen(FILTER_SCREEN_WIDTH, FILTER_SCREEN_HEIGHT);

        Flow textColumn = Flow.column()
            .childPadding(1)
            .crossAxisAlignment(Alignment.CrossAxis.START);

        FluidLockSlotWidget fluidLockSlotWidget = new FluidLockSlotWidget(machine);

        // fluid amount label
        textColumn.child(
            IKey.lang("GT5U.machines.hatch_output.lockfluid.label")
                .asWidget()
                .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        // fluid name
        IKey lockKey = IKey.dynamic(() -> {
            FluidStack fluid = fluidLockSlotWidget.getFluid();
            return fluid == null ? StatCollector.translateToLocal("GT5U.machines.hatch_output.lockfluid.empty")
                : fluid.getLocalizedName();
        });
        // noinspection unchecked,rawtypes
        textColumn.child(new TextWidget(lockKey) {

            @Override
            protected void onTextChanged(String newText) {
                // needed to scale down long fluid names
                scale(Math.min(1, (float) FILTER_TEXT_MAX_SCALE_LENGTH / newText.length()));
                super.onTextChanged(newText);
            }
        }.scale( // needed for initial scaling
            Math.min(
                1,
                (float) FILTER_TEXT_MAX_SCALE_LENGTH / lockKey.getFormatted()
                    .length()))
            .widgetTheme(GTWidgetThemes.DISPLAY_TEXT_WHITE));

        screen.child(textColumn);

        // needed for the mode setter button tooltip
        syncManager.syncValue("lockedFluidSlotSH", new FluidSlotSyncHandler(fluidLockSlotWidget));
        FluidSlotSyncHandler fluidSlotSH = new FluidSlotSyncHandler(fluidLockSlotWidget).phantom(true);

        // fluid lock slot
        screen.child(
            fluidLockSlotWidget.syncHandler(fluidSlotSH)
                .bottomRel(0)
                .rightRel(0)
                .background(GTGuiTextures.SLOT_FLUID_TANK));

        return screen;
    }

    @Override
    protected Flow createBottomLeftCornerFlow(ModularPanel panel, PanelSyncManager syncManager) {
        ByteSyncValue modeSyncer = syncManager.findSyncHandler("mode", ByteSyncValue.class);

        return super.createBottomLeftCornerFlow(panel, syncManager).child(
            new CycleButtonWidget().stateCount(2)
                .value(modeSyncer)
                .overlay(GTGuiTextures.OVERLAY_BUTTON_SCREWDRIVER)
                .tooltipDynamic(t -> {
                    byte mode = modeSyncer.getByteValue();

                    t.addLine(StatCollector.translateToLocal(AntimatterOutputHatch.getLangKeyForModeAntimatter(mode)));
                    t.addLine(
                        EnumChatFormatting.GRAY + StatCollector.translateToLocal("GT5U.gui.text.hatch.output.cycle"));
                })
                .tooltipAutoUpdate(true));
    }

    @Override
    public void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);

        syncManager.syncValue("mode", new ByteSyncValue(machine::getMode, machine::setMode).allowC2S());
    }
}
