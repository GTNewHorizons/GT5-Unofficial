package gregtech.common.gui.modularui.multiblock.godforge.panel;

import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.util.MathHelper;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.EnumSyncValue;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.layout.Column;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Row;
import com.cleanroommc.modularui.widgets.textfield.TextFieldWidget;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.godforge.ForgeOfGodsGuiUtil;
import gregtech.common.gui.modularui.multiblock.godforge.data.Formatters;
import gregtech.common.gui.modularui.multiblock.godforge.data.Fuels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.Panels;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncHypervisor;
import gregtech.common.gui.modularui.multiblock.godforge.sync.SyncValues;
import gregtech.common.modularui2.sync.LinkedBoolValue;
import gregtech.common.modularui2.widget.SelectButton;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;
import tectech.thing.metaTileEntity.multi.godforge.util.GodforgeMath;

public class FuelConfigPanel {

    private static final int SIZE_W = 78;
    private static final int SIZE_H = 130;

    public static ModularPanel openPanel(SyncHypervisor hypervisor) {
        ModularPanel panel = hypervisor.getModularPanel(Panels.FUEL_CONFIG);
        ForgeOfGodsData data = hypervisor.getData();

        registerSyncHandlers(hypervisor);

        panel.relative(hypervisor.getModularPanel(Panels.MAIN))
            .size(SIZE_W, SIZE_H)
            .topRel(0)
            .leftRelOffset(1, -3);

        Flow column = new Column().size(SIZE_W, SIZE_H);

        // Header
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.fuelconsumption")
                .alignment(Alignment.CENTER)
                .asWidget()
                .width(SIZE_W - 4)
                .alignX(0.5f)
                .marginTop(5));

        // Textbox
        column.child(
            new TextFieldWidget().setFormatAsInteger(true)
                .setNumbers(raw -> MathHelper.clamp_int(raw, 1, GodforgeMath.calculateMaxFuelFactor(data)))
                .setTextAlignment(Alignment.CENTER)
                .value(SyncValues.FUEL_FACTOR.create(hypervisor))
                .setTooltipOverride(true)
                .setScrollValues(1, 4, 64)
                .size(70, 18)
                .marginLeft(4)
                .marginTop(3));

        // Info widget
        panel.child(
            GTGuiTextures.PICTURE_INFO.asWidget()
                .size(10)
                .pos(SIZE_W - 10 - 4, 24) // i KNOW... its not really relative to anything tho...
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
                .width(SIZE_W - 4)
                .alignX(0.5f)
                .marginTop(5));

        // Fuel selector
        EnumSyncValue<Fuels> selectionSyncer = SyncValues.SELECTED_FUEL.lookupFrom(Panels.FUEL_CONFIG, hypervisor);
        Flow fuelRow = new Row().coverChildren()
            .alignX(0.5f)
            .marginTop(5)
            .childPadding(7)
            .child(createFuelSelection(hypervisor, selectionSyncer, Fuels.RESIDUE))
            .child(createFuelSelection(hypervisor, selectionSyncer, Fuels.STELLAR))
            .child(createFuelSelection(hypervisor, selectionSyncer, Fuels.MHDCSM));
        column.child(fuelRow);

        // Fuel usage text
        column.child(
            IKey.lang("gt.blockmachines.multimachine.FOG.fuelusage")
                .alignment(Alignment.CENTER)
                .asWidget()
                .width(SIZE_W - 4)
                .alignX(0.5f)
                .marginTop(5));
        column.child(IKey.dynamic(() -> {
            Formatters formatter = data.getFormatter();
            return formatter.format(data.getFuelConsumption()) + " L/5s";
        })
            .alignment(Alignment.CENTER)
            .asWidget()
            .width(SIZE_W - 4)
            .alignX(0.5f)
            .marginTop(3));

        return panel.child(column);
    }

    private static void registerSyncHandlers(SyncHypervisor hypervisor) {
        SyncValues.SELECTED_FUEL.registerFor(Panels.FUEL_CONFIG, hypervisor);
        SyncValues.FUEL_CONSUMPTION.registerFor(Panels.FUEL_CONFIG, hypervisor);
    }

    private static ParentWidget<?> createFuelSelection(SyncHypervisor hypervisor, EnumSyncValue<Fuels> syncer,
        Fuels option) {
        return new ParentWidget<>().coverChildrenWidth()
            .size(18)
            .child(
                new FluidDisplayWidget().background(IDrawable.EMPTY)
                    .fluid(option.getFluid())
                    .displayAmount(false)
                    .align(Alignment.CENTER)
                    .size(16))
            .child(
                new SelectButton().value(LinkedBoolValue.of(syncer, option))
                    .background(IDrawable.EMPTY)
                    .overlay(IDrawable.EMPTY)
                    .selectedBackground(GTGuiTextures.SLOT_OUTLINE_GREEN)
                    .disableHoverBackground()
                    .disableHoverOverlay()
                    .size(18)
                    .clickSound(ForgeOfGodsGuiUtil.getButtonSound())
                    .tooltip(t -> {
                        if (hypervisor.isClient()) {
                            t.addFromFluid(option.getFluid());
                        }
                    })
                    .tooltipShowUpTimer(TOOLTIP_DELAY));
    }
}
