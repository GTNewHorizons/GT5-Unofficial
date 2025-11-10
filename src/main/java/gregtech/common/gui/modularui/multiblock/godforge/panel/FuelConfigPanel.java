package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import gregtech.common.gui.modularui.multiblock.godforge.data.Fuels;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.data.Syncers;
import gregtech.common.gui.modularui.multiblock.godforge.util.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.util.SyncHypervisor;
import gregtech.common.gui.modularui.widget.ResizableItemDisplayWidget;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;

public class FuelConfigPanel {

    private static final int WIDTH = 78;
    private static final int HEIGHT = 130;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.FUEL_CONFIG);
        ForgeOfGodsData data = hypervisor.getData();

        registerSyncHandlers(hypervisor);

        panel.relative(hypervisor.getModularPanel(Panels.MAIN))
            .size(WIDTH, HEIGHT)
            .topRel(0)
            .leftRelOffset(1, -3);

        Flow column = new Column().size(WIDTH, HEIGHT);

        // Header
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.fuelconsumption")
                .alignment(Alignment.CENTER)
                .asWidget()
                .width(WIDTH - 4)
                .alignX(0.5f)
                .marginTop(5));

        // Textbox
        TextFieldWidget textBox = new TextFieldWidget().setFormatAsInteger(true)
            .setNumbers(() -> 1, () -> GodforgeMath.calculateMaxFuelFactor(data))
            .setTextAlignment(Alignment.CENTER)
            .value(Syncers.FUEL_FACTOR.create(hypervisor))
            .setTooltipOverride(true)
            .setScrollValues(1, 4, 64)
            .size(70, 18)
            .marginLeft(4)
            .marginTop(3);
        column.child(textBox);

        // todo actually these are probably not needed
        /*
         * data.onUpgradeSyncClient(
         * syncManager,
         * MTEForgeOfGodsGui.SYNC_UPGRADE_CFCE,
         * CFCE,
         * () -> textBox.setNumbers(1, GodforgeMath.calculateMaxFuelFactor(data)));
         * data.onUpgradeSyncClient(
         * syncManager,
         * MTEForgeOfGodsGui.SYNC_UPGRADE_GEM,
         * GEM,
         * () -> textBox.setNumbers(1, GodforgeMath.calculateMaxFuelFactor(data)));
         * data.onUpgradeSyncClient(
         * syncManager,
         * MTEForgeOfGodsGui.SYNC_UPGRADE_TSE,
         * TSE,
         * () -> textBox.setNumbers(1, GodforgeMath.calculateMaxFuelFactor(data)));
         */

        // Info widget
        panel.child(
            GTGuiTextures.PICTURE_INFO.asWidget()
                .size(10)
                .pos(WIDTH - 10 - 4, 24) // i KNOW... its not really relative to anything tho...
                .tooltip(t -> {
                    t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.0"));
                    t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.1"));
                    t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.2"));
                    t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.3"));
                    t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.4"));
                    t.addLine(translateToLocal("gt.blockmachines.multimachine.FOG.fuelinfo.5"));
                })
                .tooltipShowUpTimer(TOOLTIP_DELAY));

        // Fuel type header
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.fueltype")
                .alignment(Alignment.CENTER)
                .asWidget()
                .width(WIDTH - 4)
                .alignX(0.5f)
                .marginTop(5));

        // Fuel selector
        EnumSyncValue<Fuels> selectionSyncer = Syncers.SELECTED_FUEL.lookupFrom(Panels.FUEL_CONFIG, hypervisor);
        Flow fuelRow = new Row().coverChildren()
            .alignX(0.5f)
            .marginTop(5)
            .child(createFuelSelection(selectionSyncer, Fuels.RESIDUE).marginRight(7))
            .child(createFuelSelection(selectionSyncer, Fuels.STELLAR).marginRight(7))
            .child(createFuelSelection(selectionSyncer, Fuels.MHDCSM));
        column.child(fuelRow);

        // Fuel usage text
        LongSyncValue fuelUsageSyncer = Syncers.FUEL_CONSUMPTION.lookupFrom(Panels.FUEL_CONFIG, hypervisor);
        EnumSyncValue<Formatters> formatSyncer = Syncers.FORMATTER.lookupFrom(Panels.FUEL_CONFIG, hypervisor);
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.fuelusage")
                .alignment(Alignment.CENTER)
                .asWidget()
                .width(WIDTH - 4)
                .alignX(0.5f)
                .marginTop(5));
        column.child(IKey.dynamic(() -> {
            Formatters formatter = formatSyncer.getValue();
            return formatter.format(fuelUsageSyncer.getLongValue()) + " L/5s";
        })
            .alignment(Alignment.CENTER)
            .asWidget()
            .width(WIDTH - 4)
            .alignX(0.5f)
            .marginTop(3));

        return panel.child(column);
    }

    private static void registerSyncHandlers(SyncHypervisor hypervisor) {
        Syncers.SELECTED_FUEL.registerFor(Panels.FUEL_CONFIG, hypervisor);
        Syncers.FUEL_CONSUMPTION.registerFor(Panels.FUEL_CONFIG, hypervisor);
        Syncers.FORMATTER.registerFor(Panels.FUEL_CONFIG, hypervisor);
    }

    private static ParentWidget<?> createFuelSelection(EnumSyncValue<Fuels> syncer, Fuels option) {
        return new ParentWidget<>().coverChildren()
            .child(
                new ResizableItemDisplayWidget().background(IDrawable.EMPTY)
                    .displayAmount(false)
                    .item(GTUtility.getFluidDisplayStack(option.getFluid(), false, false))
                    .size(18))
            .child(
                // todo click sound
                new SelectButton().value(LinkedBoolValue.of(syncer, option))
                    .background(IDrawable.EMPTY)
                    .overlay(IDrawable.EMPTY)
                    .selectedBackground(GTGuiTextures.SLOT_OUTLINE_GREEN)
                    .disableHoverBackground()
                    .disableHoverOverlay()
                    .size(18)
                    .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                    .tooltip(t -> ForgeOfGodsGuiUtil.addFluidNameInfo(t, option.getFluid()))
                    .tooltipShowUpTimer(TOOLTIP_DELAY));
    }
}
