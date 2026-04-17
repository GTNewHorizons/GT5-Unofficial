package gregtech.common.gui.modularui.multiblock;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.DoubleValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ItemDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTGuiTextures;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBase;

// TODO redo entirely
public class MTELargeBoilerGui extends MTEMultiBlockBaseGui<MTELargeBoilerBase> {

    public MTELargeBoilerGui(MTELargeBoilerBase multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        ListWidget<IWidget, ?> widget = super.createTerminalTextWidget(syncManager, parent);
        widget.child(mainInfoColumn());

        if (multiblock.getBaseMetaTileEntity()
            .isActive()) {
            widget.child(createSteamOutputRow());
        }
        return widget;
    }

    private Flow mainInfoColumn() {
        Flow column = Flow.column()
            .widthRel(1f)
            .height(120);

        Flow fuelRow = Flow.row()
            .coverChildrenWidth();
        fuelRow.child(createFuelDisplay(true));
        fuelRow.child(new TextWidget(IKey.str(" + ")));
        fuelRow.child(createFuelDisplay(false));
        column.child(fuelRow);

        if (multiblock.comboFuelThisCycle) {
            column.child(new TextWidget(IKey.str("§6+25% Combo Bonus")));
        }

        ProgressWidget burnProgress = new ProgressWidget()
            .value(
                new DoubleValue.Dynamic(
                    () -> multiblock.getMaxProgresstime() > 0
                        ? (double) multiblock.getProgresstime() / multiblock.getMaxProgresstime()
                        : 0.0,
                    null))
            .direction(ProgressWidget.Direction.RIGHT)
            .size(182, 11)
            .texture(GTGuiTextures.PROGRESSBAR_PURIFICATION_UNIT, 182);
        column.child(burnProgress);

        column.child(createWaterTankWidget());

        if (multiblock.cooldownTicksLeft > 0) {
            column.child(createCooldownWidget());
        }

        return column;
    }

    private IWidget createFuelDisplay(boolean isSolid) {
        return new ItemDisplayWidget().size(20)
            .background(IDrawable.EMPTY)
            .tooltipDynamic(tooltip -> {
                if (isSolid) tooltip.addLine(StatCollector.translateToLocal("GT5U.gui.large_boiler.solid_fuel"));
                else tooltip.addLine(StatCollector.translateToLocal("GT5U.gui.large_boiler.liquid_fuel"));
            })
            .item(
                isSolid ? multiblock.getStoredInputs()
                    .stream()
                    .findFirst()
                    .orElse(null) : null);
    }

    private Flow createWaterTankWidget() {
        Flow waterColumn = Flow.column()
            .width(24)
            .height(60);

        ProgressWidget waterBar = new ProgressWidget().value(
            new DoubleValue.Dynamic(() -> (double) multiblock.internalWater / multiblock.getMaxInternalWater(), null))
            .direction(ProgressWidget.Direction.UP)
            .size(18, 52)
            .texture(GTGuiTextures.PROGRESSBAR_PURIFICATION_UNIT, 52);
        waterColumn.child(waterBar);

        TextWidget waterText = new TextWidget(
            IKey.dynamic(() -> multiblock.internalWater / 1000 + "/" + multiblock.getMaxInternalWater() / 1000 + " B"));
        waterColumn.child(waterText);

        return waterColumn;
    }

    private Flow createCooldownWidget() {
        ProgressWidget cooldownBar = new ProgressWidget()
            .value(
                new DoubleValue.Dynamic(
                    () -> (double) multiblock.cooldownTicksLeft / multiblock.getMaxCooldownTicks(),
                    null))
            .direction(ProgressWidget.Direction.RIGHT)
            .size(120, 8)
            .texture(GTGuiTextures.PROGRESSBAR_PURIFICATION_UNIT, 120);

        TextWidget text = new TextWidget(IKey.str("§eCooling down"));

        return Flow.column()
            .child(cooldownBar)
            .child(text);
    }

    private Flow createSteamOutputRow() {
        Flow row = Flow.row()
            .coverChildrenWidth()
            .paddingTop(4);

        TextWidget steamText = new TextWidget(IKey.dynamic(() -> {
            long steam = multiblock.getEUt() * 2L * multiblock.mEfficiency / 10000L;
            String type = multiblock.isSuperheated() ? "SH Steam" : "Steam";
            return "§a" + steam + " L/t " + type;
        }));
        steamText.size(180, 14);

        row.child(steamText);
        return row;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
    }
}
