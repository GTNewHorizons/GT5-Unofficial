package gregtech.common.gui.modularui.multiblock;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.DoubleSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.FluidDisplayWidget;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.cleanroommc.modularui.widgets.layout.Flow;

import gregtech.api.modularui2.GTWidgetThemes;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBase;

public class MTELargeBoilerGui extends MTEMultiBlockBaseGui<MTELargeBoilerBase> {

    private final MTELargeBoilerBase base;
    private static final int DISPLAY_ROW_HEIGHT = 15;
    private static final int DISPLAY_ROW_CHAR_LIMIT = 46;

    public MTELargeBoilerGui(MTELargeBoilerBase base) {
        super(base);
        this.base = base;
    }

    @Override
    protected void registerSyncValues(PanelSyncManager syncManager) {
        super.registerSyncValues(syncManager);
        syncManager.syncValue("superheated", new BooleanSyncValue(base::isSuperheated));
        syncManager.syncValue("fluidBurnTime", new IntSyncValue(base::getFluidBurnTime));
        syncManager.syncValue("solidBurnTime", new IntSyncValue(base::getSolidBurnTime));
        syncManager.syncValue("maxFluidBurnTime", new IntSyncValue(base::getMaxFluidBurnTime));
        syncManager.syncValue("maxSolidBurnTime", new IntSyncValue(base::getMaxSolidBurnTime));
        syncManager.syncValue("burnDecrease", new IntSyncValue(base::getBurnDecrease));
        syncManager.syncValue("currentWaterConsumption", new DoubleSyncValue(base::getCurrentWaterConsumptionPerTick));
        syncManager.syncValue("currentSteamProduction", new DoubleSyncValue(base::getCurrentSteamOutputPerTick));
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        BooleanSyncValue isMachineActive = syncManager.findSyncHandler("machineActive", BooleanSyncValue.class);
        return super.createTerminalTextWidget(syncManager, parent)
            .childIf(isMachineActive.getBoolValue(), () -> createSteamProductionTextWidget(syncManager))
            .childIf(isMachineActive.getBoolValue(), () -> createWaterConsumptionTextWidget(syncManager))
            .childIf(isMachineActive.getBoolValue(), () -> createFluidTimerTextWidget(syncManager))
            .childIf(isMachineActive.getBoolValue(), () -> createSolidTimerTextWidget(syncManager));
    }

    private IWidget createSteamProductionTextWidget(PanelSyncManager syncManager) {
        BooleanSyncValue superheatedSync = syncManager.findSyncHandler("superheated", BooleanSyncValue.class);
        FluidStack fluidStack = superheatedSync.getBoolValue() ? FluidRegistry.getFluidStack("ic2superheatedsteam", 1)
            : FluidRegistry.getFluidStack("steam", 1);
        Flow column = Flow.column()
            .coverChildren(0);
        column.child(
            Flow.row()
                .fullWidth()
                .height(DISPLAY_ROW_HEIGHT)
                .child(createFluidDrawable(fluidStack))
                .child(createHoverableTextForSteam(fluidStack, syncManager)));
        return column;
    }

    private IWidget createWaterConsumptionTextWidget(PanelSyncManager syncManager) {
        FluidStack fluidStack = FluidRegistry.getFluidStack("water", 1);
        Flow column = Flow.column()
            .coverChildren(0);
        column.child(
            Flow.row()
                .fullWidth()
                .height(DISPLAY_ROW_HEIGHT)
                .child(createFluidDrawable(fluidStack))
                .child(createHoverableTextForWater(fluidStack, syncManager)));
        return column;
    }

    private IWidget createFluidTimerTextWidget(PanelSyncManager syncManager) {
        IntSyncValue fluidBurnTimeSync = syncManager.findSyncHandler("fluidBurnTime", IntSyncValue.class);
        IntSyncValue maxFluidBurnTimeSync = syncManager.findSyncHandler("maxFluidBurnTime", IntSyncValue.class);
        IntSyncValue burnDecreaseSync = syncManager.findSyncHandler("burnDecrease", IntSyncValue.class);

        return new TextWidget<>(
            IKey.dynamic(
                () -> translateToLocalFormatted(
                    "GT5U.machines.large_boiler.gui.fluid_burn",
                    String.format("%.2f", fluidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue() / 20D))))
                        .height(DISPLAY_ROW_HEIGHT)
                        .scale(0.75f)
                        .tooltipBuilder(t -> {
                            if (showOutputRates()) {
                                t.addLine(
                                    EnumChatFormatting.AQUA
                                        + translateToLocal("GT5U.machines.large_boiler.gui.consumption_fluid")
                                        + "\n"
                                        + GTUtility.appendRate(
                                            true,
                                            1000L,
                                            false,
                                            (maxFluidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue())));
                            }
                        });
    }

    private IWidget createSolidTimerTextWidget(PanelSyncManager syncManager) {
        IntSyncValue solidBurnTimeSync = syncManager.findSyncHandler("solidBurnTime", IntSyncValue.class);
        IntSyncValue maxSolidBurnTimeSync = syncManager.findSyncHandler("maxSolidBurnTime", IntSyncValue.class);
        IntSyncValue burnDecreaseSync = syncManager.findSyncHandler("burnDecrease", IntSyncValue.class);
        return new TextWidget<>(
            IKey.dynamic(
                () -> translateToLocalFormatted(
                    "GT5U.machines.large_boiler.gui.solid_burn",
                    String.format("%.2f", solidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue() / 20D))))
                        .height(DISPLAY_ROW_HEIGHT)
                        .scale(0.75f)
                        .tooltipBuilder(t -> {
                            if (showOutputRates()) {
                                t.addLine(
                                    EnumChatFormatting.AQUA
                                        + translateToLocal("GT5U.machines.large_boiler.gui.consumption_solid")
                                        + "\n"
                                        + GTUtility.appendRate(
                                            false,
                                            1L,
                                            false,
                                            (maxSolidBurnTimeSync.getIntValue() / burnDecreaseSync.getIntValue())));
                            }
                        });
    }

    private TextWidget<?> createHoverableTextForSteam(FluidStack fluidStack, PanelSyncManager syncManager) {
        String fluidName = fluidStack.getLocalizedName();
        DoubleSyncValue currentSteamProductionSync = syncManager
            .findSyncHandler("currentSteamProduction", DoubleSyncValue.class);
        return new TextWidget<>(
            IKey.dynamic(
                () -> getFluidTextLine(
                    fluidName,
                    currentSteamProductionSync.getValue()
                        .longValue()))).height(DISPLAY_ROW_HEIGHT)
                            .scale(0.75f)
                            .tooltipBuilder(
                                t -> t.addLine(
                                    EnumChatFormatting.AQUA + fluidName
                                        + "\n"
                                        + GTUtility.appendRate(
                                            false,
                                            currentSteamProductionSync.getValue()
                                                .longValue(),
                                            false,
                                            1)))
                            .tooltipAutoUpdate(true);
    }

    private TextWidget<?> createHoverableTextForWater(FluidStack fluidStack, PanelSyncManager syncManager) {
        String fluidName = EnumChatFormatting.AQUA + fluidStack.getLocalizedName() + EnumChatFormatting.RESET;
        DoubleSyncValue currentWaterProductionSync = syncManager
            .findSyncHandler("currentWaterConsumption", DoubleSyncValue.class);
        return new TextWidget<>(
            IKey.dynamic(
                () -> getFluidTextLine(
                    fluidName,
                    currentWaterProductionSync.getValue()
                        .longValue()))).height(DISPLAY_ROW_HEIGHT)
                            .scale(0.75f)
                            .tooltipBuilder(
                                t -> t.addLine(
                                    EnumChatFormatting.AQUA + fluidName
                                        + "\n"
                                        + GTUtility.appendRate(
                                            false,
                                            currentWaterProductionSync.getValue()
                                                .longValue(),
                                            false,
                                            1)))
                            .tooltipAutoUpdate(true);
    }

    private @NotNull String getFluidTextLine(String fluidName, long amount) {
        String shortenedCount = GTUtility.formatShortenedLong(amount);
        String rateShort = GTUtility.appendRate(false, amount, true, 1);
        int amountLen = (translateToLocalFormatted("GT5U.gui.text.fluid_amount_display", "", shortenedCount)
            + rateShort).length();
        return translateToLocalFormatted(
            "GT5U.gui.text.fluid_amount_display",
            GTUtility.truncateText(fluidName, DISPLAY_ROW_CHAR_LIMIT - amountLen),
            shortenedCount) + rateShort;
    }

    private FluidDisplayWidget createFluidDrawable(FluidStack fluidStack) {
        return new FluidDisplayWidget().disableThemeBackground(true)
            .disableHoverThemeBackground(true)
            .widgetTheme(GTWidgetThemes.BACKGROUND_TERMINAL)
            .displayAmount(false)
            .value(fluidStack)
            .size(DISPLAY_ROW_HEIGHT - 1)
            .marginRight(2);
    }
}
